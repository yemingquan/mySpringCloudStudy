package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseBondDao;
import com.example.springBootDemo.entity.input.BaseBond;
import com.example.springBootDemo.service.BaseBondService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 可转债(BaseBond)表服务实现类
 *
 * @author makejava
 * @since 2023-08-13 23:45:20
 */
@Service("baseBondService")
public class BaseBondServiceImpl extends ServiceImpl<BaseBondDao, BaseBond> implements BaseBondService {
    @Resource
    private BaseBondDao baseBondDao;


    @Override
    public void imporKZZ() throws Exception {
        String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
        File file = new File(basePath + "Table_kzz.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseBond> wrapper = new EntityWrapper<>();
        delete(wrapper);

        List<BaseBond> list = ExcelImportUtil.importExcel(new FileInputStream(tempFile), BaseBond.class, importParams);
//            is.close();
        list.stream().forEach(po -> {
            //如果没有涨幅说明转债有问题
            if (po.getGains() == null) {

            } else {
                StringBuffer instructions = new StringBuffer("");
                //转股起始日
                Date convStartDate = po.getConvStartDate();
                //债券余额(万元)
                Double remainSize = po.getRemainSize() / 10000;
                //转股溢价
                Double cbOverRate = po.getCbOverRate() * 100;

                if (cbOverRate > 30) {
                    instructions.append("溢价率:" + new BigDecimal(cbOverRate).setScale(2, BigDecimal.ROUND_UP) + "%;");
                }
                if (new Date().after(convStartDate) && remainSize < 3) {
                    instructions.append("规模:" + new BigDecimal(remainSize).setScale(2, BigDecimal.ROUND_UP) + "亿;");
                } else if (new Date().before(convStartDate)) {
                    instructions.append("次新债;");
                }
                po.setInstructions(instructions.toString());
            }
        });
        insertBatch(list, list.size());
    }
}
