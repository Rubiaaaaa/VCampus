//package xxxx.xxxx.DAO;

//import xxxx.xxxx.StudentInfo;
/**导入所需要的StudentInfo类，包括uid,urole,uname,usex,uage,udate,uacademy**/

import java.sql.*;
import java.text.SimpleDateFormat;

public class StudentInfoDao {
    /**
     * 通过一卡通号查询学籍信息
     * @param uId 一卡通号
     * @return 该uId对应的一个 StudentInfo类的学籍信息数据
     */

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
                studentInfo = new studentInfo();
                studentInfo.setId(res.getString("uId"));
                studentInfo.setRole(res.getString("uRole"));
                studentInfo.setName(res.getString("uName"));
                studentInfo.setSex(res.getString("uSex"));
                studentInfo.setDate(res.getInt("uDate"));
                studentInfo.setAge(res.getString("uAge"));
                studentInfo.setAcademy(res.getString("uAcademy"));
            }

            else {
                throw new SQLException("student not found for uId: " + uId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentInfo;

    }
    /**
     * 增加学生学籍信息
     * @param stuInfo 想要添加的学生学籍信息
     * @return 是否成功
     */
    public static boolean AddStudentInfo(StudentInfo stuInfo) {
        /*
            增加学生学籍信息
            传入参数为想要添加的学生学籍信息
         */

        String sqlString = "INSERT INTO tblStudentInfo(uId, uRole, uName, uSex, uDate, uAge, uAcademy) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, studentInfo.getId());
            pstmt.setString(2, studentInfo.getRole());
            pstmt.setString(3, studentInfo.getName());
            pstmt.setString(4, studentInfo.getSex());
            pstmt.setInt(5, studentInfo.getDate());
            pstmt.setString(6, studentInfo.getAge());
            pstmt.setString(7, studentInfo.getAcademy());

            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据一卡通号删除学生学籍信息
     * @param uId 一卡通号
     * @return 是否成功删除
     */
    public boolean deleteStudentInfoById(String uId) {
        /*
         * 通过一卡通号删除学生信息
         * 传入参数为一卡通号uId
         */

        String sqlString = "DELETE FROM tblstudentInfo WHERE uId = ?";

        // 数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, uId);

            pstmt.executeUpdate(); // 执行删除操作

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询所有学生学籍信息
     * @return 所有学生的StudentInfo[]
     */
    public static StudentInfo[] showAllStudentInfo() {
            String sqlString = "SELECT * FROM tblStudentInfo";
            List<StudentInfo> studentInfoList = new ArrayList<>(); // 用于存储结果的列表

            try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
                 PreparedStatement pstmt = con.prepareStatement(sqlString);
                 ResultSet res = pstmt.executeQuery()) {

                // 逐行读取结果集
                while (res.next()) {
                    // 创建 StudentInfo 对象并添加到列表中
                    StudentInfo studentInfo = new StudentInfo(
                            res.getString("uId"),
                            res.getString("uRole"),
                            res.getString("uName"),
                            res.getString("uSex"),
                            res.getInt("uAge"),
                            res.getInt("uDate"),
                            res.getString("uAcademy")
                    );
                    studentInfoList.add(studentInfo); // 添加到列表
                }
                //System.out.println("length " + studentInfoList.size());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 将列表转换为数组并返回
            //System.out.println("length = " + studentInfoList.toArray(new StudentInfo[0]).length);
            return studentInfoList.isEmpty() ? null : studentInfoList.toArray(new StudentInfo[0]);

        }

    /**
     * 修改学生信息
     * @param stuInfo
     * @return 是否修改成功
     */
    public boolean ModifyStudentInfo(StudentInfo studentInfo){

            String sqlString = "UPDATE tblStudentInfo SET uRole = ?, uName = ?, uSex = ?, uDate = ?, uAge = ?, uAcademy = ? WHERE uId = ?";

            try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
                 PreparedStatement pstmt = con.prepareStatement(sqlString)) {


                pstmt.setString(1, studentInfo.getRole());
                pstmt.setString(2, studentInfo.getName());
                pstmt.setString(3, studentInfo.getSex());
                pstmt.setInt(4, studentInfo.getDate());
                pstmt.setString(5, studentInfo.getAge());
                pstmt.setString(6, studentInfo.getAcademy());
                pstmt.setString(7, studentInfo.getuId());

                int num = pstmt.executeUpdate();
                return num > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
}
