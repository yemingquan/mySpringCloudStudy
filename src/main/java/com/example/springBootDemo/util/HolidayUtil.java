package com.example.springBootDemo.util;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-9-3 1:17
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springBootDemo.entity.ConfDate;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.*;

/**
 * @Author xiaoliu
 * @Date 2023/7/6 14:44
 * @Version 1.0
 * 判断今天是工作日/周末/节假日 工具类
 * //0 上班 1周末 2节假日
 */
@Slf4j
public class HolidayUtil {


    static Map<String, List<String>> holiday = new HashMap<>();//假期
    static Map<String, List<String>> extraWorkDay = new HashMap<>();//调休日


    /**
     * @param year 日期参数 格式‘yyyy’，不传参则默认当前日期
     * @return
     */
    public static List<ConfDate> getYearHoliday(String year) throws ParseException {
        log.info("开始处理节假日数据");

        //获取免费api地址
        String httpUrl="https://timor.tech/api/holiday/year/"+year;
//        String httpUrl = "https://www.baidu.com/";
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        List<ConfDate> list = Lists.newArrayList();
        List hoList = new ArrayList<>();
        List extraList = new ArrayList<>();

        try {
            URL url = new URL(httpUrl);
            URLConnection connection = url.openConnection();
            //connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setRequestProperty("User-Agent", "Mozilla/4.76");
            //使用Get方式请求数据
            //connection.setRequestMethod("GET");
            //connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();

            //字符串转json
            JSONObject json = JSON.parseObject(sbf.toString());
//            JSONObject json = JSON.parseObject("{\"code\":0,\"holiday\":{\"01-01\":{\"holiday\":true,\"name\":\"元旦\",\"wage\":3,\"date\":\"2023-01-01\",\"rest\":1},\"01-02\":{\"holiday\":true,\"name\":\"元旦\",\"wage\":2,\"date\":\"2023-01-02\",\"rest\":1},\"01-21\":{\"holiday\":true,\"name\":\"除夕\",\"wage\":3,\"date\":\"2023-01-21\",\"rest\":2},\"01-22\":{\"holiday\":true,\"name\":\"初一\",\"wage\":3,\"date\":\"2023-01-22\",\"rest\":1},\"01-23\":{\"holiday\":true,\"name\":\"初二\",\"wage\":3,\"date\":\"2023-01-23\",\"rest\":1},\"01-24\":{\"holiday\":true,\"name\":\"初三\",\"wage\":3,\"date\":\"2023-01-24\",\"rest\":1},\"01-25\":{\"holiday\":true,\"name\":\"初四\",\"wage\":2,\"date\":\"2023-01-25\",\"rest\":1},\"01-26\":{\"holiday\":true,\"name\":\"初五\",\"wage\":2,\"date\":\"2023-01-26\",\"rest\":1},\"01-27\":{\"holiday\":true,\"name\":\"初六\",\"wage\":2,\"date\":\"2023-01-27\",\"rest\":1},\"01-28\":{\"holiday\":false,\"name\":\"春节后补班\",\"wage\":1,\"after\":true,\"target\":\"春节\",\"date\":\"2023-01-28\",\"rest\":1},\"01-29\":{\"holiday\":false,\"name\":\"春节后补班\",\"wage\":1,\"after\":true,\"target\":\"春节\",\"date\":\"2023-01-29\",\"rest\":1},\"04-05\":{\"holiday\":true,\"name\":\"清明节\",\"wage\":3,\"date\":\"2023-04-05\",\"rest\":67},\"04-23\":{\"holiday\":false,\"name\":\"劳动节前补班\",\"wage\":1,\"target\":\"劳动节\",\"after\":false,\"date\":\"2023-04-23\",\"rest\":1},\"04-29\":{\"holiday\":true,\"name\":\"劳动节\",\"wage\":2,\"date\":\"2023-04-29\",\"rest\":7},\"04-30\":{\"holiday\":true,\"name\":\"劳动节\",\"wage\":2,\"date\":\"2023-04-30\",\"rest\":1},\"05-01\":{\"holiday\":true,\"name\":\"劳动节\",\"wage\":3,\"date\":\"2023-05-01\",\"rest\":1},\"05-02\":{\"holiday\":true,\"name\":\"劳动节\",\"wage\":3,\"date\":\"2023-05-02\",\"rest\":1},\"05-03\":{\"holiday\":true,\"name\":\"劳动节\",\"wage\":3,\"date\":\"2023-05-03\",\"rest\":1},\"05-06\":{\"holiday\":false,\"name\":\"劳动节后补班\",\"after\":true,\"wage\":1,\"target\":\"劳动节\",\"date\":\"2023-05-06\",\"rest\":2},\"06-22\":{\"holiday\":true,\"name\":\"端午节\",\"wage\":3,\"date\":\"2023-06-22\",\"rest\":2},\"06-23\":{\"holiday\":true,\"name\":\"端午节\",\"wage\":3,\"date\":\"2023-06-23\",\"rest\":1},\"06-24\":{\"holiday\":true,\"name\":\"端午节\",\"wage\":2,\"date\":\"2023-06-24\",\"rest\":1},\"06-25\":{\"holiday\":false,\"name\":\"端午节后补班\",\"wage\":1,\"target\":\"端午节\",\"after\":true,\"date\":\"2023-06-25\",\"rest\":1},\"09-29\":{\"holiday\":true,\"name\":\"中秋节\",\"wage\":3,\"date\":\"2023-09-29\",\"rest\":27},\"09-30\":{\"holiday\":true,\"name\":\"中秋节\",\"wage\":3,\"date\":\"2023-09-30\",\"rest\":1},\"10-01\":{\"holiday\":true,\"name\":\"国庆节\",\"wage\":3,\"date\":\"2023-10-01\",\"rest\":1},\"10-02\":{\"holiday\":true,\"name\":\"国庆节\",\"wage\":3,\"date\":\"2023-10-02\",\"rest\":1},\"10-03\":{\"holiday\":true,\"name\":\"国庆节\",\"wage\":2,\"date\":\"2023-10-03\",\"rest\":1},\"10-04\":{\"holiday\":true,\"name\":\"国庆节\",\"wage\":2,\"date\":\"2023-10-04\",\"rest\":1},\"10-05\":{\"holiday\":true,\"name\":\"国庆节\",\"wage\":2,\"date\":\"2023-10-05\",\"rest\":1},\"10-06\":{\"holiday\":true,\"name\":\"国庆节\",\"wage\":2,\"date\":\"2023-10-06\",\"rest\":1},\"10-07\":{\"holiday\":false,\"after\":true,\"wage\":1,\"name\":\"国庆节后补班\",\"target\":\"国庆节\",\"date\":\"2023-10-07\",\"rest\":1},\"10-08\":{\"holiday\":false,\"after\":true,\"wage\":1,\"name\":\"国庆节后补班\",\"target\":\"国庆节\",\"date\":\"2023-10-08\",\"rest\":1},\"12-31\":{\"holiday\":true,\"name\":\"元旦\",\"wage\":2,\"date\":\"2023-12-31\",\"rest\":78}}}");
            //根据holiday字段获取jsonObject内容
            JSONObject holiday = json.getJSONObject("holiday");


            for (Map.Entry<String, Object> entry : holiday.entrySet()) {
                String value = entry.getValue().toString();
                JSONObject jsonObject = JSONObject.parseObject(value);
                String hoBool = jsonObject.getString("holiday");
                String extra = jsonObject.getString("date");
                Date date = DateUtil.parseDate(extra);
                String name = jsonObject.getString("name");

                Date lunar = LunarSolarUtil.getLunarDate(date);
                String week = DateUtil.getWeek(date);


                //判断不是假期后调休的班
                if (hoBool.equals("true")) {
                    hoList.add(extra);
                    HolidayUtil.holiday.put(year, hoList);
                } else {
                    extraList.add(extra);
                    HolidayUtil.extraWorkDay.put(year, extraList);
                }

                String type = "";
                if (hoBool.equals("true")) {
                    type = "2";
                } else {
                    type = "3";
                }
                ConfDate confDate = ConfDate.builder()
                        .date(date)
//                        .lunar(lunar)
//                        .week(week)
                        .name(name)
                        .type(type)
                        .updateFlag("1")
                        .modifedDate(new Date())
                        .modifedBy("system")
                        .build();

                list.add(confDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {
        String year = "2023";
        List<ConfDate> list = getYearHoliday(year);
        System.out.println("holiday:"+holiday);
        System.out.println("extraWorkDay:"+extraWorkDay);
        System.out.println(list);
    }
}
