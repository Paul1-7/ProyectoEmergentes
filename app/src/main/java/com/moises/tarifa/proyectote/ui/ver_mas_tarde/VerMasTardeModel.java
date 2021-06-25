package com.moises.tarifa.proyectote.ui.ver_mas_tarde;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerMasTardeModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerMasTardeModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragmento Ver Mas Tarde");
    }

    public LiveData<String> getText() {
        return mText;
    }
}