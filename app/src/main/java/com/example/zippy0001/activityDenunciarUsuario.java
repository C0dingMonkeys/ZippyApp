package com.example.zippy0001;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zippy0001.classes.Usuario;

public class activityDenunciarUsuario extends AppCompatActivity {

    Usuario idDenunciado;
    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denunciar_usuario);

        idDenunciado = (Usuario) getIntent().getExtras().getSerializable("id_denunciado");
        Log.d("id_denuncia", "id da Denuncia:" + idDenunciado.getUsuario());

        btnVoltar = findViewById(R.id.btnVoltarDenuncia);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });



    }
}