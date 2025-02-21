package VideoServer.Exception;

import VideoServer.Result.ActionResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler
    public ActionResult handle(Exception ex)
    {
        ex.printStackTrace();
        return ActionResult.SeverError();
    }
}
