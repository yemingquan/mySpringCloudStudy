package com.example.springBootDemo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.BaseDate;
import com.example.springBootDemo.service.BaseDateNewsService;
import com.example.springBootDemo.service.BaseDateService;
import com.example.springBootDemo.service.BaseDateSpecialService;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.HolidayUtil;
import com.example.springBootDemo.util.LunarSolarUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 配置表控制层
 *
 * @author makejava
 * @since 2023-08-17 18:37:58
 */
@Slf4j
@RestController
@RequestMapping("date")
@Api(tags = "基础数据-日期")
public class DateController {

    @Resource
    private BaseDateService baseDateService;
    @Resource
    private BaseDateSpecialService baseDateSpecialService;
    @Resource
    private BaseDateNewsService baseDateNewsService;

    @ApiOperationSupport(order = 1)
    @ApiOperation("初始化一年份数据")
    @PostMapping("/initDate")
    public RespBean initDate(@RequestParam(value = "year", required = false) int year) {
        try {
            EntityWrapper<BaseDate> wrapper = new EntityWrapper<>();
            wrapper.like("date", "" + year + "%");
            baseDateService.delete(wrapper);

            List<BaseDate> list = Lists.newArrayList();
            Calendar calendar = Calendar.getInstance();
            //一年的开始
            //设置日历时间，月份必须减一
            calendar.set(year, 1 - 1, 1);

            for (; calendar.get(Calendar.YEAR) < year + 1; ) {
                Date date = calendar.getTime();
                Date lunar = LunarSolarUtil.getLunarDate(date);
                String week = DateUtil.getWeek(date);
                List<String> weekend = DateConstant.WEEKEND;
                String type = "0";
                if (weekend.contains(week)) {
                    type = "1";
                }

                BaseDate po = BaseDate.builder()
                        .date(date)
                        .lunar(lunar)
                        .week(week)
                        .type(type)
                        .createDate(new Date())
                        .createBy("system")
                        .build();
//                log.info(DateUtil.format(date));

                list.add(po);
                calendar.add(Calendar.DATE, 1);
            }


            baseDateService.insertBatch(list, list.size());

            //节假日数据
            List<BaseDate> holidayList = HolidayUtil.getYearHoliday("" + year);
            baseDateService.updateBatchByDate(holidayList);
        } catch (Exception e) {
            RespBean.error("处理失败");
            e.printStackTrace();
        }
        return RespBean.success("处理成功");
    }

}

