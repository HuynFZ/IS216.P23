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
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;

public class QLDatVeForm extends javax.swing.JPanel {
    CardLayout cardMaChuyen, cardLoaiVe, cardNgayDatVe, cardTrangThai;
    private enum Mode { NONE, VIEW, ADD, DELETE, EDIT }
    private Mode currentMode = Mode.NONE;
    private final Set<JTextField> initializedPlaceholders = new HashSet<>();
    // Các biến cho gợi ý mã chuyến bay
    private DefaultListModel<String> listModelCB;
    private JList<String> suggestionListCB;
    private JWindow suggestionWindowCB;
    
    // Các biến cho gợi ý mã và tên khách hàng
    private DefaultListModel<String> listModelKH;
    private JList<String> suggestionListKH;
    private JWindow suggestionWindowKH;


    public QLDatVeForm() {
        // Khởi tạo các components
        initComponents();
        // Khởi tạo các CardLayout
        initCardLayout();
        // Nạp dữ liệu chuyến bay lên bảng tblDSChuyen
        loadChuyenBayToTable();
        // Nạp mã chuyến bay lên combobox cmbMaChuyenEdit
        loadMaChuyenBayToComboBox();
        // Đăng ký listener cho việc chọn dòng Chuyến bay
        initChuyenBaySelectionListener();
        //Đăng ký listener cho việc chọn dòng đặt vé
        initDatVeSelectionListener();
        //Set chế độ NONE ban đầu
        setMode(Mode.NONE);
         // Set placeholder cho thanh tìm kiếm
        setPlaceholder(txtTimKiem, "Nhập mã chuyến bay để tìm kiếm");
        // Thêm gợi ý tự động cho tính năng tìm kiếm
        addAutoSuggestToTimKiem();
        // Thêm gợi ý tự động cho txtMaKH
        addAutoSuggestToMaKH();
    }
    
    // Khởi tạo các cardLayout
    private void initCardLayout() {
        cardMaChuyen = (CardLayout) pnlMaChuyenCards.getLayout();
        cardLoaiVe = (CardLayout) pnlLoaiVeCards.getLayout();
        cardNgayDatVe = (CardLayout) pnlNgayDatVeCards.getLayout();
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
                lblThaoTac.setText("Thông tin vé máy bay");
                // Tắt btnXacNhan và btnHuy
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                // Không cho phép chỉnh sửa
                setThaoTacEditable(false);
                // Bật các nút Thêm, Xóa, Sửa
//                btnThem.setEnabled(true);
                btnXoa.setEnabled(true);
                btnSua.setEnabled(true);
                // Ẩn dấu * 
                showRequiredIndicators(false);
                break;
//            case ADD:
//                // Chỉnh mode sang ADD
//                currentMode = Mode.ADD;
//                // Đổi text lblThaoTac
//                lblThaoTac.setText("Thêm vé");
//                // Clear các trường thông tin trong form
//                clearFormInput();
//                // Đặt ngày hiện tại làm mặc định khi thêm mới
//                dcNgayDatVeEdit.setDate(new Date()); 
//                // Bật btnXacNhan và đổi text
//                btnXacNhan.setVisible(true);
//                btnXacNhan.setText("Xác nhận thêm");
//                // Bật btnHuy
//                btnHuy.setVisible(true);
//                // Áp dụng placeholder
//                applyPlaceholders();
//                // Cho phép chỉnh sửa
//                setThaoTacEditable(true);
//                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
////                btnThem.setEnabled(false);
//                btnXoa.setEnabled(false);
//                btnSua.setEnabled(false);
//                break;
            case EDIT:
                // Chỉnh mode sang EDIT
                currentMode = Mode.EDIT;
                applyNormalStyle();
                // Đổi text lblThaoTac
                lblThaoTac.setText("Sửa vé máy bay");
                // Bật btnXacNhan và đổi text
                btnXacNhan.setText("Xác nhận sửa");
                btnXacNhan.setVisible(true);
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Cho phép chỉnh sửa
                setThaoTacEditable(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
//                btnThem.setEnabled(false);
                btnXoa.setEnabled(false);
                btnSua.setEnabled(false);
                // Hiện dấu * 
                showRequiredIndicators(true);
                break;
            case DELETE:
                // Chỉnh mode sang DELETE
                currentMode = Mode.DELETE;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Xóa vé máy bay");
                // Bật btnXacNhan và đổi text
                btnXacNhan.setVisible(true);
                btnXacNhan.setText("Xác nhận xóa");
                // Bật btnHuy
                btnHuy.setVisible(true);
                // Vô hiệu hóa các nút Thêm, Xóa, Sửa
//                btnThem.setEnabled(false);
                btnXoa.setEnabled(false);
                btnSua.setEnabled(false);
                // Ẩn dấu * 
                showRequiredIndicators(false);
                break;
            default:
                // Chỉnh mode sang NONE
                currentMode = Mode.NONE;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thông tin vé máy bay");
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
//                btnThem.setEnabled(true);
                btnXoa.setEnabled(true);
                btnSua.setEnabled(true);
                // Ẩn dấu * 
                showRequiredIndicators(false);
                break;
        }
    }
    
    private void showRequiredIndicators(boolean show) {
        // Đặt visibility cho tất cả các JLabel dấu * đỏ
        lblStarMaChuyen.setVisible(show);
        lblStarTien.setVisible(show);
        lblStarLoaiVe.setVisible(show);
        lblStarNgayDatVe.setVisible(show);
        lblStarTrangThai.setVisible(show);
        lblStarMaKH.setVisible(show);
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
//            case ADD:
            case EDIT:
                statusCard = "EDIT";
                break;
        }
        cardMaChuyen.show(pnlMaChuyenCards, statusCard);
        cardLoaiVe.show(pnlLoaiVeCards, statusCard);
        cardNgayDatVe.show(pnlNgayDatVeCards, statusCard);
        cardTrangThai.show(pnlTrangThaiCards, statusCard);
        
        // Xử lý Mã vé, Tên khách hàng, Loại khách hàng, Số điện thoại, Email không được thêm, sửa
        if (editable == true) { 
            txtMaVe.setEnabled(false);
            txtTenKH.setEnabled(false);
            txtLoaiKH.setEnabled(false);        
            txtSDT.setEnabled(false);
            txtEmail.setEnabled(false);
        }
        
        // JTextField
        txtMaVe.setEditable(false);
        txtMaChuyenView.setEditable(editable);
        txtTongTien.setEditable(editable);
        txtLoaiVeView.setEditable(editable);
        txtNgayDatVeView.setEditable(editable);
        txtTrangThaiView.setEditable(editable);
        txtMaKH.setEditable(editable);
        txtTenKH.setEditable(false);
        txtLoaiKH.setEditable(false);        
        txtSDT.setEditable(false);
        txtEmail.setEditable(false);

