package com.example.tarea4_davidramosdelpino;

// VisualizarListaFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VisualizarListaFragment extends Fragment {

    private RecyclerView recyclerViewVisualizarLista;
    private List<Productos> listaProductos;
    private int idLista;

    public VisualizarListaFragment(List<Productos> listaProductos, int idLista) {
        this.listaProductos = listaProductos;
        this.idLista=idLista;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizar_lista, container, false);

        recyclerViewVisualizarLista = view.findViewById(R.id.recyclerViewVisualizarLista);
        recyclerViewVisualizarLista.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Crea el adaptador para el RecyclerView
        MyVisualizarListaRecyclerViewAdapter adapter = new MyVisualizarListaRecyclerViewAdapter(requireContext(), listaProductos, idLista);
        recyclerViewVisualizarLista.setAdapter(adapter);

        return view;
    }
}