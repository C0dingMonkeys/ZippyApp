package com.example.zippy0001.classes;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorMensagens extends RecyclerView.Adapter<AdaptadorMensagens.HolderMensagem> {

    Context context;
    String usuario;
    static ArrayList<Mensagens> listaMensagens;
    String nomeRemetente;
    String nomeDestinatario;

    public AdaptadorMensagens(Context contexto, String usuario, ArrayList<Mensagens> listaMensagens, String nomeRemetente, String nomeDestinatario) {
        this.context = contexto;
        this.usuario = usuario;
        this.listaMensagens = listaMensagens;
        this.nomeRemetente = nomeRemetente;
        this.nomeDestinatario = nomeDestinatario;
    }

    @NonNull
    @Override
    public AdaptadorMensagens.HolderMensagem onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_mensagens, null, false);
        return new AdaptadorMensagens.HolderMensagem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorMensagens.HolderMensagem holder, int i) {
        if(usuario.equals(listaMensagens.get(i).getUsuarioOrigem()))
        {
            holder.layoutMSG.setGravity(Gravity.END);
            holder.tvNomeUsuario.setGravity(Gravity.END);
            holder.tvMensagem.setGravity(Gravity.END);
            holder.tvNomeUsuario.setTextColor(Color.WHITE);
            holder.tvMensagem.setTextColor(Color.WHITE);
            holder.tvNomeUsuario.setText(nomeRemetente);
            holder.tvMensagem.setText(listaMensagens.get(i).getMensagem());
        }else {
            holder.backgroundMSG.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_contorno_pedido));
            holder.layoutMSG.setGravity(Gravity.START);
            holder.tvNomeUsuario.setGravity(Gravity.START);
            holder.tvMensagem.setGravity(Gravity.START);
            holder.tvNomeUsuario.setTextColor(Color.RED);
            holder.tvNomeUsuario.setText(nomeDestinatario);
            holder.tvMensagem.setText(listaMensagens.get(i).getMensagem());
        }
    }

    @Override
    public int getItemCount() {
        return listaMensagens.size();
    }

    class HolderMensagem extends RecyclerView.ViewHolder {
        TextView tvNomeUsuario, tvMensagem;
        LinearLayout layoutMSG;
        RelativeLayout backgroundMSG;
        public HolderMensagem(@NonNull View itemView) {
            super(itemView);
            tvNomeUsuario = itemView.findViewById(R.id.tvNomeUsuario);
            tvMensagem = itemView.findViewById(R.id.tvMensagem);
            layoutMSG = itemView.findViewById(R.id.layoutMsgs);
            backgroundMSG = itemView.findViewById(R.id.backgroundMSG);

        }

    }
}
