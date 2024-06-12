package com.example.zippy0001;

import static com.example.zippy0001.activityEditarPerfil.EXTRA_TRIGGER_PERFIL;
import static com.example.zippy0001.activityInicio.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorChats;
import com.example.zippy0001.classes.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
    RecyclerView rvChats;
    String idChat;
    private String URL_OBTER_CHATS = "https://zippyinternacional.com/Android/chat/buscarChats.php";
    Usuario usuario;

    @Override
    protected void onStart() {
        super.onStart();
        listaUsuarios.clear();
        obterChats();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChats = findViewById(R.id.rvChats);
        rvChats.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));


        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chat);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), activityInicio.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                Intent intent = new Intent(ChatActivity.this, activityInicio.class);
                intent.putExtra(EXTRA_TRIGGER_PERFIL, "PERFIL");//Add your return data here
                startActivity(intent);
                return true;
            } else return itemId == R.id.bottom_chat;
        });
    }

    private void obterChats() {
        listaUsuarios.clear();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuarioShared = sharedPreferences.getString("id_usuario", "");
        String nomeCliente = sharedPreferences.getString("nomeCliente", "");


        Ion.with(this)
                .load(URL_OBTER_CHATS)
                .setBodyParameter("idusuario", idUsuarioShared)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null) {
                                Log.e("obterChats", "Erro na requisição: " + e.getMessage());
                            } else {
                                JsonArray jsonArray = result.get("dados").getAsJsonArray();

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    Log.d("obterChats", "JSON Response: " + result.toString());

                                    JsonObject objectChats = jsonArray.get(i).getAsJsonObject();

                                    String dest = objectChats.get("DESTINATARIO").getAsString();
                                    String nome_dest = objectChats.get("NOME_DESTINATARIO").getAsString();
                                    String rem = objectChats.get("REMETENTE").getAsString();
                                    String nome_rem = objectChats.get("NOME_REMETENTE").getAsString();
                                    String nome_final = nome_dest;
                                    String id_final = dest;
                                    if (dest.equals(idUsuarioShared)) {
                                        id_final = rem;
                                    }
                                    if (nome_dest.equals(nomeCliente)) {
                                        nome_final = nome_rem;
                                    }
                                    Usuario usuarioDestinatario = new Usuario(
                                            id_final,
                                            nome_final);
//                                            objectChats.get("DESTINATARIO").getAsString(),
//                                            objectChats.get("NOME_DESTINATARIO").getAsString());

                                    listaUsuarios.add(usuarioDestinatario);
                                    idChat = objectChats.get("ID_CHAT").getAsString();
                                }
                                AdaptadorChats adaptador = new AdaptadorChats(ChatActivity.this, idUsuarioShared, listaUsuarios, idChat);
                                rvChats.setAdapter(adaptador);

                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }
}