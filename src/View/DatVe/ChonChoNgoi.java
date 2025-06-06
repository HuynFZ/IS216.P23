
package View.DatVe;

import Process.ChuyenBay.ChuyenBToMayB;
import Process.HanhKhach.ChonChoCallBack;
import Model.HanhKhach.UserHanhKhach;
import Process.MayBay.SoDoGhe;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class ChonChoNgoi extends javax.swing.JFrame {

    private String maChuyen, diemDi, diemDen;
    int tongSoKH;
    private List<UserHanhKhach> danhSachHanhKhach = new ArrayList<>();
    private static Map<String, JPanel> mapPanelGhe = new HashMap<>();
    private int rowDangChon = -1;
    private static Map<Integer, JPanel> gheDaChonTheoKhach = new HashMap<>();
    private ChonChoCallBack callback;
    
    public ChonChoNgoi(String maChuyen, String diemDi, String diemDen, int tongSoKH, List<UserHanhKhach> danhSachHanhKhach) throws ClassNotFoundException, SQLException {
        initComponents();
        System.out.println(maChuyen);
        String maMayBay = ChuyenBToMayB.timMaMayBay(maChuyen);
        taoMapGhe();
        taoSoDoGhe(maChuyen, danhSachHanhKhach);
        
        noiDung.setText("Chuyến bay " + maChuyen + " " + diemDi + " - " + diemDen);
        
        DefaultTableModel model = (DefaultTableModel) bangHanhKhach.getModel();
        model.setRowCount(0);
        for (UserHanhKhach hk : danhSachHanhKhach) {
            model.addRow(new Object[]{
                hk.getHoTen(),
                hk.getTienGhe(),    // Phí tiêu tốn chưa xác định
                hk.getViTriGhe()     // Ghế ngồi chưa chọn
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

        btnXacNhanChonCho.addActionListener(e -> {
            // Cập nhật lại danh sách ghế đã chọn từ bảng vào danhSachHanhKhach
            DefaultTableModel model1 = (DefaultTableModel) bangHanhKhach.getModel();
            for (int i = 0; i < model1.getRowCount(); i++) {
                String maGhe = (String) model1.getValueAt(i, 2); // ghế ngồi
                danhSachHanhKhach.get(i).setViTriGhe(maGhe);
            }

            // Gọi callback để trả dữ liệu về
            if (callback != null) {
                callback.capNhatDanhSach(danhSachHanhKhach);
            }

            dispose(); // Đóng cửa sổ chọn chỗ
        });
        
        btnHuyChonCho.addActionListener(e -> {
            dispose();
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
   
    
    public ChonChoNgoi(String maChuyen) throws ClassNotFoundException, SQLException {
        initComponents();
        String maMayBay = ChuyenBToMayB.timMaMayBay(maChuyen);
        taoMapGhe();
        taoSoDoGhe(maMayBay,danhSachHanhKhach);
    }
    
    public void setCallBack (ChonChoCallBack callback)
    {
        this.callback = callback;
    }
   /* private void addHoverEffect(JPanel panel) {
        Color originalColor = panel.getBackground();
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(originalColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(originalColor);
            }
        });
    }*/

    
    private void taoSoDoGhe(String maMayBay, List<UserHanhKhach> danhSachHanhKhach) throws ClassNotFoundException, SQLException {
    Map<String, String> danhSach = SoDoGhe.layDanhSachGhe(maMayBay);

    if (danhSach == null || danhSach.isEmpty()) {
        System.out.println("Không có dữ liệu ghế cho mã máy bay: " + maMayBay);
        return;
    }
    
    int index = 0;
    for (UserHanhKhach hk : danhSachHanhKhach) {
        String maGhe = hk.getViTriGhe();
        if (maGhe != null && !maGhe.isEmpty()) {
            JPanel panelGhe = mapPanelGhe.get(maGhe);
            if (panelGhe != null) {
                panelGhe.setBackground(Color.YELLOW);
                panelGhe.putClientProperty("trangThai", "danggiu");
                panelGhe.setVisible(true);
                gheDaChonTheoKhach.put(index, panelGhe);
                index ++;
            }
        }
    }


    for (Map.Entry<String, String> entry : danhSach.entrySet()) {
        String maGhe = entry.getKey();          // Ví dụ: "1A"
        String trangThaiGhe = entry.getValue(); // Ví dụ: "Đã xác nhận", "Trống", "Đang giữ"
        JPanel panelGhe = mapPanelGhe.get(maGhe);
        String trangThai = "trong";

        if (panelGhe != null) {
            String currentTrangThai = (String) panelGhe.getClientProperty("trangThai");

            // Nếu ghế đã được đánh dấu giữ thì bỏ qua không đổi màu
            if ("danggiu".equals(currentTrangThai)) {
                continue;
            }

            panelGhe.setVisible(true);
            switch (trangThaiGhe.trim()) {
                case "Trống":
                    panelGhe.setVisible(true);
                    if (panel11A.isVisible()) 
                    {
                        lbl11A.setVisible(true);
                        lbl11B.setVisible(true);
                    }
                    if (panel12A.isVisible()) 
                    {
                        lbl12A.setVisible(true);
                        lbl12B.setVisible(true);
                    }
                    if (panel13A.isVisible()) 
                    {
                        lbl13A.setVisible(true);
                        lbl13B.setVisible(true);
                    }
                    if (panel14A.isVisible()) 
                    {
                        lbl14A.setVisible(true);
                        lbl14B.setVisible(true);
                    }
                    if (panel15A.isVisible()) 
                    {
                        lbl15A.setVisible(true);
                        lbl15B.setVisible(true);
                    }
                    if (panel16A.isVisible()) 
                    {
                        lbl16A.setVisible(true);
                        lbl16B.setVisible(true);
                    }
                    if (panel17A.isVisible()) 
                    {
                        lbl17A.setVisible(true);
                        lbl17B.setVisible(true);
                    }
                    if (panel18A.isVisible()) 
                    {
                        lbl18A.setVisible(true);
                        lbl18B.setVisible(true);
                    }
                    if (panel19A.isVisible()) 
                    {
                        lbl19A.setVisible(true);
                        lbl19B.setVisible(true);
                    }
                    
                    panelGhe.setBackground(Color.GREEN);
                    System.out.println("Trống");
                    break;
                case "Đã xác nhận":
                    trangThai = "daxacnhan";
                    panelGhe.setVisible(true);
                    if (panel11A.isVisible()) 
                    {
                        lbl11A.setVisible(true);
                        lbl11B.setVisible(true);
                    }
                    if (panel12A.isVisible()) 
                    {
                        lbl12A.setVisible(true);
                        lbl12B.setVisible(true);
                    }
                    if (panel13A.isVisible()) 
                    {
                        lbl13A.setVisible(true);
                        lbl13B.setVisible(true);
                    }
                    if (panel14A.isVisible()) 
                    {
                        lbl14A.setVisible(true);
                        lbl14B.setVisible(true);
                    }
                    if (panel15A.isVisible()) 
                    {
                        lbl15A.setVisible(true);
                        lbl15B.setVisible(true);
                    }
                    if (panel16A.isVisible()) 
                    {
                        lbl16A.setVisible(true);
                        lbl16B.setVisible(true);
                    }
                    if (panel17A.isVisible()) 
                    {
                        lbl17A.setVisible(true);
                        lbl17B.setVisible(true);
                    }
                    if (panel18A.isVisible()) 
                    {
                        lbl18A.setVisible(true);
                        lbl18B.setVisible(true);
                    }
                    if (panel19A.isVisible()) 
                    {
                        lbl19A.setVisible(true);
                        lbl19B.setVisible(true);
                    }
                    panelGhe.setBackground(Color.RED);
                    System.out.println("Ghế " + maGhe + " đã được xác nhận.");
                    break;
                case "Đang giữ":
                    trangThai = "danggiu";
                    panelGhe.setVisible(true);
                    if (panel11A.isVisible()) 
                    {
                        lbl11A.setVisible(true);
                        lbl11B.setVisible(true);
                    }
                    if (panel12A.isVisible()) 
                    {
                        lbl12A.setVisible(true);
                        lbl12B.setVisible(true);
                    }
                    if (panel13A.isVisible()) 
                    {
                        lbl13A.setVisible(true);
                        lbl13B.setVisible(true);
                    }
                    if (panel14A.isVisible()) 
                    {
                        lbl14A.setVisible(true);
                        lbl14B.setVisible(true);
                    }
                    if (panel15A.isVisible()) 
                    {
                        lbl15A.setVisible(true);
                        lbl15B.setVisible(true);
                    }
                    if (panel16A.isVisible()) 
                    {
                        lbl16A.setVisible(true);
                        lbl16B.setVisible(true);
                    }
                    if (panel17A.isVisible()) 
                    {
                        lbl17A.setVisible(true);
                        lbl17B.setVisible(true);
                    }
                    if (panel18A.isVisible()) 
                    {
                        lbl18A.setVisible(true);
                        lbl18B.setVisible(true);
                    }
                    if (panel19A.isVisible()) 
                    {
                        lbl19A.setVisible(true);
                        lbl19B.setVisible(true);
                    }
                    panelGhe.setBackground(Color.YELLOW);
                    System.out.println("đang giữ");
                    break;
                default:
                    panelGhe.setBackground(Color.GRAY); // trạng thái không rõ
                    System.out.println("Ghế " + maGhe + " có trạng thái không xác định: " + trangThaiGhe);
                    
                    break;
            }
            panelGhe.putClientProperty("trangThai", trangThai);
            panelGhe.repaint(); // cập nhật lại hiển thị màu
        } else {
            System.out.println("Không tìm thấy panel tương ứng với mã ghế: " + maGhe);
        }
        
        panelGhe.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Color originalColor = panelGhe.getBackground();
                panelGhe.setBackground(originalColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                String trangThai = (String) panelGhe.getClientProperty("trangThai");
                if ("trong".equals(trangThai)) {
                    panelGhe.setBackground(Color.GREEN);
                } else if ("danggiu".equals(trangThai)) {
                    panelGhe.setBackground(Color.YELLOW);
                } else if ("daxacnhan".equals(trangThai)) {
                    panelGhe.setBackground(Color.RED);
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String trangThai = (String) panelGhe.getClientProperty("trangThai");

                if ("daxacnhan".equals(trangThai)) {
                    JOptionPane.showMessageDialog(null, "Ghế này đã được xác nhận, không thể chọn.");
                    return;
                }
                
                if ("danggiu".equals(trangThai)) {
                    JOptionPane.showMessageDialog(null, "Ghế này đã được giữ bởi hành khách khác, không thể chọn.");
                    return;
                }

                if (rowDangChon < 0 || rowDangChon >= bangHanhKhach.getRowCount()) {
                    JOptionPane.showMessageDialog(null, "Hãy chọn một hành khách trước.");
                    return;
                }

                // Kiểm tra nếu khách này đã chọn ghế khác rồi
                JPanel gheCu = gheDaChonTheoKhach.get(rowDangChon);
                if (gheCu != null && gheCu != panelGhe) {
                    int result = JOptionPane.showConfirmDialog(null, "Bạn có muốn đổi sang ghế này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.NO_OPTION) return;

                    // Trả lại ghế cũ
                    gheCu.setBackground(Color.GREEN);
                    gheCu.putClientProperty("trangThai", "trong");
                }

                // Cập nhật trạng thái ghế mới
                panelGhe.setBackground(Color.YELLOW);
                panelGhe.putClientProperty("trangThai", "danggiu");

                // Gán vào bảng
                bangHanhKhach.setValueAt(maGhe, rowDangChon, 2); // Cột ghế ngồi

                // Cập nhật map
                gheDaChonTheoKhach.put(rowDangChon, panelGhe);
            }
        });
    }
}
    
    JPanel selectedGhe = null;
    
    private void xuLyChonGhe(String maGhe, JPanel panelGhe) {
        // Kiểm tra có hành khách nào đang được chọn không
        if (rowDangChon == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hành khách trước!");
            return;
        }

        // Lấy model bảng
        DefaultTableModel model = (DefaultTableModel) bangHanhKhach.getModel();

        // Kiểm tra ghế đã được chọn bởi hành khách khác chưa
        for (int i = 0; i < model.getRowCount(); i++) {
            String ghe = (String) model.getValueAt(i, 2);
            if (maGhe.equals(ghe)) {
                JOptionPane.showMessageDialog(this, "Ghế này đã được chọn!");
                return;
            }
        }

        // Đặt ghế vào dòng đang chọn
        model.setValueAt(maGhe, rowDangChon, 2);

        // Cập nhật màu ghế
       // panelGhe.setBackground(Color.BLUE);
    }
    
    private void taoMapGhe()
    {

        mapPanelGhe.put("1A", panel1A);
        mapPanelGhe.put("1B", panel1B);
        mapPanelGhe.put("1C", panel1C);
        mapPanelGhe.put("1D", panel1D);
        mapPanelGhe.put("1E", panel1E);
        mapPanelGhe.put("1F", panel1F);

        mapPanelGhe.put("2A", panel2A);
        mapPanelGhe.put("2B", panel2B);
        mapPanelGhe.put("2C", panel2C);
        mapPanelGhe.put("2D", panel2D);
        mapPanelGhe.put("2E", panel2E);
        mapPanelGhe.put("2F", panel2F);

        mapPanelGhe.put("3A", panel3A);
        mapPanelGhe.put("3B", panel3B);
        mapPanelGhe.put("3C", panel3C);
        mapPanelGhe.put("3D", panel3D);
        mapPanelGhe.put("3E", panel3E);
        mapPanelGhe.put("3F", panel3F);

        mapPanelGhe.put("4A", panel4A);
        mapPanelGhe.put("4B", panel4B);
        mapPanelGhe.put("4C", panel4C);
        mapPanelGhe.put("4D", panel4D);
        mapPanelGhe.put("4E", panel4E);
        mapPanelGhe.put("4F", panel4F);

        mapPanelGhe.put("5A", panel5A);
        mapPanelGhe.put("5B", panel5B);
        mapPanelGhe.put("5C", panel5C);
        mapPanelGhe.put("5D", panel5D);
        mapPanelGhe.put("5E", panel5E);
        mapPanelGhe.put("5F", panel5F);

        mapPanelGhe.put("6A", panel6A);
        mapPanelGhe.put("6B", panel6B);
        mapPanelGhe.put("6C", panel6C);
        mapPanelGhe.put("6D", panel6D);
        mapPanelGhe.put("6E", panel6E);
        mapPanelGhe.put("6F", panel6F);

        mapPanelGhe.put("7A", panel7A);
        mapPanelGhe.put("7B", panel7B);
        mapPanelGhe.put("7C", panel7C);
        mapPanelGhe.put("7D", panel7D);
        mapPanelGhe.put("7E", panel7E);
        mapPanelGhe.put("7F", panel7F);

        mapPanelGhe.put("8A", panel8A);
        mapPanelGhe.put("8B", panel8B);
        mapPanelGhe.put("8C", panel8C);
        mapPanelGhe.put("8D", panel8D);
        mapPanelGhe.put("8E", panel8E);
        mapPanelGhe.put("8F", panel8F);

        mapPanelGhe.put("9A", panel9A);
        mapPanelGhe.put("9B", panel9B);
        mapPanelGhe.put("9C", panel9C);
        mapPanelGhe.put("9D", panel9D);
        mapPanelGhe.put("9E", panel9E);
        mapPanelGhe.put("9F", panel9F);

        mapPanelGhe.put("10A", panel10A);
        mapPanelGhe.put("10B", panel10B);
        mapPanelGhe.put("10C", panel10C);
        mapPanelGhe.put("10D", panel10D);
        mapPanelGhe.put("10E", panel10E);
        mapPanelGhe.put("10F", panel10F);

        mapPanelGhe.put("11A", panel11A);
        mapPanelGhe.put("11B", panel11B);
        mapPanelGhe.put("11C", panel11C);
        mapPanelGhe.put("11D", panel11D);
        mapPanelGhe.put("11E", panel11E);
        mapPanelGhe.put("11F", panel11F);

        mapPanelGhe.put("12A", panel12A);
        mapPanelGhe.put("12B", panel12B);
        mapPanelGhe.put("12C", panel12C);
        mapPanelGhe.put("12D", panel12D);
        mapPanelGhe.put("12E", panel12E);
        mapPanelGhe.put("12F", panel12F);

        mapPanelGhe.put("13A", panel13A);
        mapPanelGhe.put("13B", panel13B);
        mapPanelGhe.put("13C", panel13C);
        mapPanelGhe.put("13D", panel13D);
        mapPanelGhe.put("13E", panel13E);
        mapPanelGhe.put("13F", panel13F);

        mapPanelGhe.put("14A", panel14A);
        mapPanelGhe.put("14B", panel14B);
        mapPanelGhe.put("14C", panel14C);
        mapPanelGhe.put("14D", panel14D);
        mapPanelGhe.put("14E", panel14E);
        mapPanelGhe.put("14F", panel14F);

        mapPanelGhe.put("15A", panel15A);
        mapPanelGhe.put("15B", panel15B);
        mapPanelGhe.put("15C", panel15C);
        mapPanelGhe.put("15D", panel15D);
        mapPanelGhe.put("15E", panel15E);
        mapPanelGhe.put("15F", panel15F);

        mapPanelGhe.put("16A", panel16A);
        mapPanelGhe.put("16B", panel16B);
        mapPanelGhe.put("16C", panel16C);
        mapPanelGhe.put("16D", panel16D);
        mapPanelGhe.put("16E", panel16E);
        mapPanelGhe.put("16F", panel16F);

        mapPanelGhe.put("17A", panel17A);
        mapPanelGhe.put("17B", panel17B);
        mapPanelGhe.put("17C", panel17C);
        mapPanelGhe.put("17D", panel17D);
        mapPanelGhe.put("17E", panel17E);
        mapPanelGhe.put("17F", panel17F);

        mapPanelGhe.put("18A", panel18A);
        mapPanelGhe.put("18B", panel18B);
        mapPanelGhe.put("18C", panel18C);
        mapPanelGhe.put("18D", panel18D);
        mapPanelGhe.put("18E", panel18E);
        mapPanelGhe.put("18F", panel18F);

        mapPanelGhe.put("19A", panel19A);
        mapPanelGhe.put("19B", panel19B);
        mapPanelGhe.put("19C", panel19C);
        mapPanelGhe.put("19D", panel19D);
        mapPanelGhe.put("19E", panel19E);
        mapPanelGhe.put("19F", panel19F);
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel116 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel117 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel118 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jPanel120 = new javax.swing.JPanel();
        jPanel121 = new javax.swing.JPanel();
        panel7F = new javax.swing.JPanel();
        panel7D = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        lbl12A = new javax.swing.JLabel();
        lbl11A = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        panel11D = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        panel11C = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        panel11E = new javax.swing.JPanel();
        panel11F = new javax.swing.JPanel();
        panel12A = new javax.swing.JPanel();
        panel12F = new javax.swing.JPanel();
        panel12B = new javax.swing.JPanel();
        panel8A = new javax.swing.JPanel();
        panel12E = new javax.swing.JPanel();
        panel8C = new javax.swing.JPanel();
        panel12C = new javax.swing.JPanel();
        panel8B = new javax.swing.JPanel();
        panel12D = new javax.swing.JPanel();
        panel8D = new javax.swing.JPanel();
        panel8E = new javax.swing.JPanel();
        panel8F = new javax.swing.JPanel();
        panel9E = new javax.swing.JPanel();
        panel9A = new javax.swing.JPanel();
        panel9D = new javax.swing.JPanel();
        panel9F = new javax.swing.JPanel();
        panel13A = new javax.swing.JPanel();
        panel13D = new javax.swing.JPanel();
        panel13C = new javax.swing.JPanel();
        panel13F = new javax.swing.JPanel();
        panel13E = new javax.swing.JPanel();
        panel13B = new javax.swing.JPanel();
        panel14C = new javax.swing.JPanel();
        panel14A = new javax.swing.JPanel();
        panel14E = new javax.swing.JPanel();
        panel14B = new javax.swing.JPanel();
        panel14D = new javax.swing.JPanel();
        panel14F = new javax.swing.JPanel();
        panel15F = new javax.swing.JPanel();
        panel15D = new javax.swing.JPanel();
        panel15C = new javax.swing.JPanel();
        panel15A = new javax.swing.JPanel();
        panel15E = new javax.swing.JPanel();
        panel15B = new javax.swing.JPanel();
        panel16F = new javax.swing.JPanel();
        panel16C = new javax.swing.JPanel();
        panel16A = new javax.swing.JPanel();
        panel16E = new javax.swing.JPanel();
        panel16D = new javax.swing.JPanel();
        panel16B = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        panel17D = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        panel17A = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        panel17F = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        panel17B = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        panel17C = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        panel17E = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        panel18F = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        panel18A = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        panel18C = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        panel18E = new javax.swing.JPanel();
        lbl11B = new javax.swing.JLabel();
        panel18D = new javax.swing.JPanel();
        lbl12B = new javax.swing.JLabel();
        panel18B = new javax.swing.JPanel();
        lbl13B = new javax.swing.JLabel();
        panel19A = new javax.swing.JPanel();
        lbl14B = new javax.swing.JLabel();
        panel19B = new javax.swing.JPanel();
        lbl15B = new javax.swing.JLabel();
        panel19C = new javax.swing.JPanel();
        lbl16B = new javax.swing.JLabel();
        panel19D = new javax.swing.JPanel();
        panel2A = new javax.swing.JPanel();
        panel2B = new javax.swing.JPanel();
        panel2C = new javax.swing.JPanel();
        panel2D = new javax.swing.JPanel();
        panel2E = new javax.swing.JPanel();
        panel2F = new javax.swing.JPanel();
        lbl17B = new javax.swing.JLabel();
        panel19E = new javax.swing.JPanel();
        lbl18B = new javax.swing.JLabel();
        panel19F = new javax.swing.JPanel();
        lbl19B = new javax.swing.JLabel();
        lbl18A = new javax.swing.JLabel();
        lbl17A = new javax.swing.JLabel();
        lbl16A = new javax.swing.JLabel();
        lbl15A = new javax.swing.JLabel();
        lbl14A = new javax.swing.JLabel();
        lbl13A = new javax.swing.JLabel();
        lbl19A = new javax.swing.JLabel();
        panel3A = new javax.swing.JPanel();
        panel3B = new javax.swing.JPanel();
        panel3C = new javax.swing.JPanel();
        panel3D = new javax.swing.JPanel();
        panel3E = new javax.swing.JPanel();
        panel3F = new javax.swing.JPanel();
        panel4B = new javax.swing.JPanel();
        panel4C = new javax.swing.JPanel();
        panel4D = new javax.swing.JPanel();
        panel4F = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        panel4E = new javax.swing.JPanel();
        panel4A = new javax.swing.JPanel();
        panel5D = new javax.swing.JPanel();
        panel5C = new javax.swing.JPanel();
        panel5E = new javax.swing.JPanel();
        panel5A = new javax.swing.JPanel();
        panel5B = new javax.swing.JPanel();
        panel5F = new javax.swing.JPanel();
        panel6D = new javax.swing.JPanel();
        panel6A = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        panel9C = new javax.swing.JPanel();
        panel9B = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        panel10F = new javax.swing.JPanel();
        panel10D = new javax.swing.JPanel();
        panel10B = new javax.swing.JPanel();
        panel10A = new javax.swing.JPanel();
        panel10E = new javax.swing.JPanel();
        panel6E = new javax.swing.JPanel();
        panel10C = new javax.swing.JPanel();
        panel6F = new javax.swing.JPanel();
        panel11B = new javax.swing.JPanel();
        panel6B = new javax.swing.JPanel();
        panel11A = new javax.swing.JPanel();
        panel6C = new javax.swing.JPanel();
        panel7B = new javax.swing.JPanel();
        panel7E = new javax.swing.JPanel();
        panel7C = new javax.swing.JPanel();
        panel7A = new javax.swing.JPanel();
        panel1A = new javax.swing.JPanel();
        panel1B = new javax.swing.JPanel();
        panel1C = new javax.swing.JPanel();
        panel1D = new javax.swing.JPanel();
        panel1E = new javax.swing.JPanel();
        panel1F = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        jPanel236 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        jPanel237 = new javax.swing.JPanel();
        jLabel95 = new javax.swing.JLabel();
        jPanel238 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        btnXacNhanChonCho = new javax.swing.JButton();
        btnHuyChonCho = new javax.swing.JButton();
        noiDung = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bangHanhKhach = new javax.swing.JTable();
        noiDung1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel116.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout jPanel116Layout = new javax.swing.GroupLayout(jPanel116);
        jPanel116.setLayout(jPanel116Layout);
        jPanel116Layout.setHorizontalGroup(
            jPanel116Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel116Layout.setVerticalGroup(
            jPanel116Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel46.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel46.setText("Còn trống");

        jPanel117.setBackground(new java.awt.Color(255, 0, 51));

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Ghi chú");

        javax.swing.GroupLayout jPanel117Layout = new javax.swing.GroupLayout(jPanel117);
        jPanel117.setLayout(jPanel117Layout);
        jPanel117Layout.setHorizontalGroup(
            jPanel117Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel117Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel117Layout.setVerticalGroup(
            jPanel117Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel117Layout.createSequentialGroup()
                .addComponent(jLabel47)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        jPanel118.setBackground(new java.awt.Color(255, 255, 0));

        javax.swing.GroupLayout jPanel118Layout = new javax.swing.GroupLayout(jPanel118);
        jPanel118.setLayout(jPanel118Layout);
        jPanel118Layout.setHorizontalGroup(
            jPanel118Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel118Layout.setVerticalGroup(
            jPanel118Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel48.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel48.setText("Đã giữ chỗ");

        jPanel120.setBackground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout jPanel120Layout = new javax.swing.GroupLayout(jPanel120);
        jPanel120.setLayout(jPanel120Layout);
        jPanel120Layout.setHorizontalGroup(
            jPanel120Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel120Layout.setVerticalGroup(
            jPanel120Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel7F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel7FLayout = new javax.swing.GroupLayout(panel7F);
        panel7F.setLayout(panel7FLayout);
        panel7FLayout.setHorizontalGroup(
            panel7FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel7FLayout.setVerticalGroup(
            panel7FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel7D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel7DLayout = new javax.swing.GroupLayout(panel7D);
        panel7D.setLayout(panel7DLayout);
        panel7DLayout.setHorizontalGroup(
            panel7DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel7DLayout.setVerticalGroup(
            panel7DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel49.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("3");

        jLabel50.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("4");

        jLabel51.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("5");

        jLabel52.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("6");

        lbl12A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl12A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl12A.setText("12");
        lbl12A.setVisible(false);

        lbl11A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl11A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl11A.setText("11");
        lbl11A.setVisible(false);

        jLabel55.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("10");

        jLabel56.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("9");

        panel11D.setBackground(new java.awt.Color(51, 255, 51));
        panel11D.setVisible(false);

        javax.swing.GroupLayout panel11DLayout = new javax.swing.GroupLayout(panel11D);
        panel11D.setLayout(panel11DLayout);
        panel11DLayout.setHorizontalGroup(
            panel11DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel11DLayout.setVerticalGroup(
            panel11DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel57.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("8");

        panel11C.setBackground(new java.awt.Color(51, 255, 51));
        panel11C.setVisible(false);

        javax.swing.GroupLayout panel11CLayout = new javax.swing.GroupLayout(panel11C);
        panel11C.setLayout(panel11CLayout);
        panel11CLayout.setHorizontalGroup(
            panel11CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel11CLayout.setVerticalGroup(
            panel11CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel58.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText("7");

        panel11E.setBackground(new java.awt.Color(51, 255, 51));
        panel11E.setVisible(false);

        javax.swing.GroupLayout panel11ELayout = new javax.swing.GroupLayout(panel11E);
        panel11E.setLayout(panel11ELayout);
        panel11ELayout.setHorizontalGroup(
            panel11ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel11ELayout.setVerticalGroup(
            panel11ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel11F.setBackground(new java.awt.Color(51, 255, 51));
        panel11F.setVisible(false);

        javax.swing.GroupLayout panel11FLayout = new javax.swing.GroupLayout(panel11F);
        panel11F.setLayout(panel11FLayout);
        panel11FLayout.setHorizontalGroup(
            panel11FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel11FLayout.setVerticalGroup(
            panel11FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel12A.setBackground(new java.awt.Color(51, 255, 51));
        panel12A.setVisible(false);

        javax.swing.GroupLayout panel12ALayout = new javax.swing.GroupLayout(panel12A);
        panel12A.setLayout(panel12ALayout);
        panel12ALayout.setHorizontalGroup(
            panel12ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel12ALayout.setVerticalGroup(
            panel12ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel12F.setBackground(new java.awt.Color(51, 255, 51));
        panel12F.setVisible(false);

        javax.swing.GroupLayout panel12FLayout = new javax.swing.GroupLayout(panel12F);
        panel12F.setLayout(panel12FLayout);
        panel12FLayout.setHorizontalGroup(
            panel12FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel12FLayout.setVerticalGroup(
            panel12FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel12B.setBackground(new java.awt.Color(51, 255, 51));
        panel12B.setVisible(false);

        javax.swing.GroupLayout panel12BLayout = new javax.swing.GroupLayout(panel12B);
        panel12B.setLayout(panel12BLayout);
        panel12BLayout.setHorizontalGroup(
            panel12BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel12BLayout.setVerticalGroup(
            panel12BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel8A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel8ALayout = new javax.swing.GroupLayout(panel8A);
        panel8A.setLayout(panel8ALayout);
        panel8ALayout.setHorizontalGroup(
            panel8ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel8ALayout.setVerticalGroup(
            panel8ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel12E.setBackground(new java.awt.Color(51, 255, 51));
        panel12E.setVisible(false);

        javax.swing.GroupLayout panel12ELayout = new javax.swing.GroupLayout(panel12E);
        panel12E.setLayout(panel12ELayout);
        panel12ELayout.setHorizontalGroup(
            panel12ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel12ELayout.setVerticalGroup(
            panel12ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel8C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel8CLayout = new javax.swing.GroupLayout(panel8C);
        panel8C.setLayout(panel8CLayout);
        panel8CLayout.setHorizontalGroup(
            panel8CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel8CLayout.setVerticalGroup(
            panel8CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel12C.setBackground(new java.awt.Color(51, 255, 51));
        panel12C.setVisible(false);

        javax.swing.GroupLayout panel12CLayout = new javax.swing.GroupLayout(panel12C);
        panel12C.setLayout(panel12CLayout);
        panel12CLayout.setHorizontalGroup(
            panel12CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel12CLayout.setVerticalGroup(
            panel12CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel8B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel8BLayout = new javax.swing.GroupLayout(panel8B);
        panel8B.setLayout(panel8BLayout);
        panel8BLayout.setHorizontalGroup(
            panel8BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel8BLayout.setVerticalGroup(
            panel8BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel12D.setBackground(new java.awt.Color(51, 255, 51));
        panel12D.setVisible(false);

        javax.swing.GroupLayout panel12DLayout = new javax.swing.GroupLayout(panel12D);
        panel12D.setLayout(panel12DLayout);
        panel12DLayout.setHorizontalGroup(
            panel12DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel12DLayout.setVerticalGroup(
            panel12DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel8D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel8DLayout = new javax.swing.GroupLayout(panel8D);
        panel8D.setLayout(panel8DLayout);
        panel8DLayout.setHorizontalGroup(
            panel8DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel8DLayout.setVerticalGroup(
            panel8DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel8E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel8ELayout = new javax.swing.GroupLayout(panel8E);
        panel8E.setLayout(panel8ELayout);
        panel8ELayout.setHorizontalGroup(
            panel8ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel8ELayout.setVerticalGroup(
            panel8ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel8F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel8FLayout = new javax.swing.GroupLayout(panel8F);
        panel8F.setLayout(panel8FLayout);
        panel8FLayout.setHorizontalGroup(
            panel8FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel8FLayout.setVerticalGroup(
            panel8FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel9E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel9ELayout = new javax.swing.GroupLayout(panel9E);
        panel9E.setLayout(panel9ELayout);
        panel9ELayout.setHorizontalGroup(
            panel9ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel9ELayout.setVerticalGroup(
            panel9ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel9A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel9ALayout = new javax.swing.GroupLayout(panel9A);
        panel9A.setLayout(panel9ALayout);
        panel9ALayout.setHorizontalGroup(
            panel9ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel9ALayout.setVerticalGroup(
            panel9ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel9D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel9DLayout = new javax.swing.GroupLayout(panel9D);
        panel9D.setLayout(panel9DLayout);
        panel9DLayout.setHorizontalGroup(
            panel9DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel9DLayout.setVerticalGroup(
            panel9DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel9F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel9FLayout = new javax.swing.GroupLayout(panel9F);
        panel9F.setLayout(panel9FLayout);
        panel9FLayout.setHorizontalGroup(
            panel9FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel9FLayout.setVerticalGroup(
            panel9FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel13A.setBackground(new java.awt.Color(51, 255, 51));
        panel13A.setVisible(false);

        javax.swing.GroupLayout panel13ALayout = new javax.swing.GroupLayout(panel13A);
        panel13A.setLayout(panel13ALayout);
        panel13ALayout.setHorizontalGroup(
            panel13ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel13ALayout.setVerticalGroup(
            panel13ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel13D.setBackground(new java.awt.Color(51, 255, 51));
        panel13D.setVisible(false);

        javax.swing.GroupLayout panel13DLayout = new javax.swing.GroupLayout(panel13D);
        panel13D.setLayout(panel13DLayout);
        panel13DLayout.setHorizontalGroup(
            panel13DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel13DLayout.setVerticalGroup(
            panel13DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel13C.setBackground(new java.awt.Color(51, 255, 51));
        panel13C.setVisible(false);

        javax.swing.GroupLayout panel13CLayout = new javax.swing.GroupLayout(panel13C);
        panel13C.setLayout(panel13CLayout);
        panel13CLayout.setHorizontalGroup(
            panel13CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel13CLayout.setVerticalGroup(
            panel13CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel13F.setBackground(new java.awt.Color(51, 255, 51));
        panel13F.setVisible(false);

        javax.swing.GroupLayout panel13FLayout = new javax.swing.GroupLayout(panel13F);
        panel13F.setLayout(panel13FLayout);
        panel13FLayout.setHorizontalGroup(
            panel13FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel13FLayout.setVerticalGroup(
            panel13FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel13E.setBackground(new java.awt.Color(51, 255, 51));
        panel13E.setVisible(false);

        javax.swing.GroupLayout panel13ELayout = new javax.swing.GroupLayout(panel13E);
        panel13E.setLayout(panel13ELayout);
        panel13ELayout.setHorizontalGroup(
            panel13ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel13ELayout.setVerticalGroup(
            panel13ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel13B.setBackground(new java.awt.Color(51, 255, 51));
        panel13B.setVisible(false);

        javax.swing.GroupLayout panel13BLayout = new javax.swing.GroupLayout(panel13B);
        panel13B.setLayout(panel13BLayout);
        panel13BLayout.setHorizontalGroup(
            panel13BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel13BLayout.setVerticalGroup(
            panel13BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel14C.setBackground(new java.awt.Color(51, 255, 51));
        panel14C.setVisible(false);

        javax.swing.GroupLayout panel14CLayout = new javax.swing.GroupLayout(panel14C);
        panel14C.setLayout(panel14CLayout);
        panel14CLayout.setHorizontalGroup(
            panel14CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel14CLayout.setVerticalGroup(
            panel14CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel14A.setBackground(new java.awt.Color(51, 255, 51));
        panel14A.setVisible(false);

        javax.swing.GroupLayout panel14ALayout = new javax.swing.GroupLayout(panel14A);
        panel14A.setLayout(panel14ALayout);
        panel14ALayout.setHorizontalGroup(
            panel14ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel14ALayout.setVerticalGroup(
            panel14ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel14E.setBackground(new java.awt.Color(51, 255, 51));
        panel14E.setVisible(false);

        javax.swing.GroupLayout panel14ELayout = new javax.swing.GroupLayout(panel14E);
        panel14E.setLayout(panel14ELayout);
        panel14ELayout.setHorizontalGroup(
            panel14ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel14ELayout.setVerticalGroup(
            panel14ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel14B.setBackground(new java.awt.Color(51, 255, 51));
        panel14B.setVisible(false);

        javax.swing.GroupLayout panel14BLayout = new javax.swing.GroupLayout(panel14B);
        panel14B.setLayout(panel14BLayout);
        panel14BLayout.setHorizontalGroup(
            panel14BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel14BLayout.setVerticalGroup(
            panel14BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel14D.setBackground(new java.awt.Color(51, 255, 51));
        panel14D.setVisible(false);

        javax.swing.GroupLayout panel14DLayout = new javax.swing.GroupLayout(panel14D);
        panel14D.setLayout(panel14DLayout);
        panel14DLayout.setHorizontalGroup(
            panel14DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel14DLayout.setVerticalGroup(
            panel14DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel14F.setBackground(new java.awt.Color(51, 255, 51));
        panel14F.setVisible(false);

        javax.swing.GroupLayout panel14FLayout = new javax.swing.GroupLayout(panel14F);
        panel14F.setLayout(panel14FLayout);
        panel14FLayout.setHorizontalGroup(
            panel14FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel14FLayout.setVerticalGroup(
            panel14FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel15F.setBackground(new java.awt.Color(51, 255, 51));
        panel15F.setVisible(false);

        javax.swing.GroupLayout panel15FLayout = new javax.swing.GroupLayout(panel15F);
        panel15F.setLayout(panel15FLayout);
        panel15FLayout.setHorizontalGroup(
            panel15FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel15FLayout.setVerticalGroup(
            panel15FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel15D.setBackground(new java.awt.Color(51, 255, 51));
        panel15D.setVisible(false);

        javax.swing.GroupLayout panel15DLayout = new javax.swing.GroupLayout(panel15D);
        panel15D.setLayout(panel15DLayout);
        panel15DLayout.setHorizontalGroup(
            panel15DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel15DLayout.setVerticalGroup(
            panel15DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel15C.setBackground(new java.awt.Color(51, 255, 51));
        panel15C.setVisible(false);

        javax.swing.GroupLayout panel15CLayout = new javax.swing.GroupLayout(panel15C);
        panel15C.setLayout(panel15CLayout);
        panel15CLayout.setHorizontalGroup(
            panel15CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel15CLayout.setVerticalGroup(
            panel15CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel15A.setBackground(new java.awt.Color(51, 255, 51));
        panel15A.setVisible(false);

        javax.swing.GroupLayout panel15ALayout = new javax.swing.GroupLayout(panel15A);
        panel15A.setLayout(panel15ALayout);
        panel15ALayout.setHorizontalGroup(
            panel15ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel15ALayout.setVerticalGroup(
            panel15ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel15E.setBackground(new java.awt.Color(51, 255, 51));
        panel15E.setVisible(false);

        javax.swing.GroupLayout panel15ELayout = new javax.swing.GroupLayout(panel15E);
        panel15E.setLayout(panel15ELayout);
        panel15ELayout.setHorizontalGroup(
            panel15ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel15ELayout.setVerticalGroup(
            panel15ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel15B.setBackground(new java.awt.Color(51, 255, 51));
        panel15B.setVisible(false);

        javax.swing.GroupLayout panel15BLayout = new javax.swing.GroupLayout(panel15B);
        panel15B.setLayout(panel15BLayout);
        panel15BLayout.setHorizontalGroup(
            panel15BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel15BLayout.setVerticalGroup(
            panel15BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel16F.setBackground(new java.awt.Color(51, 255, 51));
        panel16F.setVisible(false);

        javax.swing.GroupLayout panel16FLayout = new javax.swing.GroupLayout(panel16F);
        panel16F.setLayout(panel16FLayout);
        panel16FLayout.setHorizontalGroup(
            panel16FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel16FLayout.setVerticalGroup(
            panel16FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel16C.setBackground(new java.awt.Color(51, 255, 51));
        panel16C.setVisible(false);

        javax.swing.GroupLayout panel16CLayout = new javax.swing.GroupLayout(panel16C);
        panel16C.setLayout(panel16CLayout);
        panel16CLayout.setHorizontalGroup(
            panel16CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel16CLayout.setVerticalGroup(
            panel16CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel16A.setBackground(new java.awt.Color(51, 255, 51));
        panel16A.setVisible(false);

        javax.swing.GroupLayout panel16ALayout = new javax.swing.GroupLayout(panel16A);
        panel16A.setLayout(panel16ALayout);
        panel16ALayout.setHorizontalGroup(
            panel16ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel16ALayout.setVerticalGroup(
            panel16ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel16E.setBackground(new java.awt.Color(51, 255, 51));
        panel16E.setVisible(false);

        javax.swing.GroupLayout panel16ELayout = new javax.swing.GroupLayout(panel16E);
        panel16E.setLayout(panel16ELayout);
        panel16ELayout.setHorizontalGroup(
            panel16ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel16ELayout.setVerticalGroup(
            panel16ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel16D.setBackground(new java.awt.Color(51, 255, 51));
        panel16D.setVisible(false);

        javax.swing.GroupLayout panel16DLayout = new javax.swing.GroupLayout(panel16D);
        panel16D.setLayout(panel16DLayout);
        panel16DLayout.setHorizontalGroup(
            panel16DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel16DLayout.setVerticalGroup(
            panel16DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel16B.setBackground(new java.awt.Color(51, 255, 51));
        panel16B.setVisible(false);

        javax.swing.GroupLayout panel16BLayout = new javax.swing.GroupLayout(panel16B);
        panel16B.setLayout(panel16BLayout);
        panel16BLayout.setHorizontalGroup(
            panel16BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel16BLayout.setVerticalGroup(
            panel16BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel59.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("1");

        panel17D.setBackground(new java.awt.Color(51, 255, 51));
        panel17D.setVisible(false);

        javax.swing.GroupLayout panel17DLayout = new javax.swing.GroupLayout(panel17D);
        panel17D.setLayout(panel17DLayout);
        panel17DLayout.setHorizontalGroup(
            panel17DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel17DLayout.setVerticalGroup(
            panel17DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel60.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("2");

        panel17A.setBackground(new java.awt.Color(51, 255, 51));
        panel17A.setVisible(false);

        javax.swing.GroupLayout panel17ALayout = new javax.swing.GroupLayout(panel17A);
        panel17A.setLayout(panel17ALayout);
        panel17ALayout.setHorizontalGroup(
            panel17ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel17ALayout.setVerticalGroup(
            panel17ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel61.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("3");

        panel17F.setBackground(new java.awt.Color(51, 255, 51));
        panel17F.setVisible(false);

        javax.swing.GroupLayout panel17FLayout = new javax.swing.GroupLayout(panel17F);
        panel17F.setLayout(panel17FLayout);
        panel17FLayout.setHorizontalGroup(
            panel17FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel17FLayout.setVerticalGroup(
            panel17FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel62.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setText("4");

        panel17B.setBackground(new java.awt.Color(51, 255, 51));
        panel17B.setVisible(false);

        javax.swing.GroupLayout panel17BLayout = new javax.swing.GroupLayout(panel17B);
        panel17B.setLayout(panel17BLayout);
        panel17BLayout.setHorizontalGroup(
            panel17BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel17BLayout.setVerticalGroup(
            panel17BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel63.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("5");

        panel17C.setBackground(new java.awt.Color(51, 255, 51));
        panel17C.setVisible(false);

        javax.swing.GroupLayout panel17CLayout = new javax.swing.GroupLayout(panel17C);
        panel17C.setLayout(panel17CLayout);
        panel17CLayout.setHorizontalGroup(
            panel17CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel17CLayout.setVerticalGroup(
            panel17CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel64.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("6");

        panel17E.setBackground(new java.awt.Color(51, 255, 51));
        panel17E.setVisible(false);

        javax.swing.GroupLayout panel17ELayout = new javax.swing.GroupLayout(panel17E);
        panel17E.setLayout(panel17ELayout);
        panel17ELayout.setHorizontalGroup(
            panel17ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel17ELayout.setVerticalGroup(
            panel17ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel65.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("7");

        panel18F.setBackground(new java.awt.Color(51, 255, 51));
        panel18F.setVisible(false);

        javax.swing.GroupLayout panel18FLayout = new javax.swing.GroupLayout(panel18F);
        panel18F.setLayout(panel18FLayout);
        panel18FLayout.setHorizontalGroup(
            panel18FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel18FLayout.setVerticalGroup(
            panel18FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel66.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("8");

        panel18A.setBackground(new java.awt.Color(51, 255, 51));
        panel18A.setVisible(false);

        javax.swing.GroupLayout panel18ALayout = new javax.swing.GroupLayout(panel18A);
        panel18A.setLayout(panel18ALayout);
        panel18ALayout.setHorizontalGroup(
            panel18ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel18ALayout.setVerticalGroup(
            panel18ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel67.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("9");

        panel18C.setBackground(new java.awt.Color(51, 255, 51));
        panel18C.setVisible(false);

        javax.swing.GroupLayout panel18CLayout = new javax.swing.GroupLayout(panel18C);
        panel18C.setLayout(panel18CLayout);
        panel18CLayout.setHorizontalGroup(
            panel18CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel18CLayout.setVerticalGroup(
            panel18CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel68.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("10");

        panel18E.setBackground(new java.awt.Color(51, 255, 51));
        panel18E.setVisible(false);

        javax.swing.GroupLayout panel18ELayout = new javax.swing.GroupLayout(panel18E);
        panel18E.setLayout(panel18ELayout);
        panel18ELayout.setHorizontalGroup(
            panel18ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel18ELayout.setVerticalGroup(
            panel18ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl11B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl11B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl11B.setText("11");
        lbl11B.setVisible(false);

        panel18D.setBackground(new java.awt.Color(51, 255, 51));
        panel18D.setVisible(false);

        javax.swing.GroupLayout panel18DLayout = new javax.swing.GroupLayout(panel18D);
        panel18D.setLayout(panel18DLayout);
        panel18DLayout.setHorizontalGroup(
            panel18DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel18DLayout.setVerticalGroup(
            panel18DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl12B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl12B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl12B.setText("12");
        lbl12B.setVisible(false);

        panel18B.setBackground(new java.awt.Color(51, 255, 51));
        panel18B.setVisible(false);

        javax.swing.GroupLayout panel18BLayout = new javax.swing.GroupLayout(panel18B);
        panel18B.setLayout(panel18BLayout);
        panel18BLayout.setHorizontalGroup(
            panel18BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel18BLayout.setVerticalGroup(
            panel18BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl13B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl13B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl13B.setText("13");
        lbl13B.setVisible(false);

        panel19A.setBackground(new java.awt.Color(51, 255, 51));
        panel19A.setVisible(false);

        javax.swing.GroupLayout panel19ALayout = new javax.swing.GroupLayout(panel19A);
        panel19A.setLayout(panel19ALayout);
        panel19ALayout.setHorizontalGroup(
            panel19ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel19ALayout.setVerticalGroup(
            panel19ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl14B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl14B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl14B.setText("14");
        lbl14B.setVisible(false);

        panel19B.setBackground(new java.awt.Color(51, 255, 51));
        panel19B.setVisible(false);

        javax.swing.GroupLayout panel19BLayout = new javax.swing.GroupLayout(panel19B);
        panel19B.setLayout(panel19BLayout);
        panel19BLayout.setHorizontalGroup(
            panel19BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel19BLayout.setVerticalGroup(
            panel19BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl15B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl15B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl15B.setText("15");
        lbl15B.setVisible(false);

        panel19C.setBackground(new java.awt.Color(51, 255, 51));
        panel19C.setVisible(false);

        javax.swing.GroupLayout panel19CLayout = new javax.swing.GroupLayout(panel19C);
        panel19C.setLayout(panel19CLayout);
        panel19CLayout.setHorizontalGroup(
            panel19CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel19CLayout.setVerticalGroup(
            panel19CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl16B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl16B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl16B.setText("16");
        lbl16B.setVisible(false);

        panel19D.setBackground(new java.awt.Color(51, 255, 51));
        panel19D.setVisible(false);

        javax.swing.GroupLayout panel19DLayout = new javax.swing.GroupLayout(panel19D);
        panel19D.setLayout(panel19DLayout);
        panel19DLayout.setHorizontalGroup(
            panel19DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel19DLayout.setVerticalGroup(
            panel19DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel2A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel2ALayout = new javax.swing.GroupLayout(panel2A);
        panel2A.setLayout(panel2ALayout);
        panel2ALayout.setHorizontalGroup(
            panel2ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel2ALayout.setVerticalGroup(
            panel2ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel2B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel2BLayout = new javax.swing.GroupLayout(panel2B);
        panel2B.setLayout(panel2BLayout);
        panel2BLayout.setHorizontalGroup(
            panel2BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel2BLayout.setVerticalGroup(
            panel2BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel2C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel2CLayout = new javax.swing.GroupLayout(panel2C);
        panel2C.setLayout(panel2CLayout);
        panel2CLayout.setHorizontalGroup(
            panel2CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel2CLayout.setVerticalGroup(
            panel2CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel2D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel2DLayout = new javax.swing.GroupLayout(panel2D);
        panel2D.setLayout(panel2DLayout);
        panel2DLayout.setHorizontalGroup(
            panel2DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel2DLayout.setVerticalGroup(
            panel2DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel2E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel2ELayout = new javax.swing.GroupLayout(panel2E);
        panel2E.setLayout(panel2ELayout);
        panel2ELayout.setHorizontalGroup(
            panel2ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel2ELayout.setVerticalGroup(
            panel2ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel2F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel2FLayout = new javax.swing.GroupLayout(panel2F);
        panel2F.setLayout(panel2FLayout);
        panel2FLayout.setHorizontalGroup(
            panel2FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel2FLayout.setVerticalGroup(
            panel2FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl17B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl17B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl17B.setText("17");
        lbl17B.setVisible(false);

        panel19E.setBackground(new java.awt.Color(51, 255, 51));
        panel19E.setVisible(false);

        javax.swing.GroupLayout panel19ELayout = new javax.swing.GroupLayout(panel19E);
        panel19E.setLayout(panel19ELayout);
        panel19ELayout.setHorizontalGroup(
            panel19ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel19ELayout.setVerticalGroup(
            panel19ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl18B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl18B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl18B.setText("18");
        lbl18B.setVisible(false);

        panel19F.setBackground(new java.awt.Color(51, 255, 51));
        panel19F.setVisible(false);

        javax.swing.GroupLayout panel19FLayout = new javax.swing.GroupLayout(panel19F);
        panel19F.setLayout(panel19FLayout);
        panel19FLayout.setHorizontalGroup(
            panel19FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel19FLayout.setVerticalGroup(
            panel19FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        lbl19B.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl19B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl19B.setText("19");
        lbl19B.setVisible(false);

        lbl18A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl18A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl18A.setText("18");
        lbl18A.setVisible(false);

        lbl17A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl17A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl17A.setText("17");
        lbl17A.setVisible(false);

        lbl16A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl16A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl16A.setText("16");
        lbl16A.setVisible(false);

        lbl15A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl15A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl15A.setText("15");
        lbl15A.setVisible(false);

        lbl14A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl14A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl14A.setText("14");
        lbl14A.setVisible(false);

        lbl13A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl13A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl13A.setText("13");
        lbl13A.setVisible(false);

        lbl19A.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        lbl19A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl19A.setText("19");
        lbl19A.setVisible(false);

        panel3A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel3ALayout = new javax.swing.GroupLayout(panel3A);
        panel3A.setLayout(panel3ALayout);
        panel3ALayout.setHorizontalGroup(
            panel3ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel3ALayout.setVerticalGroup(
            panel3ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel3B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel3BLayout = new javax.swing.GroupLayout(panel3B);
        panel3B.setLayout(panel3BLayout);
        panel3BLayout.setHorizontalGroup(
            panel3BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel3BLayout.setVerticalGroup(
            panel3BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel3C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel3CLayout = new javax.swing.GroupLayout(panel3C);
        panel3C.setLayout(panel3CLayout);
        panel3CLayout.setHorizontalGroup(
            panel3CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel3CLayout.setVerticalGroup(
            panel3CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel3D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel3DLayout = new javax.swing.GroupLayout(panel3D);
        panel3D.setLayout(panel3DLayout);
        panel3DLayout.setHorizontalGroup(
            panel3DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel3DLayout.setVerticalGroup(
            panel3DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel3E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel3ELayout = new javax.swing.GroupLayout(panel3E);
        panel3E.setLayout(panel3ELayout);
        panel3ELayout.setHorizontalGroup(
            panel3ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel3ELayout.setVerticalGroup(
            panel3ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel3F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel3FLayout = new javax.swing.GroupLayout(panel3F);
        panel3F.setLayout(panel3FLayout);
        panel3FLayout.setHorizontalGroup(
            panel3FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel3FLayout.setVerticalGroup(
            panel3FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel4B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel4BLayout = new javax.swing.GroupLayout(panel4B);
        panel4B.setLayout(panel4BLayout);
        panel4BLayout.setHorizontalGroup(
            panel4BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel4BLayout.setVerticalGroup(
            panel4BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel4C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel4CLayout = new javax.swing.GroupLayout(panel4C);
        panel4C.setLayout(panel4CLayout);
        panel4CLayout.setHorizontalGroup(
            panel4CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel4CLayout.setVerticalGroup(
            panel4CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel4D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel4DLayout = new javax.swing.GroupLayout(panel4D);
        panel4D.setLayout(panel4DLayout);
        panel4DLayout.setHorizontalGroup(
            panel4DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel4DLayout.setVerticalGroup(
            panel4DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel4F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel4FLayout = new javax.swing.GroupLayout(panel4F);
        panel4F.setLayout(panel4FLayout);
        panel4FLayout.setHorizontalGroup(
            panel4FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel4FLayout.setVerticalGroup(
            panel4FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel85.setFont(new java.awt.Font("UTM Times", 0, 25)); // NOI18N
        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel85.setText("A");

        jLabel86.setFont(new java.awt.Font("UTM Times", 0, 25)); // NOI18N
        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setText("B");

        jLabel87.setFont(new java.awt.Font("UTM Times", 0, 25)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel87.setText("C");

        panel4E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel4ELayout = new javax.swing.GroupLayout(panel4E);
        panel4E.setLayout(panel4ELayout);
        panel4ELayout.setHorizontalGroup(
            panel4ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel4ELayout.setVerticalGroup(
            panel4ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel4A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel4ALayout = new javax.swing.GroupLayout(panel4A);
        panel4A.setLayout(panel4ALayout);
        panel4ALayout.setHorizontalGroup(
            panel4ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel4ALayout.setVerticalGroup(
            panel4ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel5D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel5DLayout = new javax.swing.GroupLayout(panel5D);
        panel5D.setLayout(panel5DLayout);
        panel5DLayout.setHorizontalGroup(
            panel5DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel5DLayout.setVerticalGroup(
            panel5DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel5C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel5CLayout = new javax.swing.GroupLayout(panel5C);
        panel5C.setLayout(panel5CLayout);
        panel5CLayout.setHorizontalGroup(
            panel5CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel5CLayout.setVerticalGroup(
            panel5CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel5E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel5ELayout = new javax.swing.GroupLayout(panel5E);
        panel5E.setLayout(panel5ELayout);
        panel5ELayout.setHorizontalGroup(
            panel5ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel5ELayout.setVerticalGroup(
            panel5ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel5A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel5ALayout = new javax.swing.GroupLayout(panel5A);
        panel5A.setLayout(panel5ALayout);
        panel5ALayout.setHorizontalGroup(
            panel5ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel5ALayout.setVerticalGroup(
            panel5ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel5B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel5BLayout = new javax.swing.GroupLayout(panel5B);
        panel5B.setLayout(panel5BLayout);
        panel5BLayout.setHorizontalGroup(
            panel5BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel5BLayout.setVerticalGroup(
            panel5BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel5F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel5FLayout = new javax.swing.GroupLayout(panel5F);
        panel5F.setLayout(panel5FLayout);
        panel5FLayout.setHorizontalGroup(
            panel5FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel5FLayout.setVerticalGroup(
            panel5FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel6D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel6DLayout = new javax.swing.GroupLayout(panel6D);
        panel6D.setLayout(panel6DLayout);
        panel6DLayout.setHorizontalGroup(
            panel6DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel6DLayout.setVerticalGroup(
            panel6DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel6A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel6ALayout = new javax.swing.GroupLayout(panel6A);
        panel6A.setLayout(panel6ALayout);
        panel6ALayout.setHorizontalGroup(
            panel6ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel6ALayout.setVerticalGroup(
            panel6ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel88.setFont(new java.awt.Font("UTM Times", 0, 25)); // NOI18N
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel88.setText("D");

        jLabel89.setFont(new java.awt.Font("UTM Times", 0, 25)); // NOI18N
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("E");

        jLabel90.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel90.setText("1");

        jLabel91.setFont(new java.awt.Font("UTM Times", 0, 25)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("F");

        panel9C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel9CLayout = new javax.swing.GroupLayout(panel9C);
        panel9C.setLayout(panel9CLayout);
        panel9CLayout.setHorizontalGroup(
            panel9CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel9CLayout.setVerticalGroup(
            panel9CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel9B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel9BLayout = new javax.swing.GroupLayout(panel9B);
        panel9B.setLayout(panel9BLayout);
        panel9BLayout.setHorizontalGroup(
            panel9BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel9BLayout.setVerticalGroup(
            panel9BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel92.setFont(new java.awt.Font("UTM Times", 0, 21)); // NOI18N
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("2");

        panel10F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel10FLayout = new javax.swing.GroupLayout(panel10F);
        panel10F.setLayout(panel10FLayout);
        panel10FLayout.setHorizontalGroup(
            panel10FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel10FLayout.setVerticalGroup(
            panel10FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel10D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel10DLayout = new javax.swing.GroupLayout(panel10D);
        panel10D.setLayout(panel10DLayout);
        panel10DLayout.setHorizontalGroup(
            panel10DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel10DLayout.setVerticalGroup(
            panel10DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel10B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel10BLayout = new javax.swing.GroupLayout(panel10B);
        panel10B.setLayout(panel10BLayout);
        panel10BLayout.setHorizontalGroup(
            panel10BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel10BLayout.setVerticalGroup(
            panel10BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel10A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel10ALayout = new javax.swing.GroupLayout(panel10A);
        panel10A.setLayout(panel10ALayout);
        panel10ALayout.setHorizontalGroup(
            panel10ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel10ALayout.setVerticalGroup(
            panel10ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel10E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel10ELayout = new javax.swing.GroupLayout(panel10E);
        panel10E.setLayout(panel10ELayout);
        panel10ELayout.setHorizontalGroup(
            panel10ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel10ELayout.setVerticalGroup(
            panel10ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel6E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel6ELayout = new javax.swing.GroupLayout(panel6E);
        panel6E.setLayout(panel6ELayout);
        panel6ELayout.setHorizontalGroup(
            panel6ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel6ELayout.setVerticalGroup(
            panel6ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel10C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel10CLayout = new javax.swing.GroupLayout(panel10C);
        panel10C.setLayout(panel10CLayout);
        panel10CLayout.setHorizontalGroup(
            panel10CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel10CLayout.setVerticalGroup(
            panel10CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel6F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel6FLayout = new javax.swing.GroupLayout(panel6F);
        panel6F.setLayout(panel6FLayout);
        panel6FLayout.setHorizontalGroup(
            panel6FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel6FLayout.setVerticalGroup(
            panel6FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel11B.setBackground(new java.awt.Color(51, 255, 51));
        panel11B.setVisible(false);

        javax.swing.GroupLayout panel11BLayout = new javax.swing.GroupLayout(panel11B);
        panel11B.setLayout(panel11BLayout);
        panel11BLayout.setHorizontalGroup(
            panel11BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel11BLayout.setVerticalGroup(
            panel11BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel6B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel6BLayout = new javax.swing.GroupLayout(panel6B);
        panel6B.setLayout(panel6BLayout);
        panel6BLayout.setHorizontalGroup(
            panel6BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel6BLayout.setVerticalGroup(
            panel6BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel11A.setBackground(new java.awt.Color(51, 255, 51));
        panel11A.setVisible(false);

        javax.swing.GroupLayout panel11ALayout = new javax.swing.GroupLayout(panel11A);
        panel11A.setLayout(panel11ALayout);
        panel11ALayout.setHorizontalGroup(
            panel11ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel11ALayout.setVerticalGroup(
            panel11ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel6C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel6CLayout = new javax.swing.GroupLayout(panel6C);
        panel6C.setLayout(panel6CLayout);
        panel6CLayout.setHorizontalGroup(
            panel6CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel6CLayout.setVerticalGroup(
            panel6CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel7B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel7BLayout = new javax.swing.GroupLayout(panel7B);
        panel7B.setLayout(panel7BLayout);
        panel7BLayout.setHorizontalGroup(
            panel7BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel7BLayout.setVerticalGroup(
            panel7BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel7E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel7ELayout = new javax.swing.GroupLayout(panel7E);
        panel7E.setLayout(panel7ELayout);
        panel7ELayout.setHorizontalGroup(
            panel7ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel7ELayout.setVerticalGroup(
            panel7ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel7C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel7CLayout = new javax.swing.GroupLayout(panel7C);
        panel7C.setLayout(panel7CLayout);
        panel7CLayout.setHorizontalGroup(
            panel7CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel7CLayout.setVerticalGroup(
            panel7CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel7A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel7ALayout = new javax.swing.GroupLayout(panel7A);
        panel7A.setLayout(panel7ALayout);
        panel7ALayout.setHorizontalGroup(
            panel7ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel7ALayout.setVerticalGroup(
            panel7ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel1A.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel1ALayout = new javax.swing.GroupLayout(panel1A);
        panel1A.setLayout(panel1ALayout);
        panel1ALayout.setHorizontalGroup(
            panel1ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel1ALayout.setVerticalGroup(
            panel1ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel1B.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel1BLayout = new javax.swing.GroupLayout(panel1B);
        panel1B.setLayout(panel1BLayout);
        panel1BLayout.setHorizontalGroup(
            panel1BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel1BLayout.setVerticalGroup(
            panel1BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel1C.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel1CLayout = new javax.swing.GroupLayout(panel1C);
        panel1C.setLayout(panel1CLayout);
        panel1CLayout.setHorizontalGroup(
            panel1CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel1CLayout.setVerticalGroup(
            panel1CLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel1D.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel1DLayout = new javax.swing.GroupLayout(panel1D);
        panel1D.setLayout(panel1DLayout);
        panel1DLayout.setHorizontalGroup(
            panel1DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel1DLayout.setVerticalGroup(
            panel1DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel1E.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel1ELayout = new javax.swing.GroupLayout(panel1E);
        panel1E.setLayout(panel1ELayout);
        panel1ELayout.setHorizontalGroup(
            panel1ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel1ELayout.setVerticalGroup(
            panel1ELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        panel1F.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panel1FLayout = new javax.swing.GroupLayout(panel1F);
        panel1F.setLayout(panel1FLayout);
        panel1FLayout.setHorizontalGroup(
            panel1FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        panel1FLayout.setVerticalGroup(
            panel1FLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel121Layout = new javax.swing.GroupLayout(jPanel121);
        jPanel121.setLayout(jPanel121Layout);
        jPanel121Layout.setHorizontalGroup(
            jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel121Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl11A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl12A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl13A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl14A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl15A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl16A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl17A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl18A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl19A, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel2A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel2B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel2C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel2D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel2E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel2F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel121Layout.createSequentialGroup()
                            .addComponent(panel3A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel3B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel3C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel3D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel3E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel3F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel4A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel4B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel4C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel4D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel4E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel4F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel5A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel5B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel5C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel5D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel5E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel5F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel6A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel6B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel6C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel6D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel6E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel6F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel7A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel7B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel7C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel7D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel7E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel7F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel8A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel8B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel8C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel8D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel8E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel8F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel9A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel9B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel9C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel9D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel9E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel9F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel10A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel10B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel10C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel10D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel10E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel10F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel11A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel11B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel11C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel11D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel11E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel11F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel12A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel12B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel12C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel12D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel12E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel12F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel13A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel13B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel13C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel13D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel13E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel13F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel14A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel14B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel14C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel14D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel14E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel14F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel15A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel15B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel15C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel15D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel15E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel15F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel16A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel16B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel16C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel16D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel16E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel16F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel17A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel17B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel17C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel17D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel17E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel17F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel18A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel18B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel18C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel18D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel18E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel18F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel121Layout.createSequentialGroup()
                            .addComponent(panel19A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel19B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel19C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(panel19D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel19E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel19F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel121Layout.createSequentialGroup()
                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel121Layout.createSequentialGroup()
                        .addComponent(panel1A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel1B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel1C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(panel1D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel1E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel1F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl11B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl12B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl13B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl14B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl15B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl16B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl17B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl18B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl19B, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel121Layout.setVerticalGroup(
            jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel121Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel88)
                        .addComponent(jLabel89)
                        .addComponent(jLabel91))
                    .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel85)
                        .addComponent(jLabel86)
                        .addComponent(jLabel87)))
                .addGap(10, 10, 10)
                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel121Layout.createSequentialGroup()
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel1A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel1B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel1C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel1D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel1E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel1F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel2A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel2B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel2C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel2D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel2E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel2F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel3A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel3B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel3C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel3D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel3E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel3F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel4A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel4B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel4C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel4D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel4E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel4F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel5A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel5B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel5C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel5D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel5E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel5F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel6A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel6B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel6C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel6D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel6E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel6F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel121Layout.createSequentialGroup()
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel7A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel7B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel7C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel7D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel7E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel7F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel8A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel8B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel8C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel8D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel8E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel8F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel9A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel9B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel9C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel9D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel9E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel9F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel10A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel10B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel10C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel10D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel10E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel10F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel11A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel11B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel11C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel11D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel11E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel11F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel12A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel12B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel12C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel12D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel12E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel12F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel121Layout.createSequentialGroup()
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl11A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl12A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel121Layout.createSequentialGroup()
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel13A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel13B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel13C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel13D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel13E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel13F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel14A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel14B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel14C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel14D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel14E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel14F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel15A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel15B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel15C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel15D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel15E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel15F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel16A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel16B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel16C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel16D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel16E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel16F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel17A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel17B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel17C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel17D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel17E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel17F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panel18A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel18B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel18C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel18D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel18E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel18F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel121Layout.createSequentialGroup()
                                .addComponent(lbl13A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl14A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl15A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl16A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl17A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl18A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel19A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel19B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel19C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel19D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel19E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel19F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl19A, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel121Layout.createSequentialGroup()
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl11B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl12B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl13B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl14B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl15B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl16B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl17B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl18B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lbl19B, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel93.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel93.setText("Hàng ghế gần lối thoát hiểm");

        jPanel236.setBackground(new java.awt.Color(204, 0, 255));

        javax.swing.GroupLayout jPanel236Layout = new javax.swing.GroupLayout(jPanel236);
        jPanel236.setLayout(jPanel236Layout);
        jPanel236Layout.setHorizontalGroup(
            jPanel236Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel236Layout.setVerticalGroup(
            jPanel236Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel94.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel94.setText("Hàng ghế phía trước");

        jPanel237.setBackground(new java.awt.Color(255, 0, 255));

        javax.swing.GroupLayout jPanel237Layout = new javax.swing.GroupLayout(jPanel237);
        jPanel237.setLayout(jPanel237Layout);
        jPanel237Layout.setHorizontalGroup(
            jPanel237Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel237Layout.setVerticalGroup(
            jPanel237Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel95.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel95.setText("Hàng ghế gần cửa sổ, lối ra vào");

        jPanel238.setBackground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel238Layout = new javax.swing.GroupLayout(jPanel238);
        jPanel238.setLayout(jPanel238Layout);
        jPanel238Layout.setHorizontalGroup(
            jPanel238Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel238Layout.setVerticalGroup(
            jPanel238Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel96.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        jLabel96.setText("Đã bị chọn hoặc không được sử dụng ");

        btnXacNhanChonCho.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        btnXacNhanChonCho.setText("Xác nhận chọn chỗ ngồi");

        btnHuyChonCho.setFont(new java.awt.Font("UTM Times", 0, 16)); // NOI18N
        btnHuyChonCho.setText("Hủy chọn chỗ ngồi");
        btnHuyChonCho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyChonChoActionPerformed(evt);
            }
        });

        noiDung.setFont(new java.awt.Font("UTM Centur", 0, 18)); // NOI18N
        noiDung.setText("Chuyến bay VJ811 HAN - DAD");

        bangHanhKhach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hành khách", "Phí tiêu tốn", "Ghế ngồi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bangHanhKhach.setRowHeight(35);
        jScrollPane1.setViewportView(bangHanhKhach);

        noiDung1.setFont(new java.awt.Font("UTM Centur", 0, 16)); // NOI18N
        noiDung1.setText("Hành khách đang chọn:");

        jPanel1.setBackground(new java.awt.Color(102, 51, 255));

        jLabel1.setBackground(new java.awt.Color(0, 255, 255));
        jLabel1.setFont(new java.awt.Font("UTM Centur", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sơ đồ chỗ ngồi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel121, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHuyChonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXacNhanChonCho)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel238, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(noiDung)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel120, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(jPanel118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel237, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel95, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel236, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noiDung1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(17, Short.MAX_VALUE))))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel121, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel120, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel236, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel237, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel238, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addComponent(noiDung)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(noiDung1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHuyChonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXacNhanChonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyChonChoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyChonChoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHuyChonChoActionPerformed


//    public static void main(String args[]) {
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ChonChoNgoi(String maChuyen, String diemDi, String diemDen, int tongSoKH, List<UserHanhKhach> danhSachHanhKhach).setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable bangHanhKhach;
    private javax.swing.JButton btnHuyChonCho;
    private javax.swing.JButton btnXacNhanChonCho;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel116;
    private javax.swing.JPanel jPanel117;
    private javax.swing.JPanel jPanel118;
    private javax.swing.JPanel jPanel120;
    private javax.swing.JPanel jPanel121;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel236;
    private javax.swing.JPanel jPanel237;
    private javax.swing.JPanel jPanel238;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl11A;
    private javax.swing.JLabel lbl11B;
    private javax.swing.JLabel lbl12A;
    private javax.swing.JLabel lbl12B;
    private javax.swing.JLabel lbl13A;
    private javax.swing.JLabel lbl13B;
    private javax.swing.JLabel lbl14A;
    private javax.swing.JLabel lbl14B;
    private javax.swing.JLabel lbl15A;
    private javax.swing.JLabel lbl15B;
    private javax.swing.JLabel lbl16A;
    private javax.swing.JLabel lbl16B;
    private javax.swing.JLabel lbl17A;
    private javax.swing.JLabel lbl17B;
    private javax.swing.JLabel lbl18A;
    private javax.swing.JLabel lbl18B;
    private javax.swing.JLabel lbl19A;
    private javax.swing.JLabel lbl19B;
    private javax.swing.JLabel noiDung;
    private javax.swing.JLabel noiDung1;
    private javax.swing.JPanel panel10A;
    private javax.swing.JPanel panel10B;
    private javax.swing.JPanel panel10C;
    private javax.swing.JPanel panel10D;
    private javax.swing.JPanel panel10E;
    private javax.swing.JPanel panel10F;
    private javax.swing.JPanel panel11A;
    private javax.swing.JPanel panel11B;
    private javax.swing.JPanel panel11C;
    private javax.swing.JPanel panel11D;
    private javax.swing.JPanel panel11E;
    private javax.swing.JPanel panel11F;
    private javax.swing.JPanel panel12A;
    private javax.swing.JPanel panel12B;
    private javax.swing.JPanel panel12C;
    private javax.swing.JPanel panel12D;
    private javax.swing.JPanel panel12E;
    private javax.swing.JPanel panel12F;
    private javax.swing.JPanel panel13A;
    private javax.swing.JPanel panel13B;
    private javax.swing.JPanel panel13C;
    private javax.swing.JPanel panel13D;
    private javax.swing.JPanel panel13E;
    private javax.swing.JPanel panel13F;
    private javax.swing.JPanel panel14A;
    private javax.swing.JPanel panel14B;
    private javax.swing.JPanel panel14C;
    private javax.swing.JPanel panel14D;
    private javax.swing.JPanel panel14E;
    private javax.swing.JPanel panel14F;
    private javax.swing.JPanel panel15A;
    private javax.swing.JPanel panel15B;
    private javax.swing.JPanel panel15C;
    private javax.swing.JPanel panel15D;
    private javax.swing.JPanel panel15E;
    private javax.swing.JPanel panel15F;
    private javax.swing.JPanel panel16A;
    private javax.swing.JPanel panel16B;
    private javax.swing.JPanel panel16C;
    private javax.swing.JPanel panel16D;
    private javax.swing.JPanel panel16E;
    private javax.swing.JPanel panel16F;
    private javax.swing.JPanel panel17A;
    private javax.swing.JPanel panel17B;
    private javax.swing.JPanel panel17C;
    private javax.swing.JPanel panel17D;
    private javax.swing.JPanel panel17E;
    private javax.swing.JPanel panel17F;
    private javax.swing.JPanel panel18A;
    private javax.swing.JPanel panel18B;
    private javax.swing.JPanel panel18C;
    private javax.swing.JPanel panel18D;
    private javax.swing.JPanel panel18E;
    private javax.swing.JPanel panel18F;
    private javax.swing.JPanel panel19A;
    private javax.swing.JPanel panel19B;
    private javax.swing.JPanel panel19C;
    private javax.swing.JPanel panel19D;
    private javax.swing.JPanel panel19E;
    private javax.swing.JPanel panel19F;
    private javax.swing.JPanel panel1A;
    private javax.swing.JPanel panel1B;
    private javax.swing.JPanel panel1C;
    private javax.swing.JPanel panel1D;
    private javax.swing.JPanel panel1E;
    private javax.swing.JPanel panel1F;
    private javax.swing.JPanel panel2A;
    private javax.swing.JPanel panel2B;
    private javax.swing.JPanel panel2C;
    private javax.swing.JPanel panel2D;
    private javax.swing.JPanel panel2E;
    private javax.swing.JPanel panel2F;
    private javax.swing.JPanel panel3A;
    private javax.swing.JPanel panel3B;
    private javax.swing.JPanel panel3C;
    private javax.swing.JPanel panel3D;
    private javax.swing.JPanel panel3E;
    private javax.swing.JPanel panel3F;
    private javax.swing.JPanel panel4A;
    private javax.swing.JPanel panel4B;
    private javax.swing.JPanel panel4C;
    private javax.swing.JPanel panel4D;
    private javax.swing.JPanel panel4E;
    private javax.swing.JPanel panel4F;
    private javax.swing.JPanel panel5A;
    private javax.swing.JPanel panel5B;
    private javax.swing.JPanel panel5C;
    private javax.swing.JPanel panel5D;
    private javax.swing.JPanel panel5E;
    private javax.swing.JPanel panel5F;
    private javax.swing.JPanel panel6A;
    private javax.swing.JPanel panel6B;
    private javax.swing.JPanel panel6C;
    private javax.swing.JPanel panel6D;
    private javax.swing.JPanel panel6E;
    private javax.swing.JPanel panel6F;
    private javax.swing.JPanel panel7A;
    private javax.swing.JPanel panel7B;
    private javax.swing.JPanel panel7C;
    private javax.swing.JPanel panel7D;
    private javax.swing.JPanel panel7E;
    private javax.swing.JPanel panel7F;
    private javax.swing.JPanel panel8A;
    private javax.swing.JPanel panel8B;
    private javax.swing.JPanel panel8C;
    private javax.swing.JPanel panel8D;
    private javax.swing.JPanel panel8E;
    private javax.swing.JPanel panel8F;
    private javax.swing.JPanel panel9A;
    private javax.swing.JPanel panel9B;
    private javax.swing.JPanel panel9C;
    private javax.swing.JPanel panel9D;
    private javax.swing.JPanel panel9E;
    private javax.swing.JPanel panel9F;
    // End of variables declaration//GEN-END:variables
}
