package com.wangyichao.dao;

import com.wangyichao.utils.DataBaseTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by daivdyyl on 2017/4/27.
 */

@Repository("ownerDao")
public class OwnerDao {

    DataBaseTool dataBaseTool = DataBaseTool.getInstance();

    public JSONArray getInfoById(String collectNumber) {
        JSONArray array = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tollcollector where operator_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,collectNumber);
            rs = ps.executeQuery();
            while (rs.next()){
                JSONObject obj = new JSONObject();
                obj.put("operate_id",collectNumber);
                obj.put("name",rs.getString("name"));
                obj.put("gender",rs.getString("gender"));
                obj.put("birth_date",rs.getString("birth_date"));
                obj.put("political_status",rs.getString("political_status"));
                obj.put("minzu",rs.getString("minzu"));
                obj.put("phone_number",rs.getString("phone_number"));
                obj.put("address",rs.getString("address"));
                obj.put("email",rs.getString("email"));
                obj.put("work_age",rs.getString("work_age"));
                array.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return array;
    }

    public JSONArray getInfoByName(String collectName) {
        JSONArray array = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tollcollector where name = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,collectName);
            rs = ps.executeQuery();
            while (rs.next()){
                JSONObject obj = new JSONObject();
                obj.put("operate_id",rs.getString("operator_id"));
                obj.put("name",collectName);
                obj.put("gender",rs.getString("gender"));
                obj.put("birth_date",rs.getString("birth_date"));
                obj.put("political_status",rs.getString("political_status"));
                obj.put("minzu",rs.getString("minzu"));
                obj.put("phone_number",rs.getString("phone_number"));
                obj.put("address",rs.getString("address"));
                obj.put("email",rs.getString("email"));
                obj.put("work_age",rs.getString("work_age"));
                array.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return array;
    }

    public JSONArray getInfoById_Name(String collectNumber, String collectName) {
        JSONArray array = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tollcollector where operator_id = ? and name = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,collectNumber);
            ps.setString(2,collectName);
            rs = ps.executeQuery();
            while (rs.next()){
                JSONObject obj = new JSONObject();
                obj.put("operate_id",collectNumber);
                obj.put("name",collectName);
                obj.put("gender",rs.getString("gender"));
                obj.put("birth_date",rs.getString("birth_date"));
                obj.put("political_status",rs.getString("political_status"));
                obj.put("minzu",rs.getString("minzu"));
                obj.put("phone_number",rs.getString("phone_number"));
                obj.put("address",rs.getString("address"));
                obj.put("email",rs.getString("email"));
                obj.put("work_age",rs.getString("work_age"));
                array.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return array;
    }

    public int deleteOperator(String operator_id) {
        int result = 0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        String sql = "delete from tollcollector where operator_id=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,operator_id);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(null,ps,conn);
        }
        return result;
    }

    public int addOperatorInfo(String operator_id, String name, String gender, String birth, String political, String minzu, String phone, String address, String email, String password, String status, String work_age) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        int result = 0;
        if (birth.equals("")){
            birth = null;
        }
        if (work_age.equals("")){
            work_age = "0";
        }
        int age = Integer.parseInt(work_age);
        // 若工号重复，直接插入失败
        String sql = "insert into tollcollector values (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,operator_id);
            ps.setString(2,name);
            ps.setString(3,gender);
            ps.setString(4,birth);
            ps.setString(5,political);
            ps.setString(6,minzu);
            ps.setString(7,phone);
            ps.setString(8,address);
            ps.setString(9,email);
            ps.setString(10,password);
            ps.setString(11,status);
            ps.setString(12,work_age);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(null,ps,conn);
        }
        return result;
    }

    //判断当前密码是否正确
    public int isOldpw(String owner_id, String nowpw) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(*) from owner where owner_id = ? and owner_password = ?";
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,owner_id);
            ps.setString(2,nowpw);
            rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs,ps,conn);
        }
        return result;
    }

    //修改密码
    public boolean changePw(String owner_id, String newpw) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        int result = 0;
        String sql = "update owner set owner_password = ? where owner_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,newpw);
            ps.setString(2,owner_id);
            result = ps.executeUpdate();
            if (result == 0 ){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(null,ps,conn);
        }
        return true;
    }
}
