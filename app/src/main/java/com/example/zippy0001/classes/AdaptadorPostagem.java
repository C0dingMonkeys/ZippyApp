package com.example.zippy0001.classes;

import android.content.Context;
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

public class AdaptadorPostagem extends RecyclerView.Adapter<AdaptadorPostagem.HolderPostagem> {
    private final Context context;
    private List<PostagemGetterSetter> postagemGetterSetterList;

    public AdaptadorPostagem(Context context, List<PostagemGetterSetter> postagemGetterSetterList) {
        this.context = context;
        this.postagemGetterSetterList = postagemGetterSetterList;

    }

    @NonNull
    @Override
    public HolderPostagem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_pedidos_disponiveis, parent, false);
        return new HolderPostagem(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HolderPostagem holder, int position) {

        holder.NomeProduto.setText(postagemGetterSetterList.get(position).getNomeProduto());
        holder.LinkProduto.setText(postagemGetterSetterList.get(position).getLinkProduto());
        holder.PrecoProduto.setText(postagemGetterSetterList.get(position).getPrecoProduto());
        holder.PaisDestino.setText(postagemGetterSetterList.get(position).getPaisDestino());
        holder.CidadeDestino.setText(postagemGetterSetterList.get(position).getCidadeDestino());
        holder.CaixaProduto.setText(postagemGetterSetterList.get(position).getCaixaProduto());
        Picasso.get().load(postagemGetterSetterList.get(position).getImgProduto()).into(holder.ImgProduto);

    }

    @Override
    public int getItemCount() { return postagemGetterSetterList.size(); }

    class HolderPostagem extends RecyclerView.ViewHolder {
        TextView NomeProduto, LinkProduto, PrecoProduto, PaisDestino, CidadeDestino, CaixaProduto;
        ImageView ImgProduto;

        public HolderPostagem(@NonNull View itemView) {
            super(itemView);

            NomeProduto = itemView.findViewById(R.id.txtTituloPedido);
            LinkProduto = itemView.findViewById(R.id.txtLinkPedido);
            PrecoProduto = itemView.findViewById(R.id.txtPrecoPedido);
            PaisDestino = itemView.findViewById(R.id.txtPaisDestino);
            CidadeDestino = itemView.findViewById(R.id.txtCidadeDestino);
            CaixaProduto = itemView.findViewById(R.id.txtCaixaTipo);
            ImgProduto = itemView.findViewById(R.id.imgPedido_rv);


        }
    }
}
