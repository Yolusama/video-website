package VideoServer.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "resource.image")
@Getter
@Setter
class ImageConfig
{
    private String[] patterns;
    private String[] locations;
    private String[] paths;
}

@Configuration
@ConfigurationProperties(prefix = "resource.video")
@Getter
@Setter
class VideoConfig
{
    private String[] patterns;
    private String[] locations;
    private String[] paths;
}

@Configuration
@Getter
@Setter
public class ResourceConfig
{
    private final ImageConfig imageConfig;
    private final VideoConfig videoConfig;

    @Autowired
    public ResourceConfig(ImageConfig imageConfig,VideoConfig videoConfig)
    {
       this.imageConfig = imageConfig;
       this.videoConfig = videoConfig;
    }

    public String UserAvatarPath()
    {
        return imageConfig.getPaths()[0];
    }

    public String VideoCoverPath()
    {
        return imageConfig.getPaths()[1];
    }

    public String ShortVideoPath()
    {
        return videoConfig.getPaths()[0];
    }

    public String LongVidePath(){ return videoConfig.getPaths()[1];}
}
