package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateTypeConstant;
import com.example.springBootDemo.config.components.enums.NewSEnum;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.FileUtil;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
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
public class inputController {

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

    @ApiOperation("导入波动向上Excel数据")
    @PostMapping("/importExcelBdUpStock")
    public RespBean importExcelBdUpStock(MultipartFile multipartFile) {
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


    @ApiOperation("导入波动向下Excel数据")
    @PostMapping("/importExcelBdDownStock")
    public RespBean importExcelBdDownStock(MultipartFile multipartFile) {
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

    @ApiOperation("导入曾跌停Excel数据")
    @PostMapping("/importExcelDtStock")
    public RespBean importExcelDtStock(MultipartFile multipartFile) {
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


    @ApiOperation("导入炸板Excel数据")
    @PostMapping("/importExcelZbStock")
    public RespBean importExcelZbStock(MultipartFile multipartFile) {
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


    @ApiOperation("导入涨停Excel数据")
    @PostMapping("/importExcelZtStock")
    public RespBean importExcelZtStock(MultipartFile multipartFile) {
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


    @ApiOperation("导入涨停回封Excel数据")
    @PostMapping("/importExcelZthfStock")
    public RespBean importExcelZthfStock(MultipartFile multipartFile) {
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

    @ApiOperation("Excel导入测试")
    @PostMapping("/import")
    public RespBean importExcel(MultipartFile multipartFile) throws IOException {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        try {
            List<Student> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Student.class, importParams);
            if (studentService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperation("0-复盘初始化")
    @PostMapping("/initFP")
    public RespBean initFP(@RequestParam(value = "clearFlag", required = false) String clearFlag,
                           @RequestParam(value = "importDateFlag", required = false) String importDateFlag) {

        String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
        try {
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

            //2将导出的同花顺文件转换成xlsx后，导入到数据库中
            //导入前，需要手动填写主业内容，防止生成的时候统计不对
            if (StringUtils.isEmpty(importDateFlag)) {
                return RespBean.success("处理到文件导入前");
            }

            for (int i = 1; i <= 6; i++) {
                File file = new File(basePath + "Table" + i + ".xls");
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

    @ApiOperation("1-题材明细导入(只导入指定日期的数据，其他日期的数据不完整时，建议删除)")
    @PostMapping("/importSubjectDetail")
    public RespBean importSubjectDetail(@RequestParam(value = "date", required = true) String date,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        MultipartFile multipartFile) {
        try {
            if (StringUtils.isEmpty(date)) {
                date = DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10);
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


    @ApiOperation("Excel导入消息-根据创建时间维护")
    @PostMapping("/importNews/UseCreateDate")
    public RespBean importNews(@RequestParam(value = "clearFlag", required = false) String clearFlag,
                               MultipartFile multipartFile) throws IOException {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        if (StringUtils.isNotBlank(clearFlag)) {
            EntityWrapper<BaseDateNews> wrapper = new EntityWrapper<>();
            wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
            baseDateNewsService.delete(wrapper);
        }

        try {
            Date dealDate = baseDateService.getBeforeTypeDate(new Date(), DateTypeConstant.DEAL_LIST);
            List<BaseDateNews> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseDateNews.class, importParams);
            for (int i = 0; i < list.size(); i++) {
                BaseDateNews news = list.get(i);

                //日期
                Date date = news.getDate();
                if (date == null) {
                    date = new Date();
                    news.setDate(date);
                }
                Integer duration = news.getDuration();
                if (duration == null) {
                    duration = 0;
                }
                //延期或即时
                String type = news.getType();
                if (StringUtils.isBlank(type)) {
                    Date lastDay = DateUtil.getNextDay(date, duration);
                    if (lastDay.before(dealDate)) {
                        news.setType(NewSEnum.TYPE_INSTANTLY.getName());
                    } else {
                        news.setType(NewSEnum.TYPE_FUTURE.getName());
                    }
                }
                //影响范围
//                news.setScope(NewSEnum.getCode(NewsConstant.SCOPE, news.getScope()));
                //开盘
//                news.setHappen(NewSEnum.getCode(NewsConstant.HAPPEN, news.getHappen()));
                //创建时间
                news.setCreateDate(new Date());
            }
            if (baseDateNewsService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @GetMapping("/getNewSTemplate")
    @ApiOperation("Excel导入消息-获得摸板")
    public void exportBdRePort(HttpServletResponse response) throws Exception {
        String fileName = "NewSTemplate.xlsx";
//        URL a1 = inputController.class.getResource("templates/NewSTemplate.xlsx");
//        URL a2 = inputController.class.getClassLoader().getResource("templates/NewSTemplate.xlsx");
//        URL a3 = ClassLoader.getSystemClassLoader().getResource("templates/NewSTemplate.xlsx");
        URL a5 = ClassLoader.getSystemResource("templates/NewSTemplate.xlsx");
//        URL a6 = Thread.currentThread().getContextClassLoader().getResource("templates/NewSTemplate.xlsx");
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
}