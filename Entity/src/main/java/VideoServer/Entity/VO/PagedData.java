package VideoServer.Entity.VO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedData<T>{
    private List<T> data;
    private Long total;
}
