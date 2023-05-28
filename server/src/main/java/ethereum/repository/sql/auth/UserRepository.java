package ethereum.repository.sql.auth;

import static ethereum.repository.sql.crowdfunding.Queries.*;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ethereum.models.sql.auth.User;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbc;

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

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
        logger.info("inserting project creator, {}", user.getUsername());
        return jdbc.update(INSERT_PROJECT_CREATOR, args, argTypes);
    }

    public int updateUser(User user) {
        Object[] args = new Object[] { user.getPassword(), user.getUsername() };
        int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR };
        return jdbc.update(UPDATE_USER_PASSWORD, args, argTypes);
    }
}
