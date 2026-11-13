package com.example.quan_ly_lich_hoc_sv.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.quan_ly_lich_hoc_sv.adapters.MonHocAdapter;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SubjectManagementFragment extends Fragment {

    //Khai báo các biến giao diện
    private RecyclerView rvSubjects;
    private TextView tvEmptySubjects;
    private EditText etSearchSubject;
    private ImageButton fabAddSubject;

    //Khai báo các biến xử lý dữ liệu và Adapter
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private MonHocAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_subject_management, container, false);

        //Khởi tạo đối tượng SQLite và quản lý phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        //Tham chiếu Id đến các biến giao diện
        rvSubjects = view.findViewById(R.id.rvSubjects);
        tvEmptySubjects = view.findViewById(R.id.tvEmptySubjects);
        etSearchSubject = view.findViewById(R.id.etSearchSubject);
        fabAddSubject = view.findViewById(R.id.fabAddSubject);

        //Thiết lập RecyclerView và Adapter hiển thị danh sách môn học
        rvSubjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MonHocAdapter(new ArrayList<>(), new MonHocAdapter.OnMonHocActionListener() {
            @Override
            public void onEdit(MonHoc monHoc) {
                //Mở hộp thoại sửa thông tin môn học
                showMonHocDialog(monHoc);
            }

            @Override
            public void onDelete(MonHoc monHoc) {
                //Mở hộp thoại xác nhận xóa môn học
                confirmDeleteMonHoc(monHoc);
            }
        });
        rvSubjects.setAdapter(adapter);

        //Xử lý sự kiện khi nhấn nút (+) thêm môn học mới
        fabAddSubject.setOnClickListener(v -> showMonHocDialog(null));

        //Xử lý tìm kiếm môn học theo từ khóa khi người dùng gõ vào ô tìm kiếm
        etSearchSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Cập nhật lại danh sách môn học theo từ khóa tìm kiếm
                loadSubjects(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Kết nối thanh điều hướng dưới cùng (Bottom Navigation)
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setupBottomNavigation(view, HomeActivity.TAB_SUBJECTS);
        }

        //Tải danh sách môn học từ SQLite lên giao diện
        loadSubjects("");
        return view;
    }

    //Hàm đọc danh sách môn học từ SQLite và hiển thị lên RecyclerView
    private void loadSubjects(String keyword) {
        int userId = sessionManager.getUserId();
        List<MonHoc> list;
        if (keyword == null || keyword.isEmpty()) {
            //Lấy toàn bộ danh sách môn học của sinh viên
            list = dbHelper.layDanhSachMonHoc(userId);
        } else {
            //Tìm kiếm môn học theo tên, mã môn hoặc giảng viên
            list = dbHelper.timKiemMonHoc(userId, keyword);
        }
        //Hiển thị kết quả lên danh sách
        adapter.updateData(list);
        tvEmptySubjects.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Hàm hiển thị hộp thoại Thêm mới hoặc Cập nhật môn học
    private void showMonHocDialog(@Nullable MonHoc existingMonHoc) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_subject, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etSubjectCode = dialogView.findViewById(R.id.etSubjectCode);
        EditText etSubjectName = dialogView.findViewById(R.id.etSubjectName);
        EditText etCredits = dialogView.findViewById(R.id.etCredits);
        EditText etTeacher = dialogView.findViewById(R.id.etTeacher);
        Spinner spSemester = dialogView.findViewById(R.id.spSemester);
        EditText etAcademicYear = dialogView.findViewById(R.id.etAcademicYear);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        AppCompatButton btnSave = dialogView.findViewById(R.id.btnSave);

        //Đổ dữ liệu danh sách học kỳ vào Spinner
        String[] semesters = {"Học kỳ 1", "Học kỳ 2", "Học kỳ hè"};
        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, semesters);
        spSemester.setAdapter(semesterAdapter);

        //Nếu là chế độ Sửa thì điền sẵn thông tin môn học hiện tại vào các ô nhập
        if (existingMonHoc != null) {
            tvDialogTitle.setText("Cập nhật môn học");
            etSubjectCode.setText(existingMonHoc.getMaMonHoc());
            etSubjectName.setText(existingMonHoc.getTenMonHoc());
            etCredits.setText(String.valueOf(existingMonHoc.getSoTinChi()));
            etTeacher.setText(existingMonHoc.getGiangVien());
            etAcademicYear.setText(existingMonHoc.getNamHoc());
            for (int i = 0; i < semesters.length; i++) {
                if (semesters[i].equalsIgnoreCase(existingMonHoc.getHocKy())) {
                    spSemester.setSelection(i);
                    break;
                }
            }
        } else {
            //Chế độ Thêm môn học mới
            tvDialogTitle.setText(R.string.dialog_title_add_subject);
            etAcademicYear.setText("2026 - 2027");
        }

        //Xử lý khi nhấn nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        //Xử lý khi nhấn nút Lưu môn học
        btnSave.setOnClickListener(v -> {
            //Lấy dữ liệu từ các ô nhập liệu
            String maMon = etSubjectCode.getText().toString().trim();
            String tenMon = etSubjectName.getText().toString().trim();
            String tinChiStr = etCredits.getText().toString().trim();
            String giangVien = etTeacher.getText().toString().trim();
            String hocKy = spSemester.getSelectedItem().toString();
            String namHoc = etAcademicYear.getText().toString().trim();

            //Kiểm tra dữ liệu bắt buộc
            if (maMon.isEmpty() || tenMon.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập mã môn và tên môn học", Toast.LENGTH_SHORT).show();
                return;
            }

            //Chuyển đổi số tín chỉ sang kiểu số nguyên
            int soTinChi = 3;
            if (!tinChiStr.isEmpty()) {
                try {
                    soTinChi = Integer.parseInt(tinChiStr);
                } catch (NumberFormatException e) {
                    soTinChi = 3;
                }
            }

            //Thực hiện Thêm mới (Create) hoặc Cập nhật (Update) vào SQLite
            if (existingMonHoc == null) {
                MonHoc newMonHoc = new MonHoc(sessionManager.getUserId(), maMon, tenMon, soTinChi, giangVien, hocKy, namHoc);
                long id = dbHelper.themMonHoc(newMonHoc);
                if (id != -1) {
                    Toast.makeText(requireContext(), "Đã thêm môn học: " + tenMon, Toast.LENGTH_SHORT).show();
                    loadSubjects(etSearchSubject.getText().toString().trim());
                    dialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Lỗi khi thêm môn học", Toast.LENGTH_SHORT).show();
                }
            } else {
                existingMonHoc.setMaMonHoc(maMon);
                existingMonHoc.setTenMonHoc(tenMon);
                existingMonHoc.setSoTinChi(soTinChi);
                existingMonHoc.setGiangVien(giangVien);
                existingMonHoc.setHocKy(hocKy);
                existingMonHoc.setNamHoc(namHoc);
                int rows = dbHelper.capNhatMonHoc(existingMonHoc);
                if (rows > 0) {
                    Toast.makeText(requireContext(), "Đã cập nhật môn học!", Toast.LENGTH_SHORT).show();
                    loadSubjects(etSearchSubject.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    //Hàm xác nhận và xóa môn học khỏi SQLite (Delete)
    private void confirmDeleteMonHoc(MonHoc monHoc) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa môn học")
                .setMessage("Bạn có chắc chắn muốn xóa môn \"" + monHoc.getTenMonHoc() + "\"?\nLưu ý: Lịch học, bài tập và lịch thi thuộc môn này cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", (d, w) -> {
                    //Xóa môn học và các dữ liệu liên quan trong SQLite
                    dbHelper.xoaMonHoc(monHoc.getId());
                    Toast.makeText(requireContext(), "Đã xóa môn học", Toast.LENGTH_SHORT).show();
                    loadSubjects(etSearchSubject.getText().toString().trim());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
