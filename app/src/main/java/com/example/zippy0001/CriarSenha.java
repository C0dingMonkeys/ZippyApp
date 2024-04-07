package com.example.zippy0001;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CriarSenha extends AppCompatActivity {

    // Chave para receber o email como extra da tela anterior
    public static final String EXTRA_EMAIL = "email"; // Variavel que envia o texto para a tela seguinte
    public static final String EXTRA_SENHA = "Senha"; // Variavel que envia o texto para a tela seguinte
    private String email; //Variavel para receber o texto das telas anteriores
    private TextView txtSenha, txtConfsenha; //Instaciando variaveis da tela

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.criar_senha);

        txtSenha = findViewById(R.id.txtSenhaCadastro); //EditText da senha
        txtConfsenha = findViewById(R.id.txtConfirmarSenha); //EditText da confirmação de senha
        Button continuar = findViewById(R.id.btnContinuar2); //Botão Continuar

        email = getIntent().getStringExtra(EXTRA_EMAIL); //Recebe o texto da tela anterior e transforma em String

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verificarSenha();
            }
        });

    }

    private void verificarSenha() {
        String Senha = txtSenha.getText().toString().trim(); //Recebe a senha e transforma em String
        String Confsenha = txtConfsenha.getText().toString().trim(); //Recebe a confirmação de senha e transforma em String

        if (!isNetworkAvailable(CriarSenha.this)) { //Se não houver internet:
            // Mostrar um Toast de aviso de que não há internet
            Toast.makeText(CriarSenha.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validar a senha
        if (Senha.isEmpty()) { //Se a senha estiver vazia:
            // Mostrar uma mensagem de erro
            Toast.makeText(CriarSenha.this, "Por favor, insira a sua senha", Toast.LENGTH_SHORT).show();
        }
        if (!Senha.equals(Confsenha)) { //Se a senhas não forem iguais:
            // Mostrar uma mensagem de erro
            Toast.makeText(CriarSenha.this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
        } else { // Se não enviar senhas para as próximas telas:
            // Verificar se a senha está correta
            Intent intent = new Intent(CriarSenha.this, activityCriarNome.class);
            intent.putExtra(EXTRA_EMAIL, email);// Passar o email como extra
            intent.putExtra(EXTRA_SENHA, Senha); // Passar a senha como extra
            startActivity(intent);
        }
    }

    public static boolean isNetworkAvailable(Context context) { //API verificadora de internet
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}