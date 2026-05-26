package com.macro.mall.tiny.modules.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockOut;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 药材出库单 Service
 * </p>
 *
 * @author macro
 * @since 2026-05-12
 */
public interface MedicineStockOutService {

    /**
     * 获取出库单列表
     */
    Page<MedicineStockOut> list(Long pharmacyId, Integer status, String keyword, Integer pageSize, Integer pageNum);

    /**
     * 获取出库单详情
     */
    MedicineStockOut getDetail(Long id);

    /**
     * 创建出库单
     */
    boolean create(MedicineStockOut stockOut);

    /**
     * 更新出库单
     */
    boolean update(Long id, MedicineStockOut stockOut);

    /**
     * 删除出库单
     */
    boolean delete(Long id);

    /**
     * 根据药房获取库存药品列表
     */
    List<Map<String, Object>> getStockByPharmacy(Long pharmacyId);
}