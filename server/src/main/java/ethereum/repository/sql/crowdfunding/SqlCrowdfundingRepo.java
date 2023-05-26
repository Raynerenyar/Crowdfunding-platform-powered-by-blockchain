package ethereum.repository.sql.crowdfunding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ethereum.models.sql.crowdfunding.Project;
import ethereum.models.sql.crowdfunding.Request;
import ethereum.models.sql.crowdfunding.Token;
import ethereum.services.ethereum.eventHandler.BlockchainEventHandler;

import static ethereum.repository.sql.crowdfunding.Queries.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

@Repository
public class SqlCrowdfundingRepo implements SqlRepoInferface {

        @Autowired
        private JdbcTemplate jdbc;

        public int insertProject(
                        String projectAddress,
                        String creatorAddress,
                        String title,
                        String description,
                        String imageUrl,
                        long goal,
                        Timestamp deadline,
                        boolean completed,
                        boolean expired,
                        String acceptingtoken,
                        int tokenId,
                        Timestamp createdDate) {
                Object[] args = new Object[] {
                                projectAddress,
                                creatorAddress,
                                title,
                                description,
                                imageUrl,
                                goal,
                                deadline,
                                completed,
                                expired,
                                acceptingtoken,
                                tokenId,
                                createdDate
                };
                int[] argTypes = new int[] {
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.VARCHAR,
                                Types.INTEGER,
                                Types.TIMESTAMP,
                                Types.BOOLEAN,
                                Types.BOOLEAN,
                                Types.VARCHAR,
                                Types.INTEGER,
                                Types.TIMESTAMP
                };
                return jdbc.update(INSERT_PROJECT, args, argTypes);
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

                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("count(*)");
                        }
                });
        }

        public int insertToken(String tokenAddress, String tokenSymbol, String tokenName) {
                Object[] args = new Object[] { tokenAddress, tokenSymbol, tokenName };
                int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
                return jdbc.update(INSERT_TOKEN_ADDRESS, args, argTypes);
        }

        public List<Token> getListOfTokens() {
                return jdbc.query(SELECT_TOKENS, BeanPropertyRowMapper.newInstance(Token.class));
        }

        public int doesTokenExist(String tokenAddress) {
                Object[] args = new Object[] { tokenAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(COUNT_TOKEN, args, argTypes,
                                new ResultSetExtractor<Integer>() {

                                        public Integer extractData(ResultSet rs)
                                                        throws SQLException, DataAccessException {
                                                rs.next();
                                                return rs.getInt("token_count");
                                        }
                                });
        }

        public int getTokenId(String tokenAddress) {
                Object[] args = new Object[] { tokenAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(GET_TOKEN_ID, args, argTypes,
                                new ResultSetExtractor<Integer>() {

                                        public Integer extractData(ResultSet rs)
                                                        throws SQLException, DataAccessException {
                                                rs.next();
                                                return rs.getInt("token_id");
                                        }
                                });
        }

        public int insertContributor(String contributorAddress) {
                Object[] args = new Object[] { contributorAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.update(INSERT_CONTRIBUTOR, args, argTypes);
        }

        public int insertContribution(
                        String contributorAddress,
                        long contributionAmount,
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

        public int updateContribution(
                        boolean refunded,
                        String contributorAddress,
                        String projectAddress) {
                Object[] args = new Object[] {
                                refunded,
                                contributorAddress,
                                projectAddress
                };
                int[] argTypes = new int[] {
                                Types.BOOLEAN,
                                Types.VARCHAR,
                                Types.VARCHAR
                };
                return jdbc.update(UPDATE_STATUS_CONTRIBUTION, args, argTypes);
        }

        public int countContributor(String contributorAddress) {
                Object[] args = new Object[] { contributorAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(CONTRIBUTOR_EXISTS, args, argTypes, new ResultSetExtractor<Integer>() {

                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("count");
                        }
                });
        }

        public int insertProjectRequest(
                        int requestNo,
                        String projectAddress,
                        String title,
                        String description,
                        String recipientAddress,
                        long amount,
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

        public int insertVote(
                        int requestId,
                        String contributorAddress,
                        long valueOfVotes) {
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

        public int updateRequest(int requestId, boolean completed) {
                Object[] args = new Object[] { completed, requestId };
                int[] argTypes = new int[] { Types.BOOLEAN, Types.INTEGER };
                return jdbc.update(UPDATE_REQUEST_COMPLETED, args, argTypes);
        }

        public List<Request> selectRequestById(int requestId) {
                Object[] args = new Object[] { requestId };
                int[] argTypes = new int[] { Types.INTEGER };
                return jdbc.query(SELECT_REQUEST_BY_ID, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Request.class));
        }

        public List<Request> selectRequests(String projectAddress) {
                Object[] args = new Object[] { projectAddress };
                int[] argTypes = new int[] { Types.VARCHAR };
                return jdbc.query(SELECT_REQUESTS, args, argTypes,
                                BeanPropertyRowMapper.newInstance(Request.class));

        }

        public int countRequests(String projectAddress) {
                Object[] args = new Object[] { projectAddress };
                return jdbc.query(COUNT_REQUESTS_BY_PROJECT_ADDRESS, new ResultSetExtractor<Integer>() {

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

        public int getValueOfVotesOfRequest(String projectAddress, int requestNo) {
                Object[] args = new Object[] { projectAddress, requestNo };
                int[] argTypes = new int[] { Types.VARCHAR, Types.INTEGER };
                return jdbc.query(GET_VALUE_OF_VOTES_OF_REQUEST, args, argTypes, new ResultSetExtractor<Integer>() {

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

                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                                rs.next();
                                return rs.getInt("total_count");
                        }
                });
        }

        public int getNumOfVotesOfRequest(String projectAddress, String contributorAddress) {
                Object[] args = new Object[] { projectAddress, contributorAddress };
                int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR };
                return jdbc.query(GET_TOTAL_CONTRIBUTION_BY_PROJ_A_CONTRIBUTOR_ADDRESS, args, argTypes,
                                new ResultSetExtractor<Integer>() {

                                        public Integer extractData(ResultSet rs)
                                                        throws SQLException, DataAccessException {
                                                rs.next();
                                                return rs.getInt("total_contribution");
                                        }

                                });
        }
}
