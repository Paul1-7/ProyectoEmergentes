package com.moises.tarifa.proyectote.ui.mas_publicaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MasPublicacionesModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MasPublicacionesModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment Mas Publicaciones");
    }

    public LiveData<String> getText() {
        return mText;
    }
}