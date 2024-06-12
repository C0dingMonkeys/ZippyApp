package com.example.zippy0001;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorPostagem;
import com.example.zippy0001.classes.PostagemGetterSetter;
import com.example.zippy0001.classes.DetalhesPedidosInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements DetalhesPedidosInterface {
    List<PostagemGetterSetter> postagemGetterSetterList;
    private static String HOST = "http://zippyinternacional.com/Android/";
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

        String Caminho = "RecuperarPostagens.php";
        String CaminhoImagens = HOST + "uploads/produtos/";
        Log.d("destinos", origem + destino);

        Ion.with(this)
                .load(HOST + Caminho)
                .setBodyParameter("paisOrigem", origem)
                .setBodyParameter("paisDestino", destino)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {


                        try {
                            Log.d("POSTACTIVITY", "Requisição completa: " + (e == null ? "sucesso" : "erro"));
                            if (e != null) {
                                Log.e("POSTACTIVITY", "Erro na requisição: " + e.getMessage());
                            } else {

                                for (int i = 0; i < result.size(); i++) {
                                    Log.d("POSTACTIVITY", "JSON Response: " + result.toString());

                                    JsonObject avaliacoesObject = result.get(i).getAsJsonObject();

                                    String IdPedido = avaliacoesObject.get("idPedido").getAsString();
                                    String Destinatario = avaliacoesObject.get("idClienteDest").getAsString();
                                    String NomeProduto = avaliacoesObject.get("nomeProduto").getAsString();
                                    String LinkProduto = avaliacoesObject.get("linkProduto").getAsString();
                                    String PrecoProduto = avaliacoesObject.get("precoProduto").getAsString();
                                    String PaisDestino = avaliacoesObject.get("paisDestino").getAsString();
                                    String CidadeDestino = avaliacoesObject.get("cidadeDestino").getAsString();
                                    String CaixaProduto = avaliacoesObject.get("caixaProduto").getAsString();
                                    String ImgProduto = avaliacoesObject.get("imgProduto").getAsString();
                                    String ImgCompleta = HOST + "uploads/produtos/" + ImgProduto;

                                    Log.d("testeRV", IdPedido + " " + Destinatario +" "+ NomeProduto + "," + LinkProduto + "," + PrecoProduto + "," + PaisDestino + "," + CidadeDestino + "," + CaixaProduto + "," + ImgCompleta);
                                    PostagemGetterSetter postagemGetterSetter = new PostagemGetterSetter(IdPedido, Destinatario, NomeProduto, PrecoProduto, PaisDestino, CidadeDestino, CaixaProduto, ImgCompleta,LinkProduto);
                                    postagemGetterSetterList.add(postagemGetterSetter);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(PostActivity.this, 2, GridLayoutManager.VERTICAL, false);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostActivity.this, RecyclerView.VERTICAL, false);
                                    PostagemRV.setLayoutManager(linearLayoutManager);
                                    adaptadorPostagem = new AdaptadorPostagem(PostActivity.this, postagemGetterSetterList, PostActivity.this);
                                    PostagemRV.setAdapter(adaptadorPostagem);
                                    adaptadorPostagem.notifyDataSetChanged();


                                    Log.d("POSTACTIVITY", postagemGetterSetter.getNomeProduto());
                                }
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }


                    }
                });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
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

        Log.d("testeDEtalhes", "oi"+postagemGetterSetterList.get(position).getIdPedido()+
                postagemGetterSetterList.get(position).getIdClienteDest()+
                postagemGetterSetterList.get(position).getLinkProduto()+
                postagemGetterSetterList.get(position).getCaixaProduto()+
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