//package view.server.DAO;

//import view.client.Library.BookBorrow;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import BookDao.java;

/**
 * 图书馆模块借书DAO
 */
public class BookBorrowDao {

    /**
     * 查找该用户目前借了那些书
     *
     * @param uId 查询用户一卡通号
     * @return 该用户的所有现存借书记录，已BookBorrow[]的形式返回
     */

    public BookBorrow[] findBookBorrowsById(String uId) {
        String sqlString = "SELECT * FROM tblBookBorrow WHERE uId = '" + uId + "'";
        BookBorrow[] allbookborrow = null;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            res.last(); // 移动到最后一行以获取行数
            int count = res.getRow(); // 获取行数
            res.beforeFirst(); // 移动到结果集的开始

            if (count == 0) {
                return null; // 没有找到
            }

            allbookborrow = new BookBorrow[count];
            int index = 0;
            while (res.next()) {
                allbookborrow[index] = new BookBorrow(res.getString("uId"), res.getString("bookId"), res.getString("borrowTime"),res.getString("outdateTime"));
                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allbookborrow;
    }
    }


    /**
     * 借书，在BookBorrow表中新增该用户对该书本的借书记录
     *
     * @param borrowRecord 借书操作记录（包涵操作号、一卡通号、书号、操作时间、操作类型、备注）
     * @return 借书操作是否成功，如该书已被该用户借阅，则返回false，并不进行借书操作
     */
    public boolean Borrow(BookBorrow borrowRecord) {
        String sqlString = "INSERT INTO tblBookBorrow(uId, bookId, borrowTime, outdateTime) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            String bookId=borrowRecord.getbId();
            Book book=findBookBybId(bookId);
            if(book.getFreeNum()<1)
            {
                con.close();
                System.out.println("借书失败：当前书目已无剩余");
                return false; //
            }

            else {
                pstmt.setString(1, borrowRecord.getuId());
                pstmt.setString(2, borrowRecord.getbookId());
                pstmt.setString(3, borrowRecord.getborrowTime());
                pstmt.setString(4, borrowRecord.getoutdateTime());

                int num = pstmt.executeUpdate();
                return num > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 还书，在BookBorrow表中删除该用户对该书本的借书记录
     * @param borrowRecord 还书操作记录（包涵操作号、一卡通号、书号、操作时间、操作类型、备注）
     * @return 还书操作是否成功，如该书还未被该用户借阅，或者操作日期晚于借阅过期时间，则返回false，并不进行还书操作
     */
    public boolean Return(BookBorrow borrowRecord) {
        String sqlString = "DELETE FROM tblBookBorrow WHERE uId = ? AND bookId = ?";

        // 数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {


            pstmt.setString(1, borrowRecord.getuId());
            pstmt.setString(2, borrowRecord.getbookId());

            pstmt.executeUpdate(); // 执行删除操作

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 续借，在BookBorrow表中修改该用户对该书本的借书情况，将过期时间改为当前操作时间加10；
     *
     * @param renewRecord 续借操作记录（包涵操作号、一卡通号、书号、操作时间、操作类型、备注）
     * @return 续借操作是否成功，如该书还未被该用户借阅，则返回false，或者操作日期晚于借阅过期时间，并不进行续借操作
     */

    public boolean Renew(BookBorrow renewRecord) {
        String sqlString = "UPDATE tblBookBorrow SET outdateTime = ?, WHERE uId = ? AND bookId = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, Integer.parseInt(getoutdateTime())+10);
            pstmt.setString(2, renewRecord.getuId());
            pstmt.setString(3, getbookId());


            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查找所有用户的当前借书情况（管理员系统可用）
     *
     * @return 所有用户的当前借书情况，以BookHol[]的形式返回
     */
    public BookBorrow[] findAllBookBorrows() {
        String sqlString = "SELECT * FROM tblBookBorrow";
        List<BookBorrow> bookrecordlist = new ArrayList<>(); // 用于存储结果的列表

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            // 逐行读取结果集
            while (res.next()) {

                BookBorrow bookrecord = new BookBorrow(
                        res.getString("uId"),
                        res.getString("bookId"),
                        res.getString("borrowTime"),
                        res.getString("outdateTime")
                );
                bookrecordlist.add(bookrecord); // 添加到列表
            }
            //System.out.println("length " + studentInfoList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将列表转换为数组并返回
        //System.out.println("length = " + studentInfoList.toArray(new StudentInfo[0]).length);
        return bookrecordlistlist.isEmpty() ? null : bookrecordlistlist.toArray(new BookBorrow[0]);

}
