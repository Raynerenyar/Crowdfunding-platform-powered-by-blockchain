package ethereum.services.ethereum.smartcontract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.abi.datatypes.Type;

@Service
public class TokenFunctionsService {
        @Value("${wallet.public.address}")
        private String walletAddress;
        @Autowired
        private Web3j web3;
        private static final Logger logger = LoggerFactory.getLogger(TokenFunctionsService.class);

        public int getTokenDecimals(String tokenAddress) throws InterruptedException, ExecutionException {
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
                EthCall decimalsResponse = web3.ethCall(transaction2, DefaultBlockParameter.valueOf("latest"))
                                .sendAsync()
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
                BigInteger value = (BigInteger) decimalsResult.get(0).getValue();
                return value.intValue();
        }
}
