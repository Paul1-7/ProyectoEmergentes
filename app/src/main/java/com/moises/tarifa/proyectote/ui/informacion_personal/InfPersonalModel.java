package com.moises.tarifa.proyectote.ui.informacion_personal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfPersonalModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfPersonalModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragmento Informacion Personal");
    }

    public LiveData<String> getText() {
        return mText;
    }
}