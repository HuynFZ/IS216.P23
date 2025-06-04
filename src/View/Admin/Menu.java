
package View.Admin;

import View.Admin.Process.PrMenu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Menu extends javax.swing.JPanel {

    private ChonMenu event;
    
    public void addEventMenu (ChonMenu event)
    {
        this.event = event;
        listMenu1.addEventMenu(event);
    }
    public Menu() {
        initComponents();
        setOpaque(false);
        listMenu1.setOpaque(false);
        init();
    }

    private void init()
    {
       listMenu1.addItem(new PrMenu("1", "Bảng điều khiển", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("2", "Quản lý chuyến bay", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("3", "Quản lý máy bay", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("4", "Quản lý nhân viên", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("13", "Quản lý khách hàng", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("5", "Quản lý đặt vé", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("6", "Quản lý hành lý", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("11", "Phân công công việc", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("7", "Báo cáo và thống kê", PrMenu.MenuType.MENU));
       
       listMenu1.addItem(new PrMenu("", "Tài khoản", PrMenu.MenuType.TITLE));
       listMenu1.addItem(new PrMenu("8", "Thông tin cá nhân", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("9", "Lịch trình làm việc", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("10", "Đăng xuất", PrMenu.MenuType.MENU));
       listMenu1.addItem(new PrMenu("", "", PrMenu.MenuType.EMPTY));
      
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listMenu1 = new View.Admin.ListMenu<>();

        panelMoving.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("UTM Times", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/admin.png"))); // NOI18N
        jLabel1.setText("Trang quản trị viên");

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMovingLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintChildren(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintChildren(grphcs);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private View.Admin.ListMenu<String> listMenu1;
    private javax.swing.JPanel panelMoving;
    // End of variables declaration//GEN-END:variables
}
