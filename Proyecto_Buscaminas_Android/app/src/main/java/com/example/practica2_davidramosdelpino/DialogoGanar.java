package com.example.practica2_davidramosdelpino;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoGanar extends DialogFragment {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder myBuilder=new AlertDialog.Builder(getActivity());
        myBuilder.setTitle("Has Ganado!!!!");
        myBuilder.setMessage("Has marcado todas las minas correctamente\n");
        myBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) getActivity()).crearTablero(8,8,10);
                dialog.cancel();
            }
        });
        return  myBuilder.create();
    }
}
