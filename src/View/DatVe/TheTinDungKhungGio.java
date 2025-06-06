
package View.DatVe;

import Process.ChiaTime;
import Process.HanhKhach.ThemHanhKhach;
import Process.VeMayBay.ThemVeMayBay;
import Model.HanhKhach.UserHanhKhach;
import Process.KhachHang.LayMaKH;
import Process.KhachHang.TruDiemKhachHang;
import Process.SanBay.TimTinhTP;
import Process.ThanhToan.ThemThanhToan;
import View.TrangChu.VeCuaToiForm;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import oracle.sql.CharacterSetWithConverter;

public class TheTinDungKhungGio extends javax.swing.JFrame {

    private String maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, giaVe, loaiHinh, maKhuyenMai, maVe, diemThuong;
    int nguoiLon, treEm, emBe, tongSoKH;
    private String accId;
    private static List<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();
    private double phiChoNgoi = 0;
    private double phiHanhLy = 0;
    private double phiSuatAn = 0;
    private double phiBaoHiem = 0;
    private double tongPhiDichVu = 0;
    private double tongTien = 0;
    private double tongTienSauCung = 0;
    private final JPanel mainPanel;

    public TheTinDungKhungGio(JPanel mainJPanel, String diemDi, String diemDen, int nguoiLon, int treEm, int emBe,int tongSoKH,double tongTienSauCung, List<UserHanhKhach> danhSachHanhKhach, String accId, double tongPhiDV, double phiBaoHiem, double phiSuatAn) throws ClassNotFoundException, SQLException {
        initComponents();
        this.mainPanel = mainJPanel;
        this.tongTienSauCung = tongTienSauCung;
        this.tongTien = tongTien;
        this.tongPhiDichVu = tongPhiDV;
        this.phiBaoHiem = phiBaoHiem;
        this.phiSuatAn = phiSuatAn;
        
        this.accId = accId;
        this.tongSoKH = tongSoKH;
        String tenThanhPhoDi = TimTinhTP.timTinhTP(diemDi);
        String tenThanhPhoDen = TimTinhTP.timTinhTP(diemDen);
        diemDiDen.setText(tenThanhPhoDi + " - " + tenThanhPhoDen );
        
        
        
        for (UserHanhKhach hk : danhSachHanhKhach)
        {
            cbThongTinHK.addItem(hk.getHo() + " " + hk.getTen());
        }
        
        giaVeBay.setText("200.000 VND");
       lblTongTien.setText(String.valueOf(tongTien));
        btnHuyTT.addActionListener(e -> {
            dispose();
        });
        
        btnXacNhanTT.addActionListener(e -> {
            String maKhachHang = null;

            try {
                maKhachHang = LayMaKH.layMaKHTuAccount(accId);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDungKhungGio.class.getName()).log(Level.SEVERE, null, ex);
                return; // thoát nếu lỗi
            }

            if (maKhachHang == null || maKhachHang.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy mã khách hàng!");
                return;
            }

            try {
                 maVe = ThemVeMayBay.themVeMayBay(maChuyen, maKhachHang, tongTien, "Vé khung giờ", "Đã thanh toán");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDungKhungGio.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm vé máy bay!");
                return;
            }
            
            for (UserHanhKhach hk : danhSachHanhKhach)
            {
                String hoTen = hk.getHo() + " " + hk.getTen();
                double phiBoS = hk.getTienGhe() + hk.getPhiHanhly() + 100000;
                String giaVeClean = giaVe.replace(",", "");
                double giaVeDouble = Double.parseDouble(giaVeClean);
                java.sql.Date ngaySinhSql = new java.sql.Date(hk.getNgaySinh().getTime());
                String gioiTinh = hk.getGioiTinh();
                String gioiTinhDB;
                if (gioiTinh.equalsIgnoreCase("Nam")) {
                    gioiTinhDB = "M";
                } else if (gioiTinh.equalsIgnoreCase("Nữ")) {
                    gioiTinhDB = "F";
                } else {
                    gioiTinhDB = "U"; 
                }


                try {
                    ThemHanhKhach.themHanhKhach(hoTen, hk.getCccd(), ngaySinhSql, gioiTinhDB, hk.getQuocTich(), hk.getSoDienThoai(),
                                                hk.getEmail(), maVe, "Economy", giaVeDouble, phiBoS, hk.getViTriGhe());
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(TheTinDungKhungGio.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Lỗi khi thêm hành khách!");
                    return;
                }
                
                
                
            }
            
            
            try {
                
                 ThemThanhToan.themThanhToan(maVe, maKhuyenMai, tongTien,"Ví điện tử","Đang xử lý");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDungKhungGio.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm thanh toán!");
                return;
            }

            JOptionPane.showMessageDialog(null,"Bạn đã thanh toán thành công");

            dispose();
            try {
                setForm(new VeCuaToiForm(mainPanel, this.accId));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheTinDungKhungGio.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TheTinDungKhungGio.class.getName()).log(Level.SEVERE, null, ex);
            }
            });
        


    }
    
    private void setForm (JComponent com)
    {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cbThongTinHK = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        giaVeBay = new javax.swing.JLabel();
        diemDiDen = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        tienDV = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        tienSuatAn = new javax.swing.JLabel();
        tienBaoHiem = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        jLabel7 = new javax.swing.JLabel();
        btnHuyTT = new javax.swing.JButton();
        btnXacNhanTT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, null));

        jPanel10.setBackground(new java.awt.Color(213, 0, 0));

        jLabel18.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("THÔNG TIN ĐẶT CHỖ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel18)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        cbThongTinHK.setBackground(new java.awt.Color(229, 229, 229));
        cbThongTinHK.setEditable(true);
        cbThongTinHK.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cbThongTinHK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thông tin hành khách" }));

        jPanel11.setBackground(new java.awt.Color(174, 226, 250));

        jLabel19.setFont(new java.awt.Font("UTM Nyala", 0, 20)); // NOI18N
        jLabel19.setText("Tiền cọc");

        giaVeBay.setFont(new java.awt.Font("UTM Nyala", 1, 22)); // NOI18N
        giaVeBay.setForeground(new java.awt.Color(255, 0, 51));
        giaVeBay.setText("2.074.000 VND");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(giaVeBay, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(giaVeBay))
                .addContainerGap())
        );

        diemDiDen.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        diemDiDen.setText("Tp. Hồ Chí Minh - Hà Nội");

        jLabel32.setBackground(new java.awt.Color(229, 229, 229));
        jLabel32.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel32.setText("Dịch vụ");

        tienDV.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        tienDV.setText("jLabel29");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tienDV, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tienDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel16.setBackground(new java.awt.Color(213, 0, 0));

        jLabel24.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Tổng tiền");

        lblTongTien.setFont(new java.awt.Font("UTM Times", 1, 24)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 255, 255));
        lblTongTien.setText("2.074.000 VND");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tienSuatAn.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienSuatAn.setForeground(new java.awt.Color(51, 51, 51));
        tienSuatAn.setText("Phí suất ăn");

        tienBaoHiem.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienBaoHiem.setForeground(new java.awt.Color(51, 51, 51));
        tienBaoHiem.setText("Phí bảo hiểm");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tienSuatAn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tienBaoHiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(cbThongTinHK, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(cbThongTinHK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tienSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tienBaoHiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 204));

        jLabel20.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("THANH TOÁN BẰNG THẺ TÍN DỤNG");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel20)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jLabel1.setText("Số điện thoại");

        jTextField1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jTextField1.setText("Vui lòng nhập số điện thoại");

        jLabel2.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jLabel2.setText("Email");

        jLabel3.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jLabel3.setText("Số thẻ");

        jLabel4.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jLabel4.setText("Tháng hết hạn");

        jLabel5.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jLabel5.setText("Năm hết hạn");

        jTextField4.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jTextField4.setText("Vui lòng nhập email");

        jTextField5.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jTextField5.setText("Vui lòng nhập số thẻ");

        jLabel6.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jLabel6.setText("CVN");

        jTextField6.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        jMonthChooser1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Mã này là số có ba hoặc bốn chữ số được in ở mặt trước hoặc mặt sau của thẻ tín dụng");

        btnHuyTT.setBackground(new java.awt.Color(255, 255, 0));
        btnHuyTT.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        btnHuyTT.setText("Hủy thanh toán");

        btnXacNhanTT.setBackground(new java.awt.Color(0, 255, 0));
        btnXacNhanTT.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        btnXacNhanTT.setText("Xác nhận thanh toán");
        btnXacNhanTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanTTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jMonthChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(146, 146, 146)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jYearChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(jTextField4)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField6)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 9, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(btnHuyTT, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXacNhanTT)
                        .addGap(73, 73, 73)))
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jYearChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jMonthChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuyTT, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhanTT, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58))
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

    private void btnXacNhanTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanTTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXacNhanTTActionPerformed

//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TheTinDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TheTinDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TheTinDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TheTinDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TheTinDung().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuyTT;
    private javax.swing.JButton btnXacNhanTT;
    private javax.swing.JComboBox<String> cbThongTinHK;
    private javax.swing.JLabel diemDiDen;
    private javax.swing.JLabel giaVeBay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel tienBaoHiem;
    private javax.swing.JLabel tienDV;
    private javax.swing.JLabel tienSuatAn;
    // End of variables declaration//GEN-END:variables
}
