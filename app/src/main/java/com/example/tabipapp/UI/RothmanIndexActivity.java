package com.example.tabipapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tabipapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RothmanIndexActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_details_activity);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);




    }

    public BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    Intent i = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            i = new Intent(RothmanIndexActivity.this, MainActivity.class);
                            i.putExtra("gelenPage", "müşteri");
                            startActivity(i);
                            break;
                        case R.id.nav_ucus_detay:
                            i = new Intent(RothmanIndexActivity.this, FlightDetailsActivity.class);
                            i.putExtra("yas" , "27" );
                            i.putExtra("kilo" ,"78" );
                            i.putExtra("dogum_tarihi" ,"04.01.1996" );
                            i.putExtra("ucus_no" ,"23527869825486" );
                            i.putExtra("diger" ,"7" );
                            startActivity(i);
                            break;

                    }
                    return true;
                }
            };


}
