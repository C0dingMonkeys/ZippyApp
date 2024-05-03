package com.example.zippy0001;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Pattern;


public class activityEditarConta extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String URL_UPDATE_USUARIO = "https://zippyinternacional.com/Android/updateUsuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_conta);

        RelativeLayout editarPerfil = findViewById(R.id.layoutEditarPerfil);
        RelativeLayout editarEmail = findViewById(R.id.layoutEditarEmailTela);
        RelativeLayout editarSenha = findViewById(R.id.layoutEditarSenhaTela);
        RelativeLayout editarDtaNasc = findViewById(R.id.layoutEditarDtaNascTela);

        TextView emailEdit = findViewById(R.id.txtEmailEdit);
        TextView dataNascEdit = findViewById(R.id.txtDataNascEdit);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuario = sharedPreferences.getString("id_usuario", "");
        Log.d("idTESTECONTA", idUsuario);
        String emailCliente = sharedPreferences.getString("email", "");
        String dataNasc = sharedPreferences.getString("dataNascUsuario", "");
        String emailOculto = substituirCaracteres(emailCliente);

        emailEdit.setText(emailOculto);
        dataNascEdit.setText(dataNasc);


        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityEditarConta.this, activityEditarPerfil.class);
                startActivity(intent);
            }
        });

        editarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bslEmail(idUsuario);
            }
        });

        editarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bslSenha(idUsuario, emailCliente, dataNasc);

            }


        });

        editarDtaNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bslDataNasc(idUsuario, emailCliente, dataNasc);
            }
        });

    }


    public static String substituirCaracteres(String str) {
        if (str.length() < 5) {
            return str; // String is too short, return it as is
        }

        String first5Chars = str.substring(0, 5); // Extract the first 5 characters
        String asterisks = "****"; // Create a string of 5 asterisks
        String remainingChars = str.substring(5); // Extract the remaining characters

        return asterisks + remainingChars; // Combine the asterisks and remaining characters
    }

    private void bslEmail(String idUsuario) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityEditarConta.this);
        @SuppressLint("InflateParams") View view1 = LayoutInflater.from(activityEditarConta.this).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

        TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheet);
        TextInputLayout layoutEmail = view1.findViewById(R.id.TextLayoutEdicao);
        TextInputEditText txtEmail = view1.findViewById(R.id.TextEditEdicao);
        AppCompatButton btnSalvarEmail = view1.findViewById(R.id.btnSalvarEdicao);

        txtTituloBSL.setText(R.string.lbl_edite_seu_email);


        btnSalvarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailEditado = txtEmail.getText().toString().trim();

                boolean checarEmail = validarEmail(emailEditado, layoutEmail);
                if (checarEmail) {
                    updateUsuario(idUsuario, emailEditado, null, null);
                    bottomSheetDialog.dismiss();
                }
            }
        });
    }

    private void bslSenha(String idUsuario, String emailCliente, String dataNasc) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityEditarConta.this);
        View view1 = LayoutInflater.from(activityEditarConta.this).inflate(R.layout.bottom_sheet_layout_nome, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

        TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheetNome);
        TextInputLayout senhaLayout = view1.findViewById(R.id.TextLayoutEdicaoNome);
        TextInputLayout confSenhaLayout = view1.findViewById(R.id.TextLayoutEdicaoSobrenome);
        TextInputEditText txtSenha = view1.findViewById(R.id.TextEditEdicaoNome);
        TextInputEditText txtConfSenha = view1.findViewById(R.id.TextEditEdicaoSobrenome);

        AppCompatButton btnSalvarNome = view1.findViewById(R.id.btnSalvarEdicaoNome);

        txtTituloBSL.setText(R.string.lbl_edite_sua_senha);

        senhaLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        txtSenha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);


        txtSenha.setHint(R.string.senha_hint);
        txtConfSenha.setHint(R.string.confirmar_senha);

        btnSalvarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Senha = txtSenha.getText().toString().trim(); //Recebe o Nome do EditText
                String Confsenha = txtConfSenha.getText().toString().trim();

                Log.d("TESTECONTA", "id:" + idUsuario + " " + emailCliente + " " + Senha + " " + dataNasc);

                boolean checarSenha = validarSenha(Senha, Confsenha, senhaLayout, confSenhaLayout);
                if (checarSenha) {//Recebe o Nome do EditText
                    updateUsuario(idUsuario, emailCliente, Senha, dataNasc);
                    bottomSheetDialog.dismiss();
                }
            }
        });
    }

    private void bslDataNasc(String idUsuario, String emailCliente, String dataNasc) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityEditarConta.this);
        View view1 = LayoutInflater.from(activityEditarConta.this).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

        TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheet);
        TextInputLayout dataLayout = view1.findViewById(R.id.TextLayoutEdicao);
        TextInputEditText txtData = view1.findViewById(R.id.TextEditEdicao);

        AppCompatButton btnSalvarData = view1.findViewById(R.id.btnSalvarEdicao);

        txtTituloBSL.setText(R.string.lbl_edite_seu_nome);

        txtData.setText(dataNasc);
        txtData.setInputType(InputType.TYPE_CLASS_NUMBER);
        String DataVerify = txtData.getText().toString().trim();
        if (DataVerify.isEmpty()) {
            dataLayout.setError(getString(R.string.dataEdit_error));
        }


        btnSalvarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataEditada = txtData.getText().toString().trim();
                String senha = "";

                updateUsuario(idUsuario, emailCliente, senha, dataEditada);
                bottomSheetDialog.dismiss();

                Log.d("TESTECONTADATA", "id:" + idUsuario + " " + emailCliente + " " + dataNasc);

            }
        });
    }

    private boolean validarSenha(String Senha, String Confsenha, TextInputLayout senhaLayout, TextInputLayout confSenhaLayout) {
        // Validar a senha
        if (Senha.isEmpty()) { //Se a senha estiver vazia:
            // Mostrar uma mensagem de erro
            senhaLayout.setError("Por favor, insira a sua senha");
            return false;
        }
        if (Confsenha.isEmpty()) {
            confSenhaLayout.setError("Por favor, insira a sua senha");
            return false;
        }
        if (!Senha.equals(Confsenha)) { //Se a senhas não forem iguais:
            // Mostrar uma mensagem de erro
            confSenhaLayout.setError("Senhas não conferem");
            return false;
        } else {
            if (Senha.length() < 8) {
                senhaLayout.setError("A senha precisa de no mininmo 8 caracteres");
                return false;
            } else {
                senhaLayout.setError(null);
                confSenhaLayout.setError(null);
                return true;
            }
        }
    }

    private void updateUsuario(String idUsuario, String emailEditado, String senhaEditada, String dataNascEditada) {
        Ion.with(this)
                .load(URL_UPDATE_USUARIO)
                .setBodyParameter("idUsuario", idUsuario) // Enviar o email como parâmetro
                .setBodyParameter("novoEmail", emailEditado) // Enviar o email como parâmetro
                .setBodyParameter("novaSenha", senhaEditada) // Enviar a senha como parâmetro
                .setBodyParameter("novaDtaNasc", dataNascEditada) // Enviar a senha como parâmetro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {


                            if (e != null) {
                                Toast.makeText(activityEditarConta.this, "Este Email já está cadastrado!", Toast.LENGTH_SHORT).show();

                                // Ocorreu um erro de conexão ou outra exceção
                                Log.e("Conta", "Erro na requisição0: " + e.getMessage());
                            } else {
                                // Verifique se o resultado é válido
                                if (result != null && result.has("status")) {
                                    String status = result.get("status").getAsString();
                                    if ("ok".equals(status)) {
                                        Toast.makeText(activityEditarConta.this, "Sucesso", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", emailEditado);
                                        editor.putString("dataNascUsuario", dataNascEditada);
                                        editor.apply();
                                        recreate();

                                    } else if ("erro".equals(status)) {

                                        Log.e("Conta", "Erro na requisição1: " + result);
                                    } else {
                                        // Resposta inválida do servidor
                                        Log.e("Conta", "Erro na requisição2: " + result);
                                    }
                                } else {
                                    // Resposta inválida do servidor
                                    Log.e("Conta", "Erro na requisição3: " + result);
                                }
                            }
                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }

    public boolean validarEmail(String email, TextInputLayout emailLayout) {

        if (email.isEmpty()) {
            // Mostrar uma mensagem de erro
            emailLayout.setError("Por favor, insira o seu email");
            return false;
        } else if (!isValidEmail(email)) {
            // Mostrar uma mensagem de erro de email inválido
            emailLayout.setError("Email inválido");
            return false;
        } else {
            // Verificar se o email existe no banco de dados
            emailLayout.setError(null);
        }
        return true;
    }


    // FUNÇÃO DE VALIDAR O EMAIL
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        return Pattern.matches(emailPattern, email);
    }
}