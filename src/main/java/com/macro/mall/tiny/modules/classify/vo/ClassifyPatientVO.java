package com.macro.mall.tiny.modules.classify.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.classify.vo
 * @Author: Pikachu
 * @CreateTime: 2024-12-28 16:19:17
 * @Description: 展示已分类、未分类、已驳回伤员信息
 * @Version: 1.0
 */
@ApiModel(value = "ClassifyPatientVO对象", description = "伤员分类视图")
@Data
public class ClassifyPatientVO {

    @ApiModelProperty("处置建议")
    private String Suggestion;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("去往组室名称")
    private String departmentName;

    @ApiModelProperty("申请转移时间")
    private Date transferTime;

    @ApiModelProperty("数据来源")
    private String dataOriginal;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

}
