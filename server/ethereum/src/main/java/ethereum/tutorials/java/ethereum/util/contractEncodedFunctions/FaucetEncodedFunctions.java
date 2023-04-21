package ethereum.tutorials.java.ethereum.util.contractEncodedFunctions;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.TwlvFaucet;

public class FaucetEncodedFunctions {
    public static String distribute(TwlvFaucet loadedContract) {
        return loadedContract.distribute().encodeFunctionCall();
    }

    public static String toggleFaucet(TwlvFaucet loadedContract) {
        return loadedContract.toggleFaucet().encodeFunctionCall();
    }

    public static String getBalance(TwlvFaucet loadedContract) {
        return loadedContract.balance().encodeFunctionCall();
    }

    public static String getTokenAddress(TwlvFaucet loadedContract) {
        return loadedContract.tokenAddress().encodeFunctionCall();
    }

    public static String getMintable(TwlvFaucet loadedContract) {
        return loadedContract.mintable().encodeFunctionCall();
    }

    public static String getTimestampOfLastMint(TwlvFaucet loadedContract, String address) {
        return loadedContract.timestampOfLastMint(address).encodeFunctionCall();
    }
}
