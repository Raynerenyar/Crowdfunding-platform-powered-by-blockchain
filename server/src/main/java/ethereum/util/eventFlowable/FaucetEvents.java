package ethereum.util.eventFlowable;

import org.web3j.protocol.core.methods.request.EthFilter;

import ethereum.javaethereum.wrapper.DevFaucet;
import ethereum.javaethereum.wrapper.DevFaucet.SupplyChangeEventResponse;
import io.reactivex.Flowable;

public class FaucetEvents {
    public static Flowable<SupplyChangeEventResponse> distributionEvent(
            DevFaucet loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.supplyChangeEventFlowable(ethFilter);
    }
}
