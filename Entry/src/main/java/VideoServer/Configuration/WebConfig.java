package VideoServer.Configuration;

import VideoServer.Inteceptor.JwtAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport
{
    private final JwtAuthorizer jwtAuthorizer;
    private final ResourceConfig resourceConfig;

    private final String[] allowedOrigins={"http://localhost:5050","http://localhost:5055"};
    private final String[] notInterceptedUrls={"/User/Login","/User/Register/**","/User/EmailLogin",
            "/Common/HeartBeat","/User/VerifyToken","/image","/video","/User/IdentifyLogin/**",
            "/User/RemoveToken","/User/AskForCheckCode/**","/User/ResetPassword","/Video/GetVideoInfos",
    "/Video/GetVideo/**"};
    private final String apiPattern = "/**";
    private final String[] allowedMethods = {"GET","POST","DELETE","PUT","PATCH"};

    @Autowired
    public WebConfig(JwtAuthorizer jwtAuthorizer,ResourceConfig resourceConfig)
    {
       this.jwtAuthorizer = jwtAuthorizer;
       this.resourceConfig = resourceConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthorizer).excludePathPatterns(notInterceptedUrls);
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
       registry.addMapping(apiPattern).allowedOrigins(allowedOrigins)
               .allowedMethods(allowedMethods);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceConfig.getImageConfig().getPatterns()).
                addResourceLocations(resourceConfig.getImageConfig().getLocations());
        registry.addResourceHandler(resourceConfig.getVideoConfig().getPatterns()).
                addResourceLocations(resourceConfig.getVideoConfig().getLocations());
    }
}
