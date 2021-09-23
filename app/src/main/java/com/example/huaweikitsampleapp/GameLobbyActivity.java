package com.example.huaweikitsampleapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;


public class GameLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        String id = getIntent().getStringExtra("id");
        String userId = getIntent().getStringExtra("userId");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        loadFragment(new FirstFragment(id, userId));

        bottomNavigationView.setSelectedItemId(R.id.FirstFragment);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;

                switch (item.getItemId()) {

                    case R.id.FirstFragment:
                        temp = new FirstFragment(id, userId);
                        break;

                    case R.id.SecondFragment :
                        temp = new SecondFragment(id, userId);
                        break;

                    case R.id.ThirdFragment :
                        temp = new ThirdFragment(id, userId);
                        break;

                    case R.id.FourthFragment :
                        temp = new FourthFragment(id, userId);
                        break;

                }
                loadFragment(temp);

                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_navigation, menu);
        return true;
    }

}
