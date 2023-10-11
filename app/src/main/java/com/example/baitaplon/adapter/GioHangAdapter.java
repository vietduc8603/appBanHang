package com.example.baitaplon.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baitaplon.Interface.ImgClickListener;
import com.example.baitaplon.R;
import com.example.baitaplon.model.EventBus.TinhTongEvent;
import com.example.baitaplon.model.GioHang;
import com.example.baitaplon.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensanpham());
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+ " ");
        Glide.with(context).load(gioHang.getHinhanhsanpham()).into(holder.item_giohang_img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_giasp.setText(decimalFormat.format(gioHang.getGiasanpham()));
        long gia = gioHang.getSoluong() * gioHang.getGiasanpham();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
        holder.setListener(new ImgClickListener() {
            @Override
            public void onImgClick(View view, int pos, int giatri) {
                if(giatri == 1){
                    if(gioHangList.get(pos).getSoluong() > 1){
                        int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);

                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+ " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasanpham();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else if(gioHangList.get(pos).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                } else if (giatri == 2) {
                    if(gioHangList.get(pos).getSoluong() < 11) {
                        int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+ " ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasanpham();
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_img, imgtru, imgcong;
        TextView item_giohang_tensp, item_giohang_giasp, item_giohang_giasp2, item_giohang_soluong;
        ImgClickListener listener;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_giasp = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_gia2);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_img = itemView.findViewById(R.id.item_giohang_img);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);

            //click event
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setListener(ImgClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if(view == imgtru){
                //set giá trị 1 là trừ
                listener.onImgClick(view, getAdapterPosition(), 1);
            }else if(view == imgcong) {
                //set giá trị 2 là cộng
                listener.onImgClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
