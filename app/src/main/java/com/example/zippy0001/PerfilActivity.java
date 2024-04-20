package com.example.zippy0001;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorAvaliacao;
import com.example.zippy0001.classes.AvaliacoesGetterSetter;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {
    private static String HOST = "http://zippyinternacional.com/Android/RecuperarRecyclerAvaliacoes.php";
    RecyclerView Avaliacao1;
    ImageButton btnVoltar;
    List<AvaliacoesGetterSetter> avaliacoesGetterSettersList;
    AdaptadorAvaliacao adaptadorAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Log.d("PerfilActivity", "onCreate chamado");

        Avaliacao1 = findViewById(R.id.rvAvaliacao1);
        btnVoltar = findViewById(R.id.imgBtnPerfil);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), activityInicio.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });


        // Inicializar a lista de dispositivos vazia

        avaliacoesGetterSettersList = new ArrayList<>();
        // Inicializar o adaptador com a lista de dispositivos vazia

        carregarAvaliacoes();
        // Carregar os dados do banco de dados

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), activityInicio.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();

            }
        });

    }

    private void carregarAvaliacoes() {


        Ion.with(this)
                .load(HOST)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {


                        try {
                            Log.d("PerfilActivity", "Requisição completa: " + (e == null ? "sucesso" : "erro"));
                            if (e != null) {
                                Log.e("PerfilActivity", "Erro na requisição: " + e.getMessage());
                            } else {

                                for (int i = 0; i < result.size(); i++) {
                                    Log.d("PerfilActivity", "JSON Response: " + result.toString());

                                    JsonObject avaliacoesObject = result.get(i).getAsJsonObject();

                                    int Id = avaliacoesObject.get("id").getAsInt();
                                    String Nome01 = avaliacoesObject.get("nome01").getAsString();
                                    String Nome02 = avaliacoesObject.get("nome02").getAsString();
                                    String Pais01 = avaliacoesObject.get("pais01").getAsString();
                                    String Pais02 = avaliacoesObject.get("pais02").getAsString();
                                    String Perfil01 = avaliacoesObject.get("perfil01").getAsString();
                                    String Perfil02 = avaliacoesObject.get("perfil02").getAsString();
                                    AvaliacoesGetterSetter avaliacoesGetterSetter = new AvaliacoesGetterSetter(Nome01, Nome02, Pais01, Pais02, Perfil01, Perfil02);
                                    avaliacoesGetterSettersList.add(avaliacoesGetterSetter);

                                    Avaliacao1.setLayoutManager(new LinearLayoutManager(PerfilActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                    adaptadorAvaliacao = new AdaptadorAvaliacao(PerfilActivity.this, avaliacoesGetterSettersList);
                                    Avaliacao1.setAdapter(adaptadorAvaliacao);
                                    adaptadorAvaliacao.notifyDataSetChanged();




                                    Log.d("perfil01", avaliacoesGetterSetter.getPerfil01());
                                }
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }



                    }
                });
    }
}
