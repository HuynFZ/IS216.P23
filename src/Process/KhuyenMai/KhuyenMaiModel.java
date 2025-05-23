package Process.KhuyenMai;

import java.util.Date;

public class KhuyenMaiModel {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double giaTri;          // phần trăm giảm giá, ví dụ: 10.0 = 10%
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String dieuKienKM;
    private double dieuKienGiaTri;  // ví dụ: tổng tiền tối thiểu áp dụng khuyến mãi

    // Constructor
    public KhuyenMaiModel(String maKhuyenMai, String tenKhuyenMai, double giaTri, Date ngayBatDau,
                          Date ngayKetThuc, String dieuKienKM, double dieuKienGiaTri) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.giaTri = giaTri;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.dieuKienKM = dieuKienKM;
        this.dieuKienGiaTri = dieuKienGiaTri;
    }

    // Getters and Setters
    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public double getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(double giaTri) {
        this.giaTri = giaTri;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getDieuKienKM() {
        return dieuKienKM;
    }

    public void setDieuKienKM(String dieuKienKM) {
        this.dieuKienKM = dieuKienKM;
    }

    public double getDieuKienGiaTri() {
        return dieuKienGiaTri;
    }

    public void setDieuKienGiaTri(double dieuKienGiaTri) {
        this.dieuKienGiaTri = dieuKienGiaTri;
    }
}