package com.ternovski.linklinetest.database;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by Vadim on 2017.
 */

public class DatabaseQueries {

    private static String USER_ID = "userId";
    private static String PRIMARY_KEY = "primaryKey";

    private Realm mRealm;
    private User mUser;
    private RealmList<Room> mRooms;
    private RealmList<Device> mDevices;

    public DatabaseQueries() {
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);
        mUser = mRealm.where(User.class).equalTo(USER_ID, 1).findFirst();
        mRooms = mUser.getRooms();
        mDevices = mUser.getDevices();
    }

    public void removeDeviceFromRealm(long id) {
        mRealm.beginTransaction();
        Device device = mRealm.where(Device.class).equalTo(PRIMARY_KEY, id).findFirst();
        device.deleteFromRealm();
        mRealm.commitTransaction();
    }

    public void removeDeviceFromRealm(Device device) {
        mRealm.beginTransaction();
        device.deleteFromRealm();
        mRealm.commitTransaction();
    }

    public void moveDevice(RealmList<Device> from, RealmList<Device> to, Device device){
        mRealm.beginTransaction();
        from.remove(device);
        to.add(device);
        mRealm.commitTransaction();
    }

    public void changeDeviceState(long id, boolean deviceState) {
        mRealm.beginTransaction();
        Device device = mRealm.where(Device.class).equalTo(PRIMARY_KEY, id).findFirst();
        device.setDeviceState(deviceState);
        mRealm.commitTransaction();
    }

    public void addRoom(String roomName) {
        mRealm.beginTransaction();
        long maxId;

        if (isRoomExist()) {
            maxId = (long) mRealm.where(Room.class).max(PRIMARY_KEY);
            maxId++;
        } else {
            maxId = 1;
        }
        Room room = mRealm.createObject(Room.class, maxId);
        room.setRoomName(roomName);
        mRooms.add(room);
        mRealm.commitTransaction();
    }

    public void addDevice(String devise) {

        switch (devise) {
            case "socket":
                addDeviceToList(createDevice("333", "on", "icon_socket"));
                break;
            case "heat":
                addDeviceToList(createDevice("2222", "on", "icon_radiator"));
                break;
            case "lamp":
                addDeviceToList(createDevice("1111", "on", "icon_light_bulb"));
                break;
        }
    }

    private Device createDevice(String deviceId, String deviceState, String iconId) {
        mRealm.beginTransaction();
        long maxId;
        if (isDeviceExist()) {
            maxId = (long) mRealm.where(Device.class).max(PRIMARY_KEY);
            maxId++;
        } else {
            maxId = 1;
        }
        Device device = mRealm.createObject(Device.class, maxId);
        device.setDeviceId(deviceId);
        device.setDeviceState(false);
        device.setDeviceIcon(iconId);
        mRealm.commitTransaction();
        return device;
    }

    private void addDeviceToList(Device device) {
        mRealm.beginTransaction();
        mDevices.add(device);
        mRealm.commitTransaction();
    }


    private boolean isRoomExist() {
        Room room = mRealm.where(Room.class).equalTo(PRIMARY_KEY, 1).findFirst();
        if (room == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isDeviceExist() {
        Device device = mRealm.where(Device.class).equalTo(PRIMARY_KEY, 1).findFirst();
        if (device == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUserExist(Realm realm) {
        User user = realm.where(User.class).equalTo(USER_ID, 1).findFirst();
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    public User getUser(){
        return mUser;
    }

}
