package ethereum.tutorials.java.ethereum.login.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private String username;
    private List<String> roles;
}
