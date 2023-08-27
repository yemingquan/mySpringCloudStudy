package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.service.InputService;
import com.example.springBootDemo.service.StudentService;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.FileUtil;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
            if(StringUtils.isEmpty(importDateFlag)){
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
    public RespBean importSubjectDetail(MultipartFile multipartFile) {
        try {
            boolean flag = inputService.importSubjectDetail(multipartFile.getInputStream());
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}