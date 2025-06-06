package Process.KhachHang;

import Model.KhachHang.KhachHangModel;
import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ThongTinKH {
    public static KhachHangModel layThongTinKH(String maKhachHang) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        KhachHangModel ve = new KhachHangModel();
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT kh.HOTEN, kh.CCCD, kh.NGAYSINH, kh.GIOITINH, kh.QUOCTICH, kh.SDT, " +
                             "kh.EMAIL, kh.LOAIKHACHHANG, kh.DIEMTHUONG, kh.HANGTHANHVIEN, kh.THOIHANTV, " +
                             "kh.THOIGIANCAPNHATDT " +
                         "FROM KHACH_HANG kh " + 
                         "WHERE kh.MAKHACHHANG = ? ";

            ps = con.prepareStatement(sql);
            ps.setString(1, maKhachHang);
            rs = ps.executeQuery();

            while (rs.next()) {
                String hoTen = rs.getString("HOTEN");
                String cccd = rs.getString("CCCD");
                Date ngaySinh = rs.getDate ("NGAYSINH");
                String gioiTinh = rs.getString("GIOITINH");
                String quocTich = rs.getString("QUOCTICH");
                String soDienThoai = rs.getString("SDT");
                String email = rs.getString ("EMAIL");
                String loaiKH = rs.getString("LOAIKHACHHANG");
                String diemThuong = rs.getString("DIEMTHUONG");
                String hangTV = rs.getString("HANGTHANHVIEN");
                String thoiHanTV = rs.getString("THOIHANTV");
                String thoiGianCN = rs.getString("THOIGIANCAPNHATDT");

                ve = new KhachHangModel(
                    hoTen, cccd, ngaySinh, gioiTinh, quocTich, soDienThoai, email, loaiKH, diemThuong, hangTV, thoiHanTV, thoiGianCN
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return ve;
    }
}
