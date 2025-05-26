package View.Admin;

import ConnectDB.ConnectionUtils;       // import lớp ConnectionUtils
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import java.sql.Types;
import javax.swing.JComboBox;

/**
 *
 * @author Huy Nguyen
 */
public class QLMayBayForm extends javax.swing.JPanel {
    CardLayout cardTrangThai, cardViTri;
    private enum Mode { NONE, VIEW, ADD, DELETE, EDIT, BAOTRI};
    private Mode currentMode = Mode.NONE;
    private final Set<JTextField> initializedPlaceholders = new HashSet<>();
    private DefaultListModel<String> listModelMB;
    private JList<String> suggestionListMB;
    private JWindow suggestionWindowMB;
    /**
     * Creates new form QLChuyenBayForm
     */
    public QLMayBayForm() {
        // Khởi tạo các components
        initComponents();
        // Khởi tạo các CardLayout
        initCardLayout();
        // Nạp dữ liệu máy bay lên bảng tblDSMayBay
        loadMayBayToTable();
        // Nạp các tên sân bay lên cmbViTriEdit
        loadTenSanBayToComboBox();
        // Đăng ký listener cho việc chọn dòng máy bay
        initSelectionListener();
        // Set chế độ NONE ban đầu
        setMode(Mode.NONE);
        // Set placeholder cho thanh tìm kiếm
        setPlaceholder(txtTimKiem, "Nhập mã hoặc loại máy bay để tìm kiếm");
        // Thêm gợi ý tự động cho tính năng tìm kiếm
        addAutoSuggestToTimKiem();
    }
    
    // Khởi tạo các CardLayout
    private void initCardLayout() {
        cardTrangThai = (CardLayout) pnlTrangThaiCards.getLayout();
        cardViTri = (CardLayout) pnlViTriCards.getLayout();
    }
    
