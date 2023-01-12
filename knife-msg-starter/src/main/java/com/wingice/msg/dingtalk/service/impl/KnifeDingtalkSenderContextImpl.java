package com.wingice.msg.dingtalk.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wingice.common.mybatis.tenant.TenantContextHolder;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;
import com.wingice.msg.dingtalk.exception.DingtalkAccessTokenException;
import com.wingice.msg.dingtalk.exception.DingtalkAccountNotFoundException;
import com.wingice.msg.dingtalk.exception.MultiDingtalkAccountException;
import com.wingice.msg.dingtalk.properties.KnifeDingtalkProperties;
import com.wingice.msg.dingtalk.service.DingtalkAccountService;
import com.wingice.msg.dingtalk.service.KnifeDingtalkSenderContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 13:47
 * Mail: huhao9277@gmail.com
 */
public class KnifeDingtalkSenderContextImpl implements KnifeDingtalkSenderContext {

    private final KnifeDingtalkProperties knifeDingtalkProperties;
    private final DingtalkAccountService dingtalkAccountService;
    private final RedisTemplate<String, List<DingtalkAccount>> dingtalkAccountRedisTemplate;

    public KnifeDingtalkSenderContextImpl(KnifeDingtalkProperties knifeDingtalkProperties, DingtalkAccountService dingtalkAccountService, RedisTemplate<String, List<DingtalkAccount>> dingtalkAccountRedisTemplate) {
        this.knifeDingtalkProperties = knifeDingtalkProperties;
        this.dingtalkAccountService = dingtalkAccountService;
        this.dingtalkAccountRedisTemplate = dingtalkAccountRedisTemplate;
    }

    private final static String DINGTALK_ACCOUNT_CACHE_KEY = "KNIFE_DINGTALK_ACCOUNT";

    private final static String DINGTALK_TOKEN_URL = "https://api.dingtalk.com/v1.0/oauth2/accessToken";

    @Override
    public synchronized DingtalkAccount getDingtalkAccount() {
        List<DingtalkAccount> dingtalkAccountList = dingtalkAccountRedisTemplate.opsForValue().get(knifeDingtalkProperties.getCachePrefix() + DINGTALK_ACCOUNT_CACHE_KEY);
        if (dingtalkAccountList == null) {
            final String currentTenantId = TenantContextHolder.getTenantId();
            dingtalkAccountList = refreshDingtalkAccountCache();
            TenantContextHolder.setTenantId(currentTenantId);
        }
        if (knifeDingtalkProperties.getTenantEnable()) {
            final List<DingtalkAccount> dingtalkAccount = dingtalkAccountList.stream().filter(item -> TenantContextHolder.getTenantId().equals(item.getTenantId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dingtalkAccount)) {
                throw new DingtalkAccountNotFoundException("钉钉账户未找到");
            }
            if (dingtalkAccount.size() > 1) {
                throw new MultiDingtalkAccountException("钉钉账户不唯一");
            }
            if (dingtalkAccountList.get(0).getAccessTokenExpires() == null || System.currentTimeMillis() + 10 * 60 * 1000L > dingtalkAccount.get(0).getAccessTokenExpires()) {
                return refreshDingtalkAccountAccessToken(dingtalkAccount.get(0));
            }
            return dingtalkAccount.get(0);
        } else {
            if (CollectionUtils.isEmpty(dingtalkAccountList)) {
                throw new DingtalkAccountNotFoundException("钉钉账户未找到");
            }
            if (dingtalkAccountList.size() > 1) {
                throw new MultiDingtalkAccountException("钉钉账户不唯一");
            }
            if (dingtalkAccountList.get(0).getAccessTokenExpires() == null || System.currentTimeMillis() + 10 * 60 * 1000L > dingtalkAccountList.get(0).getAccessTokenExpires()) {
                return refreshDingtalkAccountAccessToken(dingtalkAccountList.get(0));
            }
            return dingtalkAccountList.get(0);
        }
    }

    @Override
    public List<DingtalkAccount> refreshDingtalkAccountCache() {
        final String tenantId = TenantContextHolder.getTenantId();
        TenantContextHolder.setTenantId(null);
        final List<DingtalkAccount> dingtalkAccountList = dingtalkAccountService.list();
        TenantContextHolder.setTenantId(tenantId);
        dingtalkAccountRedisTemplate.opsForValue().set(knifeDingtalkProperties.getCachePrefix() + DINGTALK_ACCOUNT_CACHE_KEY, dingtalkAccountList, 1, TimeUnit.DAYS);
        return dingtalkAccountList;
    }

    @Override
    public DingtalkAccount refreshDingtalkAccountAccessToken(DingtalkAccount dingtalkAccount) {
        final JSONObject param = new JSONObject();
        param.set("appKey", dingtalkAccount.getAppKey());
        param.set("appSecret", dingtalkAccount.getAppSecret());
        final JSONObject result = JSONUtil.parseObj(HttpUtil.post(DINGTALK_TOKEN_URL, JSONUtil.toJsonPrettyStr(param)));
        if (StrUtil.isNotBlank(result.getStr("accessToken"))) {
            dingtalkAccount.setAccessToken(result.getStr("accessToken"));
            dingtalkAccount.setAccessTokenExpires(System.currentTimeMillis() + (result.getInt("expireIn") * 1000L));
            dingtalkAccountService.updateById(dingtalkAccount);
            refreshDingtalkAccountCache();
            return dingtalkAccount;
        } else {
            throw new DingtalkAccessTokenException("钉钉AccessToken获取失败 result = " + result.getStr("code") + ":" + result.getStr("message"));
        }
    }
}
