package com.macro.mall.tiny.modules.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.medicine.model.MedicineStock;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 药材库存 Service 接口
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
public interface MedicineStockService extends IService<MedicineStock> {

    /**
     * 分页查询库存列表
     * @param pharmacyId 药房ID
     * @param drugName 药品名称（模糊查询）
     * @param batchNo 批号
     * @param drugCategory 药品分类
     * @param drugType 药材类型
     * @param pageSize 每页数量
     * @param pageNum 页码
     * @return 分页结果
     */
    Page<MedicineStock> list(Long pharmacyId, String drugName, String batchNo, String drugCategory, String drugType, Integer pageSize, Integer pageNum);

    /**
     * 获取库存详情
     * @param id 库存ID
     * @return 库存详情
     */
    MedicineStock getDetail(Long id);

    /**
     * 获取某个药品的库存列表
     * @param drugId 药品ID
     * @return 库存列表
     */
    List<MedicineStock> listByDrugId(Long drugId);

    /**
     * 根据药房获取库存药品列表（带药品详细信息）
     * @param pharmacyId 药房ID
     * @return 库存药品列表
     */
    List<Map<String, Object>> getStockListByPharmacy(Long pharmacyId);
}