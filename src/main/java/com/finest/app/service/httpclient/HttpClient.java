package com.finest.app.service.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finest.app.service.utils.FormatJsonStr;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * http客户端
 *
 *
 * @author：XiaoWei
 * @date：2019年12月23日 下午14:27:08
 */
public class HttpClient {

    /**
     * post请求传输map数据
     *
     * @param url
     * @param map
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByMap(String url, Map<String, String> map, String encoding) throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 装填参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));

        // 设置header信息
        // 指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();

        return result;
    }

    /**
     * post请求传输json数据
     *
     * @param url
     * @param json
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByJson(String url, String json, String encoding,Map map) throws ClientProtocolException, IOException {

        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 设置参数到请求对象中
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentEncoding("utf-8");
        httpPost.setEntity(stringEntity);

        for (Object obj : map.keySet()){
            httpPost.addHeader((String)obj, (String)map.get(obj));
        }

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();

        return result;
    }

    public static String sendFile(String url,MultipartFile[] files)
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";

        try
        {
            for (int i = 0; i < files.length; i++) {

                //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
                String boundary = UUID.randomUUID().toString();

                MultipartFile file = files[i];

                String fileName = file.getName();
                HttpPost httpPost = new HttpPost(url);

                //设置请求头
                httpPost.setHeader("Content-Type","multipart/form-data; boundary="+boundary);

                //HttpEntity builder
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                //字符编码
                builder.setCharset(Charset.forName("UTF-8"));
                //模拟浏览器
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //boundary
                builder.setBoundary(boundary);
                //multipart/form-data
                builder.addPart("file", new FileBody(multipartFileToFile(file)));
                //其他参数
                builder.addTextBody("filename", fileName,  ContentType.create("text/plain", Consts.UTF_8));
                //HttpEntity
                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);
                // 执行提交
                HttpResponse response = httpClient.execute(httpPost);
                //响应
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // 将响应内容转换为字符串
                    result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getName());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求传输数据
     *
     * @param url
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String sendGetData(String url, String encoding) throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json");
        // 通过请求对象获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();

        return result;
    }
    
    public void testSendPostDataByMap() throws ClientProtocolException, IOException {
        String url = "http://localhost:9091/httpService/sendPostDataByMap";
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "wyj");
        map.put("city", "南京");
        String body = sendPostDataByMap(url, map, "utf-8");
        System.out.println("响应结果：" + body);
    }
    
    public static String testSendPostDataByJson(String url,String object,Map map) throws ClientProtocolException, IOException {
        String body = sendPostDataByJson(url, object, "utf-8", map);
        System.out.println("响应结果：" + body);
        return body;
    }

    public static String sendFileByJson(String url,MultipartFile[] files) throws ClientProtocolException, IOException {
        String body = sendFile(url,files);
        System.out.println("响应结果：" + body);
        return body;
    }
    
    public void testSendGetData() throws ClientProtocolException, IOException {
        String url = "http://localhost:9091/httpService/sendGetData?name=wyj&city=南京";
        String body = sendGetData(url, "utf-8");
        System.out.println("响应结果：" + body);
    }

}