package com.example.tarea4_davidramosdelpino;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductoFragment extends Fragment {

    private int idLista; // Variable para almacenar el ID de la lista

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_list, container, false);

        // Obtener el ID de la lista del argumento
        idLista = getArguments().getInt("idLista", -1);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        // Cargar productos desde JSON
        ProductosContent.cargarProductosDesdeJSON(getContext(), recyclerView);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyProductoRecyclerViewAdapter(getContext(), ProductosContent.ITEMS, idLista));

        return view;
    }
}

