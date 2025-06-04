/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View.Admin;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 *
 * 
 */
public class QLChuyenBayForm extends javax.swing.JPanel {
    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String DB_USER = "c##QLHHK";
    private final String DB_PASSWORD = "Admin123";
    private java.util.List<String> danhSachMaMayBay = new java.util.ArrayList<>();
    private JPopupMenu suggestionsPopupMenu;
    private List<String> allFlightCodesForSuggestion = new ArrayList<>();  
    private List<Object[]> allFlightDataForFiltering = new ArrayList<>(); 
    private boolean isProgrammaticallyUpdatingText = false;
    private enum ActionState {
        NONE,
        DELETING,
        EDITING
    }
    private ActionState currentActionState = ActionState.NONE;
    private String maChuyenBayDangThaoTac = null; 

    /**
     * Creates new form QLChuyenBayForm
     */
    public QLChuyenBayForm() {
        initComponents();
        xacNhanButton.setVisible(false);
        tuChoiButton.setVisible(false);
        thoatXemButton.setVisible(false);
        xoaButton.setEnabled(false); 
        suaButton.setEnabled(false); 
        suggestionsPopupMenu = new JPopupMenu();
        suggestionsPopupMenu.setFocusable(false); 
        DefaultTableModel readOnlyTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;
                    case 1: return String.class;
                    case 2: return String.class;
                    case 3: return String.class; 
                    case 4: return String.class; 
                    case 5: return String.class;
                    case 6: return Double.class;
                    case 7: return Integer.class;
                    default: return Object.class;
                }
            }
        };

        DefaultTableModel currentModel = (DefaultTableModel) DanhSachChuyenBayTable.getModel();
        for (int i = 0; i < currentModel.getColumnCount(); i++) {
            readOnlyTableModel.addColumn(currentModel.getColumnName(i));
        }

        DanhSachChuyenBayTable.setModel(readOnlyTableModel);
        themDuLieuTuyenBayComboBox();
        themDuLieuMayBayComboBox();
        themDuLieuChuyenBayTable();
        initSearchSuggestions(); 
        DanhSachChuyenBayTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && DanhSachChuyenBayTable.getSelectedRow() != -1) {
                    int selectedRow = DanhSachChuyenBayTable.getSelectedRow();
                    maChuyenBayDangThaoTac = DanhSachChuyenBayTable.getValueAt(selectedRow, 0).toString(); 

                    dienThongTinChuyenBayVaoForm(selectedRow);
                    thoatXemButton.setVisible(true);
                    thanhCongCuLabel.setVisible(false);
                    xacNhanButton.setVisible(false);
                    tuChoiButton.setVisible(false);
                    themButton.setVisible(false);
                    xoaButton.setEnabled(true);    
                    suaButton.setEnabled(true);
                    currentActionState = ActionState.NONE;
                }
            }
        });
    }
    private void themDuLieuTuyenBayComboBox(){
        String sql = "SELECT MATUYENBAY FROM TUYEN_BAY";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tuyenBayComboBox.addItem(rs.getString("MATUYENBAY"));
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
    }
    private void themDuLieuMayBayComboBox(){
        String sql = "SELECT MAMAYBAY, LOAIMAYBAY FROM MAY_BAY";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               mayBayComboBox.addItem(rs.getString("LOAIMAYBAY"));
               String maMayBay = rs.getString("MAMAYBAY");
               danhSachMaMayBay.add(maMayBay);
            }
             
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
    }
    private void themDuLieuChuyenBayTable(){
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachChuyenBayTable.getModel();
        tableModel.setRowCount(0);
        allFlightCodesForSuggestion.clear(); 
        allFlightDataForFiltering.clear(); 
        String sql = "SELECT MACHUYENBAY, MATUYENBAY, MAMAYBAY, GIOCATCANH, GIOHACANH, TRANGTHAI, GIAVE, SOGHETRONG FROM CHUYEN_BAY";
        try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            while (rs.next()){
                String maChuyenBay = rs.getString("MACHUYENBAY");
                String maTuyenBay = rs.getString("MATUYENBAY");
                String maMayBay = rs.getString("MAMAYBAY");

                Timestamp gioCatCanhTs = rs.getTimestamp("GIOCATCANH");
                String gioCatCanhStr = (gioCatCanhTs != null) ? sdf.format(gioCatCanhTs) : null;

                Timestamp gioHaCanhTs = rs.getTimestamp("GIOHACANH");
                String gioHaCanhStr = (gioHaCanhTs != null) ? sdf.format(gioHaCanhTs) : null;
                
                String trangThai = rs.getString("TRANGTHAI");
                double giaVe = rs.getDouble("GIAVE");
                int soGheTrong = rs.getInt("SOGHETRONG");

                Object[] rowData = new Object[]{
                    maChuyenBay,
                    maTuyenBay,
                    maMayBay,
                    gioCatCanhStr,
                    gioHaCanhStr,
                    trangThai,
                    giaVe,
                    soGheTrong
                };
                tableModel.addRow(rowData);
                allFlightCodesForSuggestion.add(maChuyenBay); 
                allFlightDataForFiltering.add(rowData);
                }
                } catch (SQLException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
    }
    private String layMaMayBay() {
    int selectedIndex = mayBayComboBox.getSelectedIndex();
    if (selectedIndex != -1 && selectedIndex < danhSachMaMayBay.size()) {
        return danhSachMaMayBay.get(selectedIndex);
    }
    return null; 
    }
    private void initSearchSuggestions() {
       nhapMaCBTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if (!isProgrammaticallyUpdatingText) {
                    showSuggestions();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        nhapMaCBTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    suggestionsPopupMenu.setVisible(false);
                    timChuyenBay();
                    e.consume(); 
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionsPopupMenu.setVisible(false);
                    e.consume();
                }
            }
        });

        nhapMaCBTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Component oppositeComponent = e.getOppositeComponent();
                if (oppositeComponent == null || !SwingUtilities.isDescendingFrom(oppositeComponent, suggestionsPopupMenu)) {
                    SwingUtilities.invokeLater(() -> {
                        boolean textFieldHasFocus = nhapMaCBTextField.isFocusOwner();
                        boolean popupHasFocus = false;
                        Component currentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                        if (currentFocusOwner != null) {
                            popupHasFocus = SwingUtilities.isDescendingFrom(currentFocusOwner, suggestionsPopupMenu);
                        }

                        if (!textFieldHasFocus && !popupHasFocus) {
                            suggestionsPopupMenu.setVisible(false);
                        }
                    });
                }
            }
        });
    }
    
     private void showSuggestions() {
        String typedText = nhapMaCBTextField.getText().trim().toLowerCase();
        suggestionsPopupMenu.removeAll();

        if (typedText.isEmpty()) {
            suggestionsPopupMenu.setVisible(false);
            return;
        }

        List<String> matched = new ArrayList<>();
        for (String flightCode : allFlightCodesForSuggestion) {
            if (flightCode.toLowerCase().contains(typedText)) {
                matched.add(flightCode);
            }
        }

        if (!matched.isEmpty()) {
            for (String suggestion : matched) {
                JMenuItem menuItem = new JMenuItem(suggestion);
                menuItem.addActionListener(e_menu -> {
                   
                    suggestionsPopupMenu.setVisible(false);

                    final boolean wasEditable = nhapMaCBTextField.isEditable();
                    final boolean wasFocusable = nhapMaCBTextField.isFocusable();
                    nhapMaCBTextField.setEditable(false);
                    nhapMaCBTextField.setFocusable(false);

                    SwingUtilities.invokeLater(() -> {
                        try {
                            isProgrammaticallyUpdatingText = true;
                            nhapMaCBTextField.setText(suggestion);
                        } finally {
                            isProgrammaticallyUpdatingText = false; 

                            nhapMaCBTextField.setFocusable(wasFocusable);
                            nhapMaCBTextField.setEditable(wasEditable);

                            nhapMaCBTextField.requestFocusInWindow();
                            nhapMaCBTextField.setCaretPosition(nhapMaCBTextField.getText().length());
                        }
                    });
                });
                suggestionsPopupMenu.add(menuItem);
            }

            if (nhapMaCBTextField.isShowing() && nhapMaCBTextField.isEnabled()) {
                SwingUtilities.invokeLater(() -> {
                    if (nhapMaCBTextField.isShowing() && suggestionsPopupMenu.getComponentCount() > 0) {
                        if (!nhapMaCBTextField.getText().trim().isEmpty()){
                            suggestionsPopupMenu.show(nhapMaCBTextField, 0, nhapMaCBTextField.getHeight());
                        } else {
                            suggestionsPopupMenu.setVisible(false);
                        }
                    }
                });
            } else {
                suggestionsPopupMenu.setVisible(false);
            }
        } else {
            suggestionsPopupMenu.setVisible(false);
        }
    }
    private void timChuyenBay() {
        if (suggestionsPopupMenu.isVisible()) {
            suggestionsPopupMenu.setVisible(false);
        }
        String maCBTimKiem = nhapMaCBTextField.getText().trim();
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachChuyenBayTable.getModel();
        tableModel.setRowCount(0); 

        if (maCBTimKiem.isEmpty()) {
            for (Object[] rowData : allFlightDataForFiltering) {
                tableModel.addRow(rowData);
            }
             
        } else {
            boolean found = false;
            for (Object[] rowData : allFlightDataForFiltering) {
                if (rowData[0] != null && rowData[0].toString().equalsIgnoreCase(maCBTimKiem)) {
                    tableModel.addRow(rowData);
                    found = true;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến bay với mã: " + maCBTimKiem, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                 for (Object[] rowData : allFlightDataForFiltering) { 
                    tableModel.addRow(rowData);
                 }
            }
        }
    }
    
    
    private void dienThongTinChuyenBayVaoForm(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) DanhSachChuyenBayTable.getModel();

        String maTuyenBay = model.getValueAt(selectedRow, 1).toString();
        String maMayBay = model.getValueAt(selectedRow, 2).toString(); 
        String gioCatCanhStr = model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : null;
        String gioHaCanhStr = model.getValueAt(selectedRow, 4) != null ? model.getValueAt(selectedRow, 4).toString() : null;
        String trangThai = model.getValueAt(selectedRow, 5).toString();
        String giaVe = model.getValueAt(selectedRow, 6).toString();
       
        tuyenBayComboBox.setSelectedItem(maTuyenBay);
        int mayBayIndex = -1;
        for (int i = 0; i < danhSachMaMayBay.size(); i++) {
            if (danhSachMaMayBay.get(i).equals(maMayBay)) {
                mayBayIndex = i;
                break;
            }
        }
        if (mayBayIndex != -1) {
            mayBayComboBox.setSelectedIndex(mayBayIndex);
        } else {
            String loaiMayBayFromDb = "";
            String sqlLoaiMB = "SELECT LOAIMAYBAY FROM MAY_BAY WHERE MAMAYBAY = ?";
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sqlLoaiMB)) {
                ps.setString(1, maMayBay);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        loaiMayBayFromDb = rs.getString("LOAIMAYBAY");
                        mayBayComboBox.setSelectedItem(loaiMayBayFromDb);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        trangThaiComboBox.setSelectedItem(trangThai);
        DateTimeFormatter tableDateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    try {
        if (gioCatCanhStr != null && !gioCatCanhStr.isEmpty()) {
            LocalDateTime catCanhLDT = LocalDateTime.parse(gioCatCanhStr, tableDateTimeFormatter);
            gioCatCanhCalendar.setDateTimePermissive(catCanhLDT);
        } else {
            gioCatCanhCalendar.clear(); 
        }
        if (gioHaCanhStr != null && !gioHaCanhStr.isEmpty()) {
            LocalDateTime haCanhLDT = LocalDateTime.parse(gioHaCanhStr, tableDateTimeFormatter);
            gioHaCanhCalendar.setDateTimePermissive(haCanhLDT);
        } else {
            gioHaCanhCalendar.clear(); 
        }
    } catch (DateTimeParseException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ khi hiển thị từ bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        gioCatCanhCalendar.clear();
        gioHaCanhCalendar.clear();
    }
        

        giaVeTextField.setText(giaVe);
        capNhatSoGheLabelTheoMaMayBay(maMayBay);
        setFieldsEditable(false);
        
    }
    private void capNhatSoGheLabelTheoMaMayBay(String maMayBay) {
        if (maMayBay == null || maMayBay.isEmpty()) {
            soGheLabel.setText("0");
            return;
        }
        String sql = "SELECT SOGHE FROM MAY_BAY WHERE MAMAYBAY = ?";
        int soGhe = 0;
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMayBay);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soGhe = rs.getInt("SOGHE");
                } else {
                    soGhe = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy số ghế: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            soGhe = 0; 
        }
        soGheLabel.setText(String.valueOf(soGhe));
    }


    private void setFieldsEditable(boolean editable) {
        tuyenBayComboBox.setEnabled(editable);
        mayBayComboBox.setEnabled(editable);
        gioCatCanhCalendar.setEnabled(editable); 
        gioHaCanhCalendar.setEnabled(editable); 
        trangThaiComboBox.setEnabled(editable);
        giaVeTextField.setEnabled(editable);
    }
    private void xoaTrangCacTruong() {
        tuyenBayComboBox.setSelectedIndex(-1); 
        mayBayComboBox.setSelectedIndex(-1);
        gioCatCanhCalendar.clear(); 
        gioHaCanhCalendar.clear();   
        trangThaiComboBox.setSelectedIndex(-1); 
        giaVeTextField.setText("0");
        soGheLabel.setText("0");
        nhapMaCBTextField.setText(""); 
        setFieldsEditable(true);
        thoatXemButton.setVisible(false);
    }
    // Thêm hàm này vào trong lớp QLChuyenBayForm
    private boolean isTimeTextValid(String timeText) {
    if (timeText == null || timeText.trim().isEmpty()) {
        return true; 
    }
    String[] parts = timeText.split(":");
    if (parts.length != 2) {
        return false; 
    }
    try {
        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            return false;
        }
        return true;
    } catch (NumberFormatException e) {
        return false; 
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

        nutPanel = new javax.swing.JPanel();
        tuyenBayComboBox = new javax.swing.JComboBox<>();
        tuyenBayLabel = new javax.swing.JLabel();
        mayBayLabel = new javax.swing.JLabel();
        mayBayComboBox = new javax.swing.JComboBox<>();
        gioCatCanhLabel = new javax.swing.JLabel();
        gioHaCanhLabel = new javax.swing.JLabel();
        giaVeLabel = new javax.swing.JLabel();
        soGheTrongLabel = new javax.swing.JLabel();
        giaVeTextField = new javax.swing.JTextField();
        themButton = new javax.swing.JButton();
        xoaButton = new javax.swing.JButton();
        suaButton = new javax.swing.JButton();
        soGheLabel = new javax.swing.JLabel();
        trangThaiLabel = new javax.swing.JLabel();
        trangThaiComboBox = new javax.swing.JComboBox<>();
        xacNhanButton = new javax.swing.JButton();
        tuChoiButton = new javax.swing.JButton();
        thoatXemButton = new javax.swing.JButton();
        gioCatCanhCalendar = new com.github.lgooddatepicker.components.DateTimePicker();
        gioHaCanhCalendar = new com.github.lgooddatepicker.components.DateTimePicker();
        thanhCongCuLabel = new javax.swing.JLabel();
        danhSachPanel = new javax.swing.JPanel();
        nhapMaCBTextField = new javax.swing.JTextField();
        timKiemLabel = new javax.swing.JLabel();
        timButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DanhSachChuyenBayTable = new javax.swing.JTable();
        thanhTimKiemLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setName(""); // NOI18N

        nutPanel.setBackground(new java.awt.Color(255, 255, 255));

        tuyenBayComboBox.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        tuyenBayComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tuyenBayComboBoxActionPerformed(evt);
            }
        });

        tuyenBayLabel.setText("Tuyến Bay");
        tuyenBayLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        mayBayLabel.setText("Máy Bay");
        mayBayLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        mayBayComboBox.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        mayBayComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mayBayComboBoxActionPerformed(evt);
            }
        });

        gioCatCanhLabel.setText("Giờ cất cánh");
        gioCatCanhLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        gioHaCanhLabel.setText("Giờ hạ cánh");
        gioHaCanhLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        giaVeLabel.setText("Giá vé");
        giaVeLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        soGheTrongLabel.setText("Số ghế trống");
        soGheTrongLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        giaVeTextField.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        giaVeTextField.setText("0");
        giaVeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                giaVeTextFieldActionPerformed(evt);
            }
        });

        themButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/add (1) (1).png"))); // NOI18N
        themButton.setText("Thêm");
        themButton.setBackground(new java.awt.Color(51, 204, 255));
        themButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        themButton.setForeground(new java.awt.Color(0, 0, 102));
        themButton.setIconTextGap(10);
        themButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themButtonActionPerformed(evt);
            }
        });

        xoaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/delete (1) (1).png"))); // NOI18N
        xoaButton.setText("Xóa");
        xoaButton.setBackground(new java.awt.Color(51, 204, 255));
        xoaButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        xoaButton.setForeground(new java.awt.Color(0, 0, 102));
        xoaButton.setIconTextGap(10);
        xoaButton.setToolTipText("");
        xoaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoaButtonActionPerformed(evt);
            }
        });

        suaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/pen (1).png"))); // NOI18N
        suaButton.setText("Sửa");
        suaButton.setBackground(new java.awt.Color(51, 204, 255));
        suaButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        suaButton.setForeground(new java.awt.Color(0, 0, 102));
        suaButton.setIconTextGap(10);
        suaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaButtonActionPerformed(evt);
            }
        });

        soGheLabel.setText("0");
        soGheLabel.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        trangThaiLabel.setText("Trạng thái");
        trangThaiLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N

        trangThaiComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang mở", "Đã đóng", "Hoãn", "Hủy" }));
        trangThaiComboBox.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        xacNhanButton.setText("Xác nhận");
        xacNhanButton.setBackground(new java.awt.Color(51, 204, 255));
        xacNhanButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        xacNhanButton.setForeground(new java.awt.Color(0, 0, 102));
        xacNhanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xacNhanButtonActionPerformed(evt);
            }
        });

        tuChoiButton.setText("Từ chối");
        tuChoiButton.setBackground(new java.awt.Color(51, 204, 255));
        tuChoiButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        tuChoiButton.setForeground(new java.awt.Color(0, 0, 102));
        tuChoiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tuChoiButtonActionPerformed(evt);
            }
        });

        thoatXemButton.setText("Thoát chế độ xem");
        thoatXemButton.setBackground(new java.awt.Color(51, 204, 255));
        thoatXemButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        thoatXemButton.setForeground(new java.awt.Color(0, 0, 102));
        thoatXemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thoatXemButtonActionPerformed(evt);
            }
        });

        gioCatCanhCalendar.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N

        gioHaCanhCalendar.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N

        thanhCongCuLabel.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        thanhCongCuLabel.setForeground(new java.awt.Color(0, 51, 102));
        thanhCongCuLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thanhCongCuLabel.setText("Thêm chuyến bay");

        javax.swing.GroupLayout nutPanelLayout = new javax.swing.GroupLayout(nutPanel);
        nutPanel.setLayout(nutPanelLayout);
        nutPanelLayout.setHorizontalGroup(
            nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nutPanelLayout.createSequentialGroup()
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(nutPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nutPanelLayout.createSequentialGroup()
                                .addGap(175, 175, 175)
                                .addComponent(giaVeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(nutPanelLayout.createSequentialGroup()
                                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(mayBayLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(gioCatCanhLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(gioHaCanhLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(giaVeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(trangThaiLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(soGheTrongLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tuyenBayLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(xacNhanButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(45, 45, 45)
                                .addComponent(thoatXemButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tuChoiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addGroup(nutPanelLayout.createSequentialGroup()
                                .addGap(277, 277, 277)
                                .addComponent(soGheLabel))))
                    .addGroup(nutPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(themButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(gioHaCanhCalendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gioCatCanhCalendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(nutPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(xoaButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(suaButton)
                                .addGap(18, 18, 18))
                            .addComponent(tuyenBayComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mayBayComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(trangThaiComboBox, 0, 299, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nutPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(thanhCongCuLabel)
                .addGap(136, 136, 136))
        );
        nutPanelLayout.setVerticalGroup(
            nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nutPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(thanhCongCuLabel)
                .addGap(18, 18, 18)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xacNhanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tuChoiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thoatXemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tuyenBayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tuyenBayLabel))
                .addGap(18, 18, 18)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mayBayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mayBayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gioCatCanhLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gioCatCanhCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gioHaCanhLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gioHaCanhCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trangThaiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trangThaiComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(giaVeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(giaVeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(soGheTrongLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soGheLabel))
                .addGap(44, 44, 44)
                .addGroup(nutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(themButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xoaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(suaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        danhSachPanel.setBackground(new java.awt.Color(255, 255, 255));

        nhapMaCBTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        nhapMaCBTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapMaCBTextFieldActionPerformed(evt);
            }
        });

        timKiemLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timKiemLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/search-interface-symbol (1) (1) (1).png"))); // NOI18N
        timKiemLabel.setBackground(new java.awt.Color(255, 255, 255));
        timKiemLabel.setFont(new java.awt.Font("UTM Aptima", 1, 14)); // NOI18N

        timButton.setText("Tìm");
        timButton.setBackground(new java.awt.Color(0, 255, 255));
        timButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        timButton.setForeground(new java.awt.Color(0, 51, 102));
        timButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timButtonActionPerformed(evt);
            }
        });

        DanhSachChuyenBayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Chuyến", "Tuyến Bay", "Máy Bay", "Giờ Cất Cánh", "Giờ Hạ Cánh", "Trạng Thái", "Giá Vé", "Số Ghế Trống"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        DanhSachChuyenBayTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        DanhSachChuyenBayTable.setForeground(new java.awt.Color(0, 51, 102));
        DanhSachChuyenBayTable.setRowHeight(35);
        jScrollPane1.setViewportView(DanhSachChuyenBayTable);

        thanhTimKiemLabel.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        thanhTimKiemLabel.setForeground(new java.awt.Color(0, 51, 102));
        thanhTimKiemLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thanhTimKiemLabel.setText("Thanh tìm kiếm");

        javax.swing.GroupLayout danhSachPanelLayout = new javax.swing.GroupLayout(danhSachPanel);
        danhSachPanel.setLayout(danhSachPanelLayout);
        danhSachPanelLayout.setHorizontalGroup(
            danhSachPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhSachPanelLayout.createSequentialGroup()
                .addGroup(danhSachPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(danhSachPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE))
                    .addGroup(danhSachPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(timKiemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(nhapMaCBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(timButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(danhSachPanelLayout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(thanhTimKiemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        danhSachPanelLayout.setVerticalGroup(
            danhSachPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhSachPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(thanhTimKiemLabel)
                .addGroup(danhSachPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(danhSachPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(danhSachPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nhapMaCBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, danhSachPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timKiemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(nutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(danhSachPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(317, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(danhSachPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(167, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
 
    
    private void tuyenBayComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tuyenBayComboBoxActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tuyenBayComboBoxActionPerformed

    private void mayBayComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mayBayComboBoxActionPerformed
        // TODO add your handling code here:
        Object selectedItem = mayBayComboBox.getSelectedItem();
        if (selectedItem == null) {
            soGheLabel.setText("0"); 
            return;
        }

        String loaiMayBayDaChon = selectedItem.toString();
        String sql = "SELECT SOGHE FROM MAY_BAY WHERE LOAIMAYBAY = ?";
        int soGhe = 0; 

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMayBayDaChon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soGhe = rs.getInt("SOGHE");
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin số ghế cho loại máy bay: " + loaiMayBayDaChon, "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
        soGheLabel.setText(String.valueOf(soGhe));
    }//GEN-LAST:event_mayBayComboBoxActionPerformed

    private void giaVeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giaVeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_giaVeTextFieldActionPerformed

    
    private void xoaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaButtonActionPerformed
        // TODO add your handling code here:
        currentActionState = ActionState.DELETING;
        xacNhanButton.setVisible(true);
        tuChoiButton.setVisible(true);
        thoatXemButton.setVisible(false);
        setFieldsEditable(false);
        thanhCongCuLabel.setText("Xóa chuyến bay");
        thanhCongCuLabel.setVisible(true);
    }//GEN-LAST:event_xoaButtonActionPerformed

    private void suaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaButtonActionPerformed
        // TODO add your handling code here:
        currentActionState = ActionState.EDITING;
        xacNhanButton.setVisible(true);
        tuChoiButton.setVisible(true);
        thoatXemButton.setVisible(false);
        setFieldsEditable(true);
        tuyenBayComboBox.requestFocusInWindow(); 
        thanhCongCuLabel.setText("Sửa chuyến bay");
        thanhCongCuLabel.setVisible(true);
    }//GEN-LAST:event_suaButtonActionPerformed

    private void nhapMaCBTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapMaCBTextFieldActionPerformed
        // TODO add your handling code here:
                timChuyenBay();
    }//GEN-LAST:event_nhapMaCBTextFieldActionPerformed

    private void timButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timButtonActionPerformed
        // TODO add your handling code here:
                timChuyenBay();
    }//GEN-LAST:event_timButtonActionPerformed

    private void thoatXemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoatXemButtonActionPerformed
        // TODO add your handling code here:
                DanhSachChuyenBayTable.clearSelection();
                resetUIStateToDefault();
    }//GEN-LAST:event_thoatXemButtonActionPerformed

    private void xacNhanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xacNhanButtonActionPerformed
        // TODO add your handling code here:
        if (maChuyenBayDangThaoTac == null && (currentActionState == ActionState.DELETING || currentActionState == ActionState.EDITING)) {
        JOptionPane.showMessageDialog(this, "Lỗi: Không có chuyến bay nào được chọn để thực hiện hành động.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        resetUIStateToDefault(); 
        return;
        }

        if (currentActionState == ActionState.DELETING) {
            int confirmDialogResult = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa chuyến bay '" + maChuyenBayDangThaoTac + "' không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirmDialogResult == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM CHUYEN_BAY WHERE MACHUYENBAY = ?";
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maChuyenBayDangThaoTac);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa chuyến bay '" + maChuyenBayDangThaoTac + "' thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    themDuLieuChuyenBayTable(); 
                    xoaTrangCacTruong();       
                    DanhSachChuyenBayTable.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa chuyến bay thất bại. Không tìm thấy chuyến bay.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (e.getErrorCode() == 2292) { 
                    JOptionPane.showMessageDialog(this, "Không thể xóa chuyến bay '" + maChuyenBayDangThaoTac + "' vì có vé liên quan. Vui lòng xóa vé trước.", "Lỗi ràng buộc dữ liệu", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa chuyến bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
                }
            }
        } 
            } else if (currentActionState == ActionState.EDITING) {
                int confirmDialogResult = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn lưu các thay đổi cho chuyến bay '" + maChuyenBayDangThaoTac + "' không?",
                    "Xác nhận sửa", JOptionPane.YES_NO_OPTION);

                if (confirmDialogResult == JOptionPane.YES_OPTION) {
                    String maTuyenBayDaChon = (String) tuyenBayComboBox.getSelectedItem();
                    String maMayBayDaChon = layMaMayBay(); 
                     LocalDateTime gioCatCanhLDT = gioCatCanhCalendar.getDateTimePermissive();
    LocalDateTime gioHaCanhLDT = gioHaCanhCalendar.getDateTimePermissive();
    LocalDateTime thoiGianHienTaiLDT = LocalDateTime.now();
        
                    String trangThai = (String) trangThaiComboBox.getSelectedItem();
                    String giaVeStr = giaVeTextField.getText();
                    int soGheTrongMoi;
                    if (maTuyenBayDaChon == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn tuyến bay.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE); return; 
                    }
                if (maMayBayDaChon == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn máy bay.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE); return;
                    }
                if (gioCatCanhLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ cất cánh.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gioCatCanhLDT.isBefore(thoiGianHienTaiLDT)) {
        JOptionPane.showMessageDialog(this, "Giờ cất cánh phải sau hoặc bằng thời điểm hiện tại.", "Lỗi thời gian", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
        if (gioHaCanhLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ hạ cánh.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gioHaCanhLDT.isBefore(gioCatCanhLDT) || gioHaCanhLDT.isEqual(gioCatCanhLDT)) {
            JOptionPane.showMessageDialog(this, "Giờ hạ cánh phải sau giờ cất cánh.", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
            return;
        }
                double giaVe;
                try {
                    giaVe = Double.parseDouble(giaVeStr);
                    if (giaVe <= 0) {
                    JOptionPane.showMessageDialog(this, "Giá vé phải là một số dương.", "Lỗi giá vé", JOptionPane.ERROR_MESSAGE); return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Vui lòng nhập số.", "Lỗi giá vé", JOptionPane.ERROR_MESSAGE); return;
                }
                if (trangThai == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái chuyến bay.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE); return;
                }
                int soGheCuaMayBayMoi;
                    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement psSoGhe = con.prepareStatement("SELECT SOGHE FROM MAY_BAY WHERE MAMAYBAY = ?")) {
                        psSoGhe.setString(1, maMayBayDaChon);
                        try (ResultSet rsSoGhe = psSoGhe.executeQuery()) {
                            if (rsSoGhe.next()) {
                            soGheCuaMayBayMoi = rsSoGhe.getInt("SOGHE");
                            } else {
                            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin máy bay: " + maMayBayDaChon, "Lỗi", JOptionPane.ERROR_MESSAGE); return;
                            }
                }
                } catch (SQLException e) {
                    e.printStackTrace(); JOptionPane.showMessageDialog(this, "Lỗi khi lấy số ghế máy bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE); return;
                }

                int soVeDaBan = 0;
                    String sqlSoVe = "SELECT COUNT(*) FROM VE WHERE MACHUYENBAY = ?";
                    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement psVe = con.prepareStatement(sqlSoVe)) {
                        psVe.setString(1, maChuyenBayDangThaoTac);
                    try (ResultSet rsVe = psVe.executeQuery()) {
                        if (rsVe.next()) {
                        soVeDaBan = rsVe.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                    soGheTrongMoi = soGheCuaMayBayMoi - soVeDaBan;
            if (soGheTrongMoi < 0) { 
                JOptionPane.showMessageDialog(this, "Không thể đổi sang máy bay này. Số ghế ("+soGheCuaMayBayMoi+") không đủ cho số vé đã bán ("+soVeDaBan+").", "Lỗi", JOptionPane.ERROR_MESSAGE);
                soGheTrongMoi = 0; 
            }
            Timestamp gioCatCanhSQL = Timestamp.valueOf(gioCatCanhLDT);
            Timestamp gioHaCanhSQL = Timestamp.valueOf(gioHaCanhLDT);
            String sql = "UPDATE CHUYEN_BAY SET MATUYENBAY = ?, MAMAYBAY = ?, GIOCATCANH = ?, GIOHACANH = ?, TRANGTHAI = ?, GIAVE = ?, SOGHETRONG = ? WHERE MACHUYENBAY = ?";
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, maTuyenBayDaChon);
                ps.setString(2, maMayBayDaChon);
                ps.setTimestamp(3, gioCatCanhSQL);
                ps.setTimestamp(4, gioHaCanhSQL);
                ps.setString(5, trangThai);
                ps.setDouble(6, giaVe);
                ps.setInt(7, soGheTrongMoi); 
                ps.setString(8, maChuyenBayDangThaoTac);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật chuyến bay '" + maChuyenBayDangThaoTac + "' thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    themDuLieuChuyenBayTable(); 
                    int updatedRowIndex = -1;
                    for (int i = 0; i < DanhSachChuyenBayTable.getRowCount(); i++) {
                        if (DanhSachChuyenBayTable.getValueAt(i, 0).equals(maChuyenBayDangThaoTac)) {
                            updatedRowIndex = i;
                            break;
                        }
                    }
                    if (updatedRowIndex != -1) {
                        DanhSachChuyenBayTable.setRowSelectionInterval(updatedRowIndex, updatedRowIndex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật chuyến bay thất bại hoặc không có thông tin nào được thay đổi.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return; 
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật chuyến bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        } else { 
            if (maChuyenBayDangThaoTac != null) {
                int selectedRow = -1;
                 for (int i = 0; i < DanhSachChuyenBayTable.getRowCount(); i++) {
                    if (DanhSachChuyenBayTable.getValueAt(i, 0).equals(maChuyenBayDangThaoTac)) {
                        selectedRow = i;
                        break;
                    }
                }
                if (selectedRow != -1) {
                    dienThongTinChuyenBayVaoForm(selectedRow); 
                }
            }
        }
    }
            resetUIStateToDefault();
    }//GEN-LAST:event_xacNhanButtonActionPerformed

    private void tuChoiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tuChoiButtonActionPerformed
        // TODO add your handling code here:
        if (currentActionState == ActionState.EDITING && maChuyenBayDangThaoTac != null) {
        int selectedRow = -1; // Tìm lại dòng đang được chọn
        for (int i = 0; i < DanhSachChuyenBayTable.getRowCount(); i++) {
            if (DanhSachChuyenBayTable.getValueAt(i, 0) != null &&
                DanhSachChuyenBayTable.getValueAt(i, 0).equals(maChuyenBayDangThaoTac)) {
                selectedRow = i;
                break;
            }
        }
        if (selectedRow != -1) {
            dienThongTinChuyenBayVaoForm(selectedRow); 
        } else {
          
        }
    }
            resetUIStateToDefault();
    }//GEN-LAST:event_tuChoiButtonActionPerformed

    private void themButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themButtonActionPerformed
        // TODO add your handling code here:
        xacNhanButton.setVisible(false);
        tuChoiButton.setVisible(false);
        thoatXemButton.setVisible(false);
        String tenSequenceSQL = "SEQ_CHUYENBAY";
        String tienToMaChuyenBay = "CB";
        String maTuyenBayDaChon = (String) tuyenBayComboBox.getSelectedItem();
        String maMayBayDaChon = layMaMayBay();
        String catCanhTimeText = gioCatCanhCalendar.getTimePicker().getText();
        if (catCanhTimeText != null && !catCanhTimeText.trim().isEmpty()) {
            if (!isTimeTextValid(catCanhTimeText)) {
                JOptionPane.showMessageDialog(this, "Giờ cất cánh nhập vào không hợp lệ (HH:mm, giờ 00-23, phút 00-59).", "Lỗi giờ cất cánh", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        String haCanhTimeText = gioHaCanhCalendar.getTimePicker().getText();
        if (haCanhTimeText != null && !haCanhTimeText.trim().isEmpty()) {
            if (!isTimeTextValid(haCanhTimeText)) {
                JOptionPane.showMessageDialog(this, "Giờ hạ cánh nhập vào không hợp lệ (HH:mm, giờ 00-23, phút 00-59).", "Lỗi giờ cất cánh", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        LocalDateTime gioCatCanhLDT = gioCatCanhCalendar.getDateTimePermissive();
        LocalDateTime gioHaCanhLDT = gioHaCanhCalendar.getDateTimePermissive();
        LocalDateTime thoiGianHienTaiLDT = LocalDateTime.now();
        if (gioCatCanhLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ cất cánh.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gioCatCanhLDT.isBefore(thoiGianHienTaiLDT)) {
            JOptionPane.showMessageDialog(this, "Giờ cất cánh phải sau hoặc bằng thời điểm hiện tại.", "Lỗi thời gian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gioHaCanhLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ hạ cánh.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gioHaCanhLDT.isBefore(gioCatCanhLDT) || gioHaCanhLDT.isEqual(gioCatCanhLDT)) {
            JOptionPane.showMessageDialog(this, "Giờ hạ cánh phải sau giờ cất cánh.", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String giaVeStr = giaVeTextField.getText();
        double giaVe;
        try {
            giaVe = Double.parseDouble(giaVeStr);
            if (giaVe <= 0) {
                JOptionPane.showMessageDialog(this, "Giá vé phải là một số dương.", "Lỗi giá vé", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ.", "Lỗi giá vé", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int soGheTrongBanDau = Integer.parseInt(soGheLabel.getText());
        Timestamp gioCatCanhSQL = Timestamp.valueOf(gioCatCanhLDT);
        Timestamp gioHaCanhSQL = Timestamp.valueOf(gioHaCanhLDT);
        String maChuyenBayMoi;
        String sql = "INSERT INTO CHUYEN_BAY(MACHUYENBAY, MATUYENBAY, MAMAYBAY, GIOCATCANH, GIOHACANH, TRANGTHAI, GIAVE, SOGHETRONG) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            long nextSeqVal;
            String sqlGetSeq = "SELECT " + tenSequenceSQL + ".NEXTVAL FROM DUAL";
            try (PreparedStatement psSeq = con.prepareStatement(sqlGetSeq);
                ResultSet rsSeq = psSeq.executeQuery()) {
                if (rsSeq.next()) {
                    nextSeqVal = rsSeq.getLong(1);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể lấy giá trị từ sequence " + tenSequenceSQL + ".", "Lỗi Sequence", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            String formattedSeqNumber = String.format("%03d", nextSeqVal);
            if (!tienToMaChuyenBay.isEmpty()) {
                maChuyenBayMoi = tienToMaChuyenBay + formattedSeqNumber;
            } else {
                maChuyenBayMoi = formattedSeqNumber;
            }
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maChuyenBayMoi);
                ps.setString(2, maTuyenBayDaChon);
                ps.setString(3, maMayBayDaChon);
                ps.setTimestamp(4, gioCatCanhSQL);
                ps.setTimestamp(5, gioHaCanhSQL);
                ps.setString(6, "Đang mở");
                ps.setDouble(7, giaVe);
                ps.setInt(8, soGheTrongBanDau);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm chuyến bay thành công! Mã chuyến bay là: " + maChuyenBayMoi, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    themDuLieuChuyenBayTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm chuyến bay thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm chuyến bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_themButtonActionPerformed
    
    private void resetUIStateToDefault() {
    currentActionState = ActionState.NONE;
    xacNhanButton.setVisible(false);
    xacNhanButton.setText("Xác nhận"); 
    tuChoiButton.setVisible(false);
    themButton.setVisible(true); 

    if (DanhSachChuyenBayTable.getSelectedRow() != -1) {
        xoaButton.setEnabled(true);
        suaButton.setEnabled(true);
        thoatXemButton.setVisible(true);
        themButton.setVisible(false); 
        thanhCongCuLabel.setVisible(false);
        setFieldsEditable(false);     
    } else {
        xoaButton.setEnabled(false);
        suaButton.setEnabled(false);
        thoatXemButton.setVisible(false);
        setFieldsEditable(true); 
        maChuyenBayDangThaoTac = null; 
        thanhCongCuLabel.setText("Thêm chuyến bay");
        thanhCongCuLabel.setVisible(true);
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DanhSachChuyenBayTable;
    private javax.swing.JPanel danhSachPanel;
    private javax.swing.JLabel giaVeLabel;
    private javax.swing.JTextField giaVeTextField;
    private com.github.lgooddatepicker.components.DateTimePicker gioCatCanhCalendar;
    private javax.swing.JLabel gioCatCanhLabel;
    private com.github.lgooddatepicker.components.DateTimePicker gioHaCanhCalendar;
    private javax.swing.JLabel gioHaCanhLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> mayBayComboBox;
    private javax.swing.JLabel mayBayLabel;
    private javax.swing.JTextField nhapMaCBTextField;
    private javax.swing.JPanel nutPanel;
    private javax.swing.JLabel soGheLabel;
    private javax.swing.JLabel soGheTrongLabel;
    private javax.swing.JButton suaButton;
    private javax.swing.JLabel thanhCongCuLabel;
    private javax.swing.JLabel thanhTimKiemLabel;
    private javax.swing.JButton themButton;
    private javax.swing.JButton thoatXemButton;
    private javax.swing.JButton timButton;
    private javax.swing.JLabel timKiemLabel;
    private javax.swing.JComboBox<String> trangThaiComboBox;
    private javax.swing.JLabel trangThaiLabel;
    private javax.swing.JButton tuChoiButton;
    private javax.swing.JComboBox<String> tuyenBayComboBox;
    private javax.swing.JLabel tuyenBayLabel;
    private javax.swing.JButton xacNhanButton;
    private javax.swing.JButton xoaButton;
    // End of variables declaration//GEN-END:variables
}
