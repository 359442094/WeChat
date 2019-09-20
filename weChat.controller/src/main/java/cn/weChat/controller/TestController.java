package cn.weChat.controller;

import cn.weChat.common.annoation.ShowLogger;
import cn.weChat.model.entity.TestExample;
import cn.weChat.model.mapper.TestMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"测试接口"})
@RestController
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @ShowLogger(info = "测试")
    @ApiOperation(value = "测试",notes = "测试")
    @RequestMapping(path = "/test",method = RequestMethod.POST)
    @ResponseBody
    public Object test(@RequestParam(value = "name") String name){
        TestExample testExample=new TestExample();
        testExample.createCriteria().andTnameEqualTo(name);
        return testMapper.selectByExample(testExample);
    }

}
