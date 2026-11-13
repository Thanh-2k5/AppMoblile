package com.example.quan_ly_lich_hoc_sv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quan_ly_lich_hoc_sv.models.BaiTap;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;
import com.example.quan_ly_lich_hoc_sv.models.LichThi;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;
import com.example.quan_ly_lich_hoc_sv.models.NguoiDung;
import com.example.quan_ly_lich_hoc_sv.models.NhacNho;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Khai báo tên cơ sở dữ liệu và phiên bản
    public static final String DATABASE_NAME = "StudentScheduleManager.db";
    public static final int DATABASE_VERSION = 1;

    //1. Khai báo tên bảng và các cột của bảng NguoiDung
    public static final String TABLE_NGUOI_DUNG = "NguoiDung";
    public static final String COL_ND_ID = "id";
    public static final String COL_ND_MA_SV = "maSinhVien";
    public static final String COL_ND_HO_TEN = "hoTen";
    public static final String COL_ND_EMAIL = "email";
    public static final String COL_ND_TEN_DANG_NHAP = "tenDangNhap";
    public static final String COL_ND_MAT_KHAU = "matKhau";

    //2. Khai báo tên bảng và các cột của bảng MonHoc
    public static final String TABLE_MON_HOC = "MonHoc";
    public static final String COL_MH_ID = "id";
    public static final String COL_MH_NGUOI_DUNG_ID = "nguoiDungId";
    public static final String COL_MH_MA_MON_HOC = "maMonHoc";
    public static final String COL_MH_TEN_MON_HOC = "tenMonHoc";
    public static final String COL_MH_SO_TIN_CHI = "soTinChi";
    public static final String COL_MH_GIANG_VIEN = "giangVien";
    public static final String COL_MH_HOC_KY = "hocKy";
    public static final String COL_MH_NAM_HOC = "namHoc";

    //3. Khai báo tên bảng và các cột của bảng LichHoc
    public static final String TABLE_LICH_HOC = "LichHoc";
    public static final String COL_LH_ID = "id";
    public static final String COL_LH_MON_HOC_ID = "monHocId";
    public static final String COL_LH_THU = "thu";
    public static final String COL_LH_GIO_BAT_DAU = "gioBatDau";
    public static final String COL_LH_GIO_KET_THUC = "gioKetThuc";
    public static final String COL_LH_PHONG_HOC = "phongHoc";
    public static final String COL_LH_GIANG_VIEN = "giangVien";
    public static final String COL_LH_NGAY_BAT_DAU = "ngayBatDau";
    public static final String COL_LH_NGAY_KET_THUC = "ngayKetThuc";

    //4. Khai báo tên bảng và các cột của bảng BaiTap
    public static final String TABLE_BAI_TAP = "BaiTap";
    public static final String COL_BT_ID = "id";
    public static final String COL_BT_MON_HOC_ID = "monHocId";
    public static final String COL_BT_TEN_BAI_TAP = "tenBaiTap";
    public static final String COL_BT_NOI_DUNG = "noiDung";
    public static final String COL_BT_HAN_NOP = "hanNop";
    public static final String COL_BT_MUC_DO_UU_TIEN = "mucDoUuTien";
    public static final String COL_BT_TRANG_THAI = "trangThai";

    //5. Khai báo tên bảng và các cột của bảng LichThi
    public static final String TABLE_LICH_THI = "LichThi";
    public static final String COL_LT_ID = "id";
    public static final String COL_LT_MON_HOC_ID = "monHocId";
    public static final String COL_LT_NGAY_THI = "ngayThi";
    public static final String COL_LT_GIO_THI = "gioThi";
    public static final String COL_LT_PHONG_THI = "phongThi";
    public static final String COL_LT_THOI_LUONG = "thoiLuong";
    public static final String COL_LT_HINH_THUC_THI = "hinhThucThi";
    public static final String COL_LT_GHI_CHU = "ghiChu";

    //6. Khai báo tên bảng và các cột của bảng NhacNho
    public static final String TABLE_NHAC_NHO = "NhacNho";
    public static final String COL_NN_ID = "id";
    public static final String COL_NN_NGUOI_DUNG_ID = "nguoiDungId";
    public static final String COL_NN_TIEU_DE = "tieuDe";
    public static final String COL_NN_NOI_DUNG = "noiDung";
    public static final String COL_NN_THOI_GIAN_NHAC = "thoiGianNhac";
    public static final String COL_NN_TRANG_THAI = "trangThai";

    //Khai báo biến Singleton quản lý kết nối SQLite
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //Bật ràng buộc khóa ngoại (Foreign Key) để tự động xóa dữ liệu liên quan
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Câu lệnh SQL tạo bảng NguoiDung
        String createNguoiDung = "CREATE TABLE " + TABLE_NGUOI_DUNG + " ("
                + COL_ND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ND_MA_SV + " TEXT, "
                + COL_ND_HO_TEN + " TEXT NOT NULL, "
                + COL_ND_EMAIL + " TEXT, "
                + COL_ND_TEN_DANG_NHAP + " TEXT UNIQUE NOT NULL, "
                + COL_ND_MAT_KHAU + " TEXT NOT NULL)";

        //Câu lệnh SQL tạo bảng MonHoc
        String createMonHoc = "CREATE TABLE " + TABLE_MON_HOC + " ("
                + COL_MH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MH_NGUOI_DUNG_ID + " INTEGER NOT NULL, "
                + COL_MH_MA_MON_HOC + " TEXT NOT NULL, "
                + COL_MH_TEN_MON_HOC + " TEXT NOT NULL, "
                + COL_MH_SO_TIN_CHI + " INTEGER DEFAULT 3, "
                + COL_MH_GIANG_VIEN + " TEXT, "
                + COL_MH_HOC_KY + " TEXT, "
                + COL_MH_NAM_HOC + " TEXT, "
                + "FOREIGN KEY(" + COL_MH_NGUOI_DUNG_ID + ") REFERENCES " + TABLE_NGUOI_DUNG + "(" + COL_ND_ID + ") ON DELETE CASCADE)";

        //Câu lệnh SQL tạo bảng LichHoc
        String createLichHoc = "CREATE TABLE " + TABLE_LICH_HOC + " ("
                + COL_LH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LH_MON_HOC_ID + " INTEGER NOT NULL, "
                + COL_LH_THU + " TEXT NOT NULL, "
                + COL_LH_GIO_BAT_DAU + " TEXT NOT NULL, "
                + COL_LH_GIO_KET_THUC + " TEXT NOT NULL, "
                + COL_LH_PHONG_HOC + " TEXT, "
                + COL_LH_GIANG_VIEN + " TEXT, "
                + COL_LH_NGAY_BAT_DAU + " TEXT, "
                + COL_LH_NGAY_KET_THUC + " TEXT, "
                + "FOREIGN KEY(" + COL_LH_MON_HOC_ID + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + ") ON DELETE CASCADE)";

        //Câu lệnh SQL tạo bảng BaiTap
        String createBaiTap = "CREATE TABLE " + TABLE_BAI_TAP + " ("
                + COL_BT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BT_MON_HOC_ID + " INTEGER NOT NULL, "
                + COL_BT_TEN_BAI_TAP + " TEXT NOT NULL, "
                + COL_BT_NOI_DUNG + " TEXT, "
                + COL_BT_HAN_NOP + " TEXT, "
                + COL_BT_MUC_DO_UU_TIEN + " TEXT, "
                + COL_BT_TRANG_THAI + " TEXT, "
                + "FOREIGN KEY(" + COL_BT_MON_HOC_ID + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + ") ON DELETE CASCADE)";

        //Câu lệnh SQL tạo bảng LichThi
        String createLichThi = "CREATE TABLE " + TABLE_LICH_THI + " ("
                + COL_LT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LT_MON_HOC_ID + " INTEGER NOT NULL, "
                + COL_LT_NGAY_THI + " TEXT NOT NULL, "
                + COL_LT_GIO_THI + " TEXT NOT NULL, "
                + COL_LT_PHONG_THI + " TEXT, "
                + COL_LT_THOI_LUONG + " TEXT, "
                + COL_LT_HINH_THUC_THI + " TEXT, "
                + COL_LT_GHI_CHU + " TEXT, "
                + "FOREIGN KEY(" + COL_LT_MON_HOC_ID + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + ") ON DELETE CASCADE)";

        //Câu lệnh SQL tạo bảng NhacNho
        String createNhacNho = "CREATE TABLE " + TABLE_NHAC_NHO + " ("
                + COL_NN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NN_NGUOI_DUNG_ID + " INTEGER NOT NULL, "
                + COL_NN_TIEU_DE + " TEXT NOT NULL, "
                + COL_NN_NOI_DUNG + " TEXT, "
                + COL_NN_THOI_GIAN_NHAC + " TEXT, "
                + COL_NN_TRANG_THAI + " TEXT, "
                + "FOREIGN KEY(" + COL_NN_NGUOI_DUNG_ID + ") REFERENCES " + TABLE_NGUOI_DUNG + "(" + COL_ND_ID + ") ON DELETE CASCADE)";

        //Thực thi tạo 6 bảng dữ liệu
        db.execSQL(createNguoiDung);
        db.execSQL(createMonHoc);
        db.execSQL(createLichHoc);
        db.execSQL(createBaiTap);
        db.execSQL(createLichThi);
        db.execSQL(createNhacNho);

        //Tạo sẵn tài khoản sinh viên mẫu và dữ liệu mẫu để kiểm thử
        ContentValues userValues = new ContentValues();
        userValues.put(COL_ND_MA_SV, "2311020");
        userValues.put(COL_ND_HO_TEN, "Nguyễn Văn A");
        userValues.put(COL_ND_EMAIL, "nguyenvana@gmail.com");
        userValues.put(COL_ND_TEN_DANG_NHAP, "sinhvien");
        userValues.put(COL_ND_MAT_KHAU, "123456");
        long defaultUserId = db.insert(TABLE_NGUOI_DUNG, null, userValues);
        if (defaultUserId != -1) {
            seedSampleDataForUserInternal(db, (int) defaultUserId);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Xóa các bảng cũ nếu nâng cấp phiên bản database và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHAC_NHO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LICH_THI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BAI_TAP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LICH_HOC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MON_HOC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NGUOI_DUNG);
        onCreate(db);
    }

    //Hàm thêm dữ liệu mẫu ban đầu cho tài khoản sinh viên
    private void seedSampleDataForUserInternal(SQLiteDatabase db, int userId) {
        //Thêm 3 môn học mẫu
        long mhAndroid = insertMonHocInternal(db, userId, "INT3110", "Lập trình Android", 3, "ThS. Trần Văn B", "Học kỳ 1", "2026 - 2027");
        long mhMath = insertMonHocInternal(db, userId, "MAT1011", "Toán cao cấp", 4, "PGS. Nguyễn Văn C", "Học kỳ 1", "2026 - 2027");
        long mhEng = insertMonHocInternal(db, userId, "ENG2020", "Anh văn chuyên ngành", 2, "Cô Lê Thị D", "Học kỳ 1", "2026 - 2027");

        //Thêm lịch học mẫu
        insertLichHocInternal(db, (int) mhAndroid, "Thứ 2", "07:30", "09:30", "A.102", "ThS. Trần Văn B", "01/09/2026", "30/12/2026");
        insertLichHocInternal(db, (int) mhMath, "Thứ 2", "09:45", "11:45", "B.101", "PGS. Nguyễn Văn C", "01/09/2026", "30/12/2026");
        insertLichHocInternal(db, (int) mhEng, "Thứ 2", "13:30", "15:30", "C.204", "Cô Lê Thị D", "01/09/2026", "30/12/2026");
        insertLichHocInternal(db, (int) mhAndroid, "Thứ 6", "07:30", "09:30", "A.302", "ThS. Trần Văn B", "01/09/2026", "30/12/2026");

        //Thêm bài tập mẫu
        insertBaiTapInternal(db, (int) mhAndroid, "Báo cáo tiến độ lớn", "Hoàn thiện thiết kế SQLite và CRUD các bảng", "23:59 - 21/11/2026", "Cao", "Chưa hoàn thành");
        insertBaiTapInternal(db, (int) mhMath, "Bài tập nhóm 2", "Giải hệ phương trình tuyến tính chương 3", "17:00 - 24/11/2026", "Trung bình", "Chưa hoàn thành");
        insertBaiTapInternal(db, (int) mhEng, "Thuyết trình nhóm 3", "Chuẩn bị slide chủ đề Information Technology", "23:59 - 28/11/2026", "Thấp", "Đã hoàn thành");

        //Thêm lịch thi mẫu
        insertLichThiInternal(db, (int) mhAndroid, "24/12/2026", "08:00", "B.302", "90 phút", "Thực hành", "Mang theo thẻ sinh viên");
        insertLichThiInternal(db, (int) mhMath, "20/12/2026", "08:00", "B.101", "120 phút", "Tự luận", "Không sử dụng tài liệu");
        insertLichThiInternal(db, (int) mhEng, "28/12/2026", "13:30", "C.204", "60 phút", "Trắc nghiệm", "Có phần nghe");

        //Thêm nhắc nhở mẫu
        insertNhacNhoInternal(db, userId, "Nộp báo cáo Android", "Kiểm tra lại chức năng CRUD trước khi nộp bài", "20:00 - 20/11/2026", "Đang bật");
        insertNhacNhoInternal(db, userId, "Ôn tập Toán cao cấp", "Ôn tập chương 1, 2, 3 chuẩn bị thi cuối kỳ", "19:00 - 15/12/2026", "Đang bật");
    }

    private long insertMonHocInternal(SQLiteDatabase db, int userId, String ma, String ten, int tinChi, String gv, String hocKy, String namHoc) {
        ContentValues cv = new ContentValues();
        cv.put(COL_MH_NGUOI_DUNG_ID, userId);
        cv.put(COL_MH_MA_MON_HOC, ma);
        cv.put(COL_MH_TEN_MON_HOC, ten);
        cv.put(COL_MH_SO_TIN_CHI, tinChi);
        cv.put(COL_MH_GIANG_VIEN, gv);
        cv.put(COL_MH_HOC_KY, hocKy);
        cv.put(COL_MH_NAM_HOC, namHoc);
        return db.insert(TABLE_MON_HOC, null, cv);
    }

    private void insertLichHocInternal(SQLiteDatabase db, int monHocId, String thu, String bd, String kt, String phong, String gv, String ngayBD, String ngayKT) {
        ContentValues cv = new ContentValues();
        cv.put(COL_LH_MON_HOC_ID, monHocId);
        cv.put(COL_LH_THU, thu);
        cv.put(COL_LH_GIO_BAT_DAU, bd);
        cv.put(COL_LH_GIO_KET_THUC, kt);
        cv.put(COL_LH_PHONG_HOC, phong);
        cv.put(COL_LH_GIANG_VIEN, gv);
        cv.put(COL_LH_NGAY_BAT_DAU, ngayBD);
        cv.put(COL_LH_NGAY_KET_THUC, ngayKT);
        db.insert(TABLE_LICH_HOC, null, cv);
    }

    private void insertBaiTapInternal(SQLiteDatabase db, int monHocId, String ten, String noiDung, String hanNop, String uuTien, String trangThai) {
        ContentValues cv = new ContentValues();
        cv.put(COL_BT_MON_HOC_ID, monHocId);
        cv.put(COL_BT_TEN_BAI_TAP, ten);
        cv.put(COL_BT_NOI_DUNG, noiDung);
        cv.put(COL_BT_HAN_NOP, hanNop);
        cv.put(COL_BT_MUC_DO_UU_TIEN, uuTien);
        cv.put(COL_BT_TRANG_THAI, trangThai);
        db.insert(TABLE_BAI_TAP, null, cv);
    }

    private void insertLichThiInternal(SQLiteDatabase db, int monHocId, String ngay, String gio, String phong, String thoiLuong, String hinhThuc, String ghiChu) {
        ContentValues cv = new ContentValues();
        cv.put(COL_LT_MON_HOC_ID, monHocId);
        cv.put(COL_LT_NGAY_THI, ngay);
        cv.put(COL_LT_GIO_THI, gio);
        cv.put(COL_LT_PHONG_THI, phong);
        cv.put(COL_LT_THOI_LUONG, thoiLuong);
        cv.put(COL_LT_HINH_THUC_THI, hinhThuc);
        cv.put(COL_LT_GHI_CHU, ghiChu);
        db.insert(TABLE_LICH_THI, null, cv);
    }

    private void insertNhacNhoInternal(SQLiteDatabase db, int userId, String tieuDe, String noiDung, String thoiGian, String trangThai) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NN_NGUOI_DUNG_ID, userId);
        cv.put(COL_NN_TIEU_DE, tieuDe);
        cv.put(COL_NN_NOI_DUNG, noiDung);
        cv.put(COL_NN_THOI_GIAN_NHAC, thoiGian);
        cv.put(COL_NN_TRANG_THAI, trangThai);
        db.insert(TABLE_NHAC_NHO, null, cv);
    }

    // =========================================================================
    // 1. XỬ LÝ CRUD BẢNG NGUOI DUNG (TÀI KHOẢN SINH VIÊN)
    // =========================================================================

    //Kiểm tra tên đăng nhập đã tồn tại chưa
    public boolean kiemTraTenDangNhapTonTai(String tenDangNhap) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NGUOI_DUNG, new String[]{COL_ND_ID},
                COL_ND_TEN_DANG_NHAP + " = ?", new String[]{tenDangNhap},
                null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    //Đăng ký tài khoản sinh viên mới
    public long dangKyNguoiDung(NguoiDung user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ND_MA_SV, user.getMaSinhVien());
        values.put(COL_ND_HO_TEN, user.getHoTen());
        values.put(COL_ND_EMAIL, user.getEmail());
        values.put(COL_ND_TEN_DANG_NHAP, user.getTenDangNhap());
        values.put(COL_ND_MAT_KHAU, user.getMatKhau());
        long userId = db.insert(TABLE_NGUOI_DUNG, null, values);
        if (userId != -1) {
            seedSampleDataForUserInternal(db, (int) userId);
        }
        return userId;
    }

    //Kiểm tra thông tin đăng nhập bằng tên đăng nhập/email và mật khẩu
    public NguoiDung dangNhap(String usernameOrEmail, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NGUOI_DUNG, null,
                "(" + COL_ND_TEN_DANG_NHAP + " = ? OR " + COL_ND_EMAIL + " = ?) AND " + COL_ND_MAT_KHAU + " = ?",
                new String[]{usernameOrEmail, usernameOrEmail, password},
                null, null, null);
        NguoiDung user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = docNguoiDungTuCursor(cursor);
            cursor.close();
        }
        return user;
    }

    //Lấy thông tin sinh viên theo Id
    public NguoiDung layNguoiDungTheoId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NGUOI_DUNG, null,
                COL_ND_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);
        NguoiDung user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = docNguoiDungTuCursor(cursor);
            cursor.close();
        }
        return user;
    }

    //Cập nhật thông tin cá nhân của sinh viên
    public int capNhatNguoiDung(NguoiDung user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ND_MA_SV, user.getMaSinhVien());
        values.put(COL_ND_HO_TEN, user.getHoTen());
        values.put(COL_ND_EMAIL, user.getEmail());
        values.put(COL_ND_TEN_DANG_NHAP, user.getTenDangNhap());
        values.put(COL_ND_MAT_KHAU, user.getMatKhau());
        return db.update(TABLE_NGUOI_DUNG, values, COL_ND_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    private NguoiDung docNguoiDungTuCursor(Cursor cursor) {
        NguoiDung user = new NguoiDung();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ND_ID)));
        user.setMaSinhVien(cursor.getString(cursor.getColumnIndexOrThrow(COL_ND_MA_SV)));
        user.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(COL_ND_HO_TEN)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_ND_EMAIL)));
        user.setTenDangNhap(cursor.getString(cursor.getColumnIndexOrThrow(COL_ND_TEN_DANG_NHAP)));
        user.setMatKhau(cursor.getString(cursor.getColumnIndexOrThrow(COL_ND_MAT_KHAU)));
        return user;
    }

    // =========================================================================
    // 2. XỬ LÝ CRUD BẢNG MON HOC
    // =========================================================================

    //Thêm môn học mới (Create)
    public long themMonHoc(MonHoc monHoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MH_NGUOI_DUNG_ID, monHoc.getNguoiDungId());
        values.put(COL_MH_MA_MON_HOC, monHoc.getMaMonHoc());
        values.put(COL_MH_TEN_MON_HOC, monHoc.getTenMonHoc());
        values.put(COL_MH_SO_TIN_CHI, monHoc.getSoTinChi());
        values.put(COL_MH_GIANG_VIEN, monHoc.getGiangVien());
        values.put(COL_MH_HOC_KY, monHoc.getHocKy());
        values.put(COL_MH_NAM_HOC, monHoc.getNamHoc());
        return db.insert(TABLE_MON_HOC, null, values);
    }

    //Lấy danh sách tất cả môn học của người dùng (Read)
    public List<MonHoc> layDanhSachMonHoc(int nguoiDungId) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null,
                COL_MH_NGUOI_DUNG_ID + " = ?", new String[]{String.valueOf(nguoiDungId)},
                null, null, COL_MH_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docMonHocTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Tìm kiếm môn học theo từ khóa
    public List<MonHoc> timKiemMonHoc(int nguoiDungId, String keyword) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String likeQuery = "%" + keyword + "%";
        Cursor cursor = db.query(TABLE_MON_HOC, null,
                COL_MH_NGUOI_DUNG_ID + " = ? AND (" + COL_MH_TEN_MON_HOC + " LIKE ? OR " + COL_MH_MA_MON_HOC + " LIKE ? OR " + COL_MH_GIANG_VIEN + " LIKE ?)",
                new String[]{String.valueOf(nguoiDungId), likeQuery, likeQuery, likeQuery},
                null, null, COL_MH_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docMonHocTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Lấy thông tin môn học theo Id
    public MonHoc layMonHocTheoId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null,
                COL_MH_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);
        MonHoc monHoc = null;
        if (cursor != null && cursor.moveToFirst()) {
            monHoc = docMonHocTuCursor(cursor);
            cursor.close();
        }
        return monHoc;
    }

    //Cập nhật thông tin môn học (Update)
    public int capNhatMonHoc(MonHoc monHoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MH_MA_MON_HOC, monHoc.getMaMonHoc());
        values.put(COL_MH_TEN_MON_HOC, monHoc.getTenMonHoc());
        values.put(COL_MH_SO_TIN_CHI, monHoc.getSoTinChi());
        values.put(COL_MH_GIANG_VIEN, monHoc.getGiangVien());
        values.put(COL_MH_HOC_KY, monHoc.getHocKy());
        values.put(COL_MH_NAM_HOC, monHoc.getNamHoc());
        return db.update(TABLE_MON_HOC, values, COL_MH_ID + " = ?", new String[]{String.valueOf(monHoc.getId())});
    }

    //Xóa môn học và toàn bộ lịch học, bài tập, lịch thi liên quan (Delete)
    public int xoaMonHoc(int monHocId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LICH_HOC, COL_LH_MON_HOC_ID + " = ?", new String[]{String.valueOf(monHocId)});
        db.delete(TABLE_BAI_TAP, COL_BT_MON_HOC_ID + " = ?", new String[]{String.valueOf(monHocId)});
        db.delete(TABLE_LICH_THI, COL_LT_MON_HOC_ID + " = ?", new String[]{String.valueOf(monHocId)});
        return db.delete(TABLE_MON_HOC, COL_MH_ID + " = ?", new String[]{String.valueOf(monHocId)});
    }

    private MonHoc docMonHocTuCursor(Cursor cursor) {
        MonHoc mh = new MonHoc();
        mh.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_ID)));
        mh.setNguoiDungId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_NGUOI_DUNG_ID)));
        mh.setMaMonHoc(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_MA_MON_HOC)));
        mh.setTenMonHoc(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TEN_MON_HOC)));
        mh.setSoTinChi(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_SO_TIN_CHI)));
        mh.setGiangVien(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_GIANG_VIEN)));
        mh.setHocKy(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_HOC_KY)));
        mh.setNamHoc(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_NAM_HOC)));
        return mh;
    }

    // =========================================================================
    // 3. XỬ LÝ CRUD BẢNG LICH HOC (THỜI KHÓA BIỂU)
    // =========================================================================

    //Thêm lịch học mới
    public long themLichHoc(LichHoc lichHoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LH_MON_HOC_ID, lichHoc.getMonHocId());
        values.put(COL_LH_THU, lichHoc.getThu());
        values.put(COL_LH_GIO_BAT_DAU, lichHoc.getGioBatDau());
        values.put(COL_LH_GIO_KET_THUC, lichHoc.getGioKetThuc());
        values.put(COL_LH_PHONG_HOC, lichHoc.getPhongHoc());
        values.put(COL_LH_GIANG_VIEN, lichHoc.getGiangVien());
        values.put(COL_LH_NGAY_BAT_DAU, lichHoc.getNgayBatDau());
        values.put(COL_LH_NGAY_KET_THUC, lichHoc.getNgayKetThuc());
        return db.insert(TABLE_LICH_HOC, null, values);
    }

    //Lấy danh sách lịch học theo thứ trong tuần (kết nối bảng MonHoc)
    public List<LichHoc> layDanhSachLichHocTheoThu(int nguoiDungId, String thu) {
        List<LichHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT lh.*, mh." + COL_MH_TEN_MON_HOC + ", mh." + COL_MH_GIANG_VIEN + " AS mhGiangVien "
                + "FROM " + TABLE_LICH_HOC + " lh "
                + "INNER JOIN " + TABLE_MON_HOC + " mh ON lh." + COL_LH_MON_HOC_ID + " = mh." + COL_MH_ID + " "
                + "WHERE mh." + COL_MH_NGUOI_DUNG_ID + " = ? AND lh." + COL_LH_THU + " = ? "
                + "ORDER BY lh." + COL_LH_GIO_BAT_DAU + " ASC";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(nguoiDungId), thu});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docLichHocTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Lấy toàn bộ lịch học trong tuần
    public List<LichHoc> layTatCaLichHoc(int nguoiDungId) {
        List<LichHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT lh.*, mh." + COL_MH_TEN_MON_HOC + ", mh." + COL_MH_GIANG_VIEN + " AS mhGiangVien "
                + "FROM " + TABLE_LICH_HOC + " lh "
                + "INNER JOIN " + TABLE_MON_HOC + " mh ON lh." + COL_LH_MON_HOC_ID + " = mh." + COL_MH_ID + " "
                + "WHERE mh." + COL_MH_NGUOI_DUNG_ID + " = ? "
                + "ORDER BY lh." + COL_LH_THU + " ASC, lh." + COL_LH_GIO_BAT_DAU + " ASC";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(nguoiDungId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docLichHocTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Cập nhật thông tin lịch học
    public int capNhatLichHoc(LichHoc lichHoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LH_MON_HOC_ID, lichHoc.getMonHocId());
        values.put(COL_LH_THU, lichHoc.getThu());
        values.put(COL_LH_GIO_BAT_DAU, lichHoc.getGioBatDau());
        values.put(COL_LH_GIO_KET_THUC, lichHoc.getGioKetThuc());
        values.put(COL_LH_PHONG_HOC, lichHoc.getPhongHoc());
        values.put(COL_LH_GIANG_VIEN, lichHoc.getGiangVien());
        values.put(COL_LH_NGAY_BAT_DAU, lichHoc.getNgayBatDau());
        values.put(COL_LH_NGAY_KET_THUC, lichHoc.getNgayKetThuc());
        return db.update(TABLE_LICH_HOC, values, COL_LH_ID + " = ?", new String[]{String.valueOf(lichHoc.getId())});
    }

    //Xóa lịch học theo Id
    public int xoaLichHoc(int lichHocId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_LICH_HOC, COL_LH_ID + " = ?", new String[]{String.valueOf(lichHocId)});
    }

    private LichHoc docLichHocTuCursor(Cursor cursor) {
        LichHoc lh = new LichHoc();
        lh.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LH_ID)));
        lh.setMonHocId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LH_MON_HOC_ID)));
        lh.setThu(cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_THU)));
        lh.setGioBatDau(cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_GIO_BAT_DAU)));
        lh.setGioKetThuc(cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_GIO_KET_THUC)));
        lh.setPhongHoc(cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_PHONG_HOC)));
        String gv = cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_GIANG_VIEN));
        int mhGvIdx = cursor.getColumnIndex("mhGiangVien");
        if ((gv == null || gv.trim().isEmpty()) && mhGvIdx != -1) {
            gv = cursor.getString(mhGvIdx);
        }
        lh.setGiangVien(gv);
        lh.setNgayBatDau(cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_NGAY_BAT_DAU)));
        lh.setNgayKetThuc(cursor.getString(cursor.getColumnIndexOrThrow(COL_LH_NGAY_KET_THUC)));
        int tenMhIdx = cursor.getColumnIndex(COL_MH_TEN_MON_HOC);
        if (tenMhIdx != -1) {
            lh.setTenMonHoc(cursor.getString(tenMhIdx));
        }
        return lh;
    }

    // =========================================================================
    // 4. XỬ LÝ CRUD BẢNG BAI TAP (CÔNG VIỆC HỌC TẬP)
    // =========================================================================

    //Thêm bài tập mới
    public long themBaiTap(BaiTap baiTap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BT_MON_HOC_ID, baiTap.getMonHocId());
        values.put(COL_BT_TEN_BAI_TAP, baiTap.getTenBaiTap());
        values.put(COL_BT_NOI_DUNG, baiTap.getNoiDung());
        values.put(COL_BT_HAN_NOP, baiTap.getHanNop());
        values.put(COL_BT_MUC_DO_UU_TIEN, baiTap.getMucDoUuTien());
        values.put(COL_BT_TRANG_THAI, baiTap.getTrangThai());
        return db.insert(TABLE_BAI_TAP, null, values);
    }

    //Lấy danh sách bài tập theo trạng thái lọc (Tất cả / Chưa hoàn thành / Đã hoàn thành)
    public List<BaiTap> layDanhSachBaiTap(int nguoiDungId, String trangThaiFilter) {
        List<BaiTap> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bt.*, mh.").append(COL_MH_TEN_MON_HOC).append(" ")
           .append("FROM ").append(TABLE_BAI_TAP).append(" bt ")
           .append("INNER JOIN ").append(TABLE_MON_HOC).append(" mh ON bt.").append(COL_BT_MON_HOC_ID).append(" = mh.").append(COL_MH_ID).append(" ")
           .append("WHERE mh.").append(COL_MH_NGUOI_DUNG_ID).append(" = ? ");

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(nguoiDungId));

        if (trangThaiFilter != null && !trangThaiFilter.isEmpty() && !trangThaiFilter.equals("Tất cả")) {
            sql.append("AND bt.").append(COL_BT_TRANG_THAI).append(" = ? ");
            args.add(trangThaiFilter);
        }
        sql.append("ORDER BY bt.").append(COL_BT_ID).append(" DESC");

        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docBaiTapTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Cập nhật toàn bộ thông tin bài tập
    public int capNhatBaiTap(BaiTap baiTap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BT_MON_HOC_ID, baiTap.getMonHocId());
        values.put(COL_BT_TEN_BAI_TAP, baiTap.getTenBaiTap());
        values.put(COL_BT_NOI_DUNG, baiTap.getNoiDung());
        values.put(COL_BT_HAN_NOP, baiTap.getHanNop());
        values.put(COL_BT_MUC_DO_UU_TIEN, baiTap.getMucDoUuTien());
        values.put(COL_BT_TRANG_THAI, baiTap.getTrangThai());
        return db.update(TABLE_BAI_TAP, values, COL_BT_ID + " = ?", new String[]{String.valueOf(baiTap.getId())});
    }

    //Cập nhật nhanh trạng thái hoàn thành của bài tập
    public int capNhatTrangThaiBaiTap(int baiTapId, String trangThaiMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BT_TRANG_THAI, trangThaiMoi);
        return db.update(TABLE_BAI_TAP, values, COL_BT_ID + " = ?", new String[]{String.valueOf(baiTapId)});
    }

    //Xóa bài tập theo Id
    public int xoaBaiTap(int baiTapId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BAI_TAP, COL_BT_ID + " = ?", new String[]{String.valueOf(baiTapId)});
    }

    private BaiTap docBaiTapTuCursor(Cursor cursor) {
        BaiTap bt = new BaiTap();
        bt.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BT_ID)));
        bt.setMonHocId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BT_MON_HOC_ID)));
        bt.setTenBaiTap(cursor.getString(cursor.getColumnIndexOrThrow(COL_BT_TEN_BAI_TAP)));
        bt.setNoiDung(cursor.getString(cursor.getColumnIndexOrThrow(COL_BT_NOI_DUNG)));
        bt.setHanNop(cursor.getString(cursor.getColumnIndexOrThrow(COL_BT_HAN_NOP)));
        bt.setMucDoUuTien(cursor.getString(cursor.getColumnIndexOrThrow(COL_BT_MUC_DO_UU_TIEN)));
        bt.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(COL_BT_TRANG_THAI)));
        int tenMhIdx = cursor.getColumnIndex(COL_MH_TEN_MON_HOC);
        if (tenMhIdx != -1) {
            bt.setTenMonHoc(cursor.getString(tenMhIdx));
        }
        return bt;
    }

    // =========================================================================
    // 5. XỬ LÝ CRUD BẢNG LICH THI
    // =========================================================================

    //Thêm lịch thi mới
    public long themLichThi(LichThi lichThi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LT_MON_HOC_ID, lichThi.getMonHocId());
        values.put(COL_LT_NGAY_THI, lichThi.getNgayThi());
        values.put(COL_LT_GIO_THI, lichThi.getGioThi());
        values.put(COL_LT_PHONG_THI, lichThi.getPhongThi());
        values.put(COL_LT_THOI_LUONG, lichThi.getThoiLuong());
        values.put(COL_LT_HINH_THUC_THI, lichThi.getHinhThucThi());
        values.put(COL_LT_GHI_CHU, lichThi.getGhiChu());
        return db.insert(TABLE_LICH_THI, null, values);
    }

    //Lấy danh sách lịch thi của sinh viên
    public List<LichThi> layDanhSachLichThi(int nguoiDungId) {
        List<LichThi> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT lt.*, mh." + COL_MH_TEN_MON_HOC + " "
                + "FROM " + TABLE_LICH_THI + " lt "
                + "INNER JOIN " + TABLE_MON_HOC + " mh ON lt." + COL_LT_MON_HOC_ID + " = mh." + COL_MH_ID + " "
                + "WHERE mh." + COL_MH_NGUOI_DUNG_ID + " = ? "
                + "ORDER BY lt." + COL_LT_ID + " DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(nguoiDungId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docLichThiTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Cập nhật thông tin lịch thi
    public int capNhatLichThi(LichThi lichThi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LT_MON_HOC_ID, lichThi.getMonHocId());
        values.put(COL_LT_NGAY_THI, lichThi.getNgayThi());
        values.put(COL_LT_GIO_THI, lichThi.getGioThi());
        values.put(COL_LT_PHONG_THI, lichThi.getPhongThi());
        values.put(COL_LT_THOI_LUONG, lichThi.getThoiLuong());
        values.put(COL_LT_HINH_THUC_THI, lichThi.getHinhThucThi());
        values.put(COL_LT_GHI_CHU, lichThi.getGhiChu());
        return db.update(TABLE_LICH_THI, values, COL_LT_ID + " = ?", new String[]{String.valueOf(lichThi.getId())});
    }

    //Xóa lịch thi theo Id
    public int xoaLichThi(int lichThiId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_LICH_THI, COL_LT_ID + " = ?", new String[]{String.valueOf(lichThiId)});
    }

    private LichThi docLichThiTuCursor(Cursor cursor) {
        LichThi lt = new LichThi();
        lt.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LT_ID)));
        lt.setMonHocId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LT_MON_HOC_ID)));
        lt.setNgayThi(cursor.getString(cursor.getColumnIndexOrThrow(COL_LT_NGAY_THI)));
        lt.setGioThi(cursor.getString(cursor.getColumnIndexOrThrow(COL_LT_GIO_THI)));
        lt.setPhongThi(cursor.getString(cursor.getColumnIndexOrThrow(COL_LT_PHONG_THI)));
        lt.setThoiLuong(cursor.getString(cursor.getColumnIndexOrThrow(COL_LT_THOI_LUONG)));
        lt.setHinhThucThi(cursor.getString(cursor.getColumnIndexOrThrow(COL_LT_HINH_THUC_THI)));
        lt.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(COL_LT_GHI_CHU)));
        int tenMhIdx = cursor.getColumnIndex(COL_MH_TEN_MON_HOC);
        if (tenMhIdx != -1) {
            lt.setTenMonHoc(cursor.getString(tenMhIdx));
        }
        return lt;
    }

    // =========================================================================
    // 6. XỬ LÝ CRUD BẢNG NHAC NHO
    // =========================================================================

    //Thêm nhắc nhở mới
    public long themNhacNho(NhacNho nhacNho) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NN_NGUOI_DUNG_ID, nhacNho.getNguoiDungId());
        values.put(COL_NN_TIEU_DE, nhacNho.getTieuDe());
        values.put(COL_NN_NOI_DUNG, nhacNho.getNoiDung());
        values.put(COL_NN_THOI_GIAN_NHAC, nhacNho.getThoiGianNhac());
        values.put(COL_NN_TRANG_THAI, nhacNho.getTrangThai());
        return db.insert(TABLE_NHAC_NHO, null, values);
    }

    //Lấy danh sách nhắc nhở của người dùng
    public List<NhacNho> layDanhSachNhacNho(int nguoiDungId) {
        List<NhacNho> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NHAC_NHO, null,
                COL_NN_NGUOI_DUNG_ID + " = ?", new String[]{String.valueOf(nguoiDungId)},
                null, null, COL_NN_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(docNhacNhoTuCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    //Cập nhật thông tin nhắc nhở
    public int capNhatNhacNho(NhacNho nhacNho) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NN_TIEU_DE, nhacNho.getTieuDe());
        values.put(COL_NN_NOI_DUNG, nhacNho.getNoiDung());
        values.put(COL_NN_THOI_GIAN_NHAC, nhacNho.getThoiGianNhac());
        values.put(COL_NN_TRANG_THAI, nhacNho.getTrangThai());
        return db.update(TABLE_NHAC_NHO, values, COL_NN_ID + " = ?", new String[]{String.valueOf(nhacNho.getId())});
    }

    //Cập nhật nhanh trạng thái bật/tắt của nhắc nhở
    public int capNhatTrangThaiNhacNho(int nhacNhoId, String trangThaiMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NN_TRANG_THAI, trangThaiMoi);
        return db.update(TABLE_NHAC_NHO, values, COL_NN_ID + " = ?", new String[]{String.valueOf(nhacNhoId)});
    }

    //Xóa nhắc nhở theo Id
    public int xoaNhacNho(int nhacNhoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NHAC_NHO, COL_NN_ID + " = ?", new String[]{String.valueOf(nhacNhoId)});
    }

    private NhacNho docNhacNhoTuCursor(Cursor cursor) {
        NhacNho nn = new NhacNho();
        nn.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_NN_ID)));
        nn.setNguoiDungId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_NN_NGUOI_DUNG_ID)));
        nn.setTieuDe(cursor.getString(cursor.getColumnIndexOrThrow(COL_NN_TIEU_DE)));
        nn.setNoiDung(cursor.getString(cursor.getColumnIndexOrThrow(COL_NN_NOI_DUNG)));
        nn.setThoiGianNhac(cursor.getString(cursor.getColumnIndexOrThrow(COL_NN_THOI_GIAN_NHAC)));
        nn.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(COL_NN_TRANG_THAI)));
        return nn;
    }

    // =========================================================================
    // 7. CÁC HÀM ĐẾM SỐ LƯỢNG TỔNG HỢP TRÊN TRANG CHỦ
    // =========================================================================

    //Đếm tổng số môn học của sinh viên
    public int demSoMonHoc(int nguoiDungId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_MON_HOC + " WHERE " + COL_MH_NGUOI_DUNG_ID + " = ?",
                new String[]{String.valueOf(nguoiDungId)});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    //Đếm số ca học theo thứ trong tuần
    public int demSoLichHocTheoThu(int nguoiDungId, String thu) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_LICH_HOC + " lh "
                + "INNER JOIN " + TABLE_MON_HOC + " mh ON lh." + COL_LH_MON_HOC_ID + " = mh." + COL_MH_ID + " "
                + "WHERE mh." + COL_MH_NGUOI_DUNG_ID + " = ? AND lh." + COL_LH_THU + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(nguoiDungId), thu});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    //Đếm số bài tập chưa hoàn thành
    public int demSoBaiTapChuaXong(int nguoiDungId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_BAI_TAP + " bt "
                + "INNER JOIN " + TABLE_MON_HOC + " mh ON bt." + COL_BT_MON_HOC_ID + " = mh." + COL_MH_ID + " "
                + "WHERE mh." + COL_MH_NGUOI_DUNG_ID + " = ? AND bt." + COL_BT_TRANG_THAI + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(nguoiDungId), "Chưa hoàn thành"});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    //Đếm số nhắc nhở đang bật
    public int demSoNhacNhoDangBat(int nguoiDungId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NHAC_NHO
                        + " WHERE " + COL_NN_NGUOI_DUNG_ID + " = ? AND " + COL_NN_TRANG_THAI + " = ?",
                new String[]{String.valueOf(nguoiDungId), "Đang bật"});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }
}
