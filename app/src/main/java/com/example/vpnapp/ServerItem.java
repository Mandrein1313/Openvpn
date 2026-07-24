package com.example.vpnapp;

public class ServerItem {
    private String name;
    private String country;
    private String status;
    private int percentage;
    private int flagResId;
    private String ovpnFileName;

    public ServerItem(String name, String country, String status, int percentage, int flagResId, String ovpnFileName) {
        this.name = name;
        this.country = country;
        this.status = status;
        this.percentage = percentage;
        this.flagResId = flagResId;
        this.ovpnFileName = ovpnFileName;
    }

    // Getter
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getStatus() { return status; }
    public int getPercentage() { return percentage; }
    public int getFlagResId() { return flagResId; }     // ← เพิ่มบรรทัดนี้
    public String getOvpnFileName() { return ovpnFileName; }
}