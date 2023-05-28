package ethereum.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ethereum.models.sql.auth.Role;
import ethereum.models.sql.auth.User;
import ethereum.repository.sql.auth.RoleRepository;
import ethereum.repository.sql.auth.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findProjectCreator(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // get user's roles that are in db
        List<Role> userRoles = roleRepository.findUserRole(user);
        logger.info("getting user role >> " + userRoles.size());
        // logger.info("getting user role >> " );
        Set<Role> setRoles = new HashSet<>();
        setRoles.addAll(userRoles);
        user.setRoles(setRoles);

        //  
        return UserDetailsImpl.build(user);
    }

}
