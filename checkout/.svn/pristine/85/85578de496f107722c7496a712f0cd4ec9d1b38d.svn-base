package com.wangyichao.controller;

import com.wangyichao.service.OwnerService;
import net.sf.json.JSONArray;
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
 * Created by daivdyyl on 2017/4/27.
 */

@Controller
@RequestMapping("/ownerController")
public class OwnerController {

    @Resource(name = "ownerService")
    private OwnerService ownerService;

    //若没有加上responsebody会报500错误
    @RequestMapping(value = "/getInfoById",method = RequestMethod.GET)
    @ResponseBody
    public void getInfoById(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("collectNumber") String collectNumber){
        JSONArray coInfoArr = ownerService.getInfoById(collectNumber);
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
            pw.print(coInfoArr);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getInfoByName",method = RequestMethod.GET)
    @ResponseBody
    public void getInfoByName(HttpServletRequest request,HttpServletResponse response,
                              @RequestParam("collectName") String collectName){
        JSONArray coInfoArr = ownerService.getInfoByName(collectName);
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
            pw.print(coInfoArr);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getInfoById_Name",method = RequestMethod.GET)
    @ResponseBody
    public void getInfoById_Name(HttpServletRequest request,HttpServletResponse response,
                                 @RequestParam("collectNumber") String collectNumber,
                                 @RequestParam("collectName") String collectName){
        JSONArray coInfoArr = ownerService.getInfoById_Name(collectNumber,collectName);
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
            pw.print(coInfoArr);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/deleteOperator")
    @ResponseBody
    public void deleteOperator(HttpServletRequest request,HttpServletResponse response,
                               @RequestParam("operator_id") String operator_id){
        int result = ownerService.deleteOperator(operator_id);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/addOperatorInfo",method = RequestMethod.POST)
    @ResponseBody
    public void addOperatorInfo(HttpServletRequest request,HttpServletResponse response,
                                @RequestParam("operator_id") String operator_id,
                                @RequestParam("name") String name,
                                @RequestParam("gender") String gender,
                                @RequestParam("birth") String birth,
                                @RequestParam("political") String political,
                                @RequestParam("minzu") String minzu,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam("status") String status,
                                @RequestParam("work_age") String work_age){
        int result = ownerService.addOperatorInfo(operator_id,name,gender,birth,political,minzu,phone,address,email,password,status,work_age);
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
