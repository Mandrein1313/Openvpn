package com.example.vpnapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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
    
    // การ์ดแสดงผลเซิร์ฟเวอร์หลัก (ใช้คลิกเพื่อเปิด Dialog)
    private MaterialCardView cardServerSelect;
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

        tvStatus = findViewById(R.id.tvStatus);
        btnConnectCard = findViewById(R.id.btnConnectCard);
        btnConnectText = findViewById(R.id.btnConnectText);

        // ผูกการ์ดเลือกเซิร์ฟเวอร์
        cardServerSelect = findViewById(R.id.cardServer);
        imgServerFlag = findViewById(R.id.imgServerFlag);
        tvServerName = findViewById(R.id.tvServerName);
        tvServerStatus = findViewById(R.id.tvServerStatus);
        tvPercentage = findViewById(R.id.tvPercentage);

        loadServersFromRaw();

        // เลือกตัวแรกเป็นค่าเริ่มต้น
        if (!serverList.isEmpty()) {
            currentServer = serverList.get(0);
            updateServerCardUI(currentServer);
        }

        // เมื่อกดที่การ์ดเซิร์ฟเวอร์ ให้แสดง Dialog เต็มหน้าจอ
        cardServerSelect.setOnClickListener(v -> showServerSelectionDialog());

        btnConnectCard.setOnClickListener(v -> {
            if (!isConnected) {
                prepareAndStartVpn();
            } else {
                stopVpnService();
            }
        });
    }

    // ฟังก์ชันสร้างและแสดง Dialog แบบเต็มหน้าจอ
    private void showServerSelectionDialog() {
        Dialog dialog = new Dialog(this, R.style.FullScreenDialogTheme);
        dialog.setContentView(R.layout.dialog_server_select);

        // ตั้งค่าให้ Dialog มีขนาดเต็มหน้าจอ
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.MATCH_PARENT
            );
        }

        ImageView btnClose = dialog.findViewById(R.id.btnCloseDialog);
        ListView lvServer = dialog.findViewById(R.id.lvServerDialog);

        // ใช้ CustomSpinnerAdapter ตัวเดิมในการแสดงผลรายการใน Dialog
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, serverList);
        lvServer.setAdapter(adapter);

        // เมื่อผู้ใช้กดเลือกเซิร์ฟเวอร์ในรายการ
        lvServer.setOnItemClickListener((parent, view, position, id) -> {
            currentServer = serverList.get(position);
            updateServerCardUI(currentServer); // อัปเดต UI หน้าหลัก
            dialog.dismiss(); // ปิด Dialog
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadServersFromRaw() {
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
