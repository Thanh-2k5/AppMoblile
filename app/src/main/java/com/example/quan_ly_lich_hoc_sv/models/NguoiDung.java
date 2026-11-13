package com.example.quan_ly_lich_hoc_sv.models;

public class NguoiDung {
    //Khai báo các thuộc tính của đối tượng Người dùng
    private int id;
    private String maSinhVien;
    private String hoTen;
    private String email;
    private String tenDangNhap;
    private String matKhau;

    //Hàm khởi tạo mặc định không tham số
    public NguoiDung() {
    }

    //Hàm khởi tạo đầy đủ tham số (có id)
    public NguoiDung(int id, String maSinhVien, String hoTen, String email, String tenDangNhap, String matKhau) {
        this.id = id;
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    //Hàm khởi tạo dùng khi thêm mới người dùng (chưa có id tự tăng)
    public NguoiDung(String maSinhVien, String hoTen, String email, String tenDangNhap, String matKhau) {
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    //Các phương thức Getter và Setter để lấy và gán giá trị thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
