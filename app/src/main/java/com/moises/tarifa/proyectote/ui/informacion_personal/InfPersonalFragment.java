package com.moises.tarifa.proyectote.ui.informacion_personal;

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

public class InfPersonalFragment extends Fragment {

    private InfPersonalModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(InfPersonalModel.class);
        View root = inflater.inflate(R.layout.fragment_inf_personal, container, false);
        final TextView textView = root.findViewById(R.id.text_infpersonal);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}