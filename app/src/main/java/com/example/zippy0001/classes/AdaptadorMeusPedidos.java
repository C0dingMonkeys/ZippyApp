package com.example.zippy0001.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zippy0001.MeusPedidosInterface;
import com.example.zippy0001.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorMeusPedidos extends RecyclerView.Adapter<AdaptadorMeusPedidos.HolderPostagem> {
    private final Context context;
    private final MeusPedidosInterface meusPedidosinterface;
    private List<MeusPedidosGetterSetter> meusPedidosList;

    public AdaptadorMeusPedidos(Context context, List<MeusPedidosGetterSetter> meusPedidosList,
                                MeusPedidosInterface meusPedidosinterface) {
        this.context = context;
        this.meusPedidosList = meusPedidosList;
        this.meusPedidosinterface = meusPedidosinterface;

    }

    @NonNull
    @Override
    public HolderPostagem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_meus_pedidos, parent, false);
        return new HolderPostagem(view, meusPedidosinterface);
    }



    @Override
    public void onBindViewHolder(@NonNull HolderPostagem holder, int position) {

        holder.statusPedido.setText(meusPedidosList.get(position).getStatus());
        holder.numPedido.setText(meusPedidosList.get(position).getPedido());
        holder.nomePedido.setText(meusPedidosList.get(position).getNome());
        holder.destinoPedido.setText(meusPedidosList.get(position).getDestino());
        holder.dataPedido.setText(meusPedidosList.get(position).getData());
        Picasso.get().load(meusPedidosList.get(position).getImgPedido()).into(holder.imgPedido);
        if (meusPedidosList.get(position).getStatus().equals("Pendente")) {
            holder.confirmarPedido.setText("Excluir Pedido");
        } else if (meusPedidosList.get(position).getStatus().equals("Pago")) {
            holder.confirmarPedido.setText("Confirmar entrega");
        } else {
            holder.confirmarPedido.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() { return meusPedidosList.size(); }

    static class HolderPostagem extends RecyclerView.ViewHolder {
        TextView statusPedido, numPedido, nomePedido, destinoPedido, dataPedido;
        ImageView imgPedido;
        Button confirmarPedido;

        public HolderPostagem(@NonNull View itemView, MeusPedidosInterface meusPedidosinterface) {
            super(itemView);

            statusPedido = itemView.findViewById(R.id.statusPedido);
            numPedido = itemView.findViewById(R.id.numeroMeusPedidos);
            nomePedido = itemView.findViewById(R.id.nomeMeusPedidos);
            destinoPedido = itemView.findViewById(R.id.destinoMeusPedidos);
            dataPedido = itemView.findViewById(R.id.dataMeusPedidos);
            imgPedido = itemView.findViewById(R.id.fotoMeusPedidos);
            confirmarPedido = itemView.findViewById(R.id.btnConfirmarPedido);

            confirmarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (meusPedidosinterface != null){
                        int pos = getAdapterPosition();

                        if( pos != RecyclerView.NO_POSITION){
                            meusPedidosinterface.onItemClick(pos);
                        }
                    }
                }
            });

        }

    }
    public void updateOrders(List<MeusPedidosGetterSetter> newList) {
        meusPedidosList = newList;
        notifyDataSetChanged();
    }

}
