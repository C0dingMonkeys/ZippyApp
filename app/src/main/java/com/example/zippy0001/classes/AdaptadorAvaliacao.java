package com.example.zippy0001.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorAvaliacao extends RecyclerView.Adapter<AdaptadorAvaliacao.MyHolder> {
    private final Context context;
    private List<AvaliacoesGetterSetter> avaliacoesGetterSettersList;

    public AdaptadorAvaliacao(Context context, List<AvaliacoesGetterSetter> avaliacoesGetterSettersList) {
        this.context = context;
        this.avaliacoesGetterSettersList = avaliacoesGetterSettersList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_avaliacao_rv, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.Nome01.setText(avaliacoesGetterSettersList.get(position).getNome01());
        holder.Nome02.setText(avaliacoesGetterSettersList.get(position).getNome02());
        holder.Pais01.setText(avaliacoesGetterSettersList.get(position).getPais01());
        holder.Pais02.setText(avaliacoesGetterSettersList.get(position).getPais02());
        Picasso.get().load(avaliacoesGetterSettersList.get(position).getPerfil01()).into(holder.Perfil01);
        Picasso.get().load(avaliacoesGetterSettersList.get(position).getPerfil02()).into(holder.Perfil02);


    }

    @Override
    public int getItemCount() {
        return avaliacoesGetterSettersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView Nome01, Nome02, Pais01, Pais02;
        CircleImageView Perfil01, Perfil02;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Nome01 = itemView.findViewById(R.id.Nome01);
            Nome02 = itemView.findViewById(R.id.Nome02);
            Pais01 = itemView.findViewById(R.id.Pais01);
            Pais02 = itemView.findViewById(R.id.Pais02);
            Perfil01 = itemView.findViewById(R.id.imgPerfilAvalicao1);
            Perfil02 = itemView.findViewById(R.id.imgPerfilAvalicao2);


        }
    }
}

