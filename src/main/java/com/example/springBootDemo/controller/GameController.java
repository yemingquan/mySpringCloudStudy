package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.entity.game.ShortNameDto;
import com.example.springBootDemo.entity.result.ResultstockExcavate;
import com.example.springBootDemo.service.BaseSubjectLineDetailService;
import com.example.springBootDemo.service.ConfStockService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Comparator;
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
@Api(tags = "娱乐模块")
@RequestMapping("game")
@RestController
public class GameController {

    @Resource
    private ConfStockService confStockService;

    @Resource
    private BaseSubjectLineDetailService baseSubjectLineDetailService;

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
                              HttpServletResponse response) throws Exception {
        /**
         * 需要满足以下场景
         * 输入：
         *      行业：可叠加，分隔
         *      支业：非必填，加分项。不参与数据库检索，过滤加分项。
         *      属性：可叠加，分隔。一般是小盘、地区、次新、中字头。TODO 暂无：破发、国资委控股、
         *      姓名：可部分填写。TODO 没有配置字典
         *
         *
         * 1.输入以上任意一个条件时，可以展示对应的瀑布数据。排序方式是板块，流通市值
         * 2.输入多个行业或属性时，需要分类排序。默认分类是行业、板块、流通盘，流通盘。顺序横向/竖向流通盘排序
         *
         *  注意：三者都可以单独搜索
         */
        String fileName = "STOCK_EXCAVATE";
        List<String> mainBusinessList = Lists.newArrayList();
        List<String> nicheBusinessList = Lists.newArrayList();
        List<String> attrList = Lists.newArrayList();
        Map<String, Object> data = Maps.newHashMap();
        int queryCount = 0;

        //行业
        if (StringUtils.isNotBlank(mainBusiness)) {
            mainBusiness = StockUtil.calibrateHalfAngle(mainBusiness);
            mainBusinessList = Lists.newArrayList(mainBusiness.split(","));
            data.put("QUERY_DATE_MAIN_BUSINESS", mainBusiness);
            queryCount++;
        }

        //行业
        if (StringUtils.isNotBlank(nicheBusiness)) {
            nicheBusiness = StockUtil.calibrateHalfAngle(nicheBusiness);
            nicheBusinessList = Lists.newArrayList(nicheBusiness.split(","));
            data.put("QUERY_DATE_NICHE_BUSINESS", nicheBusiness);
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
        if (queryCount == 1) {
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


    @ApiOperationSupport(order = 00)
    @GetMapping("/ssss")
    @ApiOperation("0-0 自定义问题")
    public RespBean ssss(HttpServletResponse response) throws Exception {
//        confStockService.reflshSmallStock(null);
        confStockService.reflshCX();
        return RespBean.success("还没开发");
    }


}

