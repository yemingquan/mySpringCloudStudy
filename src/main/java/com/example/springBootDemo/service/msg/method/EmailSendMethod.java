package com.example.springBootDemo.service.msg.method;

import com.example.springBootDemo.service.msg.ISendMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/25 11:04
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Component
public class EmailSendMethod implements ISendMethod {
    @Value("${spring.mail.password}")
    private String accessKeySecret;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public Boolean sendMsg(String receiver, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            // 设置发送方邮箱地址
            helper.setFrom(emailFrom);
            helper.setTo(receiver);
            helper.setSubject(title);
            helper.setText(content, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
