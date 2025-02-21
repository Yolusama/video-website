package VideoServer.Functional;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

public class EMailSender {

  private EMailSender(){}
  private static String from;
  private static String password;
  private static boolean hasLoaded = false;

  public static void init(String host,String authorizationCode)
  {
      if(hasLoaded) return;
      from = host;
      password = authorizationCode;
      hasLoaded = true;
  }

  public static void sendTo(String to,String subject,String msg) throws MessagingException {
      Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", "smtp.qq.com");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.port", 587);

      Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
          protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
              return new javax.mail.PasswordAuthentication(from, password);
          }
      });

      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject);

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(msg);
      MimeMultipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);
      Transport.send(message);
  }
}
