package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.system.SystemConfConstant;
import com.example.springBootDemo.dao.mapper.ConfCxStockDao;
import com.example.springBootDemo.entity.ConfCxStock;
import com.example.springBootDemo.service.ConfCxStockService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
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
        String basePath = SystemConfConstant.THS_BASE_PATH;
        File file = new File(basePath + "Table_cx.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

        //导入前先删除当天的数据
        EntityWrapper<ConfCxStock> wrapper = new EntityWrapper<>();
        delete(wrapper);
        List<ConfCxStock> list = ExcelUtil.excelToList(tempFile,ConfCxStock.class);

        //            is.close();
        insertBatch(list, list.size());
    }


}
