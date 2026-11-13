package com.example.quan_ly_lich_hoc_sv.models;

public class LichHoc {
    //Khai báo các thuộc tính của đối tượng Lịch học
    private int id;
    private int monHocId;
    private String tenMonHoc; //Trường phụ hỗ trợ hiển thị tên môn học khi JOIN với bảng MonHoc
    private String thu;
    private String gioBatDau;
    private String gioKetThuc;
    private String phongHoc;
    private String giangVien;
    private String ngayBatDau;
    private String ngayKetThuc;

    //Hàm khởi tạo mặc định không tham số
    public LichHoc() {
    }

    //Hàm khởi tạo đầy đủ tham số (có id)
    public LichHoc(int id, int monHocId, String thu, String gioBatDau, String gioKetThuc, String phongHoc, String giangVien, String ngayBatDau, String ngayKetThuc) {
        this.id = id;
        this.monHocId = monHocId;
        this.thu = thu;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.phongHoc = phongHoc;
        this.giangVien = giangVien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    //Hàm khởi tạo dùng khi thêm mới lịch học (chưa có id tự tăng)
    public LichHoc(int monHocId, String thu, String gioBatDau, String gioKetThuc, String phongHoc, String giangVien, String ngayBatDau, String ngayKetThuc) {
        this.monHocId = monHocId;
        this.thu = thu;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.phongHoc = phongHoc;
        this.giangVien = giangVien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
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

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getPhongHoc() {
        return phongHoc;
    }

    public void setPhongHoc(String phongHoc) {
        this.phongHoc = phongHoc;
    }

    public String getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(String giangVien) {
        this.giangVien = giangVien;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
