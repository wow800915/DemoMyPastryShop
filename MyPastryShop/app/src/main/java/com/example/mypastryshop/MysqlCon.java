package com.example.mypastryshop;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {

    // 資料庫定義192.168.200.33
//    String mysql_ip = "192.168.200.33";//宿舍
    String mysql_ip = "192.168.0.41";//圖書館網路
//    String mysql_ip = "192.168.43.96";//教室網路
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "uvshopping";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "lan";
    String db_password = "123456";
    String userEmail =null;
    String userPassword =null;
    String userAddress =null;
    String loginEmail =null;
    String updateAddress =null;

    MysqlCon(){
    }

    MysqlCon(String UserEmail,String UserPassword,String UserAddress){
        this.userEmail =UserEmail;
        this.userPassword =UserPassword;
        this.userAddress =UserAddress;
    }

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e) {
            Log.e("DB","加載驅動失敗");
            return;
        }

        // 連接資料庫
        try {
            Log.v("DB","遠端連接成功測試");
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
        }catch(SQLException e) {
            Log.e("DB","遠端連接失敗");
            Log.e("DB", e.toString());
        }
    }

    public String insertData() {
        String data = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql ="INSERT INTO `member` (`username`, `password`, `email`, `uname`, `gender`, `birthday`, `phone`, `county`, `district`, `zipcode`, `address`, `date`, `mUpDate`) VALUES ('aaa', '"+ userPassword +"', '"+ userEmail +"', 'aaaa', '1', '2000-01-01', '0900000000', '無', '無', '000', '"+ userAddress +"', '2000-01-01 01:01:01', '2000-01-01 01:01:01');";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            return "成功";
        } catch (SQLException e) {
            e.printStackTrace();
            return "失敗";
        }
    }
    public String getData() {
        String data = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM `member`";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String gId = rs.getString("id");
                String gTitle = rs.getString("email");
                String gBirthday =rs.getString("birthday");
                String gPhone =rs.getString("phone");
                String gName = rs.getString("uname");
                String gAddress = rs.getString("address");
                data += gId+".Email: " + gTitle  + ",姓名: " + gName  + ",生日: " + gBirthday  + ",手機: " + gPhone  + ",地址: " + gAddress + "\n";
            }
            st.close();
            Log.v("DB","111112300");

        } catch (SQLException e) {
            e.printStackTrace();
            Log.v("DB","321000");
        }
        return data;
    }
    public String getOneData(String updateAddress) {
        String data = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM `member` WHERE email ='"+updateAddress+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String gId = rs.getString("id");
                String gTitle = rs.getString("email");
                String gBirthday =rs.getString("birthday");
                String gPhone =rs.getString("phone");
                String gName = rs.getString("uname");
                String gAddress = rs.getString("address");
                data +="Email: " + gTitle  +"\n"+ ",姓名: " + gName  +"\n"+ ",生日: " + gBirthday  +"\n"+ ",手機: " + gPhone  +"\n"+ ",地址: " + gAddress;
            }
            st.close();
            Log.v("DB","111112300");

        } catch (SQLException e) {
            e.printStackTrace();
            Log.v("DB","321000");
        }
        return data;
    }

    public String updateOneData(String loginEmail,String updateName,String updateBirthday,String updatePhone,String updateAddress) {
        String data = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "UPDATE `member` SET `uname` = '"+updateName+"', `birthday` = '"+updateBirthday+"', `phone` = '"+updatePhone+"', `address` = '"+updateAddress+"' WHERE `member`.`email` = '"+loginEmail+"'";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB","更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v("DB","更新失敗");
        }
        return data;
    }

}