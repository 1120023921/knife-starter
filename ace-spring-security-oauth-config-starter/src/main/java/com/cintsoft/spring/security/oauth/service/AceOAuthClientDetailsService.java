package com.cintsoft.spring.security.oauth.service;

import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.oauth.model.AceOAuthClientDetails;

/**
 * <p>
 * 终端信息表 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2021-01-12
 */
public interface AceOAuthClientDetailsService {

    /**
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @description
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/13 10:45
     */
    AceUser clientCredentialsAuthorize(String clientId, String clientSecret);

    /**
     * @param clientId 客户端id
     * @description 根据客户端id获取详情
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/13 11:00
     */
    AceOAuthClientDetails getAceOauthClientDetails(String clientId);
}
