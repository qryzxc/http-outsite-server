package com.finest.app.service.utils;

import java.util.HashMap;
import java.util.Map;

public class ServerIdMapping {
    public static Map<String,String> serverMap = new HashMap<String,String>();

    static
    {
        serverMap.put("S10-00000013","http://localhost:9092/base-interface/login.do");
    }
}