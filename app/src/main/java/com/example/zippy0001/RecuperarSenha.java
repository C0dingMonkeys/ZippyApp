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

public class RecuperarSenha extends AppCompatActivity {

    private EditText txtEsqueceuSenha;
    private Button Enviar;

    private String RedefinirSenha;
    private static final String URL_RESET_PASSWORD = "https://zippyinternacional.000webhostapp.com/testeLuix/recuperarSenha.php";
    private String email;

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        txtEsqueceuSenha = findViewById(R.id.txtEsqueceuSenha);
        Enviar = findViewById(R.id.btnEnviarSenha);

        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailRecuperar = txtEsqueceuSenha.getText().toString().trim();

                if(EmailRecuperar.isEmpty())
                {
                    Toast.makeText(RecuperarSenha.this, "Por favor, insira a sua senha", Toast.LENGTH_SHORT).show();

                }
                else {
                    resetPassword(EmailRecuperar);
                }
            }
        });


    }

    private void resetPassword(String email){
        Ion.with(this)
                .load(URL_RESET_PASSWORD)
                .setBodyParameter("email", email) // Enviar o email como parâmetro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(RecuperarSenha.this, "Erro ao verificar senha. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("ok".equals(status)) {
                                    Toast.makeText(RecuperarSenha.this, "Email de Redefinição Enviado com Sucesso!", Toast.LENGTH_SHORT).show();

                                } else if ("error1".equals(status)) {
                                    Toast.makeText(RecuperarSenha.this, "Erro ao Enviar o Email", Toast.LENGTH_SHORT).show();

                                } else if ("error2".equals(status)) {
                                    // Resposta inválida do servidor
                                    Toast.makeText(RecuperarSenha.this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(RecuperarSenha.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}