
package View.DatVe;

import Process.ChiaTime;
import Process.HanhKhach.ThemHanhKhach;
import Model.HanhKhach.UserHanhKhach;
import Process.KhachHang.LayEmailKH;
import Process.KhachHang.LayMaKH;
import Process.KhachHang.TruDiemKhachHang;
import Process.SanBay.TimTinhTP;
import Process.ThanhToan.ThemThanhToan;
import Process.VeMayBay.ThemVeMayBay;
import View.TrangChu.VeCuaToiForm;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ThanhToanNganHang extends javax.swing.JFrame {

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

    public ThanhToanNganHang(JPanel mainJPanel, String maChuyen, String diemDi, String diemDen, String gioCatCanh, String gioHaCanh, int nguoiLon, int treEm, int emBe, String giaVe, String loaiHinh, int tongSoKH, double tongGV, double thuePhi, double tongTienSauCung, List<UserHanhKhach> danhSachHanhKhach, String maKhuyenMai, String accId, String diemThuong, double tongDiemT, double tienGiam, double tongTien, double tongPhiDV, double phiChoNgoi, double phiHanhLy, double phiBaoHiem, double phiSuatAn) throws ClassNotFoundException {
        initComponents();
        
        this.tongTienSauCung = tongTienSauCung;
        this.tongTien = tongTien;
        this.tongPhiDichVu = tongPhiDV;
        this.phiChoNgoi = phiChoNgoi;
        this.phiHanhLy = phiHanhLy;
        this.phiBaoHiem = phiBaoHiem;
        this.phiSuatAn = phiSuatAn;
        this.mainPanel = mainJPanel;
        this.diemThuong = diemThuong;
        
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
        
        for (UserHanhKhach hk : danhSachHanhKhach)
        {
            cbThongTinHK.addItem(hk.getHo() + " " + hk.getTen());
        }
        
        giaVeBay.setText(giaVe + " VND");
        String giaVeRaw = giaVe.replace(",", "").replace(" VND", "").trim();
        
        // Khởi tạo format cho tiền tệ Việt Nam
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        String giaVeFormatted = nf.format(tongGV) + " VND";
        String thuePhiFormatted = nf.format(thuePhi) + " VND";
        String tongTienFormatted = nf.format(tongTienSauCung) + " VND";

        tongGiaVe.setText(giaVeFormatted);

        lblThuePhi.setText(thuePhiFormatted);

        lblTongTien.setText(tongTienFormatted);
        
        lblDiemTV.setText(tongDiemT + " VND");
        
        lblGiamGia.setText(tienGiam + "VND");
        
        tienDV.setText(tongPhiDichVu + " VND");
        tienDatCho.setText("Phí đặt chỗ: " + phiChoNgoi + " VND");
        tienHanhLy.setText("Phí hành lý: " + phiHanhLy + " VND");
        tienSuatAn.setText("Phí suất ăn: " + phiSuatAn + " VND");
        tienBaoHiem.setText("Phí bảo hiểm: " + phiBaoHiem + " VND");
        
        
        btnHuyTT.addActionListener(e -> {
            dispose();
        });
        
        btnXacNhanTT.addActionListener(e -> {
            String maKhachHang = null;
            String emailKH = null;

            try {
                maKhachHang = LayMaKH.layMaKHTuAccount(accId);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDung.class.getName()).log(Level.SEVERE, null, ex);
                return; // thoát nếu lỗi
            }

            if (maKhachHang == null || maKhachHang.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy mã khách hàng!");
                return;
            }

            try {
                 maVe = ThemVeMayBay.themVeMayBay(maChuyen, maKhachHang, tongTien, "Vé xác định", "Đã thanh toán");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDung.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(TheTinDung.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Lỗi khi thêm hành khách!");
                    return;
                }
            }

            if (this.diemThuong == null || this.diemThuong.trim().isEmpty()) {
            this.diemThuong = "0"; // hoặc hiển thị cảnh báo cho người dùng
            }   
            
            double diemThuongDouble = Double.parseDouble(this.diemThuong);
            try {
                
                 TruDiemKhachHang.truDiem(maKhachHang, diemThuongDouble);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDung.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lỗi khi trừ điểm!");
                return;
            }
            
            
            try {
                
                 ThemThanhToan.themThanhToan(maVe, maKhuyenMai, tongTien,"Ví điện tử","Đang xử lý");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TheTinDung.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm thanh toán!");
                return;
            }
            
            JOptionPane.showMessageDialog(null,"Bạn đã thanh toán thành công");
            
            try {
                emailKH = LayEmailKH.layEmailKHTuAccount(accId);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ThanhToanNganHang.class.getName()).log(Level.SEVERE, null, ex);
                return; // thoát nếu lỗi
            }

            if (emailKH == null || maKhachHang.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy mã khách hàng!");
                return;
            }
            String filePart = null;
            filePart = exportVeMayBayToPDF(maChuyen, diemDi, diemDen, gioCatCanh, gioHaCanh,loaiHinh,danhSachHanhKhach);
                        boolean checkem = false;
            EmailService mailsender = new EmailService();
            checkem = mailsender.sendEmailWithPdfAttachment(emailKH, "Vé khách hàng đã thanh toán",
                                    "<h3>Xin chào,</h3><p>Vé máy bay của bạn được gửi kèm file PDF bên dưới.</p>",
                                    filePart);
                dispose();
                
            try {
                setForm (new VeCuaToiForm(mainPanel, accId));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThanhToanNganHang.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ThanhToanNganHang.class.getName()).log(Level.SEVERE, null, ex);
            }
            });

    }

        private static String safe(Object o) {   // thêm hàm này trong class
        return o == null ? "" : o.toString();
        }


        private String exportVeMayBayToPDF(String maChuyen, String diemDi, String diemDen, 
                                      String gioCatCanh, String gioHaCanh, String loaiHinh, 
                                      List<UserHanhKhach> danhSachHanhKhach) {
        // Tạo tên file theo thời gian
        String suggestedFileName = "VeMayBay_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        // Lấy thư mục tạm của hệ thống
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + suggestedFileName;

        try (PdfWriter writer = new PdfWriter(filePath)) {
            PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document document =
                    new com.itextpdf.layout.Document(pdf, PageSize.A5.rotate());
            document.setMargins(20, 20, 20, 20);

            PdfFont font = PdfFontFactory.createFont("fonts/times.ttf", PdfEncodings.IDENTITY_H);

            document.add(new Paragraph("VÉ MÁY BAY ĐIỆN TỬ")
                    .setFont(font).setFontSize(18).setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(
                    "Ngày tạo vé: " +
                            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()))
                    .setFont(font).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            /* ---------- Thông tin chuyến bay ---------- */
            Table info = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            info.addCell(new Cell().add(new Paragraph("Mã chuyến:").setFont(font).setBold()));
            info.addCell(new Cell().add(new Paragraph(safe(maChuyen)).setFont(font)));

            info.addCell(new Cell().add(new Paragraph("Điểm đi - đến:").setFont(font).setBold()));
            info.addCell(new Cell().add(new Paragraph(
                    safe(diemDi) + " - " + safe(diemDen)).setFont(font)));

            info.addCell(new Cell().add(new Paragraph("Giờ cất cánh:").setFont(font).setBold()));
            info.addCell(new Cell().add(new Paragraph(safe(gioCatCanh)).setFont(font)));

            info.addCell(new Cell().add(new Paragraph("Giờ hạ cánh:").setFont(font).setBold()));
            info.addCell(new Cell().add(new Paragraph(safe(gioHaCanh)).setFont(font)));

            info.addCell(new Cell().add(new Paragraph("Loại hình:").setFont(font).setBold()));
            info.addCell(new Cell().add(new Paragraph(safe(loaiHinh)).setFont(font)));

            info.addCell(new Cell().add(new Paragraph("Mã vé:").setFont(font).setBold()));
            info.addCell(new Cell().add(new Paragraph(safe(maVe)).setFont(font)));

            document.add(info);
            document.add(new Paragraph("\n"));

            /* ---------- Danh sách hành khách ---------- */
            document.add(new Paragraph("DANH SÁCH HÀNH KHÁCH")
                    .setFont(font).setBold().setTextAlignment(TextAlignment.CENTER));

            Table hkTable = new Table(UnitValue.createPercentArray(
                    new float[]{5, 25, 10, 15, 25, 20})).useAllAvailableWidth();

            for (String h : new String[]{"#", "Họ tên", "GT", "Ngày sinh", "Số CCCD", "SĐT"})
                hkTable.addHeaderCell(new Cell().add(new Paragraph(h).setFont(font).setBold()));

            int stt = 1;
            for (UserHanhKhach hk : danhSachHanhKhach) {
                hkTable.addCell(new Cell().add(new Paragraph(String.valueOf(stt++)).setFont(font)));
                hkTable.addCell(new Cell().add(new Paragraph(
                        safe(hk.getHo()) + " " + safe(hk.getTen())).setFont(font)));
                hkTable.addCell(new Cell().add(new Paragraph(safe(hk.getGioiTinh())).setFont(font)));
                hkTable.addCell(new Cell().add(new Paragraph(
                        new SimpleDateFormat("dd/MM/yyyy")
                            .format(hk.getNgaySinh())).setFont(font)));
                hkTable.addCell(new Cell().add(new Paragraph(safe(hk.getCccd())).setFont(font)));
                hkTable.addCell(new Cell().add(new Paragraph(safe(hk.getSoDienThoai())).setFont(font)));
            }
            document.add(hkTable);
            document.add(new Paragraph("\n"));

            /* ---------- Chi phí ---------- */
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            Table cost = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            cost.addCell(new Cell().add(new Paragraph("Tổng giá vé").setFont(font)));
            cost.addCell(new Cell().add(new Paragraph(nf.format(tongTien) + " VND").setFont(font)));

            cost.addCell(new Cell().add(new Paragraph("Tổng phí dịch vụ").setFont(font)));
            cost.addCell(new Cell().add(new Paragraph(nf.format(tongPhiDichVu) + " VND").setFont(font)));

            cost.addCell(new Cell().add(new Paragraph("Tổng tiền sau cùng").setFont(font).setBold()));
            cost.addCell(new Cell().add(new Paragraph(nf.format(tongTienSauCung) + " VND")
                    .setFont(font).setBold()));
            document.add(cost);

            document.add(new Paragraph("\nCảm ơn quý khách đã sử dụng dịch vụ!\n")
                    .setFont(font).setTextAlignment(TextAlignment.CENTER));

            document.close();

            // Có thể in log hoặc show dialog nếu cần
            System.out.println("Đã xuất vé máy bay PDF tại: " + filePath);

            return filePath;  // Trả về đường dẫn file PDF

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Lỗi khi xuất file PDF: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
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
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        cbThongTinHK = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        giaVeBay = new javax.swing.JLabel();
        diemDiDen = new javax.swing.JLabel();
        NoiDung = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        tongGiaVe = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        lblThuePhi = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        tienDV = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        tienDatCho = new javax.swing.JLabel();
        tienHanhLy = new javax.swing.JLabel();
        tienSuatAn = new javax.swing.JLabel();
        tienBaoHiem = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        lblGiamGia = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        lblDiemTV = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnXacNhanTT = new javax.swing.JButton();
        btnHuyTT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, null));

        jPanel12.setBackground(new java.awt.Color(213, 0, 0));

        jLabel19.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("THÔNG TIN ĐẶT CHỖ");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel19)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        cbThongTinHK.setBackground(new java.awt.Color(229, 229, 229));
        cbThongTinHK.setEditable(true);
        cbThongTinHK.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cbThongTinHK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thông tin hành khách" }));

        jPanel13.setBackground(new java.awt.Color(174, 226, 250));

        jLabel20.setFont(new java.awt.Font("UTM Nyala", 0, 20)); // NOI18N
        jLabel20.setText("Chuyến đi");

        giaVeBay.setFont(new java.awt.Font("UTM Nyala", 1, 22)); // NOI18N
        giaVeBay.setForeground(new java.awt.Color(255, 0, 51));
        giaVeBay.setText("2.074.000 VND");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(giaVeBay, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tongGiaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tongGiaVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel30.setBackground(new java.awt.Color(229, 229, 229));
        jLabel30.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel30.setText("Thuế, phí");

        lblThuePhi.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblThuePhi.setText("jLabel29");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblThuePhi, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblThuePhi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel32.setBackground(new java.awt.Color(229, 229, 229));
        jLabel32.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel32.setText("Dịch vụ");

        tienDV.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        tienDV.setText("jLabel29");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tienDV, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tienDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel17.setBackground(new java.awt.Color(213, 0, 0));

        jLabel24.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Tổng tiền");

        lblTongTien.setFont(new java.awt.Font("UTM Times", 1, 24)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 255, 255));
        lblTongTien.setText("2.074.000 VND");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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

        tienBaoHiem.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        tienBaoHiem.setForeground(new java.awt.Color(51, 51, 51));
        tienBaoHiem.setText("Phí bảo hiểm");

        jLabel34.setBackground(new java.awt.Color(229, 229, 229));
        jLabel34.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel34.setText("Giảm giá");

        lblGiamGia.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblGiamGia.setText("jLabel29");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel36.setBackground(new java.awt.Color(229, 229, 229));
        jLabel36.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel36.setText("Điểm thành viên");

        lblDiemTV.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        lblDiemTV.setText("0 VND");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiemTV, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblDiemTV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tienHanhLy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                    .addComponent(tienDatCho, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tienSuatAn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tienBaoHiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(cbThongTinHK, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(cbThongTinHK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(diemDiDen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(NoiDung)
                .addGap(20, 20, 20)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienDatCho, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tienSuatAn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tienBaoHiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 204));

        jLabel21.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("THANH TOÁN TRỰC TUYẾN NGÂN HÀNG");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel21)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ttnganhang.jpg"))); // NOI18N

        btnXacNhanTT.setBackground(new java.awt.Color(0, 255, 0));
        btnXacNhanTT.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        btnXacNhanTT.setText("Xác nhận thanh toán");
        btnXacNhanTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanTTActionPerformed(evt);
            }
        });

        btnHuyTT.setBackground(new java.awt.Color(255, 255, 0));
        btnHuyTT.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        btnHuyTT.setText("Hủy thanh toán");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnHuyTT, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXacNhanTT)
                        .addGap(27, 27, 27))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuyTT, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhanTT, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
//            java.util.logging.Logger.getLogger(ThanhToanNganHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ThanhToanNganHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ThanhToanNganHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ThanhToanNganHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ThanhToanNganHang().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NoiDung;
    private javax.swing.JButton btnHuyTT;
    private javax.swing.JButton btnXacNhanTT;
    private javax.swing.JComboBox<String> cbThongTinHK;
    private javax.swing.JLabel diemDiDen;
    private javax.swing.JLabel giaVeBay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblDiemTV;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblThuePhi;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel tienBaoHiem;
    private javax.swing.JLabel tienDV;
    private javax.swing.JLabel tienDatCho;
    private javax.swing.JLabel tienHanhLy;
    private javax.swing.JLabel tienSuatAn;
    private javax.swing.JLabel tongGiaVe;
    // End of variables declaration//GEN-END:variables
}
