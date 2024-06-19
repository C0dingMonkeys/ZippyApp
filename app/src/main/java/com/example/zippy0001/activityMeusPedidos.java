package com.example.zippy0001;

import static com.example.zippy0001.PerfilFragment.SHARED_PREFS;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.classes.AdaptadorMeusPedidos;
import com.example.zippy0001.classes.MeusPedidosGetterSetter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class activityMeusPedidos extends AppCompatActivity implements MeusPedidosInterface {
    List<MeusPedidosGetterSetter> meusPedidosList;
    private static String HOST = "http://zippyinternacional.com/Android/";
    private static String URL_MEUS_PEDIDOS = "obterMeusPedidos.php";

    RecyclerView meusPedidosRV;
    TabLayout tabLayout;

    AdaptadorMeusPedidos adaptadorMeusPedidos;
    LinearLayout semPedidosLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pedidos);
        meusPedidosList = new ArrayList<>();

        meusPedidosRV = findViewById(R.id.rvMeusPedidos);
        tabLayout = findViewById(R.id.tabLayoutMeusPedidos);
        semPedidosLayout = findViewById(R.id.layoutSemPedidos);

        tabLayout.addTab(tabLayout.newTab().setText("Pendentes"));
        tabLayout.addTab(tabLayout.newTab().setText("Pagos"));
        tabLayout.addTab(tabLayout.newTab().setText("Concluidos"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterOrders(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        obterMeusPedidos();


    }

    private void filterOrders(int tabPosition) {
        List<MeusPedidosGetterSetter> filteredList = new ArrayList<>();
        for (MeusPedidosGetterSetter order : meusPedidosList) {
            if (tabPosition == 0 && order.getStatus().equals("Pendente")) {
                semPedidosLayout.setVisibility(View.GONE);
                meusPedidosRV.setVisibility(View.VISIBLE);

                // Add order to filteredList for "Pendentes" tab
                filteredList.add(order);
            } else if (tabPosition == 1 && order.getStatus().equals("Pago")) {
                semPedidosLayout.setVisibility(View.GONE);
                meusPedidosRV.setVisibility(View.VISIBLE);


                // Add order to filteredList for "Pagos" tab
                filteredList.add(order);
            } else if (tabPosition == 2 && order.getStatus().equals("Entregue")) {
                semPedidosLayout.setVisibility(View.GONE);
                meusPedidosRV.setVisibility(View.VISIBLE);

                // Add order to filteredList for "Concluidos" tab
                filteredList.add(order);
            }
        }
        if (tabPosition == 2 && filteredList.isEmpty()) {
            meusPedidosRV.setVisibility(View.GONE);
            semPedidosLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Você não possui nenhum pedido entregue no momento.", Toast.LENGTH_SHORT).show();
        }

        // Update adapter with filtered data and notify RecyclerView
        adaptadorMeusPedidos.updateOrders(filteredList);

    }

    private void obterMeusPedidos() {
        SharedPreferences sharedPreferences = activityMeusPedidos.this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idCliente = sharedPreferences.getString("id_usuario", "");

        Ion.with(this)
                .load(HOST + URL_MEUS_PEDIDOS)
                .setBodyParameter("idCliente", idCliente)
                .asJsonObject()
                .setCallback((e, result) -> {
                    try {
                        Log.d("MeusPedidosLogTry", "Requisição completa: " + (e == null ? "sucesso" : "erro"));
                        if (e != null) {
                            Log.e("MeusPedidosLogErro", "Erro na requisição: " + e.getMessage());
                        } else {
                            JsonArray jsonArrayMeusPedidos = result.get("pedidos").getAsJsonArray();

                            for (int i = 0; i < result.size(); i++) {
                                Log.d("MeusPedidosLogJSON", "JSON Response: " + result.toString());

                                JsonObject objectPedido = jsonArrayMeusPedidos.get(i).getAsJsonObject();

                                String status = objectPedido.get("STATUS_POSTAGEM").getAsString();
                                String numPedido = objectPedido.get("ID_POSTAGEM").getAsString();
                                String nomePedido = objectPedido.get("PRODUTO_POSTAGEM").getAsString();
                                String destinoPedido = objectPedido.get("PAIS_DESTINO").getAsString();
                                String dataPedido = objectPedido.get("DTA_POSTAGEM").getAsString();
                                String imgPedido = objectPedido.get("IMAGEM_PRODUTO").getAsString();
                                String imgCompleta = HOST + "uploads/produtos/" + imgPedido;

                                Log.d("testeRV", status + " " + numPedido + " " + nomePedido + "," + destinoPedido + "," + dataPedido + "," + imgPedido + "," + imgCompleta);
                                MeusPedidosGetterSetter meusPedidosGetterSetter = new MeusPedidosGetterSetter(status, numPedido, nomePedido, destinoPedido, dataPedido, imgCompleta);
                                meusPedidosList.add(meusPedidosGetterSetter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                                meusPedidosRV.setLayoutManager(linearLayoutManager);
                                adaptadorMeusPedidos = new AdaptadorMeusPedidos(this, meusPedidosList, this);
                                meusPedidosRV.setAdapter(adaptadorMeusPedidos);
                                filterOrders(0);


                                Log.d("MeusPedidosLogTesteCampos", meusPedidosGetterSetter.getStatus() + meusPedidosGetterSetter.getPedido() + meusPedidosGetterSetter.getNome() + meusPedidosGetterSetter.getDestino() + meusPedidosGetterSetter.getData() + meusPedidosGetterSetter.getImgPedido());
                            }
                        }

                    } catch (JsonIOException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        for (MeusPedidosGetterSetter order : meusPedidosList) {
            if (order.getStatus().equals("Pendente")) {
                excluirPedido();
            } else if (order.getStatus().equals("Pago")) {
                confirmarEntrega();
            }
        }
    }

    private void confirmarEntrega() {
    }

    private void excluirPedido() {
        
    }
}