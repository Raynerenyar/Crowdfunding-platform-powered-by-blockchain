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
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.TwlvFaucet;
import ethereum.tutorials.java.ethereum.repository.CrowdfundingRepository;
import ethereum.tutorials.java.ethereum.services.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.services.ethereum.LoadContractService;
import ethereum.tutorials.java.ethereum.util.eventFlowable.FactoryEvents;
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
    private CrowdfundingRepository sqlRepo;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryAddress;

    public Disposable contributeSub$;
    public Disposable createNewProjectSub$;
    public Disposable refundFromProjectSub$;
    public Disposable createRequestForProjectSub$;
    public Disposable receiveContributionFromProjectSub$;
    public Disposable faucetDistributedSub$;
    public Disposable voteRequestProjectSub$;

    public void contributeToProject(CrowdfundingFactory loadedContract, String blockHash) {
        contributeSub$ = FactoryEvents
                .contributeToProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe(event -> {
                    System.out.println("event >>>>> " + event._contributor);
                    sqlRepo.insertContributor(event._contributor);
                    sqlRepo.insertContribution(
                            event._contributor, event._amount.intValue(), event._projectAddress,
                            false);
                });
    }

    public void createNewProject(CrowdfundingFactory loadedContract, String blockHash, String description) {
        System.out.println("getting event " + blockHash);
        createNewProjectSub$ = FactoryEvents
                .createNewProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("getting event " + event._title);
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
                            new Timestamp(System.currentTimeMillis()));
                });

    }

    public void refundFromProject(CrowdfundingFactory loadedContract, String blockHash) {
        refundFromProjectSub$ = FactoryEvents
                .getRefundFromProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("event refund from project, updating contribution");
                    sqlRepo.updateContribution(
                            false,
                            event._contributor,
                            event._projectAddress);
                });
    }

    public void createRequestForProject(CrowdfundingFactory loadedContract, String blockHash, String description) {
        createRequestForProjectSub$ = FactoryEvents
                .createRequestForProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("event inserting request");
                    sqlRepo.insertProjectRequest(
                            event._projectAddress,
                            event._title,
                            description,
                            event._recipient,
                            event._amount.intValue(),
                            0,
                            false,
                            0);
                });
    }

    public void voteRequestForProject(CrowdfundingFactory loadedContract, String blockHash) {
        voteRequestProjectSub$ = FactoryEvents
                .voteRequestForProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("event inserting vote");
                    sqlRepo.insertVote(
                            event._requestNo.intValue(),
                            event._voter,
                            event._valueOfVote.intValue());
                });
    }

    public void receiveContributionProject(CrowdfundingFactory loadedContract, String blockHash) {
        receiveContributionFromProjectSub$ = FactoryEvents
                .receiveContributionFromProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    System.out.println("event update request after receiving contribution");
                    sqlRepo.updateRequest(
                            event._requestNo.intValue(),
                            false);
                });
    }

    public void faucetDistribution(TwlvFaucet loadedContract, String blockHash) {
        faucetDistributedSub$ = FaucetEvents.distributionEvent(loadedContract, blockHash, blockHash)
                .subscribe((event) -> {
                    System.out.println("event >>>>> " + event.mintedToAddress);
                    System.out.println("event >>>>> " + event.amount);

                });
    }
}
