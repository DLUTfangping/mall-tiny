package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 药品字典表
 * </p>
 *
 * @author macro
 * @since 2026-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("medicine_drug")
@ApiModel(value="MedicineDrug对象", description="药品字典表")
public class MedicineDrug implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "药品编码")
    private String drugCode;

    @ApiModelProperty(value = "药品名称")
    private String drugName;

    @ApiModelProperty(value = "通用名")
    private String commonName;

    @ApiModelProperty(value = "英文名")
    private String englishName;

    @ApiModelProperty(value = "药品类型：WESTERN=西药, TCM=中成药, HERB=中药饮片")
    private String drugType;

    @ApiModelProperty(value = "规格（如10mg×100片）")
    private String spec;

    @ApiModelProperty(value = "单位（盒、瓶、片、克）")
    private String unit;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "批准文号")
    private String approvalNumber;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "储存条件")
    private String storageCondition;

    @ApiModelProperty(value = "最低库存量")
    private BigDecimal minStock;

    @ApiModelProperty(value = "最高库存量")
    private BigDecimal maxStock;

    @ApiModelProperty(value = "默认供应商")
    private String defaultSupplier;

    @ApiModelProperty(value = "医保编码")
    private String insuranceCode;

    @ApiModelProperty(value = "基本药物标识：0=否，1=是")
    private Integer isEssential;

    @ApiModelProperty(value = "药品分类：NORMAL=普通药品, ANESTHETIC=麻醉药品, PSYCHOTROPIC_I=精神类I类, PSYCHOTROPIC_II=精神类II类")
    private String drugCategory;

    @ApiModelProperty(value = "基本规格（如0.75mg/片）")
    private String baseSpec;

    @ApiModelProperty(value = "处方药分类：OTC_RX=处方药, OTC_OTC=非处方药, OTC_BOTH=双跨")
    private String prescriptionType;

    @ApiModelProperty(value = "是否需要皮试：0=否，1=是")
    private Integer skinTestRequired;

    @ApiModelProperty(value = "剂型：TABLET片剂, CAPSULE胶囊, INJECTION注射剂, GRANULE颗粒, SOLUTION溶液, OINTMENT软膏, PATCH贴剂, HERB饮片")
    private String dosageForm;

    @ApiModelProperty(value = "基本单位：片、粒、支、克、毫升")
    private String baseUnit;

    @ApiModelProperty(value = "转换率：包装单位与基本单位的转换关系")
    private BigDecimal conversionRate;

    @ApiModelProperty(value = "状态：0=禁用，1=启用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
