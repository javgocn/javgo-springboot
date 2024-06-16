package cn.javgo.boot.base.starters.controller;

import cn.javgo.boot.base.starters.config.properties.CustomerMailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * Description: 发送邮件 Controller
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final JavaMailSender javaMailSender;

    private final CustomerMailProperties mailProperties;

    @ResponseBody
    @GetMapping("/sendEmail")
    public boolean sendEmail(@RequestParam("email") String email, @RequestParam("text") String text) {
        try {
            MimeMessage message = createMimeMsg(email, text, "java.png");
            // SimpleMailMessage message = createSimpleMsg(email, text);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            return false;
        }
        return true;
    }

    /**
     * 创建复杂邮件
     *
     * @param email               收件人邮箱
     * @param text                邮件内容
     * @param attachmentClassPath 附件路径
     * @return MimeMessage
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private MimeMessage createMimeMsg(String email, String text, String attachmentClassPath) throws MessagingException, UnsupportedEncodingException {
        // 创建复杂邮件
        MimeMessage message = javaMailSender.createMimeMessage();
        // 创建复杂邮件帮助器
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        // 设置发件人
        mimeMessageHelper.setFrom(mailProperties.getFrom(), mailProperties.getPersonal());
        // 设置收件人
        mimeMessageHelper.setTo(email);
        // 设置抄送人
        mimeMessageHelper.setBcc(mailProperties.getBcc());
        // 设置邮件主题
        mimeMessageHelper.setSubject(mailProperties.getSubject());
        // 设置邮件内容
        mimeMessageHelper.setText(text);
        // 添加附件
        mimeMessageHelper.addAttachment("附件", new ClassPathResource(attachmentClassPath));
        return message;
    }

    /**
     * 创建简单邮件
     *
     * @param email 收件人邮箱
     * @param text  邮件内容
     * @return SimpleMailMessage
     */
    private SimpleMailMessage createSimpleMsg(String email, String text) {
        // 创建简单邮件
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发件人
        message.setFrom(mailProperties.getFrom());
        // 设置收件人
        message.setTo(email);
        // 设置抄送人
        message.setBcc(mailProperties.getBcc());
        // 设置邮件主题
        message.setSubject(mailProperties.getSubject());
        // 设置邮件内容
        message.setText(text);
        return message;
    }
}
