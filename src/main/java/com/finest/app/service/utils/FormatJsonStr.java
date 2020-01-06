package com.finest.app.service.utils;

import org.apache.commons.lang.StringEscapeUtils;

public class FormatJsonStr {

    public static String transJsonStr(String str)
    {
        return StringEscapeUtils.unescapeJavaScript(str);
    }
}