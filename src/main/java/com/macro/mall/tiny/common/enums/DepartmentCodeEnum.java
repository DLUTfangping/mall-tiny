package com.macro.mall.tiny.common.enums;

public enum DepartmentCodeEnum {
    MZ("mz","门诊"),
    FL("fl", "分类"),
    SR("sr","收容"),
    ZZ("zz","重症"),
    SS("ss","手术"),
    CR("cr","传染"),
    YF("yf","药房"),
    WZ("wz","物资"),
    JY("yf","检验"),
    JC("jc","检查"),
    XX("ss","洗消"),
    GL("gl","管理");

    private final String shortCode;
    private final String description;

    // 构造方法
    DepartmentCodeEnum(String shortCode, String description) {
        this.shortCode = shortCode;
        this.description = description;
    }

    // Getter 方法

    public String getShortCode() {
        return shortCode;
    }

    public String getDescription() {
        return description;
    }

    // 可选：根据 code 查找枚举常量的方法
//    public static DepartmentCodeEnum fromCode(int code) {
//        for (DepartmentCodeEnum value : DepartmentCodeEnum.values()) {
//            if (value.getCode() == code) {
//                return value;
//            }
//        }
//        throw new IllegalArgumentException("Unknown code: " + code);
//    }

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
                ", shortCode='" + shortCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
