package com.example.tabipapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tabipapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FlightDetailsActivity extends AppCompatActivity {

    Button rothman_index_button,electro_button,blood_pressure_button,blood_glucose_button,temperature_button,blood_oxygen_button;
    EditText yas, kilo,dogum_tarihi,numara,ucus_no;
    String gelen_yas, gelen_kilo,gelen_dogum_tarihi,gelen_numara,gelen_ucus_no;
    BottomNavigationView bottomNavigationView;
    Button video_call_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_details_activity);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        yas = (EditText) findViewById(R.id.yas);
        kilo = (EditText) findViewById(R.id.kilo);
        dogum_tarihi = (EditText) findViewById(R.id.dogum_tarihi);
        numara = (EditText) findViewById(R.id.numara);
        ucus_no = (EditText) findViewById(R.id.ucus_no);

        rothman_index_button = (Button) findViewById(R.id.rothman_index_button);
        electro_button = (Button) findViewById(R.id.electro_button);
        blood_pressure_button = (Button) findViewById(R.id.blood_pressure_button);
        blood_glucose_button = (Button) findViewById(R.id.blood_glucose_button);
        temperature_button = (Button) findViewById(R.id.temperature_button);
        blood_oxygen_button = (Button) findViewById(R.id.blood_oxygen_button);


        gelen_yas = getIntent().getStringExtra("yas");
        gelen_kilo = getIntent().getStringExtra("kilo");
        gelen_dogum_tarihi = getIntent().getStringExtra("dogum_tarihi");
        gelen_numara = getIntent().getStringExtra("diger");
        gelen_ucus_no = getIntent().getStringExtra("ucus_no");



        yas.setText(gelen_yas);
        kilo.setText(gelen_kilo);
        dogum_tarihi.setText(gelen_dogum_tarihi);
        numara.setText(gelen_numara);
        ucus_no.setText(gelen_ucus_no);

        rothman_index_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FlightDetailsActivity.this, RothmanIndexActivity.class);
                startActivity(i);
            }
        });


        video_call_button = (Button) findViewById(R.id.video_call_start);
        video_call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FlightDetailsActivity.this, VideoCallActivity.class);
                startActivity(i);
            }
        });


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
                            i = new Intent(FlightDetailsActivity.this, MainActivity.class);
                            i.putExtra("gelenPage", "müşteri");
                            startActivity(i);
                            break;
                        case R.id.nav_ucus_detay:
                            i = new Intent(FlightDetailsActivity.this, FlightDetailsActivity.class);
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
