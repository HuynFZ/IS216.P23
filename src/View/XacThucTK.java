
package View;

import Process.Email.EmailService;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class XacThucTK extends javax.swing.JFrame {

    private String email,fullname, generateOTPCheck;
    private DangKyNguoiDung parent;

    public XacThucTK(String email, String fullname, DangKyNguoiDung parent) {
        initComponents();
        this.email = email;
        this.fullname = fullname;
        this.parent = parent;
        txtEmail.setText(email);
        guiMaXacThuc();

        guilai.addActionListener(e -> guiMaXacThuc());
        
        xacnhan.addActionListener(e -> {
            try {
                kiemTraMaXacThuc();
            } catch (SQLException ex) {
                Logger.getLogger(XacThucTK.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
       
        
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
    
    private void kiemTraMaXacThuc() throws SQLException
    {
        String otp = txtOTP.getText();
        
        if (otp.equals(this.generateOTPCheck)) {
            JOptionPane.showMessageDialog(this, "Xác thực thành công!");
            parent.onOTPVerified(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Mã xác thực không đúng. Vui lòng kiểm tra lại.");
        }
    }

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtOTP = new javax.swing.JTextField();
        txtEmail = new javax.swing.JLabel();
        huy = new javax.swing.JButton();
        xacnhan = new javax.swing.JButton();
        guilai = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nhập mã xác thực được gửi qua email");

        txtEmail.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        txtEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtEmail.setText("Email");

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

        guilai.setText("Gửi lại mã");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(huy, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(xacnhan, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(guilai)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtOTP, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail)
                .addGap(18, 18, 18)
                .addComponent(txtOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guilai, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
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
        try {
            parent.onOTPVerified(false);
        } catch (SQLException ex) {
            Logger.getLogger(XacThucTK.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    private javax.swing.JButton guilai;
    private javax.swing.JButton huy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtEmail;
    private javax.swing.JTextField txtOTP;
    private javax.swing.JButton xacnhan;
    // End of variables declaration//GEN-END:variables
}
