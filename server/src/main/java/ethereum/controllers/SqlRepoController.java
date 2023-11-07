package ethereum.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ethereum.services.repository.SqlRepoService;
import ethereum.models.sql.crowdfunding.Project;
import ethereum.models.sql.crowdfunding.Request;
import ethereum.models.sql.crowdfunding.Token;
import ethereum.repository.sql.crowdfunding.SqlCrowdfundingRepo;

@RestController
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api")
public class SqlRepoController {
    private static final Logger logger = LoggerFactory.getLogger(SqlRepoController.class);

    @Autowired
    private SqlRepoService crowdfundingRepoSvc;
    @Autowired
    private SqlCrowdfundingRepo sqlRepo;

    @GetMapping("/get-projects-by-creator-address/{address}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Project>> getProjectsByCreatorAddress(@PathVariable String address) {
        logger.info("get project by creator address, address >> {} ", address);
        Optional<List<Project>> opt = crowdfundingRepoSvc.getProjectsByCreatorAddress(address.toLowerCase());
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }

    @GetMapping("/get-requests/{projectAddress}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Request>> getRequests(@PathVariable String projectAddress) {
        logger.info("getting requests by project address");
        Optional<List<Request>> opt = crowdfundingRepoSvc.getRequestsByProjectAddress(projectAddress);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    // get all projects by page with all column data
    @GetMapping("/get-projects")
    public ResponseEntity<List<Project>> getProjectsWithPage(@RequestParam int offset, @RequestParam int limit) {
        logger.info("getting projects with page, limit >> {}, offset >> {}", limit, offset);
        Optional<List<Project>> opt = crowdfundingRepoSvc.getProjectsWithPage(offset, limit);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/get-single-project/{projectAddress}")
    public ResponseEntity<Project> getSingularProject(@PathVariable String projectAddress) {
        Optional<Project> opt = crowdfundingRepoSvc.getProjectByProjectAddress(projectAddress);
        if (opt.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/get-single-request/{requestId}")
    public ResponseEntity<Request> getSingularRequest(@PathVariable int requestId) {
        Optional<Request> opt = crowdfundingRepoSvc.getRequestById(requestId);
        if (opt.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("get-latest-project/{creatorAddress}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> getLatestProjectByCreatorAddress(@PathVariable String creatorAddress) {
        Optional<Project> opt = crowdfundingRepoSvc.getLatestProjectByCreatorAddress(creatorAddress.toLowerCase());
        if (opt.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/get-count-projects")
    public ResponseEntity<Integer> getCountProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(crowdfundingRepoSvc.countProjects());
    }

    @GetMapping("/get-count-requests")
    public ResponseEntity<Integer> getCountRequestsByProjectAddress(@PathVariable String projectAddress) {
        return ResponseEntity.status(HttpStatus.OK).body(crowdfundingRepoSvc.countRequestsByProject(projectAddress));
    }

    @GetMapping("/get-value-of-votes/{projectAddress}/{requestNo}")
    public ResponseEntity<Integer> getValueOfVotes(@PathVariable String projectAddress,
            @PathVariable Integer requestNo) {
        logger.info("getting value of votes, project address >> {}, request no >> {}", projectAddress, requestNo);
        return ResponseEntity.status(HttpStatus.OK)
                .body(crowdfundingRepoSvc.getValueOfVotesOfRequest(projectAddress, requestNo));
    }

    @GetMapping("/get-count-of-votes/{projectAddress}/{requestNo}")
    public ResponseEntity<Integer> getCountOfVotes(@PathVariable String projectAddress,
            @PathVariable Integer requestNo) {
        logger.info("getting COUNT of votes, project address >> {}, request no >> {}", projectAddress, requestNo);
        return ResponseEntity.status(HttpStatus.OK)
                .body(crowdfundingRepoSvc.getCountOfVotes(projectAddress, requestNo));
    }

    @GetMapping("/get-tokens")
    public ResponseEntity<List<Token>> getTokens() {
        logger.info("getting tokens");
        List<Token> tokens = sqlRepo.getListOfTokens();
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokens);
    }
}
