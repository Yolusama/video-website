package VideoServer.Service;

import VideoServer.Configuration.ResourceConfig;
import VideoServer.Entity.ProperytyEnum.FileType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;

@Service
public class FileService
{
    @Resource
    private ResourceConfig resourceConfig;
    @Resource
    private FfmpegService ffmpegService;

    private final String M3U8Suffix = "m3u8";
    private final String Mp4Suffix = "mp4";

    private void SaveFile(String root,String fileName,InputStream stream) throws IOException {
        File file = new File(root+fileName);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream output = new FileOutputStream(file);
        output.write(stream.readAllBytes());
        output.close();
        stream.close();
    }

    private void RemoveFile(String root,String fileName)
    {
        File file = new File(root+fileName);
        if(file.exists())
            file.delete();
    }

    public void ChunkingUpload(Integer index, String fileName, InputStream input,Integer endIndex,
                               String fileSuffix,String videoTitle) throws IOException, InterruptedException {
        File file = new File(String.format("%s%s.%s",resourceConfig.LongVidePath(),fileName,fileSuffix));
        if(index==0)
        {
            file.createNewFile();
        }
        else
        {
            OutputStream output = new FileOutputStream(file,true);
            output.write(input.readAllBytes());
            output.close();
            input.close();
            if(index.equals(endIndex))
            {
                File dir = new File(resourceConfig.LongVidePath()+fileName);
                dir.mkdir();
                ffmpegService.ChunkVideo(file,dir,fileName,videoTitle);
            }
        }
    }

    public byte[] RequestDownload(String videoName) throws IOException {
        File  file = new File(String.format("%s\\%s",resourceConfig.ShortVideoPath(),videoName));
        if(!file.exists())
            return null;
        FileInputStream input = new FileInputStream(file);
        byte[] res = input.readAllBytes();
        input.close();
        return res;
    }

    public File AggregateM3U8Video(String videoName)
    {
        File video = new File(String.format("%s\\%s\\%s_index.%s",resourceConfig.LongVidePath(),videoName,videoName,M3U8Suffix));
        File outPutVideo = new File(String.format("%s\\%s_index.%s",resourceConfig.LongVidePath(),videoName,Mp4Suffix));
        ffmpegService.AggregateVideo(video,outPutVideo);
        return outPutVideo;
    }

    public void UploadUserAvatar(String fileName,InputStream stream) throws IOException {
        SaveFile(resourceConfig.UserAvatarPath(),fileName,stream);
    }

    public void UploadShortVideo(String fileName,InputStream stream) throws IOException {
        SaveFile(resourceConfig.ShortVideoPath(),fileName,stream);
    }

    public void UploadVideoCover(String fileName,InputStream stream) throws IOException {
        SaveFile(resourceConfig.VideoCoverPath(),fileName,stream);
    }

    public void RemoveUserAvatar(String fileName)
    {
        RemoveFile(resourceConfig.UserAvatarPath(),fileName);
    }

    public void RemoveShortVideo(String fileName)
    {
        RemoveFile(resourceConfig.ShortVideoPath(),fileName);
    }

    public void RemoveVideoCover(String fileName)
    {
        RemoveFile(resourceConfig.VideoCoverPath(),fileName);
    }

    public void RemoveLongVideo(String fileName){
        File longVideo = new File(String.format("%s\\%s",resourceConfig.LongVidePath(),fileName));
        File[] files = longVideo.listFiles();
        for(var file:files)
          {
              file.delete();
          }
        longVideo.delete();
    }
}
