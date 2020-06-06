package com.example.inventario.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.DialogFragment;

import com.example.inventario.Home;
import com.example.inventario.R;
import com.example.inventario.ui.gallery.GalleryFragment;
import com.example.inventario.ui.slideshow.SlideshowFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }


    private HomeViewModel homeViewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_home, container, false);
     ImageButton document= (ImageButton) view.findViewById(R.id.btn_doc);
        document.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){

              GalleryFragment galleryFragment = new GalleryFragment();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new GalleryFragment());
                fr.commit();
                ((Home) getActivity()).getSupportActionBar().setTitle("Documentos");

            }
        });

        return view;
    }







}
