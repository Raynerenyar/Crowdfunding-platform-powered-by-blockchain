package ethereum.tutorials.java.ethereum.login.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ethereum.tutorials.java.ethereum.login.models.ERole;
import ethereum.tutorials.java.ethereum.login.models.Role;
import ethereum.tutorials.java.ethereum.login.models.User;
import ethereum.tutorials.java.ethereum.login.payload.request.LoginRequest;
import ethereum.tutorials.java.ethereum.login.payload.request.SignupRequest;
import ethereum.tutorials.java.ethereum.login.payload.response.MessageResponse;
import ethereum.tutorials.java.ethereum.login.payload.response.UserInfoResponse;
import ethereum.tutorials.java.ethereum.login.repository.RoleRepository;
import ethereum.tutorials.java.ethereum.login.repository.UserRepository;
import ethereum.tutorials.java.ethereum.login.security.jwt.JwtUtils;
import ethereum.tutorials.java.ethereum.login.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

@Controller
// @CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        System.out.println("signing in " + loginRequest.getUsername());
        System.out.println("signing in " + loginRequest.getPassword());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(
                        userDetails.getUsername(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.findProjectCreator(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }
        System.out.println("signing up" + signUpRequest.getUsername());
        System.out.println(signUpRequest.getPassword());
        // Create new user with username and encoded password
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        /* commented out as these statements allow anyone to register as mod or admin */
        System.out.println("assigning user role...");
        // if (strRoles == null) {
        //     Role userRole = roleRepository.findByName(ERole.ROLE_USER)
        //             .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        //     roles.add(userRole);
        // } else {
        //     strRoles.forEach(role -> {
        //         System.out.println("sign up role >> " + role);
        //         switch (role) {
        //             case "admin":
        //                 Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
        //                         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        //                 roles.add(adminRole);

        //                 break;
        //             case "mod":
        //                 Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
        //                         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        //                 roles.add(modRole);

        //                 break;
        //             default:
        //                 Role userRole = roleRepository.findByName(ERole.ROLE_USER)
        //                         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        //                 roles.add(userRole);
        //         }
        //     });
        // }

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        roleRepository.insertUserRoles(user);
        userRepository.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
