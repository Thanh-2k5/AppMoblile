package com.example.quan_ly_lich_hoc_sv.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.quan_ly_lich_hoc_sv.HomeActivity;
import com.example.quan_ly_lich_hoc_sv.MainActivity;
import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.NguoiDung;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileSettingsFragment extends Fragment {

    //Khai báo các biến giao diện
    private TextView tvUserName, tvStudentId, tvUserEmail;

    //Khai báo các biến xử lý dữ liệu
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_settings, container, false);

        //Khởi tạo đối tượng SQLite và quản lý phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        //Tham chiếu Id đến các biến giao diện
        tvUserName = view.findViewById(R.id.tvUserName);
        tvStudentId = view.findViewById(R.id.tvStudentId);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        RelativeLayout btnPersonalInfo = view.findViewById(R.id.btnPersonalInfo);
        RelativeLayout btnEditInfo = view.findViewById(R.id.btnEditInfo);
        RelativeLayout btnBackupData = view.findViewById(R.id.btnBackupData);
        RelativeLayout btnRestoreData = view.findViewById(R.id.btnRestoreData);
        RelativeLayout btnLogout = view.findViewById(R.id.btnLogout);

        //Xử lý sự kiện cho từng mục chức năng trên màn hình Cá nhân & Cài đặt
        btnPersonalInfo.setOnClickListener(v -> showPersonalInfoDialog());
        btnEditInfo.setOnClickListener(v -> showEditProfileDialog());
        btnBackupData.setOnClickListener(v -> backupDatabase());
        btnRestoreData.setOnClickListener(v -> restoreDatabase());
        btnLogout.setOnClickListener(v -> confirmLogout());

        //Thiết lập thanh điều hướng dưới cùng
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setupBottomNavigation(view, HomeActivity.TAB_PROFILE);
        }

        //Hiển thị thông tin sinh viên lên đầu trang
        loadUserProfile();
        return view;
    }

    //Hàm đọc thông tin tài khoản sinh viên từ SQLite và hiển thị lên giao diện
    private void loadUserProfile() {
        int userId = sessionManager.getUserId();
        NguoiDung user = dbHelper.layNguoiDungTheoId(userId);
        if (user != null) {
            tvUserName.setText(user.getHoTen());
            tvStudentId.setText("MSV: " + (user.getMaSinhVien() != null ? user.getMaSinhVien() : ""));
            tvUserEmail.setText("Email: " + (user.getEmail() != null ? user.getEmail() : ""));
        } else {
            tvUserName.setText(sessionManager.getFullName());
            tvStudentId.setText("MSV: " + sessionManager.getStudentId());
            tvUserEmail.setText("Email: " + sessionManager.getEmail());
        }
    }

    //Hàm hiển thị hộp thoại chi tiết thông tin cá nhân và thống kê học tập
    private void showPersonalInfoDialog() {
        int userId = sessionManager.getUserId();
        NguoiDung user = dbHelper.layNguoiDungTheoId(userId);
        int subjectCount = dbHelper.demSoMonHoc(userId);
        int pendingTasks = dbHelper.demSoBaiTapChuaXong(userId);

        String info = "Họ và tên: " + (user != null ? user.getHoTen() : sessionManager.getFullName())
                + "\nMã sinh viên: " + (user != null ? user.getMaSinhVien() : sessionManager.getStudentId())
                + "\nEmail: " + (user != null ? user.getEmail() : sessionManager.getEmail())
                + "\nTên đăng nhập: " + (user != null ? user.getTenDangNhap() : sessionManager.getUsername())
                + "\n\nTổng số môn học: " + subjectCount
                + "\nBài tập chưa hoàn thành: " + pendingTasks;

        new AlertDialog.Builder(requireContext())
                .setTitle("Thông tin cá nhân")
                .setMessage(info)
                .setPositiveButton("Đóng", null)
                .show();
    }

    //Hàm hiển thị hộp thoại chỉnh sửa thông tin cá nhân của sinh viên
    private void showEditProfileDialog() {
        int userId = sessionManager.getUserId();
        NguoiDung user = dbHelper.layNguoiDungTheoId(userId);
        if (user == null) return;

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //Tham chiếu Id đến các biến giao diện trong hộp thoại
        EditText etEditFullName = dialogView.findViewById(R.id.etEditFullName);
        EditText etEditStudentId = dialogView.findViewById(R.id.etEditStudentId);
        EditText etEditEmail = dialogView.findViewById(R.id.etEditEmail);
        EditText etEditPassword = dialogView.findViewById(R.id.etEditPassword);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        AppCompatButton btnSave = dialogView.findViewById(R.id.btnSave);

        //Hiển thị thông tin hiện tại vào các ô nhập
        etEditFullName.setText(user.getHoTen());
        etEditStudentId.setText(user.getMaSinhVien());
        etEditEmail.setText(user.getEmail());
        etEditPassword.setText(user.getMatKhau());

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        //Xử lý lưu thông tin cập nhật vào bảng NguoiDung trong SQLite
        btnSave.setOnClickListener(v -> {
            String fullName = etEditFullName.getText().toString().trim();
            String studentId = etEditStudentId.getText().toString().trim();
            String email = etEditEmail.getText().toString().trim();
            String password = etEditPassword.getText().toString().trim();

            if (fullName.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Họ tên và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setHoTen(fullName);
            user.setMaSinhVien(studentId);
            user.setEmail(email);
            user.setMatKhau(password);

            int rows = dbHelper.capNhatNguoiDung(user);
            if (rows > 0) {
                sessionManager.saveLoginSession(user, sessionManager.isRemembered());
                loadUserProfile();
                Toast.makeText(requireContext(), "Đã cập nhật thông tin cá nhân!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Hàm sao lưu cơ sở dữ liệu SQLite ra file dự phòng
    private void backupDatabase() {
        try {
            File dbFile = requireContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
            File backupFile = new File(requireContext().getFilesDir(), DatabaseHelper.DATABASE_NAME + ".bak");
            copyFile(dbFile, backupFile);
            Toast.makeText(requireContext(), "Đã sao lưu dữ liệu SQLite thành công!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Lỗi sao lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Hàm khôi phục cơ sở dữ liệu SQLite từ bản sao lưu
    private void restoreDatabase() {
        File backupFile = new File(requireContext().getFilesDir(), DatabaseHelper.DATABASE_NAME + ".bak");
        if (!backupFile.exists()) {
            Toast.makeText(requireContext(), "Chưa có bản sao lưu nào. Hãy sao lưu trước!", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle("Khôi phục dữ liệu")
                .setMessage("Bạn có chắc chắn muốn khôi phục dữ liệu từ bản sao lưu gần nhất?")
                .setPositiveButton("Khôi phục", (d, w) -> {
                    try {
                        dbHelper.close();
                        File dbFile = requireContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
                        copyFile(backupFile, dbFile);
                        loadUserProfile();
                        Toast.makeText(requireContext(), "Đã khôi phục dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Lỗi khôi phục: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    //Hàm sao chép dữ liệu giữa 2 file
    private void copyFile(File src, File dst) throws IOException {
        try (FileInputStream in = new FileInputStream(src);
             FileOutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    //Hàm xác nhận đăng xuất và quay về màn hình Đăng nhập
    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    sessionManager.logout();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
