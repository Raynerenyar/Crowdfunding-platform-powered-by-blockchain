package ethereum.tutorials.java.ethereum.models;

import java.math.BigInteger;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BcRequest {
    private String title;
    private String recipient;
    private BigInteger amount;
    private Boolean completed;
    private BigInteger noOfVoters;
    private BigInteger valueOfVotes;
}
