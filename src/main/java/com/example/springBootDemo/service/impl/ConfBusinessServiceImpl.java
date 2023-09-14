package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfBusinessDao;
import com.example.springBootDemo.entity.input.ConfBusiness;
import com.example.springBootDemo.service.ConfBusinessService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * 行业配置化(ConfBusiness)表服务实现类
 *
 * @author makejava
 * @since 2023-09-13 16:37:28
 */
@Service("confBusinessService")
public class ConfBusinessServiceImpl extends ServiceImpl<ConfBusinessDao, ConfBusiness> implements ConfBusinessService {
    @Resource
    private ConfBusinessDao confBusinessDao;
    @Resource
    private ConfBusinessService confBusinessService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean imporConfBusiness(MultipartFile mf) throws Exception {
        //全量配置
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        confBusinessService.delete(wrapper);

        List<ConfBusiness> list = ExcelUtil.importExcel(mf, ConfBusiness.class);
        return confBusinessService.insertBatch(list, list.size());
    }

    @Override
    public void exportConfBusiness(HttpServletResponse response) throws Exception {
        String fileName = "ConfBusinessExcel.xls";
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        List<ConfBusiness> list = confBusinessDao.selectList(wrapper);
        //生成excel文档
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("行业配置化", "sheet"),
                ConfBusiness.class, list);

        fileName = new String(fileName.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream outputStream = response.getOutputStream();
        response.flushBuffer();
        workbook.write(outputStream);
        // 写完数据关闭流
        outputStream.close();
    }

    public Workbook getWorkBook() {
        //创建一个工作簿
        Workbook workbook = new XSSFWorkbook();
        //创建表
        Sheet sheet = workbook.createSheet();
        //写入数据
        for (int rowNumber = 0; rowNumber < 65537; rowNumber++) {
            Row row = sheet.createRow(rowNumber);
            for (int cellNumber = 0; cellNumber < 10; cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                cell.setCellValue(cellNumber);
            }
        }
        return workbook;
    }
}
