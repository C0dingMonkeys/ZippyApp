package com.example.zippy0001;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class activityRecuperarSenha extends AppCompatActivity {

    private EditText txtEsqueceuSenha;
    private TextInputLayout recuperarLayout;

    private LoadingDialog loadingDialog;

    private static final String URL_RESET_PASSWORD = "http://zippyinternacional.com/Android/RecuperarSenhaAndroid.php";

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
        loadingDialog = new LoadingDialog(activityRecuperarSenha.this);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailRecuperar = txtEsqueceuSenha.getText().toString().trim();

                if(EmailRecuperar.isEmpty())
                {
                    recuperarLayout.setError(getString(R.string.erro_insira_email));

                }
                else {
                    loadingDialog.iniciarAlertDialog();
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
                        loadingDialog.fecharAlertDialog();
                        Log.d("erro", String.valueOf(e));
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(activityRecuperarSenha.this, R.string.erro_conexao, Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("ok".equals(status)) {
                                    //CAIXA DE ALERTA DE SUCESSO:

                                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(activityRecuperarSenha.this);
                                    fazerLogin.setTitle(R.string.titulo_alert_dialog_login);
                                    fazerLogin.setMessage(R.string.corpo_dialog_recuperar_senha);
                                    fazerLogin.setCancelable(false);
                                    fazerLogin.setPositiveButton(R.string.btn_voltar_dialog, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(activityRecuperarSenha.this, activityEmail.class);
                                            startActivity(intent);

                                        }
                                    });

                                    fazerLogin.create().show();

                                } else if ("error1".equals(status)) {
                                    Toast.makeText(activityRecuperarSenha.this, R.string.erro_enviar_email, Toast.LENGTH_SHORT).show();

                                } else if ("error2".equals(status)) {
                                    // Resposta inválida do servidor
                                    recuperarLayout.setError(getString(R.string.erro_nao_email_cadastrado));
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(activityRecuperarSenha.this, R.string.erro_desconhecido, Toast.LENGTH_SHORT).show();
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
}