    // Hàm setMode
    private void setMode(Mode mode) {
        switch (mode) {
            case VIEW:
                // Chỉnh mode sang VIEW
                currentMode = Mode.VIEW;
                applyNormalStyle();
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thông tin máy bay");
                // Tắt panel Lịch bảo trì
                pnlLichBaoTri.setVisible(false);
                // Tắt btnXacNhan và btnHuy
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                // Không cho phép chỉnh sửa
                setThaoTacEditable(false);
                // Bật các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(true);
                btnSua.setEnabled(true);
                btnXoa.setEnabled(true);
                btnBaoTri.setEnabled(true);
                // Ẩn dấu * 
                showRequiredIndicators(false); 
                break;
            case BAOTRI:
                // Chỉnh mode sang BAOTRI
                currentMode = Mode.BAOTRI;
                // Bật panel Lịch bảo trì
                pnlLichBaoTri.setVisible(true);
                // Bật btnXacNhan và đổi text
                btnXacNhan.setVisible(true);
                btnXacNhan.setText("Xác nhận bảo trì");
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa, Bảo trì
                btnThem.setEnabled(false);
                btnSua.setEnabled(false);
                btnXoa.setEnabled(false);
                btnBaoTri.setEnabled(false);
                break;
            case ADD:
                // Chỉnh mode sang ADD
                currentMode = Mode.ADD;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thêm máy bay");
                // Tắt panel Lịch bảo trì
                pnlLichBaoTri.setVisible(false);
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
                btnSua.setEnabled(false);
                btnXoa.setEnabled(false);
                btnBaoTri.setEnabled(false);
                // Hiện dấu * 
                showRequiredIndicators(true); 
                break;
            case EDIT:
                // Chỉnh mode sang EDIT
                currentMode = Mode.EDIT;
                applyNormalStyle();
                // Đổi text lblThaoTac
                lblThaoTac.setText("Sửa máy bay");
                // Tắt panel Lịch bảo trì
                pnlLichBaoTri.setVisible(false);
                // Bật btnXacNhan và đổi text
                btnXacNhan.setText("Xác nhận sửa");
                btnXacNhan.setVisible(true);
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Cho phép chỉnh sửa
                setThaoTacEditable(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(false);
                btnSua.setEnabled(false);
                btnXoa.setEnabled(false);
                btnBaoTri.setEnabled(false);
                // Hiện dấu * 
                showRequiredIndicators(true); 
                break;
            case DELETE:
                // Chỉnh mode sang DELETE
                currentMode = Mode.DELETE;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Xóa máy bay");
                // Tắt panel Lịch bảo trì
                pnlLichBaoTri.setVisible(false);
                // Bật btnXacNhan và đổi text
                btnXacNhan.setVisible(true);
                btnXacNhan.setText("Xác nhận xóa");
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
                btnThem.setEnabled(false);
                btnSua.setEnabled(false);
                btnXoa.setEnabled(false);
                btnBaoTri.setEnabled(false);
                // Ẩn dấu * 
                showRequiredIndicators(false); 
                break;
            default:
                // Chỉnh mode sang NONE
                currentMode = Mode.NONE;
                // Tắt panel Lịch bảo trì
                pnlLichBaoTri.setVisible(false);
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thông tin máy bay");
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
                btnSua.setEnabled(true);
                btnXoa.setEnabled(true);
                btnBaoTri.setEnabled(true);
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
        cardTrangThai.show(pnlTrangThaiCards, statusCard);
        cardViTri.show(pnlViTriCards, statusCard);
   
        // Xử lý Mã máy bay không được thêm, sửa
        if (editable == true) {
            txtMa.setEnabled(false);
        }
        
        // JTextField
        txtMa.setEditable(false);
        txtLoai.setEditable(editable);
        txtSoGhe.setEditable(editable);
        txtHang.setEditable(editable);
        txtTrangThaiView.setEditable(editable);
        txtViTriView.setEditable(editable);

        // JComboBox (nếu có)
        cmbTrangThaiEdit.setEnabled(editable);
        cmbViTriEdit.setEnabled(editable);
    }
    
    public void loadMayBayToTable() {
        DefaultTableModel model = (DefaultTableModel) tblDSMayBay.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng

        String sql = """
            SELECT
                mb.MaMayBay,
                mb.LoaiMayBay,
                mb.TrangThai,
                sb.TenSanBay AS TenViTriHienTai -- Lấy TenSanBay và đặt bí danh
            FROM MAY_BAY mb
            LEFT JOIN SAN_BAY sb ON mb.ViTriHienTai = sb.MaSanBay -- JOIN dựa trên MaSanBay
            ORDER BY mb.MaMayBay
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaMayBay"),
                    rs.getString("LoaiMayBay"),
                    rs.getString("TrangThai"),
                    rs.getString("TenViTriHienTai") // Lấy giá trị từ bí danh TenViTriHienTai
                });
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu máy bay:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadTenSanBayToComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        // Thêm một item rỗng để người dùng có thể chọn hoặc reset
        model.addElement(""); 

        List<String> tenSanBays = new ArrayList<>();
        String sql = "SELECT TenSanBay FROM SAN_BAY ORDER BY TenSanBay"; // Sắp xếp theo tên sân bay

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tenSanBays.add(rs.getString("TenSanBay"));
            }

            // Thêm các tên sân bay vào DefaultComboBoxModel
            for (String tenSB : tenSanBays) {
                model.addElement(tenSB);
            }

            // Set model cho JComboBox
            cmbViTriEdit.setModel(model);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải danh sách sân bay:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initSelectionListener() {
        tblDSMayBay.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Đảm bảo sự kiện chỉ xử lý khi lựa chọn đã ổn định (không phải trong quá trình kéo chuột)
                if (e.getValueIsAdjusting()) {
                    return;
                }

                int row = tblDSMayBay.getSelectedRow();
                if (row < 0) {
                    // Không có dòng nào được chọn, xóa trắng các trường nhập liệu 
                    clearFormInput(); 
                    setMode(Mode.NONE); 
                    return;
                }

                // Đặt chế độ VIEW khi một dòng được chọn
                setMode(Mode.VIEW);

                // Lấy Mã máy bay từ cột đầu tiên của bảng (cột 0)
                String maMayBay = tblDSMayBay.getValueAt(row, 0).toString();

                // Hiển thị các thông tin cơ bản đã có sẵn trên bảng vào các trường view
                txtMa.setText(maMayBay);
                txtLoai.setText(tblDSMayBay.getValueAt(row, 1).toString()); // Loại máy bay
//                txtTrangThaiView.setText(tblDSMayBay.getValueAt(row, 2).toString()); // Trạng thái
//                txtViTriView.setText(tblDSMayBay.getValueAt(row, 3).toString()); // Vị trí hiện tại

                // Nạp thêm chi tiết từ database (nếu có các trường khác không hiển thị trên bảng)
                loadMayBayDetails(maMayBay);
                loadBaoTriToTable(maMayBay);
            }
        });
    }
    
    private void loadMayBayDetails(String maMayBay) {
        String sql = """
            SELECT
                mb.MaMayBay,
                mb.LoaiMayBay,
                mb.SoGhe,
                mb.HangSanXuat,
                mb.TrangThai,
                mb.ViTriHienTai AS MaSanBay, -- Vẫn giữ MaSanBay để dùng khi UPDATE/INSERT
                sb.TenSanBay AS TenViTriHienTai
            FROM MAY_BAY mb
            LEFT JOIN SAN_BAY sb ON mb.ViTriHienTai = sb.MaSanBay
            WHERE mb.MaMayBay = ?
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMayBay); // Thiết lập tham số cho MaMayBay

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    
                    String maMayBayStr = rs.getString("MaMayBay");
                    String loaiStr = rs.getString("LoaiMayBay");
                    String soGheStr = String.valueOf(rs.getInt("SoGhe"));
                    String hangStr = rs.getString("HangSanXuat");
                    String trangThaiStr = rs.getString("TrangThai");
                    String tenViTriHienTai = rs.getString("TenViTriHienTai");
                            
                    // Điền dữ liệu vào các JTextField
                    txtMa.setText(maMayBayStr);
                    txtLoai.setText(loaiStr);
                    
                    txtSoGhe.setText(soGheStr);
                    txtHang.setText(hangStr);

                    // Điền dữ liệu vào txtTrangThaiView và txtViTriView
                    txtTrangThaiView.setText(trangThaiStr);
                    txtViTriView.setText(tenViTriHienTai); // Lấy TenViTriHienTai

                    // Điền dữ liệu vào cmbTrangThaiEdit và cmbViTriEdit (cho việc chỉnh sửa)
                    cmbTrangThaiEdit.setSelectedItem(trangThaiStr);
                    // cmbViTriEdit sẽ được set tên sân bay
                    cmbViTriEdit.setSelectedItem(tenViTriHienTai);

                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết máy bay có mã: " + maMayBay, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    clearFormInput();
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải chi tiết máy bay:\n" + ex.getMessage(),
                "Lỗi cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadBaoTriToTable(String maMayBay) {
        DefaultTableModel model = (DefaultTableModel) tblDSBaoTri.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng

        // 1. Kiểm tra mã máy bay đầu vào
        if (maMayBay == null || maMayBay.trim().isEmpty()) {
            // Nếu mã máy bay rỗng, có thể không hiển thị gì hoặc thông báo
            lblDSBaoTri.setText("Danh sách lịch bảo trì"); // Đặt lại tiêu đề mặc định
            return;
        }

        // Câu lệnh SQL để lấy thông tin từ bảng LICH_BAO_TRI
        // và lọc theo MaMayBay
        String sql = """
            SELECT MaBaoTri, NgayBaoTri, ChiPhi, GhiChu
            FROM LICH_BAO_TRI
            WHERE MaMayBay = ?
            ORDER BY NgayBaoTri DESC
        """;

        // Định dạng cho Ngày bảo trì (DATE) và Chi phí (NUMBER)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,##0.00"); // Định dạng tiền tệ với 2 số thập phân

        try (Connection conn = ConnectionUtils.getMyConnection(); // Sử dụng lớp ConnectionUtils của bạn
             PreparedStatement ps = conn.prepareStatement(sql)) { // Sử dụng PreparedStatement

            ps.setString(1, maMayBay); // Đặt tham số cho MaMayBay

            // Cập nhật tiêu đề của panel/tab để hiển thị máy bay hiện tại
             lblDSBaoTri.setText("Danh sách lịch bảo trì của máy bay " + maMayBay);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String maBaoTri = rs.getString("MaBaoTri");
                    java.sql.Date ngayBaoTri = rs.getDate("NgayBaoTri"); // Sử dụng getDate cho kiểu DATE
                    double chiPhi = rs.getDouble("ChiPhi");

                    String ngayBaoTriStr = (ngayBaoTri != null) ? sdf.format(ngayBaoTri) : "";
                    String chiPhiStr = df.format(chiPhi);
                    String ghiChuStr = rs.getString("GhiChu");

                    // Thêm dòng mới vào table model
                    model.addRow(new Object[]{maBaoTri, ngayBaoTriStr, chiPhiStr, ghiChuStr});
                    found = true;
                }

                if (!found) {
                    // Không hiển thị gì
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Lỗi SQL khi tải danh sách lịch bảo trì:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Bắt các lỗi khác như ClassNotFoundException từ ConnectionUtils
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Lỗi không xác định khi tải danh sách lịch bảo trì:\n" + ex.getMessage(),
                    "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
        }

        // Sau khi tải xong, bỏ chọn các dòng hiện có trong bảng
        tblDSBaoTri.clearSelection();
    }

    private void applyNormalStyle() {
        Font normalFont = new Font("UTM Centur", Font.PLAIN, 18);
        Color normalColor = Color.BLACK;
        
        txtMa.setFont(normalFont);
        txtMa.setForeground(normalColor);
        txtLoai.setFont(normalFont);
        txtLoai.setForeground(normalColor);
        txtSoGhe.setFont(normalFont);
        txtSoGhe.setForeground(normalColor);
        txtHang.setFont(normalFont);
        txtHang.setForeground(normalColor);
        txtTrangThaiView.setFont(normalFont);
        txtTrangThaiView.setForeground(normalColor);
        txtViTriView.setFont(normalFont);
        txtViTriView.setForeground(normalColor);
    }
    
    private void clearFormInput() {
        txtMa.setText(""); // Mã máy bay
        txtLoai.setText(""); // Loại máy bay
        txtSoGhe.setText(""); // Số ghế
        txtHang.setText(""); // Hãng sản xuất
        txtTrangThaiView.setText(""); // Trạng thái
        cmbTrangThaiEdit.setSelectedIndex(-1); // Trạng thái (JComboBox)
        txtViTriView.setText(""); // Vị trí hiện tại 
        cmbViTriEdit.setSelectedIndex(-1); // Vị trí hiện tại (JComboBox)
    }
    
    private void clearLichBaoTriInput() {
        dcNgayBaoTri.setDate(null);
        txtChiPhi.setText("");
        txtGhiChu.setText("");
    }
    
        // Phương thức áp dụng placeholder cho tất cả các textfield nhập thông tin
    private void applyPlaceholders() {
        if (currentMode == Mode.ADD || currentMode == Mode.NONE) {
            if (txtMa.getText().trim().isEmpty()) {
                setPlaceholder(txtMa, "Mã máy bay");
            }

            if (txtLoai.getText().trim().isEmpty()) {
                setPlaceholder(txtLoai, "Nhập loại máy bay");
            }
            
            if (txtSoGhe.getText().trim().isEmpty()) {
                setPlaceholder(txtSoGhe, "Nhập số ghế");
            }

            if (txtHang.getText().trim().isEmpty()) {
                setPlaceholder(txtHang, "Nhập hãng sản xuất");
            }

            if (txtTrangThaiView.getText().trim().isEmpty()) {
                setPlaceholder(txtTrangThaiView, "Chọn trạng thái");
            }
            
            if (txtViTriView.getText().trim().isEmpty()) {
                setPlaceholder(txtViTriView, "Chọn vị trí hiện tại");
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
    
    private void showRequiredIndicators(boolean show) {
        // Đặt visibility cho tất cả các JLabel dấu * đỏ
        lblStarLoai.setVisible(show);
        lblStarSoGhe.setVisible(show);
        lblStarHang.setVisible(show);
    }
    
    private String getTextFieldValue(JTextField textField, String placeholderText) {
        String text = textField.getText().trim();
        if (text.equals(placeholderText)) {
            return ""; // Trả về chuỗi rỗng nếu đó là placeholder
        }
        return text;
    }
    
    private boolean insertMayBay() {
        // Lấy dữ liệu từ các JTextField và JComboBox
        // txtMa không cần lấy vì Mã máy bay được tạo tự động bởi FN_TAO_MAMB()
        String loaiMayBay = getTextFieldValue(txtLoai, "Nhập loại máy bay"); 
        String soGheStr = getTextFieldValue(txtSoGhe, "Nhập số ghế"); 
        String hangSanXuat = getTextFieldValue(txtHang, "Nhập hãng sản xuất"); 
        
        String trangThai = (String) cmbTrangThaiEdit.getSelectedItem();
        String tenViTriHienTai = (String) cmbViTriEdit.getSelectedItem(); // Là tên sân bay
        

        // 1. Kiểm tra ràng buộc NOT NULL của ứng dụng
        if (loaiMayBay.isEmpty() || soGheStr.isEmpty() || hangSanXuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Loại máy bay, Số ghế, Hãng sản xuất).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 2. Chuyển đổi và kiểm tra Số ghế
        int soGhe;
        try {
            soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) { // Số ghế phải lớn hơn 0
                JOptionPane.showMessageDialog(this, "Số ghế phải là một số nguyên dương.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số ghế phải là một số nguyên hợp lệ.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 3. Lấy MaSanBay từ TenSanBay (chỉ khi tenViTriHienTai được cung cấp)
        String maSanBay = null;
        if (tenViTriHienTai != null) {
            maSanBay = getMaSanBayFromTenSanBay(tenViTriHienTai);
            if (maSanBay == null) { // Lỗi này chỉ xảy ra nếu người dùng CHỌN một sân bay, nhưng tên đó không hợp lệ
                JOptionPane.showMessageDialog(null, "Tên sân bay '" + tenViTriHienTai + "' không hợp lệ hoặc không tìm thấy. Vui lòng chọn lại hoặc bỏ trống.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        // Nếu tenViTriHienTai là null, maSanBay sẽ vẫn là null, và đó là điều chấp nhận được
        String sql = "INSERT INTO MAY_BAY (MaMayBay, LoaiMayBay, SoGhe, HangSanXuat, TrangThai, ViTriHienTai) " +
                     "VALUES (FN_TAO_MAMB(), ?, ?, ?, ?, ?)"; // Sử dụng hàm tạo mã tự động

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loaiMayBay);
            ps.setInt(2, soGhe);
            ps.setString(3, hangSanXuat);
            ps.setString(4, trangThai);
            ps.setString(5, maSanBay); // Lưu MaSanBay vào cột ViTriHienTai

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Thêm máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Thêm máy bay thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            handleMayBayError(ex, "insert"); // Gọi hàm xử lý lỗi riêng cho máy bay
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi thêm máy bay:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean updateMayBay() {
        // Lấy dữ liệu từ các JTextField và JComboBox
        String maMayBay = txtMa.getText().trim(); // Mã máy bay không được thay đổi
        
        String loaiMayBay = getTextFieldValue(txtLoai, "Nhập loại máy bay"); 
        String soGheStr = getTextFieldValue(txtSoGhe, "Nhập số ghế"); 
        String hangSanXuat = getTextFieldValue(txtHang, "Nhập hãng sản xuất"); 
        
        String trangThai = (String) cmbTrangThaiEdit.getSelectedItem();
        String tenViTriHienTai = (String) cmbViTriEdit.getSelectedItem(); // Là tên sân bay

        // 1. Kiểm tra dữ liệu đầu vào
        if (loaiMayBay.isEmpty() || soGheStr.isEmpty() || hangSanXuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Loại máy bay, Số ghế, Hãng sản xuất).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 2. Chuyển đổi và kiểm tra Số ghế
        int soGhe;
        try {
            soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) {
                JOptionPane.showMessageDialog(this, "Số ghế phải là một số nguyên dương.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số ghế phải là một số nguyên hợp lệ.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 3. Lấy MaSanBay từ TenSanBay
        String maSanBay = null;
        if (tenViTriHienTai != null) {
            maSanBay = getMaSanBayFromTenSanBay(tenViTriHienTai);
            if (maSanBay == null) {
                JOptionPane.showMessageDialog(null, "Tên sân bay '" + tenViTriHienTai + "' không hợp lệ hoặc không tìm thấy. Vui lòng chọn lại hoặc bỏ trống.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        String sql = "UPDATE MAY_BAY SET " +
                     "LoaiMayBay = ?, " +
                     "SoGhe = ?, " +
                     "HangSanXuat = ?, " +
                     "TrangThai = ?, " +
                     "ViTriHienTai = ? " +
                     "WHERE MaMayBay = ?";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loaiMayBay);
            ps.setInt(2, soGhe);
            ps.setString(3, hangSanXuat);
            ps.setString(4, trangThai);
            ps.setString(5, maSanBay); // Lưu MaSanBay vào cột ViTriHienTai
            ps.setString(6, maMayBay); // Tham số WHERE

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy máy bay có mã " + maMayBay + " để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            handleMayBayError(ex, "update");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi cập nhật máy bay:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean deleteMayBay(String maMayBay) {
        String sql = "DELETE FROM MAY_BAY WHERE MaMayBay = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMayBay);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa máy bay thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy máy bay để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            handleMayBayError(ex, "delete"); // Gọi hàm xử lý lỗi riêng cho máy bay
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xóa máy bay: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private void handleMayBayError(SQLException ex, String operation) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage().toLowerCase();

        // 1. Ràng buộc NOT NULL
        if (msg.contains("cannot insert null into") || (msg.contains("cannot update") && msg.contains("null"))) {
            if (msg.contains("loaimaybay")) showWarning("Loại máy bay không được để trống.");
            else if (msg.contains("soghe")) showWarning("Số ghế không được để trống.");
            else if (msg.contains("hangsanxuat")) showWarning("Hãng sản xuất không được để trống.");
            else showWarning("Một trường bắt buộc đang bị để trống.");
            return;
        }

        // 2. Ràng buộc CHECK
        // (Giả sử bạn đã đặt tên constraint là CHK_MAYBAY_TRANGTHAI)
        if (msg.contains("chk_maybay_trangthai")) {
            showWarning("Trạng thái máy bay không hợp lệ! Chỉ được chọn 'Đang sử dụng', 'Đang bảo trì', hoặc 'Dừng hoạt động'.");
            return;
        }
        // Thêm kiểm tra cho SoGhe nếu có ràng buộc CHECK trong DB (ví dụ: SoGhe >= 1)
        if (msg.contains("chk_maybay_soghe")) { // Nếu có constraint này
            showWarning("Số ghế phải là một số nguyên dương.");
            return;
        }


        // 3. Ràng buộc FOREIGN KEY (ViTriHienTai tham chiếu đến MaSanBay)
        // Mã lỗi Oracle cho ràng buộc tham chiếu (parent key not found) là ORA-02291
        if (code == 2291 && msg.contains("sys_c00") && msg.contains("vitrihientai")) {
            showWarning("Vị trí hiện tại (sân bay) không tồn tại. Vui lòng kiểm tra lại.");
            return;
        }

        // 4. Lỗi Primary Key (trùng Mã máy bay) - thường không xảy ra với FN_TAO_MAMB()
        // nhưng nếu có lỗi logic hoặc hàm FN_TAO_MAMB() bị lỗi, nó có thể xảy ra.
        if (code == 1 && msg.contains("pk_maybay")) { // Mã lỗi ORA-00001 là unique constraint violated
            showWarning("Mã máy bay đã tồn tại. Vui lòng thử lại hoặc chọn mã khác.");
            return;
        }
        
        // Lỗi khi xóa máy bay mà có bản ghi tham chiếu (ví dụ: trong bảng CHUYEN_BAY)
        // Mã lỗi Oracle cho vi phạm ràng buộc FK khi xóa (child records found) là ORA-02292
        if (operation.equals("delete") && code == 2292) {
            showWarning("Không thể xóa máy bay này vì nó đang được tham chiếu trong các bảng khác (ví dụ: chuyến bay).");
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

    // Hàm này là bắt buộc để chuyển đổi Tên sân bay sang Mã sân bay khi lưu vào DB
    private String getMaSanBayFromTenSanBay(String tenSanBay) {
        String maSanBay = null;
        String sql = "SELECT MaSanBay FROM SAN_BAY WHERE TenSanBay = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenSanBay);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maSanBay = rs.getString("MaSanBay");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lấy mã sân bay từ tên sân bay:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
        return maSanBay;
    }
    
    private boolean insertLichBaoTri(String maMayBay, Date ngayBaoTri, double chiPhi, String ghiChu) {
        // 1. Kiểm tra các ràng buộc NOT NULL của ứng dụng
        if (maMayBay == null || maMayBay.isEmpty() ||
            ngayBaoTri == null || // JDateChooser trả về null nếu không chọn
            chiPhi < 0) { // Chi phí không thể âm, mặc dù DB có thể cho phép
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ và chính xác thông tin lịch bảo trì (Mã máy bay, Ngày bảo trì, Chi phí).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Câu lệnh SQL để thêm lịch bảo trì.
        // Sử dụng hàm FN_TAO_MABT() để tự động sinh MaBaoTri.
        String sql = "INSERT INTO LICH_BAO_TRI (MaBaoTri, MaMayBay, NgayBaoTri, ChiPhi, GhiChu) " +
                     "VALUES (FN_TAO_MABT(), ?, ?, ?, ?)";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMayBay);
            // Chuyển đổi java.util.Date sang java.sql.Date để tương thích với CSDL
            ps.setDate(2, new java.sql.Date(ngayBaoTri.getTime()));
            ps.setDouble(3, chiPhi);
            // Ghi chú có thể NULL trong CSDL, kiểm tra nếu là chuỗi rỗng thì set NULL
            if (ghiChu == null || ghiChu.trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.NVARCHAR); // Hoặc VARCHAR, tùy kiểu cột GhiChu
            } else {
                ps.setString(4, ghiChu.trim());
            }

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                // Thông báo thành công đã được hiển thị trong btnXacNhanActionPerformed
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Thêm lịch bảo trì thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            handleLichBaoTriError(ex, "insert"); // Gọi hàm xử lý lỗi riêng cho lịch bảo trì
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi thêm lịch bảo trì:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private void handleLichBaoTriError(SQLException ex, String operation) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage().toLowerCase();

        // 1. Ràng buộc NOT NULL
        if (msg.contains("cannot insert null into") || (msg.contains("cannot update") && msg.contains("null"))) {
            if (msg.contains("ngaybaotri")) showWarning("Ngày bảo trì không được để trống.");
            else if (msg.contains("chiphi")) showWarning("Chi phí bảo trì không được để trống.");
            else if (msg.contains("mamaybay")) showWarning("Mã máy bay không được để trống trong lịch bảo trì.");
            else showWarning("Một trường bắt buộc trong lịch bảo trì đang bị để trống.");
            return;
        }

        // 2. Ràng buộc FOREIGN KEY (MaMayBay tham chiếu đến MAY_BAY)
        // Mã lỗi Oracle cho ràng buộc tham chiếu (parent key not found) là ORA-02291
        if (code == 2291 && msg.contains("sys_c00") && msg.contains("mamaybay")) { // Kiểm tra tên constraint nếu có thể
            showWarning("Mã máy bay không tồn tại trong hệ thống. Vui lòng kiểm tra lại Mã máy bay.");
            return;
        }

        // 3. Ràng buộc CHECK (ví dụ: Chi phí phải dương)
        // Nếu bạn có ràng buộc CHECK trong DB cho ChiPhi, hãy thêm vào đây
        // Ví dụ: ALTER TABLE LICH_BAO_TRI ADD CONSTRAINT chk_lichbaotri_chiphi CHECK (ChiPhi >= 0);
        // if (msg.contains("chk_lichbaotri_chiphi")) {
        //     showWarning("Chi phí bảo trì phải lớn hơn hoặc bằng 0.");
        //     return;
        // }

        // 4. Lỗi Primary Key (trùng MaBaoTri) - ít xảy ra với hàm tự sinh
        if (code == 1 && msg.contains("pk_lich_bao_tri")) { // Tên PK của bảng LICH_BAO_TRI
            showWarning("Mã bảo trì đã tồn tại. Vui lòng thử lại.");
            return;
        }

        // 5. Các lỗi khác
        showError(ex);
    }
        
    private void searchByMaMayBay() {
        String maMayBay = txtTimKiem.getText().trim();

        // Nếu trường tìm kiếm rỗng hoặc đang hiển thị placeholder, tải lại toàn bộ bảng
        if (maMayBay.isEmpty() || maMayBay.equals("Nhập mã hoặc loại máy bay để tìm kiếm")) {
            loadMayBayToTable(); // Hàm này tải toàn bộ máy bay (đã sửa đổi để hiển thị tên sân bay)
            return;
        }

        // Câu truy vấn SQL để tìm kiếm máy bay theo mã và JOIN với SAN_BAY để lấy tên sân bay
        String sql = """
            SELECT
                mb.MaMayBay,
                mb.LoaiMayBay,
                mb.TrangThai,
                sb.TenSanBay AS TenViTriHienTai -- Lấy TenSanBay và đặt bí danh
            FROM MAY_BAY mb
            LEFT JOIN SAN_BAY sb ON mb.ViTriHienTai = sb.MaSanBay
            WHERE UPPER(mb.MaMayBay) = UPPER(?)
            ORDER BY mb.MaMayBay
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMayBay); // Đặt tham số cho mã máy bay
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tblDSMayBay.getModel();
            model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng

            boolean found = false;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaMayBay"),
                    rs.getString("LoaiMayBay"),
                    rs.getString("TrangThai"),
                    rs.getString("TenViTriHienTai") // Lấy tên sân bay đã JOIN
                });
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy máy bay nào với mã: " + maMayBay, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm máy bay:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addAutoSuggestToTimKiem() {
        listModelMB = new DefaultListModel<>();
        suggestionListMB = new JList<>(listModelMB);
        suggestionListMB.setFont(txtTimKiem.getFont());
        suggestionListMB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tạo cửa sổ gợi ý (JWindow)
        suggestionWindowMB = new JWindow(SwingUtilities.getWindowAncestor(this));
        JScrollPane scrollPane = new JScrollPane(suggestionListMB);
        suggestionWindowMB.getContentPane().add(scrollPane);

        // Xử lý click chuột vào gợi ý
        suggestionListMB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectSuggestionMB();
                }
            }
        });

        // Xử lý phím Enter và mũi tên
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionWindowMB.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        int index = suggestionListMB.getSelectedIndex();
                        if (index < listModelMB.size() - 1) {
                            suggestionListMB.setSelectedIndex(index + 1);
                            suggestionListMB.ensureIndexIsVisible(index + 1);
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        int index = suggestionListMB.getSelectedIndex();
                        if (index > 0) {
                            suggestionListMB.setSelectedIndex(index - 1);
                            suggestionListMB.ensureIndexIsVisible(index - 1);
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        selectSuggestionMB();
                        e.consume();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Nếu không có gợi ý hiển thị, nhấn Enter sẽ thực hiện tìm kiếm
                    searchByMaMayBay();
                    e.consume();
                }
            }
        });

        // Theo dõi khi người dùng gõ
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                showSuggestionsMB();
            }

            public void removeUpdate(DocumentEvent e) {
                showSuggestionsMB();
            }

            public void changedUpdate(DocumentEvent e) {
                showSuggestionsMB();
            }
        });

        // Ẩn gợi ý khi mất focus
        txtTimKiem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Sử dụng invokeLater để đảm bảo sự kiện focusLost hoàn thành trước khi ẩn cửa sổ,
                // tránh trường hợp click vào item gợi ý không được xử lý
                SwingUtilities.invokeLater(() -> suggestionWindowMB.setVisible(false));
            }
        });
    }
    
    private void selectSuggestionMB() {
        String selected = suggestionListMB.getSelectedValue();
        if (selected != null) {
            // Lấy MaMayBay từ chuỗi gợi ý (ví dụ: "MB001 - Boeing 747")
            String maMayBay = selected.split(" - ")[0]; // Lấy phần trước " - "
            txtTimKiem.setText(maMayBay);
            suggestionWindowMB.setVisible(false);
            searchByMaMayBay(); // Gọi hàm tìm kiếm máy bay theo mã
        }
    }
    
    private void showSuggestionsMB() {
        String text = txtTimKiem.getText().trim().toLowerCase();
        if (text.isEmpty() || text.equals("Nhập mã hoặc loại máy bay để tìm kiếm".toLowerCase())) { // Kiểm tra cả placeholder
            suggestionWindowMB.setVisible(false);
            return;
        }

        listModelMB.clear();
        // Truy vấn để tìm kiếm theo MaMayBay HOẶC LoaiMayBay
        String sql = "SELECT MaMayBay, LoaiMayBay FROM MAY_BAY " +
                     "WHERE LOWER(MaMayBay) LIKE ? OR LOWER(LoaiMayBay) LIKE ? " +
                     "ORDER BY MaMayBay"; // Sắp xếp để gợi ý dễ nhìn

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + text + "%"); // Tìm kiếm MaMayBay chứa text
            ps.setString(2, "%" + text + "%"); // Tìm kiếm LoaiMayBay chứa text

            ResultSet rs = ps.executeQuery();
            List<String> suggestions = new ArrayList<>();
            while (rs.next()) {
                String maMayBay = rs.getString("MaMayBay");
                String loaiMayBay = rs.getString("LoaiMayBay");
                // Format chuỗi gợi ý để hiển thị cả Mã và Loại
                suggestions.add(maMayBay + " - " + loaiMayBay);
            }

            // Thêm các gợi ý vào listModel
            for (String s : suggestions) {
                listModelMB.addElement(s);
            }

            if (listModelMB.getSize() > 0) {
                suggestionListMB.setSelectedIndex(0); // Chọn mặc định item đầu tiên
                Point location = txtTimKiem.getLocationOnScreen();
                int windowWidth = txtTimKiem.getWidth();
                // Điều chỉnh chiều cao cửa sổ gợi ý, giới hạn tối đa 200px hoặc 10 item
                int windowHeight = Math.min(listModelMB.getSize() * 20, 200); 
                suggestionWindowMB.setBounds(location.x, location.y + txtTimKiem.getHeight(), windowWidth, windowHeight);
                suggestionWindowMB.setVisible(true);
            } else {
                suggestionWindowMB.setVisible(false);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Không show JOptionPane ở đây để tránh làm phiền người dùng khi gõ
            // System.err.println("Lỗi khi lấy gợi ý máy bay: " + ex.getMessage());
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

        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        spDSMayBay = new javax.swing.JScrollPane();
        tblDSMayBay = new javax.swing.JTable();
        spDSBaoTri = new javax.swing.JScrollPane();
        tblDSBaoTri = new javax.swing.JTable();
        lblDSBaoTri = new javax.swing.JLabel();
        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        lblLoai = new javax.swing.JLabel();
        lblSoGhe = new javax.swing.JLabel();
        lblHang = new javax.swing.JLabel();
        pnlLichBaoTri = new javax.swing.JPanel();
        lblLichBaoTri = new javax.swing.JLabel();
        lblNgayBaoTri = new javax.swing.JLabel();
        dcNgayBaoTri = new com.toedter.calendar.JDateChooser();
        txtGhiChu = new javax.swing.JTextField();
        txtChiPhi = new javax.swing.JTextField();
        lblChiPhi = new javax.swing.JLabel();
        lblGhiChu = new javax.swing.JLabel();
        btnBaoTri = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtMa = new javax.swing.JTextField();
        txtSoGhe = new javax.swing.JTextField();
        txtHang = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        lblTrangThai = new javax.swing.JLabel();
        lblViTri = new javax.swing.JLabel();
        txtLoai = new javax.swing.JTextField();
        pnlTrangThaiCards = new javax.swing.JPanel();
        cmbTrangThaiEdit = new javax.swing.JComboBox<>();
        txtTrangThaiView = new javax.swing.JTextField();
        pnlViTriCards = new javax.swing.JPanel();
        txtViTriView = new javax.swing.JTextField();
        cmbViTriEdit = new javax.swing.JComboBox<>();
        btnXacNhan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        lblStarLoai = new javax.swing.JLabel();
        lblStarHang = new javax.swing.JLabel();
        lblStarSoGhe = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1264, 824));

        pnlDanhSach.setBackground(java.awt.SystemColor.control);
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(632, 824));

        lblDanhSach.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblDanhSach.setText("Danh sách máy bay");

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

        tblDSMayBay.setAutoCreateRowSorter(true);
        tblDSMayBay.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSMayBay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã máy bay", "Loại máy bay", "Trạng thái", "Vị trí"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSMayBay.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDSMayBay.setRowHeight(30);
        spDSMayBay.setViewportView(tblDSMayBay);
        if (tblDSMayBay.getColumnModel().getColumnCount() > 0) {
            tblDSMayBay.getColumnModel().getColumn(0).setMinWidth(80);
            tblDSMayBay.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblDSMayBay.getColumnModel().getColumn(0).setMaxWidth(80);
            tblDSMayBay.getColumnModel().getColumn(1).setMinWidth(180);
            tblDSMayBay.getColumnModel().getColumn(1).setPreferredWidth(180);
            tblDSMayBay.getColumnModel().getColumn(1).setMaxWidth(180);
        }

        tblDSBaoTri.setAutoCreateRowSorter(true);
        tblDSBaoTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSBaoTri.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã bảo trì", "Ngày bảo trì", "Chi phí", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSBaoTri.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDSBaoTri.setRowHeight(30);
        spDSBaoTri.setViewportView(tblDSBaoTri);
        if (tblDSBaoTri.getColumnModel().getColumnCount() > 0) {
            tblDSBaoTri.getColumnModel().getColumn(0).setMinWidth(80);
            tblDSBaoTri.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblDSBaoTri.getColumnModel().getColumn(0).setMaxWidth(80);
            tblDSBaoTri.getColumnModel().getColumn(1).setMinWidth(180);
            tblDSBaoTri.getColumnModel().getColumn(1).setPreferredWidth(180);
            tblDSBaoTri.getColumnModel().getColumn(1).setMaxWidth(180);
        }

        lblDSBaoTri.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        lblDSBaoTri.setText("Danh sách lịch bảo trì");

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(spDSBaoTri, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(lblDanhSach))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnTimKiem))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(spDSMayBay, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(lblDSBaoTri)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblDanhSach)
                .addGap(32, 32, 32)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spDSMayBay, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDSBaoTri)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spDSBaoTri, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        pnlDanhSachLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnTimKiem, txtTimKiem});

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thêm máy bay");

        lblMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMa.setText("Mã máy bay");

        lblLoai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLoai.setText("Loại máy bay");

        lblSoGhe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblSoGhe.setText("Số ghế");

        lblHang.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblHang.setText("Hãng sản xuất");

        pnlLichBaoTri.setBackground(java.awt.Color.lightGray);

        lblLichBaoTri.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        lblLichBaoTri.setText("Lịch bảo trì");

        lblNgayBaoTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgayBaoTri.setText("Ngày bảo trì");

        dcNgayBaoTri.setDateFormatString("dd/MM/yyyy");
        dcNgayBaoTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        txtGhiChu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtGhiChu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGhiChuActionPerformed(evt);
            }
        });

        txtChiPhi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtChiPhi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChiPhiActionPerformed(evt);
            }
        });

        lblChiPhi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblChiPhi.setText("Chi phí");

        lblGhiChu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblGhiChu.setText("Ghi chú");

        javax.swing.GroupLayout pnlLichBaoTriLayout = new javax.swing.GroupLayout(pnlLichBaoTri);
        pnlLichBaoTri.setLayout(pnlLichBaoTriLayout);
        pnlLichBaoTriLayout.setHorizontalGroup(
            pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLichBaoTriLayout.createSequentialGroup()
                .addGroup(pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLichBaoTriLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNgayBaoTri)
                            .addComponent(lblChiPhi)
                            .addComponent(lblGhiChu))
                        .addGap(58, 58, 58)
                        .addGroup(pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtChiPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcNgayBaoTri, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlLichBaoTriLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(lblLichBaoTri)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnlLichBaoTriLayout.setVerticalGroup(
            pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLichBaoTriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLichBaoTri)
                .addGap(18, 18, 18)
                .addGroup(pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dcNgayBaoTri, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNgayBaoTri))
                .addGap(18, 18, 18)
                .addGroup(pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChiPhi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtChiPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlLichBaoTriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGhiChu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        btnBaoTri.setBackground(new java.awt.Color(0, 102, 153));
        btnBaoTri.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        btnBaoTri.setForeground(new java.awt.Color(255, 255, 255));
        btnBaoTri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/tools.png"))); // NOI18N
        btnBaoTri.setText("Bảo trì");
        btnBaoTri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaoTriActionPerformed(evt);
            }
        });

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

        txtMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMa.setAutoscrolls(false);

        txtSoGhe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

        txtHang.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N

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

        lblTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrangThai.setText("Trạng thái");

        lblViTri.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblViTri.setText("Vị trí hiện tại");

        txtLoai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLoai.setAutoscrolls(false);

        pnlTrangThaiCards.setLayout(new java.awt.CardLayout());

        cmbTrangThaiEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbTrangThaiEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang sử dụng", "Đang bảo trì", "Dừng hoạt động" }));
        cmbTrangThaiEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTrangThaiEditActionPerformed(evt);
            }
        });
        pnlTrangThaiCards.add(cmbTrangThaiEdit, "EDIT");

        txtTrangThaiView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        pnlTrangThaiCards.add(txtTrangThaiView, "VIEW");

        pnlViTriCards.setLayout(new java.awt.CardLayout());

        txtViTriView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        pnlViTriCards.add(txtViTriView, "VIEW");

        cmbViTriEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbViTriEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbViTriEditActionPerformed(evt);
            }
        });
        pnlViTriCards.add(cmbViTriEdit, "EDIT");

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

        lblStarLoai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarLoai.setForeground(new java.awt.Color(255, 0, 0));
        lblStarLoai.setText("*");
        lblStarLoai.setAutoscrolls(true);

        lblStarHang.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarHang.setForeground(new java.awt.Color(255, 0, 0));
        lblStarHang.setText("*");
        lblStarHang.setAutoscrolls(true);

        lblStarSoGhe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarSoGhe.setForeground(new java.awt.Color(255, 0, 0));
        lblStarSoGhe.setText("*");
        lblStarSoGhe.setAutoscrolls(true);

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(btnXacNhan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHuy)
                .addGap(140, 140, 140))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTrangThai)
                                    .addComponent(lblViTri, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pnlTrangThaiCards, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pnlViTriCards, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                        .addComponent(lblLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblStarLoai))
                                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblHang, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                                .addComponent(lblSoGhe)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblStarSoGhe)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblStarHang)))
                                .addGap(149, 149, 149)
                                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHang)
                                    .addComponent(txtSoGhe)
                                    .addComponent(txtMa)
                                    .addComponent(txtLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(lblThaoTac))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(pnlLichBaoTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(btnThem)
                        .addGap(35, 35, 35)
                        .addComponent(btnXoa)
                        .addGap(35, 35, 35)
                        .addComponent(btnSua)
                        .addGap(32, 32, 32)
                        .addComponent(btnBaoTri)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThaoTac)
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMa)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblLoai)
                        .addComponent(lblStarLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoGhe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSoGhe)
                    .addComponent(lblStarSoGhe, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHang))
                    .addComponent(lblStarHang, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTrangThaiCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlViTriCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblViTri))
                .addGap(24, 24, 24)
                .addComponent(pnlLichBaoTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoa)
                    .addComponent(btnSua)
                    .addComponent(btnBaoTri)
                    .addComponent(btnThem))
                .addGap(32, 32, 32)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXacNhan)
                    .addComponent(btnHuy))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pnlThaoTacLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtHang, txtSoGhe});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 624, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBaoTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaoTriActionPerformed
        int row = tblDSMayBay.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn máy bay cần bảo trì.");
            return;
        }
        loadMayBayDetails(tblDSMayBay.getValueAt(row, 0).toString());
        setMode(Mode.BAOTRI);
    }//GEN-LAST:event_btnBaoTriActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = tblDSMayBay.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn máy bay cần sửa.");
            return;
        }
        setMode(Mode.EDIT);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblDSMayBay.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn máy bay cần xóa.");
            return;
        }
        setMode(Mode.DELETE);
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        setMode(Mode.ADD);
    }//GEN-LAST:event_btnThemActionPerformed

    private void cmbTrangThaiEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTrangThaiEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTrangThaiEditActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        switch (currentMode) {
            case ADD:
                if (JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn thêm máy bay này?",
                    "Xác nhận thêm", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {

                    if (insertMayBay()) {
                        loadMayBayToTable(); // Tải lại bảng sau khi thêm thành công
                        setMode(Mode.NONE); // Chuyển về chế độ mặc định (NONE)
                        clearFormInput();   // Xóa form nhập liệu
                    }
                }
                break;

            case DELETE:
                int selectedRow = tblDSMayBay.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một máy bay để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Lấy mã máy bay từ cột đầu tiên (cột 0) của bảng
                String maMayBayToDelete = tblDSMayBay.getValueAt(selectedRow, 0).toString();

                if (JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa máy bay mã: " + maMayBayToDelete + "?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {

                    // Giả sử có hàm deleteMayBay(String maMayBay) trả về boolean
                    if (deleteMayBay(maMayBayToDelete)) {
                        loadMayBayToTable(); // Tải lại bảng sau khi xóa thành công
                        setMode(Mode.NONE);
                        clearFormInput();
                    }
                }
                break;

            case EDIT:
                if (JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn lưu thay đổi cho máy bay này?",
                    "Xác nhận sửa", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {

                    // Giả sử có hàm updateMayBay() trả về boolean
                    if (updateMayBay()) {
                        loadMayBayToTable(); // Tải lại bảng sau khi sửa thành công
                        setMode(Mode.NONE);
                        clearFormInput();
                    }
                }
                break;

            case BAOTRI: 
                // Lấy mã máy bay hiện tại đang được hiển thị trong form
                String maMayBayBaoTri = txtMa.getText().trim();

                if (JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn thêm lịch bảo trì cho máy bay " + maMayBayBaoTri + "?",
                    "Xác nhận bảo trì", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {

                    // Lấy ngày bảo trì từ JDateChooser
                    java.util.Date ngayBaoTri = dcNgayBaoTri.getDate();
                    if (ngayBaoTri == null) {
                        JOptionPane.showMessageDialog(this, "Ngày bảo trì không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Lấy chi phí và kiểm tra định dạng
                    double chiPhi = 0;
                    try {
                        chiPhi = Double.parseDouble(txtChiPhi.getText().trim());
                        if (chiPhi < 0) {
                            JOptionPane.showMessageDialog(this, "Chi phí bảo trì không thể âm.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Chi phí bảo trì không hợp lệ. Vui lòng nhập số.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String ghiChu = txtGhiChu.getText().trim();

                    if (insertLichBaoTri(maMayBayBaoTri, ngayBaoTri, chiPhi, ghiChu)) {
                        JOptionPane.showMessageDialog(this, "Thêm lịch bảo trì thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        setMode(Mode.VIEW); // Chuyển về chế độ xem thông tin máy bay
                        clearLichBaoTriInput(); // Xóa form lịch bảo trì
                    }
                }
                break;

            default:
                // Không làm gì nếu không ở trong các chế độ trên
                break;
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        setMode(Mode.NONE);
        tblDSMayBay.clearSelection();
        tblDSBaoTri.clearSelection();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void cmbViTriEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbViTriEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbViTriEditActionPerformed

    private void txtGhiChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGhiChuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGhiChuActionPerformed

    private void txtChiPhiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChiPhiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChiPhiActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchByMaMayBay();
        }
        setMode(Mode.NONE);
    }//GEN-LAST:event_txtTimKiemKeyPressed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        searchByMaMayBay();
        setMode(Mode.NONE);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaoTri;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbTrangThaiEdit;
    private javax.swing.JComboBox<String> cmbViTriEdit;
    private com.toedter.calendar.JDateChooser dcNgayBaoTri;
    private javax.swing.JLabel lblChiPhi;
    private javax.swing.JLabel lblDSBaoTri;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblGhiChu;
    private javax.swing.JLabel lblHang;
    private javax.swing.JLabel lblLichBaoTri;
    private javax.swing.JLabel lblLoai;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblNgayBaoTri;
    private javax.swing.JLabel lblSoGhe;
    private javax.swing.JLabel lblStarHang;
    private javax.swing.JLabel lblStarLoai;
    private javax.swing.JLabel lblStarSoGhe;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JLabel lblViTri;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlLichBaoTri;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JPanel pnlTrangThaiCards;
    private javax.swing.JPanel pnlViTriCards;
    private javax.swing.JScrollPane spDSBaoTri;
    private javax.swing.JScrollPane spDSMayBay;
    private javax.swing.JTable tblDSBaoTri;
    private javax.swing.JTable tblDSMayBay;
    private javax.swing.JTextField txtChiPhi;
    private javax.swing.JTextField txtGhiChu;
    private javax.swing.JTextField txtHang;
    private javax.swing.JTextField txtLoai;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtSoGhe;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTrangThaiView;
    private javax.swing.JTextField txtViTriView;
    // End of variables declaration//GEN-END:variables
}
