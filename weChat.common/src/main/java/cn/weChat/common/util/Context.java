package cn.weChat.common.util;

import cn.weChat.common.constant.ServiceConstant;
import cn.weChat.model.entity.User;
import cn.weChat.model.entity.UserExample;
import cn.weChat.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Context {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    private static ThreadLocal<User> userThreadLocal=new ThreadLocal<>();

    private static Context context;

    private static User user;

    @PostConstruct
    public void init(){
        context=this;
    }

    public static void initUser(){
        if(!StringUtils.isEmpty(context.redisUtil.get(ServiceConstant.SERVICE_SESSION))){
            String sessionId = context.redisUtil.get(ServiceConstant.SERVICE_SESSION).toString();
            String[] split = AESUtil.decryptStart(sessionId).split("&");
            String userName = split[0];
            String password = split[1];
            UserExample userExample=new UserExample();
            userExample.createCriteria()
                    .andNameEqualTo(userName)
                    .andPwdEqualTo(password);
            List<User> users = context.userMapper.selectByExample(userExample);
            if(users.size()>0){
                user=users.get(0);
            }
        }
    }

    public static void initThreadLocal(){
        if(user != null){
            userThreadLocal.set(user);
        }
    }

    public synchronized static User getUser(){
        initUser();
        initThreadLocal();
        return userThreadLocal.get();
    }

}
