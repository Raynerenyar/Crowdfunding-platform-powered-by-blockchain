package ethereum.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ethereum.models.payload.request.LoginRequest;
import ethereum.models.payload.request.SignupRequest;
import ethereum.models.payload.response.MessageResponse;
import ethereum.models.payload.response.UserInfoResponse;
import ethereum.models.sql.auth.ERole;
import ethereum.models.sql.auth.Role;
import ethereum.models.sql.auth.User;
import ethereum.repository.sql.auth.RoleRepository;
import ethereum.repository.sql.auth.UserRepository;
import ethereum.security.jwt.JwtUtils;
import ethereum.security.services.NonceService;
import ethereum.security.services.UserDetailsImpl;
import ethereum.services.ethereum.BlockchainService;
import ethereum.util.misc.Util;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;

@Controller
// @CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private NonceService authSvc;
    @Autowired
    private BlockchainService bcSvc;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("login request obj >> {} & {} & {}", loginRequest.getUsername(), loginRequest.getPassword(),
                loginRequest.getSigned());

        // verify if signed message is valid, otherwise bounce with error
        Boolean isVerified = this.bcSvc.verifySignedMessage(loginRequest.getSigned(), loginRequest.getNonce(),
                loginRequest.getUsername());
        if (!isVerified)
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Signed message cannot be verified"));

        // from here on verify password and username
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
        logger.info("signup request obj >> {} & {} & {} * {}", signUpRequest.getUsername(),
                signUpRequest.getSigned(), signUpRequest.getNonce());
        // verify if signed message is valid, otherwise bounce with error
        Boolean isVerified = this.bcSvc.verifySignedMessage(signUpRequest.getSigned(), signUpRequest.getNonce(),
                signUpRequest.getUsername());
        if (!isVerified)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Signed message cannot be verified"));

        if (userRepository.findProjectCreator(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }
        // Create new user with username and encoded password
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        /* commented out as these statements allow anyone to register as mod or admin */
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

    @PostMapping(path = "/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody SignupRequest loginRequest) {
        logger.info("change password request obj >> {} & {} & {} * {}", loginRequest.getUsername(),
                loginRequest.getSigned(), loginRequest.getNonce());
        // verify if signed message is valid, otherwise bounce with error
        Boolean isVerified = this.bcSvc.verifySignedMessage(loginRequest.getSigned(), loginRequest.getNonce(),
                loginRequest.getUsername());
        if (!isVerified)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Signed message cannot be verified"));

        User user = new User(loginRequest.getUsername(),
                encoder.encode(loginRequest.getPassword()));

        userRepository.updateUser(user);
        return ResponseEntity.ok(new MessageResponse("Password changed successfully!"));
    }

    @PostMapping(path = "/get-nonce", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getNonce(@RequestBody String body) {

        logger.info("endpoint from AuthController.java");
        JsonObject jsonObjResqBody = Util.readJson(body);
        String address = jsonObjResqBody.getString("address");
        logger.info("Getting nonce for >> {}", address);
        try {
            String nonce = authSvc.getNonce(address);
            String nonceResponse = Json.createObjectBuilder()
                    .add("nonce", nonce)
                    .build()
                    .toString();
            return ResponseEntity.status(HttpStatus.OK).body(nonceResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(path = "/get-url")
    public ResponseEntity<String> getUrl(@RequestParam String param) {
        return null;
    }

}
