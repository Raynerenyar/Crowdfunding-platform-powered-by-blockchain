package ethereum.tutorials.java.ethereum.services.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ethereum.tutorials.java.ethereum.javaethereum.wrapper.Crowdfunding;
import ethereum.tutorials.java.ethereum.models.Project;
import ethereum.tutorials.java.ethereum.models.ProjectRequest;
import ethereum.tutorials.java.ethereum.repository.CrowdfundingRepository;

@Service
public class RepoService {
    @Autowired
    private CrowdfundingRepository crowdRepo;

    public Optional<List<Project>> getProjectsByCreatorAddress(String address) {
        List<Project> projects = crowdRepo.selectProjectByCreatorAddress(address);
        if (projects.size() == 0)
            return Optional.empty();
        return Optional.of(projects);
    }

    public Optional<Project> getProjectByProjectAddress(String projectAddress) {

        List<Project> projects = crowdRepo.selectProjectByProjectAddress(projectAddress);
        if (projects.size() == 1)
            return Optional.of(projects.get(0));
        return Optional.empty();
    }

    public Optional<List<ProjectRequest>> getRequestsByProjectAddress(String address) {
        List<ProjectRequest> requests = crowdRepo.selectRequests(address);
        if (requests.size() == 0)
            return Optional.empty();
        return Optional.of(requests);
    }

    public Optional<List<String>> getListOfProjectAddress(String address) {
        List<String> addresses = crowdRepo.selectProjectAddressesByCreatorAddress(address);
        for (String _address : addresses) {
            System.out.println(_address);
        }
        if (addresses.size() == 0)
            return Optional.empty();
        return Optional.of(addresses);
    }

    public Optional<List<Project>> getProjectsWithPage(int offset, int limit) {
        List<Project> projects = crowdRepo.selectProjectsWithPage(offset, limit);
        if (projects.size() == 0)
            return Optional.empty();
        return Optional.of(projects);
    }

    public Integer countProjects() {
        return crowdRepo.countProjects();
    }

    public Integer countRequestsByProject(String projectAddress) {
        return crowdRepo.countRequests(projectAddress);
    }

    public Optional<Project> getLatestProjectByCreatorAddress(String creatorAddress) {
        List<Project> projects = crowdRepo.selectLatestProjectCreatorAddress(creatorAddress);
        if (projects.size() == 1)
            return Optional.of(projects.get(0));
        return Optional.empty();
    }

}
