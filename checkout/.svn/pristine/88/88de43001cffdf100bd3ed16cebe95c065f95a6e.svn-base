package com.wangyichao.controller;

import com.wangyichao.service.OperatorService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by daivdyyl on 2017/4/17.
 */

@Controller
@RequestMapping("/operatorController")
public class OperatorController {

    @Resource(name = "operatorService")
    private OperatorService operatorService;

    //查看个人信息
    @RequestMapping(value = "/getProfile",method = RequestMethod.GET)
    @ResponseBody
    public void getProfile(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("operator_id") String operator_id){
        JSONArray arr = operatorService.getProfile(operator_id);
        try {
            request.setCharacterEncoding("utf-8");  //这里不设置编码会有乱码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(arr);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //修改个人信息
    @RequestMapping(value = "/changeInfo",method = RequestMethod.POST)
    @ResponseBody
    public void changeInfo(HttpServletRequest request,HttpServletResponse response,
                           @RequestParam("operator_id") String operator_id,
                           @RequestParam("gender") String gender,
                           @RequestParam("birth") String birth,
                           @RequestParam("political") String political,
                           @RequestParam("minzu") String minzu,
                           @RequestParam("phone") String phone,
                           @RequestParam("address") String address,
                           @RequestParam("email") String email){
        System.out.println(birth);
        int result = operatorService.changeInfo(operator_id,gender,birth,political,minzu,phone,address,email);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改密码
    @RequestMapping(value = "/changePw",method = RequestMethod.GET)
    public void changePw(HttpServletRequest request,HttpServletResponse response,
                         @RequestParam("operator_id") String operator_id,
                         @RequestParam("newpw") String newpw){
        Boolean result = operatorService.changePw(operator_id,newpw);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断当前密码是否正确
    @RequestMapping(value = "/isOldpw",method = RequestMethod.GET)
    @ResponseBody
    public void isOldpw(HttpServletRequest request,HttpServletResponse response,
                        @RequestParam("operator_id") String operator_id,
                        @RequestParam("nowpw") String nowpw){

        int result = operatorService.isOldpw(operator_id,nowpw);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
