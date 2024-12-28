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
 * 病人信息表
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
@Getter
@Setter
@TableName("com_patient")
@ApiModel(value = "ComPatient对象", description = "病人信息表")
public class ComPatient implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("病人主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("病人编号")
    private String patientNum;

    @ApiModelProperty("病人姓名")
    private String name;

    @ApiModelProperty("性别（0 男、1 女、2 其他、3 未填写）")
    private Integer gender;

    @ApiModelProperty("出生日期")
    private Date birthDate;

    @ApiModelProperty("联系电话")
    private String contactNumber;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("保障标识牌")
    private String signage;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
