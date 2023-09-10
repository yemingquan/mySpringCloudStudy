package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfCxStockDao;
import com.example.springBootDemo.entity.ConfCxStock;
import com.example.springBootDemo.service.ConfCxStockService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;


/**
 * 次新股票(ConfCxStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-19 14:39:12
 */
@Service("confCxStockService")
public class ConfCxStockServiceImpl extends ServiceImpl<ConfCxStockDao, ConfCxStock> implements ConfCxStockService {
    @Resource
    private ConfCxStockDao confCxStockDao;


    @Override
    public void imporCX() throws Exception {
        String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
        File file = new File(basePath + "Table_cx.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<ConfCxStock> wrapper = new EntityWrapper<>();
        delete(wrapper);

        List<ConfCxStock> list = ExcelImportUtil.importExcel(new FileInputStream(tempFile), ConfCxStock.class, importParams);
//            is.close();
        insertBatch(list, list.size());
    }
}
