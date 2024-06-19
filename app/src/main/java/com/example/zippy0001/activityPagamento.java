package com.example.zippy0001;

import static com.example.zippy0001.PerfilFragment.EXTRA_BACK_PERFIL;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.zippy0001.classes.Usuario;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

public class activityPagamento extends AppCompatActivity {

    private String URL_OBTER_PEDIDO = "https://zippyinternacional.com/Android/obterPedido.php";
    private String BASE_URL_IMG = "https://zippyinternacional.com/Android/uploads/produtos/";
    private String URL_UPDATE_STATUS = "https://zippyinternacional.com/Android/updateStatusPedido.php";


    ImageView imgProduto, imgMetodoPagamento, imgDialog;
    TextView numeroPedido, nomePedido, paisDestino, cidadeDestino, tipoCaixa, subtotal, porcentagemViajante, porcentagemZippy, total, pagamentoEscolhido;
    Double valorSubtotal;
    String valorPedido, idPedido;
    Button btnPagar;
    RelativeLayout metodoPagamento;
    String metodo = "pix";
    Dialog dialog;
    Button btnDialog;
    TextView tituloDialog, subtituloDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        valorPedido = getIntent().getStringExtra("valor_pedido");
        valorSubtotal = Double.parseDouble(valorPedido);
        idPedido = getIntent().getStringExtra("id_pedido");

        metodoPagamento = findViewById(R.id.layoutMetodoPagamento);
        btnPagar = findViewById(R.id.btnPagamento);

        imgProduto = findViewById(R.id.imgProdutoPagamento);
        imgMetodoPagamento = findViewById(R.id.imgMetodoPagamento);


        numeroPedido = findViewById(R.id.txtNumeroPedidoPagamento);
        nomePedido = findViewById(R.id.txtNomePedidoPagamento);
        paisDestino = findViewById(R.id.txtPaisDestinoPagamento);
        cidadeDestino = findViewById(R.id.txtCidadeDestinoPagamento);
        tipoCaixa = findViewById(R.id.txtTipoCaixaPagamento);
        pagamentoEscolhido  = findViewById(R.id.txtMetodoPagamento);

        subtotal = findViewById(R.id.txtSubtotalPagamento);
        porcentagemViajante = findViewById(R.id.txtPorcentagemViajante);
        porcentagemZippy = findViewById(R.id.txtPorcentagemZippy);

        total = findViewById(R.id.txtTotalPagamento);

        obterPedido();

