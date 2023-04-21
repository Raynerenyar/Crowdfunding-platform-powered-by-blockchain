package ethereum.tutorials.java.ethereum.subscription;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.utils.Numeric;
import org.web3j.tx.Contract;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.service.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.service.ethereum.LoadContractService;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

@Component
public class BlockchainEventListener {

    @Autowired
    BlockchainService BcSvc;
    @Autowired
    Web3j web3;
    @Autowired
    LoadContractService lcSvc;
    @Value("${contract.address}")
    private String contractAddress;

    public Disposable listeningEvent() {
        Crowdfunding crowdfunding = BcSvc.loadContract(contractAddress);
        String result = "0";
        try {
            result = web3.ethBlockNumber().send().getResult();
            System.out.println("current block number >>>>> " + result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DefaultBlockParameter startBlock = new DefaultBlockParameterNumber(Numeric.toBigInt(result));
        DefaultBlockParameter endBlock = new DefaultBlockParameterNumber(
                Numeric.toBigInt(result).add(BigInteger.valueOf(1000)));
        EthFilter ethFilter = new EthFilter(startBlock, endBlock, contractAddress);
        return crowdfunding.contributeEventEventFlowable(ethFilter).subscribe(events -> {
            System.out.println("listening to event" + events._sender);
        });

    }

    public <T> Optional<String> callEventMethod(
            T loadedContract,
            String contractAddress,
            String functionName,
            Class<T> contractClass,
            Object... params) throws Exception {
        if (((Contract) contractClass.cast(loadedContract)).isValid()) {
            Arrays.asList(contractClass.getDeclaredMethods()).stream().filter((method) -> {
                return true;
            });
        }
        return null;
    }

    public void listenEvent(String crowdfundingFactoryContractAddress, String contractName) {
        if (contractName.equalsIgnoreCase("CrowdfundingFactory")) {
            CrowdfundingFactory crowdfundingFactory = lcSvc
                    .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
        }
    }
}
