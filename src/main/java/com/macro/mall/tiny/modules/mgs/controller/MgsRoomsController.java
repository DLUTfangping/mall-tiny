package com.macro.mall.tiny.modules.mgs.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.dto.MgsRoomsParam;
import com.macro.mall.tiny.modules.mgs.model.MgsRooms;
import com.macro.mall.tiny.modules.mgs.service.MgsRoomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 病房信息表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Api(tags = "MgsRoomsController")
@Tag(name = "MgsRoomsController",description = "病房管理")
@RestController
@RequestMapping("/mgs/mgsRooms")
public class MgsRoomsController {

    @Resource
    private MgsRoomsService mgsRoomsService;

    @ApiOperation("添加病房")
    @RequestMapping(value = "/create", method = RequestMethod.POST)

    public CommonResult create(@Validated @RequestBody MgsRoomsParam param) {
        boolean success = mgsRoomsService.save(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
    @ApiOperation("删除病房")
    @RequestMapping(value = "/delete/{roomNumber}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable String roomNumber) {
        boolean success = mgsRoomsService.removeByRoomNumber(roomNumber);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改病房")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody MgsRoomsParam param) {
        boolean success = mgsRoomsService.updateByRoomNumber(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据查询条件分页获取病房列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MgsRooms>> list(@RequestParam(value = "status", required = false) Integer status,
                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MgsRooms> invoiceList = mgsRoomsService.list(status, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(invoiceList));
    }

}

