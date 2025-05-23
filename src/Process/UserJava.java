
package Process;

import ConnectDB.ConnectionUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Types;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class UserJava {
    
    public int themUser(String username, String fullname, String email, String passwordHash) {
    int i = 0;
    Connection con = null;
    try {
        con = ConnectionUtils.getMyConnection();
        con.setAutoCommit(false); // Giao dịch thủ công

        String insertUser = "INSERT INTO \"USER\" (FULL_NAME, EMAIL) VALUES(?, ?)";
        PreparedStatement psUser = con.prepareStatement(insertUser, new String[] {"USER_ID"});
        psUser.setString(1, fullname);
        psUser.setString(2, email);

        int rowsAffected = psUser.executeUpdate();
        if (rowsAffected > 0) {
            ResultSet rs = psUser.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            if (userId != -1) {
                CallableStatement cs = con.prepareCall("{? = call FN_TAO_MAKH}");
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.execute();
                String maKH = cs.getString(1);

                String insertAccount = "INSERT INTO ACCOUNT(USER_ID, USERNAME, PASSWORD_HASH, MaKhachHang) VALUES(?, ?, ?, ?)";
                PreparedStatement psAccount = con.prepareStatement(insertAccount);
                psAccount.setInt(1, userId);
                psAccount.setString(2, username);
                psAccount.setString(3, passwordHash);
                psAccount.setString(4, maKH);

                i = psAccount.executeUpdate();
                
                // Thêm vào bảng khách hàng
                String insertKH = "INSERT INTO KHACH_HANG(MaKhachHang, HoTen, Email, ThoiGianCapNhatDT) VALUES (?, ?, ?, SYSTIMESTAMP)";
                PreparedStatement psKH = con.prepareStatement(insertKH);
                psKH.setString(1, maKH);
                psKH.setString(2, fullname);
                psKH.setString(3, email);
                psKH.executeUpdate();
                
                con.commit(); // Commit nếu thành công
            } else {
                con.rollback(); // rollback nếu không lấy được userId
            }
        } else {
            con.rollback(); // rollback nếu không thêm được user
        }
    } catch (ClassNotFoundException | SQLException e) {
    try {
        if (e instanceof SQLException sqlEx) {
            if (sqlEx.getErrorCode() == 1) {
                // Lỗi do trùng username hoặc email
                i = -2000;
            } else {
                JOptionPane.showMessageDialog(null, "Lỗi SQL: " + sqlEx.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }

        if (con != null) con.rollback();
    } catch (SQLException ex2) {
        ex2.printStackTrace();
    }

    } finally {
        try {
            if (con != null) con.close();
        } catch (SQLException ex) {
            System.out.println("Error Code: " + ex.getErrorCode());
        }
    }
    return i;
}

    public UserResponse dangNhapTK(String username, String passwordHash)
    {
        // int i = 0;
        UserResponse userRes = new UserResponse();
        // TODO add your handling code here:
        try (Connection con = ConnectionUtils.getMyConnection()) {
        

            String query = "SELECT ACCOUNT_ID FROM ACCOUNT WHERE USERNAME = ? AND PASSWORD_HASH = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet resultSet = ps.executeQuery();
            
            if (resultSet.next())
            { 
                int accID = resultSet.getInt("ACCOUNT_ID");
                userRes.setStatus(true);
                userRes.setAccID(accID);
                System.out.println("Status: " + userRes.isStatus());
                System.out.println("AccId: " + userRes.getAccID());
                userRes.setRole(this.getRoleName(accID));
                return userRes;
            }
            else {
               userRes.setStatus(false);
               userRes.setAccID(0);
                return userRes;
            }
//            Statement stat = con.createStatement();
//            i = stat.executeUpdate(query);
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        userRes.setStatus(false);
        userRes.setAccID(0);
        return userRes;
    }

    public String getRoleName (int accID)
    {
        String role = "";
        try (Connection con = ConnectionUtils.getMyConnection()) 
        {
            String query = "SELECT RG.\"NAME_ROLE_GROUP\" "
                            + "FROM \"ACCOUNT_ASSIGN_ROLE_GROUP\" AARG "
                            + "INNER JOIN \"ROLE_GROUP\" RG ON AARG.\"ROLE_GROUP_ID\" = RG.\"ROLE_GROUP_ID\" "
                            + "WHERE AARG.\"ACCOUNT_ID\" = ? "
                            + "AND AARG.\"IS_DELETED\" = 0 "
                            + "AND RG.\"IS_DELETED\" = 0";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, accID);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next())
            {
                role = resultSet.getString("NAME_ROLE_GROUP");
                System.out.println(role);
            }
            else System.out.println("Không tìm thấy: " + accID);
        }
        catch(Exception e)
        {
            System.out.println("Loi o day: " + e.getMessage());
        }
        return role;
    }

 
}
