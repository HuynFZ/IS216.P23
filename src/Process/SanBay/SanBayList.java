
package Process.SanBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SanBayList {
    public static Map<String,String> layDanhSachSanBay() throws ClassNotFoundException, SQLException {
        Map<String,String> danhSach = new HashMap<>();
        Connection con = null;
        try {
            con = ConnectionUtils.getMyConnection();
            String sql = "SELECT MASANBAY, TENSANBAY || ' - ' || TINHTHANHPHO AS GOIY "
                         + " FROM SAN_BAY";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maSanBay = rs.getString("MASANBAY");
                String goiY = rs.getString("GOIY");
                danhSach.put(maSanBay,goiY);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }
}
