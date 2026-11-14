package com.example.quan_ly_lich_hoc_sv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;

import java.util.List;

public class LichHocAdapter extends ArrayAdapter<LichHoc> {

    //Giao diện lắng nghe sự kiện Sửa và Xóa lịch học
    public interface OnLichHocActionListener {
        void khiSua(LichHoc lichHoc);
        void khiXoa(LichHoc lichHoc);
    }

    //Khai báo danh sách lịch học và mảng màu nền thẻ
    private final List<LichHoc> danhSach;
    private final OnLichHocActionListener boLangNghe;
    private static final int[] MANG_MAU_THE = {
            R.drawable.bg_blue_card_item,
            R.drawable.bg_card_orange,
            R.drawable.bg_card_green
    };

    public LichHocAdapter(Context nguCanh, List<LichHoc> danhSach, OnLichHocActionListener boLangNghe) {
        super(nguCanh, R.layout.item_lich_hoc, danhSach);
        this.danhSach = danhSach;
        this.boLangNghe = boLangNghe;
    }

    //Hàm cập nhật danh sách lịch học hiển thị trên ListView
    public void capNhatDuLieu(List<LichHoc> danhSachMoi) {
        danhSach.clear();
        danhSach.addAll(danhSachMoi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int viTri, @Nullable View dongGiaoDien, @NonNull ViewGroup khungCha) {
        //Gắn layout item_lich_hoc cho từng mục trong ListView
        if (dongGiaoDien == null) {
            dongGiaoDien = LayoutInflater.from(getContext()).inflate(R.layout.item_lich_hoc, khungCha, false);
        }

        //Lấy đối tượng lịch học tại vị trí hiện tại
        final LichHoc lichHoc = danhSach.get(viTri);

        //Tham chiếu Id đến các biến giao diện
        RelativeLayout theLichHoc = dongGiaoDien.findViewById(R.id.cardLichHoc);
        TextView tvTenMonLichHoc = dongGiaoDien.findViewById(R.id.tvScheduleCourseName);
        TextView tvGioHoc = dongGiaoDien.findViewById(R.id.tvScheduleTime);
        TextView tvPhongHoc = dongGiaoDien.findViewById(R.id.tvScheduleRoom);
        TextView tvGiangVien = dongGiaoDien.findViewById(R.id.tvScheduleTeacher);
        TextView btnSuaLichHoc = dongGiaoDien.findViewById(R.id.btnEditLichHoc);
        TextView btnXoaLichHoc = dongGiaoDien.findViewById(R.id.btnDeleteLichHoc);

        //Đổi màu nền thẻ luân phiên xanh - cam - lá
        theLichHoc.setBackgroundResource(MANG_MAU_THE[viTri % MANG_MAU_THE.length]);

        //Hiển thị thông tin ca học lên giao diện
        tvTenMonLichHoc.setText(lichHoc.getTenMonHoc() != null ? lichHoc.getTenMonHoc() : "Môn học");
        tvGioHoc.setText(lichHoc.getGioBatDau() + " – " + lichHoc.getGioKetThuc());
        String phong = (lichHoc.getPhongHoc() != null && !lichHoc.getPhongHoc().isEmpty()) ? lichHoc.getPhongHoc() : "Chưa xếp";
        tvPhongHoc.setText("Phòng: " + phong);
        String giangVien = (lichHoc.getGiangVien() != null && !lichHoc.getGiangVien().isEmpty()) ? lichHoc.getGiangVien() : "Chưa cập nhật";
        tvGiangVien.setText("GV: " + giangVien);

        //Xử lý sự kiện khi nhấn nút Sửa hoặc Xóa lịch học
        btnSuaLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiSua(lichHoc);
            }
        });

        btnXoaLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiXoa(lichHoc);
            }
        });

        return dongGiaoDien;
    }
}
