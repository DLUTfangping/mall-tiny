package com.macro.mall.tiny.modules.classify.model;

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
 * 标识管理表
 * </p>
 *
 * @author macro
 * @since 2024-12-21
 */
@Getter
@Setter
@TableName("classify_wristband")
@ApiModel(value = "ClassifyWristband对象", description = "标识管理表")
public class ClassifyWristband implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一标识ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("标识名称")
    private String name;

    @ApiModelProperty("固话码")
    private String fixedCode;

    @ApiModelProperty("启用状态: 0-启用, 1-禁用")
    private Integer status;

    @ApiModelProperty("绑定状态: 0-未绑定, 1-已绑定")
    private Integer bounded;

    @ApiModelProperty("绑定的人员编号")
    private String boundPersonNum;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
