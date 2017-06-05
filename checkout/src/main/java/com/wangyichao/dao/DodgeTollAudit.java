package com.wangyichao.dao;

import com.wangyichao.utils.DataBaseTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nicholas_Wang on 2016/11/16.
 * DodgeToll 类的改动版本
 */
@Repository("dodgeTollAudit")
public class DodgeTollAudit {

    DataBaseTool dataBaseTool = DataBaseTool.getInstance();
//    DataBaseTool dataBaseTool = new DataBaseTool();

    /**
     * 获取车辆基本数据
     * 保留
     *
     * @return
     */
    public JSONArray getDodgeTollAuditInfo() {

        JSONArray carInfoArr = new JSONArray();

        BufferedReader reader = null;
        String fileName = Config.getProperty("DODGE_TOLL_CAR") + "逃费稽核展现结果-20160828.csv";
        File f = new File(fileName);
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNum == 1) {
                    // 第一行为表标题略过
                    lineNum++;
                    continue;
                }
                JSONObject carInfo = new JSONObject();
                String[] car = line.split(",");
                carInfo.put("carNo", car[3]);
                carInfo.put("exTime", car[2]);
                // 由于数据里面有经纬度，所以多了 逗号分隔符 否则为23
                carInfo.put("history", car[27]);
                if (car[24].equals("无")) {
                    // 如果逃费历史大于0则预警
                    if (Integer.valueOf(car[27]) > 0) {
                        carInfo.put("audit", "预警");
                    } else {
                        carInfo.put("audit", "正常");
                    }

                } else if (car[24].equals("0")) {
                    carInfo.put("audit", "倒卡和遮挡卡");
                } else if (car[24].equals("1")) {
                    carInfo.put("audit", "两车交换卡");
                } else if (car[24].equals("10")) {
                    carInfo.put("audit", "中途取得卡");
                } else {
                    carInfo.put("audit", "车机和车分离及套牌");
                }
                carInfoArr.add(carInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return carInfoArr;

    }

//    public static void main(String[] args) {
//        System.out.println(getDodgeTollAuditInfo().toString());
//    }

