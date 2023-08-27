package com.example.springBootDemo.service.impl.report;


import cn.hutool.core.bean.BeanUtil;
import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.entity.input.*;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.SubjectReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class ReportServiceImpl implements ReportService {

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
    BaseSubjectService baseSubjecService;
    @Autowired
    BaseSubjectLineService baseSubjectLineService;
    @Autowired
    BaseSubjectLineDetailService baseSubjectLineDetailService;

    @Override
    public List<ZtReport> getZtReportByDate(String date) {
        List<ZtReport> list = Lists.newArrayList();
        List<ZtReport> list1 = baseZtStockService.getZtReportByDate(date);
        List<ZtReport> list2 = baseZthfStockService.getZtReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        //多重排序
        list = list.stream().sorted(Comparator.comparing(ZtReport::getHardenTime))
                .sorted(Comparator.comparing(ZtReport::getCombo, Comparator.reverseOrder()))
                .sorted(Comparator.comparing(ZtReport::getMainBusiness, Comparator.reverseOrder())).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<MbReport> getMbReportByDate(String date) {
        List<MbReport> list = Lists.newArrayList();
        List<MbReport> list1 = baseZbStockService.getMbReportByDate(date);
        List<MbReport> list2 = baseDtStockService.getMbReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        //多重排序
        list = list.stream().sorted(Comparator.comparing(MbReport::getMainBusiness, Comparator.reverseOrder()))
                .sorted(Comparator.comparing(MbReport::getTouchTime, Comparator.nullsLast(Date::compareTo))).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BdReport> getBdReportByDate(String date) {
        List<BdReport> list = Lists.newArrayList();
        List<BdReport> list1 = baseBdUpStockService.getBdReportByDate(date);
        List<BdReport> list2 = baseBdDownStockService.getBdReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        //多重排序
        list = list.stream().sorted(Comparator.comparing(BdReport::getEntitySize, Comparator.reverseOrder()))
                .sorted(Comparator.comparing(BdReport::getMainBusiness, Comparator.reverseOrder())).collect(Collectors.toList());
        return list;
    }

    @Override
    public void oprZtDate(List<ZtReport> list) throws ParseException {
        for (ZtReport zt : list) {
            //设置次新
            setCx(zt);
            //设置转债
            setZz(zt);
        }

        //最高板逻辑
        Integer maxCombo = list.stream().mapToInt(ZtReport::getCombo).max().getAsInt();
        if (maxCombo != null && maxCombo > 2) {
            List<ZtReport> maxComboList = list.stream().filter(po -> po.getCombo() == maxCombo).collect(Collectors.toList());
            for (ZtReport zr : maxComboList) {
                String instructions = zr.getInstructions();
                if (!instructions.contains("最高板")) {
                    zr.setInstructions(instructions + "最高板;");
                }
            }
        }

        //日内龙逻辑
        Map<String, List<ZtReport>> ztMap = list.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        for (String str : ztMap.keySet()) {
            List<ZtReport> bkList = ztMap.get(str);
            //设置首版时间
            setSbTime(bkList);
        }
    }

    @Override
    public void oprMbDate(List<MbReport> list) {
        for (MbReport zr : list) {
            //设置次新
            setCx(zr);
            //设置转债
            setZz(zr);
        }
    }

    @Override
    public void oprBdDate(List<BdReport> list) {
        for (BdReport zr : list) {
            //设置次新
            setCx(zr);
            //设置转债
            setZz(zr);
        }
    }

    @Override
    public void saveZtInstructions(List<ZtReport> list) {
        List<ZtReport> list1 = list.stream().filter(po -> "1".equals(po.getSource())).collect(Collectors.toList());
        List<ZtReport> list2 = list.stream().filter(po -> "2".equals(po.getSource())).collect(Collectors.toList());

        baseZtStockService.updateBatchById(BeanUtil.copyToList(list1, BaseZtStock.class));
        baseZthfStockService.updateBatchById(BeanUtil.copyToList(list2, BaseZthfStock.class));
    }

    @Override
    public void saveMbInstructions(List<MbReport> list) {
        List<MbReport> list1 = list.stream().filter(po -> "3".equals(po.getSource())).collect(Collectors.toList());
        List<MbReport> list2 = list.stream().filter(po -> "4".equals(po.getSource())).collect(Collectors.toList());

        baseZbStockService.updateBatchById(BeanUtil.copyToList(list1, BaseZbStock.class));
        baseDtStockService.updateBatchById(BeanUtil.copyToList(list2, BaseDtStock.class));
    }

    @Override
    public void saveBdInstructions(List<BdReport> list) {
        List<BdReport> list1 = list.stream().filter(po -> "5".equals(po.getSource())).collect(Collectors.toList());
        List<BdReport> list2 = list.stream().filter(po -> "6".equals(po.getSource())).collect(Collectors.toList());

        baseBdUpStockService.updateBatchById(BeanUtil.copyToList(list1, BaseBdUpStock.class));
        baseBdDownStockService.updateBatchById(BeanUtil.copyToList(list2, BaseBdDownStock.class));
    }

    @Override
    public List<SubjectReport> getSubjectReport(String date) {
        return baseSubjectLineDetailService.getSubjectReport(date);
    }
    private void setCx(BaseStock bs) {
        String cx = bs.getCxFlag();
        String instructions = bs.getInstructions();
        if (StringUtils.isNotBlank(cx) && !instructions.contains("次新")) {
            bs.setInstructions(instructions + cx);
        }
    }

    private void setZz(BaseStock bs) {
        String bond = bs.getBond();
        String instructions = bs.getInstructions();
        if (StringUtils.isNotBlank(bond) && !instructions.contains("含zz")) {
            bs.setInstructions(instructions + "含zz;");
        }
    }

    private void setSbTime(List<ZtReport> bkList) throws ParseException {

        if (bkList.size() < 3) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //板块涨停时间集合，用于筛选板块首版
        List<Date> finalTimeList = Lists.newArrayList();
        Integer maxCo = 3;
        for (ZtReport po : bkList) {
            Date finalHardenTime = po.getFinalHardenTime();
            Date hardenTime = po.getHardenTime();
            Integer combo = po.getCombo();
            if (finalHardenTime != null) {
                finalTimeList.add(finalHardenTime);
            } else if (sdf.parse("09:30:00").equals(hardenTime)) {

            } else {
                finalTimeList.add(po.getHardenTime());
            }
            if (combo > maxCo) {
                maxCo = combo;
            }
        }
        Date sbTime = finalTimeList.stream().min(Comparator.comparing(x -> x)).orElse(null);

        for (ZtReport po : bkList) {
            String instructions = po.getInstructions();
            Date time1 = po.getHardenTime();
            Date time2 = po.getFinalHardenTime();
            Integer combo = po.getCombo();
            String mb = po.getMainBusiness();

            if (sbTime != null && time2 == null && sbTime.equals(time1) && !instructions.contains("日内龙")) {
                instructions = instructions + "日内龙;";
            } else if (sbTime != null && sbTime.equals(time2) && !instructions.contains("回封龙")) {
                instructions = instructions + "回封龙;";
            }

            if (maxCo == combo && !instructions.contains(maxCo.toString())) {
                instructions = instructions + mb + maxCo + ";";
            }

            po.setInstructions(instructions);
        }
    }
}
