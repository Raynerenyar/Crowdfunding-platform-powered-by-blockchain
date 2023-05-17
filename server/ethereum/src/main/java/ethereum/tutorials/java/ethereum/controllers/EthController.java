package ethereum.tutorials.java.ethereum.controllers;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ethereum.tutorials.java.ethereum.services.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.services.ethereum.LoadContractService;
import ethereum.tutorials.java.ethereum.util.misc.Util;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jnr.constants.platform.IP;
import jnr.constants.platform.IPProto;

@Controller
// @CrossOrigin(origins = "*")
// @CrossOrigin(origins = "#{'${client.url}'}")
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api")
public class EthController {

    private static final Logger logger = LoggerFactory.getLogger(EthController.class);

    @Autowired
    private BlockchainService BcSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;

    @Value("${faucet.contract.address}")
    private String faucetContractAddress;

    @PostMapping(path = "/get-function-encoded")
    @ResponseBody
    public ResponseEntity<String> getFunctionEncoded(
            @RequestParam String contractName,
            @RequestParam String functionName,
            @RequestParam(required = false) String contractAddress,
            @RequestBody(required = false) Object... params) {
        logger.info("getting function, contractAddress >> {}", contractAddress);
        logger.info("getting function, contractName >> {}", contractName);
        logger.info("getting function, functionName >> {}", functionName);
        Optional<String> opt;
        if (contractName.equalsIgnoreCase("CrowdfundingFactory") || contractName.equalsIgnoreCase("DevFaucet")) {
            // opt = BcSvc.getEncoded(contractName, functionName, params);
            opt = BcSvc.getEncodedForTransaction(contractName, functionName, params);
            Optional<String> optAddress = BcSvc.getContractAddress(contractName);
            contractAddress = (optAddress.isPresent()) ? optAddress.get() : contractAddress;
        } else {
            // opt = BcSvc.getEncoded(contractName, functionName, contractAddress, params);
            opt = BcSvc.getEncodedForTransaction(contractName, functionName, contractAddress, params);
        }
        logger.info("got encoded function in opt >> {}", opt.isPresent());
        if (opt.isPresent()) {
            String encodedFunction = Json.createObjectBuilder()
                    .add("encodedFunction", opt.get())
                    .add("contractAddress", contractAddress)
                    .build()
                    .toString();
            return ResponseEntity.status(HttpStatus.OK).body((encodedFunction));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping(path = "/get-view-functions")
    @ResponseBody
    public ResponseEntity<String> getViewFunctions(
            @RequestParam String contractName,
            @RequestParam String functionName,
            @RequestParam(required = false) String contractAddress,
            @RequestBody(required = false) Object... params) {

        logger.info("getting view function, contractAddress >> {}", contractAddress);
        logger.info("getting view function, contractName >> {}", contractName);
        logger.info("getting view function, functionName >> {}", functionName);
        Optional<String> opt;
        if (contractAddress == null) {
            opt = BcSvc.viewFunctions(contractName, functionName, params);
        } else {
            opt = BcSvc.viewFunctions(contractName, functionName, contractAddress, params);
        }
        logger.info("got encoded function in opt >> {}", opt.isPresent());
        if (opt.isPresent()) {
            String returnFromView = Json.createObjectBuilder()
                    .add("returnFromView", opt.get())
                    .build()
                    .toString();
            return ResponseEntity.status(HttpStatus.OK).body((returnFromView));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

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
            switch (contractName) {
                case "CrowdfundingFactory" ->
                    BcSvc.getEvent(contractName, functionName, blockHash, crowdfundingFactoryContractAddress,
                            description);
                default -> {
                    String contractAddress = job.getString("address");
                    BcSvc.getEvent(contractName, functionName, blockHash, contractAddress,
                            description);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            switch (contractName) {
                case "DevFaucet" ->
                    BcSvc.getEvent(contractName, functionName, blockHash, faucetContractAddress);
                default -> {
                    String address = job.getString("address");
                    BcSvc.getEvent(contractName, functionName, blockHash, address);
                    return ResponseEntity.status(HttpStatus.OK).body(null);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @PostMapping(path = "/read-error-event")
    @ResponseBody
    public ResponseEntity<String> getErrorEvent(@RequestBody String body) {
        logger.info("read error event >> {}", body);
        JsonObject job = Util.readJson(body);
        logger.info("read error event >> {}", job.containsKey("description"));
        String contractName = job.getString("contractName");
        String functionName = job.getString("functionName");
        String blockHash = job.getString("blockHash");
        // handle description in body in sql, done for createNewProject, not done for request
        // if (job.containsKey("description")) {
        //     String description = job.getString("description");
        //     BcSvc.getEvent(contractName, functionName, blockHash, description);
        //     return ResponseEntity.status(HttpStatus.OK).body(null);
        // } else {
        //     BcSvc.getEvent(contractName, functionName, blockHash);
        //     return ResponseEntity.status(HttpStatus.OK).body(null);
        // }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
