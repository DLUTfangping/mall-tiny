package com.macro.mall.tiny.common.enums;
/**
 * 枚举类表示住院状态
 * 0: 住院中
 * 1: 已出院
 */
public enum AdmissionStatusEnum {
    INHOSPITAL(0, "住院中"),
    TRANSFERRING(2, "转移中"),
    DISCHARGED(1, "已出院");

    private final int code;
    private final String description;

    AdmissionStatusEnum(int code, String description) {
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
    public static AdmissionStatusEnum fromCode(int code) {
        for (AdmissionStatusEnum status : AdmissionStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("AdmissionStatus{code=%d, description='%s'}", code, description);
    }
}