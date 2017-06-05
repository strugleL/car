package com.wangyichao.controller;

import com.wangyichao.service.StatisticService;
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

/**
 * Created by Nicholas_Wang on 2016/11/18.
 */
@Controller
@RequestMapping(value = "/statistic")
public class StatisticController {

    @Resource(name = "statisticService")
    private StatisticService statisticService;

    /**
     * 获取近一个月系统报警次数
     * 新增
     *
     */

    @RequestMapping(value = "/getThisMonthsystemAlarm",method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthsystemAlarm(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = statisticService.getThisMonthsystemAlarm();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取近一个月确认的逃费次数，即打击逃费次数
     * 保留
     * @param response
     */
    @RequestMapping(value = "/getThisMonthFindDodgeTimes", method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthFindDodgeTimes(HttpServletResponse response){
        JSONObject result = statisticService.getThisMonthFindDodgeTimes();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取近一个月逃费类型的统计
     * 新增
     *
     * */
    @RequestMapping(value = "/getEscapeType",method = RequestMethod.GET)
    @ResponseBody
    public void getEscapeType(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = statisticService.getEscapeType();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取挽回逃费损失金额/报警损失金额按月变化数据
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getLossData", method = RequestMethod.GET)
    @ResponseBody
    public void getLossData(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getLossData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公司确认逃费次数/报警逃费次数按月变化数据
     * 保留
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getDodgeTollTimesData", method = RequestMethod.GET)
    @ResponseBody
    public void getDodgeTollTimesData(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getDodgeTollTimesData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取分收费站考核数据
     * @param request
     * @param response
     * @param scope
     */
    @RequestMapping(value = "/getTableData", method = RequestMethod.GET)
    @ResponseBody
    public void getTableData(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "scope", required = true) int scope) {
        JSONArray result = statisticService.getTableData(scope);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分收费站考核数据
     * @param request
     * @param response
     * @param scope
     */
    @RequestMapping(value = "/getOperatorsTableDataArray", method = RequestMethod.GET)
    @ResponseBody
    public void getOperatorsTableDataArray(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "scope", required = true) int scope) {
        JSONArray result = statisticService.getOperatorsTableDataArray(scope);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取近一个月确认的逃费次数

    @RequestMapping(value = "/getThisMonthOperatorFindDodgeTimes", method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthOperatorFindDodgeTimes(HttpServletResponse response,
                                                   @RequestParam(value = "operator") String operator){
        JSONObject result = statisticService.getThisMonthOperatorFindDodgeTimes(operator);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取近一个月取消的逃费次数
    @RequestMapping(value = "/getThisMonthCancelDodgeTimes", method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthCancelDodgeTimes(HttpServletResponse response,
                                             @RequestParam(value = "operator") String operator){
        JSONObject result = statisticService.getThisMonthCancelDodgeTimes(operator);
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
