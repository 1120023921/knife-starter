package com.wingice.msg.dingtalk.vo.msg;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

@Data
@Builder
@EqualsAndHashCode
public class Text {

    @Tolerate
    public Text() {
    }

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
