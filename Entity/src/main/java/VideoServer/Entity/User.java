package VideoServer.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("user")
@Data
public class User {
    @TableId
    private String id;
    private String email;
    private String name;
    private String password;
    private String avatar;
    private Date createTime;
    private Date lastLoginTime;
    private Integer type;
    private Integer status;
}
