package com.example.zippy0001;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorPostagem;
import com.example.zippy0001.classes.PostagemGetterSetter;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    List<PostagemGetterSetter> postagemGetterSetterList;
    private static String HOST = "http://zippyinternacional.com/Android/";
    public static final String EXTRA_ORIGEM = "origem";
    public static final String EXTRA_DESTINO = "destino";


    RecyclerView PostagemRV;
    CardView cardImg;
     TextView destinoBarra, origemBarra;

    AdaptadorPostagem adaptadorPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postagemGetterSetterList = new ArrayList<>();

        origemBarra = findViewById(R.id.txtOrigemBarra);
        destinoBarra = findViewById(R.id.txtDestinoBarra);


        PostagemRV = findViewById(R.id.rvPostagem);
        carregarPostagem();



    }
    private void carregarPostagem() {

        String origem = getIntent().getStringExtra(EXTRA_ORIGEM);
        String destino = getIntent().getStringExtra(EXTRA_DESTINO);

        origemBarra.setText(origem);
        destinoBarra.setText(destino);

        String Caminho = "RecuperarPostagens.php";
        String CaminhoImagens = HOST+"uploads/produtos/";
        Log.d("destinos", origem + destino);

        Ion.with(this)
                .load(HOST + Caminho)
                .setBodyParameter("paisOrigem",origem)
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

                                    String NomeProduto = avaliacoesObject.get("nomeProduto").getAsString();
                                    String LinkProduto = avaliacoesObject.get("linkProduto").getAsString();
                                    String PrecoProduto = avaliacoesObject.get("precoProduto").getAsString();
                                    String PaisDestino = avaliacoesObject.get("paisDestino").getAsString();
                                    String CidadeDestino = avaliacoesObject.get("cidadeDestino").getAsString();
                                    String CaixaProduto = avaliacoesObject.get("caixaProduto").getAsString();
                                    String ImgProduto = avaliacoesObject.get("imgProduto").getAsString();
                                    String ImgCompleta = HOST+"uploads/produtos/"+ImgProduto;

                                    Log.d("testeRV", NomeProduto +","+ LinkProduto +","+ PrecoProduto +","+ PaisDestino +","+ CidadeDestino +","+ CaixaProduto +","+ ImgCompleta);
                                    PostagemGetterSetter postagemGetterSetter = new PostagemGetterSetter(NomeProduto, PrecoProduto, PaisDestino, CidadeDestino, CaixaProduto, ImgCompleta);
                                    postagemGetterSetterList.add(postagemGetterSetter);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(PostActivity.this, 2, GridLayoutManager.VERTICAL,false);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostActivity.this, RecyclerView.VERTICAL, false);
                                    PostagemRV.setLayoutManager(gridLayoutManager);
                                    adaptadorPostagem = new AdaptadorPostagem(PostActivity.this, postagemGetterSetterList);
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
    }
}