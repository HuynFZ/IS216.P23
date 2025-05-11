
package View.Admin;


public class QLNhanVienForm extends javax.swing.JPanel {


    public QLNhanVienForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblCCCD = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtHoTen = new javax.swing.JTextField();
        txtChucVu = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        lblGioiTinh = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        lblChucVu = new javax.swing.JLabel();
        lblLuong = new javax.swing.JLabel();
        lblPhucLoi = new javax.swing.JLabel();
        txtMa1 = new javax.swing.JTextField();
        dcNgaySinh = new com.toedter.calendar.JDateChooser();
        cmbGioiTinh = new javax.swing.JComboBox<>();
        txtSDT = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtCCCD1 = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtPhucLoi = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        lblPhucLoi1 = new javax.swing.JLabel();
        cmbGioiTinh1 = new javax.swing.JComboBox<>();
        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        spDanhSach = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thêm nhân viên");

        lblMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMa.setText("Mã nhân viên");

        lblHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblHoTen.setText("Họ tên");

        lblCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblCCCD.setText("Số  CCCD");

        lblNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgaySinh.setText("Ngày sinh");

        btnSua.setBackground(new java.awt.Color(0, 102, 153));
        btnSua.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(0, 102, 153));
        btnXoa.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/trash.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        txtHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHoTen.setText("MaNhanVien");
        txtHoTen.setAutoscrolls(false);

        txtChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtChucVu.setText("ChucVu");

        btnThem.setBackground(new java.awt.Color(0, 102, 153));
        btnThem.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/plus.png"))); // NOI18N
        btnThem.setText(" Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        lblGioiTinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblGioiTinh.setText("Giới tính");

        lblSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblSDT.setText("Số điện thoại");

        lblEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblEmail.setText("Email");

        lblDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblDiaChi.setText("Địa chỉ");

        lblChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblChucVu.setText("Chức vụ");

        lblLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLuong.setText("Lương cơ bản");

        lblPhucLoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblPhucLoi.setText("Phúc lợi");

        txtMa1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMa1.setText("MaNhanVien");
        txtMa1.setAutoscrolls(false);

        cmbGioiTinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtSDT.setText("0123123123");
        txtSDT.setAutoscrolls(false);

        txtEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtEmail.setText("abc@gmail.com");
        txtEmail.setAutoscrolls(false);

        txtCCCD1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtCCCD1.setText("CCCD");

        txtDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtDiaChi.setText("DiaChi");

        txtPhucLoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtPhucLoi.setText("PhucLoi");
        txtPhucLoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhucLoiActionPerformed(evt);
            }
        });

        txtLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLuong.setText("LuongCoban");

        lblPhucLoi1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblPhucLoi1.setText("Ngày vào làm");

        cmbGioiTinh1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbGioiTinh1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblThaoTac)
                .addGap(177, 177, 177))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDiaChi)
                    .addComponent(lblChucVu)
                    .addComponent(lblLuong)
                    .addComponent(lblPhucLoi)
                    .addComponent(lblNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem)
                    .addComponent(lblPhucLoi1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtChucVu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMa1))
                        .addGap(45, 45, 45))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCCCD1)
                            .addComponent(txtPhucLoi)
                            .addComponent(txtLuong)
                            .addComponent(txtSDT)
                            .addComponent(cmbGioiTinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDiaChi)
                            .addComponent(txtEmail)
                            .addComponent(cmbGioiTinh1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addComponent(btnXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSua)
                        .addGap(56, 56, 56))))
        );

        pnlThaoTacLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbGioiTinh, dcNgaySinh, txtChucVu});

        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblThaoTac)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMa)
                            .addComponent(txtMa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHoTen)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCCCD)
                            .addComponent(txtCCCD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNgaySinh)
                            .addComponent(dcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGioiTinh)
                            .addComponent(cmbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSDT)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmail)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDiaChi)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChucVu)
                            .addComponent(txtChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLuong)
                            .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPhucLoi)
                            .addComponent(txtPhucLoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPhucLoi1)
                            .addComponent(cmbGioiTinh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 114, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSua)
                            .addComponent(btnXoa)
                            .addComponent(btnThem))
                        .addGap(45, 45, 45))))
        );

        pnlThaoTacLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbGioiTinh, dcNgaySinh, txtChucVu});

        pnlDanhSach.setBackground(java.awt.SystemColor.control);
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(632, 824));

        lblDanhSach.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblDanhSach.setText("Danh sách nhân viên");

        txtTimKiem.setFont(new java.awt.Font("UTM Centur", 0, 20)); // NOI18N
        txtTimKiem.setText("Thanh tìm kiếm");
        txtTimKiem.setAutoscrolls(false);
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/search.png"))); // NOI18N

        tblDanhSach.setAutoCreateRowSorter(true);
        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        spDanhSach.setViewportView(tblDanhSach);

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(lblDanhSach))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(spDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnTimKiem)
                .addGap(56, 56, 56))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblDanhSach)
                .addGap(59, 59, 59)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTimKiem)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(spDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDanhSachLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnTimKiem, txtTimKiem});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(pnlDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlThaoTac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtPhucLoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhucLoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhucLoiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbGioiTinh;
    private javax.swing.JComboBox<String> cmbGioiTinh1;
    private com.toedter.calendar.JDateChooser dcNgaySinh;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblLuong;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblPhucLoi;
    private javax.swing.JLabel lblPhucLoi1;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JScrollPane spDanhSach;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtCCCD1;
    private javax.swing.JTextField txtChucVu;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMa1;
    private javax.swing.JTextField txtPhucLoi;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
