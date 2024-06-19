package com.example.zippy0001;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorPostagem;
import com.example.zippy0001.classes.DetalhesPedidosInterface;
import com.example.zippy0001.classes.PostagemGetterSetter;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements DetalhesPedidosInterface {
    List<PostagemGetterSetter> postagemGetterSetterList;
    private static String HOST = "http://zippyinternacional.com/";
    public static final String EXTRA_ORIGEM = "origem";
    public static final String EXTRA_DESTINO = "destino";


    RecyclerView PostagemRV;
    ImageButton btnVoltar;
    TextView destinoBarra, origemBarra;

    AdaptadorPostagem adaptadorPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postagemGetterSetterList = new ArrayList<>();

        origemBarra = findViewById(R.id.txtOrigemBarra);
        destinoBarra = findViewById(R.id.txtDestinoBarra);

        btnVoltar = findViewById(R.id.btnVoltarPostagem);

        PostagemRV = findViewById(R.id.rvPostagem);
        carregarPostagem();

        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), activitySelecionarDestino.class));
            finish();
        });
    }


    private void carregarPostagem() {

        String origem = getIntent().getStringExtra(EXTRA_ORIGEM);
        String destino = getIntent().getStringExtra(EXTRA_DESTINO);

        origemBarra.setText(origem);
        destinoBarra.setText(destino);

        String Caminho = "Android/recuperarPostagem2.php";
        String CaminhoImagens = HOST + "uploads/produtos/";
        Log.d("destinos", origem + destino);

        Ion.with(this)
                .load(HOST + Caminho)
                .setBodyParameter("paisOrigem", origem)
                .setBodyParameter("paisDestino", destino)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        try {
                            Log.d("POSTACTIVITY", "Requisição completa: " + (e == null ? "sucesso" : "erro"));
                            if (e != null) {
                                Log.e("POSTACTIVITY", "Erro na requisição: " + e.getMessage());
                            } else {
                                if (result != null && result.has("status")) {
                                    String status = result.get("status").getAsString();
                                    if ("true".equals(status)) {
                                        JsonArray jsonArrayPosts = result.get("postagens").getAsJsonArray();

                                        for (int i = 0; i < jsonArrayPosts.size(); i++) {
                                            Log.d("obtermsgs", "JSON Response: " + result.toString());

                                            JsonObject objectMsg = jsonArrayPosts.get(i).getAsJsonObject();


                                            String IdPedido = objectMsg.get("ID_POSTAGEM").getAsString();
                                            String Destinatario = objectMsg.get("ID_CLIENTE").getAsString();
                                            String NomeProduto = objectMsg.get("PRODUTO_POSTAGEM").getAsString();
                                            String LinkProduto = objectMsg.get("LINK_REFERENCIA").getAsString();
                                            String PrecoProduto = objectMsg.get("VALOR_POSTAGEM").getAsString();
                                            String PaisDestino = objectMsg.get("PAIS_DESTINO").getAsString();
                                            String CidadeDestino = objectMsg.get("CIDADE_DESTINO").getAsString();
                                            String CaixaProduto = objectMsg.get("CAIXA").getAsString();
                                            String ImgProduto = objectMsg.get("IMAGEM_PRODUTO").getAsString();
                                            String ImgCompleta = HOST + "uploads/produtos/" + ImgProduto;

                                            Log.d("testeRV", IdPedido + " " + Destinatario + " " + NomeProduto + "," + LinkProduto + "," + PrecoProduto + "," + PaisDestino + "," + CidadeDestino + "," + CaixaProduto + "," + ImgCompleta);
                                            PostagemGetterSetter postagemGetterSetter = new PostagemGetterSetter(IdPedido, Destinatario, NomeProduto, PrecoProduto, PaisDestino, CidadeDestino, CaixaProduto, ImgCompleta, LinkProduto);
                                            postagemGetterSetterList.add(postagemGetterSetter);
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(PostActivity.this, 2, GridLayoutManager.VERTICAL, false);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostActivity.this, RecyclerView.VERTICAL, false);
                                            PostagemRV.setLayoutManager(linearLayoutManager);
                                            adaptadorPostagem = new AdaptadorPostagem(PostActivity.this, postagemGetterSetterList, PostActivity.this);
                                            PostagemRV.setAdapter(adaptadorPostagem);
                                            adaptadorPostagem.notifyDataSetChanged();


                                            Log.d("POSTACTIVITY", postagemGetterSetter.getNomeProduto());
                                        }
                                    } else if ("false".equals(status)) {
                                        Log.d("POSTACTIVITY", "nada pai");

                                        Toast.makeText(PostActivity.this, "nada pai", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (
                                JsonIOException ex) {
                            ex.printStackTrace();
                        }


                    }
                });

        getOnBackPressedDispatcher().

                addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        startActivity(new Intent(getApplicationContext(), activitySelecionarDestino.class));
                        finish();
                    }
                });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(PostActivity.this, activityDetalhesPost.class);
        String origem = getIntent().getStringExtra(EXTRA_ORIGEM);

        Log.d("testeDEtalhes", "oi" + postagemGetterSetterList.get(position).getIdPedido() +
                postagemGetterSetterList.get(position).getIdClienteDest() +
                postagemGetterSetterList.get(position).getLinkProduto() +
                postagemGetterSetterList.get(position).getCaixaProduto() +
                postagemGetterSetterList.get(position));

        intent.putExtra("idPedido", postagemGetterSetterList.get(position).getIdPedido());
        intent.putExtra("idClienteDest", postagemGetterSetterList.get(position).getIdClienteDest());
        intent.putExtra("NomePedido", postagemGetterSetterList.get(position).getNomeProduto());
        intent.putExtra("ValorPedido", postagemGetterSetterList.get(position).getPrecoProduto());
        intent.putExtra("CaixaPedido", postagemGetterSetterList.get(position).getCaixaProduto());
        intent.putExtra("LinkPedido", postagemGetterSetterList.get(position).getLinkProduto());
        intent.putExtra("PaisOrigem", origem);
        intent.putExtra("PaisDestino", postagemGetterSetterList.get(position).getPaisDestino());
        intent.putExtra("CidadeDestino", postagemGetterSetterList.get(position).getCidadeDestino());
        intent.putExtra("ImgProduto", postagemGetterSetterList.get(position).getImgProduto());

        startActivity(intent);
    }
}