package com.macro.mall.tiny.modules.com.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.com.dto.ClassifyTransferDTO;
import com.macro.mall.tiny.modules.com.dto.PendingPatientsDTO;
import com.macro.mall.tiny.modules.com.dto.TransferParam;
import com.macro.mall.tiny.modules.com.dto.TransferQuery;
import com.macro.mall.tiny.modules.com.model.ComPatientTransfer;

import java.util.Map;

/**
 * <p>
 * 病人转移记录表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
public interface ComPatientTransferService extends IService<ComPatientTransfer> {

    Map<Integer, ComPatientTransfer> getPatientMap(Integer transferStatus, Integer departmentId);

    Page<ClassifyTransferDTO> getTransfersOrRejectedPatients(Integer currentDepartmentId, String name,
                                                  Integer transferStatus, int page, int size);
    /**
     * @Description:  transferQuery 中的参数传 wristbandName  currentDepartmentId
     * @Author: Pikachu
     * @date: 2025/1/4 11:38 PM
     * @param: [transferQuery 【当前组室ID（也就是目标组室ID）姓名 未接收  是否是最新转移 手环号 】, page, size]
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.macro.mall.tiny.modules.com.dto.PendingPatientsDTO>
     **/
    Page<PendingPatientsDTO> getPendingPatients(TransferQuery transferQuery, int page, int size);

    CommonResult patientReceive(TransferParam param);

    CommonResult patientReject(TransferParam param);

    CommonResult patientTransfer(TransferParam param);
}
