package com.utils.connectionUtil;

import com.utils.IniReadUtil;
import com.utils.LogUtil;

import java.util.Map;

public class ConnectionPoolUtils {

    private static String driver = "";
    private static String url ="";
    private static String userName = "";
    private static String password = "";

    //私有静态方法保证单例
    private ConnectionPoolUtils(){
        //读取Ini配置
        Map<String,String> map = IniReadUtil.readIniGetDBConfig();
        if (map!=null){
            driver = map.get("driver");
            url = map.get("url");
            userName = map.get("userName");
            password = map.get("password");
        } else {
            LogUtil.markLog(2,"配置读取有误。");
            System.exit(0);
        }
    }

    private static ConnectionPool poolInstance = null;

    //获取连接
    public static ConnectionPool GetPoolInstance(){
        if(poolInstance == null) {
            poolInstance = new ConnectionPool(driver, url, userName, password);
            try {
                poolInstance.createPool();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return poolInstance;
    }


}
