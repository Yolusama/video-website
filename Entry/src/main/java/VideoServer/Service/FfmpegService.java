package VideoServer.Service;

import VideoServer.Functional.ZipFileMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.CompletableFuture;

@Service
public class FfmpegService {
    @Value("${ffmpeg.path}")
    private String execPath;

    private void Dispatch(String[] command,File outputDir,File input,String videoTitle) {
            // 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);

           CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    try {
                    System.out.println("ffmpeg开始执行...");
                    Process process = processBuilder.start();
                    // 获取输出流和错误流并输出
                    InputStream stderr = process.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null)
                        System.out.println(line);

                    // 等待命令执行完成
                    process.waitFor();

                    process.destroy();
                    System.out.println(String.format("视频已切割完成至%s",outputDir.getPath()));
                    ZipFileMaker.Make(new File(String.format("%s\\%s.zip",
                            outputDir.getPath(),videoTitle)),outputDir);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally {
                        System.out.println("ffmpeg命令执行完毕。");
                        input.delete();
                    }
                }
            });
    }

    public void ChunkVideo(File input, File outputDir,String fileName,String videoTitle) throws IOException, InterruptedException {
        String[] cmd = {
                execPath,
                "-i", input.getPath(),
                "-c:v","libx264",
                "-c:a", "aac",
                "-hls_time","30", // Segment duration in seconds
                "-hls_list_size", "0", // 0 means keep all segments in the list
                "-hls_segment_filename", outputDir.getPath()+"\\"+fileName+"_%d.ts",
                outputDir.getPath()+"\\"+fileName+"_index.m3u8"
        };

        Dispatch(cmd,outputDir,input,videoTitle);

    }

    public void AggregateVideo(File input,File output)
    {
        String[] command = {
                execPath, "-i", input.getPath(), "-c", "copy","-bsf:a","aac_adtstoasc", output.getPath()
        };
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("ffmpeg开始执行...");
                    Process process = processBuilder.start();
                    // 获取输出流和错误流并输出
                    InputStream stderr = process.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null)
                        System.out.println(line);

                    // 等待命令执行完成
                    process.waitFor();

                    process.destroy();
                    System.out.println(String.format("视频m3u8与ts文件已合并成%s", output.getPath()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    System.out.println("ffmpeg命令执行完毕。");
                }
            }
        });

    }
}
