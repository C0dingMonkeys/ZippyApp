package com.example.zippy0001;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class activityCriarNome extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "email"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_SENHA = "Senha"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_FONE = "Fone"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_NOME = "NomeCompleto"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_DATA = "DataNascimento"; // Variavel que envia o texto para a tela seguinte
    private String email; //Variavel para receber o texto das telas anteriores
    private String Senha; //Variavel para receber o texto das telas anteriores

    private EditText nomeCompleto, fone, dataNascimento; //Instanciando as variaveis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nome);

        nomeCompleto = findViewById(R.id.txtInserirNome); //EditText do Nome do cliente
        fone = findViewById(R.id.txtTelefone); //EditText do Telefone do cliente
        dataNascimento = findViewById(R.id.txtDataNasc); //EditText da Data de Nascimento do cliente
        Button Continuar = findViewById(R.id.btnContinuarNome); // Botão de prosseguir para a prox. tela

        email = getIntent().getStringExtra(EXTRA_EMAIL); //Recebe o texto da tela anterior e transforma em String
        Senha = getIntent().getStringExtra(EXTRA_SENHA); //Recebe o texto da tela anterior e transforma em String

        Continuar.setOnClickListener(new View.OnClickListener() { // Evento Botão
            @Override
            public void onClick(View v) {
                enviarDados();
            }
        });

    }

    private void enviarDados() {
        String NomeCompleto = nomeCompleto.getText().toString().trim(); //Recebe o Nome do EditText
        String DataNascimento = dataNascimento.getText().toString().trim(); //Recebe a data de Nascimento do EditText
        String Fone = fone.getText().toString().trim(); //Recebe o telefone do EditText

        Intent intent = new Intent(activityCriarNome.this, InserirIdentidade.class);
        intent.putExtra(EXTRA_EMAIL, email);// Passar o email como extra
        intent.putExtra(EXTRA_SENHA, Senha); // Passar o senha como extra
        intent.putExtra(EXTRA_NOME, NomeCompleto);// Passar o Nome como extra
        intent.putExtra(EXTRA_FONE, Fone);// Passar o telefone como extra
        intent.putExtra(EXTRA_DATA, DataNascimento); // Passar Data de Nascimento como extra
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}