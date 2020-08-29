package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.List;

import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;

public class EliminaAnnuncioDialogFragment extends DialogFragment {

    private static final String TAG = "EliminaAnnuncioFragment";
    private Announce announce;
    private Context context;

    public EliminaAnnuncioDialogFragment(Announce ann, Context ctx){

        context = ctx;
        announce = ann;
    }

    public Dialog onCreateDialog(Bundle SavedInstanceState)
    {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_announce_message)
                .setPositiveButton(R.string.delete_annonce_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "Button EliminaAnnuncio did click");

                        RemoteAPI.deleteAnnounce(announce, (string, err) -> {
                            if(err != null){
                                Toast.makeText(context, "Errore durante l'eliminazione dell'annuncio", Toast.LENGTH_SHORT)
                                        .show();
                                err.printStackTrace();

                            }
                            else /*if (string != null)*/{
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.delete_announce_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "Button Annulla did click");
                        dialog.dismiss();
                    }
                });


        alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnAnnulla = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnAnnulla.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        return alertDialog;
    }



}


