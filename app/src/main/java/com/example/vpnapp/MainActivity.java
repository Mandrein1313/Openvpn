package com.example.vpnapp;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private static final int VPN_REQUEST_CODE = 2026;
    
    private TextView tvStatus;
    private TextView btnConnectText;
    private MaterialCardView btnConnectCard;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        btnConnectCard = findViewById(R.id.btnConnectCard);
        btnConnectText = findViewById(R.id.btnConnectText);

        btnConnectCard.setOnClickListener(v -> {
            if (!isConnected) {
                prepareAndStartVpn();
            } else {
                stopVpnService();
            }
        });
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
        Intent intent = new Intent(this, MyVpnService.class);
        intent.setAction("START");
        startService(intent);

        isConnected = true;
        tvStatus.setText(getString(R.string.status_connected));
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