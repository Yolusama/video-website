package VideoServer.Entity.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO {
    private String id;
    private String email;
    private String name;
    private String token;
    private String avatar;
    private Integer type;
}
