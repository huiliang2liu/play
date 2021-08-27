package com.xh.play.entities;

public class IP {
    public String country;
    public String countryCode;
    public String region;
    public String regionName;
    public String city;
    public String zip;
    public float lat;
    public float lon;
    public String timezone;

    @Override
    public String toString() {
        return "IP{" +
                "country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", region='" + region + '\'' +
                ", regionName='" + regionName + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
