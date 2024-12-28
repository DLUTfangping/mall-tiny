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
 * 病人转移记录表
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
@Getter
@Setter
@TableName("com_patient_transfer")
@ApiModel(value = "ComPatientTransfer对象", description = "病人转移记录表")
public class ComPatientTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("转移记录的主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("病人id，关联 patient 表")
    private Integer patientId;

    @ApiModelProperty("来源组室编号，关联 department 表")
    private Integer fromDepartmentId;

    @ApiModelProperty("目标组室编号，关联 department 表")
    private Integer toDepartmentId;

    @ApiModelProperty("转出房间 ID，关联 room 表")
    private Integer fromRoomId;

    @ApiModelProperty("转入房间 ID，关联 room 表")
    private Integer toRoomId;

    @ApiModelProperty("转出床位，关联Bed")
    private Integer fromBedId;

    @ApiModelProperty("转入床位，关联Bed")
    private Integer toBedId;

    @ApiModelProperty("申请转移时间")
    private Date transferTime;

    @ApiModelProperty("接收时间")
    private Date receiveTime;

    @ApiModelProperty("驳回时间")
    private Date rejectTime;

    @ApiModelProperty("转移状态（0 待接收、1 已接收、2 已驳回）")
    private Integer transferStatus;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
