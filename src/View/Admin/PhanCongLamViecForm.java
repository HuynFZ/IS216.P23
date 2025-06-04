
package View.Admin;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


public class PhanCongLamViecForm extends javax.swing.JPanel {

    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String DB_USER = "c##QLHHK";
    private final String DB_PASSWORD = "Admin123";
    private JPopupMenu suggestionsPopupMenu;
    private List<String> allFlightCodesForSuggestion = new ArrayList<>();  
    private List<Object[]> allFlightDataForFiltering = new ArrayList<>();
    private boolean isProgrammaticallyUpdatingText = false;

    private JPopupMenu suggestionsPopupMenu1;
    private List<String> allFlightCodesForSuggestion1 = new ArrayList<>();  
    private List<Object[]> allFlightDataForFiltering1 = new ArrayList<>();
    private boolean isProgrammaticallyUpdatingText1 = false;
    
    private JPopupMenu suggestionsPopupMenu_LichBay; 
    private List<String> allEmployeeCodesForSuggestion_LichBay = new ArrayList<>(); 
    private List<String> allFlightCodesForSuggestion_LichBay = new ArrayList<>();   
    private List<Object[]> allLichBayDataForFiltering = new ArrayList<>(); 
    private boolean isProgrammaticallyUpdatingText_LichBay = false; 
    
    private String currentSelectedMaSanBay; 
    private String currentSelectedAirportCity_Truc;
    
    private List<Object[]> allLichTrucDataForFiltering = new ArrayList<>(); 
    private List<String> allEmployeeCodesForSuggestion_LichTruc = new ArrayList<>();
    private List<String> allSanBayCodesForSuggestion_LichTruc = new ArrayList<>();
    private JPopupMenu suggestionsPopupMenu_LichTruc;
    private boolean isProgrammaticallyUpdatingText_LichTruc = false;

