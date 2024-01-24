package com.example.tarea4_davidramosdelpino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tarea4_davidramosdelpino.databinding.FragmentCompartirBinding;

public class CompartirFragment extends Fragment {

    private FragmentCompartirBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompartirBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null) {
            int idLista = bundle.getInt("idLista", -1);

            // Verificar que la ID de la lista sea válida
            if (idLista != -1) {
                // Configurar el RecyclerView y su adaptador
                MyContactoRecyclerViewAdapter adapter = new MyContactoRecyclerViewAdapter(ListaContactos.contactos,getContext(),idLista);
                binding.recyclerviewContactos.setAdapter(adapter);
                binding.recyclerviewContactos.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
