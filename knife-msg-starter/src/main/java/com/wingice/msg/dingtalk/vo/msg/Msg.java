package com.wingice.msg.dingtalk.vo.msg;

import cn.hutool.core.annotation.Alias;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

@Data
@Builder
@EqualsAndHashCode
public class Msg {

    @Tolerate
    public Msg() {
    }

    private Text text;
    @Alias("msgtype")
    private String msgType;
}
