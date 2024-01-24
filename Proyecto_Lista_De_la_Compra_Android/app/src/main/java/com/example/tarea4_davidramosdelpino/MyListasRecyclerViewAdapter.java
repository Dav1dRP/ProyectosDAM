package com.example.tarea4_davidramosdelpino;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyListasRecyclerViewAdapter extends RecyclerView.Adapter<MyListasRecyclerViewAdapter.ViewHolder> {

    private List<Lista> listas;
    private AppCompatActivity appCompatActivity;
    private DialogoListas dialogoListas;

    public MyListasRecyclerViewAdapter(List<Lista> listas, AppCompatActivity activity, DialogoListas dialogoListas) {
        this.listas = listas;
        this.appCompatActivity = activity;
        this.dialogoListas = dialogoListas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int posicion=position;
        Lista lista = listas.get(position);
        holder.textNombreLista.setText(lista.getNombre());
        holder.textFechaLista.setText(lista.getFecha());
        holder.textId.setText(String.valueOf(lista.getId()));
        holder.btnVisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirVisualizarListaFragment(lista.getId());
            }
        });
        holder.btnModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirProductoFragment(lista.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public Lista getItemAtPosition(int position) {
        return listas.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textNombreLista;
        TextView textFechaLista;
        TextView textId;
        Button btnModif;
        Button btnVisu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreLista = itemView.findViewById(R.id.textNombreLista);
            textFechaLista = itemView.findViewById(R.id.textFechaLista);
            textId = itemView.findViewById(R.id.id);
            btnModif = itemView.findViewById(R.id.btnModif);
            btnVisu = itemView.findViewById(R.id.btnVisual);
        }
    }

    // Dentro de MyListasRecyclerViewAdapter

    private void abrirProductoFragment(int idListaSeleccionada) {
        // Abre el fragmento de productos y pasa la id de la lista seleccionada
        ProductoFragment productoFragment = new ProductoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("idLista", idListaSeleccionada);
        productoFragment.setArguments(bundle);

        // Reemplaza el fragmento actual con el fragmento de productos
        if (appCompatActivity != null) {
            appCompatActivity.getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, productoFragment)
                    .addToBackStack(null)
                    .commit();

            // Cierra el diálogo después de realizar la transacción
            if (dialogoListas != null && dialogoListas.isShowing()) {
                dialogoListas.dismiss();
            }
        }
    }

    private void abrirVisualizarListaFragment(int idListaSeleccionada) {
        // Abre el fragmento de visualización de la lista y pasa la id de la lista seleccionada
        List<Productos> listaProductos = obtenerListaProductosParaListaSeleccionada(appCompatActivity.getApplicationContext(), idListaSeleccionada);
        VisualizarListaFragment visualizarListaFragment = new VisualizarListaFragment(listaProductos, idListaSeleccionada);

        // Reemplaza el fragmento actual con el fragmento de visualización de la lista
        if (appCompatActivity != null) {
            appCompatActivity.getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, visualizarListaFragment)
                    .addToBackStack(null)
                    .commit();

            // Cierra el diálogo después de realizar la transacción
            if (dialogoListas != null && dialogoListas.isShowing()) {
                dialogoListas.dismiss();
            }
        }
    }


    private List<Productos> obtenerListaProductosParaListaSeleccionada(Context context, int idLista) {
        List<Productos> listaProductos = new ArrayList<>();

        try {
            SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

            String query = "SELECT p.nombre, p.descripcion, p.precio, p.url_imagen " +
                    "FROM producto p " +
                    "JOIN lista_producto lp ON p.id_prod = lp.id_producto " +
                    "WHERE lp.id_lista = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idLista)});

            while (cursor.moveToNext()) {
                @SuppressLint("Range")String nombreProducto = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range")String descripcionProducto = cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range")double precioProducto = cursor.getDouble(cursor.getColumnIndex("precio"));
                @SuppressLint("Range")String urlImagenProducto = cursor.getString(cursor.getColumnIndex("url_imagen"));

                Productos producto = new Productos(nombreProducto, descripcionProducto, urlImagenProducto, (int) precioProducto);
                Log.d("PruebaProducto", "Producto Añadido: " + producto.toString());
                listaProductos.add(producto);
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductos;
    }
}
