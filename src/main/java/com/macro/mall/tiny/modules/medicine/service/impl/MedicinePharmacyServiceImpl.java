package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.medicine.mapper.MedicinePharmacyMapper;
import com.macro.mall.tiny.modules.medicine.model.MedicinePharmacy;
import com.macro.mall.tiny.modules.medicine.service.MedicinePharmacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 药房表 Service 实现类
 * </p>
 *
 * @author macro
 * @since 2026-05-09
 */
@Service
public class MedicinePharmacyServiceImpl extends ServiceImpl<MedicinePharmacyMapper, MedicinePharmacy> implements MedicinePharmacyService {

    @Override
    public Page<MedicinePharmacy> list(Integer status, String keyword, Integer pageSize, Integer pageNum) {
        Page<MedicinePharmacy> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MedicinePharmacy> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("pharmacy_name", keyword).or().like("creator", keyword));
        }
        wrapper.orderByDesc("create_time");
        return page(page, wrapper);
    }

    @Override
    @Transactional
    public boolean setDefault(Long id) {
        // 先将所有药房的默认标识设为0
        MedicinePharmacy update = new MedicinePharmacy();
        update.setIsDefault(0);
        update.setUpdateTime(new Date());
        this.update(update, new QueryWrapper<MedicinePharmacy>().eq("is_default", 1));

        // 再将目标药房设为默认
        update.setIsDefault(1);
        return this.update(update, new QueryWrapper<MedicinePharmacy>().eq("id", id));
    }
}
