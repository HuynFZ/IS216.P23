package Process.DichVu;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThemChiTietDichVu {

    public static void themChiTietDichVu(String maHanhKhach, String maDichVu, String ghiChu)
            throws ClassNotFoundException, SQLException {

        Connection con = null;

        try {
            con = ConnectionUtils.getMyConnection();

            String sql = "INSERT INTO CT_DICH_VU (MaHanhKhach, MaDichVu, GhiChu) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, maHanhKhach);
            pstmt.setString(2, maDichVu);

            if (ghiChu != null && !ghiChu.trim().isEmpty()) {
                pstmt.setString(3, ghiChu);
            } else {
                pstmt.setNull(3, java.sql.Types.NVARCHAR);
            }

            int rows = pstmt.executeUpdate();
            pstmt.close();

            if (rows == 0) {
                throw new SQLException("Thêm chi tiết dịch vụ thất bại.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) con.close();
        }
    }
}
