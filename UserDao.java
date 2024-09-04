package com.vcampus.dao;
//dao层文件的位置
import com.vcampus.pojo.User;
//导入所需要的类，这里需要User类
import java.sql.*;

public class UserDao {

    //根据uid查找用户
    public static User findUserByuId(String uId) {

        String sqlString = "SELECT * FROM tblUser WHERE uId = ?";
        User user = null;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, uId);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                user = new User();
                user.setuId(res.getString("uId"));
                user.setuPwd(res.getString("uPwd"));
                user.setuRole(res.getString("uRole"));
            }
            //若未查找到
            else {
                throw new SQLException("User not found for uId: " + uId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    //创建用户
    public static boolean createUser(User user) {
        String sqlString = "INSERT INTO tblUser(uId, uPwd, uRole) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {


            pstmt.setString(1, user.getuId());
            pstmt.setString(2, user.getuPwd());
            pstmt.setString(3, user.getuRole());

            int num = pstmt.executeUpdate();
            return num > 0;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    //更新用户信息
    public static boolean updateUser(User user) {
        String sqlString = "UPDATE tblUser SET uPwd = ?, uRole = ? WHERE uId = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, user.getuPwd());
            pstmt.setString(2, user.getuRole());
            pstmt.setString(3, user.getuId());

            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 示例：查找全部老师
    public User[] findAllTeachers() {
        String sqlString = "SELECT * FROM tblUser WHERE uRole = 'TC'";
        User[] allTeachers = null;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            res.last(); // 移动到最后一行以获取行数
            int count = res.getRow(); // 获取行数
            res.beforeFirst(); // 移动到结果集的开始

            if (count == 0) {
                return null; // 没有找到教师
            }

            allTeachers = new User[count];
            int index = 0;
            while (res.next()) {
                allTeachers[index] = new User(res.getString("uId"), res.getString("uPwd"), res.getString("uRole"));
                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allTeachers;
    }

    // 示例：查找全部学生
    public User[] findAllStudents() {
        String sqlString = "SELECT * FROM tblUser WHERE uRole = 'ST'";
        User[] allStudents = null;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            res.last(); // 移动到最后一行以获取行数
            int count = res.getRow(); // 获取行数
            res.beforeFirst(); // 移动到结果集的开始

            if (count == 0) {
                return null; // 没有找到学生
            }

            allStudents = new User[count];
            int index = 0;
            while (res.next()) {
                allStudents[index] = new User(res.getString("uId"), res.getString("uPwd"), res.getString("uRole"));
                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allStudents;
    }
}
