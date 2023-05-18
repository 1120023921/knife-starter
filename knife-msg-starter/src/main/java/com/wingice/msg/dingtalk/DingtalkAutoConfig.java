package com.wingice.msg.dingtalk;

import com.wingice.msg.dingtalk.controller.DingtalkAccountController;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;
import com.wingice.msg.dingtalk.properties.KnifeDingtalkProperties;
import com.wingice.msg.dingtalk.service.DingtalkAccountService;
import com.wingice.msg.dingtalk.service.KnifeDingtalkMsgService;
import com.wingice.msg.dingtalk.service.KnifeDingtalkSenderContext;
import com.wingice.msg.dingtalk.service.impl.DingtalkAccountServiceImpl;
import com.wingice.msg.dingtalk.service.impl.KnifeDingtalkMsgServiceImpl;
import com.wingice.msg.dingtalk.service.impl.KnifeDingtalkSenderContextImpl;
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
 * Date: 2022/1/14
 * Time: 13:51
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class DingtalkAutoConfig {

    /**
     * @param redisConnectionFactory redis连接
     * @description token内用户信息序列化
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:43
     */
    @ConditionalOnMissingBean(name = {"dingtalkAccountRedisTemplate"})
    @Bean("dingtalkAccountRedisTemplate")
    public RedisTemplate<String, List<DingtalkAccount>> dingtalkAccountRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, List<DingtalkAccount>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @description 钉钉账户管理service
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:21
     */
    @Bean("dingtalkAccountService")
    @ConditionalOnMissingBean
    public DingtalkAccountService dingtalkAccountService() {
        return new DingtalkAccountServiceImpl();
    }

    /**
     * @param knifeDingtalkProperties      钉钉配置信息
     * @param dingtalkAccountService       钉钉账户管理service
     * @param dingtalkAccountRedisTemplate 钉钉账户缓存redis
     * @description 钉钉发送者上下文
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:19
     */
    @Bean
    @ConditionalOnProperty(name = "knife.msg.dingtalk.enable", havingValue = "true")
    @ConditionalOnMissingBean(name = {"knifeDingtalkSenderContext"})
    @ConditionalOnBean(name = {"dingtalkAccountService", "dingtalkAccountRedisTemplate"})
    public KnifeDingtalkSenderContext knifeDingtalkSenderContext(@Autowired KnifeDingtalkProperties knifeDingtalkProperties, @Autowired DingtalkAccountService dingtalkAccountService, @Autowired RedisTemplate<String, List<DingtalkAccount>> dingtalkAccountRedisTemplate) {
        final KnifeDingtalkSenderContextImpl knifeDingtalkSenderContext = new KnifeDingtalkSenderContextImpl(knifeDingtalkProperties, dingtalkAccountService, dingtalkAccountRedisTemplate);
        dingtalkAccountService.configKnifeDingtalkSenderContext(knifeDingtalkSenderContext);
        return knifeDingtalkSenderContext;
    }

    /**
     * @param knifeDingtalkSenderContext 钉钉发送上下文服务
     * @description 钉钉发送服务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:29
     */
    @Bean
    @ConditionalOnBean(name = {"knifeDingtalkSenderContext"})
    public KnifeDingtalkMsgService knifeDingtalkMsgService(KnifeDingtalkSenderContext knifeDingtalkSenderContext) {
        return new KnifeDingtalkMsgServiceImpl(knifeDingtalkSenderContext);
    }

//    @Bean
//    @ConditionalOnProperty(name = "knife.msg.dingtalk.wechat-mp-account-api-enable", havingValue = "true")
//    @ConditionalOnBean(name = {"dingtalkAccountService"})
//    public DingtalkAccountController dingtalkAccountController(DingtalkAccountService dingtalkAccountService) {
//        return new DingtalkAccountController(dingtalkAccountService);
//    }
}
