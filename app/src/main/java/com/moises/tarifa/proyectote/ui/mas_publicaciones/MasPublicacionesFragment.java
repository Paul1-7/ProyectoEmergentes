package com.moises.tarifa.proyectote.ui.mas_publicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.moises.tarifa.proyectote.R;

public class MasPublicacionesFragment extends Fragment {

    private MasPublicacionesModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(MasPublicacionesModel.class);
        View root = inflater.inflate(R.layout.fragment_mas_publicaciones, container, false);
        final TextView textView = root.findViewById(R.id.txt_publicaciones);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}