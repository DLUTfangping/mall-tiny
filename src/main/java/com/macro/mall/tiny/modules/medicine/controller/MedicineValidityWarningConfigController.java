package com.macro.mall.tiny.modules.medicine.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineValidityWarningConfig;
import com.macro.mall.tiny.modules.medicine.service.MedicineValidityWarningConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "MedicineValidityWarningConfigController")
@Tag(name = "MedicineValidityWarningConfigController", description = "有效期预警颜色配置管理")
@RequestMapping("/validityWarning/config")
public class MedicineValidityWarningConfigController {

    @Autowired
    private MedicineValidityWarningConfigService configService;

    @ApiOperation("获取所有配置")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MedicineValidityWarningConfig>> getAllConfigs() {
        List<MedicineValidityWarningConfig> configs = configService.getAllConfigs();
        return CommonResult.success(configs);
    }

    @ApiOperation("获取启用的配置")
    @RequestMapping(value = "/enabled", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MedicineValidityWarningConfig>> getEnabledConfigs() {
        List<MedicineValidityWarningConfig> configs = configService.getEnabledConfigs();
        return CommonResult.success(configs);
    }

    @ApiOperation("批量保存配置")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> saveConfigs(@RequestBody List<MedicineValidityWarningConfig> configs) {
        boolean success = configService.saveConfigs(configs);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}