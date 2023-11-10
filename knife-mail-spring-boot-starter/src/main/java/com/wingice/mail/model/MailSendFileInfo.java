package com.wingice.mail.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * 发送邮件附件信息
 */
@Data
@Builder
@ToString
public class MailSendFileInfo {

    @Tolerate
    public MailSendFileInfo() {
    }

    //文件名
    private String fileName;
    //MIME type
    private String type;
    //文件流
    private InputStream inputStream;
    //文件头参数
    @Builder.Default
    private Map<String, String> header = Collections.emptyMap();
}
