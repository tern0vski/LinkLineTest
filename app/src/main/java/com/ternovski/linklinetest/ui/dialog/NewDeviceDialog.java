package com.ternovski.linklinetest.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.ternovski.linklinetest.R;
import com.ternovski.linklinetest.database.DatabaseQueries;

/**
 * Created by Vadim on 2017.
 */

public class NewDeviceDialog extends DialogFragment implements View.OnClickListener {
    DatabaseQueries databaseQueries;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        databaseQueries = new DatabaseQueries();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity()).setTitle("НОВОЕ УСТРОЙСТВО");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_new_device, null);
        view.findViewById(R.id.lamp).setOnClickListener(this);
        view.findViewById(R.id.heat).setOnClickListener(this);
        view.findViewById(R.id.socket).setOnClickListener(this);

        adb.setView(view);
        return adb.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.socket:
                databaseQueries.addDevice("socket");
                dismiss();
                break;
            case R.id.heat:
                databaseQueries.addDevice("heat");
                dismiss();
                break;
            case R.id.lamp:
                databaseQueries.addDevice("lamp");
                dismiss();
                break;
            default:
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
