package com.macro.mall.tiny.modules.medicine.service;

import com.macro.mall.tiny.modules.medicine.model.MedicineValidityWarningConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MedicineValidityWarningConfigService extends IService<MedicineValidityWarningConfig> {

    List<MedicineValidityWarningConfig> getEnabledConfigs();

    List<MedicineValidityWarningConfig> getAllConfigs();

    boolean saveConfigs(List<MedicineValidityWarningConfig> configs);
}