package ethereum.tutorials.java.ethereum.util.contractEncodedFunctions;

import java.math.BigInteger;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;

public class FactoryEncodedFunctions {

    /* 
     * blockchain transactions
     */
    public static String createNewProject(CrowdfundingFactory loadedContract, long goal, long timeAhead,
            String tokenAddress,
            String description) {
        return loadedContract.createNewProject(BigInteger.valueOf(goal), BigInteger.valueOf(timeAhead), tokenAddress,
                description).encodeFunctionCall();
    }

    public static String contributeToProject(CrowdfundingFactory loadedContract, String projectAddress, long amount) {
        return loadedContract.contributeToProject(projectAddress, BigInteger.valueOf(amount)).encodeFunctionCall();
    }

    public static String getRefundForProject(CrowdfundingFactory loadedContract, String projectAddress) {
        return loadedContract.getRefundFromProject(projectAddress).encodeFunctionCall();
    }

    public static String createRequestForProject(CrowdfundingFactory loadedContract, String projectAddress,
            String description,
            String recipient, long amount) {
        return loadedContract
                .createRequestForProject(projectAddress, description, recipient, BigInteger.valueOf(amount))
                .encodeFunctionCall();
    }

    public static String voteRequestForProject(CrowdfundingFactory loadedContract, String projectAddress,
            long requestNum) {
        return loadedContract.voteRequestForProject(projectAddress, BigInteger.valueOf(requestNum))
                .encodeFunctionCall();
    }

    public static String receiveContributionFromProject(CrowdfundingFactory loadedContract, String projectAddress,
            long requestNum) {
        return loadedContract.receiveContributionFromProject(projectAddress, BigInteger.valueOf(requestNum))
                .encodeFunctionCall();
    }

    public static String allowAddress(CrowdfundingFactory loadedContract, String address) {
        return loadedContract.allowAddress(address).encodeFunctionCall();
    }

    public static String disallowAddress(CrowdfundingFactory loadedContract, String address) {
        return loadedContract.disallowAddress(address).encodeFunctionCall();
    }

    public static String toggleFreeForAll(CrowdfundingFactory loadedContract) {
        return loadedContract.toggleFreeForAll().encodeFunctionCall();
    }

    // View functions
    public static String getRequestOfProject(CrowdfundingFactory loadedContract, String projectAddress,
            long requestNum) {
        return loadedContract.getRequestOfProject(projectAddress, BigInteger.valueOf(requestNum)).encodeFunctionCall();
    }

    public static String getNumOfRequestOfProject(CrowdfundingFactory loadedContract, String projectAddress) {
        return loadedContract.getNumOfRequestOfProject(projectAddress).encodeFunctionCall();
    }

    public static String getdescriptionOfProject(CrowdfundingFactory loadedContract, String projectAddress) {
        return loadedContract.getDescriptionOfProject(projectAddress).encodeFunctionCall();
    }

}
