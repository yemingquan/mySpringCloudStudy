package com.example.springBootDemo.config.components.constant;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-8 17:50
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class StockConstant {
    public static List<String> BSD_STOCK_LIST;

    {
        BSD_STOCK_LIST = Lists.newArrayList(

        );
    }

    @AllArgsConstructor
    public enum BusinessTypeEnum {
        FLAG("flag", "标记"),
        INDUSTRY("industry", "行业"),
        SUBJECT("subject", "题材"),
        ATTR("attr(", "属性");

        @Setter
        @Getter
        private String code;

        @Setter
        @Getter
        private String name;
    }


    @AllArgsConstructor
    public enum PlateEnum {
        MAIN("MAIN", "主板"),
        CYB("300", "创业板"),
        KCB("688", "科创板"),
        ST("ST", "ST");

        @Setter
        @Getter
        private String prefix;

        @Setter
        @Getter
        private String name;
    }

    @AllArgsConstructor
    public enum ExchangeEnum {
        BSE("", "北交所"),
        SSE("SH", "上交所"),
        SZSE("SZ", "深交所");

        @Setter
        @Getter
        private String prefix;

        @Setter
        @Getter
        private String name;
    }

    @AllArgsConstructor
    public enum SpecilNameEnum {
        /**
         * 股票前缀含义盘点
         * 　　一、xr，xr的全拼是Exclud Right，该前缀的意思就是这只股票已经除权，购买这只股票不会再享受股票分红权利，不过这个前缀在第二个交易日就会自动消失恢复到原来的简称。
         * 　　二、dr，dr的全拼是Dividend Right，该前缀的意思是这只股票已经除权除息，购买这只股票不会再享受送股派息的权利，和xr一样，这个前缀会在第二个交易日自动消失，恢复到原来的简称。
         * 　　三、xd，xd的全拼是Without Dividend，意思上文中我们解释过了，此外这个前缀也是在第二个交易日自动消失，恢复到原来的简称。
         * 　　四、st，加了st的意思就是这个公司在连续两个会计年度都出现亏损，这是对其的特殊处理，st就是亏损股。
         * 　　五、*st，*st股表示的是连续三年亏损，股票具有退市风险，如果想要购买这只股票就要有更好的基本面分析能力。
         * 　　六、n，新股上市第一天就会在股票简称前面加上一个n的字母，它的全拼就是new，就是新的意思，此外，增发、重组、股改之后复牌第一个交易日也是用字母n来进行区别，这个前缀也是在第二个交易日自动消失恢复到原来的简称。
         *
         * 加c表示该股是上市后次日至第五日之间，对于科创板或者注册制的个股来说，在此期间不设涨跌幅限制，这是用来区分与其它有涨跌幅限制的个股。
         * 股票名称后加U ：股票名称后加特殊标识U，则代表该股票发行人尚未盈利，如上市后首次实现盈利的，则特别标识U取消。
         * 股票名称后加W：股票名称后加特殊标识W，则代表该股票发行人具有表决权差异安排；如上市后不再具有表决权差异安排的，则特别标识W取消。
         *
         * 加R代表这个股票是融资融券标的意思。
         * ST，这是对连续两个会计年度都出现亏损的公司施行的特别处理。ST即为亏损施王股。
         * *ST，是连续三年亏损，有退市风险的意思，购买这样的股票要有比较好的基本面分析能力。
         * N，新股上市首日的名称前都会加一个字母N，即英文NEW的意思。
         * S*ST，指公司经营连续三年亏损，进行退市预警和还没有完成股改。
         * SST，指公司经营连续二年亏损进行的特别处理和还没有完成股改。
         * S，还没有进行或完成股改的股票。
         * NST，经过重组或股改重新恢复上市的ST股。
         * PT，退市的股票。
         */
//        WD("WD", "发行人具有表决权差异安排,以CDR(中国存托凭证)形式登陆科创板"),
//        UW("UW", "发行人尚未盈利,发行人具有表决权差异安排"),
//        U("U", "发行人尚未盈利"),
//        W("W", "发行人具有表决权差异安排"),
//        D("D", "以CDR(中国存托凭证)形式登陆科创板"),

        XR("XR", "已经除权"),
        DR("DR", "已经除权除息"),
//        ST("ST", ""),//亏损股
//        ST_2("\\*ST", ""),//退市风险

        N("N", "新股"),
        C("C", "上市后次日至第五日之间");

        @Setter
        @Getter
        private String code;

        @Setter
        @Getter
        private String tip;

        public static List<String> getCodeList() {
            return Arrays.stream(values()).map(SpecilNameEnum::getCode).collect(Collectors.toList());
        }

        public static String getTip(String name) {
            SpecilNameEnum en = Arrays.stream(values()).filter(po -> {
                return name.matches("(" + po.getCode() + ")+[\u4e00-\u9fa5]*") || name.matches("[\u4e00-\u9fa5]*-?(" + po.getCode() + ")+");
            }).findFirst().orElse(null);
            return en == null ? "" : name + " " + en.getTip();
        }

        public static void main(String[] args) {
//            System.out.println(getTip("九号公司-WD"));
//            System.out.println(getTip("TCL科技"));
//            System.out.println(getTip("*ST中捷"));
//            System.out.println(getTip("ST中捷"));
//            System.out.println(getTip("盛科通信-U"));
//            System.out.println(getTip("C恒兴"));
        }
    }


}
