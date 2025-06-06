package View.Admin;

import Process.NhanVien.NhanVienAccountManager; // import lớp tạo tài khoản nhân viên
import ConnectDB.ConnectionUtils;       // import lớp ConnectionUtils
import com.toedter.calendar.JDateChooser;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.CallableStatement;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import javax.swing.JComboBox;


public class QLKhachHangForm extends javax.swing.JPanel {
    CardLayout cardNgaySinh, cardGioiTinh, cardChucVu, cardNgayVaoLam;
    private enum Mode { NONE, VIEW, ADD, DELETE, EDIT }
    private Mode currentMode = Mode.NONE;
    private JWindow suggestionWindow;
    private JList<String> suggestionList;
    private DefaultListModel<String> listModel;
    private final Set<JTextField> initializedPlaceholders = new HashSet<>();
    private NhanVienAccountManager nhanVienAccountManager = new NhanVienAccountManager();

    public QLKhachHangForm() {
        // Khởi tạo các components
        initComponents();
        // Khởi tạo các CardLayout
        initCardLayout();
        // Nạp dữ liệu lên bảng
        loadKhachHangToTable();
        // Đăng ký listener cho việc chọn dòng
        initSelectionListener();
        // Set chế độ NONE ban đầu
        setMode(Mode.NONE);
        // Set placeholder cho thanh tìm kiếm
        setPlaceholder(txtTimKiem, "Nhập mã khách hàng hoặc tên khách hàng");
        // Thêm gợi ý tự động cho tính năng tìm kiếm
        addAutoSuggestToTimKiem();
    }
    
    // Khởi tạo các cardLayout
    private void initCardLayout() {
        cardNgaySinh = (CardLayout) pnlNgaySinhCards.getLayout();
        cardGioiTinh = (CardLayout) pnlGioiTinhCards.getLayout();
        cardChucVu = (CardLayout) pnlChucVuCards.getLayout();
        cardNgayVaoLam = (CardLayout) pnlNgayVaoLamCards.getLayout();
    }
    
