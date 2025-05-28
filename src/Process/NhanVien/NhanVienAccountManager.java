package Process.NhanVien;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class NhanVienAccountManager {
    
    // Mật khẩu mặc định cho tài khoản nhân viên
    private static final String DEFAULT_PASSWORD = "NhanVien@123";

     /*
     * Tạo một User và Account mới cho một nhân viên dựa trên MaNhanVien.
     * Thông tin CCCD, HoTen, Email sẽ được lấy trực tiếp từ CSDL.
     * Username sẽ là CCCD của nhân viên, password là "NhanVien@123".
     *
     * @param maNhanVien Mã nhân viên (đã tồn tại trong bảng NHAN_VIEN)
     * @return true nếu tạo tài khoản thành công, false nếu thất bại.
     */
    public boolean taoUserVaAccountChoNhanVien(String maNhanVien) {
        Connection con = null;
        PreparedStatement psUser = null;
        PreparedStatement psAccount = null;
        PreparedStatement psUpdateNhanVien = null;
        ResultSet rs = null;

        String cccd = null;
        String hoTen = null;
        String email = null;

        try {
            con = ConnectionUtils.getMyConnection();
            con.setAutoCommit(false); // Bắt đầu giao dịch thủ công

            // 1. Lấy thông tin CCCD, HoTen, Email từ bảng NHAN_VIEN
            String selectNhanVienSql = "SELECT CCCD, HoTen, Email FROM NHAN_VIEN WHERE MaNhanVien = ?";
            try (PreparedStatement psSelectNV = con.prepareStatement(selectNhanVienSql)) {
                psSelectNV.setString(1, maNhanVien);
                try (ResultSet rsNV = psSelectNV.executeQuery()) {
                    if (rsNV.next()) {
                        cccd = rsNV.getString("CCCD");
                        hoTen = rsNV.getString("HoTen");
                        email = rsNV.getString("Email");
                    } else {
                        // Không tìm thấy nhân viên
                        JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Lỗi: Không tìm thấy nhân viên có mã " + maNhanVien + " trong CSDL.");
                        con.rollback();
                        return false;
                    }
                }
            }

            // Kiểm tra các thông tin cần thiết có bị null/empty không
            if (cccd == null || cccd.isEmpty() || hoTen == null || hoTen.isEmpty() || email == null || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Thông tin CCCD, Họ tên, hoặc Email của nhân viên có mã " + maNhanVien + " chưa đầy đủ trong CSDL.");
                con.rollback();
                return false;
            }

            // 2. Kiểm tra xem nhân viên đã có tài khoản chưa để tránh tạo trùng lặp
            if (kiemTraNhanVienDaCoAccount(maNhanVien, con)) {
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Nhân viên có mã " + maNhanVien + " đã có tài khoản.");
                con.rollback();
                return false;
            }

            // 3. Kiểm tra xem CCCD (username) hoặc Email đã tồn tại trong bảng ACCOUNT/USER chưa
            if (kiemTraTonTaiUsernameHoacEmail(cccd, email, con)) {
                // Lỗi cụ thể đã được in ra trong hàm kiemTraTonTaiUsernameHoacEmail
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                con.rollback();
                return false;
            }

            // A. Tạo User mới trong bảng "USER"
            String insertUserSql = "INSERT INTO \"USER\" (FULL_NAME, EMAIL) VALUES(?, ?)";
            psUser = con.prepareStatement(insertUserSql, new String[]{"USER_ID"});
            psUser.setString(1, hoTen);
            psUser.setString(2, email);

            int rowsAffectedUser = psUser.executeUpdate();
            if (rowsAffectedUser == 0) {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Không thêm được bản ghi User mới.");
                return false;
            }

            long userId = -1;
            rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getLong(1);
            } else {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Không lấy được USER_ID được tạo tự động.");
                return false;
            }

            // B. Tạo Account mới trong bảng ACCOUNT
            String insertAccountSql = "INSERT INTO ACCOUNT(USER_ID, USERNAME, PASSWORD_HASH) VALUES(?, ?, ?)";
            psAccount = con.prepareStatement(insertAccountSql, new String[]{"ACCOUNT_ID"});
            psAccount.setLong(1, userId);
            psAccount.setString(2, cccd); // CCCD làm username
            psAccount.setString(3, hashPassword(DEFAULT_PASSWORD)); // Hash mật khẩu mặc định

            int rowsAffectedAccount = psAccount.executeUpdate();
            if (rowsAffectedAccount == 0) {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Không thêm được bản ghi Account mới.");
                return false;
            }

            long accountId = -1;
            rs = psAccount.getGeneratedKeys();
            if (rs.next()) {
                accountId = rs.getLong(1);
            } else {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Không lấy được ACCOUNT_ID được tạo tự động.");
                return false;
            }

            // C. Cập nhật ACCOUNT_ID vào bảng NHAN_VIEN
            String updateNhanVienSql = "UPDATE NHAN_VIEN SET ACCOUNT_ID = ? WHERE MaNhanVien = ?";
            psUpdateNhanVien = con.prepareStatement(updateNhanVienSql);
            psUpdateNhanVien.setLong(1, accountId);
            psUpdateNhanVien.setString(2, maNhanVien);

            int rowsAffectedNhanVien = psUpdateNhanVien.executeUpdate();
            if (rowsAffectedNhanVien == 0) {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lỗi: Không cập nhật được ACCOUNT_ID cho nhân viên có mã " + maNhanVien + ".");
                return false;
            }

            con.commit(); // Commit giao dịch nếu tất cả các bước đều thành công
            JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback(); // Rollback nếu có lỗi SQL
                }
            } catch (SQLException rbEx) {
                System.err.println("Lỗi rollback: " + rbEx.getMessage());
                Logger.getLogger(NhanVienAccountManager.class.getName()).log(Level.SEVERE, null, rbEx);
            }
            JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            // In lỗi cụ thể ra console
            if (ex.getErrorCode() == 1) { // Mã lỗi 1 cho lỗi UNIQUE constraint violation (Oracle)
                System.err.println("Lỗi CSDL (trùng lặp): Email hoặc CCCD (username) đã tồn tại. Chi tiết: " + ex.getMessage());
            } else {
                System.err.println("Lỗi CSDL: " + ex.getMessage());
            }
            Logger.getLogger(NhanVienAccountManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            try {
                if (con != null) {
                    con.rollback(); // Rollback nếu có lỗi khác
                }
            } catch (SQLException rbEx) {
                System.err.println("Lỗi rollback: " + rbEx.getMessage());
                Logger.getLogger(NhanVienAccountManager.class.getName()).log(Level.SEVERE, null, rbEx);
            }
            JOptionPane.showMessageDialog(null, "Tạo tài khoản nhân viên không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.err.println("Lỗi không xác định: " + ex.getMessage());
            Logger.getLogger(NhanVienAccountManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psUser != null) psUser.close();
                if (psAccount != null) psAccount.close();
                if (psUpdateNhanVien != null) psUpdateNhanVien.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                System.err.println("Lỗi đóng tài nguyên CSDL: " + ex.getMessage());
                Logger.getLogger(NhanVienAccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*
     * Hàm kiểm tra xem một nhân viên đã có ACCOUNT_ID trong bảng NHAN_VIEN chưa.
     *
     * @param maNhanVien Mã nhân viên cần kiểm tra.
     * @param con Đối tượng Connection hiện tại.
     * @return true nếu nhân viên đã có ACCOUNT_ID, false nếu ngược lại.
     * @throws SQLException Nếu có lỗi SQL xảy ra.
     */
    private boolean kiemTraNhanVienDaCoAccount(String maNhanVien, Connection con) throws SQLException {
        String sql = "SELECT ACCOUNT_ID FROM NHAN_VIEN WHERE MaNhanVien = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject("ACCOUNT_ID") != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Hàm kiểm tra xem username (CCCD) hoặc email đã tồn tại trong hệ thống chưa.
     *
     * @param cccd Số CCCD (dùng làm username).
     * @param email Email của user.
     * @param con Đối tượng Connection hiện tại.
     * @return true nếu username hoặc email đã tồn tại, false nếu không.
     * @throws SQLException Nếu có lỗi SQL xảy ra.
     */
    private boolean kiemTraTonTaiUsernameHoacEmail(String cccd, String email, Connection con) throws SQLException {
        String checkUsernameSql = "SELECT COUNT(*) FROM ACCOUNT WHERE USERNAME = ?";
        try (PreparedStatement ps = con.prepareStatement(checkUsernameSql)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.err.println("Lỗi trùng lặp: Số CCCD '" + cccd + "' đã được sử dụng làm username cho một tài khoản khác.");
                    return true;
                }
            }
        }

        String checkEmailSql = "SELECT COUNT(*) FROM \"USER\" WHERE EMAIL = ?";
        try (PreparedStatement ps = con.prepareStatement(checkEmailSql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.err.println("Lỗi trùng lặp: Email '" + email + "' đã được sử dụng cho một User khác.");
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Giả định hàm băm mật khẩu. Trong thực tế, nên sử dụng các thư viện bảo mật
     * như BCrypt để băm mật khẩu.
     *
     * @param password Mật khẩu gốc.
     * @return Chuỗi đã băm (ví dụ đơn giản là trả về chính nó).
     */
    private String hashPassword(String password) {
        // Đây chỉ là một ví dụ đơn giản.
        // DÙNG CÁC THƯ VIỆN BĂM MẬT KHẨU MẠNH MẼ NHƯ BCrypt, Argon2.
        // Ví dụ: return BCrypt.hashpw(password, BCrypt.gensalt());
        return password; 
    }
}
