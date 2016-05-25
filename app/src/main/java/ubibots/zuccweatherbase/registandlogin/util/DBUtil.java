package ubibots.zuccweatherbase.registandlogin.util;

import java.sql.Connection;

public class DBUtil {
    private static final String jdbcUrl="jdbc:mysql://10.66.15.150:3306/weatherbase?useUnicode=true&characterEncoding=UTF-8&useSSL=true&connectTimeout=10000";
    private static final String dbUser="root";
    private static final String dbPwd="zucc";
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    public static Connection getConnection() throws java.sql.SQLException{
        return java.sql.DriverManager.getConnection(jdbcUrl, dbUser, dbPwd);
    }
}