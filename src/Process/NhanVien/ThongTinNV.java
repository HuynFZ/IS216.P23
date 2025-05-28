

package Process.NhanVien;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class ThongTinNV {
    public static NhanVienModel layThongTinNV(String maNhanVien) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        NhanVienModel nv = new NhanVienModel();
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT nv.HOTEN, nv.CCCD, nv.NGAYSINH, nv.GIOITINH, nv.SDT, nv.EMAIL," +
                                "nv.DIACHI, nv.CHUCVU, nv.LUONGCOBAN, nv.PHUCLOI, nv.NGAYVAOLAM " +
                         "FROM NHAN_VIEN nv " +
                         "WHERE nv.MANHANVIEN = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            rs = ps.executeQuery();

            while (rs.next()) {
                String hoTen = rs.getString("HOTEN");
                String cccd = rs.getString("CCCD");
                Date ngaySinh = rs.getDate ("NGAYSINH");
                String gioiTinh = rs.getString("GIOITINH");
                String soDienThoai = rs.getString("SDT");
                String email = rs.getString ("EMAIL");
                String diaChi = rs.getString ("DIACHI");
                String chucVu = rs.getString ("CHUCVU");
                String luongCoBan = rs.getString ("LUONGCOBAN");
                String phucLoi = rs.getString ("PHUCLOI");
                Date ngayVaoLam = rs.getDate("NGAYVAOLAM");

                nv = new NhanVienModel(hoTen, cccd, ngaySinh, gioiTinh, soDienThoai, email, diaChi, chucVu, luongCoBan, phucLoi, ngayVaoLam);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return nv;
    }
}

