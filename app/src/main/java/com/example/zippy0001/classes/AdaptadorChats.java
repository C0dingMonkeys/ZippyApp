package com.example.zippy0001.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.R;
import com.example.zippy0001.activityChatEspecifico;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorChats extends RecyclerView.Adapter<AdaptadorChats.ViewHolderChats> {

    Context context;
    String usuario;
    ArrayList<Usuario> listaUsuarios;
    String idChat;


    public AdaptadorChats(Context contexto, String usuario, ArrayList<Usuario> listaUsuarios, String idChat) {
        this.context = contexto;
        this.usuario = usuario;
        this.listaUsuarios = listaUsuarios;
        this.idChat = idChat;
    }

    @NonNull
    @Override
    public AdaptadorChats.ViewHolderChats onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_chats, null, false);
        return new ViewHolderChats(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorChats.ViewHolderChats holderChats, @SuppressLint("RecyclerView") final int i) {

        holderChats.tvChatUsuario.setText(listaUsuarios.get(i).getNome());
        Log.d("testeUserAdapter", listaUsuarios.get(i).getUsuario());
        holderChats.tvChatUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Toast.makeText(v.getContext(), usuario.getNombre()+" , "+listaUsuarios.get(i).getNombre(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), activityChatEspecifico.class);
                    intent.putExtra("usuario", usuario);
                    intent.putExtra("usuarioDestino", listaUsuarios.get(i));

                    intent.putExtra("idChat", idChat);
                    context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
       return listaUsuarios.size();
    }

    public class ViewHolderChats extends RecyclerView.ViewHolder {

        TextView tvChatUsuario;

        public ViewHolderChats(@NonNull View itemView) {
            super(itemView);

            tvChatUsuario = itemView.findViewById(R.id.tvChatUsuario);
        }
    }
}
