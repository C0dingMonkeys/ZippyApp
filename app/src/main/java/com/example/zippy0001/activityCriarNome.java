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
    public static final String EXTRA_SENHA = "senha"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_TEL = "tel"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_NOME = "nome"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_DATA = "dtaNasc"; // Variavel que envia o texto para a tela seguinte
    private static final String EXTRA_SOBRENOME = "sobrenome";
    private String email, senha; //Variavel para receber o texto das telas anteriores
    private EditText nomeCompleto, sobrenome, tel, dataNascimento; //Instanciando as variaveis
    private TextInputLayout nomeLayout,sobrenomeLayout, telLayout, dataLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nome);


        nomeCompleto = findViewById(R.id.txtInserirNome); //EditText do Nome do cliente
        sobrenome = findViewById(R.id.txtInserirSobrenome);
        tel = findViewById(R.id.txtTelefone); //EditText do Telefone do cliente
        dataNascimento = findViewById(R.id.txtDataNasc); //EditText da Data de Nascimento do cliente
        Button Continuar = findViewById(R.id.btnContinuarNome); // Botão de prosseguir para a prox. tela

        nomeLayout = findViewById(R.id.layoutInserirNomeEdit);
        sobrenomeLayout = findViewById(R.id.layoutInserirSobrenomeEdit);
        telLayout = findViewById(R.id.layoutTelefone);
        dataLayout = findViewById(R.id.layoutDataNasc);

        email = getIntent().getStringExtra(EXTRA_EMAIL); //Recebe o texto da tela anterior e transforma em String
        senha = getIntent().getStringExtra(EXTRA_SENHA); //Recebe o texto da tela anterior e transforma em String

        Continuar.setOnClickListener(new View.OnClickListener() { // Evento Botão
            @Override
            public void onClick(View v) {
                validarNomes();
            }
        });

    }
    public void validarNomes() {


        String NomeCompleto = nomeCompleto.getText().toString().trim(); //Recebe o Nome do EditText
        String Sobrenome = sobrenome.getText().toString().trim(); // Recebe o Sobrenome do EditText
        String DataNascimento = dataNascimento.getText().toString().trim(); //Recebe a data de Nascimento do EditText
        String Fone = tel.getText().toString().trim(); //Recebe o telefone do EditText

        if (NomeCompleto.isEmpty()) {
            // Mostrar uma mensagem de erro
            nomeLayout.setError("Por favor, insira o seu nome completo");

        }
        if (Sobrenome.isEmpty()) {
            // Mostrar uma mensagem de erro de email inválido
            sobrenomeLayout.setError("Por favor, insira seu Sobrenome");

        }
        if (Fone.isEmpty()) {
            // Mostrar uma mensagem de erro de email inválido
            telLayout.setError("Por favor, insira seu telefone");

        }
        if (DataNascimento.isEmpty()) {
            // Mostrar uma mensagem de erro de email inválido
            dataLayout.setError("Por favor, insira sua data de nascimento");

        } else {
            // Verificar se o email existe no banco de dados
            nomeLayout.setError(null);
            dataLayout.setError(null);
            sobrenomeLayout.setError(null);
            telLayout.setError(null);
            enviarDados(NomeCompleto, Sobrenome, DataNascimento, Fone);

        }
    }
    private void enviarDados(String NomeCompleto, String Sobrenome, String DataNascimento, String Tel) {

        Intent intent = new Intent(activityCriarNome.this, activityIdentidade.class);
        intent.putExtra(EXTRA_EMAIL, email);// Passar o email como extra
        intent.putExtra(EXTRA_SENHA, senha); // Passar o senha como extra
        intent.putExtra(EXTRA_NOME, NomeCompleto);// Passar o Nome como extra
        intent.putExtra(EXTRA_SOBRENOME, Sobrenome); // Passar o Sobrenome como extra
        intent.putExtra(EXTRA_TEL, Tel);// Passar o telefone como extra
        intent.putExtra(EXTRA_DATA, DataNascimento); // Passar Data de Nascimento como extra
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    sobrenomeLayout.setError(null);
                    telLayout.setError(null);
                    dataLayout.setError(null);



                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}