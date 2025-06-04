
package View.TrangChu;

import ConnectDB.ConnectionUtils;
import Process.SanBay.SanBayList;
import View.DatVe.NhapThongTinVeKhungGio;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class DatVeKhungGioForm extends javax.swing.JPanel {

    private JPopupMenu goiYSanBay = new JPopupMenu();
    private JPopupMenu goiYSanBay1 = new JPopupMenu();
    private final JPanel mainPanel;
    private final String accId;
    private final Map<String,String> danhSachSanBay;
            
    public DatVeKhungGioForm(JPanel mainPanel, String accId) throws ClassNotFoundException, SQLException {
        this.mainPanel = mainPanel;
        this.accId = accId;
        this.danhSachSanBay = SanBayList.layDanhSachSanBay();
        initComponents();
        // ẩn đi lịch khứ hồi 
        addAutoSuggestToTextFieldDiemDi();
        addAutoSuggestToTextFieldDiemDen();
        
        xacNhanDatVe.addActionListener(evt -> {

                int nguoiLon = (int) soNguoiLon.getValue();
                int treEm = (int) soTreEm.getValue();
                int emBe = (int) soEmBe.getValue();

                String diemDiTen = DiemDiText.getText();
                String diemDenTen = diemDenText.getText();
                
                String diemDi = null;
                try {
                    diemDi = SanBayList.timMaSanBayTuGoiY(diemDiTen);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DatVeKhungGioForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DatVeKhungGioForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                String diemDen = null;
                try {
                    diemDen = SanBayList.timMaSanBayTuGoiY(diemDenTen);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DatVeKhungGioForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DatVeKhungGioForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Date ngayDi = JDNgayDi.getDate();

                NhapThongTinVeKhungGio nhapThongTinVe = new NhapThongTinVeKhungGio(diemDi,diemDen, ngayDi, nguoiLon, treEm, emBe, mainPanel, accId);
                setForm (nhapThongTinVe);
        });
        
        btnVeXacDinh.addActionListener(evt -> {
            try {
                setForm(new DatVeForm(mainPanel, accId));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatVeKhungGioForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DatVeKhungGioForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        MotChieu = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        DiemDiText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        diemDenText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        JDNgayDi = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        soNguoiLon = new javax.swing.JSpinner();
        soTreEm = new javax.swing.JSpinner();
        soEmBe = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        xacNhanDatVe = new javax.swing.JButton();
        noiDungVe = new javax.swing.JLabel();
        btnVeXacDinh = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Mở cánh cửa khám phá cùng Mel Airlines");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mel Airlines - Đặt một bước nhỏ, vươn tới đỉnh mây");

        buttonGroup1.add(MotChieu);
        MotChieu.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        MotChieu.setSelected(true);
        MotChieu.setText("Một chiều");
        MotChieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MotChieuActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel5.setText("Điểm đi");

        DiemDiText.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        DiemDiText.setActionCommand("null");
        DiemDiText.setName("abf"); // NOI18N
        DiemDiText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiemDiTextActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel6.setText("Điểm đến");

        diemDenText.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        diemDenText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diemDenTextActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel7.setText("Ngày đi");

        JDNgayDi.setAlignmentX(3.0F);
        JDNgayDi.setAlignmentY(10.0F);
        JDNgayDi.setDateFormatString("dd/MM/yyyy");
        JDNgayDi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel9.setText("Người lớn");

        jLabel10.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel10.setText("Trẻ em");

        jLabel11.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel11.setText("Em bé");

        jLabel12.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.png"))); // NOI18N
        jLabel12.setText("Tìm vé máy bay");

        soNguoiLon.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        soNguoiLon.setModel(new javax.swing.SpinnerNumberModel(1, 0, 5, 1));
        soNguoiLon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        soTreEm.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        soTreEm.setModel(new javax.swing.SpinnerNumberModel(0, 0, 3, 1));
        soTreEm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        soEmBe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        soEmBe.setModel(new javax.swing.SpinnerNumberModel(0, 0, 2, 1));
        soEmBe.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        xacNhanDatVe.setBackground(new java.awt.Color(248, 252, 37));
        xacNhanDatVe.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        xacNhanDatVe.setText("Đặt vé");
        xacNhanDatVe.setPreferredSize(new java.awt.Dimension(116, 37));
        xacNhanDatVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xacNhanDatVeActionPerformed(evt);
            }
        });

        noiDungVe.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N

        btnVeXacDinh.setBackground(new java.awt.Color(255, 153, 255));
        btnVeXacDinh.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        btnVeXacDinh.setText("Vé xác định");
        btnVeXacDinh.setPreferredSize(new java.awt.Dimension(116, 37));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnVeXacDinh, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noiDungVe, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xacNhanDatVe, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVeXacDinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(noiDungVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xacNhanDatVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(MotChieu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JDNgayDi, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(soNguoiLon, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(191, 191, 191)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(soTreEm, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(216, 216, 216)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(soEmBe, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(54, 54, 54))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(DiemDiText, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(103, 103, 103)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(diemDenText, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(14, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(30, 30, 30)
                .addComponent(MotChieu)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DiemDiText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diemDenText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(jLabel7)
                .addGap(10, 10, 10)
                .addComponent(JDNgayDi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(soNguoiLon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soTreEm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soEmBe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(213, 213, 213)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void MotChieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MotChieuActionPerformed

    }//GEN-LAST:event_MotChieuActionPerformed

    private void DiemDiTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiemDiTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiemDiTextActionPerformed

    private void diemDenTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diemDenTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_diemDenTextActionPerformed

    private void xacNhanDatVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xacNhanDatVeActionPerformed
        
    }//GEN-LAST:event_xacNhanDatVeActionPerformed
    
    private void setForm (JComponent com)
    {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    




    // Thêm gợi ý sân bay
    private void addAutoSuggestToTextFieldDiemDi()
    {
       DiemDiText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
       {
        private void capNhatGoiY() {
            goiYSanBay.setVisible(false);
            goiYSanBay.removeAll();
            String input = DiemDiText.getText().toLowerCase().trim();
        
            if (!input.isEmpty()) {
                boolean found = false;
                for (Map.Entry<String,String> entry : danhSachSanBay.entrySet()) {
                    String maSanBay = entry.getKey();
                    String goiY = entry.getValue();
                    // Kiểm tra nếu tên sân bay chứa input của người dùng
                    if (goiY.toLowerCase().contains(input)) {
                        JMenuItem menuItem = new JMenuItem(goiY);
                        menuItem.addActionListener(e -> {
                            DiemDiText.setText(goiY);
                            DiemDiText.setToolTipText(maSanBay);
                            SwingUtilities.invokeLater(() -> goiYSanBay.setVisible(false));
                        });
                        goiYSanBay.add(menuItem);
                        found = true;
                    }
                }
        
                if (found) {
                    SwingUtilities.invokeLater(() -> {
                        goiYSanBay.revalidate();
                        goiYSanBay.repaint();
                        goiYSanBay.show(DiemDiText, 0, DiemDiText.getHeight());  // Hiển thị menu khi có gợi ý
                        DiemDiText.requestFocusInWindow();
                    });
                } else {
                    goiYSanBay.setVisible(false);  // Ẩn menu nếu không có gợi ý nào
                }
            } else {
                goiYSanBay.setVisible(false);  // Ẩn menu nếu ô nhập rỗng
            }
        }
        
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        });  
    }
    
    private void addAutoSuggestToTextFieldDiemDen()
    {
       diemDenText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
       {
        private void capNhatGoiY() {
            goiYSanBay1.setVisible(false);
            goiYSanBay1.removeAll();
            String input = diemDenText.getText().toLowerCase().trim();
        
            if (!input.isEmpty()) {
                boolean found = false;
                for (Map.Entry<String,String> entry : danhSachSanBay.entrySet()) {
                    String maSanBay = entry.getKey();
                    String goiY = entry.getValue();
                    // Kiểm tra nếu tên sân bay chứa input của người dùng
                    if (goiY.toLowerCase().contains(input)) {
                        JMenuItem menuItem = new JMenuItem(goiY);
                        menuItem.addActionListener(e -> {
                            diemDenText.setText(goiY);
                            diemDenText.setToolTipText(maSanBay);
                            SwingUtilities.invokeLater(() -> goiYSanBay1.setVisible(false));
                        });
                        goiYSanBay1.add(menuItem);
                        found = true;
                    }
                }
        
                if (found) {
                    SwingUtilities.invokeLater(() -> {
                        goiYSanBay1.revalidate();
                        goiYSanBay1.repaint();
                        goiYSanBay1.show(diemDenText, 0, diemDenText.getHeight());  // Hiển thị menu khi có gợi ý
                        diemDenText.requestFocusInWindow();
                    });
                } else {
                    goiYSanBay1.setVisible(false);  // Ẩn menu nếu không có gợi ý nào
                }
            } else {
                goiYSanBay1.setVisible(false);  // Ẩn menu nếu ô nhập rỗng
            }
        }
        
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DiemDiText;
    private com.toedter.calendar.JDateChooser JDNgayDi;
    private javax.swing.JRadioButton MotChieu;
    private javax.swing.JButton btnVeXacDinh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField diemDenText;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel noiDungVe;
    private javax.swing.JSpinner soEmBe;
    private javax.swing.JSpinner soNguoiLon;
    private javax.swing.JSpinner soTreEm;
    private javax.swing.JButton xacNhanDatVe;
    // End of variables declaration//GEN-END:variables
}
