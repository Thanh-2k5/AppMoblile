package com.example.quan_ly_lich_hoc_sv.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;

import java.util.List;

public class MonHocAdapter extends ArrayAdapter<MonHoc> {

    //Giao diện lắng nghe sự kiện Sửa và Xóa môn học
    public interface OnMonHocActionListener {
        void khiSua(MonHoc monHoc);
        void khiXoa(MonHoc monHoc);
    }

    //Khai báo danh sách môn học và biến lắng nghe sự kiện
    private final List<MonHoc> danhSach;
    private final OnMonHocActionListener boLangNghe;

    public MonHocAdapter(Context nguCanh, List<MonHoc> danhSach, OnMonHocActionListener boLangNghe) {
        super(nguCanh, R.layout.item_mon_hoc, danhSach);
        this.danhSach = danhSach;
        this.boLangNghe = boLangNghe;
    }

    //Hàm cập nhật lại dữ liệu danh sách môn học trên ListView
    public void capNhatDuLieu(List<MonHoc> danhSachMoi) {
        danhSach.clear();
        danhSach.addAll(danhSachMoi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int viTri, @Nullable View dongGiaoDien, @NonNull ViewGroup khungCha) {
        //Gắn layout item_mon_hoc cho từng dòng trong ListView
        if (dongGiaoDien == null) {
            dongGiaoDien = LayoutInflater.from(getContext()).inflate(R.layout.item_mon_hoc, khungCha, false);
        }

        //Lấy đối tượng môn học tại vị trí hiện tại
        final MonHoc monHoc = danhSach.get(viTri);

        //Tham chiếu Id đến các biến giao diện trong dòng ListView
        RelativeLayout theMonHoc = dongGiaoDien.findViewById(R.id.cardMonHoc);
        TextView tvTenMonHoc = dongGiaoDien.findViewById(R.id.tvTenMonHoc);
        TextView tvMaVaTinChi = dongGiaoDien.findViewById(R.id.tvMaVaTinChi);
        TextView tvGiangVien = dongGiaoDien.findViewById(R.id.tvGiangVien);
        TextView tvHocKyNamHoc = dongGiaoDien.findViewById(R.id.tvHocKyNamHoc);
        TextView btnSuaMonHoc = dongGiaoDien.findViewById(R.id.btnEditMonHoc);
        TextView btnXoaMonHoc = dongGiaoDien.findViewById(R.id.btnDeleteMonHoc);

        //Hiển thị thông tin môn học lên các TextView
        tvTenMonHoc.setText(monHoc.getTenMonHoc());
        tvMaVaTinChi.setText("Mã: " + monHoc.getMaMonHoc() + " | Tín chỉ: " + monHoc.getSoTinChi());
        String tenGiangVien = (monHoc.getGiangVien() != null && !monHoc.getGiangVien().isEmpty()) ? monHoc.getGiangVien() : "Chưa cập nhật";
        tvGiangVien.setText("GV: " + tenGiangVien);

        String chuoiHocKy = "Học kỳ: " + (monHoc.getHocKy() != null ? monHoc.getHocKy() : "Học kỳ 1");
        if (monHoc.getNamHoc() != null && !monHoc.getNamHoc().isEmpty()) {
            chuoiHocKy += " (" + monHoc.getNamHoc() + ")";
        }
        tvHocKyNamHoc.setText(chuoiHocKy);

        //Thiết lập màu nền nổi bật cho dòng môn học đầu tiên
        if (viTri == 0) {
            theMonHoc.setBackgroundResource(R.drawable.bg_blue_card_item);
            tvTenMonHoc.setTextColor(Color.WHITE);
            tvMaVaTinChi.setTextColor(Color.parseColor("#D0E2F2"));
            tvGiangVien.setTextColor(Color.parseColor("#D0E2F2"));
            tvHocKyNamHoc.setTextColor(Color.parseColor("#D0E2F2"));
        } else {
            theMonHoc.setBackgroundResource(R.drawable.bg_light_card);
            tvTenMonHoc.setTextColor(Color.parseColor("#0F2537"));
            tvMaVaTinChi.setTextColor(Color.parseColor("#5A6E85"));
            tvGiangVien.setTextColor(Color.parseColor("#5A6E85"));
            tvHocKyNamHoc.setTextColor(Color.parseColor("#5A6E85"));
        }

        //Xử lý sự kiện khi nhấn nút Sửa
        btnSuaMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiSua(monHoc);
            }
        });

        //Xử lý sự kiện khi nhấn nút Xóa
        btnXoaMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boLangNghe != null) boLangNghe.khiXoa(monHoc);
            }
        });

        return dongGiaoDien;
    }
}
