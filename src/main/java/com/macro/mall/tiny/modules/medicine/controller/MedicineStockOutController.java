package com.macro.mall.tiny.modules.medicine.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineStockOut;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 药材出库单 Controller
 * </p>
 *
 * @author macro
 * @since 2026-05-12
 */
@Controller
@Api(tags = "MedicineStockOutController")
@Tag(name = "MedicineStockOutController", description = "药材出库管理")
@RequestMapping("/stockOut")
public class MedicineStockOutController {

    @Autowired
    private MedicineStockOutService stockOutService;

    @ApiOperation("获取出库单列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MedicineStockOut>> list(
            @RequestParam(required = false) Long pharmacyId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MedicineStockOut> page = stockOutService.list(pharmacyId, status, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取出库单详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MedicineStockOut> getDetail(@PathVariable Long id) {
        MedicineStockOut stockOut = stockOutService.getDetail(id);
        return CommonResult.success(stockOut);
    }

    @ApiOperation("创建出库单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody MedicineStockOut stockOut) {
        try {
            boolean success = stockOutService.create(stockOut);
            if (success) {
                return CommonResult.success(null);
            }
            return CommonResult.failed("创建失败");
        } catch (RuntimeException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation("更新出库单")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody MedicineStockOut stockOut) {
        try {
            boolean success = stockOutService.update(id, stockOut);
            if (success) {
                return CommonResult.success(null);
            }
            return CommonResult.failed("更新失败");
        } catch (RuntimeException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation("删除出库单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = stockOutService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("根据药房获取库存药品列表")
    @RequestMapping(value = "/getStockByPharmacy", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Map<String, Object>>> getStockByPharmacy(@RequestParam Long pharmacyId) {
        List<Map<String, Object>> list = stockOutService.getStockByPharmacy(pharmacyId);
        return CommonResult.success(list);
    }
}