package com.example.zippy0001;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Pattern;

/** @noinspection Convert2Lambda*/
public class InserirEmail extends AppCompatActivity {


    private EditText txtemail;

    public static final String EXTRA_EMAIL = "email";

    private static final String URL_CHECK_EMAIL = "https://zippyinternacional.000webhostapp.com/testeLuix/login01.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inserir_email);

        txtemail = (EditText) findViewById(R.id.txtEmail);
        Button btnContinuar = (Button) findViewById(R.id.btnContinuar);
        //noinspection Convert2Lambda
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = txtemail.getText().toString().trim();

                if (email.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    Toast.makeText(InserirEmail.this, "Por favor, insira o seu email", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    // Mostrar uma mensagem de erro de email inválido
                    Toast.makeText(InserirEmail.this, "Email inválido", Toast.LENGTH_SHORT).show();
                } else {

                    // Verificar se o email existe no banco de dados
                    checkEmail(email);
                }


            }
        });

    }

    private void checkEmail(String email) {
        Ion.with(this)
                .load(URL_CHECK_EMAIL)
                .setBodyParameter("email", email)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(InserirEmail.this, "Erro ao verificar e-mail. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("true".equals(status)) {
                                    // O e-mail existe no banco de dados
                                    // Ir para a tela de inserir senha
                                    Intent intent = new Intent(InserirEmail.this, SenhaLogin.class);
                                    intent.putExtra(EXTRA_EMAIL, email);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else if ("false".equals(status)) {
                                    // O e-mail não existe no banco de dados
                                    // Ir para a tela de criar senha
                                    Intent intent = new Intent(InserirEmail.this, CriarSenha.class);
                                    intent.putExtra(EXTRA_EMAIL, email);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(InserirEmail.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(InserirEmail.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // FUNÇÃO DE VALIDAR O EMAIL
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        return Pattern.matches(emailPattern, email);
    }
}




