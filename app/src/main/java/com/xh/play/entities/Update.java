package com.xh.play.entities;

public class Update {
    public String url;
    public boolean must = false;
    public String appVer;
    public String describe;

    @Override
    public String toString() {
        return "Update{" +
                "url='" + url + '\'' +
                ", must=" + must +
                ", appVer='" + appVer + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
