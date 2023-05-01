package ethereum.tutorials.java.ethereum.javaethereum.wrapper;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
import org.web3j.tuples.generated.Tuple6;
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
public class Crowdfunding extends Contract {
    public static final String BINARY = "0x6080604052620f4240600a553480156200001857600080fd5b5060405162001966380380620019668339810160408190526200003b91620003c4565b620000463362000167565b4282116200005357600080fd5b6200005e81620001b7565b620000b05760405162461bcd60e51b815260206004820152601d60248201527f416464726573732070726f7669646564206973206e6f7420746f6b656e00000060448201526064015b60405180910390fd5b803b6200010b5760405162461bcd60e51b815260206004820152602260248201527f416464726573732070726f7669646564206973206e6f74206120636f6e74726160448201526118dd60f21b6064820152608401620000a7565b600380546001600160a01b039586166001600160a01b0319918216179091556008939093556007919091556064600655600180548316331790556004805491909316908216811790925560028054909116909117905562000514565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6040516370a0823160e01b8152336004820152600090829082906001600160a01b038316906370a0823190602401602060405180830381865afa15801562000203573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906200022991906200040f565b1015620002795760405162461bcd60e51b815260206004820152601460248201527f6d6574686f6420646f65736e27742065786973740000000000000000000000006044820152606401620000a7565b6000816001600160a01b03166318160ddd6040518163ffffffff1660e01b8152600401602060405180830381865afa158015620002ba573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190620002e091906200040f565b116200031d5760405162461bcd60e51b815260206004820152600b60248201526a0537570706c7920697320360ac1b6044820152606401620000a7565b6000816001600160a01b03166306fdde036040518163ffffffff1660e01b8152600401600060405180830381865afa1580156200035e573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f191682016040526200038891908101906200043f565b8051909150156200039d575060019392505050565b5060009392505050565b80516001600160a01b0381168114620003bf57600080fd5b919050565b60008060008060808587031215620003db57600080fd5b620003e685620003a7565b935060208501519250604085015191506200040460608601620003a7565b905092959194509250565b6000602082840312156200042257600080fd5b5051919050565b634e487b7160e01b600052604160045260246000fd5b600060208083850312156200045357600080fd5b82516001600160401b03808211156200046b57600080fd5b818501915085601f8301126200048057600080fd5b81518181111562000495576200049562000429565b604051601f8201601f19908116603f01168101908382118183101715620004c057620004c062000429565b816040528281528886848701011115620004d957600080fd5b600093505b82841015620004fd5784840186015181850187015292850192620004de565b600086848301015280965050505050505092915050565b61144280620005246000396000f3fe608060405234801561001057600080fd5b50600436106101a95760003560e01c806360b0b0f0116100f95780639fb42b1f11610097578063d42c48d111610071578063d42c48d1146103d0578063f2fde38b14610406578063f851a44014610419578063fc0c546a1461042c57600080fd5b80639fb42b1f1461039b578063a43b6fff146103a4578063c59ee1dc146103c757600080fd5b806381d12c58116100d357806381d12c58146103495780638da5cb5b1461036e578063937e09b11461037f5780639d76ea581461038857600080fd5b806360b0b0f01461031b578063696c58c41461032e578063715018a61461034157600080fd5b806334b66460116101665780634e260f6f116101405780634e260f6f146102b3578063537ec8ec146102bc57806355e1dedf146102cf5780635b8575a9146102ef57600080fd5b806334b6646014610267578063379248e21461028757806340193883146102aa57600080fd5b806312065fe0146101ae578063180b7138146101c95780631b16bb5c146101fb578063232af2551461021057806329dcb0cf1461023b5780632f0829f714610244575b600080fd5b6101b661043f565b6040519081526020015b60405180910390f35b6101dc6101d7366004610fc2565b6104b6565b604080516001600160a01b0390931683526020830191909152016101c0565b61020e61020936600461100d565b61071a565b005b600354610223906001600160a01b031681565b6040516001600160a01b0390911681526020016101c0565b6101b660075481565b6101b6610252366004610fc2565b6000908152600c602052604090206002015490565b6101b66102753660046110d8565b600d6020526000908152604090205481565b6101b6610295366004610fc2565b6000908152600c602052604090206005015490565b6101b660085481565b6101b660055481565b6101b66102ca3660046110fa565b6107c7565b6102e26102dd366004610fc2565b610a59565b6040516101c0919061116c565b6102236102fd366004610fc2565b6000908152600c60205260409020600101546001600160a01b031690565b6102236103293660046110fa565b610afb565b6101b661033c3660046110d8565b610ceb565b61020e610dc1565b61035c610357366004610fc2565b610dd5565b6040516101c09695949392919061117f565b6000546001600160a01b0316610223565b6101b660065481565b600254610223906001600160a01b031681565b6101b6600b5481565b6101b66103b2366004610fc2565b6000908152600c602052604090206004015490565b6101b660095481565b6103f66103de366004610fc2565b6000908152600c602052604090206003015460ff1690565b60405190151581526020016101c0565b61020e6104143660046110d8565b610e9f565b600154610223906001600160a01b031681565b600454610223906001600160a01b031681565b600480546040516370a0823160e01b815230928101929092526000916001600160a01b03909116906370a0823190602401602060405180830381865afa15801561048d573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104b191906111c4565b905090565b6000806104cb6000546001600160a01b031690565b6001600160a01b0316336001600160a01b031614806104f457506003546001600160a01b031633145b6105195760405162461bcd60e51b8152600401610510906111dd565b60405180910390fd5b60085460095410156105855760405162461bcd60e51b815260206004820152602f60248201527f726169736564416d6f756e74206d757374206265206d6f7265207468616e206f60448201526e1c88195c5d585b081d1bc819dbd85b608a1b6064820152608401610510565b6000838152600c60205260409020600381015460ff16156105e85760405162461bcd60e51b815260206004820152601f60248201527f546865207265717565737420686173206265656e20636f6d706c657465642e006044820152606401610510565b600a80546005919081906105fc908461123c565b6106069190611253565b8360050154116106685760405162461bcd60e51b815260206004820152602760248201527f76616c7565206f6620766f746573206661696c20746f206d65657420726571756044820152661a5c995b595b9d60ca1b6064820152608401610510565b600480546001850154600286015460405163a9059cbb60e01b81526001600160a01b03928316948101949094526024840152169063a9059cbb906044016020604051808303816000875af11580156106c4573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106e89190611275565b50505060038101805460ff191660019081179091558101546002909101546001600160a01b0390911692509050915091565b6000546001600160a01b031633148061073d57506003546001600160a01b031633145b6107595760405162461bcd60e51b8152600401610510906111dd565b600b80546000818152600c60205260408120929061077683611297565b909155508190506107878582611339565b506001810180546001600160a01b0319166001600160a01b039490941693909317909255600282015560038101805460ff19169055600060049091015550565b600082600b54101561081b5760405162461bcd60e51b815260206004820152601760248201527f5265717565737420646f6573206e6f742065786973742e0000000000000000006044820152606401610510565b6000546001600160a01b03166001600160a01b0316826001600160a01b0316036108875760405162461bcd60e51b815260206004820181905260248201527f596f75277265206e6f7420616c6c6f77656420746f20636f6e747269627574656044820152606401610510565b6001600160a01b0382166000908152600d60205260409020546108f75760405162461bcd60e51b815260206004820152602260248201527f596f75206d757374206265206120636f6e7472696275746f7220746f20766f74604482015261652160f01b6064820152608401610510565b6000838152600c602090815260408083206001600160a01b0386168452600681019092529091205460ff16156109685760405162461bcd60e51b8152602060048201526016602482015275165bdd481a185d9948185b1c9958591e481d9bdd195960521b6044820152606401610510565b60018101546001600160a01b03166109c25760405162461bcd60e51b815260206004820152601e60248201527f5265717565737420686173206e6f74206265656e20696e6974696174656400006044820152606401610510565b600854600a546001600160a01b0385166000908152600d60205260408120549092916109ed9161123c565b6109f79190611253565b905080826005016000828254610a0d91906113f9565b9091555050600482018054906000610a2483611297565b90915550506001600160a01b038416600090815260069092016020526040909120805460ff1916600117905590505b92915050565b6000818152600c60205260409020805460609190610a76906112b0565b80601f0160208091040260200160405190810160405280929190818152602001828054610aa2906112b0565b8015610aef5780601f10610ac457610100808354040283529160200191610aef565b820191906000526020600020905b815481529060010190602001808311610ad257829003601f168201915b50505050509050919050565b6000600654831015610b4f5760405162461bcd60e51b815260206004820152601c60248201527f6d696e696d756d20636f6e747269627574696f6e206e6f74206d6574000000006044820152606401610510565b60048054604051636eb1769f60e11b81526001600160a01b038581169382019390935230602482015285929091169063dd62ed3e90604401602060405180830381865afa158015610ba4573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610bc891906111c4565b1015610c165760405162461bcd60e51b815260206004820152601860248201527f746f6b656e207370656e64206e6f7420617070726f76656400000000000000006044820152606401610510565b6001600160a01b0382166000908152600d60205260408120549003610c3f576005805460010190555b600480546040516323b872dd60e01b81526001600160a01b0385811693820193909352306024820152604481018690529116906323b872dd906064016020604051808303816000875af1158015610c9a573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610cbe9190611275565b50506001600160a01b0381166000908152600d602052604090208054830190556009805490920190915590565b60006007544210158015610d025750600854600954105b610d0b57600080fd5b6001600160a01b0382166000908152600d6020526040902054610d2d57600080fd5b6001600160a01b038281166000818152600d6020526040808220805492905560048054915163a9059cbb60e01b815290810193909352602483018290529092169063a9059cbb906044016020604051808303816000875af1158015610d96573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610dba9190611275565b5092915050565b610dc9610f18565b610dd36000610f72565b565b600c60205260009081526040902080548190610df0906112b0565b80601f0160208091040260200160405190810160405280929190818152602001828054610e1c906112b0565b8015610e695780601f10610e3e57610100808354040283529160200191610e69565b820191906000526020600020905b815481529060010190602001808311610e4c57829003601f168201915b505050600184015460028501546003860154600487015460059097015495966001600160a01b039093169591945060ff16925086565b610ea7610f18565b6001600160a01b038116610f0c5760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152608401610510565b610f1581610f72565b50565b6000546001600160a01b03163314610dd35760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152606401610510565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b600060208284031215610fd457600080fd5b5035919050565b634e487b7160e01b600052604160045260246000fd5b80356001600160a01b038116811461100857600080fd5b919050565b60008060006060848603121561102257600080fd5b833567ffffffffffffffff8082111561103a57600080fd5b818601915086601f83011261104e57600080fd5b81358181111561106057611060610fdb565b604051601f8201601f19908116603f0116810190838211818310171561108857611088610fdb565b816040528281528960208487010111156110a157600080fd5b8260208601602083013760006020848301015280975050505050506110c860208501610ff1565b9150604084013590509250925092565b6000602082840312156110ea57600080fd5b6110f382610ff1565b9392505050565b6000806040838503121561110d57600080fd5b8235915061111d60208401610ff1565b90509250929050565b6000815180845260005b8181101561114c57602081850181015186830182015201611130565b506000602082860101526020601f19601f83011685010191505092915050565b6020815260006110f36020830184611126565b60c08152600061119260c0830189611126565b6001600160a01b039790971660208301525060408101949094529115156060840152608083015260a090910152919050565b6000602082840312156111d657600080fd5b5051919050565b60208082526029908201527f596f7520617265206e65697468657220746865206f776e6572206e6f722074686040820152686520666163746f727960b81b606082015260800190565b634e487b7160e01b600052601160045260246000fd5b8082028115828204841417610a5357610a53611226565b60008261127057634e487b7160e01b600052601260045260246000fd5b500490565b60006020828403121561128757600080fd5b815180151581146110f357600080fd5b6000600182016112a9576112a9611226565b5060010190565b600181811c908216806112c457607f821691505b6020821081036112e457634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561133457600081815260208120601f850160051c810160208610156113115750805b601f850160051c820191505b818110156113305782815560010161131d565b5050505b505050565b815167ffffffffffffffff81111561135357611353610fdb565b6113678161136184546112b0565b846112ea565b602080601f83116001811461139c57600084156113845750858301515b600019600386901b1c1916600185901b178555611330565b600085815260208120601f198616915b828110156113cb578886015182559484019460019091019084016113ac565b50858210156113e95787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b80820180821115610a5357610a5361122656fea26469706673582212208670440a59df3e1b6419b101c41a6ca024fe1caf677fa5b15f3fdd042ab7fbc564736f6c63430008130033";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_CONTRIBUTERS = "contributers";

