
package View.TrangChu;

import View.DatVe.*;
import Process.HanhKhach.UserHanhKhach;
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


public class XemChiTietVe extends javax.swing.JPanel {

    private String maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, giaVe, loaiHinh;
    int nguoiLon, treEm, emBe, tongSoKH;
    private final JPanel mainPanel;
    private final String accId;
    private DatVeForm datVeForm;
    private List<JPanel> khachHangPanels;
    
    public XemChiTietVe(String maChuyen, String diemDi, String diemDen, String gioCatCanh, String gioHaCanh, int nguoiLon, int treEm, int emBe, String giaVe, JPanel mainJPanel, String loaiHinh, String accId) {
        this.mainPanel = mainJPanel;
        this.accId = accId;
        initComponents();
        int tongKhachHang = nguoiLon + treEm + emBe;
        DefaultTableModel model = (DefaultTableModel) bangThongTin.getModel();
        model.setRowCount(0);
        model.addRow(new Object[] {
            maChuyen,
            diemDi,
            diemDen,
            gioCatCanh,
            gioHaCanh,
            giaVe,
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
        
        
        // Tạo focus nhập thông tin người liên hệ
        nhapHo.addFocusListener(new FocusListener() {
            @Override 
            public void focusGained(FocusEvent e)
            {
                if (nhapHo.getText().equals("Nhập họ"))
                {
                    nhapHo.setText("");
                    nhapHo.setForeground(Color.BLACK);
                }
            }
            
            @Override 
            public void focusLost(FocusEvent e)
            {
                if (nhapHo.getText().isEmpty())
                {  
                    nhapHo.setForeground(Color.BLACK);
                    nhapHo.setText("Nhập họ");
                }
            }
            
        });
        
        
        nhapEmail.addFocusListener(new FocusListener() {
            @Override 
            public void focusGained(FocusEvent e)
            {
                if (nhapEmail.getText().equals("Nhập email liên hệ"))
                {
                    nhapEmail.setText("");
                    nhapEmail.setForeground(Color.BLACK);
                }
            }
            
            @Override 
            public void focusLost(FocusEvent e)
            {
                if (nhapEmail.getText().isEmpty())
                {  
                    nhapEmail.setForeground(Color.BLACK);
                    nhapEmail.setText("Nhập email liên hệ");
                }
            }
            
        });
        
        nhapSDT.addFocusListener(new FocusListener() {
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
            
        });
        
        
        tiepTuc.addActionListener(evt -> {
            try {
                layThongTinTuPanel();
                for (UserHanhKhach hk : danhSachHanhKhach) {
                 System.out.println(hk.toString());}
                setForm (new DangKiDVu(maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, nguoiLon, treEm, emBe, giaVe, mainJPanel, loaiHinh, accId, danhSachHanhKhach));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(XemChiTietVe.class.getName()).log(Level.SEVERE, null, ex);
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
        // JDateChooser với định dạng "dd/MM/yyyy"
        JDateChooser dateChooserNgaySinh = new JDateChooser();
        dateChooserNgaySinh.setDateFormatString("dd/MM/yyyy");
        JDateChooser dateChooserNgayHetHanCCCD = new JDateChooser();
        dateChooserNgayHetHanCCCD.setDateFormatString("dd/MM/yyyy");
        JComboBox<String> cbHanhLy = new JComboBox<>(new String[] {"Không", "10kg", "20kg", "30kg"});
        
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
        JLabel lblHanhLy = new JLabel("Hành lý ký gửi:");
        lblHanhLy.setFont(labelFont);
        panel.add(lblHanhLy, gbc);
        gbc.gridx = 1;
        cbHanhLy.setFont(font);
        panel.add(cbHanhLy, gbc);
        }
        else
        {
           gbc.gridx = 2;
            JLabel lblHanhLy = new JLabel("Hành lý ký gửi:");
            lblHanhLy.setFont(labelFont);
            panel.add(lblHanhLy, gbc);
            gbc.gridx = 3;
            cbHanhLy.setFont(font);
            panel.add(cbHanhLy, gbc); 
        }

        // Thêm chiều rộng cho các JTextField để căn lề
        txtHo.setPreferredSize(new Dimension(150, 30));
        txtTen.setPreferredSize(new Dimension(150, 30));
        txtCCCD.setPreferredSize(new Dimension(150, 30));
        dateChooserNgaySinh.setPreferredSize(new Dimension(150, 30));
        dateChooserNgayHetHanCCCD.setPreferredSize(new Dimension(150, 30));
        
        panel.putClientProperty("loaiKhachHang", loaiKhachHang);
        return panel;
    }
    
    private List<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();
    
    public void layThongTinTuPanel() {
    danhSachHanhKhach.clear(); // Đảm bảo danh sách trống trước khi lưu mới

    for (JPanel panel : khachHangPanels) {
        String loaiKhachHang = ((TitledBorder) panel.getBorder()).getTitle().contains("Người lớn") ? "Người lớn" :
                               ((TitledBorder) panel.getBorder()).getTitle().contains("Trẻ em") ? "Trẻ em" : "Em bé";

        Component[] components = panel.getComponents();

        String gioiTinh = "", ho = "", ten = "", cccd = "", hanhLy = "", viTriGhe = "";
        Date ngaySinh = null, ngayHetHanCCCD = null;
        double tienGhe = 0;

        for (Component comp : components) {
            if (comp instanceof JComboBox) {
                JComboBox cb = (JComboBox) comp;
                if (cb.getItemCount() == 2) gioiTinh = (String) cb.getSelectedItem();  // Nam/Nữ
                else hanhLy = (String) cb.getSelectedItem();                            // Hành lý
            } else if (comp instanceof JTextField) {
                JTextField txt = (JTextField) comp;
                if (txt.getColumns() == 10 && ho.isEmpty()) ho = txt.getText();
                else if (txt.getColumns() == 10) ten = txt.getText();
                else cccd = txt.getText();
            } else if (comp instanceof JDateChooser) {
                JDateChooser chooser = (JDateChooser) comp;
                if (ngaySinh == null) ngaySinh = chooser.getDate();
                else ngayHetHanCCCD = chooser.getDate();
            }
        }

        // Với trẻ em/em bé, CCCD và ngày hết hạn có thể null
//
//            cccd = "";
//            ngayHetHanCCCD = null;
 //       }

        UserHanhKhach hk = new UserHanhKhach(loaiKhachHang, gioiTinh, ho, ten, ngaySinh, cccd, ngayHetHanCCCD, hanhLy, viTriGhe, tienGhe);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        nhapHo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        nhapSDT = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        nhapEmail = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        tiepTuc = new javax.swing.JButton();
        quayLai = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        giaVeBay = new javax.swing.JLabel();
        diemDiDen = new javax.swing.JLabel();
        NoiDung = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        tongGiaVe = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        lblThuePhi = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        tienDatCho = new javax.swing.JLabel();
        tienHanhly = new javax.swing.JLabel();
        tienSuatAn = new javax.swing.JLabel();
        tienBaoHiem = new javax.swing.JLabel();
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

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, null));

        jLabel5.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        jLabel5.setText("Thông tin liên hệ");

        nhapHo.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        nhapHo.setText("Nhập họ");

        jLabel7.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel7.setText("Họ và tên");

        jLabel8.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel8.setText("Điện thoại");

        nhapSDT.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        nhapSDT.setText("Nhập số điện thoại");

        jLabel9.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel9.setText("Email");

        nhapEmail.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        nhapEmail.setText("Nhập email liên hệ");
        nhapEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapEmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nhapEmail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nhapSDT)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nhapHo)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(69, 69, 69))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhapHo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhapSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel9)
                .addGap(10, 10, 10)
                .addComponent(nhapEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        jComboBox1.setBackground(new java.awt.Color(229, 229, 229));
        jComboBox1.setEditable(true);
        jComboBox1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thông tin hành khách" }));

        jPanel11.setBackground(new java.awt.Color(174, 226, 250));

        jLabel19.setFont(new java.awt.Font("UTM Nyala", 0, 20)); // NOI18N
        jLabel19.setText("Chuyến đi");

        giaVeBay.setFont(new java.awt.Font("UTM Nyala", 1, 25)); // NOI18N
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
                .addComponent(giaVeBay)
                .addGap(47, 47, 47))
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

