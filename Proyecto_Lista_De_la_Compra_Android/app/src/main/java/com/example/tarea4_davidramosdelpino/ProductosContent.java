package com.example.tarea4_davidramosdelpino;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductosContent {
    public static List<Productos> ITEMS = new ArrayList<>();

    public static void cargarProductosDesdeJSON(final Context context, final RecyclerView recyclerView) {
        String url = "https://fp.cloud.riberadeltajo.es/listacompra/listaproductos.json";
        new HttpAsyncTask(context, recyclerView).execute(url);
    }

    private static class HttpAsyncTask extends AsyncTask<String, Void, Void> {
        private Context context;
        private RecyclerView recyclerView;


        public HttpAsyncTask(Context context, RecyclerView recyclerView) {
            this.context = context;
            this.recyclerView = recyclerView;

        }

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                String jsonString = obtenerDatosDeUrl(url);
                List<Productos> productos = parsearJSON(jsonString);

                // Guardar productos en la base de datos si no existen
                for (Productos producto : productos) {
                    if (!productoExisteEnBD(producto.getNombre(), context)) {
                        // Si el producto no existe, agregarlo
                        guardarProductoEnBD(producto, context);
                    } else {
                        // Si el producto ya existe, actualizar la columna veces_comprado
                        actualizarVecesCompradoEnBD(producto.getNombre(), context);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Cargar productos desde la base de datos y actualizar el RecyclerView
            cargarProductosDesdeBD(context, recyclerView);
            Toast.makeText(context, "Productos cargados con éxito", Toast.LENGTH_SHORT).show();
        }

        private String obtenerDatosDeUrl(String urlString) throws IOException {
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Lee el InputStream
                inputStream = urlConnection.getInputStream();
                return convertirInputStreamAString(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }

        private String convertirInputStreamAString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }

        private List<Productos> parsearJSON(String jsonString) throws JSONException {
            List<Productos> listaProductos = new ArrayList<>();

            // Parsea el objeto JSON
            JSONObject jsonObject = new JSONObject(jsonString);

            // Obtiene el array "productos" del objeto JSON
            JSONArray jsonArray = jsonObject.getJSONArray("productos");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProducto = jsonArray.getJSONObject(i);
                String nombre = jsonProducto.getString("nombre");
                String descripcion = jsonProducto.getString("descripcion");
                String urlImagen = jsonProducto.getString("imagen");
                int precio = jsonProducto.getInt("precio");

                // Crea un nuevo producto y agrégalo a la lista
                Productos producto = new Productos(nombre, descripcion, urlImagen, precio);
                listaProductos.add(producto);
            }

            return listaProductos;
        }

        private void guardarProductoEnBD(Productos producto, Context context) {

            SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

            // Obtener la cantidad de veces comprado usando la nueva función
            int vecesComprado = obtenerVecesComprado(producto.getNombre(), context);

            // Utilizar INSERT OR REPLACE para evitar duplicados y actualizar registros existentes
            db.execSQL("INSERT OR REPLACE INTO producto (nombre,descripcion,url_imagen, precio, veces_comprado) VALUES (?, ?, ?,?,?);",
                    new Object[]{producto.getNombre(), producto.getDescripcion(), producto.getUrlImagen(), producto.getPrecio(), vecesComprado});

            // Cerrar la base de datos
            db.close();
        }

        private boolean productoExisteEnBD(String nombreProducto, Context context) {

            SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

            // Consultar la tabla producto para ver si el producto ya existe
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM producto WHERE nombre = ?;",
                    new String[]{nombreProducto});

            // Mover el cursor al primer resultado
            if (cursor.moveToFirst()) {
                // Obtener el valor de la columna COUNT(*)
                int count = cursor.getInt(0);

                // Cerrar el cursor y la base de datos
                cursor.close();
                db.close();

                // Si count es mayor que 0, el producto existe en la base de datos
                return count > 0;
            } else {
                // Cerrar el cursor y la base de datos
                cursor.close();
                db.close();

                // Si hubo un error, asumir que el producto no existe para evitar problemas
                return false;
            }
        }
    }

    private static void cargarProductosDesdeBD(final Context context, final RecyclerView recyclerView) {
        SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

        // Realizar una consulta para obtener todos los productos de la tabla producto
        Cursor cursor = db.rawQuery("SELECT * FROM producto ORDER BY veces_comprado DESC", null);

        // Limpiar el ArrayList antes de agregar nuevos productos
        ITEMS.clear();

        // Agregar productos desde la base de datos al ArrayList
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range") String urlImagen = cursor.getString(cursor.getColumnIndex("url_imagen"));
                @SuppressLint("Range") int precio = cursor.getInt(cursor.getColumnIndex("precio"));

                Productos producto = new Productos(nombre, descripcion, urlImagen, precio);
                ITEMS.add(producto);
            } while (cursor.moveToNext());
        }

        // Notificar al adaptador que los datos han cambiado
        if (recyclerView != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();
    }



    public static int obtenerVecesComprado(String nombreProducto, Context context) {
        int vecesComprado = 0;


        SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

        // Consultar la tabla lista_producto para obtener la cantidad total comprada del producto
        Cursor cursor = db.rawQuery("SELECT SUM(cantidad) FROM lista_producto WHERE id_producto IN (SELECT id_prod FROM producto WHERE nombre = ?);",
                new String[]{nombreProducto});

        // Mover el cursor al primer resultado
        if (cursor.moveToFirst()) {
            // Obtener el valor de la columna SUM(cantidad)
            vecesComprado = cursor.getInt(0);
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        return vecesComprado;
    }

    public static void actualizarVecesCompradoEnBD(String nombreProducto, Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

        int vecesComprado=obtenerVecesComprado(nombreProducto,context);
        // Incrementar la cantidad de veces comprado
        db.execSQL("UPDATE producto SET veces_comprado ="+vecesComprado+" WHERE nombre = ?",
                new Object[]{nombreProducto});

        // Cerrar la base de datos
        db.close();
    }


}

