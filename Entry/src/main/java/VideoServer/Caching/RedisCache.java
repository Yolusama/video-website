package VideoServer.Caching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class RedisCache {
    private RedisTemplate<String, Object> template;

    public RedisCache(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void SetItem(String key, Object value){
        template.opsForValue().set(key,value);
    }
    public void SetItem(String key, Object value,Long expire)
    {
        long now=new Date().getTime();
        template.opsForValue().set(key,value,Duration.ofSeconds((expire+now)/1000));
    }
    public void SetItem(String key,Object value,Duration expire){
        template.opsForValue().set(key,value,expire);
    }
    public Object GetItem(String key)
    {
        if(template.hasKey(key))
            return template.opsForValue().get(key);
        else return null;
    }
    public void RemoveItem(String key) throws Exception{
            if(template.hasKey(key))
                template.delete(key);
            else throw new Exception("key对应的value不存在！");
    }
    public boolean HasKey(String key){
        return template.hasKey(key);
    }
}
