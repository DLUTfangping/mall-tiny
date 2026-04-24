package com.macro.mall.tiny.modules.com.dto;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.com.dto
 * @Author: Pikachu
 * @CreateTime: 2025-01-04 22:30:21
 * @Description: 用于获取待接收病人列表  TODO 其他属性字段待补充
 * @Version: 1.0
 */
@ApiModel(value = "PendingPatientsDTO", description = "收容、重症普通组室病人列表视图")
@Data
public class PendingPatientsDTO {

    @ApiModelProperty("转移ID")
    private Integer transferId;

    @ApiModelProperty("来源组室ID")
    private Integer fromDepartmentId;

    @ApiModelProperty("来源组室名称")
    private Integer fromDepartmentName;

    @ApiModelProperty("病人ID")
    private Integer patientId;

    @ApiModelProperty("姓名")
    private String patientName;

    @ApiModelProperty("保障标识牌")
    private String signage;

    @ApiModelProperty("手环号")
    private String wristbandName;

    @ApiModelProperty("性别（0 男、1 女、2 其他、3 未填写）")
    private Integer gender;

    @ApiModelProperty("出生日期")
    private Date birthDate;

    @ApiModelProperty("处置建议")
    private String suggestion;

    @ApiModelProperty("申请转移时间")
    private Date transferTime;

    @TableField(exist = false)
    @ApiModelProperty("年龄")
    private Integer age;


    // 自定义方法，根据 birthDate 设置 age
    public Integer getAge() {
        if (birthDate == null) {
            return null; // 如果出生日期为空，则年龄返回空
        }
        // 使用 Hutool 的 DateUtil 计算年龄
        return DateUtil.ageOfNow(birthDate);
    }

}
