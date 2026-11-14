package com.example.quan_ly_lich_hoc_sv;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.BaiTap;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Khai báo các biến giao diện
    TextView tvTenSinhVien, tvNgayHienTai;
    TextView tvSoMonHoc, tvSoTietHoc, tvSoBaiTap;
    TextView tvTieuDeLichHomNay;
    LinearLayout theThongKeMonHoc, theThongKeTietHoc, theThongKeBaiTap;
    LinearLayout khungDanhSachLichHomNay, khungDanhSachBaiTapSapToi;
    View mucMonHoc, mucLichHoc, mucCongViec;

    //Khai báo biến xử lý cơ sở dữ liệu SQLite
    DatabaseHelper csdlHelper;

    @Override
    protected void onCreate(Bundle trangThaiLuu) {
        super.onCreate(trangThaiLuu);
        setContentView(R.layout.activity_trang_chu);

        //Khởi tạo đối tượng SQLite
        csdlHelper = DatabaseHelper.getInstance(this);

        //Tham chiếu Id đến các biến giao diện
        tvTenSinhVien = findViewById(R.id.tvHomeUserName);
        tvNgayHienTai = findViewById(R.id.tvHomeDate);

        tvSoMonHoc = findViewById(R.id.tvStatCoursesCount);
        tvSoTietHoc = findViewById(R.id.tvStatLessonsCount);
        tvSoBaiTap = findViewById(R.id.tvStatTasksCount);
        tvTieuDeLichHomNay = findViewById(R.id.tvTodayScheduleTitle);

        theThongKeMonHoc = findViewById(R.id.cardStatCourses);
        theThongKeTietHoc = findViewById(R.id.cardStatLessons);
        theThongKeBaiTap = findViewById(R.id.cardStatTasks);

        khungDanhSachLichHomNay = findViewById(R.id.layoutTodayScheduleList);
        khungDanhSachBaiTapSapToi = findViewById(R.id.layoutUpcomingTasksList);

        mucMonHoc = findViewById(R.id.navSubjects);
        mucLichHoc = findViewById(R.id.navSchedule);
        mucCongViec = findViewById(R.id.navTasks);

        //Xử lý sự kiện chuyển sang các Activity chức năng qua Intent khi nhấn vào thẻ thống kê
        theThongKeMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, MonHocActivity.class);
                startActivity(yDinh);
            }
        });

        theThongKeTietHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, LichHocActivity.class);
                //Truyền thứ hiện tại sang LichHocActivity qua Intent
                yDinh.putExtra("thuChon", layThuHienTaiBangTiengViet());
                startActivity(yDinh);
            }
        });

        theThongKeBaiTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, BaiTapActivity.class);
                //Truyền bộ lọc trạng thái sang BaiTapActivity qua Intent
                yDinh.putExtra("boLoc", "Chưa hoàn thành");
                startActivity(yDinh);
            }
        });

        //Xử lý sự kiện chuyển màn hình trên thanh điều hướng dưới cùng
        mucMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, MonHocActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
            }
        });

        mucLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, LichHocActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
            }
        });

        mucCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, BaiTapActivity.class);
                startActivity(yDinh);
                overridePendingTransition(0, 0);
            }
        });

        //Tổng hợp và hiển thị dữ liệu từ SQLite lên Trang chủ
        taiDuLieuTrangChu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Cập nhật lại dữ liệu thống kê mỗi khi quay lại màn hình Trang chủ
        taiDuLieuTrangChu();
    }

    //Hàm xác định thứ hiện tại trong tuần bằng tiếng Việt
    private String layThuHienTaiBangTiengViet() {
        Calendar lich = Calendar.getInstance();
        int thuTrongTuan = lich.get(Calendar.DAY_OF_WEEK);
        switch (thuTrongTuan) {
            case Calendar.MONDAY: return "Thứ 2";
            case Calendar.TUESDAY: return "Thứ 3";
            case Calendar.WEDNESDAY: return "Thứ 4";
            case Calendar.THURSDAY: return "Thứ 5";
            case Calendar.FRIDAY: return "Thứ 6";
            case Calendar.SATURDAY: return "Thứ 7";
            default: return "Chủ nhật";
        }
    }

    //Hàm tổng hợp dữ liệu từ các bảng trong SQLite và hiển thị kết quả lên Trang chủ
    private void taiDuLieuTrangChu() {
        //Hiển thị lời chào và ngày hiện tại
        tvTenSinhVien.setText("Sinh viên");
        String thuHomNay = layThuHienTaiBangTiengViet();
        SimpleDateFormat dinhDangNgay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvNgayHienTai.setText(thuHomNay + ", " + dinhDangNgay.format(Calendar.getInstance().getTime()));

        //Đếm số lượng môn học, tiết học hôm nay và bài tập chưa xong
        int tongSoMon = csdlHelper.demSoMonHoc();
        int soTietHomNay = csdlHelper.demSoLichHocTheoThu(thuHomNay);
        int soBaiTapChuaXong = csdlHelper.demSoBaiTapChuaXong();

        //Hiển thị kết quả thống kê
        tvSoMonHoc.setText(String.valueOf(tongSoMon));
        tvSoTietHoc.setText(String.valueOf(soTietHomNay));
        tvSoBaiTap.setText(String.valueOf(soBaiTapChuaXong));

        //1. Hiển thị danh sách Lịch học hôm nay (hoặc lịch học trong tuần nếu hôm nay trống)
        khungDanhSachLichHomNay.removeAllViews();
        List<LichHoc> danhSachLichHomNay = csdlHelper.layDanhSachLichHocTheoThu(thuHomNay);
        if (danhSachLichHomNay.isEmpty()) {
            List<LichHoc> tatCaLichHoc = csdlHelper.layTatCaLichHoc();
            if (tatCaLichHoc.isEmpty()) {
                tvTieuDeLichHomNay.setText("LỊCH HỌC HÔM NAY (" + thuHomNay.toUpperCase() + ")");
                themDongThongBaoTrong(khungDanhSachLichHomNay, "Chưa có lịch học nào được đăng ký.");
            } else {
                tvTieuDeLichHomNay.setText("LỊCH HỌC TRONG TUẦN");
                int gioiHan = Math.min(tatCaLichHoc.size(), 3);
                for (int i = 0; i < gioiHan; i++) {
                    themTheLichHoc(khungDanhSachLichHomNay, tatCaLichHoc.get(i), true);
                }
            }
        } else {
            tvTieuDeLichHomNay.setText("LỊCH HỌC HÔM NAY (" + thuHomNay.toUpperCase() + ")");
            for (int i = 0; i < danhSachLichHomNay.size(); i++) {
                themTheLichHoc(khungDanhSachLichHomNay, danhSachLichHomNay.get(i), false);
            }
        }

        //2. Hiển thị danh sách Công việc sắp đến hạn (chưa hoàn thành)
        khungDanhSachBaiTapSapToi.removeAllViews();
        List<BaiTap> danhSachBaiTapChuaXong = csdlHelper.layDanhSachBaiTap("Chưa hoàn thành");
        if (danhSachBaiTapChuaXong.isEmpty()) {
            themDongThongBaoTrong(khungDanhSachBaiTapSapToi, "Tuyệt vời! Bạn đã hoàn thành hết bài tập.");
        } else {
            int gioiHan = Math.min(danhSachBaiTapChuaXong.size(), 3);
            for (int i = 0; i < gioiHan; i++) {
                themMucBaiTap(khungDanhSachBaiTapSapToi, danhSachBaiTapChuaXong.get(i));
            }
        }
    }

    //Hàm tạo dòng thông báo khi danh sách trống
    private void themDongThongBaoTrong(LinearLayout khungChua, String noiDungThongBao) {
        TextView tvThongBao = new TextView(this);
        tvThongBao.setText(noiDungThongBao);
        tvThongBao.setTextColor(Color.parseColor("#A0B2C6"));
        tvThongBao.setTextSize(14f);
        LinearLayout.LayoutParams thamSoBoCuc = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thamSoBoCuc.topMargin = doiDpSangPx(10);
        tvThongBao.setLayoutParams(thamSoBoCuc);
        khungChua.addView(tvThongBao);
    }

    //Hàm tạo thẻ hiển thị một ca học trên Trang chủ
    private void themTheLichHoc(LinearLayout khungChua, final LichHoc lichHoc, boolean hienThiThu) {
        LinearLayout theLich = new LinearLayout(this);
        theLich.setOrientation(LinearLayout.VERTICAL);
        theLich.setBackgroundResource(R.drawable.bg_light_card);
        int demLe = doiDpSangPx(14);
        theLich.setPadding(demLe, demLe, demLe, demLe);
        LinearLayout.LayoutParams thamSoBoCuc = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thamSoBoCuc.topMargin = doiDpSangPx(12);
        theLich.setLayoutParams(thamSoBoCuc);

        TextView tvTenMon = new TextView(this);
        tvTenMon.setText(lichHoc.getTenMonHoc());
        tvTenMon.setTextColor(Color.parseColor("#0F2537"));
        tvTenMon.setTextSize(17f);
        tvTenMon.setTypeface(null, Typeface.BOLD);
        theLich.addView(tvTenMon);

        TextView tvGioHoc = new TextView(this);
        String tienToThu = hienThiThu ? (lichHoc.getThu() + " | ") : "";
        tvGioHoc.setText(tienToThu + lichHoc.getGioBatDau() + " – " + lichHoc.getGioKetThuc());
        tvGioHoc.setTextColor(Color.parseColor("#5A6E85"));
        tvGioHoc.setTextSize(14f);
        tvGioHoc.setPadding(0, doiDpSangPx(4), 0, 0);
        theLich.addView(tvGioHoc);

        TextView tvPhongHoc = new TextView(this);
        tvPhongHoc.setText("Phòng: " + (lichHoc.getPhongHoc() != null ? lichHoc.getPhongHoc() : ""));
        tvPhongHoc.setTextColor(Color.parseColor("#5A6E85"));
        tvPhongHoc.setTextSize(14f);
        tvPhongHoc.setPadding(0, doiDpSangPx(2), 0, 0);
        theLich.addView(tvPhongHoc);

        TextView tvGiangVien = new TextView(this);
        tvGiangVien.setText("GV: " + (lichHoc.getGiangVien() != null ? lichHoc.getGiangVien() : ""));
        tvGiangVien.setTextColor(Color.parseColor("#5A6E85"));
        tvGiangVien.setTextSize(14f);
        tvGiangVien.setPadding(0, doiDpSangPx(2), 0, 0);
        theLich.addView(tvGiangVien);

        theLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, LichHocActivity.class);
                //Truyền thứ của ca học sang LichHocActivity qua Intent
                yDinh.putExtra("thuChon", lichHoc.getThu());
                startActivity(yDinh);
            }
        });
        khungChua.addView(theLich);
    }

    //Hàm tạo mục hiển thị một bài tập sắp đến hạn trên Trang chủ
    private void themMucBaiTap(LinearLayout khungChua, BaiTap baiTap) {
        RelativeLayout dongBaiTap = new RelativeLayout(this);
        LinearLayout.LayoutParams thamSoDong = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thamSoDong.topMargin = doiDpSangPx(12);
        dongBaiTap.setLayoutParams(thamSoDong);

        View thanhMau = new View(this);
        thanhMau.setId(View.generateViewId());
        RelativeLayout.LayoutParams thamSoThanh = new RelativeLayout.LayoutParams(doiDpSangPx(4), doiDpSangPx(64));
        thamSoThanh.addRule(RelativeLayout.ALIGN_PARENT_START);
        thanhMau.setLayoutParams(thamSoThanh);
        thanhMau.setBackgroundColor(Color.parseColor("#D4A373"));
        dongBaiTap.addView(thanhMau);

        TextView nhanTrangThai = new TextView(this);
        nhanTrangThai.setId(View.generateViewId());
        RelativeLayout.LayoutParams thamSoNhan = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thamSoNhan.addRule(RelativeLayout.ALIGN_PARENT_END);
        thamSoNhan.addRule(RelativeLayout.CENTER_VERTICAL);
        nhanTrangThai.setLayoutParams(thamSoNhan);
        nhanTrangThai.setBackgroundResource(R.drawable.bg_chip_unfinished);
        nhanTrangThai.setPadding(doiDpSangPx(10), doiDpSangPx(6), doiDpSangPx(10), doiDpSangPx(6));
        nhanTrangThai.setText(baiTap.getTrangThai());
        nhanTrangThai.setTextColor(Color.parseColor("#D0DCE8"));
        nhanTrangThai.setTextSize(12f);
        dongBaiTap.addView(nhanTrangThai);

        LinearLayout khungThongTin = new LinearLayout(this);
        khungThongTin.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams thamSoThongTin = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thamSoThongTin.addRule(RelativeLayout.END_OF, thanhMau.getId());
        thamSoThongTin.addRule(RelativeLayout.START_OF, nhanTrangThai.getId());
        thamSoThongTin.setMarginStart(doiDpSangPx(12));
        thamSoThongTin.setMarginEnd(doiDpSangPx(8));
        khungThongTin.setLayoutParams(thamSoThongTin);

        TextView tvTenBai = new TextView(this);
        tvTenBai.setText(baiTap.getTenBaiTap());
        tvTenBai.setTextColor(Color.WHITE);
        tvTenBai.setTextSize(16f);
        tvTenBai.setTypeface(null, Typeface.BOLD);
        khungThongTin.addView(tvTenBai);

        TextView tvTenMon = new TextView(this);
        tvTenMon.setText("Môn: " + (baiTap.getTenMonHoc() != null ? baiTap.getTenMonHoc() : ""));
        tvTenMon.setTextColor(Color.parseColor("#A0B2C6"));
        tvTenMon.setTextSize(14f);
        tvTenMon.setPadding(0, doiDpSangPx(2), 0, 0);
        khungThongTin.addView(tvTenMon);

        TextView tvHanNop = new TextView(this);
        tvHanNop.setText("Hạn: " + (baiTap.getHanNop() != null ? baiTap.getHanNop() : ""));
        tvHanNop.setTextColor(Color.parseColor("#A0B2C6"));
        tvHanNop.setTextSize(14f);
        tvHanNop.setPadding(0, doiDpSangPx(2), 0, 0);
        khungThongTin.addView(tvHanNop);

        dongBaiTap.addView(khungThongTin);
        dongBaiTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yDinh = new Intent(MainActivity.this, BaiTapActivity.class);
                yDinh.putExtra("boLoc", "Chưa hoàn thành");
                startActivity(yDinh);
            }
        });
        khungChua.addView(dongBaiTap);
    }

    //Hàm quy đổi đơn vị dp sang pixel
    private int doiDpSangPx(int soDp) {
        float matDoManHinh = getResources().getDisplayMetrics().density;
        return Math.round(soDp * matDoManHinh);
    }
}
