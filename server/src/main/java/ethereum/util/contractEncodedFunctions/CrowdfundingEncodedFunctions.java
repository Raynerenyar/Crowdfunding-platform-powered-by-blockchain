package ethereum.util.contractEncodedFunctions;

import java.math.BigInteger;

import ethereum.javaethereum.wrapper.Crowdfunding;

public class CrowdfundingEncodedFunctions {

    public static String contributors(Crowdfunding loadedContract, String address) {
        return loadedContract.contributors(address).encodeFunctionCall();
    }

    public static String getDeadline(Crowdfunding loadedContract) {
        return loadedContract.deadline().encodeFunctionCall();
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
