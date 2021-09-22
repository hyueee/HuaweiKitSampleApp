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


public class GameLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        loadFragment(new FirstFragment());

        bottomNavigationView.setSelectedItemId(R.id.FirstFragment);

        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment temp = null;

                switch (item.getItemId()) {

                    case R.id.FirstFragment:
                        temp = new FirstFragment();
                        break;

                    case R.id.SecondFragment :
                        temp = new SecondFragment();
                        break;

                    case R.id.ThirdFragment :
                        temp = new ThirdFragment();
                        break;

                }
                loadFragment(temp);
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
