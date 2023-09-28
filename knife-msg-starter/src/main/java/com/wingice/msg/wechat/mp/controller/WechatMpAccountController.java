package com.wingice.msg.wechat.mp.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.msg.wechat.mp.entity.WechatMpAccount;
import com.wingice.msg.wechat.mp.service.WechatMpAccountService;
import com.wingice.msg.wechat.mp.validator.WechatMpAccountValidator;
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
@RequestMapping("/wechatMpAccount")
@Tag(name = "微信公众号账户管理")
public class WechatMpAccountController {

    private final WechatMpAccountService wechatMpAccountService;

    public WechatMpAccountController(WechatMpAccountService wechatMpAccountService) {
        this.wechatMpAccountService = wechatMpAccountService;
    }

    /**
     * @param wechatMpAccount 微信公众号账户账户信息
     * @description 保存微信公众号账户账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:07
     */
    @ResponseBody
    @PostMapping("/saveOrUpdateWechatMpAccount")
    @Operation(summary = "保存微信公众号账户账户信息")
    public ResultBean<Boolean> saveOrUpdateWechatMpAccount(@RequestBody WechatMpAccount wechatMpAccount) {
        WechatMpAccountValidator.saveOrUpdateWechatMpAccount(wechatMpAccount);
        return ResultBean.restResult(wechatMpAccountService.saveOrUpdateWechatMpAccount(wechatMpAccount), ErrorCodeInfo.OK);
    }

    /**
     * @description 删除微信公众号账户账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:14
     */
    @ResponseBody
    @PostMapping("/deleteWechatMpAccount")
    @Operation(summary = "删除微信公众号账户账户")
    public ResultBean<Boolean> deleteWechatMpAccount() {
        return ResultBean.restResult(wechatMpAccountService.deleteWechatMpAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取微信公众号账户账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:42
     */
    @ResponseBody
    @GetMapping("/getWechatMpAccount")
    @Operation(summary = "获取微信公众号账户账户")
    public ResultBean<WechatMpAccount> getWechatMpAccount() {
        return ResultBean.restResult(wechatMpAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

