package Process.KhachHang;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayMaKH {

    public static String layMaKHTuAccount(String accId)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String maKhachHang = "";

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT KH.MAKHACHHANG " +
                         "FROM KHACH_HANG KH " +
                         "JOIN ACCOUNT A ON A.MAKHACHHANG = KH.MAKHACHHANG " +
                         "WHERE A.ACCOUNT_ID = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, accId);

            rs = ps.executeQuery();

            if (rs.next()) {
                maKhachHang = rs.getString("MAKHACHHANG");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return maKhachHang;
    }
}
