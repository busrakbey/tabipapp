package com.example.tabipapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabipapp.Adapter.TriyajAdapter;
import com.example.tabipapp.Model.TriyajBilgileri;
import com.example.tabipapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    RecyclerView active_list, closing_list;
    ArrayList<TriyajBilgileri> source1, source2, source3, source4;
    public static List<TriyajBilgileri> activeTriyajList;
    public static List<TriyajBilgileri> closingTriyajList;
    public static List<TriyajBilgileri> allPassengerList;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    TriyajAdapter triyajAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;
    AutoCompleteTextView ucus_autocomplete;
    ImageView vector_1;
    BottomNavigationView bottomNavigationView;
    Button kullanici_bilgileri_button, yeni_triyaj_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        kullanici_bilgileri_button = (Button) findViewById(R.id.kullanici_bilgileri_button);
        yeni_triyaj_button = (Button) findViewById(R.id.yeni_triyaj);
        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();


        addTriaj();
        yeni_triyaj_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, TriyajActivity.class);
                startActivity(i);
            }
        });




        active_list = (RecyclerView) findViewById(R.id.active_list);
        closing_list = (RecyclerView) findViewById(R.id.closing_list);

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

        triyajAdapter = new TriyajAdapter(HomeActivity.this,  activeTriyajList);
        HorizontalLayout = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        active_list.setLayoutManager(HorizontalLayout);
        active_list.setAdapter(triyajAdapter);


        triyajAdapter = new TriyajAdapter(HomeActivity.this,  closingTriyajList);
        HorizontalLayout = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        closing_list.setLayoutManager(HorizontalLayout);
        closing_list.setAdapter(triyajAdapter);

       /* adapter = new DurumAdapter2(HomeActivity.this,source3, "2");
        HorizontalLayout = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        closing_list.setLayoutManager(HorizontalLayout);
        closing_list.setAdapter(adapter);*/


        autocomplete();

    }

    public void Activelist() {
        // Adding items to ArrayList


       /* source3  = new ArrayList<>();
        source3.add("TK317568\n6A");
        source3.add("TK317568\n7B");*/

    }


    public void ClosingList() {
        // Adding items to ArrayList
        source2 = new ArrayList<>();
       // source2.add("TK22114\n21F");


    }


    void autocomplete() {


        String[] list = {"TK31756821F", "TK3175686A" ,"TK2211421F", "TK2211420F" , "TK221141A"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, list);

        ucus_autocomplete.setThreshold(1);
        ucus_autocomplete.setAdapter(adapter);
        ucus_autocomplete.setTextColor(Color.BLACK);
        ucus_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                Intent i = new Intent(HomeActivity.this, TriyajActivity.class);
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
                            i = new Intent(HomeActivity.this, HomeActivity.class);
                            i.putExtra("gelenPage", "müşteri");
                            startActivity(i);
                            break;
                        case R.id.nav_ucus_detay:
                            i = new Intent(HomeActivity.this, TriyajActivity.class);
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


    void addTriaj(){

        activeTriyajList = new ArrayList<TriyajBilgileri>();
        allPassengerList = new ArrayList<TriyajBilgileri>();
        closingTriyajList = new ArrayList<TriyajBilgileri>();

        TriyajBilgileri yeni = new TriyajBilgileri();
        yeni.setId(1L);
        yeni.setDurum("0");
        yeni.setUcusNo("TK317568");
        yeni.setKoltukNo("21F");
        yeni.setAd("Büşra");
        yeni.setSoyad("Akbey");
        yeni.setCinsiyet("Kadın");
        yeni.setTriyajDurum("Yeşil");
        yeni.setYas("28");
        activeTriyajList.add(yeni);
        allPassengerList.add(yeni);

        yeni = new TriyajBilgileri();
        yeni.setDurum("0");
        yeni.setUcusNo("TK317568");
        yeni.setKoltukNo("22F");
        yeni.setAd("Münevver");
        yeni.setSoyad("Akbey");
        yeni.setCinsiyet("Kadın");
        yeni.setId(2L);
        yeni.setTriyajDurum("Kırmızı");
        yeni.setYas("24");
        activeTriyajList.add(yeni);
        allPassengerList.add(yeni);


        yeni = new TriyajBilgileri();
        yeni.setDurum("0");
        yeni.setUcusNo("TK317568");
        yeni.setKoltukNo("25F");
        yeni.setAd("Sema");
        yeni.setSoyad("Öz");
        yeni.setCinsiyet("Kadın");
        yeni.setId(5L);
        yeni.setTriyajDurum("Kırmızı");
        yeni.setYas("24");
        activeTriyajList.add(yeni);
        allPassengerList.add(yeni);


        yeni = new TriyajBilgileri();
        yeni.setDurum("0");
        yeni.setUcusNo("TK317575");
        yeni.setKoltukNo("2A");
        yeni.setAd("Aysun");
        yeni.setSoyad("Şen");
        yeni.setCinsiyet("Kadın");
        yeni.setId(3L);
        yeni.setYas("45");
        yeni.setTriyajDurum("Yeşil");
        closingTriyajList.add(yeni);
        allPassengerList.add(yeni);


        yeni = new TriyajBilgileri();
        yeni.setDurum("0");
        yeni.setUcusNo("TK317575");
        yeni.setKoltukNo("2B");
        yeni.setAd("Ali");
        yeni.setSoyad("Şen");
        yeni.setCinsiyet("Kadın");
        yeni.setId(4L);
        yeni.setYas("55");
        yeni.setTriyajDurum("Yeşil");
        closingTriyajList.add(yeni);
        allPassengerList.add(yeni);


    }
}

