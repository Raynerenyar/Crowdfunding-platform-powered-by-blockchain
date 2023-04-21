package ethereum.tutorials.java.ethereum.javaethereum.wrapper;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class TwlvFaucet extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b506040516108c73803806108c783398101604081905261002f916101a6565b6100383361013a565b60058190556000805460ff60a01b1916600160a01b179055600380546001600160a01b0384166001600160a01b0319918216811790925560068054821683179055600780549091168217905560408051638da5cb5b60e01b81529051339291638da5cb5b9160048083019260209291908290030181865afa1580156100c1573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906100e591906101d2565b6001600160a01b0316146101335760405162461bcd60e51b81526020600482015260116024820152703737ba1037bbb732b91037b3103a3bb63b60791b604482015260640160405180910390fd5b50506101f4565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80516001600160a01b03811681146101a157600080fd5b919050565b600080604083850312156101b957600080fd5b6101c28361018a565b9150602083015190509250929050565b6000602082840312156101e457600080fd5b6101ed8261018a565b9392505050565b6106c4806102036000396000f3fe608060405234801561001057600080fd5b50600436106100a95760003560e01c80638da5cb5b116100715780638da5cb5b146101425780639d635037146101535780639d76ea5814610166578063b69ef8a814610179578063e4fc6b6d14610182578063f2fde38b1461018a57600080fd5b8063295711f9146100ae5780632fc7eb00146100b85780634bf365df146100e8578063582fa4b51461010c578063715018a61461013a575b600080fd5b6100b661019d565b005b6006546100cb906001600160a01b031681565b6040516001600160a01b0390911681526020015b60405180910390f35b6000546100fc90600160a01b900460ff1681565b60405190151581526020016100df565b61012c61011a3660046105e1565b60016020526000908152604090205481565b6040519081526020016100df565b6100b66101c6565b6000546001600160a01b03166100cb565b6004546100cb906001600160a01b031681565b6003546100cb906001600160a01b031681565b61012c60025481565b6100b66101da565b6100b66101983660046105e1565b6104be565b6101a5610537565b6000805460ff60a01b198116600160a01b9182900460ff1615909102179055565b6101ce610537565b6101d86000610591565b565b600054600160a01b900460ff16151560011461023d5760405162461bcd60e51b815260206004820152601860248201527f756e61626c6520746f206d696e74207269676874206e6f77000000000000000060448201526064015b60405180910390fd5b6006546040516370a0823160e01b81523060048201526001600160a01b03909116906370a0823190602401602060405180830381865afa158015610285573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906102a99190610611565b600255600554336000908152600160205260409020546102c99190610640565b4210156103295760405162461bcd60e51b815260206004820152602860248201527f796f7572206c617374206d696e74206973206c657373207468616e2031303030604482015267207365636f6e647360c01b6064820152608401610234565b3361036b5760405162461bcd60e51b8152602060048201526012602482015271061646472657373206e6f74206265203078360741b6044820152606401610234565b60065460405163095ea7b360e01b81523360048201526103e860248201819052916001600160a01b03169063095ea7b3906044016020604051808303816000875af11580156103be573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906103e29190610659565b5060065460405163a9059cbb60e01b8152336004820152602481018390526001600160a01b039091169063a9059cbb906044016020604051808303816000875af1158015610434573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104589190610659565b50806002600082825461046b919061067b565b909155505033600081815260016020908152604091829020429055815192835282018390527f50febc49bead4837d411278852c281f9e521d4aee3ef162aabf9f4527bd7941d910160405180910390a150565b6104c6610537565b6001600160a01b03811661052b5760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152608401610234565b61053481610591565b50565b6000546001600160a01b031633146101d85760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152606401610234565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6000602082840312156105f357600080fd5b81356001600160a01b038116811461060a57600080fd5b9392505050565b60006020828403121561062357600080fd5b5051919050565b634e487b7160e01b600052601160045260246000fd5b808201808211156106535761065361062a565b92915050565b60006020828403121561066b57600080fd5b8151801515811461060a57600080fd5b818103818111156106535761065361062a56fea264697066735822122098370154e447c121b543b3f4ba44ff68c3d1df94367cb40463177e56d89d3d8764736f6c63430008130033";

    public static final String FUNC_BALANCE = "balance";

    public static final String FUNC_MINTABLE = "mintable";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_TIMESTAMPOFLASTMINT = "timestampOfLastMint";

    public static final String FUNC_TOKENADDRESS = "tokenAddress";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_TWLV = "twlv";

    public static final String FUNC_TWLVOWNER = "twlvOwner";

    public static final String FUNC_DISTRIBUTE = "distribute";

    public static final String FUNC_TOGGLEFAUCET = "toggleFaucet";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }));;

    public static final Event SUPPLYCHANGE_EVENT = new Event("SupplyChange",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }));;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected TwlvFaucet(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TwlvFaucet(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TwlvFaucet(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TwlvFaucet(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT,
                transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(
                valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT,
                        log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<SupplyChangeEventResponse> getSupplyChangeEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SUPPLYCHANGE_EVENT,
                transactionReceipt);
        ArrayList<SupplyChangeEventResponse> responses = new ArrayList<SupplyChangeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SupplyChangeEventResponse typedResponse = new SupplyChangeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.mintedToAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SupplyChangeEventResponse> supplyChangeEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SupplyChangeEventResponse>() {
            @Override
            public SupplyChangeEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SUPPLYCHANGE_EVENT, log);
                SupplyChangeEventResponse typedResponse = new SupplyChangeEventResponse();
                typedResponse.log = log;
                typedResponse.mintedToAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SupplyChangeEventResponse> supplyChangeEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SUPPLYCHANGE_EVENT));
        return supplyChangeEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> balance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> mintable() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MINTABLE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> timestampOfLastMint(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TIMESTAMPOFLASTMINT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> tokenAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKENADDRESS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> twlv() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TWLV,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> twlvOwner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TWLVOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> distribute() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DISTRIBUTE,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> toggleFaucet() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TOGGLEFAUCET,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static TwlvFaucet load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit) {
        return new TwlvFaucet(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TwlvFaucet load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new TwlvFaucet(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TwlvFaucet load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new TwlvFaucet(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TwlvFaucet load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return new TwlvFaucet(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TwlvFaucet> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _tokenAddress, BigInteger mintTimer) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokenAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(mintTimer)));
        return deployRemoteCall(TwlvFaucet.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<TwlvFaucet> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider, String _tokenAddress, BigInteger mintTimer) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokenAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(mintTimer)));
        return deployRemoteCall(TwlvFaucet.class, web3j, transactionManager, contractGasProvider, BINARY,
                encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TwlvFaucet> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit, String _tokenAddress, BigInteger mintTimer) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokenAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(mintTimer)));
        return deployRemoteCall(TwlvFaucet.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TwlvFaucet> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice,
            BigInteger gasLimit, String _tokenAddress, BigInteger mintTimer) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokenAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(mintTimer)));
        return deployRemoteCall(TwlvFaucet.class, web3j, transactionManager, gasPrice, gasLimit, BINARY,
                encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class SupplyChangeEventResponse extends BaseEventResponse {
        public String mintedToAddress;

        public BigInteger amount;
    }
}
