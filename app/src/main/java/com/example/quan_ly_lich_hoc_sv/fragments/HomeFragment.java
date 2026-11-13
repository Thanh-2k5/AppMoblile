package com.example.quan_ly_lich_hoc_sv.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quan_ly_lich_hoc_sv.HomeActivity;
import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.BaiTap;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;
import com.example.quan_ly_lich_hoc_sv.models.LichThi;
import com.example.quan_ly_lich_hoc_sv.models.NguoiDung;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    //Khai báo các biến giao diện
    private TextView tvHomeUserName, tvHomeDate, tvNotificationBadge;
    private TextView tvStatCoursesCount, tvStatLessonsCount, tvStatTasksCount;
    private TextView tvTodayScheduleTitle;
    private LinearLayout layoutTodayScheduleList, layoutUpcomingTasksList, layoutUpcomingExamsList;

    //Khai báo các biến xử lý dữ liệu
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        //Khởi tạo đối tượng SQLite và quản lý phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        //Tham chiếu Id đến các biến giao diện
        tvHomeUserName = view.findViewById(R.id.tvHomeUserName);
        tvHomeDate = view.findViewById(R.id.tvHomeDate);
        tvNotificationBadge = view.findViewById(R.id.tvNotificationBadge);
        FrameLayout btnHomeNotification = view.findViewById(R.id.btnHomeNotification);

        tvStatCoursesCount = view.findViewById(R.id.tvStatCoursesCount);
        tvStatLessonsCount = view.findViewById(R.id.tvStatLessonsCount);
        tvStatTasksCount = view.findViewById(R.id.tvStatTasksCount);
        tvTodayScheduleTitle = view.findViewById(R.id.tvTodayScheduleTitle);

        LinearLayout cardStatCourses = view.findViewById(R.id.cardStatCourses);
        LinearLayout cardStatLessons = view.findViewById(R.id.cardStatLessons);
        LinearLayout cardStatTasks = view.findViewById(R.id.cardStatTasks);

        layoutTodayScheduleList = view.findViewById(R.id.layoutTodayScheduleList);
        layoutUpcomingTasksList = view.findViewById(R.id.layoutUpcomingTasksList);
        layoutUpcomingExamsList = view.findViewById(R.id.layoutUpcomingExamsList);

        //Xử lý sự kiện chuyển nhanh sang các màn hình khi nhấn vào các thẻ thống kê
        cardStatCourses.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_SUBJECTS);
            }
        });
        cardStatLessons.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_SCHEDULE);
            }
        });
        cardStatTasks.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_TASKS);
            }
        });
        btnHomeNotification.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).openRemindersTab();
            }
        });

        //Thiết lập thanh điều hướng dưới cùng
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setupBottomNavigation(view, HomeActivity.TAB_HOME);
        }

        //Tổng hợp và hiển thị dữ liệu từ các bảng SQLite lên Trang chủ
        loadDashboardData();
        return view;
    }

    //Hàm xác định thứ hiện tại trong tuần bằng tiếng Việt
    private String getCurrentVietnameseDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        switch (day) {
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
    private void loadDashboardData() {
        int userId = sessionManager.getUserId();

        //Hiển thị họ tên sinh viên và ngày hiện tại
        NguoiDung user = dbHelper.layNguoiDungTheoId(userId);
        if (user != null) {
            tvHomeUserName.setText(user.getHoTen());
        } else {
            tvHomeUserName.setText(sessionManager.getFullName());
        }

        String todayThu = getCurrentVietnameseDayOfWeek();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvHomeDate.setText(todayThu + ", " + sdf.format(Calendar.getInstance().getTime()));

        //Đếm số lượng môn học, tiết học hôm nay, bài tập chưa xong và nhắc nhở đang bật
        int totalCourses = dbHelper.demSoMonHoc(userId);
        int todayLessons = dbHelper.demSoLichHocTheoThu(userId, todayThu);
        int pendingTasks = dbHelper.demSoBaiTapChuaXong(userId);
        int activeReminders = dbHelper.demSoNhacNhoDangBat(userId);

        //Hiển thị kết quả thống kê
        tvStatCoursesCount.setText(String.valueOf(totalCourses));
        tvStatLessonsCount.setText(String.valueOf(todayLessons));
        tvStatTasksCount.setText(String.valueOf(pendingTasks));

        tvNotificationBadge.setText(String.valueOf(activeReminders));
        tvNotificationBadge.setVisibility(activeReminders > 0 ? View.VISIBLE : View.GONE);

        //1. Hiển thị danh sách Lịch học hôm nay (hoặc lịch học trong tuần nếu hôm nay trống)
        layoutTodayScheduleList.removeAllViews();
        List<LichHoc> todaySchedules = dbHelper.layDanhSachLichHocTheoThu(userId, todayThu);
        if (todaySchedules.isEmpty()) {
            List<LichHoc> allSchedules = dbHelper.layTatCaLichHoc(userId);
            if (allSchedules.isEmpty()) {
                tvTodayScheduleTitle.setText("LỊCH HỌC HÔM NAY (" + todayThu.toUpperCase() + ")");
                addEmptyMessage(layoutTodayScheduleList, "Chưa có lịch học nào được đăng ký.");
            } else {
                tvTodayScheduleTitle.setText("LỊCH HỌC TRONG TUẦN");
                int limit = Math.min(allSchedules.size(), 3);
                for (int i = 0; i < limit; i++) {
                    addScheduleCard(layoutTodayScheduleList, allSchedules.get(i), true);
                }
            }
        } else {
            tvTodayScheduleTitle.setText("LỊCH HỌC HÔM NAY (" + todayThu.toUpperCase() + ")");
            for (LichHoc lh : todaySchedules) {
                addScheduleCard(layoutTodayScheduleList, lh, false);
            }
        }

        //2. Hiển thị danh sách Công việc sắp đến hạn (chưa hoàn thành)
        layoutUpcomingTasksList.removeAllViews();
        List<BaiTap> unfinishedTasks = dbHelper.layDanhSachBaiTap(userId, "Chưa hoàn thành");
        if (unfinishedTasks.isEmpty()) {
            addEmptyMessage(layoutUpcomingTasksList, "Tuyệt vời! Bạn đã hoàn thành hết bài tập.");
        } else {
            int limit = Math.min(unfinishedTasks.size(), 3);
            for (int i = 0; i < limit; i++) {
                addTaskItem(layoutUpcomingTasksList, unfinishedTasks.get(i));
            }
        }

        //3. Hiển thị danh sách Lịch thi sắp tới
        layoutUpcomingExamsList.removeAllViews();
        List<LichThi> exams = dbHelper.layDanhSachLichThi(userId);
        if (exams.isEmpty()) {
            addEmptyMessage(layoutUpcomingExamsList, "Chưa có lịch thi sắp tới.");
        } else {
            int limit = Math.min(exams.size(), 3);
            for (int i = 0; i < limit; i++) {
                addExamItem(layoutUpcomingExamsList, exams.get(i));
            }
        }
    }

    //Hàm tạo dòng thông báo khi danh sách trống
    private void addEmptyMessage(LinearLayout container, String message) {
        TextView tv = new TextView(requireContext());
        tv.setText(message);
        tv.setTextColor(Color.parseColor("#A0B2C6"));
        tv.setTextSize(14f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dpToPx(10);
        tv.setLayoutParams(params);
        container.addView(tv);
    }

    //Hàm tạo thẻ hiển thị một ca học trên Trang chủ
    private void addScheduleCard(LinearLayout container, LichHoc lh, boolean showThu) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_blue_card_item);
        int pad = dpToPx(14);
        card.setPadding(pad, pad, pad, pad);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dpToPx(12);
        card.setLayoutParams(params);

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(lh.getTenMonHoc());
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(17f);
        tvTitle.setTypeface(null, Typeface.BOLD);
        card.addView(tvTitle);

        TextView tvTime = new TextView(requireContext());
        String timePrefix = showThu ? (lh.getThu() + " | ") : "";
        tvTime.setText(timePrefix + lh.getGioBatDau() + " – " + lh.getGioKetThuc());
        tvTime.setTextColor(Color.parseColor("#D0E2F2"));
        tvTime.setTextSize(14f);
        tvTime.setPadding(0, dpToPx(4), 0, 0);
        card.addView(tvTime);

        TextView tvRoom = new TextView(requireContext());
        tvRoom.setText("Phòng: " + (lh.getPhongHoc() != null ? lh.getPhongHoc() : ""));
        tvRoom.setTextColor(Color.parseColor("#D0E2F2"));
        tvRoom.setTextSize(14f);
        tvRoom.setPadding(0, dpToPx(2), 0, 0);
        card.addView(tvRoom);

        TextView tvGv = new TextView(requireContext());
        tvGv.setText("GV: " + (lh.getGiangVien() != null ? lh.getGiangVien() : ""));
        tvGv.setTextColor(Color.parseColor("#D0E2F2"));
        tvGv.setTextSize(14f);
        tvGv.setPadding(0, dpToPx(2), 0, 0);
        card.addView(tvGv);

        card.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_SCHEDULE);
            }
        });
        container.addView(card);
    }

    //Hàm tạo mục hiển thị một bài tập sắp đến hạn trên Trang chủ
    private void addTaskItem(LinearLayout container, BaiTap bt) {
        RelativeLayout row = new RelativeLayout(requireContext());
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.topMargin = dpToPx(12);
        row.setLayoutParams(rowParams);

        View bar = new View(requireContext());
        bar.setId(View.generateViewId());
        RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(dpToPx(4), dpToPx(64));
        barParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        bar.setLayoutParams(barParams);
        bar.setBackgroundColor(Color.parseColor("#D4A373"));
        row.addView(bar);

        TextView statusChip = new TextView(requireContext());
        statusChip.setId(View.generateViewId());
        RelativeLayout.LayoutParams chipParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chipParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        chipParams.addRule(RelativeLayout.CENTER_VERTICAL);
        statusChip.setLayoutParams(chipParams);
        statusChip.setBackgroundResource(R.drawable.bg_chip_unfinished);
        statusChip.setPadding(dpToPx(10), dpToPx(6), dpToPx(10), dpToPx(6));
        statusChip.setText(bt.getTrangThai());
        statusChip.setTextColor(Color.parseColor("#D0DCE8"));
        statusChip.setTextSize(12f);
        row.addView(statusChip);

        LinearLayout info = new LinearLayout(requireContext());
        info.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        infoParams.addRule(RelativeLayout.END_OF, bar.getId());
        infoParams.addRule(RelativeLayout.START_OF, statusChip.getId());
        infoParams.setMarginStart(dpToPx(12));
        infoParams.setMarginEnd(dpToPx(8));
        info.setLayoutParams(infoParams);

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(bt.getTenBaiTap());
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(16f);
        tvTitle.setTypeface(null, Typeface.BOLD);
        info.addView(tvTitle);

        TextView tvCourse = new TextView(requireContext());
        tvCourse.setText("Môn: " + (bt.getTenMonHoc() != null ? bt.getTenMonHoc() : ""));
        tvCourse.setTextColor(Color.parseColor("#A0B2C6"));
        tvCourse.setTextSize(14f);
        tvCourse.setPadding(0, dpToPx(2), 0, 0);
        info.addView(tvCourse);

        TextView tvDeadline = new TextView(requireContext());
        tvDeadline.setText("Hạn: " + (bt.getHanNop() != null ? bt.getHanNop() : ""));
        tvDeadline.setTextColor(Color.parseColor("#A0B2C6"));
        tvDeadline.setTextSize(14f);
        tvDeadline.setPadding(0, dpToPx(2), 0, 0);
        info.addView(tvDeadline);

        row.addView(info);
        row.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_TASKS);
            }
        });
        container.addView(row);
    }

    //Hàm tạo mục hiển thị một lịch thi sắp tới trên Trang chủ
    private void addExamItem(LinearLayout container, LichThi lt) {
        LinearLayout info = new LinearLayout(requireContext());
        info.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dpToPx(12);
        info.setLayoutParams(params);

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(lt.getTenMonHoc() != null ? lt.getTenMonHoc() : "Môn thi");
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(16f);
        tvTitle.setTypeface(null, Typeface.BOLD);
        info.addView(tvTitle);

        TextView tvDateTime = new TextView(requireContext());
        tvDateTime.setText("Ngày: " + lt.getNgayThi() + "   Giờ: " + lt.getGioThi());
        tvDateTime.setTextColor(Color.parseColor("#A0B2C6"));
        tvDateTime.setTextSize(14f);
        tvDateTime.setPadding(0, dpToPx(3), 0, 0);
        info.addView(tvDateTime);

        TextView tvRoom = new TextView(requireContext());
        tvRoom.setText("Phòng: " + (lt.getPhongThi() != null ? lt.getPhongThi() : "")
                + " | Hình thức: " + (lt.getHinhThucThi() != null ? lt.getHinhThucThi() : ""));
        tvRoom.setTextColor(Color.parseColor("#A0B2C6"));
        tvRoom.setTextSize(14f);
        tvRoom.setPadding(0, dpToPx(2), 0, 0);
        info.addView(tvRoom);

        info.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_EXAMS);
            }
        });
        container.addView(info);
    }

    //Hàm quy đổi đơn vị dp sang pixel
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
