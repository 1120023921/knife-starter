package com.cintsoft.msg.wechat.mp.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cintsoft.common.mybatis.tenant.TenantContextHolder;
import com.cintsoft.msg.wechat.mp.entity.WechatMpAccount;
import com.cintsoft.msg.wechat.mp.exception.MultiWechatMpAccountException;
import com.cintsoft.msg.wechat.mp.exception.WechatMpAccessTokenException;
import com.cintsoft.msg.wechat.mp.exception.WechatMpAccountNotFoundException;
import com.cintsoft.msg.wechat.mp.properties.KnifeWechatMpProperties;
import com.cintsoft.msg.wechat.mp.service.KnifeWechatMpSenderContext;
import com.cintsoft.msg.wechat.mp.service.WechatMpAccountService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
public class KnifeWechatMpSenderContextImpl implements KnifeWechatMpSenderContext {

    private final KnifeWechatMpProperties knifeWechatMpProperties;
    private final WechatMpAccountService wechatMpAccountService;
    private final RedisTemplate<String, List<WechatMpAccount>> wechatMpAccountRedisTemplate;

    public KnifeWechatMpSenderContextImpl(KnifeWechatMpProperties knifeWechatMpProperties, WechatMpAccountService wechatMpAccountService, RedisTemplate<String, List<WechatMpAccount>> wechatMpAccountRedisTemplate) {
        this.knifeWechatMpProperties = knifeWechatMpProperties;
        this.wechatMpAccountService = wechatMpAccountService;
        this.wechatMpAccountRedisTemplate = wechatMpAccountRedisTemplate;
    }

    private final static String WECHAT_MP_ACCOUNT_CACHE_KEY = "KNIFE_WECHAT_MP_ACCOUNT";
    private final static String WECHAT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    @Override
    public synchronized WechatMpAccount getWechatMpAccount() {
        List<WechatMpAccount> wechatMpAccountList = wechatMpAccountRedisTemplate.opsForValue().get(knifeWechatMpProperties.getCachePrefix() + WECHAT_MP_ACCOUNT_CACHE_KEY);
        if (wechatMpAccountList == null) {
            final String currentTenantId = TenantContextHolder.getTenantId();
            wechatMpAccountList = refreshWechatMpAccountCache();
            TenantContextHolder.setTenantId(currentTenantId);
        }
        if (knifeWechatMpProperties.getTenantEnable()) {
            final List<WechatMpAccount> wechatMpAccount = wechatMpAccountList.stream().filter(item -> TenantContextHolder.getTenantId().equals(item.getTenantId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(wechatMpAccount)) {
                throw new WechatMpAccountNotFoundException("微信公众号账户未找到");
            }
            if (wechatMpAccount.size() > 1) {
                throw new MultiWechatMpAccountException("微信公众号账户不唯一");
            }
            if (wechatMpAccountList.get(0).getAccessTokenExpires() == null || System.currentTimeMillis() + 10 * 60 * 1000L > wechatMpAccount.get(0).getAccessTokenExpires()) {
                return refreshWechatMpAccountAccessToken(wechatMpAccount.get(0));
            }
            return wechatMpAccount.get(0);
        } else {
            if (CollectionUtils.isEmpty(wechatMpAccountList)) {
                throw new WechatMpAccountNotFoundException("微信公众号账户未找到");
            }
            if (wechatMpAccountList.size() > 1) {
                throw new MultiWechatMpAccountException("微信公众号账户不唯一");
            }
            if (wechatMpAccountList.get(0).getAccessTokenExpires() == null || System.currentTimeMillis() + 10 * 60 * 1000L > wechatMpAccountList.get(0).getAccessTokenExpires()) {
                return refreshWechatMpAccountAccessToken(wechatMpAccountList.get(0));
            }
            return wechatMpAccountList.get(0);
        }
    }

    @Override
    public List<WechatMpAccount> refreshWechatMpAccountCache() {
        final String tenantId = TenantContextHolder.getTenantId();
        TenantContextHolder.setTenantId(null);
        final List<WechatMpAccount> wechatMpAccountList = wechatMpAccountService.list();
        TenantContextHolder.setTenantId(tenantId);
        wechatMpAccountRedisTemplate.opsForValue().set(knifeWechatMpProperties.getCachePrefix() + WECHAT_MP_ACCOUNT_CACHE_KEY, wechatMpAccountList, 1, TimeUnit.DAYS);
        return wechatMpAccountList;
    }

    @Override
    public WechatMpAccount refreshWechatMpAccountAccessToken(WechatMpAccount wechatMpAccount) {
        final JSONObject result = JSONUtil.parseObj(HttpUtil.get(String.format(WECHAT_TOKEN_URL, wechatMpAccount.getAppid(), wechatMpAccount.getSecret())));
        final String accessToken = result.getStr("access_token");
        if (StringUtils.hasText(accessToken)) {
            wechatMpAccount.setAccessToken(accessToken);
            wechatMpAccount.setAccessTokenExpires(System.currentTimeMillis() + (result.getInt("expires_in") * 1000L));
            wechatMpAccountService.updateById(wechatMpAccount);
            refreshWechatMpAccountCache();
            return wechatMpAccount;
        } else {
            throw new WechatMpAccessTokenException("微信AccessToken获取失败 result = " + result);
        }
    }
}
