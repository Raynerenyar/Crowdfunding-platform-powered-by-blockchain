package ethereum.repository.sql.auth;

import static ethereum.repository.sql.auth.RoleQueries.*;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ethereum.models.sql.auth.ERole;
import ethereum.models.sql.auth.Role;
import ethereum.models.sql.auth.User;;

@Repository
public class RoleRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public Optional<Role> findByName(ERole name) {
        Object[] args = new Object[] { name };
        int[] argTypes = new int[] { Types.VARCHAR };
        List<Role> users = jdbc.query(FIND_ROLE, args, argTypes,
                BeanPropertyRowMapper.newInstance(Role.class));
        int firstIndex = 0;
        if (users.size() == 1) {
            return Optional.of(users.get(firstIndex));
        }
        return Optional.empty();
    }

    public boolean insertUserRoles(User user) {
        List<Object[]> batchArgs = user.getRoles().stream()
                .map(role -> {
                    return new Object[] { user.getUsername(), role.getId() };
                }).toList();
        int[] argTypes = new int[] { Types.VARCHAR, Types.INTEGER };
        int[] insertedCount = jdbc.batchUpdate(INSERT_USER_INTO_ROLE, batchArgs, argTypes);
        if (insertedCount.length >= 1) {
            return true;
        }
        return false;
    }

    // takes in a user object that already contains the username
    public List<Role> findUserRole(User user) {
        Object[] args = new Object[] { user.getUsername() };
        int[] argTypes = new int[] { Types.VARCHAR };
        return jdbc.query(FIND_USER_ROLES, args, argTypes, BeanPropertyRowMapper.newInstance(Role.class));

    }
}
