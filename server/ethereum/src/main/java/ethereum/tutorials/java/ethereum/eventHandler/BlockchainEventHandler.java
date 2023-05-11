package ethereum.tutorials.java.ethereum.eventHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

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
import ethereum.tutorials.java.ethereum.repository.SqlCrowdfundingRepo;
import ethereum.tutorials.java.ethereum.services.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.services.ethereum.LoadContractService;
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
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryAddress;

    public Disposable contributeSub$;
    public Disposable createNewProjectSub$;
    public Disposable refundFromProjectSub$;
    public Disposable createRequestForProjectSub$;
    public Disposable receiveContributionFromProjectSub$;
    public Disposable faucetDistributedSub$;
    public Disposable voteRequestProjectSub$;

    public void createNewProject(CrowdfundingFactory loadedContract, String blockHash, String description) {
        System.out.println("getting event " + blockHash);
        createNewProjectSub$ = CrowdfundingEvents
                .createNewProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("getting event " + event._title);
                    Token tokenInstance = lcSvc.loadTokenContract(event._tokenUsed);
                    String tokenName = tokenInstance.name().send();
                    String tokenSymbol = tokenInstance.symbol().send();
                    System.out.println("token name >>> " + tokenName);
                    System.out.println("token symbol >>> " + tokenSymbol);
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
                    System.out.println("event >>>>> " + event._contributor);
                    sqlRepo.insertContributor(event._contributor);
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
                    System.out.println("event refund from project, updating contribution");
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
                    System.out.println("event inserting request");
                    sqlRepo.insertProjectRequest(
                            event.requestNum.intValue(),
                            projectAddress,
                            event._title,
                            description,
                            event._recipient,
                            event._amount.intValue(),
                            0,
                            false);
                });
    }

    public void voteRequestForProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {
        voteRequestProjectSub$ = CrowdfundingEvents
                .voteRequestForProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("event inserting vote");
                    // TODO: sql query to get value of votes of a request
                    sqlRepo.insertVote(
                            event._requestNo.intValue(),
                            event._voter,
                            event._valueOfVote.intValue());
                });
    }

    public void receiveContributionProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {
        receiveContributionFromProjectSub$ = CrowdfundingEvents
                .receiveContributionFromProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("event update request after receiving contribution");
                    // received contribution so request completes
                    sqlRepo.updateRequest(
                            event._requestNo.intValue(),
                            true);
                });
    }

    // may not need to be used. can call view function to get balance of the faucet
    public void faucetDistribution(DevFaucet loadedContract, String blockHash) {
        faucetDistributedSub$ = FaucetEvents.distributionEvent(loadedContract, blockHash, blockHash)
                .subscribe((event) -> {
                    System.out.println("event >>>>> " + event.mintedToAddress);
                    System.out.println("event >>>>> " + event.amount);

                });
    }
}
