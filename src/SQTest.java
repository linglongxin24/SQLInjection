
import cn.bluemobi.dylan.util.DBUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-18.
 */
public class SQTest {
    public static void main(String args[]) {
        query4();
    }

    /**
     * SQL注入问题
     */
    public static void query4() {

        try {
            /**数据库表名**/
            String tableName = "emp_test";

            /**先查看一下数据库当中的表数据**/
            DBUtil.query(tableName, null);

            /**用户输入的用户名**/
            String name = "'1' OR '1'='1'";
            /**用户输入的密码**/
            String password = "'1' OR '1'='1'";

            /**我们拼SQL语句去查询**/
            String sql = "SELECT * FROM emp_test WHERE name = " + name + " and password = " + password;
            DBUtil.query(sql);

            /**我们用参数绑定去查询**/
            String where = "name = ?  AND password = ? ";
            String[] whereArgs = new String[]{name, password};
            DBUtil.query("emp_test", where, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //    private static void update(){
//        String sql="ALTER TABLE emp_test ADD  password VARCHAR(16)";
//        try {
//            DBUtil.executeUpdate(sql,null);
//        } catch (SQLException e) {
//        }
//        Map<String,Object> valueMap=new HashMap<>();
//        valueMap.put("password","123456");
//        try {
//            DBUtil.update("emp_test",valueMap,null);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
