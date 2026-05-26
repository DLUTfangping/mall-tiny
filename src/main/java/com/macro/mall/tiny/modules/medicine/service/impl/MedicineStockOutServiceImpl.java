package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineDrugMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicinePharmacyMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockOutDetailMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockOutMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockMapper;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.model.MedicinePharmacy;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockOut;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockOutDetail;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockOutService;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * 药材出库单 Service实现
 * </p>
 *
 * @author macro
 * @since 2026-05-12
 */
@Service
public class MedicineStockOutServiceImpl implements MedicineStockOutService {

    @Autowired
    private MedicineStockOutMapper stockOutMapper;

    @Autowired
    private MedicineStockOutDetailMapper stockOutDetailMapper;

    @Autowired
    private MedicineStockService stockService;

    @Autowired
    private MedicineDrugMapper drugMapper;

    @Autowired
    private MedicinePharmacyMapper pharmacyMapper;

    @Override
    public Page<MedicineStockOut> list(Long pharmacyId, Integer status, String keyword, Integer pageSize, Integer pageNum) {
        Page<MedicineStockOut> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MedicineStockOut> wrapper = new LambdaQueryWrapper<>();
        if (pharmacyId != null) {
            wrapper.eq(MedicineStockOut::getPharmacyId, pharmacyId);
        }
        if (status != null) {
            wrapper.eq(MedicineStockOut::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MedicineStockOut::getOutNo, keyword)
                    .or().like(MedicineStockOut::getRemark, keyword));
        }
        wrapper.orderByDesc(MedicineStockOut::getCreateTime);
        Page<MedicineStockOut> result = stockOutMapper.selectPage(page, wrapper);
        // 填充药房名称
        for (MedicineStockOut item : result.getRecords()) {
            if (item.getPharmacyId() != null) {
                MedicinePharmacy pharmacy = pharmacyMapper.selectById(item.getPharmacyId());
                if (pharmacy != null) {
                    item.setPharmacyName(pharmacy.getPharmacyName());
                }
            }
        }
        return result;
    }

    @Override
    public MedicineStockOut getDetail(Long id) {
        MedicineStockOut stockOut = stockOutMapper.selectById(id);
        if (stockOut != null) {
            // 填充药房名称
            if (stockOut.getPharmacyId() != null) {
                MedicinePharmacy pharmacy = pharmacyMapper.selectById(stockOut.getPharmacyId());
                if (pharmacy != null) {
                    stockOut.setPharmacyName(pharmacy.getPharmacyName());
                }
            }
            LambdaQueryWrapper<MedicineStockOutDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MedicineStockOutDetail::getOutId, id);
            List<MedicineStockOutDetail> details = stockOutDetailMapper.selectList(wrapper);
            // 填充每条明细的药品信息
            for (MedicineStockOutDetail detail : details) {
                if (detail.getDrugId() != null) {
                    MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
                    if (drug != null) {
                        detail.setManufacturer(drug.getManufacturer());
                    }
                }
            }
            stockOut.setDetails(details);
        }
        return stockOut;
    }

    @Override
    @Transactional
    public boolean create(MedicineStockOut stockOut) {
        String outNo = "OUT" + System.currentTimeMillis();
        stockOut.setOutNo(outNo);
        stockOut.setStatus(1);
        stockOut.setCreateTime(new Date());
        stockOut.setUpdateTime(new Date());
        // 自动设置操作人为当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            stockOut.setOperator(authentication.getName());
        } else {
            stockOut.setOperator("管理员");
        }

        int result = stockOutMapper.insert(stockOut);
        if (result > 0 && stockOut.getDetails() != null && !stockOut.getDetails().isEmpty()) {
            for (MedicineStockOutDetail detail : stockOut.getDetails()) {
                detail.setOutId(stockOut.getId());
                // 设置包装单位
                if (detail.getUnit() == null && detail.getDrugId() != null) {
                    MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
                    if (drug != null) {
                        detail.setUnit(drug.getUnit());
                    }
                }
                stockOutDetailMapper.insert(detail);
                reduceStock(detail, stockOut.getPharmacyId());
            }
            return true;
        }
        return result > 0;
    }

    @Override
    @Transactional
    public boolean update(Long id, MedicineStockOut stockOut) {
        MedicineStockOut existing = stockOutMapper.selectById(id);
        if (existing == null) {
            return false;
        }
        LambdaQueryWrapper<MedicineStockOutDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineStockOutDetail::getOutId, id);
        List<MedicineStockOutDetail> oldDetails = stockOutDetailMapper.selectList(wrapper);
        for (MedicineStockOutDetail oldDetail : oldDetails) {
            restoreStock(oldDetail, existing.getPharmacyId());
        }
        stockOutDetailMapper.delete(wrapper);

        stockOut.setId(id);
        stockOut.setUpdateTime(new Date());
        stockOutMapper.updateById(stockOut);

        if (stockOut.getDetails() != null && !stockOut.getDetails().isEmpty()) {
            for (MedicineStockOutDetail detail : stockOut.getDetails()) {
                detail.setOutId(id);
                stockOutDetailMapper.insert(detail);
                reduceStock(detail, stockOut.getPharmacyId());
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        MedicineStockOut stockOut = stockOutMapper.selectById(id);
        Long pharmacyId = stockOut != null ? stockOut.getPharmacyId() : null;
        LambdaQueryWrapper<MedicineStockOutDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineStockOutDetail::getOutId, id);
        List<MedicineStockOutDetail> details = stockOutDetailMapper.selectList(wrapper);
        for (MedicineStockOutDetail detail : details) {
            restoreStock(detail, pharmacyId);
        }
        stockOutDetailMapper.delete(wrapper);
        return stockOutMapper.deleteById(id) > 0;
    }

    @Override
    public List<Map<String, Object>> getStockByPharmacy(Long pharmacyId) {
        return stockService.getStockListByPharmacy(pharmacyId);
    }

    private void reduceStock(MedicineStockOutDetail detail, Long pharmacyId) {
        LambdaQueryWrapper<com.macro.mall.tiny.modules.medicine.model.MedicineStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.macro.mall.tiny.modules.medicine.model.MedicineStock::getDrugId, detail.getDrugId())
                .eq(com.macro.mall.tiny.modules.medicine.model.MedicineStock::getBatchNo, detail.getBatchNo())
                .eq(com.macro.mall.tiny.modules.medicine.model.MedicineStock::getPharmacyId, pharmacyId);
        com.macro.mall.tiny.modules.medicine.model.MedicineStock stock = stockService.getOne(wrapper);
        if (stock != null) {
            // 如果是包装单位，需要转换为基本单位后再扣减
            BigDecimal reduceQty = detail.getQuantity();
            if ("PACK".equals(detail.getUnitType())) {
                MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
                BigDecimal conversionRate = (drug != null && drug.getConversionRate() != null)
                        ? drug.getConversionRate() : BigDecimal.ONE;
                reduceQty = detail.getQuantity().multiply(conversionRate);
            }
            // 检查库存是否充足
            if (stock.getQuantity().compareTo(reduceQty) < 0) {
                throw new RuntimeException("库存不足：药品【" + detail.getDrugName() + "】批号【" + detail.getBatchNo() + "】库存(" + stock.getQuantity() + ")不足本次出库数量(" + reduceQty + ")");
            }
            stock.setQuantity(stock.getQuantity().subtract(reduceQty));
            stockService.updateById(stock);
        }
    }

    private void restoreStock(MedicineStockOutDetail detail, Long pharmacyId) {
        LambdaQueryWrapper<com.macro.mall.tiny.modules.medicine.model.MedicineStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.macro.mall.tiny.modules.medicine.model.MedicineStock::getDrugId, detail.getDrugId())
                .eq(com.macro.mall.tiny.modules.medicine.model.MedicineStock::getBatchNo, detail.getBatchNo())
                .eq(com.macro.mall.tiny.modules.medicine.model.MedicineStock::getPharmacyId, pharmacyId);
        com.macro.mall.tiny.modules.medicine.model.MedicineStock stock = stockService.getOne(wrapper);
        if (stock != null) {
            // 如果是包装单位，需要转换为基本单位后再恢复
            BigDecimal restoreQty = detail.getQuantity();
            if ("PACK".equals(detail.getUnitType())) {
                MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
                BigDecimal conversionRate = (drug != null && drug.getConversionRate() != null)
                        ? drug.getConversionRate() : BigDecimal.ONE;
                restoreQty = detail.getQuantity().multiply(conversionRate);
            }
            stock.setQuantity(stock.getQuantity().add(restoreQty));
            stockService.updateById(stock);
        }
    }
}