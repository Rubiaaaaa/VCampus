//package xxxx.xxxx.DAO;

//import xxxx.xxxx.Course;
/**导入所需要的Course类，包括courseId，courseName等**/

import java.sql.*;


public class CourseDao {
    /**
     * 通过课程编号查找课程
     * @param courseNum 课程编号
     * @return 查找到的课程Course
     */
    public CoursePojo findCourseByNum(String courseId) {

        String sqlString = "SELECT * FROM tblCourse WHERE courseId = ?";
        CoursePojo course = null;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, courseId);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                course = new Course();

                course.setCourseID(res.getString("courseId"));
                course.setCourseName(res.getString("courseName"));
                course.setCourseTeacher(res.getString("courseTeacher"));
                course.setCourseCapacity(res.getString("courseCapacity"));
                course.setCoursePeopleNumber(res.getString("coursePeopleNumber"));
                course.setCourseDate(res.getString("courseDate"));
                course.setCourseStart(res.getString("courseStart"));
                course.setCourseEnd(res.getString("courseEnd"));
                course.setCourseLocation(res.getString("Location"));
                course.setCourseCredit(res.getString("credit"));
            }
            //若未查找到
            else {
                throw new SQLException("not Found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return course;
    }

    /**
     * 显示所有的课程
     * @return 所有的课程Course[]
     */
    public CoursePojo[] showAllCourse() {
        String sqlString = "SELECT * FROM tblCourse";
        List<CoursePojo> courselist = new ArrayList<>(); // 用于存储结果的列表

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            // 逐行读取结果集
            while (res.next()) {
                // 创建 StudentInfo 对象并添加到列表中
                CoursePojo course = new CoursePojo(
                        res.getString("courseId"),
                        res.getString("courseName"),
                        res.getString("courseTeacher"),
                        res.getString("courseCapacity"),
                        res.getString("coursePeopleNumber"),
                        res.getString("courseDate"),
                        res.getString("courseStart"),
                        res.getString("courseEnd"),
                        res.getString("Location"),
                        res.getString("credit")

                );
                courselist.add(course); // 添加到列表
            }
            //System.out.println("length " + studentInfoList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将列表转换为数组并返回
        //System.out.println("length = " + studentInfoList.toArray(new StudentInfo[0]).length);
        return courselist.isEmpty() ? null : courselist.toArray(new CoursePojo[0]);
    }

    /**
     * 添加课程
     * @param course 用户输入的信息
     * @return 是否添加成功
     */
    public boolean createCoures(CoursePojo course) {
            String sqlString = "INSERT INTO tblcourse(courseId, courseName, courseTeacher, courseCapacity, credit, coursePeopleNumber, courseDate, courseStart, courseEnd, courseLocation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
                 PreparedStatement pstmt = con.prepareStatement(sqlString)) {

                pstmt.setString(1, course.getCourseID());
                pstmt.setString(2, course.getCourseName());
                pstmt.setString(3, course.getCourseTeacher());
                pstmt.setString(4, course.getCourseCapcity());
                pstmt.setString(5, course.getCredit());
                pstmt.setString(6, course.getCoursePeopleNumber());
                pstmt.setString(7, course.getCourseDate());
                pstmt.setString(8, course.getCourseStart());
                pstmt.setString(9, course.getCourseEnd());
                pstmt.setString(10, course.getCourseLocation());



                int num = pstmt.executeUpdate();
                return num > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

    }


    /**
     * 更新课程
     * @param course 用户输入的信息
     * @return 是否更新成功
     */
    public boolean updateCourse(CoursePojo course) {
            String sqlString = "UPDATE tblCourse SET courseName = ?, courseTeacher = ?, courseCapacity = ?, credit = ?, " +
                    "coursePeopleNumber = ?, courseDate = ?,courseStart = ?, courseEnd = ?, courseLocation = ? WHERE courseId = ?";

            try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
                 PreparedStatement pstmt = con.prepareStatement(sqlString)) {

                pstmt.setString(1, course.getCourseName());
                pstmt.setString(2, course.getCourseTeacher());
                pstmt.setString(3, course.getCourseCapcity());
                pstmt.setString(4, course.getCredit());
                pstmt.setString(5, course.getCoursePeopleNumber());
                pstmt.setString(6, course.getCourseDate());
                pstmt.setString(7, course.getCourseStart());
                pstmt.setString(8, course.getCourseEnd());
                pstmt.setString(9, course.getCourseLocation());
                pstmt.setString(10, course.getCourseID());


                int num = pstmt.executeUpdate();
                return num > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    /**
     * 删除课程
     * @param courseId 要删除的课程的编号
     * @return 是否删除成功
     */
    public boolean deleteCouresById(String courseId) {
            String sqlString = "DELETE FROM tblCourse WHERE courseId = ?";

            // 数据库地址，绝对路径
            String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

            try (Connection con = DriverManager.getConnection(url);
                 PreparedStatement pstmt = con.prepareStatement(sqlString)) {

                pstmt.setString(1, courseId);

                pstmt.executeUpdate(); // 执行删除操作

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;

}


