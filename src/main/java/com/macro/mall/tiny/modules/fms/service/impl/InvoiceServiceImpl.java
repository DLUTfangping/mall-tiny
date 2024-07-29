package com.macro.mall.tiny.modules.fms.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.fms.dto.InvoiceParam;
import com.macro.mall.tiny.modules.fms.mapper.InvoiceMapper;
import com.macro.mall.tiny.modules.fms.model.Invoice;
import com.macro.mall.tiny.modules.fms.service.InvoiceService;
import com.macro.mall.tiny.modules.fms.vo.StatisticsVo;
import com.macro.mall.tiny.modules.fms.vo.StatusVo;
import com.macro.mall.tiny.modules.ums.dto.InvoiceStatusParam;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-07-09
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

    @Override
    public boolean create(InvoiceParam invoiceParam) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceParam, invoice);
        save(invoice);
        return true;
    }

//    @Override
//    public boolean logicDeleteById(Integer id) {
//        LambdaUpdateWrapper<Invoice> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(Invoice::getId, id).set(Invoice::getStatus, Invoice.STATUS_UN_NORMAL);
//        return update(null, updateWrapper);
//    }

    @Override
    public Page<Invoice> list(String invoiceNumber, String licensePlate,LocalDate invoiceDate, Integer pageSize, Integer pageNum, Integer status) {
        Page<Invoice> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Invoice> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Invoice> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(invoiceNumber)){
            lambda.like(Invoice::getInvoiceNumber,invoiceNumber);
        }
        if(StrUtil.isNotEmpty(licensePlate)){
            lambda.like(Invoice::getLicensePlate,licensePlate.trim());
        }
        if (invoiceDate != null) {
            lambda.eq(Invoice::getInvoiceDate,invoiceDate);
        }
        if (status == null || status.equals(Invoice.STATUS_ALL)) {
            lambda.in(Invoice::getStatus, Arrays.asList(Invoice.STATUS_UN_NORMAL, Invoice.STATUS_NORMAL));
        } else {
            lambda.eq(Invoice::getStatus, status);
        }

        lambda.orderByDesc(Invoice::getUpdateTime).orderByDesc(Invoice::getId);
        return page(page,wrapper);
    }

    @Override
    public StatisticsVo sum() {
        List<Invoice> list = list();
        Triple<String, String, String> amount = getAmount(list);
        StatisticsVo statisticsVo = new StatisticsVo();
        if (!(list != null && !list.isEmpty())) {
            return statisticsVo;
        }

        Map<Date, List<Invoice>> collect = list.stream().collect(Collectors.groupingBy(Invoice::getInvoiceDate));
        List<StatisticsVo.DateAmount> dateAmountList = new ArrayList<>();
        List<StatisticsVo.DateAmount> finalDateAmountList = dateAmountList;
        collect.forEach((k, v) -> {
            StatisticsVo.DateAmount vo = new StatisticsVo.DateAmount();
            // 1.组装当天的日期
            vo.setDate(DateUtil.format(k, "yyyy-MM-dd"));
            // 2.组装当天的金额
            BigDecimal bd = v.stream().map(Invoice::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setAmount(bd.toString());

            List<Invoice> sortedList = v.stream().sorted(Comparator.comparing(Invoice::getStartTime)).collect(Collectors.toList());
            StringBuilder sbCurInvoice = new StringBuilder();
            StringBuilder sbCurTimeRound = new StringBuilder();
            sortedList.forEach(s -> {
                // 3.组装当天的发票集合
                sbCurInvoice.append(s.getInvoiceNumber()).append(",");

                // 4.组装当天的时间集合
                String startTime = DateUtil.format(s.getStartTime(), "HH:mm");
                String endTime = DateUtil.format(s.getEndTime(), "HH:mm");
                sbCurTimeRound.append('[').append(startTime).append('-').append(endTime).append(']').append(',');
            });
            vo.setCurDetailTime(sbCurTimeRound.substring(0, sbCurTimeRound.length() - 1));
            vo.setCurInvoices(sbCurInvoice.substring(0, sbCurInvoice.length() - 1));

            finalDateAmountList.add(vo);

        });
        // 5.组装金额
        statisticsVo.setTotalAmount(amount.getLeft());
        statisticsVo.setValidAmount(amount.getMiddle());
        statisticsVo.setInvalidAmount(amount.getRight());

        // 6.组装以日期为集合的列表数据
        dateAmountList = finalDateAmountList.stream().sorted(Comparator.comparing(StatisticsVo.DateAmount::getDate)).collect(Collectors.toList());
        statisticsVo.setDateAmountList(dateAmountList);
        return statisticsVo;
    }

    @Override
    public List<StatusVo> getStatusList() {
        List<StatusVo> statusVoList = new ArrayList<>();
        StatusVo vo1 = new StatusVo("全部状态",Invoice.STATUS_ALL);
        StatusVo vo2 = new StatusVo("正常状态", Invoice.STATUS_NORMAL);
        StatusVo vo3 = new StatusVo("删除状态", Invoice.STATUS_UN_NORMAL);
        statusVoList.add(vo1);
        statusVoList.add(vo2);
        statusVoList.add(vo3);
        return statusVoList;
    }



    /**
     * @Description: 获取发票的金额：总金额  有效的金额  无效的金额
     * @Author: Pikachu
     * @date: 2024/7/25 11:02 PM
     * @param: []
     * @return: org.apache.commons.lang3.tuple.Triple<java.lang.String,java.lang.String,java.lang.String>
     **/
    private Triple<String, String, String> getAmount(List<Invoice> invoices) {
        if (CollectionUtils.isEmpty(invoices)) {
            return new ImmutableTriple<>("", "", "");
        }
        String total = getInvoiceAmount(invoices);

        List<Invoice> normalInvoices = invoices.stream().filter(invoice -> Objects.equals(invoice.getStatus(), Invoice.STATUS_NORMAL)).collect(Collectors.toList());
        String valid = getInvoiceAmount(normalInvoices);

        List<Invoice> unNormalInvoices = invoices.stream().filter(invoice -> Objects.equals(invoice.getStatus(), Invoice.STATUS_UN_NORMAL)).collect(Collectors.toList());
        String invalid = getInvoiceAmount(unNormalInvoices);

        return new ImmutableTriple<>(total, valid, invalid);
    }


    /**
     * @Description: 根据发票列表获取总金额
     * @Author: Pikachu
     * @date: 2024/7/25 11:25 PM
     * @param: [invoices]
     * @return: java.lang.String
     **/
    private String getInvoiceAmount(List<Invoice> invoices) {
        if (CollectionUtils.isEmpty(invoices)) {
            return "";
        }
        BigDecimal res = invoices.stream().map(Invoice::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return res.toString();
    }

    @Override
    public boolean updateInvoicesStatus(InvoiceStatusParam invoiceStatusParam) {
        LambdaUpdateWrapper<Invoice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .ne(Invoice::getStatus, invoiceStatusParam.getStatus())
                .between(Invoice::getInvoiceDate,  invoiceStatusParam.getStartDate(), invoiceStatusParam.getEndDate())
                .set(Invoice::getStatus, invoiceStatusParam.getStatus());
        return update(null, updateWrapper);
    }
}
