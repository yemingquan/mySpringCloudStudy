package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.BaseStock;
import com.example.springBootDemo.entity.BaseStockMonitor;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.entity.game.ShortNameDto;
import com.example.springBootDemo.entity.input.StockDiary;
import com.example.springBootDemo.entity.result.ResultstockExcavate;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.PingYinUtils;
import com.example.springBootDemo.util.StockUtil;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
@Api(tags = "休闲模块")
@RequestMapping("game")
@RestController
public class GameController {

    @Resource
    private ConfStockService confStockService;
    @Resource
    private BaseStockService baseStockService;
    @Resource
    private BaseSubjectLineDetailService baseSubjectLineDetailService;
    @Autowired
    ConfDateService confDateService;
    @Autowired
    BaseStockMonitorService baseStockMonitorService;
    @Autowired
    BaseZtStockService baseZtStockService;

    @ApiOperationSupport(order = 1)
    @PostMapping("/inputStockDiary")
    @ApiOperation("0-1 写股票日记")
    public RespBean inputStockDiary(@RequestPart(required = false) MultipartFile multipartFile) throws Exception {

        List<StockDiary> stockDiaryList = ExcelUtil.excelToList(multipartFile, StockDiary.class);

        for (StockDiary dto : stockDiaryList) {
            try {
                EntityWrapper<BaseStock> wrapper = new EntityWrapper<>();
                wrapper.eq("create_Date", dto.getCreateDate());
                if (StringUtils.isNotBlank(dto.getStockCode())) {
                    wrapper.like("stock_code", dto.getStockCode());
                } else {
                    wrapper.eq("stock_name", dto.getStockName());
                }

                BaseStock bs = BaseStock.builder()
                        .instructions(dto.getInstructions())
                        .modifedDate(new Date())
                        .modifedBy("股票日记")
                        .build();
                baseStockService.update(bs, wrapper);
            } catch (Exception e) {
                log.info("{} 数据异常", dto);
                log.error("数据修改失败:", e);
            }
        }
        return RespBean.success("修改成功");
    }


    @ApiOperationSupport(order = 2)
    @PostMapping("/queryStockDiary")
    @ApiOperation("0-2 翻阅股票日记")
    public void queryStockDiary(HttpServletResponse response) throws Exception {
        /**
         * 可选，第一行：日期 市场动态
         * 可选，单个标的的日期（不选择市场动态时，会跳过部分日期，有说明就展示）
         * 可选，板块日记。第二行板块日记。然后依次展示板块内 带说明的标 的日记
         */


    }


    @ApiOperationSupport(order = 11)
    @GetMapping("/shortNameStatistics")
    @ApiOperation("1-1 缩写统计")
    public void shortNameStatistics(HttpServletResponse response) throws Exception {

        List<ConfStock> msList = confStockService.selectList(new EntityWrapper<>());
        List<ShortNameDto> dtoList = BeanUtil.copyToList(msList, ShortNameDto.class);
        for (ShortNameDto dto : dtoList) {
            String stockName = dto.getStockName();
            stockName = stockName.replaceAll("\\*", "").replaceAll(" ", "");
            dto.setShortName(PingYinUtils.toFirstChar(stockName).toUpperCase());
        }
        Map<String, List<ShortNameDto>> map = dtoList.stream().collect(Collectors.groupingBy(ShortNameDto::getShortName));

        List<ShortNameDto> resultList = Lists.newArrayList();
        for (String str : map.keySet()) {
            List<ShortNameDto> tempList = map.get(str);
            List<String> list = tempList.stream().map(dto -> dto.getStockName() + dto.getStockCode()).collect(Collectors.toList());
            ShortNameDto dto = ShortNameDto.builder().shortName(str).infoList(list).count(list.size()).build();
            resultList.add(dto);
        }
        resultList = resultList.stream().sorted(Comparator.comparing(ShortNameDto::getCount, Comparator.reverseOrder())).collect(Collectors.toList());

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(resultList, ShortNameDto.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ShortNameDto.class, resultList);
        ExcelUtil.exportExel(response, "SHORT_NAME", workbook);
    }

