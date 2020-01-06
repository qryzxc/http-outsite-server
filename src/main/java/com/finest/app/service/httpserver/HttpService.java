package com.finest.app.service.httpserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.finest.app.config.ParamsConfig;
import com.finest.app.service.httpclient.HttpClient;
import com.finest.app.service.param.rsp.Descript;
import com.finest.app.service.param.rsp.ResponseParam;
import com.finest.app.service.utils.FileUtils;
import com.finest.app.service.utils.ServerIdMapping;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * http服务端
 *
 *
 * @author：XiaoWei
 * @date：2019年12月23日 下午14:27:08
 */
@RestController
@RequestMapping("/httpService")
public class HttpService {

    @Autowired
    ParamsConfig paramsConfig;

    @RequestMapping(value = "/sendPostDataByMap", method = RequestMethod.POST)
    public String sendPostDataByMap(HttpServletRequest request, HttpServletResponse response) {
        String result = "调用成功：数据是 " + "name:" + request.getParameter("name") + " city:" + request.getParameter("city");
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/sendPostDataByJson", method = RequestMethod.POST)
    public String sendPostDataByJson(HttpServletRequest request, HttpServletResponse response,@RequestBody String requestBody) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(requestBody);
        String serviceDescript = jsonObject.getString("serviceDescript");
        JSONObject jsonData = JSONObject.parseObject(serviceDescript);
        String bisData = jsonData.getString("data");
        String header = jsonData.getString("header");
        String serviceName = jsonData.getString("serviceName");
        Map params = JSON.parseObject(header);
        String res = "";
        //res = HttpClient.testSendPostDataByJson(paramsConfig.getTransHost()+serviceName,bisData,params);
        res = HttpClient.testSendPostDataByJson(paramsConfig.getTransHost(),bisData,params);

        return res;
    }

    @RequestMapping(value = "/sendGetData", method = RequestMethod.GET)
    public String sendGetData(HttpServletRequest request, HttpServletResponse response) {
        String result = "调用成功：数据是 " + "name:" + request.getParameter("name") + " city:" + request.getParameter("city");
        return JSON.toJSONString(result);
    }
}