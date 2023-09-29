package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.ConfMySotck;
import com.example.springBootDemo.entity.game.ShortNameDto;
import com.example.springBootDemo.service.ConfMySotckService;
import com.example.springBootDemo.util.PingYinUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
    private ConfMySotckService confMySotckService;

    @ApiOperationSupport(order = 1)
    @GetMapping("/shortNameStatistics")
    @ApiOperation("1-1 缩写统计")
    public void shortNameStatistics(HttpServletResponse response) throws Exception {
        String fileName = "ShortNameDto.xls";
        String sheetName = "缩写统计";

        List<ConfMySotck> msList = confMySotckService.selectList(new EntityWrapper<>());
        List<ShortNameDto> dtoList = BeanUtil.copyToList(msList, ShortNameDto.class);
        for (ShortNameDto dto : dtoList) {
            String stockName = dto.getStockName();
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
        resultList = resultList.stream().sorted(Comparator.comparing(ShortNameDto::getCount)).collect(Collectors.toList());
        //生成excel文档
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(sheetName, "sheet"),
                ShortNameDto.class, resultList);

        fileName = new String(fileName.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream outputStream = response.getOutputStream();
        response.flushBuffer();
        workbook.write(outputStream);
        // 写完数据关闭流
        outputStream.close();
    }

    @ApiOperationSupport(order = 1)
    @GetMapping("/test")
    @ApiOperation("1-0 测试接口")
    public void test(HttpServletResponse response) throws Exception {
//        log.info("1-1{}", 'Ａ' + 1);
//        log.info("1-1{}", ('Ａ' + 1 < 128));
//
//        for (int i = 65313; ; i++) {
//            char c = (char) i;
//            log.info("char:{},字符:{}", i, c);
//        }
    }

}

