package com.ternovski.linklinetest.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ternovski.linklinetest.DragTouchListener;
import com.ternovski.linklinetest.DropListener;
import com.ternovski.linklinetest.R;
import com.ternovski.linklinetest.database.Device;
import com.ternovski.linklinetest.database.Room;
import com.ternovski.linklinetest.database.User;
import com.ternovski.linklinetest.ui.dialog.DeviceSettingsDialog;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by Vadim on 2017.
 */

public class RoomsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, RealmChangeListener<Realm> {

    private static final int TYPE_ROOM = 1;
    private static final int TYPE_DEVICE = 0;

    private int mRecyclerSize;
    private Context mContext;
    private Resources mResources;
    private boolean isNotified = false;
    private int mRoomPosition = 0;

    private Realm mRealm;
    private User mUser;
    private RealmList<Room> mRooms;
    private RealmList<Device> mDevices;


    public RoomsRecyclerAdapter(Context mContext, Resources resources) {
        this.mResources = resources;
        this.mContext = mContext;
        getRealmData();
    }

    private void getRealmData() {
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);
        mRealm.addChangeListener(this);

        mUser = mRealm.where(User.class).equalTo("userId", 1).findFirst();
        mRooms = mUser.getRooms();
        mDevices = mUser.getDevices();
        mRecyclerSize = mRooms.size() + mDevices.size();
    }

    @Override
    public void onClick(View v) {
        long deviceId = (long) v.getTag();
        Device device = mRealm.where(Device.class).equalTo("primaryKey", deviceId).findFirst();
        AppCompatActivity activity = (AppCompatActivity) mContext;
        DeviceSettingsDialog newDeviceDialog = new DeviceSettingsDialog().newInstance(device.getPrimaryKey());
        newDeviceDialog.show(activity.getSupportFragmentManager(), "");
    }


    public class ViewHolderForRoom extends RecyclerView.ViewHolder {
        public TextView mRoomName;
        public GridLayout mDeviceInRoomDeviceContainer;


        public ViewHolderForRoom(View roomView) {
            super(roomView);
            mDeviceInRoomDeviceContainer = roomView.findViewById(R.id.grid);
            mRoomName = roomView.findViewById(R.id.room_name);
        }
    }

    class ViewHolderForDevice extends RecyclerView.ViewHolder {
        public LinearLayout mDeviceContainer;

        public ViewHolderForDevice(View deviceView) {
            super(deviceView);
            mDeviceContainer = deviceView.findViewById(R.id.container);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_device_container, parent, false);
            v.setOnDragListener(new DropListener());
            return new ViewHolderForDevice(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_room_item, parent, false);
            v.setOnDragListener(new DropListener());
            return new ViewHolderForRoom(v);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (holder instanceof ViewHolderForRoom) {
            if (isNotified) {
                ((ViewHolderForRoom) holder).mDeviceInRoomDeviceContainer.removeAllViews();
            }

            ((ViewHolderForRoom) holder).mDeviceInRoomDeviceContainer.setTag(mRooms.get(mRoomPosition).getRoomDevices());
            ((ViewHolderForRoom) holder).mDeviceInRoomDeviceContainer.setOnDragListener(new DropListener());
            ((ViewHolderForRoom) holder).mRoomName.setText(mRooms.get(mRoomPosition).getRoomName());

            for (int i = 0; i < mRooms.get(mRoomPosition).getRoomDevices().size(); i++) {
                Device device = mRooms.get(mRoomPosition).getRoomDevices().get(i);
                View deviceView = createDeviceView(device, inflater.inflate(R.layout.layout_device, null), device.getPrimaryKey());
                ((ViewHolderForRoom) holder).mDeviceInRoomDeviceContainer.addView(deviceView);
            }
            mRoomPosition++;
        } else if (holder instanceof ViewHolderForDevice) {
            if (isNotified) {
                ((ViewHolderForDevice) holder).mDeviceContainer.removeAllViews();
            }
            Device device = mDevices.get(position);
            View deviceView = createDeviceView(device, inflater.inflate(R.layout.layout_device, null), device.getPrimaryKey());
            ((ViewHolderForDevice) holder).mDeviceContainer.addView(deviceView);
        }
    }

    private View createDeviceView(Device device, View deviceView, long id) {
        TextView deviceId = deviceView.findViewById(R.id.device_id);
        deviceId.setText(device.getDeviceId());

        ImageView deviceIcon = deviceView.findViewById(R.id.device_icon);
        String deviceIconId = device.getDeviceIcon();
        int resourcesId = mResources.getIdentifier(deviceIconId, "drawable", mContext.getPackageName());
        deviceIcon.setImageDrawable(mResources.getDrawable(resourcesId));

        deviceView.setTag(id);
        deviceView.setOnClickListener(this);
        deviceView.setOnLongClickListener(new DragTouchListener());
        deviceView.setOnDragListener(new DropListener());

        return deviceView;
    }

    @Override
    public int getItemCount() {
        return mRecyclerSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= mDevices.size() - 1)
            return TYPE_DEVICE;
        else
            return TYPE_ROOM;

    }

    @Override
    public void onChange(Realm realm) {
        mRecyclerSize = mDevices.size() + mRooms.size();
        isNotified = true;
        mRoomPosition = 0;
        notifyDataSetChanged();
    }

}