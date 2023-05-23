package ethereum.models.mongo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String projectAddress;
    private String posterAddress;
    private String body;
    private Date datetimePosted;
}
