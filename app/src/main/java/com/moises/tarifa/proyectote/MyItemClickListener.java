package com.moises.tarifa.proyectote;

import android.view.View;

public interface MyItemClickListener<T> {
    public void onItemClickSelected(View vista, T itemSel, int posicion);
}
