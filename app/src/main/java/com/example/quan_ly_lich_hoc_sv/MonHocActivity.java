package com.example.quan_ly_lich_hoc_sv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.quan_ly_lich_hoc_sv.adapters.MonHocAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class MonHocActivity extends AppCompatActivity {

    //Khai báo các biến giao diện
    ListView lvMonHoc;
    TextView tvMonHocTrong;
    EditText edtTimKiemMonHoc;
    ImageButton btnThemMonHoc;
    View mucTrangChu, mucLichHoc, mucCongViec;

    //Khai báo các biến xử lý dữ liệu và Adapter
    DatabaseHelper csdlHelper;
    MonHocAdapter boChuyenDoiMonHoc;
    List<MonHoc> dsMonHoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle trangThaiLuu) {
        super.onCreate(trangThaiLuu);
        setContentView(R.layout.activity_mon_hoc);

        //Khởi tạo đối tượng SQLite
        csdlHelper = DatabaseHelper.getInstance(this);

        //Tham chiếu Id đến các biến giao diện
        lvMonHoc = findViewById(R.id.lvSubjects);
        tvMonHocTrong = findViewById(R.id.tvEmptySubjects);
        edtTimKiemMonHoc = findViewById(R.id.etSearchSubject);
        btnThemMonHoc = findViewById(R.id.fabAddSubject);

        mucTrangChu = findViewById(R.id.navHome);
        mucLichHoc = findViewById(R.id.navSchedule);
        mucCongViec = findViewById(R.id.navTasks);

        //Khởi tạo Adapter và gán vào ListView hiển thị danh sách môn học
        boChuyenDoiMonHoc = new MonHocAdapter(this, dsMonHoc, new MonHocAdapter.OnMonHocActionListener() {
            @Override
            public void khiSua(MonHoc monHoc) {
                //Mở hộp thoại sửa thông tin môn học
                hienThiHopThoaiMonHoc(monHoc);
            }

            @Override
            public void khiXoa(MonHoc monHoc) {
                //Mở hộp thoại xác nhận xóa môn học
                xacNhanXoaMonHoc(monHoc);
            }
        });
        lvMonHoc.setAdapter(boChuyenDoiMonHoc);

        //Xử lý sự kiện khi chạm vào một dòng trên ListView để sửa môn học
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> khungCha, View giaoDienDong, int viTri, long maDong) {
                MonHoc monHocChon = dsMonHoc.get(viTri);
                hienThiHopThoaiMonHoc(monHocChon);
            }
        });

        //Xử lý sự kiện khi nhấn nút (+) thêm môn học mới
        btnThemMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiHopThoaiMonHoc(null);
            }
        });

        //Xử lý tìm kiếm môn học khi người dùng gõ vào ô EditText
        edtTimKiemMonHoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence chuoi, int batDau, int soLuong, int sau) {
            }

            @Override
            public void onTextChanged(CharSequence chuoi, int batDau, int truoc, int soLuong) {
                //Cập nhật lại danh sách môn học theo từ khóa tìm kiếm
                taiDanhSachMonHoc(chuoi.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable chuoi) {
            }
        });

        //Xử lý sự kiện chuyển màn hình trên thanh điều hướng dưới cùng qua Intent
        mucTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MonHocActivity.this, MainActivity.class);
                yDinh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        mucLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MonHocActivity.this, LichHocActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        mucCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MonHocActivity.this, BaiTapActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //Tải danh sách môn học từ SQLite lên ListView
        taiDanhSachMonHoc("");
    }

    //Hàm đọc danh sách môn học từ SQLite và hiển thị lên ListView
    private void taiDanhSachMonHoc(String tuKhoa) {
        List<MonHoc> danhSachKetQua;
        if (tuKhoa == null || tuKhoa.isEmpty()) {
            //Lấy toàn bộ danh sách môn học
            danhSachKetQua = csdlHelper.layDanhSachMonHoc();
        } else {
            //Tìm kiếm môn học theo tên, mã môn hoặc giảng viên
            danhSachKetQua = csdlHelper.timKiemMonHoc(tuKhoa);
        }
        //Hiển thị kết quả lên ListView
        boChuyenDoiMonHoc.capNhatDuLieu(danhSachKetQua);
        tvMonHocTrong.setVisibility(danhSachKetQua.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Hàm hiển thị hộp thoại Dialog Thêm mới hoặc Cập nhật môn học
    private void hienThiHopThoaiMonHoc(@Nullable final MonHoc monHocCanSua) {
        View giaoDienHopThoai = LayoutInflater.from(this).inflate(R.layout.dialog_them_mon_hoc, null);
        final AlertDialog hopThoai = new AlertDialog.Builder(this)
                .setView(giaoDienHopThoai)
                .create();
        if (hopThoai.getWindow() != null) {
            hopThoai.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        TextView tvTieuDeHopThoai = giaoDienHopThoai.findViewById(R.id.tvDialogTitle);
        final EditText edtMaMonHoc = giaoDienHopThoai.findViewById(R.id.etSubjectCode);
        final EditText edtTenMonHoc = giaoDienHopThoai.findViewById(R.id.etSubjectName);
        final EditText edtSoTinChi = giaoDienHopThoai.findViewById(R.id.etCredits);
        final EditText edtGiangVien = giaoDienHopThoai.findViewById(R.id.etTeacher);
        final Spinner spHocKy = giaoDienHopThoai.findViewById(R.id.spSemester);
        final EditText edtNamHoc = giaoDienHopThoai.findViewById(R.id.etAcademicYear);
        TextView btnHuy = giaoDienHopThoai.findViewById(R.id.btnCancel);
        AppCompatButton btnLuu = giaoDienHopThoai.findViewById(R.id.btnSave);

        //Đổ dữ liệu danh sách học kỳ vào Spinner
        String[] mangHocKy = {"Học kỳ 1", "Học kỳ 2", "Học kỳ hè"};
        ArrayAdapter<String> boChuyenDoiHocKy = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, mangHocKy);
        spHocKy.setAdapter(boChuyenDoiHocKy);

        //Nếu là chế độ Sửa thì điền sẵn thông tin môn học hiện tại vào các ô nhập
        if (monHocCanSua != null) {
            tvTieuDeHopThoai.setText("Cập nhật môn học");
            edtMaMonHoc.setText(monHocCanSua.getMaMonHoc());
            edtTenMonHoc.setText(monHocCanSua.getTenMonHoc());
            edtSoTinChi.setText(String.valueOf(monHocCanSua.getSoTinChi()));
            edtGiangVien.setText(monHocCanSua.getGiangVien());
            edtNamHoc.setText(monHocCanSua.getNamHoc());
            for (int i = 0; i < mangHocKy.length; i++) {
                if (mangHocKy[i].equalsIgnoreCase(monHocCanSua.getHocKy())) {
                    spHocKy.setSelection(i);
                    break;
                }
            }
        } else {
            //Chế độ Thêm môn học mới
            tvTieuDeHopThoai.setText(R.string.dialog_title_add_subject);
            edtNamHoc.setText("2026 - 2027");
        }

        //Xử lý sự kiện khi nhấn nút Hủy
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopThoai.dismiss();
            }
        });

        //Xử lý sự kiện khi nhấn nút Lưu môn học
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ các ô nhập liệu
                String maMon = edtMaMonHoc.getText().toString().trim();
                String tenMon = edtTenMonHoc.getText().toString().trim();
                String chuoiTinChi = edtSoTinChi.getText().toString().trim();
                String giangVien = edtGiangVien.getText().toString().trim();
                String hocKy = spHocKy.getSelectedItem().toString();
                String namHoc = edtNamHoc.getText().toString().trim();

                //Kiểm tra dữ liệu bắt buộc
                if (maMon.isEmpty() || tenMon.isEmpty()) {
                    Toast.makeText(MonHocActivity.this, "Vui lòng nhập mã môn và tên môn học", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Chuyển đổi số tín chỉ sang kiểu số nguyên
                int soTinChi = 3;
                if (!chuoiTinChi.isEmpty()) {
                    try {
                        soTinChi = Integer.parseInt(chuoiTinChi);
                    } catch (NumberFormatException loi) {
                        soTinChi = 3;
                    }
                }

                //Thực hiện Thêm mới hoặc Cập nhật vào SQLite
                if (monHocCanSua == null) {
                    MonHoc monHocMoi = new MonHoc(maMon, tenMon, soTinChi, giangVien, hocKy, namHoc);
                    long maMoi = csdlHelper.themMonHoc(monHocMoi);
                    if (maMoi != -1) {
                        Toast.makeText(MonHocActivity.this, "Đã thêm môn học: " + tenMon, Toast.LENGTH_SHORT).show();
                        taiDanhSachMonHoc(edtTimKiemMonHoc.getText().toString().trim());
                        hopThoai.dismiss();
                    } else {
                        Toast.makeText(MonHocActivity.this, "Lỗi khi thêm môn học", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    monHocCanSua.setMaMonHoc(maMon);
                    monHocCanSua.setTenMonHoc(tenMon);
                    monHocCanSua.setSoTinChi(soTinChi);
                    monHocCanSua.setGiangVien(giangVien);
                    monHocCanSua.setHocKy(hocKy);
                    monHocCanSua.setNamHoc(namHoc);
                    int soDongCapNhat = csdlHelper.capNhatMonHoc(monHocCanSua);
                    if (soDongCapNhat > 0) {
                        Toast.makeText(MonHocActivity.this, "Đã cập nhật môn học!", Toast.LENGTH_SHORT).show();
                        taiDanhSachMonHoc(edtTimKiemMonHoc.getText().toString().trim());
                        hopThoai.dismiss();
                    }
                }
            }
        });

        hopThoai.show();
    }

    //Hàm hiển thị hộp thoại xác nhận và xóa môn học khỏi SQLite
    private void xacNhanXoaMonHoc(final MonHoc monHoc) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa môn học")
                .setMessage("Bạn có chắc chắn muốn xóa môn \"" + monHoc.getTenMonHoc() + "\"?\nLưu ý: Lịch học và bài tập thuộc môn này cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface hopThoai, int viTriNut) {
                        csdlHelper.xoaMonHoc(monHoc.getId());
                        Toast.makeText(MonHocActivity.this, "Đã xóa môn học", Toast.LENGTH_SHORT).show();
                        taiDanhSachMonHoc(edtTimKiemMonHoc.getText().toString().trim());
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

