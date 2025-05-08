
package View.TrangChu;

import View.TrangChu.Process.PrMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuItem extends javax.swing.JPanel {

    private boolean selected;
    private boolean over;
    
    public MenuItem(PrMenu data) {
        initComponents();
        setOpaque(false);
        if (data.getType() == PrMenu.MenuType.MENU) 
        {
            icon.setIcon(data.toIcon());
            TenMenu.setText(data.getName());
        } else if (data.getType() == PrMenu.MenuType.TITLE) 
        {
            //icon.setText(data.getName());
            //icon.setFont(new Font("sansserif", 1, 12));
            //TenMenu.setVisible(false);
            TenMenu.setText(data.getName());
            TenMenu.setFont(new Font("UTM Times", 1, 20));
        } else 
        {
            TenMenu.setText(" ");
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }    
    
    public void setOver(boolean over)
    {
      this.over = over;
      repaint();
    }    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon = new javax.swing.JLabel();
        TenMenu = new javax.swing.JLabel();

        icon.setForeground(new java.awt.Color(255, 255, 255));

        TenMenu.setFont(new java.awt.Font("UTM Times", 0, 18)); // NOI18N
        TenMenu.setForeground(new java.awt.Color(255, 255, 255));
        TenMenu.setText("Tên menu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TenMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(TenMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (selected || over) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (selected) {
                g2.setColor(new Color(255, 255, 255, 80));
            } else {
                g2.setColor(new Color(255, 255, 255, 20));
            }
            g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 5, 5);
        }
        super.paintComponent(grphcs);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TenMenu;
    private javax.swing.JLabel icon;
    // End of variables declaration//GEN-END:variables
}
