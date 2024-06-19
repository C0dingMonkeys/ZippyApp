package com.example.zippy0001;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.zippy0001.classes.AdaptadorListaCartao;
import com.example.zippy0001.classes.ArrayListaCartoes;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class activityCadastrarCartao extends AppCompatActivity {
    // Constantes para os tipos de cartão
    private static final String CARD_AMEX = "3";
    private static final String CARD_VISA = "4";
    private static final String CARD_MASTER = "5";
    private static final String CARD_DISCOVER = "6";
    private static final  String EXTRA_CARTAO_SELECIONADO = "cartaoSelecionado";
    private ListView listaCartoes;
    private final ArrayList<ArrayListaCartoes> arrayCartoes = new ArrayList<>();
    RelativeLayout addCartaoLayout;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cartao);
        String idPedido = getIntent().getStringExtra("id_pedido");
        String valorPedido = getIntent().getStringExtra("valor_pedido");


        addCartaoLayout = findViewById(R.id.addCartaoLayout);
        addCartaoLayout.setOnClickListener(v -> {
            bslAddCartao();
        });


        listaCartoes = findViewById(R.id.listaCartoes);

        listaCartoes.setOnItemClickListener((parent, view, position, id) -> {
            ArrayListaCartoes cartaoSelecionado = arrayCartoes.get(position);
            Intent intent = new Intent(activityCadastrarCartao.this, activityPagamento.class);
            intent.putExtra("atualizarCartao", "cartão");
            intent.putExtra("id_pedido", idPedido);
            intent.putExtra("valor_pedido", valorPedido);
            intent.putExtra("iconeCartao", cartaoSelecionado.getImagem());
            intent.putExtra("ultimosDigitos", cartaoSelecionado.getNumCartao());
            startActivity(intent);
        });

    }

    private void bslAddCartao() {
        bottomSheetDialog = new BottomSheetDialog(activityCadastrarCartao.this);
        View view1 = LayoutInflater.from(activityCadastrarCartao.this).inflate(R.layout.bottom_sheet_layout_add_cartao, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behavior.setSkipCollapsed(true);
                }
            }
        });

        bottomSheetDialog.show();

        TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheet);
        EditText txtNomeCartao = view1.findViewById(R.id.TextEditCartaoNome);
        EditText txtCartaoNum = view1.findViewById(R.id.TextEditCartaoNum);
        EditText txtCartaoValidade = view1.findViewById(R.id.TextEditCartaoValidade);
        EditText txtCartaoCVV = view1.findViewById(R.id.TextEditCartaoCVV);

        txtTituloBSL.setText("Adicionar Cartão");

        AppCompatButton btnSalvarCartao = view1.findViewById(R.id.btnSalvarCartao);
        btnSalvarCartao.setOnClickListener(v -> {
            salvarCartao(txtNomeCartao, txtCartaoNum, txtCartaoValidade, txtCartaoCVV);
        });



    }

    private void salvarCartao(EditText etNomeCartao, EditText etNumCartao, EditText etValidadeCartao, EditText etCVVCartao) {
        String nomeCartao = etNomeCartao.getText().toString().trim();
        String numCartao = etNumCartao.getText().toString().trim();
        String validadeCartao = etValidadeCartao.getText().toString().trim();
        String cvvCartao = etCVVCartao.getText().toString().trim();

        if (nomeCartao.isEmpty() || numCartao.isEmpty() || validadeCartao.isEmpty() || cvvCartao.isEmpty()) {
            Toast.makeText(activityCadastrarCartao.this, "Erro Ao processar dados do cartão...", Toast.LENGTH_SHORT).show();
            return;
        }

        String verificadorCartao = numCartao.substring(0, 1);
        int tamanhoCartao = numCartao.length();
        String ultimosDigitos = "***" + numCartao.substring(tamanhoCartao - 4);

        int iconeCartao = getCartaoIcone(verificadorCartao);
        if (iconeCartao == -1) {
            Toast.makeText(activityCadastrarCartao.this, "Número do cartão inválido", Toast.LENGTH_SHORT).show();
        } else {
            arrayCartoes.add(new ArrayListaCartoes(iconeCartao, ultimosDigitos));
            AdaptadorListaCartao adaptadorListaCartao = new AdaptadorListaCartao(this, R.layout.list_row, arrayCartoes);
            listaCartoes.setAdapter(adaptadorListaCartao);
            bottomSheetDialog.dismiss();
        }
    }

    private int getCartaoIcone(String verificadorCartao) {
        switch (verificadorCartao) {
            case CARD_AMEX:
                return R.drawable.ic_amex;
            case CARD_VISA:
                return R.drawable.ic_visa;
            case CARD_MASTER:
                return R.drawable.ic_master;
            case CARD_DISCOVER:
                return R.drawable.ic_discover;
            default:
                return -1; // Valor para indicar que o cartão é inválido
        }
    }
}