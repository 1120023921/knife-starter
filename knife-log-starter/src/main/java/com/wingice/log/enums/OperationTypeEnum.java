package com.wingice.log.enums;

/**
 * @author proven
 * @version 1.0
 * @description: 操作类型枚举类
 * @date 2021/6/25 13:38
 */
public enum OperationTypeEnum {

    ADD("ADD","新增"),

    DELETE("DELETE","删除"),

    UPDATE("UPDATE","修改"),

    SELECT("SELECT","查询");

    OperationTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private final String value;

    private final String desc;

    public String getValue(){
        return this.value;
    }

    public String getDesc(){
        return this.desc;
    }
}
