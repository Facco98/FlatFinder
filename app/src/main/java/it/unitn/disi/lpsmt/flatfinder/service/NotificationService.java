package it.unitn.disi.lpsmt.flatfinder.service;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.i("TOKEN", s);
    }
}
