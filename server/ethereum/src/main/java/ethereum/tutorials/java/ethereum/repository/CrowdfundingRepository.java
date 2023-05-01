package ethereum.tutorials.java.ethereum.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ethereum.tutorials.java.ethereum.models.Project;
import ethereum.tutorials.java.ethereum.models.ProjectRequest;

import java.sql.Types;
import java.util.List;

import static ethereum.tutorials.java.ethereum.repository.Queries.*;

@Repository
public class CrowdfundingRepository {
    @Autowired
    private JdbcTemplate jdbc;

    public int insertProjectCreator(String creatorAddress, String name) {
        Object[] args = new Object[] { creatorAddress, name };
        int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR };
        return jdbc.update(INSERT_PROJECT_CREATOR, args, argTypes);
    }

    public int insertProject(
            String projectAddress,
            String creatorAddress,
            String title,
            String description,
            int goal,
            int deadline,
            int raisedAmount,
            boolean completed,
            boolean expired,
            int numOfRequests,
            String acceptingtoken) {
        Object[] args = new Object[] {
                projectAddress,
                creatorAddress,
                title,
                description,
                goal,
                deadline,
                raisedAmount,
                completed,
                expired,
                numOfRequests,
                acceptingtoken
        };
        int[] argTypes = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.BOOLEAN,
                Types.BOOLEAN,
                Types.INTEGER,
                Types.VARCHAR };
        return jdbc.update(INSERT_PROJECT, args, argTypes);
    }

    public int insertProjectRequest(
            String projectAddress,
            String title,
            String recipientAddress,
            int amount,
            int numOfVotes,
            boolean completed,
            double value_of_votes) {
        Object[] args = new Object[] {
                projectAddress,
                title,
                recipientAddress,
                amount,
                numOfVotes,
                completed,
                value_of_votes
        };
        int[] argTypes = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
                Types.BOOLEAN,
                Types.DOUBLE
        };
        return jdbc.update(INSERT_PROJECT_REQUEST, args, argTypes);
    }

    public int insertContributor(String contributorAddress) {
        Object[] args = new Object[] { contributorAddress };
        int[] argTypes = new int[] { Types.VARCHAR };
        return jdbc.update(INSERT_CONTRIBUTOR, args, argTypes);
    }

    public int insertContribution(
            String contributorAddress,
            int contributionAmount,
            String projectAddress,
            boolean refunded) {
        Object[] args = new Object[] {
                contributorAddress,
                contributionAmount,
                projectAddress,
                refunded };
        int[] argTypes = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.VARCHAR,
                Types.BOOLEAN };
        return jdbc.update(INSERT_CONTRIBUTION, args, argTypes);
    }

    public int insertVote(
            int requestId,
            String contributorAddress,
            int valueOfVotes) {
        Object[] args = new Object[] {
                requestId,
                contributorAddress,
                valueOfVotes
        };
        int[] argTypes = new int[] {
                Types.INTEGER,
                Types.VARCHAR,
                Types.DOUBLE
        };
        return jdbc.update(INSERT_VOTE, args, argTypes);
    }

    public int updateRequest(int requestNum, boolean completed) {
        Object[] args = new Object[] { completed, requestNum };
        int[] argTypes = new int[] { Types.BOOLEAN, Types.INTEGER };
        return jdbc.update(UPDATE_REQUEST_COMPLETED, args, argTypes);
    }

    public int updateContribution(
            boolean refunded,
            String contributorAddress,
            String projectAddress) {
        Object[] args = new Object[] {
                refunded,
                contributorAddress,
                projectAddress };
        int[] argTypes = new int[] {
                Types.BOOLEAN,
                Types.VARCHAR,
                Types.VARCHAR };
        return jdbc.update(UPDATE_CONTRIBUTION, args, argTypes);
    }

    public int updateProjectCompleted(boolean completed, String projectAddress) {
        Object[] args = new Object[] { completed, projectAddress };
        int[] argTypes = new int[] { Types.BOOLEAN, Types.VARCHAR };
        return jdbc.update(UPDATE_PROJECT_COMPLETED, args, argTypes);
    }

    public int updateProjectExpired(boolean expired, String projectAddress) {
        Object[] args = new Object[] { expired, projectAddress };
        int[] argTypes = new int[] { Types.BOOLEAN, Types.VARCHAR };
        return jdbc.update(UPDATE_PROJECT_EXPIRED, args, argTypes);
    }

    public List<Project> selectProject(String projectAddress) {
        Object[] args = new Object[] { projectAddress };
        int[] argTypes = new int[] { Types.VARCHAR };
        return jdbc.query(SELECT_PROJECT, args, argTypes, BeanPropertyRowMapper.newInstance(Project.class));
    }

    public List<ProjectRequest> selectRequest(String projectAddress) {
        Object[] args = new Object[] { projectAddress };
        int[] argTypes = new int[] { Types.VARCHAR };
        return jdbc.query(SELECT_PROJECT, args, argTypes, BeanPropertyRowMapper.newInstance(ProjectRequest.class));
    }
}
