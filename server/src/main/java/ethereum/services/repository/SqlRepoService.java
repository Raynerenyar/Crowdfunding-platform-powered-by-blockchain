package ethereum.services.repository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ethereum.models.sql.crowdfunding.Project;
import ethereum.models.sql.crowdfunding.Request;
import ethereum.repository.sql.crowdfunding.SqlCrowdfundingRepo;

@Service
public class SqlRepoService {
    @Autowired
    private SqlCrowdfundingRepo crowdRepo;
    private static final Logger logger = LoggerFactory.getLogger(SqlRepoService.class);

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

    public Optional<Request> getRequestById(int requestNum) {
        List<Request> requests = crowdRepo.selectRequestById(requestNum);
        if (requests.size() == 1)
            return Optional.of(requests.get(0));
        return Optional.empty();
    }

    public Optional<List<Request>> getRequestsByProjectAddress(String address) {
        List<Request> requests = crowdRepo.selectRequests(address);
        if (requests.size() == 0)
            return Optional.empty();
        return Optional.of(requests);
    }

    public Optional<List<String>> getListOfProjectAddress(String address) {
        List<String> addresses = crowdRepo.selectProjectAddressesByCreatorAddress(address);
        for (String _address : addresses) {
            logger.info("getting list of addresses >> {}", _address);
        }
        if (addresses.size() == 0)
            return Optional.empty();
        return Optional.of(addresses);
    }

    public Optional<List<Project>> getProjectsWithPage(int limit, int offset) {
        List<Project> projects = crowdRepo.selectProjectsWithPage(limit, offset);
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

    public Optional<Integer> getRequestId(String projectAddress, int requestNo) {
        List<Request> req = crowdRepo.selectRequestId(projectAddress, requestNo);
        if (req.size() == 1)
            return Optional.of(req.get(0).getRequestId());
        return Optional.empty();
    }

    public Integer getValueOfVotesOfRequest(String projectAddress, Integer requestNo) {
        int value = crowdRepo.getValueOfVotesOfRequest(projectAddress, requestNo);
        return value;
    }

    public Integer getCountOfVotes(String projectAddress, Integer requestNo) {
        int value = crowdRepo.getNumOfVotesOfRequest(projectAddress, requestNo);
        return value;
    }

}
