package ethereum.tutorials.java.ethereum.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ethereum.tutorials.java.ethereum.repository.CrowdfundingRepository;
import ethereum.tutorials.java.ethereum.services.repository.RepoService;
import ethereum.tutorials.java.ethereum.models.Project;
import ethereum.tutorials.java.ethereum.models.ProjectRequest;

@RestController
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api")
public class RepoController {

    @Autowired
    private RepoService crowdfundingRepoSvc;

    @GetMapping("/get-projects-by-creator-address/{address}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Project>> getProjectsByCreatorAddress(@PathVariable String address) {
        System.out.println("getting by creator address " + address);
        Optional<List<Project>> opt = crowdfundingRepoSvc.getProjectsByCreatorAddress(address);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    @GetMapping("/get-requests/{projectAddress}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProjectRequest>> getRequests(@PathVariable String projectAddress) {
        System.out.println("getting requests by project address");
        Optional<List<ProjectRequest>> opt = crowdfundingRepoSvc.getRequestsByProjectAddress(projectAddress);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    // get all projects by page with all column data
    @GetMapping("/get-projects")
    public ResponseEntity<List<Project>> getProjectsWithPage(@RequestParam int offset, @RequestParam int limit) {
        System.out.println("getting projects with limit >>> " + limit);
        System.out.println("getting projects with offset >>> " + offset);
        Optional<List<Project>> opt = crowdfundingRepoSvc.getProjectsWithPage(offset, limit);
        if (opt.isPresent()) {
            for (Project project : opt.get()) {
                System.out.println("getting projects see title >> " + project.getTitle());
            }
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/get-single-project/{projectAddress}")
    public ResponseEntity<Project> getSingularProject(@PathVariable String projectAddress) {
        Optional<Project> opt = crowdfundingRepoSvc.getProjectByProjectAddress(projectAddress);
        if (opt.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("get-latest-project/{creatorAddress}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> getLatestProjectByCreatorAddress(@PathVariable String creatorAddress) {
        Optional<Project> opt = crowdfundingRepoSvc.getLatestProjectByCreatorAddress(creatorAddress);
        System.out.println(opt.get().toString());
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
}
