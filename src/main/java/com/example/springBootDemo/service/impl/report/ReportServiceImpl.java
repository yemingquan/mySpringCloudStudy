package com.example.springBootDemo.service.impl.report;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.*;
import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-6 10:36
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Service
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
        });
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
        double entitySize = po.getEntitySize();

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

        if (Math.abs(entitySize) < 2) {
            instructions.append("十字星;");
        } else if (entitySize > 6) {
            instructions.append("大阳线;");
        } else if (entitySize < -6) {
            instructions.append("大阴线;");
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
        double value = po.getAmplitude();
        double yesterdayGains = po.getYesterdayGains();
        Double yestedayEntitySize = po.getYestedayEntitySize();

        if (amplitude == 0) {
            po.setHardenType("一字板");
            if (po.getYesterdayAmplitude() == 0) {
                instructions.append("连续加速;");
            } else {
                instructions.append("加速;");
            }
        } else if ("主板".equals(po.getPlate()) && value > 12 || value > 24) {
            po.setHardenType("大长腿");
            instructions.append("大长腿;");
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
        if (yesterdayGains < -6) {
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
        po.setInstructions(instructions.toString());
    }
}
