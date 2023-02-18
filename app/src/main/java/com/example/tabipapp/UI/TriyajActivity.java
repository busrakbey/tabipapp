package com.example.tabipapp.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tabipapp.Model.TriyajBilgileri;
import com.example.tabipapp.R;
import com.example.tabipapp.UI.MinttiVision.BluetoothActivity;
import com.example.tabipapp.UI.MinttiVision.MeasureActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TriyajActivity extends AppCompatActivity {
    AutoCompleteTextView ucus_autocomplete;
    Button hasta_durum_button,yeni_olcum,ucus_bilgileri,koluk_no_button;
    TextView kisi_bilgisi_textview,hasta_durumu_textview;
    ImageView return_button;
    BottomNavigationView bottomNavigationView;
    Long gelenId = null;
    TriyajBilgileri gelenTriyaj;
    List<TriyajBilgileri> allPassengerList = HomeActivity.allPassengerList;
    String[] listItems;
    ArrayList<Integer> items = new ArrayList<>();
    boolean[] checkedItems;
    Button durum_combobox, cagri_baslat, sesli_not_button;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triyaj_activity);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        hasta_durum_button = (Button) findViewById(R.id.hasta_durum_button);
        ucus_autocomplete = (AutoCompleteTextView) findViewById(R.id.ucus_numarasi);
        kisi_bilgisi_textview = (TextView) findViewById(R.id.kisi_bilgisi_textview);
        hasta_durumu_textview = (TextView) findViewById(R.id.hasta_durumu_textview);
        yeni_olcum = (Button) findViewById(R.id.yeni_olcum);
        return_button = (ImageView) findViewById(R.id.vector_1);
        ucus_bilgileri = (Button) findViewById(R.id.ucak);
        koluk_no_button = (Button) findViewById(R.id.koluk_no_button);
        listItems = getResources().getStringArray(R.array.hasta_durum_item);
        durum_combobox = (Button) findViewById(R.id.durum_combobox);
        checkedItems = new boolean[listItems.length];
        cagri_baslat = (Button) findViewById(R.id.cagri_baslat);
        sesli_not_button = (Button) findViewById(R.id.sesli_not_button);


        setAutocompleteValues();
        String[] s = { "Yeşil"  , "Sarı" , "Kırmızı" , "Siyah"};
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(TriyajActivity.this,
                android.R.layout.simple_spinner_item, s);

        yeni_olcum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TriyajActivity.this, BluetoothActivity.class);
                startActivity(i);
            }
        });
        durum_combobox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(TriyajActivity.this, R.style.MyDialogTheme);
                alt_bld.setTitle("Durum Seçiniz");
                alt_bld.setSingleChoiceItems(s, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        durum_combobox.setText("Durum: " + s[item]);
                        dialog.dismiss();

                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });

        cagri_baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TriyajActivity.this, VideoCallActivity.class);
                i.putExtra("roomName" , gelenTriyaj.getUcusNo());
                startActivity(i);
            }
        });

        sesli_not_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TriyajActivity.this, AudioRecordTest.class);
                startActivity(i);
            }
        });




        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gelenId = getIntent().getLongExtra("id" , 0L);
        for(TriyajBilgileri item : allPassengerList){
            if(item.getId() == gelenId) {
                gelenTriyaj = item;
                break;
            }
        }

        if(gelenTriyaj != null) {
            ucus_autocomplete.setEnabled(false);
            setUcusBilgileri();
        }
        hasta_durum_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TriyajActivity.this, R.style.MyDialogTheme);
                mBuilder.setTitle("Durum Listesi");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            items.add(position);
                        }else{
                            items.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < items.size(); i++) {
                            item = item + listItems[items.get(i)];
                            if (i != items.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        hasta_durumu_textview.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            items.clear();
                            hasta_durumu_textview.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }

    void setUcusBilgileri(){
        ucus_autocomplete.setText(gelenTriyaj.getUcusNo()+gelenTriyaj.getKoltukNo());
        ucus_bilgileri.setText("Uçuş Bilgilerim\n"+ gelenTriyaj.getUcusNo()+ "\nAnkara (ESB) - İstanbul (SAW)\n");
        koluk_no_button.setText(gelenTriyaj.getKoltukNo());
        kisi_bilgisi_textview.setText(gelenTriyaj.getAd() + " " + gelenTriyaj.getSoyad()+ ", " + gelenTriyaj.getYas()+ ", " + gelenTriyaj.getCinsiyet());
        hasta_durumu_textview.setText(gelenTriyaj.getHastaDurumu());


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
                            i = new Intent(TriyajActivity.this, HomeActivity.class);
                            i.putExtra("gelenPage", "müşteri");
                            startActivity(i);
                            break;
                        case R.id.nav_ucus_detay:
                            i = new Intent(TriyajActivity.this, TriyajActivity.class);
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



    void setAutocompleteValues() {
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
                for(TriyajBilgileri i : allPassengerList){
                    if(item.equals(i.getUcusNo()+i.getKoltukNo())) {
                        gelenTriyaj = i;
                        break;
                    }
                }
                if(gelenTriyaj != null)
                    setUcusBilgileri();

            }
        });

    }

}