    /**
     * 获取某个车辆信息
     * 保留
     *
     * @param carNo
     * @return
     */
    public JSONObject getDodgeTollCarInfo(String carNo) {
        JSONObject carInfo = new JSONObject();
        BufferedReader reader = null;
        String fileName = Config.getProperty("DODGE_TOLL_CAR") + "逃费稽核展现结果-20160828.csv";
        File f = new File(fileName);
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNum == 1) {
                    // 第一行为表标题略过
                    lineNum++;
                    continue;
                }
                String[] car = line.split(",");
                if (car[3].equals(carNo)) {
                    carInfo.put("carNo", carNo);
                    carInfo.put("escapeDist", car[23]);
                    carInfo.put("escapeMoney", car[26]);
                    carInfo.put("enStation", car[5]);
                    carInfo.put("exStation", car[7]);
                    carInfo.put("enTime", car[1]);
                    carInfo.put("exTime", car[2]);
                    carInfo.put("history", car[27]);
                    if (car[24].equals("无")) {
                        // 如果逃费历史大于0则预警
                        if (Integer.valueOf(car[27]) > 0) {
                            carInfo.put("behavior", "预警");
                        } else {
                            carInfo.put("behavior", "正常");
                        }

                    } else if (car[24].equals("0")) {
                        carInfo.put("behavior", "倒卡和遮挡卡");
                    } else if (car[24].equals("1")) {
                        carInfo.put("behavior", "两车交换卡");
                    } else if (car[24].equals("10")) {
                        carInfo.put("behavior", "中途取得卡");
                    } else {
                        carInfo.put("behavior", "车机和车分离及套牌");
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return carInfo;
    }

    /**
     * 从数据库获取车辆逃费监控信息
     *
     * @return
     */
    /**
    * 更改，从数据库中搜索入口站名，出口站名等字段传给前台页面
    * */
    public JSONArray getDodgeTollAuditInfoFromSql() {
        JSONArray carInfoArr = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        //改变数据库后的sql语句,提取数据库中前100条数据
        String sql = "select * from auditRes ORDER BY exTime DESC limit 0,300";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject carInfo = new JSONObject();
                carInfo = getCarInforme(rs);
                carInfoArr.add(carInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }

        return carInfoArr;
    }

    /**
     * 通过车牌号从数据库中获得车辆逃费监控信息
     * 新增！！！！！！！
     *
     *
     * */
    public JSONArray getDodgeTollAuditInfoBycarNumber(String carNumber){
        JSONArray carInfoArr = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        String sql = "select * from auditRes where exPlateNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,carNumber);
            rs = ps.executeQuery();
            while (rs.next()){
                JSONObject carInfo = new JSONObject();
                carInfo = getCarInforme(rs);
                carInfoArr.add(carInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return carInfoArr;
    }

    /**
     * 通过起始时间从数据库中获得车辆逃费监控信息
     * 新增！！！！！！！
     *
     *
     * */
    public JSONArray getDodgeTollAuditInfoByTime(String start,String end){
        JSONArray carInfoArr = new JSONArray();
        ArrayList<String> newEntime = new ArrayList<String>();
        Connection conn = dataBaseTool.getConn();
        //首先将日期改为 2016-08-28 17:00:00 格式
        start = start + ":00";
        end = end + ":00";
        String sql = "select * from auditRes where enTime > ? and exTime < ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,start);
            ps.setString(2,end);
            rs = ps.executeQuery();
            while (rs.next()){
                JSONObject carInfo = new JSONObject();
                carInfo = getCarInforme(rs);

                carInfoArr.add(carInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return carInfoArr;
    }

    /**
     * 通过车牌号和日期，从数据库中获取车辆信息
     *
     */
    public JSONArray getDodgeTollAuditInfoByTimeAndcarNumber(String startTime, String endTime, String carNumber) {
        JSONArray carInfoArr = new JSONArray();
        ArrayList<String> newEntime = new ArrayList<String>();
        Connection conn = dataBaseTool.getConn();
        //首先将日期改为 2016-08-28 17:00:00 格式
        startTime = startTime + ":00";
        endTime = endTime + ":00";
        String sql = "select * from auditRes where enTime > ? and exTime < ? and exPlateNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,startTime);
            ps.setString(2,endTime);
            ps.setString(3,carNumber);
            rs = ps.executeQuery();
            while (rs.next()){
                //将date和hour两个组合，通过时间再次查询，在这两个时间之间的数据
                JSONObject carInfo = new JSONObject();
                carInfo = getCarInforme(rs);

                carInfoArr.add(carInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return carInfoArr;
    }

    /**
     * 从数据库获取车辆逃费监控信息 限制查询条数
     *
     * @return
     */
    public JSONArray getDodgeTollAuditInfoFromSqlLimit(int count) {
        JSONArray carInfoArr = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        String sql = "select exRecordNo,exPlateNumber,exTime,history,escapetype,action,flag from carsearch where fordemo = 'TRUE' order by (exTime) asc LIMIT ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, count);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject carInfo = new JSONObject();
                carInfo.put("exRecordNo", rs.getString("exRecordNo"));
                carInfo.put("carNo", rs.getString("exPlateNumber"));
                carInfo.put("exTime", rs.getString("exTime"));
                carInfo.put("history", rs.getString("history"));
                carInfo.put("audit", rs.getString("escapetype"));
                carInfo.put("action", rs.getString("action"));
                carInfo.put("flag", rs.getString("flag"));
                carInfoArr.add(carInfo);
//                System.out.println("carInfo: " + carInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);

        }

        return carInfoArr;
    }

    /**
     * 根据流水号获取某辆车的详细信息
     *
     * @param laneexSerialNo
     * @return
     */
    // escapeDist,escapeMoney,history这三个字段没有
    public JSONObject getDodgeTollCarInfoFromSql(String laneexSerialNo) {
        JSONObject carInfo = new JSONObject();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from auditRes where laneexSerialNo = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, laneexSerialNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                String carNo = rs.getString("exPlateNumber");
                carInfo.put("laneexSerialNo", rs.getString("laneexSerialNo"));
                carInfo.put("carNo", carNo);
                carInfo.put("enRoad", rs.getString("enRoad"));
                carInfo.put("exRoad", rs.getString("exRoad"));
                carInfo.put("enStation", rs.getString("enStation"));
                carInfo.put("exStation", rs.getString("exStation"));
                carInfo.put("enTime", rs.getString("enTime"));
                carInfo.put("exTime", rs.getString("exTime"));
                carInfo.put("chargeMiles",rs.getString("chargeMiles"));
                carInfo.put("axisNum",rs.getString("axisNum"));
                carInfo.put("axisGroupNum",rs.getString("axisGroupNum"));
                carInfo.put("totalWeight",rs.getString("totalWeight"));
                carInfo.put("totalWeightLimit",rs.getString("totalWeightLimit"));
                carInfo.put("exOperatorID",rs.getString("exOperatorID"));
                carInfo.put("exShiftID",rs.getString("exShiftID"));
                //得到历史逃费次数
                int his = getHistoryCount(carNo);
                carInfo.put("history",his);
                System.out.println("history:" + his);
                //得到逃费类型
                String passCardDodge = rs.getString("passCardDodge");
                String weightDodge = rs.getString("weightDodge");
                String audit = getAudit(passCardDodge,weightDodge);
                carInfo.put("behavior",audit);
                carInfo.put("passCardDodge", passCardDodge);
                carInfo.put("weightDodge", weightDodge);
                carInfo.put("cashMoney", rs.getString("cashmoney"));
                carInfo.put("action",rs.getString("action"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }

        return carInfo;
    }

    /**
     * 确认车辆是否逃费操作
     *
     * @param exRecordNo
     * @param operation
     * @return
     */
    public boolean dodgeTollCarConfirm(String exRecordNo, String operation,String operator) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        String sql = "update auditRes set action = ?,operator=? where laneexSerialNo = ?";
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, operation);
            ps.setString(2,operator);
            ps.setString(3, exRecordNo);
            result = ps.executeUpdate();
            System.out.println("update result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(ps, conn);
        }
        return true;
    }

    /**
     * 修改车牌号信息
     *
     * @param laneexSerialNo
     * @param newCarNo
     * @return
     */
    public boolean changeCarNo(String laneexSerialNo, String newCarNo) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        String sql = "update data set exPlateNumber = ? where laneexSerialNo = ?";
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, newCarNo);
            ps.setString(2, laneexSerialNo);
            result = ps.executeUpdate();
            System.out.println("update result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(ps, conn);
        }

        return true;
    }

    /**
     * 修改逃费类型信息
     *
     *
     * @param passCardDodge
     * @param passCardDodge,weightDodge
     * @return
     */
    public int update(String passCardDodge, String weightDodge, String laneexSerialNo){
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        //更改逃费类型
        String sql = "update auditRes set passCardDodge = ?, weightDodge=? where laneexSerialNo = ?";
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,passCardDodge);
            ps.setString(2,weightDodge);
            ps.setString(3,laneexSerialNo);
            result = ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(ps, conn);
        }
        return result;
    }

    /**
     * 判断逃费类型
     *
     * @param passCardDodge,weightDodge
     * @return
     */
    public String getAudit(String passCardDodge,String weightDodge){
        String audit = "";
        //由于jdk版本问题，不能使用switch
        if (passCardDodge.equals("0")){
            audit = "倒卡和遮挡卡逃费";
        } else if (passCardDodge.equals("1")){
            audit = "两车交换卡逃费";
        } else if (passCardDodge.equals("2")){
            audit = "中途取得卡逃费";
        } else if (passCardDodge.equals("3")){
            audit = "套牌逃费";
        } else if (passCardDodge.equals("4")){
            audit = "U型车";
        } else if (weightDodge.equals("0")){
            if (audit == "")
                audit = "计重作假逃费";
            else
                audit = audit + ",计重作假逃费";
        } if (weightDodge.equals("1")){
            if (audit == "")
                audit = "变轴逃费";
            else
                audit = audit + ",变轴逃费";
        } else if (weightDodge.equals("2")){
            if (audit == "")
                audit = "轴数不一致";
            else
                audit = audit +",轴数不一致";
        } else if (passCardDodge.equals("-1") || weightDodge.equals("-1")){
            audit = "";
        } if (passCardDodge.equals("") && weightDodge.equals("")){
            audit = "不存在逃费行为";
        }
        return audit;
    }

    /**
     *
     * 得到车辆历史逃费次数
     *
     * @param carNo
     *
     * */
    public int getHistoryCount(String carNo){
        int result = 0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(*) from auditRes where exPlateNumber = ? and (passCardDodge != '' or weightDodge != '') and action = '1'";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,carNo);
            rs = ps.executeQuery();
            if (rs.next()){
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return result;
    }

    /**
     *
     * 通过车牌号得到逃费记录
     *
     *@param carNo
     * */
    public JSONArray getDodgeTollhistoryFromSql(String carNo) {
        JSONArray carInfoArr = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        String sql = "select * from auditRes where exPlateNumber = ? and (passCardDodge != '' or weightDodge != '') and action = '1'";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,carNo);
            rs = ps.executeQuery();
            while (rs.next()){
                JSONObject carInfo = new JSONObject();
                carInfo.put("laneexSerialNo",rs.getString("laneexSerialNo"));
                carInfo.put("carNo", rs.getString("exPlateNumber"));
                carInfo.put("enTime",rs.getString("enTime"));
                carInfo.put("exTime", rs.getString("exTime"));
                carInfo.put("enStation",rs.getString("enStation"));
                carInfo.put("exStation",rs.getString("exStation"));
                carInfo.put("axisNum",rs.getString("axisNum"));
                carInfo.put("totalWeight",rs.getString("totalWeight"));
                //通过flag确定逃费类型
                String passCardDodge = rs.getString("passCardDodge");
                String weightDodge = rs.getString("weightDodge");
                String audit = getAudit(passCardDodge,weightDodge);
                carInfo.put("audit",audit);
                carInfoArr.add(carInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return carInfoArr;
    }

    /**
     * 由于代码重复利用，将查询车辆逃费信息中的重复代码提取出来
     *
     **/
    public JSONObject getCarInforme(ResultSet rs){

        JSONObject carInfo = new JSONObject();
        try {
            carInfo.put("laneexSerialNo",rs.getString("laneexSerialNo"));
            carInfo.put("carNo", rs.getString("exPlateNumber"));
            carInfo.put("enTime",rs.getString("enTime"));
            carInfo.put("exTime", rs.getString("exTime"));
            carInfo.put("enStation",rs.getString("enStation"));
            carInfo.put("exStation",rs.getString("exStation"));
            carInfo.put("history","0");
            carInfo.put("axisNum",rs.getString("axisNum"));
            carInfo.put("totalWeight",rs.getString("totalWeight"));
            carInfo.put("action", rs.getString("action"));
            //通过flag确定逃费类型
            String passCardDodge = rs.getString("passCardDodge");
            String weightDodge = rs.getString("weightDodge");
            String audit = getAudit(passCardDodge,weightDodge);
            carInfo.put("audit",audit);
            carInfo.put("passCardDodge", passCardDodge);
            carInfo.put("weightDodge", weightDodge);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carInfo;

    }

}
