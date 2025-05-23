package Process.VeMayBay;

import ConnectDB.ConnectionUtils;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ThemVeMayBay {

    public static String themVeMayBay(String maChuyenBay, String maKhachHang, double tongTien,
                                      String loaiVe, String trangThaiVe)
            throws ClassNotFoundException, SQLException {

        Connection con = null;
        String maVe = null;

        try {
            con = ConnectionUtils.getMyConnection();

            // 1. Gọi hàm FN_TAO_MAVE để tạo mã vé
            CallableStatement callStmt = con.prepareCall("{ ? = call FN_TAO_MAVE(?) }");
            callStmt.registerOutParameter(1, Types.VARCHAR);
            callStmt.setString(2, maChuyenBay);
            callStmt.execute();
            maVe = callStmt.getString(1);
            callStmt.close();

            // 2. Thêm vào bảng VE_MAY_BAY
            String sql = "INSERT INTO VE_MAY_BAY (MaVe, MaChuyenBay, MaKhachHang, TongTien, LoaiVe, NgayDatVe, TrangThaiVe) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, maVe);
            pstmt.setString(2, maChuyenBay);
            pstmt.setString(3, maKhachHang);
            pstmt.setDouble(4, tongTien);
            pstmt.setString(5, "Vé xác định");
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, "Đã thanh toán");

            int rows = pstmt.executeUpdate();
            pstmt.close();

            if (rows == 0) {
                throw new SQLException("Thêm vé thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // hoặc return null tùy xử lý phía trên
        }

        return maVe;
    }
}
