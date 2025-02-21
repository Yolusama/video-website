package VideoServer.DbOption.ServiceProvider;

import VideoServer.Common.Constants;
import VideoServer.Common.HttpStatusCode;
import VideoServer.DbOption.Mapper.VideoMapper;
import VideoServer.DbOption.Mapper.VideoRecordMapper;
import VideoServer.DbOption.Service.VideoService;
import VideoServer.Entity.VO.PagedData;
import VideoServer.Entity.VO.VideoRecordVO;
import VideoServer.Entity.VO.VideoVO;
import VideoServer.Entity.Video;
import VideoServer.Entity.VideoRecord;
import VideoServer.Model.VideoModel;
import VideoServer.Result.ActionResult;
import VideoServer.Utils.ObjectUtil;
import VideoServer.Utils.RandomGenerator;
import VideoServer.Utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoServiceProvider extends ServiceImpl<VideoMapper, Video> implements VideoService {
    private final VideoMapper mapper;
    private final VideoRecordMapper videoRecordMapper;

    @Autowired
    public VideoServiceProvider(VideoMapper mapper,VideoRecordMapper videoRecordMapper)
    {
        this.mapper = mapper;
        this.videoRecordMapper = videoRecordMapper;
    }

    @Override
    @Transactional
        public ActionResult<String> SaveVideoInfo(VideoModel model){
        String videoId = StringUtil.IsNull(model.getId())? RandomGenerator.GenerateVideoId():model.getId();
        Video video = new Video();
        video.setTitle(model.getTitle());
        video.setDescription(model.getDescription());
        video.setName(model.getName());
        video.setType(model.getType());
        video.setPublishYear(model.getPublishYear());
        if(!StringUtil.IsNull(model.getCover()))
            video.setCover(model.getCover());
        video.setId(videoId);
        video.setCreateTime(Constants.CurrentTime());
        video.setUpdateTime(video.getCreateTime());
        saveOrUpdate(video);
        return ActionResult.SuccessWithData(videoId);
    }

    @Override
    public ActionResult<PagedData<Video>> GetVideos(Integer page, Integer pageSize, String title, Integer type, Integer status,
                                                    Integer year) {
        PagedData<Video> res = new PagedData<>();
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Video::getTitle,title);
        if(type!=null)
            wrapper.eq(Video::getType,type);
        if(status!=null)
            wrapper.eq(Video::getStatus,status);
        if(year!=null)
            wrapper.eq(Video::getPublishYear,year);
        Page<Video> _page = mapper.selectPage(Page.of(page,pageSize),wrapper);
        res.setTotal(_page.getTotal());
        res.setData(_page.getRecords());
        return ActionResult.SuccessWithData(res);
    }

    @Override
    public ActionResult ChangeStatus(Integer status, String id) {
        Boolean res = lambdaUpdate().set(Video::getStatus,status).set(Video::getUpdateTime,Constants.CurrentTime()).eq(Video::getId,id).update();
        return res? ActionResult.Success("更新状态成功！") : ActionResult.Fail("更新状态失败！");
    }

    @Override
    public ActionResult<PagedData<VideoVO>> GetVideos(Integer page, Integer pageSize, String title, Integer type, Integer publishYear) {
        Page<VideoVO> _page = Page.of(page,pageSize);
        List<VideoVO> videos = mapper.GetVideos(_page,type,title,publishYear);
        PagedData<VideoVO> res = new PagedData<>();
        res.setData(videos);
        res.setTotal(_page.getTotal());
        return ActionResult.SuccessWithData(res);
    }

    @Override
    public ActionResult<VideoVO> GetVideo(String id) {
        Video video = mapper.selectById(id);
        if(video==null)
            return ActionResult.Fail("视频资源不存在！", HttpStatusCode.NotFound);
        return ActionResult.SuccessWithData(ObjectUtil.copyToVo(video,new VideoVO()));

    }

    @Override
    @Transactional
    public ActionResult SaveVideoRecord(String userId, String videoId, Integer currentTime, Integer totalTime)
    {
        VideoRecord record = videoRecordMapper.selectOne(
                new LambdaQueryWrapper<VideoRecord>().eq(VideoRecord::getUserId,userId).
                        eq(VideoRecord::getVideoId,videoId)
        );
        if(record == null)
        {
            record = new VideoRecord();
            record.setUserId(userId);
            record.setVideoId(videoId);
            record.setCurrent(currentTime);
            record.setTotal(totalTime);
            record.setUpdateTime(Constants.CurrentTime());
            videoRecordMapper.insert(record);
        }
        else
        {
            record.setCurrent(currentTime);
            record.setUpdateTime(Constants.CurrentTime());
            videoRecordMapper.updateById(record);
        }
        return ActionResult.Success("已成功保存视频观看记录！");
    }

    @Override
    public ActionResult<Integer> GetCurrentTime(String userId, String videoId) {
        Integer res = videoRecordMapper.getCurrentTime(userId,videoId);
        if(res==null)
            return ActionResult.NotFound();
        return ActionResult.SuccessWithData(res);
    }

    @Override
    public ActionResult<PagedData<VideoRecordVO>> GetVideoRecord(Integer page, Integer pageSize, String userId) {
        Page<VideoRecordVO> _page = Page.of(page,pageSize);
        List<VideoRecordVO> data = mapper.GetVideoRecords(_page,userId);
        PagedData<VideoRecordVO> res = new PagedData<>();
        res.setData(data);
        res.setTotal(_page.getTotal());
        return ActionResult.SuccessWithData(res);
    }

    @Override
    @Transactional
    public ActionResult RemoveRecord(String userId, String videoId) {
        int res = videoRecordMapper.delete(
                new LambdaQueryWrapper<VideoRecord>().eq(VideoRecord::getUserId,userId).
                        eq(VideoRecord::getVideoId,videoId)
        );
        if(res==0)
            return ActionResult.NotFound();
        return ActionResult.Success("已移除此纪录！");
    }

    @Override
    public ActionResult ClearRecord(String userId) {
        Integer res = videoRecordMapper.clearRecord(userId);
        if(res==0)
            return ActionResult.NotFound();
        return ActionResult.Success("已清空历史记录！");
    }
}
