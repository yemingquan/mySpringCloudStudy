package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.system.SystemConfConstant;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.BaseMarket;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.FileUtil;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;


@Slf4j
@Api(tags = "基础数据-输入")
@RequestMapping("report")
@RestController
public class InputController {

    @Autowired
    InputService inputService;
    @Autowired
    StudentService studentService;
    @Autowired
    BaseSubjectLineDetailService baseSubjectLineDetailService;
    @Autowired
    BaseDateNewsService baseDateNewsService;
    @Autowired
    BaseDateService baseDateService;
    @Autowired
    BaseMarketService baseMarketService;

    @ApiOperationSupport(order = 1)
    @ApiOperation("0-复盘初始化")
    @PostMapping("/initFP")
    public RespBean initFP(@RequestParam(value = "clearFlag", required = false) String clearFlag,
                           @RequestParam(value = "importDateFlag", required = false) String importDateFlag) {
        String thsBasePath = SystemConfConstant.THS_BASE_PATH;
        try {
            //1.初始化复盘文件夹。输入clearFlag时，会清楚所有数据，一般情况下，不需要使用
            initDir(clearFlag, thsBasePath);

            //2将导出的同花顺文件转换成xlsx后，导入到数据库中
            //导入前，需要手动填写主业内容，防止生成的时候统计不对
            if (StringUtils.isEmpty(importDateFlag)) {
                return RespBean.success("处理到文件导入前");
            }

            for (int i = 1; i <= 6; i++) {
                File file = new File(thsBasePath + "Table" + i + ".xls");
                File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
                if (i == 1) inputService.importExcelZtStock(new FileInputStream(tempFile));
                if (i == 2) inputService.importExcelZthfStock(new FileInputStream(tempFile));
                if (i == 3) inputService.importExcelZbStock(new FileInputStream(tempFile));
                if (i == 4) inputService.importExcelDtStock(new FileInputStream(tempFile));
                if (i == 5) inputService.importExcelBdUpStock(new FileInputStream(tempFile));
                if (i == 6) inputService.importExcelBdDownStock(new FileInputStream(tempFile));
            }
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    public void initDir(@RequestParam(value = "clearFlag", required = false) String clearFlag, String basePath) throws IOException {
        //0清理工作-调试阶段不用开启
        if ("1".equals(clearFlag)) {
            File baseDir = new File(basePath);
            Path directory = Paths.get(basePath);
            FileUtil.deleteDirectory(directory);
            baseDir.mkdirs();
            log.info("导出文件已处理");
        }

        //1环境创建-文件夹
        String date = DateUtil.format(new Date(), "yyyy-M-d");
        File baseDir = new File("D:\\DATA\\手机备份数据\\" + date);
        File zsPath = new File(baseDir.getPath() + "\\走势");
        File qtPath = new File(baseDir.getPath() + "\\其他");
        File wpPath = new File(baseDir.getPath() + "\\尾盘");
        FileUtil.mkdirs(baseDir.getPath());
        FileUtil.mkdirs(zsPath.getPath());
        FileUtil.mkdirs(qtPath.getPath());
        FileUtil.mkdirs(wpPath.getPath());
        log.info("图片存储文件夹创立");
    }

    @ApiOperationSupport(order = 11)
    @ApiOperation("1-1 导入涨停Excel数据")
    @PostMapping("/importExcelZtStock")
    public RespBean importExcelZtStock(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importExcelZtStock(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 12)
    @ApiOperation("1-2 导入涨停回封Excel数据")
    @PostMapping("/importExcelZthfStock")
    public RespBean importExcelZthfStock(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importExcelZthfStock(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 13)
    @ApiOperation("1-3 导入炸板Excel数据")
    @PostMapping("/importExcelZbStock")
    public RespBean importExcelZbStock(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importExcelZbStock(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 14)
    @ApiOperation("1-4 导入曾跌停Excel数据")
    @PostMapping("/importExcelDtStock")
    public RespBean importExcelDtStock(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importExcelDtStock(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 15)
    @ApiOperation("1-5 导入波动向上Excel数据")
    @PostMapping("/importExcelBdUpStock")
    public RespBean importExcelBdUpStock(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importExcelBdUpStock(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 16)
    @ApiOperation("1-6 导入波动向下Excel数据")
    @PostMapping("/importExcelBdDownStock")
    public RespBean importExcelBdDownStock(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importExcelBdDownStock(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperationSupport(order = 21)
    @ApiOperation("2-1 题材明细导入(只导入指定日期的数据，其他日期的数据不完整时，建议删除)")
    @PostMapping("/importSubjectDetail")
    public RespBean importSubjectDetail(@RequestParam(value = "date", required = false) String date,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        @RequestPart MultipartFile multipartFile) {
        try {
            if (StringUtils.isEmpty(date)) {
                date = DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10);
            }
            //导入前先删除当天的数据
            baseSubjectLineDetailService.deleteBaseSubjectLineDetailByDateList(date, startDate);
            boolean flag = inputService.importSubjectDetail(multipartFile.getInputStream(), startDate, date);
            if (flag) {
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 30)
    @GetMapping("/getNewSTemplate")
    @ApiOperation("3-0 Excel导入消息-获得摸板")
    public void exportBdRePort(HttpServletResponse response) throws Exception {
        String fileName = "NewsTemplate.xlsx";
//        URL a1 = inputController.class.getResource("templates/NewsTemplate.xlsx");
//        URL a2 = inputController.class.getClassLoader().getResource("templates/NewsTemplate.xlsx");
//        URL a3 = ClassLoader.getSystemClassLoader().getResource("templates/NewsTemplate.xlsx");
        URL a5 = ClassLoader.getSystemResource("templates/NewsTemplate.xlsx");
//        URL a6 = Thread.currentThread().getContextClassLoader().getResource("templates/NewsTemplate.xlsx");
        String filePath = a5.getPath();
        File file = new File(filePath);
//        fileName = new String(fileName.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
        FileInputStream input = new FileInputStream(file);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        OutputStream outputStream = response.getOutputStream();
        OutputStream output = response.getOutputStream();
        response.flushBuffer();
        byte[] buffer = new byte[10240];
        for (int length = 0; (length = input.read(buffer)) > 0; ) {
            output.write(buffer, 0, length);
        }
        outputStream.flush();
        // 写完数据关闭流
        outputStream.close();
    }


    @ApiOperationSupport(order = 31)
    @ApiOperation("3-1 Excel导入消息-根据创建时间维护（原始数据）")
    @PostMapping("/importNews/UseCreateDate")
    public RespBean importNewsUseCreateDate(@RequestParam(value = "clearFlag", required = false) String clearFlag,
                                            @RequestPart MultipartFile multipartFile) {
        if (StringUtils.isNotBlank(clearFlag)) {
            baseDateNewsService.deleteByCreateDate(DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        }

        try {
            List<BaseDateNews> list = ExcelUtil.excelToList(multipartFile,BaseDateNews.class);
            baseDateNewsService.oprNewsData(list);
            log.info("准备插入数据");
            if (baseDateNewsService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 32)
    @ApiOperation("3-2 Excel导入消息-根据消息时间维护（增量数据）")
    @PostMapping("/importNews/UseDate")
    public RespBean importNewsUseDate(@RequestParam(value = "date", required = true) String date,
                                      @RequestParam(value = "startDate", required = false) String startDate,
                                      @RequestPart MultipartFile multipartFile) {

        //导入前先删除当天的数据
        baseDateNewsService.deleteBaseDateNewsByDateList(date, startDate);

        try {
            List<BaseDateNews> list = ExcelUtil.excelToList(multipartFile,BaseDateNews.class);
            baseDateNewsService.oprNewsData(list);
            log.info("准备插入数据");
            if (baseDateNewsService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 41)
    @ApiOperation("4-1 Excel导入消息-市场概要信息")
    @PostMapping("/market")
    public RespBean market(@RequestPart MultipartFile multipartFile) {
        try {
            List<BaseMarket> list = ExcelUtil.excelToList(multipartFile,BaseMarket.class);
            for (BaseMarket bm :list){
                String marketTrends = bm.getMarketTrends();
                if(StringUtils.isNotBlank(marketTrends)){
//                    log.info("日期:{},market_trends长度:{}",DateUtil.format(bm.getDate(), DateConstant.DATE_FORMAT_10), marketTrends.length());
                }
            }
            log.info("准备插入数据");
            if (baseMarketService.insertOrUpdateBatch(list)) {
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 9999)
    @ApiOperation("Excel导入测试")
    @PostMapping("/import")
    public RespBean importExcel(@RequestPart MultipartFile multipartFile) {
        try {
            List<Student> list = ExcelUtil.excelToList(multipartFile,Student.class);
            if (studentService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}