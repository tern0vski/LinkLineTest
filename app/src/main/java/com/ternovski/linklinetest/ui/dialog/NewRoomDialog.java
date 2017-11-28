package com.ternovski.linklinetest.ui.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.ternovski.linklinetest.R;
import com.ternovski.linklinetest.database.DatabaseQueries;

/**
 * Created by Vadim on 2017.
 */

public class NewRoomDialog extends DialogFragment {

    private EditText editText;
    DatabaseQueries databaseQueries;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("НОВАЯ КОМНАТА").setPositiveButton("создать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseQueries = new DatabaseQueries();
                        databaseQueries.addRoom(String.valueOf(editText.getText()));
                    }
                });

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_new_room, null);
        editText = view.findViewById(R.id.new_room_edit);
        adb.setView(view);
        return adb.create();
    }


}
