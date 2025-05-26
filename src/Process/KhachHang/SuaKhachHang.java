
package Process.KhachHang;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SuaKhachHang {
    public static boolean suaThongTinKH(String maKhachHang, KhachHangModel kh) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "UPDATE KHACH_HANG SET " +
                        "HOTEN = ?, " +
                        "CCCD = ?, " +
                        "NGAYSINH = ?, " +
                        "GIOITINH = ?, " +
                        "QUOCTICH = ?, " +
                        "SDT = ?, " +
                        "EMAIL = ? " +
                        "WHERE MAKHACHHANG = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getCccd());
            ps.setDate(3, new java.sql.Date(kh.getNgaySinh().getTime()));
            ps.setString(4, kh.getGioiTinh());
            ps.setString(5, kh.getQuocTich());
            ps.setString(6, kh.getSDT());
            ps.setString(7, kh.getEmail());
            ps.setString(8, maKhachHang);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
