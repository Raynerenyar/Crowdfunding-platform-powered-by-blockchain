package ethereum.services.ethereum;

import java.math.BigInteger;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.services.ethereum.eventHandler.BlockchainEventHandler;
import ethereum.javaethereum.wrapper.DevFaucet;

@Service
public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

    @Value("${wallet.public.address}")
    private String walletAddress;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;
    @Value("${faucet.contract.address}")
    private String FaucetContractAddress;

    @Autowired
    private LoadContractService lcSvc;
    @Autowired
    private BlockchainEventHandler BcEventHandler;

    public Boolean verifySignedMessage(String signature, String nonce, String address) {
        String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        String prefix = PERSONAL_MESSAGE_PREFIX + nonce.length();
        byte[] msgHash = Hash.sha3((prefix + nonce).getBytes());
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        SignatureData sd = new SignatureData(
                v,
                (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32),
                (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64));

        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(
                            new BigInteger(1, sd.getR()),
                            new BigInteger(1, sd.getS())),
                    msgHash);
            if (publicKey != null) {
                String recoveredAddress = "0x" + Keys.getAddress(publicKey);
                Boolean result = recoveredAddress.equalsIgnoreCase(address);
                logger.info(
                        "recovered address {} from signature, address >> {} >> result {}",
                        recoveredAddress,
                        address,
                        result);
                if (result == true)
                    return true;
            }
        }
        return false;
    }

    public void getEvent(String contractName, String functionName, String blockHash, String projectAddress,
            String... description) {
        try {
            switch (contractName) {
                // ignore projectAddress
                case "CrowdfundingFactory" -> {
                    CrowdfundingFactory loadedFactoryContract = lcSvc
                            .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                    switch (functionName) {
                        case "createNewProject" ->
                            BcEventHandler.createNewProject(loadedFactoryContract, blockHash, description[0],
                                    description[1]);
                    }
                }
                // ignore projectAddress
                case "DevFaucet" -> {
                    DevFaucet loadedContract = lcSvc.loadFaucetContract(FaucetContractAddress);
                    BcEventHandler.faucetDistribution(loadedContract, blockHash);
                }
                case "Crowdfunding" -> {
                    Crowdfunding loadedCrowdContract = lcSvc.loadCrowdfundingContract(projectAddress);
                    switch (functionName) {
                        case "contribute" ->
                            BcEventHandler.contribute(loadedCrowdContract, blockHash, projectAddress);
                        case "getRefund" ->
                            BcEventHandler.getRefund(loadedCrowdContract, blockHash, projectAddress);
                        case "createRequest" ->
                            BcEventHandler.createRequest(loadedCrowdContract, blockHash, description[0],
                                    projectAddress);
                        case "voteRequest" ->
                            BcEventHandler.voteRequestForProject(loadedCrowdContract, blockHash, projectAddress);
                        case "receiveContribution" ->
                            BcEventHandler.receiveContributionProject(loadedCrowdContract, blockHash, projectAddress);
                    }
                }
            }
        } finally {
            disposeDisposable();
        }
    }

    private void disposeDisposable() {
        if (BcEventHandler.contributeSub$ != null && !BcEventHandler.contributeSub$.isDisposed())
            BcEventHandler.contributeSub$.dispose();
        if (BcEventHandler.createNewProjectSub$ != null && !BcEventHandler.createNewProjectSub$.isDisposed())
            BcEventHandler.createNewProjectSub$.dispose();
        if (BcEventHandler.createRequestForProjectSub$ != null
                && !BcEventHandler.createRequestForProjectSub$.isDisposed())
            BcEventHandler.createRequestForProjectSub$.dispose();
        if (BcEventHandler.faucetDistributedSub$ != null && !BcEventHandler.faucetDistributedSub$.isDisposed())
            BcEventHandler.faucetDistributedSub$.dispose();
        if (BcEventHandler.receiveContributionFromProjectSub$ != null
                && !BcEventHandler.receiveContributionFromProjectSub$.isDisposed())
            BcEventHandler.receiveContributionFromProjectSub$.dispose();
        if (BcEventHandler.refundFromProjectSub$ != null && !BcEventHandler.refundFromProjectSub$.isDisposed())
            BcEventHandler.refundFromProjectSub$.dispose();
        if (BcEventHandler.voteRequestProjectSub$ != null
                && !BcEventHandler.voteRequestProjectSub$.isDisposed())
            BcEventHandler.voteRequestProjectSub$.dispose();
    }
}
