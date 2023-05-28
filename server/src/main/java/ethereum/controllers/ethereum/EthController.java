package ethereum.controllers.ethereum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ethereum.services.ethereum.BlockchainService;
import ethereum.util.misc.Util;
import jakarta.json.JsonObject;

@RestController
// @CrossOrigin(origins = "*")
// @CrossOrigin(origins = "#{'${client.url}'}")
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class EthController {

    private static final Logger logger = LoggerFactory.getLogger(EthController.class);

    @Autowired
    private BlockchainService bcSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;

    @Value("${faucet.contract.address}")
    private String faucetContractAddress;

    @PostMapping(path = "/read-event")
    @ResponseBody
    public ResponseEntity<String> getEvent(@RequestBody String body) {
        logger.info("read event >> {}", body);
        JsonObject job = Util.readJson(body);

        logger.info("read event, contains description >> {}", job.containsKey("description"));
        String contractName = job.getString("contractName");
        String functionName = job.getString("functionName");
        String blockHash = job.getString("blockHash");

        if (job.containsKey("description")) {
            String description = job.getString("description");
            String imageUrl = "";
            if (job.containsKey("imageUrl")) {
                imageUrl = job.getString("imageUrl");
            }
            switch (contractName) {
                case "CrowdfundingFactory" ->
                    bcSvc.getEvent(contractName, functionName, blockHash, crowdfundingFactoryContractAddress,
                            description, imageUrl);
                default -> {
                    String contractAddress = job.getString("address");
                    bcSvc.getEvent(contractName, functionName, blockHash, contractAddress,
                            description);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            switch (contractName) {
                case "DevFaucet" ->
                    bcSvc.getEvent(contractName, functionName, blockHash, faucetContractAddress);
                default -> {
                    String address = job.getString("address");
                    bcSvc.getEvent(contractName, functionName, blockHash, address);
                    return ResponseEntity.status(HttpStatus.OK).body(null);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}
