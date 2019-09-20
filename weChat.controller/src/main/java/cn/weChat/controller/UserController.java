package cn.weChat.controller;

import cn.weChat.common.ServiceException;
import cn.weChat.common.annoation.ShowLogger;
import cn.weChat.common.constant.ErrorConstant;
import cn.weChat.common.constant.ServiceConstant;
import cn.weChat.common.dto.UserLoginRequest;
import cn.weChat.common.dto.UserLoginResponse;
import cn.weChat.common.util.AESUtil;
import cn.weChat.common.util.Context;
import cn.weChat.common.util.RedisUtil;
import cn.weChat.model.entity.User;
import cn.weChat.model.entity.UserExample;
import cn.weChat.model.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = {"用户接口"})
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @ShowLogger(info = "用户登录")
    @ApiOperation(value = "用户登录",notes = "用户登录")
    @RequestMapping(path = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public UserLoginResponse login(UserLoginRequest request){
        if(StringUtils.isEmpty(request)){
            throw new ServiceException(ErrorConstant.ERROR_PARAM,"登录参数为空");
        }else if(StringUtils.isEmpty(request.getUsername())){
            throw new ServiceException(ErrorConstant.ERROR_PARAM,"登录用户参数为空");
        }else if(StringUtils.isEmpty(request.getPassword())){
            throw new ServiceException(ErrorConstant.ERROR_PARAM,"登录密码参数为空");
        }else{
            UserExample userExample=new UserExample();
            userExample.createCriteria()
                    .andNameEqualTo(request.getUsername())
                    .andPwdEqualTo(request.getPassword());
            List<User> users = userMapper.selectByExample(userExample);
            if(users.size()>0){
                String sessionId = AESUtil.encryptStart(request.getUsername()+"&"+request.getPassword()+"&");
                redisUtil.set(ServiceConstant.SERVICE_SESSION,sessionId);
                User user = users.get(0);
                UserLoginResponse response=new UserLoginResponse();
                response.setUser(convert(user));
                return response;
            }
        }
        return null;
    }

    @ApiOperation(value = "获取上下文对象",notes = "获取上下文对象")
    @RequestMapping(path = "/user/contextUser",method = RequestMethod.GET)
    @ResponseBody
    public cn.weChat.common.dto.User getContextUser(){
        return convert(Context.getUser());
    }

    public cn.weChat.common.dto.User convert(cn.weChat.model.entity.User user){
        cn.weChat.common.dto.User response = new cn.weChat.common.dto.User();
        response.setUsername(user.getName());
        response.setPassword(user.getPwd());
        return response;
    }

}
