
package Process.NhanVien;

import java.util.Date;


public class NhanVienModel {
    private String hoTen;
    private String cccd;
    private Date ngaySinh;
    private String gioiTinh;
    private String sdt;
    private String email;
    private String diaChi;
    private String chucVu;
    private String luongCoBan;
    private String phucLoi;
    private Date ngayVaoLam;

    public NhanVienModel(String hoTen, String cccd, Date ngaySinh, String gioiTinh, String sdt, String email, String diaChi, String chucVu, String luongCoBan, String phucLoi, Date ngayVaoLam) {
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.luongCoBan = luongCoBan;
        this.phucLoi = phucLoi;
        this.ngayVaoLam = ngayVaoLam;
    }

    public NhanVienModel() {
    }

    
    
    public String getHoTen() {
        return hoTen;
    }

    public String getCccd() {
        return cccd;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public String getEmail() {
        return email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getChucVu() {
        return chucVu;
    }

    public String getLuongCoBan() {
        return luongCoBan;
    }

    public String getPhucLoi() {
        return phucLoi;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public void setLuongCoBan(String luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public void setPhucLoi(String phucLoi) {
        this.phucLoi = phucLoi;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }
    
    
}


