package com.example.quan_ly_lich_hoc_sv.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.quan_ly_lich_hoc_sv.adapters.LichThiAdapter;
import com.example.quan_ly_lich_hoc_sv.adapters.NhacNhoAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.LichThi;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.example.quan_ly_lich_hoc_sv.models.NhacNho;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExamScheduleFragment extends Fragment {

    //Khai báo các biến giao diện
    private RecyclerView rvExamsAndReminders;
    private TextView tvEmptyExams;
    private TextView tvTabExamMode, tvTabReminderMode;
    private View indTabExamMode, indTabReminderMode;

    //Khai báo các biến xử lý dữ liệu và Adapter cho Lịch thi & Nhắc nhở
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private LichThiAdapter lichThiAdapter;
    private NhacNhoAdapter nhacNhoAdapter;
    private boolean isReminderTab = false;

    //Hàm đặt chế độ mở trực tiếp tab Nhắc nhở
    public void setStartOnReminderTab(boolean reminderTab) {
        this.isReminderTab = reminderTab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_exam_schedule, container, false);

        //Khởi tạo đối tượng SQLite và quản lý phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        //Tham chiếu Id đến các biến giao diện
        rvExamsAndReminders = view.findViewById(R.id.rvExamsAndReminders);
        tvEmptyExams = view.findViewById(R.id.tvEmptyExams);
        ImageButton fabAddExam = view.findViewById(R.id.fabAddExam);

        LinearLayout tabExamMode = view.findViewById(R.id.tabExamMode);
        LinearLayout tabReminderMode = view.findViewById(R.id.tabReminderMode);
        tvTabExamMode = view.findViewById(R.id.tvTabExamMode);
        tvTabReminderMode = view.findViewById(R.id.tvTabReminderMode);
        indTabExamMode = view.findViewById(R.id.indTabExamMode);
        indTabReminderMode = view.findViewById(R.id.indTabReminderMode);

        rvExamsAndReminders.setLayoutManager(new LinearLayoutManager(requireContext()));

        //Khởi tạo Adapter cho danh sách Lịch thi
        lichThiAdapter = new LichThiAdapter(new ArrayList<>(), new LichThiAdapter.OnLichThiActionListener() {
            @Override
            public void onEdit(LichThi lichThi) {
                //Mở hộp thoại sửa lịch thi
                showExamDialog(lichThi);
            }

            @Override
            public void onDelete(LichThi lichThi) {
                //Mở hộp thoại xác nhận xóa lịch thi
                confirmDeleteExam(lichThi);
            }
        });

        //Khởi tạo Adapter cho danh sách Nhắc nhở
        nhacNhoAdapter = new NhacNhoAdapter(new ArrayList<>(), new NhacNhoAdapter.OnNhacNhoActionListener() {
            @Override
            public void onEdit(NhacNho nhacNho) {
                //Mở hộp thoại sửa nhắc nhở
                showReminderDialog(nhacNho);
            }

            @Override
            public void onDelete(NhacNho nhacNho) {
                //Mở hộp thoại xác nhận xóa nhắc nhở
                confirmDeleteReminder(nhacNho);
            }

            @Override
            public void onToggleStatus(NhacNho nhacNho) {
                //Xử lý bật/tắt trạng thái nhắc nhở nhanh
                String current = nhacNho.getTrangThai();
                String next = "Đang bật".equalsIgnoreCase(current) ? "Đã tắt" : "Đang bật";
                dbHelper.capNhatTrangThaiNhacNho(nhacNho.getId(), next);
                Toast.makeText(requireContext(), "Trạng thái nhắc nhở: " + next, Toast.LENGTH_SHORT).show();
                loadCurrentTab();
            }
        });

        //Xử lý sự kiện chuyển đổi giữa 2 tab Lịch thi và Nhắc nhở
        tabExamMode.setOnClickListener(v -> switchMode(false));
        tabReminderMode.setOnClickListener(v -> switchMode(true));

        //Xử lý khi nhấn nút (+) thêm mới theo tab đang chọn
        fabAddExam.setOnClickListener(v -> {
            if (isReminderTab) {
                showReminderDialog(null);
            } else {
                showExamDialog(null);
            }
        });

        //Thiết lập thanh điều hướng dưới cùng
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setupBottomNavigation(view, HomeActivity.TAB_EXAMS);
        }

        //Hiển thị dữ liệu của tab hiện tại
        switchMode(isReminderTab);
        return view;
    }

    //Hàm đổi màu chỉ báo tab và tải dữ liệu tương ứng
    private void switchMode(boolean reminderMode) {
        isReminderTab = reminderMode;
        int activeColor = Color.parseColor("#134675");
        int inactiveColor = Color.parseColor("#708090");

        tvTabExamMode.setTextColor(!reminderMode ? activeColor : inactiveColor);
        tvTabExamMode.setTypeface(null, !reminderMode ? Typeface.BOLD : Typeface.NORMAL);
        indTabExamMode.setBackgroundColor(!reminderMode ? activeColor : Color.TRANSPARENT);

        tvTabReminderMode.setTextColor(reminderMode ? activeColor : inactiveColor);
        tvTabReminderMode.setTypeface(null, reminderMode ? Typeface.BOLD : Typeface.NORMAL);
        indTabReminderMode.setBackgroundColor(reminderMode ? activeColor : Color.TRANSPARENT);

        loadCurrentTab();
    }

    //Hàm tải danh sách Lịch thi hoặc Nhắc nhở từ SQLite
    private void loadCurrentTab() {
        int userId = sessionManager.getUserId();
        if (!isReminderTab) {
            //Hiển thị danh sách Lịch thi
            rvExamsAndReminders.setAdapter(lichThiAdapter);
            List<LichThi> list = dbHelper.layDanhSachLichThi(userId);
            lichThiAdapter.updateData(list);
            tvEmptyExams.setText("Chưa có lịch thi nào. Nhấn (+) để thêm lịch thi!");
            tvEmptyExams.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        } else {
            //Hiển thị danh sách Nhắc nhở
            rvExamsAndReminders.setAdapter(nhacNhoAdapter);
            List<NhacNho> list = dbHelper.layDanhSachNhacNho(userId);
            nhacNhoAdapter.updateData(list);
            tvEmptyExams.setText("Chưa có nhắc nhở nào. Nhấn (+) để thêm nhắc nhở!");
            tvEmptyExams.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    //Hàm hiển thị hộp thoại Thêm mới hoặc Cập nhật Lịch thi
    private void showExamDialog(@Nullable LichThi existingLichThi) {
        List<MonHoc> subjects = dbHelper.layDanhSachMonHoc(sessionManager.getUserId());
        if (subjects.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng thêm môn học trước khi thêm lịch thi!", Toast.LENGTH_LONG).show();
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_SUBJECTS);
            }
            return;
        }

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_exam, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại lịch thi
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        Spinner spSubject = dialogView.findViewById(R.id.spSubject);
        EditText etExamDate = dialogView.findViewById(R.id.etExamDate);
        EditText etExamTime = dialogView.findViewById(R.id.etExamTime);
        EditText etDuration = dialogView.findViewById(R.id.etDuration);
        EditText etExamRoom = dialogView.findViewById(R.id.etExamRoom);
        Spinner spExamFormat = dialogView.findViewById(R.id.spExamFormat);
        EditText etExamNotes = dialogView.findViewById(R.id.etExamNotes);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        AppCompatButton btnSave = dialogView.findViewById(R.id.btnSave);

        //Gán danh sách môn học và hình thức thi vào Spinner
        spSubject.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, subjects));

        String[] formats = {"Tự luận", "Trắc nghiệm", "Thực hành", "Vấn đáp", "Tiểu luận"};
        spExamFormat.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, formats));

        //Chọn ngày thi bằng DatePickerDialog
        etExamDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                etExamDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        //Chọn giờ thi bằng TimePickerDialog
        etExamTime.setOnClickListener(v -> {
            new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                etExamTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }, 8, 0, true).show();
        });

        //Nếu là cập nhật lịch thi thì điền sẵn dữ liệu cũ
        if (existingLichThi != null) {
            tvDialogTitle.setText("Cập nhật lịch thi");
            etExamDate.setText(existingLichThi.getNgayThi());
            etExamTime.setText(existingLichThi.getGioThi());
            etDuration.setText(existingLichThi.getThoiLuong());
            etExamRoom.setText(existingLichThi.getPhongThi());
            etExamNotes.setText(existingLichThi.getGhiChu());

            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId() == existingLichThi.getMonHocId()) {
                    spSubject.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < formats.length; i++) {
                if (formats[i].equalsIgnoreCase(existingLichThi.getHinhThucThi())) {
                    spExamFormat.setSelection(i);
                    break;
                }
            }
        } else {
            tvDialogTitle.setText(R.string.dialog_title_add_exam);
            etExamDate.setText("25/12/2026");
            etExamTime.setText("08:00");
            etDuration.setText("90 phút");
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        //Xử lý lưu thông tin lịch thi vào SQLite
        btnSave.setOnClickListener(v -> {
            MonHoc selectedSubject = (MonHoc) spSubject.getSelectedItem();
            String ngayThi = etExamDate.getText().toString().trim();
            String gioThi = etExamTime.getText().toString().trim();
            String thoiLuong = etDuration.getText().toString().trim();
            String phongThi = etExamRoom.getText().toString().trim();
            String hinhThuc = spExamFormat.getSelectedItem().toString();
            String ghiChu = etExamNotes.getText().toString().trim();

            if (ngayThi.isEmpty() || gioThi.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập ngày thi và giờ thi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingLichThi == null) {
                LichThi lt = new LichThi(selectedSubject.getId(), ngayThi, gioThi, phongThi, thoiLuong, hinhThuc, ghiChu);
                long id = dbHelper.themLichThi(lt);
                if (id != -1) {
                    Toast.makeText(requireContext(), "Đã thêm lịch thi!", Toast.LENGTH_SHORT).show();
                    loadCurrentTab();
                    dialog.dismiss();
                }
            } else {
                existingLichThi.setMonHocId(selectedSubject.getId());
                existingLichThi.setNgayThi(ngayThi);
                existingLichThi.setGioThi(gioThi);
                existingLichThi.setThoiLuong(thoiLuong);
                existingLichThi.setPhongThi(phongThi);
                existingLichThi.setHinhThucThi(hinhThuc);
                existingLichThi.setGhiChu(ghiChu);
                dbHelper.capNhatLichThi(existingLichThi);
                Toast.makeText(requireContext(), "Đã cập nhật lịch thi!", Toast.LENGTH_SHORT).show();
                loadCurrentTab();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Hàm xác nhận và xóa lịch thi khỏi SQLite
    private void confirmDeleteExam(LichThi lichThi) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa lịch thi")
                .setMessage("Bạn có chắc chắn muốn xóa lịch thi môn \"" + lichThi.getTenMonHoc() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    dbHelper.xoaLichThi(lichThi.getId());
                    Toast.makeText(requireContext(), "Đã xóa lịch thi", Toast.LENGTH_SHORT).show();
                    loadCurrentTab();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    //Hàm hiển thị hộp thoại Thêm mới hoặc Cập nhật Nhắc nhở
    private void showReminderDialog(@Nullable NhacNho existingNhacNho) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_reminder, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại nhắc nhở
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etReminderTitle = dialogView.findViewById(R.id.etReminderTitle);
        EditText etReminderContent = dialogView.findViewById(R.id.etReminderContent);
        EditText etReminderTime = dialogView.findViewById(R.id.etReminderTime);
        Spinner spReminderStatus = dialogView.findViewById(R.id.spReminderStatus);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        AppCompatButton btnSave = dialogView.findViewById(R.id.btnSave);

        String[] statuses = {"Đang bật", "Đã tắt"};
        spReminderStatus.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, statuses));

        //Chọn ngày giờ nhắc nhở
        etReminderTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                new TimePickerDialog(requireContext(), (timeView, hourOfDay, minute) -> {
                    etReminderTime.setText(String.format(Locale.getDefault(), "%02d:%02d - %02d/%02d/%04d",
                            hourOfDay, minute, dayOfMonth, month + 1, year));
                }, 19, 0, true).show();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        if (existingNhacNho != null) {
            tvDialogTitle.setText("Cập nhật nhắc nhở");
            etReminderTitle.setText(existingNhacNho.getTieuDe());
            etReminderContent.setText(existingNhacNho.getNoiDung());
            etReminderTime.setText(existingNhacNho.getThoiGianNhac());
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equalsIgnoreCase(existingNhacNho.getTrangThai())) {
                    spReminderStatus.setSelection(i);
                    break;
                }
            }
        } else {
            tvDialogTitle.setText("Thêm nhắc nhở mới");
            etReminderTime.setText("20:00 - 20/11/2026");
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        //Xử lý lưu nhắc nhở vào bảng NhacNho trong SQLite
        btnSave.setOnClickListener(v -> {
            String tieuDe = etReminderTitle.getText().toString().trim();
            String noiDung = etReminderContent.getText().toString().trim();
            String thoiGian = etReminderTime.getText().toString().trim();
            String trangThai = spReminderStatus.getSelectedItem().toString();

            if (tieuDe.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tiêu đề nhắc nhở", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingNhacNho == null) {
                NhacNho nn = new NhacNho(sessionManager.getUserId(), tieuDe, noiDung, thoiGian, trangThai);
                long id = dbHelper.themNhacNho(nn);
                if (id != -1) {
                    Toast.makeText(requireContext(), "Đã thêm nhắc nhở!", Toast.LENGTH_SHORT).show();
                    loadCurrentTab();
                    dialog.dismiss();
                }
            } else {
                existingNhacNho.setTieuDe(tieuDe);
                existingNhacNho.setNoiDung(noiDung);
                existingNhacNho.setThoiGianNhac(thoiGian);
                existingNhacNho.setTrangThai(trangThai);
                dbHelper.capNhatNhacNho(existingNhacNho);
                Toast.makeText(requireContext(), "Đã cập nhật nhắc nhở!", Toast.LENGTH_SHORT).show();
                loadCurrentTab();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Hàm xác nhận và xóa nhắc nhở khỏi SQLite
    private void confirmDeleteReminder(NhacNho nhacNho) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa nhắc nhở")
                .setMessage("Bạn có chắc chắn muốn xóa nhắc nhở \"" + nhacNho.getTieuDe() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    dbHelper.xoaNhacNho(nhacNho.getId());
                    Toast.makeText(requireContext(), "Đã xóa nhắc nhở", Toast.LENGTH_SHORT).show();
                    loadCurrentTab();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
