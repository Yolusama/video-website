package VideoServer.Entity.VO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDisplayVO {
    private String id;
    private String email;
    private String name;
    private String avatar;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date lastLoginTime;
}
