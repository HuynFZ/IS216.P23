package View.Admin;

import ConnectDB.ConnectionUtils;       // import lớp ConnectionUtils
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.sql.Timestamp;
import javax.swing.event.ListSelectionEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import java.util.HashMap;
import java.util.Map;


public class QLHanhLyForm extends javax.swing.JPanel {
    CardLayout cardMaChuyen, cardMaVe, cardMaHK, cardGoi, cardTrangThai;
    private enum Mode { NONE, VIEW, ADD, DELETE, EDIT }
    private Mode currentMode = Mode.NONE;
    private final Set<JTextField> initializedPlaceholders = new HashSet<>();
    private DefaultListModel<String> listModelCB;
    private JList<String> suggestionListCB;
    private JWindow suggestionWindowCB;
    private Map<String, String> goiHanhLyMap; // Map để lưu TenGoiHanhLy -> MaGoiHanhLy

    public QLHanhLyForm() {
        // Khởi tạo các components
        initComponents();
        // Khởi tạo các CardLayout
        initCardLayout();
        // Khởi tạo Map lưu gói hành lý và nạp dữ liệu
        goiHanhLyMap = new HashMap<>(); 
        loadGoiHanhLyMap();
        // Nạp dữ liệu chuyến bay lên bảng tblDSChuyen
        loadChuyenBayToTable();
        // Nạp mã chuyến bay lên combobox cmbMaChuyenEdit
        loadMaChuyenBayToComboBox();
        // Thêm các listener cho comboboxes
        addListenersForHanhLyForm(); 
        // Khởi tạo trạng thái ban đầu của cmbMaVeEdit và cmbMaHKEdit (rỗng)
        loadMaVeToComboBox(""); 
        loadMaHKToComboBox("");
        // Đăng ký listener cho việc chọn dòng Chuyến bay
        initChuyenBaySelectionListener();
        // Đăng ký listener cho việc chọn dòng hành lý
        initHanhLySelectionListener();
        // Set chế độ NONE ban đầu
        setMode(Mode.NONE);
        // Set placeholder cho thanh tìm kiếm
        setPlaceholder(txtTimKiem, "Nhập mã chuyến bay để tìm kiếm");
        // Thêm gợi ý tự động cho tính năng tìm kiếm
        addAutoSuggestToTimKiem();  
    }
    
    // Khởi tạo các cardLayout
    private void initCardLayout() {
        cardMaChuyen = (CardLayout) pnlMaChuyenCards.getLayout();
        cardMaVe = (CardLayout) pnlMaVeCards.getLayout();
        cardMaHK = (CardLayout) pnlMaHKCards.getLayout();
        cardGoi = (CardLayout) pnlGoiCards.getLayout();
        cardTrangThai = (CardLayout) pnlTrangThaiCards.getLayout();
    }

