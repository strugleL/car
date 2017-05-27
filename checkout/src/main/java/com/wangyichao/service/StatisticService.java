package com.wangyichao.service;

import com.wangyichao.dao.StatisticData;
import net.sf.ezmorph.array.DoubleArrayMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Nicholas_Wang on 2016/11/18.
 */
@Service("statisticService")
public class StatisticService {

    @Resource(name = "statisticData")
    private StatisticData statisticData;

    /**
     * 获取挽回逃费损失金额/报警损失金额按月变化数据
     * @param year
     * @return
     */
    public JSONObject getLossData(int year) {
        return statisticData.getLossData(year);
    }

    /**
     * 获取公司确认逃费次数/报警逃费次数按月变化数据
     * 保留
     * @param year
     * @return
     */
    public JSONObject getDodgeTollTimesData(int year) {
        return statisticData.getDodgeTollTimes(year);
    }

    public JSONObject getViolenceAmountData(int year) {
        return statisticData.getViolenceAmount(year);
    }

    public JSONObject getChangeCardData(int year) {
        return statisticData.getChangeCardData(year);
    }

    public JSONObject getWeightFakeData(int year) {
        return statisticData.getWeightFakeData(year);
    }

    public JSONObject getFakeDiscountData(int year) {
        return statisticData.getFakeDiscountData(year);
    }

    public JSONArray getTableData(int scope) {
        return statisticData.getTableDataArray(scope);
    }

    public JSONObject getThisMonthSaveMoney() {
        JSONObject saveMoney = new JSONObject();
        Double thisMonthMoney = statisticData.getOneMonthSaveMoney()/100;
        Double lastMonthMoney = statisticData.getLastMonthSaveMoney()/100;
        Double lastYearMonthMoney = statisticData.getLastYearMonthSaveMoney()/100;
        Double huanbi = (thisMonthMoney-lastMonthMoney)/lastMonthMoney*100;
        Double tongbi = (thisMonthMoney-lastYearMonthMoney)/lastYearMonthMoney*100;
        if (thisMonthMoney.isNaN()) {
            thisMonthMoney = 0.0;
        }
        if (huanbi.isNaN()) {
            huanbi = 0.0;
        }
        if (tongbi.isNaN()) {
            tongbi = 0.0;
        }

        if (thisMonthMoney.isInfinite()) {
            thisMonthMoney = 0.0;
        }
        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        saveMoney.put("money", thisMonthMoney);
        saveMoney.put("huanbi", huanbi);
        saveMoney.put("tongbi", tongbi);
        return saveMoney;
    }

    /**
     * 获取近一个月系统报警的逃费次数
     * 新增
     *
     */
    public JSONObject getThisMonthsystemAlarm() {
        JSONObject times = new JSONObject();
        int thisMonthTimes = statisticData.getOneMontnSystemAlarm();
        int lastMonthTimes = statisticData.getLastMonthSystemAlarm();
        int lastYearMonthTimes = statisticData.getLastYearMonthSystemAlarm();
        Double huanbi = new Double(thisMonthTimes - lastMonthTimes) / lastMonthTimes * 100;
        Double tongbi = new Double(thisMonthTimes - lastYearMonthTimes) / lastYearMonthTimes * 100;

        if (huanbi.isNaN()) {
            huanbi = 0.0;
        }
        if (tongbi.isNaN()) {
            tongbi = 0.0;
        }

        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        times.put("times", thisMonthTimes);
        times.put("huanbi", huanbi);
        times.put("tongbi", tongbi);
        return times;
    }

    /**
     * 获取近一个月确认的逃费次数，即打击逃费次数
     * 保留
     *
     */
    public JSONObject getThisMonthFindDodgeTimes() {
        JSONObject times = new JSONObject();
        int thisMonthTimes = statisticData.getOneMonthFindDodgeTimes();
        int lastMonthTimes = statisticData.getLastMonthFindDodgeTimes();
        int lastYearMonthTimes = statisticData.getLastYearMonthFindDodgeTimes();
        Double huanbi = new Double(thisMonthTimes - lastMonthTimes) / lastMonthTimes * 100;
        Double tongbi = new Double(thisMonthTimes - lastYearMonthTimes) / lastYearMonthTimes * 100;

        if (huanbi.isNaN()) {
            huanbi = 0.0;
        }
        if (tongbi.isNaN()) {
            tongbi = 0.0;
        }

        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        times.put("times", thisMonthTimes);
        times.put("huanbi", huanbi);
        times.put("tongbi", tongbi);
        return times;
    }

