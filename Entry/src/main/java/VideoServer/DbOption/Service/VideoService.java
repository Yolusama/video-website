package VideoServer.DbOption.Service;

import VideoServer.Entity.VO.PagedData;
import VideoServer.Entity.VO.VideoRecordVO;
import VideoServer.Entity.VO.VideoVO;
import VideoServer.Entity.Video;
import VideoServer.Model.VideoModel;
import VideoServer.Result.ActionResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface VideoService extends IService<Video> {
    ActionResult<String> SaveVideoInfo(VideoModel model);
    ActionResult<PagedData<Video>> GetVideos(Integer page,Integer pageSize,String title,Integer type,
                                             Integer status,Integer year);
    ActionResult ChangeStatus(Integer status,String id);
    ActionResult<PagedData<VideoVO>> GetVideos(Integer page,Integer pageSize,String title,Integer type,
                                               Integer publishYear);
    ActionResult<VideoVO> GetVideo(String id);
    ActionResult SaveVideoRecord(String userId,String videoId,Integer currentTime,Integer totalTime);
    ActionResult<Integer> GetCurrentTime(String userId,String videoId);
    ActionResult<PagedData<VideoRecordVO>> GetVideoRecord(Integer page, Integer pageSize, String userId);
    ActionResult RemoveRecord(String userId,String videoId);
    ActionResult ClearRecord(String userId);
}
