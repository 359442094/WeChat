package cn.weChat.controller;

import cn.weChat.common.constant.WeChatConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"测试"})
@RestController
public class TestController {

    @ApiOperation(value = "测试",notes = "测试")
    @RequestMapping(path = "/test",method = RequestMethod.POST)
    public String test(){
        return "test";
    }

}
