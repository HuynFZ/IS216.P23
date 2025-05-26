
package View.Admin;

import Process.KhachHang.KhachHangModel;
import Process.KhachHang.LayMaKH;
import Process.KhachHang.SuaKhachHang;
import Process.KhachHang.ThongTinKH;
import Process.NhanVien.LayMaNV;
import Process.NhanVien.NhanVienModel;
import Process.NhanVien.SuaNhanVien;
import Process.NhanVien.ThongTinNV;
import View.TrangChu.TaiKhoanForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TaiKhoanNhanVienForm extends javax.swing.JPanel {

    private final JPanel mainPanel;
    private final String accId;
    private String maNV;
    private NhanVienModel nv = new NhanVienModel();

    public TaiKhoanNhanVienForm(JPanel mainPanel, String accId) throws ClassNotFoundException, SQLException {
        this.mainPanel = mainPanel;
        this.accId =accId;
        this.maNV = LayMaNV.layMaNVTuAccount(accId);
        this.nv = ThongTinNV.layThongTinNV(maNV);
        
        initComponents();
        
        setThongTin(nv);
        
        
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
                    nv.setHoTen(txtHoTen.getText());

                    String gioiTinhStr;
                    if (cbGioiTinh.getSelectedItem().equals("Nam")) {
                        gioiTinhStr = "M";
                    } else {
                        gioiTinhStr = "F";
                    }
                    nv.setGioiTinh(gioiTinhStr);

                    nv.setDiaChi(txtDiaChi.getText());
                    nv.setCccd(txtCCCD.getText());
                    nv.setSdt(txtSDT.getText());
                    nv.setEmail(txtMail.getText());

                    
                    try {
                        if (SuaNhanVien.suaThongTinNV(maNV, nv))
                        {
                            JOptionPane.showMessageDialog(null,"Sửa thông tin thành công");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,"Sửa thông tin không thành công");
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(TaiKhoanNhanVienForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(TaiKhoanNhanVienForm.class.getName()).log(Level.SEVERE, null, ex);
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
                setThongTin(nv);
                khoaChinhSua();
            }
        });
        
                
        System.out.println("View.TrangChu.TaiKhoanForm.<init>()");
        
        
        
        
    }
    
    private void setThongTin(NhanVienModel nvSet)
    {
        txtHoTen.setText(nvSet.getHoTen());
        JDNgaySinh.setDate(nvSet.getNgaySinh());
        String gioiTinhStr;
        if (nv.getGioiTinh().equals("M"))
        {
            gioiTinhStr = "Nam";
        }
        else gioiTinhStr = "Nữ";
        cbGioiTinh.setSelectedItem(gioiTinhStr);
        
        txtDiaChi.setText(nvSet.getDiaChi());
        
        txtCCCD.setText(nvSet.getCccd());
        
        txtSDT.setText(nvSet.getSdt());
        
        txtMail.setText(nvSet.getEmail());
        
        txtChucVu.setText(nvSet.getChucVu());
        
        txtLuongCB.setText(nvSet.getLuongCoBan());
        
        txtPhucLoi.setText(nvSet.getPhucLoi());
        
        JDNgayVL.setDate(nvSet.getNgayVaoLam());
        
        khoaChinhSua();
        
        btnXacNhan.setVisible(false);
        btnHuy.setVisible(false);
    }

    private void khoaChinhSua() {
        txtHoTen.setEditable(false);
        txtDiaChi.setEditable(false);
        txtCCCD.setEditable(false);
        txtSDT.setEditable(false);
        txtMail.setEditable(false);
        txtChucVu.setEditable(false);
        txtLuongCB.setEditable(false);
        txtPhucLoi.setEditable(false);

        cbGioiTinh.setEnabled(false);

        JDNgaySinh.setEnabled(false);
        JDNgaySinh.getDateEditor().setEnabled(false);
        
        JDNgayVL.setEnabled(false);
        JDNgayVL.getDateEditor().setEnabled(false);
    }

    private void moChinhSua() {
        txtHoTen.setEditable(true);
        txtCCCD.setEditable(true);
        txtSDT.setEditable(true);
        txtMail.setEditable(true);
        txtDiaChi.setEditable(true);

        cbGioiTinh.setEnabled(true);

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
        jLabel18 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPhucLoi = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtLuongCB = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnChinhSua = new javax.swing.JButton();
        btnXacNhan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        txtChucVu = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtMail = new javax.swing.JTextField();
        JDNgayVL = new com.toedter.calendar.JDateChooser();

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

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/citizenship.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel7.setText("Địa chỉ");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/people.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel10.setText("Chức vụ");

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/crown.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel11.setText("Lương cơ bản");

        txtPhucLoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtPhucLoi.setText("jTextField1");

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/star.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel13.setText("Phúc lợi");

        txtLuongCB.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLuongCB.setText("jTextField1");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/schedule.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel14.setText("Ngày vào làm");

        btnChinhSua.setBackground(new java.awt.Color(0, 204, 255));
        btnChinhSua.setFont(new java.awt.Font("UTM Times", 1, 22)); // NOI18N
        btnChinhSua.setText("Chỉnh sửa thông tin");

        btnXacNhan.setBackground(new java.awt.Color(51, 255, 51));
        btnXacNhan.setFont(new java.awt.Font("UTM Times", 1, 22)); // NOI18N
        btnXacNhan.setText("Xác nhận thông tin");

        btnHuy.setBackground(new java.awt.Color(255, 255, 51));
        btnHuy.setFont(new java.awt.Font("UTM Times", 1, 22)); // NOI18N
        btnHuy.setText("Hủy");

        txtChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtChucVu.setText("jTextField1");

        txtDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtDiaChi.setText("jTextField1");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/id-card.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel6.setText("CCCD");

        txtCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtCCCD.setText("jTextField2");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/phone.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel9.setText("Số điện thoại");

        txtSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtSDT.setText("jTextField2");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/gmail.png"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("UTM Centur", 0, 22)); // NOI18N
        jLabel17.setText("Email");

        txtMail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMail.setText("jTextField2");

        JDNgayVL.setDateFormatString("dd/MM/yyyy\n");
        JDNgayVL.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179)
                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel31)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel15)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbGioiTinh, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JDNgaySinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(txtDiaChi)
                    .addComponent(txtCCCD)
                    .addComponent(txtSDT)
                    .addComponent(txtMail))
                .addGap(126, 126, 126)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14))
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
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtLuongCB, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(txtPhucLoi, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(txtChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(JDNgayVL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                    .addComponent(txtChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtLuongCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPhucLoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(JDNgaySinh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(24, 24, 24)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17)
                                .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JDNgayVL, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser JDNgaySinh;
    private com.toedter.calendar.JDateChooser JDNgayVL;
    private javax.swing.JButton btnChinhSua;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JComboBox<String> cbGioiTinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtChucVu;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLuongCB;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtPhucLoi;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
