package ethereum.tutorials.java.ethereum.login.repository;

public class Queries {

        public static String FIND_PROJECT_CREATOR = """
                         SELECT *
                         FROM ProjectCreators
                         WHERE username = ?
                        """;

        public static String FIND_ROLE = """
                        SELECT *
                        FROM roles
                        WHERE name = ?
                        """;

        public static String INSERT_PROJECT_CREATOR = """
                        INSERT INTO ProjectCreators (
                            username,
                            name,
                            password)
                        VALUES (?,?,?)
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
// select roles.id,roles.name from projectcreators join userroles on projectcreators.username=userroles.user_address join roles on userroles.role_id=roles.id where projectcreators.username="0x4"