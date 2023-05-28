package ethereum.controllers.ethereum.factory;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ethereum.models.payload.crowdfunding.EncodedFunction;
import ethereum.models.smartcontract.Project;
import ethereum.services.ethereum.smartcontract.FactoryFunctionsService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api/factory", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class FactoryController {
    @Autowired
    private FactoryFunctionsService factoryFuncSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;

    private static final Logger logger = LoggerFactory.getLogger(FactoryController.class);

    @PostMapping(path = "/project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EncodedFunction> postMethodName(@RequestBody Project entity) {
        logger.info("create new project {}", entity.toString());
        Optional<String> opt = factoryFuncSvc.createNewProject(entity);
        if (opt.isPresent()) {
            EncodedFunction ef = new EncodedFunction(opt.get(), crowdfundingFactoryContractAddress);
            return ResponseEntity.ok().body(ef);
        }
        return ResponseEntity.internalServerError().body(null);
    }
}
