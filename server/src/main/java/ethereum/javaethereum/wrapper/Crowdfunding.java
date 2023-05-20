package ethereum.javaethereum.wrapper;

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
    public static final String BINARY = "0x60806040523480156200001157600080fd5b5060405162001f1338038062001f1383398101604081905262000034916200048f565b6200003f336200019e565b428311620000945760405162461bcd60e51b815260206004820152601e60248201527f446561646c696e65206d75737420626520696e2074686520667574757265000060448201526064015b60405180910390fd5b6200009f82620001ee565b620000ed5760405162461bcd60e51b815260206004820152601d60248201527f416464726573732070726f7669646564206973206e6f7420746f6b656e00000060448201526064016200008b565b813b620001485760405162461bcd60e51b815260206004820152602260248201527f416464726573732070726f7669646564206973206e6f74206120636f6e74726160448201526118dd60f21b60648201526084016200008b565b600462000156828262000593565b505060079290925560065560018054336001600160a01b0319918216179091556003805482166001600160a01b039093169283179055600280549091169091179055620006b9565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6040516370a0823160e01b8152336004820152600090829082906001600160a01b038316906370a0823190602401602060405180830381865afa1580156200023a573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906200026091906200065f565b1015620002b05760405162461bcd60e51b815260206004820152601460248201527f6d6574686f6420646f65736e277420657869737400000000000000000000000060448201526064016200008b565b6000816001600160a01b03166318160ddd6040518163ffffffff1660e01b8152600401602060405180830381865afa158015620002f1573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906200031791906200065f565b11620003545760405162461bcd60e51b815260206004820152600b60248201526a0537570706c7920697320360ac1b60448201526064016200008b565b6000816001600160a01b03166306fdde036040518163ffffffff1660e01b8152600401600060405180830381865afa15801562000395573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f19168201604052620003bf919081019062000679565b511515949350505050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112620003f257600080fd5b81516001600160401b03808211156200040f576200040f620003ca565b604051601f8301601f19908116603f011681019082821181831017156200043a576200043a620003ca565b816040528381526020925086838588010111156200045757600080fd5b600091505b838210156200047b57858201830151818301840152908201906200045c565b600093810190920192909252949350505050565b60008060008060808587031215620004a657600080fd5b84516020860151604087015191955093506001600160a01b0381168114620004cd57600080fd5b60608601519092506001600160401b03811115620004ea57600080fd5b620004f887828801620003e0565b91505092959194509250565b600181811c908216806200051957607f821691505b6020821081036200053a57634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200058e57600081815260208120601f850160051c81016020861015620005695750805b601f850160051c820191505b818110156200058a5782815560010162000575565b5050505b505050565b81516001600160401b03811115620005af57620005af620003ca565b620005c781620005c0845462000504565b8462000540565b602080601f831160018114620005ff5760008415620005e65750858301515b600019600386901b1c1916600185901b1785556200058a565b600085815260208120601f198616915b8281101562000630578886015182559484019460019091019084016200060f565b50858210156200064f5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6000602082840312156200067257600080fd5b5051919050565b6000602082840312156200068c57600080fd5b81516001600160401b03811115620006a357600080fd5b620006b184828501620003e0565b949350505050565b61184a80620006c96000396000f3fe608060405234801561001057600080fd5b50600436106101cf5760003560e01c8063715018a611610104578063c1cbbca7116100a2578063f2fde38b11610071578063f2fde38b14610432578063f851a44014610445578063f8b3a2c414610458578063fc0c546a1461046157600080fd5b8063c1cbbca7146103ea578063c59ee1dc146103fd578063d42c48d114610406578063ee6f5afc1461042c57600080fd5b80639d76ea58116100de5780639d76ea58146103a35780639fb42b1f146103b6578063a43b6fff146103bf578063b2d5ae44146103e257600080fd5b8063715018a61461036557806381d12c581461036d5780638da5cb5b1461039257600080fd5b8063379248e2116101715780634cb6f4a51161014b5780634cb6f4a5146102f25780634e260f6f1461030557806355e1dedf1461030e5780635b8575a91461032157600080fd5b8063379248e2146102b157806340193883146102d45780634a79d50c146102dd57600080fd5b80631b16bb5c116101ad5780631b16bb5c146102425780631f6d49421461025757806329dcb0cf146102855780632f0829f71461028e57600080fd5b806316279055146101d4578063180b7138146101fd57806319f373611461022f575b600080fd5b6101e86101e2366004611388565b3b151590565b60405190151581526020015b60405180910390f35b61021061020b3660046113aa565b610474565b604080516001600160a01b0390931683526020830191909152016101f4565b6101e861023d366004611388565b6106cf565b610255610250366004611432565b610895565b005b610277610265366004611388565b600c6020526000908152604090205481565b6040519081526020016101f4565b61027760065481565b61027761029c3660046113aa565b6000908152600b602052604090206002015490565b6102776102bf3660046113aa565b6000908152600b602052604090206005015490565b61027760075481565b6102e5610a00565b6040516101f4919061151c565b6102556103003660046113aa565b610a8e565b61027760055481565b6102e561031c3660046113aa565b610d20565b61034d61032f3660046113aa565b6000908152600b60205260409020600101546001600160a01b031690565b6040516001600160a01b0390911681526020016101f4565b610255610dc2565b61038061037b3660046113aa565b610dd6565b6040516101f49695949392919061152f565b6000546001600160a01b031661034d565b60025461034d906001600160a01b031681565b610277600a5481565b6102776103cd3660046113aa565b6000908152600b602052604090206004015490565b610277610ea0565b6102556103f83660046113aa565b611082565b61027760085481565b6101e86104143660046113aa565b6000908152600b602052604090206003015460ff1690565b42610277565b610255610440366004611388565b611249565b60015461034d906001600160a01b031681565b61027760095481565b60035461034d906001600160a01b031681565b60008061047f6112c2565b60075460085410156104f05760405162461bcd60e51b815260206004820152602f60248201527f726169736564416d6f756e74206d757374206265206d6f7265207468616e206f60448201526e1c88195c5d585b081d1bc819dbd85b608a1b60648201526084015b60405180910390fd5b6000838152600b60205260409020600381015460ff16156105535760405162461bcd60e51b815260206004820152601f60248201527f546865207265717565737420686173206265656e20636f6d706c657465642e0060448201526064016104e7565b60008160050154600754610567919061158a565b9050600281106105c95760405162461bcd60e51b815260206004820152602760248201527f76616c7565206f6620766f746573206661696c20746f206d65657420726571756044820152661a5c995b595b9d60ca1b60648201526084016104e7565b6001820154600283015460035460405163a9059cbb60e01b81526001600160a01b039384166004820181905260248201849052939091169063a9059cbb906044016020604051808303816000875af1158015610629573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061064d91906115ac565b5060038401805460ff19166001179055604080513381526001600160a01b0384166020820152908101829052606081018890527fe8865f6295f774cc1f85678223321412003260525398a1d46e5e884a0f694a739060800160405180910390a150505060018101546002909101546001600160a01b0390911692509050915091565b6040516370a0823160e01b8152336004820152600090829082906001600160a01b038316906370a0823190602401602060405180830381865afa15801561071a573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061073e91906115ce565b10156107835760405162461bcd60e51b81526020600482015260146024820152731b595d1a1bd908191bd95cdb89dd08195e1a5cdd60621b60448201526064016104e7565b6000816001600160a01b03166318160ddd6040518163ffffffff1660e01b8152600401602060405180830381865afa1580156107c3573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906107e791906115ce565b116108225760405162461bcd60e51b815260206004820152600b60248201526a0537570706c7920697320360ac1b60448201526064016104e7565b6000816001600160a01b03166306fdde036040518163ffffffff1660e01b8152600401600060405180830381865afa158015610862573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f1916820160405261088a91908101906115e7565b511515949350505050565b61089d6112c2565b6008541561093857600854816009546108b6919061165e565b11156109385760405162461bcd60e51b815260206004820152604560248201527f54686973207472616e73616374696f6e2077696c6c20636175736520746f746160448201527f6c52657175657374416d6f756e7420746f2065786365656420726169736564416064820152641b5bdd5b9d60da1b608482015260a4016104e7565b600a80546000818152600b602052604081209192839161095783611677565b909155508190506109688682611719565b506001810180546001600160a01b0319166001600160a01b0386161790556002810183905560038101805460ff19169055600060048201819055600980548592906109b490849061165e565b90915550506040517f6fbbe1619a744707cabdefa8c81690a643c946e64fd595a1126aa6d4bc27bba1906109f190849033908990899089906117d9565b60405180910390a15050505050565b60048054610a0d90611690565b80601f0160208091040260200160405190810160405280929190818152602001828054610a3990611690565b8015610a865780601f10610a5b57610100808354040283529160200191610a86565b820191906000526020600020905b815481529060010190602001808311610a6957829003601f168201915b505050505081565b80600a541015610ae05760405162461bcd60e51b815260206004820152601760248201527f5265717565737420646f6573206e6f742065786973742e00000000000000000060448201526064016104e7565b6000546001600160a01b03163303610b555760405162461bcd60e51b815260206004820152603260248201527f417320746865206f776e65722c206e6f7420616c6c6f77656420746f20766f746044820152711948199bdc881d1a1a5cc81c995c5d595cdd60721b60648201526084016104e7565b336000908152600c6020526040902054610bbc5760405162461bcd60e51b815260206004820152602260248201527f596f75206d757374206265206120636f6e7472696275746f7220746f20766f74604482015261652160f01b60648201526084016104e7565b6000818152600b60209081526040808320338452600681019092529091205460ff1615610c245760405162461bcd60e51b8152602060048201526016602482015275165bdd481a185d9948185b1c9958591e481d9bdd195960521b60448201526064016104e7565b60018101546001600160a01b0316610c7e5760405162461bcd60e51b815260206004820152601e60248201527f5265717565737420686173206e6f74206265656e20696e69746961746564000060448201526064016104e7565b336000908152600c602052604081205460058301805491928392610ca390849061165e565b9091555050600482018054906000610cba83611677565b9091555050336000818152600684016020908152604091829020805460ff191660011790558151928352820185905281018290527fcd200ea5ac0e92a5a67c5c682507511ae3086a331d103afb7eea291ae430d5999060600160405180910390a1505050565b6000818152600b60205260409020805460609190610d3d90611690565b80601f0160208091040260200160405190810160405280929190818152602001828054610d6990611690565b8015610db65780601f10610d8b57610100808354040283529160200191610db6565b820191906000526020600020905b815481529060010190602001808311610d9957829003601f168201915b50505050509050919050565b610dca6112c2565b610dd4600061131c565b565b600b60205260009081526040902080548190610df190611690565b80601f0160208091040260200160405190810160405280929190818152602001828054610e1d90611690565b8015610e6a5780601f10610e3f57610100808354040283529160200191610e6a565b820191906000526020600020905b815481529060010190602001808311610e4d57829003601f168201915b505050600184015460028501546003860154600487015460059097015495966001600160a01b039093169591945060ff16925086565b6000600654421015610ef45760405162461bcd60e51b815260206004820152601760248201527f446561646c696e6520686173206e6f742070617373656400000000000000000060448201526064016104e7565b60075460085410610f475760405162461bcd60e51b815260206004820152601f60248201527f52616973656420616d6f756e74206973206d6f7265207468616e20676f616c0060448201526064016104e7565b336000908152600c6020526040902054610fa35760405162461bcd60e51b815260206004820152601c60248201527f596f75206e65656420746f206265206120636f6e7472696275746f720000000060448201526064016104e7565b336000818152600c60205260408082208054929055600354905163a9059cbb60e01b815260048101939093526024830182905290916001600160a01b039091169063a9059cbb906044016020604051808303816000875af115801561100c573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061103091906115ac565b50600354604080513381526001600160a01b03909216602083015281018290527ff7f394ee65e37c78e595ee4c8675e3832dff9a6cfc6a2c7405e3689f2a714c539060600160405180910390a1919050565b600354604051636eb1769f60e11b815233600482015230602482015282916001600160a01b03169063dd62ed3e90604401602060405180830381865afa1580156110d0573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906110f491906115ce565b10156111425760405162461bcd60e51b815260206004820152601860248201527f746f6b656e207370656e64206e6f7420617070726f766564000000000000000060448201526064016104e7565b336000908152600c60205260408120549003611162576005805460010190555b6003546040516323b872dd60e01b8152336004820152306024820152604481018390526001600160a01b03909116906323b872dd906064016020604051808303816000875af11580156111b9573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906111dd91906115ac565b50336000818152600c6020908152604091829020805485019055600880548501905560025482519384526001600160a01b03169083015281018290527f630c9eb89085cfe993245c555e78df45b0b8e8f31f411ddc400727738d9648349060600160405180910390a150565b6112516112c2565b6001600160a01b0381166112b65760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b60648201526084016104e7565b6112bf8161131c565b50565b6000546001600160a01b03163314610dd45760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657260448201526064016104e7565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80356001600160a01b038116811461138357600080fd5b919050565b60006020828403121561139a57600080fd5b6113a38261136c565b9392505050565b6000602082840312156113bc57600080fd5b5035919050565b634e487b7160e01b600052604160045260246000fd5b604051601f8201601f1916810167ffffffffffffffff81118282101715611402576114026113c3565b604052919050565b600067ffffffffffffffff821115611424576114246113c3565b50601f01601f191660200190565b60008060006060848603121561144757600080fd5b833567ffffffffffffffff81111561145e57600080fd5b8401601f8101861361146f57600080fd5b803561148261147d8261140a565b6113d9565b81815287602083850101111561149757600080fd5b816020840160208301376000602083830101528095505050506114bc6020850161136c565b9150604084013590509250925092565b60005b838110156114e75781810151838201526020016114cf565b50506000910152565b600081518084526115088160208601602086016114cc565b601f01601f19169290920160200192915050565b6020815260006113a360208301846114f0565b60c08152600061154260c08301896114f0565b6001600160a01b039790971660208301525060408101949094529115156060840152608083015260a090910152919050565b634e487b7160e01b600052601160045260246000fd5b6000826115a757634e487b7160e01b600052601260045260246000fd5b500490565b6000602082840312156115be57600080fd5b815180151581146113a357600080fd5b6000602082840312156115e057600080fd5b5051919050565b6000602082840312156115f957600080fd5b815167ffffffffffffffff81111561161057600080fd5b8201601f8101841361162157600080fd5b805161162f61147d8261140a565b81815285602083850101111561164457600080fd5b6116558260208301602086016114cc565b95945050505050565b8082018082111561167157611671611574565b92915050565b60006001820161168957611689611574565b5060010190565b600181811c908216806116a457607f821691505b6020821081036116c457634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561171457600081815260208120601f850160051c810160208610156116f15750805b601f850160051c820191505b81811015611710578281556001016116fd565b5050505b505050565b815167ffffffffffffffff811115611733576117336113c3565b611747816117418454611690565b846116ca565b602080601f83116001811461177c57600084156117645750858301515b600019600386901b1c1916600185901b178555611710565b600085815260208120601f198616915b828110156117ab5788860151825594840194600190910190840161178c565b50858210156117c95787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b858152600060018060a01b03808716602084015260a0604084015261180160a08401876114f0565b941660608301525060800152939250505056fea264697066735822122025fabc53a3b8b63f746495e126130763dd34834d423e7019dec7b87136a4454e64736f6c63430008130033";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_CONTRIBUTORS = "contributors";

    public static final String FUNC_DEADLINE = "deadline";

    public static final String FUNC_GOAL = "goal";

    public static final String FUNC_NOOFCONTRIBUTORS = "noOfContributors";

    public static final String FUNC_NUMREQUESTS = "numRequests";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RAISEDAMOUNT = "raisedAmount";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REQUESTS = "requests";

    public static final String FUNC_TITLE = "title";

    public static final String FUNC_TOKEN = "token";

    public static final String FUNC_TOKENADDRESS = "tokenAddress";

    public static final String FUNC_TOTALREQUESTAMOUNT = "totalRequestAmount";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_CONTRIBUTE = "contribute";

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

    public static final String FUNC_CURRBLOCKTIMESTAMP = "currBlockTimestamp";

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

    public RemoteFunctionCall<BigInteger> contributors(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONTRIBUTORS, 
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

    public RemoteFunctionCall<BigInteger> goal() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GOAL, 
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

    public RemoteFunctionCall<BigInteger> totalRequestAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALREQUESTAMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteFunctionCall<BigInteger> currBlockTimestamp() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CURRBLOCKTIMESTAMP, 
                Arrays.<Type>asList(), 
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
