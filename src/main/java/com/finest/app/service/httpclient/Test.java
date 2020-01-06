package com.finest.app.service.httpclient;

public class Test {

    public static void main(String[] args) {
        String str = "\"{\\\"appVerId\\\":25,\\\"imei\\\":\\\"866957038620922\\\",\\\"loginName\\\":\\\"005112\\\",\\\"password\\\":\\\"123456\\\"}\"";
        str = str.replaceAll("\\\\","");
        if(str.startsWith("\"") && str.endsWith("\""))
        {
            str = str.substring(1,str.length()-1);
        }
        System.out.println(str);
    }
}