    @ApiOperationSupport(order = 12)
    @GetMapping("/getPingYin")
    @ApiOperation("1-2 拼音翻译")
    public RespBean getPingYin(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "code", required = false) String code) throws Exception {
        if (StringUtils.isNotBlank(code)) {
            EntityWrapper<ConfStock> wrapper = new EntityWrapper<>();
            wrapper.like("stock_code", code);
            ConfStock cs = confStockService.selectOne(wrapper);

            if (cs != null) {
                name = cs.getStockName();
            } else {
                return RespBean.error("未查询到对应数据");
            }
        }
        return RespBean.success(name + "----" + PingYinUtils.toPinyin(name));
    }


    @ApiOperationSupport(order = 21)
    @GetMapping("/stockExcavate")
    @ApiOperation("2-1 股票挖掘")
    public void stockExcavate(@RequestParam(value = "mainBusiness", required = false) String mainBusiness,
                              @RequestParam(value = "nicheBusiness", required = false) String nicheBusiness,
                              @RequestParam(value = "attr", required = false) String attr,
                              @RequestParam(value = "keyWord", required = false) String keyWord,
                              @RequestParam(value = "exportType", required = false) String exportType,
                              HttpServletResponse response) throws Exception {
        /**
         * 需要满足以下场景
         * 输入：
         *      行业：可叠加，分隔
         *      支业：非必填，加分项。不参与数据库检索，过滤加分项。
         *      属性：可叠加，分隔。一般是小盘、地区、次新、中字头。TODO 暂无：破发、国资委控股、
         *      姓名：可部分填写。TODO 没有配置字典
         *
         * 输出：
         *      1.输入以上任意一个条件时，可以展示对应的瀑布数据。排序方式是板块，流通市值
         *      2.输入多个行业或属性时，需要分类排序。并给出推荐结果
         *          默认分类是板块、流通盘。
         *          顺序横向/竖向流通盘排序
         */
        String fileName = "STOCK_EXCAVATE";
        List<String> mainBusinessList = Lists.newArrayList();
        List<String> nicheBusinessList = Lists.newArrayList();
        List<String> attrList = Lists.newArrayList();
        Map<String, Object> data = Maps.newHashMap();
        //TODO 暂时未使用
        int queryCount = 0;

        //行业
        if (StringUtils.isNotBlank(mainBusiness)) {
            mainBusiness = StockUtil.calibrateHalfAngle(mainBusiness);
            mainBusinessList = Lists.newArrayList(mainBusiness.split(","));
            data.put("QUERY_DATE_MAIN_BUSINESS", mainBusinessList);
            queryCount++;
        }

        //支业
        if (StringUtils.isNotBlank(nicheBusiness)) {
            nicheBusiness = StockUtil.calibrateHalfAngle(nicheBusiness);
            nicheBusinessList = Lists.newArrayList(nicheBusiness.split(","));
            data.put("QUERY_DATE_NICHE_BUSINESS", nicheBusinessList);
        }

        //属性
        if (StringUtils.isNotBlank(attr)) {
            attr = StockUtil.calibrateHalfAngle(attr);
            attrList = Lists.newArrayList(attr.split(","));
            data.put("QUERY_DATE_ATTR", attrList);
            queryCount++;
        }

        //关键词
        if (StringUtils.isNotBlank(keyWord)) {
            data.put("QUERY_DATE_KEYWORD", keyWord);
            queryCount++;
        }

        if (queryCount == 0) {
            log.info("未输入检索条件，不予查询");
            return;
        }

        //搜索结果
        List<ResultstockExcavate> msList = confStockService.queryStockExcavate(mainBusinessList, attrList, keyWord);

        //如果条件较少，则生成excel简单的文档
        if (StringUtils.isBlank(exportType)) {
            mainBusinessList.addAll(nicheBusinessList);
            //其余字段展示热点行业
            List<String> hotBusinessList = baseSubjectLineDetailService.getHotBusiness(-10);
            hotBusinessList.removeAll(mainBusinessList);

            //结果加工
            for (ResultstockExcavate re : msList) {
                //趋势分析
                Double currentPrice = re.getCurrentPrice();
                Double averagePrice5 = re.getAveragePrice5();
                Double averagePrice10 = re.getAveragePrice10();

                if (averagePrice5 != null && currentPrice.compareTo(averagePrice5) >= 0) {
                    re.setTendency("向上通道");
                } else if (averagePrice5 != null) {
                    //向下通道 5-10均线震荡
                    if (averagePrice5.compareTo(currentPrice) >= 0 && currentPrice.compareTo(averagePrice10) >= 0) {
                        re.setTendency("5-10均线震荡");
                    } else if (averagePrice10.compareTo(currentPrice) >= 0) {
                        //向下通道 破10日均线
                        re.setTendency("破10日均线");
                    }
                }

                //行业再加工，默认只展示搜索的+最近热点
                //如果设置了搜索了条件，则主业展示这部分，否则这个字段为空
                List<String> business1 = Lists.newArrayList(re.getMainBusiness().split(","));
                List<String> business2 = Lists.newArrayList(re.getNicheBusiness().split(","));
                business1.addAll(business2);
                //数据展示可以用加号
                List resultMainBusiness = (List) CollectionUtils.intersection(mainBusinessList, business1);
                re.setMainBusiness(String.join("+", resultMainBusiness));

                List resultNicheBusiness = (List) CollectionUtils.intersection(hotBusinessList, business1);
                re.setNicheBusiness(String.join("+", resultNicheBusiness));
            }

            ExportParams params = ExcelUtil.getSimpleExportParams(msList, ResultstockExcavate.class);
            Workbook workbook = ExcelExportUtil.exportExcel(params, ResultstockExcavate.class, msList);
            ExcelUtil.exportExel(response, fileName, workbook);
            return;
        }


        // Excel模板导出方法
        URL a5 = ClassLoader.getSystemResource("templates/ExcavateTemplate.xlsx");
        TemplateExportParams params = new TemplateExportParams(a5.getPath(), true);
        Workbook book = ExcelExportUtil.exportExcel(params, data);
        ExcelUtil.exportExel(response, fileName, book);
    }


    @ApiOperationSupport(order = 22)
    @GetMapping("/stockEnvy")
    @ApiOperation("2-2 补涨挖掘")
    public void stockEnvy(@RequestParam(value = "mainBusiness", required = false) String mainBusiness,
                          @RequestParam(value = "nicheBusiness", required = false) String nicheBusiness,
                          @RequestParam(value = "attr", required = false) String attr,
                          @RequestParam(value = "keyWord", required = false) String keyWord,
                          @RequestParam(value = "exportType", required = false) String exportType,
                          HttpServletResponse response) throws Exception {


    }

    @ApiOperationSupport(order = 31)
    @GetMapping("/alert")
    @ApiOperation("3-1 预警 测试")
    public RespBean alert(@RequestParam(value = "date", required = true) String date) throws Exception {
        /**
         * 预警和警告
         * 预警:提前预判警告的到来，从而做好准备
         * 警告：交易日前一天发送提醒信息
         *
         */

        /**
         * 简单的预警
         *

         *
         *  大票指数预警
         *  预警：3k+3%内时、警告：3k+2%
         *
         *  量能预警
         *  机构预警：预警：>9k、警告:>1w
         *  流动性危机预警：预警:<7k、警告:<6k 且 成交量比昨天低
         *
         *
         *
         */



        StringBuffer sb = new StringBuffer();
        Date today = DateUtil.format(date, DateConstant.DATE_FORMAT_10);

        /**
         * 节前效应
         * 周末博弈（消息小于3天时）：预警：倒数第三个交易日、警告：节前倒数第二个交易日（比如周四复盘时收到警告消息）
         * 节前效应（休息大于3天时）：预警：提前半个月发送、警告：最后一周开始时
         * 月末效应：预警：最后一周开始时、警告：月底前一天
         */
        //休息日预警
        restAlert(sb, today);
        //节假日预期
        holiDayAlert(sb, today);
        //月末效应
        monthAlert(sb, today);

        /**
         * 监管变动预警（含小黑屋）
         *  预警:加入监管池时、警告（持续）：剩下不到4个交易日时
         /
        //监管新增预警
        newStockMonitorAlert(date, sb);
        //即将出监管预警
        closeToRemoveStockMonitor(date, sb);
        //        TODO 补涨节点 &新周期博弈
//        List<BaseZtStock> highStockList = baseZtStockService.queryHighStock(DateUtil.getDayDiff(today, -40), today, 6);

         /
         *  特殊节点预警
         *  预警:业绩预披露前3天、警告：业绩披露开始前一个交易日
         */


        return RespBean.success(sb);
    }



    public void closeToRemoveStockMonitor(String date, StringBuffer sb) {
        //警告（持续）：剩下不到4个交易日时
        List<BaseStockMonitor> list2 = baseStockMonitorService.getCloseToRemoveStockMonitor(date);
        if(CollectionUtils.isNotEmpty(list2)){
            sb.append("即将出监管标的:");
            for (BaseStockMonitor bsm : list2){
                sb.append(bsm.getStockName()+"("+bsm.getBusiness()+")"+"\\n");
            }
        }
    }

    public void newStockMonitorAlert(String date, StringBuffer sb) {
        /**
         * 监管变动预警（含小黑屋）
         * 预警:加入监管池时、警告（持续）：剩下不到4个交易日时
         */
//        预警:加入监管池时、警告（持续）
        List<BaseStockMonitor> list1 = baseStockMonitorService.getNewStockMonitor(date);
        if(CollectionUtils.isNotEmpty(list1)){
            sb.append("新加入监管标的:");
            for (BaseStockMonitor bsm : list1){
                sb.append(bsm.getStockName()+"("+bsm.getBusiness()+")"+"\\n");
            }
        }
    }

    public void monthAlert(StringBuffer sb, Date today) throws Exception {
        //        月末效应：预警：最后一周开始时、警告：月底前一天
        //先计算月末节点
        Date[] monthDate = DateUtil.getMonthLimit(today);
        //在根据交易日往前倒推，得到时间节点
        Date monthDealdate = confDateService.getBeforeTypeDate(monthDate[1], DateConstant.DEAL_LIST);
        //判定当前时间节点是否为推送节点
        Long monthDateBetween = DateUtil.getDaysBetween(today, monthDealdate);
        if (2 < monthDateBetween && monthDateBetween < 5) {
            sb.append("月末效应：预警：最后一周开始时\\n");
        } else if (monthDateBetween < 2) {
            sb.append("月末效应：警告：月底前一天\\n");
        }
    }

    public void holiDayAlert(StringBuffer sb, Date today) {
        Date nextHoliDayDate = confDateService.getAfterTypeDate(today, DateConstant.HOLIDAY_LIST);
        Date restdateAfterHoliDay = confDateService.getAfterTypeDate(nextHoliDayDate, DateConstant.DEAL_LIST);
        //还差几天放假
        Long betweenHoliDayDate = DateUtil.getDaysBetween(today, nextHoliDayDate);
        // 休息几天
        Long holiDay = DateUtil.getDaysBetween(nextHoliDayDate, restdateAfterHoliDay);
        if(holiDay>3){
            //节前效应（休息大于3天时）：预警：提前半个月发送、警告：最后一周开始时
            if (7 < betweenHoliDayDate && betweenHoliDayDate <= 15) {
                sb.append("节前效应预警：提前半个月发送\\n");
            } else if (betweenHoliDayDate <= 7) {
                sb.append("节前效应警告：最后一周开始时\\n");
            }
        }
    }

    public void restAlert(StringBuffer sb, Date today) {
        Date nextRestDate = confDateService.getAfterTypeDate(today, DateConstant.REST_LIST);
        Date restdateAfterRest = confDateService.getAfterTypeDate(nextRestDate, DateConstant.DEAL_LIST);
        //还差几天放假
        Long betweenRestDate = DateUtil.getDaysBetween(today, nextRestDate);
        // 休息几天
        Long restDay = DateUtil.getDaysBetween(nextRestDate, restdateAfterRest);
        //休息日博弈（消息小于3天时）：预警：倒数第三个交易日、警告：节前倒数第二个交易日（比如周四复盘时收到警告消息）
        if (betweenRestDate == 3) {
            sb.append("休息日预警：倒数第三个交易日\\n");
        } else if (betweenRestDate == 2) {
            sb.append("休息日警告：节前倒数第二个交易日（比如周四复盘时收到警告消息）\\n");
        }
    }


    @ApiOperationSupport(order = 31)
    @GetMapping("/getQuestionAndAnswer")
    @ApiOperation("3-1 刷题")
    public RespBean getQuestionAndAnswer(HttpServletResponse response) throws Exception {
//        confStockService.reflshSmallStock(null);
//        confStockService.reflshCX();
        return RespBean.success("还没开发");
    }


}

