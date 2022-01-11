package com.cintsoft.msg.mail.service;

import com.cintsoft.msg.mail.entity.MailInfo;

import java.util.List;

public interface KnifeMailService {

    /**
     * @param mailInfo 邮件信息
     * @description 发送邮件
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 14:48
     */
    Boolean sendMail(MailInfo mailInfo);

    /**
     * @param mailInfoList 发送失败邮件列表
     * @description 批量发送邮件
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 14:48
     */
    List<MailInfo> sendMail(List<MailInfo> mailInfoList);
}
