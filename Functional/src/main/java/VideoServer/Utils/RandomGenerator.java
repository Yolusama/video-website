package VideoServer.Utils;

import java.util.Random;

public class RandomGenerator
{
    private RandomGenerator(){}
    private static final String SourceTable = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String UserIdSign = "U";
    private static final String VideoIdSign = "kv";
    public static String GenerateUserId()
    {
        StringBuilder res = new StringBuilder();
        res.append(UserIdSign);
        Random random = new Random(System.currentTimeMillis());
        int count = random.nextInt(3)+8;
        res.append(random.nextInt(9)+1);
        for(int i=1;i<count;i++)
            res.append(SourceTable.charAt(random.nextInt(10)));
        return res.toString();
    }

    public static String GenerateVideoId()
    {
        StringBuilder res = new StringBuilder();
        res.append(VideoIdSign);
        Random random = new Random(System.currentTimeMillis());
        int length = 10 + random.nextInt(3);
        for(int i=0;i<length;i++)
            res.append(SourceTable.charAt(random.nextInt(10)));
        return res.toString();
    }

    public static String Generate(int count)
    {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder res = new StringBuilder();
        for(int i=0;i<count;i++)
            res.append(random.nextInt(10));
        return res.toString();
    }
}
