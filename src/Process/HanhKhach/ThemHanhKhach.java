
package Process.HanhKhach;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

public class ThemHanhKhach {

    public static boolean themHanhKhach(String hoTen, String cccd, Date ngaySinh, String gioiTinh,
                                        String quocTich, String sdt, String email, String maVe,
                                        String hangGhe, double giaTien, double phiBoSung, String viTriGhe)
            throws ClassNotFoundException, SQLException {
        
        Connection con = null;
        boolean success = false;

        try {
            con = ConnectionUtils.getMyConnection();

            // 1. Gọi hàm tạo mã hành khách
            String maHK = null;
            CallableStatement cstmt = con.prepareCall("{ ? = call FN_TAO_MAHK }");
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.execute();
            maHK = cstmt.getString(1);
            cstmt.close();

            // 2. Insert vào bảng HANH_KHACH
            String sql = "INSERT INTO HANH_KHACH (MaHanhKhach, HoTen, CCCD, NgaySinh, GioiTinh, QuocTich, SDT, Email, MaVe, HangGhe, GiaTien, PhiBoSung, ViTriGhe) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, maHK);
            pstmt.setString(2, hoTen);
            pstmt.setString(3, cccd);
            pstmt.setDate(4, ngaySinh);
            pstmt.setString(5, gioiTinh);
            pstmt.setString(6, quocTich);
            pstmt.setString(7, sdt);
            pstmt.setString(8, email);
            pstmt.setString(9, maVe);
            pstmt.setString(10, "Economy");
            pstmt.setDouble(11, giaTien);
            pstmt.setDouble(12, phiBoSung);
            pstmt.setString(13, viTriGhe);

            int rowsInserted = pstmt.executeUpdate();
            pstmt.close();

            success = rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }
}