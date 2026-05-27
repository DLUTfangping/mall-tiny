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
 * 字典明细表
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict_item")
@ApiModel(value = "SysDictItem对象", description = "字典明细表")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "关联字典类型编码")
    private String dictCode;

    @ApiModelProperty(value = "字典项编码（如：MALE）")
    private String itemCode;

    @ApiModelProperty(value = "字典项名称（如：男）")
    private String itemName;

    @ApiModelProperty(value = "排序")
    private Integer itemSort;

    @ApiModelProperty(value = "状态：0=禁用，1=启用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}