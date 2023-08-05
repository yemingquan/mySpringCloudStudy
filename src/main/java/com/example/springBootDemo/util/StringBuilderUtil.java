package com.example.springBootDemo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @所属模块 工具包
 * @描述 字符串处理
 * @创建人 xiaoYe
 * @创建时间 2020/5/16 8:58
 * @备注
 */
@Slf4j
public class StringBuilderUtil {
    public static void appendStrList(StringBuffer sb, List<String> list) {
        if (list == null) return;
        for (String str : list) {
            sb.append(str).append("\n");
        }
    }

    /**
     * 根据api名字获取类名
     *
     * @param inputAPIName
     */
    private static String getGenMessageNameByAPIName(String inputAPIName) {
        inputAPIName = inputAPIName.toLowerCase();
        String[] nameArr = inputAPIName.split("_");
        String outAPIName = "";
        for (int i = 0; i < nameArr.length; i++) {
            if (i == 0) continue;
            outAPIName = outAPIName + changeNameInitial(nameArr[i], true);
        }
        log.info("初始化java类名！[{}]", outAPIName);
        return outAPIName;
    }

    /**
     * 首字母变化
     * 默认大写
     *
     * @param name
     * @return
     */
    public static String changeNameInitial(String name, Boolean flag) {
        char[] cs = name.toCharArray();
//        大写
        if (flag) {
            cs[0] -= 32;
        } else {
            cs[0] += 32;
        }
        return String.valueOf(cs);
    }

    /**
     * 实现StringBuilder的replaceAll
     *
     * @param stb
     * @param oldStr 被替换的字符串
     * @param newStr 替换oldStr
     * @return
     */
    public static StringBuilder replaceAll(StringBuilder stb, String oldStr, String newStr) {
        if (stb == null || oldStr == null || newStr == null || stb.length() == 0 || oldStr.length() == 0)
            return stb;
        int index = stb.indexOf(oldStr);
        if (index > -1 && !oldStr.equals(newStr)) {
            int lastIndex = 0;
            while (index > -1) {
                stb.replace(index, index + oldStr.length(), newStr);
                lastIndex = index + newStr.length();
                index = stb.indexOf(oldStr, lastIndex);
            }
        }
        return stb;
    }
}
