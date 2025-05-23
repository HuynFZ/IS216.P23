
package View.DatVe;

import ConnectDB.ConnectionUtils;
import Process.ChiaTime;
import Process.HanhKhach.UserHanhKhach;
import Process.KhuyenMai.KhuyenMaiDS;
import Process.KhuyenMai.KhuyenMaiModel;
import Process.SanBay.TimTinhTP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.events.MouseEvent;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JPanel;

public class ThanhToanForm extends javax.swing.JPanel {

    private String maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, giaVe, loaiHinh, maKhuyenMai;
    int nguoiLon, treEm, emBe, tongSoKH;
    private double phiChoNgoi = 0;
    private double phiHanhLy = 0;
    private double phiSuatAn = 0;
    private double phiBaoHiem = 0;
    private double tongPhiDichVu = 0;
    private double tongTien = 0;
    private double tienGiam = 0;
    private double tongTienDiemTV = 0;
    private double tongTienSauCung = 0;
    private final JPanel mainPanel;
    private final String accId;
    private static List<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();
    private static ArrayList<KhuyenMaiModel> danhSachKhuyenMai = new ArrayList<>();
   
    public ThanhToanForm(String maChuyen, String diemDi, String diemDen, String gioCatCanh, String gioHaCanh, int nguoiLon, int treEm, int emBe, String giaVe, JPanel mainJPanel, String loaiHinh, int tongSoKH, double tongGV, double thuePhi, double tongTien, String accId, List<UserHanhKhach> danhSachHanhKhach, double phiChoNgoi, double phiHanhLy, double phiSuatAn, double phiBaoHiem, double tongPhiDichVu) throws SQLException, ClassNotFoundException {
        initComponents();
        this.mainPanel = mainJPanel;
        this.tongTien = tongTien - tongPhiDichVu;
        this.accId = accId;
        this.tongSoKH = tongSoKH;
        String tenThanhPhoDi = TimTinhTP.timTinhTP(diemDi);
        String tenThanhPhoDen = TimTinhTP.timTinhTP(diemDen);
        diemDiDen.setText(tenThanhPhoDi + " - " + tenThanhPhoDen );
        
        String[] chiaTimeDi = ChiaTime.tachTime(gioCatCanh);
        String[] chiaTimeDen = ChiaTime.tachTime(gioHaCanh);
        System.out.println(chiaTimeDi[0] + " | " + chiaTimeDen[0] );
        
        if (chiaTimeDi[0].equals(chiaTimeDen[0])) 
        {
            NoiDung.setText(chiaTimeDi[0] + " | " + chiaTimeDi[1] + " - " + chiaTimeDen[1] + " | " + maChuyen);
        }
        
        giaVeBay.setText(giaVe + " VND");
        String giaVeRaw = giaVe.replace(",", "").replace(" VND", "").trim();
        
        // Khởi tạo format cho tiền tệ Việt Nam
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        String giaVeFormatted = nf.format(tongGV) + " VND";
        String thuePhiFormatted = nf.format(thuePhi) + " VND";
        String tongTienFormatted = nf.format(tongTien) + " VND";

        tongGiaVe.setText(giaVeFormatted);

        lblThuePhi.setText(thuePhiFormatted);

        lblTongTien.setText(tongTienFormatted);
        
        lblDichVu.setText(tongPhiDichVu + " VND");
        lblDatCho.setText(phiChoNgoi + " VND");
        lblHanhLy.setText(phiHanhLy + " VND");
        lblSuatAn.setText(phiSuatAn + " VND");
        lblBaoHiem.setText(phiBaoHiem + " VND");
        
        
        getDiemThanhVien();
        timKhuyenMai();
        

        bangKhuyenMai.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                
                int selectedRow = bangKhuyenMai.getSelectedRow();
                if (selectedRow >= 0) {
                    maKhuyenMai = danhSachKhuyenMai.get(selectedRow).getMaKhuyenMai();

                    tienGiam = tinhTienGiamGia(maKhuyenMai, tongTien);
                    double tongTienSauGiam = tongTien - tienGiam - tongTienDiemTV;

                    if (tongTienSauGiam < 0) tongTienSauGiam = 0; // Không để âm

                    lblGiamGia.setText(tienGiam + " VND");
                    lblTongTien.setText(tongTienSauGiam + " VND");
                }
            }
        });

        btnHuyKhuyenMai.addActionListener((ActionEvent e) -> {
            // 1. Bỏ chọn dòng trong bảng
            bangKhuyenMai.clearSelection();
            
            // 3. Cập nhật lại tổng tiền ban đầu (chưa áp dụng giảm giá)
            lblTongTien.setText(String.format("%.0f", tongTien));
            
            lblGiamGia.setText("0 VND");
            lblDiemTV.setText("0 VND");
        });

        btnXacNhanDiem.addActionListener((ActionEvent e) -> {
            tongTienDiemTV = 0;
            double diem = Double.parseDouble(txtDiem.getText());
            tongTienDiemTV = diem * 100;
            double tongMoi = tongTien - tongTienDiemTV - tienGiam;
            lblDiemTV.setText(tongTienDiemTV + " VND");
            lblTongTien.setText(tongMoi + " VND");
        });



        btnThanhToanNgay.addActionListener(e -> {
            theTinDung.setVisible(true);
            nganHangPanel.setVisible(true);
            moMoPanel.setVisible(true);
        });
        
        btnGiuMa.addActionListener(e ->{
            theTinDung.setVisible(false);
            nganHangPanel.setVisible(false);
            moMoPanel.setVisible(false);
        });
        
        btnTraSau.addActionListener(e -> {
            theTinDung.setVisible(false);
            nganHangPanel.setVisible(false);
            moMoPanel.setVisible(false);
        });
        
        lblTheTinDung.addMouseListener(new java.awt .event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                tongTienSauCung = tongTien - tongTienDiemTV - tienGiam;
                System.out.println(tongTien + "----------------------------------");
                System.out.println(tongTienDiemTV + "----------------------------------");
                System.out.println(tienGiam + "----------------------------------");
                System.out.println(tongTienSauCung + "----------------------------------");
                System.out.println(maKhuyenMai + "----------------------------------");
                String diemThuong = txtDiem.getText();
                new TheTinDung(maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, nguoiLon, treEm, emBe, giaVe, loaiHinh, tongSoKH, tongGV, thuePhi, tongTienSauCung, danhSachHanhKhach, maKhuyenMai, accId, diemThuong).setVisible(true);
            }
        });
        
        nganHang.addMouseListener(new java.awt .event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                new ThanhToanNganHang(maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, nguoiLon, treEm, emBe, giaVe, loaiHinh, tongSoKH, tongGV, thuePhi, tongTien, danhSachHanhKhach, maKhuyenMai).setVisible(true);
            }
        });
        
        viMoMo.addMouseListener(new java.awt .event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                new ThanhToanMoMo(maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh, nguoiLon, treEm, emBe, giaVe, loaiHinh, tongSoKH, tongGV, thuePhi, tongTien, danhSachHanhKhach, maKhuyenMai).setVisible(true);
            }
        });
    }

    public double tinhTienGiamGia(String maKhuyenMai, double tongTien) {
        double giaTriGiam = 0;

        for (KhuyenMaiModel km : danhSachKhuyenMai) {
            if (km.getMaKhuyenMai().equals(maKhuyenMai)) {
                giaTriGiam = km.getGiaTri(); // % giảm
                break;
            }
        }

        return tongTien * giaTriGiam / 100.0;
    }

    
    private void getDiemThanhVien()
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionUtils.getMyConnection(); // Hàm kết nối DB của bạn

            // SQL: Join để lấy điểm thành viên từ accId
            String sql = "SELECT KH.DIEMTHUONG " +
                         "FROM KHACH_HANG KH " +
                         "JOIN ACCOUNT A ON A.MAKHACHHANG = KH.MAKHACHHANG " +
                         "WHERE A.ACCOUNT_ID = ?";

            System.out.println(sql);
            System.out.println(accId);
            ps = con.prepareStatement(sql);
            ps.setString(1, accId); // accId là biến thành viên được truyền từ constructor

            rs = ps.executeQuery();

            if (rs.next()) {
                int diem = rs.getInt("DIEMTHUONG");
                System.out.println("Điểm thành viên: " + diem);
                lblDiem.setText(String.valueOf(diem));
              
            } else {
                System.out.println("Không tìm thấy điểm thành viên!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy điểm thành viên!");
        }
    }

    private void timKhuyenMai() throws SQLException, ClassNotFoundException
    {
     
        danhSachKhuyenMai = KhuyenMaiDS.layDanhSachKhuyenMai();
        DefaultTableModel model = (DefaultTableModel) bangKhuyenMai.getModel();
        model.setRowCount(0);

        
        for (KhuyenMaiModel km : danhSachKhuyenMai)
        {
        model.addRow(new Object[] {
            km.getTenKhuyenMai(),
            km.getGiaTri(), // Format số có dấu phẩy ngăn cách
            km.getNgayBatDau(),
            km.getNgayKetThuc(),
            km.getDieuKienKM()
        });
        }
                
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        lblDichVu = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        tienDatCho = new javax.swing.JLabel();
        tienHanhLy = new javax.swing.JLabel();
        tienSuatAn = new javax.swing.JLabel();
        lblBaoHiem = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        lblGiamGia = new javax.swing.JLabel();
        lblDatCho = new javax.swing.JLabel();
        lblSuatAn = new javax.swing.JLabel();
        lblHanhLy = new javax.swing.JLabel();
        tienBaoHiem1 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        lblDiemTV = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bangKhuyenMai = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblDiem = new javax.swing.JLabel();
        btnXacNhanDiem = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtDiem = new javax.swing.JTextField();
        btnHuyKhuyenMai = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnThanhToanNgay = new javax.swing.JRadioButton();
        theTinDung = new javax.swing.JPanel();
        lblTheTinDung = new javax.swing.JLabel();
        nganHangPanel = new javax.swing.JPanel();
        nganHang = new javax.swing.JLabel();
        moMoPanel = new javax.swing.JPanel();
        viMoMo = new javax.swing.JLabel();
        btnGiuMa = new javax.swing.JRadioButton();
        btnTraSau = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));

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
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tongGiaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
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
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblThuePhi, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
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

        lblDichVu.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblDichVu.setText("jLabel29");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblDichVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        tienHanhLy.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienHanhLy.setForeground(new java.awt.Color(51, 51, 51));
        tienHanhLy.setText("Phí hành lý");

        tienSuatAn.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienSuatAn.setForeground(new java.awt.Color(51, 51, 51));
        tienSuatAn.setText("Phí suất ăn");

        lblBaoHiem.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        lblBaoHiem.setForeground(new java.awt.Color(51, 51, 51));
        lblBaoHiem.setText("Phí bảo hiểm");

        jLabel34.setBackground(new java.awt.Color(229, 229, 229));
        jLabel34.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel34.setText("Giảm giá");

        lblGiamGia.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblGiamGia.setText("0 VND");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lblDatCho.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        lblDatCho.setForeground(new java.awt.Color(51, 51, 51));
        lblDatCho.setText("Phí đặt chỗ");

        lblSuatAn.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        lblSuatAn.setForeground(new java.awt.Color(51, 51, 51));
        lblSuatAn.setText("Phí suất ăn");

        lblHanhLy.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        lblHanhLy.setForeground(new java.awt.Color(51, 51, 51));
        lblHanhLy.setText("Phí hành lý");

        tienBaoHiem1.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienBaoHiem1.setForeground(new java.awt.Color(51, 51, 51));
        tienBaoHiem1.setText("Phí bảo hiểm");

        jLabel35.setBackground(new java.awt.Color(229, 229, 229));
        jLabel35.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel35.setText("Điểm thành viên");

        lblDiemTV.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblDiemTV.setText("0 VND");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiemTV, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblDiemTV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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
                        .addGap(17, 17, 17)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tienDatCho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tienHanhLy, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDatCho, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tienSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tienBaoHiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblBaoHiem, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                    .addComponent(lblSuatAn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(22, 22, 22))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(NoiDung)
                .addGap(20, 20, 20)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienDatCho, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tienSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDatCho, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBaoHiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tienBaoHiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jLabel3.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(76, 76, 76));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.png"))); // NOI18N
        jLabel3.setText("Tìm vé máy bay");

        jLabel4.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(76, 76, 76));
        jLabel4.setText("> Nhập thông tin");

        jLabel5.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(76, 76, 76));
        jLabel5.setText("> Chọn dịch vụ");

        jLabel6.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel6.setText("> Thanh toán");

        jPanel2.setBackground(new java.awt.Color(174, 226, 250));

        jLabel2.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel2.setText("Mã khuyến mãi hoặc điểm thành viên");

        bangKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên khuyến mãi", "Giá trị", "Ngày bắt đầu", "Ngày kết thúc", "Điều kiện khuyến mãi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(bangKhuyenMai);

        jLabel1.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        jLabel1.setText("Điểm thành viên bạn có: ");

        lblDiem.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        lblDiem.setForeground(new java.awt.Color(255, 0, 51));
        lblDiem.setText("jLabel7");

        btnXacNhanDiem.setBackground(new java.awt.Color(51, 255, 0));
        btnXacNhanDiem.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        btnXacNhanDiem.setText("Xác nhận sử dụng điểm");
        btnXacNhanDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanDiemActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        jLabel8.setText("Số lượng điểm muốn sử dụng");

        btnHuyKhuyenMai.setBackground(new java.awt.Color(255, 255, 51));
        btnHuyKhuyenMai.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        btnHuyKhuyenMai.setText("Hủy khuyến mãi");
        btnHuyKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyKhuyenMaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXacNhanDiem)
                        .addGap(18, 18, 18)
                        .addComponent(btnHuyKhuyenMai))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblDiem))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHuyKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXacNhanDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))))
        );

        jButton8.setBackground(new java.awt.Color(255, 255, 0));
        jButton8.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jButton8.setText("Quay lại");
        jButton8.setPreferredSize(new java.awt.Dimension(116, 37));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        jLabel9.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel9.setText("Phương thức thanh toán");

        buttonGroup1.add(btnThanhToanNgay);
        btnThanhToanNgay.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        btnThanhToanNgay.setSelected(true);
        btnThanhToanNgay.setText("Thanh toán ngay");
        btnThanhToanNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanNgayActionPerformed(evt);
            }
        });

        theTinDung.setBackground(new java.awt.Color(255, 255, 204));

        lblTheTinDung.setBackground(new java.awt.Color(255, 255, 204));
        lblTheTinDung.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTheTinDung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/atm-card.png"))); // NOI18N
        lblTheTinDung.setText("Thẻ tín dụng");

        javax.swing.GroupLayout theTinDungLayout = new javax.swing.GroupLayout(theTinDung);
        theTinDung.setLayout(theTinDungLayout);
        theTinDungLayout.setHorizontalGroup(
            theTinDungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(theTinDungLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTheTinDung, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        theTinDungLayout.setVerticalGroup(
            theTinDungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTheTinDung, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        nganHangPanel.setBackground(new java.awt.Color(255, 255, 204));

        nganHang.setBackground(new java.awt.Color(153, 153, 0));
        nganHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        nganHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/qr-code.png"))); // NOI18N
        nganHang.setText("Thanh toán ngân hàng");

        javax.swing.GroupLayout nganHangPanelLayout = new javax.swing.GroupLayout(nganHangPanel);
        nganHangPanel.setLayout(nganHangPanelLayout);
        nganHangPanelLayout.setHorizontalGroup(
            nganHangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nganHangPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nganHang)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        nganHangPanelLayout.setVerticalGroup(
            nganHangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nganHangPanelLayout.createSequentialGroup()
                .addComponent(nganHang, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        moMoPanel.setBackground(new java.awt.Color(255, 255, 204));

        viMoMo.setBackground(new java.awt.Color(255, 255, 204));
        viMoMo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        viMoMo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/momo.png"))); // NOI18N
        viMoMo.setText("Ví momo");

        javax.swing.GroupLayout moMoPanelLayout = new javax.swing.GroupLayout(moMoPanel);
        moMoPanel.setLayout(moMoPanelLayout);
        moMoPanelLayout.setHorizontalGroup(
            moMoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moMoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(viMoMo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        moMoPanelLayout.setVerticalGroup(
            moMoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(viMoMo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        buttonGroup1.add(btnGiuMa);
        btnGiuMa.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        btnGiuMa.setText("Giữ mã đặt chỗ & thanh toán sau");
        btnGiuMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGiuMaActionPerformed(evt);
            }
        });

        buttonGroup1.add(btnTraSau);
        btnTraSau.setFont(new java.awt.Font("UTM Times", 1, 16)); // NOI18N
        btnTraSau.setText("Thanh toán trả sau");
        btnTraSau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraSauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnThanhToanNgay)
                                    .addComponent(btnTraSau)
                                    .addComponent(btnGiuMa, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(theTinDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(nganHangPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(moMoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThanhToanNgay)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(theTinDung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(moMoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nganHangPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(btnGiuMa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTraSau)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 716, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnXacNhanDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanDiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXacNhanDiemActionPerformed

    private void btnThanhToanNgayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanNgayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThanhToanNgayActionPerformed

    private void btnGiuMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGiuMaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGiuMaActionPerformed

    private void btnTraSauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraSauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTraSauActionPerformed

    private void btnHuyKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHuyKhuyenMaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NoiDung;
    private javax.swing.JTable bangKhuyenMai;
    private javax.swing.JRadioButton btnGiuMa;
    private javax.swing.JButton btnHuyKhuyenMai;
    private javax.swing.JRadioButton btnThanhToanNgay;
    private javax.swing.JRadioButton btnTraSau;
    private javax.swing.JButton btnXacNhanDiem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel diemDiDen;
    private javax.swing.JLabel giaVeBay;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBaoHiem;
    private javax.swing.JLabel lblDatCho;
    private javax.swing.JLabel lblDichVu;
    private javax.swing.JLabel lblDiem;
    private javax.swing.JLabel lblDiemTV;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblHanhLy;
    private javax.swing.JLabel lblSuatAn;
    private javax.swing.JLabel lblTheTinDung;
    private javax.swing.JLabel lblThuePhi;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel moMoPanel;
    private javax.swing.JLabel nganHang;
    private javax.swing.JPanel nganHangPanel;
    private javax.swing.JPanel theTinDung;
    private javax.swing.JLabel tienBaoHiem1;
    private javax.swing.JLabel tienDatCho;
    private javax.swing.JLabel tienHanhLy;
    private javax.swing.JLabel tienSuatAn;
    private javax.swing.JLabel tongGiaVe;
    private javax.swing.JTextField txtDiem;
    private javax.swing.JLabel viMoMo;
    // End of variables declaration//GEN-END:variables
}
