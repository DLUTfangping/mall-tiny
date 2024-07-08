package com.macro.mall.tiny.modules.fms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author macro
 * @since 2024-06-22
 */
@Getter
@Setter
@TableName("ums_students")
@ApiModel(value = "UmsStudents对象", description = "学生表")
public class UmsStudents implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int ON_STATE = 1;
    public static final int OFF_STATE = 0;

    @TableId(value = "id", type = IdType.AUTO)
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
