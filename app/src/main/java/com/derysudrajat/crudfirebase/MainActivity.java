package com.derysudrajat.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    CrewAdapter adapter;

    List<Crew> crewList = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRef;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("crew");
        fechDataFromFirebase();
        initView();
    }


    private void initView() {
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setHasFixedSize(true);
        rvMain.setItemAnimator(new DefaultItemAnimator());
    }

    private void fechDataFromFirebase() {
        adapter = new CrewAdapter(this);
        adapter.setCrewList(crewList);
        rvMain.setAdapter(adapter);

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Crew crew = dataSnapshot.getValue(Crew.class);
                    crewList.add(crew);
                    Log.d("MainActivity","Data: "+crew.getId());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            myRef.addChildEventListener(mChildEventListener);
        }
    }

    @OnClick(R.id.fab)
    void addCrew() {
        startActivity(new Intent(this, DetailActivity.class));
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        fechDataFromFirebase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailActivity.REQ_CODE_DETAIL){
            if (resultCode == RESULT_OK){
                fechDataFromFirebase();
            }
        }
    }
}
