package VideoServer.Result;

import VideoServer.Common.HttpStatusCode;

public class ActionResult<T> {
    private String message;
    private Integer code;
    private T data;

    private final static String OK = "Success/成功！";
    private final static String NotFound = "NotFound/资源不存在！";
    private final static String ServerError = "ServerError/服务器出现异常！";
    private final static String Failed = "Failed!/请求失败！";

    public ActionResult(){}

    public ActionResult(String message,Integer code)
    {
        this.message=message;
        this.code = code;
    }

    public ActionResult(String message,Integer code,T data)
    {
        this(message,code);
        this.data = data;
    }

    public static ActionResult Success(){return new ActionResult(OK,HttpStatusCode.OK);}

    public static ActionResult Success(String message)
    {
        return new ActionResult(message,HttpStatusCode.OK);
    }

    public static<T> ActionResult<T> SuccessWithData(T data)
    {
        return new ActionResult(OK,HttpStatusCode.OK,data);
    }

    public static<T> ActionResult<T> SuccessWithData(String message,T data)
    {
        return new ActionResult(message,HttpStatusCode.OK,data);
    }

    public static ActionResult Fail()
    {
        return new ActionResult(Failed,HttpStatusCode.Error);
    }

    public static ActionResult Fail(String message)
    {
        return new ActionResult(message,HttpStatusCode.Error);
    }

    public static  ActionResult NotFound()
    {
        return new ActionResult(NotFound,HttpStatusCode.NotFound);
    }

    public static ActionResult Fail(String message,Integer code)
    {
        return new ActionResult(message,code);
    }

    public static ActionResult SeverError() { return new ActionResult(ServerError,HttpStatusCode.ServerError);}


    public String getMessage() {
        return message;
    }

    public Boolean getSuccessful()
    {
        return code.equals(HttpStatusCode.OK);
    }

    public Integer getCode(){
        return code;
    }

    public T getData() {
        return data;
    }
}
