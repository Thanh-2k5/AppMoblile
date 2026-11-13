package com.example.quan_ly_lich_hoc_sv;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.quan_ly_lich_hoc_sv.fragments.ExamScheduleFragment;
import com.example.quan_ly_lich_hoc_sv.fragments.HomeFragment;
import com.example.quan_ly_lich_hoc_sv.fragments.ProfileSettingsFragment;
import com.example.quan_ly_lich_hoc_sv.fragments.ScheduleFragment;
import com.example.quan_ly_lich_hoc_sv.fragments.SubjectManagementFragment;
import com.example.quan_ly_lich_hoc_sv.fragments.TaskManagementFragment;

public class HomeActivity extends AppCompatActivity {

    //Khai báo các hằng số đại diện cho từng tab màn hình
    public static final int TAB_HOME = 0;
    public static final int TAB_SUBJECTS = 1;
    public static final int TAB_SCHEDULE = 2;
    public static final int TAB_TASKS = 3;
    public static final int TAB_EXAMS = 4;
    public static final int TAB_PROFILE = 5;

    //Khai báo biến lưu vị trí tab hiện tại và Id khung chứa Fragment
    private int currentTab = -1;
    private int containerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Tạo khung chứa (FrameLayout) để nạp các Fragment giao diện
        FrameLayout container = new FrameLayout(this);
        containerId = View.generateViewId();
        container.setId(containerId);
        setContentView(container);

        //Căn chỉnh lề theo thanh hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(container, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Mặc định hiển thị màn hình Trang chủ khi mới vào
        switchToTab(TAB_HOME);
    }

    //Hàm xử lý chuyển đổi giữa các màn hình Fragment theo chỉ số tab
    public void switchToTab(int tabIndex) {
        if (currentTab == tabIndex) return;
        currentTab = tabIndex;

        Fragment fragment;
        //Xác định Fragment cần hiển thị
        switch (tabIndex) {
            case TAB_SUBJECTS:
                fragment = new SubjectManagementFragment();
                break;
            case TAB_SCHEDULE:
                fragment = new ScheduleFragment();
                break;
            case TAB_TASKS:
                fragment = new TaskManagementFragment();
                break;
            case TAB_EXAMS:
                fragment = new ExamScheduleFragment();
                break;
            case TAB_PROFILE:
                fragment = new ProfileSettingsFragment();
                break;
            case TAB_HOME:
            default:
                fragment = new HomeFragment();
                break;
        }

        //Thay thế Fragment vào khung chứa
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    //Hàm mở trực tiếp tab Nhắc nhở khi nhấn vào biểu tượng chuông thông báo ở Trang chủ
    public void openRemindersTab() {
        currentTab = TAB_EXAMS;
        ExamScheduleFragment fragment = new ExamScheduleFragment();
        fragment.setStartOnReminderTab(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    //Hàm tham chiếu Id và bắt sự kiện chuyển tab cho thanh điều hướng dưới cùng (Bottom Navigation)
    public void setupBottomNavigation(View rootView, int activeTab) {
        //Tham chiếu Id đến các nút trên thanh điều hướng
        View navHome = rootView.findViewById(R.id.navHome);
        View navSubjects = rootView.findViewById(R.id.navSubjects);
        View navSchedule = rootView.findViewById(R.id.navSchedule);
        View navTasks = rootView.findViewById(R.id.navTasks);
        View navExams = rootView.findViewById(R.id.navExams);
        View navProfile = rootView.findViewById(R.id.navProfile);

        //Gán sự kiện chuyển màn hình khi nhấn vào từng mục
        if (navHome != null && activeTab != TAB_HOME) {
            navHome.setOnClickListener(v -> switchToTab(TAB_HOME));
        }
        if (navSubjects != null && activeTab != TAB_SUBJECTS) {
            navSubjects.setOnClickListener(v -> switchToTab(TAB_SUBJECTS));
        }
        if (navSchedule != null && activeTab != TAB_SCHEDULE) {
            navSchedule.setOnClickListener(v -> switchToTab(TAB_SCHEDULE));
        }
        if (navTasks != null && activeTab != TAB_TASKS) {
            navTasks.setOnClickListener(v -> switchToTab(TAB_TASKS));
        }
        if (navExams != null && activeTab != TAB_EXAMS) {
            navExams.setOnClickListener(v -> switchToTab(TAB_EXAMS));
        }
        if (navProfile != null && activeTab != TAB_PROFILE) {
            navProfile.setOnClickListener(v -> switchToTab(TAB_PROFILE));
        }
    }
}
