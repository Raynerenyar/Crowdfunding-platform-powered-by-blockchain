package ethereum.services.ethereum;

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
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.web3j.abi.TypeReference;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
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

import ethereum.eventHandler.BlockchainEventHandler;
import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.javaethereum.wrapper.Token;
import ethereum.models.BcRequest;
import ethereum.javaethereum.wrapper.DevFaucet;
import ethereum.util.contractEncodedFunctions.CrowdfundingViewFunctions;
import ethereum.util.contractEncodedFunctions.FactoryEncodedFunctions;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;

@Service
public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

    @Value("${wallet.public.address}")
    private String walletAddress;
    @Value("${wallet.private.key}")
    private String privateKey;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;
    @Value("${faucet.contract.address}")
    private String FaucetContractAddress;
    // @Value("${wallet.public.address}")
    // private String walletAddress;
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
                logger.info("recovered address from signature, address >> {}", recoveredAddress);
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

    // public Optional<String> getEncoded(String contractName, String functionName,
    //         Object... params) {
    //     Optional<String> opt;
    //     try {
    //         switch (contractName) {
    //             case "CrowdfundingFactory":
    //                 System.out.println("loading factory contract " + crowdfundingFactoryContractAddress);
    //                 CrowdfundingFactory crowdfundingFactory = lcSvc
    //                         .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
    //                 System.out
    //                         .println("factory contract has been " + crowdfundingFactory.isValid() + "ly loaded");
    //                 opt = getFunctionEncoded(crowdfundingFactory, functionName,
    //                         CrowdfundingFactory.class,
    //                         params);
    //                 return opt;
    //             case "DevFaucet":
    //                 System.out.println("loading faucet contract " + FaucetContractAddress);
    //                 DevFaucet devFaucet = lcSvc.loadFaucetContract(FaucetContractAddress);
    //                 System.out.println("Faucet contract has been " + devFaucet.isValid() + "ly uploaded");
    //                 opt = getFunctionEncoded(devFaucet, functionName, DevFaucet.class, params);
    //                 return opt;
    //         }
    //         return Optional.empty();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return Optional.empty();
    //     }
    // }

    // public Optional<String> getEncoded(String contractName, String functionName, String contractAddress,
    //         Object... params) {
    //     Optional<String> opt;
    //     System.out.println("getting function encoded " + contractName);
    //     System.out.println("getting function encoded " + contractAddress);
    //     try {
    //         switch (contractName) {
    //             case "CrowdfundingFactory":
    //                 System.out.println("loading crowdfunding contract " + crowdfundingFactoryContractAddress);
    //                 CrowdfundingFactory crowdfundingFactory = lcSvc
    //                         .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
    //                 System.out
    //                         .println("factory contract has been " + crowdfundingFactory.isValid() + "ly loaded");
    //                 opt = getFunctionEncoded(crowdfundingFactory, functionName,
    //                         CrowdfundingFactory.class,
    //                         params);
    //                 return opt;
    //             case "Crowdfunding":
    //                 Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(contractAddress);
    //                 crowdfunding.requests(BigInteger.valueOf(0));
    //                 System.out
    //                         .println("Crowdfunding contract has been " + crowdfunding.isValid() + "ly loaded");
    //                 opt = getFunctionEncoded(crowdfunding, functionName,
    //                         Crowdfunding.class,
    //                         params);
    //                 return opt;
    //             case "DevFaucet":
    //                 System.out.println("loading faucet contract " + FaucetContractAddress);
    //                 DevFaucet devFaucet = lcSvc.loadFaucetContract(FaucetContractAddress);
    //                 System.out.println("Faucet contract has been " + devFaucet.isValid() + "ly uploaded");
    //                 opt = getFunctionEncoded(devFaucet, functionName, DevFaucet.class, params);
    //                 return opt;
    //             case "Token":
    //                 System.out.println("loading token contract" + contractAddress);
    //                 Token tokenContract = lcSvc.loadTokenContract(contractAddress);
    //                 System.out.println("Faucet contract has been " + tokenContract.isValid() + "ly uploaded");
    //                 opt = getFunctionEncoded(tokenContract, functionName, Token.class, params);
    //                 return opt;
    //         }
    //         return Optional.empty();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return Optional.empty();
    //     }
    // }

    /** 
    * this is for contracts that has address derived from the creation of new Crowdfunding project
    * @param contractName name of the smart contract with same case
    * @param functionName name of the solidity function
    * @param contractAddress contract address
    * @param params params for the solidity functions in the exact order
    * @return Optional of String or empty
    */
    public Optional<String> getEncodedForTransaction(String contractName, String functionName, String contractAddress,
            Object... params) {
        Optional<RemoteFunctionCall<TransactionReceipt>> opt;
        logger.info("get encoded for tx, contractName >> {} " + contractName);
        logger.info("get encoded for tx, contractAddress >> {} " + contractAddress);
        try {
            switch (contractName) {
                case "Crowdfunding":
                    Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(contractAddress);
                    crowdfunding.requests(BigInteger.valueOf(0));
                    logger.info("Crowdfunding contract has been {}ly loaded", crowdfunding.isValid());
                    opt = getFunction(crowdfunding, functionName,
                            Crowdfunding.class,
                            params);
                    logger.info("got encoded function >> function encoded {}", opt.isPresent());
                    return encode(opt);

                case "Token":
                    logger.info("loading token contract, contract address >> {}", contractAddress);
                    Token tokenContract = lcSvc.loadTokenContract(contractAddress);
                    opt = getFunction(tokenContract, functionName, Token.class, params);
                    return encode(opt);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /** 
     * this is for contracts with address from env such as CrowdfundingFactory and DevFaucet
     * @param contractName name of the smart contract with same case
     * @param functionName name of the solidity function
     * @param params params for the solidity functions in the exact order
     * @return Optional of String or empty
     */
    public Optional<String> getEncodedForTransaction(String contractName, String functionName,
            Object... params) {
        Optional<RemoteFunctionCall<TransactionReceipt>> opt;
        logger.info("getting function encoded, contract name >> {}", contractName);
        logger.info("getting function encoded, factory address >> {}", crowdfundingFactoryContractAddress);
        try {
            switch (contractName) {
                case "CrowdfundingFactory":
                    logger.info("load crowdfunding contract ", crowdfundingFactoryContractAddress);
                    CrowdfundingFactory crowdfundingFactory = lcSvc
                            .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                    logger.info("factory contract has been {}ly loaded", crowdfundingFactory.isValid());
                    opt = getFunction(crowdfundingFactory, functionName,
                            CrowdfundingFactory.class,
                            params);
                    return encode(opt);

                case "DevFaucet":
                    logger.info("loading faucet contract >> {}", FaucetContractAddress);
                    DevFaucet devFaucet = lcSvc.loadFaucetContract(FaucetContractAddress);
                    logger.info("factory contract has been {}ly loaded", devFaucet.isValid());
                    opt = getFunction(devFaucet, functionName, DevFaucet.class, params);
                    return encode(opt);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<String> encode(Optional<RemoteFunctionCall<TransactionReceipt>> function) {
        if (function.isPresent()) {
            RemoteFunctionCall<TransactionReceipt> functionCall = function.get();
            String encodedFunction = functionCall.encodeFunctionCall();
            return Optional.of(encodedFunction);
        }
        return Optional.empty();
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
        logger.info("got Function Encoded");
        return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    }

    /**
     * This methods uses the function name and params to find the corresponding remote function call method from the java wrapper of the smart contract
     * @param <T>
     * @param loadedContract
     * @param functionName
     * @param contractClass
     * @param params
     * @return Optional<RemoteFunctionCall<TransactionReceipt>>
     * @throws Exception
     */
    private <T> Optional<RemoteFunctionCall<TransactionReceipt>> getFunction(
            T loadedContract,
            String functionName,
            Class<T> contractClass, // class of loadedContract
            Object... params)
            throws Exception {
        RemoteFunctionCall<TransactionReceipt> function;
        if (((Contract) contractClass.cast(loadedContract)).isValid()) {
            RemoteFunctionCall<TransactionReceipt> bfunction;

            if (params.length == 0) {
                Method contractFunction = contractClass.getMethod(functionName);
                @SuppressWarnings("unchecked") // unchecked type conversion
                RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                        .invoke(contractClass.cast(loadedContract));
                bfunction = afunction;
            } else {
                Class<?>[] paramsTypeList = new Class<?>[params.length];
                Object[] paramsArray = new Object[params.length];

                // conversting the param types that matches the solidity function
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
                // for (Class<?> classtype : paramsTypeList) {
                //     System.out.println(classtype.getName());
                // }
                logger.info("getting function from getFunction method >> {}", functionName);
                // get the method by using function name and param types
                Method contractFunction = contractClass.getMethod(functionName, paramsTypeList);
                Class<?>[] parameterTypes = contractFunction.getParameterTypes();
                for (Class<?> class1 : parameterTypes) {
                    logger.info(class1.getName());
                }
                @SuppressWarnings("unchecked") // unchecked type conversion
                RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                        .invoke(contractClass.cast(loadedContract), paramsArray);
                bfunction = afunction;
            }
            function = bfunction;
        } else {
            throw new Exception("contract not found");
        }
        logger.info("got function");
        return (function == null) ? Optional.empty() : Optional.of(function);
    }

    // public Optional<String> getContractFunctionEncoded(
    //         Object loadContract,
    //         String contractName,
    //         String contractAddress,
    //         String functionName,
    //         Object... params)
    //         throws Exception {
    //     String encodedFunction = "";
    //     Crowdfunding contract = loadCrowdfundingContract(contractAddress);
    //     System.out.println("loaded contract");
    //     System.out.println("special test contract >>>> " + contract.isValid());
    //     System.out.println("params >>>> " + params.length);

    //     if (contract.isValid()) {

    //         Iterator<Method> iter = Arrays.asList(Crowdfunding.class.getDeclaredMethods()).stream()
    //                 .filter((method) -> (method.getReturnType().getSimpleName().contains("RemoteFunctionCall")
    //                         && method.getName().equalsIgnoreCase(functionName)))
    //                 .iterator();
    //         while (iter.hasNext()) {

    //             Method contractFunction = iter.next();
    //             contractFunction.getParameterTypes();
    //             System.out.println("method name after iter" + contractFunction.getName());
    //             if (contractFunction.getName().equalsIgnoreCase(functionName)) {
    //                 RemoteFunctionCall<TransactionReceipt> function;

    //                 if (params.length == 0) {
    //                     System.out.println("OK >>>> " + functionName);
    //                     @SuppressWarnings("unchecked")
    //                     RemoteFunctionCall<TransactionReceipt> sfunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
    //                             .invoke(contract);
    //                     function = sfunction;
    //                     // System.out.println("result of calling function" + function.send().toString());

    //                 } else {
    //                     System.out.println("have params >>>> " + functionName);
    //                     List<Object> list = new LinkedList<Object>();
    //                     Class<?>[] typeArr = contractFunction.getParameterTypes();
    //                     for (int i = 0; i < params.length; i++) {
    //                         if (typeArr[i].getSimpleName().equals("BigInteger")) {
    //                             list.add(BigInteger.valueOf((int) params[i]));
    //                         } else
    //                             list.add(typeArr[i].cast(params[i]));
    //                     }

    //                     Object[] paramsArr = list.toArray();
    //                     System.out.println("length of params >>>> " + params.length + " " + params[0].getClass());
    //                     System.out.println(paramsArr[0].getClass());
    //                     @SuppressWarnings("unchecked")
    //                     RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
    //                             .invoke(contract, paramsArr);
    //                     function = afunction;
    //                 }
    //                 encodedFunction = function.encodeFunctionCall();
    //                 System.out.println("encode function >>>> " + encodedFunction);
    //             }
    //         }
    //     }
    //     return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    // }

    // public Optional<String> xxgetFunctionEncoded(String contractAddress, String functionName, Object... params)
    //         throws Exception {
    //     String encodedFunction = "";
    //     Crowdfunding contract = loadCrowdfundingContract(contractAddress);
    //     System.out.println("loaded contract");
    //     System.out.println("special test contract >>>> " + contract.isValid());
    //     System.out.println("params >>>> " + params.length);

    //     if (contract.isValid()) {

    //         Iterator<Method> iter = Arrays.asList(Crowdfunding.class.getDeclaredMethods()).stream()
    //                 .filter((method) -> (method.getReturnType().getSimpleName().contains("RemoteFunctionCall")
    //                         && method.getName().equalsIgnoreCase(functionName)))
    //                 .iterator();
    //         while (iter.hasNext()) {

    //             Method contractFunction = iter.next();
    //             contractFunction.getParameterTypes();
    //             System.out.println("method name after iter" + contractFunction.getName());
    //             if (contractFunction.getName().equalsIgnoreCase(functionName)) {
    //                 RemoteFunctionCall<TransactionReceipt> function;

    //                 if (params.length == 0) {
    //                     System.out.println("OK >>>> " + functionName);
    //                     @SuppressWarnings("unchecked")
    //                     RemoteFunctionCall<TransactionReceipt> sfunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
    //                             .invoke(contract);
    //                     function = sfunction;
    //                     // System.out.println("result of calling function" + function.send().toString());

    //                 } else {
    //                     System.out.println("have params >>>> " + functionName);
    //                     List<Object> list = new LinkedList<Object>();
    //                     Class<?>[] typeArr = contractFunction.getParameterTypes();
    //                     for (int i = 0; i < params.length; i++) {
    //                         if (typeArr[i].getSimpleName().equals("BigInteger")) {
    //                             list.add(BigInteger.valueOf((int) params[i]));
    //                         } else
    //                             list.add(typeArr[i].cast(params[i]));
    //                     }

    //                     Object[] paramsArr = list.toArray();
    //                     System.out.println("length of params >>>> " + params.length + " " + params[0].getClass());
    //                     System.out.println(paramsArr[0].getClass());
    //                     @SuppressWarnings("unchecked")
    //                     RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
    //                             .invoke(contract, paramsArr);
    //                     function = afunction;
    //                 }
    //                 encodedFunction = function.encodeFunctionCall();
    //                 System.out.println("encode function >>>> " + encodedFunction);
    //             }
    //         }
    //     }
    //     return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    // }

    public Optional<String> viewFunctions(String contractName, String functionName, Object... params) {
        logger.info("view function(without contract address), contractName >> {}, functionName >> {}", contractName,
                functionName);
        for (Object object : params) {
            logger.info("params for the view function(without contract address) >> {}", object.toString());
        }
        try {
            switch (contractName) {
                case "CrowdfundingFactory":
                    CrowdfundingFactory factory = lcSvc
                            .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                    Optional<String> functionEncoded = getFunctionEncoded(factory, functionName,
                            CrowdfundingFactory.class, params);
                    if (functionEncoded.isPresent()) {
                        Transaction transaction = Transaction.createEthCallTransaction(
                                walletAddress,
                                crowdfundingFactoryContractAddress,
                                functionEncoded.get());
                        EthCall response = web3.ethCall(transaction, DefaultBlockParameter.valueOf("latest"))
                                .sendAsync()
                                .get();
                        String value = response.getValue();
                        return Optional.of(value);
                    }
                default:
                    return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> viewFunctions(String contractName, String functionName, String contractAddress,
            Object... params) {
        logger.info("view function(with contract address), contractName >> {}, functionName >> {}", contractName,
                functionName);
        for (Object object : params) {
            logger.info("params for the view function(with contract address) >> {}", object.toString());
        }
        try {
            switch (contractName) {
                case "Crowdfunding":
                    // Crowdfunding crowdfunding = lcSvc.loadCrowdfundingContract(contractAddress);
                    // Optional<RemoteFunctionCall<TransactionReceipt>> function = getFunction(crowdfunding,
                    //         functionName,
                    //         Crowdfunding.class, params);

                    // Optional<String> functionEncoded = encode(function);
                    // logger.info("view function has been encoded >> {}", functionEncoded.isPresent());
                    // if (functionEncoded.isPresent()) {
                    //     Transaction transaction = Transaction.createEthCallTransaction(
                    //             walletAddress,
                    //             contractAddress,
                    //             functionEncoded.get());
                    //     EthCall response = web3.ethCall(transaction, DefaultBlockParameter.valueOf("latest"))
                    //             .sendAsync()
                    //             .get();
                    //     String value = response.getValue();
                    //     logger.info("value returned from view function >> {}", value);
                    //     return Optional.of(value);
                    // }
                    switch (functionName) {
                        case "raisedAmount":
                            return Optional.of(getRaisedAmount(contractAddress));
                    }
                default:
                    return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getRaisedAmount(String projectAddress) throws InterruptedException, ExecutionException {
        return CrowdfundingViewFunctions.getRaisedAmount(walletAddress, projectAddress, web3);
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
