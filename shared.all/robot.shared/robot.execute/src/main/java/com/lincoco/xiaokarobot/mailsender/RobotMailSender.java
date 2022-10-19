package com.lincoco.xiaokarobot.mailsender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/17
 */
@Slf4j
@Component
public class RobotMailSender {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to,String subject,String text){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 发件人
        mailMessage.setFrom(from);
        // 收件人
        mailMessage.setTo(to);
        // 邮件主题
        mailMessage.setSubject(subject);
        // 邮件内容
        mailMessage.setText(text);
        mailSender.send(mailMessage);
        log.info("【文本邮件】成功发送！to={}", to);
    }

    /**
     * 发送 html邮件
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        // true 为 HTML 邮件
        messageHelper.setText(content, true);
        mailSender.send(message);
        log.info("【HTML 邮件】成功发送！to={}", to);
    }

    /**
     * 发送带附件的邮件
     */
    public void sendAttachmentMail(String to, String subject, String content, String... fileArr)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);

        // 添加附件
        for (String filePath : fileArr) {
            FileSystemResource fileResource = new FileSystemResource(new File(filePath));
            if (fileResource.exists()) {
                String filename = fileResource.getFilename();
                messageHelper.addAttachment(filename, fileResource);
            }
        }
        mailSender.send(mimeMessage);
        log.info("【附件邮件】成功发送！to={}", to);
    }

    /**
     * 发送带图片的邮件
     */
    public void sendImgMail(String to, String subject, String content, Map<String, String> imgMap)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 添加图片
        for (Map.Entry<String, String> entry : imgMap.entrySet()) {
            FileSystemResource fileResource = new FileSystemResource(new File(entry.getValue()));
            if (fileResource.exists()) {
                String filename = fileResource.getFilename();
                messageHelper.addInline(entry.getKey(), fileResource);
            }
        }
        mailSender.send(mimeMessage);
        log.info("【图片邮件】成功发送！to={}", to);
    }

    /**
     * 发送模版邮件
     */
//    public void sendTemplateMail(String to, String subject, Map<String, Object> paramMap, String template)
//            throws MessagingException {
//        Context context = new Context();
//        // 设置变量的值
//        context.setVariables(paramMap);
//        String emailContent = templateEngine.process(template, context);
//        sendHtmlMail(to, subject, emailContent);
//        log.info("【模版邮件】成功发送！paramsMap={}，template={}", paramMap, template);
//    }
}
