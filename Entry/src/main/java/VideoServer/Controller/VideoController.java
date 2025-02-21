package VideoServer.Controller;

import VideoServer.Common.Constants;
import VideoServer.DbOption.Service.VideoService;
import VideoServer.DbOption.ServiceProvider.VideoServiceProvider;
import VideoServer.Entity.VO.PagedData;
import VideoServer.Entity.VO.VideoRecordVO;
import VideoServer.Entity.VO.VideoVO;
import VideoServer.Entity.Video;
import VideoServer.Model.VideoModel;
import VideoServer.Model.VideoRecordModel;
import VideoServer.Result.ActionResult;
import VideoServer.Service.FileService;
import VideoServer.Utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/Video")
public class VideoController {

    private final VideoService videoService;
    private final FileService fileService;

    @Autowired
    public VideoController(VideoServiceProvider videoServiceProvider,FileService fileService)
    {
        videoService = videoServiceProvider;
        this.fileService = fileService;
    }

    @PostMapping("/Upload")
    public CompletableFuture<ActionResult> UploadLong(@RequestPart("current")String current,
                                   @RequestPart("filePart")MultipartFile filePart,
                                   @RequestPart("end") String end,
                                   @RequestPart("fileName")String fileName,
                                   @RequestPart("fileSuffix")String fileSuffix,
                                   @RequestPart("videoTitle") String videoTitle)
            throws IOException, InterruptedException {
        int currentIndex = Integer.parseInt(current);
        int endIndex = Integer.parseInt(end);
        if(currentIndex==0)
        {
            if(!fileName.equals(Constants.NULL))
                fileService.RemoveLongVideo(fileName);
            return CompletableFuture.completedFuture(ActionResult.SuccessWithData(UUID.randomUUID().toString()));
        }
        fileService.ChunkingUpload(currentIndex,fileName,filePart.getInputStream(),endIndex,fileSuffix,videoTitle);
        return CompletableFuture.completedFuture(ActionResult.Success());
    }

    @PutMapping("/SaveVideoInfo")
    public ActionResult<String> FinishUploading(@RequestBody VideoModel model)
    {
        return videoService.SaveVideoInfo(model);
    }

    @GetMapping("/GetVideos")
    public CompletableFuture<ActionResult<PagedData<Video>>> GetVideos(@RequestParam String page,
                                                         @RequestParam String pageSize,
                                                         @RequestParam String title,
                                                         @RequestParam String status,
                                                         @RequestParam String type,
                                                         @RequestParam String year)
    {
        Integer _status = StringUtil.IsNull(status) ? null : Integer.parseInt(status);
        Integer _type = StringUtil.IsNull(type)? null : Integer.parseInt(type);
        Integer _year = StringUtil.IsNull(year)? null: Integer.parseInt(year);
        return CompletableFuture.completedFuture(videoService.GetVideos(
                Integer.parseInt(page),Integer.parseInt(pageSize),title,_type,_status,_year
        ));
    }

    @PatchMapping("/ChangeStatus/{id}")
    public ActionResult ChangeStatus(@RequestParam String status,@PathVariable String id)
    {
        return videoService.ChangeStatus(Integer.parseInt(status),id);
    }

    @GetMapping("/GetVideoInfos")
    public CompletableFuture<ActionResult<PagedData<VideoVO>>> GetVideos(@RequestParam String page,
                                                                         @RequestParam String pageSize,
                                                                         @RequestParam String title,
                                                                         @RequestParam String type,
                                                                         @RequestParam String publishYear)
    {
        Integer _type = StringUtil.IsNull(type)? null : Integer.parseInt(type);
        Integer year = StringUtil.IsNull(publishYear)? null: Integer.parseInt(publishYear);
        return CompletableFuture.completedFuture(videoService.GetVideos(
                Integer.parseInt(page),Integer.parseInt(pageSize),title,_type,year));
    }

    @PutMapping("/SaveVideoRecord")
    public ActionResult SaveVideoRecord(@RequestBody VideoRecordModel model)
    {
        return videoService.SaveVideoRecord(model.getUserId(),model.getVideoId(),model.getCurrentTime(),model.getTotalTime());
    }

    @GetMapping("/GetVideo/{id}")
    public CompletableFuture<ActionResult<VideoVO>> GetVideo(@PathVariable String id)
    {
        return CompletableFuture.completedFuture(videoService.GetVideo(id));
    }

    @PostMapping("/GetCurrentTime")
    public CompletableFuture<ActionResult<Integer>> GetCurrentTime(@RequestBody VideoRecordModel model)
    {
        return CompletableFuture.completedFuture(videoService.GetCurrentTime(model.getUserId(),model.getVideoId()));
    }

    @GetMapping("/GetVideoRecord/{userId}")
    public CompletableFuture<ActionResult<PagedData<VideoRecordVO>>> GetVideoRecord(
            @RequestParam String page,
            @RequestParam String pageSize,
            @PathVariable String userId)
    {
        return CompletableFuture.completedFuture(videoService.GetVideoRecord(
                Integer.parseInt(page),Integer.parseInt(pageSize),userId
        ));
    }

    @PutMapping("/RemoveRecord")
    public ActionResult RemoveRecord(@RequestBody VideoRecordModel model)
    {
        return videoService.RemoveRecord(model.getUserId(),model.getVideoId());
    }

    @DeleteMapping("/ClearRecord/{userId}")
    public ActionResult ClearRecord(@PathVariable String userId)
    {
        return videoService.ClearRecord(userId);
    }

}
