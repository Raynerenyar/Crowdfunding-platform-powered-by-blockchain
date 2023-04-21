package ethereum.tutorials.java.ethereum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonceResponse {
    private String nonce;
}
