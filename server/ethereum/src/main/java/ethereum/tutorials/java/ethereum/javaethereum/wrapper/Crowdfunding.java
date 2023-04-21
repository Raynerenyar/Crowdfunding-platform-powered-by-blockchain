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
    public static final String BINARY = "0x6080604052620f4240600a553480156200001857600080fd5b5060405162001672380380620016728339810160408190526200003b9162000129565b6200004633620000bc565b600380546001600160a01b0319166001600160a01b038616179055600883905562000072824262000174565b6007556064600655600180546001600160a01b03199081163317909155600480546001600160a01b039390931692821683179055600280549091169091179055506200019c915050565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80516001600160a01b03811681146200012457600080fd5b919050565b600080600080608085870312156200014057600080fd5b6200014b856200010c565b9350602085015192506040850151915062000169606086016200010c565b905092959194509250565b808201808211156200019657634e487b7160e01b600052601160045260246000fd5b92915050565b6114c680620001ac6000396000f3fe608060405234801561001057600080fd5b50600436106101a95760003560e01c8063696c58c4116100f95780639fb42b1f11610097578063d42c48d111610071578063d42c48d1146103d0578063f2fde38b14610406578063f851a44014610419578063fc0c546a1461042c57600080fd5b80639fb42b1f1461039b578063a43b6fff146103a4578063c59ee1dc146103c757600080fd5b806381d12c58116100d357806381d12c58146103495780638da5cb5b1461036e578063937e09b11461037f5780639d76ea581461038857600080fd5b8063696c58c41461030e5780636ddfaed114610321578063715018a61461034157600080fd5b806334b66460116101665780634e260f6f116101405780634e260f6f146102b3578063537ec8ec146102bc5780635b8575a9146102cf57806360b0b0f0146102fb57600080fd5b806334b6646014610267578063379248e21461028757806340193883146102aa57600080fd5b806312065fe0146101ae578063180b7138146101c95780631b16bb5c146101fb578063232af2551461021057806329dcb0cf1461023b5780632f0829f714610244575b600080fd5b6101b661043f565b6040519081526020015b60405180910390f35b6101dc6101d7366004611018565b6104b6565b604080516001600160a01b0390931683526020830191909152016101c0565b61020e610209366004611063565b610740565b005b600354610223906001600160a01b031681565b6040516001600160a01b0390911681526020016101c0565b6101b660075481565b6101b6610252366004611018565b6000908152600c602052604090206002015490565b6101b661027536600461112e565b600d6020526000908152604090205481565b6101b6610295366004611018565b6000908152600c602052604090206005015490565b6101b660085481565b6101b660055481565b61020e6102ca366004611150565b610827565b6102236102dd366004611018565b6000908152600c60205260409020600101546001600160a01b031690565b610223610309366004611150565b610aac565b6101b661031c36600461112e565b610c9f565b61033461032f366004611018565b610d75565b6040516101c091906111c2565b61020e610e17565b61035c610357366004611018565b610e2b565b6040516101c0969594939291906111d5565b6000546001600160a01b0316610223565b6101b660065481565b600254610223906001600160a01b031681565b6101b6600b5481565b6101b66103b2366004611018565b6000908152600c602052604090206004015490565b6101b660095481565b6103f66103de366004611018565b6000908152600c602052604090206003015460ff1690565b60405190151581526020016101c0565b61020e61041436600461112e565b610ef5565b600154610223906001600160a01b031681565b600454610223906001600160a01b031681565b600480546040516370a0823160e01b815230928101929092526000916001600160a01b03909116906370a0823190602401602060405180830381865afa15801561048d573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104b1919061121a565b905090565b6000806104cb6000546001600160a01b031690565b6001600160a01b0316336001600160a01b031614806104f457506003546001600160a01b031633145b6105195760405162461bcd60e51b815260040161051090611233565b60405180910390fd5b60085460095410156105855760405162461bcd60e51b815260206004820152602f60248201527f726169736564416d6f756e74206d757374206265206d6f7265207468616e206f60448201526e1c88195c5d585b081d1bc819dbd85b608a1b6064820152608401610510565b6000838152600c60205260409020600381015460ff16156105e85760405162461bcd60e51b815260206004820152601f60248201527f546865207265717565737420686173206265656e20636f6d706c657465642e006044820152606401610510565b600a80546005919081906105fc9084611292565b61060691906112a9565b8360050154116106405760405162461bcd60e51b815260206004820152600560248201526432b93937b960d91b6044820152606401610510565b600480546001850154600286015460405163a9059cbb60e01b81526001600160a01b03928316948101949094526024840152169063a9059cbb906044016020604051808303816000875af115801561069c573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106c091906112cb565b5060038301805460ff191660019081179091558301546002840154604080516001600160a01b03909316835260208301919091527fc04961a77e387a2654814d5b649460d0b0916738be46777ed5e1ee2b76c354d6910160405180910390a1505060018101546002909101546001600160a01b0390911692509050915091565b6000546001600160a01b031633148061076357506003546001600160a01b031633145b61077f5760405162461bcd60e51b815260040161051090611233565b600b80546000818152600c60205260408120929061079c836112ed565b909155508190506107ad858261138f565b506001810180546001600160a01b0319166001600160a01b0385161790556002810182905560038101805460ff19169055600060048201556040517f5392bd80651676ccaaedec1afbe841d47e369223df0ba1586110b6426ee87a3a906108199086908690869061144f565b60405180910390a150505050565b81600b5410156108795760405162461bcd60e51b815260206004820152601760248201527f5265717565737420646f6573206e6f742065786973742e0000000000000000006044820152606401610510565b6000546001600160a01b03166001600160a01b0316816001600160a01b0316036108e55760405162461bcd60e51b815260206004820181905260248201527f596f75277265206e6f7420616c6c6f77656420746f20636f6e747269627574656044820152606401610510565b6001600160a01b0381166000908152600d60205260409020546109555760405162461bcd60e51b815260206004820152602260248201527f596f75206d757374206265206120636f6e7472696275746f7220746f20766f74604482015261652160f01b6064820152608401610510565b6000828152600c602090815260408083206001600160a01b0385168452600681019092529091205460ff16156109c65760405162461bcd60e51b8152602060048201526016602482015275165bdd481a185d9948185b1c9958591e481d9bdd195960521b6044820152606401610510565b60018101546001600160a01b0316610a205760405162461bcd60e51b815260206004820152601e60248201527f5265717565737420686173206e6f74206265656e20696e6974696174656400006044820152606401610510565b600854600a546001600160a01b0384166000908152600d6020526040902054610a499190611292565b610a5391906112a9565b816005016000828254610a66919061147d565b9091555050600481018054906000610a7d836112ed565b90915550506001600160a01b039091166000908152600690910160205260409020805460ff1916600117905550565b6000600654831015610b005760405162461bcd60e51b815260206004820152601c60248201527f6d696e696d756d20636f6e747269627574696f6e206e6f74206d6574000000006044820152606401610510565b60048054604051636eb1769f60e11b81526001600160a01b038581169382019390935230602482015285929091169063dd62ed3e90604401602060405180830381865afa158015610b55573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610b79919061121a565b1015610bc75760405162461bcd60e51b815260206004820152601860248201527f746f6b656e207370656e64206e6f7420617070726f76656400000000000000006044820152606401610510565b6001600160a01b0382166000908152600d60205260408120549003610bf0576005805460010190555b600480546040516323b872dd60e01b81526001600160a01b0385811693820193909352306024820152604481018690529116906323b872dd906064016020604051808303816000875af1158015610c4b573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610c6f91906112cb565b50506001600160a01b0381166000908152600d602052604090208054830190556009805483019055805b92915050565b60006007544210158015610cb65750600854600954105b610cbf57600080fd5b6001600160a01b0382166000908152600d6020526040902054610ce157600080fd5b6001600160a01b038281166000818152600d6020526040808220805492905560048054915163a9059cbb60e01b815290810193909352602483018290529092169063a9059cbb906044016020604051808303816000875af1158015610d4a573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610d6e91906112cb565b5092915050565b6000818152600c60205260409020805460609190610d9290611306565b80601f0160208091040260200160405190810160405280929190818152602001828054610dbe90611306565b8015610e0b5780601f10610de057610100808354040283529160200191610e0b565b820191906000526020600020905b815481529060010190602001808311610dee57829003601f168201915b50505050509050919050565b610e1f610f6e565b610e296000610fc8565b565b600c60205260009081526040902080548190610e4690611306565b80601f0160208091040260200160405190810160405280929190818152602001828054610e7290611306565b8015610ebf5780601f10610e9457610100808354040283529160200191610ebf565b820191906000526020600020905b815481529060010190602001808311610ea257829003601f168201915b505050600184015460028501546003860154600487015460059097015495966001600160a01b039093169591945060ff16925086565b610efd610f6e565b6001600160a01b038116610f625760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152608401610510565b610f6b81610fc8565b50565b6000546001600160a01b03163314610e295760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152606401610510565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b60006020828403121561102a57600080fd5b5035919050565b634e487b7160e01b600052604160045260246000fd5b80356001600160a01b038116811461105e57600080fd5b919050565b60008060006060848603121561107857600080fd5b833567ffffffffffffffff8082111561109057600080fd5b818601915086601f8301126110a457600080fd5b8135818111156110b6576110b6611031565b604051601f8201601f19908116603f011681019083821181831017156110de576110de611031565b816040528281528960208487010111156110f757600080fd5b82602086016020830137600060208483010152809750505050505061111e60208501611047565b9150604084013590509250925092565b60006020828403121561114057600080fd5b61114982611047565b9392505050565b6000806040838503121561116357600080fd5b8235915061117360208401611047565b90509250929050565b6000815180845260005b818110156111a257602081850181015186830182015201611186565b506000602082860101526020601f19601f83011685010191505092915050565b602081526000611149602083018461117c565b60c0815260006111e860c083018961117c565b6001600160a01b039790971660208301525060408101949094529115156060840152608083015260a090910152919050565b60006020828403121561122c57600080fd5b5051919050565b60208082526029908201527f596f7520617265206e6569746865722074686520666163746f7279206e6f72206040820152683a34329037bbb732b960b91b606082015260800190565b634e487b7160e01b600052601160045260246000fd5b8082028115828204841417610c9957610c9961127c565b6000826112c657634e487b7160e01b600052601260045260246000fd5b500490565b6000602082840312156112dd57600080fd5b8151801515811461114957600080fd5b6000600182016112ff576112ff61127c565b5060010190565b600181811c9082168061131a57607f821691505b60208210810361133a57634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561138a57600081815260208120601f850160051c810160208610156113675750805b601f850160051c820191505b8181101561138657828155600101611373565b5050505b505050565b815167ffffffffffffffff8111156113a9576113a9611031565b6113bd816113b78454611306565b84611340565b602080601f8311600181146113f257600084156113da5750858301515b600019600386901b1c1916600185901b178555611386565b600085815260208120601f198616915b8281101561142157888601518255948401946001909101908401611402565b508582101561143f5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b606081526000611462606083018661117c565b6001600160a01b039490941660208301525060400152919050565b80820180821115610c9957610c9961127c56fea2646970667358221220b5865ee9fc15d02171b8286ff546037ca7e0417ecaab27c89da955b724cea4b064736f6c63430008130033";

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

    public static final String FUNC_GETREQUESTDESCRIPTION = "getRequestDescription";

    public static final String FUNC_GETREQUESTRECIPIENT = "getRequestRecipient";

    public static final String FUNC_GETREQUESTAMOUNT = "getRequestAmount";

    public static final String FUNC_GETREQUESTCOMPLETED = "getRequestCompleted";

    public static final String FUNC_GETREQUESTNUMOFVOTERS = "getRequestNumOfVoters";

    public static final String FUNC_GETREQUESTVALUEOFVOTES = "getRequestValueOfVotes";

    public static final Event CONTRIBUTEEVENT_EVENT = new Event("ContributeEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }));;

    public static final Event CREATEREQUESTEVENT_EVENT = new Event("CreateRequestEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }));;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }));;

    public static final Event RECEIVECONTRIBUTIONEVENT_EVENT = new Event("ReceiveContributionEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }));;

    public static final Event REFUNDFROMPROJECT_EVENT = new Event("RefundFromProject",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }));;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("5777", "0x510E165aB7C3DA68b23D6cCfc5CB25558F9e89FF");
        _addresses.put("11155111", "0x0386d8EB70024FabcD5A39a9e5200EBfACF6283C");
    }

    @Deprecated
    protected Crowdfunding(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Crowdfunding(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Crowdfunding(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Crowdfunding(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ContributeEventEventResponse> getContributeEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CONTRIBUTEEVENT_EVENT,
                transactionReceipt);
        ArrayList<ContributeEventEventResponse> responses = new ArrayList<ContributeEventEventResponse>(
                valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContributeEventEventResponse typedResponse = new ContributeEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContributeEventEventResponse> contributeEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ContributeEventEventResponse>() {
            @Override
            public ContributeEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRIBUTEEVENT_EVENT, log);
                ContributeEventEventResponse typedResponse = new ContributeEventEventResponse();
                typedResponse.log = log;
                typedResponse._sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContributeEventEventResponse> contributeEventEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRIBUTEEVENT_EVENT));
        return contributeEventEventFlowable(filter);
    }

    public static List<CreateRequestEventEventResponse> getCreateRequestEventEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CREATEREQUESTEVENT_EVENT,
                transactionReceipt);
        ArrayList<CreateRequestEventEventResponse> responses = new ArrayList<CreateRequestEventEventResponse>(
                valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CreateRequestEventEventResponse typedResponse = new CreateRequestEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._desrciption = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CreateRequestEventEventResponse> createRequestEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CreateRequestEventEventResponse>() {
            @Override
            public CreateRequestEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CREATEREQUESTEVENT_EVENT, log);
                CreateRequestEventEventResponse typedResponse = new CreateRequestEventEventResponse();
                typedResponse.log = log;
                typedResponse._desrciption = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CreateRequestEventEventResponse> createRequestEventEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CREATEREQUESTEVENT_EVENT));
        return createRequestEventEventFlowable(filter);
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

    public static List<ReceiveContributionEventEventResponse> getReceiveContributionEventEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(
                RECEIVECONTRIBUTIONEVENT_EVENT, transactionReceipt);
        ArrayList<ReceiveContributionEventEventResponse> responses = new ArrayList<ReceiveContributionEventEventResponse>(
                valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ReceiveContributionEventEventResponse typedResponse = new ReceiveContributionEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReceiveContributionEventEventResponse> receiveContributionEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReceiveContributionEventEventResponse>() {
            @Override
            public ReceiveContributionEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(RECEIVECONTRIBUTIONEVENT_EVENT,
                        log);
                ReceiveContributionEventEventResponse typedResponse = new ReceiveContributionEventEventResponse();
                typedResponse.log = log;
                typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReceiveContributionEventEventResponse> receiveContributionEventEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RECEIVECONTRIBUTIONEVENT_EVENT));
        return receiveContributionEventEventFlowable(filter);
    }

    public static List<RefundFromProjectEventResponse> getRefundFromProjectEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REFUNDFROMPROJECT_EVENT,
                transactionReceipt);
        ArrayList<RefundFromProjectEventResponse> responses = new ArrayList<RefundFromProjectEventResponse>(
                valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RefundFromProjectEventResponse typedResponse = new RefundFromProjectEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.projectAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.contributer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.RefundToken = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.RefundAmount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RefundFromProjectEventResponse> refundFromProjectEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RefundFromProjectEventResponse>() {
            @Override
            public RefundFromProjectEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REFUNDFROMPROJECT_EVENT, log);
                RefundFromProjectEventResponse typedResponse = new RefundFromProjectEventResponse();
                typedResponse.log = log;
                typedResponse.projectAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.contributer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.RefundToken = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.RefundAmount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RefundFromProjectEventResponse> refundFromProjectEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REFUNDFROMPROJECT_EVENT));
        return refundFromProjectEventFlowable(filter);
    }

    public RemoteFunctionCall<String> admin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADMIN,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> contributers(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONTRIBUTERS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> crowdfundingFactory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CROWDFUNDINGFACTORY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> deadline() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEADLINE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> goal() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GOAL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> minimumContribution() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MINIMUMCONTRIBUTION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> noOfContributors() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NOOFCONTRIBUTORS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> numRequests() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NUMREQUESTS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> raisedAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RAISEDAMOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, BigInteger, BigInteger>> requests(
            BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_REQUESTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Bool>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }));
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> getRefund(String caller) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETREFUND,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(caller)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createRequest(String _description, String _recipient,
            BigInteger _amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEREQUEST,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_description),
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

    public RemoteFunctionCall<String> getRequestDescription(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETREQUESTDESCRIPTION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getRequestRecipient(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTRECIPIENT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getRequestAmount(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTAMOUNT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> getRequestCompleted(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETREQUESTCOMPLETED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getRequestNumOfVoters(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETREQUESTNUMOFVOTERS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getRequestValueOfVotes(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETREQUESTVALUEOFVOTES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Crowdfunding load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit) {
        return new Crowdfunding(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Crowdfunding load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Crowdfunding(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Crowdfunding load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Crowdfunding(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Crowdfunding load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return new Crowdfunding(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _crowdfundingFactory, BigInteger _goal,
            BigInteger _timeAhead, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory),
                        new org.web3j.abi.datatypes.generated.Uint256(_goal),
                        new org.web3j.abi.datatypes.generated.Uint256(_timeAhead),
                        new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, contractGasProvider, BINARY,
                encodedConstructor);
    }

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider, String _crowdfundingFactory, BigInteger _goal,
            BigInteger _timeAhead, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory),
                        new org.web3j.abi.datatypes.generated.Uint256(_goal),
                        new org.web3j.abi.datatypes.generated.Uint256(_timeAhead),
                        new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, contractGasProvider, BINARY,
                encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit, String _crowdfundingFactory, BigInteger _goal, BigInteger _timeAhead,
            String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory),
                        new org.web3j.abi.datatypes.generated.Uint256(_goal),
                        new org.web3j.abi.datatypes.generated.Uint256(_timeAhead),
                        new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit, String _crowdfundingFactory, BigInteger _goal,
            BigInteger _timeAhead, String _acceptingThisToken) {
        String encodedConstructor = FunctionEncoder
                .encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory),
                        new org.web3j.abi.datatypes.generated.Uint256(_goal),
                        new org.web3j.abi.datatypes.generated.Uint256(_timeAhead),
                        new org.web3j.abi.datatypes.Address(_acceptingThisToken)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, gasPrice, gasLimit, BINARY,
                encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class ContributeEventEventResponse extends BaseEventResponse {
        public String _sender;

        public BigInteger _value;
    }

    public static class CreateRequestEventEventResponse extends BaseEventResponse {
        public String _desrciption;

        public String _recipient;

        public BigInteger _value;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class ReceiveContributionEventEventResponse extends BaseEventResponse {
        public String _recipient;

        public BigInteger _value;
    }

    public static class RefundFromProjectEventResponse extends BaseEventResponse {
        public String projectAddress;

        public String contributer;

        public String RefundToken;

        public BigInteger RefundAmount;
    }
}
