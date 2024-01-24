package com.example.tarea4_davidramosdelpino;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DialogoListas extends Dialog {

    private Context context;
    private List<Lista> listasRecientes;
    private RecyclerView recyclerViewListas;

    public DialogoListas(@NonNull Context context, List<Lista> recentLists) {
        super(context);
        this.context = context;
        this.listasRecientes = recentLists;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_listas);

        recyclerViewListas = findViewById(R.id.recyclerViewListas);

        // Configurar el RecyclerView con el adaptador
        MyListasRecyclerViewAdapter adaptadorListas = new MyListasRecyclerViewAdapter(listasRecientes, (AppCompatActivity) context,this);
        recyclerViewListas.setAdapter(adaptadorListas);
        recyclerViewListas.setLayoutManager(new LinearLayoutManager(context));

    }
}
