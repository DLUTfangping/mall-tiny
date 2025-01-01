package com.macro.mall.tiny.common.enums;

public enum CurrentEnum {
    NOT_LATEST(0, "不是最新的"),
    LATEST(1, "是最新的");

    private final int code;
    private final String description;

    // 构造方法
    CurrentEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // 获取枚举的 code 值
    public int getCode() {
        return code;
    }

    // 获取枚举的描述信息
    public String getDescription() {
        return description;
    }

    // 通过 code 获取对应的枚举
    public static CurrentEnum fromCode(int code) {
        for (CurrentEnum current : values()) {
            if (current.code == code) {
                return current;
            }
        }
        throw new IllegalArgumentException("未知的 code 值: " + code);
    }

    @Override
    public String toString() {
        return String.format("Current{code=%d, description='%s'}", code, description);
    }
}

