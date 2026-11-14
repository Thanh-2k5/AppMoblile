package com.example.quan_ly_lich_hoc_sv;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.quan_ly_lich_hoc_sv.adapters.BaiTapAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.BaiTap;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BaiTapActivity extends AppCompatActivity {

    //Khai báo các biến giao diện
    ListView lvBaiTap;
    TextView tvBaiTapTrong;
    TabLayout tabLayoutBaiTap;
    ImageButton btnThemBaiTap;
    View mucTrangChu, mucMonHoc, mucLichHoc;

    //Khai báo các biến xử lý dữ liệu và bộ lọc trạng thái
    DatabaseHelper csdlHelper;
    BaiTapAdapter boChuyenDoiBaiTap;
    List<BaiTap> dsBaiTap = new ArrayList<>();
    String boLocHienTai = "Tất cả";

    @Override
    protected void onCreate(Bundle trangThaiLuu) {
        super.onCreate(trangThaiLuu);
        setContentView(R.layout.activity_bai_tap);

        //Khởi tạo đối tượng SQLite
        csdlHelper = DatabaseHelper.getInstance(this);

        //Tham chiếu Id đến các biến giao diện
        lvBaiTap = findViewById(R.id.lvTasks);
        tvBaiTapTrong = findViewById(R.id.tvEmptyTasks);
        tabLayoutBaiTap = findViewById(R.id.tabLayoutTasks);
        btnThemBaiTap = findViewById(R.id.fabAddTask);

        mucTrangChu = findViewById(R.id.navHome);
        mucMonHoc = findViewById(R.id.navSubjects);
        mucLichHoc = findViewById(R.id.navSchedule);

        //Thêm 3 tab lọc trạng thái vào TabLayout
        tabLayoutBaiTap.addTab(tabLayoutBaiTap.newTab().setText("Tất cả"));
        tabLayoutBaiTap.addTab(tabLayoutBaiTap.newTab().setText("Chưa hoàn thành"));
        tabLayoutBaiTap.addTab(tabLayoutBaiTap.newTab().setText("Đã hoàn thành"));

        //Xử lý sự kiện lọc công việc khi chọn tab trên TabLayout
        tabLayoutBaiTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabChon) {
                int viTri = tabChon.getPosition();
                if (viTri == 1) {
                    boLocHienTai = "Chưa hoàn thành";
                } else if (viTri == 2) {
                    boLocHienTai = "Đã hoàn thành";
                } else {
                    boLocHienTai = "Tất cả";
                }
                taiDanhSachBaiTap();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabBoChon) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tabChonLai) {
            }
        });

        //Khởi tạo Adapter và gán vào ListView hiển thị danh sách bài tập/công việc
        boChuyenDoiBaiTap = new BaiTapAdapter(this, dsBaiTap, new BaiTapAdapter.OnBaiTapActionListener() {
            @Override
            public void khiSua(BaiTap baiTap) {
                //Mở hộp thoại sửa bài tập
                hienThiHopThoaiBaiTap(baiTap);
            }

            @Override
            public void khiXoa(BaiTap baiTap) {
                //Mở hộp thoại xác nhận xóa bài tập
                xacNhanXoaBaiTap(baiTap);
            }

            @Override
            public void khiDoiTrangThai(BaiTap baiTap) {
                //Đổi nhanh trạng thái bài tập khi nhấn vào nhãn trạng thái
                String trangThaiCu = baiTap.getTrangThai();
                String trangThaiMoi = "Đã hoàn thành".equalsIgnoreCase(trangThaiCu) ? "Chưa hoàn thành" : "Đã hoàn thành";
                csdlHelper.capNhatTrangThaiBaiTap(baiTap.getId(), trangThaiMoi);
                Toast.makeText(BaiTapActivity.this, "Đã chuyển trạng thái: " + trangThaiMoi, Toast.LENGTH_SHORT).show();
                taiDanhSachBaiTap();
            }
        });
        lvBaiTap.setAdapter(boChuyenDoiBaiTap);

        //Xử lý khi nhấn vào một dòng trên ListView để sửa công việc
        lvBaiTap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> khungCha, View giaoDienDong, int viTri, long maDong) {
                BaiTap baiTapChon = dsBaiTap.get(viTri);
                hienThiHopThoaiBaiTap(baiTapChon);
            }
        });

        //Xử lý sự kiện khi nhấn nút (+) thêm công việc mới
        btnThemBaiTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiHopThoaiBaiTap(null);
            }
        });

        //Xử lý sự kiện chuyển màn hình trên thanh điều hướng dưới cùng qua Intent
        mucTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(BaiTapActivity.this, MainActivity.class);
                yDinh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        mucMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(BaiTapActivity.this, MonHocActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        mucLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(BaiTapActivity.this, LichHocActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //Nhận dữ liệu bộ lọc trạng thái truyền từ MainActivity qua Intent (nếu có)
        Intent yDinhNhan = getIntent();
        if (yDinhNhan != null && yDinhNhan.hasExtra("boLoc")) {
            String boLocTuIntent = yDinhNhan.getStringExtra("boLoc");
            if ("Chưa hoàn thành".equalsIgnoreCase(boLocTuIntent)) {
                boLocHienTai = "Chưa hoàn thành";
                TabLayout.Tab tabChuaXong = tabLayoutBaiTap.getTabAt(1);
                if (tabChuaXong != null) {
                    tabChuaXong.select();
                }
            }
        }

        //Tải danh sách công việc
        taiDanhSachBaiTap();
    }

    //Hàm đọc danh sách bài tập từ SQLite theo bộ lọc và hiển thị lên ListView
    private void taiDanhSachBaiTap() {
        List<BaiTap> danhSachKetQua = csdlHelper.layDanhSachBaiTap(boLocHienTai);
        //Hiển thị kết quả lên ListView
        boChuyenDoiBaiTap.capNhatDuLieu(danhSachKetQua);
        tvBaiTapTrong.setVisibility(danhSachKetQua.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Hàm hiển thị hộp thoại Dialog Thêm mới hoặc Sửa công việc học tập
    private void hienThiHopThoaiBaiTap(@Nullable final BaiTap baiTapCanSua) {
        //Lấy danh sách môn học từ SQLite
        List<MonHoc> danhSachMonHoc = csdlHelper.layDanhSachMonHoc();
        if (danhSachMonHoc.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm môn học trước khi thêm bài tập!", Toast.LENGTH_LONG).show();
            Intent yDinh = new Intent(BaiTapActivity.this, MonHocActivity.class);
            startActivity(yDinh);
            finish();
            return;
        }

        View giaoDienHopThoai = LayoutInflater.from(this).inflate(R.layout.dialog_them_bai_tap, null);
        final AlertDialog hopThoai = new AlertDialog.Builder(this)
                .setView(giaoDienHopThoai)
                .create();
        if (hopThoai.getWindow() != null) {
            hopThoai.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        TextView tvTieuDeHopThoai = giaoDienHopThoai.findViewById(R.id.tvDialogTitle);
        final EditText edtTenBaiTap = giaoDienHopThoai.findViewById(R.id.etTaskName);
        final Spinner spMonHoc = giaoDienHopThoai.findViewById(R.id.spSubject);
        final EditText edtNoiDungBaiTap = giaoDienHopThoai.findViewById(R.id.etTaskDescription);
        final EditText edtHanNop = giaoDienHopThoai.findViewById(R.id.etSubmissionDeadline);
        final Spinner spMucDoUuTien = giaoDienHopThoai.findViewById(R.id.spPriority);
        final Spinner spTrangThaiBaiTap = giaoDienHopThoai.findViewById(R.id.spTaskStatus);
        TextView btnHuy = giaoDienHopThoai.findViewById(R.id.btnCancel);
        AppCompatButton btnLuu = giaoDienHopThoai.findViewById(R.id.btnSave);

        //Đổ dữ liệu vào các Spinner môn học, mức độ ưu tiên và trạng thái
        spMonHoc.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, danhSachMonHoc));

        String[] mangUuTien = {"Cao", "Trung bình", "Thấp"};
        spMucDoUuTien.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mangUuTien));

        String[] mangTrangThai = {"Chưa hoàn thành", "Đã hoàn thành"};
        spTrangThaiBaiTap.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mangTrangThai));

        //Mở hộp thoại DatePickerDialog và TimePickerDialog khi nhấn vào ô Hạn nộp
        edtHanNop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar lich = Calendar.getInstance();
                new DatePickerDialog(BaiTapActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker boChonNgay, final int nam, final int thang, final int ngay) {
                        new TimePickerDialog(BaiTapActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker boChonGio, int gio, int phut) {
                                String chuoiThoiGian = String.format(Locale.getDefault(), "%02d:%02d - %02d/%02d/%04d",
                                        gio, phut, ngay, thang + 1, nam);
                                edtHanNop.setText(chuoiThoiGian);
                            }
                        }, 23, 59, true).show();
                    }
                }, lich.get(Calendar.YEAR), lich.get(Calendar.MONTH), lich.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Nếu đang ở chế độ Sửa thì điền thông tin cũ vào các ô nhập
        if (baiTapCanSua != null) {
            tvTieuDeHopThoai.setText("Cập nhật công việc");
            edtTenBaiTap.setText(baiTapCanSua.getTenBaiTap());
            edtNoiDungBaiTap.setText(baiTapCanSua.getNoiDung());
            edtHanNop.setText(baiTapCanSua.getHanNop());

            for (int i = 0; i < danhSachMonHoc.size(); i++) {
                if (danhSachMonHoc.get(i).getId() == baiTapCanSua.getMonHocId()) {
                    spMonHoc.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < mangUuTien.length; i++) {
                if (mangUuTien[i].equalsIgnoreCase(baiTapCanSua.getMucDoUuTien())) {
                    spMucDoUuTien.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < mangTrangThai.length; i++) {
                if (mangTrangThai[i].equalsIgnoreCase(baiTapCanSua.getTrangThai())) {
                    spTrangThaiBaiTap.setSelection(i);
                    break;
                }
            }
        } else {
            tvTieuDeHopThoai.setText(R.string.dialog_title_add_task);
            edtHanNop.setText("23:59 - 25/11/2026");
        }

        //Xử lý khi nhấn nút Hủy
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopThoai.dismiss();
            }
        });

        //Xử lý khi nhấn nút Lưu công việc
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ các ô nhập liệu và Spinner
                String tenBaiTap = edtTenBaiTap.getText().toString().trim();
                MonHoc monHocChon = (MonHoc) spMonHoc.getSelectedItem();
                String noiDung = edtNoiDungBaiTap.getText().toString().trim();
                String hanNop = edtHanNop.getText().toString().trim();
                String uuTien = spMucDoUuTien.getSelectedItem().toString();
                String trangThai = spTrangThaiBaiTap.getSelectedItem().toString();

                if (tenBaiTap.isEmpty()) {
                    Toast.makeText(BaiTapActivity.this, "Vui lòng nhập tên công việc / bài tập", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (baiTapCanSua == null) {
                    //Thêm bài tập mới vào SQLite
                    BaiTap baiTapMoi = new BaiTap(monHocChon.getId(), tenBaiTap, noiDung, hanNop, uuTien, trangThai);
                    long maMoi = csdlHelper.themBaiTap(baiTapMoi);
                    if (maMoi != -1) {
                        Toast.makeText(BaiTapActivity.this, "Đã thêm công việc mới!", Toast.LENGTH_SHORT).show();
                        taiDanhSachBaiTap();
                        hopThoai.dismiss();
                    }
                } else {
                    //Cập nhật bài tập hiện có trong SQLite
                    baiTapCanSua.setMonHocId(monHocChon.getId());
                    baiTapCanSua.setTenBaiTap(tenBaiTap);
                    baiTapCanSua.setNoiDung(noiDung);
                    baiTapCanSua.setHanNop(hanNop);
                    baiTapCanSua.setMucDoUuTien(uuTien);
                    baiTapCanSua.setTrangThai(trangThai);
                    csdlHelper.capNhatBaiTap(baiTapCanSua);
                    Toast.makeText(BaiTapActivity.this, "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                    taiDanhSachBaiTap();
                    hopThoai.dismiss();
                }
            }
        });

        hopThoai.show();
    }

    //Hàm xác nhận và xóa bài tập khỏi SQLite
    private void xacNhanXoaBaiTap(final BaiTap baiTap) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa công việc")
                .setMessage("Bạn có chắc chắn muốn xóa công việc \"" + baiTap.getTenBaiTap() + "\"?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface hopThoai, int viTriNut) {
                        csdlHelper.xoaBaiTap(baiTap.getId());
                        Toast.makeText(BaiTapActivity.this, "Đã xóa công việc", Toast.LENGTH_SHORT).show();
                        taiDanhSachBaiTap();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

