package com.cintsoft.msg.ali.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.msg.ali.entity.AliAccount;
import com.cintsoft.msg.ali.service.AliAccountService;
import com.cintsoft.msg.ali.validator.AliAccountValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 表基础信息 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-13
 */
@Api(value = "/aliAccount", tags = "阿里短信账户管理")
@RequestMapping("/aliAccount")
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
    @ApiOperation("保存阿里短信账户信息")
    @ResponseBody
    @PostMapping("/saveOrUpdateAliAccount")
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
    @ApiOperation("删除阿里短信账户")
    @ResponseBody
    @PostMapping("/deleteAliAccount")
    public ResultBean<Boolean> deleteAliAccount() {
        return ResultBean.restResult(aliAccountService.deleteAliAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取阿里短信账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:41
     */
    @ApiOperation("获取阿里短信账户")
    @ResponseBody
    @GetMapping("/getAliAccount")
    public ResultBean<AliAccount> getAliAccount() {
        return ResultBean.restResult(aliAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

