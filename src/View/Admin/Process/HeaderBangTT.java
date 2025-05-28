
package View.Admin.Process;

import View.Admin.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;


public class HeaderBangTT extends JLabel{
    public HeaderBangTT (String text)
    {
        super(text);
        setOpaque(true);
        setBackground(Color.WHITE);
        setFont(new Font("UTM Centur", 1, 16));
        setForeground(new Color(102,102,102));
        setBorder(new EmptyBorder(10,5,10,5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.setColor(new Color(230,230,230));
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight()-1);
    }
    
    
}
