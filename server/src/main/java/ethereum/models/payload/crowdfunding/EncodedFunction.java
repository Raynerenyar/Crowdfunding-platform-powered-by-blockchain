package ethereum.models.payload.crowdfunding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncodedFunction {
    private String encodedFunction;
    private String contractAddress;
}
