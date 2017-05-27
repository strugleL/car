package com.wangyichao.dao;

import com.wangyichao.utils.DataBaseTool;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by daivdyyl on 2017/4/13.
 */
@Repository("LoginDao")
public class LoginDao {
    DataBaseTool dataBaseTool = DataBaseTool.getInstance();

    //用户登录操作
    public int collectorLogin(String username, String password,String identity) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql1 = "select count(*) from tollcollector where operator_id = ? and e_password=?";
        String sql2 = "select count(*) from owner where owner_id = ? and owner_password=?";
        int result = 0;
        try {
            // 开始判断身份
            if (identity.equals("option1")){
                ps = conn.prepareStatement(sql1);
            }else if (identity.equals("option2")){
                ps = conn.prepareStatement(sql2);
            }
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 根据身份得到用户姓名
    public String getName(String username) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select name from tollcollector where operator_id = ?";
//        String sql2 = "select name from owner where owner_id = ?";
        String name = "";
        try {
            //判断身份，不同sql语句
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            while (rs.next()){
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}
