package com.nantonelli.smu_now.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.nantonelli.smu_now.Model.IProjectDialFrag;
import com.nantonelli.smu_now.R;

/**
 * Created by ndantonelli on 12/10/15.
 */
public class UsernameDialog extends DialogFragment {

    private IProjectDialFrag iProjDialFrag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(getActivity());

        createProjectAlert.setTitle("Change Username");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        createProjectAlert.setView(inflater.inflate(R.layout.dialog_layout, null))

                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        iProjDialFrag.onDialogPositiveClick(UsernameDialog.this, ((EditText)getDialog().findViewById(R.id.project_name)).getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        iProjDialFrag.onDialogNegativeClick(UsernameDialog.this);
                    }
                });

        return createProjectAlert.create();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iProjDialFrag = (IProjectDialFrag) activity;
    }

}
