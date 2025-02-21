package VideoServer.Common;

import java.util.Date;

public class Constants {
    private Constants(){}
    public static final Integer NormalStatus = 1;
    public static final Integer AbnormalStatus = 0;
    public static final String JwtTokenSign = "token";
    public static final String JwtUserClaim = "UserId";
    public static final String DefaultAvatar = "default.png";
    public static final String DefaultVideoCover = "default-cover.png";
    public static final String NULL = "null";
    public static final String CheckCodeSign = "CheckCode";
    public static final String DefaultUserPassword = "123456";
    public static final Long ChunkSize = 1024*1024*10L;
    public static final Integer WeekDayCount = 7;
    public static final Long WeekExpire = 1000*60*60*24*7*1L;
    public static final Integer LoginCheckLength = 5;
    public static Date CurrentTime(){
        return new Date();
    }
}
