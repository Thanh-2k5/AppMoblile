package com.example.quan_ly_lich_hoc_sv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_lich_hoc_sv.R;
import com.example.quan_ly_lich_hoc_sv.models.LichThi;

import java.util.List;

public class LichThiAdapter extends RecyclerView.Adapter<LichThiAdapter.LichThiViewHolder> {

    //Giao diện lắng nghe sự kiện Sửa và Xóa lịch thi
    public interface OnLichThiActionListener {
        void onEdit(LichThi lichThi);
        void onDelete(LichThi lichThi);
    }

    //Khai báo danh sách lịch thi và biến lắng nghe sự kiện
    private List<LichThi> list;
    private final OnLichThiActionListener listener;

    public LichThiAdapter(List<LichThi> list, OnLichThiActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    //Cập nhật dữ liệu danh sách lịch thi
    public void updateData(List<LichThi> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LichThiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gắn layout item_lich_thi cho từng mục trong danh sách
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_thi, parent, false);
        return new LichThiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichThiViewHolder holder, int position) {
        LichThi lt = list.get(position);

        //Hiển thị tên môn thi, ngày thi, giờ thi, phòng thi và hình thức thi
        holder.tvExamSubjectName.setText(lt.getTenMonHoc() != null ? lt.getTenMonHoc() : "Môn thi");
        holder.tvExamDate.setText("Ngày thi: " + lt.getNgayThi());

        String thoiLuong = (lt.getThoiLuong() != null && !lt.getThoiLuong().isEmpty())
                ? " (" + lt.getThoiLuong() + ")" : "";
        holder.tvExamTime.setText("Giờ thi: " + lt.getGioThi() + thoiLuong);

        String phong = (lt.getPhongThi() != null && !lt.getPhongThi().isEmpty()) ? lt.getPhongThi() : "Chưa xếp";
        String hinhThuc = (lt.getHinhThucThi() != null && !lt.getHinhThucThi().isEmpty()) ? lt.getHinhThucThi() : "Tự luận";
        holder.tvExamRoomFormat.setText("Phòng thi: " + phong + "   Hình thức: " + hinhThuc);

        if (lt.getGhiChu() != null && !lt.getGhiChu().trim().isEmpty()) {
            holder.tvExamNotes.setVisibility(View.VISIBLE);
            holder.tvExamNotes.setText("Ghi chú: " + lt.getGhiChu());
        } else {
            holder.tvExamNotes.setVisibility(View.GONE);
        }

        //Xử lý sự kiện khi nhấn nút Sửa hoặc Xóa lịch thi
        holder.btnEditLichThi.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(lt);
        });
        holder.cardLichThi.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(lt);
        });
        holder.btnDeleteLichThi.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(lt);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class LichThiViewHolder extends RecyclerView.ViewHolder {
        //Khai báo các biến giao diện trong một thẻ lịch thi
        RelativeLayout cardLichThi;
        TextView tvExamSubjectName, tvExamDate, tvExamTime, tvExamRoomFormat, tvExamNotes;
        TextView btnEditLichThi, btnDeleteLichThi;

        public LichThiViewHolder(@NonNull View itemView) {
            super(itemView);
            //Tham chiếu Id đến các biến giao diện
            cardLichThi = itemView.findViewById(R.id.cardLichThi);
            tvExamSubjectName = itemView.findViewById(R.id.tvExamSubjectName);
            tvExamDate = itemView.findViewById(R.id.tvExamDate);
            tvExamTime = itemView.findViewById(R.id.tvExamTime);
            tvExamRoomFormat = itemView.findViewById(R.id.tvExamRoomFormat);
            tvExamNotes = itemView.findViewById(R.id.tvExamNotes);
            btnEditLichThi = itemView.findViewById(R.id.btnEditLichThi);
            btnDeleteLichThi = itemView.findViewById(R.id.btnDeleteLichThi);
        }
    }
}