    public static final String FUNC_CROWDFUNDINGFACTORY = "crowdfundingFactory";

    public static final String FUNC_DEADLINE = "deadline";

    public static final String FUNC_GOAL = "goal";

    public static final String FUNC_MINIMUMCONTRIBUTION = "minimumContribution";

    public static final String FUNC_NOOFCONTRIBUTORS = "noOfContributors";

    public static final String FUNC_NUMREQUESTS = "numRequests";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RAISEDAMOUNT = "raisedAmount";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REQUESTS = "requests";

    public static final String FUNC_TOKEN = "token";

    public static final String FUNC_TOKENADDRESS = "tokenAddress";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_CONTRIBUTE = "contribute";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final String FUNC_GETREFUND = "getRefund";

    public static final String FUNC_CREATEREQUEST = "createRequest";

    public static final String FUNC_VOTEREQUEST = "voteRequest";

    public static final String FUNC_RECEIVECONTRIBUTION = "receiveContribution";

    public static final String FUNC_GETREQUESTTITLE = "getRequestTitle";

    public static final String FUNC_GETREQUESTRECIPIENT = "getRequestRecipient";

    public static final String FUNC_GETREQUESTAMOUNT = "getRequestAmount";

    public static final String FUNC_GETREQUESTCOMPLETED = "getRequestCompleted";

