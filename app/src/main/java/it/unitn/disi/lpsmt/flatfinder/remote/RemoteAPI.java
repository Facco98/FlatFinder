package it.unitn.disi.lpsmt.flatfinder.remote;

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

    private RemoteAPI(){



    }

    public static void getAnnounceList(@Nullable Map<String, String> filters, @NonNull Completion<List<Announce>> completion){

        Task<Void, List<Announce>> announceListTask = new Task<Void, List<Announce>>((voids) -> {
            String finalUrl = ANNOUNCE_ENDPOINT;
            if( filters != null && filters.keySet().size() > 0 ){

                finalUrl += "?";
                for(Map.Entry<String, String> filter : filters.entrySet() )
                    finalUrl += filter.getKey() + "=" + filter.getValue() + "&";
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
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            String jsonString = gson.toJson(announce);
            Log.i(TAG, "POSTING ANNOUNCE: " + jsonString);
            writer.println(jsonString);
            writer.flush();
            writer.close();

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

    public static void uploadPhotosForAnnounce(@NonNull List<Photo> photos, @NonNull Integer announceID, @Nullable Completion<String> completion){

        Task<Void, String> photosTask = new Task<Void, String>((voids) -> {

            URL endpoint = new URL(PHOTO_ENDPOINT + "?idAnnuncio=" + announceID );
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("POST");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.println(new Gson().toJson(photos));
            writer.flush();
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();

        }, completion);
        photosTask.execute(new Void[1]);

    }

}
