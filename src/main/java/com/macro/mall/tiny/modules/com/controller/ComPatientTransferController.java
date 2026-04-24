package com.macro.mall.tiny.modules.com.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.com.dto.TransferParam;
import com.macro.mall.tiny.modules.com.service.ComPatientTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 病人转移记录表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
@Api(tags = "ComPatientTransferController")
@Tag(name = "ComPatientTransferController",description = "病人流转记录")
@RestController
@RequestMapping("/com/comPatientTransfer")
public class ComPatientTransferController {

    @Resource
    private ComPatientTransferService comPatientTransferService;
    @ApiOperation("病人接收")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public CommonResult patientReceive(@Validated @RequestBody TransferParam param) {
        return comPatientTransferService.patientReceive(param);
    }

    @ApiOperation("病人驳回")
    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public CommonResult patientReject(@Validated @RequestBody TransferParam param) {
        return comPatientTransferService.patientReject(param);
    }

    @ApiOperation("病人申请转组")
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public CommonResult patientTransfer(@Validated @RequestBody TransferParam param) {
        return comPatientTransferService.patientTransfer(param);
    }
}

