package com.macro.mall.tiny.common.enums;

public enum DepartmentCodeEnum {
    MZ(0, "mz","门诊"),
    FL(1, "fl", "分类"),
    SR(2, "sr","收容"),
    ZZ(3, "zz","重症"),
    CR(4, "cr","传染"),
    SS(5, "ss","手术"),
    YF(6, "yf","药房"),
    WZ(7, "wz","物资"),
    JY(8, "yf","检验"),
    JC(9, "jc","检查"),
    XX(10, "ss","洗消"),
    GL(11, "gl","管理");

    private final int code;
    private final String shortCode;
    private final String description;

    // 构造方法
    DepartmentCodeEnum(int code, String shortCode, String description) {
        this.code = code;
        this.shortCode = shortCode;
        this.description = description;
    }

    // Getter 方法
    public int getCode() {
        return code;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getDescription() {
        return description;
    }

    // 可选：根据 code 查找枚举常量的方法
    public static DepartmentCodeEnum fromCode(int code) {
        for (DepartmentCodeEnum value : DepartmentCodeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    // 可选：根据 shortCode 查找枚举常量的方法
    public static DepartmentCodeEnum fromShortCode(String shortCode) {
        for (DepartmentCodeEnum value : DepartmentCodeEnum.values()) {
            if (value.getShortCode().equals(shortCode)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown short code: " + shortCode);
    }

    @Override
    public String toString() {
        return "DepartmentCodeEnum{" +
                "code=" + code +
                ", shortCode='" + shortCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
