package com.macro.mall.tiny.modules.mgs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 机构信息表
 * </p>
 *
 * @author macro
 * @since 2024-12-26
 */
@Getter
@Setter
@TableName("mgs_institution")
@ApiModel(value = "MgsInstitution对象", description = "机构信息表")
public class MgsInstitution implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("机构编号")
    private String institutionNum;

    @NotBlank(message = "区域编号不能为空")
    @ApiModelProperty("区域编号")
    private String areaNumber;

    @NotBlank(message = "机构代码不能为空")
    @ApiModelProperty("机构代码")
    private String institutionCode;

    @NotBlank(message = "机构名称不能为空")
    @ApiModelProperty("机构名称")
    private String name;

    @NotBlank(message = "上级地址不能为空")
    @ApiModelProperty("上级地址")
    private String superiorAddress;

    @NotBlank(message = "上级名称不能为空")
    @ApiModelProperty("上级名称")
    private String superiorName;

    @NotBlank(message = "上级编号不能为空")
    @ApiModelProperty("上级编号")
    private String superiorNum;

    @ApiModelProperty("上级代码")
    private String superiorCode;

    @ApiModelProperty("秘钥")
    private String secertKey;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
