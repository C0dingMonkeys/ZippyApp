package com.example.zippy0001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SenhaLogin extends AppCompatActivity {


    // URL do script PHP que verifica se a senha está correta
    private static final String URL_CHECK_PASSWORD = "https://zippyinternacional.000webhostapp.com/testeLuix/VerificarSenhaLogin.php";
    public static final String EXTRA_EMAIL = "email";

    private TextView txtSenhaLogin, esqueceuSenha;
    private CheckBox manterLogin;

    private Button logar;
    private TextInputLayout layoutSenhaLogin;
    private String email;
    public static final String SHARED_PREFS = "sharedPrefs";


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_senha_login);

        txtSenhaLogin = findViewById(R.id.txtSenhaLogin);
        logar = findViewById(R.id.btnLogar);
        email = getIntent().getStringExtra(EXTRA_EMAIL);
        esqueceuSenha = findViewById(R.id.btnEsqueceuSenha);
        layoutSenhaLogin = findViewById(R.id.layoutSenhaLoginEdit);



        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SenhaLogin = txtSenhaLogin.getText().toString().trim();

                if (SenhaLogin.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    layoutSenhaLogin.setError("Por favor, insira a sua senha");
                } else {
                    // Verificar se a senha está correta
                    salvarLogin();
                    checkPassword(email, SenhaLogin);

                }

            }
        });


        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SenhaLogin.this, RecuperarSenha.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }


    private void salvarLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String Logado = sharedPreferences.getString("nome", "");
        if (Logado.equals("true")) {
            Intent intent = new Intent(SenhaLogin.this, Home.class);
            intent.putExtra(EXTRA_EMAIL, email);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    private void checkPassword(String email, String SenhaLogin) {
        // Usar a API Ion para fazer uma requisição HTTP POST para o script PHP
        Ion.with(this)
                .load(URL_CHECK_PASSWORD)
                .setBodyParameter("email", email) // Enviar o email como parâmetro
                .setBodyParameter("Senha", SenhaLogin) // Enviar a senha como parâmetro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(SenhaLogin.this, "Erro ao verificar senha. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("true".equals(status)) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("nome", "true");
                                    editor.apply();

                                    // O e-mail existe no banco de dados
                                    // Ir para a tela de inserir senha
//                                    Intent intent = new Intent(SenhaLogin.this, Home.class);
//                                    intent.putExtra(EXTRA_EMAIL, email);
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else if ("false".equals(status)) {
                                    layoutSenhaLogin.setError("Senha Incoreta.");

                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(SenhaLogin.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(SenhaLogin.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}