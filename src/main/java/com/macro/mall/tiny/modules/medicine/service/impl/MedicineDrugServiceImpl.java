package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineDrugMapper;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.service.MedicineDrugService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 药品字典表 Service 实现类
 * </p>
 *
 * @author macro
 * @since 2026-04-27
 */
@Service
public class MedicineDrugServiceImpl extends ServiceImpl<MedicineDrugMapper, MedicineDrug> implements MedicineDrugService {

    @Override
    public Page<MedicineDrug> list(String drugType, String prescriptionType, Integer status, String keyword, Integer pageSize, Integer pageNum) {
        Page<MedicineDrug> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MedicineDrug> wrapper = new QueryWrapper<>();
        if (drugType != null && !drugType.isEmpty()) {
            wrapper.eq("drug_type", drugType);
        }
        if (prescriptionType != null && !prescriptionType.isEmpty()) {
            wrapper.eq("prescription_type", prescriptionType);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("drug_name", keyword).or().like("drug_code", keyword));
        }
        wrapper.orderByDesc("create_time");
        return page(page, wrapper);
    }

    @Override
    public List<MedicineDrug> search(String keyword) {
        QueryWrapper<MedicineDrug> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("drug_name", keyword).or().like("drug_code", keyword).or().like("common_name", keyword));
        }
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 50");
        return list(wrapper);
    }
}
