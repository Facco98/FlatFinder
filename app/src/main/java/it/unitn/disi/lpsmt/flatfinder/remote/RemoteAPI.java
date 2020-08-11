package it.unitn.disi.lpsmt.flatfinder.remote;

import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;
import it.unitn.disi.lpsmt.flatfinder.model.gecoding.GeocodingResponse;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;
import it.unitn.disi.lpsmt.flatfinder.task.Task;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class RemoteAPI {

    private final static String ANNOUNCE_ENDPOINT = "https://f4065lwkkj.execute-api.us-east-1.amazonaws.com/announce";
    private final static String PHOTO_ENDPOINT = "https://f4065lwkkj.execute-api.us-east-1.amazonaws.com/photo";
    private static final String TAG = "REMOTE";
    private static final String GEOCODING_KEY = "94250b2bf89d401da822d4821b28b640";
    private static final String GEOCODING_ENDPOINT = "https://api.opencagedata.com/geocode/v1/json";

    private RemoteAPI(){



    }

    public static void getAnnounceList(@Nullable Map<String, String> filters, @NonNull Completion<List<Announce>> completion){

        Task<Void, List<Announce>> announceListTask = new Task<Void, List<Announce>>((voids) -> {
            String finalUrl = ANNOUNCE_ENDPOINT;
            if( filters != null && filters.keySet().size() > 0 ){

                finalUrl += "?";
                for(Map.Entry<String, String> filter : filters.entrySet() )
                    finalUrl += filter.getKey() + "=" + filter.getValue() + ",";
                finalUrl = finalUrl.substring(0, finalUrl.length()-1);

            }
            URL endpoint = new URL(finalUrl);
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            Announce[] announcesArray = gson.fromJson(reader, Announce[].class);
            return Arrays.asList(announcesArray);
        }, completion);
        announceListTask.execute(new Void[1]);

    }

    public static void createNewAnnounce(@NonNull Announce announce, @Nullable Completion<String> completion){

        Completion<String> c = completion;
        if( c == null ){

            c = (r, ex) -> {

                if( ex != null )
                    ex.printStackTrace();
                else if ( r != null )
                    Log.i(TAG, r);
            };

        }

        Task<Void, String> postTask = new Task<Void, String>((voids) -> {

            URL endpoint = new URL(ANNOUNCE_ENDPOINT);
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            String jsonString = gson.toJson(announce);
            Log.i(TAG, "POSTING ANNOUNCE: " + jsonString);
            writer.write(jsonString);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String s;
            StringBuilder sb = new StringBuilder();
            while((s = reader.readLine()) != null )
                sb.append(s);

            return sb.toString();

        }, c);
        postTask.execute(new Void[1]);

    }

    public static void getPhotosForAnnounce(@NonNull Integer announceId, @NonNull Completion<List<Photo>> completion){

        Task<Void, List<Photo>> photosTask = new Task<Void, List<Photo>>((voids) -> {

            URL endpoint = new URL(PHOTO_ENDPOINT + "?idAnnuncio="+announceId );
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            Photo[] photosArray = gson.fromJson(reader, Photo[].class);
            return Arrays.asList(photosArray);

        }, completion);
        photosTask.execute(new Void[1]);

    }

    public static void verifyAddress(@NonNull String address, @NonNull Completion<GeocodingResponse> completion){

        Task<Void, GeocodingResponse> verificationTask = new Task<Void, GeocodingResponse>((voids -> {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(GEOCODING_ENDPOINT + "?q=" +address +"&key="+GEOCODING_KEY+"&pretty=1")
                        .openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String temp;
                StringBuilder sb = new StringBuilder();
                while( (temp = in.readLine()) != null)
                    sb.append(temp);
                String jsonString = sb.toString();
                Log.d(TAG, jsonString);
                Gson gson = new Gson();
                GeocodingResponse response = gson.fromJson(jsonString, GeocodingResponse.class);
                return response;
        }), completion);
        verificationTask.execute(new Void[1]);

    }

}
