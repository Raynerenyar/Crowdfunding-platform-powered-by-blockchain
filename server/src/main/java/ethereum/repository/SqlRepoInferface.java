package ethereum.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;

import ethereum.models.Project;
import ethereum.models.Request;
import ethereum.models.Token;

public interface SqlRepoInferface {

    public int insertProject(String projectAddress, String creatorAddress, String title, String description, int goal,
            Timestamp deadline, boolean completed, boolean expired, String acceptingtoken, int tokenId,
            Timestamp createdDate);

    public int insertProjectRequest(int requestNo, String projectAddress, String title, String description,
            String recipientAddress, int amount, boolean completed);

    public int insertContributor(String contributorAddress);

    public int insertContribution(String contributorAddress, int contributionAmount, String projectAddress,
            boolean refunded);

    public int insertVote(int requestId, String contributorAddress, int valueOfVotes);

    public int updateRequest(int requestNum, boolean completed);

    public int updateContribution(boolean refunded, String contributorAddress, String projectAddress);

    public int updateProjectCompleted(boolean completed, String projectAddress);

    public int updateProjectExpired(boolean expired, String projectAddress);

    public List<Project> selectProjectByProjectAddress(String projectAddress);

    public List<Request> selectRequestById(int requestId);

    public List<Project> selectProjectByCreatorAddress(String creatorAddress);

    public List<String> selectProjectAddressesByCreatorAddress(String creatorAddress);

    public List<Request> selectRequests(String projectAddress);

    public List<Project> selectProjectsWithPage(int offset, int limit);

    public List<Project> selectLatestProjectCreatorAddress(String creatorAddress);

    public int countProjects();

    public int countRequests(String projectAddress);

    public List<Request> selectRequestId(String projectAddress, int requestNo);

    public int countContributor(String contributorAddress);

    public int getValueOfVotesOfRequest(String projectAddress, int requestNo);

    public int getNumOfVotesOfRequest(String projectAddress, int requestNo);

    public int getNumOfVotesOfRequest(String projectAddress, String contributorAddress);

    public int insertToken(String tokenAddress, String tokenSymbol, String tokenName);

    public List<Token> getListOfTokens();

    public int doesTokenExist(String tokenAddress);

    public int getTokenId(String tokenAddress);
}
