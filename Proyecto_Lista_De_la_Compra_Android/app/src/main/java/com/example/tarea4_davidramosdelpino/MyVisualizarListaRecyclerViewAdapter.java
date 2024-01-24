package com.example.tarea4_davidramosdelpino;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyVisualizarListaRecyclerViewAdapter extends RecyclerView.Adapter<MyVisualizarListaRecyclerViewAdapter.ViewHolder> {

    private List<Productos> listaProductos;
    private int idLista;  // ID de la lista actual

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private Context mContext;

    public MyVisualizarListaRecyclerViewAdapter(Context context, List<Productos> items, int idLista) {
        mContext = context;
        listaProductos = items;
        this.idLista = idLista;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visualizar_lista_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Productos producto = listaProductos.get(position);
        final int adapterPosition = position;

        // Verifica si hay una URL de imagen válida antes de intentar cargarla
        if (listaProductos.get(position).getUrlImagen() != null && !listaProductos.get(position).getUrlImagen().isEmpty()) {
            // Utiliza ExecutorService para cargar la imagen en background
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Trabajo en Background aquí
                    final Bitmap bitmap = obtenerBitmapDesdeUrl(listaProductos.get(adapterPosition).getUrlImagen());

                    // Actualiza la interfaz de usuario en el hilo principal
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Trabajo en la interfaz de usuario aquí
                            if (bitmap != null) {
                                holder.imagen.setImageBitmap(bitmap);
                            } else {
                                // Establece la imagen como nula si no se pudo cargar
                                holder.imagen.setImageDrawable(null);
                            }
                        }
                    });
                }
            });
        } else {
            // Si no hay URL de imagen, establece la imagen como nula
            holder.imagen.setImageDrawable(null);
        }

        // Configura los elementos de la interfaz de usuario con los datos del producto
        holder.nombre.setText(producto.getNombre());

        // Muestra la cantidad asociada al producto en la lista
        holder.cantidad.setText("Cantidad: " + obtenerCantidadParaProducto(producto.getNombre()));

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre;
        TextView cantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenproducto);
            nombre = itemView.findViewById(R.id.nombreproducto);
            cantidad = itemView.findViewById(R.id.cantidadproducto);
        }
    }

    // Este método obtiene la cantidad asociada al producto en la lista actual
    private int obtenerCantidadParaProducto(String nombreProducto) {
        try {
            // Asumiendo que tienes acceso a la instancia de la base de datos
            SQLiteDatabase db = mContext.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

            // Consulta para obtener la cantidad asociada al producto en la lista actual
            Cursor cursor = db.rawQuery("SELECT cantidad FROM lista_producto WHERE id_lista = ? AND id_producto IN (SELECT id_prod FROM producto WHERE nombre = ?)",
                    new String[]{String.valueOf(idLista), nombreProducto});

            // Mover el cursor al primer resultado
            if (cursor.moveToFirst()) {
                // Obtener el valor de la columna "cantidad"
                @SuppressLint("Range") int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));

                // Cierra el cursor
                cursor.close();

                return cantidad;
            }

            // Cierra el cursor
            cursor.close();

            // Retorna 0 si no hay cantidad asociada
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            // Maneja cualquier excepción que pueda ocurrir durante la obtención de la cantidad
            return 0;
        }
    }

    private Bitmap obtenerBitmapDesdeUrl(String imageName) {
        // Construir la URL completa usando la base de URL y el nombre de la imagen
        String baseUrl = "https://fp.cloud.riberadeltajo.es/listacompra/images/";
        String urlString = baseUrl + imageName;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } else {
                Log.e("Imagen", "Error de servidor: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error de conexión
            Log.e("Imagen", "Error de conexión: " + e.getMessage());
            return null;
        }
    }
}