    // Hàm setMode
    private void setMode(Mode mode) {
        switch (mode) {
            case VIEW:
                // Chỉnh mode sang VIEW
                currentMode = Mode.VIEW;
                applyNormalStyle();
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thông tin hành lý");
                // Tắt btnXacNhan và btnHuy
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                // Không cho phép chỉnh sửa
                setThaoTacEditable(false);
                // Bật các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(true);
                btnXoa.setEnabled(true);
                btnSua.setEnabled(true);
                // Ẩn dấu * 
                showRequiredIndicators(false); 
                break;
            case ADD:
                // Chỉnh mode sang ADD
                currentMode = Mode.ADD;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thêm hành lý");
                // Clear các trường thông tin trong form
                clearFormInput();
                // Bật btnXacNhan và đổi text
                btnXacNhan.setVisible(true);
                btnXacNhan.setText("Xác nhận thêm");
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Áp dụng placeholder
                applyPlaceholders();
                // Cho phép chỉnh sửa
                setThaoTacEditable(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(false);
                btnXoa.setEnabled(false);
                btnSua.setEnabled(false);
                // Hiện dấu * 
                showRequiredIndicators(true); 
                break;
            case EDIT:
                // Chỉnh mode sang EDIT
                currentMode = Mode.EDIT;
                applyNormalStyle();
                // Đổi text lblThaoTac
                lblThaoTac.setText("Sửa hành lý");
                // Bật btnXacNhan và đổi text
                btnXacNhan.setText("Xác nhận sửa");
                btnXacNhan.setVisible(true);
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Cho phép chỉnh sửa
                setThaoTacEditable(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(false);
                btnXoa.setEnabled(false);
                btnSua.setEnabled(false);
                // Hiện dấu * 
                showRequiredIndicators(true); 
                break;
            case DELETE:
                // Chỉnh mode sang DELETE
                currentMode = Mode.DELETE;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Xóa hành lý");
                // Bật btnXacNhan và đổi text
                btnXacNhan.setVisible(true);
                btnXacNhan.setText("Xác nhận xóa");
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(false);
                btnXoa.setEnabled(false);
                btnSua.setEnabled(false);
                // Ẩn dấu * 
                showRequiredIndicators(false); 
                break;
            default:
                // Chỉnh mode sang NONE
                currentMode = Mode.NONE;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thông tin hành lý");
                // Clear các trường thông tin trong form
                clearFormInput();                
                // Tắt nút Xác nhận, Hủy
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                // Áp dụng placeholder
                applyPlaceholders();
                // Không cho phép chỉnh sửa
                setThaoTacEditable(false);
                // Bật các nút Thêm, Xóa, sửa
                btnThem.setEnabled(true);
                btnXoa.setEnabled(true);
                btnSua.setEnabled(true);
                // Ẩn dấu * 
                showRequiredIndicators(false); 
                break;
        }
    }
    
    // Tắt, bật chỉnh sửa cho các component
    private void setThaoTacEditable(boolean editable) {
        // JPanel chứa cards
        String statusCard = null;
        switch (currentMode) {
            case NONE:
            case VIEW:
            case DELETE:
                statusCard = "VIEW";
                break;
            case ADD:
            case EDIT:
                statusCard = "EDIT";
                break;
        }
        cardMaChuyen.show(pnlMaChuyenCards, statusCard);
        cardMaVe.show(pnlMaVeCards, statusCard);
        cardMaHK.show(pnlMaHKCards, statusCard);
        cardGoi.show(pnlGoiCards, statusCard);
        cardTrangThai.show(pnlTrangThaiCards, statusCard);
   
        // Xử lý Mã hành lý, Tên hành khách không được thêm, sửa
        if (editable == true) {
            txtMa.setEnabled(false);
            txtMaHKView.setEnabled(false);
            txtTenHK.setEnabled(false);
        }
        
        // JTextField
        txtMa.setEditable(false);
        txtMaChuyenView.setEditable(editable);
        txtMaVeView.setEditable(editable);
        txtMaHKView.setEditable(false);
        txtTenHK.setEditable(false);
        txtTrongLuong.setEditable(editable);
        txtGoiView.setEditable(editable);
        txtTrangThaiView.setEditable(editable);
        txtViTri.setEditable(editable);

        // JComboBox (nếu có)
        cmbMaChuyenEdit.setEnabled(editable);
        cmbMaVeEdit.setEnabled(editable);
        cmbMaHKEdit.setEnabled(editable);
        cmbGoiEdit.setEnabled(editable);
        cmbTrangThaiEdit.setEnabled(editable);
    }
    
    @SuppressWarnings("unchecked")
    
    private void loadGoiHanhLyMap() {
        String sql = "SELECT MaGoiHanhLy, TenGoiHanhLy FROM GOI_HANH_LY";

        try (Connection conn = ConnectionUtils.getMyConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // Sử dụng try-with-resources cho ResultSet

            goiHanhLyMap.clear(); // Xóa dữ liệu cũ nếu có trước khi nạp lại

            while (rs.next()) {
                String maGoiHanhLy = rs.getString("MaGoiHanhLy");
                String tenGoiHanhLy = rs.getString("TenGoiHanhLy");
                goiHanhLyMap.put(tenGoiHanhLy, maGoiHanhLy); // Lưu TênGóiHanhLy làm key, MaGoiHanhLy làm value
            }
            System.out.println("Đã nạp thành công " + goiHanhLyMap.size() + " gói hành lý.");

        } catch (SQLException ex) {
            // Xử lý ngoại lệ SQL, ví dụ: hiển thị thông báo lỗi hoặc ghi log
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách gói hành lý từ CSDL: " + ex.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            // Nếu có hàm handleHanhLyError, bạn có thể gọi ở đây:
            // handleHanhLyError(ex, "loadGoiHanhLyMap");
        } catch (Exception ex) {
            // Xử lý các ngoại lệ khác
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi tải gói hành lý: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadChuyenBayToTable() {
        DefaultTableModel model = (DefaultTableModel) tblDSChuyen.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng
        
        String sql = """
            SELECT cb.MaChuyenBay, tb.SanBayDi, tb.SanBayDen,
                   cb.GioCatCanh, cb.GioHaCanh, cb.TrangThai
            FROM CHUYEN_BAY cb
            JOIN TUYEN_BAY tb ON cb.MaTuyenBay = tb.MaTuyenBay
            ORDER BY cb.GioCatCanh DESC
        """;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try (Connection conn = ConnectionUtils.getMyConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Timestamp gioCatCanh = rs.getTimestamp("GioCatCanh");
                Timestamp gioHaCanh = rs.getTimestamp("GioHaCanh");

                String gioCatCanhStr = gioCatCanh != null
                    ? sdf.format(new java.util.Date(gioCatCanh.getTime()))
                    : "";

                String gioHaCanhStr = gioHaCanh != null
                    ? sdf.format(new java.util.Date(gioHaCanh.getTime()))
                    : "";

                model.addRow(new Object[]{
                    rs.getString("MaChuyenBay"),
                    rs.getString("SanBayDi"),
                    rs.getString("SanBayDen"),
                    gioCatCanhStr,
                    gioHaCanhStr,
                    rs.getString("TrangThai")
                });
            }

            tblDSChuyen.setModel(model);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu chuyến bay:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMaChuyenBayToComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        List<String> maChuyenBays = new ArrayList<>();
        String sql = "SELECT MaChuyenBay FROM CHUYEN_BAY";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                maChuyenBays.add(rs.getString("MaChuyenBay"));
            }

            // Thêm các mã chuyến bay vào DefaultComboBoxModel
            for (String maCB : maChuyenBays) {
                model.addElement(maCB);
            }

            // Set model cho JComboBox
            cmbMaChuyenEdit.setModel(model);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải mã chuyến bay:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addListenersForHanhLyForm() {
        // Lưu trữ các listeners để dễ dàng gỡ bỏ và thêm lại
        ItemListener maChuyenListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMaChuyenBay = (String) cmbMaChuyenEdit.getSelectedItem();

                    // Gỡ bỏ listener của cmbMaVeEdit trước khi tải lại
                    removeItemListeners(cmbMaVeEdit);
                    loadMaVeToComboBox(selectedMaChuyenBay);
                    // Thêm lại listener sau khi đã tải xong
                    addMaVeListener(); // Hàm mới để thêm lại listener
                }
            }
        };
        cmbMaChuyenEdit.addItemListener(maChuyenListener);

        // Hàm để thêm lại listener cho cmbMaVeEdit
        // Điều này giúp tránh kích hoạt không mong muốn khi setModel
        addMaVeListener(); 

        // Hàm để thêm lại listener cho cmbMaHKEdit
        addMaHKListener();
    }
    
    // Helper method để gỡ bỏ tất cả ItemListener khỏi một JComboBox
    private void removeItemListeners(javax.swing.JComboBox<?> comboBox) {
        for (ItemListener listener : comboBox.getItemListeners()) {
            comboBox.removeItemListener(listener);
        }
    }
    
    // Hàm riêng để thêm ItemListener cho cmbMaVeEdit
    private void addMaVeListener() {
        // Gỡ bỏ listeners cũ để tránh trùng lặp
        removeItemListeners(cmbMaVeEdit);
        cmbMaVeEdit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMaVe = (String) cmbMaVeEdit.getSelectedItem();

                    // Gỡ bỏ listener của cmbMaHKEdit trước khi tải lại
                    removeItemListeners(cmbMaHKEdit);
                    loadMaHKToComboBox(selectedMaVe);
                    // Thêm lại listener sau khi đã tải xong
                    addMaHKListener(); // Hàm mới để thêm lại listener
                }
            }
        });
    }

    // Hàm riêng để thêm ItemListener cho cmbMaHKEdit
    private void addMaHKListener() {
        // Gỡ bỏ listeners cũ để tránh trùng lặp
        removeItemListeners(cmbMaHKEdit);
        cmbMaHKEdit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMaHK = (String) cmbMaHKEdit.getSelectedItem();
                    // Chỉ fillTenHK và loadGoiHanhLyByHanhKhach nếu item không rỗng
                    if (selectedMaHK != null && !selectedMaHK.trim().isEmpty()) {
                        fillTenHK(selectedMaHK);
                        loadGoiHanhLyByHanhKhach(selectedMaHK);
                    } else {
                        txtTenHK.setText(""); // Xóa tên hành khách nếu chọn item rỗng
                        // Clear gói hành lý nếu chọn item rỗng
                        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                        model.addElement("");
                        cmbGoiEdit.setModel(model);
                    }
                }
            }
        });
    }
    
    private void loadMaVeToComboBox(String maChuyenBay) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(""); // Thêm một item rỗng

        if (maChuyenBay == null || maChuyenBay.isEmpty() || "null".equals(maChuyenBay)) { // Thêm kiểm tra "null"
            cmbMaVeEdit.setModel(model); // Đặt lại model rỗng
            txtMaHKView.setText("");
            txtTenHK.setText("");
            // Đặt lại cmbMaHKEdit và cmbGoiEdit về rỗng
            ((DefaultComboBoxModel) cmbMaHKEdit.getModel()).removeAllElements();
            ((DefaultComboBoxModel) cmbMaHKEdit.getModel()).addElement("");
            ((DefaultComboBoxModel) cmbGoiEdit.getModel()).removeAllElements();
            ((DefaultComboBoxModel) cmbGoiEdit.getModel()).addElement("");
            return;
        }

        String sql = "SELECT MaVe FROM VE_MAY_BAY WHERE MaChuyenBay = ? ORDER BY MaVe";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyenBay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addElement(rs.getString("MaVe"));
                }
            }
            cmbMaVeEdit.setModel(model);

            // Sau khi đặt model, tự động chọn item rỗng để kích hoạt listener của cmbMaVeEdit
            // Cái này sẽ kích hoạt loadMaHKToComboBox("")
            cmbMaVeEdit.setSelectedItem(""); // Chọn item rỗng

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải mã vé cho chuyến bay " + maChuyenBay + ":\n" + ex.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMaHKToComboBox(String maVe) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(""); // Thêm một item rỗng

        // Nếu mã vé rỗng hoặc null, không cần tải gì cả, chỉ đặt lại model
        if (maVe == null || maVe.trim().isEmpty() || "null".equals(maVe)) { // Thêm kiểm tra "null"
            cmbMaHKEdit.setModel(model);
            txtTenHK.setText(""); // Xóa tên hành khách cũ
            // Đặt lại cmbGoiEdit về rỗng
            ((DefaultComboBoxModel) cmbGoiEdit.getModel()).removeAllElements();
            ((DefaultComboBoxModel) cmbGoiEdit.getModel()).addElement("");
            return;
        }

        // Câu lệnh SQL để lấy Mã Hành Khách dựa trên Mã Vé
        String sql = """
            SELECT MaHanhKhach
            FROM HANH_KHACH
            WHERE MaVe = ?
            ORDER BY HoTen
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVe);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addElement(rs.getString("MaHanhKhach"));
                }
            }
            cmbMaHKEdit.setModel(model); // Cập nhật model cho cmbMaHKEdit

            // Chọn item rỗng để kích hoạt listener của cmbMaHKEdit
            cmbMaHKEdit.setSelectedItem(""); // Chọn item rỗng

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải mã hành khách cho vé " + maVe + ":\n" + ex.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fillTenHK(String maHanhKhach) {
        // Xóa tên hành khách cũ trước
        txtTenHK.setText("");

        // Nếu mã hành khách rỗng hoặc null, không cần tải gì cả
        if (maHanhKhach == null || maHanhKhach.trim().isEmpty()) {
            return;
        }

        String sql = "SELECT HoTen FROM HANH_KHACH WHERE MaHanhKhach = ?";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHanhKhach);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtTenHK.setText(rs.getString("HoTen")); // Điền họ tên vào txtTenHK
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải tên hành khách cho mã " + maHanhKhach + ":\n" + ex.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadGoiHanhLyByHanhKhach(String maHanhKhach) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Nếu mã hành khách rỗng hoặc null, không cần tải gì cả, chỉ đặt lại model
        if (maHanhKhach == null || maHanhKhach.trim().isEmpty()) {
            cmbGoiEdit.setModel(model);
            return;
        }
        
        // Câu lệnh SQL để lấy Mã Gói Hành Lý và Tên Gói Hành Lý
        String sql = """
            SELECT DISTINCT ghl.MaGoiHanhLy, ghl.TenGoiHanhLy
            FROM GOI_HANH_LY ghl
            ORDER BY ghl.MaGoiHanhLy
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maGoiHanhLy = rs.getString("MaGoiHanhLy");
                    String tenGoiHanhLy = rs.getString("TenGoiHanhLy");
                    model.addElement(tenGoiHanhLy); // Hiển thị TenGoiHanhLy trong combobox
                }
            }
            cmbGoiEdit.setModel(model);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải gói hành lý cho hành khách " + maHanhKhach + ":\n" + ex.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initChuyenBaySelectionListener() {
        tblDSChuyen.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return; // Đảm bảo sự kiện chỉ được xử lý một lần khi lựa chọn ổn định
                }

                int selectedRow = tblDSChuyen.getSelectedRow();
                if (selectedRow < 0) { // Không có dòng nào được chọn
                    txtMaChuyenView.setText("");
                    // Xóa bảng hành lý
                    DefaultTableModel modelHanhLy = (DefaultTableModel) tblDSHanhLy.getModel();
                    if (modelHanhLy != null) { // Kiểm tra model có tồn tại không
                        modelHanhLy.setRowCount(0);
                    }
                    setMode(Mode.NONE); // Có thể đặt trạng thái không có gì được chọn
                    return;
                }

                String maChuyenBay = tblDSChuyen.getValueAt(selectedRow, 0).toString();
                txtMaChuyenView.setText(maChuyenBay); // Hiển thị mã chuyến bay đã chọn

                // Tải danh sách hành lý cho chuyến bay đã chọn
                loadHanhLyToTable(maChuyenBay);
            }
        });
    }
    
        /*
     * Tải và hiển thị danh sách hành lý của một chuyến bay cụ thể lên tblDSHanhLy.
     * Các cột hiển thị: Mã hành lý, Trọng lượng, Tên gói hành lý, Vị trí hành lý.
     *
     * @param maChuyenBay Mã của chuyến bay cần tải hành lý.
     */
    private void loadHanhLyToTable(String maChuyenBay) {
        DefaultTableModel model = (DefaultTableModel) tblDSHanhLy.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng hành lý

        // 1. Kiểm tra mã chuyến bay đầu vào
        if (maChuyenBay == null || maChuyenBay.trim().isEmpty()) {
            // Nếu mã chuyến bay rỗng, không hiển thị gì 
             lblDSHanhLy.setText("Danh sách hành lý"); // Đặt lại tiêu đề mặc định
            return;
        }

        // Câu lệnh SQL để lấy MaHanhLy, TrongLuong, TenGoiHanhLy, ViTriHanhLy
        // từ các bảng liên quan đến vé máy bay của chuyến bay được chỉ định.
        String sql = """
            SELECT
                hl.MaHanhLy,
                hl.TrongLuong,
                ghl.TenGoiHanhLy,
                hl.ViTriHanhLy
            FROM
                HANH_LY hl
            JOIN
                GOI_HANH_LY ghl ON hl.MaGoiHanhLy = ghl.MaGoiHanhLy
            JOIN
                HANH_KHACH hk ON hl.MaHanhKhach = hk.MaHanhKhach
            JOIN
                VE_MAY_BAY vmb ON hk.MaVe = vmb.MaVe
            WHERE
                vmb.MaChuyenBay = ?
            ORDER BY
                hl.MaHanhLy
        """;

        try (Connection conn = ConnectionUtils.getMyConnection(); // Sử dụng lớp ConnectionUtils 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyenBay);

            // Cập nhật tiêu đề của panel/tab để hiển thị chuyến bay hiện tại
            lblDSHanhLy.setText("Danh sách hành lý của chuyến bay " + maChuyenBay);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String maHanhLy = rs.getString("MaHanhLy");
                    double trongLuong = rs.getDouble("TrongLuong"); // Lấy trọng lượng
                    String tenGoiHanhLy = rs.getString("TenGoiHanhLy"); // Lấy tên gói hành lý
                    String viTriHanhLy = rs.getString("ViTriHanhLy");

                    // Thêm dòng mới vào table model
                    model.addRow(new Object[]{maHanhLy, trongLuong, tenGoiHanhLy, viTriHanhLy});
                    found = true;
                }

                if (!found) {
                    // Thông báo nếu không tìm thấy hành lý nào cho chuyến bay này
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hành lý nào cho chuyến bay " + maChuyenBay + ".", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi SQL khi tải danh sách hành lý:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Bắt các lỗi khác như ClassNotFoundException từ ConnectionUtils
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi không xác định khi tải danh sách hành lý:\n" + ex.getMessage(),
                    "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
        }

        // Sau khi tải xong, bỏ chọn các dòng hiện có trong bảng
        tblDSHanhLy.clearSelection();
        clearFormInput(); // Làm trống form chi tiết hành lý
        setMode(Mode.VIEW);
    }
    
    private void initHanhLySelectionListener() {
        tblDSHanhLy.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return; // Đảm bảo sự kiện chỉ được xử lý một lần khi lựa chọn ổn định
                }

                int selectedRow = tblDSHanhLy.getSelectedRow();
                if (selectedRow < 0) { // Không có dòng nào được chọn
                    clearFormInput(); // Xóa tất cả các trường input trên form
                    setMode(Mode.NONE); // Đặt mode không có gì được chọn
                    return;
                }

                setMode(Mode.VIEW); // Chuyển sang chế độ xem chi tiết

                // Lấy thông tin cơ bản từ các cột của tblDSHanhLy
                // Dựa trên thứ tự cột mới: Mã hành lý, Trọng lượng, Gói hành lý (TenGoiHanhLy), Vị trí hành lý
                String maHanhLy = getValueAtSafely(tblDSHanhLy, selectedRow, 0);
                String trongLuongTrongBang = getValueAtSafely(tblDSHanhLy, selectedRow, 1); // Trọng lượng
                String tenGoiHanhLyTrongBang = getValueAtSafely(tblDSHanhLy, selectedRow, 2); // Tên gói hành lý
                String viTriHanhLyTrongBang = getValueAtSafely(tblDSHanhLy, selectedRow, 3); // Vị trí hành lý

                // Hiển thị các thông tin lấy trực tiếp từ bảng vào các JTextField tương ứng
                txtMa.setText(maHanhLy);
                txtTrongLuong.setText(trongLuongTrongBang);
                txtGoiView.setText(tenGoiHanhLyTrongBang); // txtGoiView hiển thị tên gói hành lý
                txtViTri.setText(viTriHanhLyTrongBang);

                // Nạp thêm các thông tin chi tiết khác từ database bằng mã hành lý
                if (maHanhLy != null && !maHanhLy.isEmpty()) {
                    loadHanhLyDetails(maHanhLy);
                } else {
                    // Trường hợp mã hành lý rỗng từ bảng, xóa các trường liên quan
                    clearFormInput();
                }
            }
        });
    }
    
    private void loadHanhLyDetails(String maHanhLy) {
        // Luôn gọi hàm này để xóa các trường chi tiết và reset các combobox
        // trước khi tải dữ liệu mới.
        clearFormInput();

        String sql = """
            SELECT
                hl.MaHanhLy,
                hl.MaGoiHanhLy,
                hl.MaHanhKhach,
                hl.TrongLuong,
                hl.TrangThai,
                hl.ViTriHanhLy,
                ghl.TenGoiHanhLy,
                hk.MaVe,
                hk.HoTen AS TenHanhKhach,
                vmb.MaChuyenBay
            FROM
                HANH_LY hl
            JOIN
                GOI_HANH_LY ghl ON hl.MaGoiHanhLy = ghl.MaGoiHanhLy
            JOIN
                HANH_KHACH hk ON hl.MaHanhKhach = hk.MaHanhKhach
            JOIN
                VE_MAY_BAY vmb ON hk.MaVe = vmb.MaVe
            WHERE
                hl.MaHanhLy = ?
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHanhLy);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Lấy dữ liệu từ ResultSet
                    String maGoiHanhLyDB = rs.getString("MaGoiHanhLy");
                    String maHanhKhachDB = rs.getString("MaHanhKhach");
                    BigDecimal trongLuongBD = rs.getBigDecimal("TrongLuong");
                    String trangThaiDB = rs.getString("TrangThai");
                    String viTriHanhLyDB = rs.getString("ViTriHanhLy");
                    String tenGoiHanhLyDB = rs.getString("TenGoiHanhLy"); // Lấy TenGoiHanhLy trực tiếp từ DB
                    String maVeDB = rs.getString("MaVe");
                    String tenHanhKhachDB = rs.getString("TenHanhKhach");
                    String maChuyenBayDB = rs.getString("MaChuyenBay");

                    // --- Điền dữ liệu vào các JTextField ---
                    txtMa.setText(maHanhLy);
                    txtTrongLuong.setText(trongLuongBD != null ? new DecimalFormat("#0.00").format(trongLuongBD) : "");
                    txtViTri.setText(viTriHanhLyDB != null ? viTriHanhLyDB : "");
                    txtGoiView.setText(tenGoiHanhLyDB != null ? tenGoiHanhLyDB : ""); // Hiển thị Tên Gói Hành Lý
                    txtMaChuyenView.setText(maChuyenBayDB != null ? maChuyenBayDB : "");
                    txtMaVeView.setText(maVeDB != null ? maVeDB : "");
                    txtMaHKView.setText(maHanhKhachDB != null ? maHanhKhachDB : "");
                    txtTenHK.setText(tenHanhKhachDB != null ? tenHanhKhachDB : "");
                    txtTrangThaiView.setText(trangThaiDB != null ? trangThaiDB : "");


                    // --- Điền dữ liệu vào các JComboBox (cho chế độ chỉnh sửa) ---

                    // 1. Load và chọn MaChuyenBay
                    // Gỡ bỏ listener tạm thời để tránh kích hoạt không mong muốn
                    removeItemListeners(cmbMaChuyenEdit);
                    loadMaChuyenBayToComboBox(); // Đảm bảo hàm này nạp đủ các MaChuyenBay
                    cmbMaChuyenEdit.setSelectedItem(maChuyenBayDB);

                    // 2. Load và chọn MaVe
                    removeItemListeners(cmbMaVeEdit);
                    // loadMaVeToComboBox sẽ được gọi bởi listener của cmbMaChuyenEdit,
                    // nhưng nếu bạn không muốn phụ thuộc vào đó, có thể gọi trực tiếp ở đây.
                    // Tuy nhiên, việc kích hoạt listener của cmbMaChuyenEdit là cách tốt hơn để đảm bảo luồng
                    // Tuy nhiên, khi loadHanhLyDetails được gọi độc lập (ví dụ, từ bảng),
                    // chúng ta cần đảm bảo các ComboBoxs được nạp đúng thứ tự.
                    loadMaVeToComboBox(maChuyenBayDB); // Nạp mã vé dựa trên mã chuyến bay
                    cmbMaVeEdit.setSelectedItem(maVeDB);
                    addMaVeListener();


                    // 3. Load và chọn MaHanhKhach
                    removeItemListeners(cmbMaHKEdit);
                    loadMaHKToComboBox(maVeDB); // Nạp mã hành khách dựa trên mã vé
                    cmbMaHKEdit.setSelectedItem(maHanhKhachDB);
                    addMaHKListener();


                    // 4. Load và chọn Gói Hành Lý (TenGoiHanhLy)
                    removeItemListeners(cmbGoiEdit); // Gỡ bỏ listener của cmbGoiEdit
                    // Gọi hàm nạp gói hành lý dựa trên MaHanhKhach
                    loadGoiHanhLyByHanhKhach(maHanhKhachDB);

                    // Tìm TenGoiHanhLy trong goiHanhLyMap dựa vào MaGoiHanhLyDB
                    // Dòng này sẽ được thực thi SAU KHI loadGoiHanhLyByHanhKhach
                    String selectedTenGoi = "";
                    // Duyệt qua map để tìm TenGoiHanhLy tương ứng với MaGoiHanhLyDB
                    for (Map.Entry<String, String> entry : goiHanhLyMap.entrySet()) {
                        if (maGoiHanhLyDB != null && maGoiHanhLyDB.equals(entry.getValue())) {
                            selectedTenGoi = entry.getKey();
                            break;
                        }
                    }
                    cmbGoiEdit.setSelectedItem(selectedTenGoi); // Chọn TenGoiHanhLy

                    // 5. Load và chọn Trạng thái Hành Lý
                    removeItemListeners(cmbTrangThaiEdit); // Gỡ bỏ listener của cmbTrangThaiEdit (nếu có)
                    cmbTrangThaiEdit.setSelectedItem(trangThaiDB);


                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy chi tiết cho hành lý mã: " + maHanhLy,
                            "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    clearFormInput(); // Xóa tất cả các trường nếu không tìm thấy
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi SQL khi tải chi tiết hành lý:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            clearFormInput();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi không xác định khi tải chi tiết hành lý:\n" + ex.getMessage(),
                    "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
            clearFormInput();
        }
    }
    
    private void searchByMaChuyenBay() {
        String maCB = txtTimKiem.getText().trim();
        if (maCB.isEmpty() || maCB.equals("Nhập mã chuyến bay để tìm kiếm")) {
            loadChuyenBayToTable(); // Hiển thị toàn bộ nếu không nhập hoặc chỉ đang hiển thị placeholder
            return;
        }
        if (maCB.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã chuyến bay cần tìm!");
            return;
        }

        String sql = "SELECT cb.MaChuyenBay, tb.SanBayDi, tb.SanBayDen, cb.GioCatCanh, cb.GioHaCanh, cb.TrangThai " +
                     "FROM CHUYEN_BAY cb " +
                     "JOIN TUYEN_BAY tb ON cb.MaTuyenBay = tb.MaTuyenBay " +
                     "WHERE UPPER(cb.MaChuyenBay) = UPPER(?)";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maCB);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tblDSChuyen.getModel();

            boolean found = false;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm dd/MM/yyyy");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaChuyenBay"),
                    rs.getString("SanBayDi"),
                    rs.getString("SanBayDen"),
                    sdf.format(rs.getTimestamp("GioCatCanh")),
                    sdf.format(rs.getTimestamp("GioHaCanh")),
                    rs.getString("TrangThai")
                });
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến bay nào!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addAutoSuggestToTimKiem() {
        listModelCB = new DefaultListModel<>();
        suggestionListCB = new JList<>(listModelCB);
        suggestionListCB.setFont(txtTimKiem.getFont());
        suggestionListCB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tạo cửa sổ gợi ý (JWindow)
        suggestionWindowCB = new JWindow(SwingUtilities.getWindowAncestor(this));
        JScrollPane scrollPane = new JScrollPane(suggestionListCB);
        suggestionWindowCB.getContentPane().add(scrollPane);

        // Xử lý click chuột vào gợi ý
        suggestionListCB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectSuggestionCB();
                }
            }
        });

        // Xử lý phím Enter và mũi tên
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionWindowCB.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        int index = suggestionListCB.getSelectedIndex();
                        if (index < listModelCB.size() - 1) {
                            suggestionListCB.setSelectedIndex(index + 1);
                            suggestionListCB.ensureIndexIsVisible(index + 1);
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        int index = suggestionListCB.getSelectedIndex();
                        if (index > 0) {
                            suggestionListCB.setSelectedIndex(index - 1);
                            suggestionListCB.ensureIndexIsVisible(index - 1);
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        selectSuggestionCB();
                        e.consume();
                    }
                }
            }
        });

        // Theo dõi khi người dùng gõ
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                showSuggestionsCB();
            }

            public void removeUpdate(DocumentEvent e) {
                showSuggestionsCB();
            }

            public void changedUpdate(DocumentEvent e) {
                showSuggestionsCB();
            }
        });

        // Ẩn gợi ý khi mất focus
        txtTimKiem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> suggestionWindowCB.setVisible(false));
            }
        });
    }

    // Hàm xử lý chọn gợi ý Mã chuyến bay
    private void selectSuggestionCB() {
        String selected = suggestionListCB.getSelectedValue();
        if (selected != null) {
            txtTimKiem.setText(selected);
            suggestionWindowCB.setVisible(false);
            searchByMaChuyenBay(); // Gọi tìm kiếm theo mã chuyến bay
        }
    }

    // Hiển thị gợi ý Mã chuyến bay từ CSDL
    private void showSuggestionsCB() {
        String text = txtTimKiem.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            suggestionWindowCB.setVisible(false);
            return;
        }

        listModelCB.clear();
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT MaChuyenBay FROM CHUYEN_BAY WHERE LOWER(MaChuyenBay) LIKE ?")) {

            ps.setString(1, text + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maCB = rs.getString("MaChuyenBay");
                listModelCB.addElement(maCB);
            }

            if (listModelCB.getSize() > 0) {
                suggestionListCB.setSelectedIndex(0);
                Point location = txtTimKiem.getLocationOnScreen();
                suggestionWindowCB.setBounds(location.x, location.y + txtTimKiem.getHeight(), txtTimKiem.getWidth(), Math.min(listModelCB.getSize() * 20, 150)); // Điều chỉnh kích thước cửa sổ gợi ý
                suggestionWindowCB.setVisible(true);
            } else {
                suggestionWindowCB.setVisible(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void clearFormInput() {
        txtMa.setText(""); // Mã hành lý
        txtMaChuyenView.setText(""); // Mã chuyến bay 
        cmbMaChuyenEdit.setSelectedIndex(-1);
        txtMaVeView.setText(""); // Mã vé máy bay 
        cmbMaVeEdit.setSelectedIndex(-1);
        txtMaHKView.setText(""); // Mã hành khách
        cmbMaHKEdit.setSelectedIndex(-1);
        txtTenHK.setText(""); // Tên hành khách
        txtGoiView.setText("");
        cmbGoiEdit.setSelectedIndex(-1); // Gói hành lý (JComboBox)
        txtTrongLuong.setText(""); // Trọng lượng
        txtTrangThaiView.setText("");
        cmbTrangThaiEdit.setSelectedIndex(-1); // Trạng thái (JComboBox)
        txtViTri.setText(""); // Vị trí hành lý
    }
    
    private String getValueAtSafely(JTable table, int row, int col) {
        if (row >= 0 && row < table.getRowCount() && col >= 0 && col < table.getColumnCount()) {
            Object value = table.getValueAt(row, col);
            return (value != null) ? value.toString() : "";
        }
        return "";
    }
    
    private void applyNormalStyle() {
        Font normalFont = new Font("UTM Centur", Font.PLAIN, 18);
        Color normalColor = Color.BLACK;
        
        txtMaChuyenView.setFont(normalFont);
        txtMaChuyenView.setForeground(normalColor);
        txtMaVeView.setFont(normalFont);
        txtMaVeView.setForeground(normalColor);
        txtMaHKView.setFont(normalFont);
        txtMaHKView.setForeground(normalColor);
        txtTenHK.setFont(normalFont);
        txtTenHK.setForeground(normalColor);
        txtTrongLuong.setFont(normalFont);
        txtTrongLuong.setForeground(normalColor);
        txtGoiView.setFont(normalFont);
        txtGoiView.setForeground(normalColor);
        txtTrangThaiView.setFont(normalFont);
        txtTrangThaiView.setForeground(normalColor);
        txtViTri.setFont(normalFont);
        txtViTri.setForeground(normalColor);
    }
    
    // Phương thức áp dụng placeholder cho tất cả các textfield nhập thông tin
    private void applyPlaceholders() {
        if (currentMode == Mode.ADD || currentMode == Mode.NONE) {
            if (txtMa.getText().trim().isEmpty()) {
                setPlaceholder(txtMa, "Mã hành lý");
            }

            if (txtMaChuyenView.getText().trim().isEmpty()) {
                setPlaceholder(txtMaChuyenView, "Chọn mã chuyến bay");
            }
            
            if (txtMaVeView.getText().trim().isEmpty()) {
                setPlaceholder(txtMaVeView, "Chọn mã vé máy bay");
            }

            if (txtMaHKView.getText().trim().isEmpty()) {
                setPlaceholder(txtMaHKView, "Mã hành khách");
            }

            if (txtTenHK.getText().trim().isEmpty()) {
                setPlaceholder(txtTenHK, "Tên hành khách");
            }

            if (txtTrongLuong.getText().trim().isEmpty()) {
                setPlaceholder(txtTrongLuong, "Nhập trọng lượng");
            }

            if (txtGoiView.getText().trim().isEmpty()) {
                setPlaceholder(txtGoiView, "Chọn tên gói hành lý");
            }
            
            if (txtTrangThaiView.getText().trim().isEmpty()) {
                setPlaceholder(txtTrangThaiView, "Chọn trạng thái");
            }

            if (txtViTri.getText().trim().isEmpty()) {
                setPlaceholder(txtViTri, "Nhập vị trí hành lý");
            }
        }
    }
    
    // Đặt một JTextField thành placeholder
    private void setPlaceholder(JTextField textField, String placeholderText) {
        Color placeholderColor = Color.GRAY;
        Font placeholderFont = new Font("UTM Centur", Font.ITALIC, 18);
        Font normalFont = new Font("UTM Centur", Font.PLAIN, 18);

        if (textField.getText().trim().isEmpty()) {
            textField.setText(placeholderText);
            textField.setForeground(placeholderColor);
            textField.setFont(placeholderFont);
        }

        // Chỉ gán listener 1 lần
        if (!initializedPlaceholders.contains(textField)) {
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (textField.getText().equals(placeholderText)) {
                        textField.setText("");
                        textField.setForeground(Color.BLACK);
                        textField.setFont(normalFont);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (textField.getText().trim().isEmpty()) {
                        textField.setText(placeholderText);
                        textField.setForeground(placeholderColor);
                        textField.setFont(placeholderFont);
                    }
                }
            });
            initializedPlaceholders.add(textField);
        }
    }
    
    private boolean insertHanhLy() {
        String maHanhKhach = (String) cmbMaHKEdit.getSelectedItem();
        String trongLuongStr = getTextFieldValue(txtTrongLuong, "Nhập trọng lượng"); 
        String tenGoiHanhLy = (String) cmbGoiEdit.getSelectedItem(); // Lấy tên gói hành lý từ ComboBox
        String trangThai = (String) cmbTrangThaiEdit.getSelectedItem();
        String viTriHanhLy = getTextFieldValue(txtViTri, "Nhập vị trí hành lý");
        
        System.out.println("Mã hành khách" + maHanhKhach);
        System.out.println("Trọng lượng" + trongLuongStr);
        System.out.println("Gói hành lý" + tenGoiHanhLy);
        System.out.println("Trạng thái" + trangThai);
        System.out.println("Vị trí hành lý" + viTriHanhLy);

        // 1. Kiểm tra ràng buộc NOT NULL của ứng dụng
        if (maHanhKhach == null || maHanhKhach.isEmpty() || tenGoiHanhLy == null || tenGoiHanhLy.isEmpty() ||
            trangThai == null || trangThai.isEmpty() || viTriHanhLy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Mã hành khách, Tên gói hành lý, Trạng thái, Vị trí hành lý).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Lấy MaGoiHanhLy từ Map dựa trên TenGoiHanhLy
        String maGoiHanhLy = goiHanhLyMap.get(tenGoiHanhLy);
        if (maGoiHanhLy == null || maGoiHanhLy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy Mã gói hành lý tương ứng với tên: " + tenGoiHanhLy + ". Vui lòng kiểm tra lại dữ liệu.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Double trongLuong = null;
        // Chỉ parse nếu trọng lượng không rỗng
        if (!trongLuongStr.isEmpty()) {
            try {
                trongLuong = Double.parseDouble(trongLuongStr);
                if (trongLuong <= 0) {
                    JOptionPane.showMessageDialog(this, "Trọng lượng phải là một số dương.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Trọng lượng phải là một số hợp lệ.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // SQL theo cấu trúc bảng HANH_LY 
        String sql = "INSERT INTO HANH_LY (MaHanhLy, MaGoiHanhLy, MaHanhKhach, TrongLuong, TrangThai, ViTriHanhLy) " +
                     "VALUES (FN_TAO_MAHL(), ?, ?, ?, ?, ?)"; // FN_TAO_MAHL() là hàm tạo mã tự động

        try (Connection conn = ConnectionUtils.getMyConnection(); // Giả định ConnectionUtils tồn tại
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGoiHanhLy);    // MaGoiHanhLy
            ps.setString(2, maHanhKhach);    // MaHanhKhach
            if (trongLuong != null) {
                ps.setDouble(3, trongLuong); // TrongLuong
            } else {
                ps.setNull(3, java.sql.Types.DECIMAL); // Sử dụng DECIMAL cho NUMBER(5,2)
            }
            ps.setString(4, trangThai);      // TrangThai
            ps.setString(5, viTriHanhLy);    // ViTriHanhLy

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Thêm hành lý thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Thêm hành lý thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            handleHanhLyError(ex); // Gọi hàm xử lý lỗi đã có sẵn
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi thêm hành lý:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean updateHanhLy() {
        String maHanhLy = txtMa.getText().trim().toUpperCase(); // Lấy mã hành lý từ trường Mã HL
        String maHanhKhach = (String) cmbMaHKEdit.getSelectedItem(); // Lấy từ ComboBox
        String trongLuongStr = getTextFieldValue(txtTrongLuong, "Nhập trọng lượng"); 
        String tenGoiHanhLy = (String) cmbGoiEdit.getSelectedItem(); // Lấy tên gói hành lý
        String trangThai = (String) cmbTrangThaiEdit.getSelectedItem();
        String viTriHanhLy = getTextFieldValue(txtViTri, "Nhập vị trí hành lý");

        // 1. Kiểm tra dữ liệu đầu vào và mã hành lý
        if (maHanhLy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã hành lý không được để trống khi cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (maHanhKhach == null || maHanhKhach.isEmpty() || tenGoiHanhLy == null || tenGoiHanhLy.isEmpty() ||
            trangThai == null || trangThai.isEmpty() || viTriHanhLy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Mã hành khách, Tên gói hành lý, Trạng thái, Vị trí hành lý).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Lấy MaGoiHanhLy từ Map dựa trên TenGoiHanhLy
        String maGoiHanhLy = goiHanhLyMap.get(tenGoiHanhLy);
        if (maGoiHanhLy == null || maGoiHanhLy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy Mã gói hành lý tương ứng với tên: " + tenGoiHanhLy + ". Vui lòng kiểm tra lại dữ liệu.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Double trongLuong = null;
        if (!trongLuongStr.isEmpty()) {
            try {
                trongLuong = Double.parseDouble(trongLuongStr);
                if (trongLuong <= 0) { // Thêm kiểm tra trọng lượng hợp lệ
                    JOptionPane.showMessageDialog(this, "Trọng lượng phải là một số dương.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Trọng lượng phải là một số hợp lệ.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // SQL theo cấu trúc bảng HANH_LY mới
        String sql = "UPDATE HANH_LY SET " +
                     "MaGoiHanhLy = ?, " +
                     "MaHanhKhach = ?, " +
                     "TrongLuong = ?, " +
                     "TrangThai = ?, " +
                     "ViTriHanhLy = ? " +
                     "WHERE MaHanhLy = ?";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGoiHanhLy);    // MaGoiHanhLy
            ps.setString(2, maHanhKhach);    // MaHanhKhach
            if (trongLuong != null) {
                ps.setDouble(3, trongLuong); // TrongLuong
            } else {
                 ps.setNull(3, java.sql.Types.DECIMAL); // Sử dụng DECIMAL cho NUMBER(5,2)
            }
            ps.setString(4, trangThai);      // TrangThai
            ps.setString(5, viTriHanhLy);    // ViTriHanhLy

            ps.setString(6, maHanhLy);       // Tham số WHERE

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật hành lý thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hành lý có mã " + maHanhLy + " để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            handleHanhLyError(ex); // Gọi hàm xử lý lỗi đã có sẵn
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi cập nhật hành lý:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean deleteHanhLy(String maHanhLy) {
        String sql = "DELETE FROM HANH_LY WHERE MaHanhLy = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHanhLy);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa hành lý thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hành lý để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            handleHanhLyError(ex); // Gọi hàm xử lý lỗi
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xóa hành lý: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    // Phương thức handle lỗi HÀNH LÝ
    private void handleHanhLyError(SQLException ex) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage().toLowerCase();

        // 1. Ràng buộc NOT NULL
        if (msg.contains("cannot insert null into") || (msg.contains("cannot update") && msg.contains("null"))) {
            if (msg.contains("mave")) showWarning("Mã vé không được để trống.");
            else if (msg.contains("tengoihanhly")) showWarning("Tên gói hành lý không được để trống.");
            else if (msg.contains("trangthai")) showWarning("Trạng thái không được để trống.");
            else if (msg.contains("vitrihanhly")) showWarning("Vị trí hành lý không được để trống.");
            else showWarning("Một trường bắt buộc đang bị để trống.");
            return;
        }

        // 2. Ràng buộc CHECK
        if (msg.contains("chk_hanhly_trongluong")) {
            showWarning("Trọng lượng hành lý phải lớn hơn hoặc bằng 0.");
            return;
        }
        if (msg.contains("chk_hanhly_trangthai")) {
            showWarning("Trạng thái hành lý không hợp lệ! Chỉ được chọn 'Chưa gửi', 'Đã gửi', 'Đang vận chuyển', hoặc 'Đã nhận'.");
            return;
        }

        // 3. Ràng buộc FOREIGN KEY (MaVe)
        // Mã lỗi Oracle cho ràng buộc tham chiếu (parent key not found) là ORA-02291
        if (code == 2291 && msg.contains("sys_c00")) { // Có thể cụ thể hơn nếu tên constraint rõ ràng
            showWarning("Mã vé không tồn tại trong hệ thống. Vui lòng kiểm tra lại Mã vé.");
            return;
        }

        // 4. Lỗi Primary Key (trùng Mã hành lý) - thường không xảy ra với FN_TAO_MAHL()
        // nhưng nếu có lỗi logic hoặc hàm FN_TAO_MAHL() bị lỗi, nó có thể xảy ra.
        if ((msg.contains("duplicate") && msg.contains("mah_ly")) || (msg.contains("unique constraint") && msg.contains("pk_hanhly"))) {
            showWarning("Mã hành lý đã tồn tại. Vui lòng thử lại.");
            return;
        }

        // 5. Các lỗi khác (show toàn bộ lỗi nếu không bắt được)
        showError(ex);
    }

    // Hàm tiện ích để hiển thị cảnh báo
    private void showWarning(String text) {
        JOptionPane.showMessageDialog(this, text, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
    }

    // Hàm tiện ích để hiển thị lỗi chung
    private void showError(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Lỗi cơ sở dữ liệu:\n" + ex.getMessage(),
            "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showRequiredIndicators(boolean show) {
        // Đặt visibility cho tất cả các JLabel dấu * đỏ
        lblStarMaChuyenBay.setVisible(show);
        lblStarMaVe.setVisible(show);
        lblStarMaHK.setVisible(show);
        lblStarGoiHanhLy.setVisible(show);
        lblStarTrangThai.setVisible(show);
        lblStarViTri.setVisible(show);
    }
        /*
     * Lấy giá trị từ JTextField, nếu là placeholder thì trả về chuỗi rỗng.
     * @param textField JTextField cần lấy giá trị.
     * @param placeholderText Chuỗi placeholder dự kiến của JTextField đó.
     * @return Giá trị của JTextField (hoặc chuỗi rỗng nếu là placeholder).
     */
    private String getTextFieldValue(JTextField textField, String placeholderText) {
        String text = textField.getText().trim();
        if (text.equals(placeholderText)) {
            return ""; // Trả về chuỗi rỗng nếu đó là placeholder
        }
        return text;
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMaVe = new javax.swing.JLabel();
        lblMaHK = new javax.swing.JLabel();
        lblMaChuyen = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        lblGoi = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();
        lblViTri = new javax.swing.JLabel();
        lblTenHK = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtViTri = new javax.swing.JTextField();
        txtTrongLuong = new javax.swing.JTextField();
        txtTenHK = new javax.swing.JTextField();
        lblTrongLuong = new javax.swing.JLabel();
        btnXacNhan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        pnlTrangThaiCards = new javax.swing.JPanel();
        txtTrangThaiView = new javax.swing.JTextField();
        cmbTrangThaiEdit = new javax.swing.JComboBox<>();
        pnlGoiCards = new javax.swing.JPanel();
        txtGoiView = new javax.swing.JTextField();
        cmbGoiEdit = new javax.swing.JComboBox<>();
        pnlMaChuyenCards = new javax.swing.JPanel();
        txtMaChuyenView = new javax.swing.JTextField();
        cmbMaChuyenEdit = new javax.swing.JComboBox<>();
        lblMaHanhLy = new javax.swing.JLabel();
        pnlMaVeCards = new javax.swing.JPanel();
        txtMaVeView = new javax.swing.JTextField();
        cmbMaVeEdit = new javax.swing.JComboBox<>();
        pnlMaHKCards = new javax.swing.JPanel();
        txtMaHKView = new javax.swing.JTextField();
        cmbMaHKEdit = new javax.swing.JComboBox<>();
        lblStarMaChuyenBay = new javax.swing.JLabel();
        lblStarMaVe = new javax.swing.JLabel();
        lblStarMaHK = new javax.swing.JLabel();
        lblStarGoiHanhLy = new javax.swing.JLabel();
        lblStarTrangThai = new javax.swing.JLabel();
        lblStarViTri = new javax.swing.JLabel();
        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        pnlDSHanhLy = new javax.swing.JPanel();
        lblDSHanhLy = new javax.swing.JLabel();
        spDSHanhLy = new javax.swing.JScrollPane();
        tblDSHanhLy = new javax.swing.JTable();
        spDSChuyen = new javax.swing.JScrollPane();
        tblDSChuyen = new javax.swing.JTable();

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thêm hành lý");

        lblMaVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaVe.setText("Mã vé");

        lblMaHK.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaHK.setText("Mã hành khách");

        lblMaChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaChuyen.setText("Mã chuyến bay");

        lblNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgaySinh.setText("(kg)");

        btnSua.setBackground(new java.awt.Color(0, 102, 153));
        btnSua.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(0, 102, 153));
        btnXoa.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/trash.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(0, 102, 153));
        btnThem.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/plus.png"))); // NOI18N
        btnThem.setText(" Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        lblGoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblGoi.setText("Gói hành lý");

        lblTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrangThai.setText("Trạng thái");

        lblViTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblViTri.setText("Vị trí hành lý");

        lblTenHK.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTenHK.setText("Tên hành khách");

        txtMa.setEditable(false);
        txtMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMa.setAutoscrolls(false);
        txtMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaActionPerformed(evt);
            }
        });

        txtViTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtViTri.setAutoscrolls(false);

        txtTrongLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTrongLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrongLuongActionPerformed(evt);
            }
        });

        txtTenHK.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        lblTrongLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrongLuong.setText("Trọng lượng");

        btnXacNhan.setBackground(new java.awt.Color(0, 102, 153));
        btnXacNhan.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnXacNhan.setForeground(new java.awt.Color(255, 255, 255));
        btnXacNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/checkmark.png"))); // NOI18N
        btnXacNhan.setText("Xác nhận");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        btnHuy.setBackground(new java.awt.Color(0, 102, 153));
        btnHuy.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/remove.png"))); // NOI18N
        btnHuy.setText("Hủy");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        pnlTrangThaiCards.setLayout(new java.awt.CardLayout());

        txtTrangThaiView.setEditable(false);
        txtTrangThaiView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTrangThaiView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrangThaiViewActionPerformed(evt);
            }
        });
        pnlTrangThaiCards.add(txtTrangThaiView, "VIEW");

        cmbTrangThaiEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbTrangThaiEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chưa gửi", "Đã gửi", "Đang vận chuyển", "Đã nhận" }));
        pnlTrangThaiCards.add(cmbTrangThaiEdit, "EDIT");

        pnlGoiCards.setLayout(new java.awt.CardLayout());

        txtGoiView.setEditable(false);
        txtGoiView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtGoiView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGoiViewActionPerformed(evt);
            }
        });
        pnlGoiCards.add(txtGoiView, "VIEW");

        cmbGoiEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbGoiEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGoiEditActionPerformed(evt);
            }
        });
        pnlGoiCards.add(cmbGoiEdit, "EDIT");

        pnlMaChuyenCards.setLayout(new java.awt.CardLayout());

        txtMaChuyenView.setEditable(false);
        txtMaChuyenView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaChuyenView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaChuyenViewActionPerformed(evt);
            }
        });
        pnlMaChuyenCards.add(txtMaChuyenView, "VIEW");

        cmbMaChuyenEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbMaChuyenEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMaChuyenEditActionPerformed(evt);
            }
        });
        pnlMaChuyenCards.add(cmbMaChuyenEdit, "EDIT");

        lblMaHanhLy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaHanhLy.setText("Mã hành lý");

        pnlMaVeCards.setLayout(new java.awt.CardLayout());

        txtMaVeView.setEditable(false);
        txtMaVeView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaVeView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaVeViewActionPerformed(evt);
            }
        });
        pnlMaVeCards.add(txtMaVeView, "VIEW");

        cmbMaVeEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbMaVeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMaVeEditActionPerformed(evt);
            }
        });
        pnlMaVeCards.add(cmbMaVeEdit, "EDIT");

        pnlMaHKCards.setLayout(new java.awt.CardLayout());

        txtMaHKView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaHKView.setAutoscrolls(false);
        txtMaHKView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHKViewActionPerformed(evt);
            }
        });
        pnlMaHKCards.add(txtMaHKView, "card2");

        cmbMaHKEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbMaHKEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ký gửi", "Xách tay"}));
        cmbMaHKEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMaHKEditActionPerformed(evt);
            }
        });
        pnlMaHKCards.add(cmbMaHKEdit, "EDIT");

        lblStarMaChuyenBay.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarMaChuyenBay.setForeground(new java.awt.Color(255, 0, 0));
        lblStarMaChuyenBay.setText("*");
        lblStarMaChuyenBay.setAutoscrolls(true);

        lblStarMaVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarMaVe.setForeground(new java.awt.Color(255, 0, 0));
        lblStarMaVe.setText("*");
        lblStarMaVe.setAutoscrolls(true);

        lblStarMaHK.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarMaHK.setForeground(new java.awt.Color(255, 0, 0));
        lblStarMaHK.setText("*");
        lblStarMaHK.setAutoscrolls(true);

        lblStarGoiHanhLy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarGoiHanhLy.setForeground(new java.awt.Color(255, 0, 0));
        lblStarGoiHanhLy.setText("*");
        lblStarGoiHanhLy.setAutoscrolls(true);

        lblStarTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarTrangThai.setForeground(new java.awt.Color(255, 0, 0));
        lblStarTrangThai.setText("*");
        lblStarTrangThai.setAutoscrolls(true);

        lblStarViTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarViTri.setForeground(new java.awt.Color(255, 0, 0));
        lblStarViTri.setText("*");
        lblStarViTri.setAutoscrolls(true);

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(243, 243, 243)
                .addComponent(lblThaoTac))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblMaHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114)
                .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblMaChuyen)
                .addGap(12, 12, 12)
                .addComponent(lblStarMaChuyenBay, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(pnlMaChuyenCards, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblMaVe)
                .addGap(6, 6, 6)
                .addComponent(lblStarMaVe)
                .addGap(150, 150, 150)
                .addComponent(pnlMaVeCards, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblMaHK, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblStarMaHK)
                .addGap(66, 66, 66)
                .addComponent(pnlMaHKCards, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTenHK)
                .addGap(81, 81, 81)
                .addComponent(txtTenHK, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblGoi)
                .addGap(6, 6, 6)
                .addComponent(lblStarGoiHanhLy)
                .addGap(102, 102, 102)
                .addComponent(pnlGoiCards, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTrongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106)
                .addComponent(txtTrongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblNgaySinh))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTrangThai)
                .addGap(6, 6, 6)
                .addComponent(lblStarTrangThai)
                .addGap(111, 111, 111)
                .addComponent(pnlTrangThaiCards, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblViTri)
                .addGap(6, 6, 6)
                .addComponent(lblStarViTri)
                .addGap(88, 88, 88)
                .addComponent(txtViTri, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btnThem)
                .addGap(81, 81, 81)
                .addComponent(btnXoa)
                .addGap(72, 72, 72)
                .addComponent(btnSua))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(btnXacNhan)
                .addGap(80, 80, 80)
                .addComponent(btnHuy))
        );
        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblThaoTac)
                .addGap(8, 8, 8)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblMaHanhLy))
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMaChuyenCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaChuyen)
                            .addComponent(lblStarMaChuyenBay))))
                .addGap(22, 22, 22)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMaVeCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaVe)
                            .addComponent(lblStarMaVe))))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMaHKCards, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaHK)
                            .addComponent(lblStarMaHK))))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblTenHK))
                    .addComponent(txtTenHK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlGoiCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGoi)
                            .addComponent(lblStarGoiHanhLy))))
                .addGap(24, 24, 24)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTrongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTrongLuong)
                            .addComponent(lblNgaySinh))))
                .addGap(15, 15, 15)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTrangThaiCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTrangThai)
                            .addComponent(lblStarTrangThai))))
                .addGap(25, 25, 25)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtViTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblViTri)
                            .addComponent(lblStarViTri))))
                .addGap(82, 82, 82)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem)
                    .addComponent(btnXoa)
                    .addComponent(btnSua))
                .addGap(51, 51, 51)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXacNhan)
                    .addComponent(btnHuy)))
        );

        pnlDanhSach.setBackground(java.awt.SystemColor.control);
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(632, 824));

        lblDanhSach.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblDanhSach.setText("Danh sách chuyến bay");

        txtTimKiem.setFont(new java.awt.Font("UTM Centur", 0, 20)); // NOI18N
        txtTimKiem.setAutoscrolls(false);
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyPressed(evt);
            }
        });

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/search.png"))); // NOI18N
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        pnlDSHanhLy.setBackground(javax.swing.UIManager.getDefaults().getColor("Component.focusColor"));

        lblDSHanhLy.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        lblDSHanhLy.setText("Danh sách hành lý của chuyến bay");

        tblDSHanhLy.setAutoCreateRowSorter(true);
        tblDSHanhLy.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSHanhLy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã hành lý", "Trọng lượng", "Gói hành lý", "Vị trí hành lý"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSHanhLy.setRowHeight(30);
        spDSHanhLy.setViewportView(tblDSHanhLy);
        if (tblDSHanhLy.getColumnModel().getColumnCount() > 0) {
            tblDSHanhLy.getColumnModel().getColumn(0).setMinWidth(80);
            tblDSHanhLy.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblDSHanhLy.getColumnModel().getColumn(0).setMaxWidth(80);
            tblDSHanhLy.getColumnModel().getColumn(1).setMinWidth(80);
            tblDSHanhLy.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblDSHanhLy.getColumnModel().getColumn(1).setMaxWidth(80);
            tblDSHanhLy.getColumnModel().getColumn(2).setMinWidth(120);
            tblDSHanhLy.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblDSHanhLy.getColumnModel().getColumn(2).setMaxWidth(120);
        }

        javax.swing.GroupLayout pnlDSHanhLyLayout = new javax.swing.GroupLayout(pnlDSHanhLy);
        pnlDSHanhLy.setLayout(pnlDSHanhLyLayout);
        pnlDSHanhLyLayout.setHorizontalGroup(
            pnlDSHanhLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSHanhLyLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(lblDSHanhLy)
                .addContainerGap(122, Short.MAX_VALUE))
            .addGroup(pnlDSHanhLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(spDSHanhLy, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE))
        );
        pnlDSHanhLyLayout.setVerticalGroup(
            pnlDSHanhLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSHanhLyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDSHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(327, Short.MAX_VALUE))
            .addGroup(pnlDSHanhLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDSHanhLyLayout.createSequentialGroup()
                    .addGap(0, 46, Short.MAX_VALUE)
                    .addComponent(spDSHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        tblDSChuyen.setAutoCreateRowSorter(true);
        tblDSChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSChuyen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã CB", "Điểm đi", "Điểm đến", "Giờ cất cánh", "Giờ hạ cánh"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSChuyen.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDSChuyen.setRowHeight(30);
        spDSChuyen.setViewportView(tblDSChuyen);
        if (tblDSChuyen.getColumnModel().getColumnCount() > 0) {
            tblDSChuyen.getColumnModel().getColumn(0).setMinWidth(70);
            tblDSChuyen.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblDSChuyen.getColumnModel().getColumn(0).setMaxWidth(70);
            tblDSChuyen.getColumnModel().getColumn(1).setMinWidth(80);
            tblDSChuyen.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblDSChuyen.getColumnModel().getColumn(1).setMaxWidth(80);
            tblDSChuyen.getColumnModel().getColumn(2).setMinWidth(80);
            tblDSChuyen.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblDSChuyen.getColumnModel().getColumn(2).setMaxWidth(80);
        }

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(0, 63, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem)
                .addGap(76, 76, 76))
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(lblDanhSach))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(pnlDSHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlDanhSachLayout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(spDSChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(14, Short.MAX_VALUE)))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblDanhSach)
                .addGap(18, 18, 18)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addGap(289, 289, 289)
                .addComponent(pnlDSHanhLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlDanhSachLayout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addComponent(spDSChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(445, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(pnlDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = tblDSHanhLy.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hành lý cần sửa.");
            return;
        }
        setMode(Mode.EDIT);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblDSHanhLy.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hành lý cần xóa.");
            return;
        }
        setMode(Mode.DELETE);
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        setMode(Mode.ADD);
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
            switch (currentMode) {
            case ADD:
                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn thêm hành lý này?",
                        "Xác nhận thêm", JOptionPane.YES_NO_OPTION) 
                    == JOptionPane.YES_OPTION) {
                                        
                    if (insertHanhLy()) { // Hàm insertHanhLy() sẽ trả về boolean
                        String maChuyenBayHienTai = (String) cmbMaChuyenEdit.getSelectedItem(); // Lấy mã chuyến bay hiện tại để load lại bảng
                        if (maChuyenBayHienTai != null && !maChuyenBayHienTai.isEmpty()) {
                            loadHanhLyToTable(maChuyenBayHienTai);
                        }
                        setMode(Mode.NONE); // Chỉ trả về chế độ NONE nếu thêm thành công
                        clearFormInput(); // Xóa form sau khi thêm thành công
                    }   
                }
                // nếu NO: không làm gì, vẫn ở chế độ ADD
                break;

            case DELETE:            
                // Lấy mã hành lý từ hàng được chọn
                int selectedRow = tblDSHanhLy.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một hành lý để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String maDel = getValueAtSafely(tblDSHanhLy, selectedRow, 0); 

                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn xóa hành lý mã: " + maDel + "?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION)
                        == JOptionPane.YES_OPTION) {

                    if (deleteHanhLy(maDel)) { // Hàm deleteHanhLy() sẽ trả về boolean
                        String maChuyenBayHienTai = (String) cmbMaChuyenEdit.getSelectedItem(); // Lấy mã chuyến bay hiện tại để load lại bảng
                        if (maChuyenBayHienTai != null && !maChuyenBayHienTai.isEmpty()) {
                            loadHanhLyToTable(maChuyenBayHienTai);
                        }
                        setMode(Mode.NONE);
                        clearFormInput();
                    }
                }
                break;

            case EDIT:
                if (JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn lưu thay đổi?",
                    "Xác nhận sửa", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {

                    if (updateHanhLy()) { // Hàm updateHanhLy() sẽ trả về boolean
                        String maChuyenBayHienTai = (String) cmbMaChuyenEdit.getSelectedItem(); // Lấy mã chuyến bay hiện tại để load lại bảng
                        if (maChuyenBayHienTai != null && !maChuyenBayHienTai.isEmpty()) {
                            loadHanhLyToTable(maChuyenBayHienTai);
                        }
                        setMode(Mode.NONE);
                        clearFormInput();
                    }
                }
                break;
            default:
                // không làm gì
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        setMode(Mode.NONE);
    }//GEN-LAST:event_btnHuyActionPerformed

    private void txtTrangThaiViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrangThaiViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrangThaiViewActionPerformed

    private void txtGoiViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGoiViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGoiViewActionPerformed

    private void cmbGoiEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGoiEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGoiEditActionPerformed

    private void txtMaHKViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHKViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHKViewActionPerformed

    private void txtTrongLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrongLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrongLuongActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        searchByMaChuyenBay();
        setMode(Mode.NONE);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtMaChuyenViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaChuyenViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaChuyenViewActionPerformed

    private void cmbMaChuyenEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaChuyenEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMaChuyenEditActionPerformed

    private void txtMaVeViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaVeViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaVeViewActionPerformed

    private void cmbMaVeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaVeEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMaVeEditActionPerformed

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchByMaChuyenBay();
        }
        setMode(Mode.NONE);
    }//GEN-LAST:event_txtTimKiemKeyPressed

    private void cmbMaHKEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaHKEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMaHKEditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbGoiEdit;
    private javax.swing.JComboBox<String> cmbMaChuyenEdit;
    private javax.swing.JComboBox<String> cmbMaHKEdit;
    private javax.swing.JComboBox<String> cmbMaVeEdit;
    private javax.swing.JComboBox<String> cmbTrangThaiEdit;
    private javax.swing.JLabel lblDSHanhLy;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblGoi;
    private javax.swing.JLabel lblMaChuyen;
    private javax.swing.JLabel lblMaHK;
    private javax.swing.JLabel lblMaHanhLy;
    private javax.swing.JLabel lblMaVe;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblStarGoiHanhLy;
    private javax.swing.JLabel lblStarMaChuyenBay;
    private javax.swing.JLabel lblStarMaHK;
    private javax.swing.JLabel lblStarMaVe;
    private javax.swing.JLabel lblStarTrangThai;
    private javax.swing.JLabel lblStarViTri;
    private javax.swing.JLabel lblTenHK;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JLabel lblTrongLuong;
    private javax.swing.JLabel lblViTri;
    private javax.swing.JPanel pnlDSHanhLy;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlGoiCards;
    private javax.swing.JPanel pnlMaChuyenCards;
    private javax.swing.JPanel pnlMaHKCards;
    private javax.swing.JPanel pnlMaVeCards;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JPanel pnlTrangThaiCards;
    private javax.swing.JScrollPane spDSChuyen;
    private javax.swing.JScrollPane spDSHanhLy;
    private javax.swing.JTable tblDSChuyen;
    private javax.swing.JTable tblDSHanhLy;
    private javax.swing.JTextField txtGoiView;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtMaChuyenView;
    private javax.swing.JTextField txtMaHKView;
    private javax.swing.JTextField txtMaVeView;
    private javax.swing.JTextField txtTenHK;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTrangThaiView;
    private javax.swing.JTextField txtTrongLuong;
    private javax.swing.JTextField txtViTri;
    // End of variables declaration//GEN-END:variables
}
