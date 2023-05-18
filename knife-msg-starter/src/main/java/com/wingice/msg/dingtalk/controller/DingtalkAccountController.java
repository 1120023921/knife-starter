package com.wingice.msg.dingtalk.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;
import com.wingice.msg.dingtalk.service.DingtalkAccountService;
import com.wingice.msg.dingtalk.validator.DingtalkAccountValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 表基础信息 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-14
 */
@Api(value = "/dingtalkAccount", tags = "钉钉账户管理")
@ConditionalOnProperty(name = "knife.msg.dingtalk.wechat-mp-account-api-enable", havingValue = "true")
@Controller
@RequestMapping("/dingtalkAccount")
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
    @ApiOperation("保存钉钉账户账户信息")
    @ResponseBody
    @PostMapping("/saveOrUpdateDingtalkAccount")
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
    @ApiOperation("删除钉钉账户账户")
    @ResponseBody
    @PostMapping("/deleteDingtalkAccount")
    public ResultBean<Boolean> deleteDingtalkAccount() {
        return ResultBean.restResult(dingtalkAccountService.deleteDingtalkAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取钉钉账户账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:42
     */
    @ApiOperation("获取钉钉账户账户")
    @ResponseBody
    @GetMapping("/getDingtalkAccount")
    public ResultBean<DingtalkAccount> getDingtalkAccount() {
        return ResultBean.restResult(dingtalkAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

