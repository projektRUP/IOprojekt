package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SpinnerItem> spinnerList;
    private ArrayList<RecyclerItem> recyclerList;

    private SpinnerAdapter sAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Toast.makeText(MainActivity.this, clickedImg + ": selected img", Toast.LENGTH_SHORT).show();
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
                recyclerList.get(position).clicked("Zaznaczone");
                mRecyclerAdapter.notifyItemChanged(position);
            }

            @Override
            public void onCallClick(int posititon) {
                Toast.makeText(MainActivity.this, posititon + ": selected call", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVideoCallClick(int position) {
                Toast.makeText(MainActivity.this, position + ": selected videocall", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initList(){
        spinnerList = new ArrayList<SpinnerItem>();
        spinnerList.add(new SpinnerItem(R.drawable.ic_baseline_star_rate_24));
        spinnerList.add(new SpinnerItem(R.drawable.ic_baseline_sort_by_alpha_24));
    }
    private void initList2(){
        recyclerList = new ArrayList<>();
        recyclerList.add(new RecyclerItem(R.drawable.profile, R.drawable.rating_5, "Angry Mike"));
        recyclerList.add(new RecyclerItem(R.drawable.profile, R.drawable.rating_5, "Angry Mike"));
        recyclerList.add(new RecyclerItem(R.drawable.profile, R.drawable.rating_5, "Angry Mike"));
        recyclerList.add(new RecyclerItem(R.drawable.profile, R.drawable.rating_5, "Angry Mike"));
        recyclerList.add(new RecyclerItem(R.drawable.profile, R.drawable.rating_5, "Angry Mike"));
        recyclerList.add(new RecyclerItem(R.drawable.profile, R.drawable.rating_5, "Angry Mike"));



    }
}