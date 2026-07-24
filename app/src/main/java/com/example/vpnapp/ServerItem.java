package com.example.vpnapp;

public class ServerItem {
    private String name;
    private String status;
    private String online;
    private String max;
    private int percentage;
    private int flagResId;           // รูปธงชาติ
    private String ovpnFileName;

    public ServerItem(String name, String status, String online, String max, 
                     int percentage, int flagResId, String ovpnFileName) {
        this.name = name;
        this.status = status;
        this.online = online;
        this.max = max;
        this.percentage = percentage;
        this.flagResId = flagResId;
        this.ovpnFileName = ovpnFileName;
    }

    public String getName() { return name; }
    public String getStatus() { return status; }
    public String getOnline() { return online; }
    public String getMax() { return max; }
    public int getPercentage() { return percentage; }
    public int getFlagResId() { return flagResId; }
    public String getOvpnFileName() { return ovpnFileName; }
}
