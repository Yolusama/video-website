package VideoServer.Common;

public class HttpStatusCode {

    private HttpStatusCode(){}
    public static final Integer Unauthorized = 401;
    public static final Integer NotAcceptable = 406;
    public static final Integer OK = 200;
    public static final Integer NotFound = 404;
    public static final Integer ServerError = 500;
    public static final Integer Error = 600;
}
