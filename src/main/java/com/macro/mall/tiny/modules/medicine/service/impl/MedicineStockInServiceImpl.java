package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineDrugMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicinePharmacyMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockInDetailMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockInMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockMapper;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.model.MedicinePharmacy;
import com.macro.mall.tiny.modules.medicine.model.MedicineStock;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockIn;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockInDetail;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 药材入库单 Service 实现类
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Service
public class MedicineStockInServiceImpl extends ServiceImpl<MedicineStockInMapper, MedicineStockIn> implements MedicineStockInService {

    @Autowired
    private MedicineStockInDetailMapper stockInDetailMapper;

    @Autowired
    private MedicineStockMapper stockMapper;

    @Autowired
    private MedicinePharmacyMapper pharmacyMapper;

    @Autowired
    private MedicineDrugMapper drugMapper;

    @Override
    public Page<MedicineStockIn> list(Long pharmacyId, Integer status, String keyword, Integer pageSize, Integer pageNum) {
        Page<MedicineStockIn> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MedicineStockIn> wrapper = new QueryWrapper<>();
        if (pharmacyId != null) {
            wrapper.eq("pharmacy_id", pharmacyId);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("in_no", keyword).or().like("supplier", keyword).or().like("operator", keyword));
        }
        wrapper.orderByDesc("create_time");
        Page<MedicineStockIn> result = page(page, wrapper);

        // 填充药房名称
        for (MedicineStockIn item : result.getRecords()) {
            fillPharmacyName(item);
        }

