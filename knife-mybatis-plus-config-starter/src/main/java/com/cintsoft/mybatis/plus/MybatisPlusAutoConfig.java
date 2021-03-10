package com.cintsoft.mybatis.plus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.cintsoft.mybatis.plus.autofill.KnifeMetaObjectHandler;
import com.cintsoft.mybatis.plus.properties.KnifeTenantConfigProperties;
import com.cintsoft.mybatis.plus.tenant.KnifeTenantLineHandler;
import com.cintsoft.mybatis.plus.tenant.TenantContextHolderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description: Mybatis配置
 * Date: 2020/7/23
 * Time: 10:57
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class MybatisPlusAutoConfig {

    /**
     * @param knifeTenantConfigProperties 租户配置信息
     * @description 租户默认配置
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 11:42
     */
    @Bean
    @ConditionalOnProperty(name = "knife.tenant.enable", havingValue = "true")
    @ConditionalOnMissingBean
    public TenantLineHandler tenantLineHandler(@Autowired KnifeTenantConfigProperties knifeTenantConfigProperties) {
        return new KnifeTenantLineHandler(knifeTenantConfigProperties);
    }

    /**
     * @description 自动填充默认配置
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 11:41
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public KnifeMetaObjectHandler knifeMetaObjectHandler() {
        return new KnifeMetaObjectHandler();
    }

    @Bean
    @ConditionalOnBean(TenantLineHandler.class)
    @ConditionalOnMissingBean
    public TenantContextHolderFilter tenantContextHolderFilter() {
        return new TenantContextHolderFilter();
    }

    /**
     * @param tenantLineHandler 租户处理器
     * @description Mybaits插件配置
     * @author 胡昊
     * @email huhao9277@gmail.com
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Autowired(required = false) TenantLineHandler tenantLineHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (tenantLineHandler != null) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler));
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
