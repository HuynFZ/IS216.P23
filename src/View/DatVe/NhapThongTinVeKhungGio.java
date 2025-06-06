
package View.DatVe;

import Model.HanhKhach.UserHanhKhach;
import View.TrangChu.DatVeForm;
import View.TrangChu.DatVeKhungGioForm;
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


public class NhapThongTinVeKhungGio extends javax.swing.JPanel {

    private String maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, giaVe, loaiHinh, quocTich, maDVGhe, maGoiHanhLy, maSuatAn, maBaoHiem;
    int nguoiLon, treEm, emBe, tongSoKH;
    private Date ngayDi;
    double phiHanhly;
    private final JPanel mainPanel;
    private final String accId;
    private DatVeKhungGioForm datVeForm;
    private List<JPanel> khachHangPanels;
    
    public NhapThongTinVeKhungGio(String diemDi, String diemDen, Date ngayDi, int nguoiLon, int treEm, int emBe, JPanel mainJPanel, String accId) {
        this.mainPanel = mainJPanel;
        this.accId = accId;
        this.ngayDi = ngayDi;
        this.diemDen = diemDen;
        this.diemDi = diemDi;
        initComponents();
        int tongKhachHang = nguoiLon + treEm + emBe;
        DefaultTableModel model = (DefaultTableModel) bangThongTin.getModel();
        model.setRowCount(0);
        model.addRow(new Object[] {
            diemDi,
            diemDen,
            ngayDi,
            tongKhachHang
        });
        // Tạo panel nhập thông tin hành khách
        khachHangPanels = new ArrayList<>();
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        Font titleFont = new Font("UTM Centur", Font.BOLD, 18); // Font chữ lớn hơn và đậm
        if (nguoiLon > 0) {
        JLabel lblNguoiLon = new JLabel("Người lớn");
        lblNguoiLon.setFont(titleFont);
        lblNguoiLon.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblNguoiLon);
        contentPanel.add(Box.createVerticalStrut(5));  // Thêm khoảng cách

        for (int i = 1; i <= nguoiLon; i++) {
            JPanel khachHangPanel = taoKhachHangPanel(i,"Người lớn");
            contentPanel.add(khachHangPanel);
            khachHangPanels.add(khachHangPanel);
        }
    }

    // Tạo tiêu đề cho Trẻ em
    if (treEm > 0) {
        JLabel lblTreEm = new JLabel("Trẻ em");
        lblTreEm.setFont(titleFont);
        lblTreEm.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(10));  // Thêm khoảng cách trước tiêu đề
        contentPanel.add(lblTreEm);
        contentPanel.add(Box.createVerticalStrut(5));

