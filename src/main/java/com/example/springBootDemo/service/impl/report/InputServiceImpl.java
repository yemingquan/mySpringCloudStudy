package com.example.springBootDemo.service.impl.report;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.BaseSubject;
import com.example.springBootDemo.entity.BaseSubjectLine;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.ConfMySotck;
import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.entity.dto.QueryStockDto;
import com.example.springBootDemo.entity.input.*;
import com.example.springBootDemo.entity.report.SubjectReport;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    StudentService studentService;
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
    ConfMySotckService confMySotckService;


    @Override
    public boolean importExcelZthfStock(InputStream is) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZthfStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
        baseZthfStockService.delete(wrapper);

        List<BaseZthfStock> list = ExcelImportUtil.importExcel(is, BaseZthfStock.class, importParams);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = getMainBusinessList();
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

    public List<String> getMainBusinessList() {
//        return Lists.newArrayList();
        String date = DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10);
        String startDate = DateUtil.format(DateUtil.getDayDiff(new Date(), -20), DateUtils.DATE_FORMAT_10);
        List<SubjectReport> subList = baseSubjectLineDetailService.getSubjectReport(date, startDate);
        return subList.stream().map(SubjectReport::getMainBusiness).collect(Collectors.toList());
    }

    public void setMainBusinessList(List<String> mainBusinessList, BaseStock po) {
//        log.info("股票名称[{}],代码[{}]", po.getStockName(), po.getStockCode());
        EntityWrapper<ConfMySotck> wr = new EntityWrapper<>();
        wr.eq("STOCK_CODE", po.getStockCode());
        ConfMySotck conf = confMySotckService.selectOne(wr);
        if(conf==null){
            confMySotckService.insert(ConfMySotck.builder().stockCode(po.getStockCode()).stockName(po.getStockName()).build());
            return;
        }
        String nowMainBusiness = conf.getMainBusiness();
        if (StringUtils.isNotEmpty(nowMainBusiness)) {
            List<String> oldMBList = Lists.newArrayList(nowMainBusiness.split(","));
            oldMBList.remove("");
            if (CollectionUtils.isNotEmpty(oldMBList)) {
                List<String> newMBList = Lists.newArrayList();
                for (String str : oldMBList) {
                    if (mainBusinessList.contains(str)) {
                        newMBList.add(str);
                    }
                }

                if (CollectionUtils.isNotEmpty(newMBList)) {
                    String mainBusiness = newMBList.stream().collect(Collectors.joining("+"));
                    po.setMainBusiness(mainBusiness);
                    log.info("++++股票名称[{}],代码[{}],系统设置主业为[{}]", po.getStockName(), po.getStockCode(), mainBusiness);
                }
            }
        }
    }


    @Override
    public boolean importExcelZtStock(InputStream is) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
        baseZtStockService.delete(wrapper);

        List<BaseZtStock> list = ExcelImportUtil.importExcel(is, BaseZtStock.class, importParams);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = getMainBusinessList();
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
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseBdUpStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
        baseBdUpStockService.delete(wrapper);

        List<BaseBdUpStock> list = ExcelImportUtil.importExcel(is, BaseBdUpStock.class, importParams);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = getMainBusinessList();
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
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseBdDownStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
        baseBdDownStockService.delete(wrapper);

        List<BaseBdDownStock> list = ExcelImportUtil.importExcel(is, BaseBdDownStock.class, importParams);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = getMainBusinessList();
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
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseDtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
        baseDtStockService.delete(wrapper);

        List<BaseDtStock> list = ExcelImportUtil.importExcel(is, BaseDtStock.class, importParams);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = getMainBusinessList();
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
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZbStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10));
        baseZbStockService.delete(wrapper);

        List<BaseZbStock> list = ExcelImportUtil.importExcel(is, BaseZbStock.class, importParams);
        is.close();

        //查找历史主业
        List<String> mainBusinessList = getMainBusinessList();
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
    public boolean importSubjectDetail(InputStream is, String startDate, String date) throws Exception {
        EntityWrapper<BaseSubject> subjectWr;
        EntityWrapper<BaseSubjectLine> subjectLineWr;
        EntityWrapper<BaseSubjectLineDetail> detailWr;

        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        List<BaseSubjectDetail> imputList = ExcelImportUtil.importExcel(is, BaseSubjectDetail.class, importParams);
        is.close();

        //没有被删除过的时间，会被过滤
        imputList = imputList.stream().filter(po -> {
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

            QueryStockDto queryStock = QueryStockDto.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .stockNameList(stockNameList)
                    .build();
            Integer combo = baseZtStockService.getMaxCombo(queryStock);
            line.setCombo(combo);
            baseSubjectLineService.insertOrUpdate(line);
        }
    }


    private void datePro(BaseStock po) {
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
        if (po.getEntitySize() != null) po.setEntitySize(po.getEntitySize() * 100);


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
    private void bdInstructions(BaseStock po) {
        //实体大小
//        BigDecimal a = new BigDecimal(po.getGains()).setScale(2, BigDecimal.ROUND_UP);
//        BigDecimal b = new BigDecimal(po.getStartGains()).setScale(2, BigDecimal.ROUND_UP);
//        BigDecimal result = a.subtract(b);
//        double entitySize = result.doubleValue();


        //说明
        StringBuffer instructions = new StringBuffer(po.getInstructions());
        Double entitySize = po.getEntitySize();
        Double amplitude = po.getAmplitude();
        if (Math.abs(entitySize) < 2) {
            if (Math.abs(amplitude) > 9) {
                instructions.append("大分歧;");
            } else {
                instructions.append("十字星;");
            }
        } else if (entitySize > 7) {
            instructions.append("大阳线;");
        } else if (entitySize < -7) {
            instructions.append("负反馈;");
        }
        po.setInstructions(instructions.toString());
    }


    /**
     * 涨停报表字段处理
     *
     * @param po
     */
    private void ztInstructions(BaseStock po) throws ParseException {
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
            } else {
                instructions.append("一字分歧;");
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
    private void mbInstructions(BaseStock po) {
        //说明
        StringBuffer instructions = new StringBuffer(po.getInstructions());
        Double entitySize = po.getEntitySize();
        Double amplitude = po.getAmplitude();
        if (Math.abs(entitySize) < 2) {
            if (Math.abs(amplitude) > 9) {
                instructions.append("大分歧;");
            } else {
                instructions.append("十字星;");
            }
        } else if (entitySize > 7) {
            instructions.append("大阳线;");
        } else if (entitySize < -7) {
            instructions.append("负反馈;");
        }
        po.setInstructions(instructions.toString());
    }
}