        // JDateChooser
        dcNgayDatVeEdit.setEnabled(editable);

        // JComboBox (nếu có)
        cmbMaChuyenEdit.setEnabled(editable);
        cmbLoaiVeEdit.setEnabled(editable);
        cmbTrangThaiEdit.setEnabled(editable);
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
                    // Xóa bảng đặt vé
                    DefaultTableModel modelDatVe = (DefaultTableModel) tblDSDatVe.getModel();
                    if (modelDatVe != null) { // Kiểm tra model có tồn tại không
                        modelDatVe.setRowCount(0);
                    }
                    setMode(Mode.NONE); // Có thể đặt trạng thái không có gì được chọn
                    return;
                }

                String maChuyenBay = tblDSChuyen.getValueAt(selectedRow, 0).toString();
                txtMaChuyenView.setText(maChuyenBay); // Hiển thị mã chuyến bay đã chọn

                // Tải danh sách vé cho chuyến bay đã chọn
                loadDatVeToTable(maChuyenBay);
            }
        });
    }
        
    private void loadDatVeToTable(String maChuyenBay) {
        DefaultTableModel model = (DefaultTableModel) tblDSDatVe.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng hành lý

        // 1. Kiểm tra mã chuyến bay đầu vào
        if (maChuyenBay == null || maChuyenBay.trim().isEmpty()) {
            // Không làm gì và để bảng trống nếu không có mã chuyến bay cụ thể
            lblDSDatVe.setText("Danh sách vé máy bay"); // Đặt lại tiêu đề mặc định
            return;
        }

        // Câu lệnh SQL để lấy thông tin vé máy bay từ bảng VE_MAY_BAY
        // bao gồm MaVe, LoaiVe, NgayDatVe, TrangThaiVe
        String sql = "SELECT MaVe, LoaiVe, NgayDatVe, TrangThaiVe " +
                     "FROM VE_MAY_BAY " +
                     "WHERE MaChuyenBay = ? " +
                     "ORDER BY NgayDatVe DESC"; // Sắp xếp theo ngày đặt vé mới nhất

        try (Connection conn = ConnectionUtils.getMyConnection(); // Sử dụng lớp ConnectionUtils
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyenBay);
            // Cập nhật tiêu đề của panel/tab để hiển thị chuyến bay hiện tại
            lblDSDatVe.setText("Danh sách vé của chuyến bay " + maChuyenBay); 

            // Định dạng ngày/giờ cho cột "Ngày đặt vé"
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String maVe = rs.getString("MaVe");
                    String loaiVe = rs.getString("LoaiVe");
                    java.sql.Timestamp ngayDatVeTimestamp = rs.getTimestamp("NgayDatVe");
                    String ngayDatVeFormatted = (ngayDatVeTimestamp != null) ? sdf.format(ngayDatVeTimestamp) : "";
                    String trangThaiVe = rs.getString("TrangThaiVe");

                    // Thêm dòng mới vào table model
                    model.addRow(new Object[]{maVe, loaiVe, ngayDatVeFormatted, trangThaiVe});
                    found = true;
                }

                if (!found) {
                    // Hiển thị thông báo nếu không tìm thấy vé nào cho chuyến bay này
                    JOptionPane.showMessageDialog(this, "Không tìm thấy vé nào cho chuyến bay " + maChuyenBay + ".", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi SQL khi tải danh sách vé: " + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Bắt các lỗi khác như ClassNotFoundException từ ConnectionUtils
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi không xác định khi tải danh sách vé: " + ex.getMessage(),
                    "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
        }

        // Sau khi tải xong, xóa lựa chọn và chi tiết của vé trước đó
        tblDSDatVe.clearSelection(); // Bỏ chọn dòng hiện tại (nếu có)
        clearFormInput(); 

        // Đặt mode về trạng thái mặc định 
        if (model.getRowCount() > 0) {
            setMode(Mode.NONE); // Hoặc một mode cho biết danh sách đã tải nhưng chưa chọn chi tiết
        } else {
            // Nếu không có hành lý nào cho chuyến bay này
            setMode(Mode.NONE); // Đặt mode không có gì để hiển thị/chọn
        }
    }
    
    private void initDatVeSelectionListener() {
        tblDSDatVe.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return; // Đảm bảo sự kiện chỉ được xử lý một lần khi lựa chọn ổn định
                }

                int selectedRow = tblDSDatVe.getSelectedRow();
                if (selectedRow < 0) { // Không có dòng nào được chọn
                    clearFormInput(); // Xóa form nhập liệu ở pnlThaoTac
                    setMode(Mode.NONE); // Đặt mode không có gì được chọn
                    return;
                }

                setMode(Mode.VIEW); // Chuyển sang chế độ xem chi tiết

                // Lấy MaVe từ cột đầu tiên của tblDSDatVe
                String maVe = getValueAtSafely(tblDSDatVe, selectedRow, 0);

                // Nạp thông tin chi tiết khác từ database bằng MaVe
                if (maVe != null && !maVe.isEmpty()) {
                    loadDatVeDetails(maVe);
                } else {
                    // Trường hợp MaVe rỗng từ bảng (không mong muốn, nhưng xử lý phòng ngừa)
                    clearFormInput();
                }
            }
        });
    }
    
    private String getValueAtSafely(JTable table, int row, int col) {
        if (row >= 0 && row < table.getRowCount() && col >= 0 && col < table.getColumnCount()) {
            Object value = table.getValueAt(row, col);
            return (value != null) ? value.toString() : "";
        }
        return "";
    }
    
    private void loadDatVeDetails(String maVe) {
        String sql = "SELECT " +
                     "    vmb.MaChuyenBay, " +
                     "    vmb.MaKhachHang, " +
                     "    vmb.TongTien, " +
                     "    vmb.LoaiVe, " +
                     "    vmb.NgayDatVe, " +
                     "    vmb.TrangThaiVe, " +
                     "    kh.HoTen AS TenKhachHang, " +
                     "    kh.SDT, " +
                     "    kh.Email, " +
                     "    kh.LoaiKhachHang " +
                     "FROM " +
                     "    VE_MAY_BAY vmb " +
                     "JOIN " +
                     "    KHACH_HANG kh ON vmb.MaKhachHang = kh.MaKhachHang " +
                     "WHERE " +
                     "    vmb.MaVe = ?";

        try (Connection conn = ConnectionUtils.getMyConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVe);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Thông tin từ VE_MAY_BAY
                    txtMaVe.setText(maVe);
                    
                    String maChuyenBayDB = rs.getString("MaChuyenBay");
                    txtMaChuyenView.setText(maChuyenBayDB != null ? maChuyenBayDB : "");
                    cmbMaChuyenEdit.setSelectedItem(maChuyenBayDB);

                    String maKhachHangDB = rs.getString("MaKhachHang");
                    txtMaKH.setText(maKhachHangDB != null ? maKhachHangDB : "");

                    double tongTienDB = rs.getDouble("TongTien");
                    DecimalFormat df = new DecimalFormat("#,##0"); // Định dạng tiền tệ
                    txtTongTien.setText(df.format(tongTienDB));

                    String loaiVeDB = rs.getString("LoaiVe");
                    txtLoaiVeView.setText(loaiVeDB != null ? loaiVeDB : "");
                    cmbLoaiVeEdit.setSelectedItem(loaiVeDB);

                    Timestamp ngayDatVeTS = rs.getTimestamp("NgayDatVe");
                    if (ngayDatVeTS != null) {
                        dcNgayDatVeEdit.setDate(new Date(ngayDatVeTS.getTime()));
                         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                         txtNgayDatVeView.setText(sdf.format(ngayDatVeTS));
                    } else {
                        dcNgayDatVeEdit.setDate(null);
                         txtNgayDatVeView.setText("");
                    }

                    String trangThaiVeDB = rs.getString("TrangThaiVe");
                    txtTrangThaiView.setText(trangThaiVeDB != null ? trangThaiVeDB : "");
                    cmbTrangThaiEdit.setSelectedItem(trangThaiVeDB);

                    // Thông tin từ KHACH_HANG
                    txtTenKH.setText(rs.getString("TenKhachHang"));
                    txtSDT.setText(rs.getString("SDT"));
                    txtEmail.setText(rs.getString("Email"));
                    txtLoaiKH.setText(rs.getString("LoaiKhachHang"));

                } else {
                    // Không tìm thấy vé hoặc thông tin liên quan
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy chi tiết cho vé mã: " + maVe,
                            "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    clearFormInput(); // Xóa các trường nếu không tìm thấy
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi SQL khi tải chi tiết vé: " + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Bắt các lỗi khác như ClassNotFoundException từ ConnectionUtils
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi không xác định khi tải chi tiết vé: " + ex.getMessage(),
                    "Lỗi Chung", JOptionPane.ERROR_MESSAGE);
        }
    }
        
    private void applyNormalStyle() {
        Font normalFont = new Font("UTM Centur", Font.PLAIN, 18);
        Color normalColor = Color.BLACK;
        
        txtMaVe.setFont(normalFont);
        txtMaVe.setForeground(normalColor);
        txtMaChuyenView.setFont(normalFont);
        txtMaChuyenView.setForeground(normalColor);
        txtTongTien.setFont(normalFont);
        txtTongTien.setForeground(normalColor);        
        txtLoaiVeView.setFont(normalFont);
        txtLoaiVeView.setForeground(normalColor);
        txtNgayDatVeView.setFont(normalFont);
        txtNgayDatVeView.setForeground(normalColor);
        txtTrangThaiView.setFont(normalFont);
        txtTrangThaiView.setForeground(normalColor);
        txtMaKH.setFont(normalFont);
        txtMaKH.setForeground(normalColor);
        txtTenKH.setFont(normalFont);
        txtTenKH.setForeground(normalColor);
        txtLoaiKH.setFont(normalFont);
        txtLoaiKH.setForeground(normalColor);
        txtSDT.setFont(normalFont);
        txtSDT.setForeground(normalColor);
        txtEmail.setFont(normalFont);
        txtEmail.setForeground(normalColor);
    }
    
    private void clearFormInput() {
        txtMaVe.setText(""); // Mã vé
        txtMaChuyenView.setText(""); // Mã chuyến bay 
        cmbMaChuyenEdit.setSelectedIndex(-1);
        txtTongTien.setText(""); // Tổng tiền
        txtLoaiVeView.setText("");
        cmbLoaiVeEdit.setSelectedIndex(-1); // Loại vé
        txtNgayDatVeView.setText("");
        dcNgayDatVeEdit.setDate(null);
        txtTrangThaiView.setText("");
        cmbTrangThaiEdit.setSelectedIndex(-1); // Trạng thái (JComboBox)
        txtMaKH.setText(""); // Mã khách hàng
        txtTenKH.setText(""); // Tên khách hàng
        txtLoaiKH.setText(""); // Loại khách hàng
        txtSDT.setText(""); // Số điện thoại
        txtEmail.setText(""); // Email
    }
    
    // Phương thức áp dụng placeholder cho tất cả các textfield nhập thông tin
    private void applyPlaceholders() {
        if (currentMode == Mode.ADD || currentMode == Mode.NONE) {
            if (txtMaVe.getText().trim().isEmpty()) {
                setPlaceholder(txtMaVe, "Mã vé");
            }

            if (txtMaChuyenView.getText().trim().isEmpty()) {
                setPlaceholder(txtMaChuyenView, "Chọn mã chuyến bay");
            }
            
            if (txtTongTien.getText().trim().isEmpty()) {
                setPlaceholder(txtTongTien, "Nhập tổng tiền");
            }

            if (txtLoaiVeView.getText().trim().isEmpty()) {
                setPlaceholder(txtLoaiVeView, "Chọn loại vé");
            }

            if (txtNgayDatVeView.getText().trim().isEmpty()) {
                setPlaceholder(txtNgayDatVeView, "Nhập ngày đặt vé");
            }

            if (txtTrangThaiView.getText().trim().isEmpty()) {
                setPlaceholder(txtTrangThaiView, "Chọn trạng thái");
            }

            if (txtMaKH.getText().trim().isEmpty()) {
                setPlaceholder(txtMaKH, "Nhập mã khách hàng");
            }
            
            if (txtTenKH.getText().trim().isEmpty()) {
                setPlaceholder(txtTenKH, "Tên khách hàng");
            }

            if (txtLoaiKH.getText().trim().isEmpty()) {
                setPlaceholder(txtLoaiKH, "Loại khách hàng");
            }

            if (txtSDT.getText().trim().isEmpty()) {
                setPlaceholder(txtSDT, "Số điện thoại");
            }
            if (txtEmail.getText().trim().isEmpty()) {
                setPlaceholder(txtEmail, "Email");
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
    
    private String getTextFieldValue(JTextField textField, String placeholderText) {
        String text = textField.getText().trim();
        if (text.equals(placeholderText)) {
            return ""; // Trả về chuỗi rỗng nếu đó là placeholder
        }
        return text;
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
    
    private void addAutoSuggestToMaKH() {
        listModelKH = new DefaultListModel<>();
        suggestionListKH = new JList<>(listModelKH);
        suggestionListKH.setFont(txtMaKH.getFont()); // Sử dụng font của txtMaKH
        suggestionListKH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tạo cửa sổ gợi ý (JWindow)
        // Cửa sổ gợi ý sẽ thuộc về cửa sổ chứa form hiện tại
        suggestionWindowKH = new JWindow(SwingUtilities.getWindowAncestor(this));
        JScrollPane scrollPane = new JScrollPane(suggestionListKH);
        suggestionWindowKH.getContentPane().add(scrollPane);

        // Xử lý click chuột vào gợi ý
        suggestionListKH.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectSuggestionKH();
                }
            }
        });

        // Xử lý phím Enter và mũi tên
        txtMaKH.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionWindowKH.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        int index = suggestionListKH.getSelectedIndex();
                        if (index < listModelKH.size() - 1) {
                            suggestionListKH.setSelectedIndex(index + 1);
                            suggestionListKH.ensureIndexIsVisible(index + 1);
                        }
                        e.consume(); // Ngăn sự kiện phím được xử lý tiếp bởi txtMaKH
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        int index = suggestionListKH.getSelectedIndex();
                        if (index > 0) {
                            suggestionListKH.setSelectedIndex(index - 1);
                            suggestionListKH.ensureIndexIsVisible(index - 1);
                        }
                        e.consume(); // Ngăn sự kiện phím được xử lý tiếp bởi txtMaKH
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        selectSuggestionKH();
                        e.consume(); // Ngăn sự kiện phím được xử lý tiếp bởi txtMaKH
                    }
                }
            }
        });

        // Theo dõi khi người dùng gõ
        txtMaKH.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                showSuggestionsKH();
            }

            public void removeUpdate(DocumentEvent e) {
                showSuggestionsKH();
            }

            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý changedUpdate cho JTextField
            }
        });

        txtMaKH.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Sử dụng invokeLater để đảm bảo sự kiện focusLost hoàn thành trước khi ẩn cửa sổ,
                // tránh trường hợp click vào item gợi ý không được xử lý
                SwingUtilities.invokeLater(() -> {
                    // Chỉ ẩn nếu suggestionWindowKH không phải là nguồn sự kiện focus
                    // (nghĩa là người dùng không click vào JList)
                    if (!suggestionWindowKH.isAncestorOf(e.getOppositeComponent())) {
                        suggestionWindowKH.setVisible(false);
                    }
                });
            }
        });
    }
    
    private void selectSuggestionKH() {
        String selected = suggestionListKH.getSelectedValue();
        if (selected != null) {
            // Chuỗi gợi ý có dạng "MaKhachHang - HoTen"
            String[] parts = selected.split(" - ", 2); // Tách thành 2 phần: Mã KH và Họ Tên
            if (parts.length == 2) {
                txtMaKH.setText(parts[0]); // Điền mã khách hàng
                txtTenKH.setText(parts[1]); // Điền họ tên khách hàng
            } else {
                txtMaKH.setText(selected); // Nếu chỉ có mã KH
                txtTenKH.setText(""); // Xóa họ tên nếu không có
            }
            suggestionWindowKH.setVisible(false); // Ẩn cửa sổ gợi ý
        }
    }
    
    private void showSuggestionsKH() {
        String text = txtMaKH.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            suggestionWindowKH.setVisible(false);
            listModelKH.clear();
            return;
        }

        listModelKH.clear(); // Xóa các gợi ý cũ

        // Câu truy vấn SQL để tìm kiếm theo MaKhachHang HOẶC HoTen
        String sql = """
            SELECT MaKhachHang, HoTen
            FROM KHACH_HANG
            WHERE LOWER(MaKhachHang) LIKE ? OR LOWER(HoTen) LIKE ?
            ORDER BY MaKhachHang
        """;

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + text + "%"); // Tìm kiếm MaKhachHang chứa text
            ps.setString(2, "%" + text + "%"); // Tìm kiếm HoTen chứa text

            ResultSet rs = ps.executeQuery();
            List<String> suggestions = new ArrayList<>();
            while (rs.next()) {
                String maKH = rs.getString("MaKhachHang");
                String hoTen = rs.getString("HoTen");
                // Format chuỗi gợi ý để hiển thị cả Mã và Họ Tên
                suggestions.add(maKH + " - " + hoTen);
            }

            // Thêm các gợi ý vào listModel
            for (String s : suggestions) {
                listModelKH.addElement(s);
            }

            if (listModelKH.getSize() > 0) {
                suggestionListKH.setSelectedIndex(0); // Chọn mặc định item đầu tiên
                Point location = txtMaKH.getLocationOnScreen();
                int windowWidth = txtMaKH.getWidth();
                // Điều chỉnh chiều cao cửa sổ gợi ý, giới hạn tối đa 200px hoặc 10 item
                int windowHeight = Math.min(listModelKH.getSize() * 20, 200);
                suggestionWindowKH.setBounds(location.x, location.y + txtMaKH.getHeight(), windowWidth, windowHeight);
                suggestionWindowKH.setVisible(true);
            } else {
                suggestionWindowKH.setVisible(false);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Không show JOptionPane ở đây để tránh làm phiền người dùng khi gõ
             System.err.println("Lỗi khi lấy gợi ý khách hàng: " + ex.getMessage());
        }
    }
    
