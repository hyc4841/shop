package love.shop;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Fail.fail;

public class MySQLTest {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnection() {

        try(Connection con =
                    DriverManager.getConnection(
                            "jdbc:mysql://127.0.0.1:3306/shop?serverTimezone=Asia/Seoul",
                            "root",
                            "1234")){

            System.out.println(con);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
