package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.entity.game.ShortNameDto;
import com.example.springBootDemo.service.ConfStockService;
import com.example.springBootDemo.util.PingYinUtils;
import com.example.springBootDemo.util.StockUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
                              @RequestParam(value = "attr", required = false) String attr,
                              @RequestParam(value = "stockName", required = false) String stockName,
                              HttpServletResponse response) throws Exception {
        /**
         * 需要满足以下场景
         * 输入：
         *      行业：可叠加，分隔
         *      属性：可叠加，分隔。一般是小盘、地区、次新、中字头。TODO 暂无：破发、国资委控股、
         *      姓名：可部分填写。TODO 没有配置字典
         *
         *
         * 1.输入以上任意一个条件时，可以展示对应的瀑布数据
         * 2.输入多个行业或属性时，需要分类排序。默认分类是行业、板块、流通盘，流通盘。顺序横向/竖向流通盘排序
         *
         *  注意：三者都可以单独搜索
         */
        List<String> mainBusinessList = Lists.newArrayList();
        List<String> attrList = Lists.newArrayList();

        //行业
        if (StringUtils.isNotBlank(mainBusiness)) {
            mainBusiness = StockUtil.calibrateHalfAngle(mainBusiness);
            mainBusinessList = Lists.newArrayList(mainBusiness.split(","));
        }

        //属性
        if (StringUtils.isNotBlank(attr)) {
            attr = StockUtil.calibrateHalfAngle(attr);
            attrList = Lists.newArrayList(attr.split(","));
        }


        List<ConfStock> msList = confStockService.queryStockExcavate(mainBusinessList, attrList, stockName);
        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(msList, ConfStock.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ConfStock.class, msList);
        ExcelUtil.exportExel(response, "STOCK_EXCAVATE", workbook);
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

