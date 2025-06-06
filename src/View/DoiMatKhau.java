
package View;

import Process.Email.EmailService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DoiMatKhau extends javax.swing.JFrame {

    private String email,fullname, generateOTPCheck;

    public DoiMatKhau() throws SQLException{
        initComponents();
//        guiMaXacThuc();
//
//        guilai.addActionListener(e -> guiMaXacThuc());
        
        xacnhan.addActionListener(e -> {
            String username = txtTDN.getText();
            String newPassword = new String(matKhauM.getPassword());  // nếu dùng JPasswordField
            String newPassword2 = new String(xacThucMK.getPassword());

            if (!newPassword.equals(newPassword2)) {
                    JOptionPane.showMessageDialog(null,
                        "Mật khẩu không khớp! Vui lòng nhập lại.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    matKhauM.setText("");
                    xacThucMK.setText("");
                    matKhauM.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Mật khẩu hợp lệ!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

            boolean result = false;
                try {
                    result = updatePassword(username, newPassword);
                } catch (SQLException ex) {
                    Logger.getLogger(DoiMatKhau.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DoiMatKhau.class.getName()).log(Level.SEVERE, null, ex);
                }
            if (result) {
                JOptionPane.showMessageDialog(null, "Cập nhật mật khẩu thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật mật khẩu thất bại, kiểm tra lại username hoặc lỗi kết nối.");
            }
            }
        });
       
        
    }
    public boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        conn = ConnectionUtils.getMyConnection();
        String sql = "UPDATE ACCOUNT SET PASSWORD_HASH = ?, UPDATED_AT = SYSDATE WHERE USERNAME = ? AND IS_DELETED = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPassword = HashPassword(newPassword);
            if (hashedPassword == null) {
                return false;
            }
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String HashPassword (String password)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes){
                hexString.append(String.format("%02x", b));
            }
            System.out.println("SHA-256 Hash: " + hexString.toString());
            String passwordHash = hexString.toString();
            return passwordHash;
        } catch (NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private void guiMaXacThuc()
    {
       this.generateOTPCheck = generateOTP(6);
       boolean success = EmailService.sendOTPVerificationCode(this.email, this.fullname, generateOTPCheck);

        if (success) {
        JOptionPane.showMessageDialog(this, "Mã xác thực đã được gửi đến email.");
            // Hiện dialog nhập OTP, kiểm tra khớp
        } else {
            JOptionPane.showMessageDialog(this, "Không thể gửi email xác thực.");
        }
    }
    
//    private void kiemTraMaXacThuc() throws SQLException
//    {
//        String otp = txtOTP.getText();
//        
//        if (otp.equals(this.generateOTPCheck)) {
//            JOptionPane.showMessageDialog(this, "Xác thực thành công!");
//            parent.onOTPVerified(true);
//            dispose();
//        } else {
//            JOptionPane.showMessageDialog(this, "Mã xác thực không đúng. Vui lòng kiểm tra lại.");
//        }
//    }

    public static String generateOTP(int length) {
        String chars = "0123456789";
        StringBuilder otp = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            otp.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return otp.toString();
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JLabel();
        huy = new javax.swing.JButton();
        xacnhan = new javax.swing.JButton();
        txtTDN = new javax.swing.JTextField();
        txtEmail1 = new javax.swing.JLabel();
        txtEmail2 = new javax.swing.JLabel();
        matKhauM = new javax.swing.JPasswordField();
        xacThucMK = new javax.swing.JPasswordField();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đổi mật khẩu tài khoản");

        txtEmail.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        txtEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtEmail.setText("Tên đăng nhập");

        huy.setBackground(new java.awt.Color(255, 255, 51));
        huy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        huy.setText("Hủy");
        huy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                huyActionPerformed(evt);
            }
        });

        xacnhan.setBackground(new java.awt.Color(102, 255, 102));
        xacnhan.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        xacnhan.setText("Xác nhận");

        txtEmail1.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        txtEmail1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtEmail1.setText("Xác thực mật khẩu");

        txtEmail2.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        txtEmail2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtEmail2.setText("Mật khẩu mới");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(huy, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addComponent(xacnhan, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail1)
                            .addComponent(txtEmail2)
                            .addComponent(txtEmail))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTDN, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                            .addComponent(matKhauM)
                            .addComponent(xacThucMK))))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail)
                    .addComponent(txtTDN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail2)
                    .addComponent(matKhauM, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail1)
                    .addComponent(xacThucMK, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(huy, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xacnhan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void huyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_huyActionPerformed

        dispose();
    }//GEN-LAST:event_huyActionPerformed

//    public static void main(String args[]) {
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new XacThucTK().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton huy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField matKhauM;
    private javax.swing.JLabel txtEmail;
    private javax.swing.JLabel txtEmail1;
    private javax.swing.JLabel txtEmail2;
    private javax.swing.JTextField txtTDN;
    private javax.swing.JPasswordField xacThucMK;
    private javax.swing.JButton xacnhan;
    // End of variables declaration//GEN-END:variables
}
