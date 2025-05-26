
package View.TrangChu;

import View.DatVe.ChonChoNgoi;
import java.sql.SQLException;

public class MainTrangChu {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        TrangChu tc = new TrangChu("1");
        tc.setVisible(true);
        tc.pack();
        tc.setLocationRelativeTo(null); 

        /* ChonChoNgoi choNgoi = new ChonChoNgoi("CB002");
        choNgoi.setVisible(true);
        choNgoi.pack();
        choNgoi.setLocationRelativeTo(null);*/
    }
}
