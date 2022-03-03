package com.wingice.msg.mail.service.impl;

import com.wingice.common.mybatis.tenant.TenantContextHolder;
import com.wingice.msg.mail.entity.KnifeJavaMailSender;
import com.wingice.msg.mail.entity.MailAccount;
import com.wingice.msg.mail.exception.MailAccountNotFoundException;
import com.wingice.msg.mail.exception.MultiMailAccountException;
import com.wingice.msg.mail.properties.KnifeMailProperties;
import com.wingice.msg.mail.service.KnifeMailSenderContext;
import com.wingice.msg.mail.service.MailAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 15:06
 * Mail: huhao9277@gmail.com
 */
@Slf4j
public class KnifeKnifeMailSenderContextImpl implements KnifeMailSenderContext {

    private final KnifeMailProperties knifeMailProperties;
    private final MailAccountService mailAccountService;
    private final RedisTemplate<String, List<MailAccount>> mailAccountRedisTemplate;

    public KnifeKnifeMailSenderContextImpl(KnifeMailProperties knifeMailProperties, MailAccountService mailAccountService, RedisTemplate<String, List<MailAccount>> mailAccountRedisTemplate) {
        this.knifeMailProperties = knifeMailProperties;
        this.mailAccountService = mailAccountService;
        this.mailAccountRedisTemplate = mailAccountRedisTemplate;
    }

    private final static String MAIL_ACCOUNT_CACHE_KEY = "KNIFE_MAIL_ACCOUNT";

    @Override
    public KnifeJavaMailSender getJavaMailSender() throws MailAccountNotFoundException, MultiMailAccountException {
        List<MailAccount> mailAccountList = mailAccountRedisTemplate.opsForValue().get(knifeMailProperties.getCachePrefix() + MAIL_ACCOUNT_CACHE_KEY);
        if (mailAccountList == null) {
            final String currentTenantId = TenantContextHolder.getTenantId();
            mailAccountList = refreshMailAccountCache();
            TenantContextHolder.setTenantId(currentTenantId);
        }
        if (knifeMailProperties.getTenantEnable()) {
            final List<MailAccount> mailAccount = mailAccountList.stream().filter(item -> TenantContextHolder.getTenantId().equals(item.getTenantId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(mailAccount)) {
                throw new MailAccountNotFoundException("邮件账户未找到");
            }
            if (mailAccount.size() > 1) {
                throw new MultiMailAccountException("邮件账户不唯一");
            }
            return buildJavaMailSender(mailAccount.get(0));
        } else {
            if (CollectionUtils.isEmpty(mailAccountList)) {
                throw new MailAccountNotFoundException("邮件账户未找到");
            }
            if (mailAccountList.size() > 1) {
                throw new MultiMailAccountException("邮件账户不唯一");
            }
            return buildJavaMailSender(mailAccountList.get(0));
        }
    }

    @Override
    public List<MailAccount> refreshMailAccountCache() {
        final String tenantId = TenantContextHolder.getTenantId();
        TenantContextHolder.setTenantId(null);
        final List<MailAccount> mailAccountList = mailAccountService.list();
        TenantContextHolder.setTenantId(tenantId);
        mailAccountRedisTemplate.opsForValue().set(knifeMailProperties.getCachePrefix() + MAIL_ACCOUNT_CACHE_KEY, mailAccountList, 1, TimeUnit.DAYS);
        return mailAccountList;
    }

    private KnifeJavaMailSender buildJavaMailSender(MailAccount mailAccount) {
        final KnifeJavaMailSender sender = new KnifeJavaMailSender();
        sender.setFrom(mailAccount.getMailFrom());
        sender.setProtocol("smtp");
        sender.setDefaultEncoding("UTF-8");
        sender.setHost(mailAccount.getHost());
        sender.setPort(mailAccount.getPort());
        final Properties properties = new Properties();
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");
        if (mailAccount.getAuth().equals(1)) {
            sender.setUsername(mailAccount.getUsername());
            sender.setPassword(mailAccount.getPassword());
            properties.put("mail.smtp.auth", "true");
        } else {
            properties.put("mail.smtp.auth", "false");
        }
        if (mailAccount.getEncryption().equals(1)) {
            properties.put("mail.smtp.ssl.enable", "false");
        }
        if (mailAccount.getEncryption().equals(2)) {
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.imap.ssl.socketFactory.fallback", "false");
        }
        if (mailAccount.getEncryption().equals(3)) {
            properties.put("mail.smtp.starttls.enable", "true");
        }
        sender.setJavaMailProperties(properties);
        return sender;
    }
}
