package ethereum.services.ethereum.smartcontract;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import ethereum.javaethereum.wrapper.DevFaucet;
import ethereum.services.ethereum.LoadContractService;

@Service
public class FaucetFunctionsService {
    @Value("${wallet.public.address}")
    private String walletAddress;
    @Value("${faucet.contract.address}")
    private String faucetContractAddress;
    @Autowired
    private LoadContractService lcSvc;
    private static final Logger logger = LoggerFactory.getLogger(FaucetFunctionsService.class);

    public Optional<String> distribute() {
        try {
            DevFaucet faucet = lcSvc.loadFaucetContract(faucetContractAddress);
            String encodedFunction = faucet.distribute().encodeFunctionCall();
            return Optional.of(encodedFunction);
        } catch (Exception e) {
            logger.info("error {}", e.getMessage());
            return Optional.empty();
        }
    }
}
