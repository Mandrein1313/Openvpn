package com.example.vpnapp;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private boolean isConnected = false;
    private ServerItem currentServer;

    private List<ServerItem> serverList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ผูก View
        tvStatus = findViewById(R.id.tvStatus);
        btnConnectCard = findViewById(R.id.btnConnectCard);
        btnConnectText = findViewById(R.id.btnConnectText);
        serverSpinner = findViewById(R.id.serverSpinner);   // ต้องเพิ่มใน XML ด้วย

        // เพิ่มเซิร์ฟเวอร์
        setupServers();

        // Spinner เลือกเซิร์ฟเวอร์
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, getServerNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSpinner.setAdapter(adapter);

        serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentServer = serverList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ปุ่มเชื่อมต่อ
        btnConnectCard.setOnClickListener(v -> {
            if (!isConnected) {
                prepareAndStartVpn();
            } else {
                stopVpnService();
            }
        });
    }

    private void setupServers() {
        serverList.add(new ServerItem("Thailand Server", "TH", "ออนไลน์: 875 / จำกัด: 1200", 73, "th_vpn"));
        serverList.add(new ServerItem("Japan Server", "JP", "ออนไลน์: 452 / จำกัด: 800", 56, "jp_vpn"));
        // เพิ่มเซิร์ฟเวอร์อื่น ๆ ได้ที่นี่
    }

    private List<String> getServerNames() {
        List<String> names = new ArrayList<>();
        for (ServerItem s : serverList) {
            names.add(s.getName());
        }
        return names;
    }

    private void prepareAndStartVpn() {
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
        if (currentServer == null) {
            Toast.makeText(this, "กรุณาเลือกเซิร์ฟเวอร์", Toast.LENGTH_SHORT).show();
            return;
        }

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
