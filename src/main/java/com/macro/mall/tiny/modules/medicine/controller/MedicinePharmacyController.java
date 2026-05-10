package com.macro.mall.tiny.modules.medicine.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicinePharmacy;
import com.macro.mall.tiny.modules.medicine.service.MedicinePharmacyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 药房表 Controller
 * </p>
 *
 * @author macro
 * @since 2026-05-09
 */
@Controller
@Api(tags = "MedicinePharmacyController")
@Tag(name = "MedicinePharmacyController", description = "药房管理")
@RequestMapping("/pharmacy")
public class MedicinePharmacyController {

    @Autowired
    private MedicinePharmacyService pharmacyService;

    @ApiOperation("获取药房列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MedicinePharmacy>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MedicinePharmacy> page = pharmacyService.list(status, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取药房详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MedicinePharmacy> getItem(@PathVariable Long id) {
        MedicinePharmacy pharmacy = pharmacyService.getById(id);
        return CommonResult.success(pharmacy);
    }

    @ApiOperation("创建药房")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody MedicinePharmacy pharmacy) {
        pharmacy.setCreateTime(new Date());
        pharmacy.setUpdateTime(new Date());
        // 如果是默认药房，先取消其他默认
        if (pharmacy.getIsDefault() != null && pharmacy.getIsDefault() == 1) {
            pharmacyService.setDefault(null);
        }
        boolean success = pharmacyService.save(pharmacy);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新药房")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody MedicinePharmacy pharmacy) {
        pharmacy.setId(id);
        pharmacy.setUpdateTime(new Date());
        // 如果是默认药房，先取消其他默认
        if (pharmacy.getIsDefault() != null && pharmacy.getIsDefault() == 1) {
            pharmacyService.setDefault(id);
        }
        boolean success = pharmacyService.updateById(pharmacy);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除药房")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = pharmacyService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改药房状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        MedicinePharmacy pharmacy = new MedicinePharmacy();
        pharmacy.setId(id);
        pharmacy.setStatus(status);
        pharmacy.setUpdateTime(new Date());
        boolean success = pharmacyService.updateById(pharmacy);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("设置默认药房")
    @RequestMapping(value = "/setDefault/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> setDefault(@PathVariable Long id) {
        boolean success = pharmacyService.setDefault(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
