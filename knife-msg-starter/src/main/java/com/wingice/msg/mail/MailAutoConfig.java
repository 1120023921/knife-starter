package com.wingice.msg.mail;

import com.wingice.msg.mail.controller.MailAccountController;
import com.wingice.msg.mail.entity.MailAccount;
import com.wingice.msg.mail.properties.KnifeMailProperties;
import com.wingice.msg.mail.service.KnifeMailSenderContext;
import com.wingice.msg.mail.service.KnifeMailService;
import com.wingice.msg.mail.service.MailAccountService;
import com.wingice.msg.mail.service.impl.KnifeKnifeMailSenderContextImpl;
import com.wingice.msg.mail.service.impl.KnifeMailServiceImpl;
import com.wingice.msg.mail.service.impl.MailAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 15:23
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class MailAutoConfig {

    /**
     * @param redisConnectionFactory redis连接
     * @description token内用户信息序列化
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:43
     */
    @ConditionalOnMissingBean(name = {"mailAccountRedisTemplate"})
    @Bean("mailAccountRedisTemplate")
    public RedisTemplate<String, List<MailAccount>> mailAccountRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, List<MailAccount>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @description 邮件账户管理service
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:21
     */
    @Bean("mailAccountService")
    @ConditionalOnMissingBean
    public MailAccountService mailAccountService() {
        return new MailAccountServiceImpl();
    }

    /**
     * @param knifeMailProperties      邮件配置信息
     * @param mailAccountService       邮件账户管理service
     * @param mailAccountRedisTemplate 邮件账户缓存redis
     * @description 邮件发送者上下文
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:19
     */
    @Bean
    @ConditionalOnProperty(name = "knife.msg.mail.enable", havingValue = "true")
    @ConditionalOnMissingBean(name = {"mailSenderContext"})
    @ConditionalOnBean(name = {"mailAccountService", "mailAccountRedisTemplate"})
    public KnifeMailSenderContext mailSenderContext(@Autowired KnifeMailProperties knifeMailProperties, @Autowired MailAccountService mailAccountService, @Autowired RedisTemplate<String, List<MailAccount>> mailAccountRedisTemplate) {
        final KnifeKnifeMailSenderContextImpl mailSenderContext = new KnifeKnifeMailSenderContextImpl(knifeMailProperties, mailAccountService, mailAccountRedisTemplate);
        mailAccountService.configKnifeMailSenderContext(mailSenderContext);
        return mailSenderContext;
    }

    /**
     * @param knifeMailSenderContext 邮件发送上下文服务
     * @description 邮件发送服务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:29
     */
    @Bean
    @ConditionalOnBean(name = {"mailSenderContext"})
    public KnifeMailService knifeMailService(KnifeMailSenderContext knifeMailSenderContext) {
        return new KnifeMailServiceImpl(knifeMailSenderContext);
    }

//    @Bean
//    @ConditionalOnProperty(name = "knife.msg.mail.mail-account-api-enable", havingValue = "true")
//    @ConditionalOnBean(name = {"mailAccountService"})
//    public MailAccountController mailAccountController(MailAccountService mailAccountService) {
//        return new MailAccountController(mailAccountService);
//    }
}
