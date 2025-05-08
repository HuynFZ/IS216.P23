package View.Admin.Process;

import javax.swing.Icon;

public class PrTheTongHop {

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getTieuDe() {
        return title;
    }

    public void setTieuDe(String title) {
        this.title = title;
    }

    public String getGiaTri() {
        return values;
    }

    public void setGiaTri(String values) {
        this.values = values;
    }

    public String getMoTa() {
        return description;
    }

    public void setMoTa(String description) {
        this.description = description;
    }

    public PrTheTongHop(Icon icon, String title, String values, String description) {
        this.icon = icon;
        this.title = title;
        this.values = values;
        this.description = description;
    }

    public PrTheTongHop() {
    }

    private Icon icon;
    private String title;
    private String values;
    private String description;
}
