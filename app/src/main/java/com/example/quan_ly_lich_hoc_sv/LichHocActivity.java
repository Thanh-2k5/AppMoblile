package com.example.quan_ly_lich_hoc_sv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.quan_ly_lich_hoc_sv.adapters.LichHocAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LichHocActivity extends AppCompatActivity {

    //Khai báo các biến giao diện
    ListView lvLichHoc;
    TextView tvLichHocTrong;
    TextView tvTieuDeThuDangChon;
    TextView tvKhoangThoiGianTuan;
    TabLayout tabLayoutCacThu;
    ImageView btnTuanTruoc, btnTuanSau;
    ImageButton btnThemLichHoc;
    View mucTrangChu, mucMonHoc, mucCongViec;

    //Khai báo các biến xử lý dữ liệu và trạng thái thứ/tuần
    DatabaseHelper csdlHelper;
    LichHocAdapter boChuyenDoiLichHoc;
    List<LichHoc> dsLichHoc = new ArrayList<>();
    final String[] MANG_THU_TRONG_TUAN = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    int viTriThuDangChon = 0;
    int doLechTuan = 0;

    @Override
    protected void onCreate(Bundle trangThaiLuu) {
        super.onCreate(trangThaiLuu);
        setContentView(R.layout.activity_lich_hoc);

        //Khởi tạo đối tượng SQLite
        csdlHelper = DatabaseHelper.getInstance(this);

        //Tham chiếu Id đến các biến giao diện
        lvLichHoc = findViewById(R.id.lvSchedules);
        tvLichHocTrong = findViewById(R.id.tvEmptySchedules);
        tvTieuDeThuDangChon = findViewById(R.id.tvSelectedDayTitle);
        tvKhoangThoiGianTuan = findViewById(R.id.tvWeekRange);
        tabLayoutCacThu = findViewById(R.id.tabLayoutDays);
        btnTuanTruoc = findViewById(R.id.btnPrevWeek);
        btnTuanSau = findViewById(R.id.btnNextWeek);
        btnThemLichHoc = findViewById(R.id.fabAddSchedule);

        mucTrangChu = findViewById(R.id.navHome);
        mucMonHoc = findViewById(R.id.navSubjects);
        mucCongViec = findViewById(R.id.navTasks);

        //Thêm 7 tab tương ứng từ Thứ 2 đến Chủ nhật vào TabLayout
        for (int i = 0; i < MANG_THU_TRONG_TUAN.length; i++) {
            tabLayoutCacThu.addTab(tabLayoutCacThu.newTab().setText(MANG_THU_TRONG_TUAN[i]));
        }

        //Xử lý sự kiện khi chọn thứ trên TabLayout
        tabLayoutCacThu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabChon) {
                viTriThuDangChon = tabChon.getPosition();
                tvTieuDeThuDangChon.setText(MANG_THU_TRONG_TUAN[viTriThuDangChon]);
                taiLichHocTheoThuDangChon();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabBoChon) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tabChonLai) {
            }
        });

        //Xử lý chuyển sang tuần trước và tuần sau
        btnTuanTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLechTuan--;
                capNhatTieuDeTuan();
            }
        });
        btnTuanSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLechTuan++;
                capNhatTieuDeTuan();
            }
        });

        //Khởi tạo Adapter và gán cho ListView hiển thị thời khóa biểu
        boChuyenDoiLichHoc = new LichHocAdapter(this, dsLichHoc, new LichHocAdapter.OnLichHocActionListener() {
            @Override
            public void khiSua(LichHoc lichHoc) {
                //Mở hộp thoại sửa lịch học
                hienThiHopThoaiLichHoc(lichHoc);
            }

            @Override
            public void khiXoa(LichHoc lichHoc) {
                //Mở hộp thoại xác nhận xóa lịch học
                xacNhanXoaLichHoc(lichHoc);
            }
        });
        lvLichHoc.setAdapter(boChuyenDoiLichHoc);

        //Xử lý khi nhấn vào một dòng trong ListView để sửa lịch học
        lvLichHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> khungCha, View giaoDienDong, int viTri, long maDong) {
                LichHoc lichHocChon = dsLichHoc.get(viTri);
                hienThiHopThoaiLichHoc(lichHocChon);
            }
        });

        //Xử lý khi nhấn nút (+) thêm lịch học mới
        btnThemLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiHopThoaiLichHoc(null);
            }
        });

        //Xử lý sự kiện chuyển màn hình trên thanh điều hướng dưới cùng qua Intent
        mucTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(LichHocActivity.this, MainActivity.class);
                yDinh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        mucMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(LichHocActivity.this, MonHocActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        mucCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(LichHocActivity.this, BaiTapActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //Nhận dữ liệu thứ được truyền từ MainActivity qua Intent (nếu có)
        Intent yDinhNhan = getIntent();
        if (yDinhNhan != null && yDinhNhan.hasExtra("thuChon")) {
            String thuTuIntent = yDinhNhan.getStringExtra("thuChon");
            if (thuTuIntent != null) {
                chonTabTheoTenThu(thuTuIntent);
            }
        }

        //Hiển thị thông tin tuần và lịch học
        capNhatTieuDeTuan();
        taiLichHocTheoThuDangChon();
    }

    //Hàm tính toán và hiển thị khoảng ngày của tuần đang chọn
    private void capNhatTieuDeTuan() {
        Calendar lich = Calendar.getInstance();
        lich.add(Calendar.WEEK_OF_YEAR, doLechTuan);
        lich.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat dinhDangNgay = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String ngayDauTuan = dinhDangNgay.format(lich.getTime());
        lich.add(Calendar.DAY_OF_WEEK, 6);
        String ngayCuoiTuan = dinhDangNgay.format(lich.getTime());
        String nhanTuan = (doLechTuan == 0) ? "Tuần này" : (doLechTuan > 0 ? "Tuần +" + doLechTuan : "Tuần " + doLechTuan);
        tvKhoangThoiGianTuan.setText(nhanTuan + " (" + ngayDauTuan + " – " + ngayCuoiTuan + ")");
    }

    //Hàm truy vấn danh sách lịch học theo thứ từ SQLite và hiển thị lên ListView
    private void taiLichHocTheoThuDangChon() {
        String thuChon = MANG_THU_TRONG_TUAN[viTriThuDangChon];
        List<LichHoc> danhSachKetQua = csdlHelper.layDanhSachLichHocTheoThu(thuChon);
        //Hiển thị kết quả lên ListView
        boChuyenDoiLichHoc.capNhatDuLieu(danhSachKetQua);
        tvLichHocTrong.setVisibility(danhSachKetQua.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Hàm hiển thị hộp thoại Dialog Thêm mới hoặc Cập nhật lịch học
    private void hienThiHopThoaiLichHoc(@Nullable final LichHoc lichHocCanSua) {
        //Lấy danh sách môn học từ SQLite để đưa vào Spinner chọn môn
        final List<MonHoc> danhSachMonHoc = csdlHelper.layDanhSachMonHoc();
        if (danhSachMonHoc.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm môn học trước khi tạo thời khóa biểu!", Toast.LENGTH_LONG).show();
            Intent yDinh = new Intent(LichHocActivity.this, MonHocActivity.class);
            startActivity(yDinh);
            finish();
            return;
        }

        View giaoDienHopThoai = LayoutInflater.from(this).inflate(R.layout.dialog_them_lich_hoc, null);
        final AlertDialog hopThoai = new AlertDialog.Builder(this)
                .setView(giaoDienHopThoai)
                .create();
        if (hopThoai.getWindow() != null) {
            hopThoai.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        TextView tvTieuDeHopThoai = giaoDienHopThoai.findViewById(R.id.tvDialogTitle);
        final Spinner spMonHoc = giaoDienHopThoai.findViewById(R.id.spSubject);
        final Spinner spThuTrongTuan = giaoDienHopThoai.findViewById(R.id.spDayOfWeek);
        final Spinner spGioBatDau = giaoDienHopThoai.findViewById(R.id.spStartTime);
        final Spinner spGioKetThuc = giaoDienHopThoai.findViewById(R.id.spEndTime);
        final EditText edtPhongHoc = giaoDienHopThoai.findViewById(R.id.etRoom);
        final EditText edtGiangVien = giaoDienHopThoai.findViewById(R.id.etTeacher);
        final Spinner spNgayBatDau = giaoDienHopThoai.findViewById(R.id.spStartDate);
        final Spinner spNgayKetThuc = giaoDienHopThoai.findViewById(R.id.spEndDate);
        TextView btnHuy = giaoDienHopThoai.findViewById(R.id.btnCancel);
        AppCompatButton btnLuu = giaoDienHopThoai.findViewById(R.id.btnSave);

        //Gán danh sách môn học và thứ trong tuần vào Spinner
        ArrayAdapter<MonHoc> boChuyenDoiMon = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, danhSachMonHoc);
        spMonHoc.setAdapter(boChuyenDoiMon);

        ArrayAdapter<String> boChuyenDoiThu = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, MANG_THU_TRONG_TUAN);
        spThuTrongTuan.setAdapter(boChuyenDoiThu);
        spThuTrongTuan.setSelection(viTriThuDangChon);

        //Khởi tạo danh sách khung giờ và ngày bắt đầu/kết thúc cho Spinner
        String[] mangGioBatDau = {"07:00", "07:30", "08:00", "09:00", "09:45", "12:30", "13:00", "13:30", "15:00", "15:30", "17:30", "18:00"};
        String[] mangGioKetThuc = {"09:00", "09:30", "10:00", "11:00", "11:45", "14:30", "15:00", "15:30", "17:00", "17:30", "19:30", "20:00"};
        String[] mangNgayBatDau = {"01/09/2026", "15/09/2026", "01/10/2026", "01/01/2027"};
        String[] mangNgayKetThuc = {"30/12/2026", "15/01/2027", "30/05/2027"};

        spGioBatDau.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mangGioBatDau));
        spGioKetThuc.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mangGioKetThuc));
        spNgayBatDau.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mangNgayBatDau));
        spNgayKetThuc.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mangNgayKetThuc));

        //Tự động điền tên giảng viên tương ứng khi chọn môn học trên Spinner
        spMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> khungCha, View giaoDienMuc, int viTri, long maMuc) {
                MonHoc monHocChon = danhSachMonHoc.get(viTri);
                if (lichHocCanSua == null && monHocChon.getGiangVien() != null) {
                    edtGiangVien.setText(monHocChon.getGiangVien());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> khungCha) {
            }
        });

        //Nếu là cập nhật lịch học thì hiển thị lại thông tin cũ lên hộp thoại
        if (lichHocCanSua != null) {
            tvTieuDeHopThoai.setText("Cập nhật lịch học");
            edtPhongHoc.setText(lichHocCanSua.getPhongHoc());
            edtGiangVien.setText(lichHocCanSua.getGiangVien());

            for (int i = 0; i < danhSachMonHoc.size(); i++) {
                if (danhSachMonHoc.get(i).getId() == lichHocCanSua.getMonHocId()) {
                    spMonHoc.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < MANG_THU_TRONG_TUAN.length; i++) {
                if (MANG_THU_TRONG_TUAN[i].equalsIgnoreCase(lichHocCanSua.getThu())) {
                    spThuTrongTuan.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < mangGioBatDau.length; i++) {
                if (mangGioBatDau[i].equalsIgnoreCase(lichHocCanSua.getGioBatDau())) {
                    spGioBatDau.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < mangGioKetThuc.length; i++) {
                if (mangGioKetThuc[i].equalsIgnoreCase(lichHocCanSua.getGioKetThuc())) {
                    spGioKetThuc.setSelection(i);
                    break;
                }
            }
        } else {
            tvTieuDeHopThoai.setText(R.string.dialog_title_add_schedule);
        }

        //Xử lý khi nhấn nút Hủy
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopThoai.dismiss();
            }
        });

        //Xử lý khi nhấn nút Lưu lịch học vào SQLite
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ các Spinner và EditText
                MonHoc monHocChon = (MonHoc) spMonHoc.getSelectedItem();
                String thu = spThuTrongTuan.getSelectedItem().toString();
                String gioBatDau = spGioBatDau.getSelectedItem().toString();
                String gioKetThuc = spGioKetThuc.getSelectedItem().toString();
                String phongHoc = edtPhongHoc.getText().toString().trim();
                String giangVien = edtGiangVien.getText().toString().trim();
                String ngayBatDau = spNgayBatDau.getSelectedItem().toString();
                String ngayKetThuc = spNgayKetThuc.getSelectedItem().toString();

                if (phongHoc.isEmpty()) {
                    Toast.makeText(LichHocActivity.this, "Vui lòng nhập phòng học", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lichHocCanSua == null) {
                    //Thêm lịch học mới vào SQLite
                    LichHoc lichHocMoi = new LichHoc(monHocChon.getId(), thu, gioBatDau, gioKetThuc, phongHoc, giangVien, ngayBatDau, ngayKetThuc);
                    long maMoi = csdlHelper.themLichHoc(lichHocMoi);
                    if (maMoi != -1) {
                        Toast.makeText(LichHocActivity.this, "Đã thêm lịch học môn " + monHocChon.getTenMonHoc(), Toast.LENGTH_SHORT).show();
                        chonTabTheoTenThu(thu);
                        hopThoai.dismiss();
                    }
                } else {
                    //Cập nhật lịch học hiện có trong SQLite
                    lichHocCanSua.setMonHocId(monHocChon.getId());
                    lichHocCanSua.setThu(thu);
                    lichHocCanSua.setGioBatDau(gioBatDau);
                    lichHocCanSua.setGioKetThuc(gioKetThuc);
                    lichHocCanSua.setPhongHoc(phongHoc);
                    lichHocCanSua.setGiangVien(giangVien);
                    lichHocCanSua.setNgayBatDau(ngayBatDau);
                    lichHocCanSua.setNgayKetThuc(ngayKetThuc);
                    csdlHelper.capNhatLichHoc(lichHocCanSua);
                    Toast.makeText(LichHocActivity.this, "Đã cập nhật lịch học!", Toast.LENGTH_SHORT).show();
                    chonTabTheoTenThu(thu);
                    hopThoai.dismiss();
                }
            }
        });

        hopThoai.show();
    }

    //Hàm chuyển TabLayout tới đúng thứ vừa thêm, sửa hoặc nhận từ Intent
    private void chonTabTheoTenThu(String tenThu) {
        for (int i = 0; i < MANG_THU_TRONG_TUAN.length; i++) {
            if (MANG_THU_TRONG_TUAN[i].equalsIgnoreCase(tenThu)) {
                viTriThuDangChon = i;
                TabLayout.Tab tabTimThay = tabLayoutCacThu.getTabAt(i);
                if (tabTimThay != null) {
                    tabTimThay.select();
                }
                tvTieuDeThuDangChon.setText(MANG_THU_TRONG_TUAN[viTriThuDangChon]);
                taiLichHocTheoThuDangChon();
                break;
            }
        }
    }

    //Hàm xác nhận và xóa lịch học khỏi SQLite
    private void xacNhanXoaLichHoc(final LichHoc lichHoc) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa lịch học")
                .setMessage("Bạn có chắc chắn muốn xóa lịch học môn \"" + lichHoc.getTenMonHoc() + "\" (" + lichHoc.getGioBatDau() + " - " + lichHoc.getGioKetThuc() + ")?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface hopThoai, int viTriNut) {
                        csdlHelper.xoaLichHoc(lichHoc.getId());
                        Toast.makeText(LichHocActivity.this, "Đã xóa lịch học", Toast.LENGTH_SHORT).show();
                        taiLichHocTheoThuDangChon();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

