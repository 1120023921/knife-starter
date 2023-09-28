package com.wingice.msg.dingtalk.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;
import com.wingice.msg.dingtalk.service.DingtalkAccountService;
import com.wingice.msg.dingtalk.validator.DingtalkAccountValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 表基础信息 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-14
 */
@RequestMapping("/dingtalkAccount")
@Tag(name = "钉钉账户管理")
public class DingtalkAccountController {

    private final DingtalkAccountService dingtalkAccountService;

    public DingtalkAccountController(DingtalkAccountService dingtalkAccountService) {
        this.dingtalkAccountService = dingtalkAccountService;
    }

    /**
     * @param dingtalkAccount 钉钉账户账户信息
     * @description 保存钉钉账户账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:07
     */
    @ResponseBody
    @PostMapping("/saveOrUpdateDingtalkAccount")
    @Operation(summary = "保存钉钉账户账户信息")
    public ResultBean<Boolean> saveOrUpdateDingtalkAccount(@RequestBody DingtalkAccount dingtalkAccount) {
        DingtalkAccountValidator.saveOrUpdateDingtalkAccount(dingtalkAccount);
        return ResultBean.restResult(dingtalkAccountService.saveOrUpdateDingtalkAccount(dingtalkAccount), ErrorCodeInfo.OK);
    }

    /**
     * @description 删除钉钉账户账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:14
     */
    @ResponseBody
    @PostMapping("/deleteDingtalkAccount")
    @Operation(summary = "删除钉钉账户账户")
    public ResultBean<Boolean> deleteDingtalkAccount() {
        return ResultBean.restResult(dingtalkAccountService.deleteDingtalkAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取钉钉账户账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:42
     */
    @ResponseBody
    @GetMapping("/getDingtalkAccount")
    @Operation(summary = "获取钉钉账户账户")
    public ResultBean<DingtalkAccount> getDingtalkAccount() {
        return ResultBean.restResult(dingtalkAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

