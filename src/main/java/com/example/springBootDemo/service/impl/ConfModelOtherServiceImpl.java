package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfModelOtherDao;
import com.example.springBootDemo.entity.ConfModelOther;
import com.example.springBootDemo.service.ConfModelOtherService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * 模式——其他(ConfModelOther)表服务实现类
 *
 * @author makejava
 * @since 2023-10-06 22:15:46
 */
@Service("confModelOtherService")
public class ConfModelOtherServiceImpl extends ServiceImpl<ConfModelOtherDao, ConfModelOther> implements ConfModelOtherService {
    @Resource
    private ConfModelOtherDao confModelOtherDao;
    @Resource
    private ConfModelOtherService confModelOtherService;

    @Override
    public void queryModelOther(HttpServletResponse response) throws IOException {
        EntityWrapper ew = new EntityWrapper<>();
        ew.orderBy(false,"topic")/*.orderBy("conf_Model_Name")*/;
        List<ConfModelOther> list = confModelOtherDao.selectList(ew);

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(list, ConfModelOther.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ConfModelOther.class, list);
        ExcelUtil.exportExel(response, "ConfModelOther", workbook);
    }

    @Override
    public void imporConfModelOther(InputStream is) throws Exception {
        List<ConfModelOther> list = ExcelUtil.excelToList(is, ConfModelOther.class);
        is.close();
        confModelOtherService.insertOrUpdateBatch(list, list.size());
    }
}
