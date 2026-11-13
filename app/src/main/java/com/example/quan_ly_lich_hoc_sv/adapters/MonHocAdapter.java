package com.example.quan_ly_lich_hoc_sv.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.MonHoc;

import java.util.List;

public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.MonHocViewHolder> {

    //Giao diện lắng nghe sự kiện Sửa và Xóa môn học
    public interface OnMonHocActionListener {
        void onEdit(MonHoc monHoc);
        void onDelete(MonHoc monHoc);
    }

    //Khai báo danh sách môn học và biến lắng nghe sự kiện
    private List<MonHoc> list;
    private final OnMonHocActionListener listener;

    public MonHocAdapter(List<MonHoc> list, OnMonHocActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    //Hàm cập nhật lại dữ liệu danh sách môn học và làm mới giao diện
    public void updateData(List<MonHoc> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gắn layout item_mon_hoc cho từng dòng trong RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mon_hoc, parent, false);
        return new MonHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonHocViewHolder holder, int position) {
        //Lấy đối tượng môn học tại vị trí hiện tại
        MonHoc mh = list.get(position);

        //Hiển thị thông tin môn học lên các TextView
        holder.tvTenMonHoc.setText(mh.getTenMonHoc());
        holder.tvMaVaTinChi.setText("Mã: " + mh.getMaMonHoc() + " | Tín chỉ: " + mh.getSoTinChi());
        String gv = (mh.getGiangVien() != null && !mh.getGiangVien().isEmpty()) ? mh.getGiangVien() : "Chưa cập nhật";
        holder.tvGiangVien.setText("GV: " + gv);

        String hocKyText = "Học kỳ: " + (mh.getHocKy() != null ? mh.getHocKy() : "Học kỳ 1");
        if (mh.getNamHoc() != null && !mh.getNamHoc().isEmpty()) {
            hocKyText += " (" + mh.getNamHoc() + ")";
        }
        holder.tvHocKyNamHoc.setText(hocKyText);

        //Thiết lập màu nền nổi bật cho thẻ môn học đầu tiên
        if (position == 0) {
            holder.cardMonHoc.setBackgroundResource(R.drawable.bg_blue_card_item);
            holder.tvTenMonHoc.setTextColor(Color.WHITE);
            holder.tvMaVaTinChi.setTextColor(Color.parseColor("#D0E2F2"));
            holder.tvGiangVien.setTextColor(Color.parseColor("#D0E2F2"));
            holder.tvHocKyNamHoc.setTextColor(Color.parseColor("#D0E2F2"));
        } else {
            holder.cardMonHoc.setBackgroundResource(R.drawable.bg_light_card);
            holder.tvTenMonHoc.setTextColor(Color.parseColor("#0F2537"));
            holder.tvMaVaTinChi.setTextColor(Color.parseColor("#5A6E85"));
            holder.tvGiangVien.setTextColor(Color.parseColor("#5A6E85"));
            holder.tvHocKyNamHoc.setTextColor(Color.parseColor("#5A6E85"));
        }

        //Xử lý sự kiện khi nhấn nút Sửa hoặc chạm vào thẻ môn học
        holder.btnEditMonHoc.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(mh);
        });
        holder.cardMonHoc.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(mh);
        });

        //Xử lý sự kiện khi nhấn nút Xóa môn học
        holder.btnDeleteMonHoc.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(mh);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MonHocViewHolder extends RecyclerView.ViewHolder {
        //Khai báo các biến giao diện trong một thẻ môn học
        RelativeLayout cardMonHoc;
        TextView tvTenMonHoc, tvMaVaTinChi, tvGiangVien, tvHocKyNamHoc;
        TextView btnEditMonHoc, btnDeleteMonHoc;

        public MonHocViewHolder(@NonNull View itemView) {
            super(itemView);
            //Tham chiếu Id đến các biến giao diện
            cardMonHoc = itemView.findViewById(R.id.cardMonHoc);
            tvTenMonHoc = itemView.findViewById(R.id.tvTenMonHoc);
            tvMaVaTinChi = itemView.findViewById(R.id.tvMaVaTinChi);
            tvGiangVien = itemView.findViewById(R.id.tvGiangVien);
            tvHocKyNamHoc = itemView.findViewById(R.id.tvHocKyNamHoc);
            btnEditMonHoc = itemView.findViewById(R.id.btnEditMonHoc);
            btnDeleteMonHoc = itemView.findViewById(R.id.btnDeleteMonHoc);
        }
    }
}
