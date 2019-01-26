package com.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具类
 */
@Component
public class EmailUtil {

    @Value("${email.smtp}")
    private String smtp;
    @Value("${email.username}")
    private String username;
    @Value("${email.password}")
    private String password;
    /**
     * @sender 应该写在配置文件，目前没解决乱码问题
     */
    private String sender = "测试邮件";

    /**
     * @param to      收件人邮箱账号
     * @param subject 邮件标题
     * @param content 邮件内容
     * @throws Exception
     */
    public void send(String to, String subject, String content) throws Exception {
        //参数设置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", username);
        props.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(props);
        session.setDebug(true);
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, sender, "utf-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to, to, "utf-8"));
        message.setSubject(subject, "utf-8");
        message.setContent(content, "text/html;charset=utf-8");
        message.setSentDate(new Date());
        message.saveChanges();
        //发送邮件
        Transport transport = session.getTransport();
        transport.connect(smtp, username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

}