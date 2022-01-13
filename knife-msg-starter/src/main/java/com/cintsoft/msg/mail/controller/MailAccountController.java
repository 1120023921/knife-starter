package com.cintsoft.msg.mail.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.msg.mail.entity.MailAccount;
import com.cintsoft.msg.mail.service.MailAccountService;
import com.cintsoft.msg.mail.validator.MailAccountValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 邮件账户信息 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-11
 */
@Api(value = "/mailAccount", tags = "邮件账户管理")
@RequestMapping("/mailAccount")
public class MailAccountController {

    private final MailAccountService mailAccountService;

    public MailAccountController(MailAccountService mailAccountService) {
        this.mailAccountService = mailAccountService;
    }

    /**
     * @param mailAccount 邮件账户信息
     * @description 保存邮件账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:07
     */
    @ApiOperation("保存邮件账户信息")
    @ResponseBody
    @PostMapping("/saveOrUpdateMailAccount")
    public ResultBean<Boolean> saveOrUpdateMailAccount(@RequestBody MailAccount mailAccount) {
        MailAccountValidator.saveOrUpdateMailAccount(mailAccount);
        return ResultBean.restResult(mailAccountService.saveOrUpdateMailAccount(mailAccount), ErrorCodeInfo.OK);
    }

    /**
     * @description 删除邮件账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:14
     */
    @ApiOperation("删除邮件账户")
    @ResponseBody
    @PostMapping("/deleteMailAccount")
    public ResultBean<Boolean> deleteMailAccount() {
        return ResultBean.restResult(mailAccountService.deleteMailAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取邮件账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:42
     */
    @ApiOperation("获取邮件账户")
    @ResponseBody
    @GetMapping("/getMailAccount")
    public ResultBean<MailAccount> getMailAccount() {
        return ResultBean.restResult(mailAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

