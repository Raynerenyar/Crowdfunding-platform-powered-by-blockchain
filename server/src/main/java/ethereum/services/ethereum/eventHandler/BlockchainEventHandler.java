package ethereum.services.ethereum.eventHandler;

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
import org.springframework.beans.factory.annotation.Qualifier;
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

import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.javaethereum.wrapper.DevFaucet;
import ethereum.javaethereum.wrapper.Token;
import ethereum.models.sql.crowdfunding.Request;
import ethereum.repository.sql.crowdfunding.SqlCrowdfundingRepo;
import ethereum.repository.sql.crowdfunding.SqlRepoInferface;
import ethereum.services.ethereum.BlockchainService;
import ethereum.services.ethereum.EtherscanService;
import ethereum.services.ethereum.LoadContractService;
import ethereum.services.ethereum.smartcontract.TokenFunctionsService;
import ethereum.services.repository.SqlRepoService;
import ethereum.util.eventFlowable.CrowdfundingEvents;
import ethereum.util.eventFlowable.FaucetEvents;
import ethereum.util.misc.Util;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

@Service
public class BlockchainEventHandler {

    @Autowired
    private Web3j web3;
    @Autowired
    private LoadContractService lcSvc;
    @Autowired
    private SqlCrowdfundingRepo sqlRepo;
    @Autowired
    private SqlRepoInferface sqlRepoInter;
    @Autowired
    private EtherscanService etherscanSvc;
    @Autowired
    private TokenFunctionsService tokenFuncSvc;

    @Autowired
    private SqlRepoService sqlRepoSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryAddress;
    @Value("${faucet.contract.address}")
    private String faucetAddress;
    @Value("${crowdfunding.project.source.contract.address}")
    private String sourceCrowdfundingAddress;

    private static final Logger logger = LoggerFactory.getLogger(BlockchainEventHandler.class);

    public Disposable contributeSub$;
    public Disposable createNewProjectSub$;
    public Disposable refundFromProjectSub$;
    public Disposable createRequestForProjectSub$;
    public Disposable receiveContributionFromProjectSub$;
    public Disposable faucetDistributedSub$;
    public Disposable voteRequestProjectSub$;

    public void createNewProject(
            CrowdfundingFactory loadedContract,
            String blockHash,
            String description,
            String imageUrl) {

        createNewProjectSub$ = CrowdfundingEvents
                .createNewProject(loadedContract, crowdfundingFactoryAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: create new project, blockhash >> {}, project title >> {}",
                            blockHash,
                            event._title);

                    // sequence of params is important. must be same as the one used in the constructor of Crowdfunding contract
                    String[] params = {
                            event._goal.toString(),
                            event._deadline.toString(),
                            event._tokenUsed,
                            event._title };

                    // verify contract on etherscan sepolia
                    etherscanSvc.verifyContract(
                            sourceCrowdfundingAddress,
                            event._projectAddress,
                            "Crowdfunding",
                            params);

                    /* at this point the cliet side and the smart contract has verified that token address given by
                     * the user is a token therefore it is valid to store address into db 
                     */
                    int count = sqlRepoInter.doesTokenExist(event._tokenUsed.toLowerCase());
                    if (count == 0)
                        sqlRepoInter.insertToken(event._tokenUsed.toLowerCase(),
                                event.tokenSymbol, event.tokenName);
                    int tokenId = sqlRepoInter.getTokenId(event._tokenUsed);
                    int decimals = tokenFuncSvc.getTokenDecimals(event._tokenUsed);
                    long goal = Util.revertFromBaseUnit(event._goal, decimals);
                    sqlRepoInter.insertProject(
                            event._projectAddress,
                            event._projectCreator,
                            event._title,
                            description,
                            imageUrl,
                            goal,
                            new Timestamp(event._deadline.longValue()),
                            false,
                            false,
                            event._tokenUsed,
                            tokenId,
                            new Timestamp(System.currentTimeMillis()));
                });
    }

    public void contribute(Crowdfunding loadedContract, String blockHash, String projectAddress) {

        contributeSub$ = CrowdfundingEvents
                .contributeToProject(loadedContract, projectAddress, blockHash)
                .subscribe(event -> {

                    logger.info("event: contribute to project, contributor address >> {}",
                            event._contributor);

                    // if contributor does not exist, add contributor to DB
                    int count = sqlRepoInter.countContributor(event._contributor);
                    if (count < 1) {
                        sqlRepoInter.insertContributor(event._contributor);
                    }

                    String tokenAddress = loadedContract.tokenAddress().sendAsync().get();
                    int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
                    long amount = Util.revertFromBaseUnit(event._amount, decimals);
                    sqlRepoInter.insertContribution(
                            event._contributor,
                            amount,
                            projectAddress,
                            false);
                });
    }

    public void getRefund(Crowdfunding loadedContract, String blockHash, String projectAddress) {

        refundFromProjectSub$ = CrowdfundingEvents
                .getRefundFromProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: getting refund");
                    sqlRepoInter.updateProjectExpired(true, projectAddress);
                    sqlRepoInter.updateContribution(
                            true,
                            event._contributor,
                            projectAddress);
                });
    }

    public void createRequest(
            Crowdfunding loadedContract,
            String blockHash,
            String description,
            String projectAddress) {

        createRequestForProjectSub$ = CrowdfundingEvents
                .createRequestForProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    String tokenAddress = loadedContract.tokenAddress().sendAsync().get();
                    int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
                    long amountLong = Util.revertFromBaseUnit(event._amount, decimals);
                    logger.info("event: inserting request");
                    sqlRepoInter.insertProjectRequest(
                            event.requestNum.intValue(),
                            projectAddress,
                            event._title,
                            description,
                            event._recipient,
                            amountLong,
                            false);
                });
    }

    public void voteRequestForProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {

        voteRequestProjectSub$ = CrowdfundingEvents
                .voteRequestForProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    logger.info("event: inserting vote");
                    Optional<Integer> requestId = sqlRepoSvc.getRequestId(projectAddress,
                            event._requestNo.intValue());
                    String tokenAddress = loadedContract.tokenAddress().sendAsync().get();
                    int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
                    long valueOfVoteLong = Util.revertFromBaseUnit(event._valueOfVote, decimals);
                    sqlRepoInter.insertVote(
                            requestId.get(),
                            event._voter,
                            valueOfVoteLong);
                });
    }

    public void receiveContributionProject(Crowdfunding loadedContract, String blockHash, String projectAddress) {

        receiveContributionFromProjectSub$ = CrowdfundingEvents
                .receiveContributionFromProject(loadedContract, projectAddress, blockHash)
                .subscribe((event) -> {
                    /* received contribution so request completes
                     * update project's completed column to true
                     * fundraising project completes as goal has been reached
                     * update projectRequest completed column to true in the request using project address and requestno
                     */
                    logger.info("event: receive contribution");
                    Optional<Integer> opt = sqlRepoSvc.getRequestId(projectAddress,
                            event._requestNo.intValue());
                    sqlRepoInter.updateProjectCompleted(true, projectAddress);
                    sqlRepoInter.updateRequest(
                            opt.get(),
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
