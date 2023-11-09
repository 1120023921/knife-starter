package com.wingice.test;

import com.wingice.test.service.MailAccountService;
import com.wingice.test.service.MailReceiveService;
import com.wingice.test.service.MailSendService;
import com.wingice.test.service.impl.MailReceiveServiceImpl;
import com.wingice.test.service.impl.MailSendServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnifeMailAutoConfiguration {

    @ConditionalOnBean(MailAccountService.class)
    @ConditionalOnMissingBean(MailSendService.class)
    @Bean
    public MailSendService mailSendService(MailAccountService mailAccountService){
        return new MailSendServiceImpl(mailAccountService);
    }

    @ConditionalOnBean(MailAccountService.class)
    @ConditionalOnMissingBean(MailReceiveService.class)
    @Bean
    public MailReceiveService mailReceiveService(MailAccountService mailAccountService){
        return new MailReceiveServiceImpl(mailAccountService);
    }
}
