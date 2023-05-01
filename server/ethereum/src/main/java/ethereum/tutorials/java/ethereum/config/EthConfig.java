package ethereum.tutorials.java.ethereum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthConfig {

    @Value("${wallet.public.key}")
    private String publicKey;
    @Value("${wallet.private.key}")
    private String privateKey;
    @Value("${rpc.url}")
    private String rpcUrl;
    @Value("${chain.id}")
    private int chaintId;

    @Bean
    Web3j initWeb3j() {
        System.out.println("rpc connected to > " + rpcUrl);
        System.out.println("and connected to chain id" + chaintId);
        return Web3j.build(new HttpService(rpcUrl));
    }

}
