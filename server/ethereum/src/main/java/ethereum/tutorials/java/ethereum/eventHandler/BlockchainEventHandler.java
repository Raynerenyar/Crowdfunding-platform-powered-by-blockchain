package ethereum.tutorials.java.ethereum.eventHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.utils.Numeric;
import org.web3j.tx.Contract;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.DevFaucet;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Token;
import ethereum.tutorials.java.ethereum.models.Request;
import ethereum.tutorials.java.ethereum.repository.SqlCrowdfundingRepo;
import ethereum.tutorials.java.ethereum.services.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.services.ethereum.LoadContractService;
import ethereum.tutorials.java.ethereum.services.repository.SqlRepoService;
import ethereum.tutorials.java.ethereum.util.eventFlowable.CrowdfundingEvents;
import ethereum.tutorials.java.ethereum.util.eventFlowable.FaucetEvents;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

@Component
public class BlockchainEventHandler {

    @Autowired
    private Web3j web3;
    @Autowired
    private LoadContractService lcSvc;
    @Autowired
    private SqlCrowdfundingRepo sqlRepo;
    @Autowired
    private SqlRepoService sqlRepoSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryAddress;
    @Value("${faucet.contract.address}")
    private String faucetAddress;

    private static final Logger logger = LoggerFactory.getLogger(BlockchainEventHandler.class);

    public Disposable contributeSub$;
    public Disposable createNewProjectSub$;
    public Disposable refundFromProjectSub$;
    public Disposable createRequestForProjectSub$;
    public Disposable receiveContributionFromProjectSub$;
    public Disposable faucetDistributedSub$;
    public Disposable voteRequestProjectSub$;

    public void createNewProject(CrowdfundingFactory loadedContract, String blockHash, String description) {
        createNewProjectSub$ = CrowdfundingEvents
                .createNewProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: create new project, blockhash >> {}, project title >> {}", blockHash,
                            event._title);
                    Token tokenInstance = lcSvc.loadTokenContract(event._tokenUsed);
                    String tokenName = tokenInstance.name().send();
                    String tokenSymbol = tokenInstance.symbol().send();
                    sqlRepo.insertProject(
                            event._projectAddress,
                            event._projectCreator,
                            event._title,
                            description,
                            event._goal.intValue(),
                            new Timestamp(event._deadline.longValue()),
                            0,
                            false,
                            false,
                            0,
                            event._tokenUsed,
                            event.tokenName,
                            event.tokenSymbol,
                            new Timestamp(System.currentTimeMillis()));
                });

    }

    public void contributeToProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {
        contributeSub$ = CrowdfundingEvents
                .contributeToProject(loadedContract, projectAddress, blockHash)
                .subscribe(event -> {
                    logger.info("event: contribute to project, contributor address >> {}", event._contributor);
                    int count = sqlRepo.countContributor(event._contributor);
                    if (count < 1) {
                        sqlRepo.insertContributor(event._contributor);
                    }
                    sqlRepo.insertContribution(
                            event._contributor,
                            event._amount.intValue(),
                            projectAddress,
                            false);
                });
    }

    public void refundFromProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {
        refundFromProjectSub$ = CrowdfundingEvents
                .getRefundFromProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: getting refund");
                    sqlRepo.updateContribution(
                            false,
                            event._contributor,
                            projectAddress,
                            event._RefundAmount.intValue());
                });
    }

    public void createRequestForProject(Crowdfunding loadedContract, String blockHash, String description,
            String projectAddress) {
        createRequestForProjectSub$ = CrowdfundingEvents
                .createRequestForProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: inserting request");
                    sqlRepo.insertProjectRequest(
                            event.requestNum.intValue(),
                            projectAddress,
                            event._title,
                            description,
                            event._recipient,
                            event._amount.intValue(),
                            false);
                });
    }

    public void voteRequestForProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {
        voteRequestProjectSub$ = CrowdfundingEvents
                .voteRequestForProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: inserting vote");
                    Optional<Integer> requestId = sqlRepoSvc.getRequestId(projectAddress, event._requestNo.intValue());
                    sqlRepo.insertVote(
                            requestId.get(),
                            event._voter,
                            event._valueOfVote.intValue());
                });
    }

    public void receiveContributionProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {
        receiveContributionFromProjectSub$ = CrowdfundingEvents
                .receiveContributionFromProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: receive contribution");
                    // received contribution so request completes
                    sqlRepo.updateRequest(
                            event._requestNo.intValue(),
                            true);
                });
    }

    // may not need to be used. can call view function to get balance of the faucet
    public void faucetDistribution(DevFaucet loadedContract, String blockHash) {
        faucetDistributedSub$ = FaucetEvents.distributionEvent(loadedContract, faucetAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: faucet mint, address minted to >> {}, amount minted >> {}",
                            event.mintedToAddress, event.amount);
                });
    }
}
