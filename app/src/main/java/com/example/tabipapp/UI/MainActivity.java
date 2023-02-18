package com.example.tabipapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.tabipapp.Adapter.DurumAdapter;
import com.example.tabipapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView active_list, waiting_list, closing_list, redirected_list;
    ArrayList<Integer> source1, source2, source3, source4;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    DurumAdapter adapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;
    AutoCompleteTextView ucus_autocomplete;
    ImageView vector_1;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();


        active_list = (RecyclerView) findViewById(R.id.active_list);
        waiting_list = (RecyclerView) findViewById(R.id.waiting_list);
        closing_list = (RecyclerView) findViewById(R.id.closing_list);
        redirected_list = (RecyclerView) findViewById(R.id.redirects_list);

        ucus_autocomplete = (AutoCompleteTextView) findViewById(R.id.ucus_autocomplete);
        vector_1 = (ImageView) findViewById(R.id.vector_1);

        vector_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        active_list.setLayoutManager(RecyclerViewLayoutManager);

        Activelist();
        ClosingList();
        WaitingList();
        RedirectedList();

        adapter = new DurumAdapter(source1, "1");
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        active_list.setLayoutManager(HorizontalLayout);
        active_list.setAdapter(adapter);

        adapter = new DurumAdapter(source2, "2");
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        waiting_list.setLayoutManager(HorizontalLayout);
        waiting_list.setAdapter(adapter);


        adapter = new DurumAdapter(source3, "3");
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        closing_list.setLayoutManager(HorizontalLayout);
        closing_list.setAdapter(adapter);


        adapter = new DurumAdapter(source4, "4");
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        redirected_list.setLayoutManager(HorizontalLayout);
        redirected_list.setAdapter(adapter);

        autocomplete();

    }

    public void Activelist() {
        // Adding items to ArrayList
        source1 = new ArrayList<>();
        source1.add(R.drawable.ellipse1);
        source1.add(R.drawable.ellipse2);
        source1.add(R.drawable.ellipse3);
        source1.add(R.drawable.ellipse4);
        source1.add(R.drawable.profile);
        source1.add(R.drawable.profile2);
    }


    public void WaitingList() {
        // Adding items to ArrayList
        source2 = new ArrayList<>();
        source2.add(R.drawable.profile3);
        source2.add(R.drawable.profile4);


    }

    public void ClosingList() {
        // Adding items to ArrayList
        source3 = new ArrayList<>();
        source3.add(R.drawable.profile5);
        source3.add(R.drawable.profile6);
        source3.add(R.drawable.profile7);

    }

    public void RedirectedList() {
        // Adding items to ArrayList
        source4 = new ArrayList<>();
        source4.add(R.drawable.ic_rectangle9);
        source4.add(R.drawable.ic_rectangle9);
        source4.add(R.drawable.ic_rectangle9);
        source4.add(R.drawable.ic_rectangle9);
        source4.add(R.drawable.ic_rectangle9);
        source4.add(R.drawable.ic_rectangle9);



    }

    void autocomplete() {


        String[] list = {"23527869825486", "33527869825205" ,"43527869825266"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, list);

        ucus_autocomplete.setThreshold(1);
        ucus_autocomplete.setAdapter(adapter);
        ucus_autocomplete.setTextColor(Color.BLACK);
        ucus_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, FlightDetailsActivity.class);
                if(item.toString().equalsIgnoreCase("23527869825486")){
                    i.putExtra("yas" , "27" );
                    i.putExtra("kilo" ,"78" );
                    i.putExtra("dogum_tarihi" ,"04.01.1996" );
                    i.putExtra("ucus_no" ,item.toString() );
                    i.putExtra("diger" ,"7" );

                }
                else if(item.toString().equalsIgnoreCase("33527869825205")){
                    i.putExtra("yas" , "31" );
                    i.putExtra("kilo" ,"86" );
                    i.putExtra("dogum_tarihi" ,"04.01.1992" );
                    i.putExtra("ucus_no" ,item.toString() );
                    i.putExtra("diger" ,"7" );

                }
                else if(item.toString().equalsIgnoreCase("23527869825486")){
                    i.putExtra("yas" , "24" );
                    i.putExtra("kilo" ,"55" );
                    i.putExtra("dogum_tarihi" ,"04.01.1999" );
                    i.putExtra("ucus_no" ,item.toString() );
                    i.putExtra("diger" ,"7" );

                }
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
                            i = new Intent(MainActivity.this, MainActivity.class);
                            i.putExtra("gelenPage", "müşteri");
                            startActivity(i);
                            break;
                        case R.id.nav_ucus_detay:
                            i = new Intent(MainActivity.this, FlightDetailsActivity.class);
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
