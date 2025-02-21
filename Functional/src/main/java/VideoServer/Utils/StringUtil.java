package VideoServer.Utils;

public class StringUtil {
    private static final char Dot = '.';

    public static Boolean IsNull(String str)
    {
        return str == null || str.isEmpty();
    }

    public static String GetFileNameSuffix(String fileName)
    {
        int index = fileName.lastIndexOf(Dot);
        if(index<0)
            return "";
        return fileName.substring(index+1);
    }
}
