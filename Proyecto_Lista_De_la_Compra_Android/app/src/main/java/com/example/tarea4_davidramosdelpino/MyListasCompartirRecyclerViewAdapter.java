package com.example.tarea4_davidramosdelpino;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyListasCompartirRecyclerViewAdapter extends RecyclerView.Adapter<MyListasCompartirRecyclerViewAdapter.ViewHolder> {

    private List<Lista> listas;
    private Context context;

    private RecyclerView recyclerView;
    public MyListasCompartirRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void setListas(List<Lista> listas) {
        this.listas = listas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_compartir_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lista lista = listas.get(position);
        holder.textIdLista.setText(String.valueOf(lista.getId()));
        holder.textNombreLista.setText(lista.getNombre());
        holder.textFechaLista.setText(lista.getFecha());

        // Configura el evento del botón enviar
        holder.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                // Abre el fragmento Compartir y pasa la id de la lista seleccionada
                int idListaSeleccionada = lista.getId();
                abrirCompartirFragment(idListaSeleccionada);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listas != null ? listas.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIdLista;
        TextView textNombreLista;
        TextView textFechaLista;
        Button btnEnviar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textIdLista = itemView.findViewById(R.id.textIdLista);
            textNombreLista = itemView.findViewById(R.id.textNombreLista);
            textFechaLista = itemView.findViewById(R.id.textFechaLista);
            btnEnviar = itemView.findViewById(R.id.btnEnviar);
        }
    }

    private void abrirCompartirFragment(int idListaSeleccionada) {
        // Abre el fragmento Compartir y pasa la id de la lista seleccionada
        CompartirFragment compartirFragment = new CompartirFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("idLista", idListaSeleccionada);
        compartirFragment.setArguments(bundle);

        // Reemplaza el fragmento actual con el fragmento de Compartir
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorListasCompartir, compartirFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
