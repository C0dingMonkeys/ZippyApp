package com.example.zippy0001;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class activityIdentidade extends AppCompatActivity {

    // URL do script PHP que verifica se a senha está correta
    private static final String URL_INSERT_USER = "http://zippyinternacional.com/Android/InserirAndroid.php";

    // Chave para receber o email como extra da tela anterior
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_SENHA = "senha";
    public static final String EXTRA_NOME = "nome";
    public static final String EXTRA_SOBRENOME = "sobrenome";
    public static final String EXTRA_DATA = "dtaNasc";
    public static final String EXTRA_TEL = "tel";

    private EditText txtIdentidade;
    private TextInputLayout identidadeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_identidade);

        Button btnInserir = findViewById(R.id.btnInserir);
        txtIdentidade = findViewById(R.id.txtIdentidade);
        identidadeLayout = findViewById(R.id.layoutIdentidade);


        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receberDados();
            }
        });

    }

    public void receberDados(){
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        String senha = getIntent().getStringExtra(EXTRA_SENHA);
        String tel = getIntent().getStringExtra(EXTRA_TEL);
        String nome = getIntent().getStringExtra(EXTRA_NOME);
        String sobrenome = getIntent().getStringExtra(EXTRA_SOBRENOME);
        String dtaNasc = getIntent().getStringExtra(EXTRA_DATA);
        String identidade = txtIdentidade.getText().toString().trim();

        if (!isNetworkAvailable(activityIdentidade.this)) {
            // Mostrar um Toast de aviso de que não há internet
            Toast.makeText(activityIdentidade.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (identidade.isEmpty()) {
            // Mostrar uma mensagem de erro
            identidadeLayout.setError("Por favor, insira sua identidade");
        } else {
            Toast.makeText(getApplicationContext(), " clique",
                    Toast.LENGTH_LONG).show();
            InserirDados(nome, sobrenome, email, tel, identidade, senha, dtaNasc);

        }
    }

    private void InserirDados(String nome, String sobrenome, String email, String tel, String identidade, String senha, String dtaNasc) {

        // Usar a API Ion para fazer uma requisição HTTP POST para o script PHP
        Ion.with(this)
                .load(URL_INSERT_USER)
                .setBodyParameter("nome", nome) // Enviar o email como parâmetro
                .setBodyParameter("sobrenome", sobrenome) // Enviar a senha como parâmetro
                .setBodyParameter("email", email) // Enviar a senha como parâmetro
                .setBodyParameter("tel", tel) // Enviar a senha como parâmetro
                .setBodyParameter("identidade", identidade) // Enviar a senha como parâmetro
                .setBodyParameter("senha", senha) // Enviar a senha como parâmetro
                .setBodyParameter("dtaNasc", dtaNasc) // Enviar a senha como parâmetro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(activityIdentidade.this, "Erro ao verificar senha. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                Log.d("status", status);
                                if ("ok".equals(status)) {

                                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(activityIdentidade.this);
                                    fazerLogin.setTitle("Sucesso!");
                                    fazerLogin.setMessage("Cadastro realizado com Sucesso!\nFaça Login para continuar!");
                                    fazerLogin.setCancelable(false);
                                    fazerLogin.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(activityIdentidade.this, activityEmail.class);
                                            startActivity(intent);

                                        }
                                    });

                                    fazerLogin.create().show();
                                    // O e-mail existe no banco de dados

                                } else if ("false".equals(status)) {
                                    identidadeLayout.setError("ERRO.");

                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(activityIdentidade.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(activityIdentidade.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
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

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    public static boolean isNetworkAvailable(Context context) { //API verificadora de internet
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}