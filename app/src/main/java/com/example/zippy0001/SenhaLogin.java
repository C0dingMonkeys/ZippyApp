package com.example.zippy0001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SenhaLogin extends AppCompatActivity {


    // URL do script PHP que verifica se a senha está correta
    private static final String URL_CHECK_PASSWORD = "https://zippyinternacional.000webhostapp.com/testeLuix/VerificarSenha.php";
    public static final String EXTRA_EMAIL = "email";

    private TextView txtSenhaLogin;
    private String email;

    private Button Logar;

    String ret;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_senha_login);

        txtSenhaLogin = findViewById(R.id.txtSenhaLogin);

        Logar = findViewById(R.id.btnLogar);

        email = getIntent().getStringExtra(EXTRA_EMAIL);

        Logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SenhaLogin = txtSenhaLogin.getText().toString().trim();

                if (SenhaLogin.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    Toast.makeText(SenhaLogin.this, "Por favor, insira a sua senha", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Verificar se a senha está correta
                    checkPassword(email, SenhaLogin);
                }

            }
        });

    }

    private void checkPassword(String email, String Senha) {
        // Usar a API Ion para fazer uma requisição HTTP POST para o script PHP
        Ion.with(this)
                .load(URL_CHECK_PASSWORD)
                .setBodyParameter("email", email) // Enviar o email como parâmetro
                .setBodyParameter("Senha", Senha) // Enviar a senha como parâmetro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        ret=result.get("status").getAsString();

                        // Verificar se a requisição foi bem-sucedida
                        if (e == null) {
                            // Verificar se o resultado é válido
                            if (ret != null) {
                                // Verificar se o resultado é "true" ou "false"
                                if (ret.equals("true")) {
                                    Intent intent = new Intent(SenhaLogin.this, InserirCPF.class);
                                    intent.putExtra(EXTRA_EMAIL, email);// Passar o email como extra
                                    startActivity(intent);

                                } else if (ret.equals("false")) {
                                    // A senha está incorreta
                                    // Mostrar uma mensagem de erro
                                    Toast.makeText(SenhaLogin.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                                } else {
                                    // O resultado é inválido
                                    // Mostrar uma mensagem de erro
                                    Toast.makeText(SenhaLogin.this, "Erro: resultado inválido", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // O resultado é nulo
                                // Mostrar uma mensagem de erro
                                Toast.makeText(SenhaLogin.this, "Erro: resultado nulo", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // A requisição falhou
                            // Mostrar uma mensagem de erro
                            Toast.makeText(SenhaLogin.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}