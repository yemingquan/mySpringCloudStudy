package com.example.springBootDemo.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/4/29 19:11
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class StockUtil {


    /**
     * 字符串
     * @param str
     * @return
     */
    public static String calibrateHalfAngle(String str) {
        if(StringUtils.isBlank(str)){
            return str;
        }
        return str.replaceAll("，", ",");
    }

    public static void main(String[] args) {

//        ExcelUtil.importtExcel(MultipartFile file, Class<T> pojoClass);
//      数据输入

        StringBuilder stb = new StringBuilder("【互联网医疗;华为概念;辅助生殖;国产软件】\n" +
                "【新股与次新股;核准制次新股】\n" +
                "【重庆自贸区;两江新区;铁矿石;余热发电;成渝特区;沪股通;特钢概念;融资融券;转融券标的;富时罗素概念;富时罗素概念股;标普道琼斯A股;一季报预增;央企国资改革】\n" +
                "【一季报预增;新股与次新股;核准制次新股】\n" +
                "【央企控股;振兴东北;融资融券;转融券标的;基建工程;一带一路;3D打印;国改双百行动;债转股;口罩;医疗废物处理;富时罗素概念股;标普道琼斯A股;碳交易;节能环保;碳中和;工业4.0;一季报预增;央企国资改革】\n" +
                "【页岩气;中原经济区;超硬材料;军工;金刚石（线）;融资融券;转融券标的;富时罗素概念股;标普道琼斯A股;新材料概念】\n" +
                "【二胎概念;网红经济;乳业;家用电器;消毒剂;医美概念;新零售;一季报预增】\n" +
                "【新股与次新股;核准制次新股】\n" +
                "【人造肉;烟草;电子烟】\n" +
                "【铟;锌;涉矿;稀有金属;锂电池;锂矿;沪股通;一带一路;锂电原料;小金属概念;富时罗素概念;免税店;富时罗素概念股;标普道琼斯A股;铜】\n" +
                "【稀有金属;赣南振兴;融资融券;钨;转融券标的;稀土永磁;富时罗素概念股;富时罗素概念;小金属概念】\n" +
                "【军工;节能环保;节能电机;核电;一带一路;融资融券;转融券标的;标普道琼斯A股】\n" +
                "【循环经济;焦炭;陕西自贸区;粗苯;煤化工;尿素;天然气;融资融券;转融券标的;甲醇;标普道琼斯A股;煤炭概念;污水处理】\n" +
                "【养老概念;三沙;海南旅游岛;保健品;互联网彩票;海南自贸区;白酒概念;生物质能;摘帽;老字号】\n" +
                "【车联网;新能源整车;百度概念;阿里巴巴概念;华为概念;融资融券;转融券标的;室外经济;富时罗素概念股;新能源汽车;无人驾驶;标普道琼斯A股;参股银行;固态电池;智能汽车;华为汽车】\n" +
                "【食盐;海水淡化;溴素;三聚氰胺;白炭黑;硝酸钠;硫酸钾;污水处理;油品改革;股权转让;地方国资改革】\n" +
                "【稀有金属;鄱阳湖经济区;涉矿;高岭土概念;赣南振兴;稀缺资源;节能电机;智能玻璃;电机电控;正极材料;锂电原料;锂矿;锂电池;军工;标普道琼斯A股;新能源汽车;富时罗素概念股;摘帽】\n" +
                "【氨纶;参股券商;粘胶短纤;风电;南京国资改革;地方国资改革;江苏国资改革】\n" +
                "【铝材加工;铝电解电容;新能源整车;有色铝;无人驾驶;锂电池;新能源汽车】\n" +
                "【黄金水道;电子信息;车联网;现代服务业;智能交通;智慧城市;SAAS;雷达;集成电路概念;军工;无人驾驶;物联网;ETC;区块链】\n" +
                "【脱硫脱硝;PPP概念;废气处理;大数据;PM2.5;环境监测;雄安新区;节能环保;固废处理;透明工厂;碳中和】\n" +
                "【金融IC;核准制次新股;新股与次新股;数字货币】\n" +
                "【涉矿;地方国资改革;融资融券;转融券标的;山西国资改革;物流电商平台;HIT电池;光伏概念;煤炭概念】\n");

        //初始化-替换
        datareplace(stb);
        //过滤
        String[] arr = dataFilter(stb);

        //数据展示
        dataShow(arr);
    }

    private static void dataShow(String[] arr) {
        Map<String, Long> map = Arrays.stream(arr).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>(map.entrySet());

        //然后通过比较器来实现排序

        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {

            //降序排序
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {

                return o2.getValue().compareTo(o1.getValue());

            }

        });

        StringBuffer sb = new StringBuffer();
        int count = 0;
        for (Map.Entry<String, Long> mapping : list) {
            if (count++ >= 10) {
                break;
            }
            sb
                    .append(count + ".")
                    .append(mapping.getKey())
                    .append("-----" + mapping.getValue())
                    .append("\n")
            ;

        }
        log.info("\n[\n{}]", sb);
    }

    private static String[] dataFilter(StringBuilder stb) {
        List<String> blankList = Lists.newArrayList("国资驰援;","环渤海;","口罩;","农垦改革;","玉米;","贸易战受益股;","军民融合;","乡村振兴;","参股银行;","央企控股;","有色铝;","雄安新区;","新股与次新股;","小金属概念;","核准制次新股;","央企国资改革;","机构重仓;", "融资融券;", "转融券标的;", "标普道琼斯A股;", "富时罗素概念;", "富时罗素概念股;", "沪股通;", "MSCI概念;", "深股通;", "地方国资改革;", "一带一路;", "工业4.0;", "电子商务;", "一季报预增;", "股权转让;", "大数据;", "期货概念;", "甲醇;", "粘胶短纤;", "老字号;", "超级品牌;", "网络直播;");
        for (String s : blankList) {
            StringBuilderUtil.replaceAll(stb, s, "");
        }

        return stb.toString().split(";");
    }

    private static void datareplace(StringBuilder stb) {
        StringBuilderUtil.replaceAll(stb, "--", "");
        StringBuilderUtil.replaceAll(stb, " ", "");
        StringBuilderUtil.replaceAll(stb, "【", "");
        StringBuilderUtil.replaceAll(stb, "\n", "");
        StringBuilderUtil.replaceAll(stb, "】", ";");
        StringBuilderUtil.replaceAll(stb, ";;", ";");
    }


}
