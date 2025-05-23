package Process.KhachHang;

import ConnectDB.ConnectionUtils;
import java.sql.*;

public class TruDiemKhachHang {

    public static void truDiem(String maKhachHang, double diemTru) throws ClassNotFoundException, SQLException {
        Connection con = null;
        CallableStatement callStmt = null;

        try {
            con = ConnectionUtils.getMyConnection();

            // Gọi procedure SP_TRU_DIEM_KHACH_HANG
            callStmt = con.prepareCall("{ call SP_TRU_DIEM_KHACH_HANG(?, ?) }");
            callStmt.setString(1, maKhachHang);
            callStmt.setDouble(2, diemTru);
            callStmt.execute();

            // Tùy bạn quyết định COMMIT phía client hay rollback nếu cần
            // con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
