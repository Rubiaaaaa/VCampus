//package view.server.DAO;
//import view.client.Bank.bankAccount;

import java.sql.*;

/**
 * 银行模块账户DAO
 */
public class BankDao {


    /**
     * 判断是否挂失，true表示正常，false表示挂失
     * @param id 账户ID
     * @return 是否挂失
     */
    public boolean isLoss(String id) {
        String sqlString = "SELECT * FROM tblBank WHERE aId = ?";

        boolean check = false;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, id);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                // 如果找到记录，获取aLoss的值
                String aLoss = res.getString("aLoss");
                // 判断aLoss是否为"1"
                check = "1".equals(aLoss);
            } else {
                throw new SQLException("Record not found for aId: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return check;

    }

    /**
     * 通过一卡通号查询银行账户信息
     * @param id 一卡通号uId
     * @return 该uId对应的一个 bankAccount类的银行账户数据
     */
    public BankPojo findBankAccountById(String aId) {

        String sqlString = "SELECT * FROM tblBank WHERE aId = ?";
        BankPojo banker = null;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, aId);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                banker = new BankPojo();
                banker.setaId(res.getString("aId"));
                banker.setaName(res.getString("aName"));
                banker.setaPwd(res.getString("aPwd"));
                banker.setaMoney(res.getString("aMoney"));
                banker.setaLoss(res.getInt("aLoss"));

            }

            else {
                throw new SQLException("not found ");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentInfo;
    }

    /**
     * 通过传入的一卡通号id和用户输入的支付密码Pwd，从数据库中查找相应用户并判断密码是否正确
     * @param id 一卡通号id
     * @param Pwd 支付密码Pwd
     * @return 密码是否正确
     */
    public boolean checkPwd(String aId, String Pwd) {

        String sqlString = "SELECT * FROM tblBank WHERE aId = ?";
        BankPojo banker = null;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, aId);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return false;
            } //如果该账户不存在，则返回false
            res.beforeFirst();

            res.next();
            if (!Pwd.equals(res.getString("aPwd"))) {
                return false; //如果密码错误，则返回false
            }

            con.close();//关闭数据库连接

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 通过传入的和用户输入的原支付密码oldPwd、新密码newPwd，从数据库中查找相应用户判断密码是否正确，并更改密码
     * @param id 一卡通号id
     * @param oldPwd 原支付密码oldPwd
     * @param newPwd 新密码newPwd
     * @return 是否修改成功
     */
    public boolean changePwd(String aId, String oldPwd, String newPwd) {
        String sqlString = "SELECT * FROM tblBank WHERE aId = ?";
        BankPojo banker = null;

        // 数据库地址
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, aId);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return false; // 如果该账户不存在，则返回false
            }

            // 重新设置参数，查询到数据后再次执行查询获取密码
            pstmt.setString(1, aId);
            res = pstmt.executeQuery();

            if (res.next()) {
                if (oldPwd.equals(res.getString("aPwd"))) {
                    // 更新密码
                    String updateSql = "UPDATE tblBank SET aPwd = ? WHERE aId = ?";
                    try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
                        updateStmt.setString(1, newPwd);
                        updateStmt.setString(2, aId);
                        updateStmt.executeUpdate();
                        return true; // 密码更新成功，返回true
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    return false; // 如果密码错误，则返回false
                }
            }

            con.close(); // 关闭数据库连接

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 通过传入的一卡通号id,从数据库中查找相应用户并更改其挂失状态
     * 原本挂失则变为正常，原本正常则变为挂失
     * @param id 一卡通号id
     * @return 操作是否成功
     */
    public boolean ChangeLoss(String id) {
        String sqlSelect = "SELECT aLoss FROM tblBank WHERE aId = ?";
        String sqlUpdate = "UPDATE tblBank SET aLoss = ? WHERE aId = ?";
        boolean isToggled = false;

        // 数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement selectStmt = con.prepareStatement(sqlSelect);
             PreparedStatement updateStmt = con.prepareStatement(sqlUpdate)) {

            // 查询当前aLoss的值
            selectStmt.setString(1, id);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                String currentLoss = resultSet.getString("aLoss");
                String newLossValue = currentLoss.equals("1") ? "0" : "1";

                // 更新aLoss的值
                updateStmt.setString(1, newLossValue);
                updateStmt.setString(2, id);
                int rowsAffected = updateStmt.executeUpdate();

                isToggled = rowsAffected > 0; // 判断是否更新成功
            } else {
                throw new SQLException("Record not found for aId: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isToggled;
    }


    /**
     * 通过一卡通号查询银行账户，并进行充值操作
     * @param id 一卡通号id
     * @param money 充值金额money
     * @return 是否充值成功
     */

        public static boolean updateUserRole(BankPojo banker, Int money) {
            String sqlString = "UPDATE tblBank SET aMoney = ? WHERE aId = ?";

            try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
                 PreparedStatement pstmt = con.prepareStatement(sqlString)) {

                pstmt.setString(1, Integer.parseInt(banker.getaMoney())+money);
                pstmt.setString(2, banker.getaId());

                int num = pstmt.executeUpdate();
                return num > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


    /**
     * 查询并返回所有的银行账户信息。
     *
     * @return 包含所有银行账户的 bankAccount[] 数组，如果没有找到任何账户则返回 null。
     */
    public BankPojo[] findallBankAccounts() {
        String sqlString = "SELECT * FROM tblBank";
        List<BankPojo> BankList = new ArrayList<>(); // 用于存储结果的列表

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString);
             ResultSet res = pstmt.executeQuery()) {

            // 逐行读取结果集
            while (res.next()) {

                BankPojo banker = new BankPojo(
                        res.getString("aId"),
                        res.getString("aName"),
                        res.getString("aPwd"),
                        res.getString("aMoney"),
                        res.getInt("aLoss")

                );
                BankList.add(banker); // 添加到列表
            }
            //System.out.println("length " + studentInfoList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将列表转换为数组并返回
        //System.out.println("length = " + studentInfoList.toArray(new StudentInfo[0]).length);
        return BankList.isEmpty() ? null : BankList.toArray(new BankPojo[0]);

    }

    /**
     * 创建银行账户
     * @param bankaccount 用户输入的信息
     * @return 是否创建成功
     */
    public boolean addBankAccount(BankPojo banker) {
        String sqlString = "INSERT INTO tblBank(aId, aName, aPwd, aMoney, aLoss) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db");
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, banker.getaId());
            pstmt.setString(2, banker.getaName());
            pstmt.setString(3, banker.getaPwd());
            pstmt.setInt(4, banker.getaMoney());
            pstmt.setInt(5, banker.getaLoss());


            int num = pstmt.executeUpdate();
            return num > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
}
