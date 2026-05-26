package com.macro.mall.tiny.modules.medicine.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.service.MedicineDrugService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 药品字典表 Controller
 * </p>
 *
 * @author macro
 * @since 2026-04-27
 */
@Controller
@Api(tags = "MedicineDrugController")
@Tag(name = "MedicineDrugController", description = "药品字典管理")
@RequestMapping("/drug")
public class MedicineDrugController {

    @Autowired
    private MedicineDrugService drugService;

    @ApiOperation("获取药品字典列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MedicineDrug>> list(
            @RequestParam(required = false) String drugType,
            @RequestParam(required = false) String prescriptionType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MedicineDrug> page = drugService.list(drugType, prescriptionType, status, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取药品字典详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MedicineDrug> getItem(@PathVariable Long id) {
        MedicineDrug drug = drugService.getById(id);
        return CommonResult.success(drug);
    }

    @ApiOperation("创建药品字典")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody MedicineDrug drug) {
        drug.setCreateTime(new Date());
        drug.setUpdateTime(new Date());
        boolean success = drugService.save(drug);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新药品字典")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody MedicineDrug drug) {
        drug.setId(id);
        drug.setUpdateTime(new Date());
        boolean success = drugService.updateById(drug);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除药品字典")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = drugService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改药品状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        MedicineDrug drug = new MedicineDrug();
        drug.setId(id);
        drug.setStatus(status);
        drug.setUpdateTime(new Date());
        boolean success = drugService.updateById(drug);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("搜索药品字典")
    @RequestMapping(value = "/searchDrugs", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MedicineDrug>> search(@RequestParam(required = false) String keyword) {
        List<MedicineDrug> drugs = drugService.search(keyword);
        return CommonResult.success(drugs);
    }
}
