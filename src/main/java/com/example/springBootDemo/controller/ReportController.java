package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.example.springBootDemo.constant.DateConstant;
import com.example.springBootDemo.config.system.session.RespBean;
import com.example.springBootDemo.entity.BaseCombo;
import com.example.springBootDemo.entity.BaseStockMonitor;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.ConfDate;
import com.example.springBootDemo.entity.fix.FixZtReport;
import com.example.springBootDemo.entity.input.BaseSubjectDetail;
import com.example.springBootDemo.entity.input.BaseZtStock;
import com.example.springBootDemo.entity.input.ConfBusiness;
import com.example.springBootDemo.entity.report.*;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
    BaseZtStockService baseZtStockService;
    @Autowired
    BaseZthfStockService baseZthfStockService;
    @Autowired
    BaseComboService baseComboService;
    @Autowired
    ReportService reportService;
    @Resource
    private ConfBsdStockService confBsdStockService;
    @Autowired
    ConfDateService confDateService;
    @Autowired
    BaseSubjectLineDetailService baseSubjectLineDetailService;
    @Autowired
    BaseDateNewsService baseDateNewsService;
    @Resource
    private ConfCxStockService confCxStockService;
    @Resource
    private ConfStockService confStockService;
    @Resource
    private ConfBusinessService confBusinessService;
    @Autowired
    BaseStockService baseStockService;
    @Autowired
    BaseStockMonitorService baseStockMonitorService;
    @Autowired
    RelationConfService relationConfService;


    @ApiOperationSupport(order = 1)
    @GetMapping("/exportBKReport")
    @ApiOperation("1-0 板块报表导出(这个接口只会删除当天数据)")
    public void exportBKReport(@RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "clearFlag", required = false) String clearFlag,
                               HttpServletResponse response) {
        log.info("1-0 板块报表导出 date：{} startDate：{} clearFlag:{}", date, startDate, clearFlag);

        long start;
        String fileName = "BK0-";
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

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
                baseSubjectLineDetailService.deleteBaseSubjectLineDetailByDateList(date, null);
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
            log.info("样式配置耗时{} ms", System.currentTimeMillis() - start);

            excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);

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
        String fileName = "ZT1-";
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

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
            excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
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
        String fileName = "MB2-";
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

        List<MbReport> list = reportService.getMbReportByDate(date);

        ExcelUtil<MbReport> excelUtil = new ExcelUtil<>(MbReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprMbReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
    }

    @ApiOperationSupport(order = 13)
    @GetMapping("/exportBdRePort")
    @ApiOperation("1-3 波动报表导出")
    public void exportBdRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("1-3 波动报表导出 {}", date);
        String fileName = "BD3-";
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
        List<BdReport> list = reportService.getBdReportByDate(date);


        ExcelUtil<BdReport> excelUtil = new ExcelUtil<>(BdReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprBdReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
    }


    @ApiOperationSupport(order = 21)
    @GetMapping("/exportNewsReport")
    @ApiOperation("2-1 消息报表导出(当输入date时，date为起始时间，当两个时间都输入时，startDate为起始时间，date为最终时间)")
    public void exportNewsReport(@RequestParam(value = "date", required = false) String date,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("2-1 消息报表导出 {}", date);
        String fileName = "NEWS-";
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
        if (StringUtils.isNotBlank(startDate)) {
            startDate = confDateService.getBeforeTypeDate(startDate, DateConstant.DEAL_LIST);
        }
        List<NewsReport> list = baseDateNewsService.getNews(startDate, date);

        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
    }

    @ApiOperationSupport(order = 22)
    @GetMapping("/exportFinalReport")
    @ApiOperation("2-2 日终报表导出")
    public void exportFinalReport(@RequestParam(value = "date", required = false) String date,
                                  HttpServletResponse response) {
        log.info("2-2 日终报表导出 {}", date);
        String fileName = "日终";

        try {


        } catch (Exception e) {
            e.printStackTrace();
        }

//        date = confDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
//        List<NewsReport> list = baseDateNewsService.getNews(date);
//
//        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
//        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
//        excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
    }


    @ApiOperationSupport(order = 31)
    @GetMapping("/exportComboRePort")
    @ApiOperation("3-1 连板梯队报表导出")
    public void exportComboRePort(@RequestParam(value = "date", required = false) String date,
                                  @RequestParam(value = "stockName", required = false) String stockName,
                                  HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("1-3 连板梯队报表导出 {}", date);
        String fileName = "Combo-";
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            String dealDateStr = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

            //刷新当天的连板数据
            Map<String, Object> map = Maps.newHashMap();
            getEchelonComboData(dealDateStr, map);

            Date dealDate = DateUtil.format(dealDateStr, DateConstant.DATE_FORMAT_10);            //基础数据
            getBasicData(dealDate, data);

            //当天以及历史连板梯队
            Date tempDate = DateUtil.getDayDiff(dealDate, 1);
            for (int i = 0; i < 3; i++) {
                tempDate = confDateService.getBeforeTypeDate(DateUtil.getDayDiff(tempDate, -1), DateConstant.DEAL_LIST);
                BaseCombo baseCombo = baseComboService.selectById(tempDate);
                data.put("COMBO_" + i, baseCombo);
            }

//             //炸板数据表现
            List<ZtReport> beforZtStock = reportService.getZtReportByDate(DateUtil.format(DateUtil.getDayDiff(dealDateStr, -1), DateConstant.DATE_FORMAT_10));
            List<String> beforList = beforZtStock.stream().map(ZtReport::getStockCode).collect(Collectors.toList());
            List<ZtReport> todayZtStock = reportService.getZtReportByDate(dealDateStr);
            List<String> todayList = todayZtStock.stream().map(ZtReport::getStockCode).collect(Collectors.toList());
            beforList.removeAll(todayList);

//            EntityWrapper ew = new EntityWrapper<BaseStock>();
//            ew.eq("create_Date", date);
//            ew.in("stock_code", beforList);
//            ew.orderBy("entity_Size",false);
//            List<BaseStock> list = baseStockService.selectList(ew);
//            data.put("ZB_STOCK", list);
//
//            //对于今天涨停的,统计各种结构类型的数据。按照涨停次数分隔.比如1b:xxxx、xxxx,2b:

            //将大于5的连板数据强制改为5b，方便后续统计
            todayZtStock.stream().filter(po -> po.getCombo() >= 5).forEach(po -> po.setCombo(5));

            for (int i = 1; i <= 5; i++) {
                int finalI = i;
                //小盘
                List<String> smallList = todayZtStock.stream()
                        .filter(po -> po.getCombo() == finalI)
                        .filter(po -> po.getInstructions().contains("小盘"))
                        .map(ZtReport::getStockName)
                        .collect(Collectors.toList());
                data.put("SMALL_" + i, smallList);
                //次新
                List<String> cxList = todayZtStock.stream()
                        .filter(po -> po.getCombo() == finalI)
                        .filter(po -> po.getInstructions().contains("次新"))
                        .map(ZtReport::getStockName)
                        .collect(Collectors.toList());
                data.put("CX_" + i, cxList);
//                //低价
//                List<String>  cheapList = todayZtStock.stream()
//                        .filter(po->po.getCombo()== finalI)
//                        .filter(po->po.getInstructions().contains("低价"))
//                         .baseStockMonitorMap(ZtReport::getStockName)
//                        .collect(Collectors.toList());
//                data.put("CHEAP_"+i, cheapList);
                //名字
                if (StringUtils.isNotBlank(stockName)) {
                    List<String> stockNameList = todayZtStock.stream()
                            .filter(po -> po.getCombo() == finalI)
                            .filter(po -> po.getStockName().contains(stockName))
                            .map(ZtReport::getStockName)
                            .collect(Collectors.toList());
                    data.put("STOCK_NAME", stockName);
                    data.put("NAME_" + i, stockNameList);
                } else {
                    data.put("STOCK_NAME", "");
                    data.put("NAME_" + i, "");
                }

                //次新
                List<String> zzList = todayZtStock.stream()
                        .filter(po -> po.getCombo() == finalI)
                        .filter(po -> StringUtils.isNotBlank(po.getBond()))
                        .map(ZtReport::getStockName)
                        .collect(Collectors.toList());
                data.put("ZZ_" + i, zzList);
                //低价
//                data.put("Cheap_"+i, cheapList);
                //极端走势

                //20cm套利
                List<String> stock20List = todayZtStock.stream()
                        .filter(po -> po.getCombo() == finalI)
                        .filter(po -> !po.getPlate().equals("主板"))
                        .map(ZtReport::getStockName)
                        .collect(Collectors.toList());
                data.put("20_" + i, stock20List);
            }

            //小黑屋数据
            //查询所有当前日期之后的数据
            List<BaseStockMonitor> baseStockMonitorList = baseStockMonitorService.getStockMonitorListByDate(dealDateStr);
            Map<Date, List<BaseStockMonitor>> baseStockMonitorMap = baseStockMonitorList.stream().collect(Collectors.groupingBy(BaseStockMonitor::getMonitorEnd));
            Map<Integer, String> finalBaseStockMonitorMap = Maps.newHashMap();

            //分组排序成对应的日期格式 2-xxx()
            for (Date end : baseStockMonitorMap.keySet()) {
                int count = confDateService.queryTypeDayLimit(dealDate, end, DateConstant.DEAL_LIST);

                List<BaseStockMonitor> list = baseStockMonitorMap.get(end);
                StringBuffer sb = new StringBuffer();
                for (BaseStockMonitor bsm : list) {
                    sb.append(bsm.getStockName() + "(" + bsm.getBusiness() + ")");
                }
                finalBaseStockMonitorMap.put(count, sb.toString());
            }
            LinkedHashMap<Integer, String> sortReverseOrderMap = finalBaseStockMonitorMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            data.put("MONITOR", sortReverseOrderMap.toString());

            //题材高标 ：查询2个月内的高标信息 6以上的高标
            //展示样式 2023-11-7-圣龙股份（14）汽车| 2023-11-7-圣龙股份（14）汽车|
            Integer checkCombo = 6;
            List<BaseZtStock> highStockList = baseZtStockService.queryHighStock(DateUtil.getDayDiff(dealDateStr, -40), dealDate, checkCombo);
            StringBuffer highStock = new StringBuffer();
            while (highStockList.size() > 15) {
                Integer finalCheckCombo = checkCombo++;
                highStockList = highStockList.stream().filter(p -> p.getCombo() >= finalCheckCombo).collect(Collectors.toList());
            }
            for (BaseZtStock st : highStockList) {
                highStock.append(DateUtil.format(st.getCreateDate(), DateConstant.DATE_FORMAT_10) + "-" + st.getStockName()
                        + "(" + st.getCombo() + ")" + st.getMainBusiness() + "        ");
            }

            data.put("HIGH_STOCK", highStock);

            //异动预警


            //TODO 活跃板块
            //TODO 百日新高
            //TODO 新股
            //TODO 新债
            //TODO 活跃板块
            //TODO 过去、现在、未来


            URL a5 = ClassLoader.getSystemResource("templates/ComboTemplate.xlsx");
            TemplateExportParams params = new TemplateExportParams(a5.getPath(), true);

            // 简单模板导出方法
            Workbook book = ExcelExportUtil.exportExcel(params, data);
            ExcelUtil.exportExel(response, fileName, book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("导出成功");
    }

    @ApiOperationSupport(order = 32)
    @GetMapping("/exportModelReport")
    @ApiOperation("3-2 模式报表导出")
    public void exportModelReport(@RequestParam(value = "date", required = false) String date,
                                  HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("3-2 模式报表导出 {}", date);
        String fileName = "MODEL-";
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

        List<ModelReport> list = relationConfService.exportModelReport(date);

        ExcelUtil<ModelReport> excelUtil = new ExcelUtil<>(ModelReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprModelReport(list, date);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
    }


    @ApiOperationSupport(order = 101)
    @GetMapping("/exportZtRePort/fix")
    @ApiOperation("9-1 涨停修正报表导出")
    public void exportFixZtRePort(@RequestParam(value = "date", required = false) String date,
                                  @RequestParam(value = "outNameList", required = false) String outNameList,
                                  @RequestParam(value = "outMainBusiness", required = false) String outMainBusiness,
                                  HttpServletResponse response) throws IOException {
        log.info("9-1 涨停修正报表导出 {}", date);
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

        //获取数据
        List<ZtReport> list = reportService.getZtReportByDate(date);
        List<FixZtReport> fixList = reportService.getFixZtReports(outNameList, outMainBusiness, list);

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(fixList, FixZtReport.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, FixZtReport.class, fixList);
        ExcelUtil.exportExel(response, "FIX-ZT", workbook);
    }

    @ApiOperationSupport(order = 102)
    @PostMapping("/inputZtRePort/fix")
    @ApiOperation("9-2 涨停修正报表导入")
    public RespBean inputFixZtRePort(@RequestPart MultipartFile multipartFile) {
        log.info("9-2 涨停修正报表导入");
        try {
            reportService.inputFixZtRePort(multipartFile.getInputStream());
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 1)
    @GetMapping("/aaaaa")
    @ApiOperation("aaaaaaaaaaaaaaa")
    public void aaaaa(@RequestParam(value = "date", required = false) String date,
                      HttpServletResponse response) {
        String dealDateStr = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
        Date dealDate = DateUtil.format(dealDateStr, DateConstant.DATE_FORMAT_10);
        Map<String, Object> data = new HashMap<String, Object>();

        //基础数据
        getBasicData(dealDate, data);
        //连板数据
        getEchelonComboData(dealDateStr, data);
        //消息类数据
        getNewsData(dealDateStr, dealDate, data);

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
        // 开始生成数据
        // 获取导出excel指定模版，第二个参数true代表显示一个Excel中的所有 sheet
        URL a5 = ClassLoader.getSystemResource("templates/aaaa.xlsx");
        TemplateExportParams params = new TemplateExportParams(a5.getPath(), true);

        //图片
//        URL url = ClassLoader.getSystemResource("templates/Screenshot.jpg");
//        String path = url.getPath();
//        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(path));
//        ImageEntity image = ExcelUtil.imageToBytes(bufferedImage);
//        data.put("image", image);
        try {
            // 简单模板导出方法
            Workbook book = ExcelExportUtil.exportExcel(params, data);
            ExcelUtil.exportExel(response, "abcd", book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("导出成功");
    }

    public void getBasicData(Date dealDate, Map<String, Object> data) {
        Map<String, String> BASIC_MAP = Maps.newHashMap();
        ConfDate confDate = confDateService.queryBaseDateBydate(dealDate);
        data.put("BASIC_DATE", confDate);
        //距离最近的假期，长假倒计时（距离大于4天及以上的假期，还有几天，名称是什么）
        Date nextRest = confDateService.getAfterTypeDate(dealDate, DateConstant.REST_LIST);
        int countDownShort = DateUtil.getIntervalOfDays(dealDate, nextRest);
        BASIC_MAP.put("BASIC_COUNT_DOWN_SHORT", "还有" + countDownShort + "天休息");
        Date nextHoliday = confDateService.getAfterTypeDate(dealDate, DateConstant.HOLIDAY_LIST);
        if (nextHoliday == null) {
            BASIC_MAP.put("BASIC_COUNT_DOWN_LONG", "今年没有节日了");
        } else {
            String holidayDateDetail = confDateService.queryDateDetail(nextHoliday);
            BASIC_MAP.put("BASIC_COUNT_DOWN_LONG", holidayDateDetail);
        }
        Date nextDealDay = confDateService.getAfterTypeDate(dealDate, DateConstant.DEAL_LIST);
        String dealDateDetail = confDateService.queryDateDetail(nextDealDay);
        BASIC_MAP.put("BASIC_NEXT_DEAL_DATE", dealDateDetail);
        data.putAll(BASIC_MAP);
    }

    public void getNewsData(String dealDateStr, Date dealDate, Map<String, Object> data) {
        Map<String, String> NEWS_MAP = Maps.newHashMap();
        //周期15天内的利好消息频率  展示效果：汽车(5),数字经济（6）
        // 一天内多个时，算1个。取别名时归类为一个，比如光模块、CPO则算一个。
        Date endDate = DateUtil.getNextDay(DateUtil.parseDate(dealDateStr), 15);
        String endDateStr = DateUtil.format(endDate, DateConstant.DATE_FORMAT_10);
        List<NewsReport> newsList = baseDateNewsService.getNews(dealDateStr, endDateStr);
        Map<Date, List<NewsReport>> news15Map = newsList.stream().collect(Collectors.groupingBy(NewsReport::getDate));
        //获取别名的映射关系
        List<ConfBusiness> aliasList = confBusinessService.getAliasRelation();
        Map<String, String> aliasMap = aliasList.stream().collect(Collectors.toMap(ConfBusiness::getAlias, ConfBusiness::getBusName, (item1, item2) -> item2));
        StringBuffer sb = new StringBuffer();
        for (Date d : news15Map.keySet()) {
            List<NewsReport> tempList = news15Map.get(d);
            String mainBusiness = tempList.stream().map(po -> {
                String str = po.getMainBusiness();
                StringBuffer sbTemp = new StringBuffer();
                if (StringUtils.isBlank(str)) {
                    return sbTemp;
                }
                List<String> list = Lists.newArrayList(str.split(","));
                for (String s : list) {
                    String result = aliasMap.get(s);
                    if (result == null) {
                        result = s;
                    }
                    sbTemp.append(result + ",");
                }
                return sbTemp;
            }).distinct().collect(Collectors.joining(","));
            sb.append(mainBusiness);
        }
        List<String> businessList = Lists.newArrayList(sb.toString().split(","));
        Map<String, Integer> businessMap = CollectionUtils.getCardinalityMap(businessList);
        businessMap.remove("");
        LinkedHashMap<String, Integer> sortReverseOrderMap = (LinkedHashMap<String, Integer>) sortMapByValues(businessMap);
//        Map<String, Long> businessMap = businessList.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()));
//        log.info("businessMap:{}", businessMap);

        //key:频率,value:多个板块
        MultiValueMap<Integer, String> MultiValueNewsMap = new LinkedMultiValueMap<>();
        for (String str : sortReverseOrderMap.keySet()) {
            int count = businessMap.get(str);
            MultiValueNewsMap.add(count, str);
        }

        //打印所有值,长度最多15
        int count = 15;
        Map<Integer, List<String>> newsFrequencyMap = Maps.newLinkedHashMap();
        Set<Integer> keySet = MultiValueNewsMap.keySet();
        for (int key : keySet) {
            List<String> values = MultiValueNewsMap.get(key);
            if (count - values.size() < 0) {
                break;
            } else {
                newsFrequencyMap.put(key, values);
                count = count - values.size();
            }
        }
        log.info("newsFrequencyMap:{}", newsFrequencyMap.toString());
        NEWS_MAP.put("NEWS_FREQUENCY", newsFrequencyMap.toString());


        //未来最近的3个月的重要信息,
        Date startWeek = DateUtil.getNextDay(dealDate, -1);
        Date endWeek = DateUtil.getNextDay(dealDate, 7);
        List<NewsReport> weekMsgs = baseDateNewsService.getNews(startWeek, endWeek);
        data.put("NEWS_WEEK_NEWS", weekMsgs);

        //未来最近的3个月的重要信息,
        Date threeM = DateUtil.getNextDay(dealDate, 90);
        List<NewsReport> list = baseDateNewsService.getNews(dealDate, threeM);
        List<NewsReport> importantNews = list.stream().filter(po -> po.getImportant() > 0).collect(Collectors.toList());
        data.put("NEWS_IMPORTANT_NEWS", importantNews);
        data.putAll(NEWS_MAP);
    }

    public BaseCombo getEchelonComboData(String dealDateStr, Map<String, Object> data) {
        Map<String, String> ECHELON_COMBO_MAP = Maps.newHashMap();
        //获取基础数据，用于后续的数据生成
        List<ZtReport> list1 = baseZtStockService.getZtReportByDate(dealDateStr);
        List<ZtReport> list2 = baseZthfStockService.getZtReportByDate(dealDateStr);
        List<ZtReport> list = Lists.newArrayList();
        list.addAll(list1);
        list.addAll(list2);
        if (CollectionUtils.isEmpty(list)) {
            log.info("没有检索到对应的信息");
            return null;
        }

        //板块-数量map
        Map<String, Long> businessCountMap = list.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness, Collectors.counting()));

        //将连板梯队按照各自的梯队排好 注意5板以及以上放一块，板块内部根据主业分类，这里还要找到最高板
        Map<Integer, List<ZtReport>> comboMap = list.stream().collect(Collectors.groupingBy(ZtReport::getCombo));
        ECHELON_COMBO_MAP.put("COMBO_SIZE", "(" + list.size() + ")");
        ECHELON_COMBO_MAP.put("COMBO_ZT_SIZE", "(" + list1.size() + ")");
        ECHELON_COMBO_MAP.put("COMBO_ZTHF_SIZE", "(" + list2.size() + ")");

        //根据梯队分类，内部根据主业二次分类，value值展示为[股票(支业-说明),股票2(支业-说明)]。
        // 最终结果为：主业1：[股票(支业-说明),股票2(支业-说明)]|主业2：[股票(支业-说明),股票2(支业-说明)]
        int maxCombo = -1;
        List<ZtReport> then5List = Lists.newArrayList();
        for (Integer combo : comboMap.keySet()) {
            List<ZtReport> ztList = comboMap.get(combo);

            //最高板逻辑
            if (maxCombo < combo) {
                maxCombo = combo;
            }

            //连板明细-5以内
            if (combo >= 5) {
                then5List.addAll(ztList);
            } else {
                //连板数
                StringBuffer sb = new StringBuffer();
                getEchelonConect(ztList, sb, businessCountMap);
                ECHELON_COMBO_MAP.put("COMBO_SIZE_" + combo, "(" + ztList.size() + ")");
                ECHELON_COMBO_MAP.put("COMBO_" + combo, sb.toString());
            }
        }

        //连板明细-5以外
        StringBuffer sb = new StringBuffer("");
        getEchelonConectThen5(then5List, sb, businessCountMap);
        ECHELON_COMBO_MAP.put("COMBO_" + 5, sb.toString());

        //
        ECHELON_COMBO_MAP.put("COMBO_MAX", "(" + maxCombo + ")");
        int finalMaxCombo = maxCombo;
        String maxInfo = list.stream().filter(po -> po.getCombo() == finalMaxCombo).map(ZtReport::getStockName).collect(Collectors.joining(","));
        ECHELON_COMBO_MAP.put("COMBO_MAX_INFO", "(" + maxInfo + ")");

        //5b及以上连板数
        List<ZtReport> combo5List = list.stream().filter(po -> po.getCombo() >= 5).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(combo5List)) {
            ECHELON_COMBO_MAP.put("COMBO_SIZE_5", "-高度(" + combo5List.size() + ")");
        }
        data.putAll(ECHELON_COMBO_MAP);

        BaseCombo bc = new BaseCombo();
        bc.setDate(DateUtil.parseDate(dealDateStr));
        bc.setCombo1(ECHELON_COMBO_MAP.get("COMBO_1"));
        bc.setCombo2(ECHELON_COMBO_MAP.get("COMBO_2"));
        bc.setCombo3(ECHELON_COMBO_MAP.get("COMBO_3"));
        bc.setCombo4(ECHELON_COMBO_MAP.get("COMBO_4"));
        bc.setCombo5(ECHELON_COMBO_MAP.get("COMBO_5"));
        String comboSize1 = ECHELON_COMBO_MAP.get("COMBO_SIZE_1");
        String comboSize2 = ECHELON_COMBO_MAP.get("COMBO_SIZE_2");
        String comboSize3 = ECHELON_COMBO_MAP.get("COMBO_SIZE_3");
        String comboSize4 = ECHELON_COMBO_MAP.get("COMBO_SIZE_4");
        String comboSize5 = ECHELON_COMBO_MAP.get("COMBO_SIZE_5");
        if (StringUtils.isNotBlank(comboSize1)) {
            bc.setCombo1Count(Integer.parseInt(comboSize1.replace("(", "").replace(")", "")));
            if (bc.getCombo1().length() > 1100) {
                bc.setCombo1(null);
            }
        }
        if (StringUtils.isNotBlank(comboSize2)) {
            bc.setCombo2Count(Integer.parseInt(comboSize2.replace("(", "").replace(")", "")));
            if (bc.getCombo2().length() > 600) {
                bc.setCombo2(null);
            }
        }
        if (StringUtils.isNotBlank(comboSize3)) {
            bc.setCombo3Count(Integer.parseInt(comboSize3.replace("(", "").replace(")", "")));
            if (bc.getCombo3().length() > 200) {
                bc.setCombo3(null);
            }
        }
        if (StringUtils.isNotBlank(comboSize4)) {
            bc.setCombo4Count(Integer.parseInt(comboSize4.replace("(", "").replace(")", "")));
            if (bc.getCombo4().length() > 200) {
                bc.setCombo4(null);
            }
        }
        if (StringUtils.isNotBlank(comboSize5)) {
            bc.setCombo5Count(Integer.parseInt(comboSize5.replace("-高度(", "").replace(")", "")));
            if (bc.getCombo5().length() > 200) {
                bc.setCombo5(null);
            }
        }
        bc.setSumCount(list.size());
        bc.setZtCount(list1.size());
        bc.setZthfCount(list2.size());
        bc.setMaxCombo(maxCombo);
        bc.setMaxInfo(maxInfo);

        baseComboService.insertOrUpdate(bc);
        return bc;
    }

    public <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }

    public <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValuesReverseOrder(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> p1.getValue().compareTo(p2.getValue()))
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }


    public static Object getKey(Map map, Object value) {
        Set set = map.entrySet(); //通过entrySet()方法把map中的每个键值对变成对应成Set集合中的一个对象
        Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
        ArrayList<Object> arrayList = new ArrayList();
        while (iterator.hasNext()) {
            //Map.Entry是一种类型，指向map中的一个键值对组成的对象
            Map.Entry<Object, Object> entry = iterator.next();
            if (entry.getValue().equals(value)) {
                arrayList.add(entry.getKey());
            }
        }
        return arrayList;
    }

    /**
     * 当前板块数量排序
     * 效果：
     * 锂电池(24-3)[胜利精密, 深圳新星, 光华科技]
     * 军工(1-1)[奥维通信]
     *
     * @param ztList
     * @param sb
     * @param businessCountMap
     */
    public void getEchelonConect(List<ZtReport> ztList, StringBuffer sb, Map<String, Long> businessCountMap) {
        Map<String, List<ZtReport>> insideMap = ztList.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        Comparator<Integer> c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        Map<Integer, String> treeMap = new TreeMap<>(c);

        //处理数据
        for (String mb : insideMap.keySet()) {
            List<ZtReport> mbList = insideMap.get(mb);
            List<String> tempList = mbList.stream().map(po -> po.getStockName()).collect(Collectors.toList());
            //存入treeMap
            String treeValue = treeMap.get(tempList.size());
            if (StringUtils.isNotBlank(treeValue)) {
                treeValue = treeValue + mb.replace("最-", "") + "(" + businessCountMap.get(mb) + "-" + tempList.size() + ")" + tempList + "\n";
                treeMap.put(tempList.size(), treeValue);
            } else {
                treeMap.put(tempList.size(), mb.replace("最-", "") + "(" + businessCountMap.get(mb) + "-" + tempList.size() + ")" + tempList + "\n");
            }
        }

        //输出排序结果
        for (Integer i : treeMap.keySet()) {
            String str = treeMap.get(i);
            sb.append(str);
        }
    }

    /**
     * 9-{机器人(14-2)[日发精机, 五洲新春],消费(1-1)[一鸣食品]}
     * 1-{[AI(1-1)[泰尔股份]}
     */
    public void getEchelonConectThen5(List<ZtReport> ztList, StringBuffer sb, Map<String, Long> businessCountMap) {
        Map<Integer, List<ZtReport>> insideMap = ztList.stream().collect(Collectors.groupingBy(ZtReport::getCombo));
        Comparator<Integer> c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        Map<Integer, String> treeMap = new TreeMap<>(c);


        for (Integer combo : insideMap.keySet()) {
            List<ZtReport> mbList = insideMap.get(combo);
            Map<String, List<ZtReport>> tempList = mbList.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
            //存入treeMap
            StringBuffer sbTemp = new StringBuffer(combo + "-{");
            for (String mb : tempList.keySet()) {
                List<ZtReport> list = tempList.get(mb);
                sbTemp.append(mb.replace("最-", "") + "(" + businessCountMap.get(mb) + "-" + list.size() + ")" + list.stream().map(po -> po.getStockName()).collect(Collectors.toList()) + "|");
            }
            treeMap.put(combo, sbTemp.append("}\n").toString());
        }

        //输出排序结果
        for (Integer i : treeMap.keySet()) {
            String str = treeMap.get(i);
            sb.append(str);
        }
    }


}


//        MapUtils 常用操作java.util.Map 和 java.util.SortedMap。
//        常用方法有：
//        isNotEmpty ( ) 是否不为空
//        isEmpty ( ) 是否为空
//        putAll ( ) 添加所有元素
//        getString ( ) 获取String类型的值
//        getObject ( ) 获取Object类型的值
//        getInteger ( )获取Integer类型的值
//        get*** ( ) 类似上面的
//        EMPTY_MAP 获取一个不可修改的空类型Map
//        unmodifiableMap 获取一个不可以修改的Map（不能新增或删除）
//        unmodifiableSortedMap 获取一个不可以修改的有序的Map（不能新增或删除）
//        fixedSizeMap 获取一个固定长度的map
//        multiValueMap 获取一个多值的map(即一个key可以对应多个value值)
//                invertMap 返回一个key与value对调的map
//        predicatedMap() 返回一个满足predicate条件的map
//        lazyMap 返回一个lazy的map（值在需要的时候可以创建）