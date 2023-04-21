package ethereum.tutorials.java.ethereum.util.contractEncodedFunctions;

import java.math.BigInteger;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;

public class CrowdfundingEncodedFunctions {

    public static String contributers(Crowdfunding loadedContract, String address) {
        return loadedContract.contributers(address).encodeFunctionCall();
    }

    public static String getDeadline(Crowdfunding loadedContract) {
        return loadedContract.deadline().encodeFunctionCall();
    }

    public static String getBalance(Crowdfunding loadedContract) {
        return loadedContract.getBalance().encodeFunctionCall();
    }

    public static String getRequests(Crowdfunding loadedContract, int requestNum) {
        return loadedContract.requests(BigInteger.valueOf(requestNum)).encodeFunctionCall();
    }

    public static String getGoal(Crowdfunding loadedContract) {
        return loadedContract.goal().encodeFunctionCall();
    }

    public static String getAcceptingTokenAddress(Crowdfunding loadedContract) {
        return loadedContract.tokenAddress().encodeFunctionCall();
    }

    public static String getRaisedAmount(Crowdfunding loadedContract) {
        return loadedContract.raisedAmount().encodeFunctionCall();
    }
}
