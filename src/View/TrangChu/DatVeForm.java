
package View.TrangChu;

import ConnectDB.ConnectionUtils;
import Process.SanBay.SanBayList;
import View.DatVe.NhapThongTinVe;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class DatVeForm extends javax.swing.JPanel {

    private JPopupMenu goiYSanBay = new JPopupMenu();
    private JPopupMenu goiYSanBay1 = new JPopupMenu();
    private final JPanel mainPanel;
    private final String accId;
    private final Map<String,String> danhSachSanBay;
            
    public DatVeForm(JPanel mainPanel, String accId) throws ClassNotFoundException, SQLException {
        this.mainPanel = mainPanel;
        this.accId = accId;
        this.danhSachSanBay = SanBayList.layDanhSachSanBay();
        initComponents();
        // ẩn đi lịch khứ hồi 
        NgayVe.setVisible(false);
        lichNgayVe.setVisible(false);
        addAutoSuggestToTextFieldDiemDi();
        addAutoSuggestToTextFieldDiemDen();
        
        // Bắt sự kiện click chuột trong bảng
        bangChuyenBay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e ) 
                {
                    int selectedRow = bangChuyenBay.getSelectedRow();
                    if (selectedRow != -1)
                    {
                       String maChuyen = bangChuyenBay.getValueAt(selectedRow, 0).toString();
                       String diemDi = bangChuyenBay.getValueAt(selectedRow, 1).toString();
                       String diemDen = bangChuyenBay.getValueAt(selectedRow, 2).toString();
                       String gioCatCanh = bangChuyenBay.getValueAt(selectedRow, 3).toString();
                       String gioHaCanh = bangChuyenBay.getValueAt(selectedRow, 4).toString();
                       String giaCoBan = bangChuyenBay.getValueAt(selectedRow, 5).toString();    
                       
                       noiDungVe.setText("Chuyến bay đã chọn: " + maChuyen + " - " + diemDi + " - " + diemDen + " - " + gioCatCanh + " - " + gioHaCanh + " - " + giaCoBan);
                    }
                    xacNhanDatVe.setVisible(true);
                    xacNhanDatVe.addActionListener(evt -> {
                        int selectedRow2 = bangChuyenBay.getSelectedRow();
                        if (selectedRow2 != -1)
                        {
                            String maChuyen = bangChuyenBay.getValueAt(selectedRow, 0).toString();
                            String diemDi = bangChuyenBay.getValueAt(selectedRow, 1).toString();
                            String diemDen = bangChuyenBay.getValueAt(selectedRow, 2).toString();
                            String gioCatCanh = bangChuyenBay.getValueAt(selectedRow, 3).toString();
                            String gioHaCanh = bangChuyenBay.getValueAt(selectedRow, 4).toString();
                            String giaCoBan = bangChuyenBay.getValueAt(selectedRow, 5).toString();   
                            String loaiHinh = "";
                            
                            if (MotChieu.isSelected()) 
                            {
                                loaiHinh = "một chiều";
                            }
                            else 
                            {
                                loaiHinh = "khứ hồi";
                            }
                       
                            int nguoiLon = (int) soNguoiLon.getValue();
                            int treEm = (int) soTreEm.getValue();
                            int emBe = (int) soEmBe.getValue();
                            
                            NhapThongTinVe nhapThongTinVe = new NhapThongTinVe(maChuyen,diemDi,diemDen,gioCatCanh,gioHaCanh,nguoiLon, treEm, emBe, giaCoBan, mainPanel,loaiHinh, accId);
                            setForm (nhapThongTinVe);
                        }
                    });
                }
        });
        
        btnVeKhungGio.addActionListener(evt -> {
            try {
                setForm (new DatVeKhungGioForm(mainPanel, accId));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatVeForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DatVeForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        MotChieu = new javax.swing.JRadioButton();
        KhuHoi = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        DiemDiText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        diemDenText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        NgayVe = new javax.swing.JLabel();
        lichNgayDi = new com.toedter.calendar.JDateChooser();
        lichNgayVe = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        timChuyenBayButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        soNguoiLon = new javax.swing.JSpinner();
        soTreEm = new javax.swing.JSpinner();
        soEmBe = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        bangChuyenBay = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        xacNhanDatVe = new javax.swing.JButton();
        noiDungVe = new javax.swing.JLabel();
        btnVeKhungGio = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Mở cánh cửa khám phá cùng Mel Airlines");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mel Airlines - Đặt một bước nhỏ, vươn tới đỉnh mây");

        buttonGroup1.add(MotChieu);
        MotChieu.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        MotChieu.setSelected(true);
        MotChieu.setText("Một chiều");
        MotChieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MotChieuActionPerformed(evt);
            }
        });

        buttonGroup1.add(KhuHoi);
        KhuHoi.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        KhuHoi.setText("Khứ hồi");
        KhuHoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KhuHoiActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel5.setText("Điểm đi");

        DiemDiText.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        DiemDiText.setActionCommand("null");
        DiemDiText.setName("abf"); // NOI18N
        DiemDiText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiemDiTextActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel6.setText("Điểm đến");

        diemDenText.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        diemDenText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diemDenTextActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel7.setText("Ngày đi");

        NgayVe.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        NgayVe.setText("Ngày về");

        lichNgayDi.setAlignmentX(3.0F);
        lichNgayDi.setAlignmentY(10.0F);
        lichNgayDi.setDateFormatString("dd/MM/yyyy");
        lichNgayDi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        lichNgayVe.setDateFormatString("dd/MM/yyyy");
        lichNgayVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lichNgayVe.setMinSelectableDate(new java.util.Date(-62135791099000L));

        jLabel9.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel9.setText("Người lớn");

        jLabel10.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel10.setText("Trẻ em");

        jLabel11.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel11.setText("Em bé");

        timChuyenBayButton.setBackground(new java.awt.Color(11, 214, 205));
        timChuyenBayButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        timChuyenBayButton.setText("Tìm chuyến bay");
        timChuyenBayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timChuyenBayButtonActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.png"))); // NOI18N
        jLabel12.setText("Tìm vé máy bay");

        soNguoiLon.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        soNguoiLon.setModel(new javax.swing.SpinnerNumberModel(1, 0, 5, 1));
        soNguoiLon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        soTreEm.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        soTreEm.setModel(new javax.swing.SpinnerNumberModel(0, 0, 3, 1));
        soTreEm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        soEmBe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        soEmBe.setModel(new javax.swing.SpinnerNumberModel(0, 0, 2, 1));
        soEmBe.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel14.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        jLabel14.setText("Hạng vé");

        jComboBox1.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phổ thông", "Phổ thông đặc biệt", "Thương gia", "Hạng sang" }));

        bangChuyenBay.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        bangChuyenBay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã chuyến", "Điểm đi", "Điểm đến", "Giờ khởi hành", "Giờ đến", "Giá cơ bản"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bangChuyenBay.setRowHeight(50);
        jScrollPane2.setViewportView(bangChuyenBay);

        xacNhanDatVe.setBackground(new java.awt.Color(248, 252, 37));
        xacNhanDatVe.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        xacNhanDatVe.setText("Đặt vé");
        xacNhanDatVe.setPreferredSize(new java.awt.Dimension(116, 37));
        xacNhanDatVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xacNhanDatVeActionPerformed(evt);
            }
        });

        noiDungVe.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N

        btnVeKhungGio.setBackground(new java.awt.Color(255, 153, 255));
        btnVeKhungGio.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        btnVeKhungGio.setText("Vé khung giờ");
        btnVeKhungGio.setPreferredSize(new java.awt.Dimension(116, 37));
        btnVeKhungGio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVeKhungGioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnVeKhungGio, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noiDungVe, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xacNhanDatVe, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVeKhungGio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(noiDungVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xacNhanDatVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1054, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel12))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(53, 53, 53)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(MotChieu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(71, 71, 71)
                                    .addComponent(KhuHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(DiemDiText, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lichNgayDi, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(soNguoiLon, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(71, 71, 71)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(soTreEm, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(71, 71, 71)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(soEmBe, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(25, 25, 25)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lichNgayVe, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(diemDenText, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(NgayVe, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addGap(62, 62, 62)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(49, 49, 49)
                                                    .addComponent(timChuyenBayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))))))))
                .addContainerGap(90, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MotChieu)
                    .addComponent(KhuHoi))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DiemDiText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diemDenText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(NgayVe))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lichNgayDi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lichNgayVe, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(soNguoiLon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(soTreEm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(soEmBe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(timChuyenBayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void MotChieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MotChieuActionPerformed
        NgayVe.setVisible(false);
        lichNgayVe.setVisible(false);
    }//GEN-LAST:event_MotChieuActionPerformed

    private void KhuHoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KhuHoiActionPerformed
        NgayVe.setVisible(true);
        lichNgayVe.setVisible(true);
    }//GEN-LAST:event_KhuHoiActionPerformed

    private void DiemDiTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiemDiTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiemDiTextActionPerformed

    private void diemDenTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diemDenTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_diemDenTextActionPerformed

    private void timChuyenBayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timChuyenBayButtonActionPerformed
        Date ngayHienTai = new Date();
        Date ngayDi = lichNgayDi.getDate();
        Date ngayVe = lichNgayVe.getDate();
        
        String diemDi = DiemDiText.getToolTipText();
        String diemDen = diemDenText.getToolTipText();
        int nguoiLon = (int) soNguoiLon.getValue();
        int treEm = (int) soTreEm.getValue();
        int emBe = (int) soEmBe.getValue();
        int tongSoVe = nguoiLon + treEm + emBe;
        
        
        if (ngayDi == null) 
        {
            JOptionPane.showMessageDialog(null,"Vui lòng chọn ngày đi");
            return;
        }
        if (ngayDi.before(ngayHienTai)) {
            JOptionPane.showMessageDialog(null, "Ngày đi phải sau ngày hiện tại.");
            return;
        }
        
        if (KhuHoi.isSelected()) {
            if (ngayVe == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày về.");
                return;
            }
        
            if (!ngayVe.after(ngayDi)) {
                JOptionPane.showMessageDialog(null, "Ngày về phải sau ngày đi.");
                return;
            }
        }
        
        try {
            timChuyenBay(diemDi, diemDen, ngayDi, ngayVe, tongSoVe);
        } catch (SQLException ex) {
            Logger.getLogger(DatVeForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatVeForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_timChuyenBayButtonActionPerformed

    private void xacNhanDatVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xacNhanDatVeActionPerformed
        
    }//GEN-LAST:event_xacNhanDatVeActionPerformed

    private void btnVeKhungGioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVeKhungGioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVeKhungGioActionPerformed
    
    private void setForm (JComponent com)
    {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    
      public void timChuyenBay (String diemDi, String diemDen,Date ngayDi, Date ngayVe, int tongSoVe) throws SQLException, ClassNotFoundException, ClassNotFoundException
      {
          DefaultTableModel model = (DefaultTableModel) bangChuyenBay.getModel();
          model.setRowCount(0);
          Connection con = null;
          con = (Connection) ConnectionUtils.getMyConnection();
          String query = "SELECT CB.MACHUYENBAY, TB.SANBAYDI, TB.SANBAYDEN, CB.GIOCATCANH"
                          + ", CB.GIOHACANH, CB.GIAVE, CB.SOGHETRONG" 
                          + " FROM CHUYEN_BAY CB JOIN TUYEN_BAY TB ON CB.MATUYENBAY = TB.MATUYENBAY"
                          + " WHERE LOWER(TB.SANBAYDI) = LOWER (?) "
                          + " AND LOWER (TB.SANBAYDEN) = LOWER (?) "
                          + " AND TRUNC(CB.GIOCATCANH) = TO_DATE(?, 'DD/MM/YYYY') "
                          + " AND CB.SOGHETRONG >= ?";
           System.out.print(query);
           System.out.println("Điểm đi: " + diemDi);
            System.out.println("Điểm đến: " + diemDen);
            System.out.println("Ngày đi: " + ngayDi);
            System.out.println("Ngày về: " + ngayVe);
            System.out.println("Tổng số vé: " + tongSoVe);

          try 
          {
              PreparedStatement psDi = con.prepareStatement(query);
              SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
              String ngayDiStr = dateFormat.format(ngayDi);
              
              psDi.setString(1, diemDi);
              psDi.setString(2, diemDen);
              psDi.setString(3, ngayDiStr);
              psDi.setInt(4, tongSoVe);
              
              ResultSet rsDi = psDi.executeQuery();
              
              while (rsDi.next())
              {
                  model.addRow(new Object[]
                          {
                              rsDi.getString("MACHUYENBAY"),
                              rsDi.getString("SANBAYDI"),
                              rsDi.getString("SANBAYDEN"),
                              rsDi.getTimestamp("GIOCATCANH").toLocalDateTime(),
                              rsDi.getTimestamp("GIOHACANH").toLocalDateTime(),
                              String.format("%,.0f", rsDi.getDouble("GIAVE"))
                          });
              }
              
              if (ngayVe != null)
              {
                  
                  PreparedStatement psVe = con.prepareStatement(query);
                  //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                  String ngayVeStr = dateFormat.format(ngayVe);
                  psVe.setString(1, diemDen);
                  psVe.setString(2, diemDi);
                  psVe.setString(3,ngayVeStr);
                  psVe.setInt(4, tongSoVe);
                  
                  ResultSet rsVe = psVe.executeQuery();
                  
                  while (rsVe.next())
                  {
                      model.addRow(new Object[]
                          {
                              rsVe.getString("MACHUYENBAY"),
                              rsVe.getString("SANBAYDI"),
                              rsVe.getString("SANBAYDEN"),
                              rsVe.getTimestamp("GIOCATCANH").toLocalDateTime(),
                              rsVe.getTimestamp("GIOHACANH").toLocalDateTime(),
                              String.format("%,.0f", rsVe.getDouble("GIAVE"))
                              
                          });
                  }
              }
              
              if (model.getRowCount() == 0)
              {
                  JOptionPane.showMessageDialog(null,"Không tìm thấy chuyến bay phù hợp.");
              }
          }
          catch (SQLException e) 
                  {
                      e.printStackTrace();
                      JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
                  }
      }




    // Thêm gợi ý sân bay
    private void addAutoSuggestToTextFieldDiemDi()
    {
       DiemDiText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
       {
        private void capNhatGoiY() {
            goiYSanBay.setVisible(false);
            goiYSanBay.removeAll();
            String input = DiemDiText.getText().toLowerCase().trim();
        
            if (!input.isEmpty()) {
                boolean found = false;
                for (Map.Entry<String,String> entry : danhSachSanBay.entrySet()) {
                    String maSanBay = entry.getKey();
                    String goiY = entry.getValue();
                    // Kiểm tra nếu tên sân bay chứa input của người dùng
                    if (goiY.toLowerCase().contains(input)) {
                        JMenuItem menuItem = new JMenuItem(goiY);
                        menuItem.addActionListener(e -> {
                            DiemDiText.setText(goiY);
                            DiemDiText.setToolTipText(maSanBay);
                            SwingUtilities.invokeLater(() -> goiYSanBay.setVisible(false));
                        });
                        goiYSanBay.add(menuItem);
                        found = true;
                    }
                }
        
                if (found) {
                    SwingUtilities.invokeLater(() -> {
                        goiYSanBay.revalidate();
                        goiYSanBay.repaint();
                        goiYSanBay.show(DiemDiText, 0, DiemDiText.getHeight());  // Hiển thị menu khi có gợi ý
                        DiemDiText.requestFocusInWindow();
                    });
                } else {
                    goiYSanBay.setVisible(false);  // Ẩn menu nếu không có gợi ý nào
                }
            } else {
                goiYSanBay.setVisible(false);  // Ẩn menu nếu ô nhập rỗng
            }
        }
        
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        });  
    }
    
    private void addAutoSuggestToTextFieldDiemDen()
    {
       diemDenText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
       {
        private void capNhatGoiY() {
            goiYSanBay1.setVisible(false);
            goiYSanBay1.removeAll();
            String input = diemDenText.getText().toLowerCase().trim();
        
            if (!input.isEmpty()) {
                boolean found = false;
                for (Map.Entry<String,String> entry : danhSachSanBay.entrySet()) {
                    String maSanBay = entry.getKey();
                    String goiY = entry.getValue();
                    // Kiểm tra nếu tên sân bay chứa input của người dùng
                    if (goiY.toLowerCase().contains(input)) {
                        JMenuItem menuItem = new JMenuItem(goiY);
                        menuItem.addActionListener(e -> {
                            diemDenText.setText(goiY);
                            diemDenText.setToolTipText(maSanBay);
                            SwingUtilities.invokeLater(() -> goiYSanBay1.setVisible(false));
                        });
                        goiYSanBay1.add(menuItem);
                        found = true;
                    }
                }
        
                if (found) {
                    SwingUtilities.invokeLater(() -> {
                        goiYSanBay1.revalidate();
                        goiYSanBay1.repaint();
                        goiYSanBay1.show(diemDenText, 0, diemDenText.getHeight());  // Hiển thị menu khi có gợi ý
                        diemDenText.requestFocusInWindow();
                    });
                } else {
                    goiYSanBay1.setVisible(false);  // Ẩn menu nếu không có gợi ý nào
                }
            } else {
                goiYSanBay1.setVisible(false);  // Ẩn menu nếu ô nhập rỗng
            }
        }
        
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            capNhatGoiY();  // Cập nhật gợi ý khi có sự thay đổi trong văn bản
        }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DiemDiText;
    private javax.swing.JRadioButton KhuHoi;
    private javax.swing.JRadioButton MotChieu;
    private javax.swing.JLabel NgayVe;
    private javax.swing.JTable bangChuyenBay;
    private javax.swing.JButton btnVeKhungGio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField diemDenText;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser lichNgayDi;
    private com.toedter.calendar.JDateChooser lichNgayVe;
    private javax.swing.JLabel noiDungVe;
    private javax.swing.JSpinner soEmBe;
    private javax.swing.JSpinner soNguoiLon;
    private javax.swing.JSpinner soTreEm;
    private javax.swing.JButton timChuyenBayButton;
    private javax.swing.JButton xacNhanDatVe;
    // End of variables declaration//GEN-END:variables
}