        NoiDung.setFont(new java.awt.Font("UTM Times", 0, 14)); // NOI18N
        NoiDung.setText("T5,  24/04/2025 | 06:00 - 08:20 | M1234 | Eco");

        jLabel23.setBackground(new java.awt.Color(229, 229, 229));
        jLabel23.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel23.setText("Giá vé");

        tongGiaVe.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        tongGiaVe.setText("jLabel29");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tongGiaVe, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tongGiaVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel33.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel33.setText("jLabel29");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        tienDatCho.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienDatCho.setForeground(new java.awt.Color(51, 51, 51));
        tienDatCho.setText("Phí đặt chỗ");

        tienHanhly.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienHanhly.setForeground(new java.awt.Color(51, 51, 51));
        tienHanhly.setText("Phí hành lý");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(NoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(116, 116, 116))
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tienHanhly, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tienDatCho, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tienSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tienBaoHiem, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(NoiDung)
                .addGap(10, 10, 10)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienDatCho, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tienSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienHanhly, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tienBaoHiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(thanhTruotNhapTT)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(thanhTruotNhapTT, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nhapEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nhapEmailActionPerformed

    private void tiepTucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiepTucActionPerformed
        
    }//GEN-LAST:event_tiepTucActionPerformed

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
    private javax.swing.JLabel diemDiDen;
    private javax.swing.JLabel giaVeBay;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblThuePhi;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTextField nhapEmail;
    private javax.swing.JTextField nhapHo;
    private javax.swing.JTextField nhapSDT;
    private javax.swing.JButton quayLai;
    private javax.swing.JScrollPane thanhTruotNhapTT;
    private javax.swing.JLabel tienBaoHiem;
    private javax.swing.JLabel tienDatCho;
    private javax.swing.JLabel tienHanhly;
    private javax.swing.JLabel tienSuatAn;
    private javax.swing.JButton tiepTuc;
    private javax.swing.JLabel tongGiaVe;
    // End of variables declaration//GEN-END:variables
}
