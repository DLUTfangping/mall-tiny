package com.macro.mall.tiny.modules.com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.com.dto.ClassifyTransferDTO;
import com.macro.mall.tiny.modules.com.dto.TransferQuery;
import com.macro.mall.tiny.modules.com.model.ComPatientTransfer;

/**
 * <p>
 * 病人转移记录表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
public interface ComPatientTransferMapper extends BaseMapper<ComPatientTransfer> {
    Page<ClassifyTransferDTO> getRequestTransfers(Page<ClassifyTransferDTO> page, TransferQuery transferQuery);


}
