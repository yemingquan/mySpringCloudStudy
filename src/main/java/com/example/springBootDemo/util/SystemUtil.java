package com.example.springBootDemo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @所属模块
 * @描述
 * @创建人 xiaoYe
 * @创建时间 2020/6/6 15:31
 * @备注
 */
@Slf4j
public class SystemUtil {

    /**
     * 打印所有系统信息
     */
    public static void showAllSystemInfo() {
        String[] keys = {"java.version", "java.vendor", "java.vendor.url", "java.home",
                "java.vm.specification.version", "java.vm.specification.vendor", "java.vm.specification.name",
                "java.vm.version", "java.vm.vendor", "java.vm.name", "java.specification.version",
                "java.specification.vendor", "java.specification.name", "java.class.version", "java.class.path",
                "java.library.path", "java.io.tmpdir", "java.compiler", "java.ext.dirs", "os.name", "os.arch",
                "os.version", "file.separator", "path.separator", "line.separator", "user.name", "user.home",
                "user.dir"};

        for (String key : keys) {
            String value = System.getProperty(key);
            log.info(key + " : " + value);
        }
    }

    /**
     * 打印系统信息
     */
    public static void showSystemInfo() {
        String[] keys = {"java.version", "java.home", "os.name", "os.arch", "os.version"};

        for (String key : keys) {
            String value = System.getProperty(key);
            log.info(key + " : " + value);
        }
    }

    /**
     * 打印系统其他信息
     */
    public static void showOtherSystemInfo() {
        String[] keys = {"file.separator","path.separator", "line.separator", "user.name", "user.home", "user.dir"};

        for (String key : keys) {
            String value = System.getProperty(key);
            log.info(key + " : " + value);
        }

    }

    /**
     * 获取类加载路径
     *
     * @return
     */
    public static String getClassPath() {
        return System.getProperty("user.dir") + File.separator;
    }


    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return "";
    }

}
