package com.example.tarea4_davidramosdelpino;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyContactoRecyclerViewAdapter extends RecyclerView.Adapter<MyContactoRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Contacto> contactos;

    private Context context;
    private int idLista;

    public MyContactoRecyclerViewAdapter(ArrayList<Contacto> contactos, Context context, int idLista) {
        this.contactos = contactos;
        this.context = context;
        this.idLista = idLista;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto contacto = contactos.get(position);
        holder.nombreTextView.setText(contacto.getNombre());
        holder.numero.setText(String.valueOf(contacto.getTelefono()));
        getAsyncFoto(holder.fotoImageView, contacto.getId());
        holder.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSMS(contacto);
            }
        });
        holder.btnEnviarWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPorWhatsapp(contacto);
            }
        });
    }


    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView fotoImageView;
        public TextView nombreTextView;
        public TextView numero;

        public Button btnEnviar;
        public Button btnEnviarWhatsapp;

        public ViewHolder(View view) {
            super(view);
            fotoImageView = view.findViewById(R.id.foto);
            nombreTextView = view.findViewById(R.id.nombrecontacto);
            numero = view.findViewById(R.id.item_number);
            btnEnviar = view.findViewById(R.id.boton_enviar_lista);
            btnEnviarWhatsapp = view.findViewById(R.id.btnEnviarWhatsapp);
        }
    }

    public Bitmap getFoto(long id) {

        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), contactUri, true);
        return BitmapFactory.decodeStream(is);
    }

    private void getAsyncFoto(ImageView mFoto, Long id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Trabajo en Background aqu�
                Bitmap foto = getFoto(id);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Trabajo en la interfaz de usuario aqu�
                        mFoto.setImageBitmap(foto);

                    }
                });
            }
        });
    }
    // Método para enviar SMS con información de la lista y productos
    private void enviarSMS(Contacto contacto) {
        // Obtener la información de la lista y productos
        String mensaje = obtenerInformacionLista();

        // Utiliza un Intent para abrir la aplicación de SMS
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + contacto.telefono));
        intent.putExtra("sms_body", mensaje);
        context.startActivity(intent);
    }

    // Método para obtener información de la lista y productos
    private String obtenerInformacionLista() {
        StringBuilder mensaje = new StringBuilder();

        // Consultar la lista y productos asociados
        String query = "SELECT l.nombre AS nombreLista, p.nombre AS nombreProducto, lp.cantidad " +
                "FROM lista_compra l " +
                "JOIN lista_producto lp ON l.id_list = lp.id_lista " +
                "JOIN producto p ON lp.id_producto = p.id_prod " +
                "WHERE l.id_list = ?";

        SQLiteDatabase db = context.openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idLista)});

        // Verificar si hay resultados en la consulta
        if (cursor.moveToFirst()) {
            // Obtener el nombre de la lista
            @SuppressLint("Range") String nombreLista = cursor.getString(cursor.getColumnIndex("nombreLista"));
            mensaje.append("Lista: ").append(nombreLista).append("\n\n");

            // Agregar productos y cantidades al mensaje
            do {
                @SuppressLint("Range") String nombreProducto = cursor.getString(cursor.getColumnIndex("nombreProducto"));
                @SuppressLint("Range") int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));

                mensaje.append(nombreProducto).append(": ").append(cantidad).append(" unidades\n");
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return mensaje.toString();
    }

    private void enviarPorWhatsapp(Contacto contacto) {
        String phoneNumberWithCountryCode = "+34" + contacto.getTelefono();
        String message = obtenerInformacionLista();

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                        phoneNumberWithCountryCode, message)));

        context.startActivity(intent);
    }


}
