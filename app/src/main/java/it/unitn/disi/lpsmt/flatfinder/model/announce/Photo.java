package it.unitn.disi.lpsmt.flatfinder.model.announce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;

public class Photo {

    @SerializedName("id")
    private Integer id;
    @SerializedName("immagine_base64")
    private String encodedString;

    public Photo(Integer id, String encodedString) {
        this.id = id;
        this.encodedString = encodedString;
    }

    public Photo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEncodedString() {
        return encodedString;
    }

    public void setEncodedString(String encodedString) {
        this.encodedString = encodedString;
    }

    public Bitmap getBitmap(){

        byte[] bytes = Base64.decode(this.encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    public static Photo createPhotoFromImage(Bitmap image){

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();
        String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);

        return new Photo(null, encodedImage);

    }

    public String toString(){

        return "PHOTO[id=" + this.getId() +", encoded=" + this.getEncodedString()+"]";

    }
}
