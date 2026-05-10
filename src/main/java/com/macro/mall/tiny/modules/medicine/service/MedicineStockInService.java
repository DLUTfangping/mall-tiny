package com.macro.mall.tiny.modules.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockIn;

import java.util.List;

/**
 * <p>
 * 药材入库单服务接口
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
public interface MedicineStockInService extends IService<MedicineStockIn> {

    /**
     * 分页查询入库单列表
     */
    Page<MedicineStockIn> list(Long pharmacyId, Integer status, String keyword, Integer pageSize, Integer pageNum);

    /**
     * 获取入库单详情（包含明细）
     */
    MedicineStockIn getDetail(Long id);

    /**
     * 创建入库单
     */
    boolean create(MedicineStockIn stockIn);

    /**
     * 更新入库单
     */
    boolean update(Long id, MedicineStockIn stockIn);

    /**
     * 删除入库单
     */
    boolean delete(Long id);

    /**
     * 确认入库（更新库存）
     */
    boolean confirm(Long id);

    /**
     * 药品模糊搜索
     */
    List<MedicineDrug> searchDrugs(String keyword);
}