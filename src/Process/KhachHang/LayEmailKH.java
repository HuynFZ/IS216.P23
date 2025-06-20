
package Process.KhachHang;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayEmailKH {
    public static String layEmailKHTuAccount(String accId)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT KH.EMAIL " +
                         "FROM KHACH_HANG KH " +
                         "JOIN ACCOUNT A ON A.MAKHACHHANG = KH.MAKHACHHANG " +
                         "WHERE A.ACCOUNT_ID = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, accId);

            rs = ps.executeQuery();

            if (rs.next()) {
                email = rs.getString("EMAIL");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return email;
    }
}
