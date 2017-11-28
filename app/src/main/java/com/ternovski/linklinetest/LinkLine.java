package com.ternovski.linklinetest;

import android.app.Application;

import com.ternovski.linklinetest.database.DatabaseQueries;
import com.ternovski.linklinetest.database.Device;
import com.ternovski.linklinetest.database.Room;
import com.ternovski.linklinetest.database.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by Vadim on 2017.
 */

public class LinkLine extends Application {
    Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);

        if (!DatabaseQueries.isUserExist(mRealm))
            createUser();
    }

    private void createUser() {
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);

        mRealm.beginTransaction();
        RealmList<Device> devices = new RealmList<>();
        RealmList<Room> rooms = new RealmList<>();
        User user = mRealm.createObject(User.class, 1);
        user.setRooms(rooms);
        user.setDevices(devices);
        user.setUserLoginState(false);
        mRealm.commitTransaction();
    }

}
