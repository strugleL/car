package com.wangyichao.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * Created by daivdyyl on 2017/4/16.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{

    //|| url.matches("/[^\\s]*.js") ||url.matches("/[^\\s]*.css") || url.matches("/[^\\s]*.woff2") ||url.matches("/[^\\s]*.ttf") || url.matches("/[^\\s]*.jpg") ||url.matches("/[^\\s]*.JPG") || url.matches("/[^\\s]*.svg")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handle) throws Exception{
        String url = request.getRequestURI();
        String username = (String) request.getSession().getAttribute("username");
        //利用正则，避开所有的js、css、jpg等
        String reg = "/[^\\s]*.html";
        if (username == null && url.matches(reg)){
            if (url.equals("/car/auditPage/login.html") || url.equals("/car/auditPage/collector/checkbox.html")){
                System.out.println("成功进入");
                return true;
            }else {
//                request.getRequestDispatcher("/auditPage/login.html").forward(request,response);
                response.sendRedirect("/car/auditPage/login.html");
                System.out.println("被禁止了");
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("===========HandlerInterceptor1 postHandle");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("===========HandlerInterceptor1 afterCompletion");
    }
}
