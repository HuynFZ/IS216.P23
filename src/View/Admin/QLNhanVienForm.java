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


public class QLNhanVienForm extends javax.swing.JPanel {
    CardLayout cardNgaySinh, cardGioiTinh, cardChucVu, cardNgayVaoLam;
    private enum Mode { NONE, VIEW, ADD, DELETE, EDIT }
    private Mode currentMode = Mode.NONE;
    private JWindow suggestionWindow;
    private JList<String> suggestionList;
    private DefaultListModel<String> listModel;
    private final Set<JTextField> initializedPlaceholders = new HashSet<>();
    private NhanVienAccountManager nhanVienAccountManager = new NhanVienAccountManager();

    public QLNhanVienForm() {
        // Khởi tạo các components
        initComponents();
        // Khởi tạo các CardLayout
        initCardLayout();
        // Nạp dữ liệu lên bảng
        loadNhanVienToTable();
        // Đăng ký listener cho việc chọn dòng
        initSelectionListener();
        // Set chế độ NONE ban đầu
        setMode(Mode.NONE);
        // Set placeholder cho thanh tìm kiếm
        setPlaceholder(txtTimKiem, "Nhập mã nhân viên hoặc tên nhân viên");
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
            txtChucVuView.setText(tblDanhSach.getValueAt(row, 2).toString());
            txtNgayVaoLamView.setText(tblDanhSach.getValueAt(row, 3).toString());
            
            // Nạp thêm chi tiết từ database
            loadNhanVienDetails(maNV);
        });
    }
    
    // Tải dữ liệu nhân viên vào JTable
    private void loadNhanVienToTable() {
        DefaultTableModel model = (DefaultTableModel) tblDanhSach.getModel();
        model.setRowCount(0); // Xóa tất cả các dòng hiện có trong bảng
        
        String sql = "SELECT MaNhanVien, HoTen, ChucVu, NgayVaoLam FROM NHAN_VIEN";

        // Chuẩn bị formatter
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        try (Connection conn = ConnectionUtils.getMyConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Lấy timestamp
                java.sql.Timestamp ts = rs.getTimestamp("NgayVaoLam");
                // Format thành chuỗi dd/MM/yyyy (nếu ts == null thì để rỗng)
                String ngayVaoLam = ts != null
                    ? sdf.format(new java.util.Date(ts.getTime()))
                    : "";

                model.addRow(new Object[]{
                    rs.getString("MaNhanVien"),
                    rs.getString("HoTen"),
                    rs.getString("ChucVu"),
                    ngayVaoLam
                });
            }
            tblDanhSach.setModel(model);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu nhân viên:\n" + ex.getMessage(),
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Tải chi tiết nhân viên vào panel ThaoTac
    private void loadNhanVienDetails(String maNV) {
        String sql = "SELECT * FROM NHAN_VIEN WHERE MaNhanVien = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    String gt = rs.getString("GioiTinh");
                    if ("M".equals(gt)) gt = "Nam"; 
                    else if ("F".equals(gt)) gt = "Nữ";
                    else gt = null;
                    // Set cho txtGioiTinhView
                    txtGioiTinhView.setText(gt != null ? gt : "");
                    // Set cho cmbGioiTinhEdit
                    cmbGioiTinhEdit.setSelectedItem(gt);
                    // Set cho SDT, Email, DiaChi, Luong, PhucLoi
                    txtSDT.setText(rs.getString("SDT"));
                    txtEmail.setText(rs.getString("Email"));
                    txtDiaChi.setText(rs.getString("DiaChi"));
                    // Set cho cmbChucVuEdit
                    String cv = rs.getString("ChucVu");
                    cmbChucVuEdit.setSelectedItem(cv);
                    txtLuong.setText(rs.getBigDecimal("LuongCoBan").toPlainString());
                    txtPhucLoi.setText(rs.getString("PhucLoi"));
                    // Ngày vào làm
                    java.util.Date nvl = rs.getDate("NgayVaoLam");
                    // Set cho dcNgayVaoLamEdit
                    dcNgayVaoLamEdit.setDate(nvl);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải chi tiết nhân viên: " + ex.getMessage(),
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
        txtDiaChi.setText("");
        txtChucVuView.setText("");
        txtLuong.setText("");
        txtPhucLoi.setText("");
        txtNgayVaoLamView.setText("");

        dcNgaySinhEdit.setDate(null);
        dcNgayVaoLamEdit.setDate(new Date());

        cmbGioiTinhEdit.setSelectedIndex(-1); // Không chọn gì
        cmbChucVuEdit.setSelectedIndex(-1);
    }
    
    // Phương thức áp dụng placeholder cho tất cả các textfield nhập thông tin
    private void applyPlaceholders() {
        if (currentMode == Mode.ADD || currentMode == Mode.NONE) {
            if (txtMa.getText().trim().isEmpty())
                setPlaceholder(txtMa, "Mã nhân viên");

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

            if (txtDiaChi.getText().trim().isEmpty())
                setPlaceholder(txtDiaChi, "Nhập địa chỉ");

            if (txtChucVuView.getText().trim().isEmpty())
                setPlaceholder(txtChucVuView, "Nhập chức vụ");

            if (txtLuong.getText().trim().isEmpty())
                setPlaceholder(txtLuong, "Nhập lương cơ bản");

            if (txtPhucLoi.getText().trim().isEmpty())
                setPlaceholder(txtPhucLoi, "Nhập phúc lợi");

            if (txtNgayVaoLamView.getText().trim().isEmpty())
                setPlaceholder(txtNgayVaoLamView, "Nhập ngày vào làm");
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
        txtDiaChi.setFont(normalFont);
        txtDiaChi.setForeground(normalColor);
        txtChucVuView.setFont(normalFont);
        txtChucVuView.setForeground(normalColor);
        txtLuong.setFont(normalFont);
        txtLuong.setForeground(normalColor);
        txtPhucLoi.setFont(normalFont);
        txtPhucLoi.setForeground(normalColor);
        txtNgayVaoLamView.setFont(normalFont);
        txtNgayVaoLamView.setForeground(normalColor);
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
        txtDiaChi.setEditable(editable);
        txtChucVuView.setEditable(editable);
        txtLuong.setEditable(editable);
        txtPhucLoi.setEditable(editable);
        txtNgayVaoLamView.setEditable(editable);

        // JDateChooser
        dcNgaySinhEdit.setEnabled(editable);
        dcNgayVaoLamEdit.setEnabled(editable);

        // JComboBox (nếu có)
        cmbGioiTinhEdit.setEnabled(editable);
        cmbChucVuEdit.setEnabled(editable);
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
        txtDiaChi = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        lblNgayVaoLam = new javax.swing.JLabel();
        txtPhucLoi = new javax.swing.JTextField();
        pnlNgayVaoLamCards = new javax.swing.JPanel();
        txtNgayVaoLamView = new javax.swing.JTextField();
        dcNgayVaoLamEdit = new com.toedter.calendar.JDateChooser();
        pnlGioiTinhCards = new javax.swing.JPanel();
        cmbGioiTinhEdit = new javax.swing.JComboBox<>();
        txtGioiTinhView = new javax.swing.JTextField();
        pnlNgaySinhCards = new javax.swing.JPanel();
        dcNgaySinhEdit = new com.toedter.calendar.JDateChooser();
        txtNgaySinhView = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        pnlChucVuCards = new javax.swing.JPanel();
        cmbChucVuEdit = new javax.swing.JComboBox<>();
        txtChucVuView = new javax.swing.JTextField();
        btnHuy = new javax.swing.JButton();
        btnXacNhan = new javax.swing.JButton();
        lblBangCap = new javax.swing.JLabel();
        txtBangCap = new javax.swing.JTextField();
        dcThoiHanBC = new com.toedter.calendar.JDateChooser();
        lblThoiHanBC = new javax.swing.JLabel();
        lblStarHoTen = new javax.swing.JLabel();
        lblStarEmail = new javax.swing.JLabel();
        lblStarCCCD = new javax.swing.JLabel();
        lblStarLuongCoBan = new javax.swing.JLabel();
        lblStarNgaySinh = new javax.swing.JLabel();
        lblStarNgayVaoLam = new javax.swing.JLabel();
        lblStarChucVu = new javax.swing.JLabel();
        lblStarDiaChi = new javax.swing.JLabel();
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
        pnlThaoTac.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblThaoTac.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblThaoTac.setText("Thêm nhân viên");
        pnlThaoTac.add(lblThaoTac, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 28, -1, -1));

        lblMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblMa.setText("Mã nhân viên");
        pnlThaoTac.add(lblMa, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 81, 177, -1));

        lblHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblHoTen.setText("Họ tên");
        pnlThaoTac.add(lblHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 134, -1, -1));

        lblCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblCCCD.setText("Số  CCCD");
        pnlThaoTac.add(lblCCCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 184, -1, -1));

        lblNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgaySinh.setText("Ngày sinh");
        pnlThaoTac.add(lblNgaySinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 231, 90, -1));

        txtHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtHoTen.setAutoscrolls(false);
        txtHoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 131, 306, -1));

        lblGioiTinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblGioiTinh.setText("Giới tính");
        pnlThaoTac.add(lblGioiTinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 275, 203, -1));

        lblSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblSDT.setText("Số điện thoại");
        pnlThaoTac.add(lblSDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 328, 203, -1));

        lblEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblEmail.setText("Email");
        pnlThaoTac.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 373, 60, 30));

        lblDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblDiaChi.setText("Địa chỉ");
        pnlThaoTac.add(lblDiaChi, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 427, -1, -1));

        lblChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblChucVu.setText("Chức vụ");
        pnlThaoTac.add(lblChucVu, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 474, -1, -1));

        lblLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblLuong.setText("Lương cơ bản");
        pnlThaoTac.add(lblLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 527, -1, -1));

        lblPhucLoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblPhucLoi.setText("Phúc lợi");
        pnlThaoTac.add(lblPhucLoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 577, -1, -1));

        txtMa.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtMa.setAutoscrolls(false);
        txtMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtMa, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 81, 306, -1));

        txtEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtEmail.setAutoscrolls(false);
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 374, 307, -1));

        txtCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtCCCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 181, 307, -1));

        txtDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtDiaChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaChiActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtDiaChi, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 424, 307, -1));

        txtLuong.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLuongActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 524, 307, -1));

        lblNgayVaoLam.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblNgayVaoLam.setText("Ngày vào làm");
        pnlThaoTac.add(lblNgayVaoLam, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 624, 123, -1));

        txtPhucLoi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtPhucLoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhucLoiActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtPhucLoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 574, 307, -1));

        pnlNgayVaoLamCards.setLayout(new java.awt.CardLayout());

        txtNgayVaoLamView.setEditable(false);
        txtNgayVaoLamView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtNgayVaoLamView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayVaoLamViewActionPerformed(evt);
            }
        });
        pnlNgayVaoLamCards.add(txtNgayVaoLamView, "VIEW");

        dcNgayVaoLamEdit.setDateFormatString("dd/MM/yyyy");
        dcNgayVaoLamEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        pnlNgayVaoLamCards.add(dcNgayVaoLamEdit, "EDIT");

        pnlThaoTac.add(pnlNgayVaoLamCards, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 624, 307, -1));

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

        pnlThaoTac.add(pnlGioiTinhCards, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 275, 307, -1));

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

        pnlThaoTac.add(pnlNgaySinhCards, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 231, 307, -1));

        txtSDT.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtSDT.setAutoscrolls(false);
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtSDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 325, 307, 31));

        pnlChucVuCards.setLayout(new java.awt.CardLayout());

        cmbChucVuEdit.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        cmbChucVuEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phi công", "Tiếp viên", "Kỹ thuật viên", "Nhân viên bảo vệ", "Nhân viên thủ tục", "Quản lý" }));
        pnlChucVuCards.add(cmbChucVuEdit, "EDIT");

        txtChucVuView.setEditable(false);
        txtChucVuView.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtChucVuView.setAutoscrolls(false);
        pnlChucVuCards.add(txtChucVuView, "VIEW");

        pnlThaoTac.add(pnlChucVuCards, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 474, 307, -1));

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
        pnlThaoTac.add(btnHuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 30, 66, -1));

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
        pnlThaoTac.add(btnXacNhan, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 30, 66, -1));

        lblBangCap.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblBangCap.setText("Bằng cấp");
        pnlThaoTac.add(lblBangCap, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 668, 157, -1));

        txtBangCap.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        txtBangCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBangCapActionPerformed(evt);
            }
        });
        pnlThaoTac.add(txtBangCap, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 668, 300, -1));
        pnlThaoTac.add(dcThoiHanBC, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 720, 300, 32));

        lblThoiHanBC.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblThoiHanBC.setText("Thời hạn bằng cấp");
        pnlThaoTac.add(lblThoiHanBC, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 712, 157, -1));

        lblStarHoTen.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarHoTen.setForeground(new java.awt.Color(255, 0, 0));
        lblStarHoTen.setText("*");
        lblStarHoTen.setAutoscrolls(true);
        pnlThaoTac.add(lblStarHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 131, -1, -1));

        lblStarEmail.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarEmail.setForeground(new java.awt.Color(255, 0, 0));
        lblStarEmail.setText("*");
        lblStarEmail.setAutoscrolls(true);
        pnlThaoTac.add(lblStarEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, -1, 40));

        lblStarCCCD.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarCCCD.setForeground(new java.awt.Color(255, 0, 0));
        lblStarCCCD.setText("*");
        lblStarCCCD.setAutoscrolls(true);
        pnlThaoTac.add(lblStarCCCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 181, -1, -1));

        lblStarLuongCoBan.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarLuongCoBan.setForeground(new java.awt.Color(255, 0, 0));
        lblStarLuongCoBan.setText("*");
        lblStarLuongCoBan.setAutoscrolls(true);
        pnlThaoTac.add(lblStarLuongCoBan, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 530, 10, -1));

        lblStarNgaySinh.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarNgaySinh.setForeground(new java.awt.Color(255, 0, 0));
        lblStarNgaySinh.setText("*");
        lblStarNgaySinh.setAutoscrolls(true);
        pnlThaoTac.add(lblStarNgaySinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, -1, -1));

        lblStarNgayVaoLam.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarNgayVaoLam.setForeground(new java.awt.Color(255, 0, 0));
        lblStarNgayVaoLam.setText("*");
        lblStarNgayVaoLam.setAutoscrolls(true);
        pnlThaoTac.add(lblStarNgayVaoLam, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 620, -1, -1));

        lblStarChucVu.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarChucVu.setForeground(new java.awt.Color(255, 0, 0));
        lblStarChucVu.setText("*");
        lblStarChucVu.setAutoscrolls(true);
        pnlThaoTac.add(lblStarChucVu, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 470, -1, -1));

        lblStarDiaChi.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        lblStarDiaChi.setForeground(new java.awt.Color(255, 0, 0));
        lblStarDiaChi.setText("*");
        lblStarDiaChi.setAutoscrolls(true);
        pnlThaoTac.add(lblStarDiaChi, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 430, -1, -1));

        pnlDanhSach.setBackground(java.awt.SystemColor.control);
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(632, 824));

        lblDanhSach.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        lblDanhSach.setText("Danh sách nhân viên");

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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã NV", "Họ tên", "Chức vụ", "Ngày vào làm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
            tblDanhSach.getColumnModel().getColumn(1).setMinWidth(150);
            tblDanhSach.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblDanhSach.getColumnModel().getColumn(1).setMaxWidth(150);
            tblDanhSach.getColumnModel().getColumn(1).setHeaderValue(lblHoTen.getText());
            tblDanhSach.getColumnModel().getColumn(2).setHeaderValue(lblChucVu.getText());
            tblDanhSach.getColumnModel().getColumn(3).setHeaderValue(lblNgayVaoLam.getText());
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhSachLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(spDanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
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
        String maNV = txtTimKiem.getText().trim();
        if (maNV.isEmpty() || maNV.equals("Nhập mã nhân viên hoặc tên nhân viên")) {
            loadNhanVienToTable(); // Hiển thị toàn bộ nếu không nhập hoặc chỉ đang hiển thị placeholder
            return;
        }
        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên cần tìm!");
            return;
        }
        
        String sql = "SELECT MaNhanVien, HoTen, ChucVu, NgayVaoLam FROM NHAN_VIEN WHERE UPPER(MaNhanVien) = UPPER(?)";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tblDanhSach.getModel();

            boolean found = false;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaNhanVien"),
                    rs.getString("HoTen"),
                    rs.getString("ChucVu"),
                    rs.getTimestamp("NgayVaoLam")
                });
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào!");
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
            String maNV = selected.split(" - ")[0].trim();
            txtTimKiem.setText(maNV);
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
                 "SELECT MaNhanVien, HoTen FROM NHAN_VIEN " +
                 "WHERE LOWER(MaNhanVien) LIKE ? OR LOWER(HoTen) LIKE ?")) {

            ps.setString(1, text + "%");
            ps.setString(2, "%" + text + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maNV = rs.getString("MaNhanVien");
                String hoTen = rs.getString("HoTen");
                listModel.addElement(maNV + " - " + hoTen);
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
        
    // Insert nhân viên trả về thành công/ thất bại
    private boolean insertNhanVien() {
        Connection conn = null;
        CallableStatement csNhanVien = null; // Biến này sẽ được dùng cho INSERT NHAN_VIEN
        boolean success = false;

        // 1. Lấy dữ liệu từ các component trên form cho NHAN_VIEN
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
            // Nếu giá trị khác "Nam" hoặc "Nữ", gioiTinhDBValue sẽ là null, phù hợp với cột CHAR(1) nullable
        }

        String sdt = getTextFieldValue(txtSDT, "Nhập số điện thoại"); 
        String email = getTextFieldValue(txtEmail, "Nhập email"); 
        String diaChi = getTextFieldValue(txtDiaChi, "Nhập địa chỉ");

        String chucVu = getComboBoxSelectedValue(cmbChucVuEdit);
        
        BigDecimal luongCoBan = null;
        String luongCoBanStr = getTextFieldValue(txtLuong, "Nhập lương cơ bản");
        if (!luongCoBanStr.isEmpty()) {
            try {
                luongCoBan = new BigDecimal(luongCoBanStr);
                if (luongCoBan.compareTo(BigDecimal.ZERO) <= 0) { // Lương phải là số dương
                    JOptionPane.showMessageDialog(this, "Lương cơ bản phải là một số dương.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Lương cơ bản không hợp lệ. Vui lòng nhập số.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        String phucLoi = getTextFieldValue(txtPhucLoi, "Nhập phúc lợi");
        java.sql.Timestamp ngayVaoLam = (dcNgayVaoLamEdit.getDate() != null) ? new java.sql.Timestamp(dcNgayVaoLamEdit.getDate().getTime()) : null;
    
        // --- Kiểm tra các trường NOT NULL theo DB schema trước khi thực hiện SQL ---
        // MaNhanVien is auto-generated by FN_TAO_MANV()
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Assuming CCCD "NOT NUL" means "NOT NULL"
        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số CCCD không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // GioiTinh CHAR(1) is nullable in DB, no mandatory check here.
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (chucVu == null) { // chucVu is null if not selected or selected item was empty
            JOptionPane.showMessageDialog(this, "Chức vụ không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (luongCoBan == null) { // luongCoBan is null if text field was empty or non-positive value
            JOptionPane.showMessageDialog(this, "Lương cơ bản không được để trống và phải là một số dương hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ngayVaoLam == null) {
            JOptionPane.showMessageDialog(this, "Ngày vào làm không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // --- Hết phần kiểm tra NOT NULL ---
        
        // 2. Lấy dữ liệu từ các component trên form cho BANG_CAP (cần để kiểm tra điều kiện)
        String tenBangCap = txtBangCap.getText().trim();
        Timestamp thoiHanBC = (dcThoiHanBC.getDate() != null) ? new Timestamp(dcThoiHanBC.getDate().getTime()) : null;
        
        // --- Bắt đầu kiểm tra điều kiện ràng buộc ---
        if ("Phi công".equals(chucVu) || "Tiếp viên".equals(chucVu)) {
            if (tenBangCap.isEmpty() || thoiHanBC == null) {
                JOptionPane.showMessageDialog(this,
                        "Chức vụ '" + chucVu + "' yêu cầu phải điền đầy đủ thông tin Bằng cấp (Tên bằng cấp và Thời hạn).",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return false; // Ngừng quá trình thêm nếu không đủ điều kiện
            }
        }
        // --- Kết thúc kiểm tra điều kiện ràng buộc ---

        // SQL cho việc INSERT NHAN_VIEN và lấy về MaNhanVien
        String sqlInsertNV = "DECLARE "
                           + "  newMaNV NVARCHAR2(10); "
                           + "BEGIN "
                           + "  INSERT INTO NHAN_VIEN (MaNhanVien, HoTen, CCCD, NgaySinh, GioiTinh, SDT, Email, DiaChi, ChucVu, LuongCoBan, PhucLoi, NgayVaoLam) "
                           + "  VALUES (FN_TAO_MANV(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                           + "  RETURNING MaNhanVien INTO newMaNV; "
                           + "  ? := newMaNV; " // Gán giá trị vào tham số OUT
                           + "END;";

        // SQL cho việc INSERT BANG_CAP (sẽ được gọi trong hàm insertBangCap riêng)
        // string sqlInsertBC đã được định nghĩa trong hàm insertBangCap
        try {
            conn = ConnectionUtils.getMyConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction để đảm bảo cả NV và BC và Account đều được thêm

            // --- Thực thi INSERT NHAN_VIEN ---
            csNhanVien = conn.prepareCall(sqlInsertNV);

            // Set các tham số đầu vào cho NHAN_VIEN
            csNhanVien.setString(1, hoTen);
            csNhanVien.setString(2, cccd.isEmpty() ? null : cccd);

            csNhanVien.setDate(3, ngaySinh);
            
            if (gioiTinhDBValue != null) {      // GioiTinh (Nullable CHAR(1))
                csNhanVien.setString(4, gioiTinhDBValue);
            } else {
                csNhanVien.setNull(4, Types.CHAR);
            }

            if (!sdt.isEmpty()) {               // SDT (Nullable VARCHAR2)
                csNhanVien.setString(5, sdt);
            } else {
                csNhanVien.setNull(5, Types.VARCHAR);
            }
            
            csNhanVien.setString(6, email.isEmpty() ? null : email);
            csNhanVien.setString(7, diaChi.isEmpty() ? null : diaChi);

            csNhanVien.setString(8, chucVu);
            csNhanVien.setBigDecimal(9, luongCoBan);
            
            if (!phucLoi.isEmpty()) {           // PhucLoi (Nullable NVARCHAR2)
                csNhanVien.setString(10, phucLoi);
            } else {
                csNhanVien.setNull(10, Types.NVARCHAR);
            }
            
            csNhanVien.setTimestamp(11, ngayVaoLam);

            // Đăng ký tham số OUT để nhận mã nhân viên mới tạo
            csNhanVien.registerOutParameter(12, Types.VARCHAR);

            csNhanVien.execute(); // Thực thi lệnh thêm nhân viên

            // Lấy mã nhân viên được tạo
            String newMaNV = csNhanVien.getString(12);
            txtMa.setText(newMaNV); // Cập nhật MaNV lên giao diện

            // --- Thực thi INSERT BANG_CAP nếu có thông tin và điều kiện cho phép ---
            // Chỉ gọi insertBangCap nếu chức vụ yêu cầu hoặc người dùng đã điền thông tin bằng cấp
            if (!tenBangCap.isEmpty() && thoiHanBC != null) {
                // Chúng ta sẽ tái sử dụng hàm insertBangCap, nhưng truyền vào kết nối hiện có
                // và không để nó tự đóng connection hay commit/rollback.
                // Điều này yêu cầu sửa đổi nhỏ trong hàm insertBangCap để nó nhận Connection làm tham số.

                // Cách 1: Sửa đổi insertBangCap để nhận Connection (recommended)
                boolean bangCapAdded = insertBangCap(conn, newMaNV, tenBangCap, thoiHanBC);
                if (!bangCapAdded) {
                    conn.rollback(); // Rollback nếu thêm bằng cấp thất bại
                    JOptionPane.showMessageDialog(this, "Thêm bằng cấp thất bại. Đã hủy bỏ việc thêm nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // Nếu mọi thứ thành công (thêm nhân viên và bằng cấp), commit transaction
            conn.commit();
            JOptionPane.showMessageDialog(this, "Thêm nhân viên và bằng cấp thành công. Mã nhân viên: " + newMaNV);
            success = true;
            
            // --- Tự động gọi hàm tạo tài khoản sau khi insert NHAN_VIEN thành công ---
            // Lưu ý: hàm taoUserVaAccountChoNhanVien sẽ tự xử lý thông báo lỗi nội bộ
            // và in chi tiết lỗi ra console như đã sửa đổi.
            nhanVienAccountManager.taoUserVaAccountChoNhanVien(newMaNV);

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback(); // Rollback nếu có lỗi SQL
            } catch (SQLException rbex) {
                rbex.printStackTrace();
            }
            handleInsertNhanVienError(ex); // Sử dụng hàm xử lý lỗi của bạn
        } catch (Exception ex) {
            try {
                if (conn != null) conn.rollback(); // Rollback cho các lỗi khác
            } catch (SQLException rbex) {
                rbex.printStackTrace();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Đóng tài nguyên trong khối finally
            try {
                if (csNhanVien != null) csNhanVien.close();
                if (conn != null) conn.close(); // Đóng connection sau khi commit/rollback
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
    
    // Hàm insertBangCap cần được sửa đổi để nhận Connection và các giá trị trực tiếp
    private boolean insertBangCap(Connection conn, String maNhanVien, String tenBangCap, Timestamp thoiHanBC) throws SQLException {
        // Trạng thái mặc định cho bằng cấp mới
        String trangThaiBangCap = "Valid"; // Bạn có thể thay đổi tùy theo nghiệp vụ

        String sql = "DECLARE "
                   + "  newMaBC NVARCHAR2(10); "
                   + "BEGIN "
                   + "  INSERT INTO BANG_CAP (MaBangCap, TenBangCap, MaNhanVien, ThoiHan, TrangThai) "
                   + "  VALUES (FN_TAO_MABC(), ?, ?, ?, ?); "
                   + "END;";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, tenBangCap);
            cs.setString(2, maNhanVien);
            cs.setTimestamp(3, thoiHanBC);
            cs.setString(4, trangThaiBangCap);
            cs.execute();
            return true;
        }
    }
    
    // Update nhân viên trả về thành công/ thất bại
    private boolean updateNhanVien() {
        // 0. Lấy MaNhanVien để cập nhật (bắt buộc)
        String maNhanVien = txtMa.getText().trim();
        if (maNhanVien.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống để cập nhật.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
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
        String diaChi = getTextFieldValue(txtEmail, "Nhập email"); 

        String chucVu = getComboBoxSelectedValue(cmbChucVuEdit);

        BigDecimal luongCoBan = null;
        String luongCoBanStr = getTextFieldValue(txtLuong, "Nhập lương cơ bản");
        if (!luongCoBanStr.isEmpty()) {
            try {
                luongCoBan = new BigDecimal(luongCoBanStr);
                if (luongCoBan.compareTo(BigDecimal.ZERO) <= 0) { // Lương phải là số dương
                    JOptionPane.showMessageDialog(this, "Lương cơ bản phải là một số dương.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Lương cơ bản không hợp lệ. Vui lòng nhập số.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        String phucLoi = getTextFieldValue(txtPhucLoi, "Nhập phúc lợi");
        java.sql.Timestamp ngayVaoLam = (dcNgayVaoLamEdit.getDate() != null) ? new java.sql.Timestamp(dcNgayVaoLamEdit.getDate().getTime()) : null;

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
        if (diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (chucVu == null) { // chucVu is null if not selected or selected item was empty
            JOptionPane.showMessageDialog(this, "Chức vụ không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (luongCoBan == null) { // luongCoBan is null if text field was empty or conversion failed/was non-positive
            JOptionPane.showMessageDialog(this, "Lương cơ bản không được để trống và phải là một số dương hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ngayVaoLam == null) {
            JOptionPane.showMessageDialog(this, "Ngày vào làm không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // --- Hết phần kiểm tra NOT NULL ---
        String sql = 
            "UPDATE NHAN_VIEN SET " +
            "HoTen      = ?, " +  // 1
            "CCCD       = ?, " +  // 2
            "NgaySinh   = ?, " +  // 3
            "GioiTinh   = ?, " +  // 4
            "SDT        = ?, " +  // 5
            "Email      = ?, " +  // 6
            "DiaChi     = ?, " +  // 7
            "ChucVu     = ?, " +  // 8
            "LuongCoBan = ?, " +  // 9
            "PhucLoi    = ?, " +  // 10
            "NgayVaoLam = ? " +   // 11
            "WHERE MaNhanVien = ?"; // 12

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
            ps.setString(7, diaChi);        // DiaChi (NOT NULL, validated)

            // 8. Chức vụ
            ps.setString(8, chucVu);        // ChucVu (NOT NULL, validated)

            // 9. Lương cơ bản
            ps.setBigDecimal(9, luongCoBan); // LuongCoBan (NOT NULL, validated)

            // 10. Phúc lợi
            if (!phucLoi.isEmpty()) {       // PhucLoi (Nullable NVARCHAR2)
                ps.setString(10, phucLoi);
            } else {
                ps.setNull(10, Types.NVARCHAR);
            }

            // 11. Ngày vào làm
            ps.setTimestamp(11, ngayVaoLam); // NgayVaoLam (NOT NULL, validated)

            // 12. WHERE MaNhanVien
            ps.setString(12, maNhanVien);    // MaNhanVien for WHERE clause (NOT NULL, validated)

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công.");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên để cập nhật.", 
                                              "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
            loadNhanVienToTable();

        } catch (SQLException ex) {
            handleUpdateNhanVienError(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định:\n" + ex.getMessage(),
                                          "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    // Xóa nhân viên trả về thành công/ thất bại
    private boolean deleteNhanVien(String maNV) {
        String sql = "DELETE FROM NHAN_VIEN WHERE MaNhanVien = ?";
        try (Connection conn = ConnectionUtils.getMyConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên thành công.");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên để xóa.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xóa nhân viên: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
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
                lblThaoTac.setText("Thông tin nhân viên");
                // Tắt btnXacNhan và btnHuy
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                // Ẩn các thuộc tính bằng cấp
                lblBangCap.setVisible(false);
                lblThoiHanBC.setVisible(false);
                txtBangCap.setVisible(false);
                dcThoiHanBC.setVisible(false);
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
                // Hiện các thuộc tính bằng cấp
                lblBangCap.setVisible(true);
                lblThoiHanBC.setVisible(true);
                txtBangCap.setVisible(true);
                dcThoiHanBC.setVisible(true);
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
                // Ẩn các thuộc tính bằng cấp
                lblBangCap.setVisible(false);
                lblThoiHanBC.setVisible(false);
                txtBangCap.setVisible(false);
                dcThoiHanBC.setVisible(false);
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
                // Ẩn các thuộc tính bằng cấp
                lblBangCap.setVisible(false);
                lblThoiHanBC.setVisible(false);
                txtBangCap.setVisible(false);
                dcThoiHanBC.setVisible(false);
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
                lblThaoTac.setText("Thông tin nhân viên");
                // Clear các trường thông tin trong form
                clearFormInput();                
                // Tắt nút Xác nhận, Hủy
                btnXacNhan.setVisible(false);
                btnHuy.setVisible(false);
                // Ẩn các thuộc tính bằng cấp
                lblBangCap.setVisible(false);
                lblThoiHanBC.setVisible(false);
                txtBangCap.setVisible(false);
                dcThoiHanBC.setVisible(false);
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa.");
            return;
        }
        setMode(Mode.EDIT);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblDanhSach.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa.");
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

    private void txtNgayVaoLamViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayVaoLamViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayVaoLamViewActionPerformed

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

    private void txtDiaChiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaChiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaChiActionPerformed

    private void txtLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLuongActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        searchByMaNhanVien();
        setMode(Mode.NONE);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtPhucLoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhucLoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhucLoiActionPerformed

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
            case ADD:
                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn thêm nhân viên này?",
                        "Xác nhận thêm", JOptionPane.YES_NO_OPTION) 
                    == JOptionPane.YES_OPTION) {
                    
                    boolean success = insertNhanVien(); // Trả về true nếu thêm thành công
                    if (success) {
                        loadNhanVienToTable();
                        setMode(Mode.NONE); // chỉ trả về chế độ NONE nếu thêm thành công
                    }
                }
                // nếu NO: không làm gì, vẫn ở chế độ ADD
                break;

            case DELETE:
                String maDel = tblDanhSach.getValueAt(
                    tblDanhSach.getSelectedRow(), 0).toString();
                if (JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn xóa nhân viên mã: " + maDel + "?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                    
                    boolean success = deleteNhanVien(maDel); 
                    if (success) {
                        loadNhanVienToTable();
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
                    boolean success = updateNhanVien(); 
                    if (success) {
                        loadNhanVienToTable();
                        setMode(Mode.NONE);
                    }
                }
                break;

            default:
                // không làm gì
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void txtBangCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBangCapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBangCapActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cmbChucVuEdit;
    private javax.swing.JComboBox<String> cmbGioiTinhEdit;
    private com.toedter.calendar.JDateChooser dcNgaySinhEdit;
    private com.toedter.calendar.JDateChooser dcNgayVaoLamEdit;
    private com.toedter.calendar.JDateChooser dcThoiHanBC;
    private javax.swing.JLabel lblBangCap;
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
    private javax.swing.JLabel lblThoiHanBC;
    private javax.swing.JPanel pnlChucVuCards;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlGioiTinhCards;
    private javax.swing.JPanel pnlNgaySinhCards;
    private javax.swing.JPanel pnlNgayVaoLamCards;
    private javax.swing.JPanel pnlThaoTac;
    private javax.swing.JScrollPane spDanhSach;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtBangCap;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtChucVuView;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGioiTinhView;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtNgaySinhView;
    private javax.swing.JTextField txtNgayVaoLamView;
    private javax.swing.JTextField txtPhucLoi;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
