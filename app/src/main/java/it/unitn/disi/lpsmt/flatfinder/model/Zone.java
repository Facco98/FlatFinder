package it.unitn.disi.lpsmt.flatfinder.model;

import com.google.gson.annotations.SerializedName;

public class Zone {

    @SerializedName("id")
    private Integer id;

    @SerializedName("longitudine_centro")
    private double centerLongitude;

    @SerializedName("latitudine_centro")
    private double centerLatitude;

    @SerializedName("distanza_max")
    private double maxDistance;

    @SerializedName("indirizzo")
    private String address;

    public Zone(Integer id, double centerLongitude, double centerLatitude, double maxDistance, String address) {
        this.id = id;
        this.centerLongitude = centerLongitude;
        this.centerLatitude = centerLatitude;
        this.maxDistance = maxDistance;
        this.address = address;
    }

    public Zone() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
