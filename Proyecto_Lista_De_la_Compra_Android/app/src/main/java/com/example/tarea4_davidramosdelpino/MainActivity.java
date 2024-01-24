package com.example.tarea4_davidramosdelpino;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea4_davidramosdelpino.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SQLiteDatabase db;

    private static final int PERMISO_CONTACTOS_Y_SMS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top-level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_listasCompartir)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Crear o abrir la base de datos directamente
        db = openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

        // Crear la tabla ListaCompra con una columna de fecha
        db.execSQL("CREATE TABLE IF NOT EXISTS lista_compra " +
                "(id_list INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(255), fecha TEXT);");


        // Crear la tabla Producto
        db.execSQL("CREATE TABLE IF NOT EXISTS producto " +
                "(id_prod INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(255),descripcion VARCHAR(255), " +
                "url_imagen VARCHAR(255),precio INTEGER, veces_comprado INTEGER);");

        // Crear la tabla ListaProducto (relación muchos a muchos)
        db.execSQL("CREATE TABLE IF NOT EXISTS lista_producto " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_lista INTEGER, id_producto INTEGER,cantidad INTEGER, " +
                "FOREIGN KEY (id_lista) REFERENCES lista_compra(id_list), " +
                "FOREIGN KEY (id_producto) REFERENCES producto(id_prod));");

        // Verificar si se tienen los permisos, sino solicitarlos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
            // Ambos permisos ya están concedidos
            leerContactos();
        } else {
            // Al menos uno de los permisos no está concedido, solicitar ambos.
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                    PERMISO_CONTACTOS_Y_SMS
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // Obtén el fragmento actual en el contenedor
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(android.R.id.content);

        // Verifica si hay fragmentos en la pila
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila, realiza la navegación hacia atrás
            getSupportFragmentManager().popBackStack();
        } else {
            // Si no hay fragmentos en la pila, realiza la acción predeterminada de retroceder
            super.onBackPressed();
        }

        // Oculta el fragmento actual si es necesario
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();
        }
    }

    private void leerContactos() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String nombre = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") int tieneTelefono = cursor.getInt(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                @SuppressLint("Range") long id = cursor.getLong(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID));

                String telefono = obtenerNumeroTelefono(id);

                ListaContactos.contactos.add(new Contacto(nombre, tieneTelefono, id, telefono));
            }
            cursor.close();
        }
    }

    // Método para obtener el primer número de teléfono asociado a un contacto
    @SuppressLint("Range")
    private String obtenerNumeroTelefono(long contactId) {
        String numeroTelefono = null;

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                null, null);

        if (phones != null && phones.moveToFirst()) {
            numeroTelefono = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phones.close();
        }

        return numeroTelefono;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISO_CONTACTOS_Y_SMS) {
            if (grantResults.length > 0) {
                // Verificar ambos resultados de permisos
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso de contactos concedido
                    leerContactos();
                } else {
                    Log.d("PermisoContacto", "Permiso para Contactos Denegado. Tú no puedes acceder a Contactos.");
                }

                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso de SMS concedido, ahora puedes enviar SMS.
                } else {
                    Log.d("PermisoSMS", "Permisos para SMS denegados. Tú no puedes enviar SMS.");
                }
            }
        }
    }
}