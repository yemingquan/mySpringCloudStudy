package com.example.springBootDemo.service.impl.report;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.constant.StockConstant;
import com.example.springBootDemo.entity.*;
import com.example.springBootDemo.entity.base.BaseReportStock;
import com.example.springBootDemo.entity.dto.QueryStockDto;
import com.example.springBootDemo.entity.input.*;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.StockUtil;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-6 10:36
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Service
@Slf4j
public class InputServiceImpl implements InputService {

    @Autowired
    BaseStockService baseStockService;
    @Autowired
    BaseZtStockService baseZtStockService;
    @Autowired
    BaseZthfStockService baseZthfStockService;
    @Autowired
    BaseZbStockService baseZbStockService;
    @Autowired
    BaseDtStockService baseDtStockService;
    @Autowired
    BaseBdDownStockService baseBdDownStockService;
    @Autowired
    BaseBdUpStockService baseBdUpStockService;
    @Autowired
    BaseSubjectService baseSubjectService;
    @Autowired
    BaseSubjectLineService baseSubjectLineService;
    @Autowired
    BaseSubjectLineDetailService baseSubjectLineDetailService;
    @Autowired
    ConfStockService confStockService;
    @Autowired
    ConfDateService confDateService;

    @Override
    public boolean importExcelZthfStock(InputStream is) throws Exception {
        //导入前先删除当天的数据
        EntityWrapper<BaseZthfStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        baseZthfStockService.delete(wrapper);

        List<BaseZthfStock> list = ExcelUtil.excelToList(is, BaseZthfStock.class);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = baseSubjectLineDetailService.getHotBusiness(null);
        list.stream().forEach(po -> {
            //根据历史数据设置主业
            setMainBusinessList(mainBusinessList, po);
            datePro(po);
            try {
                ztInstructions(po);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return baseZthfStockService.insertBatch(list, list.size());
    }


    public void setMainBusinessList(List<String> mainBusinessList, BaseReportStock po) {
//        log.info("股票名称[{}],代码[{}]", po.getStockName(), po.getStockCode());
        EntityWrapper<ConfStock> wr = new EntityWrapper<>();
        wr.eq("STOCK_CODE", po.getStockCode());
        ConfStock conf = confStockService.selectOne(wr);
        if (conf == null) {
            confStockService.insert(ConfStock.builder().stockCode(po.getStockCode()).stockName(po.getStockName()).build());
            return;
        }
        String nowMainBusiness = conf.getMainBusiness();
        String nowNicheBusiness = conf.getNicheBusiness();
        nowMainBusiness = nowMainBusiness + "," + nowNicheBusiness;
        if (StringUtils.isNotEmpty(nowMainBusiness)) {
            List<String> oldMBList = Lists.newArrayList(nowMainBusiness.split(","));
            oldMBList = oldMBList.stream().distinct().collect(Collectors.toList());
            oldMBList.remove("");
            if (CollectionUtils.isNotEmpty(oldMBList)) {
                List<String> newMBList = Lists.newArrayList();
                int num = 999;
                for (String str : oldMBList) {
                    if (mainBusinessList.contains(str)) {
                        newMBList.add(str);
                        int tempNum = mainBusinessList.indexOf(str);
                        if (tempNum < num) {
                            num = tempNum;
                        }
                    }
                }
                if (num != 999) {
                    String mainBusiness = mainBusinessList.get(num);
                    newMBList.remove(mainBusiness);
                    po.setMainBusiness(mainBusiness);

                    if (CollectionUtils.isNotEmpty(newMBList)) {
                        String nicheBusiness = newMBList.stream().collect(Collectors.joining("+"));
                        po.setNicheBusiness(nicheBusiness);
                    }

                    po.setModifedDate(new Date());
                    log.info("++++股票名称[{}],代码[{}],系统设置主业为[{}],支业为[{}]", po.getStockName(), po.getStockCode(), mainBusiness, po.getNicheBusiness());
                }
            }
        }
    }


    @Override
    public boolean importExcelZtStock(InputStream is) throws Exception {
        //导入前先删除当天的数据
        EntityWrapper<BaseZtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        baseZtStockService.delete(wrapper);

        List<BaseZtStock> list = ExcelUtil.excelToList(is, BaseZtStock.class);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = baseSubjectLineDetailService.getHotBusiness(null);
        list.stream().forEach(po -> {
            //根据历史数据设置主业
            setMainBusinessList(mainBusinessList, po);
            datePro(po);
            try {
                ztInstructions(po);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return baseZtStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelBdUpStock(InputStream is) throws Exception {
        //导入前先删除当天的数据
        EntityWrapper<BaseBdUpStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        baseBdUpStockService.delete(wrapper);

        List<BaseBdUpStock> list = ExcelUtil.excelToList(is, BaseBdUpStock.class);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = baseSubjectLineDetailService.getHotBusiness(null);
        list.stream().forEach(po -> {
            //根据历史数据设置主业
            setMainBusinessList(mainBusinessList, po);
            datePro(po);
            //波动报表说明字段处理
            bdInstructions(po);
        });
        return baseBdUpStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelBdDownStock(InputStream is) throws Exception {
        //导入前先删除当天的数据
        EntityWrapper<BaseBdDownStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        baseBdDownStockService.delete(wrapper);

        List<BaseBdDownStock> list = ExcelUtil.excelToList(is, BaseBdDownStock.class);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = baseSubjectLineDetailService.getHotBusiness(null);
        list.stream().forEach(po -> {
            //根据历史数据设置主业
            setMainBusinessList(mainBusinessList, po);
            datePro(po);
            bdInstructions(po);
        });
        return baseBdDownStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelDtStock(InputStream is) throws Exception {
        //导入前先删除当天的数据
        EntityWrapper<BaseDtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        baseDtStockService.delete(wrapper);


        List<BaseDtStock> list = ExcelUtil.excelToList(is, BaseDtStock.class);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = baseSubjectLineDetailService.getHotBusiness(null);
        list.stream().forEach(po -> {
            //根据历史数据设置主业
            setMainBusinessList(mainBusinessList, po);
            datePro(po);
            mbInstructions(po);
            StringBuffer instructions = new StringBuffer(po.getInstructions());
            instructions.append("曾跌停;");
            po.setInstructions(instructions.toString());
        });
        if (list.size() == 0) {
            log.info("无数据需要处理");
            return true;
        }
        return baseDtStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelZbStock(InputStream is) throws Exception {
        //导入前先删除当天的数据
        EntityWrapper<BaseZbStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10));
        baseZbStockService.delete(wrapper);

        List<BaseZbStock> list = ExcelUtil.excelToList(is, BaseZbStock.class);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = baseSubjectLineDetailService.getHotBusiness(null);
        list.stream().forEach(po -> {
            //根据历史数据设置主业
            setMainBusinessList(mainBusinessList, po);
            datePro(po);
            mbInstructions(po);
            StringBuffer instructions = new StringBuffer(po.getInstructions());
            instructions.append("炸板;");
            po.setInstructions(instructions.toString());
        });
        return baseZbStockService.insertBatch(list, list.size());
    }

    @Override
    public void importStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "stock.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public void importExcelBdDownStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "6.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public void importExcelBdUpStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "5.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public void importExcelDtStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "4.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public void importExcelZbStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "3.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public void importExcelZthfStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "2.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public void importExcelZtStock(String thsBasePath) throws Exception {
        File file = new File(thsBasePath + "1.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
        importStock(new FileInputStream(tempFile));
    }

    @Override
    public boolean importStock(InputStream is) throws Exception {
        StopWatch stopWatch = new StopWatch();
        //导入前先删除当天的数据
        EntityWrapper<BaseStock> wrapper = new EntityWrapper<>();
        Date date = confDateService.getBeforeTypeDate(new Date(), DateConstant.DEAL_LIST);
        wrapper.eq("create_date", date);
        baseStockService.delete(wrapper);

        stopWatch.start();
        List<BaseStock> list = ExcelUtil.excelToList(is, BaseStock.class);
        Map<String, BaseStock> baseStockMap = list.stream().collect(Collectors.toMap(BaseStock::getStockCode, Function.identity(), (item1, item2) -> item1));
        is.close();
        log.info("基础股票数据导入-excel转换成对象耗时:{}ms ", stopWatch.getTime());
        stopWatch.reset();

        stopWatch.start();
        List<ConfStock> msList = confStockService.selectList(new EntityWrapper<>());
        Map<String, ConfStock> confStockMap = msList.stream().collect(Collectors.toMap(ConfStock::getStockCode, Function.identity(), (item1, item2) -> item1));
        List<ConfStock> fixConfStockList = Lists.newArrayList();
        log.info("基础股票数据导入-查询配置数据耗时:{}ms ", stopWatch.getTime());
        stopWatch.reset();

        stopWatch.start();
        insertBaseDate(date,list, confStockMap, fixConfStockList);
        log.info("基础股票数据导入-大盘统计与数据处理耗时:{}ms ", stopWatch.getTime());
        stopWatch.reset();

        stopWatch.start();
        fixConfStockDateByBasedStock(baseStockMap, msList, fixConfStockList);
        log.info("基础股票数据导入-补充配置数据耗时:{}ms ", stopWatch.getTime());
        stopWatch.reset();

        return true;
    }


    public void insertBaseDate(Date date, List<BaseStock> list, Map<String, ConfStock> confStockMap, List<ConfStock> fixConfStockList) {
        ///计算沪深成贡献值、交额、涨幅、
        BigDecimal shAmount = BigDecimal.ZERO;
        BigDecimal shContribution = BigDecimal.ZERO;
        BigDecimal shGains = BigDecimal.ZERO;

        BigDecimal szAmount = BigDecimal.ZERO;
        BigDecimal szContribution = BigDecimal.ZERO;
        BigDecimal szGains = BigDecimal.ZERO;

        //TODO 2023-10-2 有时间在弄 计算主板、科创、创业板 的成交额、贡献值、涨幅
//        BigDecimal zbAmount = BigDecimal.ZERO;
//        BigDecimal kcAmount = BigDecimal.ZERO;
//        BigDecimal cyAmount = BigDecimal.ZERO;
//        BigDecimal hContribution = BigDecimal.ZERO;
//        BigDecimal sContribution = BigDecimal.ZERO;
//        BigDecimal hGains = BigDecimal.ZERO;
//        BigDecimal sGains = BigDecimal.ZERO;

        //基础数据的循环
        for (int i = 0; i < list.size(); i++) {
            BaseStock dto = list.get(i);
            String stockCode = dto.getStockCode();
            String stockName = dto.getStockName();
//            String plate = dto.getPlate();

            dto.setCreateDate(date);
            dto.setCreateBy("基础数据导入");

            //如果基础数据新增，但配置数据没有时，同步这块数据
            ConfStock cms = confStockMap.get(stockCode);
            if (cms == null) {
                log.info("配置表新增数据 stockCode : {} name:{}", stockCode, stockName);
                cms = new ConfStock();
                BeanUtils.copyProperties(dto, cms, ConfStock.class);
                fixConfStockList.add(cms);
            }

            //基础数据修正
            //成交额 成交额为0时 为停牌数据
            Double amountD = dto.getAmount();
            if (amountD == null) {
                log.info("即将上市 stockCode : {} name:{}", stockCode, stockName);
                continue;
            } else if (amountD == 0) {
                log.info("停牌 stockCode : {} name:{}", stockCode, stockName);
                continue;
            }

            String tip = StockConstant.SpecilNameEnum.getTip(stockName);
            if (StringUtils.isNotBlank(tip)) {
                log.info("{}", tip);
            }
            BigDecimal amount = new BigDecimal(amountD).divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP);
            dto.setAmount(amount.doubleValue());
            //贡献度
            BigDecimal contribution = dto.getContribution() == null ? BigDecimal.ZERO : new BigDecimal(dto.getContribution());
            dto.setContribution(contribution.doubleValue());
            //流通盘
            BigDecimal circulation = new BigDecimal(dto.getCirculation()).divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP);
            dto.setCirculation(circulation.doubleValue());

            dto.setAmplitude(dto.getAmplitude() * 100);
            dto.setChangingHands(dto.getChangingHands() * 100);
            if (dto.getGains() != null) dto.setGains(dto.getGains() * 100);
            if (dto.getStartGains() != null) dto.setStartGains(dto.getStartGains() * 100);
            if (dto.getEntitySize() != null) {
                double entitySize = dto.getEntitySize() * 100;
                dto.setEntitySize(entitySize);
            }

            //计算统计数据
            //计算沪深成贡献值、交额、涨幅、
            if (stockCode.startsWith(StockConstant.ExchangeEnum.SSE.getPrefix())) {
                shAmount = shAmount.add(amount);
                shContribution = shContribution.add(contribution);
            } else if (stockCode.startsWith(StockConstant.ExchangeEnum.SZSE.getPrefix())) {
                szAmount = szAmount.add(amount);
                szContribution = szContribution.add(contribution);
            }
            //计算主板、科创、创业板 的成交额、贡献值、涨幅
//            if (StockConstant.PlateEnum.CYB.getName().equals(plate)) {
//                shAmount = shAmount.add(amount);
//                shContribution = shContribution.add(contribution);
//            } else if (StockConstant.PlateEnum.KCB.getName().equals(plate)) {
//                szAmount = szAmount.add(amount);
//                szContribution = szContribution.add(contribution);
//            }

        }
        shGains = shContribution.multiply(new BigDecimal("100")).divide(new BigDecimal("3117.75"), 2, BigDecimal.ROUND_HALF_UP);
        szGains = szContribution.multiply(new BigDecimal("100")).divide(new BigDecimal("10131.66"), 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal shAmountReust = shAmount.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal szAmountReust = szAmount.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP);


        log.info("统计-沪市-成交额:{},点数:{},涨幅:{}", shAmount, shContribution.setScale(2, BigDecimal.ROUND_HALF_UP), shGains.setScale(2, BigDecimal.ROUND_HALF_UP));
        log.info("统计-深市-成交额:{},点数:{},涨幅:{}", szAmount, szContribution.setScale(2, BigDecimal.ROUND_HALF_UP), szGains.setScale(2, BigDecimal.ROUND_HALF_UP));
        log.info("统计-两市成交额:{}", shAmount.add(szAmount));
        baseStockService.insertBatch(list, list.size());
    }


    public void fixConfStockDateByBasedStock(Map<String, BaseStock> baseStockMap, List<ConfStock> msList, List<ConfStock> fixConfStockList) {
        //配置数据的循环
        for (int i = 0; i < msList.size(); i++) {
            //数据补充(包括上市日期、名字、发行价格)
            ConfStock confStock = msList.get(i);
            String stockCode = confStock.getStockCode();
            BaseStock bs = baseStockMap.get(stockCode);
            //基础数据为空时，跳过补充
            if (bs == null) continue;
            Boolean flag = false;

            //上市日期
            Date issueDate = confStock.getIssueDate();
            if (issueDate == null) {
                confStock.setIssueDate(bs.getIssueDate());
                flag = true;
            }

            //名字
            String stockName = confStock.getStockName();
            String baseName = bs.getStockName();
            if (!stockName.equals(baseName) && StringUtils.isNotBlank(StockConstant.SpecilNameEnum.getTip(stockName))) {
                log.info("stockName conf:{},input:{}", stockName, baseName);
                if (StringUtils.isBlank(StockConstant.SpecilNameEnum.getTip(baseName))) {
                    confStock.setStockName(baseName);
                    flag = true;
                }
            }

            //发行价格
            Double price = confStock.getPrice();
            if (price == null) {
                confStock.setPrice(bs.getPrice());
                flag = true;
            }

            //板块
            String plate = confStock.getPlate();
            if (plate == null) {
                confStock.setPlate(bs.getPlate());
                flag = true;
            }

            //添加修正list中
            if (flag) {
                fixConfStockList.add(confStock);
            }
        }

        if(CollectionUtils.isNotEmpty(fixConfStockList)){
            confStockService.insertOrUpdateBatch(fixConfStockList, fixConfStockList.size());
        }
    }

    @Override
    public boolean importSubjectDetail(InputStream is, String startDate, String date) throws Exception {
        EntityWrapper<BaseSubject> subjectWr;
        EntityWrapper<BaseSubjectLine> subjectLineWr;
        EntityWrapper<BaseSubjectLineDetail> detailWr;

        List<BaseSubjectDetail> imputList = ExcelUtil.excelToList(is, BaseSubjectDetail.class);
        is.close();

        //没有被删除过的时间，会被过滤
        imputList = imputList.stream().filter(po -> {

            String coreName = po.getCoreName();
            coreName = StockUtil.calibrateHalfAngle(coreName);
            po.setCoreName(coreName);
            String helpName = po.getHelpName();
            helpName = StockUtil.calibrateHalfAngle(helpName);
            po.setHelpName(helpName);


            Date createDate = po.getCreateDate();
            if (StringUtils.isNotBlank(startDate)) {
                Date start = DateUtil.parseDate(startDate);
                if (start.after(createDate)) {
                    return false;
                }
            }
            if (StringUtils.isNotBlank(date)) {
                Date end = DateUtil.parseDate(date);
                if (end.before(createDate)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        //插入并自动生成题材其他数据
        genSubjectDate(imputList);
        return true;
    }



    @Override
    public void genSubjectDate(List<BaseSubjectDetail> imputList) {
        //这里需要拆分成多个对象：题材对象和题材线对象都是统计后新增或修改，而明细对象是直接新增的
        List<BaseSubjectLineDetail> importDetailList = BeanUtil.copyToList(imputList, BaseSubjectLineDetail.class);
        baseSubjectLineDetailService.insertBatch(importDetailList, importDetailList.size());
        //自动生成SubLine数据
        genSubLine(imputList);
        //自动生成Sub数据
        genSub(imputList);
        //清除无用关联
        clearSubDate();
        //清除重复数据
        clearRepetitionDate();
    }

    private void clearRepetitionDate() {
        baseSubjectLineDetailService.clearRepetitionDate();
    }

    private void clearSubDate() {
        baseSubjectLineService.clearSubDate();
        baseSubjectService.clearSubDate();
    }

    private void genSub(List<BaseSubjectDetail> imputList) {
        EntityWrapper<BaseSubject> subjectWr;
        EntityWrapper<BaseSubjectLine> subjectLineWr;//查询所有明细数据，更新line对象数据
        Map<String, List<BaseSubjectDetail>> subNameMap = imputList.stream().collect(Collectors.groupingBy(BaseSubjectDetail::getSubName));
        //根据分支线去整理数据
        for (String str : subNameMap.keySet()) {
            BaseSubjectDetail detail = subNameMap.get(str).get(0);

            //先查一下是不是第一次新增
            subjectWr = new EntityWrapper<>();
            subjectWr.eq("SUB_NAME", str);
            subjectWr.eq("STATE", "1");
            BaseSubject sub = baseSubjectService.selectOne(subjectWr);

            //如果是第一次新增，先弄个对象
            if (sub == null) {
                sub = BaseSubject.builder()
                        .subName(detail.getSubName())
                        .state("1")
                        .build();
            }
            //添加非空字段
            if (StringUtils.isNotBlank(detail.getSubInstructions())) {
                sub.setInstructions(detail.getSubInstructions());
            }
            subjectLineWr = new EntityWrapper<>();
            subjectLineWr.eq("SUB_NAME", str);
            subjectLineWr.eq("STATE", "1");
            List<BaseSubjectLine> lineList = baseSubjectLineService.selectList(subjectLineWr);

            Date startDate = lineList.stream().min(Comparator.comparing(BaseSubjectLine::getDurationStart, Comparator.nullsFirst(Date::compareTo))).map(BaseSubjectLine::getDurationStart).get();
            Date endDate = lineList.stream().max(Comparator.comparing(BaseSubjectLine::getDurationEnd, Comparator.nullsFirst(Date::compareTo))).map(BaseSubjectLine::getDurationEnd).get();
            sub.setDurationStart(startDate);
            sub.setDurationEnd(endDate);

            Integer combo = lineList.stream().max(Comparator.comparing(BaseSubjectLine::getCombo, Comparator.nullsFirst(Integer::compareTo))).map(BaseSubjectLine::getCombo).orElse(0);
            sub.setCombo(combo);
            baseSubjectService.insertOrUpdate(sub);
        }
    }

    private void genSubLine(List<BaseSubjectDetail> imputList) {
        EntityWrapper<BaseSubjectLine> subjectLineWr;
        EntityWrapper<BaseSubjectLineDetail> detailWr;//题材线逻辑处理
        Map<String, List<BaseSubjectDetail>> subLineNameMap = imputList.stream().collect(Collectors.groupingBy(BaseSubjectDetail::getSubLineName));
        //根据分支线去整理数据
        for (String str : subLineNameMap.keySet()) {
            List<BaseSubjectDetail> list = subLineNameMap.get(str);
            //大部分只会导入一条数据
            BaseSubjectDetail detail = list.get(0);

            //先查一下是不是第一次新增
            subjectLineWr = new EntityWrapper();
            subjectLineWr.eq("SUB_LINE_NAME", str);
            subjectLineWr.eq("STATE", "1");
            BaseSubjectLine line = baseSubjectLineService.selectOne(subjectLineWr);

            //如果是第一次新增，先弄个对象
            if (line == null) {
                line = BaseSubjectLine.builder().state("1").build();
            }
            BeanUtils.copyProperties(detail, line, BaseSubjectLine.class);

            //查询所有明细数据，更新line对象数据
            BaseSubjectLineDetail queryDetail = BaseSubjectLineDetail.builder().subLineName(str).build();
            List<BaseSubjectLineDetail> detailList = baseSubjectLineDetailService.getBaseSubjectLineDetailList(queryDetail);

            Date startDate = detailList.stream().min(Comparator.comparing(BaseSubjectLineDetail::getCreateDate, Comparator.nullsFirst(Date::compareTo))).map(BaseSubjectLineDetail::getCreateDate).get();
            Date endDate = detailList.stream().max(Comparator.comparing(BaseSubjectLineDetail::getCreateDate, Comparator.nullsFirst(Date::compareTo))).map(BaseSubjectLineDetail::getCreateDate).get();
            line.setDurationStart(startDate);
            line.setDurationEnd(endDate);

            //检索板块周期内辨识度标的的最大高度
            List<String> stockNameList = Lists.newArrayList();
            HashSet set = Sets.newHashSet();
            for (BaseSubjectLineDetail po : detailList) {
                String coreName = po.getCoreName();
                String helpName = po.getHelpName();
                if (StringUtils.isNoneBlank(coreName)) set.addAll(Sets.newHashSet(coreName.split(",")));
                if (StringUtils.isNoneBlank(helpName)) set.addAll(Sets.newHashSet(helpName.split(",")));
                set.remove(";");
                set.remove("");
            }
            ;
            stockNameList.addAll(set);

            if (CollectionUtils.isNotEmpty(stockNameList)) {
                QueryStockDto queryStock = QueryStockDto.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .stockNameList(stockNameList)
                        .build();
                Integer combo = baseZtStockService.getMaxCombo(queryStock);
                line.setCombo(combo);
            }
            baseSubjectLineService.insertOrUpdate(line);
        }
    }


    private void datePro(BaseReportStock po) {
        StringBuffer instructions = new StringBuffer("");

        po.setCreateDate(new Date());
        po.setModifedDate(new Date());
        BigDecimal before = new BigDecimal(po.getCirculation());
        po.setCirculation(before.divide(new BigDecimal(100000000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        po.setAmplitude(po.getAmplitude() * 100);
//                po.setYesterdayAmplitude(po.getYesterdayAmplitude() * 100);
        po.setChangingHands(po.getChangingHands() * 100);
//                po.setYesterdayChangingHands(po.getYesterdayChangingHands() * 100);
        if (po.getGains() != null) po.setGains(po.getGains() * 100);
        if (po.getStartGains() != null) po.setStartGains(po.getStartGains() * 100);
        if (po.getEntitySize() != null) {
            double entitySize = po.getEntitySize() * 100;
            po.setEntitySize(entitySize);
        }


        double value = 0;
        if (po.getCirculation() < 30) {
            instructions.append("小盘;");
        }

        value = po.getChangingHands();
        if (75 > value && value > 50) {
            instructions.append("高换手;");
        } else if (value > 75) {
            instructions.append("死亡换手;");
        }

        value = po.getAmplitude();
        if ("主板".equals(po.getPlate()) && value > 13 || value > 25) {
            instructions.append("大长腿;");
        }

        po.setInstructions(instructions.toString());
    }


    /**
     * 波动报表说明字段处理
     *
     * @param po
     */
    private void bdInstructions(BaseReportStock po) {
        //实体大小
//        BigDecimal MAIN_1 = new BigDecimal(po.getGains()).setScale(2, BigDecimal.ROUND_UP);
//        BigDecimal b = new BigDecimal(po.getStartGains()).setScale(2, BigDecimal.ROUND_UP);
//        BigDecimal result = MAIN_1.subtract(b);
//        double entitySize = result.doubleValue();


        //说明
        StringBuffer instructions = new StringBuffer(po.getInstructions());
        Double entitySize = po.getEntitySize();
        Double amplitude = po.getAmplitude();
        if (Math.abs(entitySize) < 2) {
            if (Math.abs(amplitude) > 9) {
                instructions.append("大分歧;");
            }
            if (Math.abs(amplitude) == 0) {
                instructions.append("一字;");
            } else {
                instructions.append("十字星;");
            }
        } else if (entitySize > 6) {
            instructions.append("大阳线;");
        } else if (entitySize < -6) {
            instructions.append("负反馈;");
        }
        po.setInstructions(instructions.toString());
    }


    /**
     * 涨停报表字段处理
     *
     * @param po
     */
    private void ztInstructions(BaseReportStock po) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //说明
        StringBuffer instructions = new StringBuffer(po.getInstructions());
        Date finalHardenTime = po.getFinalHardenTime();
        Date hardenTime = po.getHardenTime();
        Date finalTime = (finalHardenTime != null ? finalHardenTime : hardenTime);
        Double amplitude = po.getAmplitude();
        Double yesterdayGains = po.getYesterdayGains();
        Double yestedayEntitySize = po.getYestedayEntitySize();
        Double yesterdayAmplitude = po.getYesterdayAmplitude();
        Double changingHands = po.getChangingHands();
        Double yesterdayChangingHands = po.getYesterdayChangingHands();

        if (amplitude == 0) {
            po.setHardenType("一字板");
            if (yesterdayChangingHands != null && changingHands <= yesterdayChangingHands && yesterdayAmplitude != null && yesterdayAmplitude == 0) {
                instructions.append("连续加速;");
            } else if (yesterdayChangingHands != null && changingHands <= yesterdayChangingHands) {
                instructions.append("加速;");
            } else if(yesterdayChangingHands!=null){
                instructions.append("一字分歧;");
            }else {
                instructions.append("涨停;");
            }
        }
        if (amplitude > 19 && "主板".equals(po.getPlate())) {
            instructions.append("地天板;");
        } else if (sdf.parse("09:30:00").equals(hardenTime) && amplitude > 0) {
            po.setHardenType("T字板");
            instructions.append("T字板;");
        } else if (sdf.parse("09:40:00").after(finalTime)) {
            po.setHardenType("秒板");
            instructions.append("秒板;");
        } else if (sdf.parse("14:40:00").before(finalTime)) {
            po.setHardenType("偷袭板");
            instructions.append("偷袭板;");
        } else {
            po.setHardenType("换手板");
        }

        //弱修复|弱转强
        if (yesterdayGains != null && yesterdayGains < -6) {
            instructions.append("弱修复;");
        } else if (yestedayEntitySize != null) {
            instructions.append("弱转强;");
        }
        po.setInstructions(instructions.toString());
    }

    /**
     * 摸板报表样式处理
     *
     * @param po
     */
    private void mbInstructions(BaseReportStock po) {
        //说明
        StringBuffer instructions = new StringBuffer(po.getInstructions());
        Double entitySize = po.getEntitySize();
        Double amplitude = po.getAmplitude();
        if (Math.abs(entitySize) < 2) {
            if (Math.abs(amplitude) > 9) {
                instructions.append("大分歧;");
            }
            if (Math.abs(amplitude) == 0) {
                instructions.append("一字;");
            } else {
                instructions.append("十字星;");
            }
        } else if (entitySize > 6) {
            instructions.append("大阳线;");
        } else if (entitySize < -6) {
            instructions.append("负反馈;");
        }
        po.setInstructions(instructions.toString());
    }
}
