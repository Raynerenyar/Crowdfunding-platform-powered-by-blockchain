package ethereum.util.contractEncodedFunctions;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import ethereum.services.ethereum.BlockchainService;

public class TokenViewFunctions {
    private static final Logger logger = LoggerFactory.getLogger(TokenViewFunctions.class);
    @Autowired
    Web3j web3;
    @Autowired
    BlockchainService bcSvc;
    @Value("${wallet.public.address}")
    private String walletAddress;

    public void getTokenDecimals(String tokenAddress) throws InterruptedException, ExecutionException {
        Function decimalsFunction = new Function(
                "decimals",
                Collections.emptyList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        String decimalsEncoded = FunctionEncoder.encode(decimalsFunction);
        Transaction transaction2 = Transaction.createEthCallTransaction(
                walletAddress,
                tokenAddress,
                decimalsEncoded);
        EthCall decimalsResponse = web3.ethCall(transaction2, DefaultBlockParameter.valueOf("latest")).sendAsync()
                .get();
        String decimalsValue = decimalsResponse.getValue();
        System.out.println("getting decimals");
        logger.info("decimals >> {}", decimalsValue);
        decimalsFunction.getOutputParameters();
        List<Type> decimalsResult = FunctionReturnDecoder.decode(decimalsValue,
                decimalsFunction.getOutputParameters());
        if (decimalsResult.size() > 0) {
            logger.info("results {}", (BigInteger) decimalsResult.get(0).getValue());
        }

        // daily limit 100
        // Boolean res_ult = etherscanSvc.verifyContract("0x465bf3bb7E89DF1B1dC1e4D678FF174a803d6242",
        // 		"CrowdfundingFactory");
        // logger.info("verify contract >> {}", res_ult);

    }
}
