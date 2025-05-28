
package Process.NhanVien;

import ConnectDB.ConnectionUtils;
import Process.KhachHang.KhachHangModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SuaNhanVien {
    public static boolean suaThongTinNV(String maNhanVien, NhanVienModel nv) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "UPDATE NHAN_VIEN SET " +
                        "HOTEN = ?, " +
                        "CCCD = ?, " +
                        "NGAYSINH = ?, " +
                        "GIOITINH = ?, " +
                        "DIACHI = ?, " +
                        "SDT = ?, " +
                        "EMAIL = ? " +
                        "WHERE MANHANVIEN = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getCccd());
            ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(4, nv.getGioiTinh());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getSdt());
            ps.setString(7, nv.getEmail());
            ps.setString(8, maNhanVien);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
