package com.macro.mall.tiny.modules.com.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 病人住院信息表
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
@Getter
@Setter
@TableName("com_patient_admission")
@ApiModel(value = "ComPatientAdmission对象", description = "病人住院信息表")
public class ComPatientAdmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("住院记录的主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("病人id，关联 patient 表")
    private Integer patientId;

    @ApiModelProperty("住院编号")
    private String hospitalNum;

    @ApiModelProperty("手环号")
    private String wristbandName;

    @ApiModelProperty("病房 ID，关联 ward 表")
    private Integer roomId;

    @ApiModelProperty("病床 ID，关联 bed 表")
    private Integer bedId;

    @ApiModelProperty("组室 ID，关联 department 表")
    private Integer departmentId;

    @ApiModelProperty("处置建议 0-紧急处置 1-放射沾染 2-隔离 3-染毒")
    private String suggestion;

    @ApiModelProperty("入院时间")
    private Date admissionDate;

    @ApiModelProperty("出院时间")
    private Date dischargeDate;

    @ApiModelProperty("状态（0-住院中，1-已出院）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
