package VideoServer.DbOption.Mapper;

import VideoServer.Entity.VideoRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoRecordMapper extends BaseMapper<VideoRecord> {
    @Select("select current from VideoRecord where userId=#{userId} and videoId=#{videoId}")
    Integer getCurrentTime(@Param("userId")String userId,@Param("videoId")String videoId);
    @Delete("delete from VideoRecord where userId = #{userId}")
    Integer clearRecord(@Param("userId")String userId);
}
