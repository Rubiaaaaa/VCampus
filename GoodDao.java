//package view.server.DAO;

//import view.client.Shop.Good;

import java.sql.*;

/**
 * 超市商品DAO
 */
public class GoodDao {

    /**
     * 根据商品名称查询商品
     *
     * @param productName 商品名称
     * @return 查询到的商品对象
     */

    public Good findGoodByProductName(String productName) {

        String sqlString = "SELECT * FROM tblGood WHERE good_name = ?";
        Good good = null;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, productName);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                good = new Good();

                good.setGoodId(res.getString("good_id"));
                good.setGoodName(res.getString("good_name"));
                good.setGoodPrice(res.getString("good_price"));
                good.setGoodCategory(res.getString("category"));
                good.setGoodStock(res.getString("good_stock"));
            }
            //若未查找到
            else {
                throw new SQLException(" not found" );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return good;
    }

    /**
     * 根据商品号goodId查询商品
     *
     * @param goodId 商品号
     * @return 查询到的商品对象
     */
    public Good findGoodByGoodId(String goodId) {
        String sqlString = "SELECT * FROM tblGood WHERE good_id = ?";
        Good good = null;

        //数据库地址，绝对路径
        String url = "jdbc:sqlite:E:\\JavaProjects\\VCampus\\src\\main\\java\\com\\vcampus\\db\\my_db.db";

        //连接数据库
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(sqlString)) {

            pstmt.setString(1, goodId);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                good = new Good();

                good.setGoodId(res.getString("good_id"));
                good.setGoodName(res.getString("good_name"));
                good.setGoodPrice(res.getString("good_price"));
                good.setGoodCategory(res.getString("category"));
                good.setGoodStock(res.getString("good_stock"));
            }
            //若未查找到
            else {
                throw new SQLException(" not found" );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return good;
    }

    /**
     * 根据商品id查询商品，并对该商品库存进行减少操作
     *
     * @param goodId 商品id
     * @param num    商品减少的数量
     * @return 商品是否减少成功，若该商品原库存量小于减少数量num,则返回false
     * 如果减少后商品库存刚好为0，则在数据库中删除该商品相关数据
     */
    public boolean reduceGood(String goodId, int num) {
        String sqlString1 = "select * from tblGood where good_id = '" + goodId + "'";
        //查询原有余额
        int original_stock = 0;

        try {
            Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
            //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
            Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet res = sta.executeQuery(sqlString1);

            if (!res.next()) {
                return false;
            } //如果该商品不存在，则返回false
            res.beforeFirst();

            res.next();
            original_stock = res.getInt(6); //查询该商品原本的库存

            con.close();//关闭数据库连接

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int new_stock = original_stock - num;
        if (new_stock < 0) return false;  //若减少数量大于现有库存，则不进行减少操作，并返回false
        if (new_stock > 0) {
            String sqlString2 = "update tblGood set good_stock = '" + new_stock + "' where good_id = '" + goodId + "'";
            //若减少数量后还有库存,则进行减少操作，修改商品库存量的值

            try {
                Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
                //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名 ，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
                Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                int count = sta.executeUpdate(sqlString2);
                if (count == 0) return false; //若未进行更改操作，则返回false

                con.close();//关闭数据库连接

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sqlString2 = "delete from tblGood where good_id = '" + goodId + "'";
            //若减少数量后，库存刚好为0，则不在系统中显示该商品，在数据库中删除该商品相关数据

            try {
                Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
                //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名 ，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
                Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                int count = sta.executeUpdate(sqlString2);
                if (count == 0) return false; //若未进行删除操作，则返回false

                con.close();//关闭数据库连接

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    /**
     * 进货\增加商品 操作
     *
     * @param good 需要增加的商品，
     *             如果通过该商品id查询数据库发现该商品原来就在数据库中已存在，则视为进货操作，该传入参数good的库存goodStock值视作对该商品进货增加了多少的数量；
     *             如原本该商品在数据库中不存在，则视为新增加商品操作，传入参数good的所有属性值作为一条数据insert到数据库中
     * @return 进货\增加商品 操作是否成果
     */
    public boolean addGood(Good good) {
        boolean exist = false; //用exist变量表示传入的参数good所代表的商品在数据库中是否已经存在
        int old_stock = 0; //若该商品在数据库中原本存在，则用old_stock在查找时记录该商品的原库存量


        String sqlString = "select * from tblGood where good_id = '" + good.getGoodId() + "'";
        try {
            Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
            //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
            Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet res = sta.executeQuery(sqlString);

            if (!res.next()) {
                exist = false;
            } else {
                exist = true;
                old_stock = res.getInt(6);
            }
            res.beforeFirst();
            con.close();//关闭数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!exist) {
            //如果传入参数代表的商品在原数据库不存在，则在数据库插入一条代表该good信息的数据
            String sqlString2 = "insert into tblGood values('" + good.getGoodId() + "','" + good.getGoodName() + "'," +
                    good.getGoodPrice() + ",'" + good.getCategory() + "','"+ good.getGoodStock() + ")";

            try {
                Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
                //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名 ，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
                Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                sta.executeUpdate(sqlString2);

                con.close();//关闭数据库连接

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            //如果传入参数代表的商品在原数据库已存在，则修改其库存量属性的值，且此时将传入参数good的库存量goodStock值视作进货进了多少的数量
            int new_stock = old_stock + good.getGoodStock();

            String sqlString2 = "update tblGood set good_stock = '" + new_stock + "' where good_id = '" + good.getGoodId() + "'";

            try {
                Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
                //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名 ，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
                Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                int count = sta.executeUpdate(sqlString2);
                if (count == 0) return false; //若未进行更改操作，则返回false

                con.close();//关闭数据库连接

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    /**
     * 查询所有的商品
     *
     * @return 查询到的所有商品对象，以Good类数组的形式返回
     */
    public Good[] findAllGoods() {
        String sqlString = "select * from tblGood order by good_id";
        Good[] allGoods = new Good[10];

        try {
            Class.forName("com.hxtt.sql.access.AccessDriver");//导入Access驱动文件，本质是.class文件
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:Access:///.\\Database\\vCampus.mdb", "", "");
            //与数据库建立连接，getConnection()方法第一个参数为jdbc:Access:///+文件总路径,第二个参数是用户名，第三个参数是密码（Access是没有用户名和密码此处为空字符串）
            Statement sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet res = sta.executeQuery(sqlString);

            res.last();
            int count = res.getRow();
            res.beforeFirst();

            if (count == 0) {
                return null;
            }//若数据库中无商品信息，则返回null

            allGoods = new Good[count];
            int index = 0;
            while (res.next()) {//不断的移动光标到下一个数据
                allGoods[index] = new Good(res.getString(1), res.getString(2), res.getDouble(3),
                        res.getString(4), res.getInt(6));
                index++;
            }

            con.close();//关闭数据库连接

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allGoods;
    }


}
