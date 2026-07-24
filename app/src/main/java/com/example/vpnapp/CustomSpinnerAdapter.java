package com.example.vpnapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<ServerItem> {

    private List<ServerItem> serverList;

    public CustomSpinnerAdapter(Context context, List<ServerItem> serverList) {
        super(context, 0, serverList);
        this.serverList = serverList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, true);
    }

    private View createView(int position, View convertView, ViewGroup parent, boolean isDropdown) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        ServerItem server = serverList.get(position);

        ImageView flag = convertView.findViewById(R.id.spinner_flag);
        TextView name = convertView.findViewById(R.id.spinner_name);

        if (server != null) {
            // ตั้งค่ารูปภาพธงชาติ
            flag.setImageResource(server.getFlagResId());
            // ปรับขนาดรูปให้อยู่ในสัดส่วนที่ถูกต้องและไม่ยืดเบี้ยว
            flag.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // ตั้งค่าชื่อเซิร์ฟเวอร์
            name.setText(server.getName());
        }

        return convertView;
    }
}
