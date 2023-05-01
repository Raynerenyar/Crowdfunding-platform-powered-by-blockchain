package ethereum.tutorials.java.ethereum.util.eventFlowable;

import org.web3j.protocol.core.methods.request.EthFilter;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.ContributeToProjectEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.CreateRequestForProjectEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.CreateNewProjectEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.ReceiveContributionFromProjectEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.VoteRequestForProjectEventEventResponse;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory.GetRefundFromProjectEventEventResponse;
import io.reactivex.Flowable;

// method are named according to the functions on solidity that emits their respective events
public class FactoryEvents {
    public static Flowable<ContributeToProjectEventEventResponse> contributeToProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.contributeToProjectEventEventFlowable(ethFilter);
    }

    public static Flowable<CreateNewProjectEventEventResponse> createNewProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.createNewProjectEventEventFlowable(ethFilter);
    }

    public static Flowable<GetRefundFromProjectEventEventResponse> getRefundFromProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.getRefundFromProjectEventEventFlowable(ethFilter);
    }

    public static Flowable<CreateRequestForProjectEventEventResponse> createRequestForProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.createRequestForProjectEventEventFlowable(ethFilter);
    }

    public static Flowable<ReceiveContributionFromProjectEventEventResponse> receiveContributionFromProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.receiveContributionFromProjectEventEventFlowable(ethFilter);
    }

    public static Flowable<VoteRequestForProjectEventEventResponse> voteRequestForProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.voteRequestForProjectEventEventFlowable(ethFilter);
    }

}
