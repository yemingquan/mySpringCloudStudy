package com.example.springBootDemo.service.impl.report;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.enums.NewsEnum;
import com.example.springBootDemo.entity.BaseMarketDetail;
import com.example.springBootDemo.entity.base.BaseReportStock;
import com.example.springBootDemo.entity.fix.FixZtReport;
import com.example.springBootDemo.entity.input.*;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.SubjectReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.StockUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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
    @Autowired
    ConfDateService confDateService;
    @Autowired
    BaseMarketDetailService baseMarketDetailService;

    @Override
    public List<ZtReport> getZtReportByDate(String date) {
        List<ZtReport> list = Lists.newArrayList();
        List<ZtReport> list1 = baseZtStockService.getZtReportByDate(date);
        List<ZtReport> list2 = baseZthfStockService.getZtReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);

        //多重排序
        Map<String, List<ZtReport>> map = list.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));

        //TODO 对象数组根据板块 对象中某个属性的数量 排序，待优化
        Map<Integer, List<ZtReport>> linkedHashMap = new LinkedHashMap();
        List<Integer> sortList = Lists.newArrayList();
        for (String mainBusiness : map.keySet()) {
            List<ZtReport> mbList = map.get(mainBusiness);
            int size = mbList.size();

            List<ZtReport> tempList = linkedHashMap.get(size);
            if (CollectionUtils.isEmpty(tempList)) {
                linkedHashMap.put(size, mbList);
            } else {
                tempList.addAll(mbList);
            }

            sortList.add(size);
        }

        sortList = sortList.stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        list.clear();
        for (Integer size : sortList) {
            List<ZtReport> tempList = linkedHashMap.get(size);
            if(size>1){
                tempList = tempList.stream()
                        .sorted(Comparator.comparing(ZtReport::getHardenTime))
                        .sorted(Comparator.comparing(ZtReport::getCombo, Comparator.reverseOrder()))
                        .sorted(Comparator.comparing(ZtReport::getMainBusiness, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }else{
                tempList = tempList.stream()
                        .sorted(Comparator.comparing(ZtReport::getMainBusiness, Comparator.reverseOrder()))
                        .sorted(Comparator.comparing(ZtReport::getHardenTime))
                        .sorted(Comparator.comparing(ZtReport::getCombo, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
            list.addAll(tempList);
        }
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
        //查询是否是昨天强势的方向
        Date date = confDateService.getBeforeTypeDate(list.get(0).getCreateDate(), DateConstant.DEAL_LIST);
        //取上一个交易日
        date = DateUtil.getNextDay(date, -1);
        List<String> activeList = baseSubjectLineDetailService.getActiveBusinessList(date);
        //日内龙逻辑
        Map<String, List<ZtReport>> ztMap = list.stream().collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        for (String str : ztMap.keySet()) {
            List<ZtReport> bkList = ztMap.get(str);
            //活口逻辑
            if (bkList.size() < 3) {
                //查询昨日活跃板块
                bkList.stream().forEach(po -> {
                    if (activeList.contains(po.getMainBusiness().replace("最-", "")) && !po.getInstructions().contains("活口")) {
                        po.setInstructions(po.getInstructions() + "活口;");
                    }
                });
                continue;
            }

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

        if(CollectionUtils.isNotEmpty(list1))baseZtStockService.updateBatchById(BeanUtil.copyToList(list1, BaseZtStock.class));
        if(CollectionUtils.isNotEmpty(list2))baseZthfStockService.updateBatchById(BeanUtil.copyToList(list2, BaseZthfStock.class));
    }

    @Override
    public void saveMbInstructions(List<MbReport> list) {
        List<MbReport> list1 = list.stream().filter(po -> "3".equals(po.getSource())).collect(Collectors.toList());
        List<MbReport> list2 = list.stream().filter(po -> "4".equals(po.getSource())).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(list1))baseZbStockService.updateBatchById(BeanUtil.copyToList(list1, BaseZbStock.class));
        if(CollectionUtils.isNotEmpty(list2))baseDtStockService.updateBatchById(BeanUtil.copyToList(list2, BaseDtStock.class));
    }

    @Override
    public void saveBdInstructions(List<BdReport> list) {
        List<BdReport> list1 = list.stream().filter(po -> "5".equals(po.getSource())).collect(Collectors.toList());
        List<BdReport> list2 = list.stream().filter(po -> "6".equals(po.getSource())).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(list1))baseBdUpStockService.updateBatchById(BeanUtil.copyToList(list1, BaseBdUpStock.class));
        if(CollectionUtils.isNotEmpty(list2))baseBdDownStockService.updateBatchById(BeanUtil.copyToList(list2, BaseBdDownStock.class));
    }

    @Override
    public List<SubjectReport> getSubjectReport(String date, String startDate) {
        List<SubjectReport> list = baseSubjectLineDetailService.getSubjectReport(date, startDate);
        for (SubjectReport sr : list) {
//            log.info(sr.toString());
            sr.setMainBusiness(sr.getMainBusiness().replaceAll("最-", ""));
            sr.setWeek(DateUtil.getWeek(sr.getCreateDate()));
        }
        //添加
        Map<String, List<SubjectReport>> oprMap = list.stream().collect(Collectors.groupingBy(SubjectReport::getSubName));
        Map<String, List<SubjectReport>> relationMap = list.stream()
                .filter(k -> !NewsEnum.SCOPE_UNKNOW.getName().equals(k.getSubName())).collect(Collectors.groupingBy(SubjectReport::getMainBusiness));

        List<SubjectReport> todayList = oprMap.get(NewsEnum.SCOPE_UNKNOW.getName());
        if(CollectionUtils.isEmpty(todayList)){
            return list;
        }

        //加工今天的数据
        for (SubjectReport sr : todayList) {
            String mainBusiness = sr.getMainBusiness();
            List<SubjectReport> tempList = relationMap.get(mainBusiness);
            if(CollectionUtils.isEmpty(tempList)){

            }else if (tempList.size() > 1) {
                SubjectReport tempPo = tempList.get(0);
                sr.setSubName(tempPo.getSubName());
                sr.setSubLineName(tempPo.getSubLineName());
            } else if (tempList.size() == 1) {
                SubjectReport tempPo = tempList.get(0);
                tempPo.setSubName(Calendar.getInstance().get(Calendar.YEAR)+tempPo.getMainBusiness());
                sr.setSubName(tempPo.getSubName());
                sr.setSubLineName(tempPo.getSubLineName());
            }
        }

        //重新排个序
        list = list.stream()
                .sorted(Comparator.comparing(SubjectReport::getCreateDate))
                .sorted(Comparator.comparing(SubjectReport::getSubName))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BaseSubjectDetail> genBaseSubjectDetail(List<ZtReport> list1, List<MbReport> list2, List<BdReport> list3) {
        List<BaseSubjectDetail> genList = Lists.newArrayList();
        Map<String, List<ZtReport>> ztMap = list1.stream().filter(po -> (!po.getMainBusiness().contains("最-")||po.getInstructions().contains("活口"))).collect(Collectors.groupingBy(ZtReport::getMainBusiness));
        Map<String, List<MbReport>> mbMap = list2.stream().collect(Collectors.groupingBy(MbReport::getMainBusiness));
        Map<String, List<BdReport>> bdMap = list3.stream().collect(Collectors.groupingBy(BdReport::getMainBusiness));

        //涨停数量统计
//        Map<String, List<ZtReport>> ztMap = ztMap.s

        for (String str : ztMap.keySet()) {
            List<ZtReport> ztList = ztMap.get(str);
            List<ZtReport> mbList = ztMap.get(str);
            List<ZtReport> bdList = ztMap.get(str);

            Integer countZt = ztList.stream().filter(po -> "1".equals(po.getSource())).collect(Collectors.toList()).size();
            Integer countZthf = ztList.stream().filter(po -> "2".equals(po.getSource())).collect(Collectors.toList()).size();

            ZtReport zt = ztList.get(0);
            String date = DateUtil.format(zt.getCreateDate(), "yyyyMMdd");

            List<ZtReport> coreNameList = ztList.stream().filter(po -> {
                        String in = po.getInstructions();
                        if ( po.getCombo() >= 3) {
                            return true;
                        }
                        return false;
                    }
            ).limit(3).collect(Collectors.toList());
            String coreName = coreNameList.stream().map(po->po.getStockName()+po.getCombo()+"b").collect(Collectors.joining(","));
            //删除所有重复的标的
            ztList.removeAll(coreNameList);

            //高潮
            String helpName = "";
//            int count = ztList.size() / 4;
            List<ZtReport> helpNameList = Lists.newArrayList();
//            if (ztList.size() > 12) {
            helpNameList = ztList.stream()
                    .sorted(Comparator.comparing(ZtReport::getCombo, Comparator.nullsFirst(Integer::compareTo)))
                    .sorted(Comparator.comparing(ZtReport::getFinalHardenTime, Comparator.nullsFirst(Date::compareTo)))
                    .filter(po -> {
                                String in = po.getInstructions();
                                if (in.contains("加速") || po.getCombo() > 1 || in.contains("秒板") ||
                                        in.contains("龙") || in.contains("中军") || in.contains("连续加速")
                                    /*|| !in.contains("高度")|| in.contains("高度")|| in.contains("最高")
                                 || in.contains("活口")
                                || in.matches(".*[0-9]{1,2}")||*/) {
                                    return true;
                                }
                                return false;
                            }
                    ).limit(3).collect(Collectors.toList());
//            } else {
//                helpNameList = ztList.stream().sorted(Comparator.comparing(ZtReport::getFinalHardenTime, Comparator.nullsFirst(Date::compareTo)))
//                        .filter(po -> {
//                                    int i = 0;
//                                    if (i++ < count) {
//                                        return true;
//                                    }
//                                    return false;
//                                }
//                        ).collect(Collectors.toList());
//            }
            //删除所有重复的标的
            helpNameList.removeAll(coreNameList);
            helpName = helpNameList.stream().map(po->po.getStockName()+po.getCombo()+"b").collect(Collectors.joining((",")));


            //如果只有助攻数据，那么自动提高一级
//            if (StringUtils.isBlank(coreName) && StringUtils.isNotBlank(helpName)) {
//                coreName = helpName;
//                helpName = "";
//            }

            //如果两个都是空，那么默认取一个放到助攻那
            if (StringUtils.isBlank(coreName) && StringUtils.isBlank(helpName)) {
                helpName =  ztList.stream().map(po->po.getStockName()+po.getCombo()+"b").limit(2).collect(Collectors.joining(","));
            }


            BaseSubjectDetail detail = BaseSubjectDetail.builder()
                    .subName(NewsEnum.SCOPE_UNKNOW.getName())
                    .subLineName(date + zt.getMainBusiness())
                    .mainBusiness(zt.getMainBusiness())
                    .createDate(zt.getCreateDate())
                    .coreName(coreName)
                    .helpName(helpName)
                    .countZt(countZt)
                    .countZthf(countZthf)
                    .build();
            genList.add(detail);
        }


        return genList;
    }

    @Override
    public void getMarketDetail(List<ZtReport> list1, List<MbReport> list2, List<BdReport> list3) {
        Date date = list1.get(0).getCreateDate();
        //新增前先删除当天的数据
        EntityWrapper<BaseMarketDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", date);
        baseMarketDetailService.delete(wrapper);


        //将涨停、涨停回封、涨停炸板三块数据合并在一起
        List<BaseReportStock> upList = BeanUtil.copyToList(list1, BaseReportStock.class);
        List<MbReport> soource3List = list2.stream().filter(po -> {
            po.setHardenTime(po.getTouchTime());
            return "3".equals(po.getSource());
        }).collect(Collectors.toList());
        List<BaseReportStock> zthflist = BeanUtil.copyToList(soource3List, BaseReportStock.class);
        upList.addAll(zthflist);

        //过滤出资金进攻的时间块
        Map<String, List<BaseReportStock>> upMap = upList.stream().sorted(Comparator.comparing(BaseReportStock::getHardenTime, Comparator.nullsFirst(Date::compareTo))).collect(Collectors.groupingBy(BaseReportStock::getMainBusiness));
        for (String str : upMap.keySet()) {
            //寻找相同的时间块
            List<BaseReportStock> bkList = upMap.get(str);

            //小于3一般是
            if (bkList.size() < 3) {
                //活口逻辑
                bkList.stream().filter(po -> po.getInstructions().contains("活口")).collect(Collectors.toList());
                continue;
            }

            //模板
            log.info(bkList.get(0).getMainBusiness() + "size:{}", bkList.size());
            for (int i = 1; i < bkList.size(); i++) {
                BaseReportStock po0 = bkList.get(i - 1);
                BaseReportStock po1 = bkList.get(i);

                Date time1 = po0.getHardenTime();
                Date time11 = po0.getFinalHardenTime();
                Date time2 = po1.getHardenTime();
                Date time22 = po1.getFinalHardenTime();


                if (time11 != null && time22 != null) {
                    log.info("stockName1{}与stockName2{}双方--最终--涨停时间相差：{}", po0.getStockName(), po1.getStockName(), DateUtil.getDatePoor3(time11, time22));
                }
                if (time11 != null && time22 == null) {
                    log.info("stockName1--最终--时间{}与stockName2{}涨停时间相差：{}", po0.getStockName(), po1.getStockName(), DateUtil.getDatePoor3(time11, time2));
                }
                if (time11 == null && time22 != null) {
                    log.info("stockName1{}与stockName2{}--最终--涨停时间相差：{}", po0.getStockName(), po1.getStockName(), DateUtil.getDatePoor3(time1, time22));
                }
                log.info("stockName1{}与stockName2{}首次涨停时间相差：{}", po0.getStockName(), po1.getStockName(), DateUtil.getDatePoor3(time1, time2));

            }
        }


    }

    @Override
    public List<FixZtReport> getFixZtReports(String outNameList, String outMainBusiness, List<ZtReport> list) {
        List<String> nameList = StringUtils.isEmpty(outNameList) ? Lists.newArrayList() : Lists.newArrayList(outNameList.split(","));
        //涨停等于1的板块
        Map<String, Long> mainBusinessMap = list.stream().collect(Collectors.groupingBy(p -> p.getMainBusiness(), Collectors.counting()));
        List mainBusinessList = Lists.newArrayList();
        for (String k : mainBusinessMap.keySet()) {
            Long c = mainBusinessMap.get(k);
            if (c == 1) {
                mainBusinessList.add(k);
            }
        }
        //过滤出符合条件的数据
        list = list.stream().filter(p -> {
            String mainBusiness = p.getMainBusiness();
            //外部指定板块
            if (StringUtils.isNotBlank(outMainBusiness) && CollectionUtils.isNotEmpty(nameList) && outNameList.contains(p.getStockName())) {
                String newBusiness = StockUtil.addNewBusiness(p.getMainBusiness(), p.getNicheBusiness());
                p.setMainBusiness(outMainBusiness);
                p.setNicheBusiness(newBusiness);
                return true;
            }
            //高位去板块
            if (p.getCombo() > 1 && mainBusiness.contains("最-")) {
                p.setMainBusiness(mainBusiness.replaceAll("最-", ""));
                return true;
            }
            //首版板块单个标的
            if (p.getCombo() == 1 && mainBusinessList.contains(mainBusiness)) {
                p.setMainBusiness("最-" + mainBusiness.replaceAll("最-", ""));
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        //复制与最终排序
        List<FixZtReport> fixList = BeanUtil.copyToList(list, FixZtReport.class);
        fixList = fixList.stream()
                .sorted(Comparator.comparing(FixZtReport::getHardenTime, Comparator.nullsFirst(Date::compareTo)))
                .sorted(Comparator.comparing(FixZtReport::getCombo, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return fixList;
    }

    @Override
    public void inputFixZtRePort(InputStream inputStream) throws Exception {
        List<FixZtReport> fixZtReport = ExcelUtil.excelToList(inputStream, FixZtReport.class);
        List<BaseZtStock> ztStockList = BeanUtil.copyToList(fixZtReport, BaseZtStock.class);
        List<BaseZthfStock> zthfStockList = BeanUtil.copyToList(fixZtReport, BaseZthfStock.class);
        Date date = confDateService.getBeforeTypeDate(new Date(), DateConstant.DEAL_LIST);

        for (BaseZtStock po : ztStockList) {
            po = BaseZtStock.builder()
                    .stockName(po.getStockName())
                    .mainBusiness(po.getMainBusiness())
                    .nicheBusiness(po.getNicheBusiness())
                    .createDate(date)
                    .build();
            baseZtStockService.updatePo(po);
        }

        for (BaseZthfStock po : zthfStockList) {
            po = BaseZthfStock.builder()
                    .stockName(po.getStockName())
                    .mainBusiness(po.getMainBusiness())
                    .nicheBusiness(po.getNicheBusiness())
                    .createDate(date)
                    .build();
            baseZthfStockService.updatePo(po);
        }
    }

    private void setCx(BaseReportStock bs) {
        String cx = bs.getCxFlag();
        String instructions = bs.getInstructions();
        if (StringUtils.isNotBlank(cx) && !instructions.contains("次新")) {
            bs.setInstructions(instructions + cx);
        }
    }

    private void setZz(BaseReportStock bs) {
        String bond = bs.getBond();
        String instructions = bs.getInstructions();
        if (StringUtils.isNotBlank(bond) && !instructions.contains("含zz")) {
            bs.setInstructions(instructions + "含zz;");
        }
    }

    private void setSbTime(List<ZtReport> bkList) throws ParseException {

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
