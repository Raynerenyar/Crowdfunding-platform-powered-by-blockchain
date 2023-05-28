package ethereum.repository.sql.crowdfunding;

public class Queries {

    public static String INSERT_PROJECT_CREATOR = """
            INSERT INTO project_creators (
                username,
                name,
                password)
            VALUES (?,?,?)
            """;

    public static String INSERT_PROJECT = """
            INSERT INTO projects (
                project_address,
                creator_address,
                title,
                description,
                imageUrl,
                goal,
                deadline,
                completed,
                expired,
                accepting_token,
                token_id,
                created_date)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
            """;

    public static String UPDATE_PROJECT_COMPLETED = """
            UPDATE projects
            SET completed = ?
            WHERE project_address = ?
            """;

    public static String UPDATE_PROJECT_EXPIRED = """
            UPDATE projects
            SET expired = ?
            WHERE project_address = ?
            """;

    public static String INSERT_PROJECT_REQUEST = """
            INSERT INTO project_requests (
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
            UPDATE project_requests
            SET completed = ?
            WHERE request_id = ?
            """;

    public static String INSERT_CONTRIBUTOR = """
            INSERT INTO contributors (
                contributor_address)
            VALUES (?)
            """;

    public static String INSERT_CONTRIBUTION = """
            INSERT INTO contributions (
            contributor_address,
            contribution_amount,
            project_address,
            refunded)
            VALUES (?,?,?,?)
            """;

    public static String UPDATE_STATUS_CONTRIBUTION = """
            UPDATE contributions
            SET refunded = ?
            WHERE contributor_address = ?
            AND
            project_address = ?
            """;

    public static String INSERT_VOTE = """
            INSERT INTO votes (
                request_id,
                contributor_address,
                value_of_vote)
            VALUES (?,?,?)
            """;

    public static String SELECT_REQUESTS = """
            SELECT *
            FROM project_requests
            WHERE project_address = ?
            """;
    /* 
    SELECT *
    FROM projects
    WHERE creator_address = ?
    */
    public static String SELECT_PROJECT_BY_CREATOR_ADDRESS = """
            SELECT p.*, t.token_symbol, t.token_name
            FROM projects p
            JOIN tokens t ON p.token_id = t.token_id
            WHERE creator_address = ?
            ORDER BY created_date DESC
            """;

    public static String SELECT_PROJECT_BY_CREATOR_ADDRESS_FOR_PROJ_ADDRESS = """
            SELECT project_address
            FROM projects
            WHERE creator_address = ?
            """;

    public static String FIND_PROJECT_CREATOR = """
             SELECT *
             FROM project_creators
             WHERE username = ?
            """;
    /*
    SELECT *
    FROM projects
    WHERE project_address = ?
    */
    public static String SELECT_PROJECT_BY_PROJECT_ADDRESS = """
            SELECT p.*, t.token_symbol, t.token_name
            FROM projects p
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
            FROM projects p
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
            FROM project_requests
            WHERE project_address = ?
            """;

    public static String CONTRIBUTOR_EXISTS = """
            SELECT COUNT(*) AS count
            FROM contributors
            WHERE contributor_address = ?
                """;

    public static String SELECT_REQUEST_BY_PROJECTADDRESS_AND_REQUEST_NO = """
            SELECT request_id
            FROM project_requests
            WHERE project_address = ?
            AND request_no = ?
            """;
    public static String SELECT_REQUEST_BY_ID = """
            SELECT *
            FROM project_requests
            WHERE request_id = ?
            """;
    public static String GET_VALUE_OF_VOTES_OF_REQUEST = """
            SELECT SUM(value_of_vote) AS total_value
            FROM votes v
            JOIN project_requests pr ON v.request_id = pr.request_id
            WHERE pr.project_address = ?
            AND pr.request_no = ?;
            """;

    public static String GET_COUNT_OF_VOTES_OF_REQUEST = """
            SELECT COUNT(*) AS total_count
            FROM votes v
            JOIN project_requests pr ON v.request_id = pr.request_id
            WHERE pr.project_address = ?
            AND pr.request_no = ?;
            """;

    public static String GET_TOTAL_CONTRIBUTION_BY_PROJ_A_CONTRIBUTOR_ADDRESS = """
            SELECT SUM(contribution_amount) AS total_contribution
            FROM contributions
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

    public static String UPDATE_USER_PASSWORD = """
            UPDATE project_creators
            SET password = ?
            WHERE username = ?
            """;

}
