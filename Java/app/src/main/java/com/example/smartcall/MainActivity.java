package com.example.smartcall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import exceptions.AppDoesntHaveNecessaryPermissionsException;
import exceptions.NoWhatsappInstalledException;
import phone.Permissions;
import whatsapp.WhatsappAccesser;
import whatsapp.WhatsappContact;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SpinnerItem> spinnerList;
    private ArrayList<RecyclerItem> recyclerList;
    private ArrayList<RecyclerItem> searchRecyclerList;
    private ArrayList<WhatsappContact> contactList;
    private ArrayList<WhatsappContact> currentlyDisplayedContactList;

    private SpinnerAdapter sAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permissions.askForPermissions(this);
        }

        initList();
        initList2();

        //spinner (dropdown list)
        Spinner spinner = findViewById(R.id.spinnerView);
        sAdapter = new SpinnerAdapter(this, spinnerList);
        spinner.setAdapter(sAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem clickedItem = (SpinnerItem)parent.getItemAtPosition(position);
                int clickedImg = clickedItem.getSpinnerImg();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Recycler
        mRecyclerView = findViewById(R.id.recyclerView);
        //Jeśli nie będzie sie zmieniać warto ustawić true
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerAdapter = new RecyclerAdapter(recyclerList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mRecyclerAdapter.notifyItemChanged(position);
            }

            @Override
            public void onCallClick(int position) {
                try {
                    WhatsappAccesser.makeACall(MainActivity.this, currentlyDisplayedContactList.get(position).getVoipCallId());
                } catch (NoWhatsappInstalledException | AppDoesntHaveNecessaryPermissionsException e) {
                    e.printStackTrace();
                    // TODO notify user about why the function might not work
                }
            }

            @Override
            public void onVideoCallClick(int position) {
                try {
                    WhatsappAccesser.makeACall(MainActivity.this, currentlyDisplayedContactList.get(position).getVideoCallId());
                } catch (NoWhatsappInstalledException | AppDoesntHaveNecessaryPermissionsException e) {
                    e.printStackTrace();
                    // TODO notify user about why the function might not work
                }
            }
        });

        EditText searchText = findViewById(R.id.search_bar);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text){
        ArrayList<RecyclerItem> filteredList = new ArrayList<>();
        currentlyDisplayedContactList = new ArrayList<>();

        for (RecyclerItem item : recyclerList) {
            if (item.getNameSurname().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
                currentlyDisplayedContactList.add(contactList.get(item.getWhatsappContactIndex()));
            }
        }
        mRecyclerAdapter.filterList(filteredList);
    }

    private void initList(){
        spinnerList = new ArrayList<SpinnerItem>();
        spinnerList.add(new SpinnerItem(R.drawable.ic_baseline_star_rate_24));
        spinnerList.add(new SpinnerItem(R.drawable.ic_baseline_sort_by_alpha_24));
    }
    private void initList2(){
        recyclerList = new ArrayList<>();

        try {
            contactList = WhatsappAccesser.getWhatsappContacts(this);
            currentlyDisplayedContactList = contactList;
        } catch (NoWhatsappInstalledException e) {
            e.printStackTrace();
            // TODO notify user that whatsapp might not be installed
        } catch (AppDoesntHaveNecessaryPermissionsException e) {
            e.printStackTrace();
            // TODO notify user that the app might not have all necessary permissions
        }

        if(contactList != null) {
            int index = 0;

            for(WhatsappContact contact : contactList) {
                switch(contact.getStars()) {
                    case 1: recyclerList.add(new RecyclerItem(R.drawable.ic_baseline_person_24, R.drawable.rating_1, contact.getDisplayName(), index ++));
                        break;
                    case 2: recyclerList.add(new RecyclerItem(R.drawable.ic_baseline_person_24, R.drawable.rating_2, contact.getDisplayName(), index ++));
                        break;
                    case 3: recyclerList.add(new RecyclerItem(R.drawable.ic_baseline_person_24, R.drawable.rating_3, contact.getDisplayName(), index ++));
                        break;
                    case 4: recyclerList.add(new RecyclerItem(R.drawable.ic_baseline_person_24, R.drawable.rating_4, contact.getDisplayName(), index ++));
                        break;
                    case 5: recyclerList.add(new RecyclerItem(R.drawable.ic_baseline_person_24, R.drawable.rating_5, contact.getDisplayName(), index ++));
                        break;
                    case -1: recyclerList.add(new RecyclerItem(R.drawable.ic_baseline_person_24, R.drawable.rating_none, contact.getDisplayName(), index ++));
                        break;
                }
                System.out.println(contact);
            }
        }
    }
}