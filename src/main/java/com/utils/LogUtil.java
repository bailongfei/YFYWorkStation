package com.utils;

import com.entities.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class LogUtil {

    public static void markLog(Integer status,String message){
        Log log = new Log(status,message);
        String fileName = new SimpleDateFormat("yyyy-MM-dd").format(log.getLogDate())+"Log.txt";
        File file = new File("Log/"+fileName);
        if (file.exists()){
            logWrite(file,log);
        } else {
            try {
                File dir = new File("Log");
                if (dir.exists()&&dir.isDirectory()){
                    file.createNewFile();
                } else {
                    dir.mkdir();
                    file.createNewFile();
                }
                logWrite(file,log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void logWrite(File file,Log log){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.write("\n*****************************\r\n");
            writer.write(log.toString());
            writer.write("\r\n*****************************");
            writer.write("\r\n");
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
