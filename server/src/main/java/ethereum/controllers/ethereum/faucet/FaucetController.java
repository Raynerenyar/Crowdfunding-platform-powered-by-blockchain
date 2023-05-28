package ethereum.controllers.ethereum.faucet;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import ethereum.models.payload.crowdfunding.EncodedFunction;
import ethereum.services.ethereum.smartcontract.FaucetFunctionsService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api/faucet", produces = MediaType.APPLICATION_JSON_VALUE)
public class FaucetController {

    @Value("${faucet.contract.address}")
    private String faucetContractAddress;
    @Autowired
    private FaucetFunctionsService faucetFuncSvc;

    @GetMapping(path = "/distribute")
    public ResponseEntity<EncodedFunction> postMethodName() {

        Optional<String> opt = faucetFuncSvc.distribute();
        if (opt.isPresent()) {
            return ResponseEntity.ok().body(
                    new EncodedFunction(
                            opt.get(),
                            faucetContractAddress));
        }
        return ResponseEntity.badRequest().body(null);
    }

}
