package VideoServer.Model;

import lombok.Data;

@Data
public class VideoModel {
    private String id;
    private String title;
    private String name;
    private String description;
    private String cover;
    private Integer type;;
    private Integer totalLength;
    private Integer publishYear;
}
