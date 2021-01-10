package com.cintsoft.spring.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class AceOAuth2AccessToken implements Serializable {

	private String bearerType = "Bearer";

	private String oauth2Type = "OAuth2";

	private String accessToken;

	private String tokenType = "cintsoft";

	private String expiresIn;

	private String expiresTime;

	private String refreshToken;

	private String SCOPE = "scope";
}
