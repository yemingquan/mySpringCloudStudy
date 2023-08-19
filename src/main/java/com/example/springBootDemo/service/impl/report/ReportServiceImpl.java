package com.example.springBootDemo.service.impl.report;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.*;
import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
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

    @Override
    public List<ZtReport> getZtReportByDate(String date) {
        List<ZtReport> list = Lists.newArrayList();
        List<ZtReport> list1 = baseZtStockService.getZtReportByDate(date);
        List<ZtReport> list2 = baseZthfStockService.getZtReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<MbReport> getMbReportByDate(String date) {
        List<MbReport> list = Lists.newArrayList();
        List<MbReport> list1 = baseZbStockService.getMbReportByDate(date);
        List<MbReport> list2 = baseDtStockService.getMbReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<BdReport> getBdReportByDate(String date) {
        List<BdReport> list = Lists.newArrayList();
        List<BdReport> list1 = baseBdUpStockService.getBdReportByDate(date);
        List<BdReport> list2 = baseBdDownStockService.getBdReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public void oprZtDate(List<ZtReport> list) throws ParseException {
        //最高板逻辑
        Integer maxCombo = list.stream().filter(po -> po.getCombo() > 3).mapToInt(ZtReport::getCombo).max().getAsInt();
        if (maxCombo != null) {
            List<ZtReport> maxComboList = list.stream().filter(po -> po.getCombo() == maxCombo).collect(Collectors.toList());
            for (ZtReport zr : maxComboList) {
                String instructions = zr.getInstructions();
                if (!instructions.contains("最高板")) {
                    zr.setInstructions(instructions + "最高板;");
                }
                //设置次新
                setCx(zr);
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
        }
    }

    @Override
    public void oprBdDate(List<BdReport> list) {
        for (BdReport zr : list) {
            //设置次新
            setCx(zr);
        }
    }

    private void setCx(BaseStock bs) {
        String cx = bs.getCxFlag();
        String instructions = bs.getInstructions();
        if (StringUtils.isNotBlank(cx) && !instructions.contains("次新")) {
            bs.setInstructions(instructions + cx);
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
    public boolean importExcelZthfStock(InputStream is) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZthfStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseZthfStockService.delete(wrapper);

        List<BaseZthfStock> list = ExcelImportUtil.importExcel(is, BaseZthfStock.class, importParams);
        is.close();
        list.stream().forEach(po -> {
            datePro(po);
            try {
                ztInstructions(po);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return baseZthfStockService.insertBatch(list, list.size());
    }


    @Override
    public boolean importExcelZtStock(InputStream is) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseZtStockService.delete(wrapper);

        List<BaseZtStock> list = ExcelImportUtil.importExcel(is, BaseZtStock.class, importParams);
        is.close();
        list.stream().forEach(po -> {
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
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseBdUpStockService.delete(wrapper);

        List<BaseBdUpStock> list = ExcelImportUtil.importExcel(is, BaseBdUpStock.class, importParams);
        is.close();
        list.stream().forEach(po -> {
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
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseBdDownStockService.delete(wrapper);

        List<BaseBdDownStock> list = ExcelImportUtil.importExcel(is, BaseBdDownStock.class, importParams);
        is.close();
        list.stream().forEach(po -> {
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
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseDtStockService.delete(wrapper);

        List<BaseDtStock> list = ExcelImportUtil.importExcel(is, BaseDtStock.class, importParams);
        is.close();
        list.stream().forEach(po -> {
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
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseZbStockService.delete(wrapper);

        List<BaseZbStock> list = ExcelImportUtil.importExcel(is, BaseZbStock.class, importParams);
        is.close();
        list.stream().forEach(po -> {
            datePro(po);
            mbInstructions(po);
            StringBuffer instructions = new StringBuffer(po.getInstructions());
            instructions.append("炸板;");
            po.setInstructions(instructions.toString());
        });
        return baseZbStockService.insertBatch(list, list.size());
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
        if ("主板".equals(po.getPlate()) && value > 12 || value > 24) {
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
        double entitySize = po.getEntitySize();
        if (Math.abs(entitySize) < 2) {
            instructions.append("十字星;");
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
            if (changingHands <= yesterdayChangingHands && yesterdayAmplitude != null && yesterdayAmplitude == 0) {
                instructions.append("连续加速;");
            } else if (changingHands <= yesterdayChangingHands) {
                instructions.append("加速;");
            } else {
                instructions.append("一字分歧;");
            }
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
        double entitySize = po.getEntitySize();
        if (Math.abs(entitySize) < 2) {
            instructions.append("十字星;");
        } else if (entitySize > 7) {
            instructions.append("大阳线;");
        } else if (entitySize < -7) {
            instructions.append("负反馈;");
        }
//        instructions.append("曾跌停;");
        po.setInstructions(instructions.toString());
    }

    private void setSbTime(List<ZtReport> bkList) throws ParseException {

        if (bkList.size() < 2) {
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
