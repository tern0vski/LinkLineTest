package com.ternovski.linklinetest.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vadim on 2017.
 */

public class Room extends RealmObject {

    @PrimaryKey
    private long primaryKey;
    private RealmList<Device> devices = null;
    private String roomName;


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RealmList<Device> getRoomDevices() {
        return devices;
    }

    public void setRoomDevices(RealmList<Device> devices) {
        this.devices = devices;
    }

}
