package ethereum.tutorials.java.ethereum.services.ethereum;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import com.mysql.cj.jdbc.ha.BalanceStrategy;

import ethereum.tutorials.java.ethereum.eventHandler.BlockchainEventHandler;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Token;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.DevFaucet;
import ethereum.tutorials.java.ethereum.util.contractEncodedFunctions.FactoryEncodedFunctions;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;

@Service
public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

    @Value("${wallet.public.key}")
    private String publicKey;
    @Value("${wallet.private.key}")
    private String privateKey;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;
    @Value("${faucet.contract.address}")
    private String FaucetContractAddress;
    @Value("${rpc.url}")
    private String rpcUrl;
    @Value("${chain.id}")
    private int chaintId;

    @Autowired
    private Web3j web3;
    @Autowired
    private LoadContractService lcSvc;
    @Autowired
    private BlockchainEventHandler BcEventHandler;

    //https://eth-mainnet.gateway.pokt.network/v1/5f3453978e354ab992c4da79

    // Web3j webThree = Web3j
    //         .build(new HttpService(rpcUrl));
    // Web3ClientVersion clientVersion = webThree.web3ClientVersion().send();

    public Boolean verifySignedMessage(String signature, String nonce, String address) {
        String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        String prefix = PERSONAL_MESSAGE_PREFIX + nonce.length();
        byte[] msgHash = Hash.sha3((prefix + nonce).getBytes());
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        SignatureData sd = new SignatureData(
                v,
                (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32),
                (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64));

        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(
                            new BigInteger(1, sd.getR()),
                            new BigInteger(1, sd.getS())),
                    msgHash);
            if (publicKey != null) {
                String recoveredAddress = "0x" + Keys.getAddress(publicKey);
                Boolean result = recoveredAddress.equalsIgnoreCase(address);
                System.out.println("recovered address >>>> " + recoveredAddress);
                if (result == true)
                    return true;
            }
        }
        return false;
    }

    public <T> T loadCrowdfundingContract(String contractAddress) {
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chaintId);
        var crowdfunding = Crowdfunding.load(contractAddress, web3, txManager, contractGasProvider);

        @SuppressWarnings("unchecked")
        var r = (T) crowdfunding;

        return r;
    }

    public Optional<String> getEncoded(String contractName, String functionName,
            Object... params) {
        Optional<String> opt;
        try {
            switch (contractName) {
                case "CrowdfundingFactory":
                    System.out.println("loading crowdfunding contract " + crowdfundingFactoryContractAddress);
                    CrowdfundingFactory crowdfundingFactory = lcSvc
                            .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                    System.out
                            .println("Crowdfunding contract has been " + crowdfundingFactory.isValid() + "ly loaded");
                    opt = getFunctionEncoded(crowdfundingFactory, functionName,
                            CrowdfundingFactory.class,
                            params);
                    return opt;
                case "DevFaucet":
                    System.out.println("loading faucet contract " + FaucetContractAddress);
                    DevFaucet devFaucet = lcSvc.loadFaucetContract(FaucetContractAddress);
                    System.out.println("Faucet contract has been " + devFaucet.isValid() + "ly uploaded");
                    opt = getFunctionEncoded(devFaucet, functionName, DevFaucet.class, params);
                    return opt;
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<String> getEncoded(String contractName, String functionName, String contractAddress,
            Object... params) {
        Optional<String> opt;
        System.out.println("getting function encoded " + contractName);
        System.out.println("getting function encoded " + contractAddress);
        try {
            switch (contractName) {
                case "CrowdfundingFactory":
                    System.out.println("loading crowdfunding contract " + crowdfundingFactoryContractAddress);
                    CrowdfundingFactory crowdfundingFactory = lcSvc
                            .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                    System.out
                            .println("factory contract has been " + crowdfundingFactory.isValid() + "ly loaded");
                    opt = getFunctionEncoded(crowdfundingFactory, functionName,
                            CrowdfundingFactory.class,
                            params);
                    return opt;
                case "Crowdfunding":
                    Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(contractAddress);
                    System.out
                            .println("Crowdfunding contract has been " + crowdfunding.isValid() + "ly loaded");
                    opt = getFunctionEncoded(crowdfunding, functionName,
                            Crowdfunding.class,
                            params);
                    return opt;
                case "DevFaucet":
                    System.out.println("loading faucet contract " + FaucetContractAddress);
                    DevFaucet devFaucet = lcSvc.loadFaucetContract(FaucetContractAddress);
                    System.out.println("Faucet contract has been " + devFaucet.isValid() + "ly uploaded");
                    opt = getFunctionEncoded(devFaucet, functionName, DevFaucet.class, params);
                    return opt;
                case "Token":
                    System.out.println("loading token contract" + contractAddress);
                    Token tokenContract = lcSvc.loadTokenContract(contractAddress);
                    System.out.println("Faucet contract has been " + tokenContract.isValid() + "ly uploaded");
                    opt = getFunctionEncoded(tokenContract, functionName, Token.class, params);
                    return opt;
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private <T> Optional<String> getFunctionEncoded(
            T loadedContract,
            String functionName,
            Class<T> contractClass, // class of loadedContract
            Object... params)
            throws Exception {
        String encodedFunction = "";
        if (((Contract) contractClass.cast(loadedContract)).isValid()) {
            RemoteFunctionCall<TransactionReceipt> function;

            if (params.length == 0) {
                System.out.println("approve function? >> " + functionName);
                Method contractFunction = contractClass.getMethod(functionName);
                @SuppressWarnings("unchecked") // unchecked type conversion
                RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                        .invoke(contractClass.cast(loadedContract));
                function = afunction;
            } else {
                Class<?>[] paramsTypeList = new Class<?>[params.length];
                Object[] paramsArray = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    Class<?> paramClass = params[i].getClass();
                    if (paramClass.getSimpleName().contains("Integer")) {
                        paramsTypeList[i] = BigInteger.class;
                        paramsArray[i] = BigInteger.valueOf(((Integer) params[i]).longValue());
                    } else if (paramClass.getSimpleName().contains("Long")) {
                        paramsTypeList[i] = BigInteger.class;
                        paramsArray[i] = BigInteger.valueOf((long) params[i]);
                    } else {
                        paramsTypeList[i] = paramClass;
                        paramsArray[i] = paramClass.cast(params[i]);
                    }
                }
                for (Class<?> classtype : paramsTypeList) {
                    System.out.println(classtype.getName());
                }
                System.out.println(functionName);
                Method contractFunction = contractClass.getMethod(functionName, paramsTypeList);
                @SuppressWarnings("unchecked") // unchecked type conversion
                RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                        .invoke(contractClass.cast(loadedContract), paramsArray);
                function = afunction;
            }
            encodedFunction = function.encodeFunctionCall();
        } else {
            throw new Exception("contract not found");
        }
        System.out.println("got Function Encoded");
        return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);

        // if (((Contract) contractClass.cast(loadedContract)).isValid()) {
        //     // can try contractClass.getMethod()

        //     // return (method.getReturnType().getSimpleName().contains("RemoteFunctionCall")
        //     //         && method.getName().equalsIgnoreCase(functionName));
        //     // method names have to be exact and same case as defined in the contract
        //     Iterator<Method> iter = Arrays.asList(contractClass.getDeclaredMethods()).stream()
        //             .filter((method) -> method.getName().equalsIgnoreCase(functionName))
        //             .iterator();
        //     while (iter.hasNext()) {
        //         System.out.println("iter next");
        //         Method contractFunction = iter.next();
        //         contractFunction.getParameterTypes();

        //         // may be unnecessary to check name equality again
        //         if (contractFunction.getName().equalsIgnoreCase(functionName)) {
        //             RemoteFunctionCall<TransactionReceipt> function;
        //             System.out.println("getting encoded function for >>> " + contractFunction.getName());
        //             if (params.length == 0) {
        //                 @SuppressWarnings("unchecked") // unchecked type conversion
        //                 RemoteFunctionCall<TransactionReceipt> sfunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
        //                         .invoke(contractClass.cast(loadedContract));
        //                 function = sfunction;

        //             } else {
        //                 List<Object> list = new LinkedList<Object>();
        //                 Class<?>[] typeArr = contractFunction.getParameterTypes();
        //                 for (int i = 0; i < params.length; i++) {
        //                     if (typeArr[i].getSimpleName().equals("BigInteger")) {
        //                         System.out.println(params[i].getClass().getSimpleName());
        //                         if (params[i].getClass().getSimpleName().contains("Integer")) {
        //                             list.add(BigInteger.valueOf(((Integer) params[i]).longValue()));
        //                         } else {
        //                             // list.add(BigInteger.valueOf(((Long) ((Integer) params[i]).longValue())));
        //                             list.add(BigInteger.valueOf((long) params[i]));
        //                         }
        //                     } else
        //                         list.add(typeArr[i].cast(params[i]));
        //                 }

        //                 Object[] paramsArr = list.toArray();
        //                 @SuppressWarnings("unchecked") // unchecked type conversion
        //                 RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
        //                         .invoke(contractClass.cast(loadedContract), paramsArr);
        //                 function = afunction;
        //             }
        //             encodedFunction = function.encodeFunctionCall();
        //             System.out.println("getFunctionEncoded breaking...");
        //             break;
        //         }
        //     }
        // } else
        //     throw new Exception("contract not found");
        // return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    }

    public Optional<String> getContractFunctionEncoded(
            Object loadContract,
            String contractName,
            String contractAddress,
            String functionName,
            Object... params)
            throws Exception {
        String encodedFunction = "";
        Crowdfunding contract = loadCrowdfundingContract(contractAddress);
        System.out.println("loaded contract");
        System.out.println("special test contract >>>> " + contract.isValid());
        System.out.println("params >>>> " + params.length);

        if (contract.isValid()) {

            Iterator<Method> iter = Arrays.asList(Crowdfunding.class.getDeclaredMethods()).stream()
                    .filter((method) -> (method.getReturnType().getSimpleName().contains("RemoteFunctionCall")
                            && method.getName().equalsIgnoreCase(functionName)))
                    .iterator();
            while (iter.hasNext()) {

                Method contractFunction = iter.next();
                contractFunction.getParameterTypes();
                System.out.println("method name after iter" + contractFunction.getName());
                if (contractFunction.getName().equalsIgnoreCase(functionName)) {
                    RemoteFunctionCall<TransactionReceipt> function;

                    if (params.length == 0) {
                        System.out.println("OK >>>> " + functionName);
                        @SuppressWarnings("unchecked")
                        RemoteFunctionCall<TransactionReceipt> sfunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                                .invoke(contract);
                        function = sfunction;
                        // System.out.println("result of calling function" + function.send().toString());

                    } else {
                        System.out.println("have params >>>> " + functionName);
                        List<Object> list = new LinkedList<Object>();
                        Class<?>[] typeArr = contractFunction.getParameterTypes();
                        for (int i = 0; i < params.length; i++) {
                            if (typeArr[i].getSimpleName().equals("BigInteger")) {
                                list.add(BigInteger.valueOf((int) params[i]));
                            } else
                                list.add(typeArr[i].cast(params[i]));
                        }

                        Object[] paramsArr = list.toArray();
                        System.out.println("length of params >>>> " + params.length + " " + params[0].getClass());
                        System.out.println(paramsArr[0].getClass());
                        @SuppressWarnings("unchecked")
                        RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                                .invoke(contract, paramsArr);
                        function = afunction;
                    }
                    encodedFunction = function.encodeFunctionCall();
                    System.out.println("encode function >>>> " + encodedFunction);
                }
            }
        }
        return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    }

    public Optional<String> xxgetFunctionEncoded(String contractAddress, String functionName, Object... params)
            throws Exception {
        String encodedFunction = "";
        Crowdfunding contract = loadCrowdfundingContract(contractAddress);
        System.out.println("loaded contract");
        System.out.println("special test contract >>>> " + contract.isValid());
        System.out.println("params >>>> " + params.length);

        if (contract.isValid()) {

            Iterator<Method> iter = Arrays.asList(Crowdfunding.class.getDeclaredMethods()).stream()
                    .filter((method) -> (method.getReturnType().getSimpleName().contains("RemoteFunctionCall")
                            && method.getName().equalsIgnoreCase(functionName)))
                    .iterator();
            while (iter.hasNext()) {

                Method contractFunction = iter.next();
                contractFunction.getParameterTypes();
                System.out.println("method name after iter" + contractFunction.getName());
                if (contractFunction.getName().equalsIgnoreCase(functionName)) {
                    RemoteFunctionCall<TransactionReceipt> function;

                    if (params.length == 0) {
                        System.out.println("OK >>>> " + functionName);
                        @SuppressWarnings("unchecked")
                        RemoteFunctionCall<TransactionReceipt> sfunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                                .invoke(contract);
                        function = sfunction;
                        // System.out.println("result of calling function" + function.send().toString());

                    } else {
                        System.out.println("have params >>>> " + functionName);
                        List<Object> list = new LinkedList<Object>();
                        Class<?>[] typeArr = contractFunction.getParameterTypes();
                        for (int i = 0; i < params.length; i++) {
                            if (typeArr[i].getSimpleName().equals("BigInteger")) {
                                list.add(BigInteger.valueOf((int) params[i]));
                            } else
                                list.add(typeArr[i].cast(params[i]));
                        }

                        Object[] paramsArr = list.toArray();
                        System.out.println("length of params >>>> " + params.length + " " + params[0].getClass());
                        System.out.println(paramsArr[0].getClass());
                        @SuppressWarnings("unchecked")
                        RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                                .invoke(contract, paramsArr);
                        function = afunction;
                    }
                    encodedFunction = function.encodeFunctionCall();
                    System.out.println("encode function >>>> " + encodedFunction);
                }
            }
        }
        return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    }

    public void getEvent(String contractName, String functionName, String blockHash, String projectAddress,
            String... description) {
        try {
            switch (contractName) {
                // ignore projectAddress
                case "CrowdfundingFactory" -> {
                    CrowdfundingFactory loadedFactoryContract = lcSvc
                            .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                    switch (functionName) {
                        case "createNewProject" ->
                            BcEventHandler.createNewProject(loadedFactoryContract, blockHash, description[0]);
                    }
                }
                // ignore projectAddress
                case "DevFaucet" -> {
                    DevFaucet loadedContract = lcSvc.loadFaucetContract(FaucetContractAddress);
                    BcEventHandler.faucetDistribution(loadedContract, blockHash);
                }
                case "Crowdfunding" -> {
                    Crowdfunding loadedCrowdContract = lcSvc.loadCrowdfundingContract(projectAddress);
                    switch (functionName) {
                        case "contribute" ->
                            BcEventHandler.contributeToProject(loadedCrowdContract, blockHash, projectAddress);
                        case "getRefund" ->
                            BcEventHandler.refundFromProject(loadedCrowdContract, blockHash, projectAddress);
                        case "createRequest" ->
                            BcEventHandler.createRequestForProject(loadedCrowdContract, blockHash, description[0],
                                    projectAddress);
                        case "voteRequest" ->
                            BcEventHandler.voteRequestForProject(loadedCrowdContract, blockHash, projectAddress);
                        case "receiveContribution" ->
                            BcEventHandler.receiveContributionProject(loadedCrowdContract, blockHash, projectAddress);
                    }
                }
            }
        } finally {
            disposeDisposable();
        }

    }

    // this is just calling the contract function which does not cost gas
    // therefore can call the contract from the server side instead of the client side
    public Optional<String> getTokenBalanceFuncEncoded(String tokenAddress, Object... params) throws Exception {
        Token tokenContract = lcSvc.loadTokenContract(tokenAddress);
        String accountAddress = (String) params[0];
        BigInteger balance = tokenContract.balanceOf(accountAddress).send();
        System.out.println("balance is > " + balance);
        // String abiFunction;
        try {
            return getFunctionEncoded(tokenContract, "balanceOf", Token.class, params);
            // abiFunction = tokenContract.balanceOf(address).encodeFunctionCall();
            // return Optional.of(abiFunction);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<BigInteger> getTokenBalance(String tokenAddress, Object... params) {
        Token tokenContract = lcSvc.loadTokenContract(tokenAddress);
        String accountAddress = (String) params[0];
        try {
            BigInteger balance = tokenContract.balanceOf(accountAddress).send();
            return Optional.of(balance);
        } catch (ContractCallException e) {
            // this exception is the result of 0 balance
            return Optional.of(BigInteger.valueOf(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getContractAddress(String contractName) {
        switch (contractName) {
            case ("CrowdfundingFactory"):
                return Optional.of(crowdfundingFactoryContractAddress);
            case ("DevFaucet"):
                return Optional.of(FaucetContractAddress);
        }
        return Optional.empty();
    }

    private void disposeDisposable() {
        if (BcEventHandler.contributeSub$ != null && !BcEventHandler.contributeSub$.isDisposed())
            BcEventHandler.contributeSub$.dispose();
        if (BcEventHandler.createNewProjectSub$ != null && !BcEventHandler.createNewProjectSub$.isDisposed())
            BcEventHandler.createNewProjectSub$.dispose();
        if (BcEventHandler.createRequestForProjectSub$ != null
                && !BcEventHandler.createRequestForProjectSub$.isDisposed())
            BcEventHandler.createRequestForProjectSub$.dispose();
        if (BcEventHandler.faucetDistributedSub$ != null && !BcEventHandler.faucetDistributedSub$.isDisposed())
            BcEventHandler.faucetDistributedSub$.dispose();
        if (BcEventHandler.receiveContributionFromProjectSub$ != null
                && !BcEventHandler.receiveContributionFromProjectSub$.isDisposed())
            BcEventHandler.receiveContributionFromProjectSub$.dispose();
        if (BcEventHandler.refundFromProjectSub$ != null && !BcEventHandler.refundFromProjectSub$.isDisposed())
            BcEventHandler.refundFromProjectSub$.dispose();
        if (BcEventHandler.voteRequestProjectSub$ != null
                && !BcEventHandler.voteRequestProjectSub$.isDisposed())
            BcEventHandler.voteRequestProjectSub$.dispose();
    }
}
