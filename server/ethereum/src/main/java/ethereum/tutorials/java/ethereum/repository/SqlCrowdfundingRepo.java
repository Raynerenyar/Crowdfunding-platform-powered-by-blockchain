package ethereum.tutorials.java.ethereum.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ethereum.tutorials.java.ethereum.eventHandler.BlockchainEventHandler;
import ethereum.tutorials.java.ethereum.models.Project;
import ethereum.tutorials.java.ethereum.models.Request;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import static ethereum.tutorials.java.ethereum.repository.Queries.*;

@Repository
public class SqlCrowdfundingRepo {
        @Autowired
        private JdbcTemplate jdbc;

        // public int insertProjectCreator(String creatorAddress, String name) {
        //         Object[] args = new Object[] { creatorAddress, name };
        //         int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR };
        //         return jdbc.update(INSERT_PROJECT_CREATOR, args, argTypes);
        // }

        public int insertProject(
                        String projectAddress,
                        String creatorAddress,
                        String title,
                        String description,
                        int goal,
                        Timestamp deadline,
                        int raisedAmount,
                        boolean completed,
                        boolean expired,
                        int numOfRequests,
                        String acceptingtoken,
                        String tokenName,
                        String tokenSymbol,
                        Timestamp createdDate) {
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
                                acceptingtoken,
                                tokenName,
                                tokenSymbol,
                                createdDate
                };
                int[] argTypes = new int[] {
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.INTEGER,
                                Types.TIMESTAMP,
                                Types.INTEGER,
                                Types.BOOLEAN,
                                Types.BOOLEAN,
                                Types.INTEGER,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.TIMESTAMP };
                return jdbc.update(INSERT_PROJECT, args, argTypes);
        }

        public int insertProjectRequest(
                        int requestNo,
                        String projectAddress,
                        String title,
                        String description,
                        String recipientAddress,
                        int amount,
                        boolean completed) {
                Object[] args = new Object[] {
                                requestNo,
                                projectAddress,
                                title,
                                description,
                                recipientAddress,
                                amount,
                                completed };
                int[] argTypes = new int[] {
                                Types.INTEGER,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.INTEGER,
                                Types.BOOLEAN };
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
                        String projectAddress,
                        int amount) {
                Object[] args = new Object[] {
                                refunded,
                                contributorAddress,
                                projectAddress,
                                amount };
                int[] argTypes = new int[] {
                                Types.BOOLEAN,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.INTEGER };
                return jdbc.update(UPDATE_STATUS_CONTRIBUTION, args, argTypes);
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

        public List<Project> selectProjectByProjectAddress(String projectAddress) {
                Object[] args = new Object[] { projectAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(SELECT_PROJECT_BY_PROJECT_ADDRESS, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Project.class));
        }

        public List<Request> selectRequestById(int requestId) {
                Object[] args = new Object[] { requestId };
                int[] argTypes = new int[] { Types.INTEGER };
                return jdbc.query(SELECT_REQUEST_BY_ID, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Request.class));
        }

        public List<Project> selectProjectByCreatorAddress(String creatorAddress) {
                Object[] args = new Object[] { creatorAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(SELECT_PROJECT_BY_CREATOR_ADDRESS, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Project.class));
        }

        public List<String> selectProjectAddressesByCreatorAddress(String creatorAddress) {
                Object[] args = new Object[] { creatorAddress };
                return jdbc.queryForList(SELECT_PROJECT_BY_CREATOR_ADDRESS_FOR_PROJ_ADDRESS, String.class, args);
        }

        public List<Request> selectRequests(String projectAddress) {
                Object[] args = new Object[] { projectAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(SELECT_REQUESTS, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Request.class));
        }

        public List<Project> selectProjectsWithPage(int offset, int limit) {
                Object[] args = new Object[] { offset, limit };
                return jdbc.query(SELECT_PROJECTS_W_PAGE, BeanPropertyRowMapper.newInstance(Project.class),
                                args);
        }

        public List<Project> selectLatestProjectCreatorAddress(String creatorAddress) {
                Object[] args = new Object[] { creatorAddress };
                return jdbc.query(SELECT_PROJECTS_W_PAGE_BY_DATE,
                                BeanPropertyRowMapper.newInstance(Project.class),
                                args);
        }

        public int countProjects() {
                return jdbc.query(COUNT_PROJECTS, new ResultSetExtractor<Integer>() {
                        @Override
                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("count(*)");
                        }
                });
        }

        public int countRequests(String projectAddress) {
                Object[] args = new Object[] { projectAddress };
                return jdbc.query(COUNT_REQUESTS_BY_PROJECT_ADDRESS, new ResultSetExtractor<Integer>() {
                        @Override
                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("count(*)");
                        }
                }, args);
        }

        public List<Request> selectRequestId(String projectAddress, int requestNo) {
                Object[] args = new Object[] { projectAddress, requestNo };
                int[] argTypes = new int[] { Types.VARCHAR, Types.INTEGER };
                return jdbc.query(SELECT_REQUEST_BY_PROJECTADDRESS_AND_REQUEST_NO, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Request.class));

        }

        public int countContributor(String contributorAddress) {
                Object[] args = new Object[] { contributorAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(CONTRIBUTOR_EXISTS, args, argTypes, new ResultSetExtractor<Integer>() {

                        @Override
                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("count");
                        }
                });

        }

        public int getValueOfVotesOfRequest(String projectAddress, int requestNo) {
                Object[] args = new Object[] { projectAddress, requestNo };
                int[] argTypes = new int[] { Types.VARCHAR, Types.INTEGER };
                return jdbc.query(GET_VALUE_OF_VOTES_OF_REQUEST, args, argTypes, new ResultSetExtractor<Integer>() {

                        @Override
                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("total_value");
                        }

                });
        }

        public int getNumOfVotesOfRequest(String projectAddress, int requestNo) {
                Object[] args = new Object[] { projectAddress, requestNo };
                int[] argTypes = new int[] { Types.VARCHAR, Types.INTEGER };
                return jdbc.query(GET_COUNT_OF_VOTES_OF_REQUEST, args, argTypes, new ResultSetExtractor<Integer>() {

                        @Override
                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("total_count");
                        }

                });
        }
}
