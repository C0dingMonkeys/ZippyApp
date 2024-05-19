package com.example.zippy0001;

import static android.content.Context.MODE_PRIVATE;

import static androidx.fragment.app.FragmentManagerKt.commit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilFragment extends Fragment {
    public static final String SHARED_PREFS = "sharedPrefs";

    private CircleImageView Foto;
    String BASE_URL_IMAGEM = "https://zippyinternacional.com/Android/img/";

    public static final String EXTRA_BACK_PERFIL = "voltar_perfil";



    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);


    }
    public static String substituirCarcteres(String str) {
        if (str.length() < 5) {
            return str; // String is too short, return it as is
        }

        String first5Chars = str.substring(0, 5); // Extract the first 5 characters
        String asterisks = "****"; // Create a string of 5 asterisks
        String remainingChars = str.substring(5); // Extract the remaining characters

        return asterisks + remainingChars; // Combine the asterisks and remaining characters
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();



    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String nomeCliente = sharedPreferences.getString("nomeCliente", "");
        String sobrenomeCliente = sharedPreferences.getString("sobrenomeCliente", "");
        String emailUsuario = sharedPreferences.getString("email", "");
        String idUsuario = sharedPreferences.getString("id_usuario", "");
        String fotoPerfil = sharedPreferences.getString("fotoPerfilUsuario", "");
        String emailModificado = substituirCarcteres(emailUsuario);

        TextView txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        TextView txtEmailUsuario = view.findViewById(R.id.txtEmailUsuario);
        txtNomeUsuario.setText(requireActivity().getString(R.string.perfil_nome, nomeCliente, sobrenomeCliente));
        txtEmailUsuario.setText(emailModificado);

        Foto = view.findViewById(R.id.imgFotoPerfil);
        Picasso.get().load(BASE_URL_IMAGEM+fotoPerfil).into(Foto);

        AppCompatButton editarPerfil;
        editarPerfil = view.findViewById(R.id.btnEditarPerfil_perfil);

        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activityEditarPerfil.class);
                intent.putExtra(EXTRA_BACK_PERFIL, "PerfilFragment");
                startActivity(intent);
            }
        });
    }
}