package com.macro.mall.tiny.modules.com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.com.mapper.ComPatientMapper;
import com.macro.mall.tiny.modules.com.model.ComPatient;
import com.macro.mall.tiny.modules.com.service.ComPatientService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 病人信息表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-12-25
 */
@Service
public class ComPatientServiceImpl extends ServiceImpl<ComPatientMapper, ComPatient> implements ComPatientService {

}
