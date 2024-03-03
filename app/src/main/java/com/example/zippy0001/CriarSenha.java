package com.example.zippy0001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CriarSenha extends AppCompatActivity {

    // URL do script PHP que verifica se a senha está correta
    private static final String URL_CHECK_PASSWORD = "https://zippyinternacional.000webhostapp.com/testeLuix/VerificarSenha.php";

    // Chave para receber o email como extra da tela anterior
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_SENHA = "Senha";


    private String email;

    private TextView txtSenha, txtConfsenha;
    private Button Continuar;
    String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.criar_senha);

        txtSenha = findViewById(R.id.txtSenhaCadastro);
        txtConfsenha = findViewById(R.id.txtConfirmarSenha);
        Continuar = findViewById(R.id.btnContinuar);

        email = getIntent().getStringExtra(EXTRA_EMAIL);


        Continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Senha = txtSenha.getText().toString().trim();
                String Confsenha = txtConfsenha.getText().toString().trim();

                // Validar a senha
                if (Senha.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    Toast.makeText(CriarSenha.this, "Por favor, insira a sua senha", Toast.LENGTH_SHORT).show();
                }
                if (!Senha.equals(Confsenha)){
                    // Mostrar uma mensagem de erro
                    Toast.makeText(CriarSenha.this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Verificar se a senha está correta
                    Intent intent = new Intent(CriarSenha.this, InserirCPF.class);
                    intent.putExtra(EXTRA_EMAIL, email);// Passar o email como extra
                    intent.putExtra(EXTRA_SENHA, Senha);
                    startActivity(intent);                }

            }
        });

    }

}