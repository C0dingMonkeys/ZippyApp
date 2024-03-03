package com.example.zippy0001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zippy0001.Classes.MaskType;
import com.example.zippy0001.Classes.MaskUtil;
import com.example.zippy0001.Classes.ValidaCPF;
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

    private Button btnInserir;

    String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.inserir_cpf);

        txtcpf = (EditText) findViewById(R.id.txtCpf);
        txtcpf.addTextChangedListener(MaskUtil.insert(txtcpf, MaskType.CPF)); // Máscara de CPF

        btnInserir = (Button) findViewById(R.id.btnInserir);

        email = getIntent().getStringExtra(EXTRA_EMAIL);
        Senha = getIntent().getStringExtra(EXTRA_SENHA);


        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CPF = txtcpf.getText().toString().trim();

                if (CPF.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    Toast.makeText(InserirCPF.this, "Por favor, insira seu CPF", Toast.LENGTH_SHORT).show();
                }
                if (!ValidaCPF.isCPF(CPF)){
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
                            Toast.makeText(getApplicationContext(),
                                    " incluido com sucesso",
                                    Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),  " erro",
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }


}