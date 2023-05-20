package ethereum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.repository.SqlCrowdfundingRepo;
import ethereum.services.ethereum.BlockchainService;
import ethereum.services.ethereum.EtherscanService;
import ethereum.services.ethereum.LoadContractService;
import ethereum.services.repository.SqlRepoService;
import jnr.ffi.provider.SigType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import java.math.BigInteger;

@SpringBootApplication
public class EthereumApplication implements CommandLineRunner {

	@Autowired
	Web3j web3;
	@Autowired
	SqlCrowdfundingRepo sqlRepo;
	@Autowired
	SqlRepoService repoSvc;
	@Autowired
	BlockchainService bcSvc;

	@Value("${crowdfunding.factory.contract.address}")
	private String crowdfundingFactoryContractAddress;
	@Autowired
	private LoadContractService lcSvc;
	@Autowired
	private EtherscanService etherscanSvc;

	private static final Logger logger = LoggerFactory.getLogger(EthereumApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EthereumApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Crowdfunding c = lcSvc.loadCrowdfundingContract("0x45e3cafadf713f8dbe34936b5d0066b654a481ac");
		// System.out.println("command line runner >>> contract is valid? >>> " + c.isValid());
		// if (c.isValid()) {
		// 	System.out.println(c.goal().send());
		// 	System.out.println(c.requests(BigInteger.valueOf(0)).send().component6());
		// }

		// Function balanceOfFunction = new Function(
		// 		"balanceOf(address)",
		// 		Arrays.asList("<YOUR_ADDRESS>"), // set the function arguments
		// 		Arrays.asList(new TypeReference<Uint256>() {
		// 		}) // set the function return type
		// );
		// String methodName = "balanceOf";
		// List<Type> inputParameters = new ArrayList<>();
		// List<TypeReference<?>> outputParameters = new ArrayList<>();
		// TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
		// };

		// outputParameters.add(typeReference);
		// inputParameters.add(new Address("0x149AaeA6f804Ba0Ea228DFC929C4f42798B54d4C"));
		// // Function function = new Function(methodName, inputParameters, outputParameters);
		// Function balanceOf = new Function(methodName, inputParameters, outputParameters);
		// String encodedFunction = FunctionEncoder.encode(balanceOf);
		// Transaction transaction = Transaction.createEthCallTransaction(
		// 		"0x149AaeA6f804Ba0Ea228DFC929C4f42798B54d4C",
		// 		"0x11f1bA109B43a33F935A056D1e31616da0BEa15F",
		// 		encodedFunction);
		// EthCall response = web3.ethCall(transaction, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
		// String value = response.getValue();

		// List<Type> values = FunctionReturnDecoder.decode(value, balanceOf.getOutputParameters());
		// System.out.println((BigInteger) values.get(0).getValue());

		// Optional<String> result = bcSvc.viewFunctions("Crowdfunding", "requests",
		// 		"0x235bee48c2acff78744aaf2d97021b073dcad56e");
		// if (result.isPresent()) {
		// 	// if (result.get().toString().equals("0x0000000000000000000000000000000000000000000000000000000000000001"))
		// 	System.out.println(result.get().toString());
		// 	System.out.println("is contract? >>> " + result.get().toString()
		// 			.equals("0x0000000000000000000000000000000000000000000000000000000000000001"));
		// } else {
		// 	System.out.println("no result");

		// }

		// Function decimalsFunction = new Function(
		// 		"decimals",
		// 		Collections.emptyList(),
		// 		Arrays.asList(new TypeReference<Uint256>() {
		// 		}));
		// String decimalsEncoded = FunctionEncoder.encode(decimalsFunction);
		// Transaction transaction2 = Transaction.createEthCallTransaction(
		// 		"0x149AaeA6f804Ba0Ea228DFC929C4f42798B54d4C",
		// 		"0x7ebe54502fc6979CC81E11FD5BBbb4a7B2ca2905",
		// 		decimalsEncoded);
		// EthCall decimalsResponse = web3.ethCall(transaction2, DefaultBlockParameter.valueOf("latest")).sendAsync()
		// 		.get();
		// String decimalsValue = decimalsResponse.getValue();
		// System.out.println("getting decimals");
		// logger.info("decimals >> {}", decimalsValue);
		// decimalsFunction.getOutputParameters();
		// List<Type> decimalsResult = FunctionReturnDecoder.decode(decimalsValue,
		// 		decimalsFunction.getOutputParameters());
		// if (decimalsResult.size() > 0) {
		// 	logger.info("results {}", (BigInteger) decimalsResult.get(0).getValue());
		// }
		// String result = bcSvc.getRaisedAmount("0x235bee48c2acff78744aaf2d97021b073dcad56e");
		// System.out.println("raised amount >>> " + result);
		// daily limit 100
		// Boolean res_ult = etherscanSvc.verifyContract("0x465bf3bb7E89DF1B1dC1e4D678FF174a803d6242",
		// 		"CrowdfundingFactory");
		// logger.info("verify contract >> {}", res_ult);

	}
}
