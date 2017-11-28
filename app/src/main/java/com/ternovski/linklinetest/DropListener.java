package com.ternovski.linklinetest;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.ternovski.linklinetest.database.DatabaseQueries;
import com.ternovski.linklinetest.database.Device;
import com.ternovski.linklinetest.database.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by Vadim on 2017.
 */

public class DropListener implements View.OnDragListener {

    private Realm mRealm;
    private User mUser;
    private DatabaseQueries mDatabaseQueries;

    public DropListener() {
        mDatabaseQueries = new DatabaseQueries();
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);
        getRealm();
    }

    private void getRealm() {
        mUser = mRealm.where(User.class).equalTo("userId", 1).findFirst();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        View draggedView;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                draggedView = (View) event.getLocalState();
                draggedView.setVisibility(View.INVISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                RealmList<Device> from;
                draggedView = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) draggedView.getParent();

                if (v instanceof GridLayout) {
                    RealmList<Device> to = (RealmList<Device>) v.getTag();
                    long id = (long) draggedView.getTag();
                    Device device = mRealm.where(Device.class).equalTo("primaryKey", id).findFirst();

                    if ((owner instanceof GridLayout) && (v instanceof GridLayout)) {
                        from = (RealmList<Device>) owner.getTag();
                        mDatabaseQueries.moveDevice(from, to, device);
                    } else {
                        from = mUser.getDevices();
                        mDatabaseQueries.moveDevice(from, to, device);
                    }

                } else {
                    draggedView.setVisibility(View.VISIBLE);
                }


                break;
            case DragEvent.ACTION_DRAG_ENDED:
                draggedView = (View) event.getLocalState();
                draggedView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return true;
    }

}