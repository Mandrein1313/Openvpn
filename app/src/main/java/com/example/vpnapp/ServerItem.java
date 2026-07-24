package com.example.vpnapp;

public class ServerItem {
    private String name;
    private String status;
    private int percentage;
    private String ovpnFileName;   // ชื่อไฟล์โดยไม่ต้อง .ovpn

    public ServerItem(String name, String status, int percentage, String ovpnFileName) {
        this.name = name;
        this.status = status;
        this.percentage = percentage;
        this.ovpnFileName = ovpnFileName;
    }

    public String getName() { return name; }
    public String getStatus() { return status; }
    public int getPercentage() { return percentage; }
    public String getOvpnFileName() { return ovpnFileName; }
}