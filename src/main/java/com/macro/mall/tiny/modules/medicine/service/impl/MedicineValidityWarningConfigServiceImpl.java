package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.medicine.mapper.MedicineValidityWarningConfigMapper;
import com.macro.mall.tiny.modules.medicine.model.MedicineValidityWarningConfig;
import com.macro.mall.tiny.modules.medicine.service.MedicineValidityWarningConfigService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MedicineValidityWarningConfigServiceImpl extends ServiceImpl<MedicineValidityWarningConfigMapper, MedicineValidityWarningConfig> implements MedicineValidityWarningConfigService {

    @Override
    public List<MedicineValidityWarningConfig> getEnabledConfigs() {
        return lambdaQuery()
                .eq(MedicineValidityWarningConfig::getEnabled, 1)
                .orderByAsc(MedicineValidityWarningConfig::getSort)
                .list();
    }

    @Override
    public List<MedicineValidityWarningConfig> getAllConfigs() {
        return lambdaQuery()
                .orderByAsc(MedicineValidityWarningConfig::getSort)
                .list();
    }

    @Override
    public boolean saveConfigs(List<MedicineValidityWarningConfig> configs) {
        if (configs == null || configs.isEmpty()) {
            return false;
        }
        Date now = new Date();
        for (MedicineValidityWarningConfig config : configs) {
            config.setUpdateTime(now);
            if (config.getId() == null) {
                config.setCreateTime(now);
            }
        }
        return saveOrUpdateBatch(configs);
    }
}