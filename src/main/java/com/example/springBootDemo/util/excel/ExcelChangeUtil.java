package com.example.springBootDemo.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-8 16:19
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class ExcelChangeUtil {

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

        return  xlsxPath;
    }

    //为xlsx创建路径
    public static String createNewXlsxFilePath(File sourceFile){

        String oldPath = sourceFile.getPath();
        String newPath = oldPath.substring(0,oldPath.indexOf("."))+".xlsx";

        return newPath;


    }


    /**
     * 转换为xlsx --创建sheet
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
     * @param srcRow
     * @param destRow
     */
    public static void copyRow( HSSFRow srcRow, XSSFRow destRow) {

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
