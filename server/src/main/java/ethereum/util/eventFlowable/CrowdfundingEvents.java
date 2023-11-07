package ethereum.util.eventFlowable;

import org.web3j.protocol.core.methods.request.EthFilter;

import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.javaethereum.wrapper.CrowdfundingFactory.CreateNewProjectEventEventResponse;
import ethereum.javaethereum.wrapper.Crowdfunding.ContributeEventEventResponse;
import ethereum.javaethereum.wrapper.Crowdfunding.CreateRequestEventEventResponse;
import ethereum.javaethereum.wrapper.Crowdfunding.ReceiveContributionEventEventResponse;
import ethereum.javaethereum.wrapper.Crowdfunding.VoteRequestEventEventResponse;
import ethereum.javaethereum.wrapper.Crowdfunding.GetRefundEventEventResponse;
import io.reactivex.Flowable;

// method are named according to the functions on solidity that emits their respective events
public class CrowdfundingEvents {

    public static Flowable<CreateNewProjectEventEventResponse> createNewProject(
            CrowdfundingFactory loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.createNewProjectEventEventFlowable(ethFilter);
    }

    public static Flowable<ContributeEventEventResponse> contributeToProject(
            Crowdfunding loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.contributeEventEventFlowable(ethFilter);
    }

    public static Flowable<GetRefundEventEventResponse> getRefundFromProject(
            Crowdfunding loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.getRefundEventEventFlowable(ethFilter);
    }

    public static Flowable<CreateRequestEventEventResponse> createRequestForProject(
            Crowdfunding loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.createRequestEventEventFlowable(ethFilter);
    }

    public static Flowable<ReceiveContributionEventEventResponse> receiveContributionFromProject(
            Crowdfunding loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.receiveContributionEventEventFlowable(ethFilter);
    }

    public static Flowable<VoteRequestEventEventResponse> voteRequestForProject(
            Crowdfunding loadedContract,
            String contractAddress,
            String blockHash) {
        EthFilter ethFilter = new EthFilter(blockHash, contractAddress);
        return loadedContract.voteRequestEventEventFlowable(ethFilter);
    }

}
