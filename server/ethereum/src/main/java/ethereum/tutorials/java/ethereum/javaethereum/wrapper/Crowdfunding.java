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
    public static final String BINARY = "0x6080604052620f4240600b553480156200001857600080fd5b5060405162001ced38038062001ced8339810160408190526200003b9162000499565b620000463362000177565b4283116200005357600080fd5b6200005e82620001c7565b620000b05760405162461bcd60e51b815260206004820152601d60248201527f416464726573732070726f7669646564206973206e6f7420746f6b656e00000060448201526064015b60405180910390fd5b813b6200010b5760405162461bcd60e51b815260206004820152602260248201527f416464726573732070726f7669646564206973206e6f74206120636f6e74726160448201526118dd60f21b6064820152608401620000a7565b6005620001198282620005a2565b5050600380546001600160a01b039586166001600160a01b03199182161790915560099390935560089190915560646007556001805483163317905560048054919093169082168117909255600280549091169091179055620006c8565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6040516370a0823160e01b8152336004820152600090829082906001600160a01b038316906370a0823190602401602060405180830381865afa15801562000213573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906200023991906200066e565b1015620002895760405162461bcd60e51b815260206004820152601460248201527f6d6574686f6420646f65736e27742065786973740000000000000000000000006044820152606401620000a7565b6000816001600160a01b03166318160ddd6040518163ffffffff1660e01b8152600401602060405180830381865afa158015620002ca573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190620002f091906200066e565b116200032d5760405162461bcd60e51b815260206004820152600b60248201526a0537570706c7920697320360ac1b6044820152606401620000a7565b6000816001600160a01b03166306fdde036040518163ffffffff1660e01b8152600401600060405180830381865afa1580156200036e573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f1916820160405262000398919081019062000688565b805190915015620003ad575060019392505050565b5060009392505050565b80516001600160a01b0381168114620003cf57600080fd5b919050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112620003fc57600080fd5b81516001600160401b0380821115620004195762000419620003d4565b604051601f8301601f19908116603f01168101908282118183101715620004445762000444620003d4565b816040528381526020925086838588010111156200046157600080fd5b600091505b8382101562000485578582018301518183018401529082019062000466565b600093810190920192909252949350505050565b600080600080600060a08688031215620004b257600080fd5b620004bd86620003b7565b94506020860151935060408601519250620004db60608701620003b7565b60808701519092506001600160401b03811115620004f857600080fd5b6200050688828901620003ea565b9150509295509295909350565b600181811c908216806200052857607f821691505b6020821081036200054957634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200059d57600081815260208120601f850160051c81016020861015620005785750805b601f850160051c820191505b81811015620005995782815560010162000584565b5050505b505050565b81516001600160401b03811115620005be57620005be620003d4565b620005d681620005cf845462000513565b846200054f565b602080601f8311600181146200060e5760008415620005f55750858301515b600019600386901b1c1916600185901b17855562000599565b600085815260208120601f198616915b828110156200063f578886015182559484019460019091019084016200061e565b50858210156200065e5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6000602082840312156200068157600080fd5b5051919050565b6000602082840312156200069b57600080fd5b81516001600160401b03811115620006b257600080fd5b620006c084828501620003ea565b949350505050565b61161580620006d86000396000f3fe608060405234801561001057600080fd5b50600436106101c45760003560e01c8063715018a6116100f9578063b2d5ae4411610097578063d42c48d111610071578063d42c48d1146103e8578063f2fde38b1461041e578063f851a44014610431578063fc0c546a1461044457600080fd5b8063b2d5ae44146103c4578063c1cbbca7146103cc578063c59ee1dc146103df57600080fd5b8063937e09b1116100d3578063937e09b11461037c5780639d76ea58146103855780639fb42b1f14610398578063a43b6fff146103a157600080fd5b8063715018a61461033e57806381d12c58146103465780638da5cb5b1461036b57600080fd5b8063379248e2116101665780634cb6f4a5116101405780634cb6f4a5146102e35780634e260f6f146102f657806355e1dedf146102ff5780635b8575a91461031257600080fd5b8063379248e2146102a257806340193883146102c55780634a79d50c146102ce57600080fd5b8063232af255116101a2578063232af2551461022b57806329dcb0cf146102565780632f0829f71461025f57806334b664601461028257600080fd5b806312065fe0146101c9578063180b7138146101e45780631b16bb5c14610216575b600080fd5b6101d1610457565b6040519081526020015b60405180910390f35b6101f76101f23660046111c9565b6104ce565b604080516001600160a01b0390931683526020830191909152016101db565b610229610224366004611214565b610734565b005b60035461023e906001600160a01b031681565b6040516001600160a01b0390911681526020016101db565b6101d160085481565b6101d161026d3660046111c9565b6000908152600d602052604090206002015490565b6101d16102903660046112df565b600e6020526000908152604090205481565b6101d16102b03660046111c9565b6000908152600d602052604090206005015490565b6101d160095481565b6102d66107eb565b6040516101db9190611347565b6101d16102f13660046111c9565b610879565b6101d160065481565b6102d661030d3660046111c9565b610b25565b61023e6103203660046111c9565b6000908152600d60205260409020600101546001600160a01b031690565b610229610bc7565b6103596103543660046111c9565b610bdb565b6040516101db9695949392919061135a565b6000546001600160a01b031661023e565b6101d160075481565b60025461023e906001600160a01b031681565b6101d1600c5481565b6101d16103af3660046111c9565b6000908152600d602052604090206004015490565b6101d1610ca5565b6102296103da3660046111c9565b610e87565b6101d1600a5481565b61040e6103f63660046111c9565b6000908152600d602052604090206003015460ff1690565b60405190151581526020016101db565b61022961042c3660046112df565b6110a6565b60015461023e906001600160a01b031681565b60045461023e906001600160a01b031681565b600480546040516370a0823160e01b815230928101929092526000916001600160a01b03909116906370a0823190602401602060405180830381865afa1580156104a5573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104c9919061139f565b905090565b6000806104d961111f565b600954600a54101561054a5760405162461bcd60e51b815260206004820152602f60248201527f726169736564416d6f756e74206d757374206265206d6f7265207468616e206f60448201526e1c88195c5d585b081d1bc819dbd85b608a1b60648201526084015b60405180910390fd5b6000838152600d60205260409020600381015460ff16156105ad5760405162461bcd60e51b815260206004820152601f60248201527f546865207265717565737420686173206265656e20636f6d706c657465642e006044820152606401610541565b600b54600590600a9081906105c290846113ce565b6105cc91906113eb565b83600501541161062e5760405162461bcd60e51b815260206004820152602760248201527f76616c7565206f6620766f746573206661696c20746f206d65657420726571756044820152661a5c995b595b9d60ca1b6064820152608401610541565b600183015460028401546004805460405163a9059cbb60e01b81526001600160a01b03948516928101839052602481018490529193169063a9059cbb906044016020604051808303816000875af115801561068d573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106b1919061140d565b5060038501805460ff19166001179055604080513381526001600160a01b0384166020820152908101829052606081018990527fe8865f6295f774cc1f85678223321412003260525398a1d46e5e884a0f694a739060800160405180910390a15050505060018101546002909101546001600160a01b0390911694909350915050565b61073c61111f565b600c80546000818152600d602052604081209192839161075b8361142f565b9091555081905061076c86826114d1565b506001810180546001600160a01b0319166001600160a01b0386161790556002810183905560038101805460ff19169055600060048201556040517f6fbbe1619a744707cabdefa8c81690a643c946e64fd595a1126aa6d4bc27bba1906107dc9084903390899089908990611591565b60405180910390a15050505050565b600580546107f890611448565b80601f016020809104026020016040519081016040528092919081815260200182805461082490611448565b80156108715780601f1061084657610100808354040283529160200191610871565b820191906000526020600020905b81548152906001019060200180831161085457829003601f168201915b505050505081565b600081600c5410156108cd5760405162461bcd60e51b815260206004820152601760248201527f5265717565737420646f6573206e6f742065786973742e0000000000000000006044820152606401610541565b6000546001600160a01b0316330361093b5760405162461bcd60e51b815260206004820152602b60248201527f596f75277265206e6f7420616c6c6f77656420746f20766f746520666f72207460448201526a1a1a5cc81c995c5d595cdd60aa1b6064820152608401610541565b336000908152600e60205260409020546109a25760405162461bcd60e51b815260206004820152602260248201527f596f75206d757374206265206120636f6e7472696275746f7220746f20766f74604482015261652160f01b6064820152608401610541565b6000828152600d60209081526040808320338452600681019092529091205460ff1615610a0a5760405162461bcd60e51b8152602060048201526016602482015275165bdd481a185d9948185b1c9958591e481d9bdd195960521b6044820152606401610541565b60018101546001600160a01b0316610a645760405162461bcd60e51b815260206004820152601e60248201527f5265717565737420686173206e6f74206265656e20696e6974696174656400006044820152606401610541565b600954600b54336000908152600e6020526040812054909291610a86916113ce565b610a9091906113eb565b905080826005016000828254610aa691906115cc565b9091555050600482018054906000610abd8361142f565b9091555050336000818152600684016020908152604091829020805460ff191660011790558151928352820186905281018290527fcd200ea5ac0e92a5a67c5c682507511ae3086a331d103afb7eea291ae430d5999060600160405180910390a19392505050565b6000818152600d60205260409020805460609190610b4290611448565b80601f0160208091040260200160405190810160405280929190818152602001828054610b6e90611448565b8015610bbb5780601f10610b9057610100808354040283529160200191610bbb565b820191906000526020600020905b815481529060010190602001808311610b9e57829003601f168201915b50505050509050919050565b610bcf61111f565b610bd96000611179565b565b600d60205260009081526040902080548190610bf690611448565b80601f0160208091040260200160405190810160405280929190818152602001828054610c2290611448565b8015610c6f5780601f10610c4457610100808354040283529160200191610c6f565b820191906000526020600020905b815481529060010190602001808311610c5257829003601f168201915b505050600184015460028501546003860154600487015460059097015495966001600160a01b039093169591945060ff16925086565b6000600854421015610cf95760405162461bcd60e51b815260206004820152601760248201527f446561646c696e6520686173206e6f74207061737365640000000000000000006044820152606401610541565b600954600a5410610d4c5760405162461bcd60e51b815260206004820152601f60248201527f52616973656420616d6f756e74206973206d6f7265207468616e20676f616c006044820152606401610541565b336000908152600e6020526040902054610da85760405162461bcd60e51b815260206004820152601c60248201527f596f75206e65656420746f206265206120636f6e7472696275746572000000006044820152606401610541565b336000818152600e6020526040808220805492905560048054915163a9059cbb60e01b8152908101939093526024830182905290916001600160a01b039091169063a9059cbb906044016020604051808303816000875af1158015610e11573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610e35919061140d565b50600454604080513381526001600160a01b03909216602083015281018290527ff7f394ee65e37c78e595ee4c8675e3832dff9a6cfc6a2c7405e3689f2a714c539060600160405180910390a1919050565b600754811015610ed95760405162461bcd60e51b815260206004820152601c60248201527f6d696e696d756d20636f6e747269627574696f6e206e6f74206d6574000000006044820152606401610541565b60048054604051636eb1769f60e11b8152339281019290925230602483015282916001600160a01b039091169063dd62ed3e90604401602060405180830381865afa158015610f2c573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610f50919061139f565b1015610f9e5760405162461bcd60e51b815260206004820152601860248201527f746f6b656e207370656e64206e6f7420617070726f76656400000000000000006044820152606401610541565b336000908152600e60205260408120549003610fbe576006805460010190555b600480546040516323b872dd60e01b81523392810192909252306024830152604482018390526001600160a01b0316906323b872dd906064016020604051808303816000875af1158015611016573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061103a919061140d565b50336000818152600e6020908152604091829020805485019055600a80548501905560025482519384526001600160a01b03169083015281018290527f630c9eb89085cfe993245c555e78df45b0b8e8f31f411ddc400727738d9648349060600160405180910390a150565b6110ae61111f565b6001600160a01b0381166111135760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152608401610541565b61111c81611179565b50565b6000546001600160a01b03163314610bd95760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152606401610541565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6000602082840312156111db57600080fd5b5035919050565b634e487b7160e01b600052604160045260246000fd5b80356001600160a01b038116811461120f57600080fd5b919050565b60008060006060848603121561122957600080fd5b833567ffffffffffffffff8082111561124157600080fd5b818601915086601f83011261125557600080fd5b813581811115611267576112676111e2565b604051601f8201601f19908116603f0116810190838211818310171561128f5761128f6111e2565b816040528281528960208487010111156112a857600080fd5b8260208601602083013760006020848301015280975050505050506112cf602085016111f8565b9150604084013590509250925092565b6000602082840312156112f157600080fd5b6112fa826111f8565b9392505050565b6000815180845260005b818110156113275760208185018101518683018201520161130b565b506000602082860101526020601f19601f83011685010191505092915050565b6020815260006112fa6020830184611301565b60c08152600061136d60c0830189611301565b6001600160a01b039790971660208301525060408101949094529115156060840152608083015260a090910152919050565b6000602082840312156113b157600080fd5b5051919050565b634e487b7160e01b600052601160045260246000fd5b80820281158282048414176113e5576113e56113b8565b92915050565b60008261140857634e487b7160e01b600052601260045260246000fd5b500490565b60006020828403121561141f57600080fd5b815180151581146112fa57600080fd5b600060018201611441576114416113b8565b5060010190565b600181811c9082168061145c57607f821691505b60208210810361147c57634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156114cc57600081815260208120601f850160051c810160208610156114a95750805b601f850160051c820191505b818110156114c8578281556001016114b5565b5050505b505050565b815167ffffffffffffffff8111156114eb576114eb6111e2565b6114ff816114f98454611448565b84611482565b602080601f831160018114611534576000841561151c5750858301515b600019600386901b1c1916600185901b1785556114c8565b600085815260208120601f198616915b8281101561156357888601518255948401946001909101908401611544565b50858210156115815787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b858152600060018060a01b03808716602084015260a060408401526115b960a0840187611301565b9416606083015250608001529392505050565b808201808211156113e5576113e56113b856fea2646970667358221220524fdf843d87411aaf248ea10b5e2f8140e098d445ee48e7c692a95a0e83159064736f6c63430008130033";

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

    public static final String FUNC_TITLE = "title";

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

    public static final Event CONTRIBUTEEVENT_EVENT = new Event("ContributeEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CREATEREQUESTEVENT_EVENT = new Event("CreateRequestEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event GETREFUNDEVENT_EVENT = new Event("GetRefundEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event RECEIVECONTRIBUTIONEVENT_EVENT = new Event("ReceiveContributionEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event VOTEREQUESTEVENT_EVENT = new Event("voteRequestEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
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

    public static List<ContributeEventEventResponse> getContributeEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CONTRIBUTEEVENT_EVENT, transactionReceipt);
        ArrayList<ContributeEventEventResponse> responses = new ArrayList<ContributeEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContributeEventEventResponse typedResponse = new ContributeEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._tokenAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
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
                typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._tokenAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContributeEventEventResponse> contributeEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRIBUTEEVENT_EVENT));
        return contributeEventEventFlowable(filter);
    }

    public static List<CreateRequestEventEventResponse> getCreateRequestEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CREATEREQUESTEVENT_EVENT, transactionReceipt);
        ArrayList<CreateRequestEventEventResponse> responses = new ArrayList<CreateRequestEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CreateRequestEventEventResponse typedResponse = new CreateRequestEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.requestNum = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._projectCreator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._title = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
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
                typedResponse.requestNum = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._projectCreator = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._title = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CreateRequestEventEventResponse> createRequestEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CREATEREQUESTEVENT_EVENT));
        return createRequestEventEventFlowable(filter);
    }

    public static List<GetRefundEventEventResponse> getGetRefundEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GETREFUNDEVENT_EVENT, transactionReceipt);
        ArrayList<GetRefundEventEventResponse> responses = new ArrayList<GetRefundEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GetRefundEventEventResponse typedResponse = new GetRefundEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._RefundToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._RefundAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GetRefundEventEventResponse> getRefundEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GetRefundEventEventResponse>() {
            @Override
            public GetRefundEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(GETREFUNDEVENT_EVENT, log);
                GetRefundEventEventResponse typedResponse = new GetRefundEventEventResponse();
                typedResponse.log = log;
                typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._RefundToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._RefundAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GetRefundEventEventResponse> getRefundEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GETREFUNDEVENT_EVENT));
        return getRefundEventEventFlowable(filter);
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

    public static List<ReceiveContributionEventEventResponse> getReceiveContributionEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(RECEIVECONTRIBUTIONEVENT_EVENT, transactionReceipt);
        ArrayList<ReceiveContributionEventEventResponse> responses = new ArrayList<ReceiveContributionEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ReceiveContributionEventEventResponse typedResponse = new ReceiveContributionEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._projectCreator = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._requestNo = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReceiveContributionEventEventResponse> receiveContributionEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReceiveContributionEventEventResponse>() {
            @Override
            public ReceiveContributionEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(RECEIVECONTRIBUTIONEVENT_EVENT, log);
                ReceiveContributionEventEventResponse typedResponse = new ReceiveContributionEventEventResponse();
                typedResponse.log = log;
                typedResponse._projectCreator = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._recipient = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._requestNo = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReceiveContributionEventEventResponse> receiveContributionEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RECEIVECONTRIBUTIONEVENT_EVENT));
        return receiveContributionEventEventFlowable(filter);
    }

    public static List<VoteRequestEventEventResponse> getVoteRequestEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTEREQUESTEVENT_EVENT, transactionReceipt);
        ArrayList<VoteRequestEventEventResponse> responses = new ArrayList<VoteRequestEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteRequestEventEventResponse typedResponse = new VoteRequestEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._voter = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._requestNo = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._valueOfVote = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteRequestEventEventResponse> voteRequestEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteRequestEventEventResponse>() {
            @Override
            public VoteRequestEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTEREQUESTEVENT_EVENT, log);
                VoteRequestEventEventResponse typedResponse = new VoteRequestEventEventResponse();
                typedResponse.log = log;
                typedResponse._voter = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._requestNo = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._valueOfVote = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteRequestEventEventResponse> voteRequestEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTEREQUESTEVENT_EVENT));
        return voteRequestEventEventFlowable(filter);
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

    public RemoteFunctionCall<String> title() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TITLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteFunctionCall<TransactionReceipt> contribute(BigInteger _amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONTRIBUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> getRefund() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETREFUND, 
                Arrays.<Type>asList(), 
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

    public RemoteFunctionCall<TransactionReceipt> voteRequest(BigInteger _requestNo) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTEREQUEST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_requestNo)), 
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

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _crowdfundingFactory, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_crowdfundingFactory), 
                new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class ContributeEventEventResponse extends BaseEventResponse {
        public String _contributor;

        public String _tokenAddress;

        public BigInteger _amount;
    }

    public static class CreateRequestEventEventResponse extends BaseEventResponse {
        public BigInteger requestNum;

        public String _projectCreator;

        public String _title;

        public String _recipient;

        public BigInteger _amount;
    }

    public static class GetRefundEventEventResponse extends BaseEventResponse {
        public String _contributor;

        public String _RefundToken;

        public BigInteger _RefundAmount;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class ReceiveContributionEventEventResponse extends BaseEventResponse {
        public String _projectCreator;

        public String _recipient;

        public BigInteger _amount;

        public BigInteger _requestNo;
    }

    public static class VoteRequestEventEventResponse extends BaseEventResponse {
        public String _voter;

        public BigInteger _requestNo;

        public BigInteger _valueOfVote;
    }
}
