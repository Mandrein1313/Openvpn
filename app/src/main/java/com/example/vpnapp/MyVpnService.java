package com.example.vpnapp;

import android.content.Intent;
import android.net.VpnService;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyVpnService extends VpnService {

    private static final String TAG = "MyVpnService";
    private boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if ("START".equals(action)) {
                startOpenVpnConnection();
            } else if ("STOP".equals(action)) {
                stopOpenVpnConnection();
            }
        }
        return START_NOT_STICKY;
    }

    private void startOpenVpnConnection() {
        Log.d(TAG, "Starting OpenVPN Connection...");
        
        try {
            // 1. อ่านไฟล์ .ovpn จาก res/raw/ (เปลี่ยน jp_vpn ให้ตรงกับชื่อไฟล์ของคุณ)
            InputStream inputStream = getResources().openRawResource(R.raw.jp_vpn);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder configBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                configBuilder.append(line).append("\n");
            }
            reader.close();

            String ovpnConfig = configBuilder.toString();

            // 2. สั่งเริ่มโปรเซส OpenVPN Core
            // หมายเหตุ: การสร้าง Tunnel และ VPN Interface 
            // จะถูกประมวลผลผ่าน OpenVPN Core Library
            isRunning = true;
            Log.d(TAG, "VPN Config Loaded Successfully!");

        } catch (Exception e) {
            Log.e(TAG, "Error starting VPN: " + e.getMessage());
        }
    }

    private void stopOpenVpnConnection() {
        Log.d(TAG, "Stopping OpenVPN Connection...");
        isRunning = false;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "VPN Service Destroyed");
    }
}
