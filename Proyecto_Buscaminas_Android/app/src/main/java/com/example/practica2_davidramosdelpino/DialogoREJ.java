package com.example.practica2_davidramosdelpino;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoREJ extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflar el diseño del diálogo desde el archivo XML
        View vistaDialogo = LayoutInflater.from(getActivity()).inflate(R.layout.menu_botones, null);

        // Crear un AlertDialog
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
        myBuilder.setView(vistaDialogo);
        myBuilder.setTitle("Dificultad");

        RadioGroup radioGroup = vistaDialogo.findViewById(R.id.niveles);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int filas=0;
                int columnas=0;
                int minas=0;
                if (checkedId == R.id.Principiante) {
                    filas = 8;
                    columnas = 8;
                    minas = 10;
                } else if (checkedId == R.id.Amateur) {
                    filas = 12;
                    columnas = 12;
                    minas = 30;
                } else if (checkedId == R.id.Avanzado) {
                    filas = 16;
                    columnas = 16;
                    minas = 60;
                }

                // Llama a un método personalizado en la MainActivity para actualizar el tablero
                ((MainActivity) getActivity()).crearTablero(filas, columnas, minas);
            }
        });
        myBuilder.setPositiveButton("VOLVER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Mostrar el diálogo
        return myBuilder.create();
    }
}

