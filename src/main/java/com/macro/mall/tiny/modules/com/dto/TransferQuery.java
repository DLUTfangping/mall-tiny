package com.macro.mall.tiny.modules.com.dto;

import lombok.Data;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.com.dto
 * @Author: Pikachu
 * @CreateTime: 2024-12-30 21:03:29
 * @Description: 数据库查询条件
 * @Version: 1.0
 */
@Data
public class TransferQuery {
    // 当前组室id
    private Integer currentDepartmentId;
    // 姓名
    private String name;
    // 0 未接收 1 已接收 2 已驳回
    private Integer transferStatus;
    // 是否是最新转移记录（0-否，1-是）
    private Integer current;
}
