package com.example.vpnapp;

public class ServerItem {
    private String name;
    private String country;
    private String status;
    private int percentage;
    private String ovpnFileName;   // ชื่อไฟล์ใน raw

    public ServerItem(String name, String country, String status, int percentage, String ovpnFileName) {
        this.name = name;
        this.country = country;
        this.status = status;
        this.percentage = percentage;
        this.ovpnFileName = ovpnFileName;
    }

    // Getter
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getStatus() { return status; }
    public int getPercentage() { return percentage; }
    public String getOvpnFileName() { return ovpnFileName; }
}