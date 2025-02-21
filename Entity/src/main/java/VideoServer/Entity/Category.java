package VideoServer.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("category")
@Data
public class Category {
    @TableId(type= IdType.AUTO)
    private Long id;
    private String name;
    private Date createTime;
}
