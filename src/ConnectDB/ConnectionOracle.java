/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectDB;

import java.sql.*;

/**
 * Class Connection Oracle
 * @author nguyenminhnhut
 */
public class ConnectionOracle {
    
    /**
     * Function get Oracle Connection 
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static Connection getOracleConnection() throws ClassNotFoundException,
            SQLException {

        //Host name
        String hostName = "localhost";
        //SID Oralce
        String sid = "orcl";
        //Username
        String userName = "QLHANGKHONG";
        //Password
        String password = "Admin123";
        
        // Khai báo class Driver cho DB Oracle
        // Việc này cần thiết với Java 5
        // Java6 tự động tìm kiếm Driver thích hợp.
        // Nếu bạn dùng Java6, thì ko cần dòng này cũng được.
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Cấu trúc URL Connection dành cho Oracle
        // Ví dụ: jdbc:oracle:thin:@localhost:1521:db11g
        String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;

        //Tạo đối tượng connection
        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);
        
        return conn;
    }


}
