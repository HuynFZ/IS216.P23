
package View.Admin;

import View.Admin.Process.PrMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class ListMenu<E extends Object> extends JList<E>{
    
    private final DefaultListModel model;
    private int selectedIndex = -1;
    private int overIndex = -1;
    private ChonMenu event;
    
    public void addEventMenu (ChonMenu event)
    {
        this.event = event;
    }

    public ListMenu() {
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    int index = locationToIndex(me.getPoint());
                    Object o = model.getElementAt(index);
                    if (o instanceof PrMenu) {
                        PrMenu menu = (PrMenu) o;
                        if (menu.getType() == PrMenu.MenuType.MENU) {
                            selectedIndex = index;
                            if (event != null) {
                                event.selected(index);
                            }
                        }
                    } else {
                        selectedIndex = index;
                    }
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                overIndex = -1;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                int index = locationToIndex(me.getPoint());
                if (index != overIndex) {
                    Object o = model.getElementAt(index);
                    if (o instanceof PrMenu) {
                        PrMenu menu = (PrMenu) o;
                        if (menu.getType() == PrMenu.MenuType.MENU) {
                            overIndex = index;
                        } else {
                            overIndex = -1;
                        }
                        repaint();
                    }
                }
            }
        });
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object o, int index, boolean isSelected, boolean cellHasFocus) {
                PrMenu data;
                if (o instanceof PrMenu prMenu)
                {
                  data = prMenu; 
                }else
                {
                   data = new PrMenu("", o + "", PrMenu.MenuType.EMPTY);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex==index);
                item.setOver(overIndex == index);
                return item;
            }
            
        };
    }
    
    public void addItem(PrMenu data)
    {
        model.addElement(data);
    }
}
