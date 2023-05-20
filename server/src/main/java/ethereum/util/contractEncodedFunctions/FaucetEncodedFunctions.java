package ethereum.util.contractEncodedFunctions;

import ethereum.javaethereum.wrapper.DevFaucet;

public class FaucetEncodedFunctions {
    public static String distribute(DevFaucet loadedContract) {
        return loadedContract.distribute().encodeFunctionCall();
    }

    // public static String toggleFaucet(DevFaucet loadedContract) {
    //     return loadedContract.toggleFaucet().encodeFunctionCall();
    // }

    public static String getBalance(DevFaucet loadedContract) {
        return loadedContract.balance().encodeFunctionCall();
    }

    public static String getTokenAddress(DevFaucet loadedContract) {
        return loadedContract.tokenAddress().encodeFunctionCall();
    }

    public static String getMintable(DevFaucet loadedContract) {
        return loadedContract.mintable().encodeFunctionCall();
    }

    public static String getTimestampOfLastMint(DevFaucet loadedContract, String address) {
        return loadedContract.timestampOfLastMint(address).encodeFunctionCall();
    }
}
