package com.example.quan_ly_lich_hoc_sv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.LichHoc;

import java.util.List;

public class LichHocAdapter extends RecyclerView.Adapter<LichHocAdapter.LichHocViewHolder> {

    //Giao diện lắng nghe sự kiện Sửa và Xóa lịch học
    public interface OnLichHocActionListener {
        void onEdit(LichHoc lichHoc);
        void onDelete(LichHoc lichHoc);
    }

    //Khai báo danh sách lịch học và mảng màu nền thẻ
    private List<LichHoc> list;
    private final OnLichHocActionListener listener;
    private static final int[] CARD_BACKGROUNDS = {
            R.drawable.bg_blue_card_item,
            R.drawable.bg_card_orange,
            R.drawable.bg_card_green
    };

    public LichHocAdapter(List<LichHoc> list, OnLichHocActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    //Cập nhật danh sách lịch học hiển thị
    public void updateData(List<LichHoc> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LichHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gắn layout item_lich_hoc cho từng mục thời khóa biểu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_hoc, parent, false);
        return new LichHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichHocViewHolder holder, int position) {
        LichHoc lh = list.get(position);

        //Đổi màu nền thẻ luân phiên xanh - cam - lá
        holder.cardLichHoc.setBackgroundResource(CARD_BACKGROUNDS[position % CARD_BACKGROUNDS.length]);

        //Hiển thị thông tin ca học lên giao diện
        holder.tvScheduleCourseName.setText(lh.getTenMonHoc() != null ? lh.getTenMonHoc() : "Môn học");
        holder.tvScheduleTime.setText(lh.getGioBatDau() + " – " + lh.getGioKetThuc());
        String phong = (lh.getPhongHoc() != null && !lh.getPhongHoc().isEmpty()) ? lh.getPhongHoc() : "Chưa xếp";
        holder.tvScheduleRoom.setText("Phòng: " + phong);
        String gv = (lh.getGiangVien() != null && !lh.getGiangVien().isEmpty()) ? lh.getGiangVien() : "Chưa cập nhật";
        holder.tvScheduleTeacher.setText("GV: " + gv);

        //Xử lý sự kiện khi nhấn Sửa hoặc Xóa lịch học
        holder.btnEditLichHoc.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(lh);
        });
        holder.cardLichHoc.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(lh);
        });
        holder.btnDeleteLichHoc.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(lh);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class LichHocViewHolder extends RecyclerView.ViewHolder {
        //Khai báo các biến giao diện trong một thẻ lịch học
        RelativeLayout cardLichHoc;
        TextView tvScheduleCourseName, tvScheduleTime, tvScheduleRoom, tvScheduleTeacher;
        TextView btnEditLichHoc, btnDeleteLichHoc;

        public LichHocViewHolder(@NonNull View itemView) {
            super(itemView);
            //Tham chiếu Id đến các biến giao diện
            cardLichHoc = itemView.findViewById(R.id.cardLichHoc);
            tvScheduleCourseName = itemView.findViewById(R.id.tvScheduleCourseName);
            tvScheduleTime = itemView.findViewById(R.id.tvScheduleTime);
            tvScheduleRoom = itemView.findViewById(R.id.tvScheduleRoom);
            tvScheduleTeacher = itemView.findViewById(R.id.tvScheduleTeacher);
            btnEditLichHoc = itemView.findViewById(R.id.btnEditLichHoc);
            btnDeleteLichHoc = itemView.findViewById(R.id.btnDeleteLichHoc);
        }
    }
}
