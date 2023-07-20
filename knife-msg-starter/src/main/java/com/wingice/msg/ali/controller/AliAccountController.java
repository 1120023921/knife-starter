package com.wingice.msg.ali.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.msg.ali.entity.AliAccount;
import com.wingice.msg.ali.service.AliAccountService;
import com.wingice.msg.ali.validator.AliAccountValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 表基础信息 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-13
 */
@RequestMapping("/aliAccount")
@Tag(name = "阿里短信账户管理")
public class AliAccountController {

    private final AliAccountService aliAccountService;

    public AliAccountController(AliAccountService aliAccountService) {
        this.aliAccountService = aliAccountService;
    }

    /**
     * @param aliAccount 阿里短信账户信息
     * @description 保存阿里短信账户信息
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 17:07
     */
    @ResponseBody
    @PostMapping("/saveOrUpdateAliAccount")
    @Operation(summary = "保存阿里短信账户信息")
    public ResultBean<Boolean> saveOrUpdateAliAccount(@RequestBody AliAccount aliAccount) {
        AliAccountValidator.saveOrUpdateAliAccount(aliAccount);
        return ResultBean.restResult(aliAccountService.saveOrUpdateAliAccount(aliAccount), ErrorCodeInfo.OK);
    }

    /**
     * @description 删除阿里短信账户
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 17:14
     */
    @ResponseBody
    @PostMapping("/deleteAliAccount")
    @Operation(summary = "删除阿里短信账户")
    public ResultBean<Boolean> deleteAliAccount() {
        return ResultBean.restResult(aliAccountService.deleteAliAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取阿里短信账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:41
     */
    @ResponseBody
    @GetMapping("/getAliAccount")
    @Operation(summary = "获取阿里短信账户")
    public ResultBean<AliAccount> getAliAccount() {
        return ResultBean.restResult(aliAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

