package com.macro.mall.tiny.common.enums;

public enum BoundedEnum {
    UNBOUND(0, "未绑定"),
    BOUND(1, "已绑定");

    private final int code;
    private final String description;

    // 构造函数
    BoundedEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // 获取code值
    public int getCode() {
        return code;
    }

    // 获取描述
    public String getDescription() {
        return description;
    }

    // 根据code值获取枚举项
    public static BoundedEnum fromCode(int code) {
        for (BoundedEnum status : BoundedEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
