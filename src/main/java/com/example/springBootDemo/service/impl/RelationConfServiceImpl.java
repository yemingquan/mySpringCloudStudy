package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.RelationConfDao;
import com.example.springBootDemo.entity.RelationConf;
import com.example.springBootDemo.service.RelationConfService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * 模式关系表(RelationConf)表服务实现类
 *
 * @author makejava
 * @since 2023-10-07 09:11:30
 */
@Service("relationConfService")
public class RelationConfServiceImpl extends ServiceImpl<RelationConfDao, RelationConf> implements RelationConfService {
    @Resource
    private RelationConfDao relationConfDao;
    @Resource
    private RelationConfService relationConfService;
    
    @Override
    public void queryRelationConf(HttpServletResponse response) throws IOException {
        EntityWrapper ew = new EntityWrapper<>();
        ew.orderBy("date",false);
        List<RelationConf> list = relationConfDao.selectList(ew);

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(list, RelationConf.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, RelationConf.class, list);
        ExcelUtil.exportExel(response, "RelationConf", workbook);
    }

    @Override
    public void imporRelationConf(InputStream is) throws Exception {
        List<RelationConf> list = ExcelUtil.excelToList(is, RelationConf.class);
        is.close();
        relationConfService.insertOrUpdateBatch(list, list.size());
    }
    
}
