package com.example.zippy0001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;

public class activityCentralAjuda extends AppCompatActivity {

    ConstraintLayout cardExpandido;
    Button btnSeta;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_ajuda);

        cardExpandido = findViewById(R.id.expandableView);
        btnSeta = findViewById(R.id.arrowBtn);
        cardView = findViewById(R.id.cardView);

        btnSeta.setOnClickListener(v -> {
            if (cardExpandido.getVisibility()== View.GONE){
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                cardExpandido.setVisibility(View.VISIBLE);
                btnSeta.setBackgroundResource(R.drawable.ic_seta_cima);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                cardExpandido.setVisibility(View.GONE);
                btnSeta.setBackgroundResource(R.drawable.ic_seta_baixo);
            }
        });
    }
}