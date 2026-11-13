package com.example.quan_ly_lich_hoc_sv.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quan_ly_lich_hoc_sv.models.NguoiDung;

public class SessionManager {
    //Khai báo tên file SharedPreferences và các khóa lưu trữ thông tin phiên đăng nhập
    private static final String PREF_NAME = "StudentScheduleSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REMEMBER = "rememberLogin";

    //Khai báo biến SharedPreferences
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        //Khởi tạo đối tượng SharedPreferences
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //Lưu thông tin sinh viên sau khi đăng nhập thành công
    public void saveLoginSession(NguoiDung user, boolean remember) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getTenDangNhap());
        editor.putString(KEY_FULL_NAME, user.getHoTen());
        editor.putString(KEY_STUDENT_ID, user.getMaSinhVien());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(KEY_REMEMBER, remember);
        editor.apply();
    }

    //Lấy Id của người dùng đang đăng nhập
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, 1);
    }

    //Lấy họ tên sinh viên
    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "Nguyễn Văn A");
    }

    //Lấy mã sinh viên
    public String getStudentId() {
        return prefs.getString(KEY_STUDENT_ID, "2311020");
    }

    //Lấy địa chỉ email
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "nguyenvana@gmail.com");
    }

    //Lấy tên đăng nhập
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "sinhvien");
    }

    //Kiểm tra trạng thái ghi nhớ đăng nhập
    public boolean isRemembered() {
        return prefs.getBoolean(KEY_REMEMBER, false) && prefs.contains(KEY_USER_ID);
    }

    //Xóa thông tin phiên làm việc khi đăng xuất
    public void logout() {
        prefs.edit().clear().apply();
    }
}
