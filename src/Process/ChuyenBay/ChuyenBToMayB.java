
package Process.ChuyenBay;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChuyenBToMayB {
    public static String timMaMayBay(String maChuyenBay) throws ClassNotFoundException
    {
        Connection con = null;
        String maMayBay = null;
        try
        {
            con = ConnectionUtils.getMyConnection();
            String query = "SELECT MAMAYBAY FROM CHUYEN_BAY WHERE MACHUYENBAY = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, maChuyenBay);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                maMayBay = rs.getString("MAMAYBAY");
                System.out.println(maMayBay);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e);
        }
        return maMayBay;
    }
}
