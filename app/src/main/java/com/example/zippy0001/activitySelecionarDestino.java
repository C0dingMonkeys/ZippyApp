package com.example.zippy0001;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class activitySelecionarDestino extends AppCompatActivity {
    public static final String EXTRA_ORIGEM = "origem"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_DESTINO = "destino"; // Variavel que envia o texto para a tela seguinte
    ImageButton btnVoltar;
    TextInputEditText txtPaisDestino, txtPaisOrigem;
    TextInputLayout lytPaisDestino, lytPaisOrigem;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_destino);

        txtPaisDestino = findViewById(R.id.txtDestinoOrigem);
        txtPaisOrigem = findViewById(R.id.txtDestinoChegada);

        lytPaisDestino = findViewById(R.id.layoutDestinoChegada);
        lytPaisOrigem = findViewById(R.id.layoutDestinoOrigem);

        btnContinuar = findViewById(R.id.btnContinuarDestino);
        btnVoltar = findViewById(R.id.btnvoltarSolicitacoes);



        btnContinuar.setOnClickListener(v -> {

            String PaisOrigem = txtPaisOrigem.getText().toString().trim();
            String PaisDestino = txtPaisDestino.getText().toString().trim();

            if (PaisOrigem.isEmpty()) {
                lytPaisOrigem.setError("Insira um País");
            }
            if (PaisDestino.isEmpty()) {
                lytPaisDestino.setError("Insira um País");
            } else {
                lytPaisOrigem.setError(null);
                lytPaisDestino.setError(null);
                Intent intent = new Intent(activitySelecionarDestino.this , PostActivity.class);
                intent.putExtra(EXTRA_ORIGEM, PaisOrigem);
                intent.putExtra(EXTRA_DESTINO, PaisDestino);
                startActivity(intent);
            }
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(activitySelecionarDestino.this, activityInicio.class);
                startActivity(intent);
            }
        });


    }
}