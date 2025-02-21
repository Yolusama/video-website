package VideoServer.Entity.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoVO
{
    private String id;
    private String name;
    private String title;
    private String description;
    private String cover;
    private Integer type;
    private Integer publishYear;
}
