package VideoServer.Controller;

import VideoServer.Common.Constants;
import VideoServer.Common.HttpStatusCode;
import VideoServer.Entity.ProperytyEnum.FileType;
import VideoServer.Result.ActionResult;
import VideoServer.Service.FileService;
import VideoServer.Utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/Common")
public class CommonController
{
    private final FileService fileService;
    @Autowired
    public CommonController(FileService fileService)
    {
        this.fileService = fileService;
    }

    @GetMapping("/HeartBeat")
    public ActionResult HeartBeat()
    {
        return ActionResult.Success("芝士心跳请求...");
    }

    @PostMapping("/Upload")
    public ActionResult Upload(@RequestPart("file") MultipartFile file,
                               @RequestPart("originalFileName")String originalFileName,
                               @RequestPart("type")String type) throws IOException {
        Integer _type = Integer.parseInt(type);
        if(_type.equals(FileType.UserAvatar.Value())&&!originalFileName.equals(Constants.DefaultAvatar))
            fileService.RemoveUserAvatar(originalFileName);
        else if(_type.equals(FileType.VideoCover.Value())&&!originalFileName.equals(Constants.DefaultVideoCover))
            fileService.RemoveVideoCover(originalFileName);
        else if(_type.equals(FileType.ShortVideo.Value()))
            fileService.RemoveShortVideo(originalFileName);
        String newFileName = String.format("%s.%s",UUID.randomUUID().toString(),
                StringUtil.GetFileNameSuffix(file.getOriginalFilename()));
        if(_type.equals(FileType.UserAvatar.Value()))
            fileService.UploadUserAvatar(newFileName,file.getInputStream());
        else if(_type.equals(FileType.VideoCover.Value()))
            fileService.UploadVideoCover(newFileName,file.getInputStream());
        else if(_type.equals(FileType.ShortVideo.Value()))
            fileService.UploadShortVideo(newFileName,file.getInputStream());
        return ActionResult.SuccessWithData("已成功上传！",newFileName);
    }

    @GetMapping("/DownloadVideo")
    public HttpEntity<byte[]> DownloadVideo(@RequestParam String videoName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + videoName);
        byte[] res = fileService.RequestDownload(videoName);
        return ResponseEntity.ok().headers(headers).body(res);
    }
}
