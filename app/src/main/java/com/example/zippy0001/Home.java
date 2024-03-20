package com.example.zippy0001;

import static com.example.zippy0001.R.layout.activity_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.zippy0001.classes.CarouselAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home);

        RecyclerView recyclerView = findViewById(R.id.CarouselRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.carousel_1);
        images.add(R.drawable.tony);
        images.add(R.drawable.justin);

        CarouselAdapter adapter = new CarouselAdapter(this, images);
        recyclerView.setAdapter(adapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_chat) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }
}