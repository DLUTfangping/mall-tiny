package com.macro.mall.tiny.modules.medicine.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineStock;
import com.macro.mall.tiny.modules.medicine.service.MedicineStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 药材库存 Controller
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Controller
@Api(tags = "MedicineStockController")
@Tag(name = "MedicineStockController", description = "药材库存管理")
@RequestMapping("/stock")
public class MedicineStockController {

    @Autowired
    private MedicineStockService stockService;

    @ApiOperation("获取库存列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MedicineStock>> list(
            @RequestParam(required = false) Long pharmacyId,
            @RequestParam(required = false) String drugName,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String drugCategory,
            @RequestParam(required = false) String drugType,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MedicineStock> page = stockService.list(pharmacyId, drugName, batchNo, drugCategory, drugType, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取库存详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MedicineStock> getDetail(@PathVariable Long id) {
        MedicineStock stock = stockService.getDetail(id);
        return CommonResult.success(stock);
    }

    @ApiOperation("获取某个药品的库存列表")
    @RequestMapping(value = "/byDrug/{drugId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MedicineStock>> listByDrugId(@PathVariable Long drugId) {
        List<MedicineStock> list = stockService.listByDrugId(drugId);
        return CommonResult.success(list);
    }
}