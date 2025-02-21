package VideoServer.DbOption.Mapper;

import VideoServer.Entity.User;
import VideoServer.Entity.VO.UserDisplayVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    List<UserDisplayVO> getUsers(Page<UserDisplayVO> page, @Param("queryKey")String queryKey,@Param("status")Integer status,
                                 @Param("type")Integer type);
}
