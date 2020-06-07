package com.example.inventario.ui.equipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.inventario.R;

public class EquiposFragment extends Fragment {

    private EquiposViewModel equiposViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        equiposViewModel =
                ViewModelProviders.of(this).get(EquiposViewModel.class);
        View root = inflater.inflate(R.layout.fragment_equipos, container, false);
       // final TextView textView = root.findViewById(R.id.text_slideshow);
        equiposViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        return root;
    }
}
