package com.macro.mall.tiny.modules.fms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.fms.dto.InvoiceParam;
import com.macro.mall.tiny.modules.fms.model.Invoice;
import com.macro.mall.tiny.modules.fms.service.InvoiceService;
import com.macro.mall.tiny.modules.fms.vo.StatisticsVo;
import com.macro.mall.tiny.modules.fms.vo.StatusVo;
import com.macro.mall.tiny.modules.ums.dto.InvoiceStatusParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author macro
 * @since 2024-07-09
 */
@RestController
@Api(tags = "InvoiceController")
@Tag(name = "InvoiceController",description = "发票管理")
@RequestMapping("/invoice")
@ResponseBody
public class InvoiceController {
    @Resource
    private  InvoiceService invoiceService;
    @ApiOperation("添加发票")
    @RequestMapping(value = "/create", method = RequestMethod.POST)

    public CommonResult create(@Validated @RequestBody InvoiceParam param) {
        boolean success = invoiceService.create(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
    @ApiOperation("修改发票")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Integer id, @RequestBody Invoice invoice) {
        invoice.setId(id);
        invoice.setUpdateTime(new Date());
        boolean success = invoiceService.updateById(invoice);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除发票")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Integer id) {
        boolean success = invoiceService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改发票状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Integer id, @RequestParam(value = "status") Integer status) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setStatus(status);
        boolean success = invoiceService.updateById(invoice);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据时间范围修改发票状态")
    @RequestMapping(value = "/updateInvoicesStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInvoicesStatus(@RequestBody InvoiceStatusParam invoiceStatusParam) {
        boolean success = invoiceService.updateInvoicesStatus(invoiceStatusParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取所有发票")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<Invoice>> listAll() {
        List<Invoice> invoiceList = invoiceService.list();
        return CommonResult.success(invoiceList);
    }

    @ApiOperation("根据查询条件分页获取发票列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<Invoice>> list(@RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
                                                  @RequestParam(value = "licensePlate", required = false) String licensePlate,
                                                  @RequestParam(value = "invoiceDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDate,
                                                  @RequestParam(value = "status", required = false) Integer status,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<Invoice> invoiceList = invoiceService.list(invoiceNumber, licensePlate, invoiceDate, pageSize, pageNum, status);
        return CommonResult.success(CommonPage.restPage(invoiceList));
    }

    @ApiOperation("根据日期统计数据")
    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public CommonResult<StatisticsVo> sum() {
        StatisticsVo statisticsVo = invoiceService.sum();
        return CommonResult.success(statisticsVo);
    }

    @ApiOperation("获取状态列表")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public CommonResult<List<StatusVo>> getStatusList() {
        List<StatusVo> statusVoList = invoiceService.getStatusList();
        return CommonResult.success(statusVoList);
    }
}

