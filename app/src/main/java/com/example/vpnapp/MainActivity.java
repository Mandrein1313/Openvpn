package com.example.vpnapp;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int VPN_REQUEST_CODE = 2026;
    
    private TextView tvStatus;
    private TextView btnConnectText;
    private MaterialCardView btnConnectCard;
    private Spinner serverSpinner;

    // View บนการ์ดแสดงข้อมูลเซิร์ฟเวอร์
    private ImageView imgServerFlag;
    private TextView tvServerName;
    private TextView tvServerStatus;
    private TextView tvPercentage;

    private boolean isConnected = false;
    private ServerItem currentServer;
    private List<ServerItem> serverList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ผูก View ทั่วไป
        tvStatus = findViewById(R.id.tvStatus);
        btnConnectCard = findViewById(R.id.btnConnectCard);
        btnConnectText = findViewById(R.id.btnConnectText);
        serverSpinner = findViewById(R.id.serverSpinner);

        // ผูก View บนการ์ดเซิร์ฟเวอร์
        imgServerFlag = findViewById(R.id.imgServerFlag);
        tvServerName = findViewById(R.id.tvServerName);
        tvServerStatus = findViewById(R.id.tvServerStatus);
        tvPercentage = findViewById(R.id.tvPercentage);

        loadServersFromRaw();

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, serverList);
        serverSpinner.setAdapter(adapter);

        serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentServer = serverList.get(position);
                updateServerCardUI(currentServer); // อัปเดตข้อมูลบนการ์ดเมื่อเปลี่ยนรายการ
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnConnectCard.setOnClickListener(v -> {
            if (!isConnected) {
                prepareAndStartVpn();
            } else {
                stopVpnService();
            }
        });
    }

    private void loadServersFromRaw() {
        // ดึงรูปภาพธงชาติจาก res/drawable/flag_th.png และ flag_jp.png
        serverList.add(new ServerItem(
                "Thailand Server", 
                "Thailand", 
                "ออนไลน์: 875 / จำกัด: 1200", 
                73, 
                R.drawable.flag_th, 
                "th_vpn"
        ));

        serverList.add(new ServerItem(
                "Japan Server", 
                "Japan", 
                "ออนไลน์: 620 / จำกัด: 1000", 
                56, 
                R.drawable.flag_jp, 
                "jp_vpn"
        ));
    }

    // เมธอดสำหรับอัปเดตข้อมูลการ์ดเซิร์ฟเวอร์ที่เลือก
    private void updateServerCardUI(ServerItem server) {
        if (server != null) {
            imgServerFlag.setImageResource(server.getFlagResId());
            tvServerName.setText(server.getName());
            tvServerStatus.setText(server.getStatus());
            tvPercentage.setText(server.getPercentage() + "%");
        }
    }

    private void prepareAndStartVpn() {
        if (currentServer == null) {
            Toast.makeText(this, "กรุณาเลือกเซิร์ฟเวอร์", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST_CODE);
        } else {
            startVpnService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            startVpnService();
        } else if (requestCode == VPN_REQUEST_CODE) {
            Toast.makeText(this, getString(R.string.toast_permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    private void startVpnService() {
        Intent intent = new Intent(this, MyVpnService.class);
        intent.setAction("START");
        intent.putExtra("ovpn_file", currentServer.getOvpnFileName());
        startService(intent);

        isConnected = true;
        tvStatus.setText(getString(R.string.status_connected) + " - " + currentServer.getName());
        btnConnectText.setText(getString(R.string.btn_disconnect));
    }

    private void stopVpnService() {
        Intent intent = new Intent(this, MyVpnService.class);
        intent.setAction("STOP");
        startService(intent);

        isConnected = false;
        tvStatus.setText(getString(R.string.status_disconnected));
        btnConnectText.setText(getString(R.string.btn_connect));
    }
}
