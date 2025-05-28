/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View.Admin;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

import javax.swing.SwingUtilities;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;            
import java.util.Map;
import java.util.HashMap; 
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;

import java.awt.Dimension;

/**
 *
 * @author Huy Nguyen
 */
public class BaoCaoLamViecForm extends javax.swing.JPanel {
    /**
     * Creates new form QLChuyenBayForm
     */
    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String DB_USER = "hanghk";
    private final String DB_PASSWORD = "Admin123";
    private final String[] tatCaCacThangModel = new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12", "Tất cả các tháng" };
    
    class DisabledMonthRenderer extends DefaultListCellRenderer {
    private JComboBox<String> quyComboBoxRef; 
    public DisabledMonthRenderer(JComboBox<String> quyComboBoxRef) {
        this.quyComboBoxRef = quyComboBoxRef;
        setOpaque(true); 
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        String monthString = (String) value;
        if (monthString == null) {
            return c; 
        }

        String selectedQuyString = (String) quyComboBoxRef.getSelectedItem();
        boolean itemShouldBeEnabled = true; 

        if (selectedQuyString != null && !selectedQuyString.equals("Tất cả các quý")) {
            if (!monthString.equals("Tất cả các tháng")) { 
                try {
                    int monthNumber = Integer.parseInt(monthString.replace("Tháng ", ""));
                    int quarterNumber = Integer.parseInt(selectedQuyString.replace("Quý ", ""));
                    itemShouldBeEnabled = isMonthInQuarter(monthNumber, quarterNumber);
                } catch (NumberFormatException e) {
                    itemShouldBeEnabled = true;
                }
            }
        }
       c.setEnabled(itemShouldBeEnabled);

        if (!itemShouldBeEnabled) {
            if (isSelected) {
                c.setBackground(list.getSelectionBackground());
                c.setForeground(UIManager.getColor("Label.disabledForeground"));
            } else {
                c.setBackground(list.getBackground());
                c.setForeground(UIManager.getColor("Label.disabledForeground"));
            }
        } else {
            if (isSelected) {
                c.setBackground(list.getSelectionBackground());
                c.setForeground(list.getSelectionForeground());
            } else {
                c.setBackground(list.getBackground());
                c.setForeground(list.getForeground());
            }
        }
        return c;
        }
    }
    
    public BaoCaoLamViecForm() {
        initComponents();
        thangComboBox.setModel(new DefaultComboBoxModel<>(tatCaCacThangModel));
        thangComboBox.setRenderer(new DisabledMonthRenderer(quyComboBox));
        
        chonThangComboBox.setModel(new DefaultComboBoxModel<>(tatCaCacThangModel)); 
        chonThangComboBox.setRenderer(new DisabledMonthRenderer(chonQuyComboBox)); 

        menuPanel.setVisible(true);
        vePanel.setVisible(false);
        chuyenBayPanel.setVisible(false);

        chonThangLabel.setVisible(false);
        chonThangComboBox.setVisible(false);
        chonQuyLabel.setVisible(false);
        chonQuyComboBox.setVisible(false);
        nhapNamLabel.setVisible(false);
        nhapNamTextField.setVisible(false);
        locButton.setVisible(false);
        
        thangLabel.setVisible(false);
        thangComboBox.setVisible(false);
        quyLabel.setVisible(false);
        quyComboBox.setVisible(false);
        namLabel.setVisible(false);
        namTextField.setVisible(false);
        locCBButton.setVisible(false);
        
        boButton.setVisible(false);
        boChonButton.setVisible(false);
        
        namTextField.addFocusListener(new FocusAdapter(){
        @Override
        public void focusLost(FocusEvent e){
            xuLyNhapNam();
            }
        });
        namTextField.addActionListener(new java.awt.event.ActionListener() {
            @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            xuLyNhapNam(); 
        }
        });
        
