package com.example.zippy0001;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.zippy0001.classes.FileModel;
import com.example.zippy0001.classes.HttpService;
import com.example.zippy0001.classes.RetrofitBuilder;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class activityCriarPost extends AppCompatActivity {

    private String BASE_URL = "https://zippyinternacional.com/Android/";
    TextInputLayout LayoutNomeProd, LayoutPrecoProd, LayoutLinkReferencia, LayoutPaisOrigem, LayoutCidadeOrigem, LayoutEstadoOrigem, LayoutPaisDestino, LayoutCidadeDestino, LayoutEstadoDestino;
    TextInputEditText TextNomeProd, TextPrecoProd, TextLinkReferencia, TextPaisOrigem, TextCidadeOrigem, TextEstadoOrigem, TextPaisDestino, TextCidadeDestino, TextEstadoDestino;
    LinearLayout fotoProduto;
    ImageView ic_imgUpload;
    TextView txtUpload, warningCaixa;
    CheckBox CheckCaixaProduto;
    private String ImagemPadrao = "https://zippyinternacional.com/uploads/produtos/produtoDefault.png";
    Uri uri;
    private CharSequence[] options = {"Camera", "Galeria", "Cancelar"};
    Button testeFuncao;
    ImageButton btnVoltar;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final int REMOVE_IMG = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_post);


        requirePermission();

        LayoutNomeProd = findViewById(R.id.LayoutNomePost);
        LayoutPrecoProd = findViewById(R.id.LayoutPrecoPost);
        LayoutLinkReferencia = findViewById(R.id.LayoutLinkPost);
        LayoutPaisOrigem = findViewById(R.id.LayoutPaisOrigemPost);
        LayoutCidadeOrigem = findViewById(R.id.LayoutCidadeOrigemPost);
        LayoutEstadoOrigem = findViewById(R.id.LayoutEstadoOrigemPost);
        LayoutPaisDestino = findViewById(R.id.LayoutPaisDestinoPost);
        LayoutCidadeDestino = findViewById(R.id.LayoutCidadeDestinoPost);
        LayoutEstadoDestino = findViewById(R.id.LayoutEstadoDestinoPost);

        TextNomeProd = findViewById(R.id.TextEditNomePost);
        TextPrecoProd = findViewById(R.id.TextEditPrecoPost);
        TextLinkReferencia = findViewById(R.id.TextEditLinkPost);
        TextPaisOrigem = findViewById(R.id.TextEditPaisOrigemPost);
        TextCidadeOrigem = findViewById(R.id.TextEditCidadeOrigemPost);
        TextEstadoOrigem = findViewById(R.id.TextEditEstadoOrigemPost);
        TextPaisDestino = findViewById(R.id.TextEditPaisDestinoPost);
        TextCidadeDestino = findViewById(R.id.TextEditCidadeDestinoPost);
        TextEstadoDestino = findViewById(R.id.TextEditEstadoDestinoPost);
        txtUpload = findViewById(R.id.txtImgUpload);
        ic_imgUpload = findViewById(R.id.ic_imgUpload);
        fotoProduto = findViewById(R.id.layoutUploadImg);

        warningCaixa = findViewById(R.id.warning_caixa);
        CheckCaixaProduto = findViewById(R.id.checkboxCaixaPost);
        btnVoltar = findViewById(R.id.btnVoltarPost);
        testeFuncao = findViewById(R.id.testefuncao);

        CheckCaixaProduto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CheckCaixaProduto.isChecked()) {
                    warningCaixa.setVisibility(View.VISIBLE);
                } else {
                    warningCaixa.setVisibility(View.GONE);
                }
            }
        });

        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), activityInicio.class));
            finish();
        });
        testeFuncao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarPostagem();
            }
        });

        fotoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri == null) {
                    ImagePicker.Companion.with(activityCriarPost.this)
                            .crop()
                            .crop(4, 2)
                            .maxResultSize(512, 512)
                            .start(101);
                } else {
                    BSLremoveImg();
                }
            }
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), activityInicio.class));
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
                uri = data.getData();
                Drawable imgProduto = Drawable.createFromPath(uri.getPath());
                txtUpload.setVisibility(View.INVISIBLE);
                ic_imgUpload.setVisibility(View.INVISIBLE);
                fotoProduto.setBackground(imgProduto);

            } else {
                Toast.makeText(getApplicationContext(), "sem img", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void requirePermission() {
        ActivityCompat.requestPermissions(activityCriarPost.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);
        ActivityCompat.requestPermissions(activityCriarPost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void criarPostagem() {

        if (uri != null) {


            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            String idUsuarioShared = sharedPreferences.getString("id_usuario", "");

            String nomeProd = TextNomeProd.getText().toString().trim();
            String precoProd = TextPrecoProd.getText().toString().trim();
            String linkProd = TextLinkReferencia.getText().toString().trim();
            String paisOrigemProd = TextPaisOrigem.getText().toString().trim();
            String cidadeOrigemProd = TextCidadeOrigem.getText().toString().trim();
            String estadoOrigemProd = TextEstadoOrigem.getText().toString().trim();
            String paisDestinoProd = TextPaisDestino.getText().toString().trim();
            String cidadeDestinoProd = TextCidadeDestino.getText().toString().trim();
            String estadoDestinoProd = TextEstadoDestino.getText().toString().trim();
            String caixa = "";
            if (CheckCaixaProduto.isChecked()) {
                caixa = "Original";
            } else {
                caixa = "Avulsa";
            }

            File imagem = new File(uri.getPath());

            Log.d("erroNome", uri.getPath());


            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imagem);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage", imagem.getName(), requestBody);

            RequestBody post_idCliente = RequestBody.create(MediaType.parse("multipart/form-data"), idUsuarioShared);
            RequestBody post_nome = RequestBody.create(MediaType.parse("multipart/form-data"), nomeProd);
            RequestBody post_preco = RequestBody.create(MediaType.parse("multipart/form-data"), precoProd);
            RequestBody post_link = RequestBody.create(MediaType.parse("multipart/form-data"), linkProd);
            RequestBody post_paisOrigem = RequestBody.create(MediaType.parse("multipart/form-data"), paisOrigemProd);
            RequestBody post_cidadeOrigem = RequestBody.create(MediaType.parse("multipart/form-data"), cidadeOrigemProd);
            RequestBody post_ufOrigem = RequestBody.create(MediaType.parse("multipart/form-data"), estadoOrigemProd);
            RequestBody post_paisDestino = RequestBody.create(MediaType.parse("multipart/form-data"), paisDestinoProd);
            RequestBody post_cidadeDestino = RequestBody.create(MediaType.parse("multipart/form-data"), cidadeDestinoProd);
            RequestBody post_ufDestino = RequestBody.create(MediaType.parse("multipart/form-data"), estadoDestinoProd);
            RequestBody post_caixa = RequestBody.create(MediaType.parse("multipart/form-data"), caixa);

            HttpService service = RetrofitBuilder.getClient().create(HttpService.class);

            Call<FileModel> call = service.callUploadApiPost(filePart, post_idCliente, post_nome, post_preco, post_link, post_paisOrigem, post_cidadeOrigem, post_ufOrigem, post_paisDestino, post_cidadeDestino, post_ufDestino, post_caixa);
            call.enqueue(new Callback<FileModel>() {
                @Override
                public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                    FileModel fileModel = response.body();
                    //Toast.makeText(activityCriarPost.this, fileModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("erro", fileModel.getMessage());

                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(activityCriarPost.this);
                    fazerLogin.setTitle(R.string.titulo_dialog_posts);
                    fazerLogin.setMessage(R.string.corpo_dialog_posts);
                    fazerLogin.setCancelable(true);
                    fazerLogin.create().show();

                }

                @Override
                public void onFailure(Call<FileModel> call, Throwable t) {
                    //Toast.makeText(activityCriarPost.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("erro", t.getMessage());


                }
            });

        } else {

            cadastroDadosPostagemPadrao();

        }


    }

    private void BSLremoveImg() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityCriarPost.this);
        View view1 = LayoutInflater.from(activityCriarPost.this).inflate(R.layout.bottom_sheet_layout_remove_img, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

        Button botao = view1.findViewById(R.id.btnRemoverImg);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = null;
                fotoProduto.setBackgroundResource(R.drawable.bg_tracos);
                txtUpload.setVisibility(View.VISIBLE);
                ic_imgUpload.setVisibility(View.VISIBLE);
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void cadastroDadosPostagemPadrao() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuarioShared = sharedPreferences.getString("id_usuario", "");

        String nomeProd = TextNomeProd.getText().toString().trim();
        String precoProd = TextPrecoProd.getText().toString().trim();
        String linkProd = TextLinkReferencia.getText().toString().trim();
        String paisOrigemProd = TextPaisOrigem.getText().toString().trim();
        String cidadeOrigemProd = TextCidadeOrigem.getText().toString().trim();
        String estadoOrigemProd = TextEstadoOrigem.getText().toString().trim();
        String paisDestinoProd = TextPaisDestino.getText().toString().trim();
        String cidadeDestinoProd = TextCidadeDestino.getText().toString().trim();
        String estadoDestinoProd = TextEstadoDestino.getText().toString().trim();
        String caixa = "";
        if (CheckCaixaProduto.isChecked()) {
            caixa = "Original";
        } else {
            caixa = "Avulsa";
        }

        String Host = "criarPostagempadrao.php";
        Ion.with(this)
                .load(BASE_URL + Host)
                .setBodyParameter("id_cliente", idUsuarioShared)
                .setBodyParameter("nomeProduto", nomeProd)
                .setBodyParameter("preco", precoProd)
                .setBodyParameter("link", linkProd)
                .setBodyParameter("paisOrigem", paisOrigemProd)
                .setBodyParameter("cidadeOrigem", cidadeOrigemProd)
                .setBodyParameter("ufOrigem", estadoOrigemProd)
                .setBodyParameter("paisDestino", paisDestinoProd)
                .setBodyParameter("cidadeDestino", cidadeDestinoProd)
                .setBodyParameter("ufDestino", estadoDestinoProd)
                .setBodyParameter("caixa", caixa)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.d("errogay", e.toString());
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(activityCriarPost.this, R.string.erro_conexao, Toast.LENGTH_SHORT).show();
                        } else {
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                Log.d("status", status);
                                if ("ok".equals(status)) {

                                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(activityCriarPost.this);
                                    fazerLogin.setTitle(R.string.titulo_dialog_posts);
                                    fazerLogin.setMessage(R.string.corpo_dialog_posts);
                                    fazerLogin.setCancelable(true);
                                    fazerLogin.create().show();
                                    // O e-mail existe no banco de dados

                                } else if ("false".equals(status)) {
                                    Toast.makeText(activityCriarPost.this, R.string.erro_pedido, Toast.LENGTH_SHORT).show();

                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(activityCriarPost.this, R.string.erro_desconhecido, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(activityCriarPost.this, R.string.erro_desconhecido, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

}