    public PhanCongLamViecForm() {
        initComponents();
        menuPanel.setVisible(true);
        phanCongTrucPanel.setVisible(false);
        phanCongBayPanel.setVisible(false);
        mainPanel2.setVisible(false);
        suggestionsPopupMenu = new JPopupMenu();
        suggestionsPopupMenu.setFocusable(false); 
        
        suggestionsPopupMenu1 = new JPopupMenu();
        suggestionsPopupMenu1.setFocusable(false); 
        
        suggestionsPopupMenu_LichBay = new JPopupMenu(); 
        suggestionsPopupMenu_LichBay.setFocusable(false);
        
        suggestionsPopupMenu_LichTruc = new JPopupMenu(); 
        suggestionsPopupMenu_LichTruc.setFocusable(false);
        
        
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
                    default: return Object.class;
                }
            }
        };

        DefaultTableModel currentModel = (DefaultTableModel) DanhSachSanBayTable.getModel();
        for (int i = 0; i < currentModel.getColumnCount(); i++) {
            readOnlyTableModel.addColumn(currentModel.getColumnName(i));
        }

        DanhSachSanBayTable.setModel(readOnlyTableModel);
        
        
        
        DefaultTableModel readOnlyTableModel1 = new DefaultTableModel() {
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

        DefaultTableModel currentModel1 = (DefaultTableModel) DanhSachChuyenBayTable.getModel();
        for (int i = 0; i < currentModel1.getColumnCount(); i++) {
            readOnlyTableModel1.addColumn(currentModel1.getColumnName(i));
        }
        
        DanhSachChuyenBayTable.setModel(readOnlyTableModel1);
        
        
        DefaultTableModel currentLichModel = (DefaultTableModel) LichPhanCongBayTable.getModel();
            final String[] lichPhanCongColumnNames = new String[currentLichModel.getColumnCount()];
            for (int i = 0; i < currentLichModel.getColumnCount(); i++) {
            lichPhanCongColumnNames[i] = currentLichModel.getColumnName(i);
        }

        DefaultTableModel readOnlyLichPhanCongModel = new DefaultTableModel(new Object[][]{}, lichPhanCongColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
            return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
            String columnName = LichPhanCongBayTable.getColumnName(columnIndex);
            if (columnName.equals("Mã Nhân Viên") || columnName.equals("Mã Chuyến Bay") ||
                columnName.equals("Trạng Thái") || columnName.equals("Vai Trò") ||
                columnName.equals("Thời Gian Bắt Đầu") || columnName.equals("Thời Gian Kết Thúc")) {
                return String.class;
            }
            return Object.class;
        }
        };
        LichPhanCongBayTable.setModel(readOnlyLichPhanCongModel);
        
        DefaultTableModel currentLichTrucModel = (DefaultTableModel) LichPhanCongTrucTable.getModel();
            final String[] lichPhanCongTrucColumnNames = new String[currentLichTrucModel.getColumnCount()];
            for (int i = 0; i < currentLichTrucModel.getColumnCount(); i++) {
            lichPhanCongTrucColumnNames[i] = currentLichTrucModel.getColumnName(i);
        }
        DefaultTableModel readOnlyLichPhanCongTrucModel = new DefaultTableModel(new Object[][]{}, lichPhanCongTrucColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
            return false; 
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
            String columnName = LichPhanCongTrucTable.getColumnName(columnIndex);
            if (columnName.equals("Mã Nhân Viên") || columnName.equals("Mã Sân Bay") ||
                columnName.equals("Trạng Thái") || columnName.equals("Vai Trò") ||
                columnName.equals("Thời Gian Bắt Đầu") || columnName.equals("Thời Gian Kết Thúc")) {
                return String.class;
            }
            return Object.class;
        }
        };
        LichPhanCongTrucTable.setModel(readOnlyLichPhanCongTrucModel);
        
        nhanVienComboBox.removeAllItems();
        hienTenNhanVienLabel.setText("Tên nhân viên");
        hienVaiTroLabel.setText("Tên vai trò");
        hienDiaChiLabel.setText("Địa chỉ");
        tenSanBayLabel.setText("Tên sân bay");
        
        themDuLieuEmployeeComboBox();
        themDuLieuDanhSachSanBayTable();
        themDuLieuChuyenBayTable();
        themDuLieuPhanCongBayTable();
        themDuLieuPhanCongTrucTable();
        
        initSearchSuggestions(); 
        initSearchSuggestions1();
        initSearchSuggestions_LichBay(); 
        initSearchSuggestions_LichTruc();
        
        DanhSachSanBayTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && DanhSachSanBayTable.getSelectedRow() != -1) {
                    int selectedRow = DanhSachSanBayTable.getSelectedRow();

                    dienTenSanBayVaoForm(selectedRow);
                    exitButton.setVisible(false);
                   
                    themButton.setVisible(true);
                    
                    setFieldsEditable(true);
                }
            }
        });
        DanhSachChuyenBayTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && DanhSachChuyenBayTable.getSelectedRow() != -1) {
                    int selectedRow = DanhSachChuyenBayTable.getSelectedRow();

                    dienMaChuyenBayVaoForm(selectedRow);
                    thoatXemButton.setVisible(false);
                  
                    themButton.setVisible(true);
                   
                    setFieldsEditable(true);
                }
            }
        });
        LichPhanCongBayTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        @Override
        public void valueChanged(javax.swing.event.ListSelectionEvent event) {
            if (!event.getValueIsAdjusting() && LichPhanCongBayTable.getSelectedRow() != -1) {
                int selectedRow = LichPhanCongBayTable.getSelectedRow();
                dienThongTinLichBayVaoForm(selectedRow);
                themButton.setVisible(false); 
               
                thoatXemButton.setVisible(true); 
            }
        }
    });
        LichPhanCongTrucTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        @Override
        public void valueChanged(javax.swing.event.ListSelectionEvent event) {
            if (!event.getValueIsAdjusting() && LichPhanCongTrucTable.getSelectedRow() != -1) {
                int selectedRow = LichPhanCongTrucTable.getSelectedRow();
                dienThongTinLichTrucVaoForm(selectedRow);
                themButton.setVisible(false); 
               
                thoatXemButton.setVisible(true); 
            }
        }
    });
    }
    
    private void themDuLieuNhanVienComboBox(String airportCity){
         nhanVienComboBox.removeAllItems(); 
    hienTenNhanVienLabel.setText("Tên nhân viên");
    hienVaiTroLabel.setText("Tên vai trò");
    hienDiaChiLabel.setText("Địa chỉ");

    if (airportCity == null || airportCity.trim().isEmpty()) {
       return;
    }

    String sql = "SELECT MANHANVIEN FROM NHAN_VIEN WHERE CHUCVU IN ('Nhân viên bảo vệ', 'Nhân viên thủ tục') AND UPPER(DIACHI) LIKE UPPER(?)";

    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "%" + airportCity.trim() + "%"); 

        try (ResultSet rs = ps.executeQuery()) {
            boolean foundEmployees = false;
            while (rs.next()) {
                nhanVienComboBox.addItem(rs.getString("MANHANVIEN"));
                foundEmployees = true;
            }

            if (!foundEmployees) {
                JOptionPane.showMessageDialog(this, "Không có nhân viên ('Nhân viên thủ tục' hoặc 'Nhân viên bảo vệ') nào tại khu vực '" + airportCity + "'.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                 nhanVienComboBox.setSelectedIndex(-1); 
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách nhân viên theo sân bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    }
    }
    private void themDuLieuEmployeeComboBox(){
    employeeComboBox.removeAllItems(); 
    String sql = "SELECT MANHANVIEN FROM NHAN_VIEN WHERE CHUCVU IN ('Phi công', 'Tiếp viên')"; 
    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        boolean found = false;
        while (rs.next()) {
            employeeComboBox.addItem(rs.getString("MANHANVIEN"));
            found = true;
        }

        if (found && employeeComboBox.getItemCount() > 0) {
            employeeComboBox.setSelectedIndex(-1);
             showEmployeeNameLabel.setText("Tên nhân viên"); 
             showRoleLabel.setText("Tên vai trò");     
        } else {
            employeeComboBox.setSelectedItem(null); 
            showEmployeeNameLabel.setText("Tên nhân viên");
            showRoleLabel.setText("Tên vai trò");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách nhân viên cho phân công bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    }
    }
    private void themDuLieuDanhSachSanBayTable(){
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachSanBayTable.getModel();
        tableModel.setRowCount(0);
        allFlightCodesForSuggestion.clear(); 
        allFlightDataForFiltering.clear(); 
        
        String sql = "SELECT MASANBAY, TENSANBAY, TINHTHANHPHO, QUOCGIA FROM SAN_BAY";
        try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                String maSanBay = rs.getString("MASANBAY");
                String tenSanBay = rs.getString("TENSANBAY");
                String tinhThanhPho = rs.getString("TINHTHANHPHO");
                String quocGia = rs.getString("QUOCGIA");
                Object[] rowData = new Object[]{
                    maSanBay,
                    tenSanBay,
                    tinhThanhPho,
                    quocGia
                };
                tableModel.addRow(rowData);
                allFlightCodesForSuggestion.add(tenSanBay); 
                allFlightDataForFiltering.add(rowData);
                }
                } catch (SQLException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
    }
    
     private void themDuLieuChuyenBayTable(){
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachChuyenBayTable.getModel();
        tableModel.setRowCount(0);
        
        allFlightCodesForSuggestion1.clear(); 
        allFlightDataForFiltering1.clear();
        
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
                allFlightCodesForSuggestion1.add(maChuyenBay); 
                allFlightDataForFiltering1.add(rowData);
                }
                } catch (SQLException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
    }
    
    private void themDuLieuPhanCongBayTable(){
        DefaultTableModel tableModel = (DefaultTableModel) LichPhanCongBayTable.getModel();
        tableModel.setRowCount(0); 
        allLichBayDataForFiltering.clear();
        allEmployeeCodesForSuggestion_LichBay.clear();
        allFlightCodesForSuggestion_LichBay.clear();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        String sql = "SELECT MANHANVIEN, MACHUYENBAY, TRANGTHAI, VAITRO, THOIGIANBATDAU, THOIGIANKETTHUC FROM PHAN_CONG_BAY ORDER BY THOIGIANBATDAU DESC";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String maNhanVien = rs.getString("MANHANVIEN");
            String maChuyenBay = rs.getString("MACHUYENBAY");
            String trangThai = rs.getString("TRANGTHAI");
            String vaiTro = rs.getString("VAITRO");
            Timestamp thoiGianBatDauTS = rs.getTimestamp("THOIGIANBATDAU");
            Timestamp thoiGianKetThucTS = rs.getTimestamp("THOIGIANKETTHUC");

            String thoiGianBatDauStr = (thoiGianBatDauTS != null) ? sdf.format(thoiGianBatDauTS) : "";
            String thoiGianKetThucStr = (thoiGianKetThucTS != null) ? sdf.format(thoiGianKetThucTS) : "";
            
            Object[] rowData = {
                maNhanVien,
                maChuyenBay,
                trangThai,
                vaiTro,
                thoiGianBatDauStr,
                thoiGianKetThucStr
            };
            tableModel.addRow(rowData);
             allLichBayDataForFiltering.add(rowData); 
            if (maNhanVien != null && !maNhanVien.trim().isEmpty() && !allEmployeeCodesForSuggestion_LichBay.contains(maNhanVien)) {
                allEmployeeCodesForSuggestion_LichBay.add(maNhanVien);
            }
            if (maChuyenBay != null && !maChuyenBay.trim().isEmpty() && !allFlightCodesForSuggestion_LichBay.contains(maChuyenBay)) {
                allFlightCodesForSuggestion_LichBay.add(maChuyenBay);
            }
        }
        Collections.sort(allEmployeeCodesForSuggestion_LichBay, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(allFlightCodesForSuggestion_LichBay, String.CASE_INSENSITIVE_ORDER);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu lịch phân công bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    }
    }

    private void themDuLieuPhanCongTrucTable() {
    DefaultTableModel tableModel = (DefaultTableModel) LichPhanCongTrucTable.getModel();
    tableModel.setRowCount(0); 

    allLichTrucDataForFiltering.clear();
    allEmployeeCodesForSuggestion_LichTruc.clear();
    allSanBayCodesForSuggestion_LichTruc.clear(); 

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
   String sql = "SELECT MANHANVIEN, MASANBAY, TRANGTHAI, VAITRO, THOIGIANBATDAU, THOIGIANKETTHUC " +
                 "FROM PHAN_CONG_CA_TRUC ORDER BY THOIGIANBATDAU DESC";

    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String maNhanVien = rs.getString("MANHANVIEN");
            String maSanBay = rs.getString("MASANBAY"); 
            String trangThai = rs.getString("TRANGTHAI");
            String vaiTro = rs.getString("VAITRO");
            Timestamp thoiGianBatDauTS = rs.getTimestamp("THOIGIANBATDAU");
            Timestamp thoiGianKetThucTS = rs.getTimestamp("THOIGIANKETTHUC");

            String thoiGianBatDauStr = (thoiGianBatDauTS != null) ? sdf.format(thoiGianBatDauTS) : "";
            String thoiGianKetThucStr = (thoiGianKetThucTS != null) ? sdf.format(thoiGianKetThucTS) : "";

            Object[] rowData = {
                maNhanVien,
                maSanBay,       
                trangThai,
                vaiTro,
                thoiGianBatDauStr,
                thoiGianKetThucStr
            };
            tableModel.addRow(rowData);
            allLichTrucDataForFiltering.add(rowData); 
            if (maNhanVien != null && !maNhanVien.trim().isEmpty() && !allEmployeeCodesForSuggestion_LichTruc.contains(maNhanVien)) {
                allEmployeeCodesForSuggestion_LichTruc.add(maNhanVien);
            }
            if (maSanBay != null && !maSanBay.trim().isEmpty() && !allSanBayCodesForSuggestion_LichTruc.contains(maSanBay)) {
                allSanBayCodesForSuggestion_LichTruc.add(maSanBay); // Use maSanBay
            }
        }
        Collections.sort(allEmployeeCodesForSuggestion_LichTruc, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(allSanBayCodesForSuggestion_LichTruc, String.CASE_INSENSITIVE_ORDER);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu lịch phân công trực: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    }
}
    private void initSearchSuggestions() {
       nhapTenSBTextField.getDocument().addDocumentListener(new DocumentListener() {
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

        nhapTenSBTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    suggestionsPopupMenu.setVisible(false);
                    timSanBay();
                    e.consume(); 
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionsPopupMenu.setVisible(false);
                    e.consume();
                }
            }
        });

        nhapTenSBTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Component oppositeComponent = e.getOppositeComponent();
                if (oppositeComponent == null || !SwingUtilities.isDescendingFrom(oppositeComponent, suggestionsPopupMenu)) {
                    SwingUtilities.invokeLater(() -> {
                        boolean textFieldHasFocus = nhapTenSBTextField.isFocusOwner();
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
        String typedText = nhapTenSBTextField.getText().trim().toLowerCase();
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

                    final boolean wasEditable = nhapTenSBTextField.isEditable();
                    final boolean wasFocusable = nhapTenSBTextField.isFocusable();
                    nhapTenSBTextField.setEditable(false);
                    nhapTenSBTextField.setFocusable(false);

                    SwingUtilities.invokeLater(() -> {
                        try {
                            isProgrammaticallyUpdatingText = true;
                            nhapTenSBTextField.setText(suggestion);
                        } finally {
                            isProgrammaticallyUpdatingText = false; 

                            nhapTenSBTextField.setFocusable(wasFocusable);
                            nhapTenSBTextField.setEditable(wasEditable);

                            nhapTenSBTextField.requestFocusInWindow();
                            nhapTenSBTextField.setCaretPosition(nhapTenSBTextField.getText().length());
                        }
                    });
                });
                suggestionsPopupMenu.add(menuItem);
            }

            if (nhapTenSBTextField.isShowing() && nhapTenSBTextField.isEnabled()) {
                SwingUtilities.invokeLater(() -> {
                    if (nhapTenSBTextField.isShowing() && suggestionsPopupMenu.getComponentCount() > 0) {
                        if (!nhapTenSBTextField.getText().trim().isEmpty()){
                            suggestionsPopupMenu.show(nhapTenSBTextField, 0, nhapTenSBTextField.getHeight());
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
    private void timSanBay() {
        if (suggestionsPopupMenu.isVisible()) {
            suggestionsPopupMenu.setVisible(false);
        }
        String tenSBTimKiem = nhapTenSBTextField.getText().trim();
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachSanBayTable.getModel();
        tableModel.setRowCount(0); 

        if (tenSBTimKiem.isEmpty()) {
            for (Object[] rowData : allFlightDataForFiltering) {
                tableModel.addRow(rowData);
            }
             
        } else {
            boolean found = false;
            for (Object[] rowData : allFlightDataForFiltering) {
                if (rowData[1] != null && rowData[1].toString().equalsIgnoreCase(tenSBTimKiem)) {
                    tableModel.addRow(rowData);
                    found = true;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sân bay với tên: " + tenSBTimKiem, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                 for (Object[] rowData : allFlightDataForFiltering) { 
                    tableModel.addRow(rowData);
                 }
            }
        }
    }
    
    private void dienTenSanBayVaoForm(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) DanhSachSanBayTable.getModel();
        String maSanBay = model.getValueAt(selectedRow, 0).toString();
        String tenSanBay = model.getValueAt(selectedRow, 1).toString();
        String tinhThanhPhoSanBay = model.getValueAt(selectedRow, 2).toString();

        tenSanBayLabel.setText(tenSanBay);
            currentSelectedMaSanBay = maSanBay; 
                currentSelectedAirportCity_Truc = tinhThanhPhoSanBay; 
                themDuLieuNhanVienComboBox(currentSelectedAirportCity_Truc);
    }
    
    
    
    
    private void initSearchSuggestions1() {
       nhapMaCBTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if (!isProgrammaticallyUpdatingText1) {
                    showSuggestions1();
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
                    suggestionsPopupMenu1.setVisible(false);
                    timChuyenBay();
                    e.consume(); 
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionsPopupMenu1.setVisible(false);
                    e.consume();
                }
            }
        });

        nhapMaCBTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Component oppositeComponent = e.getOppositeComponent();
                if (oppositeComponent == null || !SwingUtilities.isDescendingFrom(oppositeComponent, suggestionsPopupMenu1)) {
                    SwingUtilities.invokeLater(() -> {
                        boolean textFieldHasFocus = nhapMaCBTextField.isFocusOwner();
                        boolean popupHasFocus = false;
                        Component currentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                        if (currentFocusOwner != null) {
                            popupHasFocus = SwingUtilities.isDescendingFrom(currentFocusOwner, suggestionsPopupMenu1);
                        }

                        if (!textFieldHasFocus && !popupHasFocus) {
                            suggestionsPopupMenu1.setVisible(false);
                        }
                    });
                }
            }
        });
    }
    
     private void showSuggestions1() {
        String typedText = nhapMaCBTextField.getText().trim().toLowerCase();
        suggestionsPopupMenu1.removeAll();

        if (typedText.isEmpty()) {
            suggestionsPopupMenu1.setVisible(false);
            return;
        }

        List<String> matched = new ArrayList<>();
        for (String flightCode : allFlightCodesForSuggestion1) {
            if (flightCode.toLowerCase().contains(typedText)) {
                matched.add(flightCode);
            }
        }

        if (!matched.isEmpty()) {
            for (String suggestion : matched) {
                JMenuItem menuItem = new JMenuItem(suggestion);
                menuItem.addActionListener(e_menu -> {
                   
                    suggestionsPopupMenu1.setVisible(false);

                    final boolean wasEditable = nhapMaCBTextField.isEditable();
                    final boolean wasFocusable = nhapMaCBTextField.isFocusable();
                    nhapMaCBTextField.setEditable(false);
                    nhapMaCBTextField.setFocusable(false);

                    SwingUtilities.invokeLater(() -> {
                        try {
                            isProgrammaticallyUpdatingText1 = true;
                            nhapMaCBTextField.setText(suggestion);
                        } finally {
                            isProgrammaticallyUpdatingText1 = false; 

                            nhapMaCBTextField.setFocusable(wasFocusable);
                            nhapMaCBTextField.setEditable(wasEditable);

                            nhapMaCBTextField.requestFocusInWindow();
                            nhapMaCBTextField.setCaretPosition(nhapMaCBTextField.getText().length());
                        }
                    });
                });
                suggestionsPopupMenu1.add(menuItem);
            }

            if (nhapMaCBTextField.isShowing() && nhapMaCBTextField.isEnabled()) {
                SwingUtilities.invokeLater(() -> {
                    if (nhapMaCBTextField.isShowing() && suggestionsPopupMenu1.getComponentCount() > 0) {
                        if (!nhapMaCBTextField.getText().trim().isEmpty()){
                            suggestionsPopupMenu1.show(nhapMaCBTextField, 0, nhapMaCBTextField.getHeight());
                        } else {
                            suggestionsPopupMenu1.setVisible(false);
                        }
                    }
                });
            } else {
                suggestionsPopupMenu1.setVisible(false);
            }
        } else {
            suggestionsPopupMenu1.setVisible(false);
        }
    }
    private void timChuyenBay() {
        if (suggestionsPopupMenu1.isVisible()) {
            suggestionsPopupMenu1.setVisible(false);
        }
        String maCBTimKiem = nhapMaCBTextField.getText().trim();
        DefaultTableModel tableModel = (DefaultTableModel) DanhSachChuyenBayTable.getModel();
        tableModel.setRowCount(0); 

        if (maCBTimKiem.isEmpty()) {
            for (Object[] rowData : allFlightDataForFiltering1) {
                tableModel.addRow(rowData);
            }
             
        } else {
            boolean found = false;
            for (Object[] rowData : allFlightDataForFiltering1) {
                if (rowData[0] != null && rowData[0].toString().equalsIgnoreCase(maCBTimKiem)) {
                    tableModel.addRow(rowData);
                    found = true;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến bay với mã: " + maCBTimKiem, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                 for (Object[] rowData : allFlightDataForFiltering1) { 
                    tableModel.addRow(rowData);
                 }
            }
        }
    }
    
    private void dienMaChuyenBayVaoForm(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) DanhSachChuyenBayTable.getModel();

        String maChuyenBay = model.getValueAt(selectedRow, 0).toString();
         String gioCatCanhStr = model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : null;
        String gioHaCanhStr = model.getValueAt(selectedRow, 4) != null ? model.getValueAt(selectedRow, 4).toString() : null;
        DateTimeFormatter tableDateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    try {
        if (gioCatCanhStr != null && !gioCatCanhStr.isEmpty()) {
            LocalDateTime catCanhLDT = LocalDateTime.parse(gioCatCanhStr, tableDateTimeFormatter);
            startTimePicker.setDateTimePermissive(catCanhLDT);
        } else {
            startTimePicker.clear(); // Hoặc setDateTimePermissive(null)
        }
        if (gioHaCanhStr != null && !gioHaCanhStr.isEmpty()) {
            LocalDateTime haCanhLDT = LocalDateTime.parse(gioHaCanhStr, tableDateTimeFormatter);
            endTimePicker.setDateTimePermissive(haCanhLDT);
        } else {
            endTimePicker.clear(); // Hoặc setDateTimePermissive(null)
        }
    } catch (DateTimeParseException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ khi hiển thị từ bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        startTimePicker.clear();
        endTimePicker.clear();
    }
        

        flightCodeLabel.setText(maChuyenBay);
    }
    
    private void initSearchSuggestions_LichBay() {
    nhapMaLichBayTextField.getDocument().addDocumentListener(new DocumentListener() {
        private void update() {
            if (!isProgrammaticallyUpdatingText_LichBay) {
                showSuggestions_LichBay();
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

    nhapMaLichBayTextField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                suggestionsPopupMenu_LichBay.setVisible(false);
                timLichBay(); 
                e.consume();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                suggestionsPopupMenu_LichBay.setVisible(false);
                e.consume();
            }
        }
    });

    nhapMaLichBayTextField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
           Component oppositeComponent = e.getOppositeComponent();
            if (oppositeComponent == null || !SwingUtilities.isDescendingFrom(oppositeComponent, suggestionsPopupMenu_LichBay)) {
                SwingUtilities.invokeLater(() -> {
                    boolean textFieldHasFocus = nhapMaLichBayTextField.isFocusOwner();
                    boolean popupHasFocus = false;
                    Component currentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (currentFocusOwner != null) {
                        popupHasFocus = SwingUtilities.isDescendingFrom(currentFocusOwner, suggestionsPopupMenu_LichBay);
                    }

                    if (!textFieldHasFocus && !popupHasFocus) {
                        suggestionsPopupMenu_LichBay.setVisible(false);
                    }
                });
            }
        }
    });
    }
    private void showSuggestions_LichBay() {
    String typedText = nhapMaLichBayTextField.getText().trim().toLowerCase();
    suggestionsPopupMenu_LichBay.removeAll();

    if (typedText.isEmpty()) {
        suggestionsPopupMenu_LichBay.setVisible(false);
        return;
    }

    List<String> matched = new ArrayList<>();

    for (String empCode : allEmployeeCodesForSuggestion_LichBay) {
        if (empCode.toLowerCase().contains(typedText)) {
            if (!matched.contains(empCode)) { 
                matched.add(empCode);
            }
        }
    }

    for (String flightCode : allFlightCodesForSuggestion_LichBay) {
        if (flightCode.toLowerCase().contains(typedText)) {
            if (!matched.contains(flightCode)) { 
                matched.add(flightCode);
            }
        }
    }

        Collections.sort(matched, String.CASE_INSENSITIVE_ORDER);

    if (!matched.isEmpty()) {
        for (String suggestion : matched) {
            JMenuItem menuItem = new JMenuItem(suggestion);
            menuItem.addActionListener(e_menu -> {
                suggestionsPopupMenu_LichBay.setVisible(false);

                final boolean wasEditable = nhapMaLichBayTextField.isEditable();
                final boolean wasFocusable = nhapMaLichBayTextField.isFocusable();
                nhapMaLichBayTextField.setEditable(false);
                nhapMaLichBayTextField.setFocusable(false);

                SwingUtilities.invokeLater(() -> {
                    try {
                        isProgrammaticallyUpdatingText_LichBay = true;
                        nhapMaLichBayTextField.setText(suggestion);
                    } finally {
                        isProgrammaticallyUpdatingText_LichBay = false;
                        nhapMaLichBayTextField.setFocusable(wasFocusable);
                        nhapMaLichBayTextField.setEditable(wasEditable);
                        nhapMaLichBayTextField.requestFocusInWindow();
                        nhapMaLichBayTextField.setCaretPosition(nhapMaLichBayTextField.getText().length());
                    }
                });
            });
            suggestionsPopupMenu_LichBay.add(menuItem);
        }

        if (nhapMaLichBayTextField.isShowing() && nhapMaLichBayTextField.isEnabled()) {
            SwingUtilities.invokeLater(() -> {
                if (nhapMaLichBayTextField.isShowing() && suggestionsPopupMenu_LichBay.getComponentCount() > 0) {
                    if (!nhapMaLichBayTextField.getText().trim().isEmpty()) {
                        suggestionsPopupMenu_LichBay.show(nhapMaLichBayTextField, 0, nhapMaLichBayTextField.getHeight());
                    } else {
                        suggestionsPopupMenu_LichBay.setVisible(false);
                    }
                }
            });
        } else {
            suggestionsPopupMenu_LichBay.setVisible(false);
        }
    } else {
        suggestionsPopupMenu_LichBay.setVisible(false);
    }
    }
    private void timLichBay() {
     if (suggestionsPopupMenu_LichBay.isVisible()) {
        suggestionsPopupMenu_LichBay.setVisible(false);
    }
    String searchText = nhapMaLichBayTextField.getText().trim().toLowerCase();
    DefaultTableModel tableModel = (DefaultTableModel) LichPhanCongBayTable.getModel();
    tableModel.setRowCount(0); 

    List<Object[]> ketQuaHienThi = new ArrayList<>();

    if (searchText.isEmpty()) {
        ketQuaHienThi.addAll(allLichBayDataForFiltering);
    } else {
        boolean found = false;
        for (Object[] rowData : allLichBayDataForFiltering) {
            String maNhanVienTrongBang = rowData[0] != null ? rowData[0].toString().toLowerCase() : "";
            String maChuyenBayTrongBang = rowData[1] != null ? rowData[1].toString().toLowerCase() : "";

            if (maNhanVienTrongBang.contains(searchText) || maChuyenBayTrongBang.contains(searchText)) {
                ketQuaHienThi.add(rowData);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy lịch phân công nào cho '" + nhapMaLichBayTextField.getText().trim() + "'", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            ketQuaHienThi.addAll(allLichBayDataForFiltering);
        }
    }

    ketQuaHienThi.sort(Comparator
        .comparing((Object[] row) -> (row[0] == null) ? "" : row[0].toString(), String.CASE_INSENSITIVE_ORDER) // Sắp xếp theo Mã Nhân Viên
        .thenComparing((Object[] row) -> (row[1] == null) ? "" : row[1].toString(), String.CASE_INSENSITIVE_ORDER) // Rồi theo Mã Chuyến Bay
    );

    for (Object[] rowData : ketQuaHienThi) {
        tableModel.addRow(rowData);
    }
}
    private void dienThongTinLichBayVaoForm(int selectedRowInLichTable) {
    DefaultTableModel modelLichBay = (DefaultTableModel) LichPhanCongBayTable.getModel();
    String maNhanVien = modelLichBay.getValueAt(selectedRowInLichTable, 0).toString();
    String maChuyenBay = modelLichBay.getValueAt(selectedRowInLichTable, 1).toString();
    String vaiTroTrongLich = modelLichBay.getValueAt(selectedRowInLichTable, 3).toString();
    String thoiGianBatDauStr = modelLichBay.getValueAt(selectedRowInLichTable, 4).toString();
    String thoiGianKetThucStr = modelLichBay.getValueAt(selectedRowInLichTable, 5).toString();
    String trangThaiFromTable = modelLichBay.getValueAt(selectedRowInLichTable, 2).toString(); // Giả sử cột TRANGTHAI là cột thứ 3 (index 2)
    for (int i = 0; i < stateComboBox.getItemCount(); i++) {
    if (stateComboBox.getItemAt(i).equals(trangThaiFromTable)) {
        stateComboBox.setSelectedIndex(i);
        break;
    }
    }
    boolean foundEmployee = false;
    for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
        if (employeeComboBox.getItemAt(i).equals(maNhanVien)) {
            employeeComboBox.setSelectedIndex(i); 
            foundEmployee = true;
            break;
        }
    }
    if (!foundEmployee) {
        employeeComboBox.setSelectedIndex(-1); 
        showEmployeeNameLabel.setText("Không rõ"); 
    }
    
    flightCodeLabel.setText(maChuyenBay);

    showRoleLabel.setText(vaiTroTrongLich);
    DateTimeFormatter tableDateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    try {
        if (thoiGianBatDauStr != null && !thoiGianBatDauStr.isEmpty()) {
            LocalDateTime batDauLDT = LocalDateTime.parse(thoiGianBatDauStr, tableDateTimeFormatter);
            startTimePicker.setDateTimePermissive(batDauLDT);
        } else {
            startTimePicker.clear();
        }

        if (thoiGianKetThucStr != null && !thoiGianKetThucStr.isEmpty()) {
            LocalDateTime ketThucLDT = LocalDateTime.parse(thoiGianKetThucStr, tableDateTimeFormatter);
            endTimePicker.setDateTimePermissive(ketThucLDT);
        } else {
            endTimePicker.clear();
        }
    } catch (DateTimeParseException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ khi điền thông tin từ bảng: " + e.getMessage(), "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
        startTimePicker.clear();
        endTimePicker.clear();
    }
    
    
    if (!phanCongBayPanel.isShowing() || mainPanel.getComponent(0) != phanCongBayPanel) {
        mainPanel.removeAll();
        mainPanel.add(phanCongBayPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
        setFieldsEditable(false);
}
    
    private void setFieldsEditable(boolean editable) {
        employeeComboBox.setEnabled(editable);
        flightCodeLabel.setEnabled(editable);
        showEmployeeNameLabel.setEnabled(editable);
        showRoleLabel.setEnabled(editable);
        startTimePicker.setEnabled(editable);
        endTimePicker.setEnabled(editable);
        stateComboBox.setEnabled(editable);
    }
    private void xoaTrangCacTruong() {
        employeeComboBox.setSelectedIndex(-1); 
        flightCodeLabel.setText("");
        showEmployeeNameLabel.setText("");
        showRoleLabel.setText("");

        startTimePicker.clear(); 
        endTimePicker.clear();   
        stateComboBox.setSelectedIndex(-1); 
        nhapMaLichBayTextField.setText(""); 
        setFieldsEditable(true);
        thoatXemButton.setVisible(false);
    }
    private void initSearchSuggestions_LichTruc() {
    if (suggestionsPopupMenu_LichTruc == null) { 
        suggestionsPopupMenu_LichTruc = new JPopupMenu();
        suggestionsPopupMenu_LichTruc.setFocusable(false); 
    }

    nhapMaLichTrucTextField.getDocument().addDocumentListener(new DocumentListener() {
        private void update() {
            if (!isProgrammaticallyUpdatingText_LichTruc) {
                showSuggestions_LichTruc(); 
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

    nhapMaLichTrucTextField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                suggestionsPopupMenu_LichTruc.setVisible(false); 
                timLichTruc(); 
                e.consume();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                suggestionsPopupMenu_LichTruc.setVisible(false); 
                e.consume();
            }
        }
    });

    nhapMaLichTrucTextField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            Component oppositeComponent = e.getOppositeComponent();
            if (oppositeComponent == null || !SwingUtilities.isDescendingFrom(oppositeComponent, suggestionsPopupMenu_LichTruc)) {
                SwingUtilities.invokeLater(() -> {
                    boolean textFieldHasFocus = nhapMaLichTrucTextField.isFocusOwner();
                    boolean popupHasFocus = false;
                    Component currentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (currentFocusOwner != null) {
                        popupHasFocus = SwingUtilities.isDescendingFrom(currentFocusOwner, suggestionsPopupMenu_LichTruc);
                    }

                    if (!textFieldHasFocus && !popupHasFocus) {
                        suggestionsPopupMenu_LichTruc.setVisible(false); // Hide the duty assignment popup
                    }
                });
            }
        }
    });
    }
    private void showSuggestions_LichTruc() {
    String typedText = nhapMaLichTrucTextField.getText().trim().toLowerCase(); // Use nhapMaLichTrucTextField
    suggestionsPopupMenu_LichTruc.removeAll(); // Use suggestionsPopupMenu_LichTruc

    if (typedText.isEmpty()) {
        suggestionsPopupMenu_LichTruc.setVisible(false);
        return;
    }

    List<String> matched = new ArrayList<>();

    for (String empCode : allEmployeeCodesForSuggestion_LichTruc) { // Use _LichTruc list
        if (empCode.toLowerCase().contains(typedText)) {
            if (!matched.contains(empCode)) {
                matched.add(empCode);
            }
        }
    }

    for (String airportCode : allSanBayCodesForSuggestion_LichTruc) { // Use _LichTruc list (airport codes)
        if (airportCode.toLowerCase().contains(typedText)) {
            if (!matched.contains(airportCode)) {
                matched.add(airportCode);
            }
        }
    }

    Collections.sort(matched, String.CASE_INSENSITIVE_ORDER);

    if (!matched.isEmpty()) {
        for (String suggestion : matched) {
            JMenuItem menuItem = new JMenuItem(suggestion);
            menuItem.addActionListener(e_menu -> {
                suggestionsPopupMenu_LichTruc.setVisible(false);

                final boolean wasEditable = nhapMaLichTrucTextField.isEditable();
                final boolean wasFocusable = nhapMaLichTrucTextField.isFocusable();
                nhapMaLichTrucTextField.setEditable(false);
                nhapMaLichTrucTextField.setFocusable(false);

                SwingUtilities.invokeLater(() -> {
                    try {
                        isProgrammaticallyUpdatingText_LichTruc = true; // Use _LichTruc flag
                        nhapMaLichTrucTextField.setText(suggestion);
                    } finally {
                        isProgrammaticallyUpdatingText_LichTruc = false;
                        nhapMaLichTrucTextField.setFocusable(wasFocusable);
                        nhapMaLichTrucTextField.setEditable(wasEditable);
                        nhapMaLichTrucTextField.requestFocusInWindow();
                        nhapMaLichTrucTextField.setCaretPosition(nhapMaLichTrucTextField.getText().length());
                    }
                });
            });
            suggestionsPopupMenu_LichTruc.add(menuItem);
        }

        if (nhapMaLichTrucTextField.isShowing() && nhapMaLichTrucTextField.isEnabled()) {
            SwingUtilities.invokeLater(() -> {
                if (nhapMaLichTrucTextField.isShowing() && suggestionsPopupMenu_LichTruc.getComponentCount() > 0) {
                    if (!nhapMaLichTrucTextField.getText().trim().isEmpty()) {
                        suggestionsPopupMenu_LichTruc.show(nhapMaLichTrucTextField, 0, nhapMaLichTrucTextField.getHeight());
                    } else {
                        suggestionsPopupMenu_LichTruc.setVisible(false);
                    }
                }
            });
        } else {
            suggestionsPopupMenu_LichTruc.setVisible(false);
        }
    } else {
        suggestionsPopupMenu_LichTruc.setVisible(false);
    }
    }
    
    private void timLichTruc() {
    if (suggestionsPopupMenu_LichTruc.isVisible()) { // Use _LichTruc popup
        suggestionsPopupMenu_LichTruc.setVisible(false);
    }
    String searchText = nhapMaLichTrucTextField.getText().trim().toLowerCase(); // Use _LichTruc text field
    DefaultTableModel tableModel = (DefaultTableModel) LichPhanCongTrucTable.getModel(); // Target duty assignment table
    tableModel.setRowCount(0);

    List<Object[]> ketQuaHienThi = new ArrayList<>();

    if (searchText.isEmpty()) {
        ketQuaHienThi.addAll(allLichTrucDataForFiltering); // Use _LichTruc data
    } else {
        boolean found = false;
        for (Object[] rowData : allLichTrucDataForFiltering) { // Use _LichTruc data
            String maNhanVienTrongBang = rowData[0] != null ? rowData[0].toString().toLowerCase() : "";
            String maSanBayTrongBang = rowData[1] != null ? rowData[1].toString().toLowerCase() : ""; // Column 1 is MaSanBay

            if (maNhanVienTrongBang.contains(searchText) || maSanBayTrongBang.contains(searchText)) {
                ketQuaHienThi.add(rowData);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy lịch phân công trực nào cho '" + nhapMaLichTrucTextField.getText().trim() + "'", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            ketQuaHienThi.addAll(allLichTrucDataForFiltering); // Show all if not found
        }
    }

    ketQuaHienThi.sort(Comparator
        .comparing((Object[] row) -> (row[0] == null) ? "" : row[0].toString(), String.CASE_INSENSITIVE_ORDER) // Mã Nhân Viên at index 0
        .thenComparing((Object[] row) -> (row[1] == null) ? "" : row[1].toString(), String.CASE_INSENSITIVE_ORDER) // Mã Sân Bay at index 1
    );

    for (Object[] rowData : ketQuaHienThi) {
        tableModel.addRow(rowData);
    }
}
    private void dienThongTinLichTrucVaoForm(int selectedRowInLichTable) {
    DefaultTableModel modelLichTruc = (DefaultTableModel) LichPhanCongTrucTable.getModel(); // Duty assignment table

    String maNhanVien = modelLichTruc.getValueAt(selectedRowInLichTable, 0).toString();
    String maSanBay = modelLichTruc.getValueAt(selectedRowInLichTable, 1).toString(); // Mã Sân Bay
    String trangThaiFromTable = modelLichTruc.getValueAt(selectedRowInLichTable, 2).toString();
    String vaiTroTrongLich = modelLichTruc.getValueAt(selectedRowInLichTable, 3).toString();
    String thoiGianBatDauStr = modelLichTruc.getValueAt(selectedRowInLichTable, 4).toString();
    String thoiGianKetThucStr = modelLichTruc.getValueAt(selectedRowInLichTable, 5).toString();

     boolean foundEmployee = false;
    for (int i = 0; i < nhanVienComboBox.getItemCount(); i++) {
        if (nhanVienComboBox.getItemAt(i).equals(maNhanVien)) {
            nhanVienComboBox.setSelectedIndex(i);
            foundEmployee = true;
            break;
        }
    }
    if (!foundEmployee) {
        nhanVienComboBox.setSelectedIndex(-1);
        hienTenNhanVienLabel.setText("Không rõ"); 
    }
    
    String tenSanBayStr = "";
    String sqlTenSanBay = "SELECT TENSANBAY FROM SAN_BAY WHERE MASANBAY = ?";
    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sqlTenSanBay)) {
        ps.setString(1, maSanBay);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            tenSanBayStr = rs.getString("TENSANBAY");
        }
        rs.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    tenSanBayLabel.setText(tenSanBayStr); // Label in phanCongTrucPanel
    currentSelectedMaSanBay = maSanBay; // Store for potential edits (though form is read-only here)

    hienVaiTroLabel.setText(vaiTroTrongLich); // Label in phanCongTrucPanel

    for (int i = 0; i < trangThaiComboBox.getItemCount(); i++) {
        if (trangThaiComboBox.getItemAt(i).equals(trangThaiFromTable)) {
            trangThaiComboBox.setSelectedIndex(i);
            break;
        }
    }

    DateTimeFormatter tableDateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    try {
        if (thoiGianBatDauStr != null && !thoiGianBatDauStr.isEmpty()) {
            LocalDateTime batDauLDT = LocalDateTime.parse(thoiGianBatDauStr, tableDateTimeFormatter);
            thoiGianBatDauPicker.setDateTimePermissive(batDauLDT); // Picker in phanCongTrucPanel
        } else {
            thoiGianBatDauPicker.clear();
        }

        if (thoiGianKetThucStr != null && !thoiGianKetThucStr.isEmpty()) {
            LocalDateTime ketThucLDT = LocalDateTime.parse(thoiGianKetThucStr, tableDateTimeFormatter);
            thoiGianKetThucPicker.setDateTimePermissive(ketThucLDT); // Picker in phanCongTrucPanel
        } else {
            thoiGianKetThucPicker.clear();
        }
    } catch (DateTimeParseException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ khi điền thông tin từ bảng: " + e.getMessage(), "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
        thoiGianBatDauPicker.clear();
        thoiGianKetThucPicker.clear();
    }

    if (mainPanel.getComponentCount() == 0 || mainPanel.getComponent(0) != phanCongTrucPanel) {
        mainPanel.removeAll();
        mainPanel.add(phanCongTrucPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    
    setFieldsEditable_Truc(false); 
    addButton.setVisible(false);   
    exitButton.setVisible(true);  
}
    
    private void setFieldsEditable_Truc(boolean editable) {
    nhanVienComboBox.setEnabled(editable);      
    tenSanBayLabel.setEnabled(editable);         
    hienTenNhanVienLabel.setEnabled(editable); 
    hienVaiTroLabel.setEnabled(editable);      
    thoiGianBatDauPicker.setEnabled(editable); 
    thoiGianKetThucPicker.setEnabled(editable);
    trangThaiComboBox.setEnabled(editable);      
}
    private void xoaTrangCacTruong_Truc() {
    nhanVienComboBox.removeAllItems();
    nhanVienComboBox.setSelectedIndex(-1);        
    tenSanBayLabel.setText("Tên sân bay");      
    hienTenNhanVienLabel.setText("Tên nhân viên"); 
    hienVaiTroLabel.setText("Tên vai trò");       
    hienDiaChiLabel.setText("Địa chỉ");
    currentSelectedMaSanBay = null;           
    currentSelectedAirportCity_Truc = null;    

    thoiGianBatDauPicker.clear();               
    thoiGianKetThucPicker.clear();                
    trangThaiComboBox.setSelectedIndex(-1);       
    
    nhapMaLichTrucTextField.setText("");          

    setFieldsEditable_Truc(true);              
    exitButton.setVisible(false);               
    addButton.setVisible(true);                  
    LichPhanCongTrucTable.clearSelection();       
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cardLayoutPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        phanCongTrucPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        nhanVienComboBox = new javax.swing.JComboBox<>();
        nhanVienLabel = new javax.swing.JLabel();
        sanBayLabel = new javax.swing.JLabel();
        tenNhanVienLabel = new javax.swing.JLabel();
        vaiTroLabel = new javax.swing.JLabel();
        tenSanBayLabel = new javax.swing.JLabel();
        hienTenNhanVienLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        phanCongBayButton = new javax.swing.JButton();
        hienVaiTroLabel = new javax.swing.JLabel();
        thoiGianKetThucPicker = new com.github.lgooddatepicker.components.DateTimePicker();
        thoiGianBatDauLabel = new javax.swing.JLabel();
        thoiGianBatDauPicker = new com.github.lgooddatepicker.components.DateTimePicker();
        thoiGianKetThucLabel = new javax.swing.JLabel();
        trangThaiLabel = new javax.swing.JLabel();
        trangThaiComboBox = new javax.swing.JComboBox<>();
        diaChiLabel = new javax.swing.JLabel();
        hienDiaChiLabel = new javax.swing.JLabel();
        phanCongTrucLabel = new javax.swing.JLabel();
        phanCongBayPanel = new javax.swing.JPanel();
        themButton = new javax.swing.JButton();
        employeeComboBox = new javax.swing.JComboBox<>();
        employeeLabel = new javax.swing.JLabel();
        flightLabel = new javax.swing.JLabel();
        employeeNameLabel = new javax.swing.JLabel();
        endTimeLabel = new javax.swing.JLabel();
        roleLabel = new javax.swing.JLabel();
        flightCodeLabel = new javax.swing.JLabel();
        showEmployeeNameLabel = new javax.swing.JLabel();
        thoatXemButton = new javax.swing.JButton();
        quayLaiButton = new javax.swing.JButton();
        phanCongTrucButton = new javax.swing.JButton();
        showRoleLabel = new javax.swing.JLabel();
        endTimePicker = new com.github.lgooddatepicker.components.DateTimePicker();
        startTimeLabel = new javax.swing.JLabel();
        startTimePicker = new com.github.lgooddatepicker.components.DateTimePicker();
        stateLabel = new javax.swing.JLabel();
        stateComboBox = new javax.swing.JComboBox<>();
        phanCongBayLabel = new javax.swing.JLabel();
        menuPanel = new javax.swing.JPanel();
        trucButton = new javax.swing.JButton();
        bayButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cardLayoutPanel1 = new javax.swing.JPanel();
        mainPanel2 = new javax.swing.JPanel();
        danhSachSanBayPanel = new javax.swing.JPanel();
        nhapTenSBTextField = new javax.swing.JTextField();
        timKiemSBLabel = new javax.swing.JLabel();
        timSBButton = new javax.swing.JButton();
        luotSB = new javax.swing.JScrollPane();
        DanhSachSanBayTable = new javax.swing.JTable();
        danhSachChuyenBayPanel = new javax.swing.JPanel();
        nhapMaCBTextField = new javax.swing.JTextField();
        timKiemCBLabel = new javax.swing.JLabel();
        timCBButton = new javax.swing.JButton();
        luotCB = new javax.swing.JScrollPane();
        DanhSachChuyenBayTable = new javax.swing.JTable();
        lichPhanCongBayPanel = new javax.swing.JPanel();
        nhapMaLichBayTextField = new javax.swing.JTextField();
        timLichBayLabel = new javax.swing.JLabel();
        timLBButton = new javax.swing.JButton();
        luotLB = new javax.swing.JScrollPane();
        LichPhanCongBayTable = new javax.swing.JTable();
        tiepTucPhanCongButton = new javax.swing.JButton();
        lichPhanCongTrucPanel = new javax.swing.JPanel();
        nhapMaLichTrucTextField = new javax.swing.JTextField();
        timLichTrucLabel = new javax.swing.JLabel();
        timLTButton = new javax.swing.JButton();
        luotLT = new javax.swing.JScrollPane();
        LichPhanCongTrucTable = new javax.swing.JTable();
        continueButton = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(252, 232, 232));
        mainPanel.setLayout(new java.awt.CardLayout());

        phanCongTrucPanel.setBackground(new java.awt.Color(255, 255, 255));

        addButton.setBackground(new java.awt.Color(0, 255, 255));
        addButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        addButton.setForeground(new java.awt.Color(0, 51, 102));
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/add (1) (1).png"))); // NOI18N
        addButton.setText("Thêm");
        addButton.setIconTextGap(10);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        nhanVienComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        nhanVienComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhanVienComboBoxActionPerformed(evt);
            }
        });

        nhanVienLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        nhanVienLabel.setForeground(new java.awt.Color(0, 51, 102));
        nhanVienLabel.setText("Nhân viên");

        sanBayLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        sanBayLabel.setForeground(new java.awt.Color(0, 51, 102));
        sanBayLabel.setText("Sân bay");

        tenNhanVienLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        tenNhanVienLabel.setForeground(new java.awt.Color(0, 51, 102));
        tenNhanVienLabel.setText("Tên nhân viên");

        vaiTroLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        vaiTroLabel.setForeground(new java.awt.Color(0, 51, 102));
        vaiTroLabel.setText("Vai trò");

        tenSanBayLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        tenSanBayLabel.setText("Tên sân bay");

        hienTenNhanVienLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        hienTenNhanVienLabel.setText("Tên nhân viên");

        exitButton.setBackground(new java.awt.Color(0, 255, 255));
        exitButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        exitButton.setForeground(new java.awt.Color(0, 51, 102));
        exitButton.setText("Thoát chế độ xem");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        backButton.setBackground(new java.awt.Color(0, 255, 255));
        backButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        backButton.setForeground(new java.awt.Color(0, 51, 102));
        backButton.setText("Quay lại");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        phanCongBayButton.setBackground(new java.awt.Color(0, 255, 255));
        phanCongBayButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        phanCongBayButton.setForeground(new java.awt.Color(0, 51, 102));
        phanCongBayButton.setText("Phân công bay");
        phanCongBayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phanCongBayButtonActionPerformed(evt);
            }
        });

        hienVaiTroLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        hienVaiTroLabel.setText("Tên vai trò");

        thoiGianBatDauLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        thoiGianBatDauLabel.setForeground(new java.awt.Color(0, 51, 102));
        thoiGianBatDauLabel.setText("Thời gian bắt đầu");

        thoiGianKetThucLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        thoiGianKetThucLabel.setForeground(new java.awt.Color(0, 51, 102));
        thoiGianKetThucLabel.setText("Thời gian kết thúc");

        trangThaiLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        trangThaiLabel.setForeground(new java.awt.Color(0, 51, 102));
        trangThaiLabel.setText("Trạng thái");

        trangThaiComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        trangThaiComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đã phân công", "Hoàn thành ", "Hủy" }));
        trangThaiComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trangThaiComboBoxActionPerformed(evt);
            }
        });

        diaChiLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        diaChiLabel.setForeground(new java.awt.Color(0, 51, 102));
        diaChiLabel.setText("Địa chỉ");

        hienDiaChiLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        hienDiaChiLabel.setText("Địa chỉ");

        phanCongTrucLabel.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        phanCongTrucLabel.setForeground(new java.awt.Color(0, 51, 102));
        phanCongTrucLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        phanCongTrucLabel.setText("Phân công trực");

        javax.swing.GroupLayout phanCongTrucPanelLayout = new javax.swing.GroupLayout(phanCongTrucPanel);
        phanCongTrucPanel.setLayout(phanCongTrucPanelLayout);
        phanCongTrucPanelLayout.setHorizontalGroup(
            phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(phanCongBayButton)
                .addGap(38, 38, 38))
            .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                        .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(thoiGianBatDauLabel)
                            .addComponent(thoiGianKetThucLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(thoiGianKetThucPicker, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(thoiGianBatDauPicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                        .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sanBayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tenNhanVienLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(vaiTroLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(diaChiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(hienVaiTroLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hienTenNhanVienLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                .addComponent(hienDiaChiLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(tenSanBayLabel))
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phanCongTrucPanelLayout.createSequentialGroup()
                        .addComponent(nhanVienLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(nhanVienComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                        .addComponent(trangThaiLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(trangThaiComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(addButton))
                    .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(exitButton))
                    .addGroup(phanCongTrucPanelLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(phanCongTrucLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        phanCongTrucPanelLayout.setVerticalGroup(
            phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phanCongTrucPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(phanCongTrucLabel)
                .addGap(18, 18, 18)
                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanVienLabel)
                    .addComponent(nhanVienComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sanBayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tenSanBayLabel))
                .addGap(30, 30, 30)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tenNhanVienLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hienTenNhanVienLabel))
                .addGap(31, 31, 31)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vaiTroLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hienVaiTroLabel))
                .addGap(20, 20, 20)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diaChiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hienDiaChiLabel))
                .addGap(18, 18, 18)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(thoiGianBatDauPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thoiGianBatDauLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(thoiGianKetThucPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thoiGianKetThucLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trangThaiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trangThaiComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(phanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phanCongBayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(451, Short.MAX_VALUE))
        );

        mainPanel.add(phanCongTrucPanel, "card5");

        phanCongBayPanel.setBackground(new java.awt.Color(255, 255, 255));

        themButton.setBackground(new java.awt.Color(0, 255, 255));
        themButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        themButton.setForeground(new java.awt.Color(0, 51, 102));
        themButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/add (1) (1).png"))); // NOI18N
        themButton.setText("Thêm");
        themButton.setIconTextGap(10);
        themButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themButtonActionPerformed(evt);
            }
        });

        employeeComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        employeeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeComboBoxActionPerformed(evt);
            }
        });

        employeeLabel.setBackground(new java.awt.Color(0, 51, 102));
        employeeLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        employeeLabel.setForeground(new java.awt.Color(0, 51, 102));
        employeeLabel.setText("Nhân viên");

        flightLabel.setBackground(new java.awt.Color(0, 51, 102));
        flightLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        flightLabel.setForeground(new java.awt.Color(0, 51, 102));
        flightLabel.setText("Chuyến bay");

        employeeNameLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        employeeNameLabel.setForeground(new java.awt.Color(0, 51, 102));
        employeeNameLabel.setText("Tên nhân viên");

        endTimeLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        endTimeLabel.setForeground(new java.awt.Color(0, 51, 102));
        endTimeLabel.setText("Thời gian kết thúc");

        roleLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        roleLabel.setForeground(new java.awt.Color(0, 51, 102));
        roleLabel.setText("Vai trò");

        flightCodeLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        flightCodeLabel.setText("Mã chuyến bay");

        showEmployeeNameLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        showEmployeeNameLabel.setText("Tên nhân viên");

        thoatXemButton.setBackground(new java.awt.Color(0, 255, 255));
        thoatXemButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        thoatXemButton.setForeground(new java.awt.Color(0, 51, 102));
        thoatXemButton.setText("Thoát chế độ xem");
        thoatXemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thoatXemButtonActionPerformed(evt);
            }
        });

        quayLaiButton.setBackground(new java.awt.Color(0, 255, 255));
        quayLaiButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        quayLaiButton.setForeground(new java.awt.Color(0, 51, 102));
        quayLaiButton.setText("Quay lại");
        quayLaiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quayLaiButtonActionPerformed(evt);
            }
        });

        phanCongTrucButton.setBackground(new java.awt.Color(0, 255, 255));
        phanCongTrucButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        phanCongTrucButton.setForeground(new java.awt.Color(0, 51, 102));
        phanCongTrucButton.setText("Phân công trực");
        phanCongTrucButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phanCongTrucButtonActionPerformed(evt);
            }
        });

        showRoleLabel.setFont(new java.awt.Font("UTM Aptima", 0, 18)); // NOI18N
        showRoleLabel.setText("Tên vai trò");

        startTimeLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        startTimeLabel.setForeground(new java.awt.Color(0, 51, 102));
        startTimeLabel.setText("Thời gian bắt đầu");

        stateLabel.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        stateLabel.setForeground(new java.awt.Color(0, 51, 102));
        stateLabel.setText("Trạng thái");

        stateComboBox.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        stateComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đã phân công", "Hoàn thành", "Hủy" }));
        stateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateComboBoxActionPerformed(evt);
            }
        });

        phanCongBayLabel.setBackground(new java.awt.Color(0, 51, 102));
        phanCongBayLabel.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        phanCongBayLabel.setForeground(new java.awt.Color(0, 51, 102));
        phanCongBayLabel.setText("Phân công bay");

        javax.swing.GroupLayout phanCongBayPanelLayout = new javax.swing.GroupLayout(phanCongBayPanel);
        phanCongBayPanel.setLayout(phanCongBayPanelLayout);
        phanCongBayPanelLayout.setHorizontalGroup(
            phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phanCongBayPanelLayout.createSequentialGroup()
                        .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                                .addComponent(startTimeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(endTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                                .addComponent(roleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(showRoleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(37, 37, 37))
                    .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                        .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                                .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(67, 67, 67)
                                .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(endTimeLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phanCongBayPanelLayout.createSequentialGroup()
                        .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(employeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(employeeNameLabel)
                                    .addComponent(flightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(showEmployeeNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(flightCodeLabel))))
                        .addGap(40, 40, 40))))
            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(quayLaiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(phanCongTrucButton)
                .addGap(35, 35, 35))
            .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(themButton))
                    .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(thoatXemButton))
                    .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(phanCongBayLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        phanCongBayPanelLayout.setVerticalGroup(
            phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phanCongBayPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(phanCongBayLabel)
                .addGap(18, 18, 18)
                .addComponent(thoatXemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeLabel)
                    .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(phanCongBayPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(flightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(flightCodeLabel))
                .addGap(29, 29, 29)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showEmployeeNameLabel))
                .addGap(35, 35, 35)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showRoleLabel))
                .addGap(34, 34, 34)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(endTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(themButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(phanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quayLaiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phanCongTrucButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(486, Short.MAX_VALUE))
        );

        mainPanel.add(phanCongBayPanel, "card5");

        menuPanel.setBackground(new java.awt.Color(255, 255, 255));

        trucButton.setBackground(new java.awt.Color(0, 255, 255));
        trucButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        trucButton.setForeground(new java.awt.Color(0, 51, 102));
        trucButton.setText("Phân công trực");
        trucButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trucButtonActionPerformed(evt);
            }
        });

        bayButton.setBackground(new java.awt.Color(0, 255, 255));
        bayButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        bayButton.setForeground(new java.awt.Color(0, 51, 102));
        bayButton.setText("Phân công bay");
        bayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("VUI LÒNG CHỌN MỘT TRONG HAI ĐỂ TIẾP TỤC");
        jLabel1.setFocusCycleRoot(true);

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addComponent(bayButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(trucButton)))
                .addGap(62, 62, 62))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(jLabel1)
                .addGap(52, 52, 52)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trucButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(839, Short.MAX_VALUE))
        );

        mainPanel.add(menuPanel, "card5");

        mainPanel2.setBackground(new java.awt.Color(235, 232, 232));
        mainPanel2.setLayout(new java.awt.CardLayout());

        danhSachSanBayPanel.setBackground(new java.awt.Color(255, 255, 255));

        nhapTenSBTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        nhapTenSBTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapTenSBTextFieldActionPerformed(evt);
            }
        });

        timKiemSBLabel.setBackground(new java.awt.Color(255, 255, 255));
        timKiemSBLabel.setFont(new java.awt.Font("UTM Aptima", 1, 14)); // NOI18N
        timKiemSBLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timKiemSBLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/search-interface-symbol (1) (1) (1).png"))); // NOI18N

        timSBButton.setBackground(new java.awt.Color(0, 255, 255));
        timSBButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        timSBButton.setForeground(new java.awt.Color(0, 51, 102));
        timSBButton.setText("Tìm");
        timSBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timSBButtonActionPerformed(evt);
            }
        });

        DanhSachSanBayTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        DanhSachSanBayTable.setForeground(new java.awt.Color(0, 51, 102));
        DanhSachSanBayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Sân Bay", "Tên Sân Bay", "Tỉnh Thành Phố", "Quốc Gia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        DanhSachSanBayTable.setRowHeight(35);
        luotSB.setViewportView(DanhSachSanBayTable);

        javax.swing.GroupLayout danhSachSanBayPanelLayout = new javax.swing.GroupLayout(danhSachSanBayPanel);
        danhSachSanBayPanel.setLayout(danhSachSanBayPanelLayout);
        danhSachSanBayPanelLayout.setHorizontalGroup(
            danhSachSanBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhSachSanBayPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(timKiemSBLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(nhapTenSBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(timSBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(danhSachSanBayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(luotSB, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addContainerGap())
        );
        danhSachSanBayPanelLayout.setVerticalGroup(
            danhSachSanBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhSachSanBayPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(danhSachSanBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timKiemSBLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(danhSachSanBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nhapTenSBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timSBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(luotSB, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(649, Short.MAX_VALUE))
        );

        mainPanel2.add(danhSachSanBayPanel, "card2");

        danhSachChuyenBayPanel.setBackground(new java.awt.Color(255, 255, 255));

        nhapMaCBTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        nhapMaCBTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapMaCBTextFieldActionPerformed(evt);
            }
        });

        timKiemCBLabel.setBackground(new java.awt.Color(255, 255, 255));
        timKiemCBLabel.setFont(new java.awt.Font("UTM Aptima", 1, 14)); // NOI18N
        timKiemCBLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timKiemCBLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/search-interface-symbol (1) (1) (1).png"))); // NOI18N

        timCBButton.setBackground(new java.awt.Color(0, 255, 255));
        timCBButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        timCBButton.setForeground(new java.awt.Color(0, 51, 102));
        timCBButton.setText("Tìm");
        timCBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timCBButtonActionPerformed(evt);
            }
        });

        DanhSachChuyenBayTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        DanhSachChuyenBayTable.setForeground(new java.awt.Color(0, 51, 102));
        DanhSachChuyenBayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Chuyến", "Mã Tuyến Bay", "Mã Máy Bay", "Giờ Cất Cánh", "Giờ Hạ Cánh", "Trạng Thái", "Giá Vé", "Số Ghế Trống"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        DanhSachChuyenBayTable.setRowHeight(35);
        luotCB.setViewportView(DanhSachChuyenBayTable);

        javax.swing.GroupLayout danhSachChuyenBayPanelLayout = new javax.swing.GroupLayout(danhSachChuyenBayPanel);
        danhSachChuyenBayPanel.setLayout(danhSachChuyenBayPanelLayout);
        danhSachChuyenBayPanelLayout.setHorizontalGroup(
            danhSachChuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhSachChuyenBayPanelLayout.createSequentialGroup()
                .addGroup(danhSachChuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(danhSachChuyenBayPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(timKiemCBLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(nhapMaCBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(timCBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(danhSachChuyenBayPanelLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(luotCB, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        danhSachChuyenBayPanelLayout.setVerticalGroup(
            danhSachChuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhSachChuyenBayPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(danhSachChuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timKiemCBLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(danhSachChuyenBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nhapMaCBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timCBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(luotCB, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(649, Short.MAX_VALUE))
        );

        mainPanel2.add(danhSachChuyenBayPanel, "card2");

        lichPhanCongBayPanel.setBackground(new java.awt.Color(255, 255, 255));

        nhapMaLichBayTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        nhapMaLichBayTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapMaLichBayTextFieldActionPerformed(evt);
            }
        });

        timLichBayLabel.setBackground(new java.awt.Color(255, 255, 255));
        timLichBayLabel.setFont(new java.awt.Font("UTM Aptima", 1, 14)); // NOI18N
        timLichBayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timLichBayLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/search-interface-symbol (1) (1) (1).png"))); // NOI18N

        timLBButton.setBackground(new java.awt.Color(0, 255, 255));
        timLBButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        timLBButton.setForeground(new java.awt.Color(0, 51, 102));
        timLBButton.setText("Tìm");
        timLBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timLBButtonActionPerformed(evt);
            }
        });

        LichPhanCongBayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên", "Mã Chuyến Bay", "Trạng Thái", "Vai Trò", "Thời Gian Bắt Đầu", "Thời Gian Kết Thúc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        LichPhanCongBayTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        LichPhanCongBayTable.setForeground(new java.awt.Color(0, 51, 102));
        luotLB.setViewportView(LichPhanCongBayTable);

        tiepTucPhanCongButton.setBackground(new java.awt.Color(0, 255, 255));
        tiepTucPhanCongButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        tiepTucPhanCongButton.setForeground(new java.awt.Color(0, 51, 102));
        tiepTucPhanCongButton.setText("Tiếp tục phân công");
        tiepTucPhanCongButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tiepTucPhanCongButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lichPhanCongBayPanelLayout = new javax.swing.GroupLayout(lichPhanCongBayPanel);
        lichPhanCongBayPanel.setLayout(lichPhanCongBayPanelLayout);
        lichPhanCongBayPanelLayout.setHorizontalGroup(
            lichPhanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lichPhanCongBayPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(timLichBayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(nhapMaLichBayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(timLBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(lichPhanCongBayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(luotLB, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lichPhanCongBayPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tiepTucPhanCongButton)
                .addGap(271, 271, 271))
        );
        lichPhanCongBayPanelLayout.setVerticalGroup(
            lichPhanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lichPhanCongBayPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(lichPhanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timLichBayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(lichPhanCongBayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nhapMaLichBayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timLBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(luotLB, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(tiepTucPhanCongButton)
                .addContainerGap(569, Short.MAX_VALUE))
        );

        mainPanel2.add(lichPhanCongBayPanel, "card2");

        lichPhanCongTrucPanel.setBackground(new java.awt.Color(255, 255, 255));

        nhapMaLichTrucTextField.setFont(new java.awt.Font("UTM Aptima", 0, 14)); // NOI18N
        nhapMaLichTrucTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapMaLichTrucTextFieldActionPerformed(evt);
            }
        });

        timLichTrucLabel.setBackground(new java.awt.Color(255, 255, 255));
        timLichTrucLabel.setFont(new java.awt.Font("UTM Aptima", 1, 14)); // NOI18N
        timLichTrucLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timLichTrucLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/QuanLyChuyenBay/search-interface-symbol (1) (1) (1).png"))); // NOI18N

        timLTButton.setBackground(new java.awt.Color(0, 255, 255));
        timLTButton.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        timLTButton.setForeground(new java.awt.Color(0, 51, 102));
        timLTButton.setText("Tìm");
        timLTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timLTButtonActionPerformed(evt);
            }
        });

        LichPhanCongTrucTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên", "Mã Sân Bay", "Trạng Thái", "Vai Trò", "Thời Gian Bắt Đầu", "Thời Gian Kết Thúc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        LichPhanCongTrucTable.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        LichPhanCongTrucTable.setForeground(new java.awt.Color(0, 51, 102));
        LichPhanCongTrucTable.setRowHeight(35);
        luotLT.setViewportView(LichPhanCongTrucTable);

        continueButton.setBackground(new java.awt.Color(0, 255, 255));
        continueButton.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        continueButton.setForeground(new java.awt.Color(0, 51, 102));
        continueButton.setText("Tiếp tục phân công");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lichPhanCongTrucPanelLayout = new javax.swing.GroupLayout(lichPhanCongTrucPanel);
        lichPhanCongTrucPanel.setLayout(lichPhanCongTrucPanelLayout);
        lichPhanCongTrucPanelLayout.setHorizontalGroup(
            lichPhanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lichPhanCongTrucPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(timLichTrucLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(nhapMaLichTrucTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(timLTButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(lichPhanCongTrucPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(luotLT, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lichPhanCongTrucPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(continueButton)
                .addGap(271, 271, 271))
        );
        lichPhanCongTrucPanelLayout.setVerticalGroup(
            lichPhanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lichPhanCongTrucPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(lichPhanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timLichTrucLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(lichPhanCongTrucPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nhapMaLichTrucTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timLTButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(luotLT, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(continueButton)
                .addContainerGap(569, Short.MAX_VALUE))
        );

        mainPanel2.add(lichPhanCongTrucPanel, "card2");

        javax.swing.GroupLayout cardLayoutPanel1Layout = new javax.swing.GroupLayout(cardLayoutPanel1);
        cardLayoutPanel1.setLayout(cardLayoutPanel1Layout);
        cardLayoutPanel1Layout.setHorizontalGroup(
            cardLayoutPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 726, Short.MAX_VALUE)
            .addGroup(cardLayoutPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardLayoutPanel1Layout.setVerticalGroup(
            cardLayoutPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(cardLayoutPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardLayoutPanelLayout = new javax.swing.GroupLayout(cardLayoutPanel);
        cardLayoutPanel.setLayout(cardLayoutPanelLayout);
        cardLayoutPanelLayout.setHorizontalGroup(
            cardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardLayoutPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cardLayoutPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardLayoutPanelLayout.setVerticalGroup(
            cardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardLayoutPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(cardLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardLayoutPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(cardLayoutPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        exitButton.setVisible(false);
        String maNhanVien = (String) nhanVienComboBox.getSelectedItem();
        String vaiTro = hienVaiTroLabel.getText();
        String maSanBay = currentSelectedMaSanBay;
        LocalDateTime thoiGianBatDauLDT = thoiGianBatDauPicker.getDateTimePermissive();
        LocalDateTime thoiGianKetThucLDT = thoiGianKetThucPicker.getDateTimePermissive();

        String trangThai = "Đã phân công";

        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            nhanVienComboBox.requestFocus();
            return;
        }

        if (maSanBay == null || maSanBay.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sân bay từ bảng danh sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (vaiTro == null || vaiTro.trim().isEmpty() || vaiTro.equals("Tên vai trò") || vaiTro.equals("Không rõ")) {
            JOptionPane.showMessageDialog(this, "Không thể xác định vai trò của nhân viên đã chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (thoiGianBatDauLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian bắt đầu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            thoiGianBatDauPicker.requestFocus();
            return;
        }

        if (thoiGianKetThucLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            thoiGianKetThucPicker.requestFocus();
            return;
        }

        if (!thoiGianKetThucLDT.isAfter(thoiGianBatDauLDT)) {
            JOptionPane.showMessageDialog(this, "Thời gian kết thúc phải sau thời gian bắt đầu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            thoiGianKetThucPicker.requestFocus();
            return;
        }
        LocalDateTime thoiGianHienTaiLDT = LocalDateTime.now();
        if (thoiGianBatDauLDT.isBefore(thoiGianHienTaiLDT)) {
            JOptionPane.showMessageDialog(this, "Thời gian bắt đầu phải sau hoặc bằng thời điểm hiện tại.", "Lỗi thời gian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Timestamp thoiGianBatDauTS = Timestamp.valueOf(thoiGianBatDauLDT);
        Timestamp thoiGianKetThucTS = Timestamp.valueOf(thoiGianKetThucLDT);

        String sql = "INSERT INTO PHAN_CONG_CA_TRUC (MANHANVIEN, MASANBAY, VAITRO, THOIGIANBATDAU, THOIGIANKETTHUC, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            ps.setString(2, maSanBay);
            ps.setString(3, vaiTro);
            ps.setTimestamp(4, thoiGianBatDauTS);
            ps.setTimestamp(5, thoiGianKetThucTS);
            ps.setString(6, trangThai);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Phân công trực thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                themDuLieuPhanCongTrucTable();
                mainPanel2.removeAll();
                mainPanel2.add(lichPhanCongTrucPanel);
                mainPanel2.repaint();
                mainPanel2.revalidate();

                xoaTrangCacTruong_Truc();
            } else {
                JOptionPane.showMessageDialog(this, "Phân công trực thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1) {
                JOptionPane.showMessageDialog(this, "Lỗi: Phân công này có thể đã tồn tại hoặc trùng lặp với một phân công khác của nhân viên.", "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
            } else if (e.getMessage().contains("ORA-02291")) {
                JOptionPane.showMessageDialog(this, "Lỗi: Mã nhân viên hoặc mã sân bay không hợp lệ.", "Lỗi Khóa Ngoại", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, "Lỗi SQL khi phân công trực: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_addButtonActionPerformed

    private void nhanVienComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhanVienComboBoxActionPerformed
        // TODO add your handling code here:
        Object selectedItem = nhanVienComboBox.getSelectedItem();
        if (selectedItem == null) {
            hienTenNhanVienLabel.setText("");
            hienVaiTroLabel.setText("");
            hienDiaChiLabel.setText("Địa chỉ");
            return;
        }

        String maNhanVienDaChon = selectedItem.toString();
        String sql = "SELECT HOTEN, CHUCVU, DIACHI FROM NHAN_VIEN WHERE MANHANVIEN = ?";
        String tenNhanVien = "";
        String tenVaiTro = "";
        String diaChi = "";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVienDaChon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tenNhanVien = rs.getString("HOTEN");
                    tenVaiTro = rs.getString("CHUCVU");
                    diaChi = rs.getString("DIACHI");
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin tên nhân viên và vai trò cho mã nhân viên: " + maNhanVienDaChon, "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
        hienTenNhanVienLabel.setText(String.valueOf(tenNhanVien));
        hienVaiTroLabel.setText(String.valueOf(tenVaiTro));
        hienDiaChiLabel.setText(String.valueOf(diaChi));
    }//GEN-LAST:event_nhanVienComboBoxActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // TODO add your handling code here:
        xoaTrangCacTruong_Truc();
        addButton.setVisible(true);

    }//GEN-LAST:event_exitButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.add(menuPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
        mainPanel2.setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void phanCongBayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phanCongBayButtonActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.add(phanCongBayPanel);
        mainPanel.repaint();
        mainPanel.revalidate();

        thoatXemButton.setVisible(false);
        mainPanel2.setVisible(true);
        mainPanel2.removeAll();
        mainPanel2.add(danhSachChuyenBayPanel);
        mainPanel2.repaint();
        mainPanel2.revalidate();

    }//GEN-LAST:event_phanCongBayButtonActionPerformed

    private void trangThaiComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trangThaiComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trangThaiComboBoxActionPerformed

    private void themButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themButtonActionPerformed
        // TODO add your handling code here:

        thoatXemButton.setVisible(false);
        String maNhanVien = (String) employeeComboBox.getSelectedItem();
        String maChuyenBay = flightCodeLabel.getText();
        String vaiTro = showRoleLabel.getText();
        LocalDateTime thoiGianBatDauLDT = startTimePicker.getDateTimePermissive();
        LocalDateTime thoiGianKetThucLDT = endTimePicker.getDateTimePermissive();
        String trangThai = "Đã phân công";
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            employeeComboBox.requestFocus();
            return;
        }

        if (maChuyenBay == null || maChuyenBay.trim().isEmpty() || maChuyenBay.equals("Mã chuyến bay")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chuyến bay từ bảng danh sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (vaiTro == null || vaiTro.trim().isEmpty() || vaiTro.equals("Tên vai trò")) {
            JOptionPane.showMessageDialog(this, "Không thể xác định vai trò của nhân viên đã chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (thoiGianBatDauLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian bắt đầu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            startTimePicker.requestFocus();
            return;
        }

        if (thoiGianKetThucLDT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            endTimePicker.requestFocus();
            return;
        }

        Timestamp thoiGianBatDauTS = Timestamp.valueOf(thoiGianBatDauLDT);
        Timestamp thoiGianKetThucTS = Timestamp.valueOf(thoiGianKetThucLDT);

        String sql = "INSERT INTO PHAN_CONG_BAY (MANHANVIEN, MACHUYENBAY, VAITRO, THOIGIANBATDAU, THOIGIANKETTHUC, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            ps.setString(2, maChuyenBay);
            ps.setString(3, vaiTro);
            ps.setTimestamp(4, thoiGianBatDauTS);
            ps.setTimestamp(5, thoiGianKetThucTS);
            ps.setString(6, trangThai);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Phân công bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                themDuLieuPhanCongBayTable();
                mainPanel2.removeAll();
                mainPanel2.add(lichPhanCongBayPanel);
                mainPanel2.repaint();
                mainPanel2.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Phân công bay thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi phân công bay: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_themButtonActionPerformed

    private void employeeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeComboBoxActionPerformed
        // TODO add your handling code here:
        Object selectedItem = employeeComboBox.getSelectedItem();
        if (selectedItem == null) {
            showEmployeeNameLabel.setText("");
            showRoleLabel.setText("");
            return;
        }

        String maNhanVienDaChon = selectedItem.toString();
        String sql = "SELECT HOTEN, CHUCVU FROM NHAN_VIEN WHERE MANHANVIEN = ?";
        String tenNhanVien = "";
        String tenVaiTro = "";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVienDaChon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tenNhanVien = rs.getString("HOTEN");
                    tenVaiTro = rs.getString("CHUCVU");
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin tên nhân viên và vai trò cho mã nhân viên: " + maNhanVienDaChon, "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lỗi: " + e.getMessage());
        }
        showEmployeeNameLabel.setText(String.valueOf(tenNhanVien));
        showRoleLabel.setText(String.valueOf(tenVaiTro));

    }//GEN-LAST:event_employeeComboBoxActionPerformed

    private void thoatXemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoatXemButtonActionPerformed
        // TODO add your handling code here:
        xoaTrangCacTruong();
        themButton.setVisible(true);

    }//GEN-LAST:event_thoatXemButtonActionPerformed

    private void quayLaiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quayLaiButtonActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.add(menuPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
        mainPanel2.setVisible(false);
    }//GEN-LAST:event_quayLaiButtonActionPerformed

    private void phanCongTrucButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phanCongTrucButtonActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.add(phanCongTrucPanel);
        mainPanel.repaint();
        mainPanel.revalidate();

        exitButton.setVisible(false);
        mainPanel2.setVisible(true);
        mainPanel2.removeAll();
        mainPanel2.add(danhSachSanBayPanel);
        mainPanel2.repaint();
        mainPanel2.revalidate();

    }//GEN-LAST:event_phanCongTrucButtonActionPerformed

    private void stateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stateComboBoxActionPerformed

    private void trucButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trucButtonActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.add(phanCongTrucPanel);
        mainPanel.repaint();
        mainPanel.revalidate();

        exitButton.setVisible(false);
        mainPanel2.setVisible(true);
        mainPanel2.removeAll();
        mainPanel2.add(danhSachSanBayPanel);
        mainPanel2.repaint();
        mainPanel2.revalidate();

    }//GEN-LAST:event_trucButtonActionPerformed

    private void bayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayButtonActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.add(phanCongBayPanel);
        mainPanel.repaint();
        mainPanel.revalidate();

        thoatXemButton.setVisible(false);
        mainPanel2.setVisible(true);
        mainPanel2.removeAll();
        mainPanel2.add(danhSachChuyenBayPanel);
        mainPanel2.repaint();
        mainPanel2.revalidate();

    }//GEN-LAST:event_bayButtonActionPerformed

    private void nhapTenSBTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapTenSBTextFieldActionPerformed
        // TODO add your handling code here:
        timSanBay();
    }//GEN-LAST:event_nhapTenSBTextFieldActionPerformed

    private void timSBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timSBButtonActionPerformed
        // TODO add your handling code here:
        timSanBay();
    }//GEN-LAST:event_timSBButtonActionPerformed

    private void nhapMaCBTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapMaCBTextFieldActionPerformed
        // TODO add your handling code here:
        timChuyenBay();
    }//GEN-LAST:event_nhapMaCBTextFieldActionPerformed

    private void timCBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timCBButtonActionPerformed
        // TODO add your handling code here:
        timChuyenBay();
    }//GEN-LAST:event_timCBButtonActionPerformed

    private void nhapMaLichBayTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapMaLichBayTextFieldActionPerformed
        // TODO add your handling code here:
        timLichBay();
    }//GEN-LAST:event_nhapMaLichBayTextFieldActionPerformed

    private void timLBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timLBButtonActionPerformed
        // TODO add your handling code here:
        timLichBay();
    }//GEN-LAST:event_timLBButtonActionPerformed

    private void tiepTucPhanCongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiepTucPhanCongButtonActionPerformed
        // TODO add your handling code here:
        mainPanel2.removeAll();
        mainPanel2.add(danhSachChuyenBayPanel);
        mainPanel2.repaint();
        mainPanel2.revalidate();

    }//GEN-LAST:event_tiepTucPhanCongButtonActionPerformed

    private void nhapMaLichTrucTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapMaLichTrucTextFieldActionPerformed
        // TODO add your handling code here:
        timLichTruc();
    }//GEN-LAST:event_nhapMaLichTrucTextFieldActionPerformed

    private void timLTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timLTButtonActionPerformed
        // TODO add your handling code here:
        timLichTruc();
    }//GEN-LAST:event_timLTButtonActionPerformed

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueButtonActionPerformed
        // TODO add your handling code here:
        mainPanel2.removeAll();
        mainPanel2.add(danhSachSanBayPanel);
        mainPanel2.repaint();
        mainPanel2.revalidate();
    }//GEN-LAST:event_continueButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DanhSachChuyenBayTable;
    private javax.swing.JTable DanhSachSanBayTable;
    private javax.swing.JTable LichPhanCongBayTable;
    private javax.swing.JTable LichPhanCongTrucTable;
    private javax.swing.JButton addButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton bayButton;
    private javax.swing.JPanel cardLayoutPanel;
    private javax.swing.JPanel cardLayoutPanel1;
    private javax.swing.JButton continueButton;
    private javax.swing.JPanel danhSachChuyenBayPanel;
    private javax.swing.JPanel danhSachSanBayPanel;
    private javax.swing.JLabel diaChiLabel;
    private javax.swing.JComboBox<String> employeeComboBox;
    private javax.swing.JLabel employeeLabel;
    private javax.swing.JLabel employeeNameLabel;
    private javax.swing.JLabel endTimeLabel;
    private com.github.lgooddatepicker.components.DateTimePicker endTimePicker;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel flightCodeLabel;
    private javax.swing.JLabel flightLabel;
    private javax.swing.JLabel hienDiaChiLabel;
    private javax.swing.JLabel hienTenNhanVienLabel;
    private javax.swing.JLabel hienVaiTroLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel lichPhanCongBayPanel;
    private javax.swing.JPanel lichPhanCongTrucPanel;
    private javax.swing.JScrollPane luotCB;
    private javax.swing.JScrollPane luotLB;
    private javax.swing.JScrollPane luotLT;
    private javax.swing.JScrollPane luotSB;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mainPanel2;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JComboBox<String> nhanVienComboBox;
    private javax.swing.JLabel nhanVienLabel;
    private javax.swing.JTextField nhapMaCBTextField;
    private javax.swing.JTextField nhapMaLichBayTextField;
    private javax.swing.JTextField nhapMaLichTrucTextField;
    private javax.swing.JTextField nhapTenSBTextField;
    private javax.swing.JButton phanCongBayButton;
    private javax.swing.JLabel phanCongBayLabel;
    private javax.swing.JPanel phanCongBayPanel;
    private javax.swing.JButton phanCongTrucButton;
    private javax.swing.JLabel phanCongTrucLabel;
    private javax.swing.JPanel phanCongTrucPanel;
    private javax.swing.JButton quayLaiButton;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JLabel sanBayLabel;
    private javax.swing.JLabel showEmployeeNameLabel;
    private javax.swing.JLabel showRoleLabel;
    private javax.swing.JLabel startTimeLabel;
    private com.github.lgooddatepicker.components.DateTimePicker startTimePicker;
    private javax.swing.JComboBox<String> stateComboBox;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JLabel tenNhanVienLabel;
    private javax.swing.JLabel tenSanBayLabel;
    private javax.swing.JButton themButton;
    private javax.swing.JButton thoatXemButton;
    private javax.swing.JLabel thoiGianBatDauLabel;
    private com.github.lgooddatepicker.components.DateTimePicker thoiGianBatDauPicker;
    private javax.swing.JLabel thoiGianKetThucLabel;
    private com.github.lgooddatepicker.components.DateTimePicker thoiGianKetThucPicker;
    private javax.swing.JButton tiepTucPhanCongButton;
    private javax.swing.JButton timCBButton;
    private javax.swing.JLabel timKiemCBLabel;
    private javax.swing.JLabel timKiemSBLabel;
    private javax.swing.JButton timLBButton;
    private javax.swing.JButton timLTButton;
    private javax.swing.JLabel timLichBayLabel;
    private javax.swing.JLabel timLichTrucLabel;
    private javax.swing.JButton timSBButton;
    private javax.swing.JComboBox<String> trangThaiComboBox;
    private javax.swing.JLabel trangThaiLabel;
    private javax.swing.JButton trucButton;
    private javax.swing.JLabel vaiTroLabel;
    // End of variables declaration//GEN-END:variables
}
