package com.wangyichao.controller;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import com.wangyichao.dao.Config;
import com.wangyichao.dao.XMLReadExcelDao;
import net.sf.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daivdyyl on 2017/3/30.
 */

@Controller
@RequestMapping("/fileInput")
public class FileinputController {

    @Resource(name = "xmlReadExcelDao")
    private XMLReadExcelDao xmlReadExcelDao;

    @RequestMapping(value = "/highSpeedFile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String fileUpload(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam("inputfile") MultipartFile file){
        JSONObject json = new JSONObject();
        System.out.println(file.getOriginalFilename());
        //采用file.transferTo方法传输文件
        String path = Config.getProperty("HIGH_SPEED_FILE")+file.getOriginalFilename();
        File newfile = new File(path);
        try {
            file.transferTo(newfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("文件存储成功");
        json.put("success","success");
        System.out.println(json);


        //上传流水文件的同时，将流水文件存到数据库
//        try {
//            xmlReadExcelDao.processOneSheet(path);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        return json.toString();
//        PrintWriter pw = null;
//        try {
//            pw = response.getWriter();
//            pw.print(json);
//            pw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @RequestMapping(value = "/imgUpload")
    @ResponseBody
    public void imgUpload(HttpServletRequest request,HttpServletResponse response,
                          @RequestParam("imginput") MultipartFile file){
        JSONObject json = new JSONObject();
        System.out.println(file.getOriginalFilename());
        //采用file.transferTo方法传输文件
        String path = Config.getProperty("IMG_FILE")+file.getOriginalFilename();
        File newfile = new File(path);
        try {
            file.transferTo(newfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(json);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/txtUpload")
    @ResponseBody
    public void txtUpload(HttpServletRequest request,HttpServletResponse response,
                          @RequestParam("txtinupt") MultipartFile file){
        JSONObject json = new JSONObject();
        System.out.println(file.getOriginalFilename());
        //采用file.transferTo方法传输文件
        String path = Config.getProperty("CAR_TRACK_URL")+file.getOriginalFilename();
        File newfile = new File(path);
        try {
            file.transferTo(newfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(json);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
