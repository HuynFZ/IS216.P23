
package Process.MayBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class SoDoGhe {
   public static Map<String,String> layDanhSachGhe(String maChuyenBay) throws ClassNotFoundException, SQLException {
        Map<String,String> danhSach = new HashMap<>();
        Connection con = null;
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT ViTriGhe, TrangThaiGhe " +
             "FROM CHUYEN_BAY_GHE " +
             "WHERE MaChuyenBay = ? " +
             "ORDER BY " +
             "    TO_NUMBER(REGEXP_SUBSTR(ViTriGhe, '^\\\\d+')), " +   // escape dấu \ trong Java
             "    SUBSTR(ViTriGhe, -1)";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maChuyenBay);
            ResultSet rs = stmt.executeQuery();
            
            System.out.println(sql);
            System.out.println("== DANH SÁCH GHẾ CỦA MÁY BAY: " + maChuyenBay + " ==");
            while (rs.next()) {
                String viTriGhe = rs.getString("VITRIGHE");
                String trangThai = rs.getString("TRANGTHAIGHE");
                danhSach.put(viTriGhe, trangThai);
                System.out.println("Ghế " + viTriGhe + ": " + trangThai);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return danhSach;
    } 
}
