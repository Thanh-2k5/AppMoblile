package com.example.quan_ly_lich_hoc_sv.models;

public class LichThi {
    //Khai báo các thuộc tính của đối tượng Lịch thi
    private int id;
    private int monHocId;
    private String tenMonHoc; //Trường phụ hỗ trợ hiển thị tên môn học khi JOIN với bảng MonHoc
    private String ngayThi;
    private String gioThi;
    private String phongThi;
    private String thoiLuong;
    private String hinhThucThi;
    private String ghiChu;

    //Hàm khởi tạo mặc định không tham số
    public LichThi() {
    }

    //Hàm khởi tạo đầy đủ tham số (có id)
    public LichThi(int id, int monHocId, String ngayThi, String gioThi, String phongThi, String thoiLuong, String hinhThucThi, String ghiChu) {
        this.id = id;
        this.monHocId = monHocId;
        this.ngayThi = ngayThi;
        this.gioThi = gioThi;
        this.phongThi = phongThi;
        this.thoiLuong = thoiLuong;
        this.hinhThucThi = hinhThucThi;
        this.ghiChu = ghiChu;
    }

    //Hàm khởi tạo dùng khi thêm mới lịch thi (chưa có id tự tăng)
    public LichThi(int monHocId, String ngayThi, String gioThi, String phongThi, String thoiLuong, String hinhThucThi, String ghiChu) {
        this.monHocId = monHocId;
        this.ngayThi = ngayThi;
        this.gioThi = gioThi;
        this.phongThi = phongThi;
        this.thoiLuong = thoiLuong;
        this.hinhThucThi = hinhThucThi;
        this.ghiChu = ghiChu;
    }

    //Các phương thức Getter và Setter để lấy và gán giá trị thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonHocId() {
        return monHocId;
    }

    public void setMonHocId(int monHocId) {
        this.monHocId = monHocId;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getNgayThi() {
        return ngayThi;
    }

    public void setNgayThi(String ngayThi) {
        this.ngayThi = ngayThi;
    }

    public String getGioThi() {
        return gioThi;
    }

    public void setGioThi(String gioThi) {
        this.gioThi = gioThi;
    }

    public String getPhongThi() {
        return phongThi;
    }

    public void setPhongThi(String phongThi) {
        this.phongThi = phongThi;
    }

    public String getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(String thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getHinhThucThi() {
        return hinhThucThi;
    }

    public void setHinhThucThi(String hinhThucThi) {
        this.hinhThucThi = hinhThucThi;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
