package ethereum.tutorials.java.ethereum.controllers;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ethereum.tutorials.java.ethereum.service.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.service.ethereum.LoadContractService;
import ethereum.tutorials.java.ethereum.util.misc.Util;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jnr.constants.platform.IP;
import jnr.constants.platform.IPProto;

@Controller
@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "#{'${client.url}'}")
public class EthController {

    @Autowired
    private BlockchainService BcSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;

    @PostMapping(path = "/get-function-encoded/")
    @ResponseBody
    public ResponseEntity<String> getFunctionEncoded(
            @RequestParam String contractName,
            @RequestParam String functionName,
            @RequestBody(required = false) Object... params) {
        System.out.println("contract name >>>>> " + contractName);
        System.out.println("function name >>>>> " + functionName);
        Optional<String> opt;
        // if (params != null) {
        //     for (Object object : params) {
        //         System.out.println("params >>>>> " + object);
        //         System.out.println(object.getClass().getSimpleName());
        //     }
        // opt = BcSvc.getEncoded(contractName, functionName, params);
        // }
        // else {
        //     opt = BcSvc.getEncoded(contractName, functionName);
        // }
        opt = BcSvc.getEncoded(contractName, functionName, params);
        if (opt.isPresent()) {
            String encodedFunction = Json.createObjectBuilder()
                    .add("encodedFunction", opt.get())
                    .build()
                    .toString();
            return ResponseEntity.status(HttpStatus.OK).body((encodedFunction));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping(path = "/read-event")
    @ResponseBody
    public ResponseEntity<String> getEvent(@RequestBody String body) {
        JsonObject job = Util.readJson(body);
        String contractName = job.getString("contractName");
        String functionName = job.getString("functionName");
        String blockHash = job.getString("blockHash");
        // handle description in body in sql, done for createNewProject, not done for request
        if (!job.isNull("description")) {
            String description = job.getString("description");
            BcSvc.getEvent(contractName, functionName, blockHash, description);
        } else {
            BcSvc.getEvent(contractName, functionName, blockHash);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
