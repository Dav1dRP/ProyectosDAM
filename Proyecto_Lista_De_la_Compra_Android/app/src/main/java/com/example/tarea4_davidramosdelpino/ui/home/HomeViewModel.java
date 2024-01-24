package com.example.tarea4_davidramosdelpino.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> nombre;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Nombre de La Lista:");
        nombre = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<String> getNombre() {
        return nombre;
    }
}