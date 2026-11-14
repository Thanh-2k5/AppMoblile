package com.example.quan_ly_lich_hoc_sv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quan_ly_lich_hoc_sv.models.BaiTap;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Khai báo tên cơ sở dữ liệu và phiên bản
    public static final String TEN_CSDL = "StudentScheduleManager.db";
    public static final int PHIEN_BAN_CSDL = 3;

    //1. Khai báo tên bảng và các cột của bảng MonHoc
    public static final String BANG_MON_HOC = "MonHoc";
    public static final String COT_MH_ID = "id";
    public static final String COT_MH_MA_MON_HOC = "maMonHoc";
    public static final String COT_MH_TEN_MON_HOC = "tenMonHoc";
    public static final String COT_MH_SO_TIN_CHI = "soTinChi";
    public static final String COT_MH_GIANG_VIEN = "giangVien";
    public static final String COT_MH_HOC_KY = "hocKy";
    public static final String COT_MH_NAM_HOC = "namHoc";

    //2. Khai báo tên bảng và các cột của bảng LichHoc
    public static final String BANG_LICH_HOC = "LichHoc";
    public static final String COT_LH_ID = "id";
    public static final String COT_LH_MON_HOC_ID = "monHocId";
    public static final String COT_LH_THU = "thu";
    public static final String COT_LH_GIO_BAT_DAU = "gioBatDau";
    public static final String COT_LH_GIO_KET_THUC = "gioKetThuc";
    public static final String COT_LH_PHONG_HOC = "phongHoc";
    public static final String COT_LH_GIANG_VIEN = "giangVien";
    public static final String COT_LH_NGAY_BAT_DAU = "ngayBatDau";
    public static final String COT_LH_NGAY_KET_THUC = "ngayKetThuc";

    //3. Khai báo tên bảng và các cột của bảng BaiTap
    public static final String BANG_BAI_TAP = "BaiTap";
    public static final String COT_BT_ID = "id";
    public static final String COT_BT_MON_HOC_ID = "monHocId";
    public static final String COT_BT_TEN_BAI_TAP = "tenBaiTap";
    public static final String COT_BT_NOI_DUNG = "noiDung";
    public static final String COT_BT_HAN_NOP = "hanNop";
    public static final String COT_BT_MUC_DO_UU_TIEN = "mucDoUuTien";
    public static final String COT_BT_TRANG_THAI = "trangThai";

    //Khai báo biến quản lý kết nối SQLite dùng chung
    private static DatabaseHelper doiTuongCSDL;

    public static synchronized DatabaseHelper getInstance(Context nguCanh) {
        if (doiTuongCSDL == null) {
            doiTuongCSDL = new DatabaseHelper(nguCanh.getApplicationContext());
        }
        return doiTuongCSDL;
    }

    public DatabaseHelper(Context nguCanh) {
        super(nguCanh, TEN_CSDL, null, PHIEN_BAN_CSDL);
    }

    @Override
    public void onConfigure(SQLiteDatabase csdl) {
        super.onConfigure(csdl);
        //Bật ràng buộc khóa ngoại để tự động xóa dữ liệu liên quan
        csdl.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase csdl) {
        //Câu lệnh SQL tạo bảng MonHoc
        String taoBangMonHoc = "CREATE TABLE " + BANG_MON_HOC + " ("
                + COT_MH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COT_MH_MA_MON_HOC + " TEXT NOT NULL, "
                + COT_MH_TEN_MON_HOC + " TEXT NOT NULL, "
                + COT_MH_SO_TIN_CHI + " INTEGER DEFAULT 3, "
                + COT_MH_GIANG_VIEN + " TEXT, "
                + COT_MH_HOC_KY + " TEXT, "
                + COT_MH_NAM_HOC + " TEXT)";

        //Câu lệnh SQL tạo bảng LichHoc
        String taoBangLichHoc = "CREATE TABLE " + BANG_LICH_HOC + " ("
                + COT_LH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COT_LH_MON_HOC_ID + " INTEGER NOT NULL, "
                + COT_LH_THU + " TEXT NOT NULL, "
                + COT_LH_GIO_BAT_DAU + " TEXT NOT NULL, "
                + COT_LH_GIO_KET_THUC + " TEXT NOT NULL, "
                + COT_LH_PHONG_HOC + " TEXT, "
                + COT_LH_GIANG_VIEN + " TEXT, "
                + COT_LH_NGAY_BAT_DAU + " TEXT, "
                + COT_LH_NGAY_KET_THUC + " TEXT, "
                + "FOREIGN KEY(" + COT_LH_MON_HOC_ID + ") REFERENCES " + BANG_MON_HOC + "(" + COT_MH_ID + ") ON DELETE CASCADE)";

        //Câu lệnh SQL tạo bảng BaiTap
        String taoBangBaiTap = "CREATE TABLE " + BANG_BAI_TAP + " ("
                + COT_BT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COT_BT_MON_HOC_ID + " INTEGER NOT NULL, "
                + COT_BT_TEN_BAI_TAP + " TEXT NOT NULL, "
                + COT_BT_NOI_DUNG + " TEXT, "
                + COT_BT_HAN_NOP + " TEXT, "
                + COT_BT_MUC_DO_UU_TIEN + " TEXT, "
                + COT_BT_TRANG_THAI + " TEXT, "
                + "FOREIGN KEY(" + COT_BT_MON_HOC_ID + ") REFERENCES " + BANG_MON_HOC + "(" + COT_MH_ID + ") ON DELETE CASCADE)";

        //Thực thi tạo 3 bảng dữ liệu
        csdl.execSQL(taoBangMonHoc);
        csdl.execSQL(taoBangLichHoc);
        csdl.execSQL(taoBangBaiTap);

        //Tạo sẵn dữ liệu mẫu ban đầu để kiểm thử
        themDuLieuMauBanDau(csdl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase csdl, int phienBanCu, int phienBanMoi) {
        //Xóa các bảng cũ nếu nâng cấp phiên bản database và tạo lại
        csdl.execSQL("DROP TABLE IF EXISTS NhacNho");
        csdl.execSQL("DROP TABLE IF EXISTS LichThi");
        csdl.execSQL("DROP TABLE IF EXISTS " + BANG_BAI_TAP);
        csdl.execSQL("DROP TABLE IF EXISTS " + BANG_LICH_HOC);
        csdl.execSQL("DROP TABLE IF EXISTS " + BANG_MON_HOC);
        csdl.execSQL("DROP TABLE IF EXISTS NguoiDung");
        onCreate(csdl);
    }

    //Hàm thêm dữ liệu mẫu ban đầu cho ứng dụng
    private void themDuLieuMauBanDau(SQLiteDatabase csdl) {
        //Thêm 3 môn học mẫu
        long mhAndroid = themMonHocMau(csdl, "INT3110", "Lập trình Android", 3, "ThS. Trần Văn B", "Học kỳ 1", "2026 - 2027");
        long mhToan = themMonHocMau(csdl, "MAT1011", "Toán cao cấp", 4, "PGS. Nguyễn Văn C", "Học kỳ 1", "2026 - 2027");
        long mhAnh = themMonHocMau(csdl, "ENG2020", "Anh văn chuyên ngành", 2, "Cô Lê Thị D", "Học kỳ 1", "2026 - 2027");

        //Thêm lịch học mẫu
        themLichHocMau(csdl, (int) mhAndroid, "Thứ 2", "07:30", "09:30", "A.102", "ThS. Trần Văn B", "01/09/2026", "30/12/2026");
        themLichHocMau(csdl, (int) mhToan, "Thứ 2", "09:45", "11:45", "B.101", "PGS. Nguyễn Văn C", "01/09/2026", "30/12/2026");
        themLichHocMau(csdl, (int) mhAnh, "Thứ 2", "13:30", "15:30", "C.204", "Cô Lê Thị D", "01/09/2026", "30/12/2026");
        themLichHocMau(csdl, (int) mhAndroid, "Thứ 6", "07:30", "09:30", "A.302", "ThS. Trần Văn B", "01/09/2026", "30/12/2026");

        //Thêm bài tập mẫu
        themBaiTapMau(csdl, (int) mhAndroid, "Báo cáo tiến độ lớn", "Hoàn thiện thiết kế SQLite và CRUD các bảng", "23:59 - 21/11/2026", "Cao", "Chưa hoàn thành");
        themBaiTapMau(csdl, (int) mhToan, "Bài tập nhóm 2", "Giải hệ phương trình tuyến tính chương 3", "17:00 - 24/11/2026", "Trung bình", "Chưa hoàn thành");
        themBaiTapMau(csdl, (int) mhAnh, "Thuyết trình nhóm 3", "Chuẩn bị slide chủ đề Information Technology", "23:59 - 28/11/2026", "Thấp", "Đã hoàn thành");
    }

    private long themMonHocMau(SQLiteDatabase csdl, String maMon, String tenMon, int soTinChi, String giangVien, String hocKy, String namHoc) {
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_MH_MA_MON_HOC, maMon);
        giaTri.put(COT_MH_TEN_MON_HOC, tenMon);
        giaTri.put(COT_MH_SO_TIN_CHI, soTinChi);
        giaTri.put(COT_MH_GIANG_VIEN, giangVien);
        giaTri.put(COT_MH_HOC_KY, hocKy);
        giaTri.put(COT_MH_NAM_HOC, namHoc);
        return csdl.insert(BANG_MON_HOC, null, giaTri);
    }

    private void themLichHocMau(SQLiteDatabase csdl, int monHocId, String thu, String gioBatDau, String gioKetThuc, String phongHoc, String giangVien, String ngayBatDau, String ngayKetThuc) {
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_LH_MON_HOC_ID, monHocId);
        giaTri.put(COT_LH_THU, thu);
        giaTri.put(COT_LH_GIO_BAT_DAU, gioBatDau);
        giaTri.put(COT_LH_GIO_KET_THUC, gioKetThuc);
        giaTri.put(COT_LH_PHONG_HOC, phongHoc);
        giaTri.put(COT_LH_GIANG_VIEN, giangVien);
        giaTri.put(COT_LH_NGAY_BAT_DAU, ngayBatDau);
        giaTri.put(COT_LH_NGAY_KET_THUC, ngayKetThuc);
        csdl.insert(BANG_LICH_HOC, null, giaTri);
    }

    private void themBaiTapMau(SQLiteDatabase csdl, int monHocId, String tenBaiTap, String noiDung, String hanNop, String mucDoUuTien, String trangThai) {
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_BT_MON_HOC_ID, monHocId);
        giaTri.put(COT_BT_TEN_BAI_TAP, tenBaiTap);
        giaTri.put(COT_BT_NOI_DUNG, noiDung);
        giaTri.put(COT_BT_HAN_NOP, hanNop);
        giaTri.put(COT_BT_MUC_DO_UU_TIEN, mucDoUuTien);
        giaTri.put(COT_BT_TRANG_THAI, trangThai);
        csdl.insert(BANG_BAI_TAP, null, giaTri);
    }

    // =========================================================================
    // 1. XỬ LÝ CRUD BẢNG MON HOC
    // =========================================================================

    //Thêm môn học mới (Create)
    public long themMonHoc(MonHoc monHoc) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_MH_MA_MON_HOC, monHoc.getMaMonHoc());
        giaTri.put(COT_MH_TEN_MON_HOC, monHoc.getTenMonHoc());
        giaTri.put(COT_MH_SO_TIN_CHI, monHoc.getSoTinChi());
        giaTri.put(COT_MH_GIANG_VIEN, monHoc.getGiangVien());
        giaTri.put(COT_MH_HOC_KY, monHoc.getHocKy());
        giaTri.put(COT_MH_NAM_HOC, monHoc.getNamHoc());
        return csdl.insert(BANG_MON_HOC, null, giaTri);
    }

    //Lấy danh sách tất cả môn học (Read)
    public List<MonHoc> layDanhSachMonHoc() {
        List<MonHoc> danhSach = new ArrayList<>();
        SQLiteDatabase csdl = this.getReadableDatabase();
        Cursor conTro = csdl.query(BANG_MON_HOC, null,
                null, null, null, null, COT_MH_ID + " DESC");
        if (conTro != null && conTro.moveToFirst()) {
            do {
                danhSach.add(docMonHocTuCursor(conTro));
            } while (conTro.moveToNext());
            conTro.close();
        }
        return danhSach;
    }

    //Tìm kiếm môn học theo từ khóa
    public List<MonHoc> timKiemMonHoc(String tuKhoa) {
        List<MonHoc> danhSach = new ArrayList<>();
        SQLiteDatabase csdl = this.getReadableDatabase();
        String mauTimKiem = "%" + tuKhoa + "%";
        Cursor conTro = csdl.query(BANG_MON_HOC, null,
                COT_MH_TEN_MON_HOC + " LIKE ? OR " + COT_MH_MA_MON_HOC + " LIKE ? OR " + COT_MH_GIANG_VIEN + " LIKE ?",
                new String[]{mauTimKiem, mauTimKiem, mauTimKiem},
                null, null, COT_MH_ID + " DESC");
        if (conTro != null && conTro.moveToFirst()) {
            do {
                danhSach.add(docMonHocTuCursor(conTro));
            } while (conTro.moveToNext());
            conTro.close();
        }
        return danhSach;
    }

    //Lấy thông tin môn học theo Id
    public MonHoc layMonHocTheoId(int id) {
        SQLiteDatabase csdl = this.getReadableDatabase();
        Cursor conTro = csdl.query(BANG_MON_HOC, null,
                COT_MH_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);
        MonHoc monHoc = null;
        if (conTro != null && conTro.moveToFirst()) {
            monHoc = docMonHocTuCursor(conTro);
            conTro.close();
        }
        return monHoc;
    }

    //Cập nhật thông tin môn học (Update)
    public int capNhatMonHoc(MonHoc monHoc) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_MH_MA_MON_HOC, monHoc.getMaMonHoc());
        giaTri.put(COT_MH_TEN_MON_HOC, monHoc.getTenMonHoc());
        giaTri.put(COT_MH_SO_TIN_CHI, monHoc.getSoTinChi());
        giaTri.put(COT_MH_GIANG_VIEN, monHoc.getGiangVien());
        giaTri.put(COT_MH_HOC_KY, monHoc.getHocKy());
        giaTri.put(COT_MH_NAM_HOC, monHoc.getNamHoc());
        return csdl.update(BANG_MON_HOC, giaTri, COT_MH_ID + " = ?", new String[]{String.valueOf(monHoc.getId())});
    }

    //Xóa môn học và toàn bộ lịch học, bài tập liên quan (Delete)
    public int xoaMonHoc(int monHocId) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        csdl.delete(BANG_LICH_HOC, COT_LH_MON_HOC_ID + " = ?", new String[]{String.valueOf(monHocId)});
        csdl.delete(BANG_BAI_TAP, COT_BT_MON_HOC_ID + " = ?", new String[]{String.valueOf(monHocId)});
        return csdl.delete(BANG_MON_HOC, COT_MH_ID + " = ?", new String[]{String.valueOf(monHocId)});
    }

    private MonHoc docMonHocTuCursor(Cursor conTro) {
        MonHoc monHoc = new MonHoc();
        monHoc.setId(conTro.getInt(conTro.getColumnIndexOrThrow(COT_MH_ID)));
        monHoc.setMaMonHoc(conTro.getString(conTro.getColumnIndexOrThrow(COT_MH_MA_MON_HOC)));
        monHoc.setTenMonHoc(conTro.getString(conTro.getColumnIndexOrThrow(COT_MH_TEN_MON_HOC)));
        monHoc.setSoTinChi(conTro.getInt(conTro.getColumnIndexOrThrow(COT_MH_SO_TIN_CHI)));
        monHoc.setGiangVien(conTro.getString(conTro.getColumnIndexOrThrow(COT_MH_GIANG_VIEN)));
        monHoc.setHocKy(conTro.getString(conTro.getColumnIndexOrThrow(COT_MH_HOC_KY)));
        monHoc.setNamHoc(conTro.getString(conTro.getColumnIndexOrThrow(COT_MH_NAM_HOC)));
        return monHoc;
    }

    // =========================================================================
    // 2. XỬ LÝ CRUD BẢNG LICH HOC (THỜI KHÓA BIỂU)
    // =========================================================================

    //Thêm lịch học mới
    public long themLichHoc(LichHoc lichHoc) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_LH_MON_HOC_ID, lichHoc.getMonHocId());
        giaTri.put(COT_LH_THU, lichHoc.getThu());
        giaTri.put(COT_LH_GIO_BAT_DAU, lichHoc.getGioBatDau());
        giaTri.put(COT_LH_GIO_KET_THUC, lichHoc.getGioKetThuc());
        giaTri.put(COT_LH_PHONG_HOC, lichHoc.getPhongHoc());
        giaTri.put(COT_LH_GIANG_VIEN, lichHoc.getGiangVien());
        giaTri.put(COT_LH_NGAY_BAT_DAU, lichHoc.getNgayBatDau());
        giaTri.put(COT_LH_NGAY_KET_THUC, lichHoc.getNgayKetThuc());
        return csdl.insert(BANG_LICH_HOC, null, giaTri);
    }

    //Lấy danh sách lịch học theo thứ trong tuần (kết nối bảng MonHoc)
    public List<LichHoc> layDanhSachLichHocTheoThu(String thu) {
        List<LichHoc> danhSach = new ArrayList<>();
        SQLiteDatabase csdl = this.getReadableDatabase();
        String cauTruyVan = "SELECT lh.*, mh." + COT_MH_TEN_MON_HOC + ", mh." + COT_MH_GIANG_VIEN + " AS mhGiangVien "
                + "FROM " + BANG_LICH_HOC + " lh "
                + "INNER JOIN " + BANG_MON_HOC + " mh ON lh." + COT_LH_MON_HOC_ID + " = mh." + COT_MH_ID + " "
                + "WHERE lh." + COT_LH_THU + " = ? "
                + "ORDER BY lh." + COT_LH_GIO_BAT_DAU + " ASC";
        Cursor conTro = csdl.rawQuery(cauTruyVan, new String[]{thu});
        if (conTro != null && conTro.moveToFirst()) {
            do {
                danhSach.add(docLichHocTuCursor(conTro));
            } while (conTro.moveToNext());
            conTro.close();
        }
        return danhSach;
    }

    //Lấy toàn bộ lịch học trong tuần
    public List<LichHoc> layTatCaLichHoc() {
        List<LichHoc> danhSach = new ArrayList<>();
        SQLiteDatabase csdl = this.getReadableDatabase();
        String cauTruyVan = "SELECT lh.*, mh." + COT_MH_TEN_MON_HOC + ", mh." + COT_MH_GIANG_VIEN + " AS mhGiangVien "
                + "FROM " + BANG_LICH_HOC + " lh "
                + "INNER JOIN " + BANG_MON_HOC + " mh ON lh." + COT_LH_MON_HOC_ID + " = mh." + COT_MH_ID + " "
                + "ORDER BY lh." + COT_LH_THU + " ASC, lh." + COT_LH_GIO_BAT_DAU + " ASC";
        Cursor conTro = csdl.rawQuery(cauTruyVan, null);
        if (conTro != null && conTro.moveToFirst()) {
            do {
                danhSach.add(docLichHocTuCursor(conTro));
            } while (conTro.moveToNext());
            conTro.close();
        }
        return danhSach;
    }

    //Cập nhật thông tin lịch học
    public int capNhatLichHoc(LichHoc lichHoc) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_LH_MON_HOC_ID, lichHoc.getMonHocId());
        giaTri.put(COT_LH_THU, lichHoc.getThu());
        giaTri.put(COT_LH_GIO_BAT_DAU, lichHoc.getGioBatDau());
        giaTri.put(COT_LH_GIO_KET_THUC, lichHoc.getGioKetThuc());
        giaTri.put(COT_LH_PHONG_HOC, lichHoc.getPhongHoc());
        giaTri.put(COT_LH_GIANG_VIEN, lichHoc.getGiangVien());
        giaTri.put(COT_LH_NGAY_BAT_DAU, lichHoc.getNgayBatDau());
        giaTri.put(COT_LH_NGAY_KET_THUC, lichHoc.getNgayKetThuc());
        return csdl.update(BANG_LICH_HOC, giaTri, COT_LH_ID + " = ?", new String[]{String.valueOf(lichHoc.getId())});
    }

    //Xóa lịch học theo Id
    public int xoaLichHoc(int lichHocId) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        return csdl.delete(BANG_LICH_HOC, COT_LH_ID + " = ?", new String[]{String.valueOf(lichHocId)});
    }

    private LichHoc docLichHocTuCursor(Cursor conTro) {
        LichHoc lichHoc = new LichHoc();
        lichHoc.setId(conTro.getInt(conTro.getColumnIndexOrThrow(COT_LH_ID)));
        lichHoc.setMonHocId(conTro.getInt(conTro.getColumnIndexOrThrow(COT_LH_MON_HOC_ID)));
        lichHoc.setThu(conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_THU)));
        lichHoc.setGioBatDau(conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_GIO_BAT_DAU)));
        lichHoc.setGioKetThuc(conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_GIO_KET_THUC)));
        lichHoc.setPhongHoc(conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_PHONG_HOC)));
        String giangVien = conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_GIANG_VIEN));
        int viTriCotGvMonHoc = conTro.getColumnIndex("mhGiangVien");
        if ((giangVien == null || giangVien.trim().isEmpty()) && viTriCotGvMonHoc != -1) {
            giangVien = conTro.getString(viTriCotGvMonHoc);
        }
        lichHoc.setGiangVien(giangVien);
        lichHoc.setNgayBatDau(conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_NGAY_BAT_DAU)));
        lichHoc.setNgayKetThuc(conTro.getString(conTro.getColumnIndexOrThrow(COT_LH_NGAY_KET_THUC)));
        int viTriCotTenMon = conTro.getColumnIndex(COT_MH_TEN_MON_HOC);
        if (viTriCotTenMon != -1) {
            lichHoc.setTenMonHoc(conTro.getString(viTriCotTenMon));
        }
        return lichHoc;
    }

    // =========================================================================
    // 3. XỬ LÝ CRUD BẢNG BAI TAP (CÔNG VIỆC HỌC TẬP)
    // =========================================================================

    //Thêm bài tập mới
    public long themBaiTap(BaiTap baiTap) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_BT_MON_HOC_ID, baiTap.getMonHocId());
        giaTri.put(COT_BT_TEN_BAI_TAP, baiTap.getTenBaiTap());
        giaTri.put(COT_BT_NOI_DUNG, baiTap.getNoiDung());
        giaTri.put(COT_BT_HAN_NOP, baiTap.getHanNop());
        giaTri.put(COT_BT_MUC_DO_UU_TIEN, baiTap.getMucDoUuTien());
        giaTri.put(COT_BT_TRANG_THAI, baiTap.getTrangThai());
        return csdl.insert(BANG_BAI_TAP, null, giaTri);
    }

    //Lấy danh sách bài tập theo trạng thái lọc (Tất cả / Chưa hoàn thành / Đã hoàn thành)
    public List<BaiTap> layDanhSachBaiTap(String boLocTrangThai) {
        List<BaiTap> danhSach = new ArrayList<>();
        SQLiteDatabase csdl = this.getReadableDatabase();
        StringBuilder cauTruyVan = new StringBuilder();
        cauTruyVan.append("SELECT bt.*, mh.").append(COT_MH_TEN_MON_HOC).append(" ")
                  .append("FROM ").append(BANG_BAI_TAP).append(" bt ")
                  .append("INNER JOIN ").append(BANG_MON_HOC).append(" mh ON bt.").append(COT_BT_MON_HOC_ID).append(" = mh.").append(COT_MH_ID).append(" ");

        List<String> thamSo = new ArrayList<>();
        if (boLocTrangThai != null && !boLocTrangThai.isEmpty() && !boLocTrangThai.equals("Tất cả")) {
            cauTruyVan.append("WHERE bt.").append(COT_BT_TRANG_THAI).append(" = ? ");
            thamSo.add(boLocTrangThai);
        }
        cauTruyVan.append("ORDER BY bt.").append(COT_BT_ID).append(" DESC");

        Cursor conTro = csdl.rawQuery(cauTruyVan.toString(), thamSo.toArray(new String[0]));
        if (conTro != null && conTro.moveToFirst()) {
            do {
                danhSach.add(docBaiTapTuCursor(conTro));
            } while (conTro.moveToNext());
            conTro.close();
        }
        return danhSach;
    }

    //Cập nhật toàn bộ thông tin bài tập
    public int capNhatBaiTap(BaiTap baiTap) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_BT_MON_HOC_ID, baiTap.getMonHocId());
        giaTri.put(COT_BT_TEN_BAI_TAP, baiTap.getTenBaiTap());
        giaTri.put(COT_BT_NOI_DUNG, baiTap.getNoiDung());
        giaTri.put(COT_BT_HAN_NOP, baiTap.getHanNop());
        giaTri.put(COT_BT_MUC_DO_UU_TIEN, baiTap.getMucDoUuTien());
        giaTri.put(COT_BT_TRANG_THAI, baiTap.getTrangThai());
        return csdl.update(BANG_BAI_TAP, giaTri, COT_BT_ID + " = ?", new String[]{String.valueOf(baiTap.getId())});
    }

    //Cập nhật nhanh trạng thái hoàn thành của bài tập
    public int capNhatTrangThaiBaiTap(int baiTapId, String trangThaiMoi) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        ContentValues giaTri = new ContentValues();
        giaTri.put(COT_BT_TRANG_THAI, trangThaiMoi);
        return csdl.update(BANG_BAI_TAP, giaTri, COT_BT_ID + " = ?", new String[]{String.valueOf(baiTapId)});
    }

    //Xóa bài tập theo Id
    public int xoaBaiTap(int baiTapId) {
        SQLiteDatabase csdl = this.getWritableDatabase();
        return csdl.delete(BANG_BAI_TAP, COT_BT_ID + " = ?", new String[]{String.valueOf(baiTapId)});
    }

    private BaiTap docBaiTapTuCursor(Cursor conTro) {
        BaiTap baiTap = new BaiTap();
        baiTap.setId(conTro.getInt(conTro.getColumnIndexOrThrow(COT_BT_ID)));
        baiTap.setMonHocId(conTro.getInt(conTro.getColumnIndexOrThrow(COT_BT_MON_HOC_ID)));
        baiTap.setTenBaiTap(conTro.getString(conTro.getColumnIndexOrThrow(COT_BT_TEN_BAI_TAP)));
        baiTap.setNoiDung(conTro.getString(conTro.getColumnIndexOrThrow(COT_BT_NOI_DUNG)));
        baiTap.setHanNop(conTro.getString(conTro.getColumnIndexOrThrow(COT_BT_HAN_NOP)));
        baiTap.setMucDoUuTien(conTro.getString(conTro.getColumnIndexOrThrow(COT_BT_MUC_DO_UU_TIEN)));
        baiTap.setTrangThai(conTro.getString(conTro.getColumnIndexOrThrow(COT_BT_TRANG_THAI)));
        int viTriCotTenMon = conTro.getColumnIndex(COT_MH_TEN_MON_HOC);
        if (viTriCotTenMon != -1) {
            baiTap.setTenMonHoc(conTro.getString(viTriCotTenMon));
        }
        return baiTap;
    }

    // =========================================================================
    // 4. CÁC HÀM ĐẾM SỐ LƯỢNG TỔNG HỢP TRÊN TRANG CHỦ
    // =========================================================================

    //Đếm tổng số môn học
    public int demSoMonHoc() {
        SQLiteDatabase csdl = this.getReadableDatabase();
        Cursor conTro = csdl.rawQuery("SELECT COUNT(*) FROM " + BANG_MON_HOC, null);
        int soLuong = 0;
        if (conTro != null && conTro.moveToFirst()) {
            soLuong = conTro.getInt(0);
            conTro.close();
        }
        return soLuong;
    }

    //Đếm số ca học theo thứ trong tuần
    public int demSoLichHocTheoThu(String thu) {
        SQLiteDatabase csdl = this.getReadableDatabase();
        String cauTruyVan = "SELECT COUNT(*) FROM " + BANG_LICH_HOC + " WHERE " + COT_LH_THU + " = ?";
        Cursor conTro = csdl.rawQuery(cauTruyVan, new String[]{thu});
        int soLuong = 0;
        if (conTro != null && conTro.moveToFirst()) {
            soLuong = conTro.getInt(0);
            conTro.close();
        }
        return soLuong;
    }

    //Đếm số bài tập chưa hoàn thành
    public int demSoBaiTapChuaXong() {
        SQLiteDatabase csdl = this.getReadableDatabase();
        String cauTruyVan = "SELECT COUNT(*) FROM " + BANG_BAI_TAP + " WHERE " + COT_BT_TRANG_THAI + " = ?";
        Cursor conTro = csdl.rawQuery(cauTruyVan, new String[]{"Chưa hoàn thành"});
        int soLuong = 0;
        if (conTro != null && conTro.moveToFirst()) {
            soLuong = conTro.getInt(0);
            conTro.close();
        }
        return soLuong;
    }
}
