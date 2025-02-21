package VideoServer.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@TableName("video")
@Getter
@Setter
public class Video{
    /**
     * 视频id
     */
    private String id;
    /**
     * 视频名称
     */
    private String name;
    /**
     * 视频播放标题
     */
    private String title;
    /**
     * 视频描述
     */
    private String description;

    private String cover;
    /**
     * 视频类型，1.电影，2.连续剧,3.短视频
     */
    private Integer type;
    /**
     * 视频状态，1.正常，0异常
     */
    private Integer status;
    /**
     * 视频创建时间
     */
    private Date createTime;
    private Integer publishYear;
    /**
     * 视频更新时间
     */
    private Date updateTime;

}

