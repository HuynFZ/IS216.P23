
package Process.HanhLy;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HanhlyDS {
    public static ArrayList<HanhLyModel> layDanhSachGoiHanhLy() throws ClassNotFoundException, SQLException {
        ArrayList<HanhLyModel> danhSach = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT MAGOIHANHLY, TENGOIHANHLY, TRONGLUONGMAX, GIATIEN FROM GOI_HANH_LY";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maGoiHanhLy = rs.getString("MAGOIHANHLY");
                String tenGoiHanhLy = rs.getString("TENGOIHANHLY");
                double trongLuongMax = rs.getDouble("TRONGLUONGMAX");
                double giaTien = rs.getDouble("GIATIEN");

                HanhLyModel hl = new HanhLyModel(maGoiHanhLy, tenGoiHanhLy, trongLuongMax, giaTien);
                danhSach.add(hl);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    public static String layTenGoiHanhLy(String maGoiHL) throws ClassNotFoundException
    {
        String tenGoiHL = "";
        Connection con = null;
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT TENGOIHANHLY FROM GOI_HANH_LY WHERE MAGOIHANHLY = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maGoiHL);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                tenGoiHL = rs.getString("TENGOIHANHLY");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return tenGoiHL;
    }

}
