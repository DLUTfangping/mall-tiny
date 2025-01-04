package com.macro.mall.tiny.modules.com.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.com.dto.ClassifyTransferDTO;
import com.macro.mall.tiny.modules.com.dto.TransferParam;
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

    Page<ClassifyTransferDTO> getRequestTransfers(Integer currentDepartmentId, String name,
                                                  Integer transferStatus, int page, int size);
    /**
     * @Description: 用于获取当前组室申请转组的病人列表
     * @Author: Pikachu
     * @Author: Pikachu
     * @date: 2025/1/4 8:46 PM
     * @param: [currentDepartmentId, name, page, size]
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.macro.mall.tiny.modules.com.dto.TransferDTO>
     **/
    Page<ClassifyTransferDTO> getTransferPatients(Integer currentDepartmentId, String name, int page, int size);

    CommonResult patientReceive(TransferParam param);

    CommonResult patientReject(TransferParam param);

}