    // Khởi tạo listener cho các dòng trong JTable
    private void initSelectionListener() {
        tblDanhSach.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = tblDanhSach.getSelectedRow();
            if (row < 0) return;
            
            setMode(Mode.VIEW);
            // Lấy mã NV từ bảng
            String maNV = tblDanhSach.getValueAt(row, 0).toString();
            // Hiển thị các cột gọn trong bảng
            txtMa.setText(maNV);
            txtHoTen.setText(tblDanhSach.getValueAt(row, 1).toString());
            
            // Nạp thêm chi tiết từ database
            loadKhachHangDetails(maNV);
        });
    }
    
    // Tải dữ liệu nhân viên vào JTable
    private void loadKhachHangToTable() {
        DefaultTableModel model = (DefaultTableModel) tblDanhSach.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng
        
        String sql = "SELECT MaKhachHang, HoTen, CCCD, GioiTinh, QuocTich, LoaiKhachHang, HangThanhVien FROM KHACH_HANG";

        // Chuẩn bị formatter
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        try (Connection conn = ConnectionUtils.getMyConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Lấy timestamp
//                java.sql.Timestamp ts = rs.getTimestamp("NgayVaoLam");
//                // Format thành chuỗi dd/MM/yyyy (nếu ts == null thì để rỗng)
//                String ngayVaoLam = ts != null
//                    ? sdf.format(new java.util.Date(ts.getTime()))
//                    : "";
                String gt = rs.getString("GioiTinh");
                if (gt == null) gt = "Nam";
                else if (gt.equals('M')) gt = "Nam";
                else gt = "Nữ";
                model.addRow(new Object[]{
                    rs.getString("MaKhachHang"),
                    rs.getString("HoTen"),
                    rs.getString("CCCD"),
                    gt,
                    rs.getString("QuocTich"),
                    rs.getString("LoaiKhachHang"),
                    rs.getString("HangThanhVien")
                });
            }
            tblDanhSach.setModel(model);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu khách hàng:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Tải chi tiết nhân viên vào panel ThaoTac
    private void loadKhachHangDetails(String maKH) {
        String sql = "SELECT * FROM KHACH_HANG WHERE MaKhachHang = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Lấy timestamp từ DB (nếu bạn muốn giữ giờ phút giây)
                    java.sql.Timestamp ts = rs.getTimestamp("ThoiHanTV");

                    if (ts != null) {
                        java.util.Date hantv = new java.util.Date(ts.getTime());
                        dcHanTV.setDate(hantv); // Cho JDateChooser hoặc tương tự
                        String strHanTV = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(hantv);
                        txtThoiHanTV.setText(strHanTV); // Set text dạng chuỗi
                    } else {
                        dcHanTV.setDate(null);
                        txtThoiHanTV.setText("");
                    }

                    txtCCCD.setText(rs.getString("CCCD"));
                    // Ngày sinh
                    java.util.Date ns = rs.getDate("NgaySinh");
                    // Set cho txtNgaySinhView
                    if (ns != null) {
                        String nsStr = new java.text.SimpleDateFormat("dd-MM-yyyy").format(ns);
                        txtNgaySinhView.setText(nsStr);
                    } else {
                        txtNgaySinhView.setText("");
                    }
                    // Set cho dcNgaySinhEdit
                    dcNgaySinhEdit.setDate(ns);
                    // Giới tính
                    String gtx = rs.getString("GioiTinh");
                    if ("M".equals(gtx)) gtx = "Nam"; 
                    else if ("F".equals(gtx)) gtx = "Nữ";
                    // Set cho txtGioiTinhView
                    txtGioiTinhView.setText(gtx != null ? gtx : "");
                    // Set cho cmbGioiTinhEdit
                    cmbGioiTinhEdit.setSelectedItem(gtx);
                    // Set cho SDT, Email, DiaChi, Luong, PhucLoi
                    txtSDT.setText(rs.getString("SDT"));
                    txtEmail.setText(rs.getString("Email"));
                    txtQuocTich.setText(rs.getString("QuocTich"));
                    // Set cho cmbChucVuEdit
                    String cv = rs.getString("LoaiKhachHang");
                    cmbLoaiKH.setSelectedItem(cv);
                    txtDiemThuong.setText(rs.getBigDecimal("DiemThuong").toPlainString());
                    txtHangTV.setText(rs.getString("HangThanhVien"));
                    // Ngày vào làm

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải chi tiết khách hàng: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Refresh dữ liệu trong form
    private void clearFormInput() {
        txtMa.setText("");
        txtHoTen.setText("");
        txtCCCD.setText("");
        txtNgaySinhView.setText("");
        txtGioiTinhView.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtQuocTich.setText("");

        txtDiemThuong.setText("");
        txtHangTV.setText("");
        txtThoiHanTV.setText("");

        dcNgaySinhEdit.setDate(null);
        dcHanTV.setDate(new Date());

        cmbGioiTinhEdit.setSelectedIndex(-1); // Không chọn gì
        cmbLoaiKH.setSelectedIndex(-1);
    }
    
    // Phương thức áp dụng placeholder cho tất cả các textfield nhập thông tin
    private void applyPlaceholders() {
        if (currentMode == Mode.ADD || currentMode == Mode.NONE) {
            if (txtMa.getText().trim().isEmpty())
                setPlaceholder(txtMa, "Mã khách hàng");

            if (txtHoTen.getText().trim().isEmpty())
                setPlaceholder(txtHoTen, "Nhập họ tên");

            if (txtCCCD.getText().trim().isEmpty())
                setPlaceholder(txtCCCD, "Nhập CCCD");

            if (txtNgaySinhView.getText().trim().isEmpty())
                setPlaceholder(txtNgaySinhView, "Nhập ngày sinh");

            if (txtGioiTinhView.getText().trim().isEmpty())
                setPlaceholder(txtGioiTinhView, "Nhập giới tính");

            if (txtSDT.getText().trim().isEmpty())
                setPlaceholder(txtSDT, "Nhập số điện thoại");

            if (txtEmail.getText().trim().isEmpty())
                setPlaceholder(txtEmail, "Nhập email");

            if (txtQuocTich.getText().trim().isEmpty())
                setPlaceholder(txtQuocTich, "Nhập quốc tịch");


            if (txtDiemThuong.getText().trim().isEmpty())
                setPlaceholder(txtDiemThuong, "Nhập điểm thưởng");

            if (txtHangTV.getText().trim().isEmpty())
                setPlaceholder(txtHangTV, "Nhập hạng thành viên");

            if (txtThoiHanTV.getText().trim().isEmpty())
                setPlaceholder(txtThoiHanTV, "Nhập thời hạn thành viên");
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
    
//    // Đặt một JComboBox thành placeholder
//    private void setPlaceholder(JComboBox<String> comboBox, String placeholderText) {
//    if (comboBox.getItemCount() == 0 || !comboBox.getItemAt(0).equals(placeholderText)) {
//        comboBox.insertItemAt(placeholderText, 0);
//    }
//    comboBox.setSelectedIndex(0);
//    comboBox.setForeground(Color.GRAY);
//
//    comboBox.addActionListener(e -> {
//        if (comboBox.getSelectedIndex() != 0) {
//            comboBox.setForeground(Color.BLACK);
//        } else {
//            comboBox.setForeground(Color.GRAY);
//        }
//    });
//    }
//    
//    // Đặt một JDateChooser thành placeholder
//    private void setPlaceholder(JDateChooser dateChooser, String placeholderText) {
//        JTextField editor = ((JTextField) dateChooser.getDateEditor().getUiComponent());
//        editor.setText(placeholderText);
//        editor.setForeground(Color.GRAY);
//
//        editor.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                if (editor.getText().equals(placeholderText)) {
//                    editor.setText("");
//                    editor.setForeground(Color.BLACK);
//                }
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                if (editor.getText().isEmpty()) {
//                    editor.setText(placeholderText);
//                    editor.setForeground(Color.GRAY);
//                    dateChooser.setDate(null); // clear actual value
//                }
//            }
//        });
//    }
    
    private void applyNormalStyle() {
        Font normalFont = new Font("UTM Centur", Font.PLAIN, 18);
        Color normalColor = Color.BLACK;
        
        txtHoTen.setFont(normalFont);
        txtHoTen.setForeground(normalColor);
        txtCCCD.setFont(normalFont);
        txtCCCD.setForeground(normalColor);
        txtNgaySinhView.setFont(normalFont);
        txtNgaySinhView.setForeground(normalColor);
        txtGioiTinhView.setFont(normalFont);
        txtGioiTinhView.setForeground(normalColor);
        txtSDT.setFont(normalFont);
        txtSDT.setForeground(normalColor);
        txtEmail.setFont(normalFont);
        txtEmail.setForeground(normalColor);
        txtQuocTich.setFont(normalFont);
        txtQuocTich.setForeground(normalColor);
        txtDiemThuong.setFont(normalFont);
        txtDiemThuong.setForeground(normalColor);
        txtHangTV.setFont(normalFont);
        txtHangTV.setForeground(normalColor);
        txtThoiHanTV.setFont(normalFont);
        txtThoiHanTV.setForeground(normalColor);
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
        cardNgaySinh.show(pnlNgaySinhCards, statusCard);
        cardGioiTinh.show(pnlGioiTinhCards, statusCard);
        cardChucVu.show(pnlChucVuCards, statusCard);
        cardNgayVaoLam.show(pnlNgayVaoLamCards, statusCard);
   
        // Xử lý Mã nhân viên không được thêm, sửa
        if (editable == true) txtMa.setEnabled(false);
        
        // JTextField
        txtMa.setEditable(false);
        txtHoTen.setEditable(editable);
        txtCCCD.setEditable(editable);
        txtNgaySinhView.setEditable(editable);
        txtGioiTinhView.setEditable(editable);
        txtSDT.setEditable(editable);
        txtEmail.setEditable(editable);
        txtQuocTich.setEditable(editable);
        txtDiemThuong.setEditable(editable);
        txtHangTV.setEditable(editable);
        txtThoiHanTV.setEditable(editable);

        // JDateChooser
        dcNgaySinhEdit.setEnabled(editable);
        dcHanTV.setEnabled(editable);

        // JComboBox (nếu có)
        cmbGioiTinhEdit.setEnabled(editable);
        cmbLoaiKH.setEnabled(editable);
    }
  
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThaoTac = new javax.swing.JPanel();
        lblThaoTac = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblCCCD = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        lblGioiTinh = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        lblChucVu = new javax.swing.JLabel();
        lblLuong = new javax.swing.JLabel();
        lblPhucLoi = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        txtQuocTich = new javax.swing.JTextField();
        txtDiemThuong = new javax.swing.JTextField();
        lblNgayVaoLam = new javax.swing.JLabel();
        txtHangTV = new javax.swing.JTextField();
        pnlNgayVaoLamCards = new javax.swing.JPanel();
        txtThoiHanTV = new javax.swing.JTextField();
        dcHanTV = new com.toedter.calendar.JDateChooser();
        pnlGioiTinhCards = new javax.swing.JPanel();
        cmbGioiTinhEdit = new javax.swing.JComboBox<>();
        txtGioiTinhView = new javax.swing.JTextField();
        pnlNgaySinhCards = new javax.swing.JPanel();
        dcNgaySinhEdit = new com.toedter.calendar.JDateChooser();
        txtNgaySinhView = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        pnlChucVuCards = new javax.swing.JPanel();
        btnHuy = new javax.swing.JButton();
        btnXacNhan = new javax.swing.JButton();
        lblStarHoTen = new javax.swing.JLabel();
        lblStarEmail = new javax.swing.JLabel();
        lblStarCCCD = new javax.swing.JLabel();
        lblStarLuongCoBan = new javax.swing.JLabel();
        lblStarNgaySinh = new javax.swing.JLabel();
        lblStarNgayVaoLam = new javax.swing.JLabel();
        lblStarChucVu = new javax.swing.JLabel();
        lblStarDiaChi = new javax.swing.JLabel();
        cmbLoaiKH = new javax.swing.JComboBox<>();
        pnlDanhSach = new javax.swing.JPanel();
        lblDanhSach = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        spDanhSach = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();

        pnlThaoTac.setBackground(java.awt.SystemColor.control);
        pnlThaoTac.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlThaoTac.setPreferredSize(new java.awt.Dimension(632, 824));

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thêm khách hàng");

        lblMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMa.setText("Mã khách hàng");

        lblHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblHoTen.setText("Họ tên");

        lblCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblCCCD.setText("Số  CCCD");

        lblNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgaySinh.setText("Ngày sinh");

        txtHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHoTen.setAutoscrolls(false);
        txtHoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenActionPerformed(evt);
            }
        });

        lblGioiTinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblGioiTinh.setText("Giới tính");

        lblSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblSDT.setText("Số điện thoại");

        lblEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblEmail.setText("Email");

        lblDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblDiaChi.setText("Quốc tịch");

        lblChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblChucVu.setText("Loại khách hàng");

        lblLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLuong.setText("Điểm thưởng");

        lblPhucLoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblPhucLoi.setText("Hạng thành viên");

        txtMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMa.setAutoscrolls(false);
        txtMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaActionPerformed(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtEmail.setAutoscrolls(false);
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });

        txtQuocTich.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtQuocTich.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuocTichActionPerformed(evt);
            }
        });

        txtDiemThuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtDiemThuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiemThuongActionPerformed(evt);
            }
        });

        lblNgayVaoLam.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgayVaoLam.setText("Thời hạn TV");

        txtHangTV.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHangTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHangTVActionPerformed(evt);
            }
        });

        pnlNgayVaoLamCards.setLayout(new java.awt.CardLayout());

        txtThoiHanTV.setEditable(false);
        txtThoiHanTV.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtThoiHanTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtThoiHanTVActionPerformed(evt);
            }
        });
        pnlNgayVaoLamCards.add(txtThoiHanTV, "VIEW");

        dcHanTV.setDateFormatString("dd/MM/yyyy");
        dcHanTV.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        pnlNgayVaoLamCards.add(dcHanTV, "EDIT");

        pnlGioiTinhCards.setLayout(new java.awt.CardLayout());

        cmbGioiTinhEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbGioiTinhEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));
        cmbGioiTinhEdit.setFocusable(false);
        cmbGioiTinhEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGioiTinhEditActionPerformed(evt);
            }
        });
        pnlGioiTinhCards.add(cmbGioiTinhEdit, "EDIT");

        txtGioiTinhView.setEditable(false);
        txtGioiTinhView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtGioiTinhView.setAutoscrolls(false);
        txtGioiTinhView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGioiTinhViewActionPerformed(evt);
            }
        });
        pnlGioiTinhCards.add(txtGioiTinhView, "VIEW");

        pnlNgaySinhCards.setLayout(new java.awt.CardLayout());

        dcNgaySinhEdit.setDateFormatString("dd/MM/yyyy");
        dcNgaySinhEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        pnlNgaySinhCards.add(dcNgaySinhEdit, "EDIT");

        txtNgaySinhView.setEditable(false);
        txtNgaySinhView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtNgaySinhView.setAutoscrolls(false);
        txtNgaySinhView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgaySinhViewActionPerformed(evt);
            }
        });
        pnlNgaySinhCards.add(txtNgaySinhView, "VIEW");

        txtSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtSDT.setAutoscrolls(false);
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        pnlChucVuCards.setLayout(new java.awt.CardLayout());

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

        lblStarHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarHoTen.setForeground(new java.awt.Color(255, 0, 0));
        lblStarHoTen.setText("*");
        lblStarHoTen.setAutoscrolls(true);

        lblStarEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarEmail.setForeground(new java.awt.Color(255, 0, 0));
        lblStarEmail.setText("*");
        lblStarEmail.setAutoscrolls(true);

        lblStarCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarCCCD.setForeground(new java.awt.Color(255, 0, 0));
        lblStarCCCD.setText("*");
        lblStarCCCD.setAutoscrolls(true);

        lblStarLuongCoBan.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarLuongCoBan.setForeground(new java.awt.Color(255, 0, 0));
        lblStarLuongCoBan.setText("*");
        lblStarLuongCoBan.setAutoscrolls(true);

        lblStarNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarNgaySinh.setForeground(new java.awt.Color(255, 0, 0));
        lblStarNgaySinh.setText("*");
        lblStarNgaySinh.setAutoscrolls(true);

        lblStarNgayVaoLam.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarNgayVaoLam.setForeground(new java.awt.Color(255, 0, 0));
        lblStarNgayVaoLam.setText("*");
        lblStarNgayVaoLam.setAutoscrolls(true);

        lblStarChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarChucVu.setForeground(new java.awt.Color(255, 0, 0));
        lblStarChucVu.setText("*");
        lblStarChucVu.setAutoscrolls(true);

        lblStarDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarDiaChi.setForeground(new java.awt.Color(255, 0, 0));
        lblStarDiaChi.setText("*");
        lblStarDiaChi.setAutoscrolls(true);

        cmbLoaiKH.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbLoaiKH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cá nhân", "Doanh nghiệp" }));

        javax.swing.GroupLayout pnlThaoTacLayout = new javax.swing.GroupLayout(pnlThaoTac);
        pnlThaoTac.setLayout(pnlThaoTacLayout);
        pnlThaoTacLayout.setHorizontalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(lblThaoTac))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblStarNgaySinh)
                        .addGap(108, 108, 108)
                        .addComponent(pnlNgaySinhCards, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(pnlGioiTinhCards, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblStarEmail)
                        .addGap(138, 138, 138)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addComponent(lblLuong)
                                .addGap(11, 11, 11)
                                .addComponent(lblStarLuongCoBan, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addComponent(lblChucVu)
                                .addGap(3, 3, 3)
                                .addComponent(lblStarChucVu))
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addComponent(lblDiaChi)
                                .addGap(12, 12, 12)
                                .addComponent(lblStarDiaChi)))
                        .addGap(57, 57, 57)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmbLoaiKH, javax.swing.GroupLayout.Alignment.TRAILING, 0, 307, Short.MAX_VALUE)
                                .addComponent(txtQuocTich))
                            .addComponent(txtDiemThuong, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addComponent(pnlChucVuCards, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addComponent(lblStarNgayVaoLam))
                            .addComponent(lblNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPhucLoi))
                        .addGap(67, 67, 67)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHangTV, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlNgayVaoLamCards, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addComponent(lblCCCD)
                                .addGap(6, 6, 6)
                                .addComponent(lblStarCCCD))
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addComponent(lblHoTen)
                                .addGap(6, 6, 6)
                                .addComponent(lblStarHoTen)))
                        .addGap(111, 111, 111)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGap(211, 211, 211)
                                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThaoTacLayout.setVerticalGroup(
            pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThaoTacLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblThaoTac)
                .addGap(17, 17, 17)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMa)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblHoTen))
                    .addComponent(lblStarHoTen)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblCCCD))
                    .addComponent(lblStarCCCD)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStarNgaySinh)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNgaySinh)
                            .addComponent(pnlNgaySinhCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(12, 12, 12)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGioiTinh)
                    .addComponent(pnlGioiTinhCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblSDT))
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblStarEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblDiaChi))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblStarDiaChi))
                    .addComponent(txtQuocTich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStarChucVu)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChucVu)
                            .addComponent(pnlChucVuCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblLuong))
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblStarLuongCoBan))
                    .addComponent(txtDiemThuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblPhucLoi)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThaoTacLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHangTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStarNgayVaoLam)
                    .addGroup(pnlThaoTacLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNgayVaoLam)
                            .addComponent(pnlNgayVaoLamCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(114, 114, 114)
                .addGroup(pnlThaoTacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuy)
                    .addComponent(btnXacNhan))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pnlDanhSach.setBackground(java.awt.SystemColor.control);
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(632, 824));

        lblDanhSach.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblDanhSach.setText("Danh sách khách hàng");

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

        tblDanhSach.setAutoCreateRowSorter(true);
        tblDanhSach.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã KH", "Họ tên", "CCCD", "Giới tính", "Quốc tịch", "Loại KH", "Hạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSach.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDanhSach.setColumnSelectionAllowed(true);
        tblDanhSach.setRowHeight(30);
        spDanhSach.setViewportView(tblDanhSach);
        tblDanhSach.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblDanhSach.getColumnModel().getColumnCount() > 0) {
            tblDanhSach.getColumnModel().getColumn(0).setMinWidth(80);
            tblDanhSach.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblDanhSach.getColumnModel().getColumn(0).setMaxWidth(80);
            tblDanhSach.getColumnModel().getColumn(1).setMinWidth(80);
            tblDanhSach.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblDanhSach.getColumnModel().getColumn(1).setMaxWidth(80);
            tblDanhSach.getColumnModel().getColumn(1).setHeaderValue(lblHoTen.getText());
        }

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

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addGap(0, 66, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnTimKiem)
                .addGap(56, 56, 56))
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(lblDanhSach)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(btnThem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXoa)
                .addGap(76, 76, 76)
                .addComponent(btnSua)
                .addGap(58, 58, 58))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(spDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblDanhSach)
                .addGap(46, 46, 46)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTimKiem)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(spDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnXoa)
                    .addComponent(btnSua))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pnlDanhSachLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnTimKiem, txtTimKiem});

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlThaoTac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Tìm kiếm bằng Mã nhân viên
    private void searchByMaNhanVien() {
        String maKH = txtTimKiem.getText().trim();
        if (maKH.isEmpty() || maKH.equals("Nhập mã khách hàng hoặc tên khách hàng")) {
            loadKhachHangToTable();// Hiển thị toàn bộ nếu không nhập hoặc chỉ đang hiển thị placeholder
            return;
        }
        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng cần tìm!");
            return;
        }
        
        String sql = "SELECT MaKhachHang, HoTen, CCCD, GioiTinh, QuocTich, LoaiKhachHang, HangThanhVien FROM KHACH_HANG WHERE UPPER(MaKhachHang) = UPPER(?)";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tblDanhSach.getModel();

            String gt = rs.getString("GioiTinh");
            if (gt.equals('M')) gt = "Nam";
            else gt = "Nữ";
            boolean found = false;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaKhachHang"),
                    rs.getString("HoTen"),
                    rs.getString("CCCD"),
                    gt,
                    rs.getString("QuocTich"),
                    rs.getString("LoaiKhachHang"),
                    rs.getString("HangThanhVien")
                });
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Gợi ý tìm kiếm
    private void addAutoSuggestToTimKiem() {
        listModel = new DefaultListModel<>();
        suggestionList = new JList<>(listModel);
        suggestionList.setFont(txtTimKiem.getFont());
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tạo cửa sổ gợi ý (JWindow)
        suggestionWindow = new JWindow(SwingUtilities.getWindowAncestor(this));
        JScrollPane scrollPane = new JScrollPane(suggestionList);
        suggestionWindow.getContentPane().add(scrollPane);

        // Xử lý click chuột vào gợi ý
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectSuggestion();
                }
            }
        });

        // Xử lý phím Enter và mũi tên
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionWindow.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        int index = suggestionList.getSelectedIndex();
                        if (index < listModel.size() - 1) {
                            suggestionList.setSelectedIndex(index + 1);
                            suggestionList.ensureIndexIsVisible(index + 1);
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        int index = suggestionList.getSelectedIndex();
                        if (index > 0) {
                            suggestionList.setSelectedIndex(index - 1);
                            suggestionList.ensureIndexIsVisible(index - 1);
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        selectSuggestion();
                        e.consume();
                    }
                }
            }
        });

        // Theo dõi khi người dùng gõ
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                showSuggestions();
            }

            public void removeUpdate(DocumentEvent e) {
                showSuggestions();
            }

            public void changedUpdate(DocumentEvent e) {
                showSuggestions();
            }
        });

        // Ẩn gợi ý khi mất focus
        txtTimKiem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> suggestionWindow.setVisible(false));
            }
        });
    }

    // Hàm xử lý chọn gợi ý
    private void selectSuggestion() {
        String selected = suggestionList.getSelectedValue();
        if (selected != null && selected.contains(" - ")) {
            String maKH = selected.split(" - ")[0].trim();
            txtTimKiem.setText(maKH);
            suggestionWindow.setVisible(false);
            btnTimKiem.doClick(); // Gọi tìm kiếm
        }
    }

    // Hiển thị gợi ý từ CSDL
    private void showSuggestions() {
        String text = txtTimKiem.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            suggestionWindow.setVisible(false);
            return;
        }

        listModel.clear();
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT MaKhachHang, HoTen FROM KHACH_HANG " +
                 "WHERE LOWER(MaKhachHang) LIKE ? OR LOWER(HoTen) LIKE ?")) {

            ps.setString(1, text + "%");
            ps.setString(2, "%" + text + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maKH = rs.getString("MaKhachHang");
                String hoTen = rs.getString("HoTen");
                listModel.addElement(maKH + " - " + hoTen);
            }

            if (listModel.getSize() > 0) {
                suggestionList.setSelectedIndex(0);
                Point location = txtTimKiem.getLocationOnScreen();
                suggestionWindow.setBounds(location.x, location.y + txtTimKiem.getHeight(), txtTimKiem.getWidth(), 120);
                suggestionWindow.setVisible(true);
            } else {
                suggestionWindow.setVisible(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void showRequiredIndicators(boolean show) {
        // Đặt visibility cho tất cả các JLabel dấu * đỏ
        lblStarHoTen.setVisible(show);
        lblStarCCCD.setVisible(show);
        lblStarNgaySinh.setVisible(show);
        lblStarEmail.setVisible(show);
        lblStarDiaChi.setVisible(show);
        lblStarChucVu.setVisible(show);
        lblStarLuongCoBan.setVisible(show);
        lblStarNgayVaoLam.setVisible(show);
    }
        
    
    // Update nhân viên trả về thành công/ thất bại
    private boolean updateKhachHang() {
        // 0. Lấy MaNhanVien để cập nhật (bắt buộc)
        String maKhachHang = txtMa.getText().trim();
        if (maKhachHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống để cập nhật.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 1. Lấy dữ liệu từ các component trên form
        String hoTen = getTextFieldValue(txtHoTen, "Nhập họ tên");
        String cccd = getTextFieldValue(txtCCCD, "Nhập CCCD");

        java.sql.Date ngaySinh = (dcNgaySinhEdit.getDate() != null) ? new java.sql.Date(dcNgaySinhEdit.getDate().getTime()) : null;

        String gioiTinhSelected = getComboBoxSelectedValue(cmbGioiTinhEdit);
        String gioiTinhDBValue = null; // Giá trị sẽ được lưu vào DB (M, F, hoặc NULL)
        if (gioiTinhSelected != null) {
            if ("Nam".equalsIgnoreCase(gioiTinhSelected)) {
                gioiTinhDBValue = "M";
            } else if ("Nữ".equalsIgnoreCase(gioiTinhSelected)) {
                gioiTinhDBValue = "F";
            }
            // Nếu giá trị khác "Nam" hoặc "Nữ", gioiTinhDBValue sẽ là null
        }

        String sdt = getTextFieldValue(txtSDT, "Nhập số điện thoại"); 
        String email = getTextFieldValue(txtEmail, "Nhập email"); 
        String quocTich = getTextFieldValue(txtQuocTich, "Nhập quốc tịch"); 

        String loaiKH = getComboBoxSelectedValue(cmbLoaiKH);

        BigDecimal diemThuong = null;
        String diemThuongStr = getTextFieldValue(txtDiemThuong, "Nhập điểm thưởng");
        if (!diemThuongStr.isEmpty()) {
            try {
                diemThuong = new BigDecimal(diemThuongStr);
                if (diemThuong.compareTo(BigDecimal.ZERO) <= 0) { // Lương phải là số dương
                    JOptionPane.showMessageDialog(this, "Điểm thưởng phải là một số dương.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Điểm thưởng không hợp lệ. Vui lòng nhập số.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        String hangTV = getTextFieldValue(txtHangTV, "Nhập hạng thành viên");
        java.sql.Timestamp ngayHanTV = (dcHanTV.getDate() != null) ? new java.sql.Timestamp(dcHanTV.getDate().getTime()) : null;

        // --- Kiểm tra các trường NOT NULL theo DB schema trước khi thực hiện SQL ---
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cccd.isEmpty()) { // Assuming CCCD "NOT NUL" means "NOT NULL"
            JOptionPane.showMessageDialog(this, "Số CCCD không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // GioiTinh CHAR(1) is nullable in DB.
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (quocTich.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quốc tịch không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (loaiKH == null) { // chucVu is null if not selected or selected item was empty
            JOptionPane.showMessageDialog(this, "Loại khách hàng không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (diemThuong == null) { // luongCoBan is null if text field was empty or conversion failed/was non-positive
            JOptionPane.showMessageDialog(this, "Điểm thưởng không được để trống và phải là một số dương hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ngayHanTV == null) {
            JOptionPane.showMessageDialog(this, "Hạn thành viên không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // --- Hết phần kiểm tra NOT NULL ---
        String sql = 
            "UPDATE KHACH_HANG SET " +
            "HoTen      = ?, " +  // 1
            "CCCD       = ?, " +  // 2
            "NgaySinh   = ?, " +  // 3
            "GioiTinh   = ?, " +  // 4
            "SDT        = ?, " +  // 5
            "Email      = ?, " +  // 6
            "QuocTich     = ?, " +  // 7
            "LoaiKhachHang    = ?, " +  // 8
            "DiemThuong = ?, " +  // 9
            "HangThanhVien    = ?, " +  // 10
            "ThoiHanTV = ? " +   // 11
            "WHERE MaKhachHang = ?"; // 12

        try (Connection conn = ConnectionUtils.getMyConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set các tham số cho PreparedStatement
            ps.setString(1, hoTen);         // HoTen (NOT NULL, validated)
            ps.setString(2, cccd);          // CCCD (NOT NULL, validated)
            ps.setDate(3, ngaySinh);        // NgaySinh (NOT NULL, validated)

            if (gioiTinhDBValue != null) {  // GioiTinh (Nullable CHAR(1))
                ps.setString(4, gioiTinhDBValue);
            } else {
                ps.setNull(4, Types.CHAR);
            }

            // 5. SDT
            if (!sdt.isEmpty()) {           // SDT (Nullable VARCHAR2)
                ps.setString(5, sdt);
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            // 6. Email
            ps.setString(6, email);         // Email (NOT NULL, validated)

            // 7. Địa chỉ
            ps.setString(7, quocTich);        // DiaChi (NOT NULL, validated)

            // 8. Chức vụ
            ps.setString(8, loaiKH);        // ChucVu (NOT NULL, validated)

            // 9. Lương cơ bản
            ps.setBigDecimal(9, diemThuong); // LuongCoBan (NOT NULL, validated)

            // 10. Phúc lợi
            if (!hangTV.isEmpty()) {       // PhucLoi (Nullable NVARCHAR2)
                ps.setString(10, hangTV);
            } else {
                ps.setNull(10, Types.NVARCHAR);
            }

            // 11. Ngày vào làm
            ps.setTimestamp(11, ngayHanTV); // NgayVaoLam (NOT NULL, validated)

            // 12. WHERE MaNhanVien
            ps.setString(12, maKhachHang);    // MaNhanVien for WHERE clause (NOT NULL, validated)

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công.");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng để cập nhật.", 
                                              "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
            loadKhachHangToTable();

        } catch (SQLException ex) {
            handleUpdateNhanVienError(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định:\n" + ex.getMessage(),
                                          "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    /*
     * Xóa nhân viên khỏi bảng NHAN_VIEN và đồng thời xóa User/Account liên quan.
     *
     * @param maNV Mã nhân viên cần xóa.
     * @return true nếu xóa nhân viên và xử lý tài khoản thành công, false nếu có lỗi.
     */
    private boolean deleteNhanVien(String maNV) {
        // Bước 1: Xóa User và Account liên kết với nhân viên.
        // Hàm xoaUserVaAccountChoNhanVien sẽ tự xử lý giao dịch và thông báo (JOptionPane)
        // cho phần việc của nó.
        boolean taiKhoanDaDuocXuLy = nhanVienAccountManager.xoaUserVaAccountChoNhanVien(maNV);

        if (!taiKhoanDaDuocXuLy) {
            // Nếu xóa tài khoản không thành công, xoaUserVaAccountChoNhanVien đã hiển thị lỗi.
            // Không cần hiển thị thêm JOptionPane ở đây để tránh lặp thông báo lỗi.
            // Chỉ cần ghi log hoặc dừng tiến trình.
            System.err.println("Không thể xóa nhân viên '" + maNV + "' do có lỗi trong quá trình xử lý tài khoản liên quan.");
            // Bạn có thể muốn một thông báo tổng quát ở đây nếu cần:
            // JOptionPane.showMessageDialog(this, "Xóa nhân viên '" + maNV + "' thất bại do không thể xử lý tài khoản liên quan.", "Lỗi Xóa Nhân Viên", JOptionPane.ERROR_MESSAGE);
            return false; // Dừng lại và không xóa bản ghi NHAN_VIEN
        }

        // Bước 2: Nếu việc xử lý tài khoản thành công (tài khoản đã được xóa
        // hoặc nhân viên không có tài khoản), thì tiến hành xóa bản ghi NHAN_VIEN.
        String sqlDeleteNhanVien = "DELETE FROM NHAN_VIEN WHERE MaNhanVien = ?";
        try (Connection conn = ConnectionUtils.getMyConnection(); // Mở connection mới cho thao tác này
             PreparedStatement ps = conn.prepareStatement(sqlDeleteNhanVien)) {

            ps.setString(1, maNV);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Hàm xoaUserVaAccountChoNhanVien đã thông báo kết quả xử lý tài khoản, ví dụ:
                // - "Đã xóa thành công tài khoản và user liên kết với nhân viên: [maNV]"
                // - HOẶC "Nhân viên '[maNV]' không có tài khoản nào được liên kết."
                // Vì vậy, thông báo ở đây nên tập trung vào việc xóa thành công bản ghi NHAN_VIEN.
                JOptionPane.showMessageDialog(null, 
                    "Đã xóa thành công thông tin khách hàng '" + maNV + "'.",
                    "Xóa Nhân Viên Thành Công",
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                // Trường hợp này có thể xảy ra nếu:
                // 1. Nhân viên không có tài khoản (xoaUserVaAccountChoNhanVien trả về true).
                // 2. Và bản ghi NHAN_VIEN cũng không tồn tại (có thể đã bị xóa bởi tiến trình khác
                //    sau khi xoaUserVaAccountChoNhanVien kiểm tra sự tồn tại của nhân viên ban đầu
                //    để lấy ACCOUNT_ID, hoặc nếu MaNhanVien không đúng nhưng vẫn qua được bước 1
                //    do không có account_id).
                // Hoặc nếu xoaUserVaAccountChoNhanVien trả về true (do NV không có tài khoản),
                // nhưng MaNhanVien không tồn tại trong bảng NHAN_VIEN.
                JOptionPane.showMessageDialog(null, 
                    "Không tìm thấy nhân viên '" + maNV + "' trong cơ sở dữ liệu để xóa (hoặc đã được xóa trước đó). Các tài khoản liên quan (nếu có) đã được xử lý.",
                    "Thông Báo",
                    JOptionPane.WARNING_MESSAGE);
                // Theo logic hàm gốc của bạn, nếu không có dòng nào bị ảnh hưởng thì là thất bại.
                return false;
            }
        } catch (SQLException exSQL) {
            exSQL.printStackTrace();
            JOptionPane.showMessageDialog(null, // Thay 'null' bằng 'this'
                "Lỗi CSDL khi xóa thông tin khách hàng '" + maNV + "': " + exSQL.getMessage(),
                "Lỗi CSDL",
                JOptionPane.ERROR_MESSAGE);
            // QUAN TRỌNG: Ở điểm này, tài khoản của nhân viên CÓ THỂ đã được xóa thành công ở Bước 1.
            // Nhưng việc xóa bản ghi nhân viên khỏi bảng NHAN_VIEN đã thất bại.
            // Đây là một trạng thái không nhất quán tiềm ẩn. Cần ghi log cẩn thận.
            System.err.println("CẢNH BÁO NGHIÊM TRỌNG: Tài khoản của khách hàng '" + maNV +
                               "' có thể đã được xóa, nhưng xóa bản ghi NHAN_VIEN khỏi bảng NHAN_VIEN thất bại! " +
                               "Cần kiểm tra dữ liệu thủ công.");
        } catch (Exception ex) { // Bắt các Exception chung khác
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, // Thay 'null' bằng 'this'
                "Lỗi không xác định khi xóa nhân viên '" + maNV + "': " + ex.getMessage(),
                "Lỗi Không Xác Định",
                JOptionPane.ERROR_MESSAGE);
        }
        return false; // Trả về false nếu có lỗi ở Bước 2 hoặc affectedRows == 0
    }
    
    // Handle lỗi insert nhân viên
    private void handleInsertNhanVienError(SQLException ex) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage().toLowerCase();

        // 1. Trigger nghiệp vụ
        if (handleTriggerErrors(code)) return;

        // 2. Ràng buộc NOT NULL
        if (handleNullConstraint(msg)) return;

        // 3. Ràng buộc CHECK & UNIQUE
        if (handleCheckAndUnique(msg)) return;

        // 4. Lỗi khác
        showError(ex);
    }

    // Handle lỗi update nhân viên
    private void handleUpdateNhanVienError(SQLException ex) {
        int code = ex.getErrorCode();
        String msg = ex.getMessage().toLowerCase();

        // 1. Trigger nghiệp vụ
        if (handleTriggerErrors(code)) return;

        // 2. Ràng buộc NOT NULL
        if (handleNullConstraint(msg)) return;

        // 3. Ràng buộc CHECK & UNIQUE
        if (handleCheckAndUnique(msg)) return;

        // 4. Lỗi khác
        showError(ex);
    }

    // Phương thức handle lỗi Trigger tái sử dụng cho cả insert & update 
    private boolean handleTriggerErrors(int code) {
        switch (code) {
            case 20001:
                showWarning("Nhân viên phải trên 18 tuổi!");
                return true;
//            case 20002:
//                showWarning("Phi công phải có bằng lái hợp lệ (PPL/CPL/HPL) và còn thời hạn!");
//                return true;
//            case 20003:
//                showWarning("Phi công chỉ có thể có bằng PPL, CPL hoặc HPL!");
//                return true;
//            case 20004:
//                showWarning("Tiếp viên phải có chứng chỉ đào tạo hợp lệ và còn thời hạn!");
//                return true;
//            case 20005:
//                showWarning("Tiếp viên phải có chứng chỉ đào tạo hợp lệ!");
//                return true;
            default:
                return false;
        }
    }

    // Phương thức handle lỗi ràng buộc NULL
    private boolean handleNullConstraint(String msg) {
        if (!(msg.contains("cannot insert null into") 
        || (msg.contains("cannot update") && msg.contains("null")))) {
            return false;
        }

        if (msg.contains("hoten"))       showWarning("Họ tên không được để trống.");
        else if (msg.contains("ngaysinh"))     showWarning("Ngày sinh không được để trống.");
        else if (msg.contains("chucvu"))       showWarning("Chức vụ không được để trống.");
        else if (msg.contains("luongcoban"))  showWarning("Lương cơ bản không được để trống.");
        else if (msg.contains("ngayvaolam"))  showWarning("Ngày vào làm không được để trống.");
        else                                  showWarning("Một trường bắt buộc đang bị để trống.");
        return true;
    }

    // Phương thức handle lỗi Unique, Check
    private boolean handleCheckAndUnique(String msg) {
        if (msg.contains("check_nhanvien_chucvu")) {
            showWarning("Chức vụ không hợp lệ: chỉ có Phi công, Tiếp viên, Kỹ thuật viên, Nhân viên bảo vệ, Nhân viên thủ tục, Quản lý.");
            return true;
        }
        if (msg.contains("chk_nhanvien_gioitinh")) {
            showWarning("Giới tính không hợp lệ! Chỉ được chọn 'M' hoặc 'F'.");
            return true;
        }
        if (msg.contains("unique_nhanvien_cccd")) {
            showWarning("CCCD đã tồn tại.");
            return true;
        }
        if (msg.contains("unique_nhanvien_sdt")) {
            showWarning("Số điện thoại đã tồn tại.");
            return true;
        }
        if (msg.contains("unique_nhanvien_email")) {
            showWarning("Email đã tồn tại.");
            return true;
        }
        if ((msg.contains("duplicate") && msg.contains("ma_nhanvien"))
          || (msg.contains("unique") && msg.contains("ma_nhanvien"))) {
            showWarning("Mã nhân viên đã tồn tại.");
            return true;
        }
        return false;
    }

    // Show hộp thoại cảnh báo
    private void showWarning(String text) {
        JOptionPane.showMessageDialog(this, text, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
    }

    // Show hộp thoại lỗi
    private void showError(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Lỗi khi xử lý nhân viên:\n" + ex.getMessage(), 
            "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
  
    // Hàm setMode
    private void setMode(Mode mode) {
        switch (mode) {
            case VIEW:
                // Chỉnh mode sang VIEW
                currentMode = Mode.VIEW;
                applyNormalStyle();
                // Đổi text lblThaoTac
                lblThaoTac.setText("Thông tin khách hàng");
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
                lblThaoTac.setText("Thêm nhân viên");
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
                lblThaoTac.setText("Sửa nhân viên");
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
                
                SwingUtilities.invokeLater(() -> {
                    txtHoTen.requestFocusInWindow();
                    txtHoTen.setCaretPosition(txtHoTen.getText().length());
                });
                break;
            case DELETE:
                // Chỉnh mode sang DELETE
                currentMode = Mode.DELETE;
                // Đổi text lblThaoTac
                lblThaoTac.setText("Xóa nhân viên");
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
                lblThaoTac.setText("Thông tin khách hàng");
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
 
    private String getTextFieldValue(JTextField textField, String placeholderText) {
        String text = textField.getText().trim();
        if (text.equals(placeholderText)) {
            return ""; // Trả về chuỗi rỗng nếu đó là placeholder
        }
        return text;
    }
    
    private String getComboBoxSelectedValue(JComboBox<String> comboBox) {
        Object selectedItem = comboBox.getSelectedItem();
        if (selectedItem == null) {
            return null;
        }
        String value = selectedItem.toString().trim();
        return value.isEmpty() ? null : value;
    }

        
    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = tblDanhSach.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.");
            return;
        }
        setMode(Mode.EDIT);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblDanhSach.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.");
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

    private void txtThoiHanTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtThoiHanTVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThoiHanTVActionPerformed

    private void txtMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaActionPerformed

    private void txtHoTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoTenActionPerformed

    private void txtCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCCCDActionPerformed

    private void txtNgaySinhViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgaySinhViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgaySinhViewActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtQuocTichActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuocTichActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuocTichActionPerformed

    private void txtDiemThuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiemThuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiemThuongActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        searchByMaNhanVien();
        setMode(Mode.NONE);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtHangTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHangTVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHangTVActionPerformed

    private void cmbGioiTinhEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGioiTinhEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGioiTinhEditActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSDTActionPerformed

    private void txtGioiTinhViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGioiTinhViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGioiTinhViewActionPerformed

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchByMaNhanVien();
        }
        setMode(Mode.NONE);
    }//GEN-LAST:event_txtTimKiemKeyPressed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        setMode(Mode.NONE);
        tblDanhSach.clearSelection();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
            switch (currentMode) {
//            case ADD:
//                if (JOptionPane.showConfirmDialog(this,
//                        "Bạn có chắc muốn thêm nhân viên này?",
//                        "Xác nhận thêm", JOptionPane.YES_NO_OPTION) 
//                    == JOptionPane.YES_OPTION) {
//                    
//                    boolean success = insertNhanVien(); // Trả về true nếu thêm thành công
//                    if (success) {
//                        loadNhanVienToTable();
//                        setMode(Mode.NONE); // chỉ trả về chế độ NONE nếu thêm thành công
//                    }
//                }
//                // nếu NO: không làm gì, vẫn ở chế độ ADD
//                break;

            case DELETE:
                String maDel = tblDanhSach.getValueAt(
                    tblDanhSach.getSelectedRow(), 0).toString();
                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn xóa nhân viên mã: " + maDel + "?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                    
                    boolean success = deleteNhanVien(maDel); 
                    if (success) {
                        loadKhachHangToTable();
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
                    boolean success = updateKhachHang(); 
                    if (success) {
                        loadKhachHangToTable();
                        setMode(Mode.NONE);
                    }
                }
                break;

            default:
                // không làm gì
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbGioiTinhEdit;
    private javax.swing.JComboBox<String> cmbLoaiKH;
    private com.toedter.calendar.JDateChooser dcHanTV;
    private com.toedter.calendar.JDateChooser dcNgaySinhEdit;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblDanhSach;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblLuong;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblNgayVaoLam;
    private javax.swing.JLabel lblPhucLoi;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblStarCCCD;
    private javax.swing.JLabel lblStarChucVu;
    private javax.swing.JLabel lblStarDiaChi;
    private javax.swing.JLabel lblStarEmail;
    private javax.swing.JLabel lblStarHoTen;
    private javax.swing.JLabel lblStarLuongCoBan;
    private javax.swing.JLabel lblStarNgaySinh;
    private javax.swing.JLabel lblStarNgayVaoLam;
    private javax.swing.JLabel lblThaoTac;
    private javax.swing.JPanel pnlChucVuCards;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlGioiTinhCards;
    private javax.swing.JPanel pnlNgaySinhCards;
    private javax.swing.JPanel pnlNgayVaoLamCards;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JScrollPane spDanhSach;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtDiemThuong;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGioiTinhView;
    private javax.swing.JTextField txtHangTV;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtNgaySinhView;
    private javax.swing.JTextField txtQuocTich;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtThoiHanTV;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
