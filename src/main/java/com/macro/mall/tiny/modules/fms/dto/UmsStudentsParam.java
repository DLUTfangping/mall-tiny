package com.macro.mall.tiny.modules.fms.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author macro
 * @since 2024-06-22
 */
@Data
public class UmsStudentsParam {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("出生日期")
    private Date dateOfBirth;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("电话号码")
    private String phoneNumber;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("入学日期")
    private Date enrollmentDate;

    @ApiModelProperty("状态：0->禁用；1->启用")
    private Integer status;


}
