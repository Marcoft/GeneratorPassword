package com.pass.GeneratorPassword;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends FragmentActivity {

    Fragment selected_fragm;

    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        selected_fragm = new FragmentHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragm).commit();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            selected_fragm = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selected_fragm = new FragmentHome();
                    break;
                case R.id.nav_like:
                    selected_fragm = new FragmentSave();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragm).commit();
            return true;
        }
    };


    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this, getResources().getString(R.string.clickExit), Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }

}

