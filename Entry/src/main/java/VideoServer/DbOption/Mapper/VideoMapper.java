package VideoServer.DbOption.Mapper;

import VideoServer.Entity.VO.VideoRecordVO;
import VideoServer.Entity.VO.VideoVO;
import VideoServer.Entity.Video;
import VideoServer.Entity.VideoRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video>
{
    List<VideoVO> GetVideos(Page<VideoVO> page,@Param("type")Integer type,@Param("title") String title,
                            @Param("publishYear")Integer publishYear);
    List<VideoRecordVO> GetVideoRecords(Page<VideoRecordVO> page,@Param("userId") String userId);
}
