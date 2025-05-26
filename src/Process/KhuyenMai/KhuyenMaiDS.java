package Process.KhuyenMai;

import ConnectDB.ConnectionUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class KhuyenMaiDS {

    public static ArrayList<KhuyenMaiModel> layDanhSachKhuyenMai() throws ClassNotFoundException, SQLException {
        ArrayList<KhuyenMaiModel> danhSach = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT MAKHUYENMAI, TENKHUYENMAI, GIATRI, NGAYBATDAU, NGAYKETTHUC, DIEUKIENKM, TONGTIENTOITHIEU FROM KHUYEN_MAI "
                        + " WHERE NGAYKETTHUC > SYSDATE";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maKhuyenMai = rs.getString("MAKHUYENMAI");
                String tenKhuyenMai = rs.getString("TENKHUYENMAI");
                double giaTri = rs.getDouble("GIATRI");
                Date ngayBatDau = rs.getDate("NGAYBATDAU");
                Date ngayKetThuc = rs.getDate("NGAYKETTHUC");
                String dieuKienKM = rs.getString("DIEUKIENKM");
                double dieuKienGiaTri = rs.getDouble("TONGTIENTOITHIEU");
                
                // Giả sử điều kiện giá trị là 0, hoặc bạn có thể parse từ dieuKienKM nếu có cấu trúc cụ thể

                KhuyenMaiModel km = new KhuyenMaiModel(maKhuyenMai, tenKhuyenMai, giaTri, ngayBatDau, ngayKetThuc, dieuKienKM, dieuKienGiaTri);
                danhSach.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}
