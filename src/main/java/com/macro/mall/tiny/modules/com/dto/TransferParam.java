package com.macro.mall.tiny.modules.com.dto;

import lombok.Data;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.com.dto
 * @Author: Pikachu
 * @CreateTime: 2024-12-30 22:01:54
 * @Description: 病人流转记录查询参数封装
 * @Version: 1.0
 */
@Data
public class TransferParam {
    // 病人id
    private Integer patientId;
    // 组室id
    private Integer departmentId;

}
