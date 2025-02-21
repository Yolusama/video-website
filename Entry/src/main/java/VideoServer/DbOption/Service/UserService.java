package VideoServer.DbOption.Service;

import VideoServer.Caching.RedisCache;
import VideoServer.Configuration.JwtConfig;
import VideoServer.Entity.User;
import VideoServer.Entity.VO.PagedData;
import VideoServer.Entity.VO.UserDisplayVO;
import VideoServer.Entity.VO.UserVO;
import VideoServer.Result.ActionResult;
import VideoServer.Service.EmailService;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.mail.MessagingException;

public interface UserService extends IService<User> {
    ActionResult<UserVO> Login(String account, String password, JwtConfig jwtConfig, RedisCache redis);
    ActionResult RemoveToken(String id,RedisCache redis) throws Exception;
    ActionResult<Boolean> VerifyToken(String id,String token,RedisCache redis);
    ActionResult<PagedData<UserDisplayVO>> GetUsers(Integer page, Integer pageSize, String queryKey, Integer status, Integer type);
    ActionResult ChangeStatus(Integer status,String id);
    ActionResult<UserVO> EmailLogin(String email,String password,JwtConfig jwtConfig,RedisCache redis);
    ActionResult GenerateCheckCode(Integer length, RedisCache redis, String email, EmailService emailService) throws MessagingException;
    ActionResult<UserVO> CheckCodeLogin(String checkCode,String email,JwtConfig jwtConfig,RedisCache redis);
    ActionResult Register(String checkCode,String email,RedisCache redis);
    ActionResult<UserDisplayVO> GetUser(String id);
    ActionResult ChangeName(String id,String newName);
    ActionResult ChangePassword(String email,String newPassword);
    ActionResult ChangeAvatar(String id,String newAvatar);
}
