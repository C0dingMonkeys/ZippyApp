package com.example.zippy0001;

import static com.example.zippy0001.activityInicio.SHARED_PREFS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorMensagens;
import com.example.zippy0001.classes.Mensagens;
import com.example.zippy0001.classes.Usuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class activityChatEspecifico extends AppCompatActivity {
    String usuario;
    Usuario usuarioDestino;
    String nomeRemetente;
    String nomeDestinatario;
    final Handler handler = new Handler();
    final int intervalo = 5000;
    private boolean telaAtiva = true;


    ArrayList<Mensagens> listaMensajes = new ArrayList<Mensagens>();

    private String URL_ENVIAR_MENSAGEM = "https://zippyinternacional.com/Android/chat/inserirMensagem.php";
    private String URL_OBTER_MSG = "https://zippyinternacional.com/Android/chat/obterMensagens.php";

    RecyclerView rvMensagens;

    EditText etTexto;
    TextView nomeSuperior;

    ImageButton btnEnviar, btnVoltar;
    String idChat = "";

    Runnable loopMensagens = new Runnable() {
        @Override
        public void run() {
            if (telaAtiva) {
                obterMensagens();
                handler.postDelayed(this, intervalo);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_especifico);

        Toolbar toolbar = findViewById(R.id.menu_denuncia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario = getIntent().getStringExtra("usuario");
        usuarioDestino = (Usuario) getIntent().getExtras().getSerializable("usuarioDestino");
        Log.d("testeUserRAIZ", "destinatario:" + usuarioDestino.getUsuario());

        String idPedido = getIntent().getStringExtra("idPedido");
        //obterMensagens();
        rvMensagens = findViewById(R.id.rvMensagens);
        rvMensagens.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        etTexto = findViewById(R.id.etTexto);
        nomeSuperior = findViewById(R.id.txtNomeDestinatario);
        nomeSuperior.setText(usuarioDestino.getNome());

        btnEnviar = findViewById(R.id.btnEnviar);
        btnVoltar = findViewById(R.id.btnVoltarChat);



        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTexto.getText().toString().isEmpty()) {
                    Toast.makeText(activityChatEspecifico.this, "Insira uma Mensagem.", Toast.LENGTH_LONG).show();
                } else {
                    enviarMensagem();
                    fecharTeclado();
                    etTexto.setText("");
                }
            }
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.denunciar_menu, menu);

        int positionOfMenuItem = 0; // or whatever...
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString(getString(R.string.menuItem_denunciar_usuario));
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    private void DenunciarTela() {
        Intent intent = new Intent(activityChatEspecifico.this, activityDenunciarUsuario.class);
        intent.putExtra("id_denunciado", usuarioDestino);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_denunciar) {
            DenunciarTela();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obterMensagens() {
        listaMensajes.clear();

        String idChat = getIntent().getStringExtra("idChat");
        HashSet<Mensagens> conjuntoMensagens = new HashSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuarioShared = sharedPreferences.getString("id_usuario", "");
        String nomeCliente = sharedPreferences.getString("nomeCliente", "");

        Log.d("teste user", "Usuario:" + usuario + "Destinatario:" + usuarioDestino.getUsuario());
        Ion.with(this)
                .load(URL_OBTER_MSG)
                .setBodyParameter("remetente", usuario)
                .setBodyParameter("destinatario", usuarioDestino.getUsuario())
//                .setBodyParameter("chatid", idChat )
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null) {
                                Log.e("obtermsgs", "Erro na requisição: " + e.getMessage());
                            } else {
                                JsonArray jsonArrayMsg = result.get("mensagens").getAsJsonArray();
                                HashMap<String, Mensagens> mensagensHashMap = new HashMap<>();

                                for (int i = 0; i < jsonArrayMsg.size(); i++) {
                                    Log.d("obtermsgs", "JSON Response: " + result.toString());

                                    JsonObject objectMsg = jsonArrayMsg.get(i).getAsJsonObject();
                                    String idMsg = objectMsg.get("ID_MSG").getAsString();
                                    String msg = objectMsg.get("MENSAGEM").getAsString();
                                    String remetente = objectMsg.get("REMETENTE").getAsString();
                                    String destinatario = objectMsg.get("DESTINATARIO").getAsString();

                                    Mensagens mensagens = new Mensagens(
                                            idMsg,
                                            msg,
                                            remetente,
                                            destinatario);

                                    if (!listaMensajes.contains(idMsg)) {
                                        listaMensajes.add(mensagens);
                                    }


                                }

                                JsonArray jsonArrayClientes = result.get("dadosCliente").getAsJsonArray();

                                for (int i = 0; i < jsonArrayClientes.size(); i++) {
                                    JsonObject objectClientes = jsonArrayClientes.get(i).getAsJsonObject();
                                    String nome_dest = objectClientes.get("NOME_DESTINATARIO").getAsString();
                                    String nome_rem = objectClientes.get("NOME_REMETENTE").getAsString();

                                    if (nome_dest.equals(nomeCliente)) {
                                        nomeRemetente = nome_dest;
                                        nomeDestinatario = nome_rem;
                                    }else {
                                        nomeRemetente = nome_rem;
                                        nomeDestinatario = nome_dest;
                                    }

                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activityChatEspecifico.this, RecyclerView.VERTICAL, false);
                                linearLayoutManager.setStackFromEnd(true);
                                rvMensagens.setLayoutManager(linearLayoutManager);
                                AdaptadorMensagens adaptadorMensagens = new AdaptadorMensagens(activityChatEspecifico.this, usuario, listaMensajes, nomeRemetente, nomeDestinatario);
                                rvMensagens.setAdapter(adaptadorMensagens);
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void enviarMensagem() {

        String idChat = getIntent().getStringExtra("idChat");
        Log.d("teste de camopos", "rementente:" + usuario + " " + "destinatario:" + usuarioDestino + " " + "mensagem:" + etTexto.getText().toString() + " " + "idChat:" + idChat);


        Ion.with(this)
                .load(URL_ENVIAR_MENSAGEM)
                .setBodyParameter("remetente", usuario)
                .setBodyParameter("destinatario", usuarioDestino.getUsuario())
                .setBodyParameter("mensagem", etTexto.getText().toString())
//                .setBodyParameter("chatid", idChat)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null) {
                                // Ocorreu um erro de conexão ou outra exceção
                                Toast.makeText(activityChatEspecifico.this, e.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                // Verifique se o resultado é válido
                                if (result != null && result.has("status")) {
                                    String status = result.get("status").getAsString();
                                    if ("ok".equals(status)) {

                                        Log.d("statusmsg", "okk");


                                    } else if ("erro".equals(status)) {
                                        Log.d("erroChat", "ERRROOOO");

                                    } else {
                                        // Resposta inválida do servidor
                                        Toast.makeText(activityChatEspecifico.this, "erro_desconhecido1", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(activityChatEspecifico.this, R.string.erro_desconhecido, Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

    }

    public void fecharTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        telaAtiva = false;
        // Remover callbacks pendentes do handler
        handler.removeCallbacks(loopMensagens);
    }

    @Override
    protected void onResume() {
        super.onResume();
        telaAtiva = true;
        handler.post(loopMensagens);
    }
}