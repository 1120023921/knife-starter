package com.cintsoft.spring.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class KnifeOAuth2AccessToken implements Serializable {

    public static String BEARER_TYPE = "Bearer";

    public static String OAUTH2_TYPE = "OAuth2";

    public static String ACCESS_TOKEN = "access_token";

    public static String TOKEN_TYPE = "token_type";

    public static String EXPIRES_IN = "expires_in";

    public static String REFRESH_TOKEN = "refresh_token";

    public static String SCOPE = "scope";

    private String value;

    private Date expiration;

    private String tokenType = BEARER_TYPE.toLowerCase();

    private String refreshToken;

    private Set<String> scope = Collections.emptySet();

    private Map<String, Object> additionalInformation = Collections.emptyMap();

    public Map<String, Object> getTokenMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put(ACCESS_TOKEN, value);
        map.put(tokenType, tokenType);
        map.put(REFRESH_TOKEN, refreshToken);
        map.put(EXPIRES_IN, (expiration.getTime() - System.currentTimeMillis()) / 1000);
        map.put(SCOPE, scope.toArray());
        return map;
    }
}
