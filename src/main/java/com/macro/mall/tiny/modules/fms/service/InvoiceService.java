package com.macro.mall.tiny.modules.fms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.fms.dto.InvoiceParam;
import com.macro.mall.tiny.modules.fms.model.Invoice;
import com.macro.mall.tiny.modules.fms.vo.StatisticsVo;
import com.macro.mall.tiny.modules.fms.vo.StatusVo;
import com.macro.mall.tiny.modules.ums.dto.InvoiceStatusParam;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author macro
 * @since 2024-07-09
 */
public interface InvoiceService extends IService<Invoice> {
    /**
     * 新增发票
     */
    boolean create(InvoiceParam invoiceParam);

//    /**
//     * 自定义实现逻辑删除
//     */
//    boolean logicDeleteById(Integer id);

    /**
     * 分页获取发票列表
     */
    Page<Invoice> list(String invoiceNumber, String licensePlate, LocalDate invoiceDate, Integer pageSize, Integer pageNum, Integer status);


    StatisticsVo sum();


    List<StatusVo> getStatusList();

    boolean updateInvoicesStatus(InvoiceStatusParam invoiceStatusParam);
}
