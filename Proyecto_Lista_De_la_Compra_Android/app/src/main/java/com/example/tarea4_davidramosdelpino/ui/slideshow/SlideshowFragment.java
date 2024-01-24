package com.example.tarea4_davidramosdelpino.ui.slideshow;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tarea4_davidramosdelpino.MyProductoRecyclerViewAdapter;
import com.example.tarea4_davidramosdelpino.Productos;
import com.example.tarea4_davidramosdelpino.ProductosContent;
import com.example.tarea4_davidramosdelpino.R;
import com.example.tarea4_davidramosdelpino.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;

        db = getActivity().openOrCreateDatabase("ListasCompras", Context.MODE_PRIVATE, null);
        final TextView nombre= binding.nombreProducto;
        final TextView descripcion= binding.descripcionProducto;
        final TextView precio=binding.precioProducto;
        final TextView imagen=binding.urlImagenProd;
        final Button btnCrear=binding.button;
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                // Validar que los campos no estén vacíos
                if (nombre.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty() || precio.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Insertar un nuevo producto en la base de datos
                db.execSQL("INSERT INTO producto (nombre, descripcion, url_imagen, precio) VALUES (?, ?, ?, ?)",
                        new String[]{nombre.getText().toString(), descripcion.getText().toString(), imagen.getText().toString(), String.valueOf(precio.getText())});

                limpiarCamposFormulario();
            }
        });

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void limpiarCamposFormulario() {
        // Limpiar los campos del formulario
        binding.nombreProducto.setText("");
        binding.descripcionProducto.setText("");
        binding.precioProducto.setText("");
        binding.urlImagenProd.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}