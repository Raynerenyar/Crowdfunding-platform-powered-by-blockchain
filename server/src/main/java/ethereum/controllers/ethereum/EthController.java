package ethereum.controllers.ethereum;

import java.math.BigInteger;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ethereum.models.payload.crowdfunding.EncodedFunction;
import ethereum.models.smartcontract.Project;
import ethereum.models.smartcontract.Request;
import ethereum.services.ethereum.BlockchainService;
import ethereum.services.ethereum.LoadContractService;
import ethereum.services.ethereum.smartcontract.CrowdfundingFunctionsService;
import ethereum.services.ethereum.smartcontract.FactoryFunctionsService;
import ethereum.util.misc.Util;
import jakarta.json.Json;
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
    @Autowired
    private CrowdfundingFunctionsService crowdfundingSvc;
    @Autowired
    private FactoryFunctionsService factorySvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;

    @Value("${faucet.contract.address}")
    private String faucetContractAddress;

    // @PostMapping(path = "/get-function-encoded")
    // @ResponseBody
    // public ResponseEntity<String> getFunctionEncoded(
    //         @RequestParam String contractName,
    //         @RequestParam String functionName,
    //         @RequestParam(required = false) String contractAddress,
    //         @RequestBody(required = false) Object... params) {
    //     logger.info("getting function, contractAddress >> {}", contractAddress);
    //     logger.info("getting function, contractName >> {}", contractName);
    //     logger.info("getting function, functionName >> {}", functionName);
    //     Optional<String> opt;
    //     if (contractName.equalsIgnoreCase("CrowdfundingFactory") || contractName.equalsIgnoreCase("DevFaucet")) {
    //         // opt = BcSvc.getEncoded(contractName, functionName, params);
    //         opt = bcSvc.getEncodedForTransaction(contractName, functionName, params);
    //         Optional<String> optAddress = bcSvc.getContractAddress(contractName);
    //         contractAddress = (optAddress.isPresent()) ? optAddress.get() : contractAddress;
    //     } else {
    //         // opt = BcSvc.getEncoded(contractName, functionName, contractAddress, params);
    //         opt = bcSvc.getEncodedForTransaction(contractName, functionName, contractAddress, params);
    //     }
    //     logger.info("got encoded function in opt >> {}", opt.isPresent());
    //     if (opt.isPresent()) {
    //         String encodedFunction = Json.createObjectBuilder()
    //                 .add("encodedFunction", opt.get())
    //                 .add("contractAddress", contractAddress)
    //                 .build()
    //                 .toString();
    //         return ResponseEntity.status(HttpStatus.OK).body((encodedFunction));
    //     }
    //     return ResponseEntity.badRequest().body(null);
    // }

    // @PostMapping(path = "/get-view-functions")
    // @ResponseBody
    // public ResponseEntity<String> getViewFunctions(
    //         @RequestParam String contractName,
    //         @RequestParam String functionName,
    //         @RequestParam(required = false) String contractAddress,
    //         @RequestBody(required = false) Object... params) {

    //     logger.info("getting view function, contractAddress >> {}", contractAddress);
    //     logger.info("getting view function, contractName >> {}", contractName);
    //     logger.info("getting view function, functionName >> {}", functionName);
    //     Optional<String> opt;
    //     if (contractAddress == null) {
    //         opt = bcSvc.viewFunctions(contractName, functionName, params);
    //     } else {
    //         opt = bcSvc.viewFunctions(contractName, functionName, contractAddress, params);
    //     }
    //     logger.info("got return value for {} in opt >> {}", functionName, opt.isPresent());
    //     if (opt.isPresent()) {
    //         logger.info("return value is {}", opt.get());
    //         String returnFromView = Json.createObjectBuilder()
    //                 .add("returnFromView", opt.get())
    //                 .build()
    //                 .toString();
    //         return ResponseEntity.status(HttpStatus.OK).body((returnFromView));
    //     }
    //     return ResponseEntity.badRequest().body(null);
    // }

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
