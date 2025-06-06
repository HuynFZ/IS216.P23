
package Process.VeMayBay;

import Model.VeMayBay.VeMayBayModel;
import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DanhSachVe {
    public static ArrayList<VeMayBayModel> layVeTuMaKhachHang(String maKhachHang) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<VeMayBayModel> danhSachVe = new ArrayList<>();

        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT vmb.MAVE, cb.MACHUYENBAY, " +
                         "       COUNT(DISTINCT hk.MAHANHKHACH) AS tonghanhkhach, " +
                         "       tb.SANBAYDI, tb.SANBAYDEN, cb.GIOCATCANH, cb.GIOHACANH, " +
                         "       vmb.NGAYDATVE, vmb.TRANGTHAIVE, tt.THOIGIANTT, tt.TRANGTHAI " +
                         "FROM KHACH_HANG kh " +
                         "INNER JOIN VE_MAY_BAY vmb ON kh.MAKHACHHANG = vmb.MAKHACHHANG " +
                         "INNER JOIN HANH_KHACH hk ON hk.MAVE = vmb.MAVE " +
                         "INNER JOIN THANH_TOAN tt ON tt.MAVE = vmb.MAVE " +
                         "INNER JOIN CHUYEN_BAY cb ON cb.MACHUYENBAY = vmb.MACHUYENBAY " +
                         "INNER JOIN TUYEN_BAY tb ON tb.MATUYENBAY = cb.MATUYENBAY " +
                         "WHERE kh.MAKHACHHANG = ? " +
                         "GROUP BY vmb.MAVE, cb.MACHUYENBAY, tb.SANBAYDI, tb.SANBAYDEN, cb.GIOCATCANH, cb.GIOHACANH, " +
                         "         vmb.NGAYDATVE, vmb.TRANGTHAIVE, tt.THOIGIANTT, tt.TRANGTHAI";

            ps = con.prepareStatement(sql);
            ps.setString(1, maKhachHang);
            rs = ps.executeQuery();

            while (rs.next()) {
                String maVe = rs.getString("MAVE");
                String maChuyenBay = rs.getString("MACHUYENBAY");
                int tongHanhKhach = rs.getInt("tonghanhkhach");
                String sanBayDi = rs.getString("SANBAYDI");
                String sanBayDen = rs.getString("SANBAYDEN");
                Timestamp gioCatCanh = rs.getTimestamp("GIOCATCANH");
                Timestamp gioHaCanh = rs.getTimestamp("GIOHACANH");
                Timestamp ngayDatVe = rs.getTimestamp("NGAYDATVE");
                String trangThaiVe = rs.getString("TRANGTHAIVE");
                Timestamp thoiGianTT = rs.getTimestamp("THOIGIANTT");
                String trangThaiTT = rs.getString("TRANGTHAI");

                VeMayBayModel ve = new VeMayBayModel(
                    maVe, maChuyenBay, tongHanhKhach, sanBayDi, sanBayDen,
                    gioCatCanh, gioHaCanh, ngayDatVe, trangThaiVe,
                    thoiGianTT, trangThaiTT
                );

                danhSachVe.add(ve);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return danhSachVe;
    }
}