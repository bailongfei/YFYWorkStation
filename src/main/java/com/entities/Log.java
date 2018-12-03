package com.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private Date logDate = new Date();

    private Integer logStatus;

    private String logStatusDesc;

    private String logMessage;

    public Date getLogDate() {
        return logDate;
    }

    private Integer getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }

    private String getLogStatusDesc() {
        logStatusDesc = "异常:";
        if (logStatus.equals(1)){
            logStatusDesc = "信息:";
        }
        return logStatusDesc;
    }

    public void setLogStatusDesc(String logStatusDesc) {
        this.logStatusDesc = logStatusDesc;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public Log(Integer logStatus, String logMessage) {
        this.logStatus = logStatus;
        this.logMessage = logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    @Override
    public String toString() {
        String toString = "日期:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logDate);
        toString = "\n"+ toString+ getLogStatusDesc()+getLogStatus()+"\n";
        toString = toString+logMessage;
        return toString;
    }
}
