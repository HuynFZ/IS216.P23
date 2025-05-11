
package View.Admin;


public class QLHanhLyForm extends javax.swing.JPanel {


    public QLHanhLyForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMaHanhLy = new javax.swing.JLabel();
        lblMaKH = new javax.swing.JLabel();
        lblMaChuyen = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtMaKH = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        lblLoai = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();
        lblViTri = new javax.swing.JLabel();
        lblGhiChu = new javax.swing.JLabel();
        lblTenKH = new javax.swing.JLabel();
        txtMaHanhLy = new javax.swing.JTextField();
        cmbLoai = new javax.swing.JComboBox<>();
        txtViTri = new javax.swing.JTextField();
        txtGhiChu = new javax.swing.JTextField();
        txtChuyen = new javax.swing.JTextField();
        txtTrongLuong = new javax.swing.JTextField();
        txtTenKH = new javax.swing.JTextField();
        lblTrongLuong = new javax.swing.JLabel();
        cmbTrangThai = new javax.swing.JComboBox<>();
        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        spDSChuyen = new javax.swing.JScrollPane();
        tblDSChuyen = new javax.swing.JTable();
        pnlDSHanhLy = new javax.swing.JPanel();
        lblDSHanhLy = new javax.swing.JLabel();
        spDSHanhLy = new javax.swing.JScrollPane();
        tblDSHanhLy = new javax.swing.JTable();

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thêm hành lý");

        lblMaHanhLy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaHanhLy.setText("Mã hành lý");

        lblMaKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaKH.setText("Mã khách hàng");

        lblMaChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaChuyen.setText("Mã chuyến bay");

        lblNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgaySinh.setText("(kg)");

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

        txtMaKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaKH.setText("MaKhachHang");
        txtMaKH.setAutoscrolls(false);

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

        lblLoai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLoai.setText("Loại hành lý");

        lblTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrangThai.setText("Trạng thái");

        lblViTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblViTri.setText("Vị trí hành lý");

        lblGhiChu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblGhiChu.setText("Ghi chú");

        lblTenKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTenKH.setText("Tên khách hàng");

        txtMaHanhLy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaHanhLy.setText("MaHanhLy");
        txtMaHanhLy.setAutoscrolls(false);
        txtMaHanhLy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHanhLyActionPerformed(evt);
            }
        });

        cmbLoai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtViTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtViTri.setText("ViTriHanhLy");
        txtViTri.setAutoscrolls(false);

        txtGhiChu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtGhiChu.setText("GhiChu");
        txtGhiChu.setAutoscrolls(false);

        txtChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtChuyen.setText("MaChuyenBay");

        txtTrongLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTrongLuong.setText("TrongLuong");

        txtTenKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTenKH.setText("TenKhachHang");

        lblTrongLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrongLuong.setText("Trọng lượng");

        cmbTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblViTri, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGhiChu)
                    .addComponent(lblTenKH)
                    .addComponent(btnThem)
                    .addComponent(lblTrongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addComponent(lblThaoTac)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaHanhLy)
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNgaySinh))))
                        .addGap(45, 45, 45))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                        .addComponent(btnXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSua)
                        .addGap(56, 56, 56))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGhiChu, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtChuyen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtTrongLuong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtViTri, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbLoai, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbTrangThai, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblThaoTac)
                .addGap(38, 38, 38)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaHanhLy)
                    .addComponent(txtMaHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaChuyen)
                    .addComponent(txtChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKH))
                .addGap(14, 14, 14)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenKH)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNgaySinh)
                    .addComponent(txtTrongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTrongLuong))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoai)
                    .addComponent(cmbLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrangThai)
                    .addComponent(cmbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblViTri)
                    .addComponent(txtViTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGhiChu)
                    .addComponent(txtGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnThem))
                .addGap(45, 45, 45))
        );

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

        pnlDSHanhLy.setBackground(javax.swing.UIManager.getDefaults().getColor("Component.focusColor"));

        lblDSHanhLy.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        lblDSHanhLy.setText("Danh sách hành lý  của chuyến ....");

        tblDSHanhLy.setAutoCreateRowSorter(true);
        tblDSHanhLy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSHanhLy.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDSHanhLy.setToolTipText("");
        spDSHanhLy.setViewportView(tblDSHanhLy);

        javax.swing.GroupLayout pnlDSHanhLyLayout = new javax.swing.GroupLayout(pnlDSHanhLy);
        pnlDSHanhLy.setLayout(pnlDSHanhLyLayout);
        pnlDSHanhLyLayout.setHorizontalGroup(
            pnlDSHanhLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spDSHanhLy, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
            .addGroup(pnlDSHanhLyLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(lblDSHanhLy)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDSHanhLyLayout.setVerticalGroup(
            pnlDSHanhLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSHanhLyLayout.createSequentialGroup()
                .addComponent(lblDSHanhLy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spDSHanhLy, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
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
                            .addComponent(pnlDSHanhLy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(34, Short.MAX_VALUE))
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
                .addComponent(pnlDSHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap())
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

    private void txtMaHanhLyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHanhLyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHanhLyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbLoai;
    private javax.swing.JComboBox<String> cmbTrangThai;
    private javax.swing.JLabel lblDSHanhLy;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblGhiChu;
    private javax.swing.JLabel lblLoai;
    private javax.swing.JLabel lblMaChuyen;
    private javax.swing.JLabel lblMaHanhLy;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JLabel lblTrongLuong;
    private javax.swing.JLabel lblViTri;
    private javax.swing.JPanel pnlDSHanhLy;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JScrollPane spDSChuyen;
    private javax.swing.JScrollPane spDSHanhLy;
    private javax.swing.JTable tblDSChuyen;
    private javax.swing.JTable tblDSHanhLy;
    private javax.swing.JTextField txtChuyen;
    private javax.swing.JTextField txtGhiChu;
    private javax.swing.JTextField txtMaHanhLy;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTrongLuong;
    private javax.swing.JTextField txtViTri;
    // End of variables declaration//GEN-END:variables
}
