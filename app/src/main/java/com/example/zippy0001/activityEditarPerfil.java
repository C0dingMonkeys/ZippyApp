package com.example.zippy0001;

import static com.example.zippy0001.PerfilFragment.EXTRA_BACK_PERFIL;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.example.zippy0001.classes.FileUtils;
import com.example.zippy0001.classes.HttpService;
import com.example.zippy0001.classes.RetrofitBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class activityEditarPerfil extends AppCompatActivity {

    private RelativeLayout editarNome, editarTelefone, editarIdentidade;
    public static final String EXTRA_TRIGGER_PERFIL = "perfil_trigger";
    public static final String SHARED_PREFS = "sharedPrefs";
    String BASE_URL_IMAGEM = "https://zippyinternacional.com/Android/";

    private String selectedImage;
    private String URL_UPDATE_CLIENTE = "https://zippyinternacional.com/Android/updateCliente.php";
    private ImageButton voltar;
    private CharSequence[] options = {"Camera", "Galeria", "Cancelar"};
    private CircleImageView EditarFoto;
    private TextView nomeEdit, foneEdit, identidadeEdit, btnUploadFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        requirePermission();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuario = sharedPreferences.getString("id_usuario", "");
        Log.d("testeId", idUsuario);
        String nomeCliente = sharedPreferences.getString("nomeCliente", "");
        String sobrenomeCliente = sharedPreferences.getString("sobrenomeCliente", "");
        String foneCliente = sharedPreferences.getString("foneCliente", "");
        String identidadeCliente = sharedPreferences.getString("identidadeCliente", "");
        String fotoPerfil = sharedPreferences.getString("fotoPerfilUsuario", "");


        String foneOculto = substituirCarcteres(foneCliente);
        String identidadeOculta = substituirCarcteres(identidadeCliente);

        voltar = findViewById(R.id.btnVoltarEditarPerfil);
        btnUploadFoto = findViewById(R.id.uploadFoto);
        EditarFoto = findViewById(R.id.editar_fotoPerfil);

        editarNome = findViewById(R.id.layoutEditarNomeTela);
        editarTelefone = findViewById(R.id.layoutEditarFoneTela);
        editarIdentidade = findViewById(R.id.layoutEditarIdentidadeTela);

        nomeEdit = findViewById(R.id.txtNomeEdit);
        foneEdit = findViewById(R.id.txtFoneEdit);
        identidadeEdit = findViewById(R.id.txtIdentidadeEdit);

        nomeEdit.setText(String.format("%s %s", nomeCliente, sobrenomeCliente));
        foneEdit.setText(foneOculto);
        identidadeEdit.setText(identidadeOculta);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(EXTRA_BACK_PERFIL)) {
                    Intent intent = new Intent(activityEditarPerfil.this, activityInicio.class);
                    intent.putExtra(EXTRA_TRIGGER_PERFIL, "PERFIL");//Add your return data here
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });

        Picasso.get().load(BASE_URL_IMAGEM+fotoPerfil).into(EditarFoto);
        EditarFoto.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activityEditarPerfil.this);
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
        });
        btnUploadFoto.setOnClickListener(v ->{
            uploadFiletoServer(idUsuario);


        });

        editarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activityEditarPerfil.this);
                View view1 = LayoutInflater.from(activityEditarPerfil.this).inflate(R.layout.bottom_sheet_layout_nome, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

                TextView txtTituloBSL = view1.findViewById(R.id.lblTituloBottomSheetNome);
                TextInputLayout textInputLayoutNome = view1.findViewById(R.id.TextLayoutEdicaoNome);
                TextInputLayout textInputLayoutSobrenome = view1.findViewById(R.id.TextLayoutEdicaoSobrenome);
                TextInputEditText textInputEditTextNome = view1.findViewById(R.id.TextEditEdicaoNome);
                TextInputEditText textInputEditTextSobrenome = view1.findViewById(R.id.TextEditEdicaoSobrenome);

                AppCompatButton btnSalvarNome = view1.findViewById(R.id.btnSalvarEdicaoNome);

                txtTituloBSL.setText(R.string.lbl_edite_seu_nome);
                textInputEditTextNome.setText(nomeCliente);
                textInputEditTextSobrenome.setText(sobrenomeCliente);
                btnSalvarNome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String NomeEditado, SobrenomeEditado;
                        NomeEditado = textInputEditTextNome.getText().toString().trim();
                        SobrenomeEditado = textInputEditTextSobrenome.getText().toString().trim();

                        updatePerfil(idUsuario, NomeEditado, SobrenomeEditado, foneCliente);

                    }
                });


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
                TextInputEditText textInputEditTextFone = view1.findViewById(R.id.TextEditEdicao);
                AppCompatButton btnSalvarFone = view1.findViewById(R.id.btnSalvarEdicao);

                txtTituloBSL.setText(R.string.lbl_edite_seu_telefone);
                textInputEditTextFone.setText(foneOculto);

                btnSalvarFone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String foneEditado;
                        foneEditado = Objects.requireNonNull(textInputEditTextFone.getText()).toString().trim();
                        updatePerfil(idUsuario, nomeCliente, sobrenomeCliente, foneEditado);
                    }
                });
            }
        });
        editarIdentidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder identidadeErro = new AlertDialog.Builder(activityEditarPerfil.this);
                identidadeErro.setTitle("Aviso!");
                identidadeErro.setMessage("Não é possivel alterar sua identidade\nCaso haja um erro, entre em contato com nosso suporte");
                identidadeErro.setPositiveButton("Ok", null);
                identidadeErro.create().show();

            }
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(activityEditarPerfil.this, activityInicio.class);
                intent.putExtra(EXTRA_TRIGGER_PERFIL, "PERFIL");//Add your return data here
                startActivity(intent);
            }
        });

    }

    private void updatePerfil(String id_usuario, String Nome, String Sobrenome, String Telefone) {
        Ion.with(this)
                .load(URL_UPDATE_CLIENTE)
                .setBodyParameter("idUsuario", id_usuario) // Enviar o email como parâmetro
                .setBodyParameter("nome", Nome) // Enviar o email como parâmetro
                .setBodyParameter("sobrenome", Sobrenome) // Enviar a senha como parâmetro
                .setBodyParameter("fone", Telefone) // Enviar a senha como parâmetro
                .asJsonObject() // Obter a resposta como uma string
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {


                            if (e != null) {
                                // Ocorreu um erro de conexão ou outra exceção
                                Log.e("Perfil", "Erro na requisição0: " + e.getMessage());
                            } else {
                                // Verifique se o resultado é válido
                                if (result != null && result.has("status")) {
                                    String status = result.get("status").getAsString();
                                    if ("ok".equals(status)) {
                                        Toast.makeText(activityEditarPerfil.this, "Sucesso", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("nomeCliente", Nome);
                                        editor.putString("sobrenomeCliente", Sobrenome);
                                        editor.putString("foneCliente", Telefone);
                                        editor.apply();
                                        recreate();

                                    } else if ("erro".equals(status)) {

                                        Log.e("Perfil", "Erro na requisição: " + result);
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
                    }
                });

    }

    public static String substituirCarcteres(String str) {
        if (str.length() < 5) {
            return str; // String is too short, return it as is
        }

        String first5Chars = str.substring(0, 5); // Extract the first 5 characters
        String asterisks = "****"; // Create a string of 5 asterisks
        String remainingChars = str.substring(5); // Extract the remaining characters

        return asterisks + remainingChars; // Combine the asterisks and remaining characters
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        selectedImage = FileUtils.getPath(activityEditarPerfil.this, getImageUri(activityEditarPerfil.this, image));
                        EditarFoto.setImageBitmap(image);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri image = data.getData();
                        selectedImage = FileUtils.getPath(activityEditarPerfil.this, image);
                        Picasso.get().load(image).into(EditarFoto);
                    }
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "minhaIMagem", "");

        return Uri.parse(path);
    }

    public void requirePermission() {
        ActivityCompat.requestPermissions(activityEditarPerfil.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public void uploadFiletoServer(String userId) {
        File file = new File(Uri.parse(selectedImage).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage", file.getName(), requestBody);

        HttpService service = RetrofitBuilder.getClient().create(HttpService.class);

        retrofit2.Call<FileModel> call = service.callUploadApi(filePart,userId);
        call.enqueue(new Callback<FileModel>() {
            @Override
            public void onResponse(retrofit2.Call<FileModel> call, retrofit2.Response<FileModel> response) {
                FileModel fileModel = response.body();

                Toast.makeText(activityEditarPerfil.this, fileModel.getMessage(), Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onFailure(retrofit2.Call<FileModel> call, Throwable t) {
                Toast.makeText(activityEditarPerfil.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}