        metodoPagamento.setOnClickListener(v -> {
            bslMetodoPagamento();
        });
        btnPagar.setOnClickListener(v -> {
            if (metodo.equals("CartaoCredito")) {
                showDialog();
                atualizarPagamento();
            } else if (metodo.equals("pix")) {
                Intent intent = new Intent(this, pagamentoPix.class);
                intent.putExtra("id_pedido", idPedido);
                startActivity(intent);
            }
        });

    }

    private void obterPedido() {

        Double numPorcentagemViajante = valorSubtotal * 0.10;
        Double numPorcentagemZippy = valorSubtotal * 0.05;
        Double numTotalFinal = valorSubtotal + numPorcentagemViajante + numPorcentagemZippy;

        String subtotalFinal = String.format("%.2f", valorSubtotal);
        String porcetagemViajanteFinal = String.format("%.2f", numPorcentagemViajante);
        String porcentagemZippyFinal = String.format("%.2f", numPorcentagemZippy);
        String totalFinal = String.format("%.2f", numTotalFinal);


        Ion.with(this)
                .load(URL_OBTER_PEDIDO)
                .setBodyParameter("idPedido", idPedido)
                .asJsonObject()
                .setCallback((e, result) -> {
                    try {
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("true".equals(status)) {
                                    JsonArray jsonArrayMsg = result.get("Pedido").getAsJsonArray();


                                    for (int i = 0; i < jsonArrayMsg.size(); i++) {
                                        Log.d("obterDadosPedido", "JSON Response: " + result.toString());

                                        JsonObject objectMsg = jsonArrayMsg.get(i).getAsJsonObject();
                                        String fotoPedido = objectMsg.get("IMAGEM_PRODUTO").getAsString();
                                        Picasso.get().load(BASE_URL_IMG + fotoPedido).into(imgProduto);
                                        numeroPedido.setText("Pedido#"+objectMsg.get("ID_POSTAGEM").getAsString());
                                        nomePedido.setText("Nome:"+objectMsg.get("PRODUTO_POSTAGEM").getAsString());
                                        paisDestino.setText("Pais de Destino:"+objectMsg.get("PAIS_DESTINO").getAsString());
                                        cidadeDestino.setText("Cidade de Destino:"+objectMsg.get("CIDADE_DESTINO").getAsString());
                                        tipoCaixa.setText("Tipo de caixa:"+objectMsg.get("CAIXA").getAsString());
                                        subtotal.setText(subtotalFinal);
                                        porcentagemViajante.setText(porcetagemViajanteFinal);
                                        porcentagemZippy.setText(porcentagemZippyFinal);
                                        total.setText(totalFinal);


                                    }
                                } else {

                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(this, R.string.erro_desconhecido, Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JsonIOException ex) {
                        ex.printStackTrace();
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra("atualizarCartao")) {
            Toast.makeText(this, "CHEGOU", Toast.LENGTH_SHORT).show();
            metodo = "CartaoCredito";
            int imgCartao = getIntent().getIntExtra("iconeCartao", 0);
            String numerosCartao = getIntent().getStringExtra("ultimosDigitos");

            imgMetodoPagamento.setImageResource(imgCartao);
            pagamentoEscolhido.setText(numerosCartao);
        }

    }
    private void showDialog(){
        dialog = new Dialog(activityPagamento.this);
        dialog.setContentView(R.layout.dialog_sucess);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_edittext_focado));
        dialog.setCancelable(false);

        btnDialog = dialog.findViewById(R.id.btnContinuarDialog);
        tituloDialog = dialog.findViewById(R.id.tituloDialogSucesso);
        subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
        imgDialog = dialog.findViewById(R.id.imgDialog);

        btnDialog.setOnClickListener(v1 -> {
            Intent intent = new Intent(activityPagamento.this, activityInicio.class);
            startActivity(intent);
        });
    }
    private void atualizarPagamento() {
        Ion.with(this)
                .load(URL_UPDATE_STATUS)
                .setBodyParameter("idPedido", idPedido)
                .asJsonObject()
                .setCallback((e, result) -> {
                    try {
                        if (e != null) {
                            // Ocorreu um erro de conexão ou outra exceção
                            Log.e("Perfil", "Erro na requisição0: " + e.getMessage());
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                if ("ok".equals(status)) {
                                    tituloDialog.setText("Sucesso");
                                    subtituloDialog.setText("Pedido pago com sucesso, acompanhe o andamento na tela de Meus Pedidos.");
                                    dialog.show();

                                } else if ("erro".equals(status)) {
                                    imgDialog.setImageResource(R.drawable.ic_error);
                                    tituloDialog.setText("Erro");
                                    subtituloDialog.setText("Ocorreu um erro ao processar seu pagamento. Tente Novamente mais tarde.");
                                    dialog.show();
                                    Log.e("Perfil", "Erro ao realizar pagamento");
                                } else {
                                    // Resposta inválida do servidor
                                    Log.e("Perfil", "Erro na requisição2: " + result);
                                }
                            } else {
                                // Resposta inválida do servidor
                                Log.e("Perfil", "Erro na requisição3: " + result);
                            }
                        }
                    } catch (JsonIOException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    private void bslMetodoPagamento() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityPagamento.this);
        View view1 = LayoutInflater.from(activityPagamento.this).inflate(R.layout.bottom_sheet_layout_pagamento, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

        TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheet);
        RadioGroup rgMetodos = view1.findViewById(R.id.rgPagamento);


        AppCompatButton btnSalvarMetodo = view1.findViewById(R.id.btnSalvarMetodo);

        txtTituloBSL.setText("Escolha um método de pagamento");

        rgMetodos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioMetodo = group.findViewById(checkedId);
                int position = group.indexOfChild(radioMetodo);

                switch (position) {
                    case 0:
                        metodo = "pix";
                        break;
                    case 1:
                        metodo = "CartaoCredito";
                        break;
                }

            }
        });


        btnSalvarMetodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (metodo.equals("CartaoCredito")){
                    Intent intent = new Intent(activityPagamento.this, activityCadastrarCartao.class);
                    intent.putExtra("id_pedido", idPedido);
                    intent.putExtra("valor_pedido", valorPedido);
                    startActivity(intent);

                }
                bottomSheetDialog.dismiss();

            }
        });
    }
}