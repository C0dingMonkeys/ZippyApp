package com.example.zippy0001;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonIOException;
import com.koushikdutta.ion.Ion;

public class pagamentoPix extends AppCompatActivity {
    private String URL_UPDATE_STATUS = "https://zippyinternacional.com/Android/updateStatusPedido.php";

    Button copiarCodigo;
    TextView pagamentoFeito;
    ImageView qrCode, imgDialog;
    String idPedido;
    Dialog dialog;
    Button btnDialog;

    TextView tituloDialog, subtituloDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_pix);

        copiarCodigo = findViewById(R.id.btnCopiarPix);
        pagamentoFeito = findViewById(R.id.pagamentoFeito);

        pagamentoFeito.setPaintFlags(pagamentoFeito.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


        qrCode = findViewById(R.id.imgQrCodePix);

        idPedido = getIntent().getStringExtra("id_pedido");

        copiarCodigo.setOnClickListener(v -> {
            Toast.makeText(pagamentoPix.this, "Código copiado para a área de transferencia", Toast.LENGTH_SHORT).show();
        });

        pagamentoFeito.setOnClickListener(v -> {

            dialog = new Dialog(pagamentoPix.this);
            dialog.setContentView(R.layout.dialog_sucess);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_edittext_focado));
            dialog.setCancelable(false);

            btnDialog = dialog.findViewById(R.id.btnContinuarDialog);
            tituloDialog = dialog.findViewById(R.id.tituloDialogSucesso);
            subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
            imgDialog = dialog.findViewById(R.id.imgDialog);

            btnDialog.setOnClickListener(v1 -> {
                Intent intent = new Intent(pagamentoPix.this, activityInicio.class);
                startActivity(intent);
            });

            atualizarPagamento();
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
}