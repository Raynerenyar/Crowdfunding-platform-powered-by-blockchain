package ethereum.controllers.ethereum.crowdfunding.transaction;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ethereum.models.payload.crowdfunding.EncodedFunction;
import ethereum.models.smartcontract.MiscEntity;
import ethereum.models.smartcontract.Request;
import ethereum.services.ethereum.smartcontract.CrowdfundingFunctionsService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api/crowdfunding/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
public class EthTxController {

    @Autowired
    private CrowdfundingFunctionsService crowdFuncSvc;
    private static final Logger logger = LoggerFactory.getLogger(EthTxController.class);

    @PostMapping(path = "/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncodedFunction> postMethodName(
            @RequestParam String projectAddress,
            @RequestBody Request entity) {
        logger.info("creating new requenst {}", entity);
        Optional<String> opt = crowdFuncSvc.createNewRequest(entity, projectAddress);
        if (opt.isPresent()) {
            return ResponseEntity.ok().body(
                    new EncodedFunction(
                            opt.get(),
                            projectAddress));
        }
        return ResponseEntity.internalServerError().body(null);
    }

    @PostMapping(path = "/receive/contribution", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncodedFunction> receiveContribution(
            @RequestParam String projectAddress,
            @RequestBody MiscEntity entity) {
        Optional<String> opt = crowdFuncSvc.receiveContribution(
                projectAddress, entity.getRequestNum());
        if (opt.isPresent())
            return ResponseEntity.ok().body(new EncodedFunction(opt.get(), projectAddress));
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping(path = "/contribute", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncodedFunction> contribute(
            @RequestParam String projectAddress,
            @RequestBody MiscEntity entity) {
        Optional<String> opt = crowdFuncSvc.contribute(projectAddress, entity.getAmount());
        if (opt.isPresent())
            return ResponseEntity.ok().body(new EncodedFunction(opt.get(), projectAddress));
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping(path = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncodedFunction> voteRequest(
            @RequestParam String projectAddress,
            @RequestBody MiscEntity entity) {
        Optional<String> opt = crowdFuncSvc.voteRequest(projectAddress, entity.getRequestNum());
        if (opt.isPresent())
            return ResponseEntity.ok().body(new EncodedFunction(opt.get(), projectAddress));
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping(path = "/refund")
    public ResponseEntity<EncodedFunction> getRefund(@RequestParam String projectAddress) {
        Optional<String> opt = crowdFuncSvc.refund(projectAddress);
        if (opt.isPresent())
            return ResponseEntity.ok().body(new EncodedFunction(opt.get(), projectAddress));
        return ResponseEntity.badRequest().body(null);
    }
}
