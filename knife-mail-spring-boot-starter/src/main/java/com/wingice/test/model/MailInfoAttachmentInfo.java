package com.wingice.test.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * 接收邮件附件信息
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class MailInfoAttachmentInfo {

    @Tolerate
    public MailInfoAttachmentInfo() {
    }

    //文件名
    private String fileName;
    //contentId
    private String contentId;
    //bodyPartNum
    private Integer bodyPartNum;
}
