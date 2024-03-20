package com.example.zippy0001.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zippy0001.R;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private final List<Integer> images;
    private final Context context;

    public CarouselAdapter(Context context, List<Integer> images) {
        this.context = context;
        this.images = images;
    }
    @NonNull
    @Override
    public CarouselAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carousel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageResource(images.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lógica para o botão
            }
        });
    }

    @Override
    public int getItemCount() {return images.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            button = itemView.findViewById(R.id.button_carousel);
        }
    }
}
