package com.wingice.mail;

import com.wingice.mail.service.*;
import com.wingice.mail.service.impl.MailOperateServiceImpl;
import com.wingice.mail.service.impl.MailReceiveServiceImpl;
import com.wingice.mail.service.impl.MailSendServiceImpl;
import com.wingice.mail.service.impl.StoreServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnifeMailAutoConfiguration {

    @ConditionalOnBean(MailAccountService.class)
    @ConditionalOnMissingBean(StoreService.class)
    @Bean
    public StoreService storeService(MailAccountService mailAccountService) {
        return new StoreServiceImpl(mailAccountService);
    }

    @ConditionalOnBean(StoreService.class)
    @ConditionalOnMissingBean(MailReceiveService.class)
    @Bean
    public MailReceiveService mailReceiveService(StoreService storeService) {
        return new MailReceiveServiceImpl(storeService);
    }

    @ConditionalOnBean({StoreService.class, MailAccountService.class})
    @ConditionalOnMissingBean(MailSendService.class)
    @Bean
    public MailSendService mailSendService(StoreService storeService, MailAccountService mailAccountService) {
        return new MailSendServiceImpl(storeService, mailAccountService);
    }

    @ConditionalOnBean(StoreService.class)
    @ConditionalOnMissingBean(MailOperateService.class)
    @Bean
    public MailOperateService mailOperateService(StoreService storeService) {
        return new MailOperateServiceImpl(storeService);
    }
}
