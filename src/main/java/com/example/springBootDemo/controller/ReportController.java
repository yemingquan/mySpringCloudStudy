package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.entity.BaseDate;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.input.BaseSubjectDetail;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    private ConfStockService confStockService;
    @Resource
    private BaseBondService baseBondService;
    @Resource
    private ConfBusinessService confBusinessService;

    @ApiOperationSupport(order = 1)
    @GetMapping("/exportBKReport")
    @ApiOperation("1-0 板块报表导出")
    public void exportBKReport(@RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "clearFlag", required = false) String clearFlag,
                               HttpServletResponse response) {
        log.info("1-0 板块报表导出 date：{} startDate：{} clearFlag:{}", date, startDate, clearFlag);

        long start;
        String fileName = "板块0000";
        date = baseDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

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
        String fileName = "涨停1111";
        date = baseDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

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
        String fileName = "摸板222";
        date = baseDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);

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
        String fileName = "波动333";
        date = baseDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
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
        String fileName = "消息";
        date = baseDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
        if (StringUtils.isNotBlank(startDate)) {
            startDate = baseDateService.getBeforeTypeDate(startDate, DateConstant.DEAL_LIST);
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
            log.info("次新更新");
            confCxStockService.imporCX();
            log.info("可转债");
            baseBondService.imporKZZ();
            log.info("增量刷新股票的主业");
            confStockService.reflshMyStock();
            log.info("日期功能刷新");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
//        List<NewsReport> list = baseDateNewsService.getNews(date);
//
//        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
//        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
//        excelUtil.exportCustomExcel(annotationMapping, list, fileName, response);
    }


    @ApiOperationSupport(order = 1)
    @GetMapping("/aaaaa")
    @ApiOperation("aaaaaaaaaaaaaaa")
    public void aaaaa(@RequestParam(value = "date", required = false) String date,
                      HttpServletResponse response) {
        String dealDateStr = baseDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
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
            ExcelUtil.exportExel(response,"abcd",book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("导出成功");
    }

    public void getBasicData(Date dealDate, Map<String, Object> data) {
        Map<String, String> BASIC_MAP = Maps.newHashMap();
        BaseDate baseDate = baseDateService.queryBaseDateBydate(dealDate);
        data.put("BASIC_DATE", baseDate);
        //距离最近的假期，长假倒计时（距离大于4天及以上的假期，还有几天，名称是什么）
        Date nextRest = baseDateService.getAfterTypeDate(dealDate, DateConstant.REST_LIST);
        int countDownShort = DateUtil.getIntervalOfDays(dealDate, nextRest);
        BASIC_MAP.put("BASIC_COUNT_DOWN_SHORT", "还有" + countDownShort + "天休息");
        Date nextHoliday = baseDateService.getAfterTypeDate(dealDate, DateConstant.HOLIDAY_LIST);
        String holidayDateDetail = baseDateService.queryDateDetail(nextHoliday);
        BASIC_MAP.put("BASIC_COUNT_DOWN_LONG", holidayDateDetail);
        Date nextDealDay = baseDateService.getAfterTypeDate(dealDate, DateConstant.DEAL_LIST);
        String dealDateDetail  = baseDateService.queryDateDetail(nextDealDay);
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

    public void getEchelonComboData(String dealDateStr, Map<String, Object> data) {
        Map<String, String> ECHELON_COMBO_MAP = Maps.newHashMap();
        //获取基础数据，用于后续的数据生成
        List<ZtReport> list1 = reportService.getZtReportByDate(dealDateStr);
        //将连板梯队按照各自的梯队排好 注意5板以及以上放一块，板块内部根据主业分类，这里还要找到最高板
        Map<Integer, List<ZtReport>> comboMap = list1.stream().collect(Collectors.groupingBy(ZtReport::getCombo));
        ECHELON_COMBO_MAP.put("COMBO_SIZE", "(" + list1.size() + ")");

        //根据梯队分类，内部根据主业二次分类，value值展示为[股票(支业-说明),股票2(支业-说明)]。
        // 最终结果为：主业1：[股票(支业-说明),股票2(支业-说明)]|主业2：[股票(支业-说明),股票2(支业-说明)]
        int maxCombo = -1;
        for (Integer combo : comboMap.keySet()) {
            List<ZtReport> ztList = comboMap.get(combo);

            //最高板逻辑
            if (maxCombo < combo) {
                maxCombo = combo;
            }

            //连板明细
            StringBuffer sb = new StringBuffer();
            if (combo >= 5) {
                getEchelonConectThen5(ztList, sb);
                String str = ECHELON_COMBO_MAP.get("COMBO_" + 5);
                if (StringUtils.isEmpty(str)) {
                    ECHELON_COMBO_MAP.put("COMBO_" + 5, sb.toString());
                } else {
                    str = str + sb.toString();
                    ECHELON_COMBO_MAP.put("COMBO_" + 5, str);
                }
            } else {
                //连板数
                getEchelonConect(ztList, sb);
                ECHELON_COMBO_MAP.put("COMBO_SIZE_" + combo, "(" + ztList.size() + ")");
                ECHELON_COMBO_MAP.put("COMBO_" + combo, sb.toString());
            }
        }
        ECHELON_COMBO_MAP.put("COMBO_MAX", "(" + maxCombo + ")");
        //5b及以上连板数
        List<ZtReport> combo5List = list1.stream().filter(po -> po.getCombo() >= 5).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(combo5List)) {
            ECHELON_COMBO_MAP.put("COMBO_SIZE_5", "-高度(" + combo5List.size() + ")");
        }
        data.putAll(ECHELON_COMBO_MAP);
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


    public void getEchelonConect(List<ZtReport> ztList, StringBuffer sb) {
        Map<String, List<ZtReport>> insideMap = ztList.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        for (String mb : insideMap.keySet()) {
            List<ZtReport> mbList = insideMap.get(mb);
            List<String> tempList = mbList.stream().map(po -> po.getStockName()).collect(Collectors.toList());
            sb.append(mb.replace("最-", "") + "(" + tempList.size() + ")" + tempList + "\n");
        }
    }

    public void getEchelonConectThen5(List<ZtReport> ztList, StringBuffer sb) {
        Map<String, List<ZtReport>> insideMap = ztList.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        for (String mb : insideMap.keySet()) {
            List<ZtReport> mbList = insideMap.get(mb);
            List<String> tempList = mbList.stream().map(po -> po.getStockName() + "-" + po.getCombo()).collect(Collectors.toList());
            sb.append(mb.replace("最-", "") + "(" + tempList.size() + ")" + tempList + "\n");
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