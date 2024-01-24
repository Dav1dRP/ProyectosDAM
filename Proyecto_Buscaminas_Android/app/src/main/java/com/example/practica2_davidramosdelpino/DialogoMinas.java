package com.example.practica2_davidramosdelpino;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoMinas extends DialogFragment {
    public int minaSeleccionada;
    private Mina[] minas;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflar el diseño del diálogo desde el archivo XML
        View vistaDialogo = LayoutInflater.from(getActivity()).inflate(R.layout.menu_minas, null);

        // Crear un Array de Minas
        minas = new Mina[6];
        minas[0] = new Mina("Mina Clásica", R.drawable.mina1);
        minas[1] = new Mina("Mina Bomber", R.drawable.mina2);
        minas[2] = new Mina("Mina Dinamita", R.drawable.mina3);
        minas[3] = new Mina("Mina Granada", R.drawable.mina4);
        minas[4] = new Mina("Mina Submarina", R.drawable.mina5);
        minas[5] = new Mina("Mina Molotov", R.drawable.mina6);

        // Obtener la referencia al ListView desde la vista del diálogo
        ListView listViewMinas = vistaDialogo.findViewById(R.id.listViewMinas);

        // Crear un adaptador personalizado para el ListView
        ArrayAdapter<Mina> adapter = new ArrayAdapter<Mina>(getActivity(), R.layout.fila_mina, minas) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return crearFila(position, convertView, parent);
            }
        };

        // Asignar el adaptador al ListView
        listViewMinas.setAdapter(adapter);

        // Configurar el evento de clic en un elemento del ListView
        listViewMinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mina minaPulsada = minas[position];
                minaSeleccionada = minaPulsada.imagen;
                ((MainActivity) getActivity()).actualizarImagenMinas(minaSeleccionada);
                dismiss();
            }
        });

        // Crear un AlertDialog
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
        myBuilder.setView(vistaDialogo);

        // Mostrar el diálogo
        return myBuilder.create();
    }

    private View crearFila(int position, View convertView, ViewGroup parent) {
        //Para programar el adaptador crearfila() va a ser invocada una vez por cada objeto del array de ciudades

        //1.Inflamos el xml con nuestra vista personalizada
        LayoutInflater miInflador=getLayoutInflater();

        //2.Encontramos a los objetos de cada una de las filas infladas
        View mifila=miInflador.inflate(R.layout.fila_mina,parent,false);
        TextView txtNombre=mifila.findViewById(R.id.textoMina);
        ImageView imagenMina=mifila.findViewById(R.id.imagen_Mina);

        //3.Rellenar los datos con el objeto i-ésimo del array de objetos (position)
        txtNombre.setText(minas[position].nombre);
        imagenMina.setImageResource(minas[position].imagen);

        // 6. Retornar la fila instanciada/inflada
        return mifila;
    }
}

