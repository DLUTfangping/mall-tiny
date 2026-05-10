package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineDrugMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineDrugWarningMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicinePharmacyMapper;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineStockMapper;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrugWarning;
import com.macro.mall.tiny.modules.medicine.model.MedicinePharmacy;
import com.macro.mall.tiny.modules.medicine.model.MedicineStock;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 药材库存 Service 实现
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Service
public class MedicineStockServiceImpl extends ServiceImpl<MedicineStockMapper, MedicineStock> implements MedicineStockService {

    @Autowired
    private MedicineDrugMapper drugMapper;

    @Autowired
    private MedicineDrugWarningMapper warningMapper;

    @Autowired
    private MedicinePharmacyMapper pharmacyMapper;

    @Override
    public Page<MedicineStock> list(Long pharmacyId, String drugName, String batchNo, String drugCategory, String drugType, Integer pageSize, Integer pageNum) {
        Page<MedicineStock> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MedicineStock> wrapper = new QueryWrapper<>();

        if (pharmacyId != null) {
            wrapper.eq("pharmacy_id", pharmacyId);
        }
        if (batchNo != null && !batchNo.isEmpty()) {
            wrapper.eq("batch_no", batchNo);
        }

        wrapper.orderByDesc("update_time");
        Page<MedicineStock> result = page(page, wrapper);

        // 填充药品和药房信息
        for (MedicineStock item : result.getRecords()) {
            fillDrugInfo(item);
            fillPharmacyName(item);
        }

        // 如果有药品名称、药品分类、药材类型条件，进行过滤
        if ((drugName != null && !drugName.isEmpty()) ||
            (drugCategory != null && !drugCategory.isEmpty()) ||
            (drugType != null && !drugType.isEmpty())) {

            result.getRecords().removeIf(stock -> {
                if (stock.getDrugName() == null) return true;
                if (drugName != null && !drugName.isEmpty() &&
                    !stock.getDrugName().contains(drugName)) return true;
                if (drugCategory != null && !drugCategory.isEmpty() &&
                    !drugCategory.equals(stock.getDrugCategory())) return true;
                if (drugType != null && !drugType.isEmpty() &&
                    !drugType.equals(stock.getDrugType())) return true;
                return false;
            });
        }

        return result;
    }

    @Override
    public MedicineStock getDetail(Long id) {
        MedicineStock stock = this.getById(id);
        if (stock == null) {
            return null;
        }
        fillDrugInfo(stock);
        fillPharmacyName(stock);
        return stock;
    }

    @Override
    public List<MedicineStock> listByDrugId(Long drugId) {
        QueryWrapper<MedicineStock> wrapper = new QueryWrapper<>();
        wrapper.eq("drug_id", drugId);
        wrapper.orderByDesc("update_time");
        List<MedicineStock> list = this.list(wrapper);

        for (MedicineStock item : list) {
            fillDrugInfo(item);
            fillPharmacyName(item);
        }

        return list;
    }

    /**
     * 填充药品信息
     */
    private void fillDrugInfo(MedicineStock stock) {
        if (stock.getDrugId() != null) {
            try {
                MedicineDrug drug = drugMapper.selectById(stock.getDrugId());
                if (drug != null) {
                    stock.setDrugCode(drug.getDrugCode());
                    stock.setDrugName(drug.getDrugName());
                    stock.setCommonName(drug.getCommonName());
                    stock.setDrugType(drug.getDrugType());
                    stock.setPrescriptionType(drug.getPrescriptionType());
                    stock.setDrugCategory(drug.getDrugCategory());
                    stock.setDosageForm(drug.getDosageForm());
                    stock.setSpec(drug.getSpec());
                    stock.setIsEssential(drug.getIsEssential());
                    stock.setSkinTestRequired(drug.getSkinTestRequired());
                    stock.setManufacturer(drug.getManufacturer());

                    // 查询预警配置
                    QueryWrapper<MedicineDrugWarning> warningWrapper = new QueryWrapper<>();
                    warningWrapper.eq("drug_code", drug.getDrugCode());
                    MedicineDrugWarning warning = warningMapper.selectOne(warningWrapper);
                    if (warning != null) {
                        stock.setMinWarningStock(warning.getMinWarningStock());
                    }
                }
            } catch (Exception e) {
                // 忽略填充错误，保持显示
            }
        }
    }

    /**
     * 填充药房名称
     */
    private void fillPharmacyName(MedicineStock stock) {
        if (stock.getPharmacyId() != null) {
            MedicinePharmacy pharmacy = pharmacyMapper.selectById(stock.getPharmacyId());
            if (pharmacy != null) {
                stock.setPharmacyName(pharmacy.getPharmacyName());
            }
        }
    }
}