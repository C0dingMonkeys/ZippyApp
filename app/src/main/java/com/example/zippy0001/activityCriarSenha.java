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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class activityCriarSenha extends AppCompatActivity {

    // Chave para receber o email como extra da tela anterior
    public static final String EXTRA_EMAIL = "email"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_SENHA = "senha"; // Variavel que envia o texto para a tela seguinte
    private String email; //Variavel para receber o texto das telas anteriores
    private TextView txtSenha, txtConfsenha;
    private TextInputLayout senhaLayout, confSenhaLayout;//Instaciando variaveis da tela

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.criar_senha);

        txtSenha = findViewById(R.id.txtSenhaCadastro); //EditText da senha
        txtConfsenha = findViewById(R.id.txtConfirmarSenha); //EditText da confirmação de senha
        senhaLayout = findViewById(R.id.layoutSenhaEdit);
        confSenhaLayout = findViewById(R.id.layoutConfSenhaEdit);
        Button continuar = findViewById(R.id.btnContinuar2); //Botão Continuar

        email = getIntent().getStringExtra(EXTRA_EMAIL); //Recebe o texto da tela anterior e transforma em String

        senhaLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    senhaLayout.setError(null);
                }
            }
        });

        confSenhaLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    confSenhaLayout.setError(null);
                }
            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarSenha();
            }
        });

    }


    public void validarSenha() {
        String Senha = txtSenha.getText().toString().trim(); //Recebe a senha e transforma em String
        String Confsenha = txtConfsenha.getText().toString().trim(); //Recebe a confirmação de senha e transforma em String

        // Validar a senha
        if (Senha.isEmpty()) { //Se a senha estiver vazia:
            // Mostrar uma mensagem de erro
            senhaLayout.setError("Por favor, insira a sua senha");
        }
        if (Confsenha.isEmpty()) {
            confSenhaLayout.setError("Por favor, insira a sua senha");

        }
        if (!Senha.equals(Confsenha)) { //Se a senhas não forem iguais:
            // Mostrar uma mensagem de erro
            confSenhaLayout.setError("Senhas não conferem");
        } else if (!Senha.isEmpty()){ // Se não enviar senhas para as próximas telas:
            senhaLayout.setError(null);
            confSenhaLayout.setError(null);
            // Verificar se a senha está correta
            Intent intent = new Intent(activityCriarSenha.this, activityCriarNome.class);
            intent.putExtra(EXTRA_EMAIL, email);// Passar o email como extra
            intent.putExtra(EXTRA_SENHA, Senha); // Passar a senha como extra
            startActivity(intent);
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
                    senhaLayout.setError(null);
                    confSenhaLayout.setError(null);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}


