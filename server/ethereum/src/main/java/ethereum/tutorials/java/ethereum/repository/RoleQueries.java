package ethereum.tutorials.java.ethereum.repository;

public class RoleQueries {

        public static String FIND_ROLE = """
                        SELECT *
                        FROM roles
                        WHERE name = ?
                        """;

        public static String INSERT_USER_INTO_ROLE = """
                        INSERT INTO userRoles (
                                user_address,
                                role_id
                        ) VALUES (?,?)
                        """;

        public static String FIND_USER_ROLES = """
                        SELECT projectcreators.username,roles.id,roles.name
                        FROM projectcreators
                        JOIN userroles
                        ON projectcreators.username=userroles.user_address
                        JOIN roles
                        ON userroles.role_id=roles.id
                        WHERE projectcreators.username = ?
                        """;

}
