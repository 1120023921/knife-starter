package com.wingice.msg.ali.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.wingice.common.mybatis.tenant.TenantContextHolder;
import com.wingice.msg.ali.entity.AliAccount;
import com.wingice.msg.ali.exception.AliAccountNotFoundException;
import com.wingice.msg.ali.exception.AliClientException;
import com.wingice.msg.ali.exception.MultiAliAccountException;
import com.wingice.msg.ali.properties.KnifeAliProperties;
import com.wingice.msg.ali.service.AliAccountService;
import com.wingice.msg.ali.service.KnifeSmsAliSenderContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/13
 * Time: 11:36
 * Ali: huhao9277@gali.com
 */
public class KnifeSmsAliSenderContextImpl implements KnifeSmsAliSenderContext {

    private final KnifeAliProperties knifeAliProperties;
    private final AliAccountService aliAccountService;
    private final RedisTemplate<String, List<AliAccount>> aliAccountRedisTemplate;

    private final static String SMS_ALI_ACCOUNT_CACHE_KEY = "KNIFE_SMS_ALI_ACCOUNT";

    public KnifeSmsAliSenderContextImpl(KnifeAliProperties knifeAliProperties, AliAccountService aliAccountService, RedisTemplate<String, List<AliAccount>> aliAccountRedisTemplate) {
        this.knifeAliProperties = knifeAliProperties;
        this.aliAccountService = aliAccountService;
        this.aliAccountRedisTemplate = aliAccountRedisTemplate;
    }

    @Override
    public Client getClient() throws AliAccountNotFoundException, MultiAliAccountException {
        List<AliAccount> aliAccountList = aliAccountRedisTemplate.opsForValue().get(knifeAliProperties.getCachePrefix() + SMS_ALI_ACCOUNT_CACHE_KEY);
        if (aliAccountList == null) {
            aliAccountList = refreshAliAccountCache();
        }
        if (knifeAliProperties.getTenantEnable()) {
            final List<AliAccount> aliAccount = aliAccountList.stream().filter(item -> TenantContextHolder.getTenantId().equals(item.getTenantId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(aliAccount)) {
                throw new AliAccountNotFoundException("阿里短信账户未找到");
            }
            if (aliAccount.size() > 1) {
                throw new MultiAliAccountException("阿里短信账户不唯一");
            }
            return buildClient(aliAccount.get(0));
        } else {
            if (CollectionUtils.isEmpty(aliAccountList)) {
                throw new AliAccountNotFoundException("阿里短信账户未找到");
            }
            if (aliAccountList.size() > 1) {
                throw new MultiAliAccountException("阿里短信账户不唯一");
            }
            return buildClient(aliAccountList.get(0));
        }
    }

    @Override
    public List<AliAccount> refreshAliAccountCache() {
        final String tenantId = TenantContextHolder.getTenantId();
        TenantContextHolder.setTenantId(null);
        final List<AliAccount> aliAccountList = aliAccountService.list();
        TenantContextHolder.setTenantId(tenantId);
        aliAccountRedisTemplate.opsForValue().set(knifeAliProperties.getCachePrefix() + SMS_ALI_ACCOUNT_CACHE_KEY, aliAccountList, 1, TimeUnit.DAYS);
        return aliAccountList;
    }

    private Client buildClient(AliAccount aliAccount) throws AliClientException {
        final Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(aliAccount.getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(aliAccount.getAccessSecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        try {
            return new Client(config);
        } catch (Exception e) {
            throw new AliClientException("构造阿里云短信Client失败");
        }
    }
}
