package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateTypeConstant;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.Goods;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.entity.input.BaseSubjectDetail;
import com.example.springBootDemo.entity.report.*;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-5 8:35
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@Api(tags = "报表模块")
@RequestMapping("report")
@RestController
public class ReportController {

    @Autowired
    InputService inputService;
    @Autowired
    ReportService reportService;
    @Autowired
    StudentService studentService;
    @Resource
    private ConfBsdStockService confBsdStockService;
    @Autowired
    BaseDateService baseDateService;
    @Autowired
    BaseSubjectLineDetailService baseSubjectLineDetailService;
    @Autowired
    BaseDateNewsService baseDateNewsService;
    @Resource
    private ConfCxStockService confCxStockService;
    @Resource
    private ConfMySotckService confMySotckService;
    @Resource
    private BaseBondService baseBondService;

    @ApiOperationSupport(order = 1)
    @GetMapping("/exportBKReport")
    @ApiOperation("1-0 板块报表导出")
    public void exportBKReport(@RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "clearFlag", required = false) String clearFlag,
                               HttpServletResponse response) {
        log.info("1-0 板块报表导出 date：{} startDate：{} clearFlag:{}", date, startDate, clearFlag);

        long start;
        String fileName = "板块0000.xlsx";
        String sheetName = "板块";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);

        try {
            //获取基础数据，用于后续的数据生成
            List<ZtReport> list1 = reportService.getZtReportByDate(date);
            List<MbReport> list2 = reportService.getMbReportByDate(date);
            List<BdReport> list3 = reportService.getBdReportByDate(date);

            if (CollectionUtils.isEmpty(list3)) {
                log.info("{} 当天没有生成任何数据", date);
                return;
            }

            //导入前先删除当天的数据
            if (StringUtils.isNotBlank(clearFlag)) {
                baseSubjectLineDetailService.deleteBaseSubjectLineDetailByDateList(date, startDate);
            }

            //查询所有明细数据，更新line对象数据
            BaseSubjectLineDetail queryDetail = BaseSubjectLineDetail.builder().createDate(DateUtil.parseDate(date)).build();
            List<BaseSubjectLineDetail> detailList = baseSubjectLineDetailService.getBaseSubjectLineDetailList(queryDetail);

            if (CollectionUtils.isEmpty(detailList) && CollectionUtils.isNotEmpty(list1)) {
                reportService.oprZtDate(list1);
                reportService.oprMbDate(list2);
                reportService.oprBdDate(list3);

                start = System.currentTimeMillis();
                reportService.saveZtInstructions(list1);
                reportService.saveMbInstructions(list2);
                reportService.saveBdInstructions(list3);
                log.info("刷新数据耗时{} ", System.currentTimeMillis() - start);

                //如果当天没有插入,则自动生成默认数据
                List<BaseSubjectDetail> genList = reportService.genBaseSubjectDetail(list1, list2, list3);
                inputService.genSubjectDate(genList);
            }

            //生成市场盘面明细数据
//            reportService.getMarketDetail(list1, list2, list3);

            //上面都是处理当天的逻辑，下面这个是真正的逻辑
            List<SubjectReport> list = reportService.getSubjectReport(date, startDate);
            ExcelUtil<SubjectReport> excelUtil = new ExcelUtil<>(SubjectReport.class);
            start = System.currentTimeMillis();
            Map<String, Map> annotationMapping = excelUtil.OprSubjectReport(list, date);
            log.info("样式配置耗时{} ", System.currentTimeMillis() - start);

            excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);

