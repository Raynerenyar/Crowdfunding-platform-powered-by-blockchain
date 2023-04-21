package ethereum.tutorials.java.ethereum.service.ethereum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;

@Service
public class LoadContractService {

    @Autowired
    private Web3j web3;
    @Value("${wallet.private.key}")
    private String privateKey;
    @Value("${contract.address}")
    private String contractAddress;
    @Value("${chain.id}")
    private int chaintId;

    public Crowdfunding loadCrowdfundingContract(String contractAddress) {
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chaintId);
        return Crowdfunding.load(contractAddress, web3, txManager, contractGasProvider);
    }

    public CrowdfundingFactory loadCrowdfundingFactoryContract(String contractAddress) {
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chaintId);
        return CrowdfundingFactory.load(contractAddress, web3, txManager, contractGasProvider);
    }
}
