package com.example.quan_ly_lich_hoc_sv.models;

public class BaiTap {
    //Khai báo các thuộc tính của đối tượng Bài tập
    private int id;
    private int monHocId;
    private String tenMonHoc; //Trường phụ hỗ trợ hiển thị tên môn học khi JOIN với bảng MonHoc
    private String tenBaiTap;
    private String noiDung;
    private String hanNop;
    private String mucDoUuTien;
    private String trangThai;

    //Hàm khởi tạo mặc định không tham số
    public BaiTap() {
    }

    //Hàm khởi tạo đầy đủ tham số (có id)
    public BaiTap(int id, int monHocId, String tenBaiTap, String noiDung, String hanNop, String mucDoUuTien, String trangThai) {
        this.id = id;
        this.monHocId = monHocId;
        this.tenBaiTap = tenBaiTap;
        this.noiDung = noiDung;
        this.hanNop = hanNop;
        this.mucDoUuTien = mucDoUuTien;
        this.trangThai = trangThai;
    }

    //Hàm khởi tạo dùng khi thêm mới bài tập (chưa có id tự tăng)
    public BaiTap(int monHocId, String tenBaiTap, String noiDung, String hanNop, String mucDoUuTien, String trangThai) {
        this.monHocId = monHocId;
        this.tenBaiTap = tenBaiTap;
        this.noiDung = noiDung;
        this.hanNop = hanNop;
        this.mucDoUuTien = mucDoUuTien;
        this.trangThai = trangThai;
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

    public String getTenBaiTap() {
        return tenBaiTap;
    }

    public void setTenBaiTap(String tenBaiTap) {
        this.tenBaiTap = tenBaiTap;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getHanNop() {
        return hanNop;
    }

    public void setHanNop(String hanNop) {
        this.hanNop = hanNop;
    }

    public String getMucDoUuTien() {
        return mucDoUuTien;
    }

    public void setMucDoUuTien(String mucDoUuTien) {
        this.mucDoUuTien = mucDoUuTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
