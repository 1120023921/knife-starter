package com.wingice.msg.mail.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.msg.mail.entity.MailAccount;
import com.wingice.msg.mail.service.MailAccountService;
import com.wingice.msg.mail.validator.MailAccountValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 邮件账户信息 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-11
 */
@RequestMapping("/mailAccount")
@Tag(name = "邮件账户管理")
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
    @ResponseBody
    @PostMapping("/saveOrUpdateMailAccount")
    @Operation(summary = "保存邮件账户信息")
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
    @ResponseBody
    @PostMapping("/deleteMailAccount")
    @Operation(summary = "删除邮件账户")
    public ResultBean<Boolean> deleteMailAccount() {
        return ResultBean.restResult(mailAccountService.deleteMailAccount(), ErrorCodeInfo.OK);
    }

    /**
     * @description 获取邮件账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/13 18:42
     */
    @ResponseBody
    @GetMapping("/getMailAccount")
    @Operation(summary = "获取邮件账户")
    public ResultBean<MailAccount> getMailAccount() {
        return ResultBean.restResult(mailAccountService.getOne(Wrappers.lambdaQuery()), ErrorCodeInfo.OK);
    }
}

