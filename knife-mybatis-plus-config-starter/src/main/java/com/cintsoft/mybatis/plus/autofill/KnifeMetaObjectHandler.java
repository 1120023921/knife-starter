package com.cintsoft.mybatis.plus.autofill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author 胡昊
 * Description: 自动填充配置
 * Date: 2020/7/23
 * Time: 11:02
 * Mail: huhao9277@gmail.com
 */
public class KnifeMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", null);
        metaObject.setValue("updateTime", null);
        metaObject.setValue("createBy", null);
        metaObject.setValue("updateBy", null);
        this.strictInsertFill(metaObject, "createTime", Long.class, System.currentTimeMillis()); // 起始版本 3.3.0(推荐使用)
//        //获取用户信息
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() != null && !"anonymousUser".equals(authentication.getPrincipal())) {
//            this.strictInsertFill(metaObject, "createBy", String.class, ((SysUser) authentication.getPrincipal()).getId());
//        }
        updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", null);
        metaObject.setValue("updateBy", null);
        this.strictUpdateFill(metaObject, "updateTime", Long.class, System.currentTimeMillis()); // 起始版本 3.3.0(推荐使用)
        //获取用户信息
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() != null && !"anonymousUser".equals(authentication.getPrincipal())) {
//            this.strictInsertFill(metaObject, "updateBy", String.class, ((SysUser) authentication.getPrincipal()).getId());
//        }
    }
}
