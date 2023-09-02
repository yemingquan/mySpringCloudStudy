package com.example.springBootDemo.util.excel;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.SubjectReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2020/12/23 17:37
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class ExcelUtil<T> implements Serializable {

    private Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static List<String> BSD_STOCK_LIST;

    {
        BSD_STOCK_LIST = Lists.newArrayList(
                "百利电气", "金科股份", "永鼎股份", "法尔胜",
                "云南城投", "金科股份", "荣盛发展",
                "太平洋", "首创证券", "中信证券",
                "中央商场", "国芳集团", "人人乐",
                "鸿博股份", "金桥信息", "浪潮信息", "工业富联", "拓维信息", "中科曙光"
        );
    }

    /**
     * excel 导出
     *
     * @param list     数据列表
     * @param fileName 导出时的excel名称
     * @param response
     */
    public static void exportExcel2(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list     数据列表
     * @param fileName 导出时的excel名称
     * @param response
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        //把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param response
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头，表格类型）
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        //把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头，表格类型）
     * @param response
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list      数据列表
     * @param title     表格内数据标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  导出时的excel名称
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }


    /**
     * excel 导出
     *
     * @param list           数据列表
     * @param title          表格内数据标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       导出时的excel名称
     * @param isCreateHeader 是否创建表头
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }


    /**
     * excel下载
     *
     * @param fileName 下载时的文件名称
     * @param response
     * @param workbook excel数据
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }


    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws IOException {
        return importExcel(file, 1, 1, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param filePath   excel文件路径
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(true);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }


    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param titleRows   表格内数据标题行
     * @param headerRows  表头行
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setSaveUrl("/excel/");
        params.setNeedSave(true);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

//    /**
//     * 设置单元格上提示
//     *
//     * @param sheet         要设置的sheet.
//     * @param promptTitle   标题
//     * @param promptContent 内容
//     * @param firstRow      开始行
//     * @param endRow        结束行
//     * @param firstCol      开始列
//     * @param endCol        结束列
//     * @return 设置好的sheet.
//     */
//    public static XSSFSheet setPrompt(XSSFSheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
//                                      int firstCol, int endCol) {
//        // 构造constraint对象
//        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
//        // 四个参数分别是：起始行、终止行、起始列、终止列
//        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
//        // 数据有效性对象
//        XSSFDataValidation data_validation_view = new XSSFDataValidation(regions, constraint);
//        data_validation_view.createPromptBox(promptTitle, promptContent);
//        sheet.addValidationData(data_validation_view);
//        return sheet;
//    }
//
//    /**
//     * 设置某些列的值只能输入预制的数据,显示下拉框.
//     *
//     * @param sheet    要设置的sheet.
//     * @param textlist 下拉框显示的内容
//     * @param firstRow 开始行
//     * @param endRow   结束行
//     * @param firstCol 开始列
//     * @param endCol   结束列
//     * @return 设置好的sheet.
//     */
//    public static XSSFSheet setValidation(XSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
//        // 加载下拉列表内容
//        CTDataValidation constraint = CTDataValidation.createExplicitListConstraint(textlist);
//        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
//        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
//        // 数据有效性对象
//        XSSFDataValidation data_validation_list = new XSSFDataValidation(regions, constraint);
//        sheet.addValidationData(data_validation_list);
//        return sheet;
//    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public void exportCustomExcel_bak(List<?> list, String fileName, String sheetName, HttpServletResponse response) {
        XSSFWorkbook workbook = null;
        try {
            // 得到所有定义字段
            List<Field> fields = getClassFields();

            // 产生工作薄对象
            workbook = new XSSFWorkbook();
            // excel2003中每个sheet中最多有65536行
            int sheetSize = 2 ^ 20;
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++) {
                // 产生工作表对象
                XSSFSheet sheet = workbook.createSheet();
                if (sheetNo == 0) {
                    workbook.setSheetName(index, sheetName);
                } else {
                    // 设置工作表的名称.
                    workbook.setSheetName(index, sheetName + index);
                }
                XSSFRow row;
                XSSFCell cell; // 产生单元格

                // 产生一行
                row = sheet.createRow(0);
                // 写入各个字段的列头名称
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    Excel attr = field.getAnnotation(Excel.class);
                    // 创建列
                    cell = row.createCell(i);
                    row.setHeight((short) (attr.height() * 20));
                    // 设置列中写入内容为String类型
                    cell.setCellType(CellType.STRING);
                    // 设置列宽
                    sheet.setColumnWidth(i, (int) ((attr.width() + 0.72) * 256));
                    // 提示信息
//                    if (StringUtils.isNotEmpty(attr.prompt())) {
//                        // 这里默认设了2-101列提示.
//                        setPrompt(sheet, "", attr.prompt(), 1, 100, i, i);
//                    }
//                    //只能选择不能输入
//                    if (attr.combo().length > 0) {
//                        // 这里默认设了2-101列只能选择不能输入.
//                        setValidation(sheet, attr.combo(), 1, 100, i, i);
//                    }
                    //设置样式
                    setRowStyle(workbook, cell, attr, true);
                }


                // 写入各条记录
                int startNo = index * sheetSize;
                int endNo = Math.min(startNo + sheetSize, list.size());
                //每条记录对应excel表中的一行
                for (int i = startNo; i < endNo; i++) {
                    row = sheet.createRow(i + 1 - startNo);
                    // 得到导出对象.
                    T vo = (T) list.get(i);
                    for (int j = 0; j < fields.size(); j++) {
                        Field f = fields.get(j);
                        // 获得field.
                        Field field = vo.getClass().getDeclaredField(f.getName());
                        // 设置实体类私有属性可访问
                        field.setAccessible(true);
                        // 使用反射的方式修改注解的值
                        Excel attr = field.getAnnotation(Excel.class);
                        try {
                            // 设置行高
                            row.setHeight((short) (attr.height() * 20));
                            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                            if (attr.isExport()) {
                                // 创建cell
                                cell = row.createCell(j);
                                setRowStyle(workbook, cell, attr, false);
                                if (vo == null) {
                                    // 如果数据存在就填入,不存在填入空格.
                                    cell.setCellValue("");
                                    continue;
                                }

                                String dateFormat = attr.dateFormat();
                                Object o = field.get(vo);
                                String readConverterExp = attr.readConverterExp();
                                if (StringUtils.isNotEmpty(dateFormat) && o != null) {
//                                    cell.setCellValue(new SimpleDateFormat(dateFormat).format((Date) field.get(dto)));
                                    cell.setCellValue((Date) o);
                                } else if (StringUtils.isNotEmpty(readConverterExp) && o != null) {
                                    cell.setCellValue(convertByExp(String.valueOf(field.get(vo)), readConverterExp));
                                } else {
                                    // 如果数据存在就填入,不存在填入空格.
                                    String value = field.get(vo) == null ? attr.defaultValue() : field.get(vo) + attr.suffix();
                                    cell.setCellValue(value);
                                    cell.setCellType(CellType.STRING);
                                }
                            }
                        } catch (Exception e) {
                            log.error("导出Excel失败{}", e.getMessage());
                        }
                    }
                }
            }

            fileName = new String(fileName.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            OutputStream outputStream = response.getOutputStream();
            response.flushBuffer();
            workbook.write(outputStream);
            // 写完数据关闭流
            outputStream.close();
            log.warn("导出成功");

//            //String filename = encodingFilename(sheetName);
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(sheetName, "UTF-8"));
//            response.flushBuffer();
//            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出Excel异常{}", e);
            throw new RuntimeException("导出Excel失败，请联系网站管理员！");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param annotationMapping
     * @param list              导出数据集合
     * @param sheetName         工作表的名称
     * @return 结果
     */
    public void exportCustomExcel(Map<String, Map> annotationMapping, List<?> list, String fileName, String sheetName, HttpServletResponse response) {
        XSSFWorkbook workbook = null;
        try {
            // 得到所有定义字段
            List<Field> fields = getClassFields();

            // 产生工作薄对象
            workbook = new XSSFWorkbook();
            // excel2003中每个sheet中最多有65536行
            int sheetSize = Integer.MAX_VALUE;
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++) {
                // 产生工作表对象
                XSSFSheet sheet = workbook.createSheet();
                if (sheetNo == 0) {
                    workbook.setSheetName(index, sheetName);
                } else {
                    // 设置工作表的名称.
                    workbook.setSheetName(index, sheetName + index);
                }
                XSSFRow row;
                XSSFCell cell; // 产生单元格

                // 产生一行
                row = sheet.createRow(0);
                // 写入各个字段的列头名称
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    Excel attr = field.getAnnotation(Excel.class);
                    // 创建列
                    cell = row.createCell(i);
                    row.setHeight((short) (attr.height() * 20));
                    // 设置列中写入内容为String类型
                    cell.setCellType(CellType.STRING);
                    // 设置列宽
                    sheet.setColumnWidth(i, (int) ((attr.width() + 0.72) * 256));
                    // 提示信息
//                    if (StringUtils.isNotEmpty(attr.prompt())) {
//                        // 这里默认设了2-101列提示.
//                        setPrompt(sheet, "", attr.prompt(), 1, 100, i, i);
//                    }
//                    //只能选择不能输入
//                    if (attr.combo().length > 0) {
//                        // 这里默认设了2-101列只能选择不能输入.
//                        setValidation(sheet, attr.combo(), 1, 100, i, i);
//                    }
                    //设置样式
                    setRowStyle(workbook, cell, attr, true);
                }


                // 写入各条记录
                int startNo = index * sheetSize;
                int endNo = Math.min(startNo + sheetSize, list.size());
                //每条记录对应excel表中的一行
                for (int i = startNo; i < endNo; i++) {
                    row = sheet.createRow(i + 1 - startNo);
                    // 得到导出对象.
                    T vo = (T) list.get(i);
                    Object key = "";
                    try {
                        Field codeField = vo.getClass().getDeclaredField("stockCode");
                        // 设置实体类私有属性可访问
                        codeField.setAccessible(true);
                        key = codeField.get(vo);
                    } catch (Exception e) {
                        Field codeField = vo.getClass().getDeclaredField("subName");
                        // 设置实体类私有属性可访问
                        codeField.setAccessible(true);
                        key = codeField.get(vo);
                        //TODO 异常更换组件
//                        log.info("异常更换组件");
                    }

                    for (int j = 0; j < fields.size(); j++) {
                        Field f = fields.get(j);
                        // 获得field.
                        Field field = vo.getClass().getDeclaredField(f.getName());

                        // 设置实体类私有属性可访问
                        field.setAccessible(true);
                        // 使用反射的方式修改注解的值
                        Excel attr = field.getAnnotation(Excel.class);
                        InvocationHandler handler = Proxy.getInvocationHandler(attr);
                        Field annotationField = handler.getClass().getDeclaredField("memberValues");
                        annotationField.setAccessible(true);
                        Map map = (Map) annotationField.get(handler);
                        Map map2 = annotationMapping.get(key + "-" + f.getName());
                        map.putAll(map2);
                        try {
                            // 设置行高
                            row.setHeight((short) (attr.height() * 20));
                            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                            if (attr.isExport()) {
                                // 创建cell
                                cell = row.createCell(j);
                                setRowStyle(workbook, cell, attr, false);
                                if (vo == null) {
                                    // 如果数据存在就填入,不存在填入空格.
                                    cell.setCellValue("");
                                    continue;
                                }

                                String dateFormat = attr.dateFormat();
                                Object o = field.get(vo);
                                String readConverterExp = attr.readConverterExp();
                                if (StringUtils.isNotEmpty(dateFormat) && o != null) {
//                                    cell.setCellValue(new SimpleDateFormat(dateFormat).format((Date) field.get(dto)));
                                    cell.setCellValue((Date) o);
                                } else if (StringUtils.isNotEmpty(readConverterExp) && o != null) {
                                    cell.setCellValue(convertByExp(String.valueOf(field.get(vo)), readConverterExp));
                                } else {
                                    // 如果数据存在就填入,不存在填入空格.
                                    String value = field.get(vo) == null ? attr.defaultValue() : field.get(vo) + attr.suffix();
                                    cell.setCellValue(value);
                                    cell.setCellType(CellType.STRING);
                                }
                            }
                        } catch (Exception e) {
                            log.error("导出Excel失败:", e);
//                            log.error("导出Excel失败{}", e.getMessage());
                        }
                    }
                }
            }