//            // 定义单元格纵向合并依赖的map条件
//            Map<Integer, int[]> mergeMap = new HashMap<>();
//// key代表要合并的列 value代表依赖的列(ps:列从0开始计数 表格里的第一纵列为第0列)
//            mergeMap.put(1, new int[]{2});
//            mergeMap.put(2, new int[]{2});
//            mergeMap.put(3, new int[]{2});
//            mergeMap.put(4, new int[]{2});
//            mergeMap.put(5, new int[]{2});
//            mergeMap.put(6, new int[]{2});
//
//// 开始合并
//            PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), mergeMap, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperationSupport(order = 11)
    @GetMapping("/exportZtRePort")
    @ApiOperation("1-1 涨停报表导出")
    public void exportZtRePort(@RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "refreshFlag", required = false) String refreshFlag,
                               HttpServletResponse response) {
        log.info("1-1 涨停报表导出 {}", date);
        String fileName = "涨停1111.xlsx";
        String sheetName = "涨停";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);

        if (StringUtils.isNotBlank(refreshFlag)) {
            //检索10天以内的数据
            log.info("刷新10天以内的辨识度标的");
            confBsdStockService.genConfBsdStock(date);
        }

        List<ZtReport> list = reportService.getZtReportByDate(date);
        ExcelUtil<ZtReport> excelUtil = new ExcelUtil<>(ZtReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = null;
        try {
            annotationMapping = excelUtil.OprZtReport(list);
            excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperationSupport(order = 12)
    @GetMapping("/exportMbRePort")
    @ApiOperation("1-2 摸板报表导出")
    public void exportMbRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("1-2 摸板报表导出 {}", date);
        String fileName = "摸板222.xlsx";
        String sheetName = "摸板";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);

        List<MbReport> list = reportService.getMbReportByDate(date);

        ExcelUtil<MbReport> excelUtil = new ExcelUtil<>(MbReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprMbReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @ApiOperationSupport(order = 13)
    @GetMapping("/exportBdRePort")
    @ApiOperation("1-3 波动报表导出")
    public void exportBdRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("1-3 波动报表导出 {}", date);
        String fileName = "波动333.xlsx";
        String sheetName = "波动";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
        List<BdReport> list = reportService.getBdReportByDate(date);


        ExcelUtil<BdReport> excelUtil = new ExcelUtil<>(BdReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprBdReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }


    @ApiOperationSupport(order = 21)
    @GetMapping("/exportNewsReport")
    @ApiOperation("2-1 消息报表导出(当输入date时，date为起始时间，当两个时间都输入时，startDate为起始时间，date为最终时间)")
    public void exportNewsReport(@RequestParam(value = "date", required = false) String date,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("2-1 消息报表导出 {}", date);
        String fileName = "消息.xlsx";
        String sheetName = "消息";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
        if (StringUtils.isNotBlank(startDate)) {
            startDate = baseDateService.getBeforeTypeDate(startDate, DateTypeConstant.DEAL_LIST);
        }
        List<NewsReport> list = baseDateNewsService.getNews(startDate, date);

        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @ApiOperationSupport(order = 22)
    @GetMapping("/exportFinalReport")
    @ApiOperation("2-2 日终报表导出")
    public void exportFinalReport(@RequestParam(value = "date", required = false) String date,
                                  HttpServletResponse response) {
        log.info("2-2 日终报表导出 {}", date);
        String fileName = "日终.xlsx";
        String sheetName = "日终";

        try {
            log.info("次新更新");
            confCxStockService.imporCX();
            log.info("可转债");
            baseBondService.imporKZZ();
            log.info("刷新股票的主业");
            confMySotckService.reflshMyStock();
            log.info("日期功能刷新");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
//        List<NewsReport> list = baseDateNewsService.getNews(date);
//
//        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
//        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
//        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }


    @ApiOperationSupport(order = 1)
    @GetMapping("/aaaaa")
    @ApiOperation("aaaaaaaaaaaaaaa")
    public static void aaaaa(HttpServletResponse response) throws IOException {
        /*空格分割
        三目运算 {{test ? obj:obj2}}
        n: 表示 这个cell是数值类型 {{n:}}
        le: 代表长度{{le:()}} 在if/else 运用{{le:() > 8 ? obj1 : obj2}}
        fd: 格式化时间 {{fd:(obj;yyyy-MM-dd)}}
        fn: 格式化数字 {{fn:(obj;###.00)}}
        fe: 遍历数据,创建row 该标签的意思是会遍历集合数据，会创建新行，如下图1图2效果
        !fe: 遍历数据不创建row
        $fe: 下移插入,把当前行,下面的行全部下移.size()行,然后插入
        #fe: 横向遍历
        v_fe: 横向遍历值
        !if: 删除当前列 {{!if:(test)}}
        单引号表示常量值 ‘’ 比如’1’ 那么输出的就是 1
        &NULL& 空格
        &INDEX& 表示循环中的序号,自动添加
        ]] 换行符 多行遍历导出
        sum： 统计数据
        整体风格和el表达式类似，大家应该也比较熟悉 采用的写法是{{属性}}，然后根据表达式里面的数据取值*/
        Goods goods1 = new Goods(110, "苹果", 1, new Date(), 0, "1");
        Goods goods2 = new Goods(111, "格子衫", 2, new Date(), 0, "0");
        Goods goods3 = new Goods(112, "拉菲红酒", 3, new Date(), 1, "1");
        Goods goods4 = new Goods(113, "玫瑰", 4, new Date(), 1, "0");

        List<Goods> goodsList = new ArrayList<>();
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);
        goodsList.add(goods4);

        //可以抽取为日期工具类
        Date date1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = df.format(date1);

        for (int i = 0; i < goodsList.size(); ++i) {
            //添加序号列
            goodsList.get(i).setOrder(i + 1);
            //Date类型日期转换
            goodsList.get(i).setDateStr(df.format(goodsList.get(i).getShelfLife()));
            //type转换成显示文字
            if (goodsList.get(i).getType() == 1) {
                goodsList.get(i).setTypeName("食品");
            } else if (goodsList.get(i).getType() == 2) {
                goodsList.get(i).setTypeName("服装");
            } else if (goodsList.get(i).getType() == 3) {
                goodsList.get(i).setTypeName("酒水");
            } else if (goodsList.get(i).getType() == 4) {
                goodsList.get(i).setTypeName("花卉");
            }
        }

        for (Goods goods : goodsList) System.out.println(goods);

        // 获取导出excel指定模版，第二个参数true代表显示一个Excel中的所有 sheet
        URL a5 = ClassLoader.getSystemResource("templates/aaaa.xlsx");
        TemplateExportParams params = new TemplateExportParams(a5.getPath(), true);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("date", date);//导出一般都要日期
        data.put("one", goods1);//导出一个对象
        data.put("list", goodsList);//导出list集合

        URL url = ClassLoader.getSystemResource("templates/Screenshot.jpg");
        String path = url.getPath();
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(path));
        ImageEntity image = ExcelUtil.imageToBytes(bufferedImage);
        data.put("image", image);
        try {
            // 简单模板导出方法
            Workbook book = ExcelExportUtil.exportExcel(params, data);
            //下载方法
            ExcelUtil.export(response, book, "abcd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperationSupport(order = 9999)
    @ApiOperation("Excel导出测试")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String fileName = "excel测试.xlsx";
        String sheetName = "excel测试";
//        ExportParams params = new ExportParams(title, "sheet1", ExcelType.XSSF);

        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        List<Student> list = studentService.selectList(wrapper);

        ExcelUtil<Student> excelUtil = new ExcelUtil<>(Student.class);
        excelUtil.exportCustomExcel_bak(list, fileName, sheetName, response);
    }
}