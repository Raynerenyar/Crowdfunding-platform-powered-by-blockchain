package ethereum.tutorials.java.ethereum.models;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private String projectAddress;
    private String creatorAddress;
    private String description;
    private BigInteger goal;
    private BigInteger deadline;
    private BigInteger raisedAmount;
    private boolean completed;
    private BigInteger numOfRequests;
    private String acceptingToken;
}
