package VideoServer.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("videorole")
@Data
public class VideoRole{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer type;
}
