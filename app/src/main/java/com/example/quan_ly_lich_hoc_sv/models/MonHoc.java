package com.example.quan_ly_lich_hoc_sv.models;

import androidx.annotation.NonNull;

public class MonHoc {
    //Khai báo các thuộc tính của đối tượng Môn học
    private int id;
    private int nguoiDungId;
    private String maMonHoc;
    private String tenMonHoc;
    private int soTinChi;
    private String giangVien;
    private String hocKy;
    private String namHoc;

    //Hàm khởi tạo mặc định không tham số
    public MonHoc() {
    }

    //Hàm khởi tạo đầy đủ tham số (có id)
    public MonHoc(int id, int nguoiDungId, String maMonHoc, String tenMonHoc, int soTinChi, String giangVien, String hocKy, String namHoc) {
        this.id = id;
        this.nguoiDungId = nguoiDungId;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.soTinChi = soTinChi;
        this.giangVien = giangVien;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
    }

    //Hàm khởi tạo dùng khi thêm mới môn học (chưa có id tự tăng)
    public MonHoc(int nguoiDungId, String maMonHoc, String tenMonHoc, int soTinChi, String giangVien, String hocKy, String namHoc) {
        this.nguoiDungId = nguoiDungId;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.soTinChi = soTinChi;
        this.giangVien = giangVien;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
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

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(String giangVien) {
        this.giangVien = giangVien;
    }

    public String getHocKy() {
        return hocKy;
    }

    public void setHocKy(String hocKy) {
        this.hocKy = hocKy;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    //Trả về tên môn học khi hiển thị đối tượng trong Spinner
    @NonNull
    @Override
    public String toString() {
        return tenMonHoc != null ? tenMonHoc : "";
    }
}
