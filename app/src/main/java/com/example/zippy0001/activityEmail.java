package com.example.zippy0001;

import static com.example.zippy0001.activitySenhaLogin.SHARED_PREFS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Pattern;

/**
 * @noinspection Convert2Lambda
 */
public class activityEmail extends AppCompatActivity {


    private EditText txtemail; //Instanciando a variavel
    private TextInputLayout emailLayout;
    public static final String EXTRA_EMAIL = "email"; // Variavel que envia o texto para a tela seguinte
    private static final String URL_CHECK_EMAIL = "http://zippyinternacional.com/Android/LoginAndroid.php"; //URL do Script


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email);

        txtemail = findViewById(R.id.txtEmail); //Definindo a variável
        emailLayout = findViewById(R.id.layoutEmail);
        Button btnContinuar = findViewById(R.id.btnContinuar1); //Botão de continuar

        salvarLogin();

        txtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    emailLayout.setError(null);
                }
            }
        });


        //noinspection Convert2Lambda
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarEmail();
            }
        });

    }

    private void salvarLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String Logado = sharedPreferences.getString("nome", "");
        if (Logado.equals("true")) {
            Intent intent = new Intent(activityEmail.this, activityInicio.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
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
                            Toast.makeText(activityEmail.this, R.string.erro_conexao, Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("true".equals(status)) {
                                    // O e-mail existe no banco de dados
                                    // Ir para a tela de inserir senha
                                    Intent intent = new Intent(activityEmail.this, activitySenhaLogin.class);
                                    intent.putExtra(EXTRA_EMAIL, email);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else if ("false".equals(status)) {
                                    // O e-mail não existe no banco de dados
                                    // Ir para a tela de criar senha
                                    Intent intent = new Intent(activityEmail.this, activityCriarSenha.class);
                                    intent.putExtra(EXTRA_EMAIL, email);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    // Resposta inválida do servidor
                                    emailLayout.setError(getString(R.string.erro_desconhecido));
                                }
                            } else {
                                // Resposta inválida do servidor
                                emailLayout.setError(getString(R.string.erro_desconhecido));
                            }
                        }
                    }
                });
    }

    public void validarEmail() {
        String email = txtemail.getText().toString().trim();

        if (email.isEmpty()) {
            // Mostrar uma mensagem de erro
            emailLayout.setError(getString(R.string.erro_insira_email));
        } else if (!isValidEmail(email)) {
            // Mostrar uma mensagem de erro de email inválido
            emailLayout.setError(getString(R.string.erro_email_invalido));
        } else {
            // Verificar se o email existe no banco de dados
            emailLayout.setError(null);
            checkEmail(email);

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

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    // FUNÇÃO DE VALIDAR O EMAIL
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        return Pattern.matches(emailPattern, email);
    }
}




