package com.example.tarea4_davidramosdelpino.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tarea4_davidramosdelpino.DialogoListas;
import com.example.tarea4_davidramosdelpino.Lista;
import com.example.tarea4_davidramosdelpino.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private SQLiteDatabase db;  // Asegúrate de tener la instancia de tu base de datos aquí

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        db = getActivity().openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);

        // Consulta para obtener las listas recientes ordenadas
        String queryListasOrdenadas = "SELECT * FROM lista_compra ORDER BY id_list DESC";
        Cursor cursorListasOrdenadas = db.rawQuery(queryListasOrdenadas, null);

        List<Lista> listasRecientes = new ArrayList<>();

        // Itera sobre el cursor para obtener las listas recientes
        while (cursorListasOrdenadas.moveToNext()) {
            @SuppressLint("Range") int idLista = cursorListasOrdenadas.getInt(cursorListasOrdenadas.getColumnIndex("id_list"));
            @SuppressLint("Range") String nombreLista = cursorListasOrdenadas.getString(cursorListasOrdenadas.getColumnIndex("nombre"));
            @SuppressLint("Range") String fechaLista = cursorListasOrdenadas.getString(cursorListasOrdenadas.getColumnIndex("fecha"));

            listasRecientes.add(new Lista(idLista, nombreLista, fechaLista));
        }

        // Cierra el cursor
        cursorListasOrdenadas.close();

        // Mostrar el diálogo con la lista de listas recientes
        DialogoListas dialogoListas = new DialogoListas(getActivity(), listasRecientes);
        dialogoListas.show();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Cierra la base de datos al destruir la vista
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