//    private boolean insertDatVe() {
//        // Lấy dữ liệu từ các component
//        // MaVe sẽ được tạo tự động bởi hàm FN_TAO_MAVE(maChuyenBay)
//        String maChuyenBay = (String) cmbMaChuyenEdit.getSelectedItem();
//        String tongTienStr = txtTongTien.getText().trim().replace(",", ""); // Xóa dấu phẩy nếu có
//        String loaiVe = (String) cmbLoaiVeEdit.getSelectedItem();
//        Date ngayDatVeDate = dcNgayDatVeEdit.getDate();
//        String trangThaiVe = (String) cmbTrangThaiEdit.getSelectedItem();
//        String maKhachHang = txtMaKH.getText().trim().toUpperCase();
//
//        // 1. Kiểm tra ràng buộc NOT NULL của ứng dụng
//        if (maChuyenBay == null || maChuyenBay.isEmpty() || maKhachHang.isEmpty() ||
//            tongTienStr.isEmpty() || loaiVe == null || loaiVe.isEmpty() ||
//            ngayDatVeDate == null || trangThaiVe == null || trangThaiVe.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Mã chuyến bay, Mã khách hàng, Tổng tiền, Loại vé, Ngày đặt vé, Trạng thái vé).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
//            return false;
//        }
//
//        Double tongTien = null;
//        try {
//            tongTien = Double.parseDouble(tongTienStr);
//            if (tongTien < 0) { // Kiểm tra giá trị hợp lệ
//                JOptionPane.showMessageDialog(this, "Tổng tiền phải là một số không âm.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Tổng tiền phải là một số hợp lệ.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
//            return false;
//        }
//
//        String sql = "INSERT INTO VE_MAY_BAY (MaVe, MaChuyenBay, MaKhachHang, TongTien, LoaiVe, NgayDatVe, TrangThaiVe) " +
//                     "VALUES (FN_TAO_MAVE(?), ?, ?, ?, ?, ?, ?)"; // Giả sử có hàm FN_TAO_MAVE(maChuyenBay)
//
//        try (Connection conn = ConnectionUtils.getMyConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, maChuyenBay);
//            ps.setString(2, maChuyenBay);
//            ps.setString(3, maKhachHang);
//            ps.setDouble(4, tongTien);
//            ps.setString(5, loaiVe);
//            ps.setTimestamp(6, new Timestamp(ngayDatVeDate.getTime())); // Chuyển Date sang Timestamp
//            ps.setString(7, trangThaiVe);
//
//            int rowsInserted = ps.executeUpdate();
//            if (rowsInserted > 0) {
//                JOptionPane.showMessageDialog(this, "Thêm vé máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                return true;
//            } else {
//                JOptionPane.showMessageDialog(this, "Thêm vé máy bay thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//
//        } catch (SQLException ex) {
//            handleDatVeError(ex, "insert"); // Gọi hàm xử lý lỗi
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi thêm vé máy bay:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//        return false;
//    }
    
    private boolean updateDatVe() {
        // Lấy dữ liệu từ các component
        String maVe = txtMaVe.getText().trim(); // Mã vé được lấy từ trường txtMaVe (không cho chỉnh sửa)
        String maChuyenBay = (String) cmbMaChuyenEdit.getSelectedItem();
        String maKhachHang = getTextFieldValue(txtMaKH, "Nhập mã khách hàng");
        // Xử lý tổng tiền và dấu ","
        String rawTongTien = getTextFieldValue(txtTongTien, "Nhập tổng tiền");
        String tongTienStr = rawTongTien.replace(",", "");
        String loaiVe = (String) cmbLoaiVeEdit.getSelectedItem();
        Date ngayDatVeDate = dcNgayDatVeEdit.getDate();
        String trangThaiVe = (String) cmbTrangThaiEdit.getSelectedItem();

        // 1. Kiểm tra dữ liệu đầu vào và mã vé
        if (maVe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã vé không được để trống khi cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (maChuyenBay == null || maChuyenBay.isEmpty() || maKhachHang.isEmpty() ||
            tongTienStr.isEmpty() || loaiVe == null || loaiVe.isEmpty() ||
            ngayDatVeDate == null || trangThaiVe == null || trangThaiVe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Mã chuyến bay, Mã khách hàng, Tổng tiền, Loại vé, Ngày đặt vé, Trạng thái vé).", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Double tongTien = null;
        try {
            tongTien = Double.parseDouble(tongTienStr);
            if (tongTien < 0) {
                JOptionPane.showMessageDialog(this, "Tổng tiền phải là một số không âm.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tổng tiền phải là một số hợp lệ.", "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "UPDATE VE_MAY_BAY SET " +
                     "MaChuyenBay = ?, " +
                     "MaKhachHang = ?, " +
                     "TongTien = ?, " +
                     "LoaiVe = ?, " +
                     "NgayDatVe = ?, " +
                     "TrangThaiVe = ? " +
                     "WHERE MaVe = ?";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyenBay);
            ps.setString(2, maKhachHang);
            ps.setDouble(3, tongTien);
            ps.setString(4, loaiVe);
            ps.setTimestamp(5, new Timestamp(ngayDatVeDate.getTime()));
            ps.setString(6, trangThaiVe);
            ps.setString(7, maVe); // Tham số WHERE

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật vé máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy vé máy bay có mã " + maVe + " để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            handleDatVeError(ex, "update"); // Gọi hàm xử lý lỗi
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi cập nhật vé máy bay:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean deleteDatVe(String maVe) {
        String sql = "DELETE FROM VE_MAY_BAY WHERE MaVe = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVe);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa vé máy bay thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy vé máy bay để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            handleDatVeError(ex, "delete"); // Gọi hàm xử lý lỗi
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xóa vé máy bay: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Phương thức handle lỗi ĐẶT VÉ
    private void handleDatVeError(SQLException ex, String operation) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage().toLowerCase();

        // In đầy đủ lỗi ra console để debug
        ex.printStackTrace();

        // 1. Ràng buộc NOT NULL (Giữ nguyên như cũ, chỉ điều chỉnh thông báo nếu cần)
        if (msg.contains("cannot insert null into") || (msg.contains("cannot update") && msg.contains("null"))) {
            if (msg.contains("vmb.machuyenbay")) showWarning("Mã chuyến bay không được để trống.");
            else if (msg.contains("vmb.makhachhang")) showWarning("Mã khách hàng không được để trống.");
            else if (msg.contains("vmb.tongtien")) showWarning("Tổng tiền không được để trống.");
            else if (msg.contains("vmb.loaive")) showWarning("Loại vé không được để trống.");
            else if (msg.contains("vmb.ngaydatve")) showWarning("Ngày đặt vé không được để trống.");
            else if (msg.contains("vmb.trangthaive")) showWarning("Trạng thái vé không được để trống.");
            else showWarning("Một trường bắt buộc đang bị để trống hoặc giá trị NULL không hợp lệ.");
            return;
        }

        // 2. Ràng buộc CHECK
        if (msg.contains("chk_vemaybay_loaive")) {
            showWarning("Loại vé không hợp lệ! Chỉ được chọn 'Vé khung giờ' hoặc 'Vé xác định'.");
            return;
        }
        if (msg.contains("chk_vemaybay_trangthaive")) {
            showWarning("Trạng thái vé không hợp lệ! Chỉ được chọn 'Chưa thanh toán', 'Đã thanh toán' hoặc 'Đã hủy'.");
            return;
        }

        // 3. Ràng buộc FOREIGN KEY
        // Mã lỗi Oracle cho ràng buộc tham chiếu (parent key not found) là ORA-02291
        if (code == 2291) {
            // Kiểm tra lỗi Foreign Key cho MaChuyenBay hoặc MaKhachHang
            if (msg.contains("sys_c00") && msg.contains("machuyenbay")) {
                showWarning("Mã chuyến bay không tồn tại trong hệ thống. Vui lòng kiểm tra lại Mã chuyến bay.");
            } else if (msg.contains("sys_c00") && msg.contains("makhachhang")) {
                showWarning("Mã khách hàng không tồn tại trong hệ thống. Vui lòng kiểm tra lại Mã khách hàng.");
            } else {
                showWarning("Có lỗi về dữ liệu tham chiếu (foreign key). Vui lòng kiểm tra Mã chuyến bay và Mã khách hàng.");
            }
            return;
        }

        // 4. Lỗi Primary Key (trùng Mã vé)
        // Mã lỗi Oracle cho Unique Constraint Violation là ORA-00001
        if (code == 1 && (msg.contains("pk_ve_may_bay") || msg.contains("duplicate key"))) {
            showWarning("Mã vé đã tồn tại trong hệ thống. Vui lòng thử lại hoặc liên hệ quản trị viên.");
            return;
        }

        // 5. Lỗi từ Triggers
        // Các lỗi tùy chỉnh từ RAISE_APPLICATION_ERROR sẽ có mã lỗi nằm trong khoảng -20000 đến -20999
        // Trigger TRG_VE_MAY_BAY_CHECK_TRANGTHAI: mã lỗi -20201
        if (code == 20201) {
            showWarning("Không thể đặt vé! Chỉ các chuyến bay đang mở mới được phép đặt vé.");
            return;
        }
        // Trigger TRG_VE_MAY_BAY_DATVE_TRUOC1H: mã lỗi -20202
        if (code == 20202) {
            showWarning("Bạn chỉ có thể đặt vé trước giờ cất cánh ít nhất 1 giờ. Vui lòng chọn chuyến bay khác hoặc thời gian hợp lệ.");
            return;
        }

        // 6. Các lỗi khác (show toàn bộ lỗi nếu không bắt được)
        showError(ex);
    }

    // Các hàm tiện ích showWarning và showError (giữ nguyên)
    private void showWarning(String text) {
        JOptionPane.showMessageDialog(this, text, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(SQLException ex) {
        // In stack trace ra console để debug chi tiết
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Đã xảy ra lỗi cơ sở dữ liệu:\n" + ex.getMessage(),
            "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    private void fillThongTinKH() {
        String maKhachHang = txtMaKH.getText().trim();

        // Nếu không có mã khách hàng hoặc mã là chuỗi rỗng
        if (maKhachHang.isEmpty()) {
            // Xóa trắng các trường thông tin khách hàng
            txtTenKH.setText("");
            txtLoaiKH.setText("");
            txtSDT.setText("");
            txtEmail.setText("");
            return;
        }

        String sql = "SELECT HoTen, LoaiKhachHang, SDT, Email " +
                     "FROM KHACH_HANG " +
                     "WHERE UPPER(MaKhachHang) = ?";

        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhachHang.toUpperCase()); // Đảm bảo so sánh không phân biệt chữ hoa chữ thường
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Điền thông tin vào các trường tương ứng
                txtTenKH.setText(rs.getString("HoTen"));
                txtLoaiKH.setText(rs.getString("LoaiKhachHang"));
                txtSDT.setText(rs.getString("SDT"));
                txtEmail.setText(rs.getString("Email"));
            } else {
                // Nếu không tìm thấy khách hàng, xóa trắng các trường và thông báo
                txtTenKH.setText("");
                txtLoaiKH.setText("");
                txtSDT.setText("");
                txtEmail.setText("");
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy khách hàng với mã: " + maKhachHang,
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi truy vấn thông tin khách hàng:\n" + ex.getMessage(),
                "Lỗi cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi không xác định khi tải thông tin khách hàng:\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMaVe = new javax.swing.JLabel();
        lblMaChuyen = new javax.swing.JLabel();
        lblTien = new javax.swing.JLabel();
        lblLoaiVe = new javax.swing.JLabel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        lblLoaiKH = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();
        txtMaVe = new javax.swing.JTextField();
        txtTongTien = new javax.swing.JTextField();
        lblNgayDat = new javax.swing.JLabel();
        lblTenKH = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        txtLoaiKH = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtTenKH = new javax.swing.JTextField();
        btnXacNhan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        pnlLoaiVeCards = new javax.swing.JPanel();
        cmbLoaiVeEdit = new javax.swing.JComboBox<>();
        txtLoaiVeView = new javax.swing.JTextField();
        pnlTrangThaiCards = new javax.swing.JPanel();
        cmbTrangThaiEdit = new javax.swing.JComboBox<>();
        txtTrangThaiView = new javax.swing.JTextField();
        pnlNgayDatVeCards = new javax.swing.JPanel();
        txtNgayDatVeView = new javax.swing.JTextField();
        dcNgayDatVeEdit = new com.toedter.calendar.JDateChooser();
        pnlMaChuyenCards = new javax.swing.JPanel();
        cmbMaChuyenEdit = new javax.swing.JComboBox<>();
        txtMaChuyenView = new javax.swing.JTextField();
        lblMaKH = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        btnTimKiemKH = new javax.swing.JButton();
        lblStarMaChuyen = new javax.swing.JLabel();
        lblStarTien = new javax.swing.JLabel();
        lblStarNgayDatVe = new javax.swing.JLabel();
        lblStarMaKH = new javax.swing.JLabel();
        lblStarTrangThai = new javax.swing.JLabel();
        lblStarLoaiVe = new javax.swing.JLabel();
        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        pnlDSVe = new javax.swing.JPanel();
        lblDSDatVe = new javax.swing.JLabel();
        spDSVe = new javax.swing.JScrollPane();
        tblDSDatVe = new javax.swing.JTable();
        spDSChuyen = new javax.swing.JScrollPane();
        tblDSChuyen = new javax.swing.JTable();

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thông tin vé máy bay");

        lblMaVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaVe.setText("Mã vé máy bay");

        lblMaChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaChuyen.setText("Mã chuyến bay");

        lblTien.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTien.setText("Tổng tiền");

        lblLoaiVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLoaiVe.setText("Loại vé");

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

        lblLoaiKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLoaiKH.setText("Loại khách hàng");

        lblTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTrangThai.setText("Trạng thái");

        txtMaVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaVe.setAutoscrolls(false);
        txtMaVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaVeActionPerformed(evt);
            }
        });

        txtTongTien.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongTienActionPerformed(evt);
            }
        });

        lblNgayDat.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgayDat.setText("Ngày đặt vé");

        lblTenKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblTenKH.setText("Tên khách hàng");

        lblEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblEmail.setText("Email");

        lblSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblSDT.setText("Số điện thoại");

        txtLoaiKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLoaiKH.setAutoscrolls(false);
        txtLoaiKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoaiKHActionPerformed(evt);
            }
        });

        txtSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtSDT.setAutoscrolls(false);
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtEmail.setAutoscrolls(false);
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtTenKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTenKH.setAutoscrolls(false);
        txtTenKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenKHActionPerformed(evt);
            }
        });

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

        pnlLoaiVeCards.setLayout(new java.awt.CardLayout());

        cmbLoaiVeEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbLoaiVeEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vé khung giờ", "Vé xác định" }));
        pnlLoaiVeCards.add(cmbLoaiVeEdit, "EDIT");

        txtLoaiVeView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLoaiVeView.setAutoscrolls(false);
        txtLoaiVeView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoaiVeViewActionPerformed(evt);
            }
        });
        pnlLoaiVeCards.add(txtLoaiVeView, "VIEW");

        pnlTrangThaiCards.setLayout(new java.awt.CardLayout());

        cmbTrangThaiEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbTrangThaiEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Chưa thanh toán", "Đã thanh toán", "Đã hủy"}));
        pnlTrangThaiCards.add(cmbTrangThaiEdit, "EDIT");

        txtTrangThaiView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtTrangThaiView.setAutoscrolls(false);
        txtTrangThaiView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrangThaiViewActionPerformed(evt);
            }
        });
        pnlTrangThaiCards.add(txtTrangThaiView, "VIEW");

        pnlNgayDatVeCards.setLayout(new java.awt.CardLayout());

        txtNgayDatVeView.setEditable(false);
        txtNgayDatVeView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtNgayDatVeView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayDatVeViewActionPerformed(evt);
            }
        });
        pnlNgayDatVeCards.add(txtNgayDatVeView, "VIEW");

        dcNgayDatVeEdit.setDateFormatString("dd/MM/yyyy");
        dcNgayDatVeEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        pnlNgayDatVeCards.add(dcNgayDatVeEdit, "EDIT");

        pnlMaChuyenCards.setLayout(new java.awt.CardLayout());

        cmbMaChuyenEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbMaChuyenEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMaChuyenEditActionPerformed(evt);
            }
        });
        pnlMaChuyenCards.add(cmbMaChuyenEdit, "EDIT");

        txtMaChuyenView.setEditable(false);
        txtMaChuyenView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaChuyenView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaChuyenViewActionPerformed(evt);
            }
        });
        pnlMaChuyenCards.add(txtMaChuyenView, "VIEW");

        lblMaKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMaKH.setText("Mã khách hàng");

        txtMaKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMaKH.setAutoscrolls(false);
        txtMaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaKHActionPerformed(evt);
            }
        });
        txtMaKH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMaKHKeyPressed(evt);
            }
        });

        btnTimKiemKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/ngoc/search.png"))); // NOI18N
        btnTimKiemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemKHActionPerformed(evt);
            }
        });

        lblStarMaChuyen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarMaChuyen.setForeground(new java.awt.Color(255, 0, 0));
        lblStarMaChuyen.setText("*");
        lblStarMaChuyen.setAutoscrolls(true);

        lblStarTien.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarTien.setForeground(new java.awt.Color(255, 0, 0));
        lblStarTien.setText("*");
        lblStarTien.setAutoscrolls(true);

        lblStarNgayDatVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarNgayDatVe.setForeground(new java.awt.Color(255, 0, 0));
        lblStarNgayDatVe.setText("*");
        lblStarNgayDatVe.setAutoscrolls(true);

        lblStarMaKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarMaKH.setForeground(new java.awt.Color(255, 0, 0));
        lblStarMaKH.setText("*");
        lblStarMaKH.setAutoscrolls(true);

        lblStarTrangThai.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarTrangThai.setForeground(new java.awt.Color(255, 0, 0));
        lblStarTrangThai.setText("*");
        lblStarTrangThai.setAutoscrolls(true);

        lblStarLoaiVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarLoaiVe.setForeground(new java.awt.Color(255, 0, 0));
        lblStarLoaiVe.setText("*");
        lblStarLoaiVe.setAutoscrolls(true);

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(btnXacNhan)
                .addGap(6, 6, 6)
                .addComponent(lblThaoTac)
                .addGap(18, 18, 18)
                .addComponent(btnHuy))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblMaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(txtMaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblMaChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblStarMaChuyen)
                .addGap(51, 51, 51)
                .addComponent(pnlMaChuyenCards, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTien, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(lblStarTien)
                .addGap(99, 99, 99)
                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblLoaiVe)
                .addGap(11, 11, 11)
                .addComponent(lblStarLoaiVe)
                .addGap(119, 119, 119)
                .addComponent(pnlLoaiVeCards, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblNgayDat)
                .addGap(18, 18, 18)
                .addComponent(lblStarNgayDatVe)
                .addGap(71, 71, 71)
                .addComponent(pnlNgayDatVeCards, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTrangThai)
                .addGap(18, 18, 18)
                .addComponent(lblStarTrangThai)
                .addGap(83, 83, 83)
                .addComponent(pnlTrangThaiCards, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblStarMaKH)
                .addGap(39, 39, 39)
                .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(txtLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(btnXoa)
                .addGap(167, 167, 167)
                .addComponent(btnSua))
        );
        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblThaoTac)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnXacNhan)
                            .addComponent(btnHuy))))
                .addGap(27, 27, 27)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblMaVe))
                    .addComponent(txtMaVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaChuyen)
                    .addComponent(lblStarMaChuyen)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(pnlMaChuyenCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTien)
                            .addComponent(lblStarTien))))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoaiVe)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblStarLoaiVe))
                    .addComponent(pnlLoaiVeCards, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNgayDat)
                    .addComponent(lblStarNgayDatVe)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(pnlNgayDatVeCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTrangThai)
                    .addComponent(lblStarTrangThai)
                    .addComponent(pnlTrangThaiCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaKH)
                            .addComponent(lblStarMaKH))))
                .addGap(6, 6, 6)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblTenKH))
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblLoaiKH))
                    .addComponent(txtLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblSDT))
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblEmail))
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoa)
                    .addComponent(btnSua)))
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

        pnlDSVe.setBackground(javax.swing.UIManager.getDefaults().getColor("Component.focusColor"));

        lblDSDatVe.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        lblDSDatVe.setText("Danh sách đặt vé của chuyến bay");

        tblDSDatVe.setAutoCreateRowSorter(true);
        tblDSDatVe.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDSDatVe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã vé", "Loại vé", "Ngày đặt vé", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSDatVe.setToolTipText("");
        tblDSDatVe.setRowHeight(30);
        spDSVe.setViewportView(tblDSDatVe);
        if (tblDSDatVe.getColumnModel().getColumnCount() > 0) {
            tblDSDatVe.getColumnModel().getColumn(0).setMinWidth(120);
            tblDSDatVe.getColumnModel().getColumn(0).setPreferredWidth(120);
            tblDSDatVe.getColumnModel().getColumn(0).setMaxWidth(120);
            tblDSDatVe.getColumnModel().getColumn(1).setMinWidth(120);
            tblDSDatVe.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblDSDatVe.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        javax.swing.GroupLayout pnlDSVeLayout = new javax.swing.GroupLayout(pnlDSVe);
        pnlDSVe.setLayout(pnlDSVeLayout);
        pnlDSVeLayout.setHorizontalGroup(
            pnlDSVeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDSVeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDSDatVe)
                .addGap(101, 101, 101))
            .addGroup(pnlDSVeLayout.createSequentialGroup()
                .addComponent(spDSVe, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlDSVeLayout.setVerticalGroup(
            pnlDSVeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSVeLayout.createSequentialGroup()
                .addComponent(lblDSDatVe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spDSVe, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
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
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(lblDanhSach))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(pnlDSVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(0, 24, Short.MAX_VALUE)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnTimKiem)
                        .addGap(55, 55, 55))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                        .addComponent(spDSChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblDanhSach)
                .addGap(29, 29, 29)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addGap(18, 18, 18)
                .addComponent(spDSChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlDSVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDanhSachLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnTimKiem, txtTimKiem});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlThaoTac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblDSDatVe.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vé cần xóa.");
            return;
        }
        setMode(Mode.DELETE);
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = tblDSDatVe.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vé cần sửa.");
            return;
        }
        setMode(Mode.EDIT);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtMaVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaVeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaVeActionPerformed

    private void txtLoaiKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoaiKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoaiKHActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtTenKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenKHActionPerformed

    private void txtTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongTienActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        switch (currentMode) {
//            case ADD:
//                if (JOptionPane.showConfirmDialog(this,
//                        "Bạn có chắc muốn thêm vé này?",
//                        "Xác nhận thêm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                    if (insertDatVe()) { // Hàm insertDatVe() sẽ trả về boolean
//                        String maChuyenBayHienTai = (String) cmbMaChuyenEdit.getSelectedItem(); // Lấy mã chuyến bay hiện tại để load lại bảng
//                        if (maChuyenBayHienTai != null && !maChuyenBayHienTai.isEmpty()) {
//                                loadDatVeToTable(maChuyenBayHienTai);
//                        }
//                        setMode(Mode.NONE); // Chỉ trả về chế độ NONE nếu thêm thành công
//                        clearFormInput(); // Xóa form sau khi thêm thành công
//                    }    
//                }
//                // Nếu NO: không làm gì, vẫn ở chế độ ADD
//                break;
            
            case DELETE:        
                // Lấy mã vé từ hàng được chọn
                int selectedRow = tblDSDatVe.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một vé để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String maVeDel = getValueAtSafely(tblDSDatVe, selectedRow, 0); 

                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn xóa vé mã: " + maVeDel + "?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (deleteDatVe(maVeDel)) { // Hàm deleteDatVe() sẽ trả về boolean
                        String maChuyenBayHienTai = (String) cmbMaChuyenEdit.getSelectedItem(); // Lấy mã chuyến bay hiện tại để load lại bảng
                        if (maChuyenBayHienTai != null && !maChuyenBayHienTai.isEmpty()) {
                                loadDatVeToTable(maChuyenBayHienTai);
                        }
                        setMode(Mode.NONE);
                        clearFormInput();
                    }
                }
                break;

            case EDIT:
                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn lưu thay đổi?",
                        "Xác nhận sửa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (updateDatVe()) { // Hàm updateDatVe() sẽ trả về boolean
                        String maChuyenBayHienTai = (String) cmbMaChuyenEdit.getSelectedItem(); // Lấy mã chuyến bay hiện tại để load lại bảng
                        if (maChuyenBayHienTai != null && !maChuyenBayHienTai.isEmpty()) {
                           loadDatVeToTable(maChuyenBayHienTai);
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
        tblDSChuyen.clearSelection();
        tblDSDatVe.clearSelection();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSDTActionPerformed

    private void txtNgayDatVeViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayDatVeViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayDatVeViewActionPerformed

    private void txtTrangThaiViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrangThaiViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrangThaiViewActionPerformed

    private void txtLoaiVeViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoaiVeViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoaiVeViewActionPerformed

    private void txtMaChuyenViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaChuyenViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaChuyenViewActionPerformed

    private void cmbMaChuyenEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaChuyenEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMaChuyenEditActionPerformed

    private void txtMaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaKHActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        searchByMaChuyenBay();
        setMode(Mode.NONE);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchByMaChuyenBay();
        }
        setMode(Mode.NONE);
    }//GEN-LAST:event_txtTimKiemKeyPressed

    private void txtMaKHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaKHKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            fillThongTinKH();
        }
    }//GEN-LAST:event_txtMaKHKeyPressed

    private void btnTimKiemKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemKHActionPerformed
        fillThongTinKH();
    }//GEN-LAST:event_btnTimKiemKHActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTimKiemKH;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbLoaiVeEdit;
    private javax.swing.JComboBox<String> cmbMaChuyenEdit;
    private javax.swing.JComboBox<String> cmbTrangThaiEdit;
    private com.toedter.calendar.JDateChooser dcNgayDatVeEdit;
    private javax.swing.JLabel lblDSDatVe;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblLoaiKH;
    private javax.swing.JLabel lblLoaiVe;
    private javax.swing.JLabel lblMaChuyen;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaVe;
    private javax.swing.JLabel lblNgayDat;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblStarLoaiVe;
    private javax.swing.JLabel lblStarMaChuyen;
    private javax.swing.JLabel lblStarMaKH;
    private javax.swing.JLabel lblStarNgayDatVe;
    private javax.swing.JLabel lblStarTien;
    private javax.swing.JLabel lblStarTrangThai;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JLabel lblTien;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JPanel pnlDSVe;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlLoaiVeCards;
    private javax.swing.JPanel pnlMaChuyenCards;
    private javax.swing.JPanel pnlNgayDatVeCards;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JPanel pnlTrangThaiCards;
    private javax.swing.JScrollPane spDSChuyen;
    private javax.swing.JScrollPane spDSVe;
    private javax.swing.JTable tblDSChuyen;
    private javax.swing.JTable tblDSDatVe;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLoaiKH;
    private javax.swing.JTextField txtLoaiVeView;
    private javax.swing.JTextField txtMaChuyenView;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaVe;
    private javax.swing.JTextField txtNgayDatVeView;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTongTien;
    private javax.swing.JTextField txtTrangThaiView;
    // End of variables declaration//GEN-END:variables
}
