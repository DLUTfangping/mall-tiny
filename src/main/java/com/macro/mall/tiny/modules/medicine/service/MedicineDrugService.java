package com.macro.mall.tiny.modules.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;

import java.util.List;

/**
 * <p>
 * 药品字典表 Service 接口
 * </p>
 *
 * @author macro
 * @since 2026-04-27
 */
public interface MedicineDrugService extends IService<MedicineDrug> {

    /**
     * 分页查询药品字典
     */
    Page<MedicineDrug> list(String drugType, String prescriptionType, Integer status, String keyword, Integer pageSize, Integer pageNum);

    /**
     * 搜索药品字典
     */
    List<MedicineDrug> search(String keyword);
}
