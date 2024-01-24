package com.example.tarea4_davidramosdelpino;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListasCompartir extends Fragment {

    private RecyclerView recyclerView;
    private MyListasCompartirRecyclerViewAdapter adaptadorListasCompartir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listas_compartir, container, false);

        recyclerView = root.findViewById(R.id.recyclerListasCompar);
        adaptadorListasCompartir = new MyListasCompartirRecyclerViewAdapter(getContext(),recyclerView);
        recyclerView.setAdapter(adaptadorListasCompartir);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener la lista de listas y actualizar el adaptador
        List<Lista> listasOrdenadas = obtenerListasOrdenadasPorFecha();
        adaptadorListasCompartir.setListas(listasOrdenadas);

        return root;
    }

    // Método para obtener las listas ordenadas por fecha
    private List<Lista> obtenerListasOrdenadasPorFecha() {
        List<Lista> listasOrdenadas = new ArrayList<>();

        SQLiteDatabase db = getActivity().openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

        // Consulta para obtener las listas ordenadas por fecha
        String queryListasOrdenadas = "SELECT * FROM lista_compra ORDER BY fecha DESC";
        Cursor cursorListasOrdenadas = db.rawQuery(queryListasOrdenadas, null);

        // Itera sobre el cursor para obtener las listas ordenadas
        while (cursorListasOrdenadas.moveToNext()) {
            @SuppressLint("Range") int idLista = cursorListasOrdenadas.getInt(cursorListasOrdenadas.getColumnIndex("id_list"));
            @SuppressLint("Range") String nombreLista = cursorListasOrdenadas.getString(cursorListasOrdenadas.getColumnIndex("nombre"));
            @SuppressLint("Range") String fechaLista = cursorListasOrdenadas.getString(cursorListasOrdenadas.getColumnIndex("fecha"));

            listasOrdenadas.add(new Lista(idLista, nombreLista, fechaLista));
        }

        // Cierra el cursor y la base de datos
        cursorListasOrdenadas.close();
        db.close();

        return listasOrdenadas;
    }
}
