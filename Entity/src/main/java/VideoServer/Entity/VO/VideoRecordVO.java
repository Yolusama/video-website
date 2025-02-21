package VideoServer.Entity.VO;

import lombok.Data;

import java.util.Date;

@Data
public class VideoRecordVO {
    private String videoId;
    private String videoCover;
    private String videoTitle;
    private Date updateTime;
    private Integer currentTime;
    private Integer totalTime;
}
