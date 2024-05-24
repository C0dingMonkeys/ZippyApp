package com.example.zippy0001;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class fragmentConfig extends Fragment {

    SwitchCompat switchModo;
    boolean modoNoturno;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchModo = view.findViewById(R.id.switchDarkLightMode);

        sharedPreferences =  this.requireActivity().getSharedPreferences( "MODE", Context.MODE_PRIVATE);
        modoNoturno = sharedPreferences.getBoolean("modoNoturno", false);
        RelativeLayout minhaConta = view.findViewById(R.id.layout_meusDados_btn);
        RelativeLayout centralAjuda = view.findViewById(R.id.layout_CentraldeAjuda_btn);



        minhaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activityEditarConta.class);
                startActivity(intent);
            }
        });

        centralAjuda.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), activityCentralAjuda.class);
            startActivity(intent);
        });



        if(modoNoturno){
            switchModo.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modoNoturno){
                    switchModo.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("modoNoturno", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("modoNoturno", true);
                }
                editor.apply();
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = new fragmentHome();
// Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();

            }
        });
    }
}