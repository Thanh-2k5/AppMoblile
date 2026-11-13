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
import com.example.quan_ly_lich_hoc_sv.models.BaiTap;

import java.util.List;

public class BaiTapAdapter extends RecyclerView.Adapter<BaiTapAdapter.BaiTapViewHolder> {

    //Giao diện lắng nghe sự kiện Sửa, Xóa và Đổi trạng thái bài tập
    public interface OnBaiTapActionListener {
        void onEdit(BaiTap baiTap);
        void onDelete(BaiTap baiTap);
        void onToggleStatus(BaiTap baiTap);
    }

    //Khai báo danh sách bài tập và biến lắng nghe sự kiện
    private List<BaiTap> list;
    private final OnBaiTapActionListener listener;

    public BaiTapAdapter(List<BaiTap> list, OnBaiTapActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    //Cập nhật lại dữ liệu danh sách bài tập
    public void updateData(List<BaiTap> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaiTapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gắn layout item_bai_tap cho từng dòng công việc
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bai_tap, parent, false);
        return new BaiTapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiTapViewHolder holder, int position) {
        BaiTap bt = list.get(position);

        //Hiển thị tiêu đề, môn học, nội dung và hạn nộp
        holder.tvTaskTitle.setText(bt.getTenBaiTap());
        holder.tvTaskCourse.setText(bt.getTenMonHoc() != null ? bt.getTenMonHoc() : "");

        if (bt.getNoiDung() != null && !bt.getNoiDung().trim().isEmpty()) {
            holder.tvTaskDesc.setVisibility(View.VISIBLE);
            holder.tvTaskDesc.setText(bt.getNoiDung());
        } else {
            holder.tvTaskDesc.setVisibility(View.GONE);
        }

        holder.tvTaskDeadline.setText("Hạn: " + (bt.getHanNop() != null ? bt.getHanNop() : "Chưa có"));

        //Thiết lập màu chữ theo mức độ ưu tiên (Cao - Trung bình - Thấp)
        String uuTien = bt.getMucDoUuTien() != null ? bt.getMucDoUuTien() : "Trung bình";
        holder.tvTaskPriority.setText(uuTien);
        if ("Cao".equalsIgnoreCase(uuTien)) {
            holder.tvTaskPriority.setTextColor(Color.parseColor("#E53935"));
        } else if ("Trung bình".equalsIgnoreCase(uuTien)) {
            holder.tvTaskPriority.setTextColor(Color.parseColor("#D97706"));
        } else {
            holder.tvTaskPriority.setTextColor(Color.parseColor("#2563EB"));
        }

        //Hiển thị nhãn trạng thái hoàn thành
        String trangThai = bt.getTrangThai() != null ? bt.getTrangThai() : "Chưa hoàn thành";
        holder.tvTaskStatusChip.setText(trangThai);
        if ("Đã hoàn thành".equalsIgnoreCase(trangThai)) {
            holder.tvTaskStatusChip.setBackgroundResource(R.drawable.bg_chip_completed);
        } else {
            holder.tvTaskStatusChip.setBackgroundResource(R.drawable.bg_chip_unfinished);
        }

        //Xử lý sự kiện đổi trạng thái, sửa và xóa bài tập
        holder.tvTaskStatusChip.setOnClickListener(v -> {
            if (listener != null) listener.onToggleStatus(bt);
        });
        holder.btnEditBaiTap.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(bt);
        });
        holder.cardBaiTap.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(bt);
        });
        holder.btnDeleteBaiTap.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(bt);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class BaiTapViewHolder extends RecyclerView.ViewHolder {
        //Khai báo các biến giao diện trong một thẻ bài tập
        RelativeLayout cardBaiTap;
        TextView tvTaskTitle, tvTaskCourse, tvTaskDesc, tvTaskDeadline, tvTaskPriority, tvTaskStatusChip;
        TextView btnEditBaiTap, btnDeleteBaiTap;

        public BaiTapViewHolder(@NonNull View itemView) {
            super(itemView);
            //Tham chiếu Id đến các biến giao diện
            cardBaiTap = itemView.findViewById(R.id.cardBaiTap);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskCourse = itemView.findViewById(R.id.tvTaskCourse);
            tvTaskDesc = itemView.findViewById(R.id.tvTaskDesc);
            tvTaskDeadline = itemView.findViewById(R.id.tvTaskDeadline);
            tvTaskPriority = itemView.findViewById(R.id.tvTaskPriority);
            tvTaskStatusChip = itemView.findViewById(R.id.tvTaskStatusChip);
            btnEditBaiTap = itemView.findViewById(R.id.btnEditBaiTap);
            btnDeleteBaiTap = itemView.findViewById(R.id.btnDeleteBaiTap);
        }
    }
}
