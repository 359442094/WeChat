package cn.weChat.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WeChatUtil {

    @Autowired
    private static WeChatUtil weChatUtil;
    @Value(value = "${wechat.token}")
    private String token;
    @Value(value = "${wechat.appid}")
    private String appId;
    @Value(value = "${wechat.appsecret}")
    private String appsecret;
    @Value(value = "${wechat.getToken}")
    private String getToken;
    @Value(value = "${wechat.createMenu}")
    private String createMenu;

    @PostConstruct
    public void init(){
        weChatUtil=this;
    }

    /**
     * 验证签名的方法
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        List<String> params = new ArrayList<String>();
        params.add(weChatUtil.token);
        params.add(timestamp);
        params.add(nonce);
        params = params.stream().sorted().collect(Collectors.<String>toList());
        // 2.将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuffer stringBuffer=new StringBuffer();
        params.stream().forEach(str->{
            stringBuffer.append(str);
        });
        String temp = SHAUtil.encode(stringBuffer.toString());
        // 3. 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return temp.equals(signature);
    }

    /**
     * get请求
     * @param url
     * @return
     */
    public static JSONObject doGetStr(String url) throws ParseException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }
        return jsonObject;
    }

    /**
     * post请求
     * @param url
     * @param outStr
     * @return
     */
    public static JSONObject doPostStr(String url,String outStr){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getToken() throws IOException, ParseException {
        String url = weChatUtil.getToken.replace("APPID", weChatUtil.appId).replace("APPSECRET", weChatUtil.appsecret);
        JSONObject jsonObject = weChatUtil.doGetStr(url);
        log.info("getToken json:"+jsonObject);
        if(jsonObject != null){
            Object access_token = jsonObject.get("access_token");
            if(!StringUtils.isEmpty(access_token)){
                System.out.println("获取token:"+access_token.toString());
                //return access_token.toString();
            }
        }
        return jsonObject;
    }

    public static JSONObject createMenu(String token,String jsonStr) {
        String url = weChatUtil.createMenu.replace("ACCESS_TOKEN",token);
        JSONObject jsonObject = weChatUtil.doPostStr(url,jsonStr);
        log.info("createMenu json:"+jsonObject);
        if(jsonObject != null){
            Object errcode = jsonObject.get("errcode");
        }
        return jsonObject;
    }

}
