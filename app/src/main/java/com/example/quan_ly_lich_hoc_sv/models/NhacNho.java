package com.example.quan_ly_lich_hoc_sv.models;

public class NhacNho {
    //Khai báo các thuộc tính của đối tượng Nhắc nhở
    private int id;
    private int nguoiDungId;
    private String tieuDe;
    private String noiDung;
    private String thoiGianNhac;
    private String trangThai;

    //Hàm khởi tạo mặc định không tham số
    public NhacNho() {
    }

    //Hàm khởi tạo đầy đủ tham số (có id)
    public NhacNho(int id, int nguoiDungId, String tieuDe, String noiDung, String thoiGianNhac, String trangThai) {
        this.id = id;
        this.nguoiDungId = nguoiDungId;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.thoiGianNhac = thoiGianNhac;
        this.trangThai = trangThai;
    }

    //Hàm khởi tạo dùng khi thêm mới nhắc nhở (chưa có id tự tăng)
    public NhacNho(int nguoiDungId, String tieuDe, String noiDung, String thoiGianNhac, String trangThai) {
        this.nguoiDungId = nguoiDungId;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.thoiGianNhac = thoiGianNhac;
        this.trangThai = trangThai;
    }

    //Các phương thức Getter và Setter để lấy và gán giá trị thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(int nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getThoiGianNhac() {
        return thoiGianNhac;
    }

    public void setThoiGianNhac(String thoiGianNhac) {
        this.thoiGianNhac = thoiGianNhac;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
