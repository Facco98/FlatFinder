package it.unitn.disi.lpsmt.flatfinder.model.gecoding;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodingResponse {

    @SerializedName("status")
    private GeocodingResponseStatus status;

    @SerializedName("results")
    private List<GeocodingResult> result;

    public GeocodingResponse() {

    }

    public GeocodingResponse(GeocodingResponseStatus status, List<GeocodingResult> result) {
        this.status = status;
        this.result = result;
    }

    public GeocodingResponseStatus getStatus() {
        return status;
    }

    public void setStatus(GeocodingResponseStatus status) {
        this.status = status;
    }

    public List<GeocodingResult> getResult() {
        return result;
    }

    public void setResult(List<GeocodingResult> result) {
        this.result = result;
    }

    public String toString(){

        String tmp = "[{STATUS=" + this.status.toString();
        tmp += "}, RESULT={" + this.result.toString() +"}]";
        return tmp;

    }
}
