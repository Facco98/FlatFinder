package it.unitn.disi.lpsmt.flatfinder.model.gecoding;

import com.google.gson.annotations.SerializedName;

public class GeocodingResult {

    @SerializedName("confidence")
    private int confidence;

    @SerializedName("geometry")
    private GeoPoint point;

    @SerializedName("formatted")
    private String formattedName;

    public GeocodingResult() {
    }

    public GeocodingResult(int confidence, GeoPoint point, String formattedName) {
        this.confidence = confidence;
        this.point = point;
        this.formattedName = formattedName;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    public String toString(){

        return "confidence=" + this.getConfidence() + ", formattedName=" +this.getFormattedName() + "geoPoint={" + this.getPoint().toString()+"}";

    }
}
