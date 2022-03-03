package com.wingice.cas.handler;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.http.HttpUtil;
import com.wingice.cas.common.constant.CasConfigProperties;
import com.wingice.spring.security.handler.KnifeSocialLoginHandler;
import com.wingice.spring.security.model.KnifeUser;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/9/18
 * Time: 10:08
 * Mail: huhao9277@gmail.com
 */
@Slf4j
public class CasLoginHandler implements KnifeSocialLoginHandler {

    private final CasConfigProperties casConfigProperties;
    private final UserDetailsService userDetailsService;

    public CasLoginHandler(CasConfigProperties casConfigProperties, UserDetailsService userDetailsService) {
        this.casConfigProperties = casConfigProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public KnifeUser authenticate(String code) {
        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("ticket", code.split("@")[0]);
        requestMap.put("service", Base64Decoder.decodeStr(code.split("@")[1]));
        String passValidateUrl = casConfigProperties.getCasServerUrlPrefix() + "/serviceValidate";
        String responseStr = HttpUtil.post(passValidateUrl, requestMap);
        try {
            final Document responseXml = DocumentHelper.parseText(responseStr);
            final Element root = responseXml.getRootElement();
            root.addNamespace("cas", "http://www.yale.edu/tp/cas");
            root.addNamespace("sso", "http://www.yale.edu/tp/cas");
            final Element authenticationFailureNode = root.element("authenticationFailure");
            if (authenticationFailureNode != null) {
                log.error("CAS登录失败,错误代码: {}, 错误原因: {}", authenticationFailureNode.attribute("code"), authenticationFailureNode.getText());
                return null;
            }
            Element authenticationSuccessNode = root.element("authenticationSuccess");
            if (authenticationSuccessNode != null) {
                final String[] userInfo = casConfigProperties.getUserField().split(":");
                for (String s : userInfo) {
                    authenticationSuccessNode = authenticationSuccessNode.element(s);
                }
                log.info("CAS登录成功, 用户为: {}", authenticationSuccessNode.getText());
                return (KnifeUser) userDetailsService.loadUserByUsername(authenticationSuccessNode.getText());
            }
        } catch (DocumentException e) {
            log.error("CasLoginHandler [authenticate] cas解析失败", e);
        }
        return null;
    }
}
