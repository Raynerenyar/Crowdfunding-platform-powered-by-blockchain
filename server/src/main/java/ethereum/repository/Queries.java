package ethereum.repository;

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
                completed,
                expired,
                accepting_token,
                token_id,
                created_date)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
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
                request_no,
                project_address,
                title,
                description,
                recipient_address,
                amount,
                completed)
            VALUES (?,?,?,?,?,?,?)
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

    public static String UPDATE_STATUS_CONTRIBUTION = """
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

    public static String SELECT_REQUESTS = """
            SELECT *
            FROM ProjectRequests
            WHERE project_address = ?
            """;
    /* 
        SELECT *
        FROM projects
        WHERE creator_address = ?
    */
    public static String SELECT_PROJECT_BY_CREATOR_ADDRESS = """
            SELECT p.*, t.token_symbol, t.token_name
            FROM Projects p
            JOIN tokens t ON p.token_id = t.token_id
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
    /*
        SELECT *
        FROM projects
        WHERE project_address = ?
    */
    public static String SELECT_PROJECT_BY_PROJECT_ADDRESS = """
            SELECT p.*, t.token_symbol, t.token_name
            FROM Projects p
            JOIN tokens t ON p.token_id = t.token_id
            WHERE project_address = ?
            """;

    /*        
        SELECT *
        FROM projects
        ORDER BY created_date DESC
        LIMIT ?, ?
    */
    public static String SELECT_PROJECTS_W_PAGE = """
            SELECT p.*, t.token_symbol, t.token_name
            FROM Projects p
            JOIN tokens t ON p.token_id = t.token_id
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

    public static String CONTRIBUTOR_EXISTS = """
            SELECT COUNT(*) AS count
            FROM contributors
            WHERE contributor_address = ?
                """;

    public static String SELECT_REQUEST_BY_PROJECTADDRESS_AND_REQUEST_NO = """
            SELECT request_id
            FROM projectrequests
            WHERE project_address = ?
            AND request_no = ?
            """;
    public static String SELECT_REQUEST_BY_ID = """
            SELECT *
            FROM projectRequests
            WHERE request_id = ?
            """;
    public static String GET_VALUE_OF_VOTES_OF_REQUEST = """
            SELECT SUM(value_of_vote) AS total_value
            FROM Votes v
            JOIN ProjectRequests pr ON v.request_id = pr.request_id
            WHERE pr.project_address = ?
            AND pr.request_no = ?;
            """;

    public static String GET_COUNT_OF_VOTES_OF_REQUEST = """
            SELECT COUNT(*) AS total_count
            FROM Votes v
            JOIN ProjectRequests pr ON v.request_id = pr.request_id
            WHERE pr.project_address = ?
            AND pr.request_no = ?;
            """;

    public static String GET_TOTAL_CONTRIBUTION_BY_PROJ_A_CONTRIBUTOR_ADDRESS = """
            SELECT SUM(contribution_amount) AS total_contribution
            FROM crowdfunding2.contributions
            WHERE contributor_address = ?
            AND project_address = ?;
            """;

    public static String INSERT_TOKEN_ADDRESS = """
            INSERT INTO tokens (
            token_address, token_symbol, token_name)
            VALUES (?,?,?)
            """;

    public static String SELECT_TOKENS = """
            SELECT * FROM tokens
            """;

    public static String COUNT_TOKEN = """
            SELECT COUNT(*) AS token_count
            FROM tokens
            WHERE token_address = ?
            """;

    public static String GET_TOKEN_ID = """
            SELECT token_id
            FROM  tokens
            WHERE token_address = ?
            """;
}
