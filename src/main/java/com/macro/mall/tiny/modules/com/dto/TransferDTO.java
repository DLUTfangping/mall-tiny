package com.macro.mall.tiny.modules.com.dto;

import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.com.dto
 * @Author: Pikachu
 * @CreateTime: 2024-12-30 07:13:25
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class TransferDTO {
    private Integer transferId;
    private Integer patientId;
    private String patientName;
    private Date transferTime;
    private Integer toDepartmentId;
    private String admissionSuggestion; // 对应 suggestion 字段

    private String rejectReason;
}
