package Process.KhuyenMai;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class LayGiaTriKM {
    public static double layGiaTriKM(String maVe)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double giaTri = 0;

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT km.GIATRI " +
                         "FROM THANH_TOAN tt " +
                         "INNER JOIN KHUYEN_MAI km ON tt.MAKHUYENMAI = km.MAKHUYENMAI " +
                         " WHERE tt.MAVE = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, maVe);

            rs = ps.executeQuery();

            if (rs.next()) {
                giaTri = rs.getDouble("GIATRI");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return giaTri;
    }
}
