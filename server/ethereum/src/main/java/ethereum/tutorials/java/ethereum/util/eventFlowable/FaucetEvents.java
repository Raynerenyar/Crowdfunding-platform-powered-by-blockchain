package ethereum.tutorials.java.ethereum.util.eventFlowable;

import org.web3j.protocol.core.methods.request.EthFilter;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.TwlvFaucet;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.TwlvFaucet.SupplyChangeEventResponse;
import io.reactivex.Flowable;

public class FaucetEvents {
    static Flowable<SupplyChangeEventResponse> contributeEvent(
            TwlvFaucet loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.supplyChangeEventFlowable(ethFilter);
    }
}
