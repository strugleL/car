package com.wangyichao.service;

import com.wangyichao.dao.OperatorDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by daivdyyl on 2017/4/17.
 */

@Service("operatorService")
public class OperatorService {

    //查看个人信息
    @Resource(name = "operatorDao")
    private OperatorDao operatorDao;

    public JSONArray getProfile(String operator_id) {

        JSONArray arr = operatorDao.getProfile(operator_id);
        return arr;
    }

    //判断当前密码是否正确
    public int isOldpw(String operator_id, String nowpw) {

        int result = operatorDao.isOldpw(operator_id, nowpw);
        return result;
    }

    //修改密码
    public boolean changePw(String operator_id, String newpw) {
        boolean result = operatorDao.changePw(operator_id,newpw);
        return result;
    }

    //修改个人信息
    public int changeInfo(String operator_id, String gender, String birth, String political, String minzu, String phone, String address, String email) {
        int result = 0;
        //对性别进行修改
        if (gender.equals("option1"))
            gender = "男";
        else {
            gender = "女";
        }
        result = operatorDao.changeInfo(operator_id,gender,birth,political,minzu,phone,address,email);
        return result;
    }

}
