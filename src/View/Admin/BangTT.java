
package View.Admin;

import View.Admin.Process.*;
import View.Admin.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BangTT extends JTable{
    public BangTT() 
    {
        setShowHorizontalLines(true);
        setGridColor(new Color(230,230,230));
        setRowHeight(40);
        setBackground(Color.WHITE);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                HeaderBangTT header = new HeaderBangTT(value + "");
                if (column == 4)
                {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
            
        });
        setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (column != 4)
                {
                    Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    com.setBackground(Color.WHITE);
                    setBorder(noFocusBorder);
                    if (isSelected)
                    {
                        com.setForeground(new Color(15,89,140));
                    } else 
                    {
                        com.setForeground(new Color(102,102,102));
                    }
                    return com;
                }else 
                {
                    TrangThaiType type = (TrangThaiType) value;
                    OTrangThai o = new OTrangThai(type);
                    return o;
                }
                
            }
            
        });
    }
    public void addRow (Object[] row)
    {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }
}
