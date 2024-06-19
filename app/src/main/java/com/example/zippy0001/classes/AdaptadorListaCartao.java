package com.example.zippy0001.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zippy0001.R;

import java.util.ArrayList;

public class AdaptadorListaCartao extends ArrayAdapter<ArrayListaCartoes> {
    private Context context;
    private int imgBandeira;

    public AdaptadorListaCartao(@NonNull Context context, int resource, @NonNull ArrayList<ArrayListaCartoes> objects) {
        super(context, resource, objects);
        this.context = context;
        this.imgBandeira = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        convertView = layoutInflater.inflate(imgBandeira, parent, false);
        ImageView imageView = convertView.findViewById(R.id.bandeiraImagem);
        TextView textView = convertView.findViewById(R.id.numCartaoLista);
        imageView.setImageResource(getItem(position).getImagem());
        textView.setText(getItem(position).getNumCartao());
        return  convertView;
    }
}
