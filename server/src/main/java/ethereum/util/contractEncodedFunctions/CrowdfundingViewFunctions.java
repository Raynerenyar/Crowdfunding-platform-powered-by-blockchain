package ethereum.util.contractEncodedFunctions;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

public class CrowdfundingViewFunctions {

        private static final Logger logger = LoggerFactory.getLogger(CrowdfundingViewFunctions.class);

        public static String getRaisedAmount(String fromAddress, String projectAddress, Web3j web3)
                        throws InterruptedException, ExecutionException {

                logger.info(fromAddress);
                logger.info(projectAddress);
                Function raisedAmountFunction = new Function(
                                "raisedAmount",
                                Collections.emptyList(),
                                Arrays.asList(new TypeReference<Uint256>() {
                                }));
                String decimalsEncoded = FunctionEncoder.encode(raisedAmountFunction);
                Transaction transaction = Transaction.createEthCallTransaction(
                                fromAddress,
                                projectAddress,
                                decimalsEncoded);
                EthCall amountResponse = web3
                                .ethCall(transaction, DefaultBlockParameter.valueOf("latest"))
                                .sendAsync()
                                .get();
                String value = amountResponse.getValue();
                logger.info("raisedAmount >> {}", value);
                raisedAmountFunction.getOutputParameters();
                List<Type> amountResult = FunctionReturnDecoder.decode(value,
                                raisedAmountFunction.getOutputParameters());

                if (amountResult.size() > 0) {
                        BigInteger valueBig = (BigInteger) amountResult.get(0).getValue();

                        logger.info("results {}", (BigInteger) amountResult.get(0).getValue());
                        return valueBig.toString();
                }
                return "0";
        }

}
