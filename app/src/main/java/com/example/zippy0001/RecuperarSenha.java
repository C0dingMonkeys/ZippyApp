package com.example.zippy0001;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class RecuperarSenha extends AppCompatActivity {

    private EditText txtEsqueceuSenha;
    private TextInputLayout recuperarLayout;

    private static final String URL_RESET_PASSWORD = "https://zippyinternacional.000webhostapp.com/testeLuix/recuperarSenha.php";

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        txtEsqueceuSenha = findViewById(R.id.txtEsqueceuSenha);
        Button enviar = findViewById(R.id.btnEnviarSenha);
        recuperarLayout = findViewById(R.id.layoutRecuperarSenha);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailRecuperar = txtEsqueceuSenha.getText().toString().trim();

                if(EmailRecuperar.isEmpty())
                {
                    recuperarLayout.setError("Por favor, insira a sua senha");

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
                                    //CAIXA DE ALERTA DE SUCESSO:

                                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(RecuperarSenha.this);
                                    fazerLogin.setTitle("Sucesso!");
                                    fazerLogin.setMessage("Email de recuperação enviado com sucesso!\nFaça Login para continuar!");
                                    fazerLogin.setCancelable(false);
                                    fazerLogin.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(RecuperarSenha.this, InserirEmail.class);
                                            startActivity(intent);

                                        }
                                    });

                                    fazerLogin.create().show();

                                } else if ("error1".equals(status)) {
                                    Toast.makeText(RecuperarSenha.this, "Erro ao Enviar o Email", Toast.LENGTH_SHORT).show();

                                } else if ("error2".equals(status)) {
                                    // Resposta inválida do servidor
                                    recuperarLayout.setError("Email não cadastrado");
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