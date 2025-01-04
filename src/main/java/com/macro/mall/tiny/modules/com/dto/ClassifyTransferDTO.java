package com.macro.mall.tiny.modules.com.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "ClassifyTransferDTO", description = "分类/驳回病人列表视图")
@Data
public class ClassifyTransferDTO {
    @ApiModelProperty("申请转移ID")
    private Integer transferId;
    @ApiModelProperty("病人ID")
    private Integer patientId;
    @ApiModelProperty("姓名")
    private String patientName;
    @ApiModelProperty("申请转移时间")
    private Date transferTime;
    @ApiModelProperty("去往组室ID")
    private Integer toDepartmentId;
    @ApiModelProperty("去往组室名称")
    private String toDepartmentName;
    @ApiModelProperty("处置建议")
    private String suggestion;
    @ApiModelProperty("驳回原因")
    private String rejectReason;
    @ApiModelProperty("数据来源 0 分类 1 导入 2 数据包（数字）")
    private Integer dataOriginal;
    @ApiModelProperty("数据来源 0 分类 1 导入 2 数据包（字符串）")
    @TableField(exist = false)
    private String dataOriginalDescription;

    // 自定义方法，根据 dataOriginal 设置 dataOriginalDescription
    public void setDataOriginal(Integer dataOriginal) {
        this.dataOriginal = dataOriginal;
        this.dataOriginalDescription = mapDataSource(dataOriginal);
    }
    private String mapDataSource(Integer dataOriginal) {
        if (dataOriginal == null) {
            return "未知";
        }
        switch (dataOriginal) {
            case 0:
                return "分类";
            case 1:
                return "导入";
            case 2:
                return "数据包";
            default:
                return "其他";
        }
    }
}
