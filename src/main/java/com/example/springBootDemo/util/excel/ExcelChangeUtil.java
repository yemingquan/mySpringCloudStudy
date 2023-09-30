package com.example.springBootDemo.util.excel;

import com.example.springBootDemo.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-8 16:19
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class ExcelChangeUtil {

    /**
     * 将csv文件转换为 excel文件（分隔符为/t的文件，转换为.xlsx文件）
     * 转换成功会删除源文件，保留新的.xlsx文件
     *
     * @param csvFile
     * @param fileName
     * @return
     * @throws Exception
     */
    public static File csvToXlsxConverter(File csvFile, String fileName) throws Exception {
        if (null == csvFile) {
            throw new Exception("文件生成失败");
        }
        File dir = new File(csvFile.getParent() + File.separator + "gen");
        FileUtil.mkdirs(dir.getPath());
        File xlsxFile = new File(dir + File.separator + fileName.replace(".xls", ".xlsx"));
        xlsxFile.createNewFile();
        //后面的编码格式要和生成csv文件的时候保持一致，否则会出现乱码问题
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "GBK"))) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            //也可以传入sheet的名字，默认是sheet0
            XSSFSheet sheet = workbook.createSheet();
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            XSSFDataFormat format = workbook.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("h:mm:ss"));

            XSSFRow row;
            XSSFCell cell;
            List<String[]> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 1) {
                    lines.add(line.split("\\t"));
                }
            }
            int rowIndex = 0;
            int cellIndex;
            for (String[] rowData : lines) {
                row = sheet.createRow(rowIndex++);
                cellIndex = 0;
                for (String cellData : rowData) {
                    cell = row.createCell(cellIndex++);
                    setCell(cell, cellData, cellStyle);
                }
            }
            try (FileOutputStream outputStream = new FileOutputStream(xlsxFile)) {
                workbook.write(outputStream);
                outputStream.close();
            }
            //记得删除临时文件，我自己的需求在后面做了处理，不需要在此删除临时文件
//            csvFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xlsxFile;
    }

    private static void setCell(XSSFCell cell, String cellData, XSSFCellStyle cellStyle) {
        try {
            if (StringUtils.isEmpty(cellData) || "--".equals(cellData)) {
                cell.setCellValue("");
            } else if (cellData.contains("%")) {
                String tempDate = cellData.replace("%", "");
                if (tempDate.matches("\\+?-?[0-9.]*")) {
                    cell.setCellValue(Double.valueOf(tempDate) / 100);
                } else {
                    cell.setCellValue(cellData);
                }
             //20年开头，并且8位数字，转数字
            } else if (cellData.startsWith("20") && cellData.matches("[0-9]{8}")) {
                cell.setCellStyle(cellStyle);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                cell.setCellValue(sdf.parse(cellData));
            } else if (cellData.matches("\\+-?[0-9.]*")) {
                cell.setCellValue(Double.valueOf(cellData));
            } else if (cellData.contains(":") && cellData.matches("[\\:0-9]*")) {
                cell.setCellStyle(cellStyle);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                cell.setCellValue(sdf.parse(cellData));
//                log.info("cellType:{}",cell.getCellType());
            } else {
                cell.setCellValue(cellData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
