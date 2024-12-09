package com.macro.mall.tiny.common.enums;

public enum CommonStatus {
    ACTIVE(0, "有效"),
    INACTIVE(1, "无效"),
    DELETED(2, "删除");

    private final int code;
    private final String description;

    CommonStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取状态代码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取状态描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     */
    public static CommonStatus fromCode(int code) {
        for (CommonStatus status : CommonStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的状态码：" + code);
    }

    @Override
    public String toString() {
        return "CommonStatus{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
