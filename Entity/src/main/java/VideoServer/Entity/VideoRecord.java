package VideoServer.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@TableName("videorecord")
@Getter
@Setter
public class VideoRecord {
    @TableId(type= IdType.AUTO)
    private Long id;
    private String userId;
    private String videoId;
    private Integer current;
    private Integer total;
    private Date updateTime;
}
