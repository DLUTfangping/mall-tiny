package com.macro.mall.tiny.modules.mgs.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.enums.CommonStatus;
import com.macro.mall.tiny.modules.mgs.dto.BedCountOfRoomDTO;
import com.macro.mall.tiny.modules.mgs.dto.MgsDepartmentsParam;
import com.macro.mall.tiny.modules.mgs.model.MgsDepartments;
import com.macro.mall.tiny.modules.mgs.service.MgsDepartmentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 组室信息表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Api(tags = "MgsDepartmentsController")
@Tag(name = "MgsDepartmentsController",description = "部门管理")
@RequestMapping("/mgs/mgsDepartments")
@RestController
public class MgsDepartmentsController {
    @Resource
    private MgsDepartmentsService mgsDepartmentsService;

    @ApiOperation("添加部门")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@Validated @RequestBody MgsDepartmentsParam param) {
        boolean success = mgsDepartmentsService.save(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
    @ApiOperation("删除部门")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Integer id) {
        boolean success = mgsDepartmentsService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }
    @ApiOperation("修改部门")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody MgsDepartmentsParam param) {
        boolean success = mgsDepartmentsService.updateById(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
    @ApiOperation("根据查询条件分页获取部门列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MgsDepartments>> list(@RequestParam(value = "status", required = false) Integer status,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MgsDepartments> invoiceList = mgsDepartmentsService.list(status, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(invoiceList));
    }

    @ApiOperation("不分页获取部门列表")
    @RequestMapping(value = "/noPageList", method = RequestMethod.GET)
    public CommonResult<List<MgsDepartments>> list() {
        List<MgsDepartments> list = mgsDepartmentsService.list(new LambdaQueryWrapper<>(new MgsDepartments())
                .eq(MgsDepartments::getStatus, CommonStatus.ACTIVE.getCode()));
        return CommonResult.success(list);
    }
    @ApiOperation("根据组室ID获取组室中病房情况及其可用的病床")
    @RequestMapping(value = "/bedCountOfRoomList", method = RequestMethod.GET)
    public CommonResult<List<BedCountOfRoomDTO>> getBedCountOfRoomList(@RequestParam(value = "departmentId") Integer departmentId) {
        return CommonResult.success(mgsDepartmentsService.getBedCountOfRoomList(departmentId));
    }
}