    /**
     * 获取近一个月逃费类型的统计
     * 新增
     *
     * */
    public JSONObject getEscapeType() {
        return statisticData.getEscapeTypeData();
    }

    public JSONObject getThisMonthAverageSaveMoney() {
        JSONObject saveMoney = new JSONObject();
        Double thisMonthMoney = statisticData.getOneMonthSaveMoney()/100;
        Double lastMonthMoney = statisticData.getLastMonthSaveMoney()/100;
        Double lastYearMonthMoney = statisticData.getLastYearMonthSaveMoney()/100;
        int thisMonthTimes = statisticData.getOneMonthFindDodgeTimes();
        int lastMonthTimes = statisticData.getLastMonthFindDodgeTimes();
        int lastYearMonthTimes = statisticData.getLastYearMonthFindDodgeTimes();
        Double averageMoney = thisMonthMoney/thisMonthTimes;
        Double lastMonthAveMoney = lastMonthMoney/lastMonthTimes;
        Double lastYearMonthAveMoney = lastYearMonthMoney/lastYearMonthTimes;
        Double huanbi = (averageMoney - lastMonthAveMoney) / lastMonthAveMoney * 100;
        Double tongbi = (averageMoney - lastYearMonthAveMoney) / lastYearMonthAveMoney * 100;
        //防止除零错误
        if (averageMoney.isNaN()) {
            averageMoney = 0.0;
        }
        if (huanbi.isNaN()) {
            huanbi = 0.0;
        }
        if (tongbi.isNaN()) {
            tongbi = 0.0;
        }

        if (averageMoney.isInfinite()) {
            averageMoney = 0.0;
        }
        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }

        saveMoney.put("averageMoney", averageMoney);
        saveMoney.put("huanbi", huanbi);
        saveMoney.put("tongbi", tongbi);
        return saveMoney;
    }

    public JSONArray getOperatorsTableDataArray(int scope) {
        return statisticData.getOperatorsTableDataArray(scope);
    }

    //获取近一个月确认的逃费次数，即打击逃费次数
    public JSONObject getThisMonthOperatorFindDodgeTimes(String operator) {
        JSONObject times = new JSONObject();
        int thisMonthTimes = statisticData.getOneMonthOperatorFindDodgeTimes(operator);
        int lastMonthTimes = statisticData.getLastMonthOperatorFindDodgeTimes(operator);
        int lastYearMonthTimes = statisticData.getLastYearMonthOperatorFindDodgeTimes(operator);
        Double huanbi = new Double(thisMonthTimes - lastMonthTimes) / lastMonthTimes * 100;
        Double tongbi = new Double(thisMonthTimes - lastYearMonthTimes) / lastYearMonthTimes * 100;

        if (huanbi.isNaN()) {
            huanbi = 0.0;
        }
        if (tongbi.isNaN()) {
            tongbi = 0.0;
        }

        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        times.put("times", thisMonthTimes);
        times.put("huanbi", huanbi);
        times.put("tongbi", tongbi);
        return times;
    }

    //获取近一个月取消的逃费次数
    public JSONObject getThisMonthCancelDodgeTimes(String operator) {
        JSONObject times = new JSONObject();
        int thisMonthTimes = statisticData.getOneMonthCancelDodgeTimes(operator);
        int lastMonthTimes = statisticData.getLastMonthCancelDodgeTimes(operator);
        int lastYearMonthTimes = statisticData.getLastYearMonthCancelDodgeTimes(operator);
        Double huanbi = new Double(thisMonthTimes - lastMonthTimes) / lastMonthTimes * 100;
        Double tongbi = new Double(thisMonthTimes - lastYearMonthTimes) / lastYearMonthTimes * 100;

        if (huanbi.isNaN()) {
            huanbi = 0.0;
        }
        if (tongbi.isNaN()) {
            tongbi = 0.0;
        }

        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        times.put("times", thisMonthTimes);
        times.put("huanbi", huanbi);
        times.put("tongbi", tongbi);
        return times;
    }

}
