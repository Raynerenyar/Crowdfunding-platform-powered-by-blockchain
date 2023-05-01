package ethereum.tutorials.java.ethereum.models;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    private int requestId;
    private String projectAddress;
    private String description;
    private String recipientAddress;
    private BigInteger amount;
    private BigInteger numOfVotes;
    private boolean completed;
    private BigInteger valueOfVotes;
}
