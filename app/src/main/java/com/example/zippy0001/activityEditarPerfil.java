package com.example.zippy0001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class activityEditarPerfil extends AppCompatActivity {

    private RelativeLayout editarNome, editarTelefone, editarIdentidade;
    private ImageButton voltar;
    private Button salvarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        editarNome = findViewById(R.id.layoutEditarNomeTela);
        editarTelefone = findViewById(R.id.layoutEditarFoneTela);
        editarIdentidade = findViewById(R.id.layoutEditarIdentidadeTela);

        editarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityEditarPerfil.this);
                View view1 = LayoutInflater.from(activityEditarPerfil.this).inflate(R.layout.bottom_sheet_layout, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

                TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheet);
                TextInputLayout textInputLayout = view1.findViewById(R.id.TextLayoutEdicao);
                TextInputEditText textInputEditText = view1.findViewById(R.id.TextEditEdicao);
                AppCompatButton button = view1.findViewById(R.id.btnSalvarEdicao);

                txtTituloBSL.setText(R.string.lbl_edite_seu_nome);
                textInputEditText.setText("Luiz da Silva Sauro");


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                editarTelefone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityEditarPerfil.this);
                        View view1 = LayoutInflater.from(activityEditarPerfil.this).inflate(R.layout.bottom_sheet_layout, null);
                        bottomSheetDialog.setContentView(view1);
                        bottomSheetDialog.show();

                        TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheet);
                        TextInputLayout textInputLayout = view1.findViewById(R.id.TextLayoutEdicao);
                        TextInputEditText textInputEditText = view1.findViewById(R.id.TextEditEdicao);
                        AppCompatButton button = view1.findViewById(R.id.btnSalvarEdicao);

                        txtTituloBSL.setText(R.string.lbl_edite_seu_telefone);
                        textInputEditText.setText("11941866230");
                    }
                });

                editarIdentidade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }
}