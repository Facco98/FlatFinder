package it.unitn.disi.lpsmt.flatfinder.service;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.task.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationService extends FirebaseMessagingService {

    private static String TAG = "NOTIFICATION Service";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if( u != null ){

            Task<Void, String> updateTask = new Task<Void, String>((voids) -> {
                URL endpoint = new URL("https://f4065lwkkj.execute-api.us-east-1.amazonaws.com/zone"
                + "?username_utente=" + u.getUid() + "&token_notifica=" + s);
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("PUT");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String tmp;
                StringBuilder sb = new StringBuilder();
                while ((tmp = reader.readLine()) != null)
                    sb.append(tmp);

                return sb.toString();
            }, (string, err) -> {

                if( err != null )
                    err.printStackTrace();
                else
                    System.out.println(string);

            });

            updateTask.execute(new Void[1]);


        }
    }
}
