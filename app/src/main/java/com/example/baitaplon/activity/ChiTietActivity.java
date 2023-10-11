package com.example.baitaplon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.baitaplon.R;
import com.example.baitaplon.model.GioHang;
import com.example.baitaplon.model.SanPhamMoi;
import com.example.baitaplon.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tensp, giasp, mota;
    Button btnthem;
    ImageView imghinhanh;
    Spinner spinner;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        InitView();
        ActionToolBar();
        InitData();
        InitControl();
    }

    private void InitControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i< Utils.manggiohang.size(); i++){
                if(Utils.manggiohang.get(i).getIdsanpham() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    Long gia = Long.parseLong(sanPhamMoi.getGiasanpham()) * Utils.manggiohang.get(i).getSoluong();
                    Utils.manggiohang.get(i).setGiasanpham(gia);
                    flag = true;
                }
            }
            if (flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiasanpham()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasanpham(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsanpham(sanPhamMoi.getId());
                gioHang.setTensanpham(sanPhamMoi.getTensanpham());
                gioHang.setHinhanhsanpham(sanPhamMoi.getHinhanhsanpham());
                Utils.manggiohang.add(gioHang);
            }
        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasanpham()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasanpham(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsanpham(sanPhamMoi.getId());
            gioHang.setTensanpham(sanPhamMoi.getTensanpham());
            gioHang.setHinhanhsanpham(sanPhamMoi.getHinhanhsanpham());
            Utils.manggiohang.add(gioHang);
        }
        if(Utils.manggiohang != null){
            int tonghang = 0;
            for (int i = 0;i< Utils.manggiohang.size(); i++){
                tonghang = tonghang + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(tonghang));
        }
    }

    private void InitData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensanpham());
        mota.setText(sanPhamMoi.getMotasanpham());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanhsanpham()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasanpham())) + "Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);
    }

    private void InitView() {
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemgiohang);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayout = findViewById(R.id.framegiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(Utils.manggiohang != null){
            int tonghang = 0;
            for (int i = 0;i< Utils.manggiohang.size(); i++){
                tonghang = tonghang + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(tonghang));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.manggiohang != null){
            int tonghang = 0;
            for (int i = 0;i< Utils.manggiohang.size(); i++){
                tonghang = tonghang + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(tonghang));
        }
    }
}