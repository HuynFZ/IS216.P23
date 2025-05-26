package View.Admin;

import View.Admin.Process.PrTheTongHop;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class TheTongHop extends javax.swing.JPanel {

    private Color color1;
    private Color color2;
    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }
    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    
    public TheTongHop() {
        initComponents();
        setOpaque(false);
        color1 = Color.BLACK;
        color2 = Color.WHITE;
    }
    
    public void setData(PrTheTongHop data)
    {
        icon.setIcon(data.getIcon());
        TieuDe.setText(data.getTieuDe());
        GiaTri.setText(data.getGiaTri());
        Mota.setText(data.getMoTa());
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon = new javax.swing.JLabel();
        TieuDe = new javax.swing.JLabel();
        GiaTri = new javax.swing.JLabel();
        Mota = new javax.swing.JLabel();

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin/3d-report.png"))); // NOI18N

        TieuDe.setFont(new java.awt.Font("UTM Centur", 1, 14)); // NOI18N
        TieuDe.setForeground(new java.awt.Color(255, 255, 255));
        TieuDe.setText("Tiêu đề");

        GiaTri.setFont(new java.awt.Font("UTM Centur", 1, 18)); // NOI18N
        GiaTri.setForeground(new java.awt.Color(255, 255, 255));
        GiaTri.setText("Giá Trị");

        Mota.setFont(new java.awt.Font("UTM Centur", 0, 14)); // NOI18N
        Mota.setForeground(new java.awt.Color(255, 255, 255));
        Mota.setText("Mô tả");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TieuDe)
                    .addComponent(icon)
                    .addComponent(GiaTri)
                    .addComponent(Mota))
                .addContainerGap(296, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TieuDe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GiaTri)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Mota)
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0,color1, 0, getHeight(), color2);
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(grphcs);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel GiaTri;
    private javax.swing.JLabel Mota;
    private javax.swing.JLabel TieuDe;
    private javax.swing.JLabel icon;
    // End of variables declaration//GEN-END:variables
}
