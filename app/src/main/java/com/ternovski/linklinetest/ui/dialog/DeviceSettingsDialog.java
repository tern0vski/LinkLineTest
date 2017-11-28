package com.ternovski.linklinetest.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ternovski.linklinetest.R;
import com.ternovski.linklinetest.database.DatabaseQueries;
import com.ternovski.linklinetest.database.Device;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Vadim on 2017.
 */

public class DeviceSettingsDialog extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseQueries databaseQueries;
    private Realm mRealm;
    private Device mDevice;
    private Switch onOffSwitch;
    private long mId;

    public DeviceSettingsDialog() {
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(config);
        databaseQueries = new DatabaseQueries();
    }

    public static DeviceSettingsDialog newInstance(long id) {

        Bundle args = new Bundle();

        DeviceSettingsDialog fragment = new DeviceSettingsDialog();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getLong("id");
        mDevice = mRealm.where(Device.class).equalTo("primaryKey", mId).findFirst();
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_device_settings, null);
        adb.setView(view);

        onOffSwitch = view.findViewById(R.id.onOffSwitch);
        onOffSwitch.setOnCheckedChangeListener(this);
        Button deleteButton = view.findViewById(R.id.delete_device);
        deleteButton.setOnClickListener(this);

        TextView deviceId = view.findViewById(R.id.device_id);
        ImageView deviceIcon = view.findViewById(R.id.device_icon);

        deviceId.setText(mDevice.getDeviceId());
        String deviceIconId = mDevice.getDeviceIcon();
        Context context = getActivity();
        int resourcesId = context.getResources().getIdentifier(deviceIconId, "drawable", context.getPackageName());
        deviceIcon.setImageDrawable(context.getResources().getDrawable(resourcesId));

        onOffSwitch.setChecked(mDevice.getDeviceState());

        return adb.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_device:
                databaseQueries.removeDeviceFromRealm(mId);
                dismiss();
                break;
            default:
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(onOffSwitch.isChecked()){
            databaseQueries.changeDeviceState(mId, true);
        }else {
            databaseQueries.changeDeviceState(mId, false);
        }
    }
}
