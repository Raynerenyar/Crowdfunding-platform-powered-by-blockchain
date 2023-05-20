package ethereum.models;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private String projectAddress;
    private String creatorAddress;
    private String title;
    private String description;
    private Integer goal; // might need BigInteger
    private Timestamp deadline;
    private Integer raisedAmount; // might need BigInteger
    private boolean completed;
    private boolean expired;
    private Integer numOfRequests; // might need BigInteger
    private String acceptingToken;
    private String tokenName;
    private String tokenSymbol;
    private Timestamp createdDate;
}
