package cn.weChat.controller;

import cn.weChat.common.util.WeChatUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"开发微信服务器"})
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private WeChatUtil weChatUtil;

    @Value(value = "${wechat.appsecret}")
    private String appsecret;
    @Value(value = "${wechat.appid}")
    private String appId;

    //http://cj.ngrok2.xiaomiqiu.cn/login
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String backUrl = "http://cj.ngrok2.xiaomiqiu.cn/loginCallBack";
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                +"appid=" + appId
                +"&redirect_uri="+ URLEncoder.encode(backUrl,"UTF-8")
                +"&response_type=code"
                +"&scope=snsapi_userinfo"
                //+"&scope=snsapi_base"
                +"&state=STATE#wechat_redict";
        log.info("url:"+url);
        response.sendRedirect(url);
    }

    @RequestMapping(path = "/loginCallBack",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject ok(HttpServletRequest request) throws IOException, ParseException {
        String code= request.getParameter("code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId
                + "&secret=" + appsecret
                + "&code=" + code
                + "&grant_type=authorization_code";

        JSONObject jsonObject = weChatUtil.doGetStr(url);
        log.info(jsonObject.toString());
        String openid = jsonObject.getString("openid");
        String token = jsonObject.getString("access_token");

        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?"
                + "access_token=" +token
                + "&openid=" + openid
                + "&lang=zh_CN";

        JSONObject uerInfo = weChatUtil.doGetStr(infoUrl);
        log.info(uerInfo.toString());

        return uerInfo;
    }

}
