package ethereum.tutorials.java.ethereum.service.ethereum;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
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
    @Value("${contract.address}")
    private String contractAddress;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;
    @Value("${rpc.url}")
    private String rpcUrl;
    @Value("${chain.id}")
    private int chaintId;

    @Autowired
    private Web3j web3;
    @Autowired
    private LoadContractService lcSvc;

    //https://eth-mainnet.gateway.pokt.network/v1/5f3453978e354ab992c4da79

    // Web3j webThree = Web3j
    //         .build(new HttpService(rpcUrl));
    // Web3ClientVersion clientVersion = webThree.web3ClientVersion().send();

    public String testRun(String fromAddress) {

        System.out.println("rpcUrl >>>>>>>> " + rpcUrl);
        System.out.println("contractAddress >>>>>>>> " + contractAddress);
        System.out.println("publickey >>>>>>> " + publicKey);
        System.out.println("fromAddress" + fromAddress);
        System.out.println("privateKey >>>>>>>> " + privateKey);
        Web3j webThree = Web3j.build(new HttpService(rpcUrl));
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        ECKeyPair keyPair = Credentials.create(privateKey, publicKey).getEcKeyPair();
        Credentials cred = Credentials.create(privateKey);
        TransactionManager cManager = new ClientTransactionManager(webThree, fromAddress.toLowerCase());
        TransactionManager txManager = new RawTransactionManager(webThree,
                cred, chaintId);

        String encodedFunction = "";
        try {
            System.out.println("loading contract");
            // Crowdfunding.load(fromAddress, webThree, cred, contractGasProvider)
            // Crowdfunding.deploy(webThree, txManager, contractGasProvider, fromAddress, null, null, encodedFunction)
            Crowdfunding crowdfunding = Crowdfunding.load(contractAddress, webThree, txManager, contractGasProvider);
            // Crowdfunding crowdfunding = Crowdfunding.deploy(webThree, txManager, contractGasProvider,
            //         BigInteger.valueOf(1000), BigInteger.valueOf(3000)).send();
            if (crowdfunding.isValid()) {
                System.out.println("contract is valid");
                // encodedFunction = crowdfunding.contribute(BigInteger.valueOf(100)).encodeFunctionCall();
                // TransactionReceipt receipt = crowdfunding.contribute(BigInteger.valueOf(100)).send();
                crowdfunding.contribute(null, encodedFunction);

                // encodedFunction = crowdfunding.contribute(BigInteger.valueOf(100)).encodeFunctionCall();
                // crowdfunding.voteRequest(BigInteger.valueOf(0));

                System.out.println("the encoded function >>>> " + encodedFunction);
                // encodedFunction = receipt.getTransactionHash();
                // Numeric.toHexString(TransactionEncoder.encode(transactionReceipt));
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return encodedFunction;
    }

    public String verifyContract() {
        /* https://api.etherscan.io/api
        ?module=contract
        &action=getsourcecode
        &address=0xBB9bc244D798123fDe783fCc1C72d3Bb8C189413
        &apikey=YourApiKeyToken
         */
        UriComponentsBuilder.fromUriString(privateKey);

        return "";
    }

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

    public <T> T loadContract(String contractAddress) {
        Credentials cred = Credentials.create(privateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TransactionManager txManager = new RawTransactionManager(web3,
                cred, chaintId);
        var crowdfunding = Crowdfunding.load(contractAddress, web3, txManager, contractGasProvider);

        @SuppressWarnings("unchecked")
        var r = (T) crowdfunding;

        return r;
    }

    public Optional<String> getEncoded(String contractName, String contractAddress, String functionName,
            Object... params) {
        Optional<String> opt;
        try {

            // can create other if statements for interaction with other contracts
            if (contractName.equalsIgnoreCase("CrowdfundingFactory")) {
                CrowdfundingFactory crowdfundingFactory = lcSvc
                        .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
                opt = getFunctionEncoded(crowdfundingFactory, contractAddress, functionName,
                        CrowdfundingFactory.class,
                        params);
                return opt;
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public <T> Optional<String> getFunctionEncoded(
            T loadedContract,
            String contractAddress,
            String functionName,
            Class<T> contractClass,
            Object... params)
            throws Exception {
        String encodedFunction = "";
        if (((Contract) contractClass.cast(loadedContract)).isValid()) {

            // return (method.getReturnType().getSimpleName().contains("RemoteFunctionCall")
            //         && method.getName().equalsIgnoreCase(functionName));
            // method names have to be exact and same case as defined in the contract
            Iterator<Method> iter = Arrays.asList(contractClass.getDeclaredMethods()).stream()
                    .filter((method) -> method.getName().equalsIgnoreCase(functionName))
                    .iterator();

            while (iter.hasNext()) {
                System.out.println("iter next");
                Method contractFunction = iter.next();
                contractFunction.getParameterTypes();
                if (contractFunction.getName().equalsIgnoreCase(functionName)) {
                    RemoteFunctionCall<TransactionReceipt> function;

                    if (params.length == 0) {
                        @SuppressWarnings("unchecked") // unchecked type conversion
                        RemoteFunctionCall<TransactionReceipt> sfunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                                .invoke(contractClass.cast(loadedContract));
                        function = sfunction;

                    } else {
                        List<Object> list = new LinkedList<Object>();
                        Class<?>[] typeArr = contractFunction.getParameterTypes();
                        for (int i = 0; i < params.length; i++) {
                            if (typeArr[i].getSimpleName().equals("BigInteger")) {
                                System.out.println(params[i].getClass().getSimpleName());
                                if (params[i].getClass().getSimpleName().contains("Integer")) {
                                    list.add(BigInteger.valueOf(((Integer) params[i]).longValue()));
                                } else {
                                    // list.add(BigInteger.valueOf(((Long) ((Integer) params[i]).longValue())));
                                    list.add(BigInteger.valueOf((long) params[i]));
                                }
                            } else
                                list.add(typeArr[i].cast(params[i]));
                        }

                        Object[] paramsArr = list.toArray();
                        @SuppressWarnings("unchecked") // unchecked type conversion
                        RemoteFunctionCall<TransactionReceipt> afunction = (RemoteFunctionCall<TransactionReceipt>) contractFunction
                                .invoke(contractClass.cast(loadedContract), paramsArr);
                        function = afunction;
                    }
                    encodedFunction = function.encodeFunctionCall();
                    System.out.println("getFunctionEncoded breaking...");
                    break;
                }
            }
        }
        return (encodedFunction.isEmpty()) ? Optional.empty() : Optional.of(encodedFunction);
    }

    public Optional<String> getContractFunctionEncoded(
            Object loadContract,
            String contractName,
            String contractAddress,
            String functionName,
            Object... params)
            throws Exception {
        String encodedFunction = "";
        Crowdfunding contract = loadContract(contractAddress);
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
        Crowdfunding contract = loadContract(contractAddress);
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

    @Bean(name = "crowdfundingAbi")
    public String getAbi() {
        FileReader fr;
        String abi = "";
        try {
            fr = new FileReader(
                    "src/main/java/ethereum/tutorials/java/ethereum/javaethereum/abi/Crowdfunding.json");
            // Reader reader = new StringReader("");
            JsonReader jsonReader = Json.createReader(fr);
            JsonArray abiArray = jsonReader.readObject().getJsonArray("abi");
            abi = abiArray.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return abi;
    }
}
