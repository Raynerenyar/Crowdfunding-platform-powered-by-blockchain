package ethereum.controllers.ethereum.crowdfunding.view;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ethereum.services.ethereum.smartcontract.CrowdfundingFunctionsService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ethereum.util.misc.Util;
import jakarta.json.JsonObject;

@RestController
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api/crowdfunding/view")
public class EthViewController {
    @Autowired
    private CrowdfundingFunctionsService crowdFuncSvc;
    private static final Logger logger = LoggerFactory.getLogger(EthViewController.class);

    @PostMapping(path = "contribute/amount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> getContributionAmount(
            @RequestParam String projectAddress,
            @RequestBody String entity) {
        JsonObject job = Util.readJson(entity);
        String contributorAddress;
        if (job.containsKey("contributorAddress")) {
            contributorAddress = job.getString("contributorAddress");
            Optional<Long> opt = crowdFuncSvc.getContributionAmount(projectAddress, contributorAddress);
            logger.info("getting contrbutor contribution {} from project {}", contributorAddress, projectAddress);
            if (opt.isPresent()) {
                return ResponseEntity.ok().body(opt.get());
            }
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping(path = "raised/amount")
    public ResponseEntity<Long> getMethodName(@RequestParam String projectAddress) {
        Optional<Long> opt = crowdFuncSvc.getRaisedAmount(projectAddress);
        logger.info("get raised amount for {}", projectAddress);
        if (opt.isPresent()) {
            return ResponseEntity.ok().body(opt.get());
        }
        return ResponseEntity.badRequest().body(null);
    }
}
