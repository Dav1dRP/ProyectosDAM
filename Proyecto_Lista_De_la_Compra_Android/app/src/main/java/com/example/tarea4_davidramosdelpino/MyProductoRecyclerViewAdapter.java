package com.example.tarea4_davidramosdelpino;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea4_davidramosdelpino.databinding.FragmentProductoBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.widget.Toast;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProductoRecyclerViewAdapter extends RecyclerView.Adapter<MyProductoRecyclerViewAdapter.ViewHolder> {

    private final List<Productos> mValues;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private int idLista;  // Almacena el ID de la lista actual
    private Context mContext;  // Almacena el contexto

    public MyProductoRecyclerViewAdapter(Context context, List<Productos> items, int idLista) {
        mContext = context;
        mValues = items;
        this.idLista = idLista;
    }
    public MyProductoRecyclerViewAdapter(List<Productos> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentProductoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int adapterPosition = position;
        holder.mItem = mValues.get(position);
        holder.nombre.setText(mValues.get(position).getNombre());
        holder.descripcion.setText(mValues.get(position).getDescripcion());
        holder.precio.setText(String.valueOf(mValues.get(position).getPrecio()));

        // Verifica si hay una URL de imagen válida antes de intentar cargarla
        if (mValues.get(adapterPosition).getUrlImagen() != null && !mValues.get(adapterPosition).getUrlImagen().isEmpty()) {
            // Utiliza ExecutorService para cargar la imagen en background
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Trabajo en Background aquí
                    final Bitmap bitmap = obtenerBitmapDesdeUrl(mValues.get(adapterPosition).getUrlImagen());

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

        // Configurar el click del botón
        holder.botonAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para insertar en la base de datos utilizando el ID de la lista
                String cantidadStr = holder.cantidad.getText().toString();
                insertarEnBD(idLista, holder.mItem.getNombre(), Integer.parseInt(cantidadStr));
            }
        });
        holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para insertar en la base de datos utilizando el ID de la lista
                String cantidadStr = holder.cantidad.getText().toString();
                int cantidad=-(Integer.parseInt(cantidadStr));
                insertarEnBD(idLista, holder.mItem.getNombre(), cantidad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nombre;
        public final TextView descripcion;
        public final TextView precio;
        public final TextView cantidad;
        public final ImageView imagen;
        public final Button botonAñadir;
        public final Button botonEliminar;
        public Productos mItem;

        public ViewHolder(FragmentProductoBinding binding) {
            super(binding.getRoot());
            nombre = binding.nombre;
            descripcion = binding.descripcion;
            precio = binding.precio;
            cantidad = binding.cantidad;
            imagen=binding.imagen;
            botonAñadir=binding.btnAddProd;
            botonEliminar= binding.btnEliminar;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombre + "'";
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

    private void insertarEnBD(int idLista, String nombreProducto, int cantidad) {
        try {

            SQLiteDatabase db = mContext.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

            // Consulta para obtener el id del producto por nombre
            Cursor cursorProducto = db.rawQuery("SELECT id_prod FROM producto WHERE nombre = ?", new String[]{nombreProducto});

            if (cursorProducto != null && cursorProducto.moveToFirst()) {
                @SuppressLint("Range") int idProducto = cursorProducto.getInt(cursorProducto.getColumnIndex("id_prod"));

                Log.d("MyProductoAdapter", "ID del producto (" + nombreProducto + "): " + idProducto);

                // Comprueba si el producto ya existe en la lista
                Cursor cursorListaProducto = db.rawQuery("SELECT * FROM lista_producto WHERE id_lista = ? AND id_producto = ?",
                        new String[]{String.valueOf(idLista), String.valueOf(idProducto)});

                if (cursorListaProducto != null && cursorListaProducto.getCount() > 0) {
                    // El producto ya existe en la lista, actualiza la cantidad
                    cursorListaProducto.moveToFirst();
                    @SuppressLint("Range") int vecesComprado = cursorListaProducto.getInt(cursorListaProducto.getColumnIndex("cantidad"));
                    vecesComprado = vecesComprado + cantidad;

                    // Actualiza la cantidad en la base de datos
                    db.execSQL("UPDATE lista_producto SET cantidad = ? WHERE id_lista = ? AND id_producto = ?",
                            new String[]{String.valueOf(vecesComprado), String.valueOf(idLista), String.valueOf(idProducto)});

                    Log.d("MyProductoAdapter", "Producto actualizado en la lista");
                } else {
                    if (cantidad > 0) {
                        // El producto no existe en la lista, inserta un nuevo registro
                        db.execSQL("INSERT INTO lista_producto (id_lista, id_producto, cantidad) VALUES (?, ?, ?)",
                                new String[]{String.valueOf(idLista), String.valueOf(idProducto), String.valueOf(cantidad)});

                        Log.d("MyProductoAdapter", "Producto insertado en la lista");
                    }
                }

                // Cierra el cursor de lista_producto
                if (cursorListaProducto != null) {
                    cursorListaProducto.close();
                }

                // Actualiza las veces compradas en la tabla de productos usando ProductosContent
                ProductosContent.actualizarVecesCompradoEnBD(nombreProducto, mContext);

                // Cierra el cursor de producto y la conexión a la base de datos
                if (cursorProducto != null) {
                    cursorProducto.close();
                }
                db.close();

                // Puedes mostrar un mensaje o realizar otras acciones después de la inserción;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Maneja cualquier excepción que pueda ocurrir durante la inserción
        }
    }


}
