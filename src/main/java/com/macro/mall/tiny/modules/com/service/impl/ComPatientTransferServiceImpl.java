package com.macro.mall.tiny.modules.com.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.enums.AdmissionStatusEnum;
import com.macro.mall.tiny.common.enums.CurrentEnum;
import com.macro.mall.tiny.common.enums.TransferStatusEnum;
import com.macro.mall.tiny.modules.com.dto.ClassifyTransferDTO;
import com.macro.mall.tiny.modules.com.dto.TransferParam;
import com.macro.mall.tiny.modules.com.dto.TransferQuery;
import com.macro.mall.tiny.modules.com.mapper.ComPatientTransferMapper;
import com.macro.mall.tiny.modules.com.model.ComPatientAdmission;
import com.macro.mall.tiny.modules.com.model.ComPatientTransfer;
import com.macro.mall.tiny.modules.com.service.ComPatientAdmissionService;
import com.macro.mall.tiny.modules.com.service.ComPatientTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 病人转移记录表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
@Service
public class ComPatientTransferServiceImpl extends ServiceImpl<ComPatientTransferMapper, ComPatientTransfer> implements ComPatientTransferService {

    @Resource
    ComPatientTransferMapper comPatientTransferMapper;

    @Resource
    private ComPatientAdmissionService comPatientAdmissionService;
    @Override
    public Map<Integer, ComPatientTransfer> getPatientMap(Integer transferStatus, Integer departmentId) {
        Map<Integer, ComPatientTransfer> retMap = new HashMap<>();
        LambdaQueryWrapper<ComPatientTransfer> queryWrapper = new LambdaQueryWrapper<>();
        if (transferStatus == TransferStatusEnum.RECEIVED.getCode()) {
            queryWrapper.eq(ComPatientTransfer::getToDepartmentId, departmentId);
        } else {
            queryWrapper.eq(ComPatientTransfer::getFromDepartmentId, departmentId);
        }
        queryWrapper.eq(ComPatientTransfer::getTransferStatus, transferStatus)
                .orderByDesc(ComPatientTransfer::getReceiveTime);
        // 查询所有符合条件的记录
        List<ComPatientTransfer> transferRecords = list(queryWrapper);
        if (CollUtil.isEmpty(transferRecords)) return retMap;

        // 使用 Map 分组，按 patientId 获取最新记录
        Map<Integer, ComPatientTransfer> latestTransfers = transferRecords.stream()
                .collect(Collectors.toMap(
                        ComPatientTransfer::getPatientId,  // 按 patientId 分组
                        transfer -> transfer,             // 分组后的值为记录本身
                        (existing, replacement) -> existing // 解决冲突时保留最早遇到的（已按时间倒序排序）
                ));
        return latestTransfers;
    }

    @Override
    public Page<ClassifyTransferDTO> getRequestTransfers(Integer currentDepartmentId, String name,
                                                         Integer transferStatus, int page, int size) {
        Page<ClassifyTransferDTO> pageRequest = new Page<>(page, size);
        TransferQuery tq = new TransferQuery();
        tq.setCurrentDepartmentId(currentDepartmentId);
        tq.setName(name);
        tq.setTransferStatus(transferStatus);
        tq.setCurrent(CurrentEnum.LATEST.getCode());
        return comPatientTransferMapper.getRequestTransfers(pageRequest, tq);
    }

    @Override
    public Page<ClassifyTransferDTO> getTransferPatients(Integer currentDepartmentId, String name, int page, int size) {

        return null;
    }

    @Override
    public CommonResult patientReceive(TransferParam param) {

        // 更新病人流转记录表(com_patient_transfer)
        LambdaUpdateWrapper<ComPatientTransfer> updateTransferWrapper = Wrappers.lambdaUpdate();
        updateTransferWrapper.eq(ComPatientTransfer::getCurrent, CurrentEnum.LATEST.getCode())
                .eq(ComPatientTransfer::getTransferStatus, TransferStatusEnum.PENDING.getCode())
                .eq(ComPatientTransfer::getPatientId, param.getPatientId());
        updateTransferWrapper.set(ComPatientTransfer::getTransferStatus, TransferStatusEnum.RECEIVED.getCode());
        updateTransferWrapper.set(ComPatientTransfer::getReceiveTime, DateUtil.date());
        if (!update(updateTransferWrapper)) return CommonResult.failed();

        // 更新病人住院表(com_patient_admission)  TODO 更新病房ID和并床位号
        LambdaUpdateWrapper<ComPatientAdmission> updateAdmissionWrapper = Wrappers.lambdaUpdate();
        updateAdmissionWrapper.eq(ComPatientAdmission::getPatientId, param.getPatientId());
        updateAdmissionWrapper.set(ComPatientAdmission::getStatus, AdmissionStatusEnum.INHOSPITAL.getCode());
        updateAdmissionWrapper.set(ComPatientAdmission::getDepartmentId, param.getDepartmentId());
        if (!comPatientAdmissionService.update(updateAdmissionWrapper)) return CommonResult.failed();
        return CommonResult.success(null);
    }

    @Override
    public CommonResult patientReject(TransferParam param) {
        // 更新病人流转记录表(com_patient_transfer)
        LambdaUpdateWrapper<ComPatientTransfer> updateTransferWrapper = Wrappers.lambdaUpdate();
        updateTransferWrapper.eq(ComPatientTransfer::getCurrent, CurrentEnum.LATEST.getCode())
                .eq(ComPatientTransfer::getTransferStatus, TransferStatusEnum.PENDING.getCode())
                .eq(ComPatientTransfer::getPatientId, param.getPatientId());
        updateTransferWrapper.set(ComPatientTransfer::getTransferStatus, TransferStatusEnum.REJECTED.getCode());
        updateTransferWrapper.set(ComPatientTransfer::getRejectTime, DateUtil.date());
        if (!update(updateTransferWrapper)) return CommonResult.failed();
        return CommonResult.success(null);
    }
}
