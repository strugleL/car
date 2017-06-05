package com.wangyichao.service;

import com.wangyichao.dao.OwnerDao;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by daivdyyl on 2017/4/27.
 */

@Service("ownerService")
public class OwnerService {

    @Resource(name = "ownerDao")
    private OwnerDao ownerDao;

    public JSONArray getInfoById(String collectNumber) {
        JSONArray array = ownerDao.getInfoById(collectNumber);
        return array;
    }

    public JSONArray getInfoByName(String collectName) {
        JSONArray array = ownerDao.getInfoByName(collectName);
        return array;
    }

    public JSONArray getInfoById_Name(String collectNumber, String collectName) {
        JSONArray array = ownerDao.getInfoById_Name(collectNumber,collectName);
        return array;
    }

    public int deleteOperator(String operator_id) {
        int result = ownerDao.deleteOperator(operator_id);
        return result;
    }

    public int addOperatorInfo(String operator_id, String name, String gender, String birth, String political, String minzu, String phone, String address, String email, String password, String status, String work_age) {
        int result = ownerDao.addOperatorInfo(operator_id,name,gender,birth,political,minzu,phone,address,email,password,status,work_age);
        return result;
    }
}
