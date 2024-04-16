package com.example.zippy0001;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class activityCriarNome extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "email"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_SENHA = "Senha"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_FONE = "Fone"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_NOME = "NomeCompleto"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_DATA = "DataNascimento"; // Variavel que envia o texto para a tela seguinte
    private String email; //Variavel para receber o texto das telas anteriores
    private String Senha; //Variavel para receber o texto das telas anteriores

    private EditText nomeCompleto, fone, dataNascimento; //Instanciando as variaveis
    TextInputLayout nomeLayout, foneLayout, dataLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nome);


        nomeCompleto = findViewById(R.id.txtInserirNome); //EditText do Nome do cliente
        fone = findViewById(R.id.txtTelefone); //EditText do Telefone do cliente
        dataNascimento = findViewById(R.id.txtDataNasc); //EditText da Data de Nascimento do cliente
        Button Continuar = findViewById(R.id.btnContinuarNome); // Botão de prosseguir para a prox. tela

        nomeLayout = findViewById(R.id.layoutInserirNomeEdit);
        foneLayout = findViewById(R.id.layoutTelefone);
        dataLayout = findViewById(R.id.layoutDataNasc);

        email = getIntent().getStringExtra(EXTRA_EMAIL); //Recebe o texto da tela anterior e transforma em String
        Senha = getIntent().getStringExtra(EXTRA_SENHA); //Recebe o texto da tela anterior e transforma em String

        Continuar.setOnClickListener(new View.OnClickListener() { // Evento Botão
            @Override
            public void onClick(View v) {
                validarNomes();
            }
        });

    }

    public boolean validarNomes() {


        String NomeCompleto = nomeCompleto.getText().toString().trim(); //Recebe o Nome do EditText
        String DataNascimento = dataNascimento.getText().toString().trim(); //Recebe a data de Nascimento do EditText
        String Fone = fone.getText().toString().trim(); //Recebe o telefone do EditText

        if (NomeCompleto.isEmpty()) {
            // Mostrar uma mensagem de erro
            nomeLayout.setError("Por favor, insira o seu nome completo");
            return false;
        } else if (Fone.isEmpty()) {
            // Mostrar uma mensagem de erro de email inválido
            foneLayout.setError("Por favor, insira seu telefone");
            return false;
        } else if (DataNascimento.isEmpty()) {
            // Mostrar uma mensagem de erro de email inválido
            dataLayout.setError("Por favor, insira sua data de nascimento");
            return false;
        } else {
            // Verificar se o email existe no banco de dados
            nomeLayout.setError(null);
            dataLayout.setError(null);
            foneLayout.setError(null);
            enviarDados(NomeCompleto, DataNascimento, Fone);
            return true;

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    nomeLayout.setError(null);
                    foneLayout.setError(null);
                    dataLayout.setError(null);


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    private void enviarDados(String NomeCompleto, String DataNascimento, String Fone) {

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