package ethereum.tutorials.java.ethereum.util.eventFlowable;

import org.web3j.protocol.core.methods.request.EthFilter;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.ContributeEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.CreateRequestEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.NewProjectInitiatedEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.ReceiveContributionEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.RefundFromProjectEventResponse;
import io.reactivex.Flowable;

public class FactoryEvents {
    static Flowable<ContributeEventEventResponse> contributeEvent(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.contributeEventEventFlowable(ethFilter);
    }

    static Flowable<NewProjectInitiatedEventResponse> newProjectInitiated(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.newProjectInitiatedEventFlowable(ethFilter);
    }

    static Flowable<RefundFromProjectEventResponse> refundFromProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.refundFromProjectEventFlowable(ethFilter);
    }

    static Flowable<CreateRequestEventEventResponse> createRequestForProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.createRequestEventEventFlowable(ethFilter);
    }

    static Flowable<ReceiveContributionEventEventResponse> receiveContribution(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.receiveContributionEventEventFlowable(ethFilter);
    }

}
