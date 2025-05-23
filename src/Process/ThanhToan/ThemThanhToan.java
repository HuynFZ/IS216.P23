package Process.ThanhToan;

import ConnectDB.ConnectionUtils;
import java.sql.*;
import java.time.LocalDateTime;

public class ThemThanhToan {

    public static String themThanhToan(String maVe, String maKhuyenMai, double soTien,
                                       String phuongThuc, String trangThai)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        String maThanhToan = null;

        try {
            con = ConnectionUtils.getMyConnection();

            // Gọi hàm sinh mã thanh toán
            CallableStatement callStmt = con.prepareCall("{ ? = call FN_TAO_MATT() }");
            callStmt.registerOutParameter(1, Types.VARCHAR);
            callStmt.execute();
            maThanhToan = callStmt.getString(1);
            callStmt.close();

            // Thêm bản ghi vào bảng THANH_TOAN
            String sql = "INSERT INTO THANH_TOAN (MaThanhToan, MaVe, MaKhuyenMai, SoTien, PhuongThuc, ThoiGianTT, TrangThai) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, maThanhToan);
            pstmt.setString(2, maVe);
            
            // Nếu mã khuyến mãi null thì setNull
            if (maKhuyenMai != null && !maKhuyenMai.trim().isEmpty()) {
                pstmt.setString(3, maKhuyenMai);
            } else {
                pstmt.setNull(3, Types.VARCHAR);
            }

            pstmt.setDouble(4, soTien);
            pstmt.setString(5, phuongThuc);
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, "Đang xử lý");

            int rows = pstmt.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Thêm thanh toán thất bại.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return maThanhToan;
    }
}
