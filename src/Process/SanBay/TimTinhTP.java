
package Process.SanBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TimTinhTP {
    public static String timTinhTP(String maSanBay) throws ClassNotFoundException
    {
        Connection con = null;
        String tenTinhTP = null;
        try
        {
            con = ConnectionUtils.getMyConnection();
            String query = "SELECT TINHTHANHPHO FROM SAN_BAY WHERE MASANBAY = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, maSanBay);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                tenTinhTP = rs.getString("TINHTHANHPHO");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return tenTinhTP;
    }
}
