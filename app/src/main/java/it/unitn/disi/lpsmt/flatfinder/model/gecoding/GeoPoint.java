package it.unitn.disi.lpsmt.flatfinder.model.gecoding;

import com.google.gson.annotations.SerializedName;

public class GeoPoint {

    @SerializedName("lng")
    private double longitude;

    @SerializedName("lat")
    private double latitude;

    public GeoPoint() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String toString() {

        return "latitude=" + this.getLatitude() + ", longitude="+this.getLongitude();

    }
}
