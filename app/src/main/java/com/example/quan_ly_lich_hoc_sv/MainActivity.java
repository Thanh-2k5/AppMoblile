package com.example.quan_ly_lich_hoc_sv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quan_ly_lich_hoc_sv.database.DatabaseHelper;
import com.example.quan_ly_lich_hoc_sv.models.NguoiDung;
import com.example.quan_ly_lich_hoc_sv.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    //Khai báo các biến giao diện
    private EditText etUsername, etPassword;
    private CheckBox cbRemember;
    private Button btnLogin;
    private TextView tvRegister;

    //Khai báo các biến xử lý logic và cơ sở dữ liệu
    private boolean isPasswordVisible = false;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    //Nhận kết quả trả về từ màn hình Đăng ký
    private final ActivityResultLauncher<Intent> registerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    //Lấy tên đăng nhập vừa đăng ký thành công điền vào ô nhập
                    String username = result.getData().getStringExtra("registered_username");
                    if (username != null) {
                        etUsername.setText(username);
                        etPassword.requestFocus();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Khởi tạo đối tượng quản lý SQLite và phiên đăng nhập
        dbHelper = DatabaseHelper.getInstance(this);
        sessionManager = new SessionManager(this);

        //Kiểm tra nếu người dùng đã chọn ghi nhớ đăng nhập thì chuyển thẳng vào Trang chủ
        if (sessionManager.isRemembered()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Thiết lập căn chỉnh lề theo thanh trạng thái hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Tham chiếu Id đến các biến giao diện
        initViews();

        //Xử lý các sự kiện tương tác của người dùng
        setupListeners();
    }

    private void initViews() {
        //Tham chiếu Id đến các biến giao diện
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        //Xử lý ẩn/hiện mật khẩu khi chạm vào biểu tượng con mắt bên phải ô mật khẩu
        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableRightIndex = 2;
                if (etPassword.getCompoundDrawables()[drawableRightIndex] != null) {
                    int drawableWidth = etPassword.getCompoundDrawables()[drawableRightIndex].getBounds().width();
                    if (event.getRawX() >= (etPassword.getRight() - drawableWidth - etPassword.getPaddingEnd())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
            }
            return false;
        });

        //Xử lý sự kiện khi nhấn nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            //Lấy dữ liệu từ các ô nhập liệu
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean remember = cbRemember.isChecked();

            //Kiểm tra dữ liệu rỗng
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            //Kiểm tra tài khoản trong cơ sở dữ liệu SQLite
            NguoiDung user = dbHelper.dangNhap(username, password);
            if (user != null) {
                //Lưu thông tin phiên đăng nhập và chuyển sang màn hình Trang chủ
                sessionManager.saveLoginSession(user, remember);
                Toast.makeText(this, "Xin chào, " + user.getHoTen() + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                //Hiển thị thông báo khi sai thông tin đăng nhập
                Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu! (TK mẫu: sinhvien / 123456)", Toast.LENGTH_LONG).show();
            }
        });

        //Xử lý chuyển sang màn hình Đăng ký tài khoản
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            registerLauncher.launch(intent);
        });
    }

    //Hàm xử lý đổi trạng thái ẩn/hiện mật khẩu
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
            isPasswordVisible = false;
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye, 0);
            isPasswordVisible = true;
        }
        //Đưa con trỏ chuột về cuối chuỗi mật khẩu
        if (etPassword.getText() != null) {
            etPassword.setSelection(etPassword.getText().length());
        }
    }
}
