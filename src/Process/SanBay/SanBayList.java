
package Process.SanBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SanBayList {
    public static ArrayList<String> layDanhSachSanBay() throws ClassNotFoundException, SQLException {
        ArrayList<String> danhSach = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT TENSANBAY || ' - ' || TINHTHANHPHO AS GOIY "
                         + " FROM SAN_BAY";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSach.add(rs.getString("GOIY"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }
}
