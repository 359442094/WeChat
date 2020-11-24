package cn.weChat.controller;

import cn.weChat.common.annoation.ShowLogger;
import cn.weChat.common.model.Button;
import cn.weChat.common.model.ClickButton;
import cn.weChat.common.model.Menu;
import cn.weChat.common.model.ViewButton;
import cn.weChat.common.util.WeChatUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

@Api(tags = {"微信相关接口"})
@Log4j
@Controller
public class WeChatController {

    /**
     * 微信公众号-服务器地址(URL)配置这个方法的访问地址
     * 开发者接入验证 确认请求来自微信服务器
     * */
    @ShowLogger(info = "验证请求")
    @ApiOperation(value = "验证请求",notes = "验证请求")
    @RequestMapping(path = "/weChat/signature",method = RequestMethod.GET)
    public void signature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //消息来源可靠性验证
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        //成为开发者验证
        String echostr = request.getParameter("echostr");
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，
        //则接入生效，成为开发者成功，否则接入失败
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
    @ResponseBody
    public String getToken() throws Exception {
        // TODO 消息的接收、处理、响应
       return WeChatUtil.getToken();
    }

    @ApiOperation(value = "初始化菜单",notes = "初始化菜单")
    @RequestMapping(path = "/weChat/menu",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createMenu() throws Exception {
        String token = WeChatUtil.getToken();
        Menu menu=new Menu();
        ViewButton leftButton=new ViewButton();
        leftButton.setType("view");
        leftButton.setName("View");
        leftButton.setUrl("https://www.baidu.com/");

        ClickButton rightButton=new ClickButton();
        rightButton.setType("click");
        rightButton.setName("Click");
        //rightButton.setUrl("https://www.baidu.com/");

        ViewButton click1=new ViewButton();
        click1.setType("view");
        click1.setName("click1");
        click1.setUrl("https://www.baidu.com/");

        rightButton.setSub_button(new Button[]{click1});

        menu.setButton(new Button[]{leftButton,rightButton});
        String json = JSONObject.toJSONString(menu);

        System.out.println("json:"+json);

        return WeChatUtil.createMenu(token, json);
    }

    @ApiOperation(value = "发送模板消息",notes = "初始化菜单")
    @RequestMapping(path = "/weChat/sendTemplateData",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendTemplateData() throws IOException, ParseException {
        String token = WeChatUtil.getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
        String json = "{\n" +
                "\n" +
                "\t\"touser\": \"oVDCL0pqQABo7Kpberhs4s2fvlpk\",\n" +
                "\t\"template_id\": \"omGxYtt-GSnl6G1iMIV-USORKRYTA1e-JiwBruuEbEg\",\n" +
                "\t\"url\": \"www.baidu.com\",\n" +
                "\t\"topcolor\": \"#FF0000\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"first\": {\n" +
                "\t\t\t\"value\": \"data1\",\n" +
                "\t\t\t\"color\": \"#173177\"\n" +
                "\t\t},\n" +
                "\t\t\"keyword1\": {\n" +
                "\t\t\t\"value\": \"data2\",\n" +
                "\t\t\t\"color\": \"#173177\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        JSONObject jsonObject = WeChatUtil.doPostStr(url, json);
        System.out.println("jsonObject:"+jsonObject);
        return jsonObject;
    }

}
