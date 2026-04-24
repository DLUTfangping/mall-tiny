package com.macro.mall.tiny;

import com.macro.mall.tiny.modules.com.dto.ClassifyTransferDTO;
import com.macro.mall.tiny.modules.com.dto.TransferQuery;
import com.macro.mall.tiny.modules.com.mapper.ComPatientTransferMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class MallTinyApplicationTests {

    @Resource
    private ComPatientTransferMapper comPatientTransferMapper;
    @Test
    public void contextLoads() {
    }
    @Test
    public void test() {
        TransferQuery tq = new TransferQuery();
        tq.setCurrentDepartmentId(6);

        tq.setTransferStatus(0);
        tq.setCurrent(1);
        List<ClassifyTransferDTO> testList = comPatientTransferMapper.getTestList(tq);
        testList.stream().forEach(x -> System.out.println(x.toString()));
    }

}