    public static final String FUNC_GETREQUESTNUMOFVOTERS = "getRequestNumOfVoters";

    public static final String FUNC_GETREQUESTVALUEOFVOTES = "getRequestValueOfVotes";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("5777", "0x510E165aB7C3DA68b23D6cCfc5CB25558F9e89FF");
        _addresses.put("11155111", "0x0386d8EB70024FabcD5A39a9e5200EBfACF6283C");
    }

    @Deprecated
    protected Crowdfunding(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Crowdfunding(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Crowdfunding(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Crowdfunding(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<String> admin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> contributers(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONTRIBUTERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> crowdfundingFactory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CROWDFUNDINGFACTORY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> deadline() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEADLINE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> goal() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GOAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> minimumContribution() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MINIMUMCONTRIBUTION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> noOfContributors() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NOOFCONTRIBUTORS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> numRequests() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NUMREQUESTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> raisedAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RAISEDAMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, BigInteger, BigInteger>> requests(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_REQUESTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, BigInteger, BigInteger>>(function,
                new Callable<Tuple6<String, String, BigInteger, Boolean, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, BigInteger, Boolean, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, BigInteger, Boolean, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> token() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> tokenAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKENADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> contribute(BigInteger amount, String caller) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONTRIBUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Address(caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> getRefund(String caller) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETREFUND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createRequest(String _title, String _recipient, BigInteger _amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEREQUEST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_title), 
                new org.web3j.abi.datatypes.Address(_recipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> voteRequest(BigInteger _requestNo, String caller) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTEREQUEST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo), 
                new org.web3j.abi.datatypes.Address(caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> receiveContribution(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RECEIVECONTRIBUTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getRequestTitle(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTTITLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getRequestRecipient(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTRECIPIENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getRequestAmount(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTAMOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> getRequestCompleted(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTCOMPLETED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getRequestNumOfVoters(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTNUMOFVOTERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getRequestValueOfVotes(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTVALUEOFVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Crowdfunding load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Crowdfunding(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Crowdfunding load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Crowdfunding(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Crowdfunding load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Crowdfunding(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Crowdfunding load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Crowdfunding(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
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
}
