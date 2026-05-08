package com.macro.mall.tiny.modules.medicine.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrugUsage;
import com.macro.mall.tiny.modules.medicine.service.MedicineDrugUsageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@Api(tags = "MedicineDrugUsageController")
@Tag(name = "MedicineDrugUsageController", description = "药品用法管理")
@RequestMapping("/drugUsage")
public class MedicineDrugUsageController {

    @Autowired
    private MedicineDrugUsageService drugUsageService;

    @ApiOperation("根据药品编码获取用法列表")
    @RequestMapping(value = "/list/{drugCode}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MedicineDrugUsage>> list(@PathVariable String drugCode) {
        List<MedicineDrugUsage> list = drugUsageService.list();
        return CommonResult.success(list);
    }

    @ApiOperation("创建药品用法")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody MedicineDrugUsage drugUsage) {
        drugUsage.setCreateTime(new Date());
        drugUsage.setUpdateTime(new Date());
        boolean success = drugUsageService.save(drugUsage);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新药品用法")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody MedicineDrugUsage drugUsage) {
        drugUsage.setId(id);
        drugUsage.setUpdateTime(new Date());
        boolean success = drugUsageService.updateById(drugUsage);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除药品用法")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = drugUsageService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
