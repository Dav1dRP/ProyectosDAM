package com.example.tarea4_davidramosdelpino.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tarea4_davidramosdelpino.MainActivity;
import com.example.tarea4_davidramosdelpino.R;
import com.example.tarea4_davidramosdelpino.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = getActivity().openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);
        final TextView textView = binding.textHome;
        final TextView nombre= binding.nombrelista;
        final Button btnCrear=binding.btnCrear;
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                String fechaActual = obtenerFechaActual();

                // Insertar la lista con nombre y fecha actual
                db.execSQL("INSERT INTO lista_compra (nombre, fecha) VALUES ('" + nombre.getText() + "', '" + fechaActual + "')");

                // Obtener el ID de la lista recién insertada
                Cursor cursor = db.rawQuery("SELECT last_insert_rowid() AS id", null);
                int idLista = -1;
                if (cursor.moveToFirst()) {
                    idLista = cursor.getInt(cursor.getColumnIndex("id"));
                }
                cursor.close();
                nombre.setText("");

                // Navegar a la siguiente pantalla y pasar el ID de la lista como argumento
                Bundle bundle = new Bundle();
                bundle.putInt("idLista", idLista);
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_productoFragment, bundle);
            }
        });
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    // Función para obtener la fecha actual
    private String obtenerFechaActual() {
        Cursor cursor = db.rawQuery("SELECT date('now')", null);
        String fechaActual = null;
        if (cursor.moveToFirst()) {
            fechaActual = cursor.getString(0);
        }
        cursor.close();
        return fechaActual;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}