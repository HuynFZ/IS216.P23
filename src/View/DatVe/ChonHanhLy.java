
package View.DatVe;

import Process.HanhKhach.ChonChoCallBack;
import Model.HanhKhach.UserHanhKhach;
import Process.HanhLy.ChonHanhLyCallBack;
import Model.HanhLy.HanhLyModel;
import Process.HanhLy.HanhlyDS;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.events.MouseEvent;

public class ChonHanhLy extends javax.swing.JFrame {

    private String maChuyen, diemDi, diemDen;
    int tongSoKH;
    private List<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();
    private ArrayList<HanhLyModel> danhSachHanhLy = new ArrayList<>();
    private int rowDangChon = -1;
    private ChonHanhLyCallBack callback;

    public ChonHanhLy(String maChuyen, String diemDi, String diemDen, int tongSoKH, List<UserHanhKhach> danhSachHanhKhach) throws ClassNotFoundException, SQLException {
        initComponents();
        this.danhSachHanhKhach = danhSachHanhKhach;
        
        danhSachHanhLy = HanhlyDS.layDanhSachGoiHanhLy();
        DefaultTableModel model = (DefaultTableModel) bangHanhLy.getModel();
        model.setRowCount(0);
        for (HanhLyModel hl : danhSachHanhLy) {
            model.addRow(new Object[]{
                hl.getMaHanhLy(),
                hl.getTenGoiHL(),
                hl.getTrongLuongMax(),    // Phí tiêu tốn chưa xác định
                hl.getPhiGoiHanhly()// Ghế ngồi chưa chọn
            });
        }
        
        noiDung.setText("Chuyến bay " + maChuyen + " " + diemDi + " - " + diemDen);
        
        DefaultTableModel modelhk = (DefaultTableModel) bangHanhKhach.getModel();
        modelhk.setRowCount(0);
        for (UserHanhKhach hk : danhSachHanhKhach) {
            modelhk.addRow(new Object[]{
                hk.getHoTen(),
                hk.getMaGoiHanhLy(),
                HanhlyDS.layTenGoiHanhLy(hk.getMaGoiHanhLy()),
                hk.getPhiHanhly()
            });
        }
        
        bangHanhKhach.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = bangHanhKhach.getSelectedRow();
                if (selectedRow != -1) {
                    rowDangChon = selectedRow;
                    String tenHanhKhach = (String) bangHanhKhach.getValueAt(selectedRow,0);
                    System.out.println(tenHanhKhach);
                    noiDung1.setText("Hành khách đang chọn: " + tenHanhKhach);
                }
            }
        }); 

        bangHanhKhach.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // tránh gọi nhiều lần trong quá trình thay đổi selection
                    bangHanhLy.clearSelection();
                }
            }
        });

       bangHanhLy.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int rowHanhKhach = bangHanhKhach.getSelectedRow();
                int rowGoiHanhLy = bangHanhLy.getSelectedRow();

                // Kiểm tra đã chọn hành khách chưa
                if (rowHanhKhach == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn hành khách trước khi chọn gói hành lý!");
                    bangHanhLy.clearSelection();
                    return;
                }

                // Kiểm tra đã chọn gói hành lý chưa
                if (rowGoiHanhLy == -1) {
                    return; // Không làm gì nếu chưa chọn gói hành lý
                }

                // Lấy dữ liệu gói hành lý từ bảng hành lý
                String maGoi = bangHanhLy.getValueAt(rowGoiHanhLy, 0).toString().trim();
                String tenGoi = bangHanhLy.getValueAt(rowGoiHanhLy, 1).toString().trim();
                String phi = bangHanhLy.getValueAt(rowGoiHanhLy, 3).toString().trim();

                // Lấy dữ liệu gói hành lý hiện tại của hành khách
                Object val = bangHanhKhach.getValueAt(rowHanhKhach, 1);
                String maGoiHK = (val != null) ? val.toString().trim() : "";

                System.out.println("DEBUG: maGoiHK = [" + maGoiHK + "]");
                System.out.println("DEBUG: maGoi = [" + maGoi + "]");

                if (maGoiHK.isEmpty()) {
                    // Khách chưa có gói hành lý, cập nhật ngay mà không hỏi
                    bangHanhKhach.setValueAt(maGoi, rowHanhKhach, 1);
                    bangHanhKhach.setValueAt(tenGoi, rowHanhKhach, 2);
                    bangHanhKhach.setValueAt(phi, rowHanhKhach, 3);
                    System.out.println("DEBUG: Cập nhật gói hành lý mới cho khách chưa có gói");
                    return;
                }

                if (maGoiHK.equals(maGoi)) {
                    // Khách chọn lại đúng gói hiện tại, không làm gì
                    System.out.println("DEBUG: Khách chọn lại gói hành lý hiện tại, bỏ qua");
                    return;
                }

                // Khách đã có gói hành lý khác, hỏi xác nhận đổi
                int result = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có muốn đổi sang gói hành lý này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    bangHanhKhach.setValueAt(maGoi, rowHanhKhach, 1);
                    bangHanhKhach.setValueAt(tenGoi, rowHanhKhach, 2);
                    bangHanhKhach.setValueAt(phi, rowHanhKhach, 3);
                    System.out.println("DEBUG: Khách đã đổi sang gói hành lý mới");
                } else {
                    System.out.println("DEBUG: Khách không đổi gói hành lý");
                    // Nếu không đổi, có thể clear selection gói hành lý để tránh nhầm lẫn
                    bangHanhLy.clearSelection();
                }
            }
        });



        
        bangHanhLy.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rowGoiHanhLy = bangHanhLy.getSelectedRow();
                int rowHanhKhach = bangHanhKhach.getSelectedRow();

                if (rowHanhKhach == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn hành khách trước khi chọn gói hành lý!");
                    return;
                }

                if (rowGoiHanhLy != -1) {
                    String maGoi = bangHanhLy.getValueAt(rowGoiHanhLy, 0).toString();
                    String tenGoi = bangHanhLy.getValueAt(rowGoiHanhLy, 1).toString();
                    String phi = bangHanhLy.getValueAt(rowGoiHanhLy, 3).toString();

                    String maGoiHK = bangHanhKhach.getValueAt(rowHanhKhach, 1) != null
                        ? bangHanhKhach.getValueAt(rowHanhKhach, 1).toString()
                        : "";

                    if (maGoiHK.equals(maGoi)) {
                        return;
                    }

                    int result = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có muốn đổi sang gói hành lý này?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        bangHanhKhach.setValueAt(maGoi, rowHanhKhach, 1);
                        bangHanhKhach.setValueAt(tenGoi, rowHanhKhach, 2);
                        bangHanhKhach.setValueAt(phi, rowHanhKhach, 3);
                    }
                }
            }
        });


        
        btnXacNhanChonCho.addActionListener(e -> {
            DefaultTableModel model1 = (DefaultTableModel) bangHanhKhach.getModel();
            for (int i = 0; i < model1.getRowCount(); i++) {
                String maGoiHanhLy = (String) model1.getValueAt(i, 1); // Cột 1: mã gói
                Object phiObj = model1.getValueAt(i, 3);               // Cột 3: phí hành lý

                double phiHanhly = 0;
                if (phiObj instanceof Number) {
                    phiHanhly = ((Number) phiObj).doubleValue();
                } else {
                    try {
                        phiHanhly = Double.parseDouble(phiObj.toString());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Phí hành lý không hợp lệ ở hàng " + (i + 1));
                        continue;
                    }
                }

                danhSachHanhKhach.get(i).setMaGoiHanhLy(maGoiHanhLy);
                danhSachHanhKhach.get(i).setPhiHanhly(phiHanhly);
            }

            // Gọi callback để trả dữ liệu về
            if (callback != null) {
                callback.capNhatDanhSach(danhSachHanhKhach);
            }

            dispose(); // Đóng cửa sổ chọn chỗ
        });
        
        btnHuyChonHL.addActionListener(e -> {
            dispose();
        });

        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bangHanhKhach = new javax.swing.JTable();
        noiDung = new javax.swing.JLabel();
        noiDung1 = new javax.swing.JLabel();
        noiDung2 = new javax.swing.JLabel();
        btnXacNhanChonCho = new javax.swing.JButton();
        btnHuyChonHL = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        bangHanhLy = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(102, 51, 255));

        jLabel1.setBackground(new java.awt.Color(0, 255, 255));
        jLabel1.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đăng kí hành lý thêm");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        bangHanhKhach.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        bangHanhKhach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hành khách", "Mã gói hành lý", "Gói hành lý", "Phí hành lý"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bangHanhKhach.setRowHeight(35);
        jScrollPane1.setViewportView(bangHanhKhach);

        noiDung.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        noiDung.setText("Chuyến bay VJ811 HAN - DAD");

        noiDung1.setFont(new java.awt.Font("UTM Centur", 0, 16)); // NOI18N
        noiDung1.setText("Hành khách đang chọn:");

        noiDung2.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        noiDung2.setText("Danh sách gói hành lý");

        btnXacNhanChonCho.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        btnXacNhanChonCho.setText("Xác nhận đăng kí hành lý");
        btnXacNhanChonCho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanChonChoActionPerformed(evt);
            }
        });

        btnHuyChonHL.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        btnHuyChonHL.setText("Hủy chọn hành lý");
        btnHuyChonHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyChonHLActionPerformed(evt);
            }
        });

        bangHanhLy.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        bangHanhLy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã gói hành lý", "Gói hành lý", "Trọng lượng tối đa", "Phí hành lý"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bangHanhLy.setRowHeight(35);
        jScrollPane2.setViewportView(bangHanhLy);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(251, 251, 251)
                .addComponent(btnHuyChonHL, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(151, 151, 151)
                .addComponent(btnXacNhanChonCho)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(noiDung1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noiDung)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noiDung2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noiDung)
                    .addComponent(noiDung2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(36, 36, 36)
                .addComponent(noiDung1)
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuyChonHL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhanChonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
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

    private void btnHuyChonHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyChonHLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHuyChonHLActionPerformed

    private void btnXacNhanChonChoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanChonChoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXacNhanChonChoActionPerformed

/*
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChonHanhLy().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable bangHanhKhach;
    private javax.swing.JTable bangHanhLy;
    private javax.swing.JButton btnHuyChonHL;
    private javax.swing.JButton btnXacNhanChonCho;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel noiDung;
    private javax.swing.JLabel noiDung1;
    private javax.swing.JLabel noiDung2;
    // End of variables declaration//GEN-END:variables
}
