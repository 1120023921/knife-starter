package com.wingice.msg.ali;

import com.wingice.msg.ali.controller.AliAccountController;
import com.wingice.msg.ali.entity.AliAccount;
import com.wingice.msg.ali.properties.KnifeAliProperties;
import com.wingice.msg.ali.service.AliAccountService;
import com.wingice.msg.ali.service.KnifeSmsAliSenderContext;
import com.wingice.msg.ali.service.KnifeSmsAliService;
import com.wingice.msg.ali.service.impl.AliAccountServiceImpl;
import com.wingice.msg.ali.service.impl.KnifeSmsAliSenderContextImpl;
import com.wingice.msg.ali.service.impl.KnifeSmsAliServiceImpl;
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
 * Ali: huhao9277@gali.com
 */
@Configuration
public class SmsAliAutoConfig {

    /**
     * @param redisConnectionFactory redis连接
     * @description token内用户信息序列化
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2021/1/10 21:43
     */
    @ConditionalOnMissingBean(name = {"aliAccountRedisTemplate"})
    @Bean("aliAccountRedisTemplate")
    public RedisTemplate<String, List<AliAccount>> aliAccountRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, List<AliAccount>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @description 邮件账户管理service
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 16:21
     */
    @Bean("aliAccountService")
    @ConditionalOnMissingBean
    public AliAccountService aliAccountService() {
        return new AliAccountServiceImpl();
    }

    /**
     * @param knifeAliProperties      邮件配置信息
     * @param aliAccountService       邮件账户管理service
     * @param aliAccountRedisTemplate 邮件账户缓存redis
     * @description 邮件发送者上下文
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 16:19
     */
    @Bean
    @ConditionalOnProperty(name = "knife.msg.sms.ali.enable", havingValue = "true")
    @ConditionalOnMissingBean(name = {"knifeSmsAliSenderContext"})
    @ConditionalOnBean(name = {"aliAccountService", "aliAccountRedisTemplate"})
    public KnifeSmsAliSenderContext knifeSmsAliSenderContext(@Autowired KnifeAliProperties knifeAliProperties, @Autowired AliAccountService aliAccountService, @Autowired RedisTemplate<String, List<AliAccount>> aliAccountRedisTemplate) {
        final KnifeSmsAliSenderContextImpl smsAliSenderContext = new KnifeSmsAliSenderContextImpl(knifeAliProperties, aliAccountService, aliAccountRedisTemplate);
        aliAccountService.configKnifeSmsAliSenderContext(smsAliSenderContext);
        return smsAliSenderContext;
    }

    /**
     * @param knifeSmsAliSenderContext 邮件发送上下文服务
     * @description 邮件发送服务
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 16:29
     */
    @Bean
    @ConditionalOnBean(name = {"knifeSmsAliSenderContext"})
    public KnifeSmsAliService knifeSmsAliService(KnifeSmsAliSenderContext knifeSmsAliSenderContext) {
        return new KnifeSmsAliServiceImpl(knifeSmsAliSenderContext);
    }

//    @Bean
//    @ConditionalOnProperty(name = "knife.msg.sms.ali.ali-account-api-enable", havingValue = "true")
//    @ConditionalOnBean(name = {"aliAccountService"})
//    public AliAccountController aliAccountController(AliAccountService aliAccountService) {
//        return new AliAccountController(aliAccountService);
//    }
}
