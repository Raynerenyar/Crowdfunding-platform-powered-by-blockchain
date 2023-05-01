package ethereum.tutorials.java.ethereum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ethereum.tutorials.java.ethereum.config.FirebaseConfig;
import ethereum.tutorials.java.ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.tutorials.java.ethereum.repository.CrowdfundingRepository;
import ethereum.tutorials.java.ethereum.service.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.service.ethereum.LoadContractService;
import jnr.ffi.provider.SigType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;

@SpringBootApplication
public class EthereumApplication implements CommandLineRunner {

	@Autowired
	FirebaseApp firebase; // might not need to autowire it
	@Autowired
	Web3j web3;
	@Autowired
	CrowdfundingRepository sqlRepo;

	@Value("${firebase.project.id}")
	private String firebaseProjId;

	@Value("${firebase.app.id}")
	private String firebaseAppId;

	@Value("${crowdfunding.factory.contract.address}")
	private String crowdfundingFactoryContractAddress;

	private static final Logger logger = LoggerFactory.getLogger(EthereumApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EthereumApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// bcEventListener.listeningEvent();

		// Web3j web3 = Web3j
		// 		.build(new HttpService("https://eth-mainnet.gateway.pokt.network/v1/5f3453978e354ab992c4da79"));

		// final String ethAddress = "0x2759bC7b8f9F2b47eEeFFB2f5751E0CFF3fF1aD8";
		// EthGetBalance balResp = web3.ethGetBalance(ethAddress, DefaultBlockParameter.valueOf("latest")).sendAsync()
		// 		.get(10, TimeUnit.SECONDS);
		// BigInteger balance = balResp.getBalance();
		// System.out.println("0x2759bC7b8f9F2b47eEeFFB2f5751E0CFF3fF1aD8 balance is >>>: " + balance);
		// BigDecimal quotient = BigDecimal.valueOf(Math.pow(10, 18));
		// BigDecimal scaledBal = new BigDecimal(balance);
		// scaledBal = scaledBal.divide(quotient);
		// System.out.println(ethAddress + " balance after scaling is >>>>: " + scaledBal);

		// Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
		// EthGasPrice gasPrice = web3.ethGasPrice().send();
		// EthBlockNumber blockNumber = web3.ethBlockNumber().send();

		// System.out.println(gasPrice.getGasPrice());
		// System.out.println(blockNumber.getBlockNumber());
		// System.out.println(clientVersion.getWeb3ClientVersion());

		// ethSvc.testRun();
		// testSql();
	}

	// public void testSql() {
	// 	String testAddress = "0x0";
	// 	sqlRepo.insertContributor(testAddress);
	// 	sqlRepo.insertProjectCreator(testAddress, "testName");

	// 	sqlRepo.insertProject(
	// 			testAddress,
	// 			testAddress,
	// 			"test",
	// 			0,
	// 			0,
	// 			0,
	// 			false,
	// 			false,
	// 			0,
	// 			testAddress);

	// 	sqlRepo.insertProjectRequest(
	// 			testAddress,
	// 			"test tilte",
	// 			testAddress,
	// 			0,
	// 			0,
	// 			false,
	// 			0.00);

	// 	sqlRepo.insertVote(0, testAddress, 0);

	// 	sqlRepo.insertContribution(
	// 			testAddress,
	// 			0,
	// 			testAddress,
	// 			false);

	// 	sqlRepo.updateContribution(true, testAddress, testAddress);

	// 	sqlRepo.updateRequest(1, true);
	// }

}
