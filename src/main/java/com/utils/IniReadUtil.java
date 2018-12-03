package com.utils;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置读取工具
 */
public class IniReadUtil {

    public static Map<String, String> readIniGetImageSocketConfig() {
        Map<String, String> map = new HashMap<>();
        map.put("ip", "127.0.0.1");
        map.put("port", "8888");
        try {
            File file = new File("wsConfig.ini");
            if (!file.exists() && !file.isDirectory()) {
                return map;
            } else {
                Ini.Section section = new Ini(file).get("ImageSocket");
                String ip = section.get("ip");
                String port = section.get("port");
                map.put("ip", ip);
                map.put("port", port);
            }
            return map;
        } catch (IOException ignore) {
            LogUtil.markLog(2,"读取配置文件失败，详情：[ImageSocket]");
        }
        return map;
    }

    public static Map<String, String> readIniGetCallSocketConfig() {
        Map<String, String> map = new HashMap<>();
        map.put("port", "8888");
        map.put("ip", "127.0.0.1");
        try {
            File file = new File("wsConfig.ini");
            if (!file.exists() && !file.isDirectory()) {
                return map;
            } else {
                Ini.Section section = new Ini(file).get("CallSocket");
                String ip = section.get("ip");
                String port = section.get("port");
                map.put("ip", ip);
                map.put("port", port);
            }
            return map;
        } catch (IOException ignore) {
            LogUtil.markLog(2,"读取配置文件失败，详情：[CallSocket]");
        }
        return map;
    }

    public static Integer readIniGetListenerPort() {
        try {
            File file = new File("wsConfig.ini");
            if (!file.exists() && !file.isDirectory()) {
                return null;
            } else {
                Ini.Section section = new Ini(file).get("ListenerPort");
                String port = section.get("port");
                return Integer.parseInt(port);
            }
        } catch (IOException ignore) {
            LogUtil.markLog(2,"读取配置文件失败，详情：[ListenerPort]");
        }
        return null;
    }

    public static Map<String, String> readIniGetDBConfig() {
        Map<String, String> map;
        try {
            File file = new File("wsConfig.ini");
            if (!file.exists() && !file.isDirectory()) {
                return null;
            } else {
                map = new HashMap<>();
                Ini.Section section = new Ini(file).get("DBConfig");
                String driver = section.get("driver");
                String url = section.get("url");
                String userName = section.get("userName");
                String password = section.get("password");
                map.put("driver", driver);
                map.put("url", url);
                map.put("userName", userName);
                map.put("password", password);
            }
            return map;
        } catch (IOException ignore) {

        }
        return null;
    }

}
