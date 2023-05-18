package com.wingice.msg.wechat.mp;

import com.wingice.msg.wechat.mp.controller.WechatMpAccountController;
import com.wingice.msg.wechat.mp.entity.WechatMpAccount;
import com.wingice.msg.wechat.mp.properties.KnifeWechatMpProperties;
import com.wingice.msg.wechat.mp.service.KnifeWechatMpMsgService;
import com.wingice.msg.wechat.mp.service.KnifeWechatMpSenderContext;
import com.wingice.msg.wechat.mp.service.WechatMpAccountService;
import com.wingice.msg.wechat.mp.service.impl.KnifeWechatMpMsgServiceImpl;
import com.wingice.msg.wechat.mp.service.impl.KnifeWechatMpSenderContextImpl;
import com.wingice.msg.wechat.mp.service.impl.WechatMpAccountServiceImpl;
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
public class WechatMpAutoConfig {

    /**
     * @param redisConnectionFactory redis连接
     * @description token内用户信息序列化
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:43
     */
    @ConditionalOnMissingBean(name = {"wechatMpAccountRedisTemplate"})
    @Bean("wechatMpAccountRedisTemplate")
    public RedisTemplate<String, List<WechatMpAccount>> wechatMpAccountRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, List<WechatMpAccount>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @description 微信公众号账户管理service
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:21
     */
    @Bean("wechatMpAccountService")
    @ConditionalOnMissingBean
    public WechatMpAccountService wechatMpAccountService() {
        return new WechatMpAccountServiceImpl();
    }

    /**
     * @param knifeWechatMpProperties      微信公众号配置信息
     * @param wechatMpAccountService       微信公众号账户管理service
     * @param wechatMpAccountRedisTemplate 微信公众号账户缓存redis
     * @description 微信公众号发送者上下文
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:19
     */
    @Bean
    @ConditionalOnProperty(name = "knife.msg.wechat.mp.enable", havingValue = "true")
    @ConditionalOnMissingBean(name = {"knifeWechatMpSenderContext"})
    @ConditionalOnBean(name = {"wechatMpAccountService", "wechatMpAccountRedisTemplate"})
    public KnifeWechatMpSenderContext knifeWechatMpSenderContext(@Autowired KnifeWechatMpProperties knifeWechatMpProperties, @Autowired WechatMpAccountService wechatMpAccountService, @Autowired RedisTemplate<String, List<WechatMpAccount>> wechatMpAccountRedisTemplate) {
        final KnifeWechatMpSenderContextImpl knifeWechatMpSenderContext = new KnifeWechatMpSenderContextImpl(knifeWechatMpProperties, wechatMpAccountService, wechatMpAccountRedisTemplate);
        wechatMpAccountService.configKnifeWechatMpSenderContext(knifeWechatMpSenderContext);
        return knifeWechatMpSenderContext;
    }

    /**
     * @param knifeWechatMpSenderContext 微信公众号发送上下文服务
     * @description 微信公众号发送服务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 16:29
     */
    @Bean
    @ConditionalOnBean(name = {"knifeWechatMpSenderContext"})
    public KnifeWechatMpMsgService knifeWechatMpMsgService(KnifeWechatMpSenderContext knifeWechatMpSenderContext) {
        return new KnifeWechatMpMsgServiceImpl(knifeWechatMpSenderContext);
    }

//    @Bean
//    @ConditionalOnProperty(name = "knife.msg.wechat.mp.wechat-mp-account-api-enable", havingValue = "true")
//    @ConditionalOnBean(name = {"wechatMpAccountService"})
//    public WechatMpAccountController wechatMpAccountController(WechatMpAccountService wechatMpAccountService) {
//        return new WechatMpAccountController(wechatMpAccountService);
//    }
}
