
package View.Admin;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BangDieuKhien extends javax.swing.JPanel {

    public BangDieuKhien() {
        initComponents();
//        card1.setData(new PrTheTongHop(new ImageIcon(getClass().getResource("/com/raven/icon/stock.png")), "Stock Total", "$200000", "Increased by 60%"));
//        card2.setData(new PrTheTongHop(new ImageIcon(getClass().getResource("/com/raven/icon/profit.png")), "Total Profit", "$15000", "Increased by 25%"));
//        card3.setData(new PrTheTongHop(new ImageIcon(getClass().getResource("/com/raven/icon/flag.png")), "Unique Visitors", "$300000", "Increased by 70%"));
//        //  add row table
//        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
//        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        //spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        table.addRow(new Object[]{"Nguyen Van Manh Huy", "nguyenvanmanhhuy@gmail.com", "Admin", "25 Apr,2025", TrangThaiType.PENDING});
        table.addRow(new Object[]{"Nguyen Le Bao Ngoc", "nguyenlebaongoc@gmail.com", "Editor", "25 Apr,2025", TrangThaiType.APPROVED});
       table.addRow(new Object[]{"Truong Nguyen Dai Thang", "truongnguyendaithang@gmail.com", "Subscriber", "25 Apr,2025", TrangThaiType.APPROVED});
        table.addRow(new Object[]{"Nguyen Thanh Khang", "nguyenthanhkhang@gmail.com", "Admin", "25 Apr,2025", TrangThaiType.REJECT});
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JLayeredPane();
        theTongHop1 = new View.Admin.TheTongHop();
        theTongHop2 = new View.Admin.TheTongHop();
        theTongHop3 = new View.Admin.TheTongHop();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new View.Admin.BangTT();

        panel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        theTongHop1.setColor1(new java.awt.Color(186, 123, 247));
        theTongHop1.setColor2(new java.awt.Color(167, 94, 161));
        panel.add(theTongHop1);

        theTongHop2.setColor1(new java.awt.Color(241, 208, 62));
        theTongHop2.setColor2(new java.awt.Color(211, 184, 91));
        panel.add(theTongHop2);

        theTongHop3.setColor1(new java.awt.Color(142, 142, 250));
        theTongHop3.setColor2(new java.awt.Color(123, 123, 225));
        panel.add(theTongHop3);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("UTM Centur", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Bảng trạng thái");

        jScrollPane1.setBorder(null);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Họ và tên", "Email", "Vai trò", "Joined", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1224, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane panel;
    private View.Admin.BangTT table;
    private View.Admin.TheTongHop theTongHop1;
    private View.Admin.TheTongHop theTongHop2;
    private View.Admin.TheTongHop theTongHop3;
    // End of variables declaration//GEN-END:variables
}
