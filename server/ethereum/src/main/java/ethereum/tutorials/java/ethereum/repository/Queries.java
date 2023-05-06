package ethereum.tutorials.java.ethereum.repository;

import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;

public class Queries {

        public static String INSERT_PROJECT_CREATOR = """
                        INSERT INTO ProjectCreators (
                            username,
                            name,
                            password)
                        VALUES (?,?,?)
                        """;

        public static String INSERT_PROJECT = """
                        INSERT INTO Projects (
                            project_address,
                            creator_address,
                            title,
                            description,
                            goal,
                            deadline,
                            raised_amount,
                            completed,
                            expired,
                            num_of_requests,
                            accepting_token,
                            created_date)
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                        """;

        public static String UPDATE_PROJECT_COMPLETED = """
                        UPDATE Projects
                        SET completed = ?
                        WHERE project_address = ?
                        """;

        public static String UPDATE_PROJECT_EXPIRED = """
                        UPDATE Projects
                        SET expired = ?
                        WHERE project_address = ?
                        """;

        public static String INSERT_PROJECT_REQUEST = """
                        INSERT INTO ProjectRequests (
                            project_address,
                            title,
                            description,
                            recipient_address,
                            amount,
                            num_of_votes,
                            completed,
                            value_of_votes)
                        VALUES (?,?,?,?,?,?,?,?)
                        """;

        public static String UPDATE_REQUEST_COMPLETED = """
                        UPDATE ProjectRequests
                        SET completed = ?
                        WHERE request_id = ?
                        """;

        public static String INSERT_CONTRIBUTOR = """
                        INSERT INTO Contributors (
                            contributor_address)
                        VALUES (?)
                        """;

        public static String INSERT_CONTRIBUTION = """
                        INSERT INTO Contributions (
                            contributor_address,
                            contribution_amount,
                            project_address,
                            refunded)
                        VALUES (?,?,?,?)
                        """;

        public static String UPDATE_CONTRIBUTION = """
                        UPDATE Contributions
                        SET refunded = ?
                        WHERE contributor_address = ?
                        AND
                        project_address = ?
                        """;

        public static String INSERT_VOTE = """
                        INSERT INTO Votes (
                            request_id,
                            contributor_address,
                            value_of_vote)
                        VALUES (?,?,?)
                        """;

        // public static String SELECT_PROJECT = """
        //                 SELECT
        //                 creator_address,
        //                 description,
        //                 goal,
        //                 deadline,
        //                 raised_amount,
        //                 completed,
        //                 num_of_requests,
        //                 accepting_token
        //                 FROM Projects
        //                 WHERE project_address = ?
        //                 """;

        public static String SELECT_REQUEST = """
                        SELECT *
                        FROM ProjectRequests
                        WHERE project_address = ?
                        """;

        public static String SELECT_PROJECT_BY_CREATOR_ADDRESS = """
                        SELECT *
                        FROM projects
                        WHERE creator_address = ?
                        """;

        public static String SELECT_PROJECT_BY_CREATOR_ADDRESS_FOR_PROJ_ADDRESS = """
                        SELECT project_address
                        FROM projects
                        WHERE creator_address = ?
                        """;

        public static String FIND_PROJECT_CREATOR = """
                         SELECT *
                         FROM ProjectCreators
                         WHERE username = ?
                        """;

        public static String SELECT_PROJECT_BY_PROJECT_ADDRESS = """
                        SELECT *
                        FROM projects
                        WHERE project_address = ?
                        """;

        public static String SELECT_PROJECTS_W_PAGE = """
                        SELECT *
                        FROM projects
                        ORDER BY created_date DESC
                        LIMIT ?, ?
                        """;

        public static String SELECT_PROJECTS_W_PAGE_BY_DATE = """
                        SELECT *
                        FROM projects
                        WHERE creator_address = ?
                        ORDER BY created_date DESC
                        LIMIT 1;
                        """;

        public static String SELECT_ALL_PROJECTS = """
                        SELECT *
                        FROM projects
                        """;

        public static String COUNT_PROJECTS = """
                        SELECT COUNT(*)
                        FROM projects
                        """;

        public static String COUNT_REQUESTS_BY_PROJECT_ADDRESS = """
                        SELECT COUNT(*)
                        FROM projectrequests
                        WHERE project_address = ?
                        """;
}
