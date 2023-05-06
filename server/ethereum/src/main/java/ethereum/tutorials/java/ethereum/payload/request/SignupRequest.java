package ethereum.tutorials.java.ethereum.payload.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<String> role;
}
