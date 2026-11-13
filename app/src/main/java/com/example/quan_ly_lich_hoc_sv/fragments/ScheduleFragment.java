package com.example.quan_ly_lich_hoc_sv.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_lich_hoc_sv.HomeActivity;
import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.adapters.LichHocAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    //Khai báo các biến giao diện
    private RecyclerView rvSchedules;
    private TextView tvEmptySchedules;
    private TextView tvSelectedDayTitle;
    private TextView tvWeekRange;
    private TextView[] dayTextViews;

    //Khai báo các biến xử lý dữ liệu và trạng thái thứ/tuần
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private LichHocAdapter adapter;
    private final String[] DAYS_FULL = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    private int selectedDayIndex = 0;
    private int weekOffset = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule, container, false);

        //Khởi tạo đối tượng SQLite và quản lý phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        //Tham chiếu Id đến các biến giao diện
        rvSchedules = view.findViewById(R.id.rvSchedules);
        tvEmptySchedules = view.findViewById(R.id.tvEmptySchedules);
        tvSelectedDayTitle = view.findViewById(R.id.tvSelectedDayTitle);
        tvWeekRange = view.findViewById(R.id.tvWeekRange);
        ImageView btnPrevWeek = view.findViewById(R.id.btnPrevWeek);
        ImageView btnNextWeek = view.findViewById(R.id.btnNextWeek);
        ImageButton fabAddSchedule = view.findViewById(R.id.fabAddSchedule);

        //Tham chiếu Id đến các nhãn hiển thị 7 ngày trong tuần (T2 - CN)
        dayTextViews = new TextView[]{
                view.findViewById(R.id.tvDayT2),
                view.findViewById(R.id.tvDayT3),
                view.findViewById(R.id.tvDayT4),
                view.findViewById(R.id.tvDayT5),
                view.findViewById(R.id.tvDayT6),
                view.findViewById(R.id.tvDayT7),
                view.findViewById(R.id.tvDayCN)
        };

        //Tham chiếu Id đến các ô chọn thứ trong tuần
        LinearLayout[] dayButtons = new LinearLayout[]{
                view.findViewById(R.id.btnDayT2),
                view.findViewById(R.id.btnDayT3),
                view.findViewById(R.id.btnDayT4),
                view.findViewById(R.id.btnDayT5),
                view.findViewById(R.id.btnDayT6),
                view.findViewById(R.id.btnDayT7),
                view.findViewById(R.id.btnDayCN)
        };

        //Xử lý sự kiện khi chọn từng thứ trong tuần
        for (int i = 0; i < dayButtons.length; i++) {
            final int index = i;
            dayButtons[i].setOnClickListener(v -> selectDay(index));
        }

        //Xử lý chuyển sang tuần trước và tuần sau
        btnPrevWeek.setOnClickListener(v -> {
            weekOffset--;
            updateWeekHeader();
        });
        btnNextWeek.setOnClickListener(v -> {
            weekOffset++;
            updateWeekHeader();
        });

        //Thiết lập RecyclerView và Adapter cho danh sách thời khóa biểu
        rvSchedules.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LichHocAdapter(new ArrayList<>(), new LichHocAdapter.OnLichHocActionListener() {
            @Override
            public void onEdit(LichHoc lichHoc) {
                //Mở hộp thoại sửa lịch học
                showScheduleDialog(lichHoc);
            }

            @Override
            public void onDelete(LichHoc lichHoc) {
                //Mở hộp thoại xác nhận xóa lịch học
                confirmDeleteSchedule(lichHoc);
            }
        });
        rvSchedules.setAdapter(adapter);

        //Xử lý khi nhấn nút (+) thêm lịch học mới
        fabAddSchedule.setOnClickListener(v -> showScheduleDialog(null));

        //Thiết lập thanh điều hướng dưới cùng
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setupBottomNavigation(view, HomeActivity.TAB_SCHEDULE);
        }

        //Hiển thị thông tin tuần và lịch học của Thứ 2 mặc định
        updateWeekHeader();
        selectDay(0);
        return view;
    }

    //Hàm tính toán và hiển thị khoảng ngày của tuần đang chọn
    private void updateWeekHeader() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, weekOffset);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String start = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String end = sdf.format(cal.getTime());
        String prefix = (weekOffset == 0) ? "Tuần này" : (weekOffset > 0 ? "Tuần +" + weekOffset : "Tuần " + weekOffset);
        tvWeekRange.setText(prefix + " (" + start + " – " + end + ")");
    }

    //Hàm đổi màu ô thứ đang chọn và tải lịch học tương ứng
    private void selectDay(int index) {
        selectedDayIndex = index;
        for (int i = 0; i < dayTextViews.length; i++) {
            if (i == index) {
                dayTextViews[i].setBackgroundResource(R.drawable.bg_day_active);
                dayTextViews[i].setTextColor(Color.WHITE);
                dayTextViews[i].setTypeface(null, Typeface.BOLD);
            } else {
                dayTextViews[i].setBackgroundResource(R.drawable.bg_day_inactive);
                dayTextViews[i].setTextColor(Color.parseColor("#0F2537"));
                dayTextViews[i].setTypeface(null, Typeface.NORMAL);
            }
        }
        //Hiển thị tiêu đề thứ đang chọn và lọc dữ liệu
        tvSelectedDayTitle.setText(DAYS_FULL[index]);
        loadSchedulesForCurrentDay();
    }

    //Hàm truy vấn danh sách lịch học theo thứ từ SQLite
    private void loadSchedulesForCurrentDay() {
        int userId = sessionManager.getUserId();
        String thu = DAYS_FULL[selectedDayIndex];
        List<LichHoc> list = dbHelper.layDanhSachLichHocTheoThu(userId, thu);
        //Hiển thị kết quả lên danh sách
        adapter.updateData(list);
        tvEmptySchedules.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Hàm hiển thị hộp thoại Thêm mới hoặc Cập nhật lịch học
    private void showScheduleDialog(@Nullable LichHoc existingLichHoc) {
        //Lấy danh sách môn học từ SQLite để đưa vào Spinner chọn môn
        List<MonHoc> subjects = dbHelper.layDanhSachMonHoc(sessionManager.getUserId());
        if (subjects.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng thêm môn học trước khi tạo thời khóa biểu!", Toast.LENGTH_LONG).show();
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_SUBJECTS);
            }
            return;
        }

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_schedule, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        Spinner spSubject = dialogView.findViewById(R.id.spSubject);
        Spinner spDayOfWeek = dialogView.findViewById(R.id.spDayOfWeek);
        Spinner spStartTime = dialogView.findViewById(R.id.spStartTime);
        Spinner spEndTime = dialogView.findViewById(R.id.spEndTime);
        EditText etRoom = dialogView.findViewById(R.id.etRoom);
        EditText etTeacher = dialogView.findViewById(R.id.etTeacher);
        Spinner spStartDate = dialogView.findViewById(R.id.spStartDate);
        Spinner spEndDate = dialogView.findViewById(R.id.spEndDate);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        AppCompatButton btnSave = dialogView.findViewById(R.id.btnSave);

        //Gán danh sách môn học và thứ trong tuần vào Spinner
        ArrayAdapter<MonHoc> subjectAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, subjects);
        spSubject.setAdapter(subjectAdapter);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, DAYS_FULL);
        spDayOfWeek.setAdapter(dayAdapter);
        spDayOfWeek.setSelection(selectedDayIndex);

        //Khởi tạo danh sách khung giờ và ngày bắt đầu/kết thúc
        String[] startTimes = {"07:00", "07:30", "08:00", "09:00", "09:45", "12:30", "13:00", "13:30", "15:00", "15:30", "17:30", "18:00"};
        String[] endTimes = {"09:00", "09:30", "10:00", "11:00", "11:45", "14:30", "15:00", "15:30", "17:00", "17:30", "19:30", "20:00"};
        String[] startDates = {"01/09/2026", "15/09/2026", "01/10/2026", "01/01/2027"};
        String[] endDates = {"30/12/2026", "15/01/2027", "30/05/2027"};

        spStartTime.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, startTimes));
        spEndTime.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, endTimes));
        spStartDate.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, startDates));
        spEndDate.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, endDates));

        //Tự động điền tên giảng viên tương ứng khi chọn môn học
        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MonHoc selected = subjects.get(position);
                if (existingLichHoc == null && selected.getGiangVien() != null) {
                    etTeacher.setText(selected.getGiangVien());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Nếu là cập nhật lịch học thì hiển thị lại thông tin cũ lên hộp thoại
        if (existingLichHoc != null) {
            tvDialogTitle.setText("Cập nhật lịch học");
            etRoom.setText(existingLichHoc.getPhongHoc());
            etTeacher.setText(existingLichHoc.getGiangVien());

            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId() == existingLichHoc.getMonHocId()) {
                    spSubject.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < DAYS_FULL.length; i++) {
                if (DAYS_FULL[i].equalsIgnoreCase(existingLichHoc.getThu())) {
                    spDayOfWeek.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < startTimes.length; i++) {
                if (startTimes[i].equalsIgnoreCase(existingLichHoc.getGioBatDau())) {
                    spStartTime.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < endTimes.length; i++) {
                if (endTimes[i].equalsIgnoreCase(existingLichHoc.getGioKetThuc())) {
                    spEndTime.setSelection(i);
                    break;
                }
            }
        } else {
            tvDialogTitle.setText(R.string.dialog_title_add_schedule);
        }

        //Xử lý khi nhấn nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        //Xử lý khi nhấn nút Lưu lịch học vào SQLite
        btnSave.setOnClickListener(v -> {
            //Lấy dữ liệu từ các ô chọn và ô nhập
            MonHoc selectedSubject = (MonHoc) spSubject.getSelectedItem();
            String thu = spDayOfWeek.getSelectedItem().toString();
            String gioBD = spStartTime.getSelectedItem().toString();
            String gioKT = spEndTime.getSelectedItem().toString();
            String phong = etRoom.getText().toString().trim();
            String giangVien = etTeacher.getText().toString().trim();
            String ngayBD = spStartDate.getSelectedItem().toString();
            String ngayKT = spEndDate.getSelectedItem().toString();

            if (phong.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập phòng học", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingLichHoc == null) {
                //Thêm lịch học mới vào SQLite
                LichHoc lh = new LichHoc(selectedSubject.getId(), thu, gioBD, gioKT, phong, giangVien, ngayBD, ngayKT);
                long id = dbHelper.themLichHoc(lh);
                if (id != -1) {
                    Toast.makeText(requireContext(), "Đã thêm lịch học môn " + selectedSubject.getTenMonHoc(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < DAYS_FULL.length; i++) {
                        if (DAYS_FULL[i].equalsIgnoreCase(thu)) {
                            selectDay(i);
                            break;
                        }
                    }
                    dialog.dismiss();
                }
            } else {
                //Cập nhật lịch học hiện có trong SQLite
                existingLichHoc.setMonHocId(selectedSubject.getId());
                existingLichHoc.setThu(thu);
                existingLichHoc.setGioBatDau(gioBD);
                existingLichHoc.setGioKetThuc(gioKT);
                existingLichHoc.setPhongHoc(phong);
                existingLichHoc.setGiangVien(giangVien);
                existingLichHoc.setNgayBatDau(ngayBD);
                existingLichHoc.setNgayKetThuc(ngayKT);
                dbHelper.capNhatLichHoc(existingLichHoc);
                Toast.makeText(requireContext(), "Đã cập nhật lịch học!", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < DAYS_FULL.length; i++) {
                    if (DAYS_FULL[i].equalsIgnoreCase(thu)) {
                        selectDay(i);
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Hàm xác nhận và xóa lịch học khỏi SQLite
    private void confirmDeleteSchedule(LichHoc lichHoc) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa lịch học")
                .setMessage("Bạn có chắc chắn muốn xóa lịch học môn \"" + lichHoc.getTenMonHoc() + "\" (" + lichHoc.getGioBatDau() + " - " + lichHoc.getGioKetThuc() + ")?")
                .setPositiveButton("Xóa", (d, w) -> {
                    dbHelper.xoaLichHoc(lichHoc.getId());
                    Toast.makeText(requireContext(), "Đã xóa lịch học", Toast.LENGTH_SHORT).show();
                    loadSchedulesForCurrentDay();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