        quyComboBox.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                quyComboBoxItemStateChanged(evt);
            }
        });
        thangComboBox.addItemListener(new java.awt.event.ItemListener() {
        private Object lastValidSelectedItem = thangComboBox.getSelectedItem(); 
        @Override
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String selectedMonthString = (String) evt.getItem(); 
                if (selectedMonthString == null) return;

                String selectedQuyString = (String) quyComboBox.getSelectedItem();
                boolean isSelectionCurrentlyValid = true;

                if (selectedQuyString != null && !selectedQuyString.equals("Tất cả các quý")) {
                    if (!selectedMonthString.equals("Tất cả các tháng")) {
                        try {
                            int monthNumber = Integer.parseInt(selectedMonthString.replace("Tháng ", ""));
                            int quarterNumber = Integer.parseInt(selectedQuyString.replace("Quý ", ""));
                            isSelectionCurrentlyValid = isMonthInQuarter(monthNumber, quarterNumber);
                        } catch (NumberFormatException e) {
                            isSelectionCurrentlyValid = true;
                        }
                    }
                }

                if (!isSelectionCurrentlyValid) {
                    SwingUtilities.invokeLater(() -> { 
                        thangComboBox.setSelectedItem(lastValidSelectedItem);
                        
                    });
                } else {
                    lastValidSelectedItem = selectedMonthString;
                }
            }
        }
    });
        xoaDuLieuBangCB();
        chartPanel.setLayout(new BorderLayout());
        chartPanel.removeAll();
        chartPanel.revalidate();
        chartPanel.repaint();
        
        
    nhapNamTextField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            xuLyNhapNamVe();
        }
    });
    nhapNamTextField.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            xuLyNhapNamVe();
        }
    });

    chonQuyComboBox.addItemListener(new java.awt.event.ItemListener() {
        @Override
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            quyComboBoxVeItemStateChanged(evt);
        }
    });

    chonThangComboBox.addItemListener(new java.awt.event.ItemListener() {
        private Object lastValidSelectedItemVe = chonThangComboBox.getSelectedItem();

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String selectedMonthString = (String) evt.getItem();
                if (selectedMonthString == null) return;

                String selectedQuyString = (String) chonQuyComboBox.getSelectedItem();
                boolean isSelectionCurrentlyValid = true;

                if (selectedQuyString != null && !selectedQuyString.equals("Tất cả các quý")) {
                    if (!selectedMonthString.equals("Tất cả các tháng")) {
                        try {
                            int monthNumber = Integer.parseInt(selectedMonthString.replace("Tháng ", ""));
                            int quarterNumber = Integer.parseInt(selectedQuyString.replace("Quý ", ""));
                            isSelectionCurrentlyValid = isMonthInQuarter(monthNumber, quarterNumber);
                        } catch (NumberFormatException e) {
                            isSelectionCurrentlyValid = true;
                        }
                    }
                }

                if (!isSelectionCurrentlyValid) {
                    SwingUtilities.invokeLater(() -> {
                        chonThangComboBox.setSelectedItem(lastValidSelectedItemVe);
                    });
                } else {
                    lastValidSelectedItemVe = selectedMonthString;
                }
            }
        }
    });
     xoaDuLieuBangVe(); 
    chartPanelVe.setLayout(new BorderLayout()); 
    chartPanelVe.removeAll();
    chartPanelVe.revalidate();
    chartPanelVe.repaint();
          
    }
    
    private void xoaDuLieuBangCB() {
        DefaultTableModel model = (DefaultTableModel) DanhSachCBTable.getModel();
        model.setRowCount(0);
    }
    private void xoaDuLieuBangVe() {
        DefaultTableModel model = (DefaultTableModel) DanhSachVeTable.getModel();
        model.setRowCount(0);
    }
    private void themDuLieuChuyenBayTable(){
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachCBTable.getModel();
        tableModel.setRowCount(0);
        String sql = "SELECT EXTRACT(MONTH FROM GIOCATCANH), TO_CHAR(GIOCATCANH, 'Q'), EXTRACT(YEAR FROM GIOCATCANH)"
            + ", COUNT(c.MACHUYENBAY), SUM(t.KHOANGCACH) FROM CHUYEN_BAY c, TUYEN_BAY t WHERE c.MATUYENBAY = t.MATUYENBAY "
            + "AND c.TRANGTHAI = ? GROUP BY EXTRACT(MONTH FROM GIOCATCANH), TO_CHAR(GIOCATCANH, 'Q'), EXTRACT(YEAR FROM GIOCATCANH) "
            + "ORDER BY EXTRACT(YEAR FROM GIOCATCANH) ASC, TO_CHAR(GIOCATCANH, 'Q') ASC, EXTRACT(MONTH FROM GIOCATCANH) ASC"; 
        boolean foundData = false;
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, "Đang mở");
        try (ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                foundData = true;
                int thang;
                int quy;
                int nam;
                int soLuongCB;
                double tongQuangDuong;
                thang = rs.getInt(1);
                String quyStr = rs.getString(2);
                quy = Integer.parseInt(quyStr);
                nam = rs.getInt(3);
                soLuongCB = rs.getInt(4);
                tongQuangDuong = rs.getDouble(5);
                
            Object[] rowData = new Object[]{
                thang,
                quy,
                nam,
                soLuongCB,
                tongQuangDuong,
            };
            tableModel.addRow(rowData);
            
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi định dạng số khi xử lý dữ liệu quý: " + nfe.getMessage(), "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
    } catch (SQLException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
        catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Đã có lỗi không mong muốn xảy ra: " + e.getMessage(), "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
    }
        if (!foundData) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu chuyến bay nào.", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
               }
    }
    
    private void themDuLieuVeTable() {
    DefaultTableModel tableModel = (DefaultTableModel) DanhSachVeTable.getModel();
    tableModel.setRowCount(0);
    String sql = "SELECT EXTRACT(MONTH FROM NGAYDATVE), TO_CHAR(NGAYDATVE, 'Q'), EXTRACT(YEAR FROM NGAYDATVE)"
            + ", COUNT(MAVE), SUM(TONGTIEN) FROM VE_MAY_BAY WHERE TRANGTHAIVE = ? "
            + "GROUP BY EXTRACT(MONTH FROM NGAYDATVE), TO_CHAR(NGAYDATVE, 'Q'), EXTRACT(YEAR FROM NGAYDATVE) "
            + "ORDER BY EXTRACT(YEAR FROM NGAYDATVE) ASC, TO_CHAR(NGAYDATVE, 'Q') ASC, EXTRACT(MONTH FROM NGAYDATVE) ASC";
    boolean foundData = false;
    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, "Đã thanh toán");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                foundData = true;
                int thang = rs.getInt(1);
                String quyStr = rs.getString(2);
                int quy = Integer.parseInt(quyStr);
                int nam = rs.getInt(3);
                int soLuongVe = rs.getInt(4);
                double doanhThu = rs.getDouble(5);

                Object[] rowData = new Object[]{
                    thang,
                    quy,
                    nam,
                    soLuongVe,
                    doanhThu,
                };
                tableModel.addRow(rowData);
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi định dạng số khi xử lý dữ liệu quý từ vé: " + nfe.getMessage(), "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu vé: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Đã có lỗi không mong muốn xảy ra khi tải dữ liệu vé: " + e.getMessage(), "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
    }
    if (!foundData) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu vé nào đã thanh toán.", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
    }
}

     private boolean isMonthInQuarter(int month, int quarter) {
    switch (quarter) {
        case 1: return month >= 1 && month <= 3;
        case 2: return month >= 4 && month <= 6;
        case 3: return month >= 7 && month <= 9;
        case 4: return month >= 10 && month <= 12;
        default: return false; 
    }
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CardLayoutPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        veButton = new javax.swing.JButton();
        chuyenBayButton = new javax.swing.JButton();
        vePanel = new javax.swing.JPanel();
        luotVe = new javax.swing.JScrollPane();
        DanhSachVeTable = new javax.swing.JTable();
        xuatBaoCaoButton = new javax.swing.JButton();
        backBackButton = new javax.swing.JButton();
        chonThangLabel = new javax.swing.JLabel();
        chonThangComboBox = new javax.swing.JComboBox<>();
        chonQuyComboBox = new javax.swing.JComboBox<>();
        chonQuyLabel = new javax.swing.JLabel();
        tatCaNamButton = new javax.swing.JButton();
        namCuTheButton = new javax.swing.JButton();
        vuiLongLabel = new javax.swing.JLabel();
        nhapNamLabel = new javax.swing.JLabel();
        nhapNamTextField = new javax.swing.JTextField();
        locButton = new javax.swing.JButton();
        xemBaoCaoButton = new javax.swing.JButton();
        boButton = new javax.swing.JButton();
        chartPanelVe = new javax.swing.JPanel();
        chuyenBayPanel = new javax.swing.JPanel();
        luotCB = new javax.swing.JScrollPane();
        DanhSachCBTable = new javax.swing.JTable();
        xuatButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        thangLabel = new javax.swing.JLabel();
        thangComboBox = new javax.swing.JComboBox<>();
        quyComboBox = new javax.swing.JComboBox<>();
        quyLabel = new javax.swing.JLabel();
        allYearButton = new javax.swing.JButton();
        specificYearButton = new javax.swing.JButton();
        pleaseLabel = new javax.swing.JLabel();
        namLabel = new javax.swing.JLabel();
        namTextField = new javax.swing.JTextField();
        locCBButton = new javax.swing.JButton();
        xemButton = new javax.swing.JButton();
        boChonButton = new javax.swing.JButton();
        chartPanel = new javax.swing.JPanel();

        setName(""); // NOI18N
        setLayout(new java.awt.CardLayout());

        menuPanel.setBackground(new java.awt.Color(255, 255, 255));

        veButton.setBackground(new java.awt.Color(0, 255, 255));
        veButton.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        veButton.setForeground(new java.awt.Color(0, 51, 102));
        veButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/airplane-ticket (1).png"))); // NOI18N
        veButton.setText("Vé");
        veButton.setIconTextGap(30);
        veButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                veButtonActionPerformed(evt);
            }
        });

        chuyenBayButton.setBackground(new java.awt.Color(0, 255, 255));
        chuyenBayButton.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        chuyenBayButton.setForeground(new java.awt.Color(0, 51, 102));
        chuyenBayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/plane (1).png"))); // NOI18N
        chuyenBayButton.setText("Chuyến Bay");
        chuyenBayButton.setIconTextGap(30);
        chuyenBayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chuyenBayButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(veButton, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 270, Short.MAX_VALUE)
                .addComponent(chuyenBayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chuyenBayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(veButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(419, Short.MAX_VALUE))
        );

        vePanel.setBackground(new java.awt.Color(255, 255, 255));

        DanhSachVeTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        DanhSachVeTable.setForeground(new java.awt.Color(0, 51, 102));
        DanhSachVeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tháng", "Quý", "Năm", "Số lượng vé", "Doanh thu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        luotVe.setViewportView(DanhSachVeTable);

        xuatBaoCaoButton.setBackground(new java.awt.Color(0, 255, 255));
        xuatBaoCaoButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        xuatBaoCaoButton.setForeground(new java.awt.Color(0, 51, 102));
        xuatBaoCaoButton.setText("Xuất báo cáo");

        backBackButton.setBackground(new java.awt.Color(0, 255, 255));
        backBackButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        backBackButton.setForeground(new java.awt.Color(0, 51, 102));
        backBackButton.setText("Quay Lại");
        backBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBackButtonActionPerformed(evt);
            }
        });

        chonThangLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        chonThangLabel.setForeground(new java.awt.Color(0, 51, 102));
        chonThangLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chonThangLabel.setText("Chọn tháng ");

        chonThangComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        chonThangComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12", "Tất cả các tháng" }));

        chonQuyComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        chonQuyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quý 1", "Quý 2", "Quý 3", "Quý 4", "Tất cả các quý" }));

        chonQuyLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        chonQuyLabel.setForeground(new java.awt.Color(0, 51, 102));
        chonQuyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chonQuyLabel.setText("Chọn quý ");

        tatCaNamButton.setBackground(new java.awt.Color(0, 255, 255));
        tatCaNamButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        tatCaNamButton.setForeground(new java.awt.Color(0, 51, 102));
        tatCaNamButton.setText("Chọn tất cả các năm");
        tatCaNamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tatCaNamButtonActionPerformed(evt);
            }
        });

        namCuTheButton.setBackground(new java.awt.Color(0, 255, 255));
        namCuTheButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        namCuTheButton.setForeground(new java.awt.Color(0, 51, 102));
        namCuTheButton.setText("Chọn năm cụ thể");
        namCuTheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namCuTheButtonActionPerformed(evt);
            }
        });

        vuiLongLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        vuiLongLabel.setForeground(new java.awt.Color(0, 51, 102));
        vuiLongLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        vuiLongLabel.setText("Vui lòng chọn một trong hai để lọc");

        nhapNamLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        nhapNamLabel.setForeground(new java.awt.Color(0, 51, 102));
        nhapNamLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nhapNamLabel.setText("Nhập năm (Vui lòng enter khi nhập xong)");

        nhapNamTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N

        locButton.setBackground(new java.awt.Color(0, 255, 255));
        locButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        locButton.setForeground(new java.awt.Color(0, 51, 102));
        locButton.setText("Lọc");
        locButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locButtonActionPerformed(evt);
            }
        });

        xemBaoCaoButton.setBackground(new java.awt.Color(0, 255, 255));
        xemBaoCaoButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        xemBaoCaoButton.setForeground(new java.awt.Color(0, 51, 102));
        xemBaoCaoButton.setText("Xem báo cáo");

        boButton.setBackground(new java.awt.Color(0, 255, 255));
        boButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        boButton.setForeground(new java.awt.Color(0, 51, 102));
        boButton.setText("Bỏ chọn");
        boButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boButtonActionPerformed(evt);
            }
        });

        chartPanelVe.setBackground(new java.awt.Color(255, 255, 255));
        chartPanelVe.setForeground(new java.awt.Color(0, 51, 102));

        javax.swing.GroupLayout chartPanelVeLayout = new javax.swing.GroupLayout(chartPanelVe);
        chartPanelVe.setLayout(chartPanelVeLayout);
        chartPanelVeLayout.setHorizontalGroup(
            chartPanelVeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartPanelVeLayout.setVerticalGroup(
            chartPanelVeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 175, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout vePanelLayout = new javax.swing.GroupLayout(vePanel);
        vePanel.setLayout(vePanelLayout);
        vePanelLayout.setHorizontalGroup(
            vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vePanelLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(xemBaoCaoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vePanelLayout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(backBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xuatBaoCaoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))))
            .addGroup(vePanelLayout.createSequentialGroup()
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(luotVe, javax.swing.GroupLayout.DEFAULT_SIZE, 1054, Short.MAX_VALUE))
                    .addGroup(vePanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chonThangLabel)
                            .addComponent(nhapNamLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vuiLongLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(vePanelLayout.createSequentialGroup()
                                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chonThangComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tatCaNamButton))
                                .addGap(184, 184, 184)
                                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(namCuTheButton)
                                    .addGroup(vePanelLayout.createSequentialGroup()
                                        .addComponent(chonQuyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chonQuyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(nhapNamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vePanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(locButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boButton))
                .addGap(465, 465, 465))
            .addGroup(vePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartPanelVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        vePanelLayout.setVerticalGroup(
            vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vePanelLayout.createSequentialGroup()
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vePanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(namCuTheButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tatCaNamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vuiLongLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chonThangComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chonThangLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chonQuyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chonQuyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhapNamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nhapNamLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(boButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(luotVe, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chartPanelVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(vePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xuatBaoCaoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xemBaoCaoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        chuyenBayPanel.setBackground(new java.awt.Color(255, 255, 255));

        DanhSachCBTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        DanhSachCBTable.setForeground(new java.awt.Color(0, 51, 102));
        DanhSachCBTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tháng", "Quý", "Năm", "Số lượng chuyến bay", "Tổng quảng đường"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        luotCB.setViewportView(DanhSachCBTable);
        if (DanhSachCBTable.getColumnModel().getColumnCount() > 0) {
            DanhSachCBTable.getColumnModel().getColumn(4).setResizable(false);
        }

        xuatButton.setBackground(new java.awt.Color(0, 255, 255));
        xuatButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        xuatButton.setForeground(new java.awt.Color(0, 51, 102));
        xuatButton.setText("Xuất báo cáo");
        xuatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xuatButtonActionPerformed(evt);
            }
        });

        backButton.setBackground(new java.awt.Color(0, 255, 255));
        backButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        backButton.setForeground(new java.awt.Color(0, 51, 102));
        backButton.setText("Quay Lại");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        thangLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        thangLabel.setForeground(new java.awt.Color(0, 51, 102));
        thangLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thangLabel.setText("Chọn tháng ");

        thangComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        thangComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12", "Tất cả các tháng" }));

        quyComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        quyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quý 1", "Quý 2", "Quý 3", "Quý 4", "Tất cả các quý" }));

        quyLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        quyLabel.setForeground(new java.awt.Color(0, 51, 102));
        quyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        quyLabel.setText("Chọn quý ");

        allYearButton.setBackground(new java.awt.Color(0, 255, 255));
        allYearButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        allYearButton.setForeground(new java.awt.Color(0, 51, 102));
        allYearButton.setText("Chọn tất cả các năm");
        allYearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allYearButtonActionPerformed(evt);
            }
        });

        specificYearButton.setBackground(new java.awt.Color(0, 255, 255));
        specificYearButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        specificYearButton.setForeground(new java.awt.Color(0, 51, 102));
        specificYearButton.setText("Chọn năm cụ thể");
        specificYearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specificYearButtonActionPerformed(evt);
            }
        });

        pleaseLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        pleaseLabel.setForeground(new java.awt.Color(0, 51, 102));
        pleaseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pleaseLabel.setText("Vui lòng chọn một trong hai để lọc");

        namLabel.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        namLabel.setForeground(new java.awt.Color(0, 51, 102));
        namLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namLabel.setText("Nhập năm (Vui lòng enter khi nhập xong)");

        namTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N

        locCBButton.setBackground(new java.awt.Color(0, 255, 255));
        locCBButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        locCBButton.setForeground(new java.awt.Color(0, 51, 102));
        locCBButton.setText("Lọc");
        locCBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locCBButtonActionPerformed(evt);
            }
        });

        xemButton.setBackground(new java.awt.Color(0, 255, 255));
        xemButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        xemButton.setForeground(new java.awt.Color(0, 51, 102));
        xemButton.setText("Xem báo cáo");
        xemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xemButtonActionPerformed(evt);
            }
        });

        boChonButton.setBackground(new java.awt.Color(0, 255, 255));
        boChonButton.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        boChonButton.setForeground(new java.awt.Color(0, 51, 102));
        boChonButton.setText("Bỏ chọn");
        boChonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boChonButtonActionPerformed(evt);
            }
        });

        chartPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout chuyenBayPanelLayout = new javax.swing.GroupLayout(chuyenBayPanel);
        chuyenBayPanel.setLayout(chuyenBayPanelLayout);
        chuyenBayPanelLayout.setHorizontalGroup(
            chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                .addGap(442, 442, 442)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chuyenBayPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(xemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(xuatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(luotCB))
                    .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(namLabel)
                            .addComponent(pleaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(thangLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(allYearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(thangComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(195, 195, 195)
                                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(specificYearButton)
                                    .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                                        .addComponent(quyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                                        .addComponent(quyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(namTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chuyenBayPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(locCBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boChonButton))
                .addGap(421, 421, 421))
            .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        chuyenBayPanelLayout.setVerticalGroup(
            chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chuyenBayPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(specificYearButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(allYearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pleaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(thangComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thangLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(boChonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locCBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(luotCB, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(chuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xuatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CardLayoutPanelLayout = new javax.swing.GroupLayout(CardLayoutPanel);
        CardLayoutPanel.setLayout(CardLayoutPanelLayout);
        CardLayoutPanelLayout.setHorizontalGroup(
            CardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardLayoutPanelLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(400, Short.MAX_VALUE))
            .addGroup(CardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(CardLayoutPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(vePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(448, Short.MAX_VALUE)))
            .addGroup(CardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(CardLayoutPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(chuyenBayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(442, Short.MAX_VALUE)))
        );
        CardLayoutPanelLayout.setVerticalGroup(
            CardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardLayoutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
            .addGroup(CardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(CardLayoutPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(vePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(120, Short.MAX_VALUE)))
            .addGroup(CardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(CardLayoutPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(chuyenBayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(122, Short.MAX_VALUE)))
        );

        add(CardLayoutPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void chuyenBayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chuyenBayButtonActionPerformed
        // TODO add your handling code here:
        menuPanel.setVisible(false);
        vePanel.setVisible(false);
        chuyenBayPanel.setVisible(true);
        xoaDuLieuBangCB();
        clearChart();
        boChonButton.setVisible(false);
        locCBButton.setVisible(false);
        allYearButton.setEnabled(true);
        specificYearButton.setEnabled(true);
        thangLabel.setVisible(false);
        thangComboBox.setVisible(false);
        quyLabel.setVisible(false);
        quyComboBox.setVisible(false);
        namLabel.setVisible(false);
        namTextField.setVisible(false);
    }//GEN-LAST:event_chuyenBayButtonActionPerformed

    private void veButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_veButtonActionPerformed
        // TODO add your handling code here:
        menuPanel.setVisible(false);
        vePanel.setVisible(true);
        chuyenBayPanel.setVisible(false);
        xoaDuLieuBangVe();
        clearChartVe();
        tatCaNamButton.setEnabled(true);
        namCuTheButton.setEnabled(true);
        chonThangLabel.setVisible(false);
        chonThangComboBox.setVisible(false);
        chonQuyLabel.setVisible(false);
        chonQuyComboBox.setVisible(false);
        nhapNamLabel.setVisible(false);
        nhapNamTextField.setVisible(false);
        locButton.setVisible(false);
        boButton.setVisible(false);        
    }//GEN-LAST:event_veButtonActionPerformed

    private void backBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBackButtonActionPerformed
        // TODO add your handling code here:
        menuPanel.setVisible(true);
        vePanel.setVisible(false);
        chuyenBayPanel.setVisible(false);
        xoaDuLieuBangVe();
        clearChartVe();
    }//GEN-LAST:event_backBackButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
         menuPanel.setVisible(true);
        vePanel.setVisible(false);
        chuyenBayPanel.setVisible(false);
        xoaDuLieuBangCB();
        clearChart();
        
    }//GEN-LAST:event_backButtonActionPerformed

    private void tatCaNamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tatCaNamButtonActionPerformed
        // TODO add your handling code here:
        locButton.setVisible(true);
        boButton.setVisible(true);
        namCuTheButton.setEnabled(false);
        tatCaNamButton.setEnabled(false);
    }//GEN-LAST:event_tatCaNamButtonActionPerformed

    private void namCuTheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namCuTheButtonActionPerformed
        // TODO add your handling code here:
        chonThangLabel.setVisible(true);
        chonThangComboBox.setVisible(true);
        chonQuyLabel.setVisible(true);
        chonQuyComboBox.setVisible(true);
        nhapNamLabel.setVisible(true);
        nhapNamTextField.setVisible(true);
        locButton.setVisible(true);
        boButton.setVisible(true);
        tatCaNamButton.setEnabled(false);
        namCuTheButton.setEnabled(false);
        
        chonThangComboBox.setEnabled(false);
        chonQuyComboBox.setEnabled(false);
        
        nhapNamTextField.requestFocusInWindow(); 
        xoaDuLieuBangVe(); 
        clearChartVe();
    }//GEN-LAST:event_namCuTheButtonActionPerformed

    private void allYearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allYearButtonActionPerformed
        // TODO add your handling code here:
        locCBButton.setVisible(true);
        allYearButton.setEnabled(false);
        specificYearButton.setEnabled(false);
        boChonButton.setVisible(true);
        clearChart();
    }//GEN-LAST:event_allYearButtonActionPerformed

    private void specificYearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_specificYearButtonActionPerformed
        // TODO add your handling code here:
         thangLabel.setVisible(true);
        thangComboBox.setVisible(true);
        quyLabel.setVisible(true);
        quyComboBox.setVisible(true);
        namLabel.setVisible(true);
        namTextField.setVisible(true);
        locCBButton.setVisible(true);
        allYearButton.setEnabled(false);
        specificYearButton.setEnabled(false);
        boChonButton.setVisible(true);
        //Set Enabled cho mỗi trường nhập năm
        thangComboBox.setEnabled(false);
        quyComboBox.setEnabled(false);
        
        namTextField.requestFocusInWindow(); 
        xoaDuLieuBangCB(); 
        clearChart();
        
    }//GEN-LAST:event_specificYearButtonActionPerformed

    private void boButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boButtonActionPerformed
        // TODO add your handling code here:
        namCuTheButton.setEnabled(true);
        tatCaNamButton.setEnabled(true);
        locButton.setVisible(false);
        boButton.setVisible(false);
        
        chonThangLabel.setVisible(false);
        chonThangComboBox.setVisible(false);
        chonQuyLabel.setVisible(false);
        chonQuyComboBox.setVisible(false);
        nhapNamLabel.setVisible(false);
        nhapNamTextField.setVisible(false);
        
        nhapNamTextField.setText("");
        if (chonQuyComboBox.getItemCount() > 0) {
             chonQuyComboBox.setSelectedIndex(chonQuyComboBox.getItemCount()-1); 
        }
        chonThangComboBox.setModel(new DefaultComboBoxModel<>(tatCaCacThangModel)); 
         if (chonThangComboBox.getItemCount() > 0) {
            chonThangComboBox.setSelectedIndex(chonThangComboBox.getItemCount()-1); 
        }
        
        xoaDuLieuBangVe();
        clearChartVe();
    }//GEN-LAST:event_boButtonActionPerformed

    private void boChonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boChonButtonActionPerformed
        // TODO add your handling code here:
        specificYearButton.setEnabled(true);
        allYearButton.setEnabled(true);
        boChonButton.setVisible(false);
        locCBButton.setVisible(false);
        
         thangLabel.setVisible(false);
        thangComboBox.setVisible(false);
        quyLabel.setVisible(false);
        quyComboBox.setVisible(false);
        namLabel.setVisible(false);
        namTextField.setVisible(false);
        
        namTextField.setText("");
        if (quyComboBox.getItemCount() > 0) {
             quyComboBox.setSelectedIndex(quyComboBox.getItemCount()-1); 
        }
        thangComboBox.setModel(new DefaultComboBoxModel<>(tatCaCacThangModel)); 
         if (thangComboBox.getItemCount() > 0) {
            thangComboBox.setSelectedIndex(thangComboBox.getItemCount()-1); 
        }
        
        xoaDuLieuBangCB();
        clearChart();
    }//GEN-LAST:event_boChonButtonActionPerformed

    private void xuLyNhapNam() { 
        if (!allYearButton.isEnabled()) { 
        String yearText = namTextField.getText().trim();
        if (!yearText.isEmpty()) {
            try {
                int namDaNhap = Integer.parseInt(yearText); 
                 if (namDaNhap < 1900 || namDaNhap > LocalDate.now().getYear() + 10) { 
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập một năm thực tế (ví dụ: 2024).", "Năm Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
                    quyComboBox.setEnabled(false); 
                    thangComboBox.setEnabled(false);
                    thangComboBox.repaint();
                    return; 
                }

                quyComboBox.setEnabled(true);
                if (quyComboBox.getItemCount() > 0) { 
                    quyComboBox.setSelectedIndex(quyComboBox.getItemCount() - 1); 
                }

                thangComboBox.setEnabled(false); 
                thangComboBox.setSelectedItem("Tất cả các tháng"); 
                thangComboBox.repaint(); 
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập một năm hợp lệ (ví dụ: 2024).", "Lỗi Định Dạng Năm", JOptionPane.ERROR_MESSAGE);
                quyComboBox.setEnabled(false); 
                thangComboBox.setEnabled(false);
                thangComboBox.repaint();
            }
        } else {
            quyComboBox.setEnabled(false);
            thangComboBox.setEnabled(false);
            thangComboBox.repaint();
        }
    }
}
    private void xuLyNhapNamVe() {
    if (!tatCaNamButton.isEnabled()) { 
        String yearText = nhapNamTextField.getText().trim();
        if (!yearText.isEmpty()) {
            try {
                int namDaNhap = Integer.parseInt(yearText);
                if (namDaNhap < 1900 || namDaNhap > LocalDate.now().getYear() + 10) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập một năm thực tế (ví dụ: 2024).", "Năm Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
                    chonQuyComboBox.setEnabled(false);
                    chonThangComboBox.setEnabled(false);
                    chonThangComboBox.repaint();
                    return;
                }

                chonQuyComboBox.setEnabled(true);
                if (chonQuyComboBox.getItemCount() > 0) {
                    chonQuyComboBox.setSelectedIndex(chonQuyComboBox.getItemCount() - 1); 
                }

                chonThangComboBox.setEnabled(false); 
                chonThangComboBox.setSelectedItem("Tất cả các tháng");
                chonThangComboBox.repaint();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập một năm hợp lệ (ví dụ: 2024).", "Lỗi Định Dạng Năm", JOptionPane.ERROR_MESSAGE);
                chonQuyComboBox.setEnabled(false);
                chonThangComboBox.setEnabled(false);
                chonThangComboBox.repaint();
            }
        } else { 
            chonQuyComboBox.setEnabled(false);
            chonThangComboBox.setEnabled(false);
            chonThangComboBox.repaint();
        }
    }
}

    private void quyComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (!allYearButton.isEnabled() && quyComboBox.isEnabled()) {
            thangComboBox.setEnabled(true); 
            
           
            thangComboBox.setSelectedItem("Tất cả các tháng"); 
            
            thangComboBox.repaint(); 
        }
            }
        
    }

    private void quyComboBoxVeItemStateChanged(java.awt.event.ItemEvent evt) {
    if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
        if (!tatCaNamButton.isEnabled() && chonQuyComboBox.isEnabled()) {
            chonThangComboBox.setEnabled(true); 
            chonThangComboBox.setSelectedItem("Tất cả các tháng");
            chonThangComboBox.repaint(); 
        }
    }
}
    private void locCBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locCBButtonActionPerformed
        // TODO add your handling code here:
        xoaDuLieuBangCB();
        String namTruyenVaoBieuDo = null;
        String quyTruyenVaoBieuDo = (String) quyComboBox.getSelectedItem();
        String thangTruyenVaoBieuDo = (String) thangComboBox.getSelectedItem();
        if (namTextField.isVisible()) { 
        System.out.println("Lọc năm cụ thể...");
        String namStr = namTextField.getText().trim();
         namTruyenVaoBieuDo = namStr;
        if (namStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm.", "Thiếu Thông Tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer nam = null;
        try {
            nam = Integer.parseInt(namStr);
            if (nam < 1900 || nam > LocalDate.now().getYear() + 10) {
                JOptionPane.showMessageDialog(this, "Năm nhập không hợp lệ. Vui lòng nhập năm thực tế.", "Lỗi Năm Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Năm nhập không hợp lệ. Phải là một số.", "Lỗi Định Dạng Năm", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!quyComboBox.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm hợp lệ và rời khỏi trường nhập năm để kích hoạt chọn quý.", "Trình Tự Lọc Chưa Hoàn Tất", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String chonThangStr = (String) thangComboBox.getSelectedItem();
        String chonQuyStr = (String) quyComboBox.getSelectedItem();
        quyTruyenVaoBieuDo = chonQuyStr;
        thangTruyenVaoBieuDo = chonThangStr;
        Integer thang = null;
        if (thangComboBox.isEnabled() && chonThangStr != null && !chonThangStr.equals("Tất cả các tháng")) {
            try {
                thang = Integer.parseInt(chonThangStr.replace("Tháng ", ""));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse tháng: " + chonThangStr);
            }
        }

        Integer quy = null;
        if (quyComboBox.isEnabled() && chonQuyStr != null && !chonQuyStr.equals("Tất cả các quý")) {
            try {
                quy = Integer.parseInt(chonQuyStr.replace("Quý ", ""));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse quý: " + chonQuyStr);
            }
        }

        locDuLieuChuyenBayTable(thang, quy, nam);

    } else if (!allYearButton.isEnabled() && !specificYearButton.isEnabled()) {
        System.out.println("Lọc tất cả các năm...");
        themDuLieuChuyenBayTable(); 
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn 'Tất cả các năm' hoặc 'Năm cụ thể' trước khi lọc.", "Chưa Chọn Kiểu Lọc", JOptionPane.INFORMATION_MESSAGE);
    }
        DefaultTableModel model = (DefaultTableModel) DanhSachCBTable.getModel();
        if (model.getRowCount() > 0) {
            veBieuDoChuyenBay(model, namTruyenVaoBieuDo, quyTruyenVaoBieuDo, thangTruyenVaoBieuDo);
        } else {
            clearChart();
            displayNoDataOnChartPanel();
        }
        
    }//GEN-LAST:event_locCBButtonActionPerformed

    private void locButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locButtonActionPerformed
        // TODO add your handling code here:
        xoaDuLieuBangVe();
        String namTruyenVaoBieuDo = null;
        String quyTruyenVaoBieuDo = (String) chonQuyComboBox.getSelectedItem();
        String thangTruyenVaoBieuDo = (String) chonThangComboBox.getSelectedItem();

        if (nhapNamTextField.isVisible()) { 
            System.out.println("Lọc vé theo năm cụ thể...");
            String namStr = nhapNamTextField.getText().trim();
        namTruyenVaoBieuDo = namStr;

        if (namStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm.", "Thiếu Thông Tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer nam = null;
        try {
            nam = Integer.parseInt(namStr);
            if (nam < 1900 || nam > LocalDate.now().getYear() + 10) {
                JOptionPane.showMessageDialog(this, "Năm nhập không hợp lệ. Vui lòng nhập năm thực tế.", "Lỗi Năm Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Năm nhập không hợp lệ. Phải là một số.", "Lỗi Định Dạng Năm", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!chonQuyComboBox.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm hợp lệ và rời khỏi trường nhập năm để kích hoạt chọn quý.", "Trình Tự Lọc Chưa Hoàn Tất", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String chonThangStr = (String) chonThangComboBox.getSelectedItem();
        String chonQuyStr = (String) chonQuyComboBox.getSelectedItem();
        quyTruyenVaoBieuDo = chonQuyStr; 
        thangTruyenVaoBieuDo = chonThangStr;

        Integer thang = null;
        if (chonThangComboBox.isEnabled() && chonThangStr != null && !chonThangStr.equals("Tất cả các tháng")) {
            try {
                thang = Integer.parseInt(chonThangStr.replace("Tháng ", ""));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse tháng (Vé): " + chonThangStr);
            }
        }

        Integer quy = null;
        if (chonQuyComboBox.isEnabled() && chonQuyStr != null && !chonQuyStr.equals("Tất cả các quý")) {
            try {
                quy = Integer.parseInt(chonQuyStr.replace("Quý ", ""));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse quý (Vé): " + chonQuyStr);
            }
        }
        locDuLieuVeTable(thang, quy, nam);

    } else if (!tatCaNamButton.isEnabled() && !namCuTheButton.isEnabled()) { 
        System.out.println("Lọc vé tất cả các năm...");
        themDuLieuVeTable();
        quyTruyenVaoBieuDo = "Tất cả các quý"; 
        thangTruyenVaoBieuDo = "Tất cả các tháng";
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn 'Tất cả các năm' hoặc 'Năm cụ thể' trước khi lọc (Vé).", "Chưa Chọn Kiểu Lọc", JOptionPane.INFORMATION_MESSAGE);
    }

    DefaultTableModel model = (DefaultTableModel) DanhSachVeTable.getModel();
    if (model.getRowCount() > 0) {
        veBieuDoDoanhThuVe(model, namTruyenVaoBieuDo, quyTruyenVaoBieuDo, thangTruyenVaoBieuDo);
    } else {
        clearChartVe();
        displayNoDataOnChartPanelVe();
    }
    }//GEN-LAST:event_locButtonActionPerformed

    private void xemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xemButtonActionPerformed
        // TODO add your handling code here:
       
   
    }//GEN-LAST:event_xemButtonActionPerformed

    private void xuatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xuatButtonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_xuatButtonActionPerformed
     
private void locDuLieuChuyenBayTable(Integer thangFilter, Integer quyFilter, Integer namFilter) {
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachCBTable.getModel();
        tableModel.setRowCount(0); 

        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT EXTRACT(MONTH FROM GIOCATCANH), TO_CHAR(GIOCATCANH, 'Q'), EXTRACT(YEAR FROM GIOCATCANH), " +
            "COUNT(c.MACHUYENBAY), SUM(t.KHOANGCACH) " +
            "FROM CHUYEN_BAY c JOIN TUYEN_BAY t ON c.MATUYENBAY = t.MATUYENBAY " + 
            "WHERE c.TRANGTHAI = ? " 
        );

        List<Object> params = new ArrayList<>();
        params.add("Đang mở"); 

        if (namFilter != null) {
            sqlBuilder.append("AND EXTRACT(YEAR FROM GIOCATCANH) = ? ");
            params.add(namFilter);
        }
        if (quyFilter != null) {
            sqlBuilder.append("AND TO_CHAR(GIOCATCANH, 'Q') = ? ");
            params.add(String.valueOf(quyFilter)); 
        }
        if (thangFilter != null) {
            sqlBuilder.append("AND EXTRACT(MONTH FROM GIOCATCANH) = ? ");
            params.add(thangFilter);
        }

        sqlBuilder.append("GROUP BY EXTRACT(MONTH FROM GIOCATCANH), TO_CHAR(GIOCATCANH, 'Q'), EXTRACT(YEAR FROM GIOCATCANH) ");
        sqlBuilder.append("ORDER BY EXTRACT(YEAR FROM GIOCATCANH) ASC, TO_CHAR(GIOCATCANH, 'Q') ASC, EXTRACT(MONTH FROM GIOCATCANH) ASC");

        System.out.println("Đang thực thi Query: " + sqlBuilder.toString());
        System.out.println("Với tham số: " + params);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                boolean foundData = false;
                while (rs.next()) {
                    foundData = true;
                    int thang = rs.getInt(1);
                    String quyStrDb = rs.getString(2); 
                    int quy = Integer.parseInt(quyStrDb);
                    int nam = rs.getInt(3);
                    int soLuongCB = rs.getInt(4);
                    double tongQuangDuong = rs.getDouble(5);
                    Object[] rowData = new Object[]{thang, quy, nam, soLuongCB, tongQuangDuong};
                    tableModel.addRow(rowData);
                }
                if (!foundData) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu nào phù hợp với tiêu chí lọc.", "Thông Báo Kết Quả", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn CSDL: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi định dạng số khi xử lý dữ liệu từ CSDL: " + nfe.getMessage(), "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã có lỗi không mong muốn xảy ra: " + e.getMessage(), "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    private void locDuLieuVeTable(Integer thangFilter, Integer quyFilter, Integer namFilter) {
    DefaultTableModel tableModel = (DefaultTableModel) DanhSachVeTable.getModel();
    tableModel.setRowCount(0);

    StringBuilder sqlBuilder = new StringBuilder(
        "SELECT EXTRACT(MONTH FROM NGAYDATVE), TO_CHAR(NGAYDATVE, 'Q'), EXTRACT(YEAR FROM NGAYDATVE), " +
        "COUNT(MAVE), SUM(TONGTIEN) " +
        "FROM VE_MAY_BAY " +
        "WHERE TRANGTHAIVE = ? " 
    );

    List<Object> params = new ArrayList<>();
    params.add("Đã thanh toán");

    if (namFilter != null) {
        sqlBuilder.append("AND EXTRACT(YEAR FROM NGAYDATVE) = ? ");
        params.add(namFilter);
    }
    if (quyFilter != null) {
        sqlBuilder.append("AND TO_CHAR(NGAYDATVE, 'Q') = ? ");
        params.add(String.valueOf(quyFilter));
    }
    if (thangFilter != null) {
        sqlBuilder.append("AND EXTRACT(MONTH FROM NGAYDATVE) = ? ");
        params.add(thangFilter);
    }

    sqlBuilder.append("GROUP BY EXTRACT(MONTH FROM NGAYDATVE), TO_CHAR(NGAYDATVE, 'Q'), EXTRACT(YEAR FROM NGAYDATVE) ");
    sqlBuilder.append("ORDER BY EXTRACT(YEAR FROM NGAYDATVE) ASC, TO_CHAR(NGAYDATVE, 'Q') ASC, EXTRACT(MONTH FROM NGAYDATVE) ASC");

    System.out.println("Đang thực thi Query Vé: " + sqlBuilder.toString());
    System.out.println("Với tham số Vé: " + params);

    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        try (ResultSet rs = ps.executeQuery()) {
            boolean foundData = false;
            while (rs.next()) {
                foundData = true;
                int thang = rs.getInt(1);
                String quyStrDb = rs.getString(2);
                int quy = Integer.parseInt(quyStrDb);
                int nam = rs.getInt(3);
                int soLuongVe = rs.getInt(4);
                double doanhThu = rs.getDouble(5);
                Object[] rowData = new Object[]{thang, quy, nam, soLuongVe, doanhThu};
                tableModel.addRow(rowData);
            }
            if (!foundData) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu vé nào phù hợp với tiêu chí lọc.", "Thông Báo Kết Quả", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn CSDL (Vé): " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi định dạng số khi xử lý dữ liệu từ CSDL (Vé): " + nfe.getMessage(), "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Đã có lỗi không mong muốn xảy ra (Vé): " + e.getMessage(), "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
    }
    }
      private void clearChart() {
        chartPanel.removeAll();
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    private void displayNoDataOnChartPanel() {
        clearChart();
        JLabel noDataLabel = new JLabel("Không có dữ liệu để hiển thị biểu đồ.");
        noDataLabel.setHorizontalAlignment(JLabel.CENTER);
        noDataLabel.setFont(new Font("UTM Aptima", Font.PLAIN, 16));
        chartPanel.add(noDataLabel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    
    private void clearChartVe() {
        chartPanelVe.removeAll();
        chartPanelVe.revalidate();
        chartPanelVe.repaint();
    }

    private void displayNoDataOnChartPanelVe() {
        clearChartVe();
        JLabel noDataLabel = new JLabel("Không có dữ liệu doanh thu để hiển thị biểu đồ.");
        noDataLabel.setHorizontalAlignment(JLabel.CENTER);
        noDataLabel.setFont(new Font("UTM Aptima", Font.PLAIN, 16)); 
        chartPanelVe.add(noDataLabel, BorderLayout.CENTER);
        chartPanelVe.revalidate();
        chartPanelVe.repaint();
    }
    
    private void veBieuDoChuyenBay(DefaultTableModel tableModel, String namDaChonStr, String quyDaChonStr, String thangDaChonStr) {
         clearChart();

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String chartTitle = "Biểu đồ Thống kê Số Lượng Chuyến Bay"; 
    String categoryAxisLabel = "Thời gian";
    String valueAxisLabel = "Số lượng chuyến bay"; 

    boolean locTheoNamCuThe = namTextField.isVisible() && namDaChonStr != null && !namDaChonStr.isEmpty();
    Integer namFilter = locTheoNamCuThe ? Integer.parseInt(namDaChonStr) : null;

    boolean locTheoQuyCuThe = locTheoNamCuThe && quyComboBox.isEnabled() &&
            quyDaChonStr != null && !quyDaChonStr.equals("Tất cả các quý");
    Integer quyFilter = locTheoQuyCuThe ? Integer.parseInt(quyDaChonStr.replace("Quý ", "")) : null;

    boolean locTheoThangCuThe = locTheoQuyCuThe && thangComboBox.isEnabled() &&
            thangDaChonStr != null && !thangDaChonStr.equals("Tất cả các tháng");
    Integer thangFilter = locTheoThangCuThe ? Integer.parseInt(thangDaChonStr.replace("Tháng ", "")) : null;

    if (!locTheoNamCuThe) {
        chartTitle = "Thống kê Số Lượng Chuyến Bay Theo Năm";
        categoryAxisLabel = "Năm";
        Map<Integer, Integer> soLuongCBNam = new HashMap<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int nam = (Integer) tableModel.getValueAt(i, 2);
            int slcb = (Integer) tableModel.getValueAt(i, 3);
            soLuongCBNam.put(nam, soLuongCBNam.getOrDefault(nam, 0) + slcb);
        }
        List<Integer> sortedYears = new ArrayList<>(soLuongCBNam.keySet());
        java.util.Collections.sort(sortedYears);

        for (Integer nam : sortedYears) {
            dataset.addValue(soLuongCBNam.get(nam), valueAxisLabel, String.valueOf(nam));
        }

    } else { 
        if (!locTheoQuyCuThe) { 
            chartTitle = "Thống kê Số Lượng Chuyến Bay Năm " + namFilter + " (Theo Tháng)";
            categoryAxisLabel = "Tháng";
            Map<Integer, Integer> soLuongCBThang = new HashMap<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int namTrongBang = (Integer) tableModel.getValueAt(i, 2);
                if (namTrongBang == namFilter.intValue()) { 
                    int thang = (Integer) tableModel.getValueAt(i, 0);
                    int slcb = (Integer) tableModel.getValueAt(i, 3);
                    soLuongCBThang.put(thang, soLuongCBThang.getOrDefault(thang,0) + slcb);
                }
            }
            List<Integer> sortedMonths = new ArrayList<>(soLuongCBThang.keySet());
            java.util.Collections.sort(sortedMonths);
            for(Integer thang : sortedMonths){
                 String category = "T" + thang;
                 dataset.addValue(soLuongCBThang.get(thang), valueAxisLabel, category);
            }

        } else { 
            if (!locTheoThangCuThe) { 
                chartTitle = "Thống kê Số Lượng Chuyến Bay Quý " + quyFilter + "/" + namFilter + " (Theo Tháng)";
                categoryAxisLabel = "Tháng";
                Map<Integer, Integer> soLuongCBThangTrongQuy = new HashMap<>();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int namTrongBang = (Integer) tableModel.getValueAt(i, 2);
                    int quyTrongBang = (Integer) tableModel.getValueAt(i, 1);
                    if (namTrongBang == namFilter.intValue() && quyTrongBang == quyFilter.intValue()) {
                        int thang = (Integer) tableModel.getValueAt(i, 0);
                        int slcb = (Integer) tableModel.getValueAt(i, 3);
                         soLuongCBThangTrongQuy.put(thang, soLuongCBThangTrongQuy.getOrDefault(thang,0) + slcb);
                    }
                }
                List<Integer> sortedMonthsInQuarter = new ArrayList<>(soLuongCBThangTrongQuy.keySet());
                java.util.Collections.sort(sortedMonthsInQuarter);
                for(Integer thang : sortedMonthsInQuarter){
                    String category = "T" + thang;
                    dataset.addValue(soLuongCBThangTrongQuy.get(thang), valueAxisLabel, category);
                }
            } else { 
                chartTitle = "Số Lượng Chuyến Bay Tháng " + thangFilter + "/Quý " + quyFilter + "/" + namFilter;
                categoryAxisLabel = "Tháng " + thangFilter;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int namTrongBang = (Integer) tableModel.getValueAt(i, 2);
                    int quyTrongBang = (Integer) tableModel.getValueAt(i, 1);
                    int thangTrongBang = (Integer) tableModel.getValueAt(i, 0);
                    if (namTrongBang == namFilter.intValue() && quyTrongBang == quyFilter.intValue() && thangTrongBang == thangFilter.intValue()) {
                        int slcb = (Integer) tableModel.getValueAt(i, 3);
                        String category = "Tháng " + thangFilter;
                        dataset.addValue(slcb, valueAxisLabel, category);
                        break; 
                    }
                }
            }
        }
    }

    if (dataset.getColumnCount() == 0) {
        displayNoDataOnChartPanel();
        return;
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            chartTitle,
            categoryAxisLabel,
            valueAxisLabel, 
            dataset,
            PlotOrientation.VERTICAL,
            true,  
            true,  
            false  
    );

    CategoryPlot plot = barChart.getCategoryPlot();
    plot.setBackgroundPaint(Color.WHITE);

    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setSeriesPaint(0, new Color(79, 129, 189)); 
    renderer.setItemMargin(0.05); 
    renderer.setDrawBarOutline(false);
     
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    
    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setLabelFont(new Font("UTM Aptima", Font.BOLD, 14));
    domainAxis.setTickLabelFont(new Font("UTM Aptima", Font.PLAIN, 12));
    if (dataset.getColumnCount() > 8) { 
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
    }

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setLabelFont(new Font("UTM Aptima", Font.BOLD, 14));
    rangeAxis.setTickLabelFont(new Font("UTM Aptima", Font.PLAIN, 12));
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
    rangeAxis.setAutoRangeIncludesZero(true); 
    rangeAxis.setLowerBound(0.0);             
    if (rangeAxis.getUpperBound() < 5) { 
        rangeAxis.setUpperBound(Math.max(5, rangeAxis.getUpperBound() + 1)); 
    }


    barChart.getTitle().setFont(new Font("UTM Aptima", Font.BOLD, 18));
    barChart.getTitle().setPaint(new Color(50, 50, 50));

    if (barChart.getLegend() != null) {
        barChart.getLegend().setItemFont(new Font("UTM Aptima", Font.PLAIN, 12));
        barChart.getLegend().setBorder(0,0,0,0); 
    }

    ChartPanel cp = new ChartPanel(barChart);
    cp.setMouseWheelEnabled(false);   
    cp.setDomainZoomable(false);      
    cp.setRangeZoomable(false);      

    int desiredWidth = 600; 
        int desiredHeight = 200; 
        cp.setPreferredSize(new Dimension(desiredWidth, desiredHeight));
    chartPanel.add(cp, BorderLayout.CENTER);
    chartPanel.revalidate();
    chartPanel.repaint();
}
    private void veBieuDoDoanhThuVe(DefaultTableModel tableModel, String namDaChonStr, String quyDaChonStr, String thangDaChonStr) {
    clearChartVe();

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String chartTitle = "Biểu đồ Thống kê Doanh Thu Vé";
    String categoryAxisLabel = "Thời gian";
    String valueAxisLabel = "Doanh thu (VNĐ)"; 
    boolean locTheoNamCuThe = nhapNamTextField.isVisible() && namDaChonStr != null && !namDaChonStr.isEmpty();
    Integer namFilter = locTheoNamCuThe ? Integer.parseInt(namDaChonStr) : null;

    boolean locTheoQuyCuThe = locTheoNamCuThe && chonQuyComboBox.isEnabled() &&
                              quyDaChonStr != null && !quyDaChonStr.equals("Tất cả các quý");
    Integer quyFilter = locTheoQuyCuThe ? Integer.parseInt(quyDaChonStr.replace("Quý ", "")) : null;

    boolean locTheoThangCuThe = locTheoQuyCuThe && chonThangComboBox.isEnabled() &&
                                thangDaChonStr != null && !thangDaChonStr.equals("Tất cả các tháng");
    Integer thangFilter = locTheoThangCuThe ? Integer.parseInt(thangDaChonStr.replace("Tháng ", "")) : null;

    if (!locTheoNamCuThe) { 
        chartTitle = "Thống kê Doanh Thu Vé Theo Năm";
        categoryAxisLabel = "Năm";
        Map<Integer, Double> doanhThuNam = new HashMap<>(); 
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int nam = (Integer) tableModel.getValueAt(i, 2);
            double doanhThu = (Double) tableModel.getValueAt(i, 4); 
            doanhThuNam.put(nam, doanhThuNam.getOrDefault(nam, 0.0) + doanhThu);
        }
        List<Integer> sortedYears = new ArrayList<>(doanhThuNam.keySet());
        java.util.Collections.sort(sortedYears);

        for (Integer nam : sortedYears) {
            dataset.addValue(doanhThuNam.get(nam), valueAxisLabel, String.valueOf(nam));
        }

    } else {
        if (!locTheoQuyCuThe) { 
            chartTitle = "Thống kê Doanh Thu Vé Năm " + namFilter + " (Theo Tháng)";
            categoryAxisLabel = "Tháng";
            Map<Integer, Double> doanhThuThang = new HashMap<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int namTrongBang = (Integer) tableModel.getValueAt(i, 2);
                if (namTrongBang == namFilter.intValue()) {
                    int thang = (Integer) tableModel.getValueAt(i, 0);
                    double doanhThu = (Double) tableModel.getValueAt(i, 4); 
                    doanhThuThang.put(thang, doanhThuThang.getOrDefault(thang, 0.0) + doanhThu);
                }
            }
            List<Integer> sortedMonths = new ArrayList<>(doanhThuThang.keySet());
            java.util.Collections.sort(sortedMonths);
            for (Integer thang : sortedMonths) {
                String category = "T" + thang;
                dataset.addValue(doanhThuThang.get(thang), valueAxisLabel, category);
            }

        } else { 
            if (!locTheoThangCuThe) { 
                chartTitle = "Thống kê Doanh Thu Vé Quý " + quyFilter + "/" + namFilter + " (Theo Tháng)";
                categoryAxisLabel = "Tháng";
                Map<Integer, Double> doanhThuThangTrongQuy = new HashMap<>();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int namTrongBang = (Integer) tableModel.getValueAt(i, 2);
                    int quyTrongBang = (Integer) tableModel.getValueAt(i, 1); 
                    if (namTrongBang == namFilter.intValue() && quyTrongBang == quyFilter.intValue()) {
                        int thang = (Integer) tableModel.getValueAt(i, 0);
                        double doanhThu = (Double) tableModel.getValueAt(i, 4);
                        doanhThuThangTrongQuy.put(thang, doanhThuThangTrongQuy.getOrDefault(thang, 0.0) + doanhThu);
                    }
                }
                List<Integer> sortedMonthsInQuarter = new ArrayList<>(doanhThuThangTrongQuy.keySet());
                java.util.Collections.sort(sortedMonthsInQuarter);
                for (Integer thang : sortedMonthsInQuarter) {
                    String category = "T" + thang;
                    dataset.addValue(doanhThuThangTrongQuy.get(thang), valueAxisLabel, category);
                }
            } else { 
                chartTitle = "Doanh Thu Vé Tháng " + thangFilter + "/Quý " + quyFilter + "/" + namFilter;
                categoryAxisLabel = "Tháng " + thangFilter;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int namTrongBang = (Integer) tableModel.getValueAt(i, 2);
                    int quyTrongBang = (Integer) tableModel.getValueAt(i, 1);
                    int thangTrongBang = (Integer) tableModel.getValueAt(i, 0);
                    if (namTrongBang == namFilter.intValue() && quyTrongBang == quyFilter.intValue() && thangTrongBang == thangFilter.intValue()) {
                        double doanhThu = (Double) tableModel.getValueAt(i, 4);
                        String category = "Tháng " + thangFilter;
                        dataset.addValue(doanhThu, valueAxisLabel, category);
                        break; 
                    }
                }
            }
        }
    }

    if (dataset.getColumnCount() == 0) {
        displayNoDataOnChartPanelVe();
        return;
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            chartTitle,
            categoryAxisLabel,
            valueAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true,  
            true, 
            false  
    );

    CategoryPlot plot = barChart.getCategoryPlot();
    plot.setBackgroundPaint(Color.WHITE);

    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setSeriesPaint(0, new Color(0, 153, 76)); 
    renderer.setItemMargin(0.05);
    renderer.setDrawBarOutline(false);
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1}: {2}", java.text.NumberFormat.getInstance())); // Định dạng tooltip cho số tiền

    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setLabelFont(new Font("UTM Aptima", Font.BOLD, 14));
    domainAxis.setTickLabelFont(new Font("UTM Aptima", Font.PLAIN, 12));
    if (dataset.getColumnCount() > 8) {
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD); 
    }

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setLabelFont(new Font("UTM Aptima", Font.BOLD, 14));
    rangeAxis.setTickLabelFont(new Font("UTM Aptima", Font.PLAIN, 12));
    rangeAxis.setAutoRangeIncludesZero(true);
    rangeAxis.setLowerBound(0.0);
    

    barChart.getTitle().setFont(new Font("UTM Aptima", Font.BOLD, 18));
    barChart.getTitle().setPaint(new Color(50, 50, 50));

    if (barChart.getLegend() != null) {
        barChart.getLegend().setItemFont(new Font("UTM Aptima", Font.PLAIN, 12));
        barChart.getLegend().setBorder(0,0,0,0);
    }

    ChartPanel cp = new ChartPanel(barChart);
    cp.setMouseWheelEnabled(false);
    cp.setDomainZoomable(false);
    cp.setRangeZoomable(false);
    
    int desiredWidth = 600; 
    int desiredHeight = 190; 
    cp.setPreferredSize(new Dimension(desiredWidth, desiredHeight));

    chartPanelVe.add(cp, BorderLayout.CENTER);
    chartPanelVe.revalidate();
    chartPanelVe.repaint();
}
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CardLayoutPanel;
    private javax.swing.JTable DanhSachCBTable;
    private javax.swing.JTable DanhSachVeTable;
    private javax.swing.JButton allYearButton;
    private javax.swing.JButton backBackButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton boButton;
    private javax.swing.JButton boChonButton;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JPanel chartPanelVe;
    private javax.swing.JComboBox<String> chonQuyComboBox;
    private javax.swing.JLabel chonQuyLabel;
    private javax.swing.JComboBox<String> chonThangComboBox;
    private javax.swing.JLabel chonThangLabel;
    private javax.swing.JButton chuyenBayButton;
    private javax.swing.JPanel chuyenBayPanel;
    private javax.swing.JButton locButton;
    private javax.swing.JButton locCBButton;
    private javax.swing.JScrollPane luotCB;
    private javax.swing.JScrollPane luotVe;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JButton namCuTheButton;
    private javax.swing.JLabel namLabel;
    private javax.swing.JTextField namTextField;
    private javax.swing.JLabel nhapNamLabel;
    private javax.swing.JTextField nhapNamTextField;
    private javax.swing.JLabel pleaseLabel;
    private javax.swing.JComboBox<String> quyComboBox;
    private javax.swing.JLabel quyLabel;
    private javax.swing.JButton specificYearButton;
    private javax.swing.JButton tatCaNamButton;
    private javax.swing.JComboBox<String> thangComboBox;
    private javax.swing.JLabel thangLabel;
    private javax.swing.JButton veButton;
    private javax.swing.JPanel vePanel;
    private javax.swing.JLabel vuiLongLabel;
    private javax.swing.JButton xemBaoCaoButton;
    private javax.swing.JButton xemButton;
    private javax.swing.JButton xuatBaoCaoButton;
    private javax.swing.JButton xuatButton;
    // End of variables declaration//GEN-END:variables
}
