package com.macro.mall.tiny.modules.classify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.classify.dto
 * @Author: Pikachu
 * @CreateTime: 2024-12-25 21:59:21
 * @Description: 快速分类参数
 * @Version: 1.0
 */
@Data
@ApiModel(value = "快速分类参数", description = "快速分类参数")
public class FastClassifyParam {

    @ApiModelProperty("标识牌")
    private String signage;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("分类时间")
    @NotNull(message = "时间不能为空")
    @PastOrPresent(message = "不能是未来的时间")
    private Date classifyTime;

    @ApiModelProperty("手环号")
    @NotBlank(message = "手环号不能为空")
    private String wristbandName;

    @ApiModelProperty("分类去向组室ID")
    @NotNull(message = "分类去向组室不能为空")
    private Integer toDepartmentId;

    @ApiModelProperty("分类去向病房ID")
    @NotNull(message = "分类去向病房不能为空")
    private Integer toRoomId;

    @ApiModelProperty("处置建议")
    private String suggestion;

    @ApiModelProperty("备注")
    private String note;
}
