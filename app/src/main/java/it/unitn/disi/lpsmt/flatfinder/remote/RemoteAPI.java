package it.unitn.disi.lpsmt.flatfinder.remote;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.Zone;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;
import it.unitn.disi.lpsmt.flatfinder.task.Task;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class RemoteAPI {

    private final static String ANNOUNCE_ENDPOINT = "https://f4065lwkkj.execute-api.us-east-1.amazonaws.com/announce";
    private final static String PHOTO_ENDPOINT = "https://f4065lwkkj.execute-api.us-east-1.amazonaws.com/photo";
    private final static String ZONE_ENDPOINT = "https://f4065lwkkj.execute-api.us-east-1.amazonaws.com/zone";

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

    public static void listZones(@NonNull User user, @Nullable Completion<List<Zone>> completion){

        Task<Void, List<Zone>> listZonesTask = new Task<Void, List<Zone>>((voids) -> {

            URL endpoint = new URL(ZONE_ENDPOINT + "?username_utente="+user.getSub() );
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            Zone[] zones = gson.fromJson(reader, Zone[].class);
            return Arrays.asList(zones);

        }, completion);
        listZonesTask.execute(new Void[1]);

    }

    public static void registerZone(@NonNull User user, @NonNull Zone zone, @Nullable Completion<String> completion){


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            try {
                Task<Void, String> registerTask = new Task<Void, String>((voids) -> {
                    URL endpoint = new URL(ZONE_ENDPOINT + "?username_utente=" + user.getSub() + "&token_notifica=" + task.getResult().getToken());
                    HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                    connection.setRequestMethod("POST");
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
                    Gson gson = new GsonBuilder().create();
                    String jsonString = gson.toJson(zone);
                    Log.i(TAG, "POSTING ZONE: " + jsonString);
                    writer.println(jsonString);
                    writer.flush();
                    writer.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    StringBuilder sb = new StringBuilder();
                    while ((s = reader.readLine()) != null)
                        sb.append(s);
                    Log.d(TAG, sb.toString());

                    return sb.toString();

                }, completion);
                registerTask.execute(new Void[1]);

            } catch (Exception e) {
                if( completion != null )
                    completion.onComplete(null, e);
            }


        });
    }

    public static void deleteZone(@NonNull Zone zone, @NonNull Completion<String> completion){

        Task<Void, String> deleteTask = new Task<Void, String>((voids) -> {

            URL endpoint = new URL(ZONE_ENDPOINT);
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("DELETE");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.println(new Gson().toJson(zone));
            System.out.println("SENDING: " + new Gson().toJson(zone));
            writer.flush();
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            System.out.println("READT");
            return sb.toString();

        }, completion);
        deleteTask.execute(new Void[1]);

    }

    public static void updateNotificationToken(@NonNull String s, @NonNull Completion<String> completion){

        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if( u != null ){

            Task<Void, String> updateTask = new Task<Void, String>((voids) -> {
                URL endpoint = new URL(ZONE_ENDPOINT
                        + "?username_utente=" + u.getUid() + "&token_notifica=" + s);
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("PUT");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String tmp;
                StringBuilder sb = new StringBuilder();
                while ((tmp = reader.readLine()) != null)
                    sb.append(tmp);

                return sb.toString();
            }, completion);

            updateTask.execute(new Void[1]);


        }


    }

    public static void deleteAnnounce(@NonNull Announce announce, @NonNull Completion<String> completion){

        Task<Void, String> deleteTask = new Task<Void, String>((voids) -> {

            URL endpoint = new URL(ANNOUNCE_ENDPOINT+"?idAnnuncio="+announce.getId());
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("DELETE");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();

        }, completion);
        deleteTask.execute(new Void[1]);

    }

    public static void updateAnnounce(@NonNull Announce announce, @NonNull Completion<String> completion){

        Task<Void, String> updateTask = new Task<Void, String>((voids) -> {

            URL endpoint = new URL(ANNOUNCE_ENDPOINT);
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("PUT");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.println(new Gson().toJson(announce));
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
        updateTask.execute(new Void[1]);

    }

    public void updatePhotosForAnnounce(@NonNull List<Photo> photos, @NonNull Integer announceID, @NonNull Completion<String> completion){

        Task<Void, String> updateTask = new Task<Void, String>((voids) -> {

            URL endpoint = new URL(PHOTO_ENDPOINT+"?idAnnuncio=" + announceID);
            HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
            connection.setRequestMethod("PUT");
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
        updateTask.execute(new Void[1]);
    }

}
