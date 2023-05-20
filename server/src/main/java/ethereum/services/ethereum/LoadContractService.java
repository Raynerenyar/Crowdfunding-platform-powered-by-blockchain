package ethereum.services.ethereum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.javaethereum.wrapper.Token;
import ethereum.javaethereum.wrapper.DevFaucet;

@Service
public class LoadContractService {

    // @Autowired
    // private Web3j web3;
    @Value("${wallet.private.key}")
    private String privateKey;
    @Value("${chain.id}")
    private int chainId;
    @Value("${rpc.url}")
    private String rpcUrl;

    public Crowdfunding loadCrowdfundingContract(String contractAddress) {
        Web3j web3 = getWeb3(rpcUrl);
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return Crowdfunding.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public CrowdfundingFactory loadCrowdfundingFactoryContract(String contractAddress) {
        Web3j web3 = getWeb3(rpcUrl);
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return CrowdfundingFactory.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public DevFaucet loadFaucetContract(String contractAddress) {
        Web3j web3 = getWeb3(rpcUrl);
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return DevFaucet.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public Token loadTokenContract(String tokenAddress) {
        Web3j web3 = getWeb3(rpcUrl);
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return Token.load(tokenAddress, web3, txManager, contractGasProvider);
    }

    private Web3j getWeb3(String rpcUrl) {
        return Web3j.build(new HttpService(rpcUrl));
    }

}
