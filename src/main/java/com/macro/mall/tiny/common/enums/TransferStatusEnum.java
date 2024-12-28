package com.macro.mall.tiny.common.enums;



/**
 * 枚举类表示转移状态
 * 0: 待接收
 * 1: 已接收
 * 2: 已驳回
 */
public enum TransferStatusEnum {
    PENDING(0, "待接收"),
    RECEIVED(1, "已接收"),
    REJECTED(2, "已驳回");

    private final int code;
    private final String description;

    TransferStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取状态码
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取状态描述
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据状态码获取枚举值
     * @param code 状态码
     * @return 对应的枚举值，如果找不到返回 null
     */
    public static TransferStatusEnum fromCode(int code) {
        for (TransferStatusEnum status : TransferStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("TransferStatus{code=%d, description='%s'}", code, description);
    }
}