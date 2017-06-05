package com.wangyichao.controller;

import com.wangyichao.dao.LoginDao;
import com.wangyichao.service.LoginService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by daivdyyl on 2017/4/13.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Resource(name = "LoginService")
    private LoginService loginService;
    @Resource(name = "LoginDao")
    private LoginDao loginDao;

    //登录操作
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public void login(HttpServletRequest request, HttpServletResponse response,
                               HttpSession session,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("identity") String identity){
        int result = loginService.login(username,password,identity);
        session.setAttribute("username",username);
        session.setAttribute("identity",identity);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从session中得到用户的姓名
    @RequestMapping(value = "/getUsername",method = RequestMethod.GET)
    @ResponseBody
    public void getUsername(HttpServletRequest request,HttpSession session,HttpServletResponse response){

        //从session中得到用户id和身份，传到后台进行判断
        String username = (String) session.getAttribute("username");
        String name = loginService.getName(username);
        JSONObject user = new JSONObject();
        try {
            request.setCharacterEncoding("utf-8");  //这里不设置编码会有乱码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        user.put("username",username);
        user.put("name",name);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(user);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从session中得到用户的id
    @RequestMapping(value = "/getId",method = RequestMethod.GET)
    @ResponseBody
    public void getId(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        String username = (String) session.getAttribute("username");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(username);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //注销,session失效
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        int result = 0;
//        session.removeAttribute("username");
        session.invalidate();
//        if (session.getAttribute("username") == null){
//            result = 1;
//        }
        if (session == null){
            result = 1;
        }
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
