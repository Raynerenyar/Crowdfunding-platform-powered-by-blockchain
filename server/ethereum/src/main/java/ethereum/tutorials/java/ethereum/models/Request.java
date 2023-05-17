package ethereum.tutorials.java.ethereum.models;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private int requestId;
    private int requestNo;
    private String projectAddress;
    private String title;
    private String description;
    private String recipientAddress;
    private Integer amount; // might need BigInteger
    private Integer numOfVotes; // might need BigInteger
    private boolean completed;
}
