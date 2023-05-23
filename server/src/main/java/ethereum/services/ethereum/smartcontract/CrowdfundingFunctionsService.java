package ethereum.services.ethereum.smartcontract;

import java.math.BigInteger;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.models.smartcontract.Request;
import ethereum.services.ethereum.BlockchainService;
import ethereum.services.ethereum.LoadContractService;
import ethereum.util.misc.Util;

@Service
public class CrowdfundingFunctionsService {
    @Autowired
    private Web3j web3;
    @Autowired
    private LoadContractService lcSvc;
    @Autowired
    private TokenFunctionsService tokenFuncSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;
    private static final Logger logger = LoggerFactory.getLogger(CrowdfundingFunctionsService.class);

    public Optional<String> createNewRequest(Request request, String projectAddress) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);
            String tokenAddress = crowdfunding.tokenAddress().sendAsync().get();
            int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
            String encodedFunction = crowdfunding
                    .createRequest(
                            request.getTitle(),
                            request.getRecipient(),
                            Util.convertToBaseUnit(request.getAmount(), decimals))
                    .encodeFunctionCall();
            return Optional.of(encodedFunction);
        } catch (Exception e) {
            logger.info("create new request error {} ", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> contribute(String projectAddress, int amount) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);
            String tokenAddress = crowdfunding.tokenAddress().sendAsync().get();
            int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
            String encodedFunction = crowdfunding
                    .contribute(Util.convertToBaseUnit(amount, decimals))
                    .encodeFunctionCall();
            return Optional.of(encodedFunction);
        } catch (Exception e) {
            logger.info("contribute error {} ", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> receiveContribution(String projectAddress, int requestNum) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);
            String encodedFunction = crowdfunding
                    .receiveContribution(BigInteger.valueOf(requestNum))
                    .encodeFunctionCall();
            return Optional.of(encodedFunction);
        } catch (Exception e) {
            logger.info("receive contribution error {} ", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> voteRequest(String projectAddress, int requestNum) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);

            String encodedFunction = crowdfunding
                    .voteRequest(BigInteger.valueOf(requestNum))
                    .encodeFunctionCall();
            return Optional.of(encodedFunction);
            // crowdfunding.voteRequest()
        } catch (Exception e) {
            logger.info("vote request error {} ", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> refund(String projectAddress) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);
            String encodedFunction = crowdfunding.getRefund().encodeFunctionCall();
            return Optional.of(encodedFunction);
        } catch (Exception e) {
            logger.info("refund error {} ", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Long> getContributionAmount(String projectAddress, String contributorAddress) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);
            BigInteger amountBN = crowdfunding.contributors(contributorAddress).sendAsync().get();
            String tokenAddress = crowdfunding.tokenAddress().sendAsync().get();
            int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
            long amount = Util.revertFromBaseUnit(amountBN, decimals);
            return Optional.of(amount);
        } catch (Exception e) {
            logger.info("get contribution amount {} ", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Long> getRaisedAmount(String projectAddress) {
        try {
            Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(projectAddress);
            BigInteger amountBN = crowdfunding.raisedAmount().sendAsync().get();
            String tokenAddress = crowdfunding.tokenAddress().sendAsync().get();
            int decimals = tokenFuncSvc.getTokenDecimals(tokenAddress);
            long amount = Util.revertFromBaseUnit(amountBN, decimals);
            return Optional.of(amount);
        } catch (Exception e) {
            logger.info("get raised amount {} ", e.getMessage());
            return Optional.empty();
        }
    }
}
