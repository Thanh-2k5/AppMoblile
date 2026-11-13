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
import com.example.quan_ly_lich_hoc_sv.adapters.BaiTapAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.BaiTap;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskManagementFragment extends Fragment {

    //Khai báo các biến giao diện
    private RecyclerView rvTasks;
    private TextView tvEmptyTasks;
    private TextView tvFilterAll, tvFilterUnfinished, tvFilterCompleted;
    private View indFilterAll, indFilterUnfinished, indFilterCompleted;

    //Khai báo các biến xử lý dữ liệu và bộ lọc trạng thái
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private BaiTapAdapter adapter;
    private String currentFilter = "Tất cả";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_management, container, false);

        //Khởi tạo đối tượng SQLite và quản lý phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        //Tham chiếu Id đến các biến giao diện
        rvTasks = view.findViewById(R.id.rvTasks);
        tvEmptyTasks = view.findViewById(R.id.tvEmptyTasks);
        ImageButton fabAddTask = view.findViewById(R.id.fabAddTask);

        LinearLayout tabFilterAll = view.findViewById(R.id.tabFilterAll);
        LinearLayout tabFilterUnfinished = view.findViewById(R.id.tabFilterUnfinished);
        LinearLayout tabFilterCompleted = view.findViewById(R.id.tabFilterCompleted);

        tvFilterAll = view.findViewById(R.id.tvFilterAll);
        tvFilterUnfinished = view.findViewById(R.id.tvFilterUnfinished);
        tvFilterCompleted = view.findViewById(R.id.tvFilterCompleted);

        indFilterAll = view.findViewById(R.id.indFilterAll);
        indFilterUnfinished = view.findViewById(R.id.indFilterUnfinished);
        indFilterCompleted = view.findViewById(R.id.indFilterCompleted);

        //Xử lý sự kiện lọc công việc theo 3 tab: Tất cả, Chưa hoàn thành, Đã hoàn thành
        tabFilterAll.setOnClickListener(v -> setFilter("Tất cả"));
        tabFilterUnfinished.setOnClickListener(v -> setFilter("Chưa hoàn thành"));
        tabFilterCompleted.setOnClickListener(v -> setFilter("Đã hoàn thành"));

        //Thiết lập RecyclerView và Adapter hiển thị danh sách bài tập/công việc
        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BaiTapAdapter(new ArrayList<>(), new BaiTapAdapter.OnBaiTapActionListener() {
            @Override
            public void onEdit(BaiTap baiTap) {
                //Mở hộp thoại sửa bài tập
                showTaskDialog(baiTap);
            }

            @Override
            public void onDelete(BaiTap baiTap) {
                //Mở hộp thoại xác nhận xóa bài tập
                confirmDeleteTask(baiTap);
            }

            @Override
            public void onToggleStatus(BaiTap baiTap) {
                //Đổi nhanh trạng thái bài tập khi nhấn vào nhãn trạng thái
                String current = baiTap.getTrangThai();
                String next = "Đã hoàn thành".equalsIgnoreCase(current) ? "Chưa hoàn thành" : "Đã hoàn thành";
                dbHelper.capNhatTrangThaiBaiTap(baiTap.getId(), next);
                Toast.makeText(requireContext(), "Đã chuyển trạng thái: " + next, Toast.LENGTH_SHORT).show();
                loadTasks();
            }
        });
        rvTasks.setAdapter(adapter);

        //Xử lý sự kiện khi nhấn nút (+) thêm công việc mới
        fabAddTask.setOnClickListener(v -> showTaskDialog(null));

        //Thiết lập thanh điều hướng dưới cùng
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setupBottomNavigation(view, HomeActivity.TAB_TASKS);
        }

        //Mặc định hiển thị tất cả công việc
        setFilter("Tất cả");
        return view;
    }

    //Hàm cập nhật giao diện tab lọc và tải lại danh sách công việc
    private void setFilter(String filter) {
        currentFilter = filter;
        int activeColor = Color.parseColor("#134675");
        int inactiveColor = Color.parseColor("#708090");

        tvFilterAll.setTextColor("Tất cả".equals(filter) ? activeColor : inactiveColor);
        tvFilterAll.setTypeface(null, "Tất cả".equals(filter) ? Typeface.BOLD : Typeface.NORMAL);
        indFilterAll.setBackgroundColor("Tất cả".equals(filter) ? activeColor : Color.TRANSPARENT);

        tvFilterUnfinished.setTextColor("Chưa hoàn thành".equals(filter) ? activeColor : inactiveColor);
        tvFilterUnfinished.setTypeface(null, "Chưa hoàn thành".equals(filter) ? Typeface.BOLD : Typeface.NORMAL);
        indFilterUnfinished.setBackgroundColor("Chưa hoàn thành".equals(filter) ? activeColor : Color.TRANSPARENT);

        tvFilterCompleted.setTextColor("Đã hoàn thành".equals(filter) ? activeColor : inactiveColor);
        tvFilterCompleted.setTypeface(null, "Đã hoàn thành".equals(filter) ? Typeface.BOLD : Typeface.NORMAL);
        indFilterCompleted.setBackgroundColor("Đã hoàn thành".equals(filter) ? activeColor : Color.TRANSPARENT);

        loadTasks();
    }

    //Hàm đọc danh sách bài tập từ SQLite theo bộ lọc
    private void loadTasks() {
        List<BaiTap> list = dbHelper.layDanhSachBaiTap(sessionManager.getUserId(), currentFilter);
        //Hiển thị kết quả lên RecyclerView
        adapter.updateData(list);
        tvEmptyTasks.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Hàm hiển thị hộp thoại Thêm mới hoặc Sửa công việc học tập
    private void showTaskDialog(@Nullable BaiTap existingBaiTap) {
        //Lấy danh sách môn học từ SQLite
        List<MonHoc> subjects = dbHelper.layDanhSachMonHoc(sessionManager.getUserId());
        if (subjects.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng thêm môn học trước khi thêm bài tập!", Toast.LENGTH_LONG).show();
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToTab(HomeActivity.TAB_SUBJECTS);
            }
            return;
        }

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etTaskName = dialogView.findViewById(R.id.etTaskName);
        Spinner spSubject = dialogView.findViewById(R.id.spSubject);
        EditText etTaskDescription = dialogView.findViewById(R.id.etTaskDescription);
        EditText etSubmissionDeadline = dialogView.findViewById(R.id.etSubmissionDeadline);
        Spinner spPriority = dialogView.findViewById(R.id.spPriority);
        Spinner spTaskStatus = dialogView.findViewById(R.id.spTaskStatus);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        AppCompatButton btnSave = dialogView.findViewById(R.id.btnSave);

        //Đổ dữ liệu vào các Spinner môn học, mức độ ưu tiên và trạng thái
        spSubject.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, subjects));

        String[] priorities = {"Cao", "Trung bình", "Thấp"};
        spPriority.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities));

        String[] statuses = {"Chưa hoàn thành", "Đã hoàn thành"};
        spTaskStatus.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, statuses));

        //Mở hộp thoại chọn ngày và giờ khi nhấn vào ô Hạn nộp
        etSubmissionDeadline.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                new TimePickerDialog(requireContext(), (timeView, hourOfDay, minute) -> {
                    String formatted = String.format(Locale.getDefault(), "%02d:%02d - %02d/%02d/%04d",
                            hourOfDay, minute, dayOfMonth, month + 1, year);
                    etSubmissionDeadline.setText(formatted);
                }, 23, 59, true).show();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        //Nếu đang ở chế độ Sửa thì điền thông tin cũ vào các ô nhập
        if (existingBaiTap != null) {
            tvDialogTitle.setText("Cập nhật công việc");
            etTaskName.setText(existingBaiTap.getTenBaiTap());
            etTaskDescription.setText(existingBaiTap.getNoiDung());
            etSubmissionDeadline.setText(existingBaiTap.getHanNop());

            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId() == existingBaiTap.getMonHocId()) {
                    spSubject.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < priorities.length; i++) {
                if (priorities[i].equalsIgnoreCase(existingBaiTap.getMucDoUuTien())) {
                    spPriority.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equalsIgnoreCase(existingBaiTap.getTrangThai())) {
                    spTaskStatus.setSelection(i);
                    break;
                }
            }
        } else {
            tvDialogTitle.setText(R.string.dialog_title_add_task);
            etSubmissionDeadline.setText("23:59 - 25/11/2026");
        }

        //Xử lý khi nhấn nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        //Xử lý khi nhấn nút Lưu công việc
        btnSave.setOnClickListener(v -> {
            //Lấy dữ liệu từ các ô nhập liệu
            String tenBaiTap = etTaskName.getText().toString().trim();
            MonHoc selectedMonHoc = (MonHoc) spSubject.getSelectedItem();
            String noiDung = etTaskDescription.getText().toString().trim();
            String hanNop = etSubmissionDeadline.getText().toString().trim();
            String uuTien = spPriority.getSelectedItem().toString();
            String trangThai = spTaskStatus.getSelectedItem().toString();

            if (tenBaiTap.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên công việc / bài tập", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingBaiTap == null) {
                //Thêm bài tập mới vào SQLite
                BaiTap bt = new BaiTap(selectedMonHoc.getId(), tenBaiTap, noiDung, hanNop, uuTien, trangThai);
                long id = dbHelper.themBaiTap(bt);
                if (id != -1) {
                    Toast.makeText(requireContext(), "Đã thêm công việc mới!", Toast.LENGTH_SHORT).show();
                    loadTasks();
                    dialog.dismiss();
                }
            } else {
                //Cập nhật bài tập hiện có trong SQLite
                existingBaiTap.setMonHocId(selectedMonHoc.getId());
                existingBaiTap.setTenBaiTap(tenBaiTap);
                existingBaiTap.setNoiDung(noiDung);
                existingBaiTap.setHanNop(hanNop);
                existingBaiTap.setMucDoUuTien(uuTien);
                existingBaiTap.setTrangThai(trangThai);
                dbHelper.capNhatBaiTap(existingBaiTap);
                Toast.makeText(requireContext(), "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                loadTasks();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Hàm xác nhận và xóa bài tập khỏi SQLite
    private void confirmDeleteTask(BaiTap baiTap) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa công việc")
                .setMessage("Bạn có chắc chắn muốn xóa công việc \"" + baiTap.getTenBaiTap() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    dbHelper.xoaBaiTap(baiTap.getId());
                    Toast.makeText(requireContext(), "Đã xóa công việc", Toast.LENGTH_SHORT).show();
                    loadTasks();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
