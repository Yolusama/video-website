package VideoServer.Controller;

import VideoServer.Caching.RedisCache;
import VideoServer.Configuration.JwtConfig;
import VideoServer.DbOption.Service.UserService;
import VideoServer.DbOption.ServiceProvider.UserServiceProvider;
import VideoServer.Entity.VO.PagedData;
import VideoServer.Entity.VO.UserDisplayVO;
import VideoServer.Entity.VO.UserVO;
import VideoServer.Model.UserEmailLoginModel;
import VideoServer.Model.UserLoginModel;
import VideoServer.Model.UserTokenModel;
import VideoServer.Result.ActionResult;
import VideoServer.Service.EmailService;
import VideoServer.Utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/User")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final RedisCache redis;
    private final JwtConfig jwtConfig;

    @Autowired
    public UserController(UserServiceProvider userServiceProvider,EmailService emailService, RedisCache redis,JwtConfig jwtConfig)
    {
         userService = userServiceProvider;
         this.emailService = emailService;
         this.redis = redis;
         this.jwtConfig = jwtConfig;
    }

    @PostMapping("/Login")
    public ActionResult<UserVO> Login(@RequestBody UserLoginModel model)
    {
        return userService.Login(model.getAccount(),model.getPassword(),jwtConfig,redis);
    }

    @DeleteMapping("/RemoveToken/{id}")
    public ActionResult RemoveToken(@PathVariable String id) throws Exception {
        return userService.RemoveToken(id,redis);
    }

    @PostMapping("/VerifyToken")
    public ActionResult<Boolean> VerifyToken(@RequestBody UserTokenModel model)
    {
        return userService.VerifyToken(model.getId(),model.getToken(),redis);
    }

    @GetMapping("/GetUsers")
    public CompletableFuture<ActionResult<PagedData<UserDisplayVO>>> GetUsers(@RequestParam String page,
                                                                              @RequestParam String pageSize,
                                                                              @RequestParam String queryKey,
                                                                              @RequestParam String status,
                                                                              @RequestParam String type)
    {
        Integer _status = StringUtil.IsNull(status) ? null:Integer.parseInt(status);
        Integer _type = StringUtil.IsNull(type) ? null:Integer.parseInt(type);
        return CompletableFuture.completedFuture(
                userService.GetUsers(Integer.parseInt(page),Integer.parseInt(pageSize),queryKey,
                        _status,_type)
        );
    }

    @PatchMapping("/ChangeStatus/{id}")
    public ActionResult ChangeStatus(@RequestParam String status,@PathVariable String id)
    {
        return userService.ChangeStatus(Integer.parseInt(status),id);
    }

    @PostMapping("/EmailLogin")
    public ActionResult<UserVO> EmailLogin(@RequestBody UserEmailLoginModel model){
        return userService.EmailLogin(model.getEmail(),model.getPassword(),jwtConfig,redis);
    }

    @GetMapping("/AskForCheckCode/{length}")
    public ActionResult AskForCheckCode(@RequestParam String email,@PathVariable String length) throws MessagingException {
        Integer len = Integer.parseInt(length);
        return userService.GenerateCheckCode(len,redis,email,emailService);
    }

    @PostMapping("/IdentifyLogin/{checkCode}")
    public ActionResult<UserVO> IdentifyLogin(@RequestParam String email,
                                              @PathVariable String checkCode)
    {
        return userService.CheckCodeLogin(checkCode,email, jwtConfig,redis);
    }

    @PutMapping("/Register/{checkCode}")
    public ActionResult Register(@PathVariable String checkCode,@RequestParam String email)
    {
        return userService.Register(checkCode,email,redis);
    }

    @GetMapping("/GetUser/{id}")
    public CompletableFuture<ActionResult<UserDisplayVO>> GetUser(@PathVariable String id)
    {
        return CompletableFuture.completedFuture(userService.GetUser(id));
    }

    @PatchMapping("/ChangeName/{id}")
    public ActionResult ChangeName(@RequestParam String newName,@PathVariable String id)
    {
        return userService.ChangeName(id,newName);
    }

    @PutMapping("/ChangeAvatar/{id}")
    public ActionResult ChangeAvatar(@RequestParam String newAvatar,@PathVariable String id)
    {
       return userService.ChangeAvatar(id,newAvatar);
    }

    @PatchMapping("/ChangePassowrd")
    public ActionResult ChangePassword(@RequestBody UserEmailLoginModel model)
    {
        return userService.ChangePassword(model.getEmail(),model.getPassword());
    }

}