        return result;
    }

    @Override
    public MedicineStockIn getDetail(Long id) {
        MedicineStockIn stockIn = this.getById(id);
        if (stockIn == null) {
            return null;
        }
        fillPharmacyName(stockIn);

        // 查询明细列表
        QueryWrapper<MedicineStockInDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("in_id", id);
        List<MedicineStockInDetail> details = stockInDetailMapper.selectList(detailWrapper);
        stockIn.setDetails(details);

        return stockIn;
    }

    @Override
    @Transactional
    public boolean create(MedicineStockIn stockIn) {
        // 生成入库单号
        stockIn.setInNo(generateInNo());
        stockIn.setCreateTime(new Date());
        stockIn.setUpdateTime(new Date());
        stockIn.setStatus(0); // 草稿状态

        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (stockIn.getDetails() != null && !stockIn.getDetails().isEmpty()) {
            for (MedicineStockInDetail detail : stockIn.getDetails()) {
                detail.setCreateTime(new Date());
                if (detail.getAmount() != null) {
                    totalAmount = totalAmount.add(detail.getAmount());
                }
            }
        }
        stockIn.setTotalAmount(totalAmount);

        // 保存主表
        boolean success = this.save(stockIn);
        if (!success) {
            return false;
        }

        // 保存明细表（主表保存后才有ID）
        if (stockIn.getDetails() != null && !stockIn.getDetails().isEmpty()) {
            for (MedicineStockInDetail detail : stockIn.getDetails()) {
                detail.setInId(stockIn.getId());
                // 设置包装单位
                if (detail.getUnit() == null && detail.getDrugId() != null) {
                    MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
                    if (drug != null) {
                        detail.setUnit(drug.getUnit());
                    }
                }
                stockInDetailMapper.insert(detail);
            }
        }

        return true;
    }

    @Override
    @Transactional
    public boolean update(Long id, MedicineStockIn stockIn) {
        MedicineStockIn existStockIn = this.getById(id);
        if (existStockIn == null) {
            return false;
        }

        // 如果已入库，不允许修改
        if (existStockIn.getStatus() != null && existStockIn.getStatus() == 1) {
            return false;
        }

        stockIn.setId(id);
        stockIn.setUpdateTime(new Date());

        // 重新计算总金额
        if (stockIn.getDetails() != null && !stockIn.getDetails().isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (MedicineStockInDetail detail : stockIn.getDetails()) {
                detail.setInId(id);
                detail.setCreateTime(new Date());
                if (detail.getAmount() != null) {
                    totalAmount = totalAmount.add(detail.getAmount());
                }
            }
            stockIn.setTotalAmount(totalAmount);
        }

        // 更新主表
        boolean success = this.updateById(stockIn);
        if (!success) {
            return false;
        }

        // 删除旧明细，插入新明细
        stockInDetailMapper.delete(new QueryWrapper<MedicineStockInDetail>().eq("in_id", id));
        if (stockIn.getDetails() != null && !stockIn.getDetails().isEmpty()) {
            for (MedicineStockInDetail detail : stockIn.getDetails()) {
                detail.setInId(id);
                // 设置包装单位
                if (detail.getUnit() == null && detail.getDrugId() != null) {
                    MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
                    if (drug != null) {
                        detail.setUnit(drug.getUnit());
                    }
                }
                stockInDetailMapper.insert(detail);
            }
        }

        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        MedicineStockIn existStockIn = this.getById(id);
        if (existStockIn == null) {
            return false;
        }

        // 如果已入库，不允许删除
        if (existStockIn.getStatus() != null && existStockIn.getStatus() == 1) {
            return false;
        }

        // 删除明细
        stockInDetailMapper.delete(new QueryWrapper<MedicineStockInDetail>().eq("in_id", id));

        // 删除主表
        return this.removeById(id);
    }

    @Override
    @Transactional
    public boolean confirm(Long id) {
        MedicineStockIn stockIn = this.getById(id);
        if (stockIn == null) {
            return false;
        }

        // 必须是草稿状态才能确认
        if (stockIn.getStatus() == null || stockIn.getStatus() != 0) {
            return false;
        }

        // 查询明细
        QueryWrapper<MedicineStockInDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("in_id", id);
        List<MedicineStockInDetail> details = stockInDetailMapper.selectList(detailWrapper);

        if (details.isEmpty()) {
            return false;
        }

        // 更新库存
        for (MedicineStockInDetail detail : details) {
            // 查询药品信息获取转换率
            MedicineDrug drug = drugMapper.selectById(detail.getDrugId());
            BigDecimal conversionRate = (drug != null && drug.getConversionRate() != null)
                    ? drug.getConversionRate() : BigDecimal.ONE;
            // 入库数量转换为基本单位
            BigDecimal baseQuantity = detail.getQuantity().multiply(conversionRate);

            // 检查是否已存在相同批号的库存
            QueryWrapper<MedicineStock> stockWrapper = new QueryWrapper<>();
            stockWrapper.eq("drug_id", detail.getDrugId());
            stockWrapper.eq("pharmacy_id", stockIn.getPharmacyId());
            stockWrapper.eq("batch_no", detail.getBatchNo());
            MedicineStock existStock = stockMapper.selectOne(stockWrapper);

            if (existStock != null) {
                // 累加库存（基本单位）
                existStock.setQuantity(existStock.getQuantity().add(baseQuantity));
                existStock.setUpdateTime(new Date());
                stockMapper.updateById(existStock);
            } else {
                // 新增库存记录（基本单位）
                MedicineStock newStock = new MedicineStock();
                newStock.setDrugId(detail.getDrugId());
                newStock.setPharmacyId(stockIn.getPharmacyId());
                newStock.setBatchNo(detail.getBatchNo());
                newStock.setQuantity(baseQuantity);
                newStock.setProductionDate(detail.getProductionDate());
                newStock.setExpiryDate(detail.getExpiryDate());
                newStock.setUnitPrice(detail.getUnitPrice());
                newStock.setCreateTime(new Date());
                newStock.setUpdateTime(new Date());
                stockMapper.insert(newStock);
            }
        }

        // 更新主表状态为已入库
        MedicineStockIn update = new MedicineStockIn();
        update.setId(id);
        update.setStatus(1);
        update.setUpdateTime(new Date());
        return this.updateById(update);
    }

    @Override
    public List<MedicineDrug> searchDrugs(String keyword) {
        QueryWrapper<MedicineDrug> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1); // 只查询启用的药品
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("drug_name", keyword).or().like("drug_code", keyword).or().like("common_name", keyword));
        }
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 50"); // 限制返回数量
        return drugMapper.selectList(wrapper);
    }

    /**
     * 生成入库单号
     */
    private String generateInNo() {
        return "IN" + System.currentTimeMillis();
    }

    /**
     * 填充药房名称
     */
    private void fillPharmacyName(MedicineStockIn stockIn) {
        if (stockIn.getPharmacyId() != null) {
            MedicinePharmacy pharmacy = pharmacyMapper.selectById(stockIn.getPharmacyId());
            if (pharmacy != null) {
                stockIn.setPharmacyName(pharmacy.getPharmacyName());
            }
        }
    }
}