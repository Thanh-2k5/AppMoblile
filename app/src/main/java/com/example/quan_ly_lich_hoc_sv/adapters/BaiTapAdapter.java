package com.example.quan_ly_lich_hoc_sv.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.BaiTap;

import java.util.List;

public class BaiTapAdapter extends ArrayAdapter<BaiTap> {

    //Giao diện lắng nghe sự kiện Sửa, Xóa và Đổi trạng thái bài tập
    public interface OnBaiTapActionListener {
        void khiSua(BaiTap baiTap);
        void khiXoa(BaiTap baiTap);
        void khiDoiTrangThai(BaiTap baiTap);
    }

    //Khai báo danh sách bài tập và biến lắng nghe sự kiện
    private final List<BaiTap> danhSach;
    private final OnBaiTapActionListener boLangNghe;

    public BaiTapAdapter(Context nguCanh, List<BaiTap> danhSach, OnBaiTapActionListener boLangNghe) {
        super(nguCanh, R.layout.item_bai_tap, danhSach);
        this.danhSach = danhSach;
        this.boLangNghe = boLangNghe;
    }

    //Cập nhật lại dữ liệu danh sách bài tập trên ListView
    public void capNhatDuLieu(List<BaiTap> danhSachMoi) {
        danhSach.clear();
        danhSach.addAll(danhSachMoi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int viTri, @Nullable View dongGiaoDien, @NonNull ViewGroup khungCha) {
        //Gắn layout item_bai_tap cho từng dòng trong ListView
        if (dongGiaoDien == null) {
            dongGiaoDien = LayoutInflater.from(getContext()).inflate(R.layout.item_bai_tap, khungCha, false);
        }

        //Lấy đối tượng bài tập tại vị trí hiện tại
        final BaiTap baiTap = danhSach.get(viTri);

        //Tham chiếu Id đến các biến giao diện
        TextView tvTenBaiTap = dongGiaoDien.findViewById(R.id.tvTaskTitle);
        TextView tvMonHocBaiTap = dongGiaoDien.findViewById(R.id.tvTaskCourse);
        TextView tvMoTaBaiTap = dongGiaoDien.findViewById(R.id.tvTaskDesc);
        TextView tvHanNopBaiTap = dongGiaoDien.findViewById(R.id.tvTaskDeadline);
        TextView tvMucDoUuTien = dongGiaoDien.findViewById(R.id.tvTaskPriority);
        TextView tvNhanTrangThai = dongGiaoDien.findViewById(R.id.tvTaskStatusChip);
        TextView btnSuaBaiTap = dongGiaoDien.findViewById(R.id.btnEditBaiTap);
        TextView btnXoaBaiTap = dongGiaoDien.findViewById(R.id.btnDeleteBaiTap);

        //Hiển thị tiêu đề, môn học, nội dung và hạn nộp
        tvTenBaiTap.setText(baiTap.getTenBaiTap());
        tvMonHocBaiTap.setText(baiTap.getTenMonHoc() != null ? baiTap.getTenMonHoc() : "");

        if (baiTap.getNoiDung() != null && !baiTap.getNoiDung().trim().isEmpty()) {
            tvMoTaBaiTap.setVisibility(View.VISIBLE);
            tvMoTaBaiTap.setText(baiTap.getNoiDung());
        } else {
            tvMoTaBaiTap.setVisibility(View.GONE);
        }

        tvHanNopBaiTap.setText("Hạn: " + (baiTap.getHanNop() != null ? baiTap.getHanNop() : "Chưa có"));

        //Thiết lập màu chữ theo mức độ ưu tiên (Cao - Trung bình - Thấp)
        String uuTien = baiTap.getMucDoUuTien() != null ? baiTap.getMucDoUuTien() : "Trung bình";
        tvMucDoUuTien.setText("Ưu tiên: " + uuTien);
        if ("Cao".equalsIgnoreCase(uuTien)) {
            tvMucDoUuTien.setTextColor(Color.parseColor("#E53935"));
        } else if ("Trung bình".equalsIgnoreCase(uuTien)) {
            tvMucDoUuTien.setTextColor(Color.parseColor("#D97706"));
        } else {
            tvMucDoUuTien.setTextColor(Color.parseColor("#2563EB"));
        }

        //Hiển thị nhãn trạng thái hoàn thành
        String trangThai = baiTap.getTrangThai() != null ? baiTap.getTrangThai() : "Chưa hoàn thành";
        tvNhanTrangThai.setText(trangThai);
        if ("Đã hoàn thành".equalsIgnoreCase(trangThai)) {
            tvNhanTrangThai.setBackgroundResource(R.drawable.bg_chip_completed);
        } else {
            tvNhanTrangThai.setBackgroundResource(R.drawable.bg_chip_unfinished);
        }

        //Xử lý sự kiện đổi trạng thái, sửa và xóa bài tập
        tvNhanTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiDoiTrangThai(baiTap);
            }
        });

        btnSuaBaiTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiSua(baiTap);
            }
        });

        btnXoaBaiTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiXoa(baiTap);
            }
        });

        return dongGiaoDien;
    }
}
