package com.example.zippy0001;

import static com.example.zippy0001.R.layout.activity_home;
import static com.example.zippy0001.activityEditarPerfil.EXTRA_TRIGGER_PERFIL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.zippy0001.classes.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class activityInicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    String BASE_URL_IMAGEM = "https://zippyinternacional.com/Android/img/";
    String URL_RECUPERAR_DADOS_USUARIO = "https://zippyinternacional.com/Android/selectUsuario.php";
    String URL_RECUPERAR_DADOS_CLIENTE = "https://zippyinternacional.com/Android/selectCliente.php";
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String idUsuarioShared = sharedPreferences.getString("id_usuario", "");
        String emailShared = sharedPreferences.getString("email", "");
        String nomeUsuario = sharedPreferences.getString("nomeCliente", "");
        recuperarDadosTBUSUARIO(emailShared);
        recuperarDadosTBCLIENTE(idUsuarioShared);
        Log.d("idTeste", idUsuarioShared);
        Usuario usuario = new Usuario(idUsuarioShared, nomeUsuario);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra(EXTRA_TRIGGER_PERFIL)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PerfilFragment()).commit();

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home);


        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_header);
        navigationView.setNavigationItemSelectedListener(this);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new fragmentHome()).commit();
        }

        navigationView.bringToFront();


        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new fragmentHome()).commit();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PerfilFragment()).commit();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                return true;
            } else if (itemId == R.id.bottom_chat) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view_header);
        View header = navigationView.getHeaderView(0);
        Button verPerfil = (Button) header.findViewById(R.id.btnVerPerfil);
        verPerfil.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PerfilFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);


        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (isCurrentFragmentMain()) {
                        super.getClass(); // Chamada padrão do botão Voltar
                    } else {
                        // Navega para o fragmento principal
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new fragmentHome()).commit();
                    }
                    finish();
                }

            }
        });

    }

    private boolean isCurrentFragmentMain() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        return fragment instanceof fragmentHome;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        if (menuItemId == R.id.drawer_soliticao) {
            startActivity(new Intent(getApplicationContext(), activitySelecionarDestino.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else if (menuItemId == R.id.drawer_config) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new fragmentConfig()).commit();
        } else if (menuItemId == R.id.drawer_post) {
            startActivity(new Intent(getApplicationContext(), activityCriarPost.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
        } else if (menuItemId == R.id.drawer_sair) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id_usuario", "");
            editor.putString("nome", "");
            editor.putString("email", "");
            editor.putString("nomeCliente", "");
            editor.putString("foneCliente", "");
            editor.putString("identidadeCliente", "");
            editor.putString("statusUsuario", "");
            editor.putString("dataNascUsuario", "");
            editor.putString("fotoPerfilUsuario", "");
            editor.apply();

            startActivity(new Intent(getApplicationContext(), activityEmail.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void recuperarDadosTBCLIENTE(String idUsuario) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_header);
        View header = navigationView.getHeaderView(0);
        TextView username = (TextView) header.findViewById(R.id.txtHeaderDrawer);
        Ion.with(this)
                .load(URL_RECUPERAR_DADOS_CLIENTE)
                .setBodyParameter("idUsuario", idUsuario)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (e != null) {
                                Log.e("acitivityInicio", "Erro na requisição: " + e.getMessage());
                            } else {

                                for (int i = 0; i < result.size(); i++) {
                                    Log.d("activityInicio", "JSON Response: " + result.toString());

                                    JsonArray dadosArray = result.get(i).getAsJsonArray();
                                    String Nome = dadosArray.get(2).getAsString();
                                    String Sobrenome = dadosArray.get(3).getAsString();
                                    String Fone = dadosArray.get(4).getAsString();
                                    String Identidade = dadosArray.get(5).getAsString();

                                    Log.d("teste2", "o nome é:" + Nome);

                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("nomeCliente", Nome);
                                    editor.putString("sobrenomeCliente", Sobrenome);
                                    editor.putString("foneCliente", Fone);
                                    editor.putString("identidadeCliente", Identidade);
                                    editor.apply();

                                    username.setText(Nome);

                                }
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void recuperarDadosTBUSUARIO(String emailShared) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_header);
        View header = navigationView.getHeaderView(0);
        CircleImageView Foto = header.findViewById(R.id.FotoPerfilDrawer);
        Ion.with(this)
                .load(URL_RECUPERAR_DADOS_USUARIO)
                .setBodyParameter("email", emailShared)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (e != null) {
                                Log.e("acitivityInicio", "Erro na requisição: " + e.getMessage());
                            } else {

                                for (int i = 0; i < result.size(); i++) {
                                    Log.d("activityInicio2", "JSON Response: " + result.toString());

                                    JsonArray dadosArray = result.get(i).getAsJsonArray();
                                    String Id_usuario = dadosArray.get(0).getAsString();
                                    String email = dadosArray.get(1).getAsString();
                                    String status = dadosArray.get(2).getAsString();
                                    String dataNasc = dadosArray.get(3).getAsString();
                                    String fotoPerfil = dadosArray.get(4).getAsString();

                                    Log.d("teste3", "o status é:" + Id_usuario);

                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", email);
                                    editor.putString("id_usuario", Id_usuario);
                                    editor.putString("statusUsuario", status);
                                    editor.putString("dataNascUsuario", dataNasc);
                                    editor.putString("fotoPerfilUsuario", fotoPerfil);
                                    editor.apply();
                                    Picasso.get().load(BASE_URL_IMAGEM + fotoPerfil).into(Foto);
                                    Log.d("foto", BASE_URL_IMAGEM + fotoPerfil);

                                }
                            }

                        } catch (JsonIOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });


    }



}