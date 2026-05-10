package com.macro.mall.tiny.modules.medicine.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrug;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockIn;
import com.macro.mall.tiny.modules.medicine.service.MedicineDrugService;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockInService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 药材入库单 Controller
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Controller
@Api(tags = "MedicineStockInController")
@Tag(name = "MedicineStockInController", description = "药材入库管理")
@RequestMapping("/stockIn")
public class MedicineStockInController {

    @Autowired
    private MedicineStockInService stockInService;

    @Autowired
    private MedicineDrugService drugService;

    @ApiOperation("获取入库单列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MedicineStockIn>> list(
            @RequestParam(required = false) Long pharmacyId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MedicineStockIn> page = stockInService.list(pharmacyId, status, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取入库单详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MedicineStockIn> getDetail(@PathVariable Long id) {
        MedicineStockIn stockIn = stockInService.getDetail(id);
        return CommonResult.success(stockIn);
    }

    @ApiOperation("创建入库单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody MedicineStockIn stockIn) {
        boolean success = stockInService.create(stockIn);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @ApiOperation("更新入库单")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody MedicineStockIn stockIn) {
        boolean success = stockInService.update(id, stockIn);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @ApiOperation("删除入库单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = stockInService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("确认入库")
    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> confirm(@PathVariable Long id) {
        boolean success = stockInService.confirm(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("确认入库失败");
    }

    @ApiOperation("搜索药品（模糊查询）")
    @RequestMapping(value = "/searchDrugs", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MedicineDrug>> searchDrugs(@RequestParam(required = false) String keyword) {
        List<MedicineDrug> drugs = stockInService.searchDrugs(keyword);
        return CommonResult.success(drugs);
    }
}
