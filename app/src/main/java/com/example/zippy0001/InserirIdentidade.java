package com.example.zippy0001;

import static com.example.zippy0001.activityCriarNome.EXTRA_FONE;
import static com.example.zippy0001.classes.MaskType.CPF;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zippy0001.classes.MaskUtil;
import com.example.zippy0001.classes.ValidaCPF;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class InserirIdentidade extends AppCompatActivity {

    // URL do script PHP que verifica se a senha está correta
    private static final String URL_INSERT_USER = "https://zippyinternacional.000webhostapp.com/testeLuix/InserirZippy.php";

    // Chave para receber o email como extra da tela anterior
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_SENHA = "Senha";
    public static final String EXTRA_NOME = "NomeCompleto";
    public static final String EXTRA_DATA = "DataNascimento";


    private String email;
    private String Senha;
    private String NomeCompleto;
    private String DataNascimento;
    private String Fone;
    private Button btnInserir;
    private EditText txtIdentidade;
    private TextInputLayout identidadeLayout;



    String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inserir_cpf);

        txtIdentidade = findViewById(R.id.txtIdentidade);
        btnInserir = findViewById(R.id.btnInserir);
        identidadeLayout = findViewById(R.id.layoutIdentidade);

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Identidade = txtIdentidade.getText().toString().trim();
                email = getIntent().getStringExtra(EXTRA_EMAIL);
                Senha = getIntent().getStringExtra(EXTRA_SENHA);
                Fone = getIntent().getStringExtra(EXTRA_FONE);
                NomeCompleto = getIntent().getStringExtra(EXTRA_NOME);
                DataNascimento = getIntent().getStringExtra(EXTRA_DATA);


                if (!isNetworkAvailable(InserirIdentidade.this)) {
                    // Mostrar um Toast de aviso de que não há internet
                    Toast.makeText(InserirIdentidade.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Identidade.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    identidadeLayout.setError("Por favor, insira sua identidade");
                }
                 else {
                    InserirDados();
                }
            }
        });

    }

    private void InserirDados() {
        // Usar a API Ion para fazer uma requisição HTTP POST para o script PHP
        Ion.with(this)
                .load(URL_INSERT_USER)
                .setBodyParameter("email", email) // Enviar o email como parâmetro
                .setBodyParameter("Senha", Senha) // Enviar a senha como parâmetro
                .setBodyParameter("NomeCompleto", NomeCompleto) // Enviar o nome como parâmetro
                .setBodyParameter("Fone", Fone) // Enviar o telefone como parâmetro
                .setBodyParameter("DataNascimento", DataNascimento) // Enviar a data de nascimento como parâmetro
                .setBodyParameter("Identidade", txtIdentidade.getText().toString()) // Enviar o CPF como parametro

                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        ret = result.get("status").getAsString(); //Recebe o resultado do PHP
                        if (ret.equals("ok")) // Se for "OK":
                        {
                            //CAIXA DE ALERTA DE SUCESSO:

                            AlertDialog.Builder fazerLogin = new AlertDialog.Builder(InserirIdentidade.this);
                            fazerLogin.setTitle("Sucesso!");
                            fazerLogin.setMessage("Cadastro realizado com Sucesso!\nFaça Login para continuar!");
                            fazerLogin.setCancelable(false);
                            fazerLogin.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(InserirIdentidade.this, InserirEmail.class);
                                    startActivity(intent);

                                }
                            });

                            fazerLogin.create().show();

                        } else // Se não:
                        {
                            //Exibe Toast de erro:
                            Toast.makeText(getApplicationContext(), " erro",
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }

    public static boolean isNetworkAvailable(Context context) { //API verificadora de internet
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}