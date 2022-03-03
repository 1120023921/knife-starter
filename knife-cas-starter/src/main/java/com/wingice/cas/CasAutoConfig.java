package com.wingice.cas;

import com.wingice.cas.common.constant.CasConfigProperties;
import com.wingice.cas.handler.CasLoginHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/9/18
 * Time: 11:13
 * Mail: huhao9277@gmail.com
 */
public class CasAutoConfig {

    @ConditionalOnProperty(value = "knife.security.cas.enable", havingValue = "true")
    @ConditionalOnMissingBean(name = "cas")
    @Bean("cas")
    public CasLoginHandler casLoginHandler(CasConfigProperties casConfigProperties, UserDetailsService userDetailsService) {
        return new CasLoginHandler(casConfigProperties, userDetailsService);
    }
}
