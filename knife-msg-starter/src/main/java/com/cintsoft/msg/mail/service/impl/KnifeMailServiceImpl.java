package com.cintsoft.msg.mail.service.impl;

import com.cintsoft.msg.mail.entity.KnifeJavaMailSender;
import com.cintsoft.msg.mail.service.KnifeMailSenderContext;
import com.cintsoft.msg.mail.service.KnifeMailService;
import com.cintsoft.msg.mail.vo.MailInfo;
import com.cintsoft.msg.mail.vo.MailResult;
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
    public MailResult sendMail(MailInfo mailInfo) {
        final KnifeJavaMailSender javaMailSender = (KnifeJavaMailSender) knifeMailSenderContext.getJavaMailSender();
        try {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);//true表示支持复杂类型
            messageHelper.setFrom(javaMailSender.getFrom());//邮件发信人
            messageHelper.setTo(mailInfo.getTo().split(","));//邮件收信人
            messageHelper.setSubject(mailInfo.getSubject());//邮件主题
            messageHelper.setText(mailInfo.getText(), mailInfo.getMultipart() == null || mailInfo.getMultipart());//邮件内容
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
            return MailResult.builder()
                    .msgId(mailInfo.getMsgId())
                    .code("0")
                    .errMsg("OK")
                    .build();
        } catch (MessagingException e) {
//            log.error("KnifeMailServiceImpl [sendMail] 邮件消息构造失败", e);
            return MailResult.builder()
                    .msgId(mailInfo.getMsgId())
                    .code("10000")
                    .errMsg("邮件消息构造失败 " + e.getMessage())
                    .build();
        } catch (MailException e) {
//            log.error("KnifeMailServiceImpl [sendMail] 邮件发送失败", e);
            return MailResult.builder()
                    .msgId(mailInfo.getMsgId())
                    .code("10001")
                    .errMsg("邮件发送失败 " + e.getMessage())
                    .build();
        }
    }

    @Override
    public List<MailResult> sendMail(List<MailInfo> mailInfoList) {
        final List<MailResult> mailResultList = new ArrayList<>();
        final KnifeJavaMailSender javaMailSender = (KnifeJavaMailSender) knifeMailSenderContext.getJavaMailSender();
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
                mailResultList.add(MailResult.builder()
                        .msgId(mailInfo.getMsgId())
                        .code("0")
                        .errMsg("OK")
                        .build());
            } catch (MessagingException e) {
//                log.error("KnifeMailServiceImpl [sendMail] 邮件消息构造失败", e);
                mailResultList.add(MailResult.builder()
                        .msgId(mailInfo.getMsgId())
                        .code("10000")
                        .errMsg("邮件消息构造失败 " + e.getMessage())
                        .build());
            } catch (MailException e) {
                log.error("KnifeMailServiceImpl [sendMail] 邮件发送失败", e);
                mailResultList.add(MailResult.builder()
                        .msgId(mailInfo.getMsgId())
                        .code("10001")
                        .errMsg("邮件发送失败 " + e.getMessage())
                        .build());
            }
        });
        return mailResultList;
    }
}
