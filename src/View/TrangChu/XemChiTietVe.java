
package View.TrangChu;

import Process.ChiaTime;
import Process.TimeConverter;
import Process.HanhKhach.DanhSachHK;
import View.DatVe.*;
import Process.HanhKhach.UserHanhKhach;
import Process.KhuyenMai.LayGiaTriKM;
import Process.SanBay.TimSanBay;
import Process.SanBay.TimTinhTP;
import Process.VeMayBay.LayGiaVe;
import View.TrangChu.DatVeForm;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


public class XemChiTietVe extends javax.swing.JPanel {

    private String maVe, maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, giaVe, loaiHinh, quocTich, maDVGhe, maGoiHanhLy, maSuatAn, maBaoHiem;
    int nguoiLon, treEm, emBe, tongSoKH;
    double phiHanhly, phiBoSung;
    private final JPanel mainPanel;
    private final String accId;
    private DatVeForm datVeForm;
    private List<JPanel> khachHangPanels;
    private String maKhachHang;
    private ArrayList<UserHanhKhach> danhSachHanhKhach;
    
    public XemChiTietVe(String maVe, String maChuyen, String diemDi, String diemDen, String gioCatCanh, String gioHaCanh, int tongSoKH, JPanel mainJPanel, String accId) throws ClassNotFoundException, SQLException {
        this.mainPanel = mainJPanel;
        this.accId = accId;
        initComponents();
        this.tongSoKH = tongSoKH;
        this.maVe = maVe;
        this.danhSachHanhKhach = DanhSachHK.layDanhSachHK(maVe);
        this.gioCatCanh =gioCatCanh;
        this.gioHaCanh = gioHaCanh;
        giaVe = LayGiaVe.layGiaVe(maChuyen);
        DefaultTableModel model = (DefaultTableModel) bangThongTin.getModel();
        model.setRowCount(0);
        model.addRow(new Object[] {
            maChuyen,
            diemDi,
            diemDen,
            gioCatCanh,
            gioHaCanh,
            giaVe,
            tongSoKH
        });
        // Tạo panel nhập thông tin hành khách
        khachHangPanels = new ArrayList<>();
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        Font titleFont = new Font("UTM Centur", Font.BOLD, 18); // Font chữ lớn hơn và đậm
        int i=0;
        for (UserHanhKhach hk : danhSachHanhKhach) {
            i++;
            JPanel khachHangPanel = taoKhachHangPanel(i, hk);
            contentPanel.add(khachHangPanel);
            khachHangPanels.add(khachHangPanel);
            phiBoSung += hk.getPhiBoSung();
        }
        
        
        
        lblPhiDV.setText(String.valueOf(phiBoSung));
        
        
        lblgiaVeBay.setText(giaVe);
        
        double giaVeDB = Double.parseDouble(this.giaVe);
        double tongGiaVe = giaVeDB * tongSoKH;
        
        lblTongGiaVe.setText(String.valueOf(tongGiaVe));
        
        double thuePhi = tongGiaVe * 0.1;
        
        lblThuePhi.setText(String.valueOf(thuePhi));
        
        double giaTri = LayGiaTriKM.layGiaTriKM(maVe);
        
        lblGiamGia.setText(String.valueOf(giaTri));
        

        String tenThanhPhoDi = TimTinhTP.timTinhTP(diemDi);
        String tenThanhPhoDen = TimTinhTP.timTinhTP(diemDen);
        diemDiDen.setText(tenThanhPhoDi + " - " + tenThanhPhoDen );
        
        System.out.println(gioCatCanh);
        System.out.println(gioHaCanh);
        
        System.out.println("Chuỗi gốc: [" + gioCatCanh + "]");
            System.out.println("Length: " + gioCatCanh.length());
            for (int z = 0; z < gioCatCanh.length(); z++) {
                System.out.println("Char at " + z + ": " + (int) gioCatCanh.charAt(z) + " => '" + gioCatCanh.charAt(z) + "'");
            }


        String[] chiaTimeDi = ChiaTime.tachTime(gioCatCanh);
        String[] chiaTimeDen = ChiaTime.tachTime(gioHaCanh);
        
        System.out.println(chiaTimeDi);
        System.out.println(chiaTimeDen);

        if (chiaTimeDi != null && chiaTimeDen != null) {
            System.out.println(chiaTimeDi[0] + " | " + chiaTimeDen[0]);
            
            if (chiaTimeDi[0].equals(chiaTimeDen[0])) {
                NoiDung.setText(chiaTimeDi[0] + " | " + chiaTimeDi[1] + " - " + chiaTimeDen[1] + " | " + maChuyen);
            }
        }
        
        NhapThongTin.setViewportView(contentPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
        NhapThongTin.revalidate();
        NhapThongTin.repaint();
    }
    // tạo nội dung nhập cho từng hành khách
    private JPanel taoKhachHangPanel(int index, UserHanhKhach hk)
    {
        JPanel panel = new JPanel (new GridBagLayout());
        panel.setBackground(Color.WHITE);
         // Font cho tiêu đề
        Font titleFont = new Font("UTM Centur", Font.BOLD, 16); // Font chữ lớn hơn và đậm

        // Tạo Border với tiêu đề
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Hành khách " + index);
        titledBorder.setTitleFont(titleFont);
        panel.setBorder(titledBorder);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 2, 35, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;  // Căn các thành phần sang phía trái của panel
        gbc.weightx = 1.0;  // Đảm bảo các thành phần sẽ giãn ra khi có không gian trống
        gbc.weighty = 0.0;
        
        JTextField txtHo = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtCCCD = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtQuocTich = new JTextField();
        JTextField txtNgaySinh = new JTextField();
        JTextField txtGioiTinh = new JTextField();
        JTextField txtViTriGhe = new JTextField();
        
        
        Font font = new Font("UTM Times", Font.PLAIN, 18);
        Font labelFont = new Font("UTM Times", Font.BOLD, 14);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(labelFont);  // Thay đổi font chữ cho nhãn
        panel.add(lblGioiTinh, gbc);
        gbc.gridx = 1;
        txtGioiTinh.setFont(font);  // Thay đổi font chữ cho combo box
        txtGioiTinh.setText(hk.getGioiTinh());
        panel.add(txtGioiTinh, gbc);

        gbc.gridx = 2;
        JLabel lblHo = new JLabel("Họ:");
        lblHo.setFont(labelFont);
        panel.add(lblHo, gbc);
        gbc.gridx = 3;
        txtHo.setFont(font);  // Thay đổi font chữ cho text field
        txtHo.setText(hk.getHo());
        panel.add(txtHo, gbc);

        gbc.gridx = 4;
        JLabel lblTen = new JLabel("Tên đệm và tên:");
        lblTen.setFont(labelFont);
        panel.add(lblTen, gbc);
        gbc.gridx = 5;
        txtTen.setFont(font);
        txtTen.setText(hk.getTen());
        panel.add(txtTen, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(labelFont);
        panel.add(lblNgaySinh, gbc);
        gbc.gridx = 1;
        txtNgaySinh.setFont(font);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngaySinhStr = sdf.format(hk.getNgaySinh());
        txtNgaySinh.setText(ngaySinhStr);
        panel.add(txtNgaySinh, gbc);

        
        gbc.gridx = 2;
        JLabel lblCCCD = new JLabel("CCCD:");
        lblCCCD.setFont(labelFont);
        panel.add(lblCCCD, gbc);
        gbc.gridx = 3;
        txtCCCD.setFont(font);
        txtCCCD.setText(hk.getCccd());
        panel.add(txtCCCD, gbc);
        
        /*
        gbc.gridx = 4;
        JLabel lblNgayHetHanCCCD = new JLabel("Ngày hết hạn CCCD:");
        lblNgayHetHanCCCD.setFont(labelFont);
        panel.add(lblNgayHetHanCCCD, gbc);
        gbc.gridx = 5;
        dateChooserNgayHetHanCCCD.setFont(font);
        panel.add(dateChooserNgayHetHanCCCD, gbc);*/
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblQuocTich= new JLabel("Quốc tịch:");
        lblQuocTich.setFont(labelFont);
        panel.add(lblQuocTich, gbc);
        gbc.gridx = 1;
        txtQuocTich.setFont(font);
        txtQuocTich.setText(hk.getQuocTich());
        panel.add(txtQuocTich, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        JLabel lblSDT= new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        panel.add(lblSDT, gbc);
        gbc.gridx = 3;
        txtSDT.setFont(font);
        txtSDT.setText(hk.getSoDienThoai());
        panel.add(txtSDT, gbc);
        
        gbc.gridx = 4; gbc.gridy = 2;
        JLabel lblEmail= new JLabel("Email:");
        lblEmail.setFont(labelFont);
        panel.add(lblEmail, gbc);
        gbc.gridx = 5;
        txtEmail.setFont(font);
        txtEmail.setText(hk.getEmail());
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblViTriGhe = new JLabel("Vị trí ghế:");
        lblEmail.setFont(labelFont);
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtViTriGhe.setFont(font);
        txtViTriGhe.setText(hk.getViTriGhe());
        panel.add(txtViTriGhe, gbc);

        // Thêm chiều rộng cho các JTextField để căn lề
        txtGioiTinh.setPreferredSize(new Dimension(80, 25));
        txtQuocTich.setPreferredSize(new Dimension(80, 25));
        txtHo.setPreferredSize(new Dimension(80, 25));
        txtTen.setPreferredSize(new Dimension(80, 25));
        txtCCCD.setPreferredSize(new Dimension(80, 25));
        txtSDT.setPreferredSize(new Dimension(80, 25));
        txtEmail.setPreferredSize(new Dimension(80, 25));
        txtNgaySinh.setPreferredSize(new Dimension(80, 25));
        txtViTriGhe.setPreferredSize(new Dimension(80, 25));
        
        return panel;
    }
    
    
    public void layThongTinTuPanel() {
        danhSachHanhKhach.clear(); // Xóa dữ liệu cũ

        for (JPanel panel : khachHangPanels) {
            // Lấy loại khách hàng từ tiêu đề panel
            String borderTitle = ((TitledBorder) panel.getBorder()).getTitle();
            String loaiKhachHang = "Khách hàng"; // Mặc định
            if (borderTitle.contains("Người lớn")) loaiKhachHang = "Người lớn";
            else if (borderTitle.contains("Trẻ em")) loaiKhachHang = "Trẻ em";
            else if (borderTitle.contains("Em bé")) loaiKhachHang = "Em bé";

            String gioiTinh = "";
            String ho = "";
            String ten = "";
            String cccd = "";
            Date ngaySinh = null;
            Date ngayHetHanCCCD = null;
            String quocTich = "";
            String sdt = "";
            String email = "";
            String maDVGhe = "";
            String viTriGhe = "";
            double tienGhe = 0;
            String maGoiHanhLy = "";
            double phiHanhLy = 0;
            String maSuatAn = "";
            String maBaoHiem = "";

            for (Component comp : panel.getComponents()) {
                if (comp instanceof JComboBox) {
                    JComboBox cb = (JComboBox) comp;
                    // Giả sử JComboBox giới tính có 2 phần tử: Nam, Nữ
                    if (cb.getItemCount() == 2) {
                        gioiTinh = (String) cb.getSelectedItem();
                    } else {
                        quocTich = (String) cb.getSelectedItem();
                    }
                } else if (comp instanceof JTextField) {
                    JTextField txt = (JTextField) comp;
                    int cols = txt.getColumns();
                    // Dựa vào độ dài cột để phân biệt trường
                    if (cols == 10 && ho.isEmpty()) {
                        ho = txt.getText();
                    } else if (cols == 10) {
                        ten = txt.getText();
                    } else if (cols == 15) {
                        cccd = txt.getText();
                    } else if (cols == 20 && sdt.isEmpty()) {
                        sdt = txt.getText();
                    } else if (cols == 20) {
                        email = txt.getText();
                    }
                } else if (comp instanceof JDateChooser) {
                    JDateChooser chooser = (JDateChooser) comp;
                    if (ngaySinh == null) {
                        ngaySinh = chooser.getDate();
                    } else {
                        ngayHetHanCCCD = chooser.getDate();
                    }
                }
            }

            UserHanhKhach hk = new UserHanhKhach(loaiKhachHang, gioiTinh, ho, ten, ngaySinh, quocTich, cccd, ngayHetHanCCCD, maDVGhe, viTriGhe, tienGhe, maGoiHanhLy, phiHanhLy, maSuatAn, maBaoHiem,sdt, email);
            danhSachHanhKhach.add(hk);
        }
    }


    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bangThongTin = new javax.swing.JTable();
        thanhTruotNhapTT = new javax.swing.JScrollPane();
        NhapThongTin = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        quayLai = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cbThongTinHK = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        lblgiaVeBay = new javax.swing.JLabel();
        diemDiDen = new javax.swing.JLabel();
        NoiDung = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lblTongGiaVe = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        lblThuePhi = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        lblPhiDV = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        lblGiamGia = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        bangThongTin.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        bangThongTin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã chuyến bay", "Điểm đi", "Điểm đến", "GioCatCanh", "GioHaCanh", "Giá cơ bản", "Số lượng hành khách"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bangThongTin.setRowHeight(45);
        bangThongTin.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(bangThongTin);

        NhapThongTin.setBackground(new java.awt.Color(255, 255, 255));
        thanhTruotNhapTT.setViewportView(NhapThongTin);

        quayLai.setBackground(new java.awt.Color(255, 255, 0));
        quayLai.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        quayLai.setText("Quay lại");
        quayLai.setPreferredSize(new java.awt.Dimension(116, 37));
        quayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quayLaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(quayLai, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quayLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        jLabel19.setText("Chuyến đi");

        lblgiaVeBay.setFont(new java.awt.Font("UTM Nyala", 1, 25)); // NOI18N
        lblgiaVeBay.setForeground(new java.awt.Color(255, 0, 51));
        lblgiaVeBay.setText("2.074.000 VND");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblgiaVeBay)
                .addGap(47, 47, 47))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(lblgiaVeBay))
                .addContainerGap())
        );

        diemDiDen.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        diemDiDen.setText("Tp. Hồ Chí Minh - Hà Nội");

        NoiDung.setFont(new java.awt.Font("UTM Times", 0, 14)); // NOI18N
        NoiDung.setText("T5,  24/04/2025 | 06:00 - 08:20 | M1234 | Eco");

        jLabel23.setBackground(new java.awt.Color(229, 229, 229));
        jLabel23.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel23.setText("Giá vé");

        lblTongGiaVe.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblTongGiaVe.setText("jLabel29");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblTongGiaVe, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblTongGiaVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel30.setBackground(new java.awt.Color(229, 229, 229));
        jLabel30.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel30.setText("Thuế, phí");

        lblThuePhi.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblThuePhi.setText("jLabel29");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblThuePhi, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblThuePhi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel32.setBackground(new java.awt.Color(229, 229, 229));
        jLabel32.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel32.setText("Dịch vụ");

        lblPhiDV.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblPhiDV.setText("jLabel29");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblPhiDV, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblPhiDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel35.setBackground(new java.awt.Color(229, 229, 229));
        jLabel35.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel35.setText("Giảm giá");

        lblGiamGia.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblGiamGia.setText("jLabel29");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(cbThongTinHK, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(116, 116, 116))
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(cbThongTinHK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(NoiDung)
                .addGap(10, 10, 10)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBackground(new java.awt.Color(51, 51, 255));

        jLabel1.setFont(new java.awt.Font("UTM Times", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chi tiết vé");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(thanhTruotNhapTT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(thanhTruotNhapTT)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void quayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quayLaiActionPerformed
        if (datVeForm == null){
        try {
            datVeForm = new DatVeForm(mainPanel, accId);
        } catch (ClassNotFoundException | SQLException ex)
                {
                ex.printStackTrace();
                }
        }
        setForm(datVeForm);
    }//GEN-LAST:event_quayLaiActionPerformed

    private void setForm (JComponent com)
    {
        for (Component component : mainPanel.getComponents()) {
        component.setVisible(false);
    }
    
        // Hiển thị lại form muốn chuyển đến
        mainPanel.add(com);
        com.setVisible(true);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane NhapThongTin;
    private javax.swing.JLabel NoiDung;
    private javax.swing.JTable bangThongTin;
    private javax.swing.JComboBox<String> cbThongTinHK;
    private javax.swing.JLabel diemDiDen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDichVu1;
    private javax.swing.JLabel lblDichVu2;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblPhiDV;
    private javax.swing.JLabel lblThuePhi;
    private javax.swing.JLabel lblTongGiaVe;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblgiaVeBay;
    private javax.swing.JButton quayLai;
    private javax.swing.JScrollPane thanhTruotNhapTT;
    // End of variables declaration//GEN-END:variables
}
