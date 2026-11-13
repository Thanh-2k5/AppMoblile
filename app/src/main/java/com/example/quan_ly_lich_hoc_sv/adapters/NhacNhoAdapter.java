package com.example.quan_ly_lich_hoc_sv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.NhacNho;

import java.util.List;

public class NhacNhoAdapter extends RecyclerView.Adapter<NhacNhoAdapter.NhacNhoViewHolder> {

    //Giao diện lắng nghe sự kiện Sửa, Xóa và Bật/Tắt nhắc nhở
    public interface OnNhacNhoActionListener {
        void onEdit(NhacNho nhacNho);
        void onDelete(NhacNho nhacNho);
        void onToggleStatus(NhacNho nhacNho);
    }

    //Khai báo danh sách nhắc nhở và biến lắng nghe sự kiện
    private List<NhacNho> list;
    private final OnNhacNhoActionListener listener;

    public NhacNhoAdapter(List<NhacNho> list, OnNhacNhoActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    //Cập nhật danh sách nhắc nhở
    public void updateData(List<NhacNho> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NhacNhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gắn layout item_nhac_nho cho từng dòng nhắc nhở
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhac_nho, parent, false);
        return new NhacNhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhacNhoViewHolder holder, int position) {
        NhacNho nn = list.get(position);

        //Hiển thị tiêu đề, nội dung và thời gian nhắc nhở
        holder.tvReminderTitle.setText(nn.getTieuDe());
        if (nn.getNoiDung() != null && !nn.getNoiDung().trim().isEmpty()) {
            holder.tvReminderContent.setVisibility(View.VISIBLE);
            holder.tvReminderContent.setText(nn.getNoiDung());
        } else {
            holder.tvReminderContent.setVisibility(View.GONE);
        }
        holder.tvReminderTime.setText(nn.getThoiGianNhac() != null ? nn.getThoiGianNhac() : "");

        //Hiển thị trạng thái Đang bật / Đã tắt
        String trangThai = nn.getTrangThai() != null ? nn.getTrangThai() : "Đang bật";
        holder.tvReminderStatusChip.setText(trangThai);
        if ("Đang bật".equalsIgnoreCase(trangThai)) {
            holder.tvReminderStatusChip.setBackgroundResource(R.drawable.bg_chip_completed);
        } else {
            holder.tvReminderStatusChip.setBackgroundResource(R.drawable.bg_chip_unfinished);
        }

        //Xử lý sự kiện khi nhấn đổi trạng thái, sửa hoặc xóa nhắc nhở
        holder.tvReminderStatusChip.setOnClickListener(v -> {
            if (listener != null) listener.onToggleStatus(nn);
        });
        holder.btnEditNhacNho.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(nn);
        });
        holder.cardNhacNho.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(nn);
        });
        holder.btnDeleteNhacNho.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(nn);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class NhacNhoViewHolder extends RecyclerView.ViewHolder {
        //Khai báo các biến giao diện trong một thẻ nhắc nhở
        RelativeLayout cardNhacNho;
        TextView tvReminderTitle, tvReminderContent, tvReminderTime, tvReminderStatusChip;
        TextView btnEditNhacNho, btnDeleteNhacNho;

        public NhacNhoViewHolder(@NonNull View itemView) {
            super(itemView);
            //Tham chiếu Id đến các biến giao diện
            cardNhacNho = itemView.findViewById(R.id.cardNhacNho);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvReminderContent = itemView.findViewById(R.id.tvReminderContent);
            tvReminderTime = itemView.findViewById(R.id.tvReminderTime);
            tvReminderStatusChip = itemView.findViewById(R.id.tvReminderStatusChip);
            btnEditNhacNho = itemView.findViewById(R.id.btnEditNhacNho);
            btnDeleteNhacNho = itemView.findViewById(R.id.btnDeleteNhacNho);
        }
    }
}
