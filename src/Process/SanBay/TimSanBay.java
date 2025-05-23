
package Process.SanBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TimSanBay {
    public static String timTenSanBay(String maSanBay) throws ClassNotFoundException
    {
        Connection con = null;
        String tenSanBay = null;
        try
        {
            con = ConnectionUtils.getMyConnection();
            String query = "SELECT TENSANBAY FROM SAN_BAY WHERE MASANBAY = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, maSanBay);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                tenSanBay = rs.getString("TENSANBAY");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return tenSanBay;
    }
}
