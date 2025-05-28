
package Process.ChuyenBay;

import java.time.LocalDateTime;

public class ChuyenBay {
    private String maChuyenBay;
    private String diemDi;
    private String diemDen;
    private LocalDateTime gioCatCanh;
    private LocalDateTime gioHaCanh;
    private double giaVe;
    private int soGheTrong;

    public ChuyenBay(String maChuyenBay, String diemDi, String diemDen, LocalDateTime gioCatCanh, LocalDateTime gioHaCanh, double giaVe, int soGheTrong) {
        this.maChuyenBay = maChuyenBay;
        this.diemDi = diemDi;
        this.diemDen = diemDen;
        this.gioCatCanh = gioCatCanh;
        this.gioHaCanh = gioHaCanh;
        this.giaVe = giaVe;
        this.soGheTrong = soGheTrong;
    }

    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public String getDiemDi() {
        return diemDi;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public LocalDateTime getGioCatCanh() {
        return gioCatCanh;
    }

    public LocalDateTime getGioHaCanh() {
        return gioHaCanh;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public int getSoGheTrong() {
        return soGheTrong;
    }
    
    
}
