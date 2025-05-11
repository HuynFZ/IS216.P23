
package View.Admin;


public class QLDatVeForm extends javax.swing.JPanel {


    public QLDatVeForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMaVe = new javax.swing.JLabel();
        lblMaChuyen = new javax.swing.JLabel();
        lblTien = new javax.swing.JLabel();
        lblLoaiVe = new javax.swing.JLabel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtMaChuyen = new javax.swing.JTextField();
        lblLoaiKH = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();
        txtMaVe = new javax.swing.JTextField();
        dcNgayDat = new com.toedter.calendar.JDateChooser();
        cmbTrangThai = new javax.swing.JComboBox<>();
        txtTien = new javax.swing.JTextField();
        cmbLoaiVe = new javax.swing.JComboBox<>();
        lblNgayDat = new javax.swing.JLabel();
        lblTenKH = new javax.swing.JLabel();
        lblCCCD = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        txtLoaiKH = new javax.swing.JTextField();
        txtNgaySinh = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        txtTenKH = new javax.swing.JTextField();
        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        spDSChuyen = new javax.swing.JScrollPane();
        tblDSChuyen = new javax.swing.JTable();
        pnlDSVe = new javax.swing.JPanel();
        lblDanhSach1 = new javax.swing.JLabel();
        spDSVe = new javax.swing.JScrollPane();
        tblDSVe = new javax.swing.JTable();

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thông tin vé máy bay");

        lblMaVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaVe.setText("Mã vé máy bay");

        lblMaChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaChuyen.setText("Mã chuyến bay");

        lblTien.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTien.setText("Tổng tiền");

        lblLoaiVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLoaiVe.setText("Loại vé");

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

        txtMaChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaChuyen.setText("MaChuyenBay");
        txtMaChuyen.setAutoscrolls(false);

        lblLoaiKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLoaiKH.setText("Loại khách hàng");

        lblTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrangThai.setText("Trạng thái");

        txtMaVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaVe.setText("MaVe");
        txtMaVe.setAutoscrolls(false);
        txtMaVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaVeActionPerformed(evt);
            }
        });

        cmbTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtTien.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTien.setText("TongTien");

        cmbLoaiVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbLoaiVe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblNgayDat.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgayDat.setText("Ngày đặt vé");

        lblTenKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTenKH.setText("Tên khách hàng");

        lblCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblCCCD.setText("Số CCCD");

        lblNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgaySinh.setText("Ngày sinh");

        txtLoaiKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLoaiKH.setText("MaVe");
        txtLoaiKH.setAutoscrolls(false);
        txtLoaiKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoaiKHActionPerformed(evt);
            }
        });

        txtNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtNgaySinh.setText("MaVe");
        txtNgaySinh.setAutoscrolls(false);
        txtNgaySinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgaySinhActionPerformed(evt);
            }
        });

        txtCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtCCCD.setText("MaVe");
        txtCCCD.setAutoscrolls(false);
        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });

        txtTenKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTenKH.setText("MaVe");
        txtTenKH.setAutoscrolls(false);
        txtTenKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblMaChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTien, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblLoaiVe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTrangThai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNgayDat, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(btnXoa)))
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                        .addComponent(btnSua)
                        .addGap(129, 129, 129))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbTrangThai, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaVe)
                            .addComponent(dcNgayDat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbLoaiVe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTien, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaChuyen, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLoaiKH, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenKH, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(45, 45, 45))))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(lblThaoTac)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblThaoTac)
                .addGap(32, 32, 32)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaVe)
                    .addComponent(txtMaVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaChuyen)
                    .addComponent(txtMaChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTien))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoaiVe)
                    .addComponent(cmbLoaiVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dcNgayDat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNgayDat))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrangThai)
                    .addComponent(cmbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenKH)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoaiKH)
                    .addComponent(txtLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNgaySinh)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCCCD)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoa)
                    .addComponent(btnSua))
                .addGap(45, 45, 45))
        );

        pnlThaoTacLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbTrangThai, dcNgayDat});

        pnlDanhSach.setBackground(java.awt.SystemColor.control);
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(632, 824));

        lblDanhSach.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblDanhSach.setText("Danh sách chuyến bay");

        txtTimKiem.setFont(new java.awt.Font("UTM Centur", 0, 20)); // NOI18N
        txtTimKiem.setText("Thanh tìm kiếm chuyến bay");
        txtTimKiem.setAutoscrolls(false);
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/search.png"))); // NOI18N

        tblDSChuyen.setAutoCreateRowSorter(true);
        tblDSChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSChuyen.setModel(new javax.swing.table.DefaultTableModel(
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
        spDSChuyen.setViewportView(tblDSChuyen);

        pnlDSVe.setBackground(javax.swing.UIManager.getDefaults().getColor("Component.focusColor"));

        lblDanhSach1.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        lblDanhSach1.setText("Danh sách vé của chuyến ....");

        tblDSVe.setAutoCreateRowSorter(true);
        tblDSVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSVe.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDSVe.setToolTipText("");
        spDSVe.setViewportView(tblDSVe);

        javax.swing.GroupLayout pnlDSVeLayout = new javax.swing.GroupLayout(pnlDSVe);
        pnlDSVe.setLayout(pnlDSVeLayout);
        pnlDSVeLayout.setHorizontalGroup(
            pnlDSVeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spDSVe)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDSVeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDanhSach1)
                .addGap(101, 101, 101))
        );
        pnlDSVeLayout.setVerticalGroup(
            pnlDSVeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSVeLayout.createSequentialGroup()
                .addComponent(lblDanhSach1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spDSVe, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(0, 64, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnTimKiem)
                .addGap(55, 55, 55))
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(lblDanhSach))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spDSChuyen, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                            .addComponent(pnlDSVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblDanhSach)
                .addGap(39, 39, 39)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addGap(18, 18, 18)
                .addComponent(spDSChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(pnlDSVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtMaVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaVeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaVeActionPerformed

    private void txtLoaiKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoaiKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoaiKHActionPerformed

    private void txtNgaySinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgaySinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgaySinhActionPerformed

    private void txtCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCCCDActionPerformed

    private void txtTenKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenKHActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbLoaiVe;
    private javax.swing.JComboBox<String> cmbTrangThai;
    private com.toedter.calendar.JDateChooser dcNgayDat;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblDanhSach1;
    private javax.swing.JLabel lblLoaiKH;
    private javax.swing.JLabel lblLoaiVe;
    private javax.swing.JLabel lblMaChuyen;
    private javax.swing.JLabel lblMaVe;
    private javax.swing.JLabel lblNgayDat;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JLabel lblTien;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JPanel pnlDSVe;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JScrollPane spDSChuyen;
    private javax.swing.JScrollPane spDSVe;
    private javax.swing.JTable tblDSChuyen;
    private javax.swing.JTable tblDSVe;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtLoaiKH;
    private javax.swing.JTextField txtMaChuyen;
    private javax.swing.JTextField txtMaVe;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTien;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
