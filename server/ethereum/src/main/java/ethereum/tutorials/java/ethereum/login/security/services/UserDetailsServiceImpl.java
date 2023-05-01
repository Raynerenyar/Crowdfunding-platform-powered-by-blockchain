package ethereum.tutorials.java.ethereum.login.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ethereum.tutorials.java.ethereum.login.models.Role;
import ethereum.tutorials.java.ethereum.login.models.User;
import ethereum.tutorials.java.ethereum.login.repository.RoleRepository;
import ethereum.tutorials.java.ethereum.login.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findProjectCreator(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        System.out.println("userdetailsservice >>> " + user.getUsername());

        // get user's roles that are in db
        List<Role> userRoles = roleRepository.findUserRole(user);
        Set<Role> setRoles = new HashSet<>();
        setRoles.addAll(userRoles);
        user.setRoles(setRoles);

        //  
        return UserDetailsImpl.build(user);
    }

}
