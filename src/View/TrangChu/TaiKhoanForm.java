
package View.TrangChu;

import Process.KhachHang.KhachHangModel;
import Process.KhachHang.LayMaKH;
import Process.KhachHang.SuaKhachHang;
import Process.KhachHang.ThongTinKH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TaiKhoanForm extends javax.swing.JPanel {

    private final JPanel mainPanel;
    private final String accId;
    private String maKH;
    private KhachHangModel kh = new KhachHangModel();

    public TaiKhoanForm(JPanel mainPanel, String accId) throws ClassNotFoundException, SQLException {
        this.mainPanel = mainPanel;
        this.accId =accId;
        this.maKH = LayMaKH.layMaKHTuAccount(accId);
        this.kh = ThongTinKH.layThongTinKH(maKH);
        
        initComponents();
        
        setThongTin(kh);
        
        
        btnChinhSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moChinhSua();
                btnXacNhan.setVisible(true);
                btnHuy.setVisible(true);
            }
        });
        
        btnXacNhan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn lưu thay đổi?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) {
                    kh.setHoTen(txtHoTen.getText());

                    String gioiTinhStr;
                    if (cbGioiTinh.getSelectedItem().equals("Nam")) {
                        gioiTinhStr = "M";
                    } else {
                        gioiTinhStr = "F";
                    }
                    kh.setGioiTinh(gioiTinhStr);

                    kh.setQuocTich((String) cbQuocTich.getSelectedItem());
                    kh.setCccd(txtCCCD.getText());
                    kh.setSDT(txtSDT.getText());
                    kh.setEmail(txtMail.getText());

                    
                    try {
                        if (SuaKhachHang.suaThongTinKH(maKH, kh))
                        {
                            JOptionPane.showMessageDialog(null,"Sửa thông tin thành công");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,"Sửa thông tin không thành công");
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(TaiKhoanForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(TaiKhoanForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    btnXacNhan.setVisible(false);
                    btnHuy.setVisible(false);
                    khoaChinhSua();
                }
            }
        });
        
        btnHuy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                setThongTin(kh);
                khoaChinhSua();
            }
        });
        
                
        System.out.println("View.TrangChu.TaiKhoanForm.<init>()");
        
        
        
        
    }
    
    private void setThongTin(KhachHangModel khset)
    {
        txtHoTen.setText(khset.getHoTen());
        JDNgaySinh.setDate(khset.getNgaySinh());
        String gioiTinhStr;
        if (kh.getGioiTinh().equals("M"))
        {
            gioiTinhStr = "Nam";
        }
        else gioiTinhStr = "Nữ";
        cbGioiTinh.setSelectedItem(gioiTinhStr);
        
        cbQuocTich.setSelectedItem(khset.getQuocTich());
        
        txtCCCD.setText(khset.getCccd());
        
        txtSDT.setText(khset.getSDT());
        
        txtMail.setText(khset.getEmail());
        
        cbLoaiKH.setSelectedItem(khset.getLoaiKH());
        
        txtHangTV.setText(khset.getHangTV());
        
        txtDiemThuong.setText(khset.getDiemThuong());
        
        txtHanTV.setText(khset.getThoiGianCapNhat());
        
        khoaChinhSua();
        
        btnXacNhan.setVisible(false);
        btnHuy.setVisible(false);
    }

    private void khoaChinhSua() {
        txtHoTen.setEditable(false);
        txtCCCD.setEditable(false);
        txtSDT.setEditable(false);
        txtMail.setEditable(false);
        txtHangTV.setEditable(false);
        txtDiemThuong.setEditable(false);
        txtHanTV.setEditable(false);

        cbGioiTinh.setEnabled(false);
        cbQuocTich.setEnabled(false);
        cbLoaiKH.setEnabled(false);

        JDNgaySinh.setEnabled(false);
        JDNgaySinh.getDateEditor().setEnabled(false);
    }

    private void moChinhSua() {
        txtHoTen.setEditable(true);
        txtCCCD.setEditable(true);
        txtSDT.setEditable(true);
        txtMail.setEditable(true);

        cbGioiTinh.setEnabled(true);
        cbQuocTich.setEnabled(true);
        cbLoaiKH.setEnabled(true);

    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        JDNgaySinh = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbGioiTinh = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbQuocTich = new com.toedter.components.JLocaleChooser();
        jLabel18 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbLoaiKH = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtDiemThuong = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtHangTV = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtHanTV = new javax.swing.JTextField();
        btnChinhSua = new javax.swing.JButton();
        btnXacNhan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMail = new javax.swing.JTextField();

        setBackground(new java.awt.Color(252, 250, 250));

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));

        jLabel1.setFont(new java.awt.Font("UTM Times", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thông tin cá nhân");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/user (1).png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel2.setText("Họ và tên");

        txtHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHoTen.setText("jTextField1");

        jLabel4.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel4.setText("Ngày sinh");

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/birthday-cake.png"))); // NOI18N

        JDNgaySinh.setDateFormatString("dd/MM/yyyy\n");
        JDNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/gender.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel5.setText("Giới tính");

        cbGioiTinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", " " }));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/citizenship.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel6.setText("Quốc tịch");

        cbQuocTich.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Việt Nam", "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua và Barbuda", "Ả Rập Xê Út", "Argentina", "Armenia", "Úc", "Áo", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Bỉ", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia và Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Campuchia", "Cameroon", "Canada", "Cộng hòa Trung Phi", "Chad", "Chile", "Trung Quốc", "Colombia", "Comoros", "Congo (Cộng hòa)", "Congo (Cộng hòa Dân chủ)", "Costa Rica", "Croatia", "Cuba", "Síp", "Cộng hòa Séc", "Đan Mạch", "Djibouti", "Dominica", "Cộng hòa Dominica", "Đông Timor", "Ecuador", "Ai Cập", "El Salvador", "Guinea Xích Đạo", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Phần Lan", "Pháp", "Gabon", "Gambia", "Georgia", "Đức", "Ghana", "Hy Lạp", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "Ấn Độ", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Ý", "Jamaica", "Nhật Bản", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Hàn Quốc", "Kuwait", "Kyrgyzstan", "Lào", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Litva", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Quần đảo Marshall", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mông Cổ", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Hà Lan", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Bắc Macedonia", "Na Uy", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Ba Lan", "Bồ Đào Nha", "Qatar", "Romania", "Nga", "Rwanda", "Saint Kitts và Nevis", "Saint Lucia", "Saint Vincent và Grenadines", "Samoa", "San Marino", "São Tomé và Príncipe", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Quần đảo Solomon", "Somalia", "Nam Phi", "Nam Sudan", "Tây Ban Nha", "Sri Lanka", "Sudan", "Suriname", "Thụy Điển", "Thụy Sĩ", "Syria", "Đài Loan", "Tajikistan", "Tanzania", "Thái Lan", "Togo", "Tonga", "Trinidad và Tobago", "Tunisia", "Thổ Nhĩ Kỳ", "Turkmenistan", "Tuvalu", "Uganda", "Ukraina", "Các Tiểu vương quốc Ả Rập Thống nhất", "Vương quốc Anh", "Hoa Kỳ", "Uruguay", "Uzbekistan", "Vanuatu", "Thành Vatican", "Venezuela", "Việt Nam", "Yemen", "Zambia", "Zimbabwe" }));
        cbQuocTich.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/id-card.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel7.setText("CCCD");

        txtCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtCCCD.setText("jTextField1");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/people.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel10.setText("Loại khách hàng");

        cbLoaiKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cbLoaiKH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cá nhân", "Doanh nghiệp" }));
        cbLoaiKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLoaiKHActionPerformed(evt);
            }
        });

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/crown.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel11.setText("Hạng thành viên");

        txtDiemThuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtDiemThuong.setText("jTextField1");

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/star.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel13.setText("Điểm thưởng");

        txtHangTV.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHangTV.setText("jTextField1");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/schedule.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel14.setText("Thời hạn thành viên");

        txtHanTV.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHanTV.setText("jTextField1");

        btnChinhSua.setBackground(new java.awt.Color(0, 204, 255));
        btnChinhSua.setFont(new java.awt.Font("UTM Times", 1, 22)); // NOI18N
        btnChinhSua.setText("Chỉnh sửa thông tin");

        btnXacNhan.setBackground(new java.awt.Color(51, 255, 51));
        btnXacNhan.setFont(new java.awt.Font("UTM Times", 1, 22)); // NOI18N
        btnXacNhan.setText("Xác nhận thông tin");

        btnHuy.setBackground(new java.awt.Color(255, 255, 51));
        btnHuy.setFont(new java.awt.Font("UTM Times", 1, 22)); // NOI18N
        btnHuy.setText("Hủy");

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/phone.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel8.setText("Số điện thoại");

        txtSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtSDT.setText("jTextField1");

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/gmail.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel9.setText("Email");

        txtMail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMail.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(131, 131, 131)
                        .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                                .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel31)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cbGioiTinh, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(JDNgaySinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtHoTen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbQuocTich, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(135, 135, 135)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel13))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel11))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel10)))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHangTV, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiemThuong, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addGap(14, 14, 14)
                                .addComponent(txtHanTV, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbLoaiKH)))
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtHangTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDiemThuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(JDNgaySinh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHanTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbQuocTich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(140, 140, 140)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(374, 374, 374))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbLoaiKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLoaiKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbLoaiKHActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser JDNgaySinh;
    private javax.swing.JButton btnChinhSua;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JComboBox<String> cbGioiTinh;
    private javax.swing.JComboBox<String> cbLoaiKH;
    private com.toedter.components.JLocaleChooser cbQuocTich;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtDiemThuong;
    private javax.swing.JTextField txtHanTV;
    private javax.swing.JTextField txtHangTV;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
