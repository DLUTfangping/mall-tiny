package com.macro.mall.tiny.modules.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.medicine.model.MedicinePharmacy;

/**
 * <p>
 * 药房表 Service 接口
 * </p>
 *
 * @author macro
 * @since 2026-05-09
 */
public interface MedicinePharmacyService extends IService<MedicinePharmacy> {

    /**
     * 分页查询药房
     */
    Page<MedicinePharmacy> list(Integer status, String keyword, Integer pageSize, Integer pageNum);

    /**
     * 设置默认药房（只能有一个默认药房）
     */
    boolean setDefault(Long id);
}
