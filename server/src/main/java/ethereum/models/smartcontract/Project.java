package ethereum.models.smartcontract;

import java.math.BigInteger;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private int goal;
    private long deadline;
    private String tokenAddress;
    private String title;
}
