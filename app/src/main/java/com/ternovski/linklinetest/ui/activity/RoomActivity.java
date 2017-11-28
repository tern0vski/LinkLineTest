package com.ternovski.linklinetest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ternovski.linklinetest.R;
import com.ternovski.linklinetest.adapters.RoomsRecyclerAdapter;
import com.ternovski.linklinetest.ui.dialog.NewDeviceDialog;
import com.ternovski.linklinetest.ui.dialog.NewRoomDialog;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        RoomsRecyclerAdapter roomsRecyclerAdapter = new RoomsRecyclerAdapter(this, getResources());
        recyclerView.setAdapter(roomsRecyclerAdapter);


        Button addNewRoom = findViewById(R.id.add_new_room);
        addNewRoom.setOnClickListener(this);

        Button addNewDevice = findViewById(R.id.add_new_divice);
        addNewDevice.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_room:
                NewRoomDialog newRoomDialog = new NewRoomDialog();
                newRoomDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.add_new_divice:
                NewDeviceDialog newDeviceDialog = new NewDeviceDialog();
                newDeviceDialog.show(getSupportFragmentManager(), "");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
