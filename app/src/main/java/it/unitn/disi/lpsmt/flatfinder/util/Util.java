package it.unitn.disi.lpsmt.flatfinder.util;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static final int SELECT_IMAGE = 200;

    private Util(){

    }

    public static String dateToString( Date date ){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        System.err.println(format.format(date));
        return format.format(date);
    }

    public static Date stringToDate( String s ) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        return format.parse(s);

    }

    public static AlertDialog getDialog(Context context, String TAG){
        Log.d(TAG, "alert create");
        return new AlertDialog.Builder(context)
                .setTitle("Caricamento")
                .setMessage("Attendi mentre carichiamo l'applicazione")
                .setCancelable(false)
                .create();
    }

    public static void showDialog(AlertDialog alertDialog, String TAG){
        alertDialog.show();
        Log.d(TAG, "alert show");
    }

    public static void dismissDialog(AlertDialog alertDialog, String TAG){
        alertDialog.hide();
        alertDialog.dismiss();
        Log.d(TAG, "alert dismiss");
    }

}
