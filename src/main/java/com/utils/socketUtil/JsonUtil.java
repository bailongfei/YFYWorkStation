package com.utils.socketUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    private static StringProperty queuePos = new SimpleStringProperty("");
    private static StringProperty remainingCustomerNum = new SimpleStringProperty("");

    public static StringProperty getQueuePos() {
        return queuePos;
    }

    public static StringProperty getRemainingCustomerNum(){
        return remainingCustomerNum;
    }

    public static Map<String,String> analysisJson(String jsonString){
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Map<String,String> jsonMap = new HashMap<>();
        jsonMap.put("id",jsonObject.get("id").toString());
        jsonMap.put("remainingCustomerNum",jsonObject.get("remainingCustomerNum").toString());
        Platform.runLater(()->{
            queuePos.setValue(jsonMap.get("id"));
            remainingCustomerNum.setValue(jsonMap.get("remainingCustomerNum"));
        });
        return jsonMap;
    }

    public static String parseString(String title,String customerID,String windowNo){
        Map<String,String> jsonMap = new HashMap<>();
        jsonMap.put("title",title);
        jsonMap.put("customerID",customerID);
        jsonMap.put("windowNo",windowNo);
        return JSONObject.toJSONString(jsonMap,SerializerFeature.WriteMapNullValue);
    }

    public static String parseReqString(){
        Map<String,String> jsonMap = new HashMap<>();
        jsonMap.put("requestData","");
        return JSONObject.toJSONString(jsonMap,SerializerFeature.WriteMapNullValue);
    }

}
