package com.ternovski.linklinetest.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vadim on 2017.
 */

public class User extends RealmObject {

    @PrimaryKey
    private int userId;
    private RealmList<Room> rooms = null;
    private RealmList<Device> devices = null;
    private boolean isUserLogin;

    public RealmList<Device> getDevices() {
        return devices;
    }

    public void setDevices(RealmList<Device> devices) {
        this.devices = devices;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public RealmList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(RealmList<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isUserLogin() {
        return isUserLogin;
    }

    public void setUserLoginState(boolean userLogin) {
        isUserLogin = userLogin;
    }
}
