package it.unitn.disi.lpsmt.flatfinder.model.gecoding;

import com.google.gson.annotations.SerializedName;

public class GeocodingResponseStatus {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public GeocodingResponseStatus() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString(){

        return "code=" +this.getCode() + ", msg=" + this.getMessage();

    }
}
