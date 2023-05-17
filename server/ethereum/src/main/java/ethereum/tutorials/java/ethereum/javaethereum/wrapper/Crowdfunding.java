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
    public static final String BINARY = "0x6080604052620f4240600a553480156200001857600080fd5b5060405162001f2438038062001f248339810160408190526200003b9162000457565b620000463362000166565b4283116200005357600080fd5b6200005e82620001b6565b620000b05760405162461bcd60e51b815260206004820152601d60248201527f416464726573732070726f7669646564206973206e6f7420746f6b656e00000060448201526064015b60405180910390fd5b813b6200010b5760405162461bcd60e51b815260206004820152602260248201527f416464726573732070726f7669646564206973206e6f74206120636f6e74726160448201526118dd60f21b6064820152608401620000a7565b60046200011982826200055b565b5050600892909255600755606460065560018054336001600160a01b0319918216179091556003805482166001600160a01b03909316928317905560028054909116909117905562000681565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6040516370a0823160e01b8152336004820152600090829082906001600160a01b038316906370a0823190602401602060405180830381865afa15801562000202573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019062000228919062000627565b1015620002785760405162461bcd60e51b815260206004820152601460248201527f6d6574686f6420646f65736e27742065786973740000000000000000000000006044820152606401620000a7565b6000816001600160a01b03166318160ddd6040518163ffffffff1660e01b8152600401602060405180830381865afa158015620002b9573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190620002df919062000627565b116200031c5760405162461bcd60e51b815260206004820152600b60248201526a0537570706c7920697320360ac1b6044820152606401620000a7565b6000816001600160a01b03166306fdde036040518163ffffffff1660e01b8152600401600060405180830381865afa1580156200035d573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f1916820160405262000387919081019062000641565b511515949350505050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112620003ba57600080fd5b81516001600160401b0380821115620003d757620003d762000392565b604051601f8301601f19908116603f0116810190828211818310171562000402576200040262000392565b816040528381526020925086838588010111156200041f57600080fd5b600091505b8382101562000443578582018301518183018401529082019062000424565b600093810190920192909252949350505050565b600080600080608085870312156200046e57600080fd5b84516020860151604087015191955093506001600160a01b03811681146200049557600080fd5b60608601519092506001600160401b03811115620004b257600080fd5b620004c087828801620003a8565b91505092959194509250565b600181811c90821680620004e157607f821691505b6020821081036200050257634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200055657600081815260208120601f850160051c81016020861015620005315750805b601f850160051c820191505b8181101562000552578281556001016200053d565b5050505b505050565b81516001600160401b0381111562000577576200057762000392565b6200058f81620005888454620004cc565b8462000508565b602080601f831160018114620005c75760008415620005ae5750858301515b600019600386901b1c1916600185901b17855562000552565b600085815260208120601f198616915b82811015620005f857888601518255948401946001909101908401620005d7565b5085821015620006175787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6000602082840312156200063a57600080fd5b5051919050565b6000602082840312156200065457600080fd5b81516001600160401b038111156200066b57600080fd5b6200067984828501620003a8565b949350505050565b61189380620006916000396000f3fe608060405234801561001057600080fd5b50600436106101da5760003560e01c80635b8575a911610104578063a43b6fff116100a2578063d42c48d111610071578063d42c48d11461042b578063f2fde38b14610451578063f851a44014610464578063fc0c546a1461047757600080fd5b8063a43b6fff146103e4578063b2d5ae4414610407578063c1cbbca71461040f578063c59ee1dc1461042257600080fd5b80638da5cb5b116100de5780638da5cb5b146103ae578063937e09b1146103bf5780639d76ea58146103c85780639fb42b1f146103db57600080fd5b80635b8575a91461033d578063715018a61461038157806381d12c581461038957600080fd5b8063313ce5671161017c5780634a79d50c1161014b5780634a79d50c146102f95780634cb6f4a51461030e5780634e260f6f1461032157806355e1dedf1461032a57600080fd5b8063313ce567146102a457806334b66460146102ad578063379248e2146102cd57806340193883146102f057600080fd5b806319f37361116101b857806319f37361146102505780631b16bb5c1461026357806329dcb0cf146102785780632f0829f71461028157600080fd5b806312065fe0146101df57806316279055146101fa578063180b71381461021e575b600080fd5b6101e761048a565b6040519081526020015b60405180910390f35b61020e6102083660046113ba565b3b151590565b60405190151581526020016101f1565b61023161022c3660046113dc565b6104fc565b604080516001600160a01b0390931683526020830191909152016101f1565b61020e61025e3660046113ba565b61076a565b610276610271366004611464565b610930565b005b6101e760075481565b6101e761028f3660046113dc565b6000908152600c602052604090206002015490565b6101e7600a5481565b6101e76102bb3660046113ba565b600d6020526000908152604090205481565b6101e76102db3660046113dc565b6000908152600c602052604090206005015490565b6101e760085481565b6103016109e7565b6040516101f1919061154e565b61027661031c3660046113dc565b610a75565b6101e760055481565b6103016103383660046113dc565b610d00565b61036961034b3660046113dc565b6000908152600c60205260409020600101546001600160a01b031690565b6040516001600160a01b0390911681526020016101f1565b610276610da2565b61039c6103973660046113dc565b610db6565b6040516101f196959493929190611561565b6000546001600160a01b0316610369565b6101e760065481565b600254610369906001600160a01b031681565b6101e7600b5481565b6101e76103f23660046113dc565b6000908152600c602052604090206004015490565b6101e7610e80565b61027661041d3660046113dc565b611062565b6101e760095481565b61020e6104393660046113dc565b6000908152600c602052604090206003015460ff1690565b61027661045f3660046113ba565b61127b565b600154610369906001600160a01b031681565b600354610369906001600160a01b031681565b6003546040516370a0823160e01b81523060048201526000916001600160a01b0316906370a0823190602401602060405180830381865afa1580156104d3573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104f791906115a6565b905090565b6000806105076112f4565b60085460095410156105785760405162461bcd60e51b815260206004820152602f60248201527f726169736564416d6f756e74206d757374206265206d6f7265207468616e206f60448201526e1c88195c5d585b081d1bc819dbd85b608a1b60648201526084015b60405180910390fd5b6000838152600c60205260409020600381015460ff16156105db5760405162461bcd60e51b815260206004820152601f60248201527f546865207265717565737420686173206265656e20636f6d706c657465642e00604482015260640161056f565b600a546105e99060026115d5565b600a5482600501546008546105fe91906115f2565b61060891906115d5565b106106655760405162461bcd60e51b815260206004820152602760248201527f76616c7565206f6620766f746573206661696c20746f206d65657420726571756044820152661a5c995b595b9d60ca1b606482015260840161056f565b6001810154600282015460035460405163a9059cbb60e01b81526001600160a01b039384166004820181905260248201849052939091169063a9059cbb906044016020604051808303816000875af11580156106c5573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106e99190611614565b5060038301805460ff19166001179055604080513381526001600160a01b0384166020820152908101829052606081018790527fe8865f6295f774cc1f85678223321412003260525398a1d46e5e884a0f694a739060800160405180910390a1505060018101546002909101546001600160a01b0390911692509050915091565b6040516370a0823160e01b8152336004820152600090829082906001600160a01b038316906370a0823190602401602060405180830381865afa1580156107b5573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906107d991906115a6565b101561081e5760405162461bcd60e51b81526020600482015260146024820152731b595d1a1bd908191bd95cdb89dd08195e1a5cdd60621b604482015260640161056f565b6000816001600160a01b03166318160ddd6040518163ffffffff1660e01b8152600401602060405180830381865afa15801561085e573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061088291906115a6565b116108bd5760405162461bcd60e51b815260206004820152600b60248201526a0537570706c7920697320360ac1b604482015260640161056f565b6000816001600160a01b03166306fdde036040518163ffffffff1660e01b8152600401600060405180830381865afa1580156108fd573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f191682016040526109259190810190611636565b511515949350505050565b6109386112f4565b600b80546000818152600c6020526040812091928391610957836116ad565b90915550819050610968868261174f565b506001810180546001600160a01b0319166001600160a01b0386161790556002810183905560038101805460ff19169055600060048201556040517f6fbbe1619a744707cabdefa8c81690a643c946e64fd595a1126aa6d4bc27bba1906109d8908490339089908990899061180f565b60405180910390a15050505050565b600480546109f4906116c6565b80601f0160208091040260200160405190810160405280929190818152602001828054610a20906116c6565b8015610a6d5780601f10610a4257610100808354040283529160200191610a6d565b820191906000526020600020905b815481529060010190602001808311610a5057829003601f168201915b505050505081565b80600b541015610ac75760405162461bcd60e51b815260206004820152601760248201527f5265717565737420646f6573206e6f742065786973742e000000000000000000604482015260640161056f565b6000546001600160a01b03163303610b355760405162461bcd60e51b815260206004820152602b60248201527f596f75277265206e6f7420616c6c6f77656420746f20766f746520666f72207460448201526a1a1a5cc81c995c5d595cdd60aa1b606482015260840161056f565b336000908152600d6020526040902054610b9c5760405162461bcd60e51b815260206004820152602260248201527f596f75206d757374206265206120636f6e7472696275746f7220746f20766f74604482015261652160f01b606482015260840161056f565b6000818152600c60209081526040808320338452600681019092529091205460ff1615610c045760405162461bcd60e51b8152602060048201526016602482015275165bdd481a185d9948185b1c9958591e481d9bdd195960521b604482015260640161056f565b60018101546001600160a01b0316610c5e5760405162461bcd60e51b815260206004820152601e60248201527f5265717565737420686173206e6f74206265656e20696e697469617465640000604482015260640161056f565b336000908152600d602052604081205460058301805491928392610c8390849061184a565b9091555050600482018054906000610c9a836116ad565b9091555050336000818152600684016020908152604091829020805460ff191660011790558151928352820185905281018290527fcd200ea5ac0e92a5a67c5c682507511ae3086a331d103afb7eea291ae430d5999060600160405180910390a1505050565b6000818152600c60205260409020805460609190610d1d906116c6565b80601f0160208091040260200160405190810160405280929190818152602001828054610d49906116c6565b8015610d965780601f10610d6b57610100808354040283529160200191610d96565b820191906000526020600020905b815481529060010190602001808311610d7957829003601f168201915b50505050509050919050565b610daa6112f4565b610db4600061134e565b565b600c60205260009081526040902080548190610dd1906116c6565b80601f0160208091040260200160405190810160405280929190818152602001828054610dfd906116c6565b8015610e4a5780601f10610e1f57610100808354040283529160200191610e4a565b820191906000526020600020905b815481529060010190602001808311610e2d57829003601f168201915b505050600184015460028501546003860154600487015460059097015495966001600160a01b039093169591945060ff16925086565b6000600754421015610ed45760405162461bcd60e51b815260206004820152601760248201527f446561646c696e6520686173206e6f7420706173736564000000000000000000604482015260640161056f565b60085460095410610f275760405162461bcd60e51b815260206004820152601f60248201527f52616973656420616d6f756e74206973206d6f7265207468616e20676f616c00604482015260640161056f565b336000908152600d6020526040902054610f835760405162461bcd60e51b815260206004820152601c60248201527f596f75206e65656420746f206265206120636f6e747269627574657200000000604482015260640161056f565b336000818152600d60205260408082208054929055600354905163a9059cbb60e01b815260048101939093526024830182905290916001600160a01b039091169063a9059cbb906044016020604051808303816000875af1158015610fec573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906110109190611614565b50600354604080513381526001600160a01b03909216602083015281018290527ff7f394ee65e37c78e595ee4c8675e3832dff9a6cfc6a2c7405e3689f2a714c539060600160405180910390a1919050565b6006548110156110b45760405162461bcd60e51b815260206004820152601c60248201527f6d696e696d756d20636f6e747269627574696f6e206e6f74206d657400000000604482015260640161056f565b600354604051636eb1769f60e11b815233600482015230602482015282916001600160a01b03169063dd62ed3e90604401602060405180830381865afa158015611102573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061112691906115a6565b10156111745760405162461bcd60e51b815260206004820152601860248201527f746f6b656e207370656e64206e6f7420617070726f7665640000000000000000604482015260640161056f565b336000908152600d60205260408120549003611194576005805460010190555b6003546040516323b872dd60e01b8152336004820152306024820152604481018390526001600160a01b03909116906323b872dd906064016020604051808303816000875af11580156111eb573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061120f9190611614565b50336000818152600d6020908152604091829020805485019055600980548501905560025482519384526001600160a01b03169083015281018290527f630c9eb89085cfe993245c555e78df45b0b8e8f31f411ddc400727738d9648349060600160405180910390a150565b6112836112f4565b6001600160a01b0381166112e85760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b606482015260840161056f565b6112f18161134e565b50565b6000546001600160a01b03163314610db45760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572604482015260640161056f565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80356001600160a01b03811681146113b557600080fd5b919050565b6000602082840312156113cc57600080fd5b6113d58261139e565b9392505050565b6000602082840312156113ee57600080fd5b5035919050565b634e487b7160e01b600052604160045260246000fd5b604051601f8201601f1916810167ffffffffffffffff81118282101715611434576114346113f5565b604052919050565b600067ffffffffffffffff821115611456576114566113f5565b50601f01601f191660200190565b60008060006060848603121561147957600080fd5b833567ffffffffffffffff81111561149057600080fd5b8401601f810186136114a157600080fd5b80356114b46114af8261143c565b61140b565b8181528760208385010111156114c957600080fd5b816020840160208301376000602083830101528095505050506114ee6020850161139e565b9150604084013590509250925092565b60005b83811015611519578181015183820152602001611501565b50506000910152565b6000815180845261153a8160208601602086016114fe565b601f01601f19169290920160200192915050565b6020815260006113d56020830184611522565b60c08152600061157460c0830189611522565b6001600160a01b039790971660208301525060408101949094529115156060840152608083015260a090910152919050565b6000602082840312156115b857600080fd5b5051919050565b634e487b7160e01b600052601160045260246000fd5b80820281158282048414176115ec576115ec6115bf565b92915050565b60008261160f57634e487b7160e01b600052601260045260246000fd5b500490565b60006020828403121561162657600080fd5b815180151581146113d557600080fd5b60006020828403121561164857600080fd5b815167ffffffffffffffff81111561165f57600080fd5b8201601f8101841361167057600080fd5b805161167e6114af8261143c565b81815285602083850101111561169357600080fd5b6116a48260208301602086016114fe565b95945050505050565b6000600182016116bf576116bf6115bf565b5060010190565b600181811c908216806116da57607f821691505b6020821081036116fa57634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561174a57600081815260208120601f850160051c810160208610156117275750805b601f850160051c820191505b8181101561174657828155600101611733565b5050505b505050565b815167ffffffffffffffff811115611769576117696113f5565b61177d8161177784546116c6565b84611700565b602080601f8311600181146117b2576000841561179a5750858301515b600019600386901b1c1916600185901b178555611746565b600085815260208120601f198616915b828110156117e1578886015182559484019460019091019084016117c2565b50858210156117ff5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b858152600060018060a01b03808716602084015260a0604084015261183760a0840187611522565b9416606083015250608001529392505050565b808201808211156115ec576115ec6115bf56fea264697066735822122057f275efb836cef319015184384368922d198c4b493d66284a1f0cbf68f12a7864736f6c63430008130033";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_CONTRIBUTERS = "contributers";

    public static final String FUNC_DEADLINE = "deadline";

    public static final String FUNC_DECIMALS = "decimals";

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

    public static final String FUNC_ISCONTRACT = "isContract";

    public static final String FUNC_ISTOKEN = "isToken";

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

    public RemoteFunctionCall<BigInteger> deadline() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEADLINE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS, 
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

    public RemoteFunctionCall<Boolean> isContract(String _address) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isToken(String _address) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_goal), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_acceptingThisToken), 
                new org.web3j.abi.datatypes.Utf8String(_title)));
        return deployRemoteCall(Crowdfunding.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crowdfunding> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _goal, BigInteger _deadline, String _acceptingThisToken, String _title) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_goal), 
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