//            response.reset();
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", "UTF-8")); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。XSSF:xlsx    HSSF:xls  中文要用encode处理一下
//            response.setContentType("application/msexcel");
//            response.setContentType("application/octet-stream;charset=UTF-8");
            fileName = new String(fileName.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            OutputStream outputStream = response.getOutputStream();
            response.flushBuffer();
            workbook.write(outputStream);
            // 写完数据关闭流
            outputStream.close();
            log.warn("导出成功");

//            //String filename = encodingFilename(sheetName);
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(sheetName, "UTF-8"));
//            response.flushBuffer();
//            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出Excel异常{}", e);
            throw new RuntimeException("导出Excel失败，请联系网站管理员！");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private XSSFCellStyle setRowStyle(XSSFWorkbook workbook, XSSFCell cell, Excel attr, boolean isHead) {
        //样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        //字体
        XSSFFont font = workbook.createFont();

        if (isHead) {
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            //导出列头字体颜色
            font.setColor(attr.headerColor().index);
            //导出列头背景颜色
            cellStyle.setFillForegroundColor(attr.headerBackgroundColor().index);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            // 粗体显示
            font.setBold(attr.bold());
            font.setColor(attr.color().index);
            font.setUnderline(attr.fontUnderLine()); //下划线
            font.setStrikeout(attr.strikeout()); //下划线

            //设置颜色
            cellStyle.setBottomBorderColor(attr.backgroundColor().index);
            //背景色
            cellStyle.setFillForegroundColor(attr.backgroundColor().index);

            String dateFormat = attr.dateFormat();
            if (StringUtils.isNotEmpty(dateFormat)) {
                XSSFDataFormat format = workbook.createDataFormat();
                cellStyle.setDataFormat(format.getFormat(dateFormat));
            }
        }

        //背景色填充模式
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 选择需要用到的字体格式
        cellStyle.setFont(font);
        // 写入列名
        cellStyle.setAlignment(attr.align());
        cell.setCellValue(attr.name());
        //自动换行
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);

        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        return cellStyle;
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @return 解析后值
     * @throws Exception
     */
    private static String convertByExp(String propertyValue, String converterExp) throws Exception {
        try {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource) {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return propertyValue;
    }


    /**
     * 导出数据通常处理
     *
     * @param po
     * @param map
     */
    private void generalOpr(BaseStock po, Map map) {
        //通用
        Double circulation = po.getCirculation();
        byte fontUnderLine = Font.U_NONE;
        boolean strikeout = false;
        if (circulation < 30) {
            fontUnderLine = Font.U_SINGLE;
        } else if (circulation > 400) {
            strikeout = true;
        }

        Integer combo = po.getCombo();
        IndexedColors color = IndexedColors.BLACK;
        if (combo != null && combo > 1) {
            color = IndexedColors.RED;
        }

        map.put("fontUnderLine", fontUnderLine);
        map.put("strikeout", strikeout);
        map.put("color", color);

        //底色
        String source = po.getSource();
        if ("3".equals(source) || "5".equals(source)) {
            map.put("backgroundColor", IndexedColors.LIGHT_YELLOW);
        } else if ("4".equals(source) || "6".equals(source)) {
            map.put("backgroundColor", IndexedColors.LIGHT_GREEN);
        }

        //所属板块
        String plate = po.getPlate();
        if (!"主板".equals(plate)) {
            map.put("bold", true);
        } else {
            map.put("bold", false);
        }
//            if (i % 2 == 1) {
//                //单数 浅灰
//                map.put("backgroundColor", IndexedColors.GREY_25_PERCENT);
//            } else {
////                    map.put("backgroundColor", IndexedColors.TURQUOISE);
//            }
    }

    /**
     * 初始化注解数据
     *
     * @param map
     * @return
     */
    private Map<String, Object> initDefaultAnnotationMap(HashMap<String, Object> map) {
        //所有动态配置，大家不一样的东西，都要在这里配置一下
        Map<String, Object> defaultAnnotationMap;
        defaultAnnotationMap = SerializationUtils.clone(map);
        defaultAnnotationMap.remove("name");
        defaultAnnotationMap.remove("dateFormat");
        defaultAnnotationMap.remove("suffix");
        defaultAnnotationMap.remove("bold");
        return defaultAnnotationMap;
    }

    /**
     * 获取当前字段注解的配置
     *
     * @param po
     * @param f
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private Map getAnnotationMap(T po, Field f) throws NoSuchFieldException, IllegalAccessException {
        // 获得field.
        Field field = po.getClass().getDeclaredField(f.getName());
        // 设置实体类私有属性可访问
        field.setAccessible(true);
        // 使用反射的方式修改注解的值
        Excel annotation = field.getAnnotation(Excel.class);
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        Field annotationField = handler.getClass().getDeclaredField("memberValues");
        annotationField.setAccessible(true);
        return (Map) annotationField.get(handler);
    }

    /**
     * 得到class所有属性字段
     *
     * @return
     */
    private List<Field> getClassFields() {
        // 得到所有定义字段
        Field[] allFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<Field>();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Excel.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * 涨停报表样式处理
     *
     * @param list
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public Map<String, Map> OprZtReport(List<ZtReport> list) throws IllegalAccessException, NoSuchFieldException, ParseException {
        //注解和对象的映射关系 key:代码-属性 value:注解配置
        Map<String, Map> annotationMapping = Maps.newConcurrentMap();
        //注解只有一个，所以不符合条件的话，要给默认值
        Map<String, Object> defaultAnnotationMap = Maps.newConcurrentMap();
        Boolean firstFlag = true;
        List<Field> fields = getClassFields();


        //涨停底色处理
        Map<String, List<ZtReport>> ztMap = list.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        Map<String, Object> colorMap = Maps.newConcurrentMap();
        IndexedColors[] colorArr = {IndexedColors.PALE_BLUE, IndexedColors.GREY_25_PERCENT, IndexedColors.LIGHT_CORNFLOWER_BLUE, IndexedColors.LIGHT_TURQUOISE, IndexedColors.LIME, IndexedColors.LIGHT_GREEN};
        int num = 0;
        for (String str : ztMap.keySet()) {
            if (str.contains("最")) {
                colorMap.put(str, IndexedColors.WHITE);
            } else {
                colorMap.put(str, colorArr[num++ % colorArr.length]);
            }
        }


        //集合循环
        for (int i = 0; i < list.size(); i++) {
            ZtReport po = list.get(i);
            String stockCode = po.getStockCode();

            //注解循环
            for (int j = 0; j < fields.size(); j++) {
                Field f = fields.get(j);
                // 获取注解映射的map对象
                Map map = getAnnotationMap((T) po, f);

                String key = stockCode + "-" + f.getName();
                //                Object o = field.get((T) po);
                //                log.info(i + "-" + key + "-" + o);

                if (firstFlag) {
                    defaultAnnotationMap = initDefaultAnnotationMap((HashMap<String, Object>) map);
                    firstFlag = false;
                } else {
                    map.putAll(defaultAnnotationMap);
                }

                generalOpr(po, map);
                specialOprZtReport(po, f, map, colorMap);

                //深拷贝后，把映射关系放到map中
                Map<String, Object> afterMap = SerializationUtils.clone((HashMap<String, Object>) map);
                annotationMapping.put(key, afterMap);
            }
        }
        return annotationMapping;
    }


    /**
     * 模板报表样式处理
     *
     * @param list
     * @return
     */
    public Map<String, Map> OprMbReport(List<MbReport> list) throws NoSuchFieldException, IllegalAccessException {
        //注解和对象的映射关系 key:代码-属性 value:注解配置
        Map<String, Map> annotationMapping = Maps.newConcurrentMap();
        //注解只有一个，所以不符合条件的话，要给默认值
        Map<String, Object> defaultAnnotationMap = Maps.newConcurrentMap();
        Boolean firstFlag = true;
        List<Field> fields = getClassFields();

        //集合循环
        for (int i = 0; i < list.size(); i++) {
            MbReport po = list.get(i);
            String stockCode = po.getStockCode();

            //注解循环
            for (int j = 0; j < fields.size(); j++) {
                Field f = fields.get(j);
                // 获取注解映射的map对象
                Map map = getAnnotationMap((T) po, f);

                String key = stockCode + "-" + f.getName();
                //                Object o = field.get((T) po);
                //                log.info(i + "-" + key + "-" + o);

                if (firstFlag) {
                    defaultAnnotationMap = initDefaultAnnotationMap((HashMap<String, Object>) map);
                    firstFlag = false;
                } else {
                    map.putAll(defaultAnnotationMap);
                }

                generalOpr(po, map);
                specialOprMbReport(po, f, map);

                //深拷贝后，把映射关系放到map中
                Map<String, Object> afterMap = SerializationUtils.clone((HashMap<String, Object>) map);
                annotationMapping.put(key, afterMap);
            }
        }
        return annotationMapping;
    }


    /**
     * 波动报表样式处理
     *
     * @param list
     * @return
     */
    public Map<String, Map> OprBdReport(List<BdReport> list) throws NoSuchFieldException, IllegalAccessException {
        //注解和对象的映射关系 key:代码-属性 value:注解配置
        Map<String, Map> annotationMapping = Maps.newConcurrentMap();
        //注解只有一个，所以不符合条件的话，要给默认值
        Map<String, Object> defaultAnnotationMap = Maps.newConcurrentMap();
        Boolean firstFlag = true;
        List<Field> fields = getClassFields();

        //集合循环
        for (int i = 0; i < list.size(); i++) {
            BdReport po = list.get(i);
            String stockCode = po.getStockCode();


            //注解循环
            for (int j = 0; j < fields.size(); j++) {
                Field f = fields.get(j);
                // 获取注解映射的map对象
                Map map = getAnnotationMap((T) po, f);

                String key = stockCode + "-" + f.getName();
                //                Object o = field.get((T) po);
                //                log.info(i + "-" + key + "-" + o);

                if (firstFlag) {
                    defaultAnnotationMap = initDefaultAnnotationMap((HashMap<String, Object>) map);
                    firstFlag = false;
                } else {
                    map.putAll(defaultAnnotationMap);
                }

                generalOpr(po, map);
                specialOprBdReport(po, f, map);

                //深拷贝后，把映射关系放到map中
                Map<String, Object> afterMap = SerializationUtils.clone((HashMap<String, Object>) map);
                annotationMapping.put(key, afterMap);
            }
        }
        return annotationMapping;
    }

    /**
     * 单个特殊处理中的通用部分
     *
     * @param po
     * @param f
     * @param map
     */
    private void generalSpecialOpr(BaseStock po, Field f, Map map) {

        //具体
        switch (f.getName()) {
            //流通盘大小
            case "circulation":
                //与通用不同的是，这里需要改变底色
                Double value = po.getCirculation();
                if (value < 30) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value > 400) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //昨日涨幅
            case "yesterdayGains":
                value = po.getYesterdayGains();
                if (value == null) break;
                if (value < -5) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value > 9) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //开盘涨幅
            case "startGains":
                value = po.getStartGains();
                if (value < -5) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value > 5) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //涨幅
            case "gains":
                map.put("color", IndexedColors.RED);
                break;
            case "entitySize":
                map.put("color", IndexedColors.RED);
                value = po.getEntitySize();
                if (value < -6) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value > 6) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //换手
            case "changingHands":
                value = po.getChangingHands();
                if (value == 0) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value > 50) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //昨日换手
            case "yesterdayChangingHands":
                //辨识度标的逻辑
                value = po.getYesterdayChangingHands();
                if (value == null) break;
                if (value == 0) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value > 50) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //振幅
            case "amplitude":
                value = po.getAmplitude();
                if ("主板".equals(po.getPlate()) && value > 12 || value > 24) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value == 0) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //昨日振幅
            case "yesterdayAmplitude":
                value = po.getYesterdayAmplitude();
                if (value == null) break;
                if ("主板".equals(po.getPlate()) && value > 12 || value > 24) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                } else if (value == 0) {
                    map.put("backgroundColor", IndexedColors.CORNFLOWER_BLUE);
                }
                break;
            //股票名称
            case "stockName":
                map.put("color", IndexedColors.RED);
                //辨识度标的逻辑
                String valueStr = po.getStockName();
                if (BSD_STOCK_LIST.contains(valueStr)) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                }
                break;
            //说明
            case "instructions":
                map.put("color", IndexedColors.RED);
                break;
        }
    }


    /**
     * 导出样式特殊处理
     *
     * @param po
     * @param f
     * @param map
     * @param colorMap
     */
    private void specialOprZtReport(ZtReport po, Field f, Map map, Map colorMap) {
        //根据主业给与背景随机颜色
        map.put("backgroundColor", colorMap.get(po.getMainBusiness()));
        //具体
        switch (f.getName()) {
            //日内龙
            case "hardenTime":
                //与通用不同的是，这里需要改变底色
                String str = po.getInstructions();
                if (StringUtils.isNoneBlank(str) && str.contains("日内龙")) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                }
                break;
            //回封龙
            case "finalHardenTime":
                //与通用不同的是，这里需要改变底色
                str = po.getInstructions();
                if (StringUtils.isNoneBlank(str) && str.contains("回封龙")) {
                    map.put("backgroundColor", IndexedColors.TURQUOISE);
                }
                break;
        }
        //具体
        generalSpecialOpr(po, f, map);
    }

    private void specialOprBdReport(BdReport po, Field f, Map map) {

        generalSpecialOpr(po, f, map);
    }


    private void specialOprMbReport(MbReport po, Field f, Map map) {
        generalSpecialOpr(po, f, map);
    }

    /**
     * 波动报表样式处理
     *
     * @param list
     * @return
     */
    public Map<String, Map> OprSubjectReport(List<SubjectReport> list) throws NoSuchFieldException, IllegalAccessException {
        //注解和对象的映射关系 key:代码-属性 value:注解配置
        Map<String, Map> annotationMapping = Maps.newConcurrentMap();
        //注解只有一个，所以不符合条件的话，要给默认值
        Map<String, Object> defaultAnnotationMap = Maps.newConcurrentMap();
        Boolean firstFlag = true;
        List<Field> fields = getClassFields();

        //底色处理
        Map<String, List<SubjectReport>> ztMap = list.stream().collect(Collectors.groupingBy(SubjectReport::getSubName));
        Map<String, Object> colorMap = Maps.newConcurrentMap();
        IndexedColors[] colorArr = {IndexedColors.PALE_BLUE, IndexedColors.GREY_25_PERCENT, IndexedColors.LIGHT_CORNFLOWER_BLUE, IndexedColors.LIGHT_TURQUOISE, IndexedColors.LIME, IndexedColors.LIGHT_GREEN};
        int num = 0;
        for (String str : ztMap.keySet()) {
            colorMap.put(str, colorArr[num++ % colorArr.length]);
        }

        //集合循环
        for (int i = 0; i < list.size(); i++) {
            SubjectReport po = list.get(i);
            String subName = po.getSubName();

            //注解循环
            for (int j = 0; j < fields.size(); j++) {
                Field f = fields.get(j);
                // 获取注解映射的map对象
                Map map = getAnnotationMap((T) po, f);
                String key = subName + "-" + f.getName();

                if (firstFlag) {
                    defaultAnnotationMap = initDefaultAnnotationMap((HashMap<String, Object>) map);
                    firstFlag = false;
                } else {
                    map.putAll(defaultAnnotationMap);
                }
                //根据主业给与背景随机颜色
                map.put("backgroundColor", colorMap.get(po.getSubName()));

                //深拷贝后，把映射关系放到map中
                Map<String, Object> afterMap = SerializationUtils.clone((HashMap<String, Object>) map);
                annotationMapping.put(key, afterMap);
            }
        }
        return annotationMapping;
    }
}


// 深-转成 JSON 再转回来  类型会变的不对
// 深-使用 Apache 的序列化工具类 SerializationUtils
// 浅-新建 Map 时将原 Map 传入构造方法
//                Map<String, Object> afterMap = JSON.parseObject(JSON.toJSONString(map));
//    Map<String, Object> afterMap = SerializationUtils.clone((HashMap<String, Object>) map);