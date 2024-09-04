//package view.server.DAO;

//import view.client.Library.Book;

import java.sql.*;

/**
 * 图书馆模块书籍DAO
 */
public class BookDao {


    /**
     * 根据书号查询书
     * @param bid 书号
     * @return 查询到的书本对象
     */
    public Book findBookBybId(String bookId) {

        Book book = new Book("", "", "", 0, 0, 0);//BookId,name,author,totalnum,freenum,borrownum

        String sqlString = "SELECT * FROM tblBook WHERE bookId = ?";


        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, bookId);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                book = new Book();
                book.setbId(res.getString("bookId"));
                book.setbName(res.getString("bookname"));
                book.setAuthor(res.getString("author"));
                book.setTotalNum(res.getInt("totalNum"));
                book.setFreeNum(res.getInt("freeNum"));
                book.setBorrowNum(res.getInt("borrowNum"));

            }
            //若未查找到
            else {
                throw new SQLException("not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    /**
     * 根据书名查询书
     *
     * @param bookname 书名
     * @return book
     */
    public Book[] findBookByBookName(String bookname) {
        Book book = new Book("", "", "", 0, 0, 0);//BookId,name,author,totalnum,freenum,borrownum

        String sqlString = "SELECT * FROM tblBook WHERE bookname = ?";


        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, bookname);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                book = new Book();
                book.setbId(res.getString("bookId"));
                book.setbName(res.getString("bookname"));
                book.setAuthor(res.getString("author"));
                book.setTotalNum(res.getInt("totalNum"));
                book.setFreeNum(res.getInt("freeNum"));
                book.setBorrowNum(res.getInt("borrowNum"));


            }
            //若未查找到
            else {
                throw new SQLException("not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }


    /**
     * 向数据库中新增插入书本
     *
     * @param book 需新增的书本
     * @return 新增插入是否成功, 若数据库中原本已存在该书籍，则返回false，不执行新增插入操作
     */
    public boolean addBook(Book book) {
        String sqlString = "INSERT INTO tblBook(bookId,bookname,author,totalNum,freeNum,borrowNum) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {


            pstmt.setString(1, book.getbId());
            pstmt.setString(2, book.getbName());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getTotalNum());
            pstmt.setInt(5, book.getFreeNum());
            pstmt.setInt(6, book.getBorrowNum());


            int num = pstmt.executeUpdate();
            return num > 0;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据书号删除书
     *
     * @param bId 书号
     * @return 删除操作是否成功，如该书原本在数据库中不存在，则返回false，并不进行删除操作
     */
    public boolean DeleteBook(String bId) {
        String sqlString = "DELETE FROM tblBook WHERE bookId = ?";

        // 数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, bId);

            pstmt.executeUpdate(); // 执行删除操作

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 修改书籍信息(可修改书名)
     *
     * @param book 修改的书籍（存储的是修改后新书的信息）
     * @return 修改操作是否成功，如该书原本在数据库中不存在，则返回false，并不进行修改操作
     */
    public boolean ModifyBook(Book book) {
        String sqlString = "UPDATE tblBook SET bookname = ?, author = ? WHERE bookid = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, user.getbName());
            pstmt.setString(2, user.getAuthor());
            pstmt.setString(3, book.getbId());



            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据书号借书，将该书籍在馆册数减一，被借次数加一
     *
     * @param bId 书号
     * @return 借书操作是否成功，如该书原本在数据库中不存在，或在馆册数不足以借阅则返回false，并不进行借书操作
     */
    public boolean borrowBook(Book book) {
        String sqlString = "UPDATE tblBook SET borrowNum = ?, freeNum= ? WHERE bookId = ?";

        // 数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            if(book.getFreeNum()<1)
            {
                con.close();
                System.out.println("借书失败：当前书目已无剩余");
                return false; //
            }
            else {
                pstmt.setInt(1, book.getBorrrowBum() + 1);
                pstmt.setInt(2, book.getFreeNum() - 1);
                pstmt.setString(3, book.getbId());
            }
            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据书号还书，将该书籍在馆册数加一
     *
     * @param bId 书号
     * @return 还书操作是否成功，如该书原本在数据库中不存在，或还书后在馆册数大于馆藏册数，则返回false，并不进行借书操作
     */
    public boolean Return(String bId) {
        String sqlString = "UPDATE tblBook SET borrowNum = ?, freeNum= ? WHERE bookId = ?";

        // 数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {


                pstmt.setInt(1, book.getBorrrowBum() - 1);
                pstmt.setInt(2, book.getFreeNum() + 1);
                pstmt.setString(3, book.getbId());

            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有书籍
     *
     * @return 查询到的所有书本对象，返回一个Book类的数组
     */
    public BookPojo[] findAllBooks() {
        String sqlString = "SELECT * FROM tblBook";
        List<BookPojo> booklist = new ArrayList<>(); // 用于存储结果的列表

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            // 逐行读取结果集
            while (res.next()) {

                BookPojo book = new BookPojo(
                        res.getString("bookId"),
                        res.getString("bookname"),
                        res.getString("author"),
                        res.getInt("totalNum"),
                        res.getInt("freeNum"),
                        res.getInt("borrowNum")

                );
                booklist.add(book); // 添加到列表
            }
            //System.out.println("length " + studentInfoList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将列表转换为数组并返回
        //System.out.println("length = " + studentInfoList.toArray(new StudentInfo[0]).length);
        return booklist.isEmpty() ? null : booklist.toArray(new BookPojo[0]);
    }

