package com.example.hacknc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hacknc.adapter.ReportListAdapter;
import com.example.hacknc.model.MPothole;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportsActivity extends AppCompatActivity implements  ReportListAdapter.OnItemClickListener  {

    private RecyclerView recyclerView;
    private ReportListAdapter reportListAdapter;
    private DatabaseReference databaseReference;
    private List<MPothole> reportList;
    private ProgressBar mProgressCircle;
    private List<LatLng> latLngsList = new ArrayList<>();
    private FloatingActionButton bt_map ;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reports");

        }

        mProgressCircle = findViewById(R.id.progress_circle);
        recyclerView = findViewById(R.id.rv_reports);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportList = new ArrayList<>();



        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("shreyash","onDataChanged"+dataSnapshot);
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    MPothole potholeReport = reportSnapshot.getValue(MPothole.class);
                    LatLng temp = new LatLng(potholeReport.getLattitude(),potholeReport.getLongitude());
                    latLngsList.add(temp);
                    reportList.add(potholeReport);
                }
                Log.d("shreyash","latlngList:"+latLngsList);
                Collections.reverse(reportList);
                reportListAdapter = new ReportListAdapter(ReportsActivity.this, reportList);

                recyclerView.setAdapter(reportListAdapter);
                reportListAdapter.setOnItemClickListener(ReportsActivity.this);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReportsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });

        /*bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putParcelableArrayList("markerList", (ArrayList<? extends Parcelable>) latLngsList);
                Intent intent = new Intent(ReportsActivity.this,MapsActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("from",0);
                startActivity(intent);

            }
        });*/
    }

    @Override
    public void onItemClick(int position,double lat,double longi,String addressLine1) {
       /* Toast.makeText(this, "pos"+position+"lat "+lat, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("long",longi);
        intent.putExtra("address",addressLine1);
        intent.putExtra("from",1);
        startActivity(intent);*/

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }
}