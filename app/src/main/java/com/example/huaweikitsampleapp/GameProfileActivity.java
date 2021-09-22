package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class GameProfileActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageView;
    DatabaseReference myRef;
    TextView textView, gameLink;
    SliderView sliderView;
    ArrayList<String> images = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile);

        String id = getIntent().getStringExtra("id");

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        imageView = findViewById(R.id.imageViewCollapsing);
        textView = findViewById(R.id.gameDetails2);
        gameLink = findViewById(R.id.gameLink);
        sliderView = findViewById(R.id.image_slider);

        myRef = FirebaseDatabase.getInstance().getReference().child("game").child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    images.clear();
                    for (DataSnapshot ss : snapshot.child("image").getChildren()) {
                        images.add(ss.getValue().toString());

                    }

                    collapsingToolbarLayout.setTitle(name);
                    GameProfileSliderAdapter sliderAdapter = new GameProfileSliderAdapter(images);

                    sliderView.setSliderAdapter(sliderAdapter);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();

                    myRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        if (id.equals("csgo")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.csgo);
            textView.setText(R.string.csgo_about);
        } else if (id.equals("valorant")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.valorant);
            textView.setText(R.string.valorant_about);
        } else if (id.equals("dbd")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.dbd);
            textView.setText(R.string.dbd_about);
        } else if (id.equals("l4d")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.l4d);
            textView.setText(R.string.l4d_about);
        } else if (id.equals("dragonest")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.dragonest);
            textView.setText(R.string.dragonest_about);
        } else if (id.equals("genshin")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.genshin);
            textView.setText(R.string.genshin_about);
        } else if (id.equals("gta5")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.gta5);
            textView.setText(R.string.gta5_about);
        } else if (id.equals("maple")) {
            collapsingToolbarLayout.setBackgroundResource(R.drawable.maple);
            textView.setText(R.string.maple_about);
        }

        gameLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals("csgo")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://store.steampowered.com/app/730/CounterStrike_Global_Offensive/"));
                    startActivity(myIntent);
                } else if (id.equals("valorant")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://playvalorant.com/"));
                    startActivity(myIntent);
                } else if (id.equals("dbd")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://deadbydaylight.com/en"));
                    startActivity(myIntent);
                } else if (id.equals("l4d")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://store.steampowered.com/app/500/Left_4_Dead/"));
                    startActivity(myIntent);
                } else if (id.equals("dragonest")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sea.dragonnest.com/main"));
                    startActivity(myIntent);
                } else if (id.equals("genshin")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://genshin.mihoyo.com/"));
                    startActivity(myIntent);
                } else if (id.equals("gta5")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.rockstargames.com/V/"));
                    startActivity(myIntent);
                } else if (id.equals("maple")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maplestory.nexon.net/"));
                    startActivity(myIntent);
                }
            }
        });

    }
}
