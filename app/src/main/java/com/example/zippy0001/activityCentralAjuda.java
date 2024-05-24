package com.example.zippy0001;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.cachapa.expandablelayout.ExpandableLayout;

public class activityCentralAjuda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_ajuda);


        ExpandableLayout FAQ1, FAQ2, FAQ3, FAQ4, FAQ5;
        RelativeLayout btnAbrir, btnFAQ2, btnFAQ3, btnFAQ4, btnFAQ5;
        ImageButton btnVoltar;

        FAQ1 = findViewById(R.id.card_expandido_FAQ1);
        FAQ2 = findViewById(R.id.card_expandido_FAQ2);
        FAQ3 = findViewById(R.id.card_expandido_FAQ3);
        FAQ4 = findViewById(R.id.card_expandido_FAQ4);
        FAQ5 = findViewById(R.id.card_expandido_FAQ5);

        btnAbrir = findViewById(R.id.btnCardFAQ1);
        btnFAQ2 = findViewById(R.id.btnCardFAQ2);
        btnFAQ3 = findViewById(R.id.btnCardFAQ3);
        btnFAQ4 = findViewById(R.id.btnCardFAQ4);
        btnFAQ5 = findViewById(R.id.btnCardFAQ5);
        btnVoltar = findViewById(R.id.btnVoltarCA);

        final Animation rotateUp = AnimationUtils.loadAnimation(this, R.anim.rotate_up);
        final Animation rotateDown = AnimationUtils.loadAnimation(this, R.anim.rotate_down);

        ImageView setaFAQ1 = (ImageView) findViewById(R.id.ic_seta_FAQ1);
        ImageView setaFAQ2 = (ImageView) findViewById(R.id.ic_seta_FAQ2);
        ImageView setaFAQ3 = (ImageView) findViewById(R.id.ic_seta_FAQ3);
        ImageView setaFAQ4 = (ImageView) findViewById(R.id.ic_seta_FAQ4);
        ImageView setaFAQ5 = (ImageView) findViewById(R.id.ic_seta_FAQ5);

btnVoltar.setOnClickListener(v -> {
    finish();
});
        btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FAQ1.isExpanded()) {
                    FAQ1.collapse();
                    setaFAQ1.startAnimation(rotateUp);

                } else {
                    FAQ1.expand();
                    setaFAQ1.startAnimation(rotateDown);
                }
            }
        });

        btnFAQ2.setOnClickListener(v -> {
            if (FAQ2.isExpanded()) {
                FAQ2.collapse();
                setaFAQ2.startAnimation(rotateUp);

            } else {
                FAQ2.expand();
                setaFAQ2.startAnimation(rotateDown);
            }
        });

        btnFAQ3.setOnClickListener(v -> {
            if (FAQ3.isExpanded()) {
                FAQ3.collapse();
                setaFAQ3.startAnimation(rotateUp);

            } else {
                FAQ3.expand();
                setaFAQ3.startAnimation(rotateDown);
            }
        });

        btnFAQ4.setOnClickListener(v -> {
            if (FAQ4.isExpanded()) {
                FAQ4.collapse();
                setaFAQ4.startAnimation(rotateUp);

            } else {
                FAQ4.expand();
                setaFAQ4.startAnimation(rotateDown);
            }
        });
        btnFAQ5.setOnClickListener(v -> {
            if (FAQ5.isExpanded()) {
                FAQ5.collapse();
                setaFAQ5.startAnimation(rotateUp);

            } else {
                FAQ5.expand();
                setaFAQ5.startAnimation(rotateDown);
            }
        });

    }

}