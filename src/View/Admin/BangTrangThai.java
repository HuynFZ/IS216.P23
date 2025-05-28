
package View.Admin;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;


public class BangTrangThai extends JLabel{
    public TrangThaiType getType()
    {
        return type;
    }
    public BangTrangThai ()
    {
        setForeground(Color.WHITE);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }
    private TrangThaiType type;
    public void setType(TrangThaiType type)
    {
        this.type = type;
        setText(type.toString());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (type!=null)
        {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            int padding = 4;
            if (type == TrangThaiType.PENDING)
            {
                g = new GradientPaint(0, 0, new Color(186,123,247), 0, getHeight(), new Color(167,94,236));
            } else if (type == TrangThaiType.APPROVED)
            {
                g = new GradientPaint(0, 0, new Color(142,142,250), 0, getHeight(), new Color(123,123,245));
            } else 
            {
                g = new GradientPaint(0, 0, new Color(241,208,62), 0, getHeight(), new Color(211,184,61));
            }
            g2.setPaint(g);
            g2.fillRoundRect(padding, padding, getWidth() - 2 *padding, getHeight() - 2 * padding, 10, 10);
        }
        super.paintComponent(grphcs);
    }
    
}
