
package Process.NhanVien;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayMaNV {
    public static String layMaNVTuAccount(String accId)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String maNhanVien ="";

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT NV.MANHANVIEN " +
                        "FROM NHAN_VIEN nv " +
                        "WHERE NV.ACCOUNT_ID = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, accId);

            rs = ps.executeQuery();

            if (rs.next()) {
                maNhanVien = rs.getString("MANHANVIEN");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return maNhanVien;
    }
}
