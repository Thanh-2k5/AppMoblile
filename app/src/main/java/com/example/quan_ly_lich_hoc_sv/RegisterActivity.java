package com.example.quan_ly_lich_hoc_sv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.NguoiDung;

public class RegisterActivity extends AppCompatActivity {

    //Khai báo các biến giao diện
    private EditText etRegisterFullName, etRegisterStudentId, etRegisterEmail;
    private EditText etRegisterUsername, etRegisterPassword, etRegisterConfirmPassword;
    private Button btnRegisterSubmit;
    private TextView tvBackToLogin;

    //Khai báo các biến trạng thái và cơ sở dữ liệu
    private boolean isPassVisible = false;
    private boolean isConfirmPassVisible = false;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //Thiết lập khoảng đệm hệ thống cho màn hình
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Khởi tạo đối tượng kết nối SQLite
        dbHelper = DatabaseHelper.getInstance(this);

        //Tham chiếu Id đến các biến giao diện
        initViews();

        //Xử lý sự kiện cho các nút bấm
        setupListeners();
    }

    private void initViews() {
        //Tham chiếu Id đến các biến giao diện
        etRegisterFullName = findViewById(R.id.etRegisterFullName);
        etRegisterStudentId = findViewById(R.id.etRegisterStudentId);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        //Xử lý ẩn/hiện mật khẩu ở ô nhập mật khẩu
        etRegisterPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (etRegisterPassword.getCompoundDrawables()[2] != null) {
                    int width = etRegisterPassword.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= (etRegisterPassword.getRight() - width - etRegisterPassword.getPaddingEnd())) {
                        isPassVisible = !isPassVisible;
                        toggleVisibility(etRegisterPassword, isPassVisible);
                        return true;
                    }
                }
            }
            return false;
        });

        //Xử lý ẩn/hiện mật khẩu ở ô xác nhận mật khẩu
        etRegisterConfirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (etRegisterConfirmPassword.getCompoundDrawables()[2] != null) {
                    int width = etRegisterConfirmPassword.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= (etRegisterConfirmPassword.getRight() - width - etRegisterConfirmPassword.getPaddingEnd())) {
                        isConfirmPassVisible = !isConfirmPassVisible;
                        toggleVisibility(etRegisterConfirmPassword, isConfirmPassVisible);
                        return true;
                    }
                }
            }
            return false;
        });

        //Xử lý sự kiện khi nhấn nút Đăng ký
        btnRegisterSubmit.setOnClickListener(v -> handleRegister());

        //Xử lý sự kiện quay lại màn hình Đăng nhập
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    //Hàm thay đổi kiểu hiển thị mật khẩu
    private void toggleVisibility(EditText editText, boolean visible) {
        if (visible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye, 0);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
        }
        if (editText.getText() != null) {
            editText.setSelection(editText.getText().length());
        }
    }

    //Hàm kiểm tra thông tin và lưu tài khoản mới vào SQLite
    private void handleRegister() {
        //Lấy dữ liệu người dùng nhập vào
        String fullName = etRegisterFullName.getText().toString().trim();
        String studentId = etRegisterStudentId.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String username = etRegisterUsername.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String confirmPass = etRegisterConfirmPassword.getText().toString().trim();

        //Kiểm tra các trường không được để trống
        if (fullName.isEmpty() || studentId.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Kiểm tra mật khẩu xác nhận có trùng khớp không
        if (!password.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Kiểm tra tên đăng nhập đã tồn tại trong SQLite chưa
        if (dbHelper.kiemTraTenDangNhapTonTai(username)) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Tạo đối tượng NguoiDung và lưu vào bảng NguoiDung trong SQLite
        NguoiDung newUser = new NguoiDung(studentId, fullName, email, username, password);
        long result = dbHelper.dangKyNguoiDung(newUser);

        //Hiển thị kết quả đăng ký
        if (result != -1) {
            Toast.makeText(this, "Đăng ký tài khoản thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("registered_username", username);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}
