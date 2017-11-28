package com.ternovski.linklinetest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ternovski.linklinetest.R;
import com.ternovski.linklinetest.database.DatabaseQueries;
import com.ternovski.linklinetest.websocket.ConnectionListener;
import com.ternovski.linklinetest.websocket.WebSocketClient;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements ConnectionListener, View.OnClickListener {

    private EditText emailField;
    private EditText passwordField;
    private Button signInButton;
    private LinearLayout content;
    private RelativeLayout progressBarContainer;
    private DatabaseQueries mDbQueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content);
        progressBarContainer = findViewById(R.id.progress_bar_container);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        signInButton = findViewById(R.id.sign_in);
        signInButton.setOnClickListener(this);

        mDbQueries = new DatabaseQueries();
        changeVisibility();
        if (mDbQueries.getUser().isUserLogin()) {
            startNewActivity();
        }else {
            changeVisibility();
        }
    }

    @Override
    public void onConnect() {
        changeUserLoginState();
        startNewActivity();
    }

    @Override
    public void onError() {
        changeUserLoginState();
        startNewActivity();
    }

    private void changeUserLoginState(){
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        mDbQueries.getUser().setUserLoginState(true);
        realm.commitTransaction();
    }

    private void startNewActivity() {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        String login = String.valueOf(emailField.getText());
        String password = String.valueOf(passwordField.getText());
        WebSocketClient wsClient = new WebSocketClient(this, login, password);
        wsClient.connect();
        changeVisibility();
    }

    private void changeVisibility() {
        if (progressBarContainer.getVisibility() == View.GONE) {
            progressBarContainer.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        } else {
            progressBarContainer.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }
    }
}
