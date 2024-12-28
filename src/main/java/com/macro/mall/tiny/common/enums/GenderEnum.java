package com.macro.mall.tiny.common.enums;

/**
 * 性别枚举类
 * 表示性别的不同状态：男、女、其他、未定义。
 */
public enum GenderEnum {
    MALE(0, "男"),
    FEMALE(1, "女"),
    OTHER(2, "其他"),
    UNDEFINED(3, "未定义");

    private final int code;
    private final String description;

    // 构造方法
    GenderEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取性别代码
     *
     * @return 性别代码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取性别描述
     *
     * @return 性别描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举值
     *
     * @param code 性别代码
     * @return 对应的GenderEnum，若无匹配则返回 UNDEFINED
     */
    public static GenderEnum fromCode(int code) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getCode() == code) {
                return gender;
            }
        }
        return UNDEFINED;
    }

    @Override
    public String toString() {
        return String.format("GenderEnum{code=%d, description='%s'}", code, description);
    }
}

