
package Process.KhachHang;

import java.util.Date;

public class KhachHangModel {
    private String hoTen;
    private String cccd;
    private Date ngaySinh;
    private String gioiTinh;
    private String quocTich;
    private String SDT;
    private String Email;
    private String loaiKH;
    private String diemThuong;
    private String hangTV;
    private String thoiHanTV;
    private String thoiGianCapNhat;

    public KhachHangModel(String hoTen, String cccd, Date ngaySinh, String gioiTinh, String quocTich, String SDT, String Email, String loaiKH, String diemThuong, String hangTV, String thoiHanTV, String thoiGianCapNhat) {
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.quocTich = quocTich;
        this.SDT = SDT;
        this.Email = Email;
        this.loaiKH = loaiKH;
        this.diemThuong = diemThuong;
        this.hangTV = hangTV;
        this.thoiHanTV = thoiHanTV;
        this.thoiGianCapNhat = thoiGianCapNhat;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    

    public KhachHangModel() {
        
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

    public String getSDT() {
        return SDT;
    }

    public String getEmail() {
        return Email;
    }

    public String getLoaiKH() {
        return loaiKH;
    }

    public String getDiemThuong() {
        return diemThuong;
    }

    public String getHangTV() {
        return hangTV;
    }

    public String getThoiHanTV() {
        return thoiHanTV;
    }

    public String getThoiGianCapNhat() {
        return thoiGianCapNhat;
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

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setLoaiKH(String loaiKH) {
        this.loaiKH = loaiKH;
    }

    public void setDiemThuong(String diemThuong) {
        this.diemThuong = diemThuong;
    }

    public void setHangTV(String hangTV) {
        this.hangTV = hangTV;
    }

    public void setThoiHanTV(String thoiHanTV) {
        this.thoiHanTV = thoiHanTV;
    }

    public void setThoiGianCapNhat(String thoiGianCapNhat) {
        this.thoiGianCapNhat = thoiGianCapNhat;
    }
    
    
    
}
