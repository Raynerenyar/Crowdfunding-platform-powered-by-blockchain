package ethereum.models.smartcontract;

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
