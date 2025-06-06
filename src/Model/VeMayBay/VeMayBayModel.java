

package Model.VeMayBay;

import java.sql.Timestamp;


public class VeMayBayModel {
    private String maVe;
    private String maChuyenBay;
    private int tongHanhKhach;
    private String maSanBayDi;
    private String maSanBayDen;
    private Timestamp gioCatCanh;
    private Timestamp gioHaCanh;
    private Timestamp ngayDatVe;
    private String trangThaiVe;
    private Timestamp thoiGianTT;
    private String trangThaiTT;

    public VeMayBayModel(String maVe, String maChuyenBay, int tongHanhKhach, String maSanBayDi, String maSanBayDen, Timestamp gioCatCanh, Timestamp gioHaCanh, Timestamp ngayDatVe, String trangThaiVe, Timestamp thoiGianTT, String trangThaiTT) {
        this.maVe = maVe;
        this.maChuyenBay = maChuyenBay;
        this.tongHanhKhach = tongHanhKhach;
        this.maSanBayDi = maSanBayDi;
        this.maSanBayDen = maSanBayDen;
        this.gioCatCanh = gioCatCanh;
        this.gioHaCanh = gioHaCanh;
        this.ngayDatVe = ngayDatVe;
        this.trangThaiVe = trangThaiVe;
        this.thoiGianTT = thoiGianTT;
        this.trangThaiTT = trangThaiTT;
    }

    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    

    public String getMaVe() {
        return maVe;
    }

    public int getTongHanhKhach() {
        return tongHanhKhach;
    }

    public String getMaSanBayDi() {
        return maSanBayDi;
    }

    public String getMaSanBayDen() {
        return maSanBayDen;
    }

    public Timestamp getGioCatCanh() {
        return gioCatCanh;
    }

    public Timestamp getGioHaCanh() {
        return gioHaCanh;
    }

    public Timestamp getNgayDatVe() {
        return ngayDatVe;
    }

    public String getTrangThaiVe() {
        return trangThaiVe;
    }

    public Timestamp getThoiGianTT() {
        return thoiGianTT;
    }

    public String getTrangThaiTT() {
        return trangThaiTT;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public void setTongHanhKhach(int tongHanhKhach) {
        this.tongHanhKhach = tongHanhKhach;
    }

    public void setMaSanBayDi(String maSanBayDi) {
        this.maSanBayDi = maSanBayDi;
    }

    public void setMaSanBayDen(String maSanBayDen) {
        this.maSanBayDen = maSanBayDen;
    }

    public void setGioCatCanh(Timestamp gioCatCanh) {
        this.gioCatCanh = gioCatCanh;
    }

    public void setGioHaCanh(Timestamp gioHaCanh) {
        this.gioHaCanh = gioHaCanh;
    }

    public void setNgayDatVe(Timestamp ngayDatVe) {
        this.ngayDatVe = ngayDatVe;
    }

    public void setTrangThaiVe(String trangThaiVe) {
        this.trangThaiVe = trangThaiVe;
    }

    public void setThoiGianTT(Timestamp thoiGianTT) {
        this.thoiGianTT = thoiGianTT;
    }

    public void setTrangThaiTT(String trangThaiTT) {
        this.trangThaiTT = trangThaiTT;
    }
    
    
}
