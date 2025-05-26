
package Process.VeMayBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LayGiaVe {
    public static String layGiaVe(String maChuyenBay)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String giaVe = "";

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT cb.GIAVE " +
                         "FROM CHUYEN_BAY cb " +
                         "WHERE cb.MACHUYENBAY = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, maChuyenBay);

            rs = ps.executeQuery();

            if (rs.next()) {
                giaVe = rs.getString("GIAVE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return giaVe;
    }
}
