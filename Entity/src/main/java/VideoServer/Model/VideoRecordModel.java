package VideoServer.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRecordModel
{
    private String videoId;
    private String userId;
    private Integer currentTime;
    private Integer totalTime;
}
