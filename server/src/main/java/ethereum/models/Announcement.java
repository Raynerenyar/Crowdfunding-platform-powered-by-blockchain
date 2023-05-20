package ethereum.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    // private String contentType; // announcement or comment
    private String projectAddress;
    private String creatorAddress;
    private String body;
    private Date datetimePosted;
    private Date datetimeEdited;
}
