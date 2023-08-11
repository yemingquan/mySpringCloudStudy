package com.example.springBootDemo.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.nio.charset.Charset;
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
        File xlsxFile = new File(csvFile.getParent() + File.separator + fileName.replace(".xls", ".xlsx"));
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


    public static File convertCsvToExcel(File csvFile, String fileName) throws IOException {
        //csv文件在本都存储时，直接读取就行
        //File csvFile= new File("csv文件地址");
        File xlsxFile = File.createTempFile(fileName, ".xlsx");
        // 使用EasyExcel读取csv文件
        ExcelReaderBuilder readerBuilder = EasyExcel.read(csvFile).charset(Charset.forName("GBK"));
        //指定从第几行开始读取，默认情况下会把第一行当做表头忽略读取，会少一行的数据，参数0从第一行开始读取数据
        readerBuilder.headRowNumber(0);
        List<Object> dataList = readerBuilder.sheet().doReadSync();

        // 使用EasyExcel写入xlsx文件
        ExcelWriterBuilder writerBuilder = EasyExcel.write(xlsxFile);
        writerBuilder.sheet().doWrite(dataList);
        return xlsxFile;
    }

    /**
     * xls 文件转换为xlsx文件
     */

    public static String xls2xlsx(File sourceFile) throws IOException {

        //创建hssworkbook 操作xls 文件
        POIFSFileSystem fs = new POIFSFileSystem(new File(sourceFile.getPath()));
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fs);

        //创建xssfworkbook 操作xlsx 文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        int sheetNum = hssfWorkbook.getNumberOfSheets();

        String xlsxPath = createNewXlsxFilePath(sourceFile);

        for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {

            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetIndex);

            if (workbook.getSheet(hssfSheet.getSheetName()) == null) {
                XSSFSheet xssfSheet = workbook.createSheet(hssfSheet.getSheetName());
                copySheets(hssfSheet, xssfSheet);
            } else {
                copySheets(hssfSheet, workbook.createSheet(hssfSheet.getSheetName()));
            }


            FileOutputStream fileOut = new FileOutputStream(xlsxPath);

            //将复制的xls数据写入到新的xlsx文件中
            workbook.write(fileOut);

            workbook.close();
            hssfWorkbook.close();

            //删除原有的xls文件  直接删除有点暴力 可以考虑在另外的目录下保存一下
            sourceFile.delete();
        }

        return xlsxPath;
    }

    //为xlsx创建路径
    public static String createNewXlsxFilePath(File sourceFile) {

        String oldPath = sourceFile.getPath();
        String newPath = oldPath.substring(0, oldPath.indexOf(".")) + ".xlsx";

        return newPath;


    }


    /**
     * 转换为xlsx --创建sheet
     *
     * @param source
     * @param destination
     */

    public static void copySheets(HSSFSheet source, XSSFSheet destination) {

        int maxColumnNum = 0;

        for (int i = source.getFirstRowNum(); i <= source.getLastRowNum(); i++) {
            HSSFRow srcRow = source.getRow(i);
            XSSFRow destRow = destination.createRow(i);
            if (srcRow != null) {
                // 拷贝行
                copyRow(srcRow, destRow);
                if (srcRow.getLastCellNum() > maxColumnNum) {
                    maxColumnNum = srcRow.getLastCellNum();
                }
            }
        }
        for (int i = 0; i <= maxColumnNum; i++) {
            destination.setColumnWidth(i, source.getColumnWidth(i));
        }

    }


    /**
     * 转换xlsx --  复制行
     *
     * @param srcRow
     * @param destRow
     */
    public static void copyRow(HSSFRow srcRow, XSSFRow destRow) {

        // 拷贝行高
        destRow.setHeight(srcRow.getHeight());
        for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
            HSSFCell oldCell = srcRow.getCell(j);
            XSSFCell newCell = destRow.getCell(j);
            if (oldCell != null) {
                if (newCell == null) {
                    newCell = destRow.createCell(j);
                }
                // 拷贝单元格
                copyCell(oldCell, newCell);

            }
        }

    }


    /**
     * 转换xlsx -- 复制单元格
     *
     * @param oldCell
     * @param newCell
     */
    public static void copyCell(HSSFCell oldCell, XSSFCell newCell) {

        switch (oldCell.getCellType()) {
            case STRING:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case BLANK:
                newCell.setCellType(CellType.BLANK);
                break;
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            default:
                break;
        }

    }
}
