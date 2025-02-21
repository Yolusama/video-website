package VideoServer.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtil {
    public static<T> T construct(Class<T> type,Object... values){
          var fields=type.getDeclaredFields();
          T res=null;
        try {
            T obj = type.getConstructor().newInstance();
            for (int i = 0; i < fields.length; i++) {
                var field = fields[i];
                field.setAccessible(true);
                field.set(obj,values[i]);
            }
            res=obj;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return res;
    }
    public static<T> void copy(T src,T to){
       var type=src.getClass();
       var fields=type.getDeclaredFields();
       try {
           for (var field : fields) {
               field.setAccessible(true);
               field.set(to, field.get(src));
           }
       }
       catch (Exception ex){
           ex.printStackTrace();
       }
    }
    public static<T,Vo> Vo copyToVo(T src,Vo to){
        var srcType=src.getClass();
        var voType=to.getClass();
        var voFields=voType.getDeclaredFields();
        var srcFields = srcType.getDeclaredFields();
        try {
           for(var voField:voFields){
               voField.setAccessible(true);
               if(Arrays.stream(srcFields).filter(e->e.getName().equals(voField.getName())).findFirst().isEmpty()) continue;
               var srcField=srcType.getDeclaredField(voField.getName());
               if(!srcField.getType().equals(voField.getType()))continue;
               srcField.setAccessible(true);
               voField.set(to,srcField.get(src));
           }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return to;
    }
    public static<T,Vo> void copyFromVo(Vo src,T to) throws NoSuchFieldException, IllegalAccessException {
        var srcType = src.getClass();
        var toType = to.getClass();
        var srcFields = srcType.getDeclaredFields();
        var toFields = toType.getDeclaredFields();
        for (var srcField : srcFields) {
            var stream = Arrays.stream(toFields);
            srcField.setAccessible(true);
            if (stream.filter(e -> e.getName().equals(srcField.getName())).findFirst().isEmpty()) continue;
            var toField = toType.getDeclaredField(srcField.getName());
            if(!toField.getType().equals(srcField.getType())) continue;
            toField.setAccessible(true);
            toField.set(to, srcField.get(src));
        }
    }
    public static<T> Map<String,Object> ObjectToMap(T obj) throws IllegalAccessException {
        var type=obj.getClass();
        var fields=type.getDeclaredFields();
        Map<String,Object> res=new HashMap<>();
        for(var field:fields){
            field.setAccessible(true);
            res.put(field.getName(),field.get(obj));
        }
        return res;
    }
}
