package ethereum.tutorials.java.ethereum.services.ethereum;

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

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.TWLV;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.TwlvFaucet;

@Service
public class LoadContractService {

    @Autowired
    private Web3j web3;
    @Value("${wallet.private.key}")
    private String privateKey;
    @Value("${chain.id}")
    private int chainId;
    @Value("${rpc.url}")
    private String rpcUrl;

    public Crowdfunding loadCrowdfundingContract(String contractAddress) {
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return Crowdfunding.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public CrowdfundingFactory loadCrowdfundingFactoryContract(String contractAddress) {
        System.out.println("loading crowdfunding contract");
        System.out.println("loading with private key: " + privateKey);
        System.out.println("to chain id" + chainId);
        System.out.println(web3.ethAccounts().toString());
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return CrowdfundingFactory.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public TwlvFaucet loadFaucetContract(String contractAddress) {
        // Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        System.out.println("loading faucet contract" + contractAddress);
        System.out.println("loading with private key: " + privateKey);
        System.out.println("to chain id" + chainId);
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return TwlvFaucet.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public TWLV loadTokenContract(String tokenAddress) {
        System.out.println(tokenAddress);
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chainId);
        return TWLV.load(tokenAddress, web3, txManager, contractGasProvider);
    }

}
