package cn.weChat.controller;

import cn.weChat.common.model.Button;
import cn.weChat.common.model.Menu;
import cn.weChat.common.model.ViewButton;
import cn.weChat.common.util.WeChatUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Api(tags = {"开发微信服务器"})
@Log4j
@Controller
public class WeChatController {

    // 开发者接入验证 确认请求来自微信服务器
    @ApiOperation(value = "验证请求",notes = "验证请求")
    @RequestMapping(path = "/weChat/signature",method = RequestMethod.GET)
    public void signature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //消息来源可靠性验证
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");       // 随机数
        String echostr = request.getParameter("echostr");//成为开发者验证
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        PrintWriter out = response.getWriter();
        if(WeChatUtil.checkSignature(signature, timestamp, nonce)){
            log.info("=======请求校验成功======" + echostr);
            out.print(echostr);
        }
        if(out != null){
            out.close();
            out = null;
        }
    }

    @ApiOperation(value = "获取token",notes = "获取token")
    @RequestMapping(path = "/weChat/getToken",method = RequestMethod.GET)
    public String getToken() throws Exception {
        // TODO 消息的接收、处理、响应
       return WeChatUtil.getToken();
    }

    @ApiOperation(value = "初始化菜单",notes = "初始化菜单")
    @RequestMapping(path = "/weChat/createMenu",method = RequestMethod.POST)
    public int createMenu() throws Exception {
        String accessToken=WeChatUtil.getToken();
        Menu menu=new Menu();
        ViewButton button1=new ViewButton();
        button1.setName("ViewButton1");
        button1.setType("view");
        button1.setUrl("https://www.baidu.com/");
        ViewButton button2=new ViewButton();
        button2.setName("ViewButton2");
        button2.setType("view1c");
        button2.setUrl("https://www.baidu.com/");
        menu.setButtons(new Button[]{button1,button2});
        String jsonString = JSONObject.toJSONString(menu);
        return WeChatUtil.createMenu(accessToken, jsonString);
    }

}
