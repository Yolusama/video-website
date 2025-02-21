package VideoServer.DbOption.ServiceProvider;

import VideoServer.Caching.RedisCache;
import VideoServer.Common.Constants;
import VideoServer.Common.HttpStatusCode;
import VideoServer.Configuration.JwtConfig;
import VideoServer.DbOption.Mapper.UserMapper;
import VideoServer.DbOption.Service.UserService;
import VideoServer.Entity.ProperytyEnum.UserType;
import VideoServer.Entity.User;
import VideoServer.Entity.VO.PagedData;
import VideoServer.Entity.VO.UserDisplayVO;
import VideoServer.Entity.VO.UserVO;
import VideoServer.Functional.JwtGenerator;
import VideoServer.Result.ActionResult;
import VideoServer.Service.EmailService;
import VideoServer.Utils.Md5Util;
import VideoServer.Utils.ObjectUtil;
import VideoServer.Utils.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.Duration;

@Service
public class UserServiceProvider extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper mapper;

    @Autowired
    public UserServiceProvider(UserMapper mapper)
    {
        this.mapper = mapper;
    }

    private String  LoganDataCaching(JwtConfig jwtConfig,String payload,RedisCache redis)
    {
        final String key = String.format("%s_%s",payload, Constants.JwtTokenSign);
        String token = JwtGenerator.Generate(jwtConfig.getSecretKey(),payload,Constants.WeekExpire,
                jwtConfig.getIssuser(),jwtConfig.getAudience());
        redis.SetItem(key,token,Duration.ofDays(Constants.WeekDayCount));
        return token;
    }
    @Override
    @Transactional
    public ActionResult<UserVO> Login(String account, String password, JwtConfig jwtConfig, RedisCache redis) {
        var wrapper = new LambdaQueryWrapper<User>().
                eq(User::getId,account).or().eq(User::getEmail,account);
        User user = getOne(wrapper);
        if(user == null)
            return ActionResult.Fail("用户不存在！", HttpStatusCode.NotFound);
        if(!Md5Util.Check(password,user.getPassword()))
            return ActionResult.Fail("密码错误！");
        UserVO res = new UserVO();
        ObjectUtil.copyToVo(user,res);
        res.setToken(LoganDataCaching(jwtConfig,user.getId(),redis));
        user.setLastLoginTime(Constants.CurrentTime());
        mapper.updateById(user);
        return ActionResult.SuccessWithData("登录成功！",res);
    }

    @Override
    public ActionResult RemoveToken(String id, RedisCache redis) throws Exception {
        String key = String.format("%s_%s",id,Constants.JwtTokenSign);
        if(redis.HasKey(key))
        {
            redis.RemoveItem(key);
            return ActionResult.Success();
        }
        return ActionResult.Fail();
    }

    @Override
    public ActionResult<Boolean> VerifyToken(String id, String token, RedisCache redis) {
        String key = String.format("%s_%s",id,Constants.JwtTokenSign);
        if(!redis.HasKey(key))
            return ActionResult.Fail("身份验证信息已过期！");
        if(redis.GetItem(key).equals(token))
            return ActionResult.SuccessWithData(true);
        else return ActionResult.SuccessWithData(false);
    }

    @Override
    public ActionResult<PagedData<UserDisplayVO>> GetUsers(Integer page, Integer pageSize, String queryKey, Integer status, Integer type) {
        PagedData<UserDisplayVO> res = new PagedData<>();
        Page<UserDisplayVO> _page = Page.of(page, pageSize);
        res.setData( mapper.getUsers(_page,queryKey,status,type));
        res.setTotal(_page.getTotal());
        return ActionResult.SuccessWithData(res);
    }

    @Override
    public ActionResult ChangeStatus(Integer status,String id) {
        Boolean res = lambdaUpdate().set(User::getStatus,status).eq(User::getId,id).update();
        return res? ActionResult.Success("更新状态成功！") : ActionResult.Fail("更新状态失败！");
    }

    @Override
    public ActionResult<UserVO> EmailLogin(String email, String password, JwtConfig jwtConfig, RedisCache redis) {
        var wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getEmail,email);
        User user = mapper.selectOne(wrapper);
        if(user == null)
            return ActionResult.Fail("用户不存在！", HttpStatusCode.NotFound);
        if(!Md5Util.Check(password,user.getPassword()))
            return ActionResult.Fail("密码错误！");
        UserVO res = new UserVO();
        ObjectUtil.copyToVo(user,res);
        res.setToken(LoganDataCaching(jwtConfig,user.getId(),redis));
        user.setLastLoginTime(Constants.CurrentTime());
        mapper.updateById(user);
        return ActionResult.SuccessWithData("登录成功！",res);
    }

    @Override
    public ActionResult GenerateCheckCode(Integer length, RedisCache redis, String email, EmailService emailService) throws MessagingException {
        String key = String.format("%s_%s",email,Constants.CheckCodeSign);
        String checkCode = RandomGenerator.Generate(length);
        redis.SetItem(key,checkCode,Duration.ofMinutes(Constants.LoginCheckLength));
        String message = String.format("验证码为%s，请于五分钟内使用！",checkCode);
        emailService.Send(email,"验证码",message);
        return ActionResult.Success("已生成验证码并发送至目标邮箱:"+email);
    }

    @Override
    public ActionResult<UserVO> CheckCodeLogin(String checkCode, String email, JwtConfig jwtConfig, RedisCache redis) {
        String key = String.format("%s_%s",email,Constants.CheckCodeSign);
        if(!redis.HasKey(key))
            return ActionResult.Fail("验证码已过期！请重新获取！");
        if(!redis.GetItem(key).equals(checkCode))
            return ActionResult.Fail("验证码错误！");
        else
        {
            User user = mapper.selectOne(new LambdaQueryWrapper<User>()
            .eq(User::getEmail,email));
            if(user==null)
                return ActionResult.Fail("用户不存在！",HttpStatusCode.NotFound);
            UserVO res = new UserVO();
            ObjectUtil.copyToVo(user,res);
            res.setToken(LoganDataCaching(jwtConfig,user.getId(),redis));
            user.setLastLoginTime(Constants.CurrentTime());
            mapper.updateById(user);
            return ActionResult.SuccessWithData(res);
        }
    }

    @Override
    public ActionResult Register(String checkCode,String email,RedisCache redis) {
        String key = String.format("%s_%s",email,Constants.CheckCodeSign);
        if(!redis.HasKey(key))
            return ActionResult.Fail("验证码已过期！");
        if(!redis.GetItem(key).equals(checkCode))
            return ActionResult.Fail("验证码错误！");
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getEmail,email));
        if(user!=null)
            return ActionResult.Fail("该电子邮箱已注册！");
        user = new User();
        user.setId(RandomGenerator.GenerateUserId());
        user.setName(String.format("用户%s",user.getId()));
        user.setEmail(email);
        user.setPassword(Md5Util.Encrypt(Constants.DefaultUserPassword));
        user.setType(UserType.Common.Value());
        user.setCreateTime(Constants.CurrentTime());
        mapper.insert(user);
        return ActionResult.Success("已成功注册！");
    }

    @Override
    public ActionResult<UserDisplayVO> GetUser(String id) {
        User user = mapper.selectById(id);
        if(user==null)
            return ActionResult.NotFound();
        return ActionResult.SuccessWithData(ObjectUtil.copyToVo(user,new UserDisplayVO()));
    }

    @Override
    @Transactional
    public ActionResult ChangeName(String id, String newName) {
        Boolean res = lambdaUpdate().set(User::getName,newName).
                eq(User::getId,id).update();
        if(res)
            return ActionResult.Success("名称已更改！");
        else
            return ActionResult.Fail("名称更新失败！");
    }

    @Override
    public ActionResult ChangePassword(String email, String newPassword) {
        User user = mapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail,email));
        if(user==null)
            return ActionResult.Fail("用户不存在!",HttpStatusCode.NotFound);
        user.setPassword(Md5Util.Encrypt(newPassword));
        mapper.updateById(user);
        return ActionResult.Success("用户秘密已更改！");
    }

    @Override
    @Transactional
    public ActionResult ChangeAvatar(String id, String newAvatar) {
        Boolean res = lambdaUpdate().set(User::getAvatar,newAvatar).
                eq(User::getId,id).update();
        if(res)
            return ActionResult.Success("头像已更改！");
        else
            return ActionResult.Fail("头像更新失败！");
    }
}
