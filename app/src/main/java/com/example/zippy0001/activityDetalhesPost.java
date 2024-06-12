package com.example.zippy0001;

import static com.example.zippy0001.activityInicio.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zippy0001.classes.Usuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

public class activityDetalhesPost extends AppCompatActivity {

    private String URL_CRIAR_CHAT = "https://zippyinternacional.com/Android/chat/criarChat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_post);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuarioRemetente = sharedPreferences.getString("id_usuario", "");

        String idPedido = getIntent().getStringExtra("idPedido");
        String idClienteDest = getIntent().getStringExtra("idClienteDest");
        String NomePedido = getIntent().getStringExtra("NomePedido");
        String ValorPedido = getIntent().getStringExtra("ValorPedido");
        String CaixaPedido = getIntent().getStringExtra("CaixaPedido");
        String LinkPedido = getIntent().getStringExtra("LinkPedido");
        String PaisDestino = getIntent().getStringExtra("PaisDestino");
        String PaisOrigem = getIntent().getStringExtra("PaisOrigem");
        String CidadeDestino = getIntent().getStringExtra("CidadeDestino");
        String ImgProduto = getIntent().getStringExtra("ImgProduto");

        TextView Nome = findViewById(R.id.detalhesNome);
        TextView Valor = findViewById(R.id.detalhesValor);
        TextView Caixa = findViewById(R.id.detalhesCaixaTipo);
        TextView Link = findViewById(R.id.detalhesLinkRef);
        TextView PaisOrig = findViewById(R.id.detalhesPaisOrigem);
        TextView PaisDest = findViewById(R.id.detalhesPaisDestino);
        TextView CidadeDest = findViewById(R.id.detalhesCidadeDestino);
        ImageView Img = findViewById(R.id.imgPedidoDetalhes);
        Button Chat = findViewById(R.id.btnIniciarChat);

        Log.d("testeRecebido", idPedido + " " + idClienteDest + " " + ValorPedido + " " + CaixaPedido + " " + LinkPedido + " " + PaisDestino + " " + CidadeDestino + " " + ImgProduto);

        Nome.setText(NomePedido);
        Valor.setText(ValorPedido);
        Caixa.setText(CaixaPedido);
        Link.setText(LinkPedido);
        PaisOrig.setText(PaisOrigem);
        PaisDest.setText(PaisDestino);
        CidadeDest.setText(CidadeDestino);
        Picasso.get().load(ImgProduto).into(Img);

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarChat();


            }
        });


    }

    private void criarChat() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuarioRemetente = sharedPreferences.getString("id_usuario", "");
        String idPedido = getIntent().getStringExtra("idPedido");
        String idClienteDest = getIntent().getStringExtra("idClienteDest");
        Log.d("testeIDS", idClienteDest + idUsuarioRemetente + idPedido);

        Ion.with(this)
                .load(URL_CRIAR_CHAT)
                .setBodyParameter("remetente", idUsuarioRemetente)
                .setBodyParameter("destinatario", idClienteDest)
                .setBodyParameter("id_pedido", idPedido)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null) {
                                // Ocorreu um erro de conexão ou outra exceção
                                Toast.makeText(activityDetalhesPost.this, R.string.erro_conexao, Toast.LENGTH_SHORT).show();
                            } else {
                                // Verifique se o resultado é válido
                                if (result != null && result.has("result")) {
                                    String status = result.get("result").getAsString();
                                    if ("sucesso".equals(status)) {

                                        String idChat = result.get("idChat").getAsString();
                                        Intent intent = new Intent(activityDetalhesPost.this, ChatActivity.class);
                                        intent.putExtra("idchat", idChat);
                                        startActivity(intent);

                                        startActivity(intent);
                                        Log.d("idChat", idChat);


                                    } else if ("erro ao criar chat!".equals(status)) {
                                        Log.d("erroChat", "ERRROOOO");

                                    } else {
                                        // Resposta inválida do servidor
                                        Toast.makeText(activityDetalhesPost.this, "erro_desconhecido1", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(activityDetalhesPost.this, R.string.erro_desconhecido, Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }
}