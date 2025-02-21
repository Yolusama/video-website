package VideoServer.Inteceptor;

import VideoServer.Caching.RedisCache;
import VideoServer.Common.Constants;
import VideoServer.Common.HttpStatusCode;
import VideoServer.Configuration.JwtConfig;
import VideoServer.Functional.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizer implements HandlerInterceptor {
    private final JwtConfig config;
    private final RedisCache redisCache;

    @Autowired
    public JwtAuthorizer(JwtConfig config, RedisCache redisCache) {
        this.config = config;
        this.redisCache = redisCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(!(handler instanceof HandlerMethod))
            return true;
        try {
            String token = request.getHeader(Constants.JwtTokenSign);
            if(token==null||token.isEmpty())
            {
                response.setStatus(HttpStatusCode.NotAcceptable);
                return false;
            }
            else
            {
                String userId = JwtParser.Parse(config.getSecretKey(),token);
                if(userId == null)
                {
                    response.setStatus(HttpStatusCode.NotAcceptable);
                    return false;
                }
                String tokenKey = String.format("%s_%s",userId, Constants.JwtTokenSign);
                if(!redisCache.GetItem(tokenKey).equals(token))
                {
                    response.setStatus(HttpStatusCode.Unauthorized);
                    return false;
                }
                return true;
            }
        }
        catch (Exception ex)
        {
            response.setStatus(HttpStatusCode.ServerError);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
