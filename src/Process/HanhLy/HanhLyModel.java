
package Process.HanhLy;

public class HanhLyModel {
    private String maHanhLy;
    private String tenGoiHL;
    private double trongLuongMax;
    private double phiGoiHanhly;

    public HanhLyModel(String maHanhLy, String tenGoiHL, double trongLuongMax, double phiGoiHanhly) {
        this.maHanhLy = maHanhLy;
        this.tenGoiHL = tenGoiHL;
        this.trongLuongMax = trongLuongMax;
        this.phiGoiHanhly = phiGoiHanhly;
    }

    public String getMaHanhLy() {
        return maHanhLy;
    }

    public void setMaHanhLy(String maHanhLy) {
        this.maHanhLy = maHanhLy;
    }



    public String getTenGoiHL() {
        return tenGoiHL;
    }

    public double getTrongLuongMax() {
        return trongLuongMax;
    }

    public double getPhiGoiHanhly() {
        return phiGoiHanhly;
    }

    public void setTenGoiHL(String tenGoiHL) {
        this.tenGoiHL = tenGoiHL;
    }

    public void setTrongLuongMax(double trongLuongMax) {
        this.trongLuongMax = trongLuongMax;
    }

    public void setPhiGoiHanhly(double phiGoiHanhly) {
        this.phiGoiHanhly = phiGoiHanhly;
    }
    
    
}
