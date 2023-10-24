package com.example.springBootDemo.service.impl.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.enums.SendTypeEnum;
import com.example.springBootDemo.config.components.system.SystemConfConstant;
import com.example.springBootDemo.entity.BaseStock;
import com.example.springBootDemo.entity.SendVo;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.msg.SendMsgHandle;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-10-13 16:48
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */


@Component
@Slf4j
public class TaskService {

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
    ConfDateService confDateService;
    @Autowired
    BaseStockService baseStockService;
    @Resource
    private BaseBondService baseBondService;
    @Resource
    private ConfStockService confStockService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void monitorTask() {
        try {
            monitorTaskMethod(null);
        } catch (Exception e) {
            log.error("监控任务执行失败{}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 5 0 * * ?")
    public void finalTask() {
        try {
            finalTask(null);
        } catch (Exception e) {
            log.error("日终任务执行失败{}", e.getMessage());
        }
    }

    /**
     * 执行一些日终任务
     * @param date
     */
    public void finalTask(String date) throws Exception {
        //获得交易日日期
        date = getTaskDealDate(date);
        log.info("次新刷新");
        //TODO 老的方法要去掉
//            confCxStockService.imporCX();
        confStockService.reflshCX();
        log.info("小盘刷新");
//        confStockService.reflshSmallStock(date);
        log.info("可转债");
        baseBondService.imporKZZ();
        log.info("增量刷新股票的主业");
        confStockService.reflshMyStock(date);
        log.info("日期功能刷新");
        //TODO 日期刷新最好不要用外部传入时间
    }

    /**
     * 触发时间：每天凌晨执行一次，项目启动时会自动补偿
     * <p>
     * 需要监控：
     * 前面一个交易日的基础数据 ：1+6
     * （可扩展）题材+市场概要
     * <p>
     * 触发条件：数据库中对应的数据不存在
     * 通知方式：邮件
     *
     * @param date
     * @return
     */
    public void monitorTaskMethod(String date) throws Exception {
        //获得交易日日期
        date = getTaskDealDate(date);

        String taskName = "监控任务";
        log.info("{}启动", taskName);
        StringBuffer bf = new StringBuffer("<p>监控任务警告：</p>");
        Boolean sendFlag = false;
        //检索数据是否成功落库
//        date = getTaskDealDate(date);

        //获取基础数据，用于后续的数据生成
        List<ZtReport> list1 = baseZtStockService.getZtReportByDate(date);
        List<ZtReport> list2 = baseZthfStockService.getZtReportByDate(date);
        List<MbReport> list3 = baseZbStockService.getMbReportByDate(date);
        List<MbReport> list4 = baseDtStockService.getMbReportByDate(date);
        List<BdReport> list5 = baseBdUpStockService.getBdReportByDate(date);
        List<BdReport> list6 = baseBdDownStockService.getBdReportByDate(date);

        EntityWrapper<BaseStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", date);
        BaseStock baseStock = baseStockService.selectOne(wrapper);


        if (CollectionUtils.isEmpty(list1)) {
            bf.append("基础数据-涨停-未落库<br>");
            sendFlag = true;
        }
        if (CollectionUtils.isEmpty(list2)) {
            bf.append("基础数据-涨停回封-未落库<br>");
            sendFlag = true;
        }
        if (CollectionUtils.isEmpty(list3)) {
            bf.append("基础数据-炸板-未落库<br>");
            sendFlag = true;
        }
        if (CollectionUtils.isEmpty(list4)) {
            bf.append("基础数据-跌停-未落库<br>");
            sendFlag = true;
        }
        if (CollectionUtils.isEmpty(list5)) {
            bf.append("基础数据-涨停-未落库<br>");
            sendFlag = true;
        }
        if (CollectionUtils.isEmpty(list6)) {
            bf.append("基础数据-涨停-未落库<br>");
            sendFlag = true;
        }
        if (baseStock == null) {
            bf.append("基础数据-基础数据-未落库<br>");
            sendFlag = true;
        }


        //邮件发送
        if (sendFlag) {
            bf.append("<p align=\"right\">来自于-复盘系统-" + taskName + "-自动发送</p><br>");
            //如果没有落库则会生成邮件
            SendVo sendVo = SendVo.builder()
                    .title("监控任务-基础数据落库异常")
                    .content(bf.toString())
                    .receiver(SystemConfConstant.receiver)//处理者账号
                    .sendType(SendTypeEnum.EMAIL.getCode())//邮件
                    .build();

            //异步线程推送
            new Thread(() -> {
                log.info("异步推送预警短信!");
                SendMsgHandle sendMsgHandle = new SendMsgHandle(sendVo);
                sendMsgHandle.sendMsg();
            }).start();


        }

        //TODO 定时任务表 补偿用
        log.info("监控任务结束");

    }

    public String getTaskDealDate(String date) throws Exception {
        //取得交易日日期
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
        //交易日日期-1 这里如果减了一天是非交易日，那么还要调用一下日期配置
        Date dateStr = DateUtil.getDayDiff(date, -1);

        date = DateUtil.format(dateStr, DateConstant.DATE_FORMAT_10);
        //前一个交易日的交易日期
        date = confDateService.getBeforeTypeDate(date, DateConstant.DEAL_LIST);
        return date;
    }

}
