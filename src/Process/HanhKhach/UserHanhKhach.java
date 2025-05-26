
package Process.HanhKhach;

import java.sql.Timestamp;
import java.util.Date;

public class UserHanhKhach {
    private String loaiKhachHang; // "Người lớn", "Trẻ em", "Em bé"
    private String gioiTinh;      // "Nam" hoặc "Nữ"
    private String ho;
    private String ten;
    private Date ngaySinh;
    private String quocTich;
    private String cccd;               // Chỉ với người lớn
    private Date ngayHetHanCCCD;      // Chỉ với người lớn
    private String maDVGhe;
    private String viTriGhe;
    private double tienGhe;
    private String maGoiHanhLy;
    private double phiHanhly;
    private String maSuatAn;
    private String maBaoHiem;
    private String soDienThoai;
    private String email;
    private String maHanhKhach;
    private double phiBoSung;

    public UserHanhKhach(String loaiKhachHang, String gioiTinh, String ho, String ten, Date ngaySinh, String quocTich, String cccd, Date ngayHetHanCCCD, String maDVGhe, String viTriGhe, double tienGhe, String maGoiHanhLy, double phiHanhly, String maSuatAn, String maBaoHiem, String soDienThoai, String email) {
        this.loaiKhachHang = loaiKhachHang;
        this.gioiTinh = gioiTinh;
        this.ho = ho;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.quocTich = quocTich;
        this.cccd = cccd;
        this.ngayHetHanCCCD = ngayHetHanCCCD;
        this.maDVGhe = maDVGhe;
        this.viTriGhe = viTriGhe;
        this.tienGhe = tienGhe;
        this.maGoiHanhLy = maGoiHanhLy;
        this.phiHanhly = phiHanhly;
        this.maSuatAn = maSuatAn;
        this.maBaoHiem = maBaoHiem;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    public UserHanhKhach(String loaiKhachHang, String gioiTinh, String ho, String ten, Date ngaySinh, String quocTich, String cccd, Date ngayHetHanCCCD, String maDVGhe, String viTriGhe, double tienGhe, String maGoiHanhLy, double phiHanhly, String maSuatAn, String maBaoHiem, String soDienThoai, String email, String maHanhKhach) {
        this.loaiKhachHang = loaiKhachHang;
        this.gioiTinh = gioiTinh;
        this.ho = ho;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.quocTich = quocTich;
        this.cccd = cccd;
        this.ngayHetHanCCCD = ngayHetHanCCCD;
        this.maDVGhe = maDVGhe;
        this.viTriGhe = viTriGhe;
        this.tienGhe = tienGhe;
        this.maGoiHanhLy = maGoiHanhLy;
        this.phiHanhly = phiHanhly;
        this.maSuatAn = maSuatAn;
        this.maBaoHiem = maBaoHiem;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.maHanhKhach = maHanhKhach;
    }

    public UserHanhKhach(String maHanhKhach, String gioiTinh, String hoTen, Date ngaySinh, String quocTich,
                         String cccd, String sdt, String email, double phiBoSung, String viTriGhe) {
        this.maHanhKhach = maHanhKhach;
        this.gioiTinh = gioiTinh;

        // Tách họ tên
        tachHoTen(hoTen);

        this.ngaySinh = ngaySinh;
        this.quocTich = quocTich;
        this.cccd = cccd;
        this.soDienThoai = sdt;
        this.email = email;
        this.phiBoSung = phiBoSung;
        this.viTriGhe = viTriGhe;
    }

    private void tachHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            this.ho = "";
            this.ten = "";
            return;
        }

        String[] parts = hoTen.trim().split("\\s+");
        if (parts.length == 1) {
            this.ho = "";
            this.ten = parts[0];
        } else {
            this.ten = parts[parts.length - 1];
            this.ho = String.join(" ", java.util.Arrays.copyOfRange(parts, 0, parts.length - 1));
        }
    }

    public double getPhiBoSung() {
        return phiBoSung;
    }

    public void setPhiBoSung(double phiBoSung) {
        this.phiBoSung = phiBoSung;
    }
    
    

    public String getMaHanhKhach() {
        return maHanhKhach;
    }

    public void setMaHanhKhach(String maHanhKhach) {
        this.maHanhKhach = maHanhKhach;
    }
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

    

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    
    
    
    public String getQuocTich() {
        return quocTich;
    }

    public String getMaDVGhe() {
        return maDVGhe;
    }

    public String getMaGoiHanhLy() {
        return maGoiHanhLy;
    }

    public double getPhiHanhly() {
        return phiHanhly;
    }

    public String getMaSuatAn() {
        return maSuatAn;
    }

    public String getMaBaoHiem() {
        return maBaoHiem;
    }



    // Getters
    public String getLoaiKhachHang() {
        return loaiKhachHang;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getHo() {
        return ho;
    }

    public String getTen() {
        return ten;
    }
    
    public String getHoTen()
    {
        return ho + " " + ten;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public String getCccd() {
        return cccd;
    }

    public Date getNgayHetHanCCCD() {
        return ngayHetHanCCCD;
    }

    // Setters
    public void setLoaiKhachHang(String loaiKhachHang) {
        this.loaiKhachHang = loaiKhachHang;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public void setMaDVGhe(String maDVGhe) {
        this.maDVGhe = maDVGhe;
    }

    public void setMaGoiHanhLy(String maGoiHanhLy) {
        this.maGoiHanhLy = maGoiHanhLy;
    }

    public void setPhiHanhly(double phiHanhly) {
        this.phiHanhly = phiHanhly;
    }

    public void setMaSuatAn(String maSuatAn) {
        this.maSuatAn = maSuatAn;
    }

    public void setMaBaoHiem(String maBaoHiem) {
        this.maBaoHiem = maBaoHiem;
    }
 
    
    
    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setNgayHetHanCCCD(Date ngayHetHanCCCD) {
        this.ngayHetHanCCCD = ngayHetHanCCCD;
    }

    public String getViTriGhe() {
        return viTriGhe;
    }

    public double getTienGhe() {
        return tienGhe;
    }

    public void setViTriGhe(String viTriGhe) {
        this.viTriGhe = viTriGhe;
    }

    public void setTienGhe(double tienGhe) {
        this.tienGhe = tienGhe;
    }

    @Override
    public String toString() {
        return "UserHanhKhach{" + "loaiKhachHang=" + loaiKhachHang + ", gioiTinh=" + gioiTinh + ", ho=" + ho + ", ten=" + ten + ", ngaySinh=" + ngaySinh + ", quocTich=" + quocTich + ", cccd=" + cccd + ", ngayHetHanCCCD=" + ngayHetHanCCCD + ", maDVGhe=" + maDVGhe + ", viTriGhe=" + viTriGhe + ", tienGhe=" + tienGhe + ", maGoiHanhLy=" + maGoiHanhLy + ", phiHanhly=" + phiHanhly + ", maSuatAn=" + maSuatAn + ", maBaoHiem=" + maBaoHiem + '}';
    }

   
    
    
}
