package com.macro.mall.tiny.modules.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict_type")
@ApiModel(value = "SysDictType对象", description = "字典类型表")
public class SysDictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "字典编码（如：SEX, DRUG_TYPE）")
    private String dictCode;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "业务分类（如：基础数据、药材业务）")
    private String category;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态：0=禁用，1=启用")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}