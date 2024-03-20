package com.example.zippy0001;

import static com.example.zippy0001.classes.MaskType.CPF;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zippy0001.classes.MaskUtil;
import com.example.zippy0001.classes.ValidaCPF;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class InserirCPF extends AppCompatActivity {

    // URL do script PHP que verifica se a senha está correta
    private static final String URL_INSERT_USER = "https://zippyinternacional.000webhostapp.com/testeLuix/InserirZippy.php";

    // Chave para receber o email como extra da tela anterior
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_SENHA = "Senha";

    private String email;
    private String Senha;

    private EditText txtcpf;



    String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inserir_cpf);

        txtcpf = findViewById(R.id.txtCpf);
        txtcpf.addTextChangedListener(MaskUtil.insert(txtcpf, CPF)); // Máscara de CPF


        Button btnInserir = findViewById(R.id.btnInserir);

        email = getIntent().getStringExtra(EXTRA_EMAIL);
        Senha = getIntent().getStringExtra(EXTRA_SENHA);


        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtCPF = txtcpf.getText().toString().trim();


                if (!isNetworkAvailable(InserirCPF.this)) {
                    // Mostrar um Toast de aviso de que não há internet
                    Toast.makeText(InserirCPF.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtCPF.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    Toast.makeText(InserirCPF.this, "Por favor, insira seu CPF", Toast.LENGTH_SHORT).show();
                }
                if (!ValidaCPF.isCPF(txtCPF)){
                    // Mostrar uma mensagem de erro
                    Toast.makeText(InserirCPF.this, "CPF inválido!", Toast.LENGTH_SHORT).show();
                }
                else {

                    InserirDados();
                }
            }
        });

    }

    private void InserirDados()
    {
        // Usar a API Ion para fazer uma requisição HTTP POST para o script PHP
        Ion.with(this)
                .load(URL_INSERT_USER)
                .setBodyParameter("email", email) // Enviar o email como parâmetro
                .setBodyParameter("Senha", Senha) // Enviar a senha como parâmetro
                .setBodyParameter("CPF", txtcpf.getText().toString()) // Enviar o CPF como parametro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        ret=result.get("status").getAsString ();
                        if(ret.equals ( "ok" ))
                        {
                            AlertDialog.Builder fazerLogin = new AlertDialog.Builder(InserirCPF.this);
                            fazerLogin.setTitle("Sucesso!");
                            fazerLogin.setMessage("Cadastro realizado com Sucesso!\nFaça Login para continuar!");
                            fazerLogin.setCancelable(false);
                            fazerLogin.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(InserirCPF.this, InserirEmail.class);
                                    startActivity(intent);

                                }
                            });

                            fazerLogin.create().show();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),  " erro",
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}