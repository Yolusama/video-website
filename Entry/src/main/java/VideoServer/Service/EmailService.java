package VideoServer.Service;

import VideoServer.Configuration.EmailConfig;
import VideoServer.Functional.EMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@Service
public class EmailService {
    @Resource
    private EmailConfig emailConfig;

    public EmailService(EmailConfig emailConfig)
    {
       this.emailConfig = emailConfig;
        EMailSender.init(emailConfig.getHost(),emailConfig.getAuthorizationCode());
    }

    public void Send(String email,String title,String message) throws MessagingException {
        EMailSender.sendTo(email,title,message);
    }
}
