package com.wangyichao.service;

import com.wangyichao.dao.LoginDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by daivdyyl on 2017/4/13.
 */
@Service("LoginService")
public class LoginService {
    @Resource(name = "LoginDao")
    private LoginDao loginDao;

    public int collectorLogin(String username, String password,String identity) {
        int result = loginDao.collectorLogin(username,password,identity);
        // 由于登录的身份不同，返回不同的result,根据不同result，登录成功后的跳转不同页面
        if (result == 1 && identity.equals("option1")){
            result = 1;
        }
        if (result == 1 && identity.equals("option2")){
            result = 2;
        }
        return result;
    }

    public String getName(String username) {
        String name = loginDao.getName(username);
        return name;
    }
}
