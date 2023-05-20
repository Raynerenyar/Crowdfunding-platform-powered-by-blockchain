package ethereum.repository;

import static ethereum.repository.Queries.*;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ethereum.models.User;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbc;

    public Optional<User> findProjectCreator(String creatorAddress) {
        Object[] args = new Object[] { creatorAddress };
        int[] argTypes = new int[] { Types.VARCHAR };
        List<User> users = jdbc.query(FIND_PROJECT_CREATOR, args, argTypes,
                BeanPropertyRowMapper.newInstance(User.class));
        int firstIndex = 0;
        if (users.size() == 1) {
            return Optional.of(users.get(firstIndex));
        }
        return Optional.empty();
    }

    // insert project creator
    public int saveUser(User user) {
        String name = "user's name";
        Object[] args = new Object[] { user.getUsername(), name, user.getPassword() };
        int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
        return jdbc.update(INSERT_PROJECT_CREATOR, args, argTypes);
    }
}
