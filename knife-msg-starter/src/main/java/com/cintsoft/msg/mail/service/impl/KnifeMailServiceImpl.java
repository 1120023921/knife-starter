package com.cintsoft.msg.mail.service.impl;

import com.cintsoft.msg.mail.entity.KnifeJavaMailSenderImpl;
import com.cintsoft.msg.mail.entity.MailInfo;
import com.cintsoft.msg.mail.service.KnifeMailSenderContext;
import com.cintsoft.msg.mail.service.KnifeMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 15:02
 * Mail: huhao9277@gmail.com
 */
@Slf4j
public class KnifeMailServiceImpl implements KnifeMailService {

    private final KnifeMailSenderContext knifeMailSenderContext;

    public KnifeMailServiceImpl(KnifeMailSenderContext knifeMailSenderContext) {
        this.knifeMailSenderContext = knifeMailSenderContext;
    }

    @Override
    public Boolean sendMail(MailInfo mailInfo) {
        final KnifeJavaMailSenderImpl javaMailSender = (KnifeJavaMailSenderImpl) knifeMailSenderContext.getJavaMailSender();
        try {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);//true表示支持复杂类型
            messageHelper.setFrom(javaMailSender.getFrom());//邮件发信人
            messageHelper.setTo(mailInfo.getTo().split(","));//邮件收信人
            messageHelper.setSubject(mailInfo.getSubject());//邮件主题
            messageHelper.setText(mailInfo.getText(), mailInfo.getMultipart());//邮件内容
            if (StringUtils.hasText(mailInfo.getCc())) {//抄送
                messageHelper.setCc(mailInfo.getCc().split(","));
            }
            if (StringUtils.hasText(mailInfo.getBcc())) {//密送
                messageHelper.setCc(mailInfo.getBcc().split(","));
            }
            if (mailInfo.getSentDate() != null) {//发送时间
                messageHelper.setSentDate(new Date(mailInfo.getSentDate()));
            }
            javaMailSender.send(messageHelper.getMimeMessage());//正式发送邮件
            return true;
        } catch (MessagingException e) {
            log.error("KnifeMailServiceImpl [sendMail] 邮件消息构造失败", e);
            return false;
        } catch (MailException e) {
            log.error("KnifeMailServiceImpl [sendMail] 邮件发送失败", e);
            return false;
        }
    }

    @Override
    public List<MailInfo> sendMail(List<MailInfo> mailInfoList) {
        final List<MailInfo> errorMailInfoList = new ArrayList<>();
        final KnifeJavaMailSenderImpl javaMailSender = (KnifeJavaMailSenderImpl) knifeMailSenderContext.getJavaMailSender();
        mailInfoList.forEach(mailInfo -> {
            try {
                final MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);//true表示支持复杂类型
                messageHelper.setFrom(javaMailSender.getFrom());//邮件发信人
                messageHelper.setTo(mailInfo.getTo().split(","));//邮件收信人
                messageHelper.setSubject(mailInfo.getSubject());//邮件主题
                messageHelper.setText(mailInfo.getText(), mailInfo.getMultipart());//邮件内容
                if (StringUtils.hasText(mailInfo.getCc())) {//抄送
                    messageHelper.setCc(mailInfo.getCc().split(","));
                }
                if (StringUtils.hasText(mailInfo.getBcc())) {//密送
                    messageHelper.setCc(mailInfo.getBcc().split(","));
                }
                if (mailInfo.getSentDate() != null) {//发送时间
                    messageHelper.setSentDate(new Date(mailInfo.getSentDate()));
                }
                javaMailSender.send(messageHelper.getMimeMessage());//正式发送邮件
            } catch (MessagingException e) {
                log.error("KnifeMailServiceImpl [sendMail] 邮件消息构造失败", e);
                errorMailInfoList.add(mailInfo);
            } catch (MailException e) {
                log.error("KnifeMailServiceImpl [sendMail] 邮件发送失败", e);
                errorMailInfoList.add(mailInfo);
            }
        });
        return errorMailInfoList;
    }
}
