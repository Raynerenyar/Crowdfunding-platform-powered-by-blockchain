package ethereum.repository.sql.auth;

public class RoleQueries {

        public static String FIND_ROLE = """
                        SELECT *
                        FROM roles
                        WHERE name = ?
                        """;

        public static String INSERT_USER_INTO_ROLE = """
                        INSERT INTO user_roles (
                                user_address,
                                role_id
                        ) VALUES (?,?)
                        """;

        public static String FIND_USER_ROLES = """
                        SELECT project_creators.username,roles.id,roles.name
                        FROM project_creators
                        JOIN user_roles
                        ON project_creators.username=user_roles.user_address
                        JOIN roles
                        ON user_roles.role_id=roles.id
                        WHERE project_creators.username = ?
                        """;

}
