
package Process.HanhKhach;

import Model.HanhKhach.UserHanhKhach;
import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DanhSachHK {
    public static ArrayList<UserHanhKhach> layDanhSachHK(String maVe) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT hk.MAHANHKHACH, hk.GIOITINH, hk.HOTEN, hk.NGAYSINH, hk.QUOCTICH, hk.CCCD," +
                         " hk.SDT, hk.EMAIL, hk.PHIBOSUNG, hk.VITRIGHE" +
                         " FROM HANH_KHACH hk WHERE hk.MAVE = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, maVe);
            rs = ps.executeQuery();

            while (rs.next()) {
                String maHanhKhach = rs.getString("MAHANHKHACH");
                String gioiTinh = rs.getString("GIOITINH");
                String hoTen = rs.getString("HOTEN");
                Timestamp ngaySinh = rs.getTimestamp("NGAYSINH");
                String quocTich = rs.getString("QUOCTICH");
                String cccd = rs.getString("CCCD");
                String sdt = rs.getString("SDT");
                String email = rs.getString("EMAIL");
                double phiBoSung = rs.getDouble("PHIBOSUNG");
                String viTriGhe = rs.getString("VITRIGHE");

                UserHanhKhach hk = new UserHanhKhach(maHanhKhach, gioiTinh, hoTen, ngaySinh, quocTich, cccd, sdt, email, phiBoSung, viTriGhe);
                danhSachHanhKhach.add(hk);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return danhSachHanhKhach;
    }
}
