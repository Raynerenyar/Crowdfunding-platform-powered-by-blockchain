package ethereum.tutorials.java.ethereum.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.json.Json;
import jnr.constants.platform.IP;
import jnr.constants.platform.IPProto;

@Controller
@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "#{'${client.url}'}")
public class EthController {

    @Autowired
    private BlockchainService BcSvc;

    @PostMapping(path = "/get-function-encoded/")
    @ResponseBody
    public ResponseEntity<String> getFunctionEncoded(
            @RequestParam String contractName,
            @RequestParam String functionName,
            @RequestBody Object[] params) {
        System.out.println("contract name >>>>> " + contractName);
        System.out.println("function name >>>>> " + functionName);
        for (Object object : params) {
            System.out.println("params >>>>> " + object);
            System.out.println(object.getClass().getSimpleName());
        }
        Optional<String> opt;
        opt = BcSvc.getEncoded(contractName, contractName, functionName, params);
        if (opt.isPresent()) {
            String encodedFunction = Json.createObjectBuilder()
                    .add("encodedFunction", opt.get())
                    .build()
                    .toString();
            return ResponseEntity.status(HttpStatus.OK).body((encodedFunction));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