        for (int i = 1; i <= treEm; i++) {
            JPanel khachHangPanel = taoKhachHangPanel(i, "Trẻ em");
            contentPanel.add(khachHangPanel);
            khachHangPanels.add(khachHangPanel);
        }
    }

    // Tạo tiêu đề cho Em bé
    if (emBe > 0) {
        JLabel lblEmBe = new JLabel("Em bé");
        lblEmBe.setFont(titleFont);
        lblEmBe.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblEmBe);
        contentPanel.add(Box.createVerticalStrut(5));

        for (int i = 1; i <= emBe; i++) {
            JPanel khachHangPanel = taoKhachHangPanel(i, "Em bé");
            contentPanel.add(khachHangPanel);
            khachHangPanels.add(khachHangPanel);
        }
    }
            
        NhapThongTin.setViewportView(contentPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
        NhapThongTin.revalidate();
        NhapThongTin.repaint();
        
        
        
        /*nhapSDT.addFocusListener(new FocusListener() {
            @Override 
            public void focusGained(FocusEvent e)
            {
                if (nhapSDT.getText().equals("Nhập số điện thoại"))
                {
                    nhapSDT.setText("");
                    nhapSDT.setForeground(Color.BLACK);
                }
            }
            
            @Override 
            public void focusLost(FocusEvent e)
            {
                if (nhapSDT.getText().isEmpty())
                {  
                    nhapSDT.setForeground(Color.BLACK);
                    nhapSDT.setText("Nhập số điện thoại");
                }
            }
            
        });*/
        
        
        tiepTuc.addActionListener(evt -> {
                layThongTinTuPanel();
                for (UserHanhKhach hk : danhSachHanhKhach) {
                 System.out.println(hk.toString());}
            try {
                System.out.println(this.diemDi);
                System.out.println(this.diemDen);
                setForm (new DangKiDVuKhungGio(this.diemDi, this.diemDen, ngayDi, nguoiLon, treEm, emBe, mainJPanel,accId, danhSachHanhKhach));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NhapThongTinVeKhungGio.class.getName()).log(Level.SEVERE, null, ex);
            }


        });
            
    }
    // tạo nội dung nhập cho từng hành khách
    private JPanel taoKhachHangPanel(int index, String loaiKhachHang)
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

        String[] gioiTinhOptions = {"Nam", "Nữ"};
        JComboBox<String> cbGioiTinh = new JComboBox<>(gioiTinhOptions);
        JTextField txtHo = new JTextField(10);
        JTextField txtTen = new JTextField(10);
        JTextField txtCCCD = new JTextField(15);
        JTextField txtSDT = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        // JDateChooser với định dạng "dd/MM/yyyy"
        JDateChooser dateChooserNgaySinh = new JDateChooser();
        dateChooserNgaySinh.setDateFormatString("dd/MM/yyyy");
        JDateChooser dateChooserNgayHetHanCCCD = new JDateChooser();
        dateChooserNgayHetHanCCCD.setDateFormatString("dd/MM/yyyy");
        JComboBox<String> cbQuocTich = new JComboBox<>(new String[] {
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua và Barbuda", "Ả Rập Xê Út",
            "Argentina", "Armenia", "Úc", "Áo", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados",
            "Belarus", "Bỉ", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia và Herzegovina", "Botswana",
            "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Campuchia", "Cameroon",
            "Canada", "Cộng hòa Trung Phi", "Chad", "Chile", "Trung Quốc", "Colombia", "Comoros", 
            "Congo (Cộng hòa)", "Congo (Cộng hòa Dân chủ)", "Costa Rica", "Croatia", "Cuba", "Síp", 
            "Cộng hòa Séc", "Đan Mạch", "Djibouti", "Dominica", "Cộng hòa Dominica", "Đông Timor", 
            "Ecuador", "Ai Cập", "El Salvador", "Guinea Xích Đạo", "Eritrea", "Estonia", "Eswatini", 
            "Ethiopia", "Fiji", "Phần Lan", "Pháp", "Gabon", "Gambia", "Georgia", "Đức", "Ghana", 
            "Hy Lạp", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", 
            "Hungary", "Iceland", "Ấn Độ", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Ý", 
            "Jamaica", "Nhật Bản", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Hàn Quốc", "Kuwait", 
            "Kyrgyzstan", "Lào", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", 
            "Litva", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", 
            "Quần đảo Marshall", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", 
            "Mông Cổ", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", 
            "Hà Lan", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Bắc Macedonia", "Na Uy", "Oman", 
            "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", 
            "Philippines", "Ba Lan", "Bồ Đào Nha", "Qatar", "Romania", "Nga", "Rwanda", "Saint Kitts và Nevis", 
            "Saint Lucia", "Saint Vincent và Grenadines", "Samoa", "San Marino", "São Tomé và Príncipe", 
            "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", 
            "Quần đảo Solomon", "Somalia", "Nam Phi", "Nam Sudan", "Tây Ban Nha", "Sri Lanka", "Sudan", 
            "Suriname", "Thụy Điển", "Thụy Sĩ", "Syria", "Đài Loan", "Tajikistan", "Tanzania", "Thái Lan", 
            "Togo", "Tonga", "Trinidad và Tobago", "Tunisia", "Thổ Nhĩ Kỳ", "Turkmenistan", "Tuvalu", 
            "Uganda", "Ukraina", "Các Tiểu vương quốc Ả Rập Thống nhất", "Vương quốc Anh", "Hoa Kỳ", 
            "Uruguay", "Uzbekistan", "Vanuatu", "Thành Vatican", "Venezuela", "Việt Nam", "Yemen", 
            "Zambia", "Zimbabwe"
        });

        cbQuocTich.setSelectedItem("Việt Nam");
        
        
        Font font = new Font("UTM Times", Font.PLAIN, 18);
        Font labelFont = new Font("UTM Times", Font.BOLD, 14);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(labelFont);  // Thay đổi font chữ cho nhãn
        panel.add(lblGioiTinh, gbc);
        gbc.gridx = 1;
        cbGioiTinh.setFont(font);  // Thay đổi font chữ cho combo box
        panel.add(cbGioiTinh, gbc);

        gbc.gridx = 2;
        JLabel lblHo = new JLabel("Họ:");
        lblHo.setFont(labelFont);
        panel.add(lblHo, gbc);
        gbc.gridx = 3;
        txtHo.setFont(font);  // Thay đổi font chữ cho text field
        panel.add(txtHo, gbc);

        gbc.gridx = 4;
        JLabel lblTen = new JLabel("Tên đệm và tên:");
        lblTen.setFont(labelFont);
        panel.add(lblTen, gbc);
        gbc.gridx = 5;
        txtTen.setFont(font);
        panel.add(txtTen, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(labelFont);
        panel.add(lblNgaySinh, gbc);
        gbc.gridx = 1;
        dateChooserNgaySinh.setFont(font);
        panel.add(dateChooserNgaySinh, gbc);
        if (loaiKhachHang.equals("Người lớn"))
        {
        gbc.gridx = 2;
        JLabel lblCCCD = new JLabel("CCCD:");
        lblCCCD.setFont(labelFont);
        panel.add(lblCCCD, gbc);
        gbc.gridx = 3;
        txtCCCD.setFont(font);
        panel.add(txtCCCD, gbc);
        
        gbc.gridx = 4;
        JLabel lblNgayHetHanCCCD = new JLabel("Ngày hết hạn CCCD:");
        lblNgayHetHanCCCD.setFont(labelFont);
        panel.add(lblNgayHetHanCCCD, gbc);
        gbc.gridx = 5;
        dateChooserNgayHetHanCCCD.setFont(font);
        panel.add(dateChooserNgayHetHanCCCD, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblQuocTich= new JLabel("Quốc tịch:");
        lblQuocTich.setFont(labelFont);
        panel.add(lblQuocTich, gbc);
        gbc.gridx = 1;
        cbQuocTich.setFont(font);
        panel.add(cbQuocTich, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        JLabel lblSDT= new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        panel.add(lblSDT, gbc);
        gbc.gridx = 3;
        txtSDT.setFont(font);
        panel.add(txtSDT, gbc);
        
        gbc.gridx = 4; gbc.gridy = 2;
        JLabel lblEmail= new JLabel("Email:");
        lblEmail.setFont(labelFont);
        panel.add(lblEmail, gbc);
        gbc.gridx = 5;
        txtEmail.setFont(font);
        panel.add(txtEmail, gbc);
        }
        else
        {
           gbc.gridx = 2;
           JLabel lblQuocTich= new JLabel("Quốc tịch");
            lblQuocTich.setFont(labelFont);
            panel.add(lblQuocTich, gbc);
            gbc.gridx = 3;
            cbQuocTich.setFont(font);
            panel.add(cbQuocTich, gbc);
        }

        // Thêm chiều rộng cho các JTextField để căn lề
        cbGioiTinh.setPreferredSize(new Dimension(120, 30));
        cbQuocTich.setPreferredSize(new Dimension(120, 30));
        txtHo.setPreferredSize(new Dimension(120, 30));
        txtTen.setPreferredSize(new Dimension(120, 30));
        txtCCCD.setPreferredSize(new Dimension(120, 30));
        txtSDT.setPreferredSize(new Dimension(120, 30));
        txtEmail.setPreferredSize(new Dimension(120, 30));
        dateChooserNgaySinh.setPreferredSize(new Dimension(120, 30));
        dateChooserNgayHetHanCCCD.setPreferredSize(new Dimension(120, 30));
        
        panel.putClientProperty("loaiKhachHang", loaiKhachHang);
        return panel;
    }
    
    private List<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();
    
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bangThongTin = new javax.swing.JTable();
        thanhTruotNhapTT = new javax.swing.JScrollPane();
        NhapThongTin = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        tiepTuc = new javax.swing.JButton();
        quayLai = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(76, 76, 76));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.png"))); // NOI18N
        jLabel3.setText("Tìm vé máy bay");

        jLabel4.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel4.setText("> Nhập thông tin");

        bangThongTin.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        bangThongTin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Điểm đi", "Điểm đến", "Ngày đi", "Số lượng hành khách"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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

        tiepTuc.setBackground(new java.awt.Color(248, 252, 37));
        tiepTuc.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        tiepTuc.setText("Tiếp tục");
        tiepTuc.setPreferredSize(new java.awt.Dimension(116, 37));
        tiepTuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tiepTucActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tiepTuc, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiepTuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quayLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(jLabel4))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(thanhTruotNhapTT, javax.swing.GroupLayout.PREFERRED_SIZE, 1193, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(thanhTruotNhapTT, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void tiepTucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiepTucActionPerformed
        
    }//GEN-LAST:event_tiepTucActionPerformed

    private void quayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quayLaiActionPerformed
        if (datVeForm == null){
        try {
            datVeForm = new DatVeKhungGioForm(mainPanel, accId);
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
    private javax.swing.JTable bangThongTin;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton quayLai;
    private javax.swing.JScrollPane thanhTruotNhapTT;
    private javax.swing.JButton tiepTuc;
    // End of variables declaration//GEN-END:variables
}
