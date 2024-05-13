package com.example.zippy0001;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.zippy0001.classes.AddPostRes;
import com.example.zippy0001.classes.HttpService;
import com.example.zippy0001.classes.PedidoGetterSetter;
import com.example.zippy0001.classes.RetrofitBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

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
    ImageView fotoProduto;
    CheckBox CheckCaixaProduto;
    private String ImagemPadrao = "https://zippyinternacional.com/uploads/produtos/produtoDefault.png";

    private String ImagemSelecionada;

    private CharSequence[] options = {"Camera", "Galeria", "Cancelar"};
    Button testeFuncao;
    public static final String SHARED_PREFS = "sharedPrefs";

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

        fotoProduto = findViewById(R.id.fotoProdutoPost);

        Picasso.get().load(ImagemPadrao).into(fotoProduto);

        CheckCaixaProduto = findViewById(R.id.checkboxCaixaPost);
        testeFuncao = findViewById(R.id.testefuncao);



        testeFuncao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarPostagem();
            }
        });

        fotoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activityCriarPost.this);
                builder.setTitle("Selecione uma imagem");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Camera")) {
                            Intent tirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(tirarFoto, 0);
                        } else if (options[which].equals("Galeria")) {
                            Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galeria, 1);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        ImagemSelecionada = FileUtils.getPath(activityCriarPost.this, getImageUri(activityCriarPost.this, image));
                        fotoProduto.setImageBitmap(image);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri image = data.getData();
                        ImagemSelecionada = FileUtils.getPath(activityCriarPost.this, image);
                        Picasso.get().load(image).into(fotoProduto);
                    }
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "myImage", "");

        return Uri.parse(path);
    }

    public void requirePermission() {
        ActivityCompat.requestPermissions(activityCriarPost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void criarPostagem() {

        if (ImagemSelecionada != null) {


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

            File imagem = new File(Uri.parse(ImagemSelecionada).getPath());

            Log.d("erroNome", Uri.parse(ImagemSelecionada).getPath());


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

            Call<FileModel> call = service.callUploadApiPost(filePart, post_idCliente ,post_nome,post_preco, post_link, post_paisOrigem, post_cidadeOrigem, post_ufOrigem, post_paisDestino, post_cidadeDestino, post_ufDestino, post_caixa);
            call.enqueue(new Callback<FileModel>() {
                @Override
                public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                    FileModel fileModel = response.body();
                    Toast.makeText(activityCriarPost.this, fileModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("erro", fileModel.getMessage());

                }

                @Override
                public void onFailure(Call<FileModel> call, Throwable t) {
                    Toast.makeText(activityCriarPost.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("erro", t.getMessage());


                }
            });

        } else {

            Toast.makeText(activityCriarPost.this, "caralho", Toast.LENGTH_SHORT).show();
            cadastroDadosPostagemPadrao();

        }


    }

    private void cadastroDadosPostagem(String idUsuarioShared, String nomeProd, String precoProd, String linkProd, String paisOrigemProd, String cidadeOrigemProd, String estadoOrigemProd, String paisDestinoProd, String cidadeDestinoProd, String estadoDestinoProd, String caixa ){


        String Host = "criarPostagem.php";
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
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(activityCriarPost.this, "Erro ao verificar senha. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                            Log.d("erro", e.toString());
                        } else {
                            Log.d("erro", result.toString());
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                Log.d("status", status);
                                if ("true".equals(status)) {
//                                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(activityCriarPost.this);
//                                    fazerLogin.setTitle("Sucesso!");
//                                    fazerLogin.setMessage("Post Realizado");
//                                    fazerLogin.create().show();
                                    Toast.makeText(activityCriarPost.this, "Deu Certo.", Toast.LENGTH_SHORT).show();

                                } else if ("false".equals(status)) {


                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(activityCriarPost.this, "Erro desconhecido ao verificar e-mail.1", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(activityCriarPost.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });
    }

    private void cadastroDadosPostagemPadrao(){
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
                            // Ocorreu um erro de conexão ou outra exceção
                            Toast.makeText(activityCriarPost.this, "Erro ao verificar senha. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                            Log.d("erro", e.toString());
                        } else {
                            Log.d("erro", result.toString());
                            // Verifique se o resultado é válido
                            if (result != null && result.has("status")) {
                                String status = result.get("status").getAsString();
                                Log.d("status", status);
                                if ("true".equals(status)) {
//                                    AlertDialog.Builder fazerLogin = new AlertDialog.Builder(activityCriarPost.this);
//                                    fazerLogin.setTitle("Sucesso!");
//                                    fazerLogin.setMessage("Post Realizado");
//                                    fazerLogin.create().show();
                                    Toast.makeText(activityCriarPost.this, "Deu Certo.", Toast.LENGTH_SHORT).show();

                                } else if ("false".equals(status)) {


                                } else {
                                    // Resposta inválida do servidor
                                    Toast.makeText(activityCriarPost.this, "Erro desconhecido ao verificar e-mail.1", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Resposta inválida do servidor
                                Toast.makeText(activityCriarPost.this, "Erro desconhecido ao verificar e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });

    }

}