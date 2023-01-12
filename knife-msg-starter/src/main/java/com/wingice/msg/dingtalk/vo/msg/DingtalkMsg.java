package com.wingice.msg.dingtalk.vo.msg;

import cn.hutool.core.annotation.Alias;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

@Data
@Builder
@EqualsAndHashCode
public class DingtalkMsg {

    @Tolerate
    public DingtalkMsg() {
    }

    private Msg msg;
    @Alias("to_all_user")
    private String toAllUser;
    @Alias("agent_id")
    private String agentId;
    @Alias("dept_id_list")
    private String deptIdList;
    @Alias("userid_list")
    private String useridList;
    private String msgId;
}
