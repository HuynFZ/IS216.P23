
package View.TrangChu;


import View.DangKyVaDangNhap;
import View.DangNhapNguoiDung;
import java.awt.CardLayout;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class TrangChu extends javax.swing.JFrame {

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        menuTC = new View.TrangChu.MenuTrangChu();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(menuTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1277, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuTC, javax.swing.GroupLayout.DEFAULT_SIZE, 1139, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private final String accId;
    public TrangChu(String accId) {
        initComponents();
        this.accId = accId;
       menuTC.addEventMenu((int index) -> {
           if (index == 0)
           {
               setForm(new TrangChuForm());
           } else if (index == 1)
           {
               try {
                   setForm(new DatVeForm(mainPanel, accId));
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
               } catch (SQLException ex) {
                   Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
               }
           } else if (index == 2)
           {
               try {
                   setForm(new VeCuaToiForm(mainPanel, accId));
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
               } catch (SQLException ex) {
                   Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           else if (index == 5)
           {
               try {
                   setForm(new TaiKhoanForm(mainPanel, accId));
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
               } catch (SQLException ex) {
                   Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
               }

           } else if (index == 6)
           {
                int choice = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Đóng form TrangChu
                    dispose();
                    
                    DangNhapNguoiDung DangNhapFrame = new DangNhapNguoiDung();
                    DangNhapFrame.setVisible(true);
                    DangNhapFrame.pack();
                    DangNhapFrame.setLocationRelativeTo(null);
                }
           } else
           {
               
           }
        });
       setForm(new TrangChuForm());
               
    }

    private void setForm (JComponent com)
    {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> {
//            new TrangChu(accId).setVisible(true);
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private View.TrangChu.MenuTrangChu menuTC;
    // End of variables declaration//GEN-END:variables
}
