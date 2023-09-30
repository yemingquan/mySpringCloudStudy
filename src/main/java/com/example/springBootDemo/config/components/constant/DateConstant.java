package com.example.springBootDemo.config.components.constant;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-8 17:50
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Service
public class DateConstant {

    public static final String DATE_FORMAT_10 = "yyyy-MM-dd";

    //0工作日、1周末、2节日、3调休
    public static final String DATE_TYPE_WORK = "0";
    public static final String DATE_TYPE_WORKEND = "1";
    public static final String DATE_TYPE_HOLIDAY = "2";
    public static final String DATE_TYPE_CHANGE = "3";

    public static List<String> DEAL_LIST;
    public static List<String> WORK_LIST;
    public static List<String> REST_LIST;
    public static List<String> HOLIDAY_LIST;
    public static List<String> WEEKEND;

    {
        DEAL_LIST = Lists.newArrayList(DATE_TYPE_WORK);
        WORK_LIST = Lists.newArrayList(DATE_TYPE_WORK, DATE_TYPE_CHANGE);
        REST_LIST = Lists.newArrayList(DATE_TYPE_WORKEND, DATE_TYPE_HOLIDAY);
        HOLIDAY_LIST = Lists.newArrayList(DATE_TYPE_HOLIDAY);
        WEEKEND =  Lists.newArrayList("星期六", "星期日");
    